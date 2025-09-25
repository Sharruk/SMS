package sms.domain;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.List;

@JsonTypeName("teacher")
public class Teacher extends User {
    private String teacherId;
    private String department;
    private String specialization;
    private double salary;
    private List<String> teachingCourses;

    // Default constructor for Jackson
    public Teacher() {
        super();
        this.teachingCourses = new ArrayList<>();
    }

    public Teacher(String userId, String firstName, String lastName, String email, String password,
                   String teacherId, String department, String specialization, double salary) {
        super(userId, firstName, lastName, email, password);
        this.teacherId = teacherId;
        this.department = department;
        this.specialization = specialization;
        this.salary = salary;
        this.teachingCourses = new ArrayList<>();
    }

    @Override
    public String getRole() {
        return "TEACHER";
    }

    @Override
    public void displayUserInfo() {
        System.out.println("=== Teacher Information ===");
        System.out.println("Teacher ID: " + teacherId);
        System.out.println("Name: " + getFullName());
        System.out.println("Email: " + getEmail());
        System.out.println("Department: " + department);
        System.out.println("Specialization: " + specialization);
        System.out.println("Salary: $" + salary);
        System.out.println("Teaching Courses: " + teachingCourses.size());
        System.out.println("Status: " + (isActive() ? "Active" : "Inactive"));
    }

    public void assignCourse(String courseId) {
        if (!teachingCourses.contains(courseId)) {
            teachingCourses.add(courseId);
            System.out.println("Successfully assigned to course: " + courseId);
        } else {
            System.out.println("Already teaching course: " + courseId);
        }
    }

    public void removeCourse(String courseId) {
        if (teachingCourses.remove(courseId)) {
            System.out.println("Successfully removed from course: " + courseId);
        } else {
            System.out.println("Not teaching course: " + courseId);
        }
    }

    // Getters and Setters
    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public List<String> getTeachingCourses() {
        return teachingCourses;
    }

    public void setTeachingCourses(List<String> teachingCourses) {
        this.teachingCourses = teachingCourses;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "teacherId='" + teacherId + '\'' +
                ", name='" + getFullName() + '\'' +
                ", department='" + department + '\'' +
                ", specialization='" + specialization + '\'' +
                ", salary=" + salary +
                ", coursesCount=" + teachingCourses.size() +
                '}';
    }
}