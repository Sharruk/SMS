package sms.domain;

import com.fasterxml.jackson.annotation.JsonTypeName;
import sms.exceptions.*;
import sms.search.Searchable;
import sms.sort.Sortable;
import sms.validation.InputValidator;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@JsonTypeName("admin")
public class Admin extends User implements Searchable<Object>, Sortable<Object> {
    private List<Student> managedStudents;
    private List<Teacher> managedTeachers;
    private List<Course> managedCourses;
    private List<Batch> managedBatches;

    // Default constructor for Jackson
    public Admin() {
        super();
        this.managedStudents = new ArrayList<>();
        this.managedTeachers = new ArrayList<>();
        this.managedCourses = new ArrayList<>();
        this.managedBatches = new ArrayList<>();
    }

    public Admin(int userId, String name, String email, String username, String password) throws ValidationException {
        super(userId, name, email, username, password);
        this.managedStudents = new ArrayList<>();
        this.managedTeachers = new ArrayList<>();
        this.managedCourses = new ArrayList<>();
        this.managedBatches = new ArrayList<>();
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }

    // Course Management
    public void manageCourse() {
        System.out.println("Admin " + getName() + " managing courses...");
        System.out.println("Total courses managed: " + managedCourses.size());
    }

    // Student Management
    public void manageStudent() {
        System.out.println("Admin " + getName() + " managing students...");
        System.out.println("Total students managed: " + managedStudents.size());
    }

    public Student registerStudent(String name, String email) throws ValidationException {
        InputValidator.validateName(name);
        InputValidator.validateEmail(email);

        int newId = managedStudents.size() + 1000;
        String username = email.split("@")[0];
        String defaultPassword = "Default123";
        
        Student student = new Student(newId, name, email, username, defaultPassword);
        managedStudents.add(student);
        
        System.out.println("✓ Admin: Registered new student - " + name + " (ID: " + newId + ")");
        return student;
    }

    public void assignBatch(Student student, Batch batch) throws NotFoundException {
        if (student == null) {
            throw new NotFoundException("Student not found", "Student", "null");
        }
        if (batch == null) {
            throw new NotFoundException("Batch not found", "Batch", "null");
        }

        student.setBatch(batch);
        System.out.println("Admin: Assigned batch " + batch.getYearRange() + " to student " + student.getName());
    }

    public void assignCourse(Student student, Course course) throws NotFoundException {
        if (student == null) {
            throw new NotFoundException("Student not found", "Student", "null");
        }
        if (course == null) {
            throw new NotFoundException("Course not found", "Course", "null");
        }

        student.addCourse(course);
        System.out.println("Admin: Assigned course " + course.getCourseId() + " to student " + student.getName());
    }

    public void assignCourse(Teacher teacher, Course course) throws NotFoundException {
        if (teacher == null) {
            throw new NotFoundException("Teacher not found", "Teacher", "null");
        }
        if (course == null) {
            throw new NotFoundException("Course not found", "Course", "null");
        }

        teacher.addCourse(course);
        course.setFacultyName(teacher.getName());
        System.out.println("Admin: Assigned course " + course.getCourseId() + " to teacher " + teacher.getName());
    }

    public void setDefaultPassword(User user) throws ValidationException {
        if (user == null) {
            throw new ValidationException("User cannot be null", "user", "null");
        }
        
        String defaultPassword = "Password123!";
        user.setPassword(defaultPassword);
        System.out.println("Admin: Set default password for user " + user.getName());
    }

    public void verifyAdmission(Student student) throws ValidationException {
        if (student == null) {
            throw new ValidationException("Student cannot be null", "student", "null");
        }
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            throw new ValidationException("Student name is required for admission", "name", student.getName());
        }
        if (student.getEmail() == null || !student.getEmail().contains("@")) {
            throw new ValidationException("Valid email is required for admission", "email", student.getEmail());
        }

        System.out.println("Admin: Verified admission for student " + student.getName());
    }

    public void createCredentials(User user, String email, String password) throws ValidationException {
        if (user == null) {
            throw new ValidationException("User cannot be null", "user", "null");
        }
        if (email == null || !email.contains("@")) {
            throw new ValidationException("Invalid email format", "email", email);
        }
        if (password == null || password.length() < 6) {
            throw new ValidationException("Password must be at least 6 characters", "password", "too_short");
        }

        user.setEmail(email);
        user.setPassword(password);
        user.setUsername(email.split("@")[0]);
        
        System.out.println("Admin: Created credentials for user " + user.getName());
    }

    public void addCourseToStudent(Student student, Course course) throws NotFoundException {
        assignCourse(student, course);
    }

    public void addCourseToFaculty(Teacher teacher, Course course) throws NotFoundException {
        assignCourse(teacher, course);
    }

    // Search implementation - Admin can search all types
    @Override
    public List<Object> search(String criteria) {
        List<Object> results = new ArrayList<>();
        
        // Search students
        List<Student> studentResults = managedStudents.stream()
            .filter(s -> s.getName().toLowerCase().contains(criteria.toLowerCase()) ||
                        s.getEmail().toLowerCase().contains(criteria.toLowerCase()) ||
                        String.valueOf(s.getUserId()).contains(criteria))
            .collect(Collectors.toList());
        results.addAll(studentResults);
        
        // Search teachers
        List<Teacher> teacherResults = managedTeachers.stream()
            .filter(t -> t.getName().toLowerCase().contains(criteria.toLowerCase()) ||
                        t.getEmail().toLowerCase().contains(criteria.toLowerCase()) ||
                        String.valueOf(t.getUserId()).contains(criteria))
            .collect(Collectors.toList());
        results.addAll(teacherResults);
        
        // Search courses
        List<Course> courseResults = managedCourses.stream()
            .filter(c -> c.getCourseName().toLowerCase().contains(criteria.toLowerCase()) ||
                        c.getCourseId().toLowerCase().contains(criteria.toLowerCase()))
            .collect(Collectors.toList());
        results.addAll(courseResults);
        
        return results;
    }

    // Sort implementation - Admin can sort all types
    @Override
    public List<Object> sort(String criteria) {
        List<Object> allItems = new ArrayList<>();
        allItems.addAll(managedStudents);
        allItems.addAll(managedTeachers);
        allItems.addAll(managedCourses);
        
        // Simple sorting by name for demonstration
        if ("name".equalsIgnoreCase(criteria)) {
            allItems.sort((a, b) -> {
                String nameA = getItemName(a);
                String nameB = getItemName(b);
                return nameA.compareTo(nameB);
            });
        }
        
        return allItems;
    }

    private String getItemName(Object item) {
        if (item instanceof User) {
            return ((User) item).getName();
        } else if (item instanceof Course) {
            return ((Course) item).getCourseName();
        }
        return item.toString();
    }

    // Getters and Setters
    public List<Student> getManagedStudents() {
        return managedStudents;
    }

    public void setManagedStudents(List<Student> managedStudents) {
        this.managedStudents = managedStudents;
    }

    public List<Teacher> getManagedTeachers() {
        return managedTeachers;
    }

    public void setManagedTeachers(List<Teacher> managedTeachers) {
        this.managedTeachers = managedTeachers;
    }

    public List<Course> getManagedCourses() {
        return managedCourses;
    }

    public void setManagedCourses(List<Course> managedCourses) {
        this.managedCourses = managedCourses;
    }

    public List<Batch> getManagedBatches() {
        return managedBatches;
    }

    public void setManagedBatches(List<Batch> managedBatches) {
        this.managedBatches = managedBatches;
    }

    // ===================== MENU SYSTEM =====================
    
    public void showMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("         ADMIN MENU");
        System.out.println("=".repeat(50));
        System.out.println("1. Student Management");
        System.out.println("2. Teacher Management");
        System.out.println("3. Course Management");
        System.out.println("4. Reports & Statistics");
        System.out.println("5. View Messages");
        System.out.println("0. Logout");
        System.out.println("=".repeat(50));
        System.out.print("Choose an option: ");
    }

    public void showStudentManagementMenu() {
        System.out.println("\n--- Student Management ---");
        System.out.println("1. Register New Student");
        System.out.println("2. Update Student Details");
        System.out.println("3. Remove/Deactivate Student");
        System.out.println("4. Search Students");
        System.out.println("5. Enroll Student in Course");
        System.out.println("6. View All Students");
        System.out.println("0. Back to Main Menu");
        System.out.print("Choose an option: ");
    }

    public void showTeacherManagementMenu() {
        System.out.println("\n--- Teacher Management ---");
        System.out.println("1. Register New Teacher");
        System.out.println("2. Update Teacher Details");
        System.out.println("3. Remove/Deactivate Teacher");
        System.out.println("4. Search Teachers");
        System.out.println("5. Assign Teacher to Course");
        System.out.println("6. View All Teachers");
        System.out.println("0. Back to Main Menu");
        System.out.print("Choose an option: ");
    }

    public void showCourseManagementMenu() {
        System.out.println("\n--- Course Management ---");
        System.out.println("1. Create New Course");
        System.out.println("2. Update Course Details");
        System.out.println("3. Remove/Archive Course");
        System.out.println("4. Assign Course to Teacher");
        System.out.println("5. Enroll Students in Course");
        System.out.println("6. View All Courses");
        System.out.println("0. Back to Main Menu");
        System.out.print("Choose an option: ");
    }

    @Override
    public String toString() {
        return "Admin{" +
                "userId=" + getUserId() +
                ", name='" + getName() + '\'' +
                ", managedStudents=" + managedStudents.size() +
                ", managedTeachers=" + managedTeachers.size() +
                ", managedCourses=" + managedCourses.size() +
                '}';
    }
}