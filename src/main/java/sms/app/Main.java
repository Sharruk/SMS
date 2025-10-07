package sms.app;

import sms.data.*;
import sms.domain.*;
import sms.exceptions.*;
import sms.services.FileUploadService;
import sms.services.UploadService;
import sms.validation.InputValidator;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Repository<Student> studentRepository;
    private static Repository<Teacher> teacherRepository;
    private static Repository<Admin> adminRepository;
    private static Repository<Course> courseRepository;
    private static MessageRepository messageRepository;
    private static AssignmentRepository assignmentRepository;
    private static GradeRepository gradeRepository;
    private static SubmissionRepository submissionRepository;
    private static UploadService<File> uploadService;
    private static Scanner scanner;

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("   Learning Management System (LMS)     ");
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
        messageRepository = new MessageRepository();
        assignmentRepository = new AssignmentRepository();
        gradeRepository = new GradeRepository();
        submissionRepository = new SubmissionRepository();
        
        // Initialize upload service
        uploadService = new FileUploadService();
        
        // Initialize scanner for user input
        scanner = new Scanner(System.in);
        
        System.out.println("LMS system initialized successfully!");
        System.out.println("JSON files: students.json, teachers.json, admins.json, courses.json, messages.json, assignments.json, grades.json");
        
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
        System.out.println("=".repeat(70));

        try {
            // 1. INHERITANCE DEMONSTRATION
            System.out.println("\n1. === INHERITANCE DEMONSTRATION ===");
            System.out.println("Creating all User hierarchy objects (User -> Admin, Teacher, Student, Principal):");
            
            Admin admin = new Admin(1001, "Alice Smith", "alice@lms.edu", "alice", "admin123");
            Teacher teacher = new Teacher(2001, "Bob Johnson", "bob@lms.edu", "bob", "teacher123");
            Student student = new Student(3001, "Charlie Brown", "charlie@student.edu", "charlie", "student123");
            Principal principal = new Principal(4001, "Diana Wilson", "diana@lms.edu", "diana", "principal123");
            
            System.out.println("✓ Admin created: " + admin.getName() + " (inherits from User)");
            System.out.println("✓ Teacher created: " + teacher.getName() + " (inherits from User)");
            System.out.println("✓ Student created: " + student.getName() + " (inherits from User)");
            System.out.println("✓ Principal created: " + principal.getName() + " (inherits from User)");

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
            System.out.println("(Demo uses in-memory objects only - no persistent storage pollution)");
            
            // Demonstrate type-safe generic repository interface
            Repository<Student> studentRepo = studentRepository;
            Repository<Teacher> teacherRepo = teacherRepository;
            Repository<Admin> adminRepo = adminRepository;
            Repository<Course> courseRepo = courseRepository;
            
            System.out.println("Generic Repository<T> interface supports type-safe operations:");
            System.out.println("  - Repository<Student> for student data");
            System.out.println("  - Repository<Teacher> for teacher data");
            System.out.println("  - Repository<Admin> for admin data");
            System.out.println("  - Repository<Course> for course data");
            
            Course course = new Course("LMS101", "Object-Oriented Programming", 3, "Prof. Bob Johnson", "Mon/Wed/Fri", "10:00-11:00", "Fall 2025");
            
            System.out.println("Generic repositories working with different types successfully!");
            
            // Show current repository counts (from persistent storage)
            long studentCount = studentRepo.getAll().size();
            long teacherCount = teacherRepo.getAll().size();
            long adminCount = adminRepo.getAll().size();
            long courseCount = courseRepo.getAll().size();
            System.out.println("Current repository counts - Students: " + studentCount + ", Teachers: " + teacherCount + 
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
        System.out.println("\n--- Principal Access Mode ---");
        System.out.println("Would you like to:");
        System.out.println("1. Run Quick Demo (automated)");
        System.out.println("2. Interactive Principal Menu");
        System.out.print("Choose: ");
        
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 1) {
                runQuickPrincipalDemo();
            } else if (choice == 2) {
                runPrincipalMenu();
            } else {
                System.out.println("Invalid choice.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    private static void runQuickPrincipalDemo() {
        System.out.println("\n--- Quick Principal Demo ---");
        try {
            Principal principal = new Principal(5001, "Principal Smith", "principal@lms.edu", "principal", "pass123");
            Admin admin = new Admin(5002, "Admin User", "admin@lms.edu", "admin", "pass123");
            Teacher teacher = new Teacher(5003, "Teacher User", "teacher@lms.edu", "teacher", "pass123");
            
            principal.addAdmin(admin);
            principal.appointTeacher(teacher);
            principal.viewReports();
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
            e.log();
        }
    }

    private static void runPrincipalMenu() {
        try {
            Principal principal = new Principal(1, "Principal Davis", "principal@lms.edu", "principal", "pass123");
            
            System.out.println("\n" + "=".repeat(60));
            System.out.println("   Welcome, " + principal.getName() + " - Principal Account");
            System.out.println("   All operations work directly with persistent storage");
            System.out.println("=".repeat(60));
            
            runPrincipalMenuLoop(principal);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
            e.log();
        }
    }
    
    private static void runPrincipalMenuLoop(Principal principal) {
        while (true) {
            try {
                principal.showMenu();
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        handleAdminManagement(principal);
                        break;
                    case 2:
                        handleTeacherManagement(principal);
                        break;
                    case 3:
                        handleCourseManagement(principal);
                        break;
                    case 4:
                        viewAllStudentsFromRepository();
                        break;
                    case 5:
                        viewPrincipalReportsFromRepositories(principal);
                        break;
                    case 6:
                        sendMessageFromPrincipal(principal);
                        break;
                    case 0:
                        System.out.println("Logging out from Principal account...");
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

    private static void loadRepositoryDataIntoPrincipal(Principal principal) {
        try {
            List<Admin> allAdmins = adminRepository.getAll();
            for (Admin admin : allAdmins) {
                principal.getManagedAdmins().add(admin);
            }
            
            List<Teacher> allTeachers = teacherRepository.getAll();
            for (Teacher teacher : allTeachers) {
                principal.getAppointedTeachers().add(teacher);
            }
            
            List<Course> allCourses = courseRepository.getAll();
            for (Course course : allCourses) {
                principal.getManagedCourses().add(course);
            }
            
            System.out.println("Loaded existing data from repositories:");
            System.out.println("  - " + allAdmins.size() + " admins");
            System.out.println("  - " + allTeachers.size() + " teachers");
            System.out.println("  - " + allCourses.size() + " courses");
        } catch (Exception e) {
            System.out.println("Warning: Could not load repository data: " + e.getMessage());
        }
    }

    private static void viewPrincipalReportsFromRepositories(Principal principal) {
        try {
            List<Admin> allAdmins = adminRepository.getAll();
            List<Teacher> allTeachers = teacherRepository.getAll();
            List<Course> allCourses = courseRepository.getAll();
            List<Student> allStudents = studentRepository.getAll();
            
            System.out.println("\n" + "=".repeat(60));
            System.out.println("         PRINCIPAL'S SYSTEM REPORTS & STATISTICS");
            System.out.println("=".repeat(60));
            System.out.println("Total Admins: " + allAdmins.size());
            System.out.println("Total Teachers: " + allTeachers.size());
            System.out.println("Total Courses: " + allCourses.size());
            System.out.println("Total Students: " + allStudents.size());
            
            System.out.println("\nAdmins:");
            if (allAdmins.isEmpty()) {
                System.out.println("  No admins found.");
            } else {
                for (Admin admin : allAdmins) {
                    System.out.println("  - " + admin.getName() + " (ID: " + admin.getUserId() + ")");
                }
            }
            
            System.out.println("\nTeachers:");
            if (allTeachers.isEmpty()) {
                System.out.println("  No teachers found.");
            } else {
                for (Teacher teacher : allTeachers) {
                    System.out.println("  - " + teacher.getName() + " (ID: " + teacher.getUserId() + 
                                     ", Courses: " + teacher.getCourses().size() + ")");
                }
            }

            System.out.println("\nCourses:");
            if (allCourses.isEmpty()) {
                System.out.println("  No courses found.");
            } else {
                for (Course course : allCourses) {
                    System.out.println("  - " + course.getCourseName() + " (" + course.getCourseId() + 
                                     ") - Faculty: " + (course.getFacultyName() != null ? course.getFacultyName() : "Unassigned"));
                }
            }

            System.out.println("\nStudents:");
            if (allStudents.isEmpty()) {
                System.out.println("  No students found.");
            } else {
                for (Student student : allStudents) {
                    System.out.println("  - " + student.getName() + " (ID: " + student.getUserId() + 
                                     ", Courses: " + student.getCourses().size() + ")");
                }
            }
            System.out.println("=".repeat(60) + "\n");
        } catch (Exception e) {
            System.out.println("Error retrieving reports: " + e.getMessage());
        }
    }

    private static void handleAdminManagement(Principal principal) {
        while (true) {
            try {
                principal.showAdminManagementMenu();
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        addNewAdmin(principal);
                        break;
                    case 2:
                        viewAllAdminsFromRepository();
                        break;
                    case 3:
                        removeAdminById(principal);
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static void viewAllAdminsFromRepository() {
        try {
            List<Admin> admins = adminRepository.getAll();
            System.out.println("\n=== All Admins ===");
            if (admins.isEmpty()) {
                System.out.println("No admins found.");
            } else {
                for (int i = 0; i < admins.size(); i++) {
                    Admin admin = admins.get(i);
                    System.out.println((i + 1) + ". " + admin.getName() + " (ID: " + admin.getUserId() + 
                                     ", Email: " + admin.getEmail() + ")");
                }
            }
            System.out.println("Total Admins: " + admins.size());
            System.out.println("==================\n");
        } catch (Exception e) {
            System.out.println("Error retrieving admins: " + e.getMessage());
        }
    }

    private static void addNewAdmin(Principal principal) {
        try {
            int userId = 0;
            while (true) {
                System.out.print("Enter Admin ID: ");
                String idInput = scanner.nextLine();
                try {
                    InputValidator.validateNumericId(idInput);
                    userId = Integer.parseInt(idInput.trim());
                    break;
                } catch (ValidationException e) {
                    System.out.println("⚠️ Invalid ID: ID must be a number. Please try again.");
                }
            }
            
            String name = "";
            while (true) {
                System.out.print("Enter Name: ");
                name = scanner.nextLine();
                try {
                    InputValidator.validateName(name);
                    break;
                } catch (ValidationException e) {
                    System.out.println("⚠️ Invalid input: Name should contain only letters and spaces. Please try again.");
                }
            }
            
            String email = "";
            while (true) {
                System.out.print("Enter Email: ");
                email = scanner.nextLine();
                try {
                    InputValidator.validateEmail(email);
                    break;
                } catch (ValidationException e) {
                    System.out.println("⚠️ Invalid input: Enter a valid email format. Please try again.");
                }
            }
            
            String username = "";
            while (true) {
                System.out.print("Enter Username: ");
                username = scanner.nextLine();
                try {
                    InputValidator.validateUsername(username);
                    break;
                } catch (ValidationException e) {
                    System.out.println("⚠️ Username cannot be empty. Please try again.");
                }
            }
            
            String password = "";
            while (true) {
                System.out.print("Enter Password: ");
                password = scanner.nextLine();
                try {
                    InputValidator.validatePassword(password);
                    break;
                } catch (ValidationException e) {
                    System.out.println("⚠️ Password must be at least 4 characters. Please try again.");
                }
            }
            
            Admin admin = new Admin(userId, name, email, username, password);
            adminRepository.add(admin);
            System.out.println("✓ Principal: Added admin " + admin.getName() + " (ID: " + admin.getUserId() + ")");
            
        } catch (Exception e) {
            System.out.println("Error adding admin: " + e.getMessage());
        }
    }

    private static void removeAdminById(Principal principal) {
        try {
            System.out.print("Enter Admin ID to remove: ");
            int userId = Integer.parseInt(scanner.nextLine());
            
            List<Admin> allAdmins = adminRepository.getAll();
            Admin toRemove = null;
            for (Admin admin : allAdmins) {
                if (admin.getUserId() == userId) {
                    toRemove = admin;
                    break;
                }
            }
            
            if (toRemove != null) {
                adminRepository.delete(toRemove);
                System.out.println("✓ Principal: Removed admin " + toRemove.getName() + " (ID: " + userId + ")");
            } else {
                System.out.println("✗ Principal: Admin with ID " + userId + " not found");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Please enter a number.");
        } catch (Exception e) {
            System.out.println("Error removing admin: " + e.getMessage());
        }
    }

    private static void handleTeacherManagement(Principal principal) {
        while (true) {
            try {
                principal.showTeacherManagementMenu();
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        appointNewTeacher(principal);
                        break;
                    case 2:
                        viewAllTeachersFromRepository();
                        break;
                    case 3:
                        removeTeacherById(principal);
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static void viewAllTeachersFromRepository() {
        try {
            List<Teacher> teachers = teacherRepository.getAll();
            System.out.println("\n=== All Teachers ===");
            if (teachers.isEmpty()) {
                System.out.println("No teachers found.");
            } else {
                for (int i = 0; i < teachers.size(); i++) {
                    Teacher teacher = teachers.get(i);
                    System.out.println((i + 1) + ". " + teacher.getName() + " (ID: " + teacher.getUserId() + 
                                     ", Email: " + teacher.getEmail() + ", Courses: " + teacher.getCourses().size() + ")");
                }
            }
            System.out.println("Total Teachers: " + teachers.size());
            System.out.println("====================\n");
        } catch (Exception e) {
            System.out.println("Error retrieving teachers: " + e.getMessage());
        }
    }

    private static void appointNewTeacher(Principal principal) {
        try {
            int userId = 0;
            while (true) {
                System.out.print("Enter Teacher ID: ");
                String idInput = scanner.nextLine();
                try {
                    InputValidator.validateNumericId(idInput);
                    userId = Integer.parseInt(idInput.trim());
                    break;
                } catch (ValidationException e) {
                    System.out.println("⚠️ Invalid ID: ID must be a number. Please try again.");
                }
            }
            
            String name = "";
            while (true) {
                System.out.print("Enter Name: ");
                name = scanner.nextLine();
                try {
                    InputValidator.validateName(name);
                    break;
                } catch (ValidationException e) {
                    System.out.println("⚠️ Invalid input: Name should contain only letters and spaces. Please try again.");
                }
            }
            
            String email = "";
            while (true) {
                System.out.print("Enter Email: ");
                email = scanner.nextLine();
                try {
                    InputValidator.validateEmail(email);
                    break;
                } catch (ValidationException e) {
                    System.out.println("⚠️ Invalid input: Enter a valid email format. Please try again.");
                }
            }
            
            String username = "";
            while (true) {
                System.out.print("Enter Username: ");
                username = scanner.nextLine();
                try {
                    InputValidator.validateUsername(username);
                    break;
                } catch (ValidationException e) {
                    System.out.println("⚠️ Username cannot be empty. Please try again.");
                }
            }
            
            String password = "";
            while (true) {
                System.out.print("Enter Password: ");
                password = scanner.nextLine();
                try {
                    InputValidator.validatePassword(password);
                    break;
                } catch (ValidationException e) {
                    System.out.println("⚠️ Password must be at least 4 characters. Please try again.");
                }
            }
            
            Teacher teacher = new Teacher(userId, name, email, username, password);
            teacherRepository.add(teacher);
            System.out.println("✓ Principal: Appointed teacher " + teacher.getName() + " (ID: " + teacher.getUserId() + ")");
            
        } catch (Exception e) {
            System.out.println("Error appointing teacher: " + e.getMessage());
        }
    }

    private static void removeTeacherById(Principal principal) {
        try {
            System.out.print("Enter Teacher ID to remove: ");
            int userId = Integer.parseInt(scanner.nextLine());
            
            List<Teacher> allTeachers = teacherRepository.getAll();
            Teacher toRemove = null;
            for (Teacher teacher : allTeachers) {
                if (teacher.getUserId() == userId) {
                    toRemove = teacher;
                    break;
                }
            }
            
            if (toRemove != null) {
                teacherRepository.delete(toRemove);
                System.out.println("✓ Principal: Removed teacher " + toRemove.getName() + " (ID: " + userId + ")");
            } else {
                System.out.println("✗ Principal: Teacher with ID " + userId + " not found");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Please enter a number.");
        } catch (Exception e) {
            System.out.println("Error removing teacher: " + e.getMessage());
        }
    }

    private static void handleCourseManagement(Principal principal) {
        while (true) {
            try {
                principal.showCourseManagementMenu();
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        createNewCourse(principal);
                        break;
                    case 2:
                        assignTeacherToCourseFromRepository(principal);
                        break;
                    case 3:
                        viewAllCoursesFromRepository();
                        break;
                    case 4:
                        removeCourseById(principal);
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static void viewAllCoursesFromRepository() {
        try {
            List<Course> courses = courseRepository.getAll();
            System.out.println("\n=== All Courses ===");
            if (courses.isEmpty()) {
                System.out.println("No courses found.");
            } else {
                for (int i = 0; i < courses.size(); i++) {
                    Course course = courses.get(i);
                    System.out.println((i + 1) + ". " + course.getCourseName() + 
                                     " (ID: " + course.getCourseId() + 
                                     ", Credits: " + course.getCreditHours() +
                                     ", Faculty: " + (course.getFacultyName() != null ? course.getFacultyName() : "Unassigned") + ")");
                }
            }
            System.out.println("Total Courses: " + courses.size());
            System.out.println("===================\n");
        } catch (Exception e) {
            System.out.println("Error retrieving courses: " + e.getMessage());
        }
    }

    private static void createNewCourse(Principal principal) {
        try {
            String courseId = "";
            while (true) {
                System.out.print("Enter Course ID (e.g., LMS101): ");
                courseId = scanner.nextLine();
                try {
                    InputValidator.validateCourseId(courseId);
                    break;
                } catch (ValidationException e) {
                    System.out.println("⚠️ Invalid Course ID: Course ID cannot be empty. Please try again.");
                }
            }
            
            String courseName = "";
            while (true) {
                System.out.print("Enter Course Name: ");
                courseName = scanner.nextLine();
                try {
                    InputValidator.validateCourseName(courseName);
                    break;
                } catch (ValidationException e) {
                    System.out.println("⚠️ Invalid Course Name: Course Name cannot be empty. Please try again.");
                }
            }
            
            int creditHours = 0;
            while (true) {
                System.out.print("Enter Credit Hours: ");
                String creditInput = scanner.nextLine();
                try {
                    InputValidator.validateCreditHours(creditInput);
                    creditHours = Integer.parseInt(creditInput.trim());
                    break;
                } catch (ValidationException e) {
                    System.out.println("⚠️ Invalid Credit Hours: Credit Hours must be a number. Please try again.");
                }
            }
            
            System.out.print("Enter Class Days (e.g., Mon/Wed/Fri): ");
            String classDays = scanner.nextLine();
            
            System.out.print("Enter Class Times (e.g., 10:00-11:00): ");
            String classTimes = scanner.nextLine();
            
            System.out.print("Enter Class Dates (e.g., Fall 2025): ");
            String classDates = scanner.nextLine();
            
            Course course = new Course(courseId, courseName, creditHours, "Unassigned", 
                                      classDays, classTimes, classDates);
            courseRepository.add(course);
            System.out.println("✓ Principal: Created course " + course.getCourseName() + " (ID: " + course.getCourseId() + ")");
            
        } catch (Exception e) {
            System.out.println("Error creating course: " + e.getMessage());
        }
    }

    private static void assignTeacherToCourseFromRepository(Principal principal) {
        try {
            viewAllTeachersFromRepository();
            System.out.print("Enter Teacher ID: ");
            int teacherId = Integer.parseInt(scanner.nextLine());
            
            viewAllCoursesFromRepository();
            System.out.print("Enter Course ID: ");
            String courseId = scanner.nextLine();
            
            Teacher teacher = null;
            for (Teacher t : teacherRepository.getAll()) {
                if (t.getUserId() == teacherId) {
                    teacher = t;
                    break;
                }
            }
            
            Course course = null;
            for (Course c : courseRepository.getAll()) {
                if (c.getCourseId().equals(courseId)) {
                    course = c;
                    break;
                }
            }
            
            if (teacher != null && course != null) {
                teacher.addCourse(course);
                course.setFacultyName(teacher.getName());
                
                teacherRepository.update(teacher);
                courseRepository.update(course);
                
                System.out.println("✓ Principal: Assigned course " + course.getCourseId() + 
                                  " to teacher " + teacher.getName());
            } else {
                System.out.println("✗ Teacher or Course not found.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("Error assigning course: " + e.getMessage());
        }
    }

    private static void removeCourseById(Principal principal) {
        try {
            System.out.print("Enter Course ID to remove: ");
            String courseId = scanner.nextLine();
            
            List<Course> allCourses = courseRepository.getAll();
            Course toRemove = null;
            for (Course course : allCourses) {
                if (course.getCourseId().equals(courseId)) {
                    toRemove = course;
                    break;
                }
            }
            
            if (toRemove != null) {
                courseRepository.delete(toRemove);
                System.out.println("✓ Principal: Removed course " + toRemove.getCourseName() + " (ID: " + courseId + ")");
            } else {
                System.out.println("✗ Principal: Course with ID " + courseId + " not found");
            }
        } catch (Exception e) {
            System.out.println("Error removing course: " + e.getMessage());
        }
    }

    private static void viewAllStudentsFromRepository() {
        try {
            List<Student> students = studentRepository.getAll();
            System.out.println("\n=== All Students ===");
            if (students.isEmpty()) {
                System.out.println("No students found.");
            } else {
                for (int i = 0; i < students.size(); i++) {
                    Student student = students.get(i);
                    System.out.println((i + 1) + ". " + student.getName() + 
                                     " (ID: " + student.getUserId() + 
                                     ", Email: " + student.getEmail() + 
                                     ", Courses: " + student.getCourses().size() + ")");
                }
            }
            System.out.println("Total Students: " + students.size());
            System.out.println("====================\n");
        } catch (Exception e) {
            System.out.println("Error retrieving students: " + e.getMessage());
        }
    }

    private static void demonstrateAdminAccess() {
        System.out.println("\n--- Admin Access Mode ---");
        System.out.println("Would you like to:");
        System.out.println("1. Run Quick Demo (automated)");
        System.out.println("2. Interactive Admin Menu");
        System.out.print("Choose: ");
        
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 1) {
                runQuickAdminDemo();
            } else if (choice == 2) {
                runAdminMenu();
            } else {
                System.out.println("Invalid choice.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    private static void runQuickAdminDemo() {
        System.out.println("\n--- Quick Admin Demo ---");
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
        System.out.println("\n--- Teacher Access Mode ---");
        System.out.println("Would you like to:");
        System.out.println("1. Run Quick Demo (automated)");
        System.out.println("2. Interactive Teacher Menu");
        System.out.print("Choose: ");
        
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 1) {
                runQuickTeacherDemo();
            } else if (choice == 2) {
                runTeacherMenu();
            } else {
                System.out.println("Invalid choice.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    private static void runQuickTeacherDemo() {
        System.out.println("\n--- Quick Teacher Demo ---");
        try {
            Teacher teacher = new Teacher(7001, "Teacher Demo", "teacher@lms.edu", "teacher", "pass123");
            teacher.createAssignment();
            teacher.viewStudents();
            teacher.viewAllAttendance();
            
            List<Student> results = teacher.search("Demo");
            System.out.println("Teacher search results: " + results.size() + " students found");
            
        } catch (ValidationException e) {
            System.out.println("Validation error: " + e.getMessage());
            e.log();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void runTeacherMenu() {
        try {
            List<Teacher> allTeachers = teacherRepository.getAll();
            
            if (allTeachers.isEmpty()) {
                System.out.println("\nNo teachers found in the system. Please create a teacher first.");
                System.out.print("Create a new teacher? (yes/no): ");
                String response = scanner.nextLine();
                if (response.equalsIgnoreCase("yes")) {
                    System.out.print("Enter Teacher ID: ");
                    int userId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter Username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter Password: ");
                    String password = scanner.nextLine();
                    
                    Teacher newTeacher = new Teacher(userId, name, email, username, password);
                    teacherRepository.add(newTeacher);
                    allTeachers.add(newTeacher);
                } else {
                    return;
                }
            }
            
            System.out.println("\n=== Select Teacher Account ===");
            for (int i = 0; i < allTeachers.size(); i++) {
                Teacher t = allTeachers.get(i);
                System.out.println((i + 1) + ". " + t.getName() + " (ID: " + t.getUserId() + ", Email: " + t.getEmail() + ")");
            }
            
            System.out.print("Enter selection number (1, 2, etc.) to login, or 0 to cancel: ");
            int teacherChoice = Integer.parseInt(scanner.nextLine());
            
            if (teacherChoice <= 0 || teacherChoice > allTeachers.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            
            Teacher teacher = allTeachers.get(teacherChoice - 1);
            loadTeacherCourses(teacher);
            loadStudentsForTeacher(teacher);
            
            System.out.println("\n" + "=".repeat(60));
            System.out.println("   Welcome, " + teacher.getName() + " - Teacher Account");
            System.out.println("   User ID: " + teacher.getUserId());
            System.out.println("   Courses: " + teacher.getCourses().size());
            System.out.println("   Students: " + teacher.getStudents().size());
            System.out.println("   All operations work directly with persistent storage");
            System.out.println("=".repeat(60));
            
            while (true) {
                try {
                    teacher.showMenu();
                    int choice = Integer.parseInt(scanner.nextLine());
                    
                    switch (choice) {
                        case 1:
                            handleAssignmentManagement(teacher);
                            break;
                        case 2:
                            handleGradesManagement(teacher);
                            break;
                        case 3:
                            handleMessaging(teacher);
                            break;
                        case 4:
                            teacher.showDashboard();
                            break;
                        case 5:
                            viewTeacherCourses(teacher);
                            break;
                        case 6:
                            viewTeacherStudents(teacher);
                            break;
                        case 7:
                            handleFileUpload(teacher);
                            break;
                        case 0:
                            System.out.println("Logging out from Teacher account...");
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
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void loadTeacherCourses(Teacher teacher) {
        try {
            List<Course> allCourses = courseRepository.getAll();
            for (Course course : allCourses) {
                if (String.valueOf(teacher.getUserId()).equals(course.getFacultyName()) || 
                    teacher.getName().equals(course.getFacultyName())) {
                    teacher.addCourse(course);
                }
            }
        } catch (Exception e) {
            System.out.println("Warning: Could not load courses: " + e.getMessage());
        }
    }

    private static void loadStudentsForTeacher(Teacher teacher) {
        try {
            List<Student> allStudents = studentRepository.getAll();
            for (Student student : allStudents) {
                for (Course teacherCourse : teacher.getCourses()) {
                    for (Course studentCourse : student.getCourses()) {
                        if (teacherCourse.getCourseId().equals(studentCourse.getCourseId())) {
                            teacher.addStudent(student);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Warning: Could not load students: " + e.getMessage());
        }
    }

    private static void handleAssignmentManagement(Teacher teacher) {
        while (true) {
            try {
                teacher.showAssignmentManagementMenu();
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        createAssignment(teacher);
                        break;
                    case 2:
                        viewTeacherAssignments(teacher);
                        break;
                    case 3:
                        updateAssignment(teacher);
                        break;
                    case 4:
                        deleteAssignment(teacher);
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void createAssignment(Teacher teacher) {
        try {
            if (teacher.getCourses().isEmpty()) {
                System.out.println("You have no courses assigned. Cannot create assignments.");
                return;
            }
            
            System.out.println("\n--- Create New Assignment ---");
            System.out.println("Your Courses:");
            for (int i = 0; i < teacher.getCourses().size(); i++) {
                Course c = teacher.getCourses().get(i);
                System.out.println((i + 1) + ". " + c.getCourseName() + " (" + c.getCourseId() + ")");
            }
            
            System.out.print("Select course (1, 2, etc.): ");
            int courseChoice = Integer.parseInt(scanner.nextLine());
            if (courseChoice < 1 || courseChoice > teacher.getCourses().size()) {
                System.out.println("Invalid course selection.");
                return;
            }
            
            Course selectedCourse = teacher.getCourses().get(courseChoice - 1);
            
            System.out.print("Enter Assignment Title: ");
            String title = scanner.nextLine();
            
            System.out.print("Enter Description: ");
            String description = scanner.nextLine();
            
            System.out.print("Enter Due Date (YYYY-MM-DD): ");
            String dueDate = scanner.nextLine();
            
            int assignmentId = assignmentRepository.getNextAssignmentId();
            Assignment assignment = new Assignment(assignmentId, selectedCourse.getCourseId(), 
                                                  teacher.getUserId(), title, description, dueDate);
            
            assignmentRepository.add(assignment);
            System.out.println("Assignment created successfully! ID: " + assignmentId);
            
        } catch (Exception e) {
            System.out.println("Error creating assignment: " + e.getMessage());
        }
    }

    private static void viewTeacherAssignments(Teacher teacher) {
        try {
            List<Assignment> assignments = assignmentRepository.getAssignmentsByTeacherId(teacher.getUserId());
            
            if (assignments.isEmpty()) {
                System.out.println("\nNo assignments found.");
                return;
            }
            
            System.out.println("\n=== Your Assignments ===");
            for (Assignment a : assignments) {
                System.out.println("ID: " + a.getId());
                System.out.println("  Course: " + a.getCourseId());
                System.out.println("  Title: " + a.getTitle());
                System.out.println("  Description: " + a.getDescription());
                System.out.println("  Due Date: " + a.getDueDate());
                System.out.println();
            }
            System.out.println("Total: " + assignments.size() + " assignments");
            
        } catch (Exception e) {
            System.out.println("Error viewing assignments: " + e.getMessage());
        }
    }

    private static void updateAssignment(Teacher teacher) {
        try {
            List<Assignment> assignments = assignmentRepository.getAssignmentsByTeacherId(teacher.getUserId());
            
            if (assignments.isEmpty()) {
                System.out.println("No assignments to update.");
                return;
            }
            
            System.out.println("\n--- Update Assignment ---");
            for (int i = 0; i < assignments.size(); i++) {
                Assignment a = assignments.get(i);
                System.out.println((i + 1) + ". " + a.getTitle() + " (Course: " + a.getCourseId() + ", Due: " + a.getDueDate() + ")");
            }
            
            System.out.print("Select assignment to update (1, 2, etc.): ");
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice < 1 || choice > assignments.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            
            Assignment assignment = assignments.get(choice - 1);
            
            System.out.print("Enter new title (or press Enter to keep '" + assignment.getTitle() + "'): ");
            String title = scanner.nextLine();
            if (!title.trim().isEmpty()) {
                assignment.setTitle(title);
            }
            
            System.out.print("Enter new description (or press Enter to keep current): ");
            String description = scanner.nextLine();
            if (!description.trim().isEmpty()) {
                assignment.setDescription(description);
            }
            
            System.out.print("Enter new due date (or press Enter to keep '" + assignment.getDueDate() + "'): ");
            String dueDate = scanner.nextLine();
            if (!dueDate.trim().isEmpty()) {
                assignment.setDueDate(dueDate);
            }
            
            assignmentRepository.update(assignment);
            System.out.println("Assignment updated successfully!");
            
        } catch (Exception e) {
            System.out.println("Error updating assignment: " + e.getMessage());
        }
    }

    private static void deleteAssignment(Teacher teacher) {
        try {
            List<Assignment> assignments = assignmentRepository.getAssignmentsByTeacherId(teacher.getUserId());
            
            if (assignments.isEmpty()) {
                System.out.println("No assignments to delete.");
                return;
            }
            
            System.out.println("\n--- Delete Assignment ---");
            for (int i = 0; i < assignments.size(); i++) {
                Assignment a = assignments.get(i);
                System.out.println((i + 1) + ". " + a.getTitle() + " (Course: " + a.getCourseId() + ")");
            }
            
            System.out.print("Select assignment to delete (1, 2, etc.): ");
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice < 1 || choice > assignments.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            
            Assignment assignment = assignments.get(choice - 1);
            System.out.print("Are you sure you want to delete '" + assignment.getTitle() + "'? (yes/no): ");
            String confirm = scanner.nextLine();
            
            if (confirm.equalsIgnoreCase("yes")) {
                assignmentRepository.delete(assignment);
                System.out.println("Assignment deleted successfully!");
            } else {
                System.out.println("Deletion cancelled.");
            }
            
        } catch (Exception e) {
            System.out.println("Error deleting assignment: " + e.getMessage());
        }
    }

    private static void handleGradesManagement(Teacher teacher) {
        while (true) {
            try {
                teacher.showGradesManagementMenu();
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        assignGrade(teacher);
                        break;
                    case 2:
                        updateGrade(teacher);
                        break;
                    case 3:
                        viewGradesByCourse(teacher);
                        break;
                    case 4:
                        viewAllTeacherGrades(teacher);
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void assignGrade(Teacher teacher) {
        try {
            if (teacher.getCourses().isEmpty()) {
                System.out.println("You have no courses assigned.");
                return;
            }
            
            if (teacher.getStudents().isEmpty()) {
                System.out.println("You have no students enrolled in your courses.");
                return;
            }
            
            System.out.println("\n--- Assign Grade ---");
            System.out.println("Your Courses:");
            for (int i = 0; i < teacher.getCourses().size(); i++) {
                Course c = teacher.getCourses().get(i);
                System.out.println((i + 1) + ". " + c.getCourseName() + " (" + c.getCourseId() + ")");
            }
            
            System.out.print("Select course (1, 2, etc.): ");
            int courseChoice = Integer.parseInt(scanner.nextLine());
            if (courseChoice < 1 || courseChoice > teacher.getCourses().size()) {
                System.out.println("Invalid course selection.");
                return;
            }
            
            Course selectedCourse = teacher.getCourses().get(courseChoice - 1);
            
            System.out.println("\nStudents in your courses:");
            for (int i = 0; i < teacher.getStudents().size(); i++) {
                Student s = teacher.getStudents().get(i);
                System.out.println((i + 1) + ". " + s.getName() + " (ID: " + s.getUserId() + ")");
            }
            
            System.out.print("Select student (1, 2, etc.): ");
            int studentChoice = Integer.parseInt(scanner.nextLine());
            if (studentChoice < 1 || studentChoice > teacher.getStudents().size()) {
                System.out.println("Invalid student selection.");
                return;
            }
            
            Student selectedStudent = teacher.getStudents().get(studentChoice - 1);
            
            System.out.print("Enter grade (A, A-, B+, B, B-, C+, C, C-, D, F): ");
            String gradeValue = scanner.nextLine();
            
            Grade existingGrade = gradeRepository.getGradeByStudentAndCourse(
                selectedStudent.getUserId(), selectedCourse.getCourseId());
            
            if (existingGrade != null) {
                System.out.println("Grade already exists. Use update option to modify.");
                return;
            }
            
            Grade grade = new Grade(selectedStudent.getUserId(), selectedCourse.getCourseId(), 
                                   teacher.getUserId(), gradeValue);
            
            gradeRepository.add(grade);
            System.out.println("Grade assigned successfully!");
            
        } catch (Exception e) {
            System.out.println("Error assigning grade: " + e.getMessage());
        }
    }

    private static void updateGrade(Teacher teacher) {
        try {
            List<Grade> grades = gradeRepository.getGradesByTeacherId(teacher.getUserId());
            
            if (grades.isEmpty()) {
                System.out.println("No grades to update.");
                return;
            }
            
            System.out.println("\n--- Update Grade ---");
            for (int i = 0; i < grades.size(); i++) {
                Grade g = grades.get(i);
                System.out.println((i + 1) + ". Student ID: " + g.getStudentId() + 
                                 ", Course: " + g.getCourseId() + ", Grade: " + g.getGrade());
            }
            
            System.out.print("Select grade to update (1, 2, etc.): ");
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice < 1 || choice > grades.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            
            Grade grade = grades.get(choice - 1);
            
            System.out.print("Enter new grade (current: " + grade.getGrade() + "): ");
            String newGrade = scanner.nextLine();
            
            if (!newGrade.trim().isEmpty()) {
                grade.setGrade(newGrade);
                gradeRepository.update(grade);
                System.out.println("Grade updated successfully!");
            }
            
        } catch (Exception e) {
            System.out.println("Error updating grade: " + e.getMessage());
        }
    }

    private static void viewGradesByCourse(Teacher teacher) {
        try {
            if (teacher.getCourses().isEmpty()) {
                System.out.println("You have no courses assigned.");
                return;
            }
            
            System.out.println("\n--- View Grades by Course ---");
            System.out.println("Your Courses:");
            for (int i = 0; i < teacher.getCourses().size(); i++) {
                Course c = teacher.getCourses().get(i);
                System.out.println((i + 1) + ". " + c.getCourseName() + " (" + c.getCourseId() + ")");
            }
            
            System.out.print("Select course (1, 2, etc.): ");
            int courseChoice = Integer.parseInt(scanner.nextLine());
            if (courseChoice < 1 || courseChoice > teacher.getCourses().size()) {
                System.out.println("Invalid course selection.");
                return;
            }
            
            Course selectedCourse = teacher.getCourses().get(courseChoice - 1);
            List<Grade> grades = gradeRepository.getGradesByCourseId(selectedCourse.getCourseId());
            
            if (grades.isEmpty()) {
                System.out.println("\nNo grades found for this course.");
                return;
            }
            
            System.out.println("\n=== Grades for " + selectedCourse.getCourseName() + " ===");
            for (Grade g : grades) {
                System.out.println("Student ID: " + g.getStudentId() + " - Grade: " + g.getGrade());
            }
            System.out.println("Total: " + grades.size() + " grades");
            
        } catch (Exception e) {
            System.out.println("Error viewing grades: " + e.getMessage());
        }
    }

    private static void viewAllTeacherGrades(Teacher teacher) {
        try {
            List<Grade> grades = gradeRepository.getGradesByTeacherId(teacher.getUserId());
            
            if (grades.isEmpty()) {
                System.out.println("\nNo grades found.");
                return;
            }
            
            System.out.println("\n=== All Your Grades ===");
            for (Grade g : grades) {
                System.out.println("Student ID: " + g.getStudentId() + 
                                 ", Course: " + g.getCourseId() + 
                                 ", Grade: " + g.getGrade());
            }
            System.out.println("Total: " + grades.size() + " grades given");
            
        } catch (Exception e) {
            System.out.println("Error viewing grades: " + e.getMessage());
        }
    }

    private static void handleMessaging(Teacher teacher) {
        while (true) {
            try {
                teacher.showMessagingMenu();
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        viewTeacherMessages(teacher);
                        break;
                    case 2:
                        sendMessageToStudents(teacher);
                        break;
                    case 3:
                        viewUnreadMessages(teacher);
                        break;
                    case 4:
                        markMessagesAsRead(teacher);
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void viewTeacherMessages(Teacher teacher) {
        try {
            List<Message> messages = messageRepository.getMessagesForUser(teacher.getUserId(), "TEACHER");
            
            if (messages.isEmpty()) {
                System.out.println("\nNo messages found.");
                return;
            }
            
            System.out.println("\n=== Your Messages ===");
            for (Message m : messages) {
                System.out.println("ID: " + m.getMessageId());
                System.out.println("  From: " + m.getFromUserName() + " (" + m.getFromRole() + ")");
                System.out.println("  Message: " + m.getMessage());
                System.out.println("  Timestamp: " + m.getTimestamp());
                System.out.println("  Status: " + (m.isRead() ? "Read" : "Unread"));
                System.out.println();
            }
            System.out.println("Total: " + messages.size() + " messages");
            
        } catch (Exception e) {
            System.out.println("Error viewing messages: " + e.getMessage());
        }
    }

    private static void sendMessageToStudents(Teacher teacher) {
        try {
            if (teacher.getStudents().isEmpty()) {
                System.out.println("You have no students enrolled in your courses.");
                return;
            }
            
            System.out.println("\n--- Send Message to Students ---");
            System.out.println("Your Students:");
            for (int i = 0; i < teacher.getStudents().size(); i++) {
                Student s = teacher.getStudents().get(i);
                System.out.println((i + 1) + ". " + s.getName() + " (ID: " + s.getUserId() + ")");
            }
            
            System.out.print("Select student (1, 2, etc.): ");
            int studentChoice = Integer.parseInt(scanner.nextLine());
            if (studentChoice < 1 || studentChoice > teacher.getStudents().size()) {
                System.out.println("Invalid student selection.");
                return;
            }
            
            Student selectedStudent = teacher.getStudents().get(studentChoice - 1);
            
            System.out.print("Enter your message: ");
            String messageContent = scanner.nextLine();
            
            int messageId = messageRepository.getNextMessageId();
            Message message = new Message(messageId, teacher.getUserId(), teacher.getName(), "TEACHER",
                                         selectedStudent.getUserId(), selectedStudent.getName(), "STUDENT",
                                         messageContent);
            
            messageRepository.add(message);
            System.out.println("Message sent successfully!");
            
        } catch (Exception e) {
            System.out.println("Error sending message: " + e.getMessage());
        }
    }

    private static void viewUnreadMessages(Teacher teacher) {
        try {
            List<Message> unreadMessages = messageRepository.getUnreadMessagesForUser(teacher.getUserId(), "TEACHER");
            
            if (unreadMessages.isEmpty()) {
                System.out.println("\nNo unread messages.");
                return;
            }
            
            System.out.println("\n=== Unread Messages ===");
            for (Message m : unreadMessages) {
                System.out.println("ID: " + m.getMessageId());
                System.out.println("  From: " + m.getFromUserName() + " (" + m.getFromRole() + ")");
                System.out.println("  Message: " + m.getMessage());
                System.out.println("  Timestamp: " + m.getTimestamp());
                System.out.println();
            }
            System.out.println("Total: " + unreadMessages.size() + " unread messages");
            
        } catch (Exception e) {
            System.out.println("Error viewing unread messages: " + e.getMessage());
        }
    }

    private static void markMessagesAsRead(Teacher teacher) {
        try {
            List<Message> unreadMessages = messageRepository.getUnreadMessagesForUser(teacher.getUserId(), "TEACHER");
            
            if (unreadMessages.isEmpty()) {
                System.out.println("No unread messages.");
                return;
            }
            
            System.out.println("\n--- Mark Messages as Read ---");
            for (int i = 0; i < unreadMessages.size(); i++) {
                Message m = unreadMessages.get(i);
                System.out.println((i + 1) + ". From: " + m.getFromUserName() + " - " + m.getMessage().substring(0, Math.min(50, m.getMessage().length())) + "...");
            }
            
            System.out.print("Select message to mark as read (1, 2, etc., or 'all'): ");
            String choice = scanner.nextLine();
            
            if (choice.equalsIgnoreCase("all")) {
                for (Message m : unreadMessages) {
                    m.setRead(true);
                    messageRepository.update(m);
                }
                System.out.println("All messages marked as read!");
            } else {
                int msgChoice = Integer.parseInt(choice);
                if (msgChoice >= 1 && msgChoice <= unreadMessages.size()) {
                    Message message = unreadMessages.get(msgChoice - 1);
                    message.setRead(true);
                    messageRepository.update(message);
                    System.out.println("Message marked as read!");
                } else {
                    System.out.println("Invalid selection.");
                }
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        } catch (Exception e) {
            System.out.println("Error marking messages as read: " + e.getMessage());
        }
    }

    private static void viewTeacherCourses(Teacher teacher) {
        System.out.println("\n=== Your Courses ===");
        if (teacher.getCourses().isEmpty()) {
            System.out.println("No courses assigned.");
        } else {
            for (Course c : teacher.getCourses()) {
                c.displayCourseInfo();
                System.out.println();
            }
            System.out.println("Total: " + teacher.getCourses().size() + " courses");
        }
    }

    private static void viewTeacherStudents(Teacher teacher) {
        System.out.println("\n=== Your Students ===");
        if (teacher.getStudents().isEmpty()) {
            System.out.println("No students enrolled in your courses.");
        } else {
            for (Student s : teacher.getStudents()) {
                System.out.println("- " + s.getName() + " (ID: " + s.getUserId() + ", Email: " + s.getEmail() + ")");
            }
            System.out.println("Total: " + teacher.getStudents().size() + " students");
        }
    }

    private static void runStudentMenu() {
        try {
            List<Student> allStudents = studentRepository.getAll();
            
            if (allStudents.isEmpty()) {
                System.out.println("\nNo students found in the system. Please register a student first.");
                System.out.print("Create a new student? (yes/no): ");
                String response = scanner.nextLine();
                if (response.equalsIgnoreCase("yes")) {
                    System.out.print("Enter Student ID: ");
                    int userId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter Username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter Password: ");
                    String password = scanner.nextLine();
                    
                    Student newStudent = new Student(userId, name, email, username, password);
                    studentRepository.add(newStudent);
                    allStudents.add(newStudent);
                } else {
                    return;
                }
            }
            
            System.out.println("\n=== Select Student Account ===");
            for (int i = 0; i < allStudents.size(); i++) {
                Student s = allStudents.get(i);
                System.out.println((i + 1) + ". " + s.getName() + " (ID: " + s.getUserId() + ", Email: " + s.getEmail() + ")");
            }
            
            System.out.print("Enter selection number (1, 2, etc.) to login, or 0 to cancel: ");
            int studentChoice = Integer.parseInt(scanner.nextLine());
            
            if (studentChoice <= 0 || studentChoice > allStudents.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            
            Student student = allStudents.get(studentChoice - 1);
            loadStudentCourses(student);
            
            System.out.println("\n" + "=".repeat(60));
            System.out.println("   Welcome, " + student.getName() + " - Student Account");
            System.out.println("   User ID: " + student.getUserId());
            System.out.println("   Courses: " + student.getCourses().size());
            System.out.println("   All operations work directly with persistent storage");
            System.out.println("=".repeat(60));
            
            while (true) {
                try {
                    student.showMenu();
                    int choice = Integer.parseInt(scanner.nextLine());
                    
                    switch (choice) {
                        case 1:
                            handleAssignmentManagement(student);
                            break;
                        case 2:
                            handleViewGrades(student);
                            break;
                        case 3:
                            handleStudentMessaging(student);
                            break;
                        case 4:
                            showStudentDashboard(student);
                            break;
                        case 5:
                            viewStudentCourses(student);
                            break;
                        case 6:
                            handleFileUpload(student);
                            break;
                        case 0:
                            System.out.println("Logging out from Student account...");
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
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void loadStudentCourses(Student student) {
        try {
            List<Course> allCourses = courseRepository.getAll();
            for (Course course : allCourses) {
                student.addCourse(course);
            }
        } catch (Exception e) {
            System.out.println("Warning: Could not load courses: " + e.getMessage());
        }
    }

    private static void handleAssignmentManagement(Student student) {
        while (true) {
            try {
                student.showAssignmentManagementMenu();
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        viewStudentAssignments(student);
                        break;
                    case 2:
                        submitAssignment(student);
                        break;
                    case 3:
                        updateSubmission(student);
                        break;
                    case 4:
                        deleteSubmission(student);
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void viewStudentAssignments(Student student) {
        try {
            if (student.getCourses().isEmpty()) {
                System.out.println("\nYou are not enrolled in any courses.");
                return;
            }
            
            System.out.println("\n=== Your Assignments ===");
            boolean foundAssignments = false;
            
            for (Course course : student.getCourses()) {
                List<Assignment> courseAssignments = assignmentRepository.getAssignmentsByCourseId(course.getCourseId());
                
                if (!courseAssignments.isEmpty()) {
                    foundAssignments = true;
                    System.out.println("\nCourse: " + course.getCourseName() + " (" + course.getCourseId() + ")");
                    for (Assignment a : courseAssignments) {
                        System.out.println("  Assignment ID: " + a.getId());
                        System.out.println("  Title: " + a.getTitle());
                        System.out.println("  Description: " + a.getDescription());
                        System.out.println("  Due Date: " + a.getDueDate());
                        
                        Submission submission = submissionRepository.getSubmissionByStudentAndAssignment(
                            student.getUserId(), a.getId());
                        if (submission != null) {
                            System.out.println("  Status: SUBMITTED (" + submission.getTimestamp() + ")");
                            System.out.println("  File: " + submission.getFileName());
                        } else {
                            System.out.println("  Status: NOT SUBMITTED");
                        }
                        System.out.println();
                    }
                }
            }
            
            if (!foundAssignments) {
                System.out.println("No assignments found for your courses.");
            }
            
        } catch (Exception e) {
            System.out.println("Error viewing assignments: " + e.getMessage());
        }
    }

    private static void submitAssignment(Student student) {
        try {
            java.util.ArrayList<Assignment> availableAssignments = new java.util.ArrayList<>();
            
            for (Course course : student.getCourses()) {
                List<Assignment> courseAssignments = assignmentRepository.getAssignmentsByCourseId(course.getCourseId());
                for (Assignment a : courseAssignments) {
                    Submission existing = submissionRepository.getSubmissionByStudentAndAssignment(
                        student.getUserId(), a.getId());
                    if (existing == null) {
                        availableAssignments.add(a);
                    }
                }
            }
            
            if (availableAssignments.isEmpty()) {
                System.out.println("\nNo pending assignments to submit.");
                return;
            }
            
            System.out.println("\n--- Submit Assignment ---");
            System.out.println("Available Assignments:");
            for (int i = 0; i < availableAssignments.size(); i++) {
                Assignment a = availableAssignments.get(i);
                System.out.println((i + 1) + ". " + a.getTitle() + " (Due: " + a.getDueDate() + ")");
            }
            
            System.out.print("Select assignment to submit (1, 2, etc.): ");
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice < 1 || choice > availableAssignments.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            
            Assignment assignment = availableAssignments.get(choice - 1);
            
            System.out.print("Enter file name to upload: ");
            String fileName = scanner.nextLine();
            
            try {
                File file = new File(fileName);
                uploadService.store(file);
                uploadService.saveMetadata(file);
                String filePath = uploadService.getUploadDirectory() + fileName;
                
                int submissionId = submissionRepository.getNextSubmissionId();
                Submission submission = new Submission(submissionId, assignment.getId(), 
                                                      student.getUserId(), fileName, filePath);
                
                submissionRepository.add(submission);
                System.out.println("Assignment submitted successfully!");
                System.out.println("Submission ID: " + submissionId);
                
            } catch (UploadException e) {
                System.out.println("Upload failed: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.out.println("Error submitting assignment: " + e.getMessage());
        }
    }

    private static void updateSubmission(Student student) {
        try {
            List<Submission> mySubmissions = submissionRepository.getSubmissionsByStudentId(student.getUserId());
            
            if (mySubmissions.isEmpty()) {
                System.out.println("You have no submissions to update.");
                return;
            }
            
            System.out.println("\n--- Update Submission ---");
            for (int i = 0; i < mySubmissions.size(); i++) {
                Submission s = mySubmissions.get(i);
                Assignment assignment = null;
                try {
                    assignment = assignmentRepository.getAssignmentById(s.getAssignmentId());
                } catch (NotFoundException e) {
                    assignment = null;
                }
                System.out.println((i + 1) + ". Assignment: " + (assignment != null ? assignment.getTitle() : "Unknown"));
                System.out.println("   File: " + s.getFileName());
                System.out.println("   Submitted: " + s.getTimestamp());
            }
            
            System.out.print("Select submission to update (1, 2, etc.): ");
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice < 1 || choice > mySubmissions.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            
            Submission submission = mySubmissions.get(choice - 1);
            
            System.out.print("Enter new file name to upload: ");
            String fileName = scanner.nextLine();
            
            try {
                File file = new File(fileName);
                uploadService.store(file);
                uploadService.saveMetadata(file);
                String filePath = uploadService.getUploadDirectory() + fileName;
                
                submission.setFileName(fileName);
                submission.setFilePath(filePath);
                submission.setTimestamp(java.time.LocalDateTime.now().format(
                    java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                
                submissionRepository.update(submission);
                System.out.println("Submission updated successfully!");
                
            } catch (UploadException e) {
                System.out.println("Upload failed: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.out.println("Error updating submission: " + e.getMessage());
        }
    }

    private static void deleteSubmission(Student student) {
        try {
            List<Submission> mySubmissions = submissionRepository.getSubmissionsByStudentId(student.getUserId());
            
            if (mySubmissions.isEmpty()) {
                System.out.println("You have no submissions to delete.");
                return;
            }
            
            System.out.println("\n--- Delete Submission ---");
            for (int i = 0; i < mySubmissions.size(); i++) {
                Submission s = mySubmissions.get(i);
                Assignment assignment = null;
                try {
                    assignment = assignmentRepository.getAssignmentById(s.getAssignmentId());
                } catch (NotFoundException e) {
                    assignment = null;
                }
                System.out.println((i + 1) + ". Assignment: " + (assignment != null ? assignment.getTitle() : "Unknown"));
                System.out.println("   File: " + s.getFileName());
            }
            
            System.out.print("Select submission to delete (1, 2, etc.): ");
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice < 1 || choice > mySubmissions.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            
            Submission submission = mySubmissions.get(choice - 1);
            System.out.print("Are you sure you want to delete this submission? (yes/no): ");
            String confirm = scanner.nextLine();
            
            if (confirm.equalsIgnoreCase("yes")) {
                submissionRepository.delete(submission);
                System.out.println("Submission deleted successfully!");
            } else {
                System.out.println("Deletion cancelled.");
            }
            
        } catch (Exception e) {
            System.out.println("Error deleting submission: " + e.getMessage());
        }
    }

    private static void handleViewGrades(Student student) {
        try {
            List<Grade> allGrades = gradeRepository.getAll();
            List<Grade> myGrades = allGrades.stream()
                .filter(g -> g.getStudentId() == student.getUserId())
                .collect(java.util.stream.Collectors.toList());
            
            if (myGrades.isEmpty()) {
                System.out.println("\nNo grades found.");
                return;
            }
            
            System.out.println("\n=== View Grades ===");
            System.out.println("1. View All Grades");
            System.out.println("2. Filter by Course");
            System.out.print("Choose an option: ");
            
            int choice = Integer.parseInt(scanner.nextLine());
            
            if (choice == 1) {
                System.out.println("\n=== Your Grades ===");
                for (Grade g : myGrades) {
                    System.out.println("Course: " + g.getCourseId() + " - Grade: " + g.getGrade());
                }
                
                double total = 0;
                int count = 0;
                for (Grade g : myGrades) {
                    double gradeValue = convertGradeToPoints(g.getGrade());
                    if (gradeValue >= 0) {
                        total += gradeValue;
                        count++;
                    }
                }
                
                if (count > 0) {
                    double gpa = total / count;
                    System.out.println("\nGPA: " + String.format("%.2f", gpa));
                }
                
                System.out.println("Total: " + myGrades.size() + " grades");
                
            } else if (choice == 2) {
                if (student.getCourses().isEmpty()) {
                    System.out.println("You are not enrolled in any courses.");
                    return;
                }
                
                System.out.println("\nYour Courses:");
                for (int i = 0; i < student.getCourses().size(); i++) {
                    Course c = student.getCourses().get(i);
                    System.out.println((i + 1) + ". " + c.getCourseName() + " (" + c.getCourseId() + ")");
                }
                
                System.out.print("Select course (1, 2, etc.): ");
                int courseChoice = Integer.parseInt(scanner.nextLine());
                if (courseChoice < 1 || courseChoice > student.getCourses().size()) {
                    System.out.println("Invalid course selection.");
                    return;
                }
                
                Course selectedCourse = student.getCourses().get(courseChoice - 1);
                
                Grade grade = myGrades.stream()
                    .filter(g -> g.getCourseId().equals(selectedCourse.getCourseId()))
                    .findFirst()
                    .orElse(null);
                
                if (grade != null) {
                    System.out.println("\nCourse: " + selectedCourse.getCourseName());
                    System.out.println("Grade: " + grade.getGrade());
                } else {
                    System.out.println("\nNo grade assigned yet for this course.");
                }
            }
            
        } catch (Exception e) {
            System.out.println("Error viewing grades: " + e.getMessage());
        }
    }

    private static double convertGradeToPoints(String grade) {
        switch (grade.toUpperCase()) {
            case "A": return 4.0;
            case "A-": return 3.7;
            case "B+": return 3.3;
            case "B": return 3.0;
            case "B-": return 2.7;
            case "C+": return 2.3;
            case "C": return 2.0;
            case "C-": return 1.7;
            case "D": return 1.0;
            case "F": return 0.0;
            default: return -1;
        }
    }

    private static void handleStudentMessaging(Student student) {
        while (true) {
            try {
                student.showMessagingMenu();
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        viewStudentMessages(student);
                        break;
                    case 2:
                        sendMessageToTeacher(student);
                        break;
                    case 3:
                        viewStudentUnreadMessages(student);
                        break;
                    case 4:
                        markStudentMessagesAsRead(student);
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void viewStudentMessages(Student student) {
        try {
            List<Message> messages = messageRepository.getMessagesForUser(student.getUserId(), "STUDENT");
            
            if (messages.isEmpty()) {
                System.out.println("\nNo messages found.");
                return;
            }
            
            System.out.println("\n=== Your Messages ===");
            for (Message m : messages) {
                System.out.println("ID: " + m.getMessageId());
                System.out.println("  From: " + m.getFromUserName() + " (" + m.getFromRole() + ")");
                System.out.println("  Message: " + m.getMessage());
                System.out.println("  Timestamp: " + m.getTimestamp());
                System.out.println("  Status: " + (m.isRead() ? "Read" : "Unread"));
                System.out.println();
            }
            System.out.println("Total: " + messages.size() + " messages");
            
        } catch (Exception e) {
            System.out.println("Error viewing messages: " + e.getMessage());
        }
    }

    private static void sendMessageToTeacher(Student student) {
        try {
            List<Teacher> allTeachers = teacherRepository.getAll();
            
            if (allTeachers.isEmpty()) {
                System.out.println("No teachers available in the system.");
                return;
            }
            
            System.out.println("\n--- Send Message to Teacher ---");
            System.out.println("Available Teachers:");
            for (int i = 0; i < allTeachers.size(); i++) {
                Teacher t = allTeachers.get(i);
                System.out.println((i + 1) + ". " + t.getName() + " (ID: " + t.getUserId() + ")");
            }
            
            System.out.print("Select teacher (1, 2, etc.): ");
            int teacherChoice = Integer.parseInt(scanner.nextLine());
            if (teacherChoice < 1 || teacherChoice > allTeachers.size()) {
                System.out.println("Invalid teacher selection.");
                return;
            }
            
            Teacher selectedTeacher = allTeachers.get(teacherChoice - 1);
            
            System.out.print("Enter your message: ");
            String messageContent = scanner.nextLine();
            
            int messageId = messageRepository.getNextMessageId();
            Message message = new Message(messageId, student.getUserId(), student.getName(), "STUDENT",
                                         selectedTeacher.getUserId(), selectedTeacher.getName(), "TEACHER",
                                         messageContent);
            
            messageRepository.add(message);
            System.out.println("Message sent successfully!");
            
        } catch (Exception e) {
            System.out.println("Error sending message: " + e.getMessage());
        }
    }

    private static void viewStudentUnreadMessages(Student student) {
        try {
            List<Message> unreadMessages = messageRepository.getUnreadMessagesForUser(student.getUserId(), "STUDENT");
            
            if (unreadMessages.isEmpty()) {
                System.out.println("\nNo unread messages.");
                return;
            }
            
            System.out.println("\n=== Unread Messages ===");
            for (Message m : unreadMessages) {
                System.out.println("ID: " + m.getMessageId());
                System.out.println("  From: " + m.getFromUserName() + " (" + m.getFromRole() + ")");
                System.out.println("  Message: " + m.getMessage());
                System.out.println("  Timestamp: " + m.getTimestamp());
                System.out.println();
            }
            System.out.println("Total: " + unreadMessages.size() + " unread messages");
            
        } catch (Exception e) {
            System.out.println("Error viewing unread messages: " + e.getMessage());
        }
    }

    private static void markStudentMessagesAsRead(Student student) {
        try {
            List<Message> unreadMessages = messageRepository.getUnreadMessagesForUser(student.getUserId(), "STUDENT");
            
            if (unreadMessages.isEmpty()) {
                System.out.println("No unread messages.");
                return;
            }
            
            System.out.println("\n--- Mark Messages as Read ---");
            for (int i = 0; i < unreadMessages.size(); i++) {
                Message m = unreadMessages.get(i);
                System.out.println((i + 1) + ". From: " + m.getFromUserName() + " - " + 
                                 m.getMessage().substring(0, Math.min(50, m.getMessage().length())) + "...");
            }
            
            System.out.print("Select message to mark as read (1, 2, etc., or 'all'): ");
            String choice = scanner.nextLine();
            
            if (choice.equalsIgnoreCase("all")) {
                for (Message m : unreadMessages) {
                    m.setRead(true);
                    messageRepository.update(m);
                }
                System.out.println("All messages marked as read!");
            } else {
                int msgChoice = Integer.parseInt(choice);
                if (msgChoice >= 1 && msgChoice <= unreadMessages.size()) {
                    Message message = unreadMessages.get(msgChoice - 1);
                    message.setRead(true);
                    messageRepository.update(message);
                    System.out.println("Message marked as read!");
                } else {
                    System.out.println("Invalid selection.");
                }
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        } catch (Exception e) {
            System.out.println("Error marking messages as read: " + e.getMessage());
        }
    }

    private static void showStudentDashboard(Student student) {
        try {
            student.showDashboard();
            
            java.util.ArrayList<Assignment> pendingAssignments = new java.util.ArrayList<>();
            for (Course course : student.getCourses()) {
                List<Assignment> courseAssignments = assignmentRepository.getAssignmentsByCourseId(course.getCourseId());
                for (Assignment a : courseAssignments) {
                    Submission submission = submissionRepository.getSubmissionByStudentAndAssignment(
                        student.getUserId(), a.getId());
                    if (submission == null) {
                        pendingAssignments.add(a);
                    }
                }
            }
            
            List<Submission> mySubmissions = submissionRepository.getSubmissionsByStudentId(student.getUserId());
            List<Grade> myGrades = gradeRepository.getAll().stream()
                .filter(g -> g.getStudentId() == student.getUserId())
                .collect(java.util.stream.Collectors.toList());
            List<Message> unreadMessages = messageRepository.getUnreadMessagesForUser(student.getUserId(), "STUDENT");
            
            System.out.println("Pending Assignments: " + pendingAssignments.size());
            System.out.println("Submitted Assignments: " + mySubmissions.size());
            System.out.println("Total Grades: " + myGrades.size());
            System.out.println("Unread Messages: " + unreadMessages.size());
            
            if (!pendingAssignments.isEmpty() && pendingAssignments.size() <= 3) {
                System.out.println("\nUpcoming Assignments:");
                for (Assignment a : pendingAssignments) {
                    System.out.println("  - " + a.getTitle() + " (Due: " + a.getDueDate() + ")");
                }
            }
            
            if (!unreadMessages.isEmpty() && unreadMessages.size() <= 3) {
                System.out.println("\nLatest Messages:");
                for (int i = 0; i < Math.min(3, unreadMessages.size()); i++) {
                    Message m = unreadMessages.get(i);
                    System.out.println("  - From " + m.getFromUserName() + ": " + 
                                     m.getMessage().substring(0, Math.min(40, m.getMessage().length())) + "...");
                }
            }
            
            System.out.println("=".repeat(60));
            
        } catch (Exception e) {
            System.out.println("Error displaying dashboard: " + e.getMessage());
        }
    }

    private static void viewStudentCourses(Student student) {
        System.out.println("\n=== Your Enrolled Courses ===");
        if (student.getCourses().isEmpty()) {
            System.out.println("No courses enrolled.");
        } else {
            for (Course c : student.getCourses()) {
                c.displayCourseInfo();
                System.out.println();
            }
            System.out.println("Total: " + student.getCourses().size() + " courses");
        }
    }

    private static void demonstrateStudentAccess() {
        System.out.println("\n--- Student Access Mode ---");
        System.out.println("Would you like to:");
        System.out.println("1. Run Quick Demo (automated)");
        System.out.println("2. Interactive Student Menu");
        System.out.print("Choose: ");
        
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 1) {
                runQuickStudentDemo();
            } else if (choice == 2) {
                runStudentMenu();
            } else {
                System.out.println("Invalid choice.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    private static void runQuickStudentDemo() {
        System.out.println("\n--- Quick Student Demo ---");
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
        System.out.println("Demonstrating repository-based search and sort capabilities...\n");
        
        try {
            // Sort and display Teachers by Name
            System.out.println("--- Teachers Sorted by Name ---");
            List<Teacher> sortedTeachers = teacherRepository.sort("name");
            if (sortedTeachers.isEmpty()) {
                System.out.println("No teachers found.");
            } else {
                for (Teacher t : sortedTeachers) {
                    System.out.println("  - " + t.getName() + " (ID: " + t.getUserId() + ", Email: " + t.getEmail() + ")");
                }
                System.out.println("Total: " + sortedTeachers.size() + " teachers");
            }
            
            // Sort and display Students by Name
            System.out.println("\n--- Students Sorted by Name ---");
            List<Student> sortedStudentsByName = studentRepository.sort("name");
            if (sortedStudentsByName.isEmpty()) {
                System.out.println("No students found.");
            } else {
                for (Student s : sortedStudentsByName) {
                    System.out.println("  - " + s.getName() + " (ID: " + s.getUserId() + ", Email: " + s.getEmail() + ")");
                }
                System.out.println("Total: " + sortedStudentsByName.size() + " students");
            }
            
            // Sort and display Students by ID
            System.out.println("\n--- Students Sorted by ID ---");
            List<Student> sortedStudentsById = studentRepository.sort("id");
            if (sortedStudentsById.isEmpty()) {
                System.out.println("No students found.");
            } else {
                for (Student s : sortedStudentsById) {
                    System.out.println("  - ID: " + s.getUserId() + " - " + s.getName());
                }
                System.out.println("Total: " + sortedStudentsById.size() + " students");
            }
            
            // Sort and display Courses by Code
            System.out.println("\n--- Courses Sorted by Code ---");
            List<Course> sortedCourses = courseRepository.sort("code");
            if (sortedCourses.isEmpty()) {
                System.out.println("No courses found.");
            } else {
                for (Course c : sortedCourses) {
                    System.out.println("  - " + c.getCourseId() + ": " + c.getCourseName() + 
                                     " (" + c.getCreditHours() + " credits)");
                }
                System.out.println("Total: " + sortedCourses.size() + " courses");
            }
            
        } catch (RepositoryException e) {
            System.out.println("Error during search/sort: " + e.getMessage());
            e.log();
        }
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

    // ==================== ADMIN MENU IMPLEMENTATION ====================

    private static void runAdminMenu() {
        try {
            List<Admin> allAdmins = adminRepository.getAll();
            
            if (allAdmins.isEmpty()) {
                System.out.println("\nNo admins found in the system. Please create an admin first.");
                System.out.print("Create a new admin? (yes/no): ");
                String response = scanner.nextLine();
                if (response.equalsIgnoreCase("yes")) {
                    System.out.print("Enter Admin ID: ");
                    int userId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter Username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter Password: ");
                    String password = scanner.nextLine();
                    
                    Admin newAdmin = new Admin(userId, name, email, username, password);
                    adminRepository.add(newAdmin);
                    allAdmins.add(newAdmin);
                } else {
                    return;
                }
            }
            
            System.out.println("\n=== Select Admin Account ===");
            for (int i = 0; i < allAdmins.size(); i++) {
                Admin a = allAdmins.get(i);
                System.out.println((i + 1) + ". " + a.getName() + " (ID: " + a.getUserId() + ", Email: " + a.getEmail() + ")");
            }
            
            System.out.print("Enter selection number (1, 2, etc.) to login, or 0 to cancel: ");
            int adminChoice = Integer.parseInt(scanner.nextLine());
            
            if (adminChoice <= 0 || adminChoice > allAdmins.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            
            Admin admin = allAdmins.get(adminChoice - 1);
            
            System.out.println("\n" + "=".repeat(60));
            System.out.println("   Welcome, " + admin.getName() + " - Admin Account");
            System.out.println("   User ID: " + admin.getUserId());
            System.out.println("   All operations work directly with persistent storage");
            System.out.println("=".repeat(60));
            
            while (true) {
                try {
                    admin.showMenu();
                    int choice = Integer.parseInt(scanner.nextLine());
                    
                    switch (choice) {
                        case 1:
                            handleAdminStudentManagement(admin);
                            break;
                        case 2:
                            handleAdminTeacherManagement(admin);
                            break;
                        case 3:
                            handleAdminCourseManagement(admin);
                            break;
                        case 4:
                            showAdminReports(admin);
                            break;
                        case 5:
                            viewAdminMessages(admin);
                            break;
                        case 6:
                            handleFileUpload(admin);
                            break;
                        case 0:
                            System.out.println("Logging out from Admin account...");
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
        } catch (Exception e) {
            System.out.println("Error loading admin menu: " + e.getMessage());
        }
    }

    private static void handleAdminStudentManagement(Admin admin) {
        while (true) {
            try {
                admin.showStudentManagementMenu();
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        registerNewStudent(admin);
                        break;
                    case 2:
                        updateStudentDetails(admin);
                        break;
                    case 3:
                        removeStudent(admin);
                        break;
                    case 4:
                        searchStudents(admin);
                        break;
                    case 5:
                        enrollStudentInCourse(admin);
                        break;
                    case 6:
                        viewAllStudentsFromRepository();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static void registerNewStudent(Admin admin) {
        try {
            int userId = 0;
            while (true) {
                System.out.print("Enter Student ID: ");
                String idInput = scanner.nextLine();
                try {
                    InputValidator.validateNumericId(idInput);
                    userId = Integer.parseInt(idInput.trim());
                    break;
                } catch (ValidationException e) {
                    System.out.println("⚠️ Invalid ID: ID must be a number. Please try again.");
                }
            }
            
            String name = "";
            while (true) {
                System.out.print("Enter Name: ");
                name = scanner.nextLine();
                try {
                    InputValidator.validateName(name);
                    break;
                } catch (ValidationException e) {
                    System.out.println("⚠️ Invalid input: Name should contain only letters and spaces. Please try again.");
                }
            }
            
            String email = "";
            while (true) {
                System.out.print("Enter Email: ");
                email = scanner.nextLine();
                try {
                    InputValidator.validateEmail(email);
                    break;
                } catch (ValidationException e) {
                    System.out.println("⚠️ Invalid input: Enter a valid email format. Please try again.");
                }
            }
            
            String username = "";
            while (true) {
                System.out.print("Enter Username: ");
                username = scanner.nextLine();
                try {
                    InputValidator.validateUsername(username);
                    break;
                } catch (ValidationException e) {
                    System.out.println("⚠️ Username cannot be empty. Please try again.");
                }
            }
            
            String password = "";
            while (true) {
                System.out.print("Enter Password: ");
                password = scanner.nextLine();
                try {
                    InputValidator.validatePassword(password);
                    break;
                } catch (ValidationException e) {
                    System.out.println("⚠️ Password must be at least 4 characters. Please try again.");
                }
            }
            
            Student student = new Student(userId, name, email, username, password);
            studentRepository.add(student);
            System.out.println("✓ Admin: Registered student " + student.getName() + " (ID: " + student.getUserId() + ")");
            
        } catch (Exception e) {
            System.out.println("Error registering student: " + e.getMessage());
        }
    }

    private static void updateStudentDetails(Admin admin) {
        try {
            System.out.print("Enter Student ID to update: ");
            int userId = Integer.parseInt(scanner.nextLine());
            
            List<Student> allStudents = studentRepository.getAll();
            Student toUpdate = null;
            for (Student student : allStudents) {
                if (student.getUserId() == userId) {
                    toUpdate = student;
                    break;
                }
            }
            
            if (toUpdate != null) {
                System.out.print("Enter new Name (or press Enter to skip): ");
                String name = scanner.nextLine();
                if (!name.trim().isEmpty()) {
                    toUpdate.setName(name);
                }
                
                System.out.print("Enter new Email (or press Enter to skip): ");
                String email = scanner.nextLine();
                if (!email.trim().isEmpty()) {
                    toUpdate.setEmail(email);
                }
                
                studentRepository.update(toUpdate);
                System.out.println("✓ Admin: Updated student " + toUpdate.getName() + " (ID: " + userId + ")");
            } else {
                System.out.println("✗ Admin: Student with ID " + userId + " not found");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Please enter a number.");
        } catch (Exception e) {
            System.out.println("Error updating student: " + e.getMessage());
        }
    }

    private static void removeStudent(Admin admin) {
        try {
            System.out.print("Enter Student ID to remove: ");
            int userId = Integer.parseInt(scanner.nextLine());
            
            List<Student> allStudents = studentRepository.getAll();
            Student toRemove = null;
            for (Student student : allStudents) {
                if (student.getUserId() == userId) {
                    toRemove = student;
                    break;
                }
            }
            
            if (toRemove != null) {
                studentRepository.delete(toRemove);
                System.out.println("✓ Admin: Removed student " + toRemove.getName() + " (ID: " + userId + ")");
            } else {
                System.out.println("✗ Admin: Student with ID " + userId + " not found");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Please enter a number.");
        } catch (Exception e) {
            System.out.println("Error removing student: " + e.getMessage());
        }
    }

    private static void searchStudents(Admin admin) {
        try {
            System.out.print("Enter search criteria (name, email, or ID): ");
            String criteria = scanner.nextLine();
            
            List<Student> results = studentRepository.find(criteria);
            System.out.println("\n=== Search Results ===");
            if (results.isEmpty()) {
                System.out.println("No students found matching '" + criteria + "'");
            } else {
                for (int i = 0; i < results.size(); i++) {
                    Student student = results.get(i);
                    System.out.println((i + 1) + ". " + student.getName() + 
                                     " (ID: " + student.getUserId() + 
                                     ", Email: " + student.getEmail() + ")");
                }
            }
            System.out.println("Total Results: " + results.size());
            System.out.println("====================\n");
        } catch (Exception e) {
            System.out.println("Error searching students: " + e.getMessage());
        }
    }

    private static void enrollStudentInCourse(Admin admin) {
        try {
            viewAllStudentsFromRepository();
            System.out.print("Enter Student ID: ");
            int studentId = Integer.parseInt(scanner.nextLine());
            
            viewAllCoursesFromRepository();
            System.out.print("Enter Course ID: ");
            String courseId = scanner.nextLine();
            
            List<Student> allStudents = studentRepository.getAll();
            Student student = null;
            for (Student s : allStudents) {
                if (s.getUserId() == studentId) {
                    student = s;
                    break;
                }
            }
            
            List<Course> allCourses = courseRepository.getAll();
            Course course = null;
            for (Course c : allCourses) {
                if (c.getCourseId().equals(courseId)) {
                    course = c;
                    break;
                }
            }
            
            if (student != null && course != null) {
                student.addCourse(course);
                studentRepository.update(student);
                System.out.println("✓ Admin: Enrolled " + student.getName() + " in course " + course.getCourseName());
            } else {
                System.out.println("✗ Student or Course not found.");
            }
        } catch (Exception e) {
            System.out.println("Error enrolling student: " + e.getMessage());
        }
    }

    private static void handleAdminTeacherManagement(Admin admin) {
        while (true) {
            try {
                admin.showTeacherManagementMenu();
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        registerNewTeacher(admin);
                        break;
                    case 2:
                        updateTeacherDetails(admin);
                        break;
                    case 3:
                        removeTeacher(admin);
                        break;
                    case 4:
                        searchTeachers(admin);
                        break;
                    case 5:
                        assignTeacherToCourse(admin);
                        break;
                    case 6:
                        viewAllTeachersFromRepository();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static void registerNewTeacher(Admin admin) {
        try {
            int userId = 0;
            while (true) {
                System.out.print("Enter Teacher ID: ");
                String idInput = scanner.nextLine();
                try {
                    InputValidator.validateNumericId(idInput);
                    userId = Integer.parseInt(idInput.trim());
                    break;
                } catch (ValidationException e) {
                    System.out.println("⚠️ Invalid ID: ID must be a number. Please try again.");
                }
            }
            
            String name = "";
            while (true) {
                System.out.print("Enter Name: ");
                name = scanner.nextLine();
                try {
                    InputValidator.validateName(name);
                    break;
                } catch (ValidationException e) {
                    System.out.println("⚠️ Invalid input: Name should contain only letters and spaces. Please try again.");
                }
            }
            
            String email = "";
            while (true) {
                System.out.print("Enter Email: ");
                email = scanner.nextLine();
                try {
                    InputValidator.validateEmail(email);
                    break;
                } catch (ValidationException e) {
                    System.out.println("⚠️ Invalid input: Enter a valid email format. Please try again.");
                }
            }
            
            String username = "";
            while (true) {
                System.out.print("Enter Username: ");
                username = scanner.nextLine();
                try {
                    InputValidator.validateUsername(username);
                    break;
                } catch (ValidationException e) {
                    System.out.println("⚠️ Username cannot be empty. Please try again.");
                }
            }
            
            String password = "";
            while (true) {
                System.out.print("Enter Password: ");
                password = scanner.nextLine();
                try {
                    InputValidator.validatePassword(password);
                    break;
                } catch (ValidationException e) {
                    System.out.println("⚠️ Password must be at least 4 characters. Please try again.");
                }
            }
            
            Teacher teacher = new Teacher(userId, name, email, username, password);
            teacherRepository.add(teacher);
            System.out.println("✓ Admin: Registered teacher " + teacher.getName() + " (ID: " + teacher.getUserId() + ")");
            
        } catch (Exception e) {
            System.out.println("Error registering teacher: " + e.getMessage());
        }
    }

    private static void updateTeacherDetails(Admin admin) {
        try {
            System.out.print("Enter Teacher ID to update: ");
            int userId = Integer.parseInt(scanner.nextLine());
            
            List<Teacher> allTeachers = teacherRepository.getAll();
            Teacher toUpdate = null;
            for (Teacher teacher : allTeachers) {
                if (teacher.getUserId() == userId) {
                    toUpdate = teacher;
                    break;
                }
            }
            
            if (toUpdate != null) {
                System.out.print("Enter new Name (or press Enter to skip): ");
                String name = scanner.nextLine();
                if (!name.trim().isEmpty()) {
                    toUpdate.setName(name);
                }
                
                System.out.print("Enter new Email (or press Enter to skip): ");
                String email = scanner.nextLine();
                if (!email.trim().isEmpty()) {
                    toUpdate.setEmail(email);
                }
                
                teacherRepository.update(toUpdate);
                System.out.println("✓ Admin: Updated teacher " + toUpdate.getName() + " (ID: " + userId + ")");
            } else {
                System.out.println("✗ Admin: Teacher with ID " + userId + " not found");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Please enter a number.");
        } catch (Exception e) {
            System.out.println("Error updating teacher: " + e.getMessage());
        }
    }

    private static void removeTeacher(Admin admin) {
        try {
            System.out.print("Enter Teacher ID to remove: ");
            int userId = Integer.parseInt(scanner.nextLine());
            
            List<Teacher> allTeachers = teacherRepository.getAll();
            Teacher toRemove = null;
            for (Teacher teacher : allTeachers) {
                if (teacher.getUserId() == userId) {
                    toRemove = teacher;
                    break;
                }
            }
            
            if (toRemove != null) {
                teacherRepository.delete(toRemove);
                System.out.println("✓ Admin: Removed teacher " + toRemove.getName() + " (ID: " + userId + ")");
            } else {
                System.out.println("✗ Admin: Teacher with ID " + userId + " not found");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Please enter a number.");
        } catch (Exception e) {
            System.out.println("Error removing teacher: " + e.getMessage());
        }
    }

    private static void searchTeachers(Admin admin) {
        try {
            System.out.print("Enter search criteria (name, email, or ID): ");
            String criteria = scanner.nextLine();
            
            List<Teacher> results = teacherRepository.find(criteria);
            System.out.println("\n=== Search Results ===");
            if (results.isEmpty()) {
                System.out.println("No teachers found matching '" + criteria + "'");
            } else {
                for (int i = 0; i < results.size(); i++) {
                    Teacher teacher = results.get(i);
                    System.out.println((i + 1) + ". " + teacher.getName() + 
                                     " (ID: " + teacher.getUserId() + 
                                     ", Email: " + teacher.getEmail() + 
                                     ", Courses: " + teacher.getCourses().size() + ")");
                }
            }
            System.out.println("Total Results: " + results.size());
            System.out.println("====================\n");
        } catch (Exception e) {
            System.out.println("Error searching teachers: " + e.getMessage());
        }
    }

    private static void assignTeacherToCourse(Admin admin) {
        try {
            viewAllTeachersFromRepository();
            System.out.print("Enter Teacher ID: ");
            int teacherId = Integer.parseInt(scanner.nextLine());
            
            viewAllCoursesFromRepository();
            System.out.print("Enter Course ID: ");
            String courseId = scanner.nextLine();
            
            List<Teacher> allTeachers = teacherRepository.getAll();
            Teacher teacher = null;
            for (Teacher t : allTeachers) {
                if (t.getUserId() == teacherId) {
                    teacher = t;
                    break;
                }
            }
            
            List<Course> allCourses = courseRepository.getAll();
            Course course = null;
            for (Course c : allCourses) {
                if (c.getCourseId().equals(courseId)) {
                    course = c;
                    break;
                }
            }
            
            if (teacher != null && course != null) {
                teacher.addCourse(course);
                course.setFacultyName(teacher.getName());
                teacherRepository.update(teacher);
                courseRepository.update(course);
                System.out.println("✓ Admin: Assigned course " + course.getCourseName() + " to teacher " + teacher.getName());
            } else {
                System.out.println("✗ Teacher or Course not found.");
            }
        } catch (Exception e) {
            System.out.println("Error assigning teacher to course: " + e.getMessage());
        }
    }

    private static void handleAdminCourseManagement(Admin admin) {
        while (true) {
            try {
                admin.showCourseManagementMenu();
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        createNewCourse(admin);
                        break;
                    case 2:
                        updateCourseDetails(admin);
                        break;
                    case 3:
                        removeCourse(admin);
                        break;
                    case 4:
                        assignTeacherToCourse(admin);
                        break;
                    case 5:
                        enrollStudentInCourse(admin);
                        break;
                    case 6:
                        viewAllCoursesFromRepository();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static void createNewCourse(Admin admin) {
        try {
            String courseId = "";
            while (true) {
                System.out.print("Enter Course ID (e.g., LMS101): ");
                courseId = scanner.nextLine();
                try {
                    InputValidator.validateCourseId(courseId);
                    break;
                } catch (ValidationException e) {
                    System.out.println("⚠️ Invalid Course ID: Course ID cannot be empty. Please try again.");
                }
            }
            
            String courseName = "";
            while (true) {
                System.out.print("Enter Course Name: ");
                courseName = scanner.nextLine();
                try {
                    InputValidator.validateCourseName(courseName);
                    break;
                } catch (ValidationException e) {
                    System.out.println("⚠️ Invalid Course Name: Course Name cannot be empty. Please try again.");
                }
            }
            
            int creditHours = 0;
            while (true) {
                System.out.print("Enter Credit Hours: ");
                String creditInput = scanner.nextLine();
                try {
                    InputValidator.validateCreditHours(creditInput);
                    creditHours = Integer.parseInt(creditInput.trim());
                    break;
                } catch (ValidationException e) {
                    System.out.println("⚠️ Invalid Credit Hours: Credit Hours must be a number. Please try again.");
                }
            }
            
            System.out.print("Enter Class Days (e.g., Mon/Wed/Fri): ");
            String classDays = scanner.nextLine();
            
            System.out.print("Enter Class Times (e.g., 10:00-11:00): ");
            String classTimes = scanner.nextLine();
            
            System.out.print("Enter Class Dates (e.g., Fall 2025): ");
            String classDates = scanner.nextLine();
            
            Course course = new Course(courseId, courseName, creditHours, "Unassigned", classDays, classTimes, classDates);
            courseRepository.add(course);
            System.out.println("✓ Admin: Created course " + course.getCourseName() + " (ID: " + course.getCourseId() + ")");
            
        } catch (Exception e) {
            System.out.println("Error creating course: " + e.getMessage());
        }
    }

    private static void updateCourseDetails(Admin admin) {
        try {
            System.out.print("Enter Course ID to update: ");
            String courseId = scanner.nextLine();
            
            List<Course> allCourses = courseRepository.getAll();
            Course toUpdate = null;
            for (Course course : allCourses) {
                if (course.getCourseId().equals(courseId)) {
                    toUpdate = course;
                    break;
                }
            }
            
            if (toUpdate != null) {
                System.out.print("Enter new Course Name (or press Enter to skip): ");
                String courseName = scanner.nextLine();
                if (!courseName.trim().isEmpty()) {
                    toUpdate.setCourseName(courseName);
                }
                
                System.out.print("Enter new Credit Hours (or press Enter to skip): ");
                String creditHoursStr = scanner.nextLine();
                if (!creditHoursStr.trim().isEmpty()) {
                    toUpdate.setCreditHours(Integer.parseInt(creditHoursStr));
                }
                
                courseRepository.update(toUpdate);
                System.out.println("✓ Admin: Updated course " + toUpdate.getCourseName() + " (ID: " + courseId + ")");
            } else {
                System.out.println("✗ Admin: Course with ID " + courseId + " not found");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid credit hours. Please enter a number.");
        } catch (Exception e) {
            System.out.println("Error updating course: " + e.getMessage());
        }
    }

    private static void removeCourse(Admin admin) {
        try {
            System.out.print("Enter Course ID to remove: ");
            String courseId = scanner.nextLine();
            
            List<Course> allCourses = courseRepository.getAll();
            Course toRemove = null;
            for (Course course : allCourses) {
                if (course.getCourseId().equals(courseId)) {
                    toRemove = course;
                    break;
                }
            }
            
            if (toRemove != null) {
                courseRepository.delete(toRemove);
                System.out.println("✓ Admin: Removed course " + toRemove.getCourseName() + " (ID: " + courseId + ")");
            } else {
                System.out.println("✗ Admin: Course with ID " + courseId + " not found");
            }
        } catch (Exception e) {
            System.out.println("Error removing course: " + e.getMessage());
        }
    }

    private static void showAdminReports(Admin admin) {
        try {
            List<Student> allStudents = studentRepository.getAll();
            List<Teacher> allTeachers = teacherRepository.getAll();
            List<Course> allCourses = courseRepository.getAll();
            
            System.out.println("\n" + "=".repeat(60));
            System.out.println("         ADMIN REPORTS & STATISTICS");
            System.out.println("=".repeat(60));
            System.out.println("Total Students: " + allStudents.size());
            System.out.println("Total Teachers: " + allTeachers.size());
            System.out.println("Total Courses: " + allCourses.size());
            
            System.out.println("\n--- Students Per Course ---");
            for (Course course : allCourses) {
                long studentCount = allStudents.stream()
                    .filter(s -> s.getCourses().stream()
                        .anyMatch(c -> c.getCourseId().equals(course.getCourseId())))
                    .count();
                System.out.println("  " + course.getCourseName() + ": " + studentCount + " students");
            }
            
            System.out.println("\n--- Teacher Workload ---");
            for (Teacher teacher : allTeachers) {
                System.out.println("  " + teacher.getName() + ": " + teacher.getCourses().size() + " courses");
            }
            
            System.out.println("\n--- Course Enrollment ---");
            for (Course course : allCourses) {
                long enrolled = allStudents.stream()
                    .filter(s -> s.getCourses().stream()
                        .anyMatch(c -> c.getCourseId().equals(course.getCourseId())))
                    .count();
                System.out.println("  " + course.getCourseName() + " (" + course.getCourseId() + "): " + enrolled + " enrolled");
            }
            
            System.out.println("=".repeat(60) + "\n");
        } catch (Exception e) {
            System.out.println("Error generating reports: " + e.getMessage());
        }
    }

    private static void viewAdminMessages(Admin admin) {
        try {
            List<Message> messages = messageRepository.getMessagesForUser(admin.getUserId(), "ADMIN");
            List<Message> unreadMessages = messageRepository.getUnreadMessagesForUser(admin.getUserId(), "ADMIN");
            
            System.out.println("\n" + "=".repeat(60));
            System.out.println("         MESSAGE INBOX FOR " + admin.getName());
            System.out.println("=".repeat(60));
            System.out.println("Total Messages: " + messages.size());
            System.out.println("Unread Messages: " + unreadMessages.size());
            System.out.println();
            
            if (messages.isEmpty()) {
                System.out.println("No messages found.");
            } else {
                for (int i = 0; i < messages.size(); i++) {
                    Message msg = messages.get(i);
                    String status = msg.isRead() ? "READ" : "UNREAD";
                    System.out.println((i + 1) + ". [" + status + "] From: " + msg.getFromUserName() + 
                                     " (" + msg.getFromRole() + ")");
                    System.out.println("   Time: " + msg.getTimestamp());
                    System.out.println("   Message: " + msg.getMessage());
                    System.out.println();
                }
                
                System.out.print("Mark message as read? Enter message number (or 0 to skip): ");
                try {
                    int msgNum = Integer.parseInt(scanner.nextLine());
                    if (msgNum > 0 && msgNum <= messages.size()) {
                        Message toMark = messages.get(msgNum - 1);
                        toMark.setRead(true);
                        messageRepository.update(toMark);
                        System.out.println("✓ Message marked as read.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Skipping mark as read.");
                }
            }
            
            System.out.println("=".repeat(60) + "\n");
        } catch (Exception e) {
            System.out.println("Error retrieving messages: " + e.getMessage());
        }
    }

    // ==================== PRINCIPAL MESSAGING ====================

    private static void sendMessageFromPrincipal(Principal principal) {
        try {
            System.out.println("\n--- Send Message ---");
            System.out.println("1. Send to Admin");
            System.out.println("2. Send to Teacher");
            System.out.print("Choose recipient type: ");
            
            int recipientType = Integer.parseInt(scanner.nextLine());
            
            if (recipientType == 1) {
                List<Admin> admins = adminRepository.getAll();
                if (admins.isEmpty()) {
                    System.out.println("No admins found in the system.");
                    return;
                }
                
                System.out.println("\n=== Available Admins ===");
                for (int i = 0; i < admins.size(); i++) {
                    Admin admin = admins.get(i);
                    System.out.println((i + 1) + ". " + admin.getName() + " (ID: " + admin.getUserId() + ")");
                }
                
                System.out.print("\nEnter Admin ID: ");
                int adminId = Integer.parseInt(scanner.nextLine());
                
                Admin targetAdmin = null;
                for (Admin admin : admins) {
                    if (admin.getUserId() == adminId) {
                        targetAdmin = admin;
                        break;
                    }
                }
                
                if (targetAdmin != null) {
                    System.out.print("Enter your message: ");
                    String messageText = scanner.nextLine();
                    
                    int messageId = messageRepository.getNextMessageId();
                    Message message = new Message(
                        messageId,
                        principal.getUserId(),
                        principal.getName(),
                        "PRINCIPAL",
                        targetAdmin.getUserId(),
                        targetAdmin.getName(),
                        "ADMIN",
                        messageText
                    );
                    
                    messageRepository.add(message);
                    System.out.println("✓ Message sent to " + targetAdmin.getName());
                } else {
                    System.out.println("✗ Admin with ID " + adminId + " not found");
                }
            } else if (recipientType == 2) {
                List<Teacher> teachers = teacherRepository.getAll();
                if (teachers.isEmpty()) {
                    System.out.println("No teachers found in the system.");
                    return;
                }
                
                System.out.println("\n=== Available Teachers ===");
                for (int i = 0; i < teachers.size(); i++) {
                    Teacher teacher = teachers.get(i);
                    System.out.println((i + 1) + ". " + teacher.getName() + " (ID: " + teacher.getUserId() + ")");
                }
                
                System.out.print("\nEnter Teacher ID: ");
                int teacherId = Integer.parseInt(scanner.nextLine());
                
                Teacher targetTeacher = null;
                for (Teacher teacher : teachers) {
                    if (teacher.getUserId() == teacherId) {
                        targetTeacher = teacher;
                        break;
                    }
                }
                
                if (targetTeacher != null) {
                    System.out.print("Enter your message: ");
                    String messageText = scanner.nextLine();
                    
                    int messageId = messageRepository.getNextMessageId();
                    Message message = new Message(
                        messageId,
                        principal.getUserId(),
                        principal.getName(),
                        "PRINCIPAL",
                        targetTeacher.getUserId(),
                        targetTeacher.getName(),
                        "TEACHER",
                        messageText
                    );
                    
                    messageRepository.add(message);
                    System.out.println("✓ Message sent to " + targetTeacher.getName());
                } else {
                    System.out.println("✗ Teacher with ID " + teacherId + " not found");
                }
            } else {
                System.out.println("Invalid recipient type.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        } catch (Exception e) {
            System.out.println("Error sending message: " + e.getMessage());
        }
    }

    private static void handleFileUpload(User user) {
        try {
            System.out.println("\n=== File Upload ===");
            System.out.print("Enter file path to upload: ");
            String filePath = scanner.nextLine();
            
            if (filePath == null || filePath.trim().isEmpty()) {
                System.out.println("File path cannot be empty.");
                return;
            }
            
            String role = user.getRole().toLowerCase();
            ((FileUploadService) uploadService).uploadFile(filePath, user.getName(), role);
            
        } catch (Exception e) {
            System.out.println("Error uploading file: " + e.getMessage());
        }
    }
}