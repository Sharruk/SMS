# Student Management System (SMS)

## Overview

This is a complete Java-based Student Management System built with a layered architecture, following object-oriented programming principles. The system provides a console-based interface for managing students, courses, and enrollment processes with JSON file persistence. It demonstrates comprehensive OOP concepts including inheritance, polymorphism, encapsulation, generics, and custom exception handling.

## User Preferences

Preferred communication style: Simple, everyday language.

## System Architecture

### Technology Stack
- **Java 17+**: Core programming language
- **Maven**: Build system and dependency management
- **Jackson Databind**: JSON serialization/deserialization
- **Console Interface**: Text-based user interaction

### Package Structure
- **sms.domain**: Core entities (User, Student, Teacher, Course)
- **sms.exceptions**: Custom exception classes (AuthenticationException, RepositoryException)
- **sms.data**: Data access layer with generic Repository interface and implementations
- **sms.services**: Business logic layer (AdminService, UploadService)
- **sms.app**: Main application entry point with console interface

### Core Entities
**User (Abstract Class)**: Base class with common user attributes and abstract methods for polymorphism
**Student**: Extends User with academic-specific fields (GPA, major, year, enrolled courses)
**Teacher**: Extends User with teaching-specific fields (department, specialization, salary, teaching courses)
**Course**: Standalone entity with enrollment management and capacity controls

### Data Persistence
- **JSON Files**: students.json, courses.json for data storage
- **Generic Repository Pattern**: Type-safe CRUD operations using Repository<T> interface
- **Jackson Integration**: Automatic JSON serialization with proper type handling

### Key Features
- **OOP Demonstration**: Critical demonstrateOopFeatures() method showcasing all OOP principles
- **Console Menu System**: Interactive text-based interface with multiple management options
- **Error Handling**: Comprehensive custom exception handling with try-catch-finally blocks
- **Data Management**: Upload/download capabilities and sample data generation

## Recent Changes

### September 25, 2025
- **Complete System Implementation**: Built entire Java SMS with layered architecture
- **OOP Features Integration**: Implemented comprehensive OOP demonstration method
- **Security Enhancements**: Added @JsonIgnore annotations for password fields
- **Jackson Configuration**: Fixed deserialization issues with FAIL_ON_UNKNOWN_PROPERTIES
- **Maven Setup**: Configured build system with Java 17 and Jackson dependencies
- **Workflow Configuration**: Set up console-based execution workflow

## External Dependencies

### Build System
- **Maven 3.11+**: Project build and dependency management
- **Java 17**: Target JDK version

### Libraries
- **Jackson Databind 2.15.2**: JSON processing and object mapping
- **Jackson Annotations**: Type handling and serialization control

### Runtime Environment
- **Console/Terminal**: Text-based user interface
- **File System**: JSON file persistence (students.json, courses.json)