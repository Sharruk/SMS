package sms.domain;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.List;

@JsonTypeName("principal")
public class Principal extends User {
    private List<Admin> managedAdmins;
    private List<Teacher> appointedTeachers;

    // Default constructor for Jackson
    public Principal() {
        super();
        this.managedAdmins = new ArrayList<>();
        this.appointedTeachers = new ArrayList<>();
    }

    public Principal(int userId, String name, String email, String username, String password) {
        super(userId, name, email, username, password);
        this.managedAdmins = new ArrayList<>();
        this.appointedTeachers = new ArrayList<>();
    }

    @Override
    public String getRole() {
        return "PRINCIPAL";
    }

    public void addAdmin(Admin admin) {
        if (admin != null && !managedAdmins.contains(admin)) {
            managedAdmins.add(admin);
            System.out.println("Principal: Added admin " + admin.getName() + " (ID: " + admin.getUserId() + ")");
        } else {
            System.out.println("Principal: Admin already exists or is null");
        }
    }

    public void appointTeacher(Teacher teacher) {
        if (teacher != null && !appointedTeachers.contains(teacher)) {
            appointedTeachers.add(teacher);
            System.out.println("Principal: Appointed teacher " + teacher.getName() + " (ID: " + teacher.getUserId() + ")");
        } else {
            System.out.println("Principal: Teacher already appointed or is null");
        }
    }

    public void assignCourse(Teacher teacher, Course course) {
        if (teacher == null || course == null) {
            System.out.println("Principal: Cannot assign course - teacher or course is null");
            return;
        }

        if (!appointedTeachers.contains(teacher)) {
            System.out.println("Principal: Teacher must be appointed before course assignment");
            return;
        }

        teacher.addCourse(course);
        course.setFacultyName(teacher.getName());
        System.out.println("Principal: Assigned course " + course.getCourseId() + 
                          " to teacher " + teacher.getName());
    }

    public void viewReports() {
        System.out.println("\n=== Principal's System Reports ===");
        System.out.println("Total Admins Managed: " + managedAdmins.size());
        System.out.println("Total Teachers Appointed: " + appointedTeachers.size());
        
        System.out.println("\nAdmins:");
        for (Admin admin : managedAdmins) {
            System.out.println("  - " + admin.getName() + " (ID: " + admin.getUserId() + ")");
        }
        
        System.out.println("\nTeachers:");
        for (Teacher teacher : appointedTeachers) {
            System.out.println("  - " + teacher.getName() + " (ID: " + teacher.getUserId() + 
                             ", Courses: " + teacher.getCourses().size() + ")");
        }
        System.out.println("================================\n");
    }

    // Getters and Setters
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

    @Override
    public String toString() {
        return "Principal{" +
                "userId=" + getUserId() +
                ", name='" + getName() + '\'' +
                ", admins=" + managedAdmins.size() +
                ", teachers=" + appointedTeachers.size() +
                '}';
    }
}