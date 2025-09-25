package sms.exceptions;

public class AuthorizationException extends BaseException {
    private String userRole;
    private String requiredPermission;

    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(String message, String userRole, String requiredPermission) {
        super(message);
        this.userRole = userRole;
        this.requiredPermission = requiredPermission;
    }

    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getUserRole() {
        return userRole;
    }

    public String getRequiredPermission() {
        return requiredPermission;
    }

    @Override
    public void log() {
        super.log();
        if (userRole != null && requiredPermission != null) {
            System.err.println("User role: " + userRole + ", Required permission: " + requiredPermission);
        }
    }
}