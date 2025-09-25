package sms.services;

import sms.data.Repository;
import sms.domain.Student;
import sms.domain.Teacher;
import sms.domain.Course;
import sms.domain.User;
import sms.exceptions.AuthenticationException;
import sms.exceptions.RepositoryException;

import java.util.List;
import java.util.Optional;

public class AdminService {
    private final Repository<Student> studentRepository;
    private final Repository<Course> courseRepository;

    public AdminService(Repository<Student> studentRepository, Repository<Course> courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    // Student Management
    public Student addStudent(Student student) throws RepositoryException {
        System.out.println("AdminService: Adding new student - " + student.getFullName());
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() throws RepositoryException {
        System.out.println("AdminService: Retrieving all students");
        return studentRepository.findAll();
    }

    public Optional<Student> findStudentById(String id) throws RepositoryException {
        System.out.println("AdminService: Searching for student with ID: " + id);
        return studentRepository.findById(id);
    }

    public Student updateStudent(Student student) throws RepositoryException {
        System.out.println("AdminService: Updating student - " + student.getFullName());
        return studentRepository.update(student);
    }

    public boolean removeStudent(String id) throws RepositoryException {
        System.out.println("AdminService: Removing student with ID: " + id);
        return studentRepository.deleteById(id);
    }

    // Course Management
    public Course addCourse(Course course) throws RepositoryException {
        System.out.println("AdminService: Adding new course - " + course.getCourseName());
        return courseRepository.save(course);
    }

    public List<Course> getAllCourses() throws RepositoryException {
        System.out.println("AdminService: Retrieving all courses");
        return courseRepository.findAll();
    }

    public Optional<Course> findCourseById(String id) throws RepositoryException {
        System.out.println("AdminService: Searching for course with ID: " + id);
        return courseRepository.findById(id);
    }

    public Course updateCourse(Course course) throws RepositoryException {
        System.out.println("AdminService: Updating course - " + course.getCourseName());
        return courseRepository.update(course);
    }

    public boolean removeCourse(String id) throws RepositoryException {
        System.out.println("AdminService: Removing course with ID: " + id);
        return courseRepository.deleteById(id);
    }

    // Enrollment Management
    public boolean enrollStudentInCourse(String studentId, String courseId) throws RepositoryException {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        Optional<Course> courseOpt = courseRepository.findById(courseId);

        if (studentOpt.isEmpty()) {
            System.out.println("AdminService: Student not found with ID: " + studentId);
            return false;
        }

        if (courseOpt.isEmpty()) {
            System.out.println("AdminService: Course not found with ID: " + courseId);
            return false;
        }

        Student student = studentOpt.get();
        Course course = courseOpt.get();

        // Enroll student in course
        student.enrollInCourse(courseId);
        course.enrollStudent(studentId);

        // Update both entities
        studentRepository.update(student);
        courseRepository.update(course);

        System.out.println("AdminService: Successfully enrolled " + student.getFullName() + 
                          " in " + course.getCourseName());
        return true;
    }

    // Authentication simulation
    public User authenticateUser(String email, String password) throws AuthenticationException {
        try {
            // Search in students
            List<Student> students = studentRepository.findAll();
            for (Student student : students) {
                if (email.equals(student.getEmail()) && password.equals(student.getPasswordForAuth())) {
                    if (!student.isActive()) {
                        throw new AuthenticationException("User account is inactive", "INACTIVE_ACCOUNT");
                    }
                    System.out.println("AdminService: Student authenticated successfully - " + student.getFullName());
                    return student;
                }
            }

            // If not found in students or teachers, throw exception
            throw new AuthenticationException("Invalid email or password", "INVALID_CREDENTIALS");

        } catch (RepositoryException e) {
            throw new AuthenticationException("Authentication failed due to system error: " + e.getMessage(), 
                                            "SYSTEM_ERROR", e);
        }
    }

    // System Statistics
    public void displaySystemStatistics() throws RepositoryException {
        long studentCount = studentRepository.count();
        long courseCount = courseRepository.count();

        System.out.println("\n=== System Statistics ===");
        System.out.println("Total Students: " + studentCount);
        System.out.println("Total Courses: " + courseCount);
        System.out.println("Data files: students.json, courses.json");
        System.out.println("========================\n");
    }
}