# Learning Management System (LMS)

## Overview

This is a comprehensive Java-based Learning Management System built with a layered architecture, following object-oriented programming principles and implementing a complete PlantUML class diagram specification. The system provides a console-based interface for managing students, courses, teachers, admins, and principals with JSON file persistence. It demonstrates comprehensive OOP concepts including inheritance, polymorphism, encapsulation, generics, custom exception handling, search/sort interfaces, upload services, and role-based access control.

## User Preferences

Preferred communication style: Simple, everyday language.

## System Architecture

### Technology Stack
- **Java 17+**: Core programming language
- **Maven**: Build system and dependency management
- **Jackson Databind**: JSON serialization/deserialization with polymorphic type handling
- **Console Interface**: Text-based user interaction with comprehensive menu system

### Package Structure
- **sms.domain**: Core entities (User abstract class, Admin, Teacher, Student, Principal, Course, Department, Batch, Attendance, StudentTable)
- **sms.exceptions**: Complete exception hierarchy (BaseException, AuthenticationException, ValidationException, NotFoundException, RepositoryException, UploadException, AuthorizationException)
- **sms.data**: Data access layer with generic Repository<T> interface and implementations (StudentRepository, TeacherRepository, AdminRepository, CourseRepository)
- **sms.services**: Business logic layer (UploadService<T> interface, FileUploadService implementation)
- **sms.search**: Search capability interfaces (Searchable<T>)
- **sms.sort**: Sort capability interfaces (Sortable<T>)
- **sms.app**: Main application entry point with comprehensive console interface and OOP demonstration

### Core Entities
**User (Abstract Class)**: Base class with common user attributes, abstract getRole() method, login/logout/upload methods
**Admin**: Extends User with comprehensive management capabilities (manage students, teachers, courses, batches), implements Searchable<Object> and Sortable<Object>
**Teacher**: Extends User with teaching-specific fields and methods (create assignments, mark attendance, view students), implements Searchable<Student> and Sortable<Student>
**Student**: Extends User with academic-specific fields (department, batch, courses), implements Searchable<Course> and Sortable<Course>
**Principal**: Extends User with administrative oversight (manage admins, appoint teachers, assign courses, view reports)
**Course**: Comprehensive course entity with scheduling information (courseId, courseName, creditHours, facultyName, classDays, classTimes, classDates)
**Department**: Academic department with name and courses
**Batch**: Student cohort with year range
**Attendance**: Attendance tracking entity
**StudentTable**: Student data table entity

### Data Persistence
- **JSON Files**: students.json, teachers.json, admins.json, courses.json for data storage
- **Generic Repository Pattern**: Type-safe CRUD operations using Repository<T> interface
- **Jackson Integration**: Polymorphic JSON serialization with @JsonTypeInfo and @JsonTypeName annotations
- **Error Recovery**: Graceful handling of JSON parsing errors with empty repository initialization

### Key Features
- **Comprehensive OOP Demonstration**: Critical demonstrateOopFeatures() method showcasing all OOP principles with inheritance hierarchy, polymorphism, encapsulation, generics, and exception handling
- **Role-based Access Control**: Each user role has specific capabilities and access levels
- **Search and Sort Interfaces**: Role-based search and sort capabilities (Admin searches everything, Teacher searches students, Student searches courses)
- **Upload Service**: File upload with validation, storage, and metadata management
- **Interactive Console Menu**: Complete menu system for testing all features
- **Exception Hierarchy**: Comprehensive custom exception handling with structured metadata and logging
- **Generic Repository**: Type-safe repository pattern with CRUD operations

## Recent Changes

### October 3, 2025 - Fresh GitHub Import to Replit
- **Project Import**: Successfully imported Learning Management System from GitHub repository
- **Environment Setup**: Java 19 (GraalVM 22.3.1) and Maven 3.9.9 configured automatically
- **Dependencies**: All Maven dependencies downloaded and compiled successfully (Jackson Databind 2.15.2)
- **Workflow Configuration**: Console application workflow set up with proper output type
- **Deployment Setup**: VM deployment configured for console-based application
- **Application Running**: LMS started successfully in console mode
- **Data Validation**: All JSON repositories loading correctly (2 admins, 3 courses, 0 students, 0 teachers)
- **OOP Demo**: Comprehensive OOP features demonstration runs automatically on startup
- **Interactive Menu**: Main menu system ready for user interaction
- **Ready for Use**: Application fully functional in Replit environment

### October 3, 2025 (Latest Update)
- **Admin Menu System**: Complete interactive Admin menu with repository-based operations
  - Admin selection from repository (not hard-coded)
  - Student Management: Register, Update, Remove, Search, Enroll in Course (all via studentRepository)
  - Teacher Management: Register, Update, Remove, Search, Assign to Course (all via teacherRepository)
  - Course Management: Create, Update, Remove, Assign Teacher, Enroll Students (all via courseRepository)
  - Reports & Statistics: Students per course, teacher workload, course enrollment analytics
- **Messaging System**: Principal-to-Admin/Teacher messaging with inbox
  - Message domain class with timestamp, read status, sender/receiver info
  - MessageRepository for messages.json persistence
  - Principal can send messages to any admin/teacher in the system
  - Admin can view messages in inbox and mark as read
  - All messages persist to messages.json with full metadata
- **Repository-First Architecture**: All CRUD handlers directly use repositories (studentRepository, teacherRepository, courseRepository, messageRepository)
- **Admin Authentication**: runAdminMenu loads actual admin from AdminRepository by selection
- **Data Consistency**: All operations persist immediately to JSON files

### October 3, 2025 (Earlier)
- **Principal Role Enhancement**: Enhanced Principal class with comprehensive menu-driven interface for managing system entities
- **Admin Management**: Add/View/Remove admins with full CRUD operations persisting to JSON
- **Teacher Management**: Appoint/View/Remove teachers with repository integration
- **Course Management**: Create/Assign/View/Remove courses with teacher assignment capabilities
- **Student Viewing**: View all registered students from repository
- **Comprehensive Reports**: Enhanced reporting system showing statistics from all four repositories
- **Repository Integration**: All interactive menu operations work directly with repositories (bypass local lists)
- **Data Integrity**: Fixed OOP demo to prevent persistent storage pollution (demo uses in-memory objects only)
- **Clean Persistence**: All CRUD operations properly persist to JSON files without duplication

### September 25, 2025
- **Complete LMS Implementation**: Built comprehensive Java LMS based on PlantUML specification
- **Full Domain Model**: Implemented all domain classes with proper inheritance hierarchy
- **Exception Hierarchy**: Created complete exception hierarchy with BaseException and all specific exceptions
- **Repository Layer**: Implemented generic Repository<T> interface with type-safe CRUD operations
- **Search/Sort Interfaces**: Added role-based search and sort capabilities
- **Upload Service**: Integrated file upload service with validation and error handling
- **Role-based Access**: Implemented Principal, Admin, Teacher, Student roles with specific capabilities
- **OOP Features**: Comprehensive demonstration of inheritance, polymorphism, encapsulation, generics, exception handling
- **JSON Persistence**: Enhanced Jackson configuration for polymorphic serialization
- **Interactive Console**: Complete menu system for feature demonstration
- **Maven Configuration**: Updated with proper Java 17 and Jackson dependencies
- **System Testing**: Successful compilation and execution with all features working

## External Dependencies

### Build System
- **Maven 3.11+**: Project build and dependency management
- **Java 17**: Target JDK version

### Libraries
- **Jackson Databind 2.15.2**: JSON processing and polymorphic object mapping
- **Jackson Annotations**: Type handling and serialization control

### Runtime Environment
- **Console/Terminal**: Interactive text-based user interface
- **File System**: JSON file persistence with error recovery
- **Upload Directory**: File upload storage and management

## Architecture Highlights

### OOP Principles Demonstrated
1. **Inheritance**: Complete User hierarchy with Admin, Teacher, Student, Principal
2. **Polymorphism**: Overridden methods called via User reference
3. **Encapsulation**: Private fields with public getters/setters
4. **Generics**: Repository<T> interface with type safety
5. **Exception Handling**: Custom exception hierarchy with try-catch-finally blocks

### Design Patterns
- **Repository Pattern**: Generic Repository<T> interface for data access
- **Strategy Pattern**: Role-based access with different capabilities per user type
- **Template Method**: Abstract User class with concrete implementations
- **Factory Pattern**: Jackson polymorphic deserialization

### Security Features
- **Password Protection**: @JsonIgnore annotations prevent password serialization
- **File Validation**: Upload service validates file types and sizes
- **Role-based Access**: Each user type has restricted access to relevant data
- **Exception Logging**: Comprehensive error tracking and logging

## System Capabilities

### Principal Access
- Add and manage admins
- Appoint teachers
- Assign courses to teachers
- View system reports

### Admin Access
- Register and manage students
- Manage teachers and courses
- Assign batches and courses
- Set default passwords
- Verify admissions
- Search and sort all system entities

### Teacher Access
- Create assignments
- Mark attendance
- View assigned students
- View submitted assignments
- Change password
- Search and sort assigned students

### Student Access
- View assignments and grades
- Mark own attendance
- View attendance records
- Change password
- Search and sort enrolled courses

## Usage Instructions

### Running in Replit
The application is configured to run automatically in Replit:
1. Click the "Run" button at the top of the Replit interface
2. The workflow "LMS Application" will start automatically
3. The console will show the OOP features demonstration followed by an interactive menu
4. Use the console to interact with the menu options

### Running Manually (Terminal)
1. **Compile**: `mvn clean compile`
2. **Run**: `mvn exec:java -Dexec.mainClass="sms.app.Main"`
3. **Features**: The system will automatically demonstrate all OOP features and then provide an interactive menu
4. **Data Files**: JSON files are created automatically for data persistence
5. **Upload Directory**: Files are uploaded to the uploads/ directory

### Important Notes for Replit
- The application is a console-based interactive system (not a web application)
- JSON data files must include a "type" field for polymorphic deserialization (e.g., "type": "admin", "type": "student")
- The workflow is configured to use Maven exec plugin for optimal performance

## Testing and Quality Assurance

- **Compilation**: Successfully compiles with Java 17 and Maven
- **Execution**: Runs without errors and demonstrates all features
- **OOP Compliance**: All OOP principles properly implemented and demonstrated
- **Exception Handling**: Comprehensive exception hierarchy with proper error recovery
- **Role-based Security**: Each user type has appropriate access restrictions
- **Data Persistence**: JSON serialization/deserialization working correctly
- **Interactive Interface**: Complete console menu system for feature testing