package sms.app;

import sms.data.*;
import sms.domain.*;
import sms.exceptions.*;
import sms.validation.InputValidator;

import java.util.List;
import java.util.Scanner;

public class PrincipalMenuHandler {
    private final Repository<Student> studentRepository;
    private final Repository<Teacher> teacherRepository;
    private final Repository<Admin> adminRepository;
    private final Repository<Course> courseRepository;
    private final MessageRepository messageRepository;
    private final Scanner scanner;

    public PrincipalMenuHandler(
        Repository<Student> studentRepository,
        Repository<Teacher> teacherRepository,
        Repository<Admin> adminRepository,
        Repository<Course> courseRepository,
        MessageRepository messageRepository,
        Scanner scanner
    ) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.adminRepository = adminRepository;
        this.courseRepository = courseRepository;
        this.messageRepository = messageRepository;
        this.scanner = scanner;
    }

    public void run() {
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
    
    private void runPrincipalMenuLoop(Principal principal) {
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

    private void viewPrincipalReportsFromRepositories(Principal principal) {
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

    private void handleAdminManagement(Principal principal) {
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

    private void viewAllAdminsFromRepository() {
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

    private void addNewAdmin(Principal principal) {
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

    private void removeAdminById(Principal principal) {
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

    private void handleTeacherManagement(Principal principal) {
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

    private void viewAllTeachersFromRepository() {
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

    private void appointNewTeacher(Principal principal) {
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

    private void removeTeacherById(Principal principal) {
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

    private void handleCourseManagement(Principal principal) {
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

    private void viewAllCoursesFromRepository() {
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

    private void createNewCourse(Principal principal) {
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

    private void assignTeacherToCourseFromRepository(Principal principal) {
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

    private void removeCourseById(Principal principal) {
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

    private void viewAllStudentsFromRepository() {
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

    private void sendMessageFromPrincipal(Principal principal) {
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
            System.out.println("Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("Error sending message: " + e.getMessage());
        }
    }
}
