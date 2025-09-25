package sms.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import sms.domain.Course;
import sms.exceptions.RepositoryException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CourseRepositoryImpl implements Repository<Course> {
    private static final String DATA_FILE = "courses.json";
    private final Map<String, Course> courses;
    private final ObjectMapper objectMapper;

    public CourseRepositoryImpl() {
        this.courses = new HashMap<>();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            loadAll();
        } catch (RepositoryException e) {
            System.out.println("Warning: Could not load existing course data: " + e.getMessage());
        }
    }

    @Override
    public Course save(Course course) throws RepositoryException {
        if (course == null || course.getCourseId() == null) {
            throw new RepositoryException("Course or Course ID cannot be null", "SAVE", "COURSE");
        }
        
        courses.put(course.getCourseId(), course);
        saveAll();
        return course;
    }

    @Override
    public Optional<Course> findById(String id) throws RepositoryException {
        if (id == null) {
            throw new RepositoryException("ID cannot be null", "FIND", "COURSE");
        }
        return Optional.ofNullable(courses.get(id));
    }

    @Override
    public List<Course> findAll() throws RepositoryException {
        return new ArrayList<>(courses.values());
    }

    @Override
    public Course update(Course course) throws RepositoryException {
        if (course == null || course.getCourseId() == null) {
            throw new RepositoryException("Course or Course ID cannot be null", "UPDATE", "COURSE");
        }
        
        if (!courses.containsKey(course.getCourseId())) {
            throw new RepositoryException("Course not found with ID: " + course.getCourseId(), "UPDATE", "COURSE");
        }
        
        courses.put(course.getCourseId(), course);
        saveAll();
        return course;
    }

    @Override
    public boolean deleteById(String id) throws RepositoryException {
        if (id == null) {
            throw new RepositoryException("ID cannot be null", "DELETE", "COURSE");
        }
        
        boolean removed = courses.remove(id) != null;
        if (removed) {
            saveAll();
        }
        return removed;
    }

    @Override
    public void loadAll() throws RepositoryException {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            System.out.println("Course data file does not exist. Starting with empty repository.");
            return;
        }

        try {
            List<Course> courseList = objectMapper.readValue(file, new TypeReference<List<Course>>() {});
            courses.clear();
            for (Course course : courseList) {
                courses.put(course.getCourseId(), course);
            }
            System.out.println("Loaded " + courses.size() + " courses from " + DATA_FILE);
        } catch (IOException e) {
            throw new RepositoryException("Failed to load courses from file: " + e.getMessage(), "LOAD", "COURSE", e);
        }
    }

    @Override
    public void saveAll() throws RepositoryException {
        try {
            List<Course> courseList = new ArrayList<>(courses.values());
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE), courseList);
        } catch (IOException e) {
            throw new RepositoryException("Failed to save courses to file: " + e.getMessage(), "SAVE", "COURSE", e);
        }
    }

    @Override
    public long count() {
        return courses.size();
    }

    // Additional methods for Course-specific operations
    public List<Course> findByTeacherId(String teacherId) throws RepositoryException {
        return courses.values().stream()
                .filter(course -> teacherId.equals(course.getTeacherId()))
                .toList();
    }

    public List<Course> findActiveCoursesWithAvailableSlots() throws RepositoryException {
        return courses.values().stream()
                .filter(course -> course.isActive() && course.getAvailableSlots() > 0)
                .toList();
    }

    public List<Course> findByCredits(int credits) throws RepositoryException {
        return courses.values().stream()
                .filter(course -> course.getCredits() == credits)
                .toList();
    }
}