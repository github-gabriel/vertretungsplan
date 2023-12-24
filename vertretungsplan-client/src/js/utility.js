import axios from 'axios'

const API_VERSION = 'v1'
const SERVER_PORT = '8080'
export const RESOURCES_URL = `http://localhost:${SERVER_PORT}/api/${API_VERSION}/resources`
export const AUTHENTICATION_URL = `http://localhost:8080/api/${API_VERSION}/auth`
export const USER_ENDPOINT = '/user'
export const TEACHER_ENDPOINT = '/teacher'
export const ADMINISTRATOR_ENDPOINT = '/administration'

export const ADMINISTRATOR_ROLE = 'ADMINISTRATOR'
export const TEACHER_ROLE = 'TEACHER'

// Maps column names in header to according field of DTO
export const TABLE_DICT = {
  'Lehrer': 'teacher',
  'Stunde': 'hour',
  'Name': 'name',
  'Fach': 'subject',
  'Art': 'art',
  'Vertreter': 'substitute',
  'Rolle': 'role',
  'Klasse': 'course',
  'Faecher': 'subjects',
  'Anwesenheit': 'attendance',
  'Schueler': 'students',
  'Tag': 'day',
  'Delete': 'delete'
}

export function initTables (tableNamesWithTitles) {
  const container = document.getElementsByClassName('grid-container')[0]
  tableNamesWithTitles.forEach(({ title, id }) => {
    // Create the table container
    const tableContainerWithHeading = document.createElement('div')
    tableContainerWithHeading.className = 'table-container-heading'

    // Create the title
    const tableTitle = document.createElement('h1')
    tableTitle.innerHTML = `<mark>${title}</mark>`
    tableContainerWithHeading.appendChild(tableTitle)

    const tableContainer = document.createElement('div')
    tableContainer.className = 'table-container'

    // Create the table
    const table = document.createElement('table')
    table.id = id

    // Create the table head
    const thead = document.createElement('thead')
    const trHead = document.createElement('tr')
    trHead.id = `${id}-head`
    thead.appendChild(trHead)
    table.appendChild(thead)

    // Create the table body
    const tbody = document.createElement('tbody')
    tbody.id = `${id}-body`
    table.appendChild(tbody)

    tableContainer.appendChild(table)
    tableContainerWithHeading.appendChild(tableContainer)

    container.appendChild(tableContainerWithHeading)
  })
}

export function createTable (response, thead, tbody, table_headers) {
  const data = response.data

  table_headers.forEach((item) => {
    thead.innerHTML += `<th>${item}</th>`
  })

  data.forEach((entry) => {
    const row = document.createElement('tr')
    table_headers.forEach((header) => {
      const cell = document.createElement('td')
      cell.textContent = entry[TABLE_DICT[header]]
      row.appendChild(cell)
    })
    tbody.appendChild(row)
  })
}

/*
Additional(!) to privilege checks from the server.
Redirects user if he doesn't have the required role,
needed to access the page
 */
export function isAuthorized (role) {
  const config = {
    method: 'get',
    maxBodyLength: Infinity,
    url: RESOURCES_URL + USER_ENDPOINT + '/role',
    withCredentials: true
  }

  axios.request(config)
    .then((response) => {
      if (!role.includes(response.data['role'])) {
        window.location.href = '../pages/login.html'
      }
    })
    .catch((error) => {
      handleError(error)
    })
}

export function logoutUser () {
  const config = {
    method: 'post',
    maxBodyLength: Infinity,
    url: AUTHENTICATION_URL + '/logout',
    withCredentials: true
  }

  axios.request(config)
    .then(() => {
      window.location.href = '../pages/login.html'
    })
    .catch((error) => {
      handleError(error)
    })
}

export function makeRequest (success, errorFunc, method, url, data = null, headers = null, withCredentials = true) {
  const config = {
    method,
    maxBodyLength: Infinity,
    url,
    withCredentials,
    data,
    headers
  }

  axios.request(config)
    .then((response) => {
      success(response)
    })
    .catch((error) => {
      errorFunc(error)
    })
}

export function handleError (error) {
  if (error.response.data['message']) {
    showPopup(error.response.data['message'], 'red')
  } else {
    if (error.response.status === 403 || error.response.status === 401) {
      showPopup('Du bist nicht autorisiert auf diese Resource zuzugreifen! Du wirst in Kürze zum Login weitergeleitet.', 'red')
      setTimeout(() => {
        window.location.href = '../pages/login.html'
      }, 3000)
    } else {
      showPopup('An unexpected error occurred!', 'red')
    }
  }
}

export function showPopup (message, backgroundColor) {
  function createPopup () {
    const popupClass = document.createElement('div')
    popupClass.id = 'popup'
    popupClass.style.backgroundColor = backgroundColor
    popupClass.style.border = '2px solid ' + backgroundColor

    const closeBtn = document.createElement('span')
    closeBtn.id = 'close-btn'
    closeBtn.innerHTML = '&times;'
    closeBtn.addEventListener('click', function () {
      document.body.removeChild(popupClass)
    })

    const popupContent = document.createElement('p')
    popupContent.textContent = message

    popupClass.appendChild(closeBtn)
    popupClass.appendChild(popupContent)

    document.body.appendChild(popupClass)
  }

  if (document.getElementById('popup') !== null) {
    document.getElementById('popup').remove()
    createPopup()
  } else {
    createPopup()
  }
}

export function makeTableCollapsibleByTitle () {
  function collapse () {
    let content = this.parentElement.nextElementSibling.getElementsByTagName('table')[0]
    if (content.style.display === 'table' || content.style.display === '') {
      content.style.display = 'none'
      this.textContent += '...'
      this.parentElement.nextElementSibling.style.margin = '0'
    } else {
      content.style.display = 'table'
      this.innerText = this.innerText.replace('...', '')
      this.parentElement.nextElementSibling.style.margin = '25px 0'
    }
  }

  document.querySelectorAll('mark').forEach(heading => {
    // Check if element should be collapsible
    heading.textContent !== 'Vertretungsplan Eintrag erstellen' ? heading.addEventListener('click', collapse) : null
  })
}

