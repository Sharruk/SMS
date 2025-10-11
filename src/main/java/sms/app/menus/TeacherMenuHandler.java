package sms.app.menus;

import sms.data.*;
import sms.domain.*;
import sms.exceptions.*;
import sms.validation.InputValidator;
import sms.services.FileUploadService;
import sms.services.UploadService;
import java.io.File;
import java.util.*;

public class TeacherMenuHandler {
    private Scanner scanner;
    private Repository<Student> studentRepository;
    private Repository<Teacher> teacherRepository;
    private Repository<Course> courseRepository;
    private AssignmentRepository assignmentRepository;
    private GradeRepository gradeRepository;
    private MessageRepository messageRepository;
    private UploadService<File> uploadService;

    public TeacherMenuHandler(Scanner scanner, Repository<Student> studentRepo,
                             Repository<Teacher> teacherRepo, Repository<Course> courseRepo,
                             AssignmentRepository assignmentRepo, GradeRepository gradeRepo,
                             MessageRepository messageRepo) {
        this.scanner = scanner;
        this.studentRepository = studentRepo;
        this.teacherRepository = teacherRepo;
        this.courseRepository = courseRepo;
        this.assignmentRepository = assignmentRepo;
        this.gradeRepository = gradeRepo;
        this.messageRepository = messageRepo;
        this.uploadService = new FileUploadService();
    }

    public void demonstrateTeacherAccess() {
        System.out.println("\n--- Teacher Access Mode ---");
        System.out.println("Would you like to:");
        System.out.println("1. Run Quick Demo (automated)");
        System.out.println("2. Interactive Teacher Menu");
        System.out.print("Choose: ");
        
        try {
            int choice = Integer.parseInt(this.scanner.nextLine());
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

    public void runQuickTeacherDemo() {
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

    public void runTeacherMenu() {
        try {
            List<Teacher> allTeachers = this.teacherRepository.getAll();
            
            if (allTeachers.isEmpty()) {
                System.out.println("\nNo teachers found in the system. Please create a teacher first.");
                System.out.print("Create a new teacher? (yes/no): ");
                String response = this.scanner.nextLine();
                if (response.equalsIgnoreCase("yes")) {
                    System.out.print("Enter Teacher ID: ");
                    int userId = Integer.parseInt(this.scanner.nextLine());
                    System.out.print("Enter Name: ");
                    String name = this.scanner.nextLine();
                    System.out.print("Enter Email: ");
                    String email = this.scanner.nextLine();
                    System.out.print("Enter Username: ");
                    String username = this.scanner.nextLine();
                    System.out.print("Enter Password: ");
                    String password = this.scanner.nextLine();
                    
                    Teacher newTeacher = new Teacher(userId, name, email, username, password);
                    this.teacherRepository.add(newTeacher);
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
            int teacherChoice = Integer.parseInt(this.scanner.nextLine());
            
            if (teacherChoice <= 0 || teacherChoice > allTeachers.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            
            Teacher teacher = allTeachers.get(teacherChoice - 1);
            
            System.out.print("Enter password: ");
            String enteredPassword = this.scanner.nextLine();
            
            if (!enteredPassword.equals(teacher.getPasswordForAuth())) {
                System.out.println("❌ Invalid password. Access denied.");
                return;
            }
            
            System.out.println("✓ Authentication successful!");
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
                    int choice = Integer.parseInt(this.scanner.nextLine());
                    
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

    public void loadTeacherCourses(Teacher teacher) {
        try {
            List<Course> allCourses = this.courseRepository.getAll();
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

    public void loadStudentsForTeacher(Teacher teacher) {
        try {
            List<Student> allStudents = this.studentRepository.getAll();
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

    public void handleAssignmentManagement(Teacher teacher) {
        while (true) {
            try {
                teacher.showAssignmentManagementMenu();
                int choice = Integer.parseInt(this.scanner.nextLine());
                
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

    public void createAssignment(Teacher teacher) {
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
            int courseChoice = Integer.parseInt(this.scanner.nextLine());
            if (courseChoice < 1 || courseChoice > teacher.getCourses().size()) {
                System.out.println("Invalid course selection.");
                return;
            }
            
            Course selectedCourse = teacher.getCourses().get(courseChoice - 1);
            
            System.out.print("Enter Assignment Title: ");
            String title = this.scanner.nextLine();
            
            System.out.print("Enter Description: ");
            String description = this.scanner.nextLine();
            
            System.out.print("Enter Due Date (YYYY-MM-DD): ");
            String dueDate = this.scanner.nextLine();
            
            int assignmentId = this.assignmentRepository.getNextAssignmentId();
            Assignment assignment = new Assignment(assignmentId, selectedCourse.getCourseId(), 
                                                  teacher.getUserId(), title, description, dueDate);
            
            this.assignmentRepository.add(assignment);
            System.out.println("Assignment created successfully! ID: " + assignmentId);
            
        } catch (Exception e) {
            System.out.println("Error creating assignment: " + e.getMessage());
        }
    }

    public void viewTeacherAssignments(Teacher teacher) {
        try {
            List<Assignment> assignments = this.assignmentRepository.getAssignmentsByTeacherId(teacher.getUserId());
            
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

    public void updateAssignment(Teacher teacher) {
        try {
            List<Assignment> assignments = this.assignmentRepository.getAssignmentsByTeacherId(teacher.getUserId());
            
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
            int choice = Integer.parseInt(this.scanner.nextLine());
            if (choice < 1 || choice > assignments.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            
            Assignment assignment = assignments.get(choice - 1);
            
            System.out.print("Enter new title (or press Enter to keep '" + assignment.getTitle() + "'): ");
            String title = this.scanner.nextLine();
            if (!title.trim().isEmpty()) {
                assignment.setTitle(title);
            }
            
            System.out.print("Enter new description (or press Enter to keep current): ");
            String description = this.scanner.nextLine();
            if (!description.trim().isEmpty()) {
                assignment.setDescription(description);
            }
            
            System.out.print("Enter new due date (or press Enter to keep '" + assignment.getDueDate() + "'): ");
            String dueDate = this.scanner.nextLine();
            if (!dueDate.trim().isEmpty()) {
                assignment.setDueDate(dueDate);
            }
            
            this.assignmentRepository.update(assignment);
            System.out.println("Assignment updated successfully!");
            
        } catch (Exception e) {
            System.out.println("Error updating assignment: " + e.getMessage());
        }
    }

    public void deleteAssignment(Teacher teacher) {
        try {
            List<Assignment> assignments = this.assignmentRepository.getAssignmentsByTeacherId(teacher.getUserId());
            
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
            int choice = Integer.parseInt(this.scanner.nextLine());
            if (choice < 1 || choice > assignments.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            
            Assignment assignment = assignments.get(choice - 1);
            System.out.print("Are you sure you want to delete '" + assignment.getTitle() + "'? (yes/no): ");
            String confirm = this.scanner.nextLine();
            
            if (confirm.equalsIgnoreCase("yes")) {
                this.assignmentRepository.delete(assignment);
                System.out.println("Assignment deleted successfully!");
            } else {
                System.out.println("Deletion cancelled.");
            }
            
        } catch (Exception e) {
            System.out.println("Error deleting assignment: " + e.getMessage());
        }
    }

    public void handleGradesManagement(Teacher teacher) {
        while (true) {
            try {
                teacher.showGradesManagementMenu();
                int choice = Integer.parseInt(this.scanner.nextLine());
                
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

    public void assignGrade(Teacher teacher) {
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
            int courseChoice = Integer.parseInt(this.scanner.nextLine());
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
            int studentChoice = Integer.parseInt(this.scanner.nextLine());
            if (studentChoice < 1 || studentChoice > teacher.getStudents().size()) {
                System.out.println("Invalid student selection.");
                return;
            }
            
            Student selectedStudent = teacher.getStudents().get(studentChoice - 1);
            
            System.out.print("Enter grade (A, A-, B+, B, B-, C+, C, C-, D, F): ");
            String gradeValue = this.scanner.nextLine();
            
            Grade existingGrade = this.gradeRepository.getGradeByStudentAndCourse(
                selectedStudent.getUserId(), selectedCourse.getCourseId());
            
            if (existingGrade != null) {
                System.out.println("Grade already exists. Use update option to modify.");
                return;
            }
            
            Grade grade = new Grade(selectedStudent.getUserId(), selectedCourse.getCourseId(), 
                                   teacher.getUserId(), gradeValue);
            
            this.gradeRepository.add(grade);
            System.out.println("Grade assigned successfully!");
            
        } catch (Exception e) {
            System.out.println("Error assigning grade: " + e.getMessage());
        }
    }

    public void updateGrade(Teacher teacher) {
        try {
            List<Grade> grades = this.gradeRepository.getGradesByTeacherId(teacher.getUserId());
            
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
            int choice = Integer.parseInt(this.scanner.nextLine());
            if (choice < 1 || choice > grades.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            
            Grade grade = grades.get(choice - 1);
            
            System.out.print("Enter new grade (current: " + grade.getGrade() + "): ");
            String newGrade = this.scanner.nextLine();
            
            if (!newGrade.trim().isEmpty()) {
                grade.setGrade(newGrade);
                this.gradeRepository.update(grade);
                System.out.println("Grade updated successfully!");
            }
            
        } catch (Exception e) {
            System.out.println("Error updating grade: " + e.getMessage());
        }
    }

    public void viewGradesByCourse(Teacher teacher) {
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
            int courseChoice = Integer.parseInt(this.scanner.nextLine());
            if (courseChoice < 1 || courseChoice > teacher.getCourses().size()) {
                System.out.println("Invalid course selection.");
                return;
            }
            
            Course selectedCourse = teacher.getCourses().get(courseChoice - 1);
            List<Grade> grades = this.gradeRepository.getGradesByCourseId(selectedCourse.getCourseId());
            
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

    public void viewAllTeacherGrades(Teacher teacher) {
        try {
            List<Grade> grades = this.gradeRepository.getGradesByTeacherId(teacher.getUserId());
            
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

    public void handleMessaging(Teacher teacher) {
        while (true) {
            try {
                teacher.showMessagingMenu();
                int choice = Integer.parseInt(this.scanner.nextLine());
                
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

    public void viewTeacherMessages(Teacher teacher) {
        try {
            List<Message> messages = this.messageRepository.getMessagesForUser(teacher.getUserId(), "TEACHER");
            
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

    public void sendMessageToStudents(Teacher teacher) {
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
            int studentChoice = Integer.parseInt(this.scanner.nextLine());
            if (studentChoice < 1 || studentChoice > teacher.getStudents().size()) {
                System.out.println("Invalid student selection.");
                return;
            }
            
            Student selectedStudent = teacher.getStudents().get(studentChoice - 1);
            
            System.out.print("Enter your message: ");
            String messageContent = this.scanner.nextLine();
            
            int messageId = this.messageRepository.getNextMessageId();
            Message message = new Message(messageId, teacher.getUserId(), teacher.getName(), "TEACHER",
                                         selectedStudent.getUserId(), selectedStudent.getName(), "STUDENT",
                                         messageContent);
            
            this.messageRepository.add(message);
            System.out.println("Message sent successfully!");
            
        } catch (Exception e) {
            System.out.println("Error sending message: " + e.getMessage());
        }
    }

    public void viewUnreadMessages(Teacher teacher) {
        try {
            List<Message> unreadMessages = this.messageRepository.getUnreadMessagesForUser(teacher.getUserId(), "TEACHER");
            
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

    public void markMessagesAsRead(Teacher teacher) {
        try {
            List<Message> unreadMessages = this.messageRepository.getUnreadMessagesForUser(teacher.getUserId(), "TEACHER");
            
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
            String choice = this.scanner.nextLine();
            
            if (choice.equalsIgnoreCase("all")) {
                for (Message m : unreadMessages) {
                    m.setRead(true);
                    this.messageRepository.update(m);
                }
                System.out.println("All messages marked as read!");
            } else {
                int msgChoice = Integer.parseInt(choice);
                if (msgChoice >= 1 && msgChoice <= unreadMessages.size()) {
                    Message message = unreadMessages.get(msgChoice - 1);
                    message.setRead(true);
                    this.messageRepository.update(message);
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

    public void viewTeacherCourses(Teacher teacher) {
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

    public void viewTeacherStudents(Teacher teacher) {
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

    public void handleFileUpload(Teacher teacher) {
        try {
            System.out.println("\n=== File Upload ===");
            System.out.print("Enter file path to upload: ");
            String filePath = this.scanner.nextLine();
            
            if (filePath == null || filePath.trim().isEmpty()) {
                System.out.println("File path cannot be empty.");
                return;
            }
            
            String role = teacher.getRole().toLowerCase();
            FileUploadService fileService = (FileUploadService) this.uploadService;
            List<String> visibleTo = new ArrayList<>();
            List<Student> students = teacher.getStudents();
            
            System.out.println("\n=== Set File Visibility ===");
            System.out.println("1. Visible to ALL students");
            System.out.println("2. Visible to MY students only");
            System.out.println("3. Visible to SPECIFIC students");
            System.out.println("4. Private (only me and admins)");
            System.out.print("Choose option: ");
            
            String choice = this.scanner.nextLine();
            
            switch (choice) {
                case "1":
                    visibleTo.add("ALL");
                    break;
                case "2":
                    if (students.isEmpty()) {
                        System.out.println("You have no assigned students. File will be private.");
                    } else {
                        for (Student student : students) {
                            visibleTo.add(student.getEmail());
                        }
                        System.out.println("File will be visible to your " + students.size() + " student(s)");
                    }
                    break;
                case "3":
                    if (students.isEmpty()) {
                        System.out.println("You have no assigned students. File will be private.");
                    } else {
                        System.out.println("\nYour Students:");
                        for (int i = 0; i < students.size(); i++) {
                            System.out.println((i + 1) + ". " + students.get(i).getName() + 
                                             " (" + students.get(i).getEmail() + ")");
                        }
                        System.out.print("Enter student numbers (comma-separated, e.g., 1,3,5): ");
                        String input = this.scanner.nextLine();
                        String[] indices = input.split(",");
                        
                        for (String idx : indices) {
                            try {
                                int index = Integer.parseInt(idx.trim()) - 1;
                                if (index >= 0 && index < students.size()) {
                                    visibleTo.add(students.get(index).getEmail());
                                }
                            } catch (NumberFormatException e) {
                            }
                        }
                        System.out.println("File will be visible to " + visibleTo.size() + " student(s)");
                    }
                    break;
                case "4":
                default:
                    System.out.println("File will be private (only you and admins can see it)");
                    break;
            }
            
            fileService.uploadFile(filePath, teacher.getEmail(), role, visibleTo);
            
            System.out.println("\n--- Uploaded Files for " + teacher.getRole() + " ---");
            fileService.displayUploadedFilesByRole(role);
            
        } catch (Exception e) {
            System.out.println("Error uploading file: " + e.getMessage());
        }
    }
}
