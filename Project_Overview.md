# 📚 LMS Project - File Overview & OOP Concepts Mapping

This document provides a comprehensive overview of all classes/files in the LMS (Learning Management System) project, their purposes, OOP concepts implemented, and how they connect with other files.

---

## 📁 Package: sms.app (Application Layer)

### 📄 Main.java
- **Purpose**: Entry point of the application. Initializes repositories, demonstrates OOP features, and provides interactive menu system.
- **Concepts**: 
  - Classes & Objects (creates instances of all entities)
  - Polymorphism (User array with different subtype objects)
  - Exception Handling (try-catch-finally blocks)
  - Collections Framework (ArrayList usage)
- **Connections**: 
  - Uses: All repository classes (StudentRepository, TeacherRepository, etc.)
  - Creates: User objects (Student, Teacher, Admin, Principal)
  - Demonstrates: FileUploadService, exception handling

---

## 📁 Package: sms.domain (Domain Entities)

### 📄 User.java (Abstract Base Class)
- **Purpose**: Abstract base class representing all types of users in the LMS system.
- **Concepts**: 
  - Inheritance (base class for Student, Teacher, Admin, Principal)
  - Abstraction (abstract method `getRole()`)
  - Encapsulation (private fields with getters/setters)
  - Polymorphism (overridden methods)
  - Constructors (default and parameterized)
- **Connections**: 
  - Extended by: Student.java, Teacher.java, Admin.java, Principal.java
  - Uses: InputValidator.java, FileUploadService.java
  - Throws: ValidationException, AuthenticationException, UploadException

### 📄 Student.java
- **Purpose**: Represents a student user with course enrollment, assignment viewing, and grade tracking.
- **Concepts**: 
  - Inheritance (extends User)
  - Polymorphism (overrides `getRole()`)
  - Generics (implements Searchable<Course>, Sortable<Course>)
  - Collections Framework (ArrayList for courses, Stream API)
  - Encapsulation (private fields)
- **Connections**: 
  - Extends: User.java
  - Implements: Searchable.java, Sortable.java
  - Works with: Course.java, Department.java, Batch.java
  - Managed by: StudentRepository.java

### 📄 Teacher.java
- **Purpose**: Represents a teacher user who manages assignments, grades, attendance, and student communications.
- **Concepts**: 
  - Inheritance (extends User)
  - Polymorphism (overrides `getRole()`)
  - Generics (implements Searchable<Student>, Sortable<Student>)
  - Collections Framework (ArrayList, Stream API)
- **Connections**: 
  - Extends: User.java
  - Implements: Searchable.java, Sortable.java
  - Works with: Student.java, Course.java, Assignment.java
  - Managed by: TeacherRepository.java

### 📄 Admin.java
- **Purpose**: Represents an admin user who manages students, teachers, courses, and batches.
- **Concepts**: 
  - Inheritance (extends User)
  - Polymorphism (overrides `getRole()`)
  - Generics (implements Searchable<Object>, Sortable<Object>)
  - Collections Framework (ArrayList operations)
- **Connections**: 
  - Extends: User.java
  - Implements: Searchable.java, Sortable.java
  - Manages: Student.java, Teacher.java, Course.java, Batch.java
  - Managed by: AdminRepository.java

### 📄 Principal.java
- **Purpose**: Represents a principal user with administrative oversight capabilities.
- **Concepts**: 
  - Inheritance (extends User)
  - Polymorphism (overrides `getRole()`)
  - Encapsulation (private fields)
- **Connections**: 
  - Extends: User.java
  - Oversees: Admin.java, Teacher.java, Student.java

### 📄 Course.java
- **Purpose**: Represents an academic course with details like course ID, name, credit hours, faculty, and schedule.
- **Concepts**: 
  - Classes & Objects (entity class)
  - Encapsulation (private fields with getters/setters)
  - Constructors (overloaded constructors with chaining)
  - Access Modifiers (public, private)
- **Connections**: 
  - Used by: Student.java, Teacher.java, Admin.java
  - Managed by: CourseRepository.java
  - Related to: Assignment.java, Grade.java

### 📄 Department.java
- **Purpose**: Represents an academic department that groups courses.
- **Concepts**: 
  - Classes & Objects (entity class)
  - Encapsulation (private fields)
- **Connections**: 
  - Used by: Student.java, Course.java

### 📄 Batch.java
- **Purpose**: Represents a student batch/cohort with year range information.
- **Concepts**: 
  - Classes & Objects (entity class)
  - Encapsulation (private fields)
- **Connections**: 
  - Used by: Student.java, Admin.java

### 📄 Assignment.java
- **Purpose**: Represents a course assignment with title, description, and deadline.
- **Concepts**: 
  - Classes & Objects (entity class)
  - Encapsulation (private fields with getters/setters)
- **Connections**: 
  - Created by: Teacher.java
  - Viewed by: Student.java
  - Managed by: AssignmentRepository.java
  - Related to: Submission.java

### 📄 Grade.java
- **Purpose**: Represents a student's grade for a specific course.
- **Concepts**: 
  - Classes & Objects (entity class)
  - Encapsulation (private fields)
- **Connections**: 
  - Created by: Teacher.java
  - Viewed by: Student.java
  - Managed by: GradeRepository.java

### 📄 Message.java
- **Purpose**: Represents internal communication messages between users.
- **Concepts**: 
  - Classes & Objects (entity class)
  - Encapsulation (private fields)
- **Connections**: 
  - Used by: Student.java, Teacher.java, Admin.java
  - Managed by: MessageRepository.java

### 📄 Attendance.java
- **Purpose**: Represents attendance records for students.
- **Concepts**: 
  - Classes & Objects (entity class)
  - Encapsulation (private fields)
- **Connections**: 
  - Managed by: Teacher.java
  - Related to: Student.java, Course.java

### 📄 Submission.java
- **Purpose**: Represents a student's assignment submission.
- **Concepts**: 
  - Classes & Objects (entity class)
  - Encapsulation (private fields)
- **Connections**: 
  - Created by: Student.java
  - Reviewed by: Teacher.java
  - Managed by: SubmissionRepository.java
  - Related to: Assignment.java

### 📄 UploadMetadata.java
- **Purpose**: Stores metadata for uploaded files (filename, path, upload date, visibility).
- **Concepts**: 
  - Classes & Objects (entity class)
  - Encapsulation (private fields)
  - Collections Framework (List for visibility control)
- **Connections**: 
  - Used by: FileUploadService.java
  - Managed by: UploadRepository.java

### 📄 StudentTable.java
- **Purpose**: Helper class for displaying student information in tabular format.
- **Concepts**: 
  - Classes & Objects (utility class)
  - Encapsulation (private fields)
- **Connections**: 
  - Uses: Student.java data

---

## 📁 Package: sms.data (Data Access Layer / Repository Pattern)

### 📄 Repository.java (Generic Interface)
- **Purpose**: Generic interface defining CRUD operations for any entity type.
- **Concepts**: 
  - Generics (Repository<T>)
  - Abstraction (interface without implementation)
  - Exception Handling (throws custom exceptions)
- **Connections**: 
  - Implemented by: StudentRepository, TeacherRepository, AdminRepository, CourseRepository, etc.
  - Defines contract for: add(), update(), delete(), getAll(), find(), sort()

### 📄 StudentRepository.java
- **Purpose**: Manages CRUD operations and JSON persistence for Student entities.
- **Concepts**: 
  - Generics (implements Repository<Student>)
  - Collections Framework (ArrayList)
  - File Handling (JSON read/write with ObjectMapper)
  - Exception Handling (try-catch, custom exceptions)
- **Connections**: 
  - Implements: Repository.java
  - Manages: Student.java entities
  - File: students.json
  - Uses: ObjectMapper for JSON serialization

### 📄 TeacherRepository.java
- **Purpose**: Manages CRUD operations and JSON persistence for Teacher entities.
- **Concepts**: 
  - Generics (implements Repository<Teacher>)
  - Collections Framework (ArrayList)
  - File Handling (JSON operations)
- **Connections**: 
  - Implements: Repository.java
  - Manages: Teacher.java entities
  - File: teachers.json

### 📄 AdminRepository.java
- **Purpose**: Manages CRUD operations and JSON persistence for Admin entities.
- **Concepts**: 
  - Generics (implements Repository<Admin>)
  - Collections Framework (ArrayList)
  - File Handling (JSON operations)
- **Connections**: 
  - Implements: Repository.java
  - Manages: Admin.java entities
  - File: admins.json

### 📄 CourseRepository.java
- **Purpose**: Manages CRUD operations and JSON persistence for Course entities.
- **Concepts**: 
  - Generics (implements Repository<Course>)
  - Collections Framework (ArrayList, Stream API)
  - File Handling (JSON operations)
- **Connections**: 
  - Implements: Repository.java
  - Manages: Course.java entities
  - File: courses.json

### 📄 AssignmentRepository.java
- **Purpose**: Manages CRUD operations and JSON persistence for Assignment entities.
- **Concepts**: 
  - Generics (implements Repository<Assignment>)
  - File Handling (JSON operations)
- **Connections**: 
  - Implements: Repository.java
  - Manages: Assignment.java entities
  - File: assignments.json

### 📄 GradeRepository.java
- **Purpose**: Manages CRUD operations and JSON persistence for Grade entities.
- **Concepts**: 
  - Generics (implements Repository<Grade>)
  - File Handling (JSON operations)
- **Connections**: 
  - Implements: Repository.java
  - Manages: Grade.java entities
  - File: grades.json

### 📄 MessageRepository.java
- **Purpose**: Manages CRUD operations and JSON persistence for Message entities.
- **Concepts**: 
  - Generics (implements Repository<Message>)
  - File Handling (JSON operations)
- **Connections**: 
  - Implements: Repository.java
  - Manages: Message.java entities
  - File: messages.json

### 📄 SubmissionRepository.java
- **Purpose**: Manages CRUD operations and JSON persistence for Submission entities.
- **Concepts**: 
  - Generics (implements Repository<Submission>)
  - File Handling (JSON operations)
- **Connections**: 
  - Implements: Repository.java
  - Manages: Submission.java entities
  - File: submissions.json

### 📄 UploadRepository.java
- **Purpose**: Manages CRUD operations and JSON persistence for file upload metadata.
- **Concepts**: 
  - Generics (implements Repository<UploadMetadata>)
  - File Handling (JSON operations)
- **Connections**: 
  - Implements: Repository.java
  - Manages: UploadMetadata.java entities
  - File: uploads.json

---

## 📁 Package: sms.exceptions (Custom Exception Hierarchy)

### 📄 BaseException.java (Abstract Exception Class)
- **Purpose**: Abstract base class for all custom exceptions in the system.
- **Concepts**: 
  - Inheritance (extends Exception)
  - Abstraction (abstract base for specific exceptions)
  - Exception Handling (custom exception with timestamp and logging)
- **Connections**: 
  - Extended by: ValidationException, NotFoundException, RepositoryException, AuthenticationException, AuthorizationException, UploadException
  - Provides: log() method, timestamp tracking

### 📄 ValidationException.java
- **Purpose**: Thrown when input validation fails (e.g., invalid email, empty fields).
- **Concepts**: 
  - Inheritance (extends BaseException)
  - Exception Handling (custom exception with field details)
- **Connections**: 
  - Extends: BaseException.java
  - Thrown by: InputValidator.java, User.java, Repository classes
  - Caught in: Main.java, service classes

### 📄 NotFoundException.java
- **Purpose**: Thrown when a requested entity is not found in the repository.
- **Concepts**: 
  - Inheritance (extends BaseException)
  - Exception Handling (with entity type and search criteria)
- **Connections**: 
  - Extends: BaseException.java
  - Thrown by: Repository classes
  - Caught in: Main.java, domain classes

### 📄 RepositoryException.java
- **Purpose**: Thrown when repository operations fail (e.g., file read/write errors).
- **Concepts**: 
  - Inheritance (extends BaseException)
  - Exception Handling (with operation and entity type details)
- **Connections**: 
  - Extends: BaseException.java
  - Thrown by: All Repository classes
  - Caught in: Main.java

### 📄 AuthenticationException.java
- **Purpose**: Thrown when user authentication fails (login errors).
- **Concepts**: 
  - Inheritance (extends BaseException)
  - Exception Handling (with username and error code)
- **Connections**: 
  - Extends: BaseException.java
  - Thrown by: User.java login() method
  - Caught in: Main.java

### 📄 AuthorizationException.java
- **Purpose**: Thrown when a user lacks required permissions.
- **Concepts**: 
  - Inheritance (extends BaseException)
  - Exception Handling (with user role and required permission)
- **Connections**: 
  - Extends: BaseException.java
  - Thrown by: Admin.java, Principal.java

### 📄 UploadException.java
- **Purpose**: Thrown when file upload operations fail.
- **Concepts**: 
  - Inheritance (extends BaseException)
  - Exception Handling (with filename and file type)
- **Connections**: 
  - Extends: BaseException.java
  - Thrown by: FileUploadService.java
  - Caught in: User.java, Main.java

---

## 📁 Package: sms.services (Business Logic Layer)

### 📄 UploadService.java (Generic Interface)
- **Purpose**: Generic interface defining file upload operations.
- **Concepts**: 
  - Generics (UploadService<T>)
  - Abstraction (interface)
  - Exception Handling (defines exceptions to throw)
- **Connections**: 
  - Implemented by: FileUploadService.java
  - Defines: validate(), store(), saveMetadata(), getUploadDirectory()

### 📄 FileUploadService.java
- **Purpose**: Handles file upload operations, validation, storage, and metadata management with role-based visibility.
- **Concepts**: 
  - Generics (implements UploadService<File>)
  - File Handling (file storage, directory creation)
  - Exception Handling (try-catch with custom exceptions)
  - Collections Framework (HashMap for metadata, ArrayList)
  - Encapsulation (private fields and methods)
- **Connections**: 
  - Implements: UploadService.java
  - Uses: UploadRepository.java, UploadMetadata.java
  - Used by: User.java, Student.java, Teacher.java, Admin.java
  - Directory: uploads/

---

## 📁 Package: sms.search (Search Interface)

### 📄 Searchable.java (Generic Interface)
- **Purpose**: Generic interface for implementing search functionality on different entity types.
- **Concepts**: 
  - Generics (Searchable<T>)
  - Abstraction (interface without implementation)
- **Connections**: 
  - Implemented by: Student.java (Searchable<Course>), Teacher.java (Searchable<Student>), Admin.java (Searchable<Object>)
  - Defines: search(String criteria) method

---

## 📁 Package: sms.sort (Sort Interface)

### 📄 Sortable.java (Generic Interface)
- **Purpose**: Generic interface for implementing sort functionality on different entity types.
- **Concepts**: 
  - Generics (Sortable<T>)
  - Abstraction (interface without implementation)
- **Connections**: 
  - Implemented by: Student.java (Sortable<Course>), Teacher.java (Sortable<Student>), Admin.java (Sortable<Object>)
  - Defines: sort(String criteria) method

---

## 📁 Package: sms.validation (Input Validation)

### 📄 InputValidator.java
- **Purpose**: Centralized input validation utility for all user inputs (name, email, username, password, IDs).
- **Concepts**: 
  - Encapsulation (validation logic encapsulated in one class)
  - Exception Handling (throws ValidationException with field details)
  - Access Modifiers (public static methods)
- **Connections**: 
  - Used by: User.java, Student.java, Teacher.java, Admin.java, Main.java
  - Throws: ValidationException.java
  - Validates: Names, emails, usernames, passwords, numeric IDs, course data

---

## 🔗 Key Relationships Summary

### Inheritance Hierarchy:
```
Exception
  └── BaseException (abstract)
        ├── ValidationException
        ├── NotFoundException
        ├── RepositoryException
        ├── AuthenticationException
        ├── AuthorizationException
        └── UploadException

User (abstract)
  ├── Student
  ├── Teacher
  ├── Admin
  └── Principal
```

### Interface Implementation:
```
Repository<T>
  ├── StudentRepository (Repository<Student>)
  ├── TeacherRepository (Repository<Teacher>)
  ├── AdminRepository (Repository<Admin>)
  ├── CourseRepository (Repository<Course>)
  ├── AssignmentRepository (Repository<Assignment>)
  ├── GradeRepository (Repository<Grade>)
  ├── MessageRepository (Repository<Message>)
  ├── SubmissionRepository (Repository<Submission>)
  └── UploadRepository (Repository<UploadMetadata>)

Searchable<T> & Sortable<T>
  ├── Student (implements Searchable<Course>, Sortable<Course>)
  ├── Teacher (implements Searchable<Student>, Sortable<Student>)
  └── Admin (implements Searchable<Object>, Sortable<Object>)

UploadService<T>
  └── FileUploadService (implements UploadService<File>)
```

### Data Flow:
```
Main.java (Entry Point)
  ↓
Creates and uses User objects (Student, Teacher, Admin, Principal)
  ↓
User objects interact with Repository classes
  ↓
Repository classes perform CRUD operations
  ↓
Data persisted to JSON files (students.json, teachers.json, etc.)
  ↓
FileUploadService manages file uploads
  ↓
UploadRepository stores metadata in uploads.json
```

---

## 📊 OOP Concepts Coverage by Package

| Package | Concepts Demonstrated |
|---------|----------------------|
| **sms.app** | Classes & Objects, Polymorphism, Collections, Exception Handling |
| **sms.domain** | Inheritance, Encapsulation, Polymorphism, Abstraction, Constructors, Generics |
| **sms.data** | Generics, Collections, File Handling, Exception Handling |
| **sms.exceptions** | Inheritance, Abstraction, Exception Handling |
| **sms.services** | Generics, Abstraction, File Handling, Collections, Encapsulation |
| **sms.search** | Generics, Abstraction |
| **sms.sort** | Generics, Abstraction |
| **sms.validation** | Encapsulation, Exception Handling, Access Modifiers |

---

## 📝 Notes

- **JSON Files**: students.json, teachers.json, admins.json, courses.json, messages.json, assignments.json, grades.json, submissions.json, uploads.json
- **Upload Directory**: uploads/ (stores uploaded files)
- **Packages**: Total 8 packages organizing code by functionality
- **Total Classes**: 38 Java files demonstrating comprehensive OOP principles
- **Design Patterns**: Repository Pattern, Strategy Pattern (via interfaces), Template Method (BaseException)

---

**This LMS project demonstrates all 12 required OOP concepts with real-world implementation and proper separation of concerns across multiple packages.**
