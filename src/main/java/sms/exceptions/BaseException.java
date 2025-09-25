package sms.exceptions;

import java.time.LocalDateTime;

public abstract class BaseException extends Exception {
    private String message;
    private LocalDateTime timestamp;

    public BaseException(String message) {
        super(message);
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public void log() {
        System.err.println("[" + timestamp + "] " + this.getClass().getSimpleName() + ": " + message);
        if (getCause() != null) {
            System.err.println("Caused by: " + getCause().getMessage());
        }
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}