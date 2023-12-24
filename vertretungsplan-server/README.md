<h1 align="center">Vertretungsplan Server</h1>
<h3 align="center">A <u>faster</u> substitution plan!</h2>

<p align="center">
    <h4 align="center">Developed with the software and tools below</h4>
    <div align="center">
    <img src="https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot">
    <img src="https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white">
    <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white">
    <img src="https://img.shields.io/badge/Junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white">
    <img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=Spring-Security&logoColor=white">
    <img src="https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white">
    <img src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white">
    </div>
</p>

---

## Table of Contents

- [Table of Contents](#table-of-contents)
- [Overview](#overview)
- [Features](#features)
- [Modules](#modules)
  - [Application Properties](#applicationproperties)
  - [Application Test Properties](#application-testproperties)
- [Repository Structure](#repository-structure)
- [Getting Started](#getting-started)
  - [Requirements](#requirements)
  - [Installation](#installation)
  - [Run the Server](#running-the-server)
  - [Tests](#tests)
  - [Postman](#postman)
- [Creating custom entities](#creating-custom-entities)
- [Roadmap](#roadmap)

## Overview

This backend application is built using Spring Boot and Spring Security. It relies on MySQL as its database, and the entities within the system have intricate relationships with one another. I encountered a learning curve during a previous unsuccessful project, where I attempted to store different objects as JSON data in a single column, including those requiring relationships with other objects. After reassessing and scrapping that initial project, I dedicated time to understanding various relationship types, Hibernate inheritance mapping (especially for users), and the concept of natural keys.

Throughout the development of this application, I incorporated several new features that hadn't crossed my mind previously. Examples include implementing JWT for user sessions and incorporating robust testing practices. This release encapsulates everything I've learned about backend development thus far. The process was both enjoyable and enlightening, providing me with a more profound understanding of how backends operate.

## Features

#### Efficient User Sessions

Instead of using traditional Session Tokens, our application employs JWT (JSON Web Tokens) for user sessions. This eliminates the need to save and manage individual Session Tokens. The server can effortlessly verify the signature on a cookie, set by the application itself. This approach ensures a seamless, fast, and secure authorization process.

#### Effective Testing and Debugging:

The application is equipped with a robust testing framework, both internally and externally. This enables quick debugging and error analysis. Any issues, whether in code or unexpected behavior in endpoints, become immediately apparent.

#### Logging for Insight

Logs play a crucial role in identifying issues. In the development profile, everything above the TRACE log level is logged to the console. In the production profile, warnings (WARN) are logged in the application, while errors (ERROR) are logged in the complete application, including dependencies. Logs are stored in files with a daily rollover and a maximum history of 30 files. The use of Spring AOP logging minimizes the number of loggers, maintaining a centralized logging module for tracking various operations in the application.

#### Structured Architecture

The project is designed with different architectures to ensure structure and optimization. The primary architecture is the layered approach, consisting of a Controller, Service, and Repository Layer. This provides a well-organized structure for handling data retrieval, processing, and presentation. User entities utilize Hibernate Inheritance Mapping for a consistent structure, sharing common attributes such as Emails, Names, Passwords, and Roles.

#### Exception Handling

An exception handler ensures that any thrown exceptions are logically conveyed to the user without displaying the entire stack trace. The full details are saved on the server side, allowing for centralized handling of exceptions at a global level.

#### Role-Based Access Control

Various endpoints are secured using Spring Security, accessible only to specified roles. These roles are user-specific and determine the resources to which the user should have access.

## Modules

| Module             | Description                                                                                                                                                                                                             |
| ------------------ | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| mvnw               | Maven Wrapper for Linux                                                                                                                                                                                                 |
| mvnw.cmd           | Maven Wrapper for Windows                                                                                                                                                                                               |
| pom.xml            | The pom.xml file contains information about the project and configuration information for maven to build the project such as dependencies, build directory, source directory, test source directory, plugin, goals etc. |
| postman            | This directory contains the Postman Collection and Environment for testing with Postman, as well as a custom bash script to automatically run that collection with the environment                                      |
| src/main           | In this directory is the main source code located                                                                                                                                                                       |
| src/test           | Here you can find the source code of different tests, testing various parts of the code in `src/main`                                                                                                                   |
| src/main/resources | This directory contains the `application.properties` file, as well as `logback-spring.xml` for configuring various properties in the Spring Boot application or the Logback Logger                                      |
| src/test/resources | This directory contains the `application-test.properties` for specifieing various properties for the testing environment                                                                                                |

### application.properties

This file contains crucial configurations for the application. Here are some key settings you should be aware of:

```
spring.datasource.url=jdbc:mysql://localhost:3306/vertretungsplan
spring.datasource.username=root
spring.datasource.password=
```

These settings customize the data source, allowing the application to perform operations on the database.

```
spring.profiles.active=prod
```

This line designates the active profile. The valid profiles are `prod` and `dev`, impacting factors such as how the current day is determined.

### application-test.properties

This file holds essential configurations necessary for testing the application. Here are notable settings you should remember:

```
spring.datasource.url=jdbc:h2:mem:test;DB_CLOSE_DELAY=-1
spring.datasource.driver-class-name=org.h2.Driver
# spring.datasource.username=sa
# spring.datasource.password=sa
```

These lines establish the data source, specifically using an H2 in-memory database for testing purposes.

```
spring.config.activate.on-profile=test
```

This setting selects the active profile for the test package, which should be `test` during testing to avoid conflicts, such as inadvertent instantiation of beans that shouldn't be created.

## Repository Structure

```
│   mvnw
│   mvnw.cmd
│   pom.xml
│   README.md
│
├───postman
│       run_collection_postman.bat
│       Vertretungsplan.postman_collection.json
│       Vertreungsplan.postman_environment.json
│
├───src
│   ├───main
│   │   ├───java
│   │   └───resources
│   │           application.properties
│   │           logback-spring.xml
│   │
│   └───test
│       ├───java
│       └───resources
│               application-test.properties
│
└───target
```

## Getting Started

### Requirements

For this project, you need to at least [Java Version 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) and optionally [Maven](https://maven.apache.org/download.cgi) installed. You can check your Maven version using

```
> mvn -version
Apache Maven 3.9.5 (57804ffe001d7215b5e7bcb531cf83df38f93546)
Maven home: C:\Program Files (x86)\Maven\apache-maven-3.9.5
Java version: 21.0.1, vendor: Oracle Corporation, runtime: C:\Program Files\Java\jdk-21
Default locale: de_DE, platform encoding: UTF-8
OS name: "windows 11", version: "10.0", arch: "amd64", family: "windows"
```

(A Maven installation isn't **required** since this project also comes with the Maven Wrapper for Linux and Windows)

and your Java Version with

```
> java --version
java 21.0.1 2023-10-17 LTS
Java(TM) SE Runtime Environment (build 21.0.1+12-LTS-29)
Java HotSpot(TM) 64-Bit Server VM (build 21.0.1+12-LTS-29, mixed mode, sharing)
```

It's also important to have a MySQL Database called **vertretungsplan** on port **3306**. For this, I used [XAMPP](https://www.apachefriends.org/de/download.html) which comes with Apache and MySQL out of the box and also uses port **3306** for MySQL by default, so I would recommend using XAMPP. Then just start up Apache and MySQL and create a new database under http://localhost/phpmyadmin/ called **vertretungsplan**, so this URL (`spring.datasource.url=jdbc:mysql://localhost:3306/vertretungsplan`) from the [application.properties](#applicationproperties) can be resolved.

### Installation

1. Clone this repository

```
git clone https://github.com/github-gabriel/vertretungsplan.git
```

2. Go into the `vertretungsplan-server` folder

```
cd .\vertretungsplan-server\
```

3. To compile, build and test the code run

```
mvn clean install
```

or if you don't have Maven installed you can just use the Maven Wrapper inside the project

```
.\mvnw clean install
```

### Running the Server

After you built everything you can run

```
mvn spring-boot:run
```

or use the Maven Wrapper

```
.\mvnw spring-boot:run
```

to start the application.

After successfully running the application you can view the documentation of various API Endpoints under http://localhost:8080/swagger-ui/index.html#/

### Tests

To run the internal tests written with JUnit 5, Mockito and AssertJ run

```
mvn test
```

or use the Maven Wrapper

```
.\mvnw test
```

Ideally, your results should look like this

```
[INFO] Results:
[INFO]
[INFO] Tests run: 71, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  8.668 s
[INFO] Finished at: 2023-12-24T19:41:29+01:00
[INFO] ------------------------------------------------------------------------
```

### Postman

To test the different Endpoints consider using [Postman](https://www.postman.com/). You can [import the collection and environment](https://learning.postman.com/docs/getting-started/importing-and-exporting/importing-and-exporting-overview/) from the [Postman folder](postman/) to get started. Remember to use the imported environment with the imported collection, so the requests have access to the variables defined in the environment. You can use the provided [Batch file for Windows](postman/run_collection_postman.bat) to run the collection for x iterations with a delay between each collection run for y minutes.

⚠️ Important: ⚠️ Don't forget to set your Postman Token for `YOUR_POSTMAN_API_KEY` in the provided [Batch file](postman/run_collection_postman.bat) to execute the collection

After execution your results should look like this

```
┌─────────────────────────┬──────────────────┬──────────────────┐
│                         │         executed │           failed │
├─────────────────────────┼──────────────────┼──────────────────┤
│              iterations │                1 │                0 │
├─────────────────────────┼──────────────────┼──────────────────┤
│                requests │               19 │                0 │
├─────────────────────────┼──────────────────┼──────────────────┤
│            test-scripts │               38 │                0 │
├─────────────────────────┼──────────────────┼──────────────────┤
│      prerequest-scripts │               19 │                0 │
├─────────────────────────┼──────────────────┼──────────────────┤
│              assertions │               37 │                0 │
├─────────────────────────┴──────────────────┴──────────────────┤
│ total run duration: 1007ms                                    │
├───────────────────────────────────────────────────────────────┤
│ total data received: 6.4kB (approx)                           │
├───────────────────────────────────────────────────────────────┤
│ average response time: 27ms [min: 5ms, max: 94ms, s.d.: 22ms] │
└───────────────────────────────────────────────────────────────┘
```

## Creating custom entities

Since this application is intended for showcase only, there currently aren't endpoints for creating/editing individual entities,
which is the reason why you have to create them programmatically if you want different entities to the default ones, because the **prod** profile, for example, uses the actual current day for determining the current day, whereas the **dev**
profile always uses Monday as the current day to showcase the algorithm.

The data for the application is saved in the [VertretungsplanApplication.java](src/main/java/de/gabriel/vertretungsplan/VertretungsplanApplication.java) which contains a CommandLineRunner Bean

```java
@Bean
@Profile("!test")
CommandLineRunner commandLineRunner(SubjectRepository subjectRepository, CourseRepository courseRepository,
                                    TimetableEntryRepository timetableEntryRepository, DayRepository dayRepository,
                                    UserService userService) {
```

There you save the Users (Student, Teacher, Administrator) using the `userService` with the accordingly named methods and the other entities with their according repositories. For the different users, you can also use the `DEFAULT_PASSWORD` constant from [ApplicationConfig.java](src/main/java/de/gabriel/vertretungsplan/config/ApplicationConfig.java).

To create the entities, use the following constructors

**Subject**

```java
Subject de = new Subject("Deutsch", "DE");
```

**Teacher**

```java
 Teacher lde = new Teacher("Juliane Köppen", "JK", "juliane.köppen@gmail.com", DEFAULT_PASSWORD, Attendance.ABWESEND);
```

**Day**

```java
DayEntity monday = new DayEntity(Day.MONDAY);
```

**Administrator**

```java
Administrator administrator = new Administrator("administrator", "administrator@gmail.com", DEFAULT_PASSWORD);
```

**TimetableEntries**

```java
TimetableEntry timetableEntryKL_1 = new TimetableEntry(1, kl, de, lde, montag);
```

For students and courses it's important to first create the course, then set it for the student and save the student, then set the students for the course and save the course.

```java
Course kl = new Course("KL"); // Create course

List<Student> studentList = new ArrayList<>();

for (int i = 0; i < 50; i++) {
    Student student = new Student("student" + i, "student" + i + "@gmail.com", DEFAULT_PASSWORD, kl); // Create student and set course
    studentList.add(student);
}

userService.saveStudents(studentList); // Save students

Set<Student> studentSet = new HashSet<>(studentList);
kl.setStudents(studentSet); // Set student set for course
courseRepository.save(kl); // Save course
```

## Roadmap

- [x] Code Testing with JUnit
- [x] External API Tests with Postman
- [x] Internal Tests with JUnit, Mockito and AssertJ
- [x] Security through the usage of JWTs
- [x] Separation of code in different layers (Layered Architecture)
- [x] Logging for different profiles with Logback
- [x] Global Error Handling
- [x] JWT for handling user sessions
- [x] Algorithm to automatically create substitution plan entries
- [x] Teacher being able to set their attendance status
- [ ] Endpoints for individually creating or editing entities
