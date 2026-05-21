package sms.models;

/**
 * Represents a teacher user.
 *
 * Demonstrates:
 *   - INHERITANCE      : extends User
 *   - METHOD OVERRIDING: getRole() and displayMenu()
 */
public class Teacher extends User {

    private String department;

    // Constructor
    public Teacher(int id, String name, String email, String username,
                   String password, String department) {
        super(id, name, email, username, password);
        this.department = department;
    }

    // METHOD OVERRIDING
    @Override
    public String getRole() {
        return "TEACHER";
    }

    // METHOD OVERRIDING - Teacher sees a different menu than Student or Admin
    @Override
    public void displayMenu() {
        System.out.println("\n==================================================");
        System.out.println("              TEACHER MENU - " + getName());
        System.out.println("==================================================");
        System.out.println("1. View My Courses");
        System.out.println("2. Manage Grades");
        System.out.println("3. Manage Assignments");
        System.out.println("4. View All Students");
        System.out.println("0. Logout");
        System.out.println("==================================================");
        System.out.print("Choose an option: ");
    }

    // Getters and setters
    public String getDepartment()            { return department; }
    public void   setDepartment(String dept) { this.department = dept; }

    @Override
    public String toString() {
        return super.toString() + " | Dept: " + department;
    }

    // Format: id,name,email,username,password,department
    public String toFileString() {
        return getId() + "," + getName() + "," + getEmail() + "," +
               getUsername() + "," + getPassword() + "," + department;
    }

    public static Teacher fromFileString(String line) {
        String[] parts = line.split(",", -1);
        return new Teacher(
            Integer.parseInt(parts[0].trim()),
            parts[1].trim(), parts[2].trim(),
            parts[3].trim(), parts[4].trim(),
            parts[5].trim()
        );
    }
}
