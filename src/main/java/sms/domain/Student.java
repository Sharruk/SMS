package sms.domain;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.List;

@JsonTypeName("student")
public class Student extends User {
    private String studentId;
    private String major;
    private int year;
    private double gpa;
    private List<String> enrolledCourses;

    // Default constructor for Jackson
    public Student() {
        super();
        this.enrolledCourses = new ArrayList<>();
    }

    public Student(String userId, String firstName, String lastName, String email, String password,
                   String studentId, String major, int year) {
        super(userId, firstName, lastName, email, password);
        this.studentId = studentId;
        this.major = major;
        this.year = year;
        this.gpa = 0.0;
        this.enrolledCourses = new ArrayList<>();
    }

    @Override
    public String getRole() {
        return "STUDENT";
    }

    @Override
    public void displayUserInfo() {
        System.out.println("=== Student Information ===");
        System.out.println("Student ID: " + studentId);
        System.out.println("Name: " + getFullName());
        System.out.println("Email: " + getEmail());
        System.out.println("Major: " + major);
        System.out.println("Year: " + year);
        System.out.println("GPA: " + gpa);
        System.out.println("Enrolled Courses: " + enrolledCourses.size());
        System.out.println("Status: " + (isActive() ? "Active" : "Inactive"));
    }

    public void enrollInCourse(String courseId) {
        if (!enrolledCourses.contains(courseId)) {
            enrolledCourses.add(courseId);
            System.out.println("Successfully enrolled in course: " + courseId);
        } else {
            System.out.println("Already enrolled in course: " + courseId);
        }
    }

    public void dropCourse(String courseId) {
        if (enrolledCourses.remove(courseId)) {
            System.out.println("Successfully dropped course: " + courseId);
        } else {
            System.out.println("Not enrolled in course: " + courseId);
        }
    }

    // Getters and Setters
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public List<String> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void setEnrolledCourses(List<String> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", name='" + getFullName() + '\'' +
                ", major='" + major + '\'' +
                ", year=" + year +
                ", gpa=" + gpa +
                ", coursesCount=" + enrolledCourses.size() +
                '}';
    }
}