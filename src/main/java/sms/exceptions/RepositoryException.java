package sms.exceptions;

public class RepositoryException extends BaseException {
    private String operation;
    private String entityType;

    public RepositoryException(String message) {
        super(message);
        this.operation = "UNKNOWN";
        this.entityType = "UNKNOWN";
    }

    public RepositoryException(String message, String operation, String entityType) {
        super(message);
        this.operation = operation;
        this.entityType = entityType;
    }

    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
        this.operation = "UNKNOWN";
        this.entityType = "UNKNOWN";
    }

    public RepositoryException(String message, String operation, String entityType, Throwable cause) {
        super(message, cause);
        this.operation = operation;
        this.entityType = entityType;
    }

    public String getOperation() {
        return operation;
    }

    public String getEntityType() {
        return entityType;
    }

    @Override
    public void log() {
        super.log();
        System.err.println("Operation: " + operation + ", Entity: " + entityType);
    }
}