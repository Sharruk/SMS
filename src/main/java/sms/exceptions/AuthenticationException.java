package sms.exceptions;

public class AuthenticationException extends BaseException {
    private String errorCode;
    private String username;

    public AuthenticationException(String message) {
        super(message);
        this.errorCode = "AUTH_ERROR";
    }

    public AuthenticationException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public AuthenticationException(String message, String username, String errorCode) {
        super(message);
        this.username = username;
        this.errorCode = errorCode;
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "AUTH_ERROR";
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void log() {
        super.log();
        System.err.println("Error code: " + errorCode);
        if (username != null) {
            System.err.println("Username: " + username);
        }
    }
}