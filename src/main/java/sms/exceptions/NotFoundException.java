package sms.exceptions;

public class NotFoundException extends BaseException {
    private String entityType;
    private String searchCriteria;

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, String entityType, String searchCriteria) {
        super(message);
        this.entityType = entityType;
        this.searchCriteria = searchCriteria;
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getEntityType() {
        return entityType;
    }

    public String getSearchCriteria() {
        return searchCriteria;
    }

    @Override
    public void log() {
        super.log();
        if (entityType != null && searchCriteria != null) {
            System.err.println("Entity: " + entityType + ", Search criteria: " + searchCriteria);
        }
    }
}