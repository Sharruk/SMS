# Learning Management System (LMS)

## Overview
This project is a comprehensive Java-based Learning Management System (LMS) designed with a layered architecture and object-oriented programming principles. It provides a console-based interface for managing students, courses, teachers, administrators, and principals, featuring JSON file persistence. The system showcases advanced OOP concepts such as inheritance, polymorphism, encapsulation, generics, custom exception handling, search/sort functionalities, upload services, and robust role-based access control. The primary goal is to deliver a fully functional and scalable LMS demonstrating best practices in software design.

## User Preferences
Preferred communication style: Simple, everyday language.

## System Architecture

### Technology Stack
- **Java 17+**: Core programming language.
- **Maven**: Build system and dependency management.
- **Jackson Databind**: For JSON serialization/deserialization with polymorphic type handling.
- **Console Interface**: Text-based user interaction with a comprehensive menu system.

### Core Entities
- **User (Abstract Class)**: Base for Admin, Teacher, Student, Principal.
- **Admin**: Manages students, teachers, courses, and batches.
- **Teacher**: Manages assignments, grades, attendance, and communicates with students.
- **Student**: Manages academic information, courses, assignments, and grades.
- **Principal**: Provides administrative oversight, manages admins, teachers, and courses.
- **Course**: Defines course details and scheduling.
- **Department**: Groups courses and manages academic structures.
- **Batch**: Manages student cohorts.
- **Assignment**: Tracks assignment details for courses.
- **Grade**: Records student performance in courses.
- **Message**: Facilitates internal communication between user roles.

### Data Persistence
- **JSON Files**: Utilizes `students.json`, `teachers.json`, `admins.json`, `courses.json`, `messages.json`, `assignments.json`, `grades.json` for data storage.
- **Generic Repository Pattern**: Provides type-safe CRUD operations.
- **Jackson Integration**: Enables polymorphic JSON serialization/deserialization.

### Key Features
- **Comprehensive OOP Demonstration**: Showcases inheritance, polymorphism, encapsulation, generics, and custom exception handling.
- **Role-based Access Control (RBAC)**: Differentiates capabilities for Principal, Admin, Teacher, and Student roles.
- **Search and Sort**: Role-specific search and sort functionalities for system entities.
- **Upload Service**: Handles file uploads with validation.
- **Interactive Console Menu**: A complete menu system for all features.
- **Custom Exception Hierarchy**: Structured exception handling for robust error management.

## External Dependencies

### Build System
- **Maven 3.11+**: Project build and dependency management.
- **Java 17**: Target JDK version.

### Libraries
- **Jackson Databind 2.15.2**: JSON processing and polymorphic object mapping.
- **Jackson Annotations**: For type handling and serialization control.

### Runtime Environment
- **Console/Terminal**: Interactive text-based user interface.
- **File System**: For JSON file persistence and upload directory management.

## Recent Changes

### 2025-10-03: Replit Environment Setup - GitHub Import Completed
- Successfully imported Java-based LMS project from GitHub
- Installed Java (GraalVM 19.0.2) and Maven (3.9.9) via Replit modules
- Compiled the project successfully with Maven (35 source files)
- Configured workflow "LMS Application" to run the console application via `mvn exec:java`
- Application starts successfully and demonstrates all OOP features on launch
- JSON data persistence is working correctly (students.json, teachers.json, admins.json, courses.json, messages.json, assignments.json, grades.json)
- Console menu system is functional and ready for user interaction
- Workflow runs in console mode (output_type: console) as this is a terminal-based application
- .gitignore already properly configured for Java/Maven projects

## Running the Application

The application is configured to run automatically via the "LMS Application" workflow in the console. When started:
1. Maven downloads dependencies and initializes the application
2. Loads all data from JSON files (students, teachers, admins, courses, messages, assignments, grades)
3. Runs a comprehensive demonstration of OOP features (inheritance, polymorphism, encapsulation, generics, exception handling, search/sort, upload service, role-based access)
4. Presents an interactive menu with options for:
   - Role-based Access Demo (Principal, Admin, Teacher, Student)
   - Search & Sort Demo
   - File Upload Demo
   - User Management
   - Course Management
   - System Statistics
   - Complete OOP Features Demo

To interact with the application, use the console in the workflow panel. The application waits for user input at the menu prompt.

### Manual Commands
If you need to run the application manually:
```bash
mvn clean compile          # Compile the project
mvn exec:java              # Run the application
```

## Application Status
✓ All repositories initialized successfully
✓ Data loaded: 2 students, 2 teachers, 2 admins, 3 courses, 3 messages, 3 assignments, 3 grades
✓ OOP features demonstration completed successfully
✓ Interactive menu is active and waiting for user input