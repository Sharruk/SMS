package sms.app.menus;

import sms.data.*;
import sms.domain.*;
import sms.exceptions.*;
import sms.validation.InputValidator;
import sms.services.FileUploadService;
import sms.services.UploadService;
import java.io.File;
import java.util.*;

public class AdminMenuHandler {
    private Scanner scanner;
    private Repository<Student> studentRepository;
    private Repository<Teacher> teacherRepository;
    private Repository<Course> courseRepository;
    private Repository<Admin> adminRepository;
    private MessageRepository messageRepository;
    private UploadService<File> uploadService;

    public AdminMenuHandler(Scanner scanner, Repository<Student> studentRepo,
                           Repository<Teacher> teacherRepo, Repository<Course> courseRepo,
                           Repository<Admin> adminRepo, MessageRepository messageRepo) {
        this.scanner = scanner;
        this.studentRepository = studentRepo;
        this.teacherRepository = teacherRepo;
        this.courseRepository = courseRepo;
        this.adminRepository = adminRepo;
        this.messageRepository = messageRepo;
        this.uploadService = new FileUploadService();
    }

    public void demonstrateAdminAccess() {
        System.out.println("\n--- Admin Access Mode ---");
        System.out.println("Would you like to:");
        System.out.println("1. Run Quick Demo (automated)");
        System.out.println("2. Interactive Admin Menu");
        System.out.print("Choose: ");
        
        try {
            int choice = Integer.parseInt(this.scanner.nextLine());
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

    public void runQuickAdminDemo() {
        System.out.println("\n--- Quick Admin Demo ---");
        try {
            Admin admin = new Admin(6001, "Admin Demo", "admin@lms.edu", "admin", "pass123");
            Student student = admin.registerStudent("John Doe", "john@student.edu");
            admin.manageStudent();
            admin.manageCourse();
            
            List<Object> results = admin.search("John");
            System.out.println("Admin search results: " + results.size() + " items found");
            
        } catch (ValidationException e) {
            System.out.println("Validation error: " + e.getMessage());
            e.log();
        }
    }

    public void runAdminMenu() {
        try {
            List<Admin> allAdmins = this.adminRepository.getAll();
            
            if (allAdmins.isEmpty()) {
                System.out.println("\nNo admins found in the system. Please create an admin first.");
                System.out.print("Create a new admin? (yes/no): ");
                String response = this.scanner.nextLine();
                if (response.equalsIgnoreCase("yes")) {
                    System.out.print("Enter Admin ID: ");
                    int userId = Integer.parseInt(this.scanner.nextLine());
                    System.out.print("Enter Name: ");
                    String name = this.scanner.nextLine();
                    System.out.print("Enter Email: ");
                    String email = this.scanner.nextLine();
                    System.out.print("Enter Username: ");
                    String username = this.scanner.nextLine();
                    System.out.print("Enter Password: ");
                    String password = this.scanner.nextLine();
                    
                    Admin newAdmin = new Admin(userId, name, email, username, password);
                    this.adminRepository.add(newAdmin);
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
            int adminChoice = Integer.parseInt(this.scanner.nextLine());
            
            if (adminChoice <= 0 || adminChoice > allAdmins.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            
            Admin admin = allAdmins.get(adminChoice - 1);
            
            System.out.print("Enter password: ");
            String enteredPassword = this.scanner.nextLine();
            
            if (!enteredPassword.equals(admin.getPasswordForAuth())) {
                System.out.println("❌ Invalid password. Access denied.");
                return;
            }
            
            System.out.println("✓ Authentication successful!");
            
            System.out.println("\n" + "=".repeat(60));
            System.out.println("   Welcome, " + admin.getName() + " - Admin Account");
            System.out.println("   User ID: " + admin.getUserId());
            System.out.println("   All operations work directly with persistent storage");
            System.out.println("=".repeat(60));
            
            while (true) {
                try {
                    admin.showMenu();
                    int choice = Integer.parseInt(this.scanner.nextLine());
                    
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

    public void handleAdminStudentManagement(Admin admin) {
        while (true) {
            try {
                admin.showStudentManagementMenu();
                int choice = Integer.parseInt(this.scanner.nextLine());
                
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

    public void registerNewStudent(Admin admin) {
        try {
            int userId = 0;
            while (true) {
                System.out.print("Enter Student ID: ");
                String idInput = this.scanner.nextLine();
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
                name = this.scanner.nextLine();
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
                email = this.scanner.nextLine();
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
                username = this.scanner.nextLine();
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
                password = this.scanner.nextLine();
                try {
                    InputValidator.validatePassword(password);
                    break;
                } catch (ValidationException e) {
                    System.out.println("⚠️ Password must be at least 4 characters. Please try again.");
                }
            }
            
            Student student = new Student(userId, name, email, username, password);
            this.studentRepository.add(student);
            System.out.println("✓ Admin: Registered student " + student.getName() + " (ID: " + student.getUserId() + ")");
            
        } catch (Exception e) {
            System.out.println("Error registering student: " + e.getMessage());
        }
    }

    public void updateStudentDetails(Admin admin) {
        try {
            System.out.print("Enter Student ID to update: ");
            int userId = Integer.parseInt(this.scanner.nextLine());
            
            List<Student> allStudents = this.studentRepository.getAll();
            Student toUpdate = null;
            for (Student student : allStudents) {
                if (student.getUserId() == userId) {
                    toUpdate = student;
                    break;
                }
            }
            
            if (toUpdate != null) {
                System.out.print("Enter new Name (or press Enter to skip): ");
                String name = this.scanner.nextLine();
                if (!name.trim().isEmpty()) {
                    toUpdate.setName(name);
                }
                
                System.out.print("Enter new Email (or press Enter to skip): ");
                String email = this.scanner.nextLine();
                if (!email.trim().isEmpty()) {
                    toUpdate.setEmail(email);
                }
                
                this.studentRepository.update(toUpdate);
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

    public void removeStudent(Admin admin) {
        try {
            System.out.print("Enter Student ID to remove: ");
            int userId = Integer.parseInt(this.scanner.nextLine());
            
            List<Student> allStudents = this.studentRepository.getAll();
            Student toRemove = null;
            for (Student student : allStudents) {
                if (student.getUserId() == userId) {
                    toRemove = student;
                    break;
                }
            }
            
            if (toRemove != null) {
                this.studentRepository.delete(toRemove);
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

    public void searchStudents(Admin admin) {
        try {
            System.out.print("Enter search criteria (name, email, or ID): ");
            String criteria = this.scanner.nextLine();
            
            List<Student> results = this.studentRepository.find(criteria);
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

    public void enrollStudentInCourse(Admin admin) {
        try {
            viewAllStudentsFromRepository();
            System.out.print("Enter Student ID: ");
            int studentId = Integer.parseInt(this.scanner.nextLine());
            
            viewAllCoursesFromRepository();
            System.out.print("Enter Course ID: ");
            String courseId = this.scanner.nextLine();
            
            List<Student> allStudents = this.studentRepository.getAll();
            Student student = null;
            for (Student s : allStudents) {
                if (s.getUserId() == studentId) {
                    student = s;
                    break;
                }
            }
            
            List<Course> allCourses = this.courseRepository.getAll();
            Course course = null;
            for (Course c : allCourses) {
                if (c.getCourseId().equals(courseId)) {
                    course = c;
                    break;
                }
            }
            
            if (student != null && course != null) {
                student.addCourse(course);
                this.studentRepository.update(student);
                System.out.println("✓ Admin: Enrolled " + student.getName() + " in course " + course.getCourseName());
            } else {
                System.out.println("✗ Student or Course not found.");
            }
        } catch (Exception e) {
            System.out.println("Error enrolling student: " + e.getMessage());
        }
    }

    public void handleAdminTeacherManagement(Admin admin) {
        while (true) {
            try {
                admin.showTeacherManagementMenu();
                int choice = Integer.parseInt(this.scanner.nextLine());
                
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

    public void registerNewTeacher(Admin admin) {
        try {
            int userId = 0;
            while (true) {
                System.out.print("Enter Teacher ID: ");
                String idInput = this.scanner.nextLine();
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
                name = this.scanner.nextLine();
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
                email = this.scanner.nextLine();
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
                username = this.scanner.nextLine();
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
                password = this.scanner.nextLine();
                try {
                    InputValidator.validatePassword(password);
                    break;
                } catch (ValidationException e) {
                    System.out.println("⚠️ Password must be at least 4 characters. Please try again.");
                }
            }
            
            Teacher teacher = new Teacher(userId, name, email, username, password);
            this.teacherRepository.add(teacher);
            System.out.println("✓ Admin: Registered teacher " + teacher.getName() + " (ID: " + teacher.getUserId() + ")");
            
        } catch (Exception e) {
            System.out.println("Error registering teacher: " + e.getMessage());
        }
    }

    public void updateTeacherDetails(Admin admin) {
        try {
            System.out.print("Enter Teacher ID to update: ");
            int userId = Integer.parseInt(this.scanner.nextLine());
            
            List<Teacher> allTeachers = this.teacherRepository.getAll();
            Teacher toUpdate = null;
            for (Teacher teacher : allTeachers) {
                if (teacher.getUserId() == userId) {
                    toUpdate = teacher;
                    break;
                }
            }
            
            if (toUpdate != null) {
                System.out.print("Enter new Name (or press Enter to skip): ");
                String name = this.scanner.nextLine();
                if (!name.trim().isEmpty()) {
                    toUpdate.setName(name);
                }
                
                System.out.print("Enter new Email (or press Enter to skip): ");
                String email = this.scanner.nextLine();
                if (!email.trim().isEmpty()) {
                    toUpdate.setEmail(email);
                }
                
                this.teacherRepository.update(toUpdate);
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

    public void removeTeacher(Admin admin) {
        try {
            System.out.print("Enter Teacher ID to remove: ");
            int userId = Integer.parseInt(this.scanner.nextLine());
            
            List<Teacher> allTeachers = this.teacherRepository.getAll();
            Teacher toRemove = null;
            for (Teacher teacher : allTeachers) {
                if (teacher.getUserId() == userId) {
                    toRemove = teacher;
                    break;
                }
            }
            
            if (toRemove != null) {
                this.teacherRepository.delete(toRemove);
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

    public void searchTeachers(Admin admin) {
        try {
            System.out.print("Enter search criteria (name, email, or ID): ");
            String criteria = this.scanner.nextLine();
            
            List<Teacher> results = this.teacherRepository.find(criteria);
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

    public void assignTeacherToCourse(Admin admin) {
        try {
            viewAllTeachersFromRepository();
            System.out.print("Enter Teacher ID: ");
            int teacherId = Integer.parseInt(this.scanner.nextLine());
            
            viewAllCoursesFromRepository();
            System.out.print("Enter Course ID: ");
            String courseId = this.scanner.nextLine();
            
            List<Teacher> allTeachers = this.teacherRepository.getAll();
            Teacher teacher = null;
            for (Teacher t : allTeachers) {
                if (t.getUserId() == teacherId) {
                    teacher = t;
                    break;
                }
            }
            
            List<Course> allCourses = this.courseRepository.getAll();
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
                this.teacherRepository.update(teacher);
                this.courseRepository.update(course);
                System.out.println("✓ Admin: Assigned course " + course.getCourseName() + " to teacher " + teacher.getName());
            } else {
                System.out.println("✗ Teacher or Course not found.");
            }
        } catch (Exception e) {
            System.out.println("Error assigning teacher to course: " + e.getMessage());
        }
    }

    public void handleAdminCourseManagement(Admin admin) {
        while (true) {
            try {
                admin.showCourseManagementMenu();
                int choice = Integer.parseInt(this.scanner.nextLine());
                
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

    public void createNewCourse(Admin admin) {
        try {
            String courseId = "";
            while (true) {
                System.out.print("Enter Course ID (e.g., LMS101): ");
                courseId = this.scanner.nextLine();
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
                courseName = this.scanner.nextLine();
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
                String creditInput = this.scanner.nextLine();
                try {
                    InputValidator.validateCreditHours(creditInput);
                    creditHours = Integer.parseInt(creditInput.trim());
                    break;
                } catch (ValidationException e) {
                    System.out.println("⚠️ Invalid Credit Hours: Credit Hours must be a number. Please try again.");
                }
            }
            
            System.out.print("Enter Class Days (e.g., Mon/Wed/Fri): ");
            String classDays = this.scanner.nextLine();
            
            System.out.print("Enter Class Times (e.g., 10:00-11:00): ");
            String classTimes = this.scanner.nextLine();
            
            System.out.print("Enter Class Dates (e.g., Fall 2025): ");
            String classDates = this.scanner.nextLine();
            
            Course course = new Course(courseId, courseName, creditHours, "Unassigned", classDays, classTimes, classDates);
            this.courseRepository.add(course);
            System.out.println("✓ Admin: Created course " + course.getCourseName() + " (ID: " + course.getCourseId() + ")");
            
        } catch (Exception e) {
            System.out.println("Error creating course: " + e.getMessage());
        }
    }

    public void updateCourseDetails(Admin admin) {
        try {
            System.out.print("Enter Course ID to update: ");
            String courseId = this.scanner.nextLine();
            
            List<Course> allCourses = this.courseRepository.getAll();
            Course toUpdate = null;
            for (Course course : allCourses) {
                if (course.getCourseId().equals(courseId)) {
                    toUpdate = course;
                    break;
                }
            }
            
            if (toUpdate != null) {
                System.out.print("Enter new Course Name (or press Enter to skip): ");
                String courseName = this.scanner.nextLine();
                if (!courseName.trim().isEmpty()) {
                    toUpdate.setCourseName(courseName);
                }
                
                System.out.print("Enter new Credit Hours (or press Enter to skip): ");
                String creditHoursStr = this.scanner.nextLine();
                if (!creditHoursStr.trim().isEmpty()) {
                    toUpdate.setCreditHours(Integer.parseInt(creditHoursStr));
                }
                
                this.courseRepository.update(toUpdate);
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

    public void removeCourse(Admin admin) {
        try {
            System.out.print("Enter Course ID to remove: ");
            String courseId = this.scanner.nextLine();
            
            List<Course> allCourses = this.courseRepository.getAll();
            Course toRemove = null;
            for (Course course : allCourses) {
                if (course.getCourseId().equals(courseId)) {
                    toRemove = course;
                    break;
                }
            }
            
            if (toRemove != null) {
                this.courseRepository.delete(toRemove);
                System.out.println("✓ Admin: Removed course " + toRemove.getCourseName() + " (ID: " + courseId + ")");
            } else {
                System.out.println("✗ Admin: Course with ID " + courseId + " not found");
            }
        } catch (Exception e) {
            System.out.println("Error removing course: " + e.getMessage());
        }
    }

    public void showAdminReports(Admin admin) {
        try {
            List<Student> allStudents = this.studentRepository.getAll();
            List<Teacher> allTeachers = this.teacherRepository.getAll();
            List<Course> allCourses = this.courseRepository.getAll();
            
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

    public void viewAdminMessages(Admin admin) {
        try {
            List<Message> messages = this.messageRepository.getMessagesForUser(admin.getUserId(), "ADMIN");
            List<Message> unreadMessages = this.messageRepository.getUnreadMessagesForUser(admin.getUserId(), "ADMIN");
            
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
                    int msgNum = Integer.parseInt(this.scanner.nextLine());
                    if (msgNum > 0 && msgNum <= messages.size()) {
                        Message toMark = messages.get(msgNum - 1);
                        toMark.setRead(true);
                        this.messageRepository.update(toMark);
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

    public void viewAllStudentsFromRepository() {
        try {
            List<Student> students = this.studentRepository.getAll();
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

    public void viewAllTeachersFromRepository() {
        try {
            List<Teacher> teachers = this.teacherRepository.getAll();
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

    public void viewAllCoursesFromRepository() {
        try {
            List<Course> courses = this.courseRepository.getAll();
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

    public void handleFileUpload(Admin admin) {
        try {
            System.out.println("\n=== File Upload ===");
            System.out.print("Enter file path to upload: ");
            String filePath = this.scanner.nextLine();
            
            if (filePath == null || filePath.trim().isEmpty()) {
                System.out.println("File path cannot be empty.");
                return;
            }
            
            String role = admin.getRole().toLowerCase();
            FileUploadService fileService = (FileUploadService) this.uploadService;
            List<String> visibleTo = new ArrayList<>();
            
            System.out.println("\n=== Set File Visibility ===");
            System.out.println("1. Visible to ALL users");
            System.out.println("2. Private (only admins)");
            System.out.print("Choose option: ");
            
            String choice = this.scanner.nextLine();
            
            if ("1".equals(choice)) {
                visibleTo.add("ALL");
                System.out.println("File will be visible to all users");
            } else {
                System.out.println("File will be private (only admins can see it)");
            }
            
            fileService.uploadFile(filePath, admin.getEmail(), role, visibleTo);
            
            System.out.println("\n--- Uploaded Files for " + admin.getRole() + " ---");
            fileService.displayUploadedFilesByRole(role);
            
        } catch (Exception e) {
            System.out.println("Error uploading file: " + e.getMessage());
        }
    }
}
