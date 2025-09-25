package sms.exceptions;

public class ValidationException extends BaseException {
    private String fieldName;
    private String invalidValue;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, String fieldName, String invalidValue) {
        super(message);
        this.fieldName = fieldName;
        this.invalidValue = invalidValue;
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getInvalidValue() {
        return invalidValue;
    }

    @Override
    public void log() {
        super.log();
        if (fieldName != null) {
            System.err.println("Invalid field: " + fieldName + " = '" + invalidValue + "'");
        }
    }
}