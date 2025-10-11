package sms.app;

import sms.app.menus.*;
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
            initializeSystem();
            demonstrateOopFeatures();
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
        studentRepository = new StudentRepository();
        teacherRepository = new TeacherRepository();
        adminRepository = new AdminRepository();
        courseRepository = new CourseRepository();
        messageRepository = new MessageRepository();
        assignmentRepository = new AssignmentRepository();
        gradeRepository = new GradeRepository();
        submissionRepository = new SubmissionRepository();
        uploadService = new FileUploadService();
        scanner = new Scanner(System.in);
        
        System.out.println("LMS system initialized successfully!");
        System.out.println("JSON files: students.json, teachers.json, admins.json, courses.json, messages.json, assignments.json, grades.json");
        displaySystemStatistics();
    }

    public static void demonstrateOopFeatures() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("     COMPREHENSIVE LMS OOP FEATURES DEMONSTRATION");
        System.out.println("=".repeat(70));

        try {
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

            System.out.println("\n2. === POLYMORPHISM DEMONSTRATION ===");
            System.out.println("Using polymorphism with User reference to call overridden methods:");
            
            User[] users = {admin, teacher, student, principal};
            for (User user : users) {
                System.out.println("\nUser Type: " + user.getClass().getSimpleName());
                System.out.println("Role: " + user.getRole());
                try {
                    user.login();
                    user.logout();
                } catch (AuthenticationException e) {
                    e.log();
                }
            }

            System.out.println("\n3. === ENCAPSULATION DEMONSTRATION ===");
            System.out.println("Demonstrating encapsulation with private fields and public getters/setters:");
            System.out.println("Original student ID: " + student.getUserId());
            student.setUserId(3002);
            System.out.println("Updated student ID: " + student.getUserId());
            System.out.println("Teacher email (encapsulated): " + teacher.getEmail());
            teacher.setEmail("bob.johnson@lms.edu");
            System.out.println("Updated teacher email: " + teacher.getEmail());

            System.out.println("\n4. === GENERICS DEMONSTRATION ===");
            System.out.println("Using Generic Repository<T> interface with different types:");
            System.out.println("(Demo uses in-memory objects only - no persistent storage pollution)");
            
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
            System.out.println("Current repository counts - Students: " + studentRepo.getAll().size() + 
                              ", Teachers: " + teacherRepo.getAll().size() + 
                              ", Admins: " + adminRepo.getAll().size() + 
                              ", Courses: " + courseRepo.getAll().size());

            System.out.println("\n5. === CUSTOM EXCEPTION HIERARCHY DEMONSTRATION ===");
            System.out.println("Demonstrating comprehensive exception hierarchy with try-catch-finally:");
            
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

            System.out.println("\n6. === SEARCH AND SORT INTERFACES DEMONSTRATION ===");
            System.out.println("Demonstrating role-based search and sort capabilities:");
            
            System.out.println("\nAdmin search capabilities (FULL ACCESS):");
            List<Object> adminSearchResults = admin.search("Bob");
            System.out.println("Admin found " + adminSearchResults.size() + " results for 'Bob'");
            List<Object> adminSortResults = admin.sort("name");
            System.out.println("Admin sorted " + adminSortResults.size() + " items by name");
            
            System.out.println("\nTeacher search capabilities (RESTRICTED ACCESS):");
            teacher.addStudent(student);
            List<Student> teacherSearchResults = teacher.search("Charlie");
            System.out.println("Teacher found " + teacherSearchResults.size() + " students for 'Charlie'");
            List<Student> teacherSortResults = teacher.sort("name");
            System.out.println("Teacher sorted " + teacherSortResults.size() + " students by name");
            
            System.out.println("\nStudent search capabilities (LIMITED ACCESS):");
            student.addCourse(course);
            List<Course> studentSearchResults = student.search("Programming");
            System.out.println("Student found " + studentSearchResults.size() + " courses for 'Programming'");
            List<Course> studentSortResults = student.sort("name");
            System.out.println("Student sorted " + studentSortResults.size() + " courses by name");

            System.out.println("\n7. === UPLOAD SERVICE DEMONSTRATION ===");
            System.out.println("Demonstrating file upload with validation and exception handling:");
            
            try {
                File testFile = new File("test_upload.txt");
                testFile.createNewFile();
                System.out.println("User attempting to upload file...");
                student.upload(testFile);
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

            System.out.println("\n8. === ROLE-BASED ACCESS CONTROL DEMONSTRATION ===");
            System.out.println("Demonstrating role-specific capabilities:");
            
            System.out.println("\nPrincipal capabilities:");
            principal.addAdmin(admin);
            principal.appointTeacher(teacher);
            principal.assignCourse(teacher, course);
            principal.viewReports();
            
            System.out.println("Admin capabilities:");
            admin.manageCourse();
            admin.manageStudent();
            
            System.out.println("Teacher capabilities:");
            teacher.viewStudents();
            teacher.viewAllAttendance();
            
            System.out.println("Student capabilities:");
            student.viewAssignment();
            student.viewGrades();

            System.out.println("\n9. === COMPREHENSIVE INTEGRATION ===");
            System.out.println("All LMS features working together:");
            
            try {
                List<Student> allStudents = studentRepository.getAll();
                System.out.println("Retrieved " + allStudents.size() + " students using generic repository");
                
                for (Student s : allStudents) {
                    User userRef = s;
                    System.out.println("  - " + userRef.getName() + " (" + userRef.getRole() + ")");
                    List<Course> studentCourses = s.search("");
                    System.out.println("    Enrolled in " + studentCourses.size() + " courses");
                }
            } catch (RepositoryException e) {
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
                        demonstrateOopFeatures();
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
                    PrincipalMenuHandler principalHandler = new PrincipalMenuHandler(
                        scanner, adminRepository, teacherRepository, courseRepository, 
                        studentRepository, messageRepository
                    );
                    principalHandler.demonstratePrincipalAccess();
                    break;
                case 2:
                    AdminMenuHandler adminHandler = new AdminMenuHandler(
                        scanner, studentRepository, teacherRepository, courseRepository, 
                        adminRepository, messageRepository
                    );
                    adminHandler.demonstrateAdminAccess();
                    break;
                case 3:
                    TeacherMenuHandler teacherHandler = new TeacherMenuHandler(
                        studentRepository, teacherRepository, courseRepository,
                        assignmentRepository, submissionRepository, messageRepository,
                        gradeRepository, uploadService, scanner
                    );
                    teacherHandler.run();
                    break;
                case 4:
                    StudentMenuHandler studentHandler = new StudentMenuHandler(
                        studentRepository, courseRepository, teacherRepository,
                        assignmentRepository, submissionRepository, messageRepository,
                        gradeRepository, uploadService, scanner
                    );
                    studentHandler.run();
                    break;
                default:
                    System.out.println("Invalid role selection.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    private static void displaySystemStatistics() {
        try {
            System.out.println("\n=== LMS SYSTEM STATISTICS ===");
            System.out.println("Total Students: " + studentRepository.getAll().size());
            System.out.println("Total Teachers: " + teacherRepository.getAll().size());
            System.out.println("Total Admins: " + adminRepository.getAll().size());
            System.out.println("Total Courses: " + courseRepository.getAll().size());
            System.out.println("Data files: students.json, teachers.json, admins.json, courses.json");
            System.out.println("Upload directory: uploads/");
            System.out.println("==============================\n");
        } catch (RepositoryException e) {
            System.out.println("Error retrieving statistics: " + e.getMessage());
        }
    }

    private static void demonstrateSearchSort() {
        System.out.println("\n=== SEARCH & SORT DEMONSTRATION ===");
        System.out.println("This demo shows the search and sort interfaces:");
        System.out.println("- Admin can search ALL entities (full access)");
        System.out.println("- Teacher can search their ASSIGNED students");
        System.out.println("- Student can search their ENROLLED courses");
        System.out.println("\nFor full interactive search/sort, login with a specific role.");
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    private static void demonstrateFileUpload() {
        System.out.println("\n=== FILE UPLOAD DEMONSTRATION ===");
        System.out.println("The LMS includes a comprehensive file upload system:");
        System.out.println("- Validates file types and sizes");
        System.out.println("- Stores files with metadata tracking");
        System.out.println("- Handles upload exceptions gracefully");
        System.out.println("\nFor file upload functionality, login as a Teacher or Student.");
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    private static void manageUsers() {
        System.out.println("\n=== USER MANAGEMENT ===");
        System.out.println("For user management, please login with appropriate role:");
        System.out.println("- Principal: Manage admins and teachers");
        System.out.println("- Admin: Manage students and teachers");
        System.out.println("\nPress Enter to return to main menu...");
        scanner.nextLine();
    }

    private static void manageCourses() {
        System.out.println("\n=== COURSE MANAGEMENT ===");
        System.out.println("For course management, please login with appropriate role:");
        System.out.println("- Principal: Create and assign courses");
        System.out.println("- Admin: Manage course enrollments");
        System.out.println("\nPress Enter to return to main menu...");
        scanner.nextLine();
    }
}
