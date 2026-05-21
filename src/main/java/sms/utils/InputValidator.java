package sms.utils;

import sms.exceptions.ValidationException;

/**
 * Utility class with static methods for validating user inputs.
 *
 * Demonstrates:
 *   - ENCAPSULATION : rules are centralised here, not scattered in menu code
 *   - EXCEPTION HANDLING : throws ValidationException on failure
 *
 * All methods are static — call directly as InputValidator.validateName(name).
 */
public class InputValidator {

    // Validate that a name contains only letters and spaces
    public static void validateName(String name) throws ValidationException {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Name cannot be empty", "name");
        }
        if (!name.matches("^[a-zA-Z ]+$")) {
            throw new ValidationException("Name must contain only letters and spaces", "name");
        }
    }

    // Validate email format (e.g., user@domain.com)
    public static void validateEmail(String email) throws ValidationException {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email cannot be empty", "email");
        }
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new ValidationException("Must be a valid email (e.g., user@domain.com)", "email");
        }
    }

    // Validate username — no spaces allowed
    public static void validateUsername(String username) throws ValidationException {
        if (username == null || username.trim().isEmpty()) {
            throw new ValidationException("Username cannot be empty", "username");
        }
        if (username.contains(" ")) {
            throw new ValidationException("Username cannot contain spaces", "username");
        }
    }

    // Validate password — minimum 4 characters
    public static void validatePassword(String password) throws ValidationException {
        if (password == null || password.isEmpty()) {
            throw new ValidationException("Password cannot be empty", "password");
        }
        if (password.length() < 4) {
            throw new ValidationException("Password must be at least 4 characters long", "password");
        }
    }

    // Validate a positive number field
    public static void validatePositiveNumber(int number, String fieldName) throws ValidationException {
        if (number <= 0) {
            throw new ValidationException(fieldName + " must be a positive number", fieldName);
        }
    }
}
