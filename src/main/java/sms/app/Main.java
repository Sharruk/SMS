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
    private static MessageRepository messageRepository;
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
        messageRepository = new MessageRepository();
        
        // Initialize upload service
        uploadService = new FileUploadService();
        
        // Initialize scanner for user input
        scanner = new Scanner(System.in);
        
        System.out.println("LMS system initialized successfully!");
        System.out.println("JSON files: students.json, teachers.json, admins.json, courses.json, messages.json");
        
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
        Principal principal = new Principal(5001, "Dr. Principal", "principal@lms.edu", "principal", "pass123");
        Admin admin = new Admin(5002, "Admin User", "admin@lms.edu", "admin", "pass123");
        Teacher teacher = new Teacher(5003, "Teacher User", "teacher@lms.edu", "teacher", "pass123");
        
        principal.addAdmin(admin);
        principal.appointTeacher(teacher);
        principal.viewReports();
    }

    private static void runPrincipalMenu() {
        Principal principal = new Principal(1, "Dr. Principal", "principal@lms.edu", "principal", "pass123");
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("   Welcome, " + principal.getName() + " - Principal Account");
        System.out.println("   All operations work directly with persistent storage");
        System.out.println("=".repeat(60));
        
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
            
            Admin admin = new Admin(userId, name, email, username, password);
            adminRepository.add(admin);
            System.out.println("✓ Principal: Added admin " + admin.getName() + " (ID: " + admin.getUserId() + ")");
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Please enter a number.");
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
            
            Teacher teacher = new Teacher(userId, name, email, username, password);
            teacherRepository.add(teacher);
            System.out.println("✓ Principal: Appointed teacher " + teacher.getName() + " (ID: " + teacher.getUserId() + ")");
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Please enter a number.");
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
            System.out.print("Enter Course ID (e.g., LMS101): ");
            String courseId = scanner.nextLine();
            
            System.out.print("Enter Course Name: ");
            String courseName = scanner.nextLine();
            
            System.out.print("Enter Credit Hours: ");
            int creditHours = Integer.parseInt(scanner.nextLine());
            
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
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid credit hours. Please enter a number.");
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
            
            Student student = new Student(userId, name, email, username, password);
            studentRepository.add(student);
            System.out.println("✓ Admin: Registered student " + student.getName() + " (ID: " + student.getUserId() + ")");
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Please enter a number.");
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
            
            Teacher teacher = new Teacher(userId, name, email, username, password);
            teacherRepository.add(teacher);
            System.out.println("✓ Admin: Registered teacher " + teacher.getName() + " (ID: " + teacher.getUserId() + ")");
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Please enter a number.");
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
            System.out.print("Enter Course ID (e.g., LMS101): ");
            String courseId = scanner.nextLine();
            
            System.out.print("Enter Course Name: ");
            String courseName = scanner.nextLine();
            
            System.out.print("Enter Credit Hours: ");
            int creditHours = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Enter Class Days (e.g., Mon/Wed/Fri): ");
            String classDays = scanner.nextLine();
            
            System.out.print("Enter Class Times (e.g., 10:00-11:00): ");
            String classTimes = scanner.nextLine();
            
            System.out.print("Enter Class Dates (e.g., Fall 2025): ");
            String classDates = scanner.nextLine();
            
            Course course = new Course(courseId, courseName, creditHours, "Unassigned", classDays, classTimes, classDates);
            courseRepository.add(course);
            System.out.println("✓ Admin: Created course " + course.getCourseName() + " (ID: " + course.getCourseId() + ")");
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid credit hours. Please enter a number.");
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
}