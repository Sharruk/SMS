package sms.managers;

import sms.exceptions.RecordNotFoundException;
import sms.models.Student;
import sms.utils.FileHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages all student data — loads from students.txt, saves back to it.
 *
 * Demonstrates:
 *   - FILE HANDLING  : reads/writes via FileHandler (BufferedReader/Writer)
 *   - COLLECTIONS    : uses ArrayList, Stream API for search and sort
 *   - EXCEPTION HANDLING: throws RecordNotFoundException when a student is missing
 */
public class StudentManager {

    private static final String FILE_PATH = "data/students.txt";
    private List<Student> students;

    public StudentManager() {
        this.students = new ArrayList<>();
        loadFromFile();
    }

    // Load all students from students.txt into memory
    private void loadFromFile() {
        List<String> lines = FileHandler.readLines(FILE_PATH);
        students.clear();
        for (String line : lines) {
            try {
                students.add(Student.fromFileString(line));
            } catch (Exception e) {
                System.err.println("Warning: Could not parse student record: " + line);
            }
        }
        System.out.println("Loaded " + students.size() + " student(s) from students.txt");
    }

    // Save all students back to students.txt (called after every change)
    private void saveToFile() {
        List<String> lines = new ArrayList<>();
        for (Student s : students) {
            lines.add(s.toFileString());
        }
        FileHandler.writeLines(FILE_PATH, lines);
    }

    // ADD a new student and persist
    public void addStudent(Student student) {
        students.add(student);
        saveToFile();
        System.out.println("Student added successfully: " + student.getName());
    }

    // UPDATE an existing student by ID
    public void updateStudent(Student updated) throws RecordNotFoundException {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId() == updated.getId()) {
                students.set(i, updated);
                saveToFile();
                System.out.println("Student updated: " + updated.getName());
                return;
            }
        }
        throw new RecordNotFoundException("Student with ID " + updated.getId() + " not found.");
    }

    // DELETE a student by ID
    public void deleteStudent(int id) throws RecordNotFoundException {
        Student toDelete = findById(id);
        students.remove(toDelete);
        saveToFile();
        System.out.println("Student deleted: " + toDelete.getName());
    }

    // FIND by numeric ID
    public Student findById(int id) throws RecordNotFoundException {
        for (Student s : students) {
            if (s.getId() == id) return s;
        }
        throw new RecordNotFoundException("Student with ID " + id + " not found.");
    }

    // FIND by username + password (used for login)
    public Student findByLogin(String username, String password) {
        for (Student s : students) {
            if (s.getUsername().equals(username) && s.getPassword().equals(password)) {
                return s;
            }
        }
        return null; // returns null if no match
    }

    // SEARCH by name (case-insensitive, partial match)
    public List<Student> searchByName(String keyword) {
        return students.stream()
            .filter(s -> s.getName().toLowerCase().contains(keyword.toLowerCase()))
            .collect(Collectors.toList());
    }

    // SORT by name alphabetically
    public List<Student> sortByName() {
        List<Student> sorted = new ArrayList<>(students);
        sorted.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        return sorted;
    }

    // SORT by ID numerically
    public List<Student> sortById() {
        List<Student> sorted = new ArrayList<>(students);
        sorted.sort((a, b) -> Integer.compare(a.getId(), b.getId()));
        return sorted;
    }

    public List<Student> getAllStudents() { return new ArrayList<>(students); }
    public int getCount()                { return students.size(); }

    // Generate next available ID (max existing ID + 1)
    public int getNextId() {
        int maxId = 0;
        for (Student s : students) {
            if (s.getId() > maxId) maxId = s.getId();
        }
        return maxId + 1;
    }
}
