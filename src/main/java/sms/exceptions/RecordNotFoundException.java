package sms.exceptions;

/**
 * Thrown when a searched record does not exist in the system.
 * Extends AppException (demonstrates EXCEPTION HIERARCHY / INHERITANCE).
 */
public class RecordNotFoundException extends AppException {

    public RecordNotFoundException(String message) {
        super(message);
    }
}
