import axios from 'axios'
import { AUTHENTICATION_URL, handleError } from './utility.js'

const LOGIN_BTN = document.getElementById('submit')

function getUserCredentials (event) {
  event.preventDefault()

  const email = document.getElementById('email').value
  const password = document.getElementById('password').value

  loginUser(email, password)
}

function loginUser (email, password) {
  const data = JSON.stringify({
    email,
    password
  })

  const config = {
    method: 'post',
    maxBodyLength: Infinity,
    url: AUTHENTICATION_URL + '/authenticate',
    headers: {
      'Content-Type': 'application/json'
    },
    data,
    withCredentials: true
  }

  axios.request(config)
    .then(() => {
      window.location.href = '../pages/substitution_plan.html'
    })
    .catch((error) => {
      handleError(error)
    })
}

window.onload = function () {
  LOGIN_BTN.addEventListener('click', getUserCredentials)
}

