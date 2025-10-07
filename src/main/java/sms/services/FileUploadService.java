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
import java.util.ArrayList;
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
        return uploadFile(filePath, userName, role, new ArrayList<>());
    }
    
    /**
     * Role-based file upload method with visibility control
     * @param filePath Path to the file to upload
     * @param userName Name of the user uploading the file
     * @param role Role of the user (admin, teacher, student)
     * @param visibleTo List of emails who can see this file, or ["ALL"] for everyone
     * @return The full path where the file was uploaded
     * @throws UploadException if upload fails
     */
    public String uploadFile(String filePath, String userName, String role, List<String> visibleTo) throws UploadException {
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
                sourceFile.length(),
                visibleTo
            );
            
            uploadRepository.add(metadata);
            
            // Display success message
            String roleDisplay = role.substring(0, 1).toUpperCase() + role.substring(1);
            String visibilityMsg = visibleTo != null && visibleTo.contains("ALL") ? 
                "Visible to: ALL" : 
                (visibleTo != null && !visibleTo.isEmpty() ? "Visible to: " + visibleTo.size() + " user(s)" : "Private");
            System.out.println("\n✓ File uploaded successfully by " + roleDisplay + ": " + userName + 
                             " → " + targetPath + " (" + visibilityMsg + ")");
            
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
    
    /**
     * Get files visible to a specific user based on email and role
     * @param userEmail Email of the user
     * @param userRole Role of the user (admin, teacher, student)
     * @return List of files visible to this user
     */
    public List<UploadMetadata> getFilesVisibleToUser(String userEmail, String userRole) {
        List<UploadMetadata> visibleFiles = new ArrayList<>();
        
        try {
            List<UploadMetadata> allUploads = uploadRepository.getAll();
            
            for (UploadMetadata upload : allUploads) {
                if (isFileVisibleToUser(upload, userEmail, userRole)) {
                    visibleFiles.add(upload);
                }
            }
        } catch (RepositoryException e) {
            System.err.println("Error retrieving files: " + e.getMessage());
        }
        
        return visibleFiles;
    }
    
    /**
     * Check if a file is visible to a specific user
     * @param upload The file metadata
     * @param userEmail Email of the user
     * @param userRole Role of the user
     * @return true if file is visible to user, false otherwise
     */
    private boolean isFileVisibleToUser(UploadMetadata upload, String userEmail, String userRole) {
        // Admin can see everything
        if ("admin".equalsIgnoreCase(userRole) || "ADMIN".equals(userRole)) {
            return true;
        }
        
        // User can see files they uploaded
        if (userEmail != null && userEmail.equalsIgnoreCase(upload.getUploadedBy())) {
            return true;
        }
        
        // Check visibility list
        List<String> visibleTo = upload.getVisibleTo();
        if (visibleTo != null) {
            // File is visible to ALL
            if (visibleTo.contains("ALL")) {
                return true;
            }
            
            // Check if user's email is in the visibility list
            if (userEmail != null) {
                for (String email : visibleTo) {
                    if (email.equalsIgnoreCase(userEmail)) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    /**
     * Display files visible to a specific user
     * @param userEmail Email of the user
     * @param userName Name of the user
     * @param userRole Role of the user
     */
    public void displayFilesVisibleToUser(String userEmail, String userName, String userRole) {
        List<UploadMetadata> visibleFiles = getFilesVisibleToUser(userEmail, userRole);
        
        if (visibleFiles.isEmpty()) {
            System.out.println("\n" + userName + " (" + userRole + "): No files visible to you.");
            return;
        }
        
        System.out.println("\n=== FILES VISIBLE TO " + userName.toUpperCase() + " (" + userRole.toUpperCase() + ") ===");
        for (UploadMetadata upload : visibleFiles) {
            System.out.println(upload.toString());
        }
        System.out.println("Total: " + visibleFiles.size() + " file(s) visible to you\n");
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
    
    /**
     * Helper method for Teacher to upload file visible to their students
     * @param filePath Path to the file
     * @param teacher Teacher uploading the file
     * @param studentEmails List of student emails who should see the file
     * @return Path where file was uploaded
     * @throws UploadException if upload fails
     */
    public String uploadFileForStudents(String filePath, String teacherEmail, List<String> studentEmails) throws UploadException {
        if (studentEmails == null || studentEmails.isEmpty()) {
            throw new UploadException("Student list cannot be empty for teacher uploads", filePath, getFileExtension(filePath), null);
        }
        return uploadFile(filePath, teacherEmail, "teacher", studentEmails);
    }
    
    /**
     * Helper method for Student to upload file visible to their teachers
     * @param filePath Path to the file
     * @param studentEmail Student's email
     * @param teacherEmails List of teacher emails who should see the file
     * @return Path where file was uploaded
     * @throws UploadException if upload fails
     */
    public String uploadFileForTeachers(String filePath, String studentEmail, List<String> teacherEmails) throws UploadException {
        if (teacherEmails == null || teacherEmails.isEmpty()) {
            throw new UploadException("Teacher list cannot be empty for student uploads", filePath, getFileExtension(filePath), null);
        }
        return uploadFile(filePath, studentEmail, "student", teacherEmails);
    }
    
    /**
     * Helper method for Admin to upload file visible to everyone
     * @param filePath Path to the file
     * @param adminEmail Admin's email
     * @return Path where file was uploaded
     * @throws UploadException if upload fails
     */
    public String uploadFileForAll(String filePath, String adminEmail) throws UploadException {
        List<String> visibleToAll = new ArrayList<>();
        visibleToAll.add("ALL");
        return uploadFile(filePath, adminEmail, "admin", visibleToAll);
    }
    
    /**
     * Helper method for Admin to upload file visible to specific users
     * @param filePath Path to the file
     * @param adminEmail Admin's email
     * @param visibleToEmails List of emails who can see the file
     * @return Path where file was uploaded
     * @throws UploadException if upload fails
     */
    public String uploadFileForSpecificUsers(String filePath, String adminEmail, List<String> visibleToEmails) throws UploadException {
        return uploadFile(filePath, adminEmail, "admin", visibleToEmails);
    }
}