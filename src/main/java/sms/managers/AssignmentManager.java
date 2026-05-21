package sms.managers;

import sms.models.Assignment;
import sms.utils.FileHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages all assignment data — loads from assignments.txt, saves back to it.
 */
public class AssignmentManager {

    private static final String FILE_PATH = "data/assignments.txt";
    private List<Assignment> assignments;

    public AssignmentManager() {
        this.assignments = new ArrayList<>();
        loadFromFile();
    }

    private void loadFromFile() {
        List<String> lines = FileHandler.readLines(FILE_PATH);
        assignments.clear();
        for (String line : lines) {
            try {
                assignments.add(Assignment.fromFileString(line));
            } catch (Exception e) {
                System.err.println("Warning: Could not parse assignment record: " + line);
            }
        }
        System.out.println("Loaded " + assignments.size() + " assignment(s) from assignments.txt");
    }

    private void saveToFile() {
        List<String> lines = new ArrayList<>();
        for (Assignment a : assignments) {
            lines.add(a.toFileString());
        }
        FileHandler.writeLines(FILE_PATH, lines);
    }

    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
        saveToFile();
        System.out.println("Assignment created: " + assignment.getTitle());
    }

    public void deleteAssignment(int id) {
        assignments.removeIf(a -> a.getId() == id);
        saveToFile();
        System.out.println("Assignment #" + id + " deleted.");
    }

    // Get all assignments for a specific course
    public List<Assignment> getAssignmentsByCourse(String courseId) {
        return assignments.stream()
            .filter(a -> a.getCourseId().equalsIgnoreCase(courseId))
            .collect(Collectors.toList());
    }

    public List<Assignment> getAllAssignments() { return new ArrayList<>(assignments); }
    public int getCount()                       { return assignments.size(); }

    public int getNextId() {
        int maxId = 0;
        for (Assignment a : assignments) {
            if (a.getId() > maxId) maxId = a.getId();
        }
        return maxId + 1;
    }
}
