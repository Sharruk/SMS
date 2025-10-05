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
- **Rule**: Minimum 6 characters required
- **Examples**:
  - ✅ Valid: `"pass123"`, `"myPassword"`, `"123456"`
  - ❌ Invalid: `"12345"`, `"pass"`, `""`

### 5. **User ID Validation**
- **Rule**: Must be a positive number
- **Examples**:
  - ✅ Valid: `1001`, `2023`, `5000`
  - ❌ Invalid: `0`, `-1`, `-100`

## How It Works

### Centralized Validation
All validation is handled by the `InputValidator` class located in `src/main/java/sms/validation/InputValidator.java`. This ensures consistency across the entire application.

### Automatic Validation
When creating a new User (Student, Teacher, Admin, Principal), validation happens automatically:

```java
// This will automatically validate all fields
Student student = new Student(1001, "John Smith", "john@example.com", "john", "password123");
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

Example error messages:
- `⚠️ Invalid input: Name should contain only letters and spaces`
- `⚠️ Invalid input: Email must be in valid format (e.g., user@domain.com)`
- `⚠️ Invalid input: Username cannot contain spaces`
- `⚠️ Invalid input: Password must be at least 6 characters long`
- `⚠️ Invalid input: User ID must be a positive number`

## Testing Validation

You can test the validation by:

1. **Running the application** and selecting menu options to register new users
2. **Trying invalid inputs** to see the validation in action
3. **Checking the console output** for validation error messages

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
                "password123"              // ✅ Valid: 6+ characters
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
                "123"                      // ❌ Invalid: less than 6 chars
            );
            
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
            // Will display the first validation error encountered
        }
    }
}
```

## Benefits

1. **Data Integrity**: Ensures all user data meets quality standards
2. **Security**: Prevents injection of malicious data
3. **User Experience**: Clear error messages help users fix their input
4. **Maintainability**: Centralized validation is easy to update
5. **Consistency**: Same validation rules apply throughout the application
