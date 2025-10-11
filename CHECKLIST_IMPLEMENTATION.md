# CHECKLIST_IMPLEMENTATION.md

## Why Maven is Used

- Maven (Apache Maven) is a build automation and project management tool.  
- It simplifies compiling, packaging, running, and managing dependencies — so I don't need to manually compile multiple `.java` files.  
- Instead of remembering many commands, I can use:
  `mvn compile` and `mvn exec:java -Dexec.mainClass="sms.app.Main"`.
- It ensures the same build works on every machine (clean and portable).  
- Dependencies (like external libraries) are managed through `pom.xml` instead of manual jar handling.

---

## ✅ Main.java Refactoring - COMPLETED (October 2025)

### 📊 Refactoring Achievement

The file `Main.java` has been **successfully refactored** from **3,811 lines to 433 lines** — an **88.6% reduction** in code size!

### 🔍 New Modular Structure

| Component | Lines | Description |
|-----------|-------|-------------|
| **Main.java** | **433** | Entry point with initialization and orchestration only |
| System Initialization | ~50 | Loads data, sets up repositories, and initializes the LMS |
| OOP Demonstrations | ~250 | Demonstrates inheritance, polymorphism, encapsulation, generics, and custom exceptions |
| Main Menu System | ~80 | Handles main menu navigation and role selection |
| Helper Methods | ~50 | Displays statistics, demos, and utility functions |
| **PrincipalMenuHandler.java** | ~800 | All Principal role menu operations and workflows |
| **AdminMenuHandler.java** | ~1,000 | All Admin role menu operations and workflows |
| **TeacherMenuHandler.java** | ~800 | All Teacher role menu operations and workflows |
| **StudentMenuHandler.java** | ~700 | All Student role menu operations and workflows |

### 🏗️ Implemented Architecture

The refactored design follows **Clean Architecture** and **Separation of Concerns** principles:

```
sms/
├── app/
│   ├── Main.java (433 lines)         → Entry point, initialization, OOP demos
│   └── menus/                        → Modular menu handler package
│       ├── PrincipalMenuHandler.java → Principal operations
│       ├── AdminMenuHandler.java     → Admin operations
│       ├── TeacherMenuHandler.java   → Teacher operations
│       └── StudentMenuHandler.java   → Student operations
```

### ✨ Benefits Achieved

- ✅ **Separation of Concerns**: Each role has its own dedicated handler class
- ✅ **Type-safe Constructor Injection**: Dependencies properly managed
- ✅ **Maintainability**: Easy to locate and modify role-specific functionality
- ✅ **Scalability**: Simple to add new roles or features without touching Main.java
- ✅ **Testability**: Individual menu handlers can be tested independently
- ✅ **OOP Preservation**: All OOP demonstrations remain intact in Main.java
- ✅ **Zero Regressions**: All features working correctly after refactoring
- ✅ **Architect Approved**: Passed comprehensive code review

### 🎯 Enhanced Explanation (Viva-Ready)

> **"Mam, we initially had Main.java with 3,811 lines containing all menu logic for all four user roles. To follow clean architecture principles, we refactored it to just 433 lines by extracting role-specific menu operations into dedicated handler classes — one for each role (Principal, Admin, Teacher, Student). Now Main.java focuses only on system initialization and OOP demonstrations, while delegating user interactions to appropriate menu handlers. This modular design improves maintainability, testability, and follows the Separation of Concerns principle."**

### 📈 Refactoring Metrics

- **Before**: Main.java = 3,811 lines (monolithic)
- **After**: Main.java = 433 lines + 4 menu handlers (modular)
- **Reduction**: 88.6% decrease in Main.java size
- **Build Status**: ✅ Maven BUILD SUCCESS
- **Testing**: ✅ All features verified and working
- **Review Status**: ✅ PASS (Architect approved)

---

## Classes & Objects

| Concept | File Name | Class/Method | How it is implemented | Why it is used | What role it plays | Other files using it |
|---------|-----------|--------------|------------------------|----------------|---------------------|-----------------------|
| Classes & Objects | User.java | User class | Abstract class representing a user with fields like userId, name, email, username, password | To serve as base class for all user types in the system | Parent class for inheritance hierarchy | Admin.java, Teacher.java, Student.java, Principal.java |
| Classes & Objects | Admin.java | Admin class | Concrete class extending User with specific admin fields and methods | To represent admin users with student/teacher/course management capabilities | Manages students, teachers, courses, and batches | Main.java, AdminRepository.java |
| Classes & Objects | Student.java | Student class | Concrete class extending User with student-specific fields like courses, batch, dept | To represent student users in the system | Handles student operations like viewing assignments, grades, attendance | Main.java, StudentRepository.java |
| Classes & Objects | Teacher.java | Teacher class | Concrete class extending User with teacher-specific fields like courses, students, batches | To represent teacher users who teach courses and manage students | Handles teacher operations like creating assignments, marking attendance | Main.java, TeacherRepository.java |
| Classes & Objects | Course.java | Course class | Regular class with fields courseId, courseName, creditHours, facultyName, etc. | To represent courses in the LMS | Stores course information and schedules | Student.java, Teacher.java, Admin.java |

### 🧾 Code Snippet (User.java):
```java
public abstract class User {
    private int userId;
    private String name;
    private String email;
    private String username;
    private String password;
    
    public User() {}
    
    public User(int userId, String name, String email, String username, String password) {
        this.userId = userId;
        this.name = name;
        // ... other initializations
    }
}
```

### 🧾 Code Snippet (Course.java):
```java
public class Course {
    private String courseId;
    private String courseName;
    private int creditHours;
    private String facultyName;
    
    public Course() {}
    
    public Course(String courseId, String courseName, int creditHours) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.creditHours = creditHours;
    }
}
```

---

## Encapsulation

| Concept | File Name | Class/Method | How it is implemented | Why it is used | What role it plays | Other files using it |
|---------|-----------|--------------|------------------------|----------------|---------------------|-----------------------|
| Encapsulation | User.java | Private fields with getters/setters | All fields are private, accessed via public getters and setters | To hide internal data and provide controlled access | Protects data integrity and allows validation during access | All subclasses (Admin, Teacher, Student, Principal) |
| Encapsulation | Admin.java | getManagedStudents(), setManagedStudents() | Private List<Student> managedStudents with public accessor methods | To control access to admin's managed student list | Ensures managed students can only be modified through controlled methods | Main.java |
| Encapsulation | Course.java | getCourseId(), setCourseId() | Private fields with public getters and setters | To protect course data from direct modification | Allows validation and business logic during field updates | Teacher.java, Student.java |
| Encapsulation | BaseException.java | Private message and timestamp | Private fields with public getters, encapsulated error details | To protect exception data from external modification | Ensures exception information is immutable after creation | ValidationException.java, NotFoundException.java |

### 🧾 Code Snippet (User.java):
```java
// Private fields (Encapsulation)
private int userId;
private String name;
private String email;

// Public getters and setters
public int getUserId() {
    return userId;
}

public void setUserId(int userId) {
    this.userId = userId;
}
```

---

## Inheritance

| Concept | File Name | Class/Method | How it is implemented | Why it is used | What role it plays | Other files using it |
|---------|-----------|--------------|------------------------|----------------|---------------------|-----------------------|
| Inheritance | User.java → Admin.java | extends User | Admin class extends User class using "extends" keyword | To inherit common user properties and methods | Establishes "IS-A" relationship where Admin IS-A User | Main.java demonstrates inheritance |
| Inheritance | User.java → Teacher.java | extends User | Teacher class extends User class | To reuse User's fields and methods for teacher-specific functionality | Teacher inherits userId, name, email from User | Main.java demonstrates inheritance |
| Inheritance | User.java → Student.java | extends User | Student class extends User class | To inherit user base functionality for students | Student gets login(), logout() methods from User | Main.java demonstrates inheritance |
| Inheritance | User.java → Principal.java | extends User | Principal class extends User class | To inherit user capabilities for principal role | Principal inherits all User properties and behaviors | Main.java demonstrates inheritance |
| Inheritance | BaseException.java → ValidationException.java | extends BaseException | ValidationException extends BaseException | To inherit common exception functionality | Creates exception hierarchy for validation errors | InputValidator.java, Admin.java |

### 🧾 Code Snippet (Admin.java):
```java
public class Admin extends User implements Searchable<Object>, Sortable<Object> {
    private List<Student> managedStudents;
    
    public Admin(int userId, String name, String email, String username, String password) 
            throws ValidationException {
        super(userId, name, email, username, password); // Call parent constructor
        this.managedStudents = new ArrayList<>();
    }
}
```

### 🧾 Code Snippet (ValidationException.java):
```java
public class ValidationException extends BaseException {
    private String fieldName;
    private String invalidValue;

    public ValidationException(String message, String fieldName, String invalidValue) {
        super(message); // Calls parent BaseException constructor
        this.fieldName = fieldName;
        this.invalidValue = invalidValue;
    }
}
```

---

## Polymorphism

| Concept | File Name | Class/Method | How it is implemented | Why it is used | What role it plays | Other files using it |
|---------|-----------|--------------|------------------------|----------------|---------------------|-----------------------|
| Polymorphism | User.java | abstract String getRole() | Abstract method in User, overridden in Admin, Teacher, Student, Principal | To provide role-specific implementation via same method signature | Allows different user types to return their specific roles | Main.java calls polymorphically |
| Polymorphism | Admin.java | @Override getRole() | Returns "ADMIN" - overrides User's abstract method | To provide Admin-specific role identification | Identifies user type at runtime | Main.java demonstrates this |
| Polymorphism | Main.java | demonstrateOopFeatures() | User[] users array holding different user types, calling getRole() | To demonstrate runtime polymorphism with User reference | Shows polymorphic method calls on User array | All User subclasses |
| Polymorphism | Student.java | @Override toString() | Overrides Object's toString() method with student-specific format | To provide custom string representation | Returns formatted student information | Repository classes, Main.java |

### 🧾 Code Snippet (Main.java - Polymorphism):
```java
// Polymorphic array
User[] users = {admin, teacher, student, principal};

for (User user : users) {
    System.out.println("Role: " + user.getRole()); // Polymorphic call
    user.login(); // Polymorphic method call
    user.logout(); // Polymorphic method call
}
```

### 🧾 Code Snippet (Admin.java):
```java
@Override
public String getRole() {
    return "ADMIN";
}
```

---

## Abstraction

| Concept | File Name | Class/Method | How it is implemented | Why it is used | What role it plays | Other files using it |
|---------|-----------|--------------|------------------------|----------------|---------------------|-----------------------|
| Abstraction | User.java | abstract class User | Declared with "abstract" keyword, cannot be instantiated | To define common structure without implementation | Provides template for all user types | Admin.java, Teacher.java, Student.java, Principal.java |
| Abstraction | User.java | abstract String getRole() | Abstract method with no body | To force subclasses to provide role implementation | Ensures every user type defines its role | All User subclasses |
| Abstraction | Searchable.java | interface Searchable<T> | Interface with search() method signature | To define search contract for implementing classes | Enforces search capability across different entities | Admin.java, Teacher.java, Student.java |
| Abstraction | Sortable.java | interface Sortable<T> | Interface with sort() method signature | To define sorting contract | Ensures consistent sorting behavior | Admin.java, Teacher.java, Student.java |
| Abstraction | Repository.java | interface Repository<T> | Generic interface with CRUD method signatures | To define data access contract | Abstracts data persistence operations | StudentRepository.java, TeacherRepository.java, AdminRepository.java |
| Abstraction | BaseException.java | abstract class BaseException | Abstract exception class extending Exception | To provide common exception functionality | Base for custom exception hierarchy | ValidationException.java, NotFoundException.java |

### 🧾 Code Snippet (User.java):
```java
public abstract class User {
    // Abstract method - no implementation
    public abstract String getRole();
    
    // Concrete method
    public void login() throws AuthenticationException {
        System.out.println("User " + username + " logged in");
    }
}
```

### 🧾 Code Snippet (Searchable.java):
```java
public interface Searchable<T> {
    /**
     * Search for entities based on provided criteria
     */
    List<T> search(String criteria);
}
```

---

## Constructors

| Concept | File Name | Class/Method | How it is implemented | Why it is used | What role it plays | Other files using it |
|---------|-----------|--------------|------------------------|----------------|---------------------|-----------------------|
| Default Constructor | User.java | public User() | No-argument constructor for Jackson deserialization | Required for JSON deserialization | Allows object creation without parameters | All Repository classes |
| Parameterized Constructor | User.java | User(int, String...) | Constructor with 5 parameters including validation | To create User objects with initial values | Initializes object with validated data | Admin.java, Teacher.java, Student.java |
| Constructor Overloading | Course.java | Multiple constructors | Has default constructor, 3-param constructor, and 7-param constructor | To provide flexibility in object creation | Allows creating Course with different detail levels | Main.java, CourseRepository.java |
| Constructor Chaining | Admin.java | super() call | Calls parent User constructor using super(userId, name...) | To reuse parent class initialization | Ensures User fields are properly initialized | All User subclasses |
| Constructor with Validation | User.java | User(int, String...) | Calls InputValidator.validateAllUserFields() in constructor | To ensure data integrity at object creation | Prevents invalid objects from being created | All domain classes |

### 🧾 Code Snippet (Course.java - Constructor Overloading):
```java
// Default constructor
public Course() {}

// Simplified constructor
public Course(String courseId, String courseName, int creditHours) {
    this.courseId = courseId;
    this.courseName = courseName;
    this.creditHours = creditHours;
}

// Full constructor with @JsonCreator
@JsonCreator
public Course(@JsonProperty("courseId") String courseId,
             @JsonProperty("courseName") String courseName,
             @JsonProperty("creditHours") int creditHours,
             @JsonProperty("facultyName") String facultyName,
             @JsonProperty("classDays") String classDays,
             @JsonProperty("classTimes") String classTimes,
             @JsonProperty("classDates") String classDates) {
    // ... initialization
}
```

### 🧾 Code Snippet (Admin.java - Constructor Chaining):
```java
public Admin(int userId, String name, String email, String username, String password) 
        throws ValidationException {
    super(userId, name, email, username, password); // Constructor chaining
    this.managedStudents = new ArrayList<>();
    this.managedTeachers = new ArrayList<>();
}
```

---

## Access Modifiers

| Concept | File Name | Class/Method | How it is implemented | Why it is used | What role it plays | Other files using it |
|---------|-----------|--------------|------------------------|----------------|---------------------|-----------------------|
| Public | User.java | public class User | Class declared with public modifier | To allow access from any package | Makes User class accessible throughout project | All packages |
| Private | User.java | private int userId | Fields declared with private modifier | To restrict direct access to class members | Protects data, enforces encapsulation | Accessed via getters/setters |
| Protected | (Could use) | protected methods | Not heavily used but available for inheritance | To allow access to subclasses | Would allow subclass access while hiding from others | Potential use in User hierarchy |
| Default (Package) | Repository.java | interface Repository | No modifier means package-private for some methods | To restrict access to same package | Controls visibility within sms.data package | Same package classes |
| Public Methods | User.java | public String getRole() | Methods with public access | To provide external interface | Allows other classes to interact with objects | All packages |
| Private Static Final | User.java | private static final FileUploadService | Constant service instance | To create class-level constant | Provides shared service instance | User class methods |

### 🧾 Code Snippet (User.java):
```java
public abstract class User {  // public class
    private int userId;        // private field
    private String name;       // private field
    
    // public method
    public String getName() {
        return name;
    }
    
    // private static final
    private static final FileUploadService uploadService = new FileUploadService();
}
```

---

## Packages

| Concept | File Name | Class/Method | How it is implemented | Why it is used | What role it plays | Other files using it |
|---------|-----------|--------------|------------------------|----------------|---------------------|-----------------------|
| Domain Package | sms.domain.* | package sms.domain | User, Admin, Teacher, Student, Course, etc. | To organize core business entities | Contains all domain model classes | All other packages import from here |
| Data Package | sms.data.* | package sms.data | Repository interface, StudentRepository, TeacherRepository, etc. | To separate data access layer | Handles all data persistence operations | Main.java, domain classes |
| Exceptions Package | sms.exceptions.* | package sms.exceptions | BaseException, ValidationException, NotFoundException, etc. | To organize custom exception hierarchy | Centralizes error handling classes | All packages use exceptions |
| Services Package | sms.services.* | package sms.services | UploadService, FileUploadService | To contain business logic services | Provides reusable service functionality | User.java, Main.java |
| Validation Package | sms.validation.* | package sms.validation | InputValidator class | To centralize validation logic | Contains all input validation methods | User.java, Admin.java, domain classes |
| Search Package | sms.search.* | package sms.search | Searchable interface | To define search capabilities | Provides search contract | Admin.java, Teacher.java, Student.java |
| App Package | sms.app.* | package sms.app | Main class | To contain application entry point | Houses main() method and initialization | All packages |
| Menus Package | sms.app.menus.* | package sms.app.menus | PrincipalMenuHandler, AdminMenuHandler, TeacherMenuHandler, StudentMenuHandler | To modularize role-specific menu operations | Contains all menu handler classes for each user role | Main.java delegates to these handlers |

### 🧾 Code Snippet (Package Structure):
```java
// User.java
package sms.domain;
import sms.exceptions.AuthenticationException;
import sms.validation.InputValidator;

// StudentRepository.java
package sms.data;
import sms.domain.Student;
import sms.exceptions.RepositoryException;

// InputValidator.java
package sms.validation;
import sms.exceptions.ValidationException;

// AdminMenuHandler.java (New - Refactored from Main.java)
package sms.app.menus;
import sms.data.*;
import sms.domain.*;
import sms.validation.InputValidator;
```

---

## Exception Handling

| Concept | File Name | Class/Method | How it is implemented | Why it is used | What role it plays | Other files using it |
|---------|-----------|--------------|------------------------|----------------|---------------------|-----------------------|
| Custom Exception | BaseException.java | abstract class BaseException extends Exception | Creates custom exception hierarchy with timestamp and logging | To provide application-specific exceptions | Base for all custom exceptions | All exception subclasses |
| Custom Exception | ValidationException.java | class extends BaseException | Extends BaseException with fieldName and invalidValue | To handle validation errors with specific details | Captures validation failure information | InputValidator.java, User.java |
| Try-Catch | Main.java | demonstrateOopFeatures() | try-catch blocks catching ValidationException, NotFoundException | To handle exceptions during OOP demonstration | Gracefully handles errors during execution | All exception types |
| Try-Catch-Finally | Main.java | exception demos | try-catch-finally blocks with cleanup | To ensure cleanup code runs regardless of exception | Demonstrates complete exception handling | Various exception types |
| Throw | User.java | login() method | throws AuthenticationException when validation fails | To propagate errors to caller | Signals authentication failure | Main.java catches it |
| Throws Declaration | User.java | User constructor | throws ValidationException | To declare checked exceptions | Informs caller of potential exceptions | All User subclasses |

### 🧾 Code Snippet (Main.java - Try-Catch-Finally):
```java
try {
    System.out.println("Testing ValidationException...");
    admin.registerStudent("", "invalid-email");
} catch (ValidationException e) {
    System.out.println("Caught ValidationException:");
    System.out.println("  Field: " + e.getFieldName());
    e.log();
} finally {
    System.out.println("  Validation test completed (finally block executed)");
}
```

### 🧾 Code Snippet (User.java):
```java
public void login() throws AuthenticationException {
    if (username == null || username.trim().isEmpty()) {
        throw new AuthenticationException("Username cannot be empty", 
                                         username, "INVALID_USERNAME");
    }
    System.out.println("User " + username + " logged in successfully");
}
```

---

## Generics

| Concept | File Name | Class/Method | How it is implemented | Why it is used | What role it plays | Other files using it |
|---------|-----------|--------------|------------------------|----------------|---------------------|-----------------------|
| Generic Interface | Repository.java | interface Repository<T> | Defined with type parameter <T> for CRUD operations | To create type-safe data access pattern | Provides reusable CRUD operations for any type | StudentRepository, TeacherRepository, AdminRepository |
| Generic Interface | Searchable.java | interface Searchable<T> | Interface with generic type parameter for search results | To allow type-safe search implementations | Returns typed search results | Admin, Teacher, Student |
| Generic Interface | Sortable.java | interface Sortable<T> | Interface with generic type for sortable items | To provide type-safe sorting | Returns typed sorted lists | Admin, Teacher, Student |
| Generic Class Implementation | StudentRepository.java | implements Repository<Student> | Implements generic interface with specific type | To provide type-safe student operations | Ensures compile-time type safety | Main.java |
| Generic Usage | Main.java | Repository<Student> studentRepo | Uses generic type with specific parameter | To maintain type safety in variable declarations | Ensures only Student objects are used | All repository usage |
| Multiple Type Parameters | Searchable.java, Admin.java | Admin implements Searchable<Object> | Can search multiple types | To handle heterogeneous search results | Allows searching across different entity types | Admin class |

### 🧾 Code Snippet (Repository.java):
```java
public interface Repository<T> {
    void add(T item) throws RepositoryException, ValidationException;
    void update(T item) throws RepositoryException, NotFoundException;
    void delete(T item) throws RepositoryException, NotFoundException;
    List<T> getAll() throws RepositoryException;
    List<T> find(String criteria) throws RepositoryException;
}
```

### 🧾 Code Snippet (Main.java):
```java
// Generic repository usage
Repository<Student> studentRepo = studentRepository;
Repository<Teacher> teacherRepo = teacherRepository;
Repository<Admin> adminRepo = adminRepository;
Repository<Course> courseRepo = courseRepository;
```

---

## Collections Framework

| Concept | File Name | Class/Method | How it is implemented | Why it is used | What role it plays | Other files using it |
|---------|-----------|--------------|------------------------|----------------|---------------------|-----------------------|
| ArrayList | Admin.java | private List<Student> managedStudents | ArrayList initialization in constructor | To store dynamic list of students | Provides resizable array for managing students | Admin methods |
| List Interface | Repository.java | List<T> getAll() | Returns List<T> from repository methods | To provide standard collection interface | Ensures consistency across repositories | All repository implementations |
| Stream API | Admin.java | search() method | students.stream().filter().collect(Collectors.toList()) | To perform functional-style operations | Enables declarative data filtering | Search implementations |
| Lambda Expressions | Student.java | sort() method | sortedCourses.sort((a, b) -> a.getName().compareTo(b.getName())) | To provide inline comparison logic | Simplifies sorting with concise syntax | All sort implementations |
| Filter Operation | StudentRepository.java | find() method | students.stream().filter(student -> ...) | To search with predicates | Filters students based on criteria | Repository search |
| Collectors | Admin.java | search() | collect(Collectors.toList()) | To collect stream results | Converts stream back to List | All stream operations |
| Enhanced For Loop | Main.java | demonstrateOopFeatures() | for (User user : users) | To iterate over collections | Simplifies array/collection iteration | Throughout the codebase |

### 🧾 Code Snippet (Admin.java - Stream & Lambda):
```java
@Override
public List<Object> search(String criteria) {
    // Using Stream API with Lambda expressions
    List<Student> studentResults = managedStudents.stream()
        .filter(s -> s.getName().toLowerCase().contains(criteria.toLowerCase()) ||
                    s.getEmail().toLowerCase().contains(criteria.toLowerCase()))
        .collect(Collectors.toList());
    return results;
}
```

### 🧾 Code Snippet (Student.java - Lambda Sorting):
```java
public List<Course> sort(String criteria) {
    List<Course> sortedCourses = new ArrayList<>(courses);
    sortedCourses.sort((a, b) -> a.getCourseName().compareTo(b.getCourseName()));
    return sortedCourses;
}
```

---

## File Handling

| Concept | File Name | Class/Method | How it is implemented | Why it is used | What role it plays | Other files using it |
|---------|-----------|--------------|------------------------|----------------|---------------------|-----------------------|
| File Reading | StudentRepository.java | loadAll() | Uses ObjectMapper to read JSON file with readValue() | To load student data from JSON file | Deserializes JSON to Student objects | Constructor calls this |
| File Writing | StudentRepository.java | saveAll() | Uses ObjectMapper to write JSON with writeValue() | To persist student data to JSON file | Serializes Student objects to JSON | add(), update(), delete() call this |
| File Class | StudentRepository.java | File file = new File(DATA_FILE) | Creates File object for file operations | To represent file system path | Provides file reference for I/O | loadAll(), saveAll() |
| JSON Serialization | StudentRepository.java | objectMapper.writeValue() | Jackson ObjectMapper serializes objects to JSON | To convert Java objects to JSON format | Persists data in human-readable format | All Repository classes |
| JSON Deserialization | StudentRepository.java | objectMapper.readValue() | Jackson ObjectMapper deserializes JSON to objects | To convert JSON to Java objects | Loads data from files into memory | All Repository classes |
| IOException Handling | StudentRepository.java | try-catch IOException | Catches IOException in file operations | To handle file I/O errors | Ensures graceful error handling | All file operations |
| TypeReference | StudentRepository.java | new TypeReference<List<Student>>() | Used for generic type deserialization | To preserve type information during JSON deserialization | Enables proper generic type handling | All repository load operations |

### 🧾 Code Snippet (StudentRepository.java - File Reading):
```java
private void loadAll() throws RepositoryException {
    File file = new File(DATA_FILE);
    if (!file.exists()) {
        return;
    }
    
    try {
        List<Student> studentList = objectMapper.readValue(file, 
            new TypeReference<List<Student>>() {});
        students.clear();
        students.addAll(studentList);
        System.out.println("Loaded " + students.size() + " students");
    } catch (IOException e) {
        throw new RepositoryException("Failed to load students", "LOAD", "STUDENT", e);
    }
}
```

### 🧾 Code Snippet (StudentRepository.java - File Writing):
```java
private void saveAll() throws RepositoryException {
    try {
        objectMapper.writerWithDefaultPrettyPrinter()
                   .writeValue(new File(DATA_FILE), students);
    } catch (IOException e) {
        throw new RepositoryException("Failed to save students", "SAVE", "STUDENT", e);
    }
}
```

---

## Quick Links

- [Classes & Objects](#classes--objects) — User.java (class User), Admin.java (class Admin), Course.java (class Course)
- [Encapsulation](#encapsulation) — User.java (private fields with getters/setters), Admin.java (getManagedStudents)
- [Inheritance](#inheritance) — Admin.java (extends User), Teacher.java (extends User), Student.java (extends User)
- [Polymorphism](#polymorphism) — Main.java (demonstrateOopFeatures - User[] array), Admin.java (@Override getRole)
- [Abstraction](#abstraction) — User.java (abstract class), Searchable.java (interface), Repository.java (interface)
- [Constructors](#constructors) — Course.java (constructor overloading), Admin.java (constructor chaining with super)
- [Access Modifiers](#access-modifiers) — User.java (public/private modifiers)
- [Packages](#packages) — sms.domain (domain models), sms.data (repositories), sms.exceptions (exceptions)
- [Exception Handling](#exception-handling) — Main.java (try-catch-finally), User.java (throws), ValidationException.java
- [Generics](#generics) — Repository.java (interface Repository<T>), Searchable.java (interface Searchable<T>)
- [Collections Framework](#collections-framework) — Admin.java (ArrayList, Stream, Lambda), Student.java (sort with lambda)
- [File Handling](#file-handling) — StudentRepository.java (loadAll/saveAll methods with ObjectMapper)
