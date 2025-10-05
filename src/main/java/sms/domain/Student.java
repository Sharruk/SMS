package sms.domain;

import com.fasterxml.jackson.annotation.JsonTypeName;
import sms.exceptions.ValidationException;
import sms.search.Searchable;
import sms.sort.Sortable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@JsonTypeName("student")
public class Student extends User implements Searchable<Course>, Sortable<Course> {
    private String id;
    private Department dept;
    private Batch batch;
    private List<Course> courses;

    // Default constructor for Jackson
    public Student() {
        super();
        this.courses = new ArrayList<>();
    }

    public Student(int userId, String name, String email, String username, String password) throws ValidationException {
        super(userId, name, email, username, password);
        this.id = "S" + userId;
        this.courses = new ArrayList<>();
    }

    @Override
    public String getRole() {
        return "STUDENT";
    }

    public void changePassword(String newPassword) throws ValidationException {
        if (newPassword == null || newPassword.length() < 6) {
            throw new ValidationException("Password must be at least 6 characters", "password", newPassword);
        }
        
        setPassword(newPassword);
        System.out.println("Student " + getName() + ": Password changed successfully");
    }

    public void viewAssignment() {
        System.out.println("Student " + getName() + ": Viewing assignments...");
        for (Course course : courses) {
            System.out.println("- Assignment for " + course.getCourseName());
        }
    }

    public void markOwnAttendance() throws ValidationException {
        if (courses.isEmpty()) {
            throw new ValidationException("No courses enrolled to mark attendance", "courses", "empty");
        }
        
        System.out.println("Student " + getName() + ": Marked attendance for " + courses.size() + " courses");
    }

    public void viewAttendance() {
        System.out.println("\n=== Attendance for Student " + getName() + " ===");
        for (Course course : courses) {
            System.out.println("Course: " + course.getCourseName() + " - Present");
        }
        System.out.println("====================================\n");
    }

    public void viewGrades() {
        System.out.println("\n=== Grades for Student " + getName() + " ===");
        for (Course course : courses) {
            System.out.println("Course: " + course.getCourseName() + " - Grade: A");
        }
        System.out.println("==================================\n");
    }

    // Helper methods
    public void addCourse(Course course) {
        if (course != null && !courses.contains(course)) {
            courses.add(course);
            System.out.println("Student " + getName() + ": Enrolled in " + course.getCourseId());
        }
    }

    public void removeCourse(Course course) {
        if (courses.remove(course)) {
            System.out.println("Student " + getName() + ": Dropped " + course.getCourseId());
        }
    }

    // Search implementation - Student can search their courses
    @Override
    public List<Course> search(String criteria) {
        return courses.stream()
                .filter(c -> c.getCourseName().toLowerCase().contains(criteria.toLowerCase()) ||
                           c.getCourseId().toLowerCase().contains(criteria.toLowerCase()) ||
                           (c.getFacultyName() != null && c.getFacultyName().toLowerCase().contains(criteria.toLowerCase())))
                .collect(Collectors.toList());
    }

    // Sort implementation - Student can sort their courses
    @Override
    public List<Course> sort(String criteria) {
        List<Course> sortedCourses = new ArrayList<>(courses);
        
        switch (criteria.toLowerCase()) {
            case "name":
                sortedCourses.sort((a, b) -> a.getCourseName().compareTo(b.getCourseName()));
                break;
            case "id":
                sortedCourses.sort((a, b) -> a.getCourseId().compareTo(b.getCourseId()));
                break;
            case "credits":
                sortedCourses.sort((a, b) -> Integer.compare(a.getCreditHours(), b.getCreditHours()));
                break;
            default:
                // Default sort by course name
                sortedCourses.sort((a, b) -> a.getCourseName().compareTo(b.getCourseName()));
        }
        
        return sortedCourses;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Department getDept() {
        return dept;
    }

    public void setDept(Department dept) {
        this.dept = dept;
    }

    public Batch getBatch() {
        return batch;
    }

    public void setBatch(Batch batch) {
        this.batch = batch;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    @Override
    public String toString() {
        return "Student{" +
                "userId=" + getUserId() +
                ", name='" + getName() + '\'' +
                ", id='" + id + '\'' +
                ", dept=" + (dept != null ? dept.getName() : "Not Assigned") +
                ", batch=" + (batch != null ? batch.getYearRange() : "Not Assigned") +
                ", courses=" + courses.size() +
                '}';
    }

    public void showMenu() {
        System.out.println("\n==================================================");
        System.out.println("         STUDENT MENU");
        System.out.println("==================================================");
        System.out.println("1. Assignment Management");
        System.out.println("2. View Grades");
        System.out.println("3. Messaging");
        System.out.println("4. Dashboard");
        System.out.println("5. View My Courses");
        System.out.println("0. Logout");
        System.out.println("==================================================");
        System.out.print("Choose an option: ");
    }

    public void showAssignmentManagementMenu() {
        System.out.println("\n=== Assignment Management ===");
        System.out.println("1. View Assignments");
        System.out.println("2. Submit Assignment");
        System.out.println("3. Update Submission");
        System.out.println("4. Delete Submission");
        System.out.println("0. Back to Main Menu");
        System.out.print("Choose an option: ");
    }

    public void showMessagingMenu() {
        System.out.println("\n=== Messaging ===");
        System.out.println("1. View All Messages");
        System.out.println("2. Send Message to Teacher");
        System.out.println("3. View Unread Messages");
        System.out.println("4. Mark Messages as Read");
        System.out.println("0. Back to Main Menu");
        System.out.print("Choose an option: ");
    }

    public void showDashboard() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("   STUDENT DASHBOARD - " + getName());
        System.out.println("=".repeat(60));
        System.out.println("Student ID: " + getUserId());
        System.out.println("Email: " + getEmail());
        System.out.println("Courses Enrolled: " + courses.size());
        System.out.println("=".repeat(60));
    }
}