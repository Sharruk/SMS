package sms.exceptions;

/**
 * Thrown when user input fails a validation rule.
 * Extends AppException (demonstrates EXCEPTION HIERARCHY / INHERITANCE).
 */
public class ValidationException extends AppException {

    private String fieldName; // which field failed validation

    public ValidationException(String message, String fieldName) {
        super("Invalid " + fieldName + ": " + message);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
