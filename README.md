# Course Enrollment System by Sheishembekova Aigerim

---

## Project Description

Student Enrollment System is an application designed to manage student registrations, course offerings, and enrollments in a structured and user-friendly manner. The system enables users to add, view, update, and delete students and courses, as well as manage student enrollments in courses. Each student contains personal information (first name, last name, email) and a score. Each course contains a name, instructor, and credit hours.

The application provides a Graphical User Interface (GUI) built with Java Swing. All data is stored in a SQLite database using JDBC, thereby ensuring persistence across program sessions. The program also incorporates input validation and error handling mechanisms to guarantee stable and reliable execution. Additionally, CSV import and export functionality is provided for data portability.

This project demonstrates the practical application of fundamental Object-Oriented Programming (OOP) principles, including encapsulation, inheritance, and polymorphism.

---

## Project Objectives

The principal objective of this project is to develop a fully functional student enrollment management system while applying core Java programming and OOP concepts.

The specific aims of the project are as follows:

- Implement complete CRUD (Create, Read, Update, Delete) functionality for students and courses.
- Provide a Graphical User Interface (GUI) for user interaction using Java Swing.
- Ensure data persistence through SQLite database storage with JDBC.
- Apply OOP principles (encapsulation, inheritance, polymorphism).
- Validate user input using regex patterns and range checks to prevent incorrect data entry.
- Implement secure user authentication with password hashing (SHA-256).
- Provide role-based access control (Teacher and Student roles) with different permissions.
- Handle runtime errors using appropriate exception handling techniques (try-catch blocks).
- Organize the program into modular, reusable packages (model, repository, service, ui).
- Enable data import and export through CSV file operations.

---

## Project Requirements

The system satisfies the following requirements:

| # | Requirement | Description |
|---|-------------|-------------|
| 1 | CRUD Operations | Full Create, Read, Update, Delete functionality for Students and Courses |
| 2 | Graphical User Interface | Swing-based GUI with windows, dialogs, tables, and forms |
| 3 | Input Validation | Validation for names (letters only, 2-50 chars), email format, score (0-100), credits (1-6) |
| 4 | Data Persistence | SQLite database storage with JDBC for permanent data retention between sessions |
| 5 | File Import/Export | CSV import and export functionality for student data |
| 6 | Modular Architecture | Code organized into separate packages: Tables (model), repository, service |
| 7 | Error Handling | Try-catch blocks and user-friendly error/warning dialogs via JOptionPane |
| 8 | Encapsulation (OOP) | All class fields are private; access provided via public getters and setters |
| 9 | Inheritance (OOP) | Student class extends Person base class, reusing common attributes |
| 10 | Polymorphism (OOP) | Repository interfaces (StudentRepository, CourseRepository, EnrollmentRepository) with concrete JDBC implementations |
| 11 | User Authentication | Login system with username/password verification |
| 12 | Role-Based Access Control | Teacher (full CRUD access) and Student (view-only) roles with different menus |
| 13 | Enrollment Management | Register students to courses, update enrollments, and drop enrollments |
| 14 | Data Visualization | JTable components for displaying lists of students and courses |
| 15 | Password Security | SHA-256 hashing algorithm for stored passwords |

---

## Documentation

### Data Structures

The project employs the following data structures:

- **ArrayList** — used in repository implementations to store and return collections of objects retrieved from the database.
- **HashMap** — used in `AuthService` to store user credentials in memory for authentication.

### Main Classes and Modules

| Class | Package | Description |
|-------|---------|-------------|
| `Person` | `Tables` | Base class storing common attributes: firstName, lastName, email |
| `Student` | `Tables` | Extends Person; adds id, score, and userId fields |
| `Course` | `Tables` | Stores course data: id, courseName, instructorName, credits |
| `Enrollment` | `Tables` | Stores enrollment data: id, studentId, courseId, enrollmentDate |
| `User` | `Tables` | Stores user credentials: username, passwordHash, role |
| `Role` | `Tables` | Enum defining user roles: TEACHER, STUDENT |
| `StudentRepository` | `repository` | Interface defining CRUD operations for Student entities |
| `StudentRepositoryImpl` | `repository` | JDBC implementation of StudentRepository |
| `CourseRepository` | `repository` | Interface defining CRUD operations for Course entities |
| `CourseRepositoryImpl` | `repository` | JDBC implementation of CourseRepository |
| `EnrollmentRepository` | `repository` | Interface defining enrollment management operations |
| `EnrollmentRepositoryImpl` | `repository` | JDBC implementation with JOIN queries for enrollment data |
| `AuthService` | `service` | Handles authentication, password hashing, and current user session |
| `Main` | `org.example` | Swing GUI entry point; contains all UI components and event handlers |

### Inheritance Structure


The `Student` class inherits `firstName`, `lastName`, and `email` fields from the `Person` base class, demonstrating the **Inheritance** OOP principle and reducing code duplication.

### Polymorphism Implementation

The project demonstrates **Polymorphism** through the Repository pattern:

- `StudentRepository` (interface) → `StudentRepositoryImpl` (JDBC implementation)
- `CourseRepository` (interface) → `CourseRepositoryImpl` (JDBC implementation)
- `EnrollmentRepository` (interface) → `EnrollmentRepositoryImpl` (JDBC implementation)

This design allows the application to easily switch between different storage implementations (e.g., file-based, in-memory) without modifying the calling code.

### Key Functions

| Function | Location | Description |
|----------|----------|-------------|
| `addStudent()` | Main (GUI) | Opens dialog, validates input, calls repository to save student |
| `showStudents()` | Main (GUI) | Retrieves all students and displays in JTable |
| `updateStudent()` | Main (GUI) | Loads existing student, opens edit dialog, saves changes |
| `deleteStudent()` | Main (GUI) | Removes student after confirmation |
| `addCourse()` | Main (GUI) | Opens dialog, validates input, saves course |
| `enrollStudent()` | Main (GUI) | Displays dropdowns, registers student to selected course |
| `showEnrollments()` | Main (GUI) | Displays formatted enrollment list with JOIN data |
| `exportToCSV()` | Main (GUI) | Writes student data to CSV file |
| `importFromCSV()` | Main (GUI) | Reads CSV, validates, and imports students |
| `login()` | AuthService | Verifies credentials, hashes password, sets current user |
| `isTeacher()` / `isStudent()` | AuthService | Role checking for UI rendering |

### Algorithms Used

- **Linear search** — iterating through ArrayLists to populate dropdown options and tables.
- **Regex pattern matching** — validating names (`[A-Za-z]{2,50}`) and email format.
- **SHA-256 hashing** — secure one-way password storage.
- **SQL JOIN queries** — combining student and course data for enrollment display.
- **Conditional logic** — role-based menu rendering and permission checking.

### Challenges Faced

- **Managing role-based UI rendering** — ensuring Teacher and Student menus display correctly based on authentication state.
- **Handling nullable foreign keys** — properly managing `user_id` field in Student table that may be null.
- **Ensuring consistent input validation** — implementing regex patterns that work across all dialog forms.
- **Designing clean class structure** — separating model, repository, service, and UI layers while maintaining OOP principles.
- **Implementing enrollment JOIN queries** — writing SQL that correctly combines data from three tables (students, courses, enrollments).
- **Managing database connection resources** — using try-with-resources to prevent connection leaks.
