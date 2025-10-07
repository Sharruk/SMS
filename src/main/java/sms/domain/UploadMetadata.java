package sms.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UploadMetadata {
    @JsonProperty("id")
    private int id;
    
    @JsonProperty("fileName")
    private String fileName;
    
    @JsonProperty("uploadedBy")
    private String uploadedBy;
    
    @JsonProperty("role")
    private String role;
    
    @JsonProperty("filePath")
    private String filePath;
    
    @JsonProperty("timestamp")
    private String timestamp;
    
    @JsonProperty("fileSize")
    private long fileSize;

    public UploadMetadata() {
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public UploadMetadata(int id, String fileName, String uploadedBy, String role, String filePath, long fileSize) {
        this.id = id;
        this.fileName = fileName;
        this.uploadedBy = uploadedBy;
        this.role = role;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s uploaded by %s (%s) - %s bytes - %s", 
                           id, fileName, uploadedBy, role, fileSize, timestamp);
    }
}
