import {
  ADMINISTRATOR_ROLE,
  createTable,
  handleError,
  logoutUser,
  makeRequest,
  RESOURCES_URL,
  TEACHER_ROLE,
  USER_ENDPOINT
} from './utility.js'

function initTableByProfile () {
  makeRequest((response) => {
      const table = document.getElementsByClassName('table-container-wrapper')[0]
      const redirect = document.createElement('h1')

      if (response.data['role'] === ADMINISTRATOR_ROLE) {
        redirect.innerHTML = 'Du bist Verwalter? Einfach<a target=”_blank” href=\'../pages/administrator.html\'> hier</a> klicken!'
      } else if (response.data['role'] === TEACHER_ROLE) {
        redirect.innerHTML = 'Du bist Lehrer? Einfach<a target=”_blank” href=\'../pages/teacher.html\'> hier</a> klicken!'
      }
      table.appendChild(redirect)
    },
    handleError,
    'get',
    RESOURCES_URL + USER_ENDPOINT + '/role'
  )
}

function init () {
  document.getElementById('logout-img').addEventListener('click', logoutUser)

  makeRequest(function (response) {
      createTable(response,
        document.getElementById('substitution-plan-head'),
        document.getElementById('substitution-plan-body'),
        ['Klasse', 'Stunde', 'Fach', 'Art', 'Lehrer', 'Vertreter'])
      initTableByProfile()
    },
    handleError,
    'get',
    RESOURCES_URL + '/substitution-plan'
  )
}

window.onload = init
