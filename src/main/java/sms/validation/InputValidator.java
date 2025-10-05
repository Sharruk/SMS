package sms.validation;

import sms.exceptions.ValidationException;
import java.util.regex.Pattern;

public class InputValidator {
    
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s]+$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[^\\s]+$");
    private static final int MIN_PASSWORD_LENGTH = 6;
    
    public static void validateName(String name) throws ValidationException {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("⚠️ Invalid input: Name cannot be empty", "name", name);
        }
        
        if (!NAME_PATTERN.matcher(name.trim()).matches()) {
            throw new ValidationException("⚠️ Invalid input: Name should contain only letters and spaces", "name", name);
        }
    }
    
    public static void validateEmail(String email) throws ValidationException {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("⚠️ Invalid input: Email cannot be empty", "email", email);
        }
        
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new ValidationException("⚠️ Invalid input: Email must be in valid format (e.g., user@domain.com)", "email", email);
        }
    }
    
    public static void validateUsername(String username) throws ValidationException {
        if (username == null || username.trim().isEmpty()) {
            throw new ValidationException("⚠️ Invalid input: Username cannot be empty", "username", username);
        }
        
        if (!USERNAME_PATTERN.matcher(username).matches() || username.contains(" ")) {
            throw new ValidationException("⚠️ Invalid input: Username cannot contain spaces", "username", username);
        }
    }
    
    public static void validatePassword(String password) throws ValidationException {
        if (password == null || password.isEmpty()) {
            throw new ValidationException("⚠️ Invalid input: Password cannot be empty", "password", "***");
        }
        
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new ValidationException("⚠️ Invalid input: Password must be at least " + MIN_PASSWORD_LENGTH + " characters long", "password", "***");
        }
    }
    
    public static void validateUserId(int userId) throws ValidationException {
        if (userId <= 0) {
            throw new ValidationException("⚠️ Invalid input: User ID must be a positive number", "userId", String.valueOf(userId));
        }
    }
    
    public static void validateAllUserFields(int userId, String name, String email, String username, String password) throws ValidationException {
        validateUserId(userId);
        validateName(name);
        validateEmail(email);
        validateUsername(username);
        validatePassword(password);
    }
}
