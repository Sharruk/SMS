package sms.managers;

import sms.models.Admin;
import sms.utils.FileHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages admin accounts — loads from admins.txt.
 * Admins are typically pre-configured, not added at runtime.
 */
public class AdminManager {

    private static final String FILE_PATH = "data/admins.txt";
    private List<Admin> admins;

    public AdminManager() {
        this.admins = new ArrayList<>();
        loadFromFile();
    }

    private void loadFromFile() {
        List<String> lines = FileHandler.readLines(FILE_PATH);
        admins.clear();
        for (String line : lines) {
            try {
                admins.add(Admin.fromFileString(line));
            } catch (Exception e) {
                System.err.println("Warning: Could not parse admin record: " + line);
            }
        }
        System.out.println("Loaded " + admins.size() + " admin(s) from admins.txt");
    }

    // Find admin by username and password (used for login)
    public Admin findByLogin(String username, String password) {
        for (Admin a : admins) {
            if (a.getUsername().equals(username) && a.getPassword().equals(password)) {
                return a;
            }
        }
        return null;
    }

    public List<Admin> getAllAdmins() { return new ArrayList<>(admins); }
    public int getCount()            { return admins.size(); }
}
