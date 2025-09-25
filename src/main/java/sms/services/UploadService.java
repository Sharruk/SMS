package sms.services;

import sms.exceptions.UploadException;
import sms.exceptions.ValidationException;

/**
 * Generic interface for file upload services
 * @param <T> The type of file being uploaded
 */
public interface UploadService<T> {
    /**
     * Validate the uploaded file
     * @param file The file to validate
     * @return true if valid, false otherwise
     * @throws ValidationException if validation fails
     */
    boolean validate(T file) throws ValidationException;
    
    /**
     * Store the uploaded file
     * @param file The file to store
     * @throws UploadException if storage fails
     */
    void store(T file) throws UploadException;
    
    /**
     * Save metadata about the uploaded file
     * @param file The file whose metadata to save
     * @throws UploadException if metadata saving fails
     */
    void saveMetadata(T file) throws UploadException;
}