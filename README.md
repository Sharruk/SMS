# Learning Management System (LMS)

A comprehensive Java-based Learning Management System that demonstrates object-oriented programming principles through a complete class hierarchy implementation.

## Overview

This LMS provides a console-based interface for managing students, courses, teachers, admins, and principals with JSON file persistence. The system showcases comprehensive OOP concepts including inheritance, polymorphism, encapsulation, generics, custom exception handling, search/sort interfaces, upload services, and role-based access control.

## Technology Stack

- **Java 17+**: Core programming language
- **Maven**: Build system and dependency management
- **Jackson Databind**: JSON serialization/deserialization with polymorphic type handling
- **Console Interface**: Text-based user interaction with comprehensive menu system

## Features

### Core OOP Demonstrations
- **Inheritance**: Complete User hierarchy (Admin, Teacher, Student, Principal)
- **Polymorphism**: Overridden methods called via User reference
- **Encapsulation**: Private fields with public getters/setters
- **Generics**: Repository<T> interface with type safety
- **Exception Handling**: Custom exception hierarchy with try-catch-finally blocks

### System Capabilities
- **Role-based Access Control**: Each user role has specific capabilities and access levels
- **Search and Sort Interfaces**: Role-based search and sort capabilities
- **Upload Service**: File upload with validation, storage, and metadata management
- **Interactive Console Menu**: Complete menu system for testing all features
- **JSON Persistence**: Data storage with error recovery and graceful handling

### User Roles and Access
- **Principal**: Add and manage admins, appoint teachers, assign courses, view reports
- **Admin**: Register students, manage teachers/courses, assign batches, search all entities
- **Teacher**: Create assignments, mark attendance, view students, search assigned students
- **Student**: View assignments/grades, mark attendance, search enrolled courses

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## How to Run in Terminal

### Option 1: Using Maven (Recommended)

1. **Compile the project:**
   ```bash
   mvn clean compile
   ```

2. **Run the application:**
   ```bash
   mvn exec:java -Dexec.mainClass="sms.app.Main"
   ```

### Option 2: Using Java directly

1. **Compile all Java files:**
   ```bash
   mvn clean compile
   ```

2. **Run with classpath:**
   ```bash
   java -cp "target/classes:target/dependency/*" sms.app.Main
   ```

### Option 3: Build and run JAR

1. **Build the JAR file:**
   ```bash
   mvn clean package
   ```

2. **Run the JAR:**
   ```bash
   java -jar target/student-management-system-1.0.0.jar
   ```

## Project Structure

```
src/
├── main/java/sms/
│   ├── app/                 # Main application entry point
│   │   └── Main.java
│   ├── data/                # Data access layer with repositories
│   │   ├── Repository.java
│   │   ├── StudentRepository.java
│   │   ├── TeacherRepository.java
│   │   ├── AdminRepository.java
│   │   └── CourseRepository.java
│   ├── domain/              # Core entities and domain models
│   │   ├── User.java        # Abstract base class
│   │   ├── Admin.java
│   │   ├── Teacher.java
│   │   ├── Student.java
│   │   ├── Principal.java
│   │   ├── Course.java
│   │   ├── Department.java
│   │   ├── Batch.java
│   │   ├── Attendance.java
│   │   └── StudentTable.java
│   ├── exceptions/          # Custom exception hierarchy
│   │   ├── BaseException.java
│   │   ├── AuthenticationException.java
│   │   ├── ValidationException.java
│   │   ├── NotFoundException.java
│   │   ├── RepositoryException.java
│   │   ├── UploadException.java
│   │   └── AuthorizationException.java
│   ├── search/              # Search capability interfaces
│   │   └── Searchable.java
│   ├── services/            # Business logic layer
│   │   ├── UploadService.java
│   │   └── FileUploadService.java
│   └── sort/                # Sort capability interfaces
│       └── Sortable.java
```

## Data Files

The application automatically creates and manages these JSON files:
- `students.json` - Student data
- `teachers.json` - Teacher data  
- `admins.json` - Admin data
- `courses.json` - Course data
- `uploads/` - Directory for file uploads

## Usage

When you run the application, it will:

1. **Automatically demonstrate OOP features** - Shows inheritance, polymorphism, encapsulation, generics, and exception handling
2. **Display an interactive menu** with options for:
   - Role-based Access Demo
   - Search & Sort Demo
   - File Upload Demo
   - User Management
   - Course Management
   - System Statistics
   - Complete OOP Features Demo

3. **Navigate the menu** by entering the corresponding number

## Example Commands

```bash
# Quick start
mvn exec:java -Dexec.mainClass="sms.app.Main"

# Clean build and run
mvn clean compile exec:java -Dexec.mainClass="sms.app.Main"

# Build for distribution
mvn clean package
```

## Dependencies

The project uses minimal dependencies for maximum compatibility:

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.15.2</version>
</dependency>
```

## Troubleshooting

- **Java version error**: Ensure you have Java 17 or higher installed
- **Maven not found**: Install Maven or use the Maven wrapper if provided
- **JSON parsing errors**: The application will gracefully handle corrupted JSON files by initializing empty repositories
- **Permission errors**: Ensure write permissions for JSON files and uploads directory

## Learning Objectives

This project demonstrates:
- Advanced object-oriented programming concepts
- Design patterns (Repository, Strategy, Template Method, Factory)
- Exception handling best practices
- Generic programming with type safety
- JSON serialization with polymorphic types
- Role-based access control implementation
- Interactive console application development