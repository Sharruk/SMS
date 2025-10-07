import sms.services.FileUploadService;
import sms.domain.UploadMetadata;
import sms.exceptions.UploadException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * DEMONSTRATION: Role-Based File Visibility System
 * 
 * This demonstrates the new file upload visibility features:
 * 
 * 1. TEACHER UPLOADS:
 *    - Teachers can upload files visible only to their assigned students
 *    - Example: Dr. Robert Johnson uploads Assignment1.pdf
 *    - Visible to: [john.smith@uni.edu, sarah.johnson@uni.edu]
 *    - Other students see nothing
 * 
 * 2. ADMIN UPLOADS:
 *    - Admins can upload files visible to everyone using ["ALL"]
 *    - Admins can also upload to specific users
 * 
 * 3. STUDENT UPLOADS:
 *    - Students can upload files visible only to their teachers
 *    - Optionally visible to admins
 */
public class FILE_VISIBILITY_DEMO {
    
    public static void main(String[] args) {
        FileUploadService uploadService = new FileUploadService();
        
        System.out.println("====================================================");
        System.out.println("   FILE VISIBILITY SYSTEM DEMONSTRATION");
        System.out.println("====================================================\n");
        
        // ========================================
        // SCENARIO 1: Teacher uploads for specific students
        // ========================================
        System.out.println("SCENARIO 1: Teacher Upload");
        System.out.println("Teacher: Dr. Robert Johnson");
        System.out.println("File: Assignment1.pdf");
        System.out.println("Visible to: john.smith@uni.edu, sarah.johnson@uni.edu");
        System.out.println("---------------------------------------------------");
        
        try {
            // Teacher uploads file visible to specific students
            List<String> studentEmails = Arrays.asList("john.smith@uni.edu", "sarah.johnson@uni.edu");
            uploadService.uploadFileForStudents(
                "Assignment_Submission.txt",  // Using existing file for demo
                "robert.johnson@uni.edu",
                studentEmails
            );
            
            // Display what each student sees
            System.out.println("\nStudent John Smith logs in:");
            uploadService.displayFilesVisibleToUser("john.smith@uni.edu", "John Smith", "student");
            
            System.out.println("Student Sarah Johnson logs in:");
            uploadService.displayFilesVisibleToUser("sarah.johnson@uni.edu", "Sarah Johnson", "student");
            
            System.out.println("Other student (mike.wilson@uni.edu) logs in:");
            uploadService.displayFilesVisibleToUser("mike.wilson@uni.edu", "Mike Wilson", "student");
            
        } catch (UploadException e) {
            System.err.println("Upload error: " + e.getMessage());
        }
        
        // ========================================
        // SCENARIO 2: Admin uploads visible to ALL
        // ========================================
        System.out.println("\n====================================================");
        System.out.println("SCENARIO 2: Admin Upload (Visible to ALL)");
        System.out.println("Admin: admin@uni.edu");
        System.out.println("File: System_Announcement.pdf");
        System.out.println("Visible to: ALL");
        System.out.println("---------------------------------------------------");
        
        try {
            // Admin uploads file visible to everyone
            uploadService.uploadFileForAll(
                "Mini_Project_Report.docx",  // Using existing file for demo
                "admin@uni.edu"
            );
            
            System.out.println("\nAny student logs in:");
            uploadService.displayFilesVisibleToUser("john.smith@uni.edu", "John Smith", "student");
            
            System.out.println("Any teacher logs in:");
            uploadService.displayFilesVisibleToUser("robert.johnson@uni.edu", "Dr. Johnson", "teacher");
            
        } catch (UploadException e) {
            System.err.println("Upload error: " + e.getMessage());
        }
        
        // ========================================
        // SCENARIO 3: Student uploads for teacher
        // ========================================
        System.out.println("\n====================================================");
        System.out.println("SCENARIO 3: Student Upload (Visible to Teacher)");
        System.out.println("Student: John Smith");
        System.out.println("File: Homework_Submission.pdf");
        System.out.println("Visible to: robert.johnson@uni.edu");
        System.out.println("---------------------------------------------------");
        
        try {
            // Student uploads file visible to their teacher
            List<String> teacherEmails = Arrays.asList("robert.johnson@uni.edu");
            uploadService.uploadFileForTeachers(
                "test_document.txt",  // Using existing file for demo
                "john.smith@uni.edu",
                teacherEmails
            );
            
            System.out.println("\nTeacher (Dr. Johnson) logs in:");
            uploadService.displayFilesVisibleToUser("robert.johnson@uni.edu", "Dr. Johnson", "teacher");
            
            System.out.println("Other teacher logs in:");
            uploadService.displayFilesVisibleToUser("mary.williams@uni.edu", "Dr. Williams", "teacher");
            
            System.out.println("Admin logs in (can see everything):");
            uploadService.displayFilesVisibleToUser("admin@uni.edu", "Admin", "admin");
            
        } catch (UploadException e) {
            System.err.println("Upload error: " + e.getMessage());
        }
        
        // ========================================
        // SCENARIO 4: Admin uploads to specific users
        // ========================================
        System.out.println("\n====================================================");
        System.out.println("SCENARIO 4: Admin Upload (Specific Teachers)");
        System.out.println("Admin: admin@uni.edu");
        System.out.println("File: Faculty_Meeting_Notes.pdf");
        System.out.println("Visible to: robert.johnson@uni.edu, mary.williams@uni.edu");
        System.out.println("---------------------------------------------------");
        
        try {
            // Admin uploads file visible to specific teachers
            List<String> teacherEmails = Arrays.asList("robert.johnson@uni.edu", "mary.williams@uni.edu");
            uploadService.uploadFileForSpecificUsers(
                "PROJECT_REPORT.pdf",  // Using existing file for demo
                "admin@uni.edu",
                teacherEmails
            );
            
            System.out.println("\nDr. Johnson logs in:");
            uploadService.displayFilesVisibleToUser("robert.johnson@uni.edu", "Dr. Johnson", "teacher");
            
            System.out.println("Other teacher (not in list) logs in:");
            uploadService.displayFilesVisibleToUser("other.teacher@uni.edu", "Dr. Smith", "teacher");
            
        } catch (UploadException e) {
            System.err.println("Upload error: " + e.getMessage());
        }
        
        System.out.println("\n====================================================");
        System.out.println("   DEMONSTRATION COMPLETE");
        System.out.println("====================================================");
    }
}

/**
 * KEY FEATURES IMPLEMENTED:
 * 
 * 1. UploadMetadata now has 'visibleTo' field (List<String>)
 *    - Stores list of email addresses
 *    - Special value "ALL" for public visibility
 * 
 * 2. FileUploadService methods:
 *    - uploadFile(filePath, userName, role, visibleTo)
 *    - uploadFileForStudents(filePath, teacherEmail, studentEmails)
 *    - uploadFileForTeachers(filePath, studentEmail, teacherEmails)
 *    - uploadFileForAll(filePath, adminEmail)
 *    - uploadFileForSpecificUsers(filePath, adminEmail, emails)
 * 
 * 3. Visibility checking:
 *    - getFilesVisibleToUser(userEmail, userRole)
 *    - displayFilesVisibleToUser(userEmail, userName, userRole)
 * 
 * 4. Visibility rules:
 *    - Admin can see ALL files (administrative privilege)
 *    - Users can see files they uploaded
 *    - Users can see files where visibleTo contains "ALL"
 *    - Users can see files where visibleTo contains their email
 * 
 * USAGE EXAMPLES:
 * 
 * Teacher uploading for students:
 * ---
 * List<String> students = teacher.getStudents()
 *     .stream()
 *     .map(Student::getEmail)
 *     .collect(Collectors.toList());
 * uploadService.uploadFileForStudents("assignment.pdf", teacher.getEmail(), students);
 * 
 * Admin uploading public announcement:
 * ---
 * uploadService.uploadFileForAll("announcement.pdf", admin.getEmail());
 * 
 * Student submitting homework:
 * ---
 * List<String> teachers = Arrays.asList(student.getTeacherEmail());
 * uploadService.uploadFileForTeachers("homework.pdf", student.getEmail(), teachers);
 */
