package sms.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import sms.exceptions.AuthenticationException;
import sms.exceptions.UploadException;
import sms.exceptions.ValidationException;
import sms.services.FileUploadService;
import sms.services.UploadService;
import sms.validation.InputValidator;
import java.io.File;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = Student.class, name = "student"),
    @JsonSubTypes.Type(value = Teacher.class, name = "teacher"),
    @JsonSubTypes.Type(value = Admin.class, name = "admin"),
    @JsonSubTypes.Type(value = Principal.class, name = "principal")
})
public abstract class User {
    private int userId;
    private String name;
    private String email;
    private String username;
    @com.fasterxml.jackson.annotation.JsonIgnore
    private String password;
    
    // Upload service for file operations
    private static final FileUploadService uploadService = new FileUploadService();

    // Default constructor for Jackson
    public User() {}

    public User(int userId, String name, String email, String username, String password) throws ValidationException {
        InputValidator.validateAllUserFields(userId, name, email, username, password);
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    // Abstract method for polymorphism demonstration
    public abstract String getRole();

    // Login method
    public void login() throws AuthenticationException {
        if (username == null || username.trim().isEmpty()) {
            throw new AuthenticationException("Username cannot be empty", username, "INVALID_USERNAME");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new AuthenticationException("Password cannot be empty", username, "INVALID_PASSWORD");
        }
        System.out.println("User " + username + " (" + getRole() + ") logged in successfully");
    }

    // Logout method
    public void logout() {
        System.out.println("User " + username + " (" + getRole() + ") logged out");
    }

    // Upload method using the upload service
    public void upload(File file) throws UploadException, ValidationException {
        System.out.println("User " + username + " attempting to upload file: " + file.getName());
        
        try {
            uploadService.validate(file);
            uploadService.store(file);
            uploadService.saveMetadata(file);
            System.out.println("File upload completed successfully by user: " + username);
        } catch (ValidationException e) {
            e.log();
            throw e;
        } catch (UploadException e) {
            e.log();
            throw e;
        }
    }

    // Getters and Setters (Encapsulation)
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Public method for authentication service access
    public String getPasswordForAuth() {
        return password;
    }
    
    // Setter for JSON deserialization
    public void setPasswordForAuth(String password) {
        this.password = password;
    }

    @com.fasterxml.jackson.annotation.JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", role='" + getRole() + '\'' +
                '}';
    }
}