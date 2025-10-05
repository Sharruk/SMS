# Input Validation Guide

## Overview
The LMS now includes comprehensive input validation for user registration (Student, Teacher, Admin, Principal). All validation rules are enforced through a centralized `InputValidator` class with custom exception handling.

## Validation Rules

### 1. **Name Validation**
- **Rule**: Only alphabetic characters and spaces allowed
- **Examples**:
  - ✅ Valid: `"John Smith"`, `"Mary Jane"`, `"Alice"`
  - ❌ Invalid: `"Dr. Smith"`, `"John123"`, `"user@name"`, `""`

### 2. **Email Validation**
- **Rule**: Must be a valid email format (user@domain.com)
- **Examples**:
  - ✅ Valid: `"john@example.com"`, `"user.name@domain.org"`
  - ❌ Invalid: `"notanemail"`, `"user@"`, `"@domain.com"`, `""`

### 3. **Username Validation**
- **Rule**: Cannot be empty or contain spaces
- **Examples**:
  - ✅ Valid: `"john123"`, `"user_name"`, `"alice"`
  - ❌ Invalid: `"john smith"`, `"user name"`, `""`

### 4. **Password Validation**
- **Rule**: Minimum 4 characters required
- **Examples**:
  - ✅ Valid: `"pass"`, `"pass123"`, `"myPassword"`
  - ❌ Invalid: `"123"`, `"ab"`, `""`

### 5. **User ID Validation**
- **Rule**: Must be a positive number
- **Examples**:
  - ✅ Valid: `1001`, `2023`, `5000`
  - ❌ Invalid: `0`, `-1`, `-100`

## How It Works

### Centralized Validation
All validation is handled by the `InputValidator` class located in `src/main/java/sms/validation/InputValidator.java`. This ensures consistency across the entire application.

### Loop-Based Validation with Retry
All interactive registration workflows (Teacher, Admin, Student, Course) use loop-based validation that prompts users to retry until they provide valid input:

```java
// Example: Teacher ID validation with retry loop
int userId = 0;
while (true) {
    System.out.print("Enter Teacher ID: ");
    String idInput = scanner.nextLine();
    try {
        InputValidator.validateNumericId(idInput);
        userId = Integer.parseInt(idInput.trim());
        break;  // Exit loop when valid
    } catch (ValidationException e) {
        System.out.println("⚠️ Invalid ID: ID must be a number. Please try again.");
        // Loop continues, prompting user again
    }
}
```

### Automatic Validation
When creating a new User (Student, Teacher, Admin, Principal), validation happens automatically in constructors:

```java
// This will automatically validate all fields
Student student = new Student(1001, "John Smith", "john@example.com", "john", "pass123");
```

### Exception Handling
If validation fails, a `ValidationException` is thrown with a clear error message:

```java
try {
    Student student = new Student(1001, "Dr. Smith", "email", "user", "123");
} catch (ValidationException e) {
    System.out.println(e.getMessage());
    // Output: ⚠️ Invalid input: Name should contain only letters and spaces
}
```

## Error Messages

All validation errors include:
- Clear description of what went wrong
- The field that failed validation
- User-friendly emoji indicators (⚠️)
- Prompt to try again

Example error messages for user registration:
- `⚠️ Invalid ID: ID must be a number. Please try again.`
- `⚠️ Invalid input: Name should contain only letters and spaces. Please try again.`
- `⚠️ Invalid input: Enter a valid email format. Please try again.`
- `⚠️ Username cannot be empty. Please try again.`
- `⚠️ Password must be at least 4 characters. Please try again.`

Example error messages for course creation:
- `⚠️ Invalid Course ID: Course ID cannot be empty. Please try again.`
- `⚠️ Invalid Course Name: Course Name cannot be empty. Please try again.`
- `⚠️ Invalid Credit Hours: Credit Hours must be a number. Please try again.`

## Testing Validation

You can test the validation by:

1. **Running the application** and selecting menu options to register new users/courses
2. **Trying invalid inputs** to see the retry loop in action:
   - Enter non-numeric ID (e.g., "abc")
   - Enter name with numbers or symbols (e.g., "Dr. Smith", "John123")
   - Enter invalid email format (e.g., "notanemail")
   - Enter username with spaces (e.g., "john smith")
   - Enter short password (e.g., "123")
3. **Observing the retry behavior**: The system will display an error message and prompt you to try again
4. **Entering valid data**: Once you provide valid input, the system accepts it and moves to the next field

## Code Example

```java
import sms.domain.Student;
import sms.exceptions.ValidationException;

public class Example {
    public static void main(String[] args) {
        try {
            // Valid creation
            Student validStudent = new Student(
                1001, 
                "Alice Johnson",           // ✅ Valid: letters and spaces only
                "alice@student.edu",       // ✅ Valid: proper email format
                "alice",                   // ✅ Valid: no spaces
                "pass123"                  // ✅ Valid: 4+ characters
            );
            System.out.println("Student created successfully!");
            
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
            e.log(); // Logs detailed error information
        }
        
        try {
            // Invalid creation - will throw ValidationException
            Student invalidStudent = new Student(
                -1,                        // ❌ Invalid: negative ID
                "Dr. Smith",               // ❌ Invalid: contains period
                "notanemail",              // ❌ Invalid: not email format
                "user name",               // ❌ Invalid: contains space
                "123"                      // ❌ Invalid: less than 4 chars
            );
            
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
            // Will display the first validation error encountered
        }
    }
}
```

## Validation Methods Available

The `InputValidator` class provides the following validation methods:

### User Validation Methods
- `validateNumericId(String id)` - Validates numeric user IDs
- `validateName(String name)` - Validates user names (letters and spaces only)
- `validateEmail(String email)` - Validates email format
- `validateUsername(String username)` - Validates username (no spaces)
- `validatePassword(String password)` - Validates password (min 4 characters)
- `validateUserId(int userId)` - Validates user ID is positive

### Course Validation Methods
- `validateCourseId(String courseId)` - Validates course ID is not empty
- `validateCourseName(String courseName)` - Validates course name is not empty
- `validateCreditHours(String creditHours)` - Validates credit hours is a positive number

## Benefits

1. **Data Integrity**: Ensures all user and course data meets quality standards
2. **Security**: Prevents injection of malicious data
3. **User Experience**: Loop-based retry with clear error messages helps users correct their input
4. **Maintainability**: Centralized validation is easy to update
5. **Consistency**: Same validation rules apply throughout the application
6. **Error Prevention**: Users cannot proceed until they provide valid data
