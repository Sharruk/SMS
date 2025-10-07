package sms.services;

import sms.data.UploadRepository;
import sms.domain.UploadMetadata;
import sms.exceptions.RepositoryException;
import sms.exceptions.UploadException;
import sms.exceptions.ValidationException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUploadService implements UploadService<File> {
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final String[] ALLOWED_EXTENSIONS = {".csv", ".json", ".txt", ".xlsx", ".pdf", ".doc", ".docx"};
    private final String uploadDirectory;
    private final Map<String, String> fileMetadata;
    private UploadRepository uploadRepository;

    public FileUploadService() {
        this.uploadDirectory = "uploads/";
        this.fileMetadata = new HashMap<>();
        this.uploadRepository = new UploadRepository();
        createUploadDirectory();
        createRoleDirectories();
    }

    public FileUploadService(String uploadDirectory) {
        this.uploadDirectory = uploadDirectory;
        this.fileMetadata = new HashMap<>();
        this.uploadRepository = new UploadRepository();
        createUploadDirectory();
        createRoleDirectories();
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

    private void createRoleDirectories() {
        try {
            String[] roleDirs = {"admin", "teachers", "students", "others"};
            for (String roleDir : roleDirs) {
                Path path = Paths.get(uploadDirectory + roleDir);
                if (!path.toFile().exists()) {
                    path.toFile().mkdirs();
                }
            }
        } catch (Exception e) {
            System.err.println("Warning: Could not create role directories: " + e.getMessage());
        }
    }

    /**
     * Role-based file upload method
     * @param filePath Path to the file to upload
     * @param userName Name of the user uploading the file
     * @param role Role of the user (admin, teacher, student)
     * @return The full path where the file was uploaded
     * @throws UploadException if upload fails
     */
    public String uploadFile(String filePath, String userName, String role) throws UploadException {
        try {
            File sourceFile = new File(filePath);
            
            // Validate the file
            validate(sourceFile);
            
            // Determine target directory based on role
            String roleDir = getRoleDirectory(role);
            String targetPath = uploadDirectory + roleDir + "/" + sourceFile.getName();
            
            // Create target file and copy
            Path target = Paths.get(targetPath);
            Files.copy(sourceFile.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
            
            // Save metadata to repository
            int uploadId = uploadRepository.getNextUploadId();
            UploadMetadata metadata = new UploadMetadata(
                uploadId,
                sourceFile.getName(),
                userName,
                role,
                targetPath,
                sourceFile.length()
            );
            
            uploadRepository.add(metadata);
            
            // Display success message
            String roleDisplay = role.substring(0, 1).toUpperCase() + role.substring(1);
            System.out.println("\n✓ File uploaded successfully by " + roleDisplay + ": " + userName + 
                             " → " + targetPath);
            
            return targetPath;
            
        } catch (ValidationException e) {
            throw new UploadException("File validation failed: " + e.getMessage(), 
                                    filePath, getFileExtension(filePath), e);
        } catch (Exception e) {
            throw new UploadException("Failed to upload file: " + e.getMessage(), 
                                    filePath, getFileExtension(filePath), e);
        }
    }

    /**
     * Get the role-specific directory
     */
    private String getRoleDirectory(String role) {
        switch (role.toLowerCase()) {
            case "admin":
                return "admin";
            case "teacher":
                return "teachers";
            case "student":
                return "students";
            default:
                return "others";
        }
    }

    /**
     * Display all uploaded files
     */
    public void displayUploadedFiles() {
        try {
            List<UploadMetadata> uploads = uploadRepository.sort("timestamp");
            
            if (uploads.isEmpty()) {
                System.out.println("\nNo files uploaded yet.");
                return;
            }
            
            System.out.println("\n=== UPLOADED FILES ===");
            for (UploadMetadata upload : uploads) {
                System.out.println(upload.toString());
            }
            System.out.println("Total: " + uploads.size() + " file(s)");
            
        } catch (RepositoryException e) {
            System.err.println("Error displaying uploaded files: " + e.getMessage());
        }
    }

    /**
     * Display uploaded files for a specific role
     */
    public void displayUploadedFilesByRole(String role) {
        try {
            List<UploadMetadata> uploads = uploadRepository.getUploadsByRole(role);
            
            if (uploads.isEmpty()) {
                System.out.println("\nNo files uploaded by " + role + " yet.");
                return;
            }
            
            System.out.println("\n=== UPLOADED FILES (" + role.toUpperCase() + ") ===");
            for (UploadMetadata upload : uploads) {
                System.out.println(upload.toString());
            }
            System.out.println("Total: " + uploads.size() + " file(s)");
            
        } catch (RepositoryException e) {
            System.err.println("Error displaying uploaded files: " + e.getMessage());
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