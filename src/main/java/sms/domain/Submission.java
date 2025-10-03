package sms.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Submission {
    private int submissionId;
    private int assignmentId;
    private int studentId;
    private String fileName;
    private String filePath;
    private String timestamp;

    public Submission() {}

    @JsonCreator
    public Submission(@JsonProperty("submissionId") int submissionId,
                     @JsonProperty("assignmentId") int assignmentId,
                     @JsonProperty("studentId") int studentId,
                     @JsonProperty("fileName") String fileName,
                     @JsonProperty("filePath") String filePath,
                     @JsonProperty("timestamp") String timestamp) {
        this.submissionId = submissionId;
        this.assignmentId = assignmentId;
        this.studentId = studentId;
        this.fileName = fileName;
        this.filePath = filePath;
        this.timestamp = timestamp;
    }

    public Submission(int submissionId, int assignmentId, int studentId, String fileName, String filePath) {
        this.submissionId = submissionId;
        this.assignmentId = assignmentId;
        this.studentId = studentId;
        this.fileName = fileName;
        this.filePath = filePath;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public int getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(int submissionId) {
        this.submissionId = submissionId;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    @Override
    public String toString() {
        return "Submission{" +
                "submissionId=" + submissionId +
                ", assignmentId=" + assignmentId +
                ", studentId=" + studentId +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
