import axios from 'axios'
import * as utility from './utility.js'
import {
  handleError,
  initTables,
  logoutUser,
  makeRequest,
  makeTableCollapsibleByTitle,
  RESOURCES_URL,
  TEACHER_ENDPOINT,
  TEACHER_ROLE
} from './utility.js'

const ATTENDANCE = ['ANWESEND', 'ABWESEND']
const ATTENDANCE_DROPDOWN = document.getElementById('dropdown')

const TABLES = [
  { title: 'Stundenpläne', id: 'timetable-entries' },
  { title: 'Vertretungspläne', id: 'substitution-plan' }
]

function changeAttendanceStatus () {
  let selectedIndex = ATTENDANCE_DROPDOWN.selectedIndex

  const data = JSON.stringify({
    attendance: ATTENDANCE[selectedIndex]
  })

  makeRequest(
    function () {
      window.alert('Dein Status wurde erfolgreich auf ' + ATTENDANCE[selectedIndex] + ' geändert.')
    },
    handleError,
    'post',
    `${RESOURCES_URL}${TEACHER_ENDPOINT}/attendance-status`,
    data,
    {
      'Content-Type': 'application/json'
    }
  )
}

function getCurrentDay () {
  const config = {
    method: 'get',
    maxBodyLength: Infinity,
    url: RESOURCES_URL + '/current-day',
    withCredentials: true
  }

  return axios.request(config)
}

function initTimetableEntriesTable () {
  makeRequest(function (response) {
      utility.createTable(response,
        document.getElementById('timetable-entries-head'),
        document.getElementById('timetable-entries-body'),
        ['Stunde', 'Klasse', 'Fach', 'Lehrer', 'Tag'])
    },
    handleError,
    'get',
    RESOURCES_URL + TEACHER_ENDPOINT + '/timetable-entries'
  )
}

function initSubstitutionPlanEntriesTable () {
  makeRequest(function (response) {
      utility.createTable(response,
        document.getElementById('substitution-plan-head'),
        document.getElementById('substitution-plan-body'),
        ['Klasse', 'Stunde', 'Art', 'Lehrer', 'Vertreter', 'Fach'])
    },
    handleError,
    'get',
    RESOURCES_URL + TEACHER_ENDPOINT + '/substitution-plan'
  )
}

function initInfoCard () {
  // Init Dropdown
  for (let i = 0; i < ATTENDANCE.length; i++) {
    const option = document.createElement('option')
    option.text = ATTENDANCE[i]
    option.value = ATTENDANCE[i]
    ATTENDANCE_DROPDOWN.appendChild(option)
  }
  ATTENDANCE_DROPDOWN.addEventListener('change', changeAttendanceStatus)

  // Show current day and DateTime
  getCurrentDay().then((response) => {
    const CURRENT_DAY = response.data['currentDay'].charAt(0) + response.data['currentDay'].slice(1).toLowerCase()
    document.getElementById('day').textContent = '🗓️Heute ist ' + CURRENT_DAY + ', der ' + new Date(response.data['currentDateTime']).toLocaleDateString()
  }).catch((error) => {
    handleError(error)
  })

  // Set Dropdown to current Attendance Status
  axios.request({
    method: 'get',
    maxBodyLength: Infinity,
    url: RESOURCES_URL + TEACHER_ENDPOINT + '/attendance-status',
    withCredentials: true
  }).then((response) => {
    const index = ATTENDANCE.indexOf(response.data.attendance)
    ATTENDANCE_DROPDOWN.value = ATTENDANCE[index]
    ATTENDANCE_DROPDOWN.selectedIndex = index
  }).catch((error) => {
    handleError(error)
  })
}

function init () {
  utility.isAuthorized([TEACHER_ROLE])

  initTables(TABLES)

  initInfoCard()
  initSubstitutionPlanEntriesTable()
  initTimetableEntriesTable()

  makeTableCollapsibleByTitle()

  document.getElementById('logout-img').addEventListener('click', logoutUser)
}

window.onload = init
