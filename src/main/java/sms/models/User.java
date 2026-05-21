package sms.models;

/**
 * Abstract base class for all users in the system.
 *
 * Demonstrates:
 *   - ABSTRACTION  : abstract class with abstract methods
 *   - ENCAPSULATION: private fields with public getters/setters
 *   - INHERITANCE  : Student, Teacher, Admin all extend this class
 */
public abstract class User {

    // ENCAPSULATION - all fields are private
    private int id;
    private String name;
    private String email;
    private String username;
    private String password;

    // Constructor used by every subclass via super()
    public User(int id, String name, String email, String username, String password) {
        this.id       = id;
        this.name     = name;
        this.email    = email;
        this.username = username;
        this.password = password;
    }

    // ABSTRACTION - subclasses MUST implement these methods
    public abstract String getRole();
    public abstract void displayMenu();

    // Common behaviour shared by all users (INHERITANCE benefit)
    public void login() {
        System.out.println("\nWelcome, " + name + "! Logged in as " + getRole() + ".");
    }

    public void logout() {
        System.out.println("\nGoodbye, " + name + ". Logged out successfully.");
    }

    // ENCAPSULATION - controlled access through getters and setters
    public int    getId()       { return id; }
    public void   setId(int id) { this.id = id; }

    public String getName()           { return name; }
    public void   setName(String n)   { this.name = n; }

    public String getEmail()          { return email; }
    public void   setEmail(String e)  { this.email = e; }

    public String getUsername()             { return username; }
    public void   setUsername(String u)     { this.username = u; }

    public String getPassword()             { return password; }
    public void   setPassword(String p)     { this.password = p; }

    @Override
    public String toString() {
        return "ID: " + id + " | Name: " + name + " | Email: " + email + " | Role: " + getRole();
    }
}
