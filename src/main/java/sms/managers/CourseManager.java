package sms.managers;

import sms.exceptions.RecordNotFoundException;
import sms.models.Course;
import sms.utils.FileHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages all course data — loads from courses.txt, saves back to it.
 *
 * Demonstrates METHOD OVERLOADING:
 *   assignTeacher(String courseId, String teacherName) — assigns by name
 */
public class CourseManager {

    private static final String FILE_PATH = "data/courses.txt";
    private List<Course> courses;

    public CourseManager() {
        this.courses = new ArrayList<>();
        loadFromFile();
    }

    private void loadFromFile() {
        List<String> lines = FileHandler.readLines(FILE_PATH);
        courses.clear();
        for (String line : lines) {
            try {
                courses.add(Course.fromFileString(line));
            } catch (Exception e) {
                System.err.println("Warning: Could not parse course record: " + line);
            }
        }
        System.out.println("Loaded " + courses.size() + " course(s) from courses.txt");
    }

    private void saveToFile() {
        List<String> lines = new ArrayList<>();
        for (Course c : courses) {
            lines.add(c.toFileString());
        }
        FileHandler.writeLines(FILE_PATH, lines);
    }

    public void addCourse(Course course) {
        courses.add(course);
        saveToFile();
        System.out.println("Course added: " + course.getCourseName());
    }

    public void deleteCourse(String courseId) throws RecordNotFoundException {
        Course toDelete = findById(courseId);
        courses.remove(toDelete);
        saveToFile();
        System.out.println("Course deleted: " + toDelete.getCourseName());
    }

    public Course findById(String courseId) throws RecordNotFoundException {
        for (Course c : courses) {
            if (c.getCourseId().equalsIgnoreCase(courseId)) return c;
        }
        throw new RecordNotFoundException("Course with ID '" + courseId + "' not found.");
    }

    // Assign a teacher to a course by updating the teacherName field
    public void assignTeacher(String courseId, String teacherName) throws RecordNotFoundException {
        Course course = findById(courseId);
        course.setTeacherName(teacherName);
        saveToFile();
        System.out.println("Assigned " + teacherName + " to course " + course.getCourseName());
    }

    // Get all courses taught by a specific teacher
    public List<Course> getCoursesByTeacher(String teacherName) {
        return courses.stream()
            .filter(c -> c.getTeacherName().equalsIgnoreCase(teacherName))
            .collect(Collectors.toList());
    }

    // Sort courses by name alphabetically
    public List<Course> sortByName() {
        List<Course> sorted = new ArrayList<>(courses);
        sorted.sort((a, b) -> a.getCourseName().compareToIgnoreCase(b.getCourseName()));
        return sorted;
    }

    public List<Course> getAllCourses() { return new ArrayList<>(courses); }
    public int getCount()              { return courses.size(); }
}
