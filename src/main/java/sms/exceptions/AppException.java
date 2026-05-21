package sms.exceptions;

/**
 * Base exception class for all custom exceptions in this system.
 * All other exceptions extend this class.
 *
 * Demonstrates EXCEPTION HANDLING and INHERITANCE in exceptions.
 */
public class AppException extends Exception {

    public AppException(String message) {
        super(message);
    }
}
