package sms.domain;

import com.fasterxml.jackson.annotation.JsonTypeName;
import sms.exceptions.ValidationException;
import sms.search.Searchable;
import sms.sort.Sortable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@JsonTypeName("teacher")
public class Teacher extends User implements Searchable<Student>, Sortable<Student> {
    private String id;
    private List<Course> courses;
    private List<Batch> batches;
    private List<Student> students; // Students taught by this teacher

    // Default constructor for Jackson
    public Teacher() {
        super();
        this.courses = new ArrayList<>();
        this.batches = new ArrayList<>();
        this.students = new ArrayList<>();
    }

    public Teacher(int userId, String name, String email, String username, String password) throws ValidationException {
        super(userId, name, email, username, password);
        this.id = "T" + userId;
        this.courses = new ArrayList<>();
        this.batches = new ArrayList<>();
        this.students = new ArrayList<>();
    }

    @Override
    public String getRole() {
        return "TEACHER";
    }

    public void changePassword(String newPassword) throws ValidationException {
        if (newPassword == null || newPassword.length() < 6) {
            throw new ValidationException("Password must be at least 6 characters", "password", newPassword);
        }
        
        setPassword(newPassword);
        System.out.println("Teacher " + getName() + ": Password changed successfully");
    }

    public void createAssignment() throws ValidationException {
        if (courses.isEmpty()) {
            throw new ValidationException("Teacher must have assigned courses to create assignments", "courses", "empty");
        }
        
        System.out.println("Teacher " + getName() + ": Created assignment for " + courses.size() + " courses");
    }

    public void markAttendance() throws ValidationException {
        if (students.isEmpty()) {
            throw new ValidationException("No students assigned to mark attendance", "students", "empty");
        }
        
        System.out.println("Teacher " + getName() + ": Marked attendance for " + students.size() + " students");
    }

    public void viewStudents() {
        System.out.println("\n=== Students under Teacher " + getName() + " ===");
        if (students.isEmpty()) {
            System.out.println("No students assigned.");
        } else {
            for (Student student : students) {
                System.out.println("- " + student.getName() + " (ID: " + student.getUserId() + ")");
            }
        }
        System.out.println("Total: " + students.size() + " students\n");
    }

    public void viewSubmittedAssignments() {
        System.out.println("Teacher " + getName() + ": Viewing submitted assignments...");
        System.out.println("Assignments from " + students.size() + " students in " + courses.size() + " courses");
    }

    public void viewAllAttendance() {
        System.out.println("Teacher " + getName() + ": Viewing attendance records...");
        for (Course course : courses) {
            System.out.println("Course: " + course.getCourseName() + " - " + students.size() + " students");
        }
    }

    // Helper methods
    public void addCourse(Course course) {
        if (course != null && !courses.contains(course)) {
            courses.add(course);
            System.out.println("Teacher " + getName() + ": Added course " + course.getCourseId());
        }
    }

    public void addBatch(Batch batch) {
        if (batch != null && !batches.contains(batch)) {
            batches.add(batch);
            System.out.println("Teacher " + getName() + ": Added batch " + batch.getYearRange());
        }
    }

    public void addStudent(Student student) {
        if (student != null && !students.contains(student)) {
            students.add(student);
            System.out.println("Teacher " + getName() + ": Added student " + student.getName());
        }
    }

    // Search implementation - Teacher can search their students
    @Override
    public List<Student> search(String criteria) {
        return students.stream()
                .filter(s -> s.getName().toLowerCase().contains(criteria.toLowerCase()) ||
                           s.getEmail().toLowerCase().contains(criteria.toLowerCase()) ||
                           String.valueOf(s.getUserId()).contains(criteria))
                .collect(Collectors.toList());
    }

    // Sort implementation - Teacher can sort their students
    @Override
    public List<Student> sort(String criteria) {
        List<Student> sortedStudents = new ArrayList<>(students);
        
        switch (criteria.toLowerCase()) {
            case "name":
                sortedStudents.sort((a, b) -> a.getName().compareTo(b.getName()));
                break;
            case "id":
                sortedStudents.sort((a, b) -> Integer.compare(a.getUserId(), b.getUserId()));
                break;
            case "email":
                sortedStudents.sort((a, b) -> a.getEmail().compareTo(b.getEmail()));
                break;
            default:
                // Default sort by name
                sortedStudents.sort((a, b) -> a.getName().compareTo(b.getName()));
        }
        
        return sortedStudents;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public List<Batch> getBatches() {
        return batches;
    }

    public void setBatches(List<Batch> batches) {
        this.batches = batches;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public void showMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("         TEACHER MENU");
        System.out.println("=".repeat(50));
        System.out.println("1. Assignment Management");
        System.out.println("2. Grades Management");
        System.out.println("3. Messaging System");
        System.out.println("4. Dashboard");
        System.out.println("5. View My Courses");
        System.out.println("6. View My Students");
        System.out.println("7. Upload File");
        System.out.println("0. Logout");
        System.out.println("=".repeat(50));
        System.out.print("Choose an option: ");
    }

    public void showAssignmentManagementMenu() {
        System.out.println("\n--- Assignment Management ---");
        System.out.println("1. Create New Assignment");
        System.out.println("2. View All My Assignments");
        System.out.println("3. Update Assignment");
        System.out.println("4. Delete Assignment");
        System.out.println("0. Back to Main Menu");
        System.out.print("Choose an option: ");
    }

    public void showGradesManagementMenu() {
        System.out.println("\n--- Grades Management ---");
        System.out.println("1. Assign Grade to Student");
        System.out.println("2. Update Student Grade");
        System.out.println("3. View Grades by Course");
        System.out.println("4. View All Grades Given");
        System.out.println("0. Back to Main Menu");
        System.out.print("Choose an option: ");
    }

    public void showMessagingMenu() {
        System.out.println("\n--- Messaging System ---");
        System.out.println("1. View My Messages");
        System.out.println("2. Send Message to Students");
        System.out.println("3. View Unread Messages");
        System.out.println("4. Mark Messages as Read");
        System.out.println("0. Back to Main Menu");
        System.out.print("Choose an option: ");
    }

    public void showDashboard() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("         TEACHER DASHBOARD - " + getName());
        System.out.println("=".repeat(60));
        System.out.println("User ID: " + getUserId());
        System.out.println("Email: " + getEmail());
        System.out.println("\nCourse Summary:");
        System.out.println("  Total Courses: " + courses.size());
        if (!courses.isEmpty()) {
            System.out.println("  Courses:");
            for (Course course : courses) {
                System.out.println("    - " + course.getCourseName() + " (" + course.getCourseId() + ")");
            }
        }
        System.out.println("\nStudent Summary:");
        System.out.println("  Total Students: " + students.size());
        System.out.println("=".repeat(60));
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "userId=" + getUserId() +
                ", name='" + getName() + '\'' +
                ", id='" + id + '\'' +
                ", courses=" + courses.size() +
                ", students=" + students.size() +
                '}';
    }
}