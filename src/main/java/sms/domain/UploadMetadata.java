package sms.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
    
    @JsonProperty("visibleTo")
    private List<String> visibleTo;

    public UploadMetadata() {
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.visibleTo = new ArrayList<>();
    }

    public UploadMetadata(int id, String fileName, String uploadedBy, String role, String filePath, long fileSize) {
        this.id = id;
        this.fileName = fileName;
        this.uploadedBy = uploadedBy;
        this.role = role;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.visibleTo = new ArrayList<>();
    }
    
    public UploadMetadata(int id, String fileName, String uploadedBy, String role, String filePath, long fileSize, List<String> visibleTo) {
        this.id = id;
        this.fileName = fileName;
        this.uploadedBy = uploadedBy;
        this.role = role;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.visibleTo = visibleTo != null ? new ArrayList<>(visibleTo) : new ArrayList<>();
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
    
    public List<String> getVisibleTo() {
        return visibleTo;
    }
    
    public void setVisibleTo(List<String> visibleTo) {
        this.visibleTo = visibleTo != null ? new ArrayList<>(visibleTo) : new ArrayList<>();
    }

    @Override
    public String toString() {
        String visibility = visibleTo != null && !visibleTo.isEmpty() ? 
            (visibleTo.contains("ALL") ? "Visible to: ALL" : "Visible to: " + visibleTo.size() + " user(s)") : 
            "No visibility set";
        return String.format("[%s] %s uploaded by %s (%s) - %s bytes - %s - %s", 
                           id, fileName, uploadedBy, role, fileSize, timestamp, visibility);
    }
}
