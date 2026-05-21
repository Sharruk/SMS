package sms.managers;

import sms.exceptions.RecordNotFoundException;
import sms.models.Teacher;
import sms.utils.FileHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages all teacher data — loads from teachers.txt, saves back to it.
 */
public class TeacherManager {

    private static final String FILE_PATH = "data/teachers.txt";
    private List<Teacher> teachers;

    public TeacherManager() {
        this.teachers = new ArrayList<>();
        loadFromFile();
    }

    private void loadFromFile() {
        List<String> lines = FileHandler.readLines(FILE_PATH);
        teachers.clear();
        for (String line : lines) {
            try {
                teachers.add(Teacher.fromFileString(line));
            } catch (Exception e) {
                System.err.println("Warning: Could not parse teacher record: " + line);
            }
        }
        System.out.println("Loaded " + teachers.size() + " teacher(s) from teachers.txt");
    }

    private void saveToFile() {
        List<String> lines = new ArrayList<>();
        for (Teacher t : teachers) {
            lines.add(t.toFileString());
        }
        FileHandler.writeLines(FILE_PATH, lines);
    }

    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
        saveToFile();
        System.out.println("Teacher added: " + teacher.getName());
    }

    public void updateTeacher(Teacher updated) throws RecordNotFoundException {
        for (int i = 0; i < teachers.size(); i++) {
            if (teachers.get(i).getId() == updated.getId()) {
                teachers.set(i, updated);
                saveToFile();
                System.out.println("Teacher updated: " + updated.getName());
                return;
            }
        }
        throw new RecordNotFoundException("Teacher with ID " + updated.getId() + " not found.");
    }

    public void deleteTeacher(int id) throws RecordNotFoundException {
        Teacher toDelete = findById(id);
        teachers.remove(toDelete);
        saveToFile();
        System.out.println("Teacher deleted: " + toDelete.getName());
    }

    public Teacher findById(int id) throws RecordNotFoundException {
        for (Teacher t : teachers) {
            if (t.getId() == id) return t;
        }
        throw new RecordNotFoundException("Teacher with ID " + id + " not found.");
    }

    // Find by username + password (used for login)
    public Teacher findByLogin(String username, String password) {
        for (Teacher t : teachers) {
            if (t.getUsername().equals(username) && t.getPassword().equals(password)) {
                return t;
            }
        }
        return null;
    }

    public List<Teacher> searchByName(String keyword) {
        return teachers.stream()
            .filter(t -> t.getName().toLowerCase().contains(keyword.toLowerCase()))
            .collect(Collectors.toList());
    }

    public List<Teacher> getAllTeachers() { return new ArrayList<>(teachers); }
    public int getCount()                { return teachers.size(); }

    public int getNextId() {
        int maxId = 0;
        for (Teacher t : teachers) {
            if (t.getId() > maxId) maxId = t.getId();
        }
        return maxId + 1;
    }
}
