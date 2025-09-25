package sms.domain;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private String courseId;
    private String courseName;
    private String description;
    private int credits;
    private String teacherId;
    private int maxEnrollment;
    private List<String> enrolledStudents;
    private boolean isActive;

    // Default constructor for Jackson
    public Course() {
        this.enrolledStudents = new ArrayList<>();
    }

    public Course(String courseId, String courseName, String description, int credits, int maxEnrollment) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.description = description;
        this.credits = credits;
        this.maxEnrollment = maxEnrollment;
        this.enrolledStudents = new ArrayList<>();
        this.isActive = true;
    }

    public boolean enrollStudent(String studentId) {
        if (enrolledStudents.size() >= maxEnrollment) {
            System.out.println("Course is full. Cannot enroll student: " + studentId);
            return false;
        }
        if (!enrolledStudents.contains(studentId)) {
            enrolledStudents.add(studentId);
            System.out.println("Student " + studentId + " enrolled in course: " + courseId);
            return true;
        } else {
            System.out.println("Student " + studentId + " is already enrolled in course: " + courseId);
            return false;
        }
    }

    public boolean dropStudent(String studentId) {
        if (enrolledStudents.remove(studentId)) {
            System.out.println("Student " + studentId + " dropped from course: " + courseId);
            return true;
        } else {
            System.out.println("Student " + studentId + " is not enrolled in course: " + courseId);
            return false;
        }
    }

    @com.fasterxml.jackson.annotation.JsonIgnore
    public int getAvailableSlots() {
        return maxEnrollment - enrolledStudents.size();
    }

    public void displayCourseInfo() {
        System.out.println("=== Course Information ===");
        System.out.println("Course ID: " + courseId);
        System.out.println("Course Name: " + courseName);
        System.out.println("Description: " + description);
        System.out.println("Credits: " + credits);
        System.out.println("Teacher ID: " + (teacherId != null ? teacherId : "Not Assigned"));
        System.out.println("Enrollment: " + enrolledStudents.size() + "/" + maxEnrollment);
        System.out.println("Available Slots: " + getAvailableSlots());
        System.out.println("Status: " + (isActive ? "Active" : "Inactive"));
    }

    // Getters and Setters
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public int getMaxEnrollment() {
        return maxEnrollment;
    }

    public void setMaxEnrollment(int maxEnrollment) {
        this.maxEnrollment = maxEnrollment;
    }

    public List<String> getEnrolledStudents() {
        return enrolledStudents;
    }

    public void setEnrolledStudents(List<String> enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId='" + courseId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", credits=" + credits +
                ", enrollment=" + enrolledStudents.size() + "/" + maxEnrollment +
                ", isActive=" + isActive +
                '}';
    }
}