package sms.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a student user.
 *
 * Demonstrates:
 *   - INHERITANCE   : extends User
 *   - METHOD OVERRIDING : getRole() and displayMenu()
 *   - ENCAPSULATION : private fields with getters/setters
 */
public class Student extends User {

    private String department;
    private String batch;
    private List<String> enrolledCourseIds; // stores Course IDs the student is enrolled in

    // Constructor
    public Student(int id, String name, String email, String username,
                   String password, String department, String batch) {
        super(id, name, email, username, password); // calls User constructor
        this.department       = department;
        this.batch            = batch;
        this.enrolledCourseIds = new ArrayList<>();
    }

    // METHOD OVERRIDING - every subclass must return its own role
    @Override
    public String getRole() {
        return "STUDENT";
    }

    // METHOD OVERRIDING - each role shows a different menu
    @Override
    public void displayMenu() {
        System.out.println("\n==================================================");
        System.out.println("              STUDENT MENU - " + getName());
        System.out.println("==================================================");
        System.out.println("1. View My Enrolled Courses");
        System.out.println("2. View My Grades");
        System.out.println("3. View My Assignments");
        System.out.println("0. Logout");
        System.out.println("==================================================");
        System.out.print("Choose an option: ");
    }

    // Enroll the student in a course (add course ID to list)
    public void enrollInCourse(String courseId) {
        if (!enrolledCourseIds.contains(courseId)) {
            enrolledCourseIds.add(courseId);
        }
    }

    // Getters and setters
    public String getDepartment()              { return department; }
    public void   setDepartment(String dept)   { this.department = dept; }

    public String getBatch()                   { return batch; }
    public void   setBatch(String batch)       { this.batch = batch; }

    public List<String> getEnrolledCourseIds() { return enrolledCourseIds; }
    public void setEnrolledCourseIds(List<String> ids) { this.enrolledCourseIds = ids; }

    @Override
    public String toString() {
        return super.toString() + " | Dept: " + department + " | Batch: " + batch;
    }

    // Convert to comma-separated line for file storage
    // Format: id,name,email,username,password,department,batch,courseId1;courseId2
    public String toFileString() {
        String courses = String.join(";", enrolledCourseIds);
        return getId() + "," + getName() + "," + getEmail() + "," +
               getUsername() + "," + getPassword() + "," +
               department + "," + batch + "," + courses;
    }

    // Create a Student object from a stored CSV line
    public static Student fromFileString(String line) {
        String[] parts = line.split(",", -1);
        Student s = new Student(
            Integer.parseInt(parts[0].trim()),
            parts[1].trim(), parts[2].trim(),
            parts[3].trim(), parts[4].trim(),
            parts[5].trim(), parts[6].trim()
        );
        // Parse enrolled course IDs (8th field, semicolon-separated)
        if (parts.length > 7 && !parts[7].trim().isEmpty()) {
            for (String cid : parts[7].trim().split(";")) {
                s.enrollInCourse(cid.trim());
            }
        }
        return s;
    }
}
