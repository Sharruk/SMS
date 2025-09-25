package sms.app;

import sms.data.*;
import sms.domain.*;
import sms.exceptions.*;
import sms.services.FileUploadService;
import sms.services.UploadService;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Repository<Student> studentRepository;
    private static Repository<Teacher> teacherRepository;
    private static Repository<Admin> adminRepository;
    private static Repository<Course> courseRepository;
    private static UploadService<File> uploadService;
    private static Scanner scanner;

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("   Learning Management System (LMS)     ");
        System.out.println("     Based on PlantUML Specification     ");
        System.out.println("==========================================");
        System.out.println("Initializing comprehensive LMS system...\n");

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
        // Initialize repositories with new interface
        studentRepository = new StudentRepository();
        teacherRepository = new TeacherRepository();
        adminRepository = new AdminRepository();
        courseRepository = new CourseRepository();
        
        // Initialize upload service
        uploadService = new FileUploadService();
        
        // Initialize scanner for user input
        scanner = new Scanner(System.in);
        
        System.out.println("LMS system initialized successfully!");
        System.out.println("JSON files: students.json, teachers.json, admins.json, courses.json");
        
        // Display current system statistics
        displaySystemStatistics();
    }

    /**
     * CRITICAL METHOD: Demonstrates all OOP features as required by PlantUML specification
     * This method showcases Inheritance, Polymorphism, Encapsulation, Generics, and Exception Handling
     * Plus new features: Search/Sort interfaces, Upload service, Role-based access
     */
    public static void demonstrateOopFeatures() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("     COMPREHENSIVE LMS OOP FEATURES DEMONSTRATION");
        System.out.println("        Based on PlantUML Class Diagram");
        System.out.println("=".repeat(70));

        try {
            // 1. INHERITANCE DEMONSTRATION
            System.out.println("\n1. === INHERITANCE DEMONSTRATION ===");
            System.out.println("Creating all User hierarchy objects (User -> Admin, Teacher, Student, Principal):");
            
            Admin admin = new Admin(1001, "Dr. Alice Smith", "alice@lms.edu", "alice", "admin123");
            Teacher teacher = new Teacher(2001, "Prof. Bob Johnson", "bob@lms.edu", "bob", "teacher123");
            Student student = new Student(3001, "Charlie Brown", "charlie@student.edu", "charlie", "student123");
            Principal principal = new Principal(4001, "Dr. Diana Wilson", "diana@lms.edu", "diana", "principal123");
            
            System.out.println("Admin created: " + admin.getName() + " (inherits from User)");
            System.out.println("Teacher created: " + teacher.getName() + " (inherits from User)");
            System.out.println("Student created: " + student.getName() + " (inherits from User)");
            System.out.println("Principal created: " + principal.getName() + " (inherits from User)");

            // 2. POLYMORPHISM DEMONSTRATION
            System.out.println("\n2. === POLYMORPHISM DEMONSTRATION ===");
            System.out.println("Using polymorphism with User reference to call overridden methods:");
            
            User[] users = {admin, teacher, student, principal}; // Polymorphic array
            
            for (User user : users) {
                System.out.println("\nUser Type: " + user.getClass().getSimpleName());
                System.out.println("Role: " + user.getRole()); // Polymorphic method call
                try {
                    user.login(); // Polymorphic method call
                    user.logout(); // Polymorphic method call
                } catch (AuthenticationException e) {
                    e.log();
                }
            }

            // 3. ENCAPSULATION DEMONSTRATION
            System.out.println("\n3. === ENCAPSULATION DEMONSTRATION ===");
            System.out.println("Demonstrating encapsulation with private fields and public getters/setters:");
            
            System.out.println("Original student ID: " + student.getUserId());
            student.setUserId(3002); // Using setter (encapsulation)
            System.out.println("Updated student ID: " + student.getUserId()); // Using getter (encapsulation)
            
            System.out.println("Teacher email (encapsulated): " + teacher.getEmail());
            teacher.setEmail("bob.johnson@lms.edu"); // Using setter (encapsulation)
            System.out.println("Updated teacher email: " + teacher.getEmail()); // Using getter (encapsulation)

            // 4. GENERICS DEMONSTRATION
            System.out.println("\n4. === GENERICS DEMONSTRATION ===");
            System.out.println("Using Generic Repository<T> interface with different types:");
            
            // Save entities using generic repository methods
            Repository<Student> studentRepo = studentRepository;
            Repository<Teacher> teacherRepo = teacherRepository;
            Repository<Admin> adminRepo = adminRepository;
            Repository<Course> courseRepo = courseRepository;
            
            System.out.println("Saving entities using Repository<T> generics...");
            studentRepo.add(student);
            teacherRepo.add(teacher);
            adminRepo.add(admin);
            
            Course course = new Course("LMS101", "Object-Oriented Programming", 3, "Prof. Bob Johnson", "Mon/Wed/Fri", "10:00-11:00", "Fall 2025");
            courseRepo.add(course);
            
            System.out.println("Generic repositories working with different types successfully!");
            
            // Demonstrate generic method calls
            long studentCount = studentRepo.getAll().size();
            long teacherCount = teacherRepo.getAll().size();
            long adminCount = adminRepo.getAll().size();
            long courseCount = courseRepo.getAll().size();
            System.out.println("Repository counts - Students: " + studentCount + ", Teachers: " + teacherCount + 
                              ", Admins: " + adminCount + ", Courses: " + courseCount);

            // 5. CUSTOM EXCEPTION HANDLING DEMONSTRATION
            System.out.println("\n5. === CUSTOM EXCEPTION HIERARCHY DEMONSTRATION ===");
            System.out.println("Demonstrating comprehensive exception hierarchy with try-catch-finally:");
            
            // ValidationException Demo
            try {
                System.out.println("Testing ValidationException...");
                admin.registerStudent("", "invalid-email");
            } catch (ValidationException e) {
                System.out.println("Caught ValidationException:");
                System.out.println("  Field: " + e.getFieldName());
                System.out.println("  Invalid Value: " + e.getInvalidValue());
                e.log();
            } finally {
                System.out.println("  Validation test completed (finally block executed)");
            }
            
            // NotFoundException Demo
            try {
                System.out.println("\nTesting NotFoundException...");
                admin.assignBatch(null, new Batch("2024-2028"));
            } catch (NotFoundException e) {
                System.out.println("Caught NotFoundException:");
                System.out.println("  Entity Type: " + e.getEntityType());
                System.out.println("  Search Criteria: " + e.getSearchCriteria());
                e.log();
            } finally {
                System.out.println("  NotFound test completed (finally block executed)");
            }

            // AuthenticationException Demo
            try {
                System.out.println("\nTesting AuthenticationException...");
                User invalidUser = new Student(9999, "Invalid User", "invalid@test.com", "", "");
                invalidUser.login();
            } catch (AuthenticationException e) {
                System.out.println("Caught AuthenticationException:");
                System.out.println("  Error Code: " + e.getErrorCode());
                System.out.println("  Username: " + e.getUsername());
                e.log();
            } finally {
                System.out.println("  Authentication test completed (finally block executed)");
            }

            // 6. SEARCH AND SORT INTERFACES DEMONSTRATION
            System.out.println("\n6. === SEARCH AND SORT INTERFACES DEMONSTRATION ===");
            System.out.println("Demonstrating role-based search and sort capabilities:");
            
            // Admin can search everything
            System.out.println("\nAdmin search capabilities (FULL ACCESS):");
            List<Object> adminSearchResults = admin.search("Bob");
            System.out.println("Admin found " + adminSearchResults.size() + " results for 'Bob'");
            
            List<Object> adminSortResults = admin.sort("name");
            System.out.println("Admin sorted " + adminSortResults.size() + " items by name");
            
            // Teacher can search their students
            System.out.println("\nTeacher search capabilities (RESTRICTED ACCESS):");
            teacher.addStudent(student);
            List<Student> teacherSearchResults = teacher.search("Charlie");
            System.out.println("Teacher found " + teacherSearchResults.size() + " students for 'Charlie'");
            
            List<Student> teacherSortResults = teacher.sort("name");
            System.out.println("Teacher sorted " + teacherSortResults.size() + " students by name");
            
            // Student can search their courses
            System.out.println("\nStudent search capabilities (LIMITED ACCESS):");
            student.addCourse(course);
            List<Course> studentSearchResults = student.search("Programming");
            System.out.println("Student found " + studentSearchResults.size() + " courses for 'Programming'");
            
            List<Course> studentSortResults = student.sort("name");
            System.out.println("Student sorted " + studentSortResults.size() + " courses by name");

            // 7. UPLOAD SERVICE DEMONSTRATION
            System.out.println("\n7. === UPLOAD SERVICE DEMONSTRATION ===");
            System.out.println("Demonstrating file upload with validation and exception handling:");
            
            try {
                // Create a test file for demonstration
                File testFile = new File("test_upload.txt");
                testFile.createNewFile();
                
                System.out.println("User attempting to upload file...");
                student.upload(testFile);
                
                // Clean up
                testFile.delete();
                
            } catch (UploadException e) {
                System.out.println("Caught UploadException:");
                System.out.println("  File: " + e.getFileName());
                System.out.println("  Type: " + e.getFileType());
                e.log();
            } catch (ValidationException e) {
                System.out.println("Caught ValidationException during upload:");
                e.log();
            } catch (Exception e) {
                System.out.println("Caught general exception: " + e.getMessage());
            } finally {
                System.out.println("  Upload test completed (finally block executed)");
            }

            // 8. ROLE-BASED ACCESS CONTROL DEMONSTRATION
            System.out.println("\n8. === ROLE-BASED ACCESS CONTROL DEMONSTRATION ===");
            System.out.println("Demonstrating role-specific capabilities:");
            
            // Principal capabilities
            System.out.println("\nPrincipal capabilities:");
            principal.addAdmin(admin);
            principal.appointTeacher(teacher);
            principal.assignCourse(teacher, course);
            principal.viewReports();
            
            // Admin capabilities
            System.out.println("Admin capabilities:");
            admin.manageCourse();
            admin.manageStudent();
            
            // Teacher capabilities
            System.out.println("Teacher capabilities:");
            teacher.viewStudents();
            teacher.viewAllAttendance();
            
            // Student capabilities
            System.out.println("Student capabilities:");
            student.viewAssignment();
            student.viewGrades();

            // 9. COMPREHENSIVE FEATURE INTEGRATION
            System.out.println("\n9. === COMPREHENSIVE INTEGRATION ===");
            System.out.println("All LMS features working together:");
            
            // Use polymorphism, generics, exceptions, search/sort together
            try {
                List<Student> allStudents = studentRepository.getAll(); // Generics
                System.out.println("Retrieved " + allStudents.size() + " students using generic repository");
                
                for (Student s : allStudents) {
                    User userRef = s; // Polymorphism
                    System.out.println("  - " + userRef.getName() + " (" + userRef.getRole() + ")");
                    
                    // Demonstrate search capability
                    List<Course> studentCourses = s.search(""); // Search interface
                    System.out.println("    Enrolled in " + studentCourses.size() + " courses");
                }
                
            } catch (RepositoryException e) { // Custom exception handling
                System.out.println("Error retrieving students: " + e.getMessage());
                e.log();
            }

        } catch (Exception e) {
            System.err.println("Error in comprehensive OOP demonstration: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n" + "=".repeat(70));
        System.out.println("   COMPREHENSIVE LMS OOP FEATURES DEMONSTRATION COMPLETED");
        System.out.println("✓ Inheritance: Full User hierarchy (Admin, Teacher, Student, Principal)");
        System.out.println("✓ Polymorphism: Overridden methods called via User reference");
        System.out.println("✓ Encapsulation: Private fields with public getters/setters");
        System.out.println("✓ Generics: Repository<T> interface with type safety");
        System.out.println("✓ Exception Handling: Complete exception hierarchy with try-catch-finally");
        System.out.println("✓ Search/Sort: Role-based search and sort interfaces");
        System.out.println("✓ Upload Service: File upload with validation and error handling");
        System.out.println("✓ Role-based Access: Principal, Admin, Teacher, Student capabilities");
        System.out.println("=".repeat(70) + "\n");
    }

    private static void runMainMenu() {
        while (true) {
            try {
                displayMainMenu();
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        handleRoleSelection();
                        break;
                    case 2:
                        demonstrateSearchSort();
                        break;
                    case 3:
                        demonstrateFileUpload();
                        break;
                    case 4:
                        manageUsers();
                        break;
                    case 5:
                        manageCourses();
                        break;
                    case 6:
                        displaySystemStatistics();
                        break;
                    case 7:
                        demonstrateOopFeatures(); // Allow re-running the demo
                        break;
                    case 0:
                        System.out.println("Thank you for using the Learning Management System!");
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
        System.out.println("\n" + "=".repeat(50));
        System.out.println("         LMS MAIN MENU");
        System.out.println("=".repeat(50));
        System.out.println("1. Role-based Access Demo");
        System.out.println("2. Search & Sort Demo");
        System.out.println("3. File Upload Demo");
        System.out.println("4. User Management");
        System.out.println("5. Course Management");
        System.out.println("6. System Statistics");
        System.out.println("7. Complete OOP Features Demo");
        System.out.println("0. Exit");
        System.out.println("=".repeat(50));
        System.out.print("Choose an option: ");
    }

    private static void handleRoleSelection() {
        System.out.println("\n=== ROLE-BASED ACCESS DEMONSTRATION ===");
        System.out.println("Select a role to demonstrate:");
        System.out.println("1. Principal");
        System.out.println("2. Admin");
        System.out.println("3. Teacher");
        System.out.println("4. Student");
        System.out.print("Choose role: ");
        
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    demonstratePrincipalAccess();
                    break;
                case 2:
                    demonstrateAdminAccess();
                    break;
                case 3:
                    demonstrateTeacherAccess();
                    break;
                case 4:
                    demonstrateStudentAccess();
                    break;
                default:
                    System.out.println("Invalid role selection.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    private static void demonstratePrincipalAccess() {
        System.out.println("\n--- Principal Access Demo ---");
        Principal principal = new Principal(5001, "Dr. Principal", "principal@lms.edu", "principal", "pass123");
        Admin admin = new Admin(5002, "Admin User", "admin@lms.edu", "admin", "pass123");
        Teacher teacher = new Teacher(5003, "Teacher User", "teacher@lms.edu", "teacher", "pass123");
        
        principal.addAdmin(admin);
        principal.appointTeacher(teacher);
        principal.viewReports();
    }

    private static void demonstrateAdminAccess() {
        System.out.println("\n--- Admin Access Demo ---");
        try {
            Admin admin = new Admin(6001, "Admin Demo", "admin@lms.edu", "admin", "pass123");
            Student student = admin.registerStudent("John Doe", "john@student.edu");
            admin.manageStudent();
            admin.manageCourse();
            
            // Demonstrate search capability
            List<Object> results = admin.search("John");
            System.out.println("Admin search results: " + results.size() + " items found");
            
        } catch (ValidationException e) {
            System.out.println("Validation error: " + e.getMessage());
            e.log();
        }
    }

    private static void demonstrateTeacherAccess() {
        System.out.println("\n--- Teacher Access Demo ---");
        try {
            Teacher teacher = new Teacher(7001, "Teacher Demo", "teacher@lms.edu", "teacher", "pass123");
            teacher.createAssignment();
            teacher.viewStudents();
            teacher.viewAllAttendance();
            
            // Demonstrate search capability
            List<Student> results = teacher.search("Demo");
            System.out.println("Teacher search results: " + results.size() + " students found");
            
        } catch (ValidationException e) {
            System.out.println("Validation error: " + e.getMessage());
            e.log();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void demonstrateStudentAccess() {
        System.out.println("\n--- Student Access Demo ---");
        try {
            Student student = new Student(8001, "Student Demo", "student@lms.edu", "student", "pass123");
            student.viewAssignment();
            student.viewGrades();
            student.viewAttendance();
            
            // Demonstrate search capability
            List<Course> results = student.search("Demo");
            System.out.println("Student search results: " + results.size() + " courses found");
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void demonstrateSearchSort() {
        System.out.println("\n=== SEARCH & SORT DEMONSTRATION ===");
        System.out.println("Demonstrating role-based search and sort capabilities...");
        
        Admin admin = new Admin(9001, "Search Admin", "search@lms.edu", "search", "pass123");
        System.out.println("Admin searching for 'Demo': " + admin.search("Demo").size() + " results");
        System.out.println("Admin sorting by name: " + admin.sort("name").size() + " items");
    }

    private static void demonstrateFileUpload() {
        System.out.println("\n=== FILE UPLOAD DEMONSTRATION ===");
        try {
            File testFile = new File("demo_upload.txt");
            testFile.createNewFile();
            
            Student student = new Student(10001, "Upload Demo", "upload@student.edu", "upload", "pass123");
            student.upload(testFile);
            
            testFile.delete();
            
        } catch (Exception e) {
            System.out.println("Upload demonstration error: " + e.getMessage());
        }
    }

    private static void manageUsers() {
        System.out.println("\n=== USER MANAGEMENT ===");
        try {
            System.out.println("Students: " + studentRepository.getAll().size());
            System.out.println("Teachers: " + teacherRepository.getAll().size());
            System.out.println("Admins: " + adminRepository.getAll().size());
        } catch (RepositoryException e) {
            System.out.println("Error retrieving user data: " + e.getMessage());
            e.log();
        }
    }

    private static void manageCourses() {
        System.out.println("\n=== COURSE MANAGEMENT ===");
        try {
            List<Course> courses = courseRepository.getAll();
            System.out.println("Total courses: " + courses.size());
            for (Course course : courses) {
                course.displayCourseInfo();
            }
        } catch (RepositoryException e) {
            System.out.println("Error retrieving course data: " + e.getMessage());
            e.log();
        }
    }

    private static void displaySystemStatistics() {
        try {
            long studentCount = studentRepository.getAll().size();
            long teacherCount = teacherRepository.getAll().size();
            long adminCount = adminRepository.getAll().size();
            long courseCount = courseRepository.getAll().size();

            System.out.println("\n=== LMS SYSTEM STATISTICS ===");
            System.out.println("Total Students: " + studentCount);
            System.out.println("Total Teachers: " + teacherCount);
            System.out.println("Total Admins: " + adminCount);
            System.out.println("Total Courses: " + courseCount);
            System.out.println("Data files: students.json, teachers.json, admins.json, courses.json");
            System.out.println("Upload directory: uploads/");
            System.out.println("==============================\n");
        } catch (RepositoryException e) {
            System.out.println("Error retrieving system statistics: " + e.getMessage());
            e.log();
        }
    }
}