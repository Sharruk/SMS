package sms.services;

import sms.exceptions.UploadException;
import sms.exceptions.ValidationException;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class FileUploadService implements UploadService<File> {
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final String[] ALLOWED_EXTENSIONS = {".csv", ".json", ".txt", ".xlsx"};
    private final String uploadDirectory;
    private final Map<String, String> fileMetadata;

    public FileUploadService() {
        this.uploadDirectory = "uploads/";
        this.fileMetadata = new HashMap<>();
        createUploadDirectory();
    }

    public FileUploadService(String uploadDirectory) {
        this.uploadDirectory = uploadDirectory;
        this.fileMetadata = new HashMap<>();
        createUploadDirectory();
    }

    private void createUploadDirectory() {
        try {
            Path path = Paths.get(uploadDirectory);
            if (!path.toFile().exists()) {
                path.toFile().mkdirs();
            }
        } catch (Exception e) {
            System.err.println("Warning: Could not create upload directory: " + e.getMessage());
        }
    }

    @Override
    public boolean validate(File file) throws ValidationException {
        if (file == null) {
            throw new ValidationException("File cannot be null", "file", "null");
        }

        if (!file.exists()) {
            throw new ValidationException("File does not exist", "file", file.getAbsolutePath());
        }

        if (file.length() > MAX_FILE_SIZE) {
            throw new ValidationException("File size exceeds maximum allowed size", 
                                        "fileSize", String.valueOf(file.length()));
        }

        String fileName = file.getName().toLowerCase();
        boolean hasValidExtension = false;
        for (String ext : ALLOWED_EXTENSIONS) {
            if (fileName.endsWith(ext)) {
                hasValidExtension = true;
                break;
            }
        }

        if (!hasValidExtension) {
            throw new ValidationException("File extension not allowed", 
                                        "extension", getFileExtension(fileName));
        }

        return true;
    }

    @Override
    public void store(File file) throws UploadException {
        try {
            validate(file);
            
            String targetPath = uploadDirectory + file.getName();
            Path target = Paths.get(targetPath);
            
            // In a real implementation, you would copy the file
            // For this demo, we'll just simulate storage
            System.out.println("FileUploadService: File stored successfully at " + targetPath);
            
        } catch (ValidationException e) {
            throw new UploadException("File validation failed: " + e.getMessage(), 
                                    file.getName(), getFileExtension(file.getName()), e);
        } catch (Exception e) {
            throw new UploadException("Failed to store file: " + e.getMessage(), 
                                    file.getName(), getFileExtension(file.getName()), e);
        }
    }

    @Override
    public void saveMetadata(File file) throws UploadException {
        try {
            String fileName = file.getName();
            String metadata = String.format("File: %s, Size: %d bytes, Uploaded: %s", 
                                           fileName, file.length(), LocalDateTime.now());
            
            fileMetadata.put(fileName, metadata);
            System.out.println("FileUploadService: Metadata saved for " + fileName);
            
        } catch (Exception e) {
            throw new UploadException("Failed to save metadata: " + e.getMessage(), 
                                    file.getName(), getFileExtension(file.getName()), e);
        }
    }

    private String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return lastDot > 0 ? fileName.substring(lastDot) : "";
    }

    public Map<String, String> getAllMetadata() {
        return new HashMap<>(fileMetadata);
    }

    public String getUploadDirectory() {
        return uploadDirectory;
    }
}