package sms.domain;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.List;

@JsonTypeName("principal")
public class Principal extends User {
    private List<Admin> managedAdmins;
    private List<Teacher> appointedTeachers;
    private List<Course> managedCourses;

    // Default constructor for Jackson
    public Principal() {
        super();
        this.managedAdmins = new ArrayList<>();
        this.appointedTeachers = new ArrayList<>();
        this.managedCourses = new ArrayList<>();
    }

    public Principal(int userId, String name, String email, String username, String password) {
        super(userId, name, email, username, password);
        this.managedAdmins = new ArrayList<>();
        this.appointedTeachers = new ArrayList<>();
        this.managedCourses = new ArrayList<>();
    }

    @Override
    public String getRole() {
        return "PRINCIPAL";
    }

    // ===================== MENU SYSTEM =====================
    
    public void showMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("         PRINCIPAL MENU");
        System.out.println("=".repeat(50));
        System.out.println("1. Manage Admins");
        System.out.println("2. Manage Teachers");
        System.out.println("3. Manage Courses");
        System.out.println("4. View Students");
        System.out.println("5. View Reports & Statistics");
        System.out.println("0. Logout");
        System.out.println("=".repeat(50));
        System.out.print("Choose an option: ");
    }

    public void showAdminManagementMenu() {
        System.out.println("\n--- Admin Management ---");
        System.out.println("1. Add Admin");
        System.out.println("2. View All Admins");
        System.out.println("3. Remove Admin");
        System.out.println("0. Back to Main Menu");
        System.out.print("Choose an option: ");
    }

    public void showTeacherManagementMenu() {
        System.out.println("\n--- Teacher Management ---");
        System.out.println("1. Appoint Teacher");
        System.out.println("2. View All Teachers");
        System.out.println("3. Remove Teacher");
        System.out.println("0. Back to Main Menu");
        System.out.print("Choose an option: ");
    }

    public void showCourseManagementMenu() {
        System.out.println("\n--- Course Management ---");
        System.out.println("1. Create Course");
        System.out.println("2. Assign Teacher to Course");
        System.out.println("3. View All Courses");
        System.out.println("4. Remove Course");
        System.out.println("0. Back to Main Menu");
        System.out.print("Choose an option: ");
    }

    // ===================== ADMIN MANAGEMENT =====================

    public void addAdmin(Admin admin) {
        if (admin != null && !managedAdmins.contains(admin)) {
            managedAdmins.add(admin);
            System.out.println("✓ Principal: Added admin " + admin.getName() + " (ID: " + admin.getUserId() + ")");
        } else {
            System.out.println("✗ Principal: Admin already exists or is null");
        }
    }

    public void viewAdmins() {
        System.out.println("\n=== All Admins ===");
        if (managedAdmins.isEmpty()) {
            System.out.println("No admins found.");
        } else {
            for (int i = 0; i < managedAdmins.size(); i++) {
                Admin admin = managedAdmins.get(i);
                System.out.println((i + 1) + ". " + admin.getName() + " (ID: " + admin.getUserId() + 
                                 ", Email: " + admin.getEmail() + ")");
            }
        }
        System.out.println("Total Admins: " + managedAdmins.size());
        System.out.println("==================\n");
    }

    public boolean removeAdmin(int userId) {
        Admin toRemove = null;
        for (Admin admin : managedAdmins) {
            if (admin.getUserId() == userId) {
                toRemove = admin;
                break;
            }
        }
        
        if (toRemove != null) {
            managedAdmins.remove(toRemove);
            System.out.println("✓ Principal: Removed admin " + toRemove.getName() + " (ID: " + userId + ")");
            return true;
        } else {
            System.out.println("✗ Principal: Admin with ID " + userId + " not found");
            return false;
        }
    }

    // ===================== TEACHER MANAGEMENT =====================

    public void appointTeacher(Teacher teacher) {
        if (teacher != null && !appointedTeachers.contains(teacher)) {
            appointedTeachers.add(teacher);
            System.out.println("✓ Principal: Appointed teacher " + teacher.getName() + " (ID: " + teacher.getUserId() + ")");
        } else {
            System.out.println("✗ Principal: Teacher already appointed or is null");
        }
    }

    public void viewTeachers() {
        System.out.println("\n=== All Teachers ===");
        if (appointedTeachers.isEmpty()) {
            System.out.println("No teachers found.");
        } else {
            for (int i = 0; i < appointedTeachers.size(); i++) {
                Teacher teacher = appointedTeachers.get(i);
                System.out.println((i + 1) + ". " + teacher.getName() + " (ID: " + teacher.getUserId() + 
                                 ", Email: " + teacher.getEmail() + ", Courses: " + teacher.getCourses().size() + ")");
            }
        }
        System.out.println("Total Teachers: " + appointedTeachers.size());
        System.out.println("====================\n");
    }

    public boolean removeTeacher(int userId) {
        Teacher toRemove = null;
        for (Teacher teacher : appointedTeachers) {
            if (teacher.getUserId() == userId) {
                toRemove = teacher;
                break;
            }
        }
        
        if (toRemove != null) {
            appointedTeachers.remove(toRemove);
            System.out.println("✓ Principal: Removed teacher " + toRemove.getName() + " (ID: " + userId + ")");
            return true;
        } else {
            System.out.println("✗ Principal: Teacher with ID " + userId + " not found");
            return false;
        }
    }

    // ===================== COURSE MANAGEMENT =====================

    public void createCourse(Course course) {
        if (course != null && !managedCourses.contains(course)) {
            managedCourses.add(course);
            System.out.println("✓ Principal: Created course " + course.getCourseName() + " (ID: " + course.getCourseId() + ")");
        } else {
            System.out.println("✗ Principal: Course already exists or is null");
        }
    }

    public void assignCourse(Teacher teacher, Course course) {
        if (teacher == null || course == null) {
            System.out.println("✗ Principal: Cannot assign course - teacher or course is null");
            return;
        }

        if (!appointedTeachers.contains(teacher)) {
            System.out.println("✗ Principal: Teacher must be appointed before course assignment");
            return;
        }

        teacher.addCourse(course);
        course.setFacultyName(teacher.getName());
        System.out.println("✓ Principal: Assigned course " + course.getCourseId() + 
                          " to teacher " + teacher.getName());
    }

    public void viewCourses() {
        System.out.println("\n=== All Courses ===");
        if (managedCourses.isEmpty()) {
            System.out.println("No courses found.");
        } else {
            for (int i = 0; i < managedCourses.size(); i++) {
                Course course = managedCourses.get(i);
                System.out.println((i + 1) + ". " + course.getCourseName() + 
                                 " (ID: " + course.getCourseId() + 
                                 ", Credits: " + course.getCreditHours() +
                                 ", Faculty: " + (course.getFacultyName() != null ? course.getFacultyName() : "Unassigned") + ")");
            }
        }
        System.out.println("Total Courses: " + managedCourses.size());
        System.out.println("===================\n");
    }

    public boolean removeCourse(String courseId) {
        Course toRemove = null;
        for (Course course : managedCourses) {
            if (course.getCourseId().equals(courseId)) {
                toRemove = course;
                break;
            }
        }
        
        if (toRemove != null) {
            managedCourses.remove(toRemove);
            System.out.println("✓ Principal: Removed course " + toRemove.getCourseName() + " (ID: " + courseId + ")");
            return true;
        } else {
            System.out.println("✗ Principal: Course with ID " + courseId + " not found");
            return false;
        }
    }

    // ===================== REPORTS & STATISTICS =====================

    public void viewReports() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("         PRINCIPAL'S SYSTEM REPORTS & STATISTICS");
        System.out.println("=".repeat(60));
        System.out.println("Total Admins Managed: " + managedAdmins.size());
        System.out.println("Total Teachers Appointed: " + appointedTeachers.size());
        System.out.println("Total Courses Created: " + managedCourses.size());
        
        System.out.println("\nAdmins:");
        if (managedAdmins.isEmpty()) {
            System.out.println("  No admins found.");
        } else {
            for (Admin admin : managedAdmins) {
                System.out.println("  - " + admin.getName() + " (ID: " + admin.getUserId() + ")");
            }
        }
        
        System.out.println("\nTeachers:");
        if (appointedTeachers.isEmpty()) {
            System.out.println("  No teachers found.");
        } else {
            for (Teacher teacher : appointedTeachers) {
                System.out.println("  - " + teacher.getName() + " (ID: " + teacher.getUserId() + 
                                 ", Courses: " + teacher.getCourses().size() + ")");
            }
        }

        System.out.println("\nCourses:");
        if (managedCourses.isEmpty()) {
            System.out.println("  No courses found.");
        } else {
            for (Course course : managedCourses) {
                System.out.println("  - " + course.getCourseName() + " (" + course.getCourseId() + 
                                 ") - Faculty: " + (course.getFacultyName() != null ? course.getFacultyName() : "Unassigned"));
            }
        }
        System.out.println("=".repeat(60) + "\n");
    }

    // ===================== GETTERS AND SETTERS =====================

    public List<Admin> getManagedAdmins() {
        return managedAdmins;
    }

    public void setManagedAdmins(List<Admin> managedAdmins) {
        this.managedAdmins = managedAdmins;
    }

    public List<Teacher> getAppointedTeachers() {
        return appointedTeachers;
    }

    public void setAppointedTeachers(List<Teacher> appointedTeachers) {
        this.appointedTeachers = appointedTeachers;
    }

    public List<Course> getManagedCourses() {
        return managedCourses;
    }

    public void setManagedCourses(List<Course> managedCourses) {
        this.managedCourses = managedCourses;
    }

    @Override
    public String toString() {
        return "Principal{" +
                "userId=" + getUserId() +
                ", name='" + getName() + '\'' +
                ", admins=" + managedAdmins.size() +
                ", teachers=" + appointedTeachers.size() +
                ", courses=" + managedCourses.size() +
                '}';
    }
}