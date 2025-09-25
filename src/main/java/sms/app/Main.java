package sms.app;

import sms.data.Repository;
import sms.data.StudentRepositoryImpl;
import sms.data.CourseRepositoryImpl;
import sms.domain.Student;
import sms.domain.Teacher;
import sms.domain.Course;
import sms.domain.User;
import sms.exceptions.AuthenticationException;
import sms.exceptions.RepositoryException;
import sms.services.AdminService;
import sms.services.UploadService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static Repository<Student> studentRepository;
    private static Repository<Course> courseRepository;
    private static AdminService adminService;
    private static UploadService uploadService;
    private static Scanner scanner;

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("    Student Management System (SMS)      ");
        System.out.println("==========================================");
        System.out.println("Initializing system...\n");

        try {
            // Initialize repositories and services
            initializeSystem();
            
            // Demonstrate OOP features (CRITICAL REQUIREMENT)
            demonstrateOopFeatures();
            
            // Start interactive menu
            runMainMenu();
            
        } catch (Exception e) {
            System.err.println("System initialization failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    private static void initializeSystem() throws RepositoryException {
        // Initialize repositories with JSON persistence
        studentRepository = new StudentRepositoryImpl();
        courseRepository = new CourseRepositoryImpl();
        
        // Initialize services
        adminService = new AdminService(studentRepository, courseRepository);
        uploadService = new UploadService(studentRepository, courseRepository);
        
        // Initialize scanner for user input
        scanner = new Scanner(System.in);
        
        System.out.println("System initialized successfully!");
        System.out.println("JSON files: students.json, courses.json");
        
        // Display current system statistics
        adminService.displaySystemStatistics();
    }

    /**
     * CRITICAL METHOD: Demonstrates all OOP features as required
     * This method showcases Inheritance, Polymorphism, Encapsulation, Generics, and Exception Handling
     */
    public static void demonstrateOopFeatures() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("        DEMONSTRATING OOP FEATURES");
        System.out.println("=".repeat(60));

        try {
            // 1. INHERITANCE DEMONSTRATION
            System.out.println("\n1. === INHERITANCE DEMONSTRATION ===");
            System.out.println("Creating Student and Teacher objects (both inherit from User):");
            
            Student student = new Student("demo_user1", "Alice", "Johnson", "alice@email.com", "pass123",
                                        "STU999", "Computer Science", 2);
            Teacher teacher = new Teacher("demo_user2", "Dr. Smith", "Brown", "smith@email.com", "pass456",
                                        "TEA001", "Computer Science", "Algorithms", 75000.0);
            
            System.out.println("Student created: " + student.getFullName() + " (inherits from User)");
            System.out.println("Teacher created: " + teacher.getFullName() + " (inherits from User)");

            // 2. POLYMORPHISM DEMONSTRATION
            System.out.println("\n2. === POLYMORPHISM DEMONSTRATION ===");
            System.out.println("Using polymorphism with User reference to call overridden methods:");
            
            User[] users = {student, teacher}; // Polymorphic array
            
            for (User user : users) {
                System.out.println("\nUser Type: " + user.getClass().getSimpleName());
                System.out.println("Role: " + user.getRole()); // Polymorphic method call
                user.displayUserInfo(); // Polymorphic method call
                System.out.println("Active Status: " + user.isActive());
            }

            // 3. ENCAPSULATION DEMONSTRATION
            System.out.println("\n3. === ENCAPSULATION DEMONSTRATION ===");
            System.out.println("Demonstrating encapsulation with private fields and public getters/setters:");
            
            System.out.println("Original student GPA: " + student.getGpa());
            student.setGpa(3.85); // Using setter (encapsulation)
            System.out.println("Updated student GPA: " + student.getGpa()); // Using getter (encapsulation)
            
            System.out.println("Original teacher salary: $" + teacher.getSalary());
            teacher.setSalary(80000.0); // Using setter (encapsulation)
            System.out.println("Updated teacher salary: $" + teacher.getSalary()); // Using getter (encapsulation)

            // 4. GENERICS DEMONSTRATION
            System.out.println("\n4. === GENERICS DEMONSTRATION ===");
            System.out.println("Using Generic Repository<T> interface with different types:");
            
            // Save entities using generic repository methods
            Repository<Student> studentRepo = studentRepository;
            Repository<Course> courseRepo = courseRepository;
            
            System.out.println("Saving student using Repository<Student>...");
            studentRepo.save(student);
            
            Course demoCourse = new Course("DEMO101", "OOP Demonstration", "Course for OOP demo", 3, 20);
            System.out.println("Saving course using Repository<Course>...");
            courseRepo.save(demoCourse);
            
            System.out.println("Generic repositories working with different types successfully!");
            
            // Demonstrate generic method calls
            long studentCount = studentRepo.count();
            long courseCount = courseRepo.count();
            System.out.println("Repository counts - Students: " + studentCount + ", Courses: " + courseCount);

            // 5. CUSTOM EXCEPTION HANDLING DEMONSTRATION
            System.out.println("\n5. === CUSTOM EXCEPTION HANDLING DEMONSTRATION ===");
            System.out.println("Demonstrating try-catch-finally with custom exceptions:");
            
            // Authentication Exception Demo
            try {
                System.out.println("Attempting authentication with invalid credentials...");
                adminService.authenticateUser("invalid@email.com", "wrongpassword");
            } catch (AuthenticationException e) {
                System.out.println("Caught AuthenticationException:");
                System.out.println("  Error Code: " + e.getErrorCode());
                System.out.println("  Message: " + e.getMessage());
            } finally {
                System.out.println("  Authentication attempt completed (finally block executed)");
            }
            
            // Repository Exception Demo
            try {
                System.out.println("\nAttempting to find student with null ID...");
                studentRepository.findById(null);
            } catch (RepositoryException e) {
                System.out.println("Caught RepositoryException:");
                System.out.println("  Operation: " + e.getOperation());
                System.out.println("  Entity Type: " + e.getEntityType());
                System.out.println("  Message: " + e.getMessage());
            } finally {
                System.out.println("  Repository operation completed (finally block executed)");
            }

            // 6. COMPREHENSIVE FEATURE INTEGRATION
            System.out.println("\n6. === COMPREHENSIVE INTEGRATION ===");
            System.out.println("All OOP features working together in the system:");
            
            // Use polymorphism, generics, and exception handling together
            try {
                List<Student> allStudents = studentRepository.findAll(); // Generics
                System.out.println("Retrieved " + allStudents.size() + " students using generic repository");
                
                for (Student s : allStudents) {
                    User userRef = s; // Polymorphism
                    System.out.println("  - " + userRef.getFullName() + " (" + userRef.getRole() + ")");
                }
                
            } catch (RepositoryException e) { // Custom exception handling
                System.out.println("Error retrieving students: " + e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("Error in OOP demonstration: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("     OOP FEATURES DEMONSTRATION COMPLETED");
        System.out.println("✓ Inheritance: Student/Teacher extend User");
        System.out.println("✓ Polymorphism: Overridden methods called via User reference");
        System.out.println("✓ Encapsulation: Private fields with public getters/setters");
        System.out.println("✓ Generics: Repository<T> interface with type safety");
        System.out.println("✓ Exception Handling: Custom exceptions with try-catch-finally");
        System.out.println("=".repeat(60) + "\n");
    }

    private static void runMainMenu() {
        while (true) {
            try {
                displayMainMenu();
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        handleLogin();
                        break;
                    case 2:
                        viewAllStudents();
                        break;
                    case 3:
                        addNewCourse();
                        break;
                    case 4:
                        viewAllCourses();
                        break;
                    case 5:
                        addNewStudent();
                        break;
                    case 6:
                        enrollStudentInCourse();
                        break;
                    case 7:
                        uploadService.displayUploadMenu(scanner);
                        break;
                    case 8:
                        adminService.displaySystemStatistics();
                        break;
                    case 9:
                        demonstrateOopFeatures(); // Allow re-running the demo
                        break;
                    case 0:
                        System.out.println("Thank you for using Student Management System!");
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private static void displayMainMenu() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("         MAIN MENU");
        System.out.println("=".repeat(40));
        System.out.println("1. Login");
        System.out.println("2. View All Students");
        System.out.println("3. Add New Course");
        System.out.println("4. View All Courses");
        System.out.println("5. Add New Student");
        System.out.println("6. Enroll Student in Course");
        System.out.println("7. Upload/Data Management");
        System.out.println("8. System Statistics");
        System.out.println("9. Demonstrate OOP Features");
        System.out.println("0. Exit");
        System.out.println("=".repeat(40));
        System.out.print("Choose an option: ");
    }

    private static void handleLogin() {
        System.out.println("\n=== LOGIN ===");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        try {
            User user = adminService.authenticateUser(email, password);
            System.out.println("Login successful! Welcome, " + user.getFullName());
            System.out.println("Role: " + user.getRole());
            user.displayUserInfo();
        } catch (AuthenticationException e) {
            System.out.println("Login failed: " + e.getMessage());
            System.out.println("Error Code: " + e.getErrorCode());
        }
    }

    private static void viewAllStudents() {
        try {
            List<Student> students = adminService.getAllStudents();
            System.out.println("\n=== ALL STUDENTS ===");
            if (students.isEmpty()) {
                System.out.println("No students found.");
            } else {
                for (Student student : students) {
                    System.out.println(student);
                }
            }
        } catch (RepositoryException e) {
            System.out.println("Error retrieving students: " + e.getMessage());
        }
    }

    private static void addNewCourse() {
        System.out.println("\n=== ADD NEW COURSE ===");
        System.out.print("Course ID: ");
        String courseId = scanner.nextLine();
        System.out.print("Course Name: ");
        String courseName = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Credits: ");
        int credits = Integer.parseInt(scanner.nextLine());
        System.out.print("Max Enrollment: ");
        int maxEnrollment = Integer.parseInt(scanner.nextLine());

        try {
            Course course = new Course(courseId, courseName, description, credits, maxEnrollment);
            adminService.addCourse(course);
            System.out.println("Course added successfully!");
            course.displayCourseInfo();
        } catch (RepositoryException e) {
            System.out.println("Error adding course: " + e.getMessage());
        }
    }

    private static void viewAllCourses() {
        try {
            List<Course> courses = adminService.getAllCourses();
            System.out.println("\n=== ALL COURSES ===");
            if (courses.isEmpty()) {
                System.out.println("No courses found.");
            } else {
                for (Course course : courses) {
                    System.out.println(course);
                }
            }
        } catch (RepositoryException e) {
            System.out.println("Error retrieving courses: " + e.getMessage());
        }
    }

    private static void addNewStudent() {
        System.out.println("\n=== ADD NEW STUDENT ===");
        System.out.print("User ID: ");
        String userId = scanner.nextLine();
        System.out.print("First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Student ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Major: ");
        String major = scanner.nextLine();
        System.out.print("Year: ");
        int year = Integer.parseInt(scanner.nextLine());

        try {
            Student student = new Student(userId, firstName, lastName, email, password, studentId, major, year);
            adminService.addStudent(student);
            System.out.println("Student added successfully!");
            student.displayUserInfo();
        } catch (RepositoryException e) {
            System.out.println("Error adding student: " + e.getMessage());
        }
    }

    private static void enrollStudentInCourse() {
        System.out.println("\n=== ENROLL STUDENT IN COURSE ===");
        System.out.print("Student User ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Course ID: ");
        String courseId = scanner.nextLine();

        try {
            boolean success = adminService.enrollStudentInCourse(studentId, courseId);
            if (success) {
                System.out.println("Enrollment successful!");
            } else {
                System.out.println("Enrollment failed. Please check the IDs and try again.");
            }
        } catch (RepositoryException e) {
            System.out.println("Error during enrollment: " + e.getMessage());
        }
    }
}