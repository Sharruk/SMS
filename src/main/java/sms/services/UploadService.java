package sms.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import sms.data.Repository;
import sms.domain.Student;
import sms.domain.Course;
import sms.exceptions.RepositoryException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class UploadService {
    private final Repository<Student> studentRepository;
    private final Repository<Course> courseRepository;
    private final ObjectMapper objectMapper;

    public UploadService(Repository<Student> studentRepository, Repository<Course> courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Upload students from a JSON file
     */
    public void uploadStudentsFromFile(String filePath) throws RepositoryException {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new RepositoryException("File does not exist: " + filePath, "UPLOAD", "STUDENT");
            }

            // Read students from file (assuming it's a JSON array)
            Student[] students = objectMapper.readValue(file, Student[].class);
            
            int successCount = 0;
            int errorCount = 0;

            for (Student student : students) {
                try {
                    studentRepository.save(student);
                    successCount++;
                    System.out.println("Uploaded student: " + student.getFullName());
                } catch (RepositoryException e) {
                    errorCount++;
                    System.out.println("Failed to upload student " + student.getFullName() + ": " + e.getMessage());
                }
            }

            System.out.println("\nUpload Summary:");
            System.out.println("Successfully uploaded: " + successCount + " students");
            System.out.println("Failed uploads: " + errorCount + " students");

        } catch (IOException e) {
            throw new RepositoryException("Failed to read upload file: " + e.getMessage(), "UPLOAD", "STUDENT", e);
        }
    }

    /**
     * Upload courses from a JSON file
     */
    public void uploadCoursesFromFile(String filePath) throws RepositoryException {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new RepositoryException("File does not exist: " + filePath, "UPLOAD", "COURSE");
            }

            // Read courses from file (assuming it's a JSON array)
            Course[] courses = objectMapper.readValue(file, Course[].class);
            
            int successCount = 0;
            int errorCount = 0;

            for (Course course : courses) {
                try {
                    courseRepository.save(course);
                    successCount++;
                    System.out.println("Uploaded course: " + course.getCourseName());
                } catch (RepositoryException e) {
                    errorCount++;
                    System.out.println("Failed to upload course " + course.getCourseName() + ": " + e.getMessage());
                }
            }

            System.out.println("\nUpload Summary:");
            System.out.println("Successfully uploaded: " + successCount + " courses");
            System.out.println("Failed uploads: " + errorCount + " courses");

        } catch (IOException e) {
            throw new RepositoryException("Failed to read upload file: " + e.getMessage(), "UPLOAD", "COURSE", e);
        }
    }

    /**
     * Interactive method to create sample data for testing
     */
    public void createSampleData() throws RepositoryException {
        System.out.println("UploadService: Creating sample data for testing...");
        
        // Create sample students
        Student student1 = new Student("user1", "John", "Doe", "john.doe@email.com", "password123", 
                                     "STU001", "Computer Science", 2);
        Student student2 = new Student("user2", "Jane", "Smith", "jane.smith@email.com", "password456", 
                                     "STU002", "Mathematics", 3);
        Student student3 = new Student("user3", "Mike", "Johnson", "mike.johnson@email.com", "password789", 
                                     "STU003", "Physics", 1);

        // Create sample courses
        Course course1 = new Course("CS101", "Introduction to Programming", 
                                  "Basic programming concepts and algorithms", 3, 30);
        Course course2 = new Course("MATH201", "Calculus II", 
                                  "Advanced calculus and differential equations", 4, 25);
        Course course3 = new Course("PHYS101", "General Physics I", 
                                  "Mechanics and thermodynamics", 4, 35);

        try {
            // Save students
            studentRepository.save(student1);
            studentRepository.save(student2);
            studentRepository.save(student3);

            // Save courses
            courseRepository.save(course1);
            courseRepository.save(course2);
            courseRepository.save(course3);

            System.out.println("Sample data created successfully:");
            System.out.println("- 3 students added");
            System.out.println("- 3 courses added");

        } catch (RepositoryException e) {
            throw new RepositoryException("Failed to create sample data: " + e.getMessage(), "CREATE", "SAMPLE_DATA", e);
        }
    }

    /**
     * Export current data to backup files
     */
    public void exportDataToBackup() throws RepositoryException {
        try {
            // Force save current state
            studentRepository.saveAll();
            courseRepository.saveAll();
            
            System.out.println("UploadService: Data exported to backup files successfully");
            System.out.println("- Students saved to: students.json");
            System.out.println("- Courses saved to: courses.json");
            
        } catch (RepositoryException e) {
            throw new RepositoryException("Failed to export data: " + e.getMessage(), "EXPORT", "BACKUP", e);
        }
    }

    /**
     * Display upload menu for interactive use
     */
    public void displayUploadMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n=== Upload Service Menu ===");
            System.out.println("1. Create Sample Data");
            System.out.println("2. Export Data to Backup");
            System.out.println("3. Upload Students from File");
            System.out.println("4. Upload Courses from File");
            System.out.println("5. Return to Main Menu");
            System.out.print("Choose an option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        createSampleData();
                        break;
                    case 2:
                        exportDataToBackup();
                        break;
                    case 3:
                        System.out.print("Enter student file path: ");
                        String studentFile = scanner.nextLine();
                        uploadStudentsFromFile(studentFile);
                        break;
                    case 4:
                        System.out.print("Enter course file path: ");
                        String courseFile = scanner.nextLine();
                        uploadCoursesFromFile(courseFile);
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (RepositoryException e) {
                System.out.println("Upload error: " + e.getMessage());
            }
        }
    }
}