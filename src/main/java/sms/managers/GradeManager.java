package sms.managers;

import sms.models.Grade;
import sms.utils.FileHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages all grade data — loads from grades.txt, saves back to it.
 */
public class GradeManager {

    private static final String FILE_PATH = "data/grades.txt";
    private List<Grade> grades;

    public GradeManager() {
        this.grades = new ArrayList<>();
        loadFromFile();
    }

    private void loadFromFile() {
        List<String> lines = FileHandler.readLines(FILE_PATH);
        grades.clear();
        for (String line : lines) {
            try {
                grades.add(Grade.fromFileString(line));
            } catch (Exception e) {
                System.err.println("Warning: Could not parse grade record: " + line);
            }
        }
        System.out.println("Loaded " + grades.size() + " grade(s) from grades.txt");
    }

    private void saveToFile() {
        List<String> lines = new ArrayList<>();
        for (Grade g : grades) {
            lines.add(g.toFileString());
        }
        FileHandler.writeLines(FILE_PATH, lines);
    }

    public void addGrade(Grade grade) {
        // If grade already exists for this student+course, update it
        for (int i = 0; i < grades.size(); i++) {
            Grade g = grades.get(i);
            if (g.getStudentId() == grade.getStudentId() &&
                g.getCourseId().equalsIgnoreCase(grade.getCourseId())) {
                grades.set(i, grade); // update existing
                saveToFile();
                System.out.println("Grade updated for " + grade.getStudentName());
                return;
            }
        }
        grades.add(grade); // add new
        saveToFile();
        System.out.println("Grade assigned to " + grade.getStudentName());
    }

    // Get all grades for a specific student
    public List<Grade> getGradesByStudent(int studentId) {
        return grades.stream()
            .filter(g -> g.getStudentId() == studentId)
            .collect(Collectors.toList());
    }

    // Get all grades for a specific course
    public List<Grade> getGradesByCourse(String courseId) {
        return grades.stream()
            .filter(g -> g.getCourseId().equalsIgnoreCase(courseId))
            .collect(Collectors.toList());
    }

    public List<Grade> getAllGrades() { return new ArrayList<>(grades); }
    public int getCount()            { return grades.size(); }
}
