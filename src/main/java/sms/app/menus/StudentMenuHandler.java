package sms.app.menus;

import sms.data.*;
import sms.domain.*;
import sms.exceptions.*;
import sms.validation.InputValidator;
import sms.services.FileUploadService;
import sms.services.UploadService;
import java.io.File;
import java.util.*;

public class StudentMenuHandler {
    private Scanner scanner;
    private Repository<Student> studentRepository;
    private Repository<Course> courseRepository;
    private Repository<Teacher> teacherRepository;
    private AssignmentRepository assignmentRepository;
    private GradeRepository gradeRepository;
    private MessageRepository messageRepository;
    private SubmissionRepository submissionRepository;
    private UploadService<File> uploadService;

    public StudentMenuHandler(Scanner scanner, Repository<Student> studentRepo,
                             Repository<Course> courseRepo, AssignmentRepository assignmentRepo,
                             GradeRepository gradeRepo, MessageRepository messageRepo,
                             SubmissionRepository submissionRepo) {
        this.scanner = scanner;
        this.studentRepository = studentRepo;
        this.courseRepository = courseRepo;
        this.assignmentRepository = assignmentRepo;
        this.gradeRepository = gradeRepo;
        this.messageRepository = messageRepo;
        this.submissionRepository = submissionRepo;
        this.uploadService = new FileUploadService();
    }

    public void demonstrateStudentAccess() {
        System.out.println("\n--- Student Access Mode ---");
        System.out.println("Would you like to:");
        System.out.println("1. Run Quick Demo (automated)");
        System.out.println("2. Interactive Student Menu");
        System.out.print("Choose: ");
        
        try {
            int choice = Integer.parseInt(this.scanner.nextLine());
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

    public void runQuickStudentDemo() {
        System.out.println("\n--- Quick Student Demo ---");
        try {
            Student student = new Student(8001, "Student Demo", "student@lms.edu", "student", "pass123");
            student.viewAssignment();
            student.viewGrades();
            student.viewAttendance();
            
            List<Course> results = student.search("Demo");
            System.out.println("Student search results: " + results.size() + " courses found");
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void runStudentMenu() {
        try {
            List<Student> allStudents = this.studentRepository.getAll();
            
            if (allStudents.isEmpty()) {
                System.out.println("\nNo students found in the system. Please register a student first.");
                System.out.print("Create a new student? (yes/no): ");
                String response = this.scanner.nextLine();
                if (response.equalsIgnoreCase("yes")) {
                    System.out.print("Enter Student ID: ");
                    int userId = Integer.parseInt(this.scanner.nextLine());
                    System.out.print("Enter Name: ");
                    String name = this.scanner.nextLine();
                    System.out.print("Enter Email: ");
                    String email = this.scanner.nextLine();
                    System.out.print("Enter Username: ");
                    String username = this.scanner.nextLine();
                    System.out.print("Enter Password: ");
                    String password = this.scanner.nextLine();
                    
                    Student newStudent = new Student(userId, name, email, username, password);
                    this.studentRepository.add(newStudent);
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
            int studentChoice = Integer.parseInt(this.scanner.nextLine());
            
            if (studentChoice <= 0 || studentChoice > allStudents.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            
            Student student = allStudents.get(studentChoice - 1);
            
            System.out.print("Enter password: ");
            String enteredPassword = this.scanner.nextLine();
            
            if (!enteredPassword.equals(student.getPasswordForAuth())) {
                System.out.println("❌ Invalid password. Access denied.");
                return;
            }
            
            System.out.println("✓ Authentication successful!");
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
                    int choice = Integer.parseInt(this.scanner.nextLine());
                    
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

    public void loadStudentCourses(Student student) {
        try {
            List<Course> allCourses = this.courseRepository.getAll();
            for (Course course : allCourses) {
                student.addCourse(course);
            }
        } catch (Exception e) {
            System.out.println("Warning: Could not load courses: " + e.getMessage());
        }
    }

    public void handleAssignmentManagement(Student student) {
        while (true) {
            try {
                student.showAssignmentManagementMenu();
                int choice = Integer.parseInt(this.scanner.nextLine());
                
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

    public void viewStudentAssignments(Student student) {
        try {
            if (student.getCourses().isEmpty()) {
                System.out.println("\nYou are not enrolled in any courses.");
                return;
            }
            
            System.out.println("\n=== Your Assignments ===");
            boolean foundAssignments = false;
            
            for (Course course : student.getCourses()) {
                List<Assignment> courseAssignments = this.assignmentRepository.getAssignmentsByCourseId(course.getCourseId());
                
                if (!courseAssignments.isEmpty()) {
                    foundAssignments = true;
                    System.out.println("\nCourse: " + course.getCourseName() + " (" + course.getCourseId() + ")");
                    for (Assignment a : courseAssignments) {
                        System.out.println("  Assignment ID: " + a.getId());
                        System.out.println("  Title: " + a.getTitle());
                        System.out.println("  Description: " + a.getDescription());
                        System.out.println("  Due Date: " + a.getDueDate());
                        
                        Submission submission = this.submissionRepository.getSubmissionByStudentAndAssignment(
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

    public void submitAssignment(Student student) {
        try {
            java.util.ArrayList<Assignment> availableAssignments = new java.util.ArrayList<>();
            
            for (Course course : student.getCourses()) {
                List<Assignment> courseAssignments = this.assignmentRepository.getAssignmentsByCourseId(course.getCourseId());
                for (Assignment a : courseAssignments) {
                    Submission existing = this.submissionRepository.getSubmissionByStudentAndAssignment(
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
            int choice = Integer.parseInt(this.scanner.nextLine());
            if (choice < 1 || choice > availableAssignments.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            
            Assignment assignment = availableAssignments.get(choice - 1);
            
            System.out.print("Enter file path to upload: ");
            String fileName = this.scanner.nextLine();
            
            try {
                File file = new File(fileName);
                this.uploadService.store(file);
                this.uploadService.saveMetadata(file);
                String filePath = this.uploadService.getUploadDirectory() + fileName;
                
                int submissionId = this.submissionRepository.getNextSubmissionId();
                Submission submission = new Submission(submissionId, assignment.getId(), 
                                                      student.getUserId(), fileName, filePath);
                
                this.submissionRepository.add(submission);
                System.out.println("Assignment submitted successfully!");
                System.out.println("Submission ID: " + submissionId);
                
            } catch (UploadException e) {
                System.out.println("Upload failed: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.out.println("Error submitting assignment: " + e.getMessage());
        }
    }

    public void updateSubmission(Student student) {
        try {
            List<Submission> mySubmissions = this.submissionRepository.getSubmissionsByStudentId(student.getUserId());
            
            if (mySubmissions.isEmpty()) {
                System.out.println("You have no submissions to update.");
                return;
            }
            
            System.out.println("\n--- Update Submission ---");
            for (int i = 0; i < mySubmissions.size(); i++) {
                Submission s = mySubmissions.get(i);
                Assignment assignment = null;
                try {
                    assignment = this.assignmentRepository.getAssignmentById(s.getAssignmentId());
                } catch (NotFoundException e) {
                    assignment = null;
                }
                System.out.println((i + 1) + ". Assignment: " + (assignment != null ? assignment.getTitle() : "Unknown"));
                System.out.println("   File: " + s.getFileName());
                System.out.println("   Submitted: " + s.getTimestamp());
            }
            
            System.out.print("Select submission to update (1, 2, etc.): ");
            int choice = Integer.parseInt(this.scanner.nextLine());
            if (choice < 1 || choice > mySubmissions.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            
            Submission submission = mySubmissions.get(choice - 1);
            
            System.out.print("Enter new file name to upload: ");
            String fileName = this.scanner.nextLine();
            
            try {
                File file = new File(fileName);
                this.uploadService.store(file);
                this.uploadService.saveMetadata(file);
                String filePath = this.uploadService.getUploadDirectory() + fileName;
                
                submission.setFileName(fileName);
                submission.setFilePath(filePath);
                submission.setTimestamp(java.time.LocalDateTime.now().format(
                    java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                
                this.submissionRepository.update(submission);
                System.out.println("Submission updated successfully!");
                
            } catch (UploadException e) {
                System.out.println("Upload failed: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.out.println("Error updating submission: " + e.getMessage());
        }
    }

    public void deleteSubmission(Student student) {
        try {
            List<Submission> mySubmissions = this.submissionRepository.getSubmissionsByStudentId(student.getUserId());
            
            if (mySubmissions.isEmpty()) {
                System.out.println("You have no submissions to delete.");
                return;
            }
            
            System.out.println("\n--- Delete Submission ---");
            for (int i = 0; i < mySubmissions.size(); i++) {
                Submission s = mySubmissions.get(i);
                Assignment assignment = null;
                try {
                    assignment = this.assignmentRepository.getAssignmentById(s.getAssignmentId());
                } catch (NotFoundException e) {
                    assignment = null;
                }
                System.out.println((i + 1) + ". Assignment: " + (assignment != null ? assignment.getTitle() : "Unknown"));
                System.out.println("   File: " + s.getFileName());
            }
            
            System.out.print("Select submission to delete (1, 2, etc.): ");
            int choice = Integer.parseInt(this.scanner.nextLine());
            if (choice < 1 || choice > mySubmissions.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            
            Submission submission = mySubmissions.get(choice - 1);
            System.out.print("Are you sure you want to delete this submission? (yes/no): ");
            String confirm = this.scanner.nextLine();
            
            if (confirm.equalsIgnoreCase("yes")) {
                this.submissionRepository.delete(submission);
                System.out.println("Submission deleted successfully!");
            } else {
                System.out.println("Deletion cancelled.");
            }
            
        } catch (Exception e) {
            System.out.println("Error deleting submission: " + e.getMessage());
        }
    }

    public void handleViewGrades(Student student) {
        try {
            List<Grade> allGrades = this.gradeRepository.getAll();
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
            
            int choice = Integer.parseInt(this.scanner.nextLine());
            
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
                int courseChoice = Integer.parseInt(this.scanner.nextLine());
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

    public double convertGradeToPoints(String grade) {
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

    public void handleStudentMessaging(Student student) {
        while (true) {
            try {
                student.showMessagingMenu();
                int choice = Integer.parseInt(this.scanner.nextLine());
                
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

    public void viewStudentMessages(Student student) {
        try {
            List<Message> messages = this.messageRepository.getMessagesForUser(student.getUserId(), "STUDENT");
            
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

    public void sendMessageToTeacher(Student student) {
        try {
            List<Teacher> allTeachers = this.teacherRepository.getAll();
            
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
            int teacherChoice = Integer.parseInt(this.scanner.nextLine());
            if (teacherChoice < 1 || teacherChoice > allTeachers.size()) {
                System.out.println("Invalid teacher selection.");
                return;
            }
            
            Teacher selectedTeacher = allTeachers.get(teacherChoice - 1);
            
            System.out.print("Enter your message: ");
            String messageContent = this.scanner.nextLine();
            
            int messageId = this.messageRepository.getNextMessageId();
            Message message = new Message(messageId, student.getUserId(), student.getName(), "STUDENT",
                                         selectedTeacher.getUserId(), selectedTeacher.getName(), "TEACHER",
                                         messageContent);
            
            this.messageRepository.add(message);
            System.out.println("Message sent successfully!");
            
        } catch (Exception e) {
            System.out.println("Error sending message: " + e.getMessage());
        }
    }

    public void viewStudentUnreadMessages(Student student) {
        try {
            List<Message> unreadMessages = this.messageRepository.getUnreadMessagesForUser(student.getUserId(), "STUDENT");
            
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

    public void markStudentMessagesAsRead(Student student) {
        try {
            List<Message> unreadMessages = this.messageRepository.getUnreadMessagesForUser(student.getUserId(), "STUDENT");
            
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

    public void showStudentDashboard(Student student) {
        try {
            student.showDashboard();
            
            java.util.ArrayList<Assignment> pendingAssignments = new java.util.ArrayList<>();
            for (Course course : student.getCourses()) {
                List<Assignment> courseAssignments = this.assignmentRepository.getAssignmentsByCourseId(course.getCourseId());
                for (Assignment a : courseAssignments) {
                    Submission submission = this.submissionRepository.getSubmissionByStudentAndAssignment(
                        student.getUserId(), a.getId());
                    if (submission == null) {
                        pendingAssignments.add(a);
                    }
                }
            }
            
            List<Submission> mySubmissions = this.submissionRepository.getSubmissionsByStudentId(student.getUserId());
            List<Grade> myGrades = this.gradeRepository.getAll().stream()
                .filter(g -> g.getStudentId() == student.getUserId())
                .collect(java.util.stream.Collectors.toList());
            List<Message> unreadMessages = this.messageRepository.getUnreadMessagesForUser(student.getUserId(), "STUDENT");
            
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

    public void viewStudentCourses(Student student) {
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

    public void handleFileUpload(Student student) {
        try {
            System.out.println("\n=== File Upload ===");
            System.out.print("Enter file path to upload: ");
            String filePath = this.scanner.nextLine();
            
            if (filePath == null || filePath.trim().isEmpty()) {
                System.out.println("File path cannot be empty.");
                return;
            }
            
            String role = student.getRole().toLowerCase();
            FileUploadService fileService = (FileUploadService) this.uploadService;
            List<String> visibleTo = new ArrayList<>();
            
            System.out.println("\n=== Set File Visibility ===");
            System.out.println("1. Visible to my teachers");
            System.out.println("2. Private (only me and admins)");
            System.out.print("Choose option: ");
            
            String choice = this.scanner.nextLine();
            
            if ("1".equals(choice)) {
                try {
                    List<Teacher> allTeachers = this.teacherRepository.getAll();
                    for (Teacher teacher : allTeachers) {
                        for (Course teacherCourse : teacher.getCourses()) {
                            for (Course studentCourse : student.getCourses()) {
                                if (teacherCourse.getCourseId().equals(studentCourse.getCourseId())) {
                                    if (!visibleTo.contains(teacher.getEmail())) {
                                        visibleTo.add(teacher.getEmail());
                                    }
                                }
                            }
                        }
                    }
                    if (!visibleTo.isEmpty()) {
                        System.out.println("File will be visible to " + visibleTo.size() + " teacher(s)");
                    }
                } catch (Exception e) {
                    System.out.println("Could not load teachers. File will be private.");
                }
            }
            
            fileService.uploadFile(filePath, student.getEmail(), role, visibleTo);
            
            System.out.println("\n--- Uploaded Files for " + student.getRole() + " ---");
            fileService.displayUploadedFilesByRole(role);
            
        } catch (Exception e) {
            System.out.println("Error uploading file: " + e.getMessage());
        }
    }
}
