package sms.models;

/**
 * Represents an admin user with full management capabilities.
 *
 * Demonstrates:
 *   - INHERITANCE      : extends User
 *   - METHOD OVERRIDING: getRole() and displayMenu()
 */
public class Admin extends User {

    // Constructor
    public Admin(int id, String name, String email, String username, String password) {
        super(id, name, email, username, password);
    }

    // METHOD OVERRIDING
    @Override
    public String getRole() {
        return "ADMIN";
    }

    // METHOD OVERRIDING - Admin sees the most powerful menu
    @Override
    public void displayMenu() {
        System.out.println("\n==================================================");
        System.out.println("              ADMIN MENU - " + getName());
        System.out.println("==================================================");
        System.out.println("1. Manage Students");
        System.out.println("2. Manage Teachers");
        System.out.println("3. Manage Courses");
        System.out.println("4. Manage Grades");
        System.out.println("5. Manage Assignments");
        System.out.println("6. View Reports");
        System.out.println("0. Logout");
        System.out.println("==================================================");
        System.out.print("Choose an option: ");
    }

    // Format: id,name,email,username,password
    public String toFileString() {
        return getId() + "," + getName() + "," + getEmail() + "," +
               getUsername() + "," + getPassword();
    }

    public static Admin fromFileString(String line) {
        String[] parts = line.split(",", -1);
        return new Admin(
            Integer.parseInt(parts[0].trim()),
            parts[1].trim(), parts[2].trim(),
            parts[3].trim(), parts[4].trim()
        );
    }
}
