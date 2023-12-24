import * as utility from './utility.js'
import {
  ADMINISTRATOR_ENDPOINT,
  ADMINISTRATOR_ROLE,
  handleError,
  initTables,
  logoutUser,
  makeRequest,
  makeTableCollapsibleByTitle,
  RESOURCES_URL,
  TABLE_DICT
} from './utility.js'
import axios from 'axios'

const COURSES_DROPDOWN = document.getElementById('class-dropdown')
const TEACHER_DROPDOWN = document.getElementById('teacher-dropdown')
const SUBSTITUTION_DROPDOWN = document.getElementById('substitution-dropdown')
const SUBJECT_DROPDOWN = document.getElementById('subject-dropdown')
const HOUR_DROPDOWN = document.getElementById('hour-dropdown')
const ART_DROPDOWN = document.getElementById('art-dropdown')

const SUBMIT_BTN = document.getElementById('submit')

const SUBSTITUTION_PLAN_TABLE_HEADERS = ['Klasse', 'Stunde', 'Fach', 'Art', 'Lehrer', 'Vertreter', 'Delete']

const SUBSTITUTION_PLAN_BODY = document.getElementById('substitution-plan-body')
const SUBSTITUTION_PLAN_HEAD = document.getElementById('substitution-plan-head')

const NO_SUBSTITUTE_TEXT = 'Keine'

const TABLES = [
  { title: 'Schüler', id: 'students' },
  { title: 'Lehrer', id: 'teachers' },
  { title: 'Verwaltung', id: 'administration' },
  { title: 'Stundenpläne', id: 'timetable-entries' },
  { title: 'Klassen', id: 'classes' },
]

function deleteSubstitutionPlanEntry (id, row) {
  makeRequest(() => {
    row.remove()
    setTimeout(() => window.alert('Vertretungsplan Eintrag erfolgreich gelöscht!'), 100)
  }, handleError, 'delete', RESOURCES_URL + ADMINISTRATOR_ENDPOINT + '/substitution-plan/' + id, id)
}

function addRowToTable (data, thead, tbody, head) {
  data.forEach((entry) => {
    const row = tbody.insertRow()
    head.forEach((item, i) => {
      const cell = row.insertCell(i)
      item = TABLE_DICT[item]
      item === TABLE_DICT['Delete'] ? createCellWithDeleteButton(entry, item, row, cell) : cell.textContent = entry[item]
    })
  })
}

function createTableWithButtons (response, thead, tbody, head) {
  const data = response.data
  head.forEach(item => thead.innerHTML += `<th>${item}</th>`)

  data.forEach(entry => {
    const row = document.createElement('tr')
    head.forEach(item => {
      const cell = document.createElement('td')
      createCellWithDeleteButton(entry, TABLE_DICT[item], row, cell)
      row.appendChild(cell)
    })
    tbody.appendChild(row)
  })
}

function createCellWithDeleteButton (entry, item, row, cell) {
  if (item === TABLE_DICT['Delete']) {
    const button = document.createElement('button')
    const img = document.createElement('img')

    img.src = '/src/assets/icons/delete.svg'

    button.appendChild(img)
    button.className = 'delete'
    button.onclick = () => deleteSubstitutionPlanEntry(entry.id, row)

    cell.appendChild(button)
  } else {
    cell.textContent = entry[item]
  }
}

function initStudentsTable () {
  makeRequest(function (response) {
      utility.createTable(response,
        document.getElementById('students-head'),
        document.getElementById('students-body'),
        ['Name', 'Klasse', 'Rolle'])
    },
    handleError,
    'get',
    RESOURCES_URL + ADMINISTRATOR_ENDPOINT + '/students')
}

function initTeachersTable () {
  makeRequest(function (response) {
      utility.createTable(response,
        document.getElementById('teachers-head'),
        document.getElementById('teachers-body'),
        ['Name', 'Rolle', 'Anwesenheit', 'Faecher'])
    },
    handleError,
    'get',
    RESOURCES_URL + ADMINISTRATOR_ENDPOINT + '/teachers')
}

function initAdministrationTable () {
  makeRequest(function (response) {
      utility.createTable(response,
        document.getElementById('administration-head'),
        document.getElementById('administration-body'),
        ['Name', 'Rolle'])
    },
    handleError,
    'get',
    RESOURCES_URL + ADMINISTRATOR_ENDPOINT + '/administrators')
}

function initTimetableEntriesTable () {
  document.querySelectorAll('h1').forEach((item) => {
    if (item.textContent === 'Stundenpläne') {
      item.parentElement.classList.add('table-container-timetable-entries')
    }
  })

  const timetableEntriesBody = document.getElementById('timetable-entries-body')
  const timetableEntriesHead = document.getElementById('timetable-entries-head')

  axios.request({
    method: 'get',
    url: RESOURCES_URL + ADMINISTRATOR_ENDPOINT + '/courses',
    withCredentials: true
  })
    .then((response) => {
      timetableEntriesHead.innerHTML += `<th>Klasse</th>`
      const courses = response.data

      courses.forEach((klasse) => {
        const row = document.createElement('tr')
        const cell = document.createElement('td')
        cell.textContent = klasse.name

        // Use a closure to capture the current 'klasse' in the event listener
        cell.addEventListener('click', () => expandTimetableEntriesByClass(klasse, cell))

        row.appendChild(cell)
        timetableEntriesBody.appendChild(row)
      })
    })
    .catch((error) => {
      handleError(error)
    })
}

function expandTimetableEntriesByClass (klasse, clickedCell) {
  if (clickedCell.children.length > 0) {
    clickedCell.removeChild(clickedCell.children[0])
  } else {
    // Create a new table
    const newTable = document.createElement('table')

    // Add table header
    const headerRow = document.createElement('tr')
    headerRow.innerHTML = '<th>Stunde</th><th>Klasse</th><th>Fach</th><th>Lehrer</th><th>Tag</th>'
    newTable.appendChild(headerRow)

    const container = document.createElement('div')
    container.className = 'table-container'
    container.appendChild(newTable)

    const containerWrapper = document.createElement('div')
    containerWrapper.className = 'table-container-wrapper'
    containerWrapper.appendChild(container)

    // Add table rows with JSON data
    axios.request({
      method: 'get',
      url: RESOURCES_URL + ADMINISTRATOR_ENDPOINT + `/timetable-entries/${klasse.name}`,
      withCredentials: true
    })
      .then((response) => {
        response.data.forEach((entry) => {
          const row = document.createElement('tr')
          row.innerHTML = `<td>${entry.hour}</td><td>${entry.course}</td><td>${entry.subject}</td><td>${entry.teacher}</td><td>${entry.day}</td>`
          newTable.appendChild(row)
        })
        clickedCell.appendChild(containerWrapper)
      })
      .catch((error) => {
        handleError(error)
      })
  }
}

function initCollapsibleClassesTables (response, thead, tbody) {
  const TABLE_HEADERS = ['Name', 'Schueler']

  const data = response.data
  TABLE_HEADERS.forEach((item) => {
    thead.innerHTML += `<th>${item}</th>`
  })

  data.forEach((entry) => {
    const row = document.createElement('tr')
    TABLE_HEADERS.forEach((header) => {
      const cell = document.createElement('td')
      if (header === 'Schueler') {
        cell.textContent = entry[TABLE_DICT[header]] + ` ${entry['numberOfStudents'] - 5 > 0 ? ' und ' + (entry['numberOfStudents'] - 5) + ' weitere...' : ''}`
      } else {
        cell.textContent = entry[TABLE_DICT[header]]
      }
      row.appendChild(cell)
    })
    tbody.appendChild(row)
  })
}

function initClassesTable () {
  makeRequest(function (response) {
      initCollapsibleClassesTables(response, document.getElementById('classes-head'), document.getElementById('classes-body'))
    },
    handleError,
    'get',
    RESOURCES_URL + ADMINISTRATOR_ENDPOINT + '/courses')
}

function createNewSubstitutionPlanEntry (courseId, hour, art, teacherId, substitutionId, subjectId) {
  const data = JSON.stringify({
    'courseId': courseId,
    'hour': hour,
    'art': art,
    typ: 'MANUELL',
    'teacherId': teacherId,
    'substituteId': substitutionId,
    'subjectId': subjectId
  })

  makeRequest(function (response) {
    addRowToTable(
      [{
        'art': ART_DROPDOWN.value,
        'subject': SUBJECT_DROPDOWN.value,
        'course': COURSES_DROPDOWN.value,
        'teacher': TEACHER_DROPDOWN.value,
        'hour': HOUR_DROPDOWN.value,
        'typ': 'MANUELL',
        'substitute': SUBSTITUTION_DROPDOWN.value === NO_SUBSTITUTE_TEXT ? '' : SUBSTITUTION_DROPDOWN.value,
        'id': response.data['id']
      }], SUBSTITUTION_PLAN_HEAD, SUBSTITUTION_PLAN_BODY, SUBSTITUTION_PLAN_TABLE_HEADERS
    )
  }, handleError, 'post', RESOURCES_URL + ADMINISTRATOR_ENDPOINT + '/substitution-plan', data, {
    'Content-Type': 'application/json'
  })
}

function createSubstitutionPlan () {
  makeRequest(function (response) {
    const courses = new Map(Object.entries(response.data['courses']))
    const teachers = new Map(Object.entries(response.data['teachers']))
    const subjects = new Map(Object.entries(response.data['subjects']))

    const courseId = courses.get(COURSES_DROPDOWN.value)
    const hour = HOUR_DROPDOWN.value
    const art = ART_DROPDOWN.value
    const teacherId = teachers.get(TEACHER_DROPDOWN.value)
    const substitutionId = teachers.get(SUBSTITUTION_DROPDOWN.value) === NO_SUBSTITUTE_TEXT ? null : teachers.get(SUBSTITUTION_DROPDOWN.value)
    const subjectId = subjects.get(SUBJECT_DROPDOWN.value)

    if (art === 'VERTRETUNG' && substitutionId == null) {
      window.alert('Bitte wähle einen Vertreter aus!')
    } else if (art === 'ENTFALL' && substitutionId != null) {
      window.alert('Eine Stunde die entfällt kann keine Vertretung haben!')
    } else {
      createNewSubstitutionPlanEntry(courseId, hour, art, teacherId, substitutionId, subjectId)
    }

  }, handleError, 'get', RESOURCES_URL + ADMINISTRATOR_ENDPOINT + '/entities')
}

function initCreateSubstitutionPlanEntryView () {
  makeRequest(function (response) {
    const courses = new Map(Object.entries(response.data['courses']))
    const teachers = new Map(Object.entries(response.data['teachers']))
    const subjects = new Map(Object.entries(response.data['subjects']))

    courses.forEach((value, key) => {
      const option = document.createElement('option')
      option.value = key
      option.text = key
      COURSES_DROPDOWN.appendChild(option)
    })

    teachers.forEach((value, key) => {
      const option = document.createElement('option')
      option.value = key
      option.text = key
      TEACHER_DROPDOWN.appendChild(option)
      SUBSTITUTION_DROPDOWN.appendChild(option.cloneNode(true))
    })

    const option = document.createElement('option')
    option.value = NO_SUBSTITUTE_TEXT
    option.text = NO_SUBSTITUTE_TEXT
    SUBSTITUTION_DROPDOWN.appendChild(option)

    subjects.forEach((value, key) => {
      const option = document.createElement('option')
      option.value = key
      option.text = key
      SUBJECT_DROPDOWN.appendChild(option)
    })
  }, handleError, 'get', RESOURCES_URL + ADMINISTRATOR_ENDPOINT + '/entities')
}

function init () {
  utility.isAuthorized([ADMINISTRATOR_ROLE])

  initTables(TABLES)

  makeRequest(function (response) {
      createTableWithButtons(response, SUBSTITUTION_PLAN_HEAD, SUBSTITUTION_PLAN_BODY, SUBSTITUTION_PLAN_TABLE_HEADERS)
    },
    handleError,
    'get',
    RESOURCES_URL + '/substitution-plan/MANUELL')

  initStudentsTable()
  initTeachersTable()
  initAdministrationTable()
  initTimetableEntriesTable()
  initClassesTable()
  initCreateSubstitutionPlanEntryView()

  makeTableCollapsibleByTitle()

  SUBMIT_BTN.addEventListener('click', createSubstitutionPlan)

  document.getElementById('logout-img').addEventListener('click', logoutUser)
}

window.onload = init
