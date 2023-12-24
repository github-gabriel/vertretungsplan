<h1 align="center">Vertretungsplan Client</h1>
<h3 align="center">A more <u>beautiful</u> substitution plan!</h2>

<p align="center">
    <h4 align="center">Developed with the software and tools below</h4>
    <div align="center">
        <img src="https://img.shields.io/badge/Vite-B73BFE?style=for-the-badge&logo=vite&logoColor=FFD62E">
        <img src="https://img.shields.io/badge/html5-%23E34F26.svg?style=for-the-badge& logo=html5&logoColor=white">
        <img src="https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&   logoColor=white">
        <img src="https://img.shields.io/badge/JavaScript-323330?style=for-the-badge&logo=javascript&logoColor=F7DF1E">
        <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white">
    </div>
</p>

---

## Table of Contents

- [Table of Contents](#table-of-contents)
- [Overview](#overview)
- [Features](#features)
- [Modules](#modules)
- [Repository Structure](#repository-structure)
- [Getting Started](#getting-started)
  - [Installation](#installation)
  - [Run the Client](#running-the-client)
- [Roadmap](#roadmap)

## Overview

This is the client of the Vertretungsplan application, written in HTML, CSS and Vanilla JS since I didn't want to learn another JS framework, that will be outdated after 2 weeks. In the middle of this project, I decided to switch from
Webpack to Vite for bundling the project, since I still needed Axios for making the requests to my backend and I just got to like Vite because it's very modern and has nice config files to configure everything and just seems easier to work with
in general. I gained valuable insights into web development while working on this website. As someone with limited experience in this field, the project taught me how to effectively communicate with a server and make use of the data it returns within the application.

## Features

This frontend utilizes the Axios module for server requests. Initially, incorporating cookies into the requests posed a challenge, especially for authenticating and transferring the JWT. However, I successfully resolved this hurdle by utilizing the `withCredentials` parameter.

Another obstacle involved coherently presenting data. For instance, for timetable entries, organizing them by course is essential. Achieving this required implementing various techniques to display entries within the corresponding course column, all within a table structure.

Vite enhances the bundling and deployment of this application, providing a seamless and enjoyable development experience. The human-readable configuration files simplify the JavaScript ecosystem, making testing and site previewing quick and straightforward.

Authentication is a prerequisite for accessing any page, initiating a session lasting seven days upon successful authentication. A JWT cookie, valid for a week, is set during this process. The site automatically verifies permissions based on your role, either granting or denying access to the page. Logging out removes the cookie and current session, allowing for authentication with a new account.

In the event of an error, a pop-up displays with a detailed message. In most cases, this message originates from the backend, offering specific information about the error. If not, a generic error message informs the user of an issue.

## Modules

| Module            | Description                                                                                                                                              |
| ----------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------- |
| index.html        | This is the main HTML file, serving as the landing page and the entry point of the project                                                               |
| package-lock.json | This file locks down the version of each installed package to ensure consistent dependency resolution and reproducibility across different environments. |
| package.json      | A file in a Node.js project that defines metadata about the project and its dependencies                                                                 |
| vite.config.js    | A configuration file used by Vite                                                                                                                        |
| dist              | Here you can find the finished build after building the project with Vite                                                                                |
| src               | This folder contains the source code of the website, including HTML,CSS and JS                                                                           |

## Repository Structure

```
│   index.html
│   package-lock.json
│   package.json
│   README.md
│   vite.config.js
│
├───dist
├───node_modules
└───src
    ├───assets
    ├───js
    ├───pages
    └───styles
```

## Getting Started

### ⚠️Important:⚠️ Make sure you can [run the server](../vertretungsplan-server/README.md/#running-the-server), which is essential for the client to work

### Installation

1. Clone this repository

```
git clone https://github.com/github-gabriel/vertretungsplan.git
```

2. Go into the `vertretungsplan-client` folder

```
cd .\vertretungsplan-client\
```

3. Install the dependencies with `npm`

```
npm install
```

### Running the Client

If you want to make changes to the client use

```
vite
```

or using npm

```
npm run dev
```

This will start the Vite dev server in the current directory at http://localhost:5173, allowing you to make changes while running

To build this project run

```
vite build
```

or using npm

```
npm run build
```

The output will be placed in the `dist`` folder.

To then preview this build locally use

```
vite preview
```

or using npm

```
npm run preview
```

This will boot up a local static web server at http://localhost:4173

## Roadmap

- [x] Intuitive and User-Friendly Interface
- [x] Best possible presentation of data
- [x] Admin dashboard to view important data
- [x] Error handling and informing the user of errors
- [x] Making use of cookies for authorization
- [ ] Admin dashboard to edit/create different entities
