package sms.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import sms.domain.Course;
import sms.exceptions.RepositoryException;
import sms.exceptions.ValidationException;
import sms.exceptions.NotFoundException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CourseRepository implements Repository<Course> {
    private static final String DATA_FILE = "courses.json";
    private final List<Course> courses;
    private final ObjectMapper objectMapper;

    public CourseRepository() {
        this.courses = new ArrayList<>();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            loadAll();
        } catch (RepositoryException e) {
            System.out.println("Warning: Could not load existing course data: " + e.getMessage());
        }
    }

    @Override
    public void add(Course course) throws RepositoryException, ValidationException {
        if (course == null) {
            throw new ValidationException("Course cannot be null", "course", "null");
        }
        if (course.getCourseId() == null || course.getCourseId().trim().isEmpty()) {
            throw new ValidationException("Course ID cannot be empty", "courseId", course.getCourseId());
        }
        
        courses.add(course);
        saveAll();
        System.out.println("CourseRepository: Added course " + course.getCourseName());
    }

    @Override
    public void update(Course course) throws RepositoryException, NotFoundException {
        if (course == null) {
            throw new NotFoundException("Course not found", "Course", "null");
        }
        
        boolean found = false;
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getCourseId().equals(course.getCourseId())) {
                courses.set(i, course);
                found = true;
                break;
            }
        }
        
        if (!found) {
            throw new NotFoundException("Course not found with ID: " + course.getCourseId(), "Course", course.getCourseId());
        }
        
        saveAll();
        System.out.println("CourseRepository: Updated course " + course.getCourseName());
    }

    @Override
    public void delete(Course course) throws RepositoryException, NotFoundException {
        if (course == null) {
            throw new NotFoundException("Course not found", "Course", "null");
        }
        
        boolean removed = courses.removeIf(c -> c.getCourseId().equals(course.getCourseId()));
        if (!removed) {
            throw new NotFoundException("Course not found with ID: " + course.getCourseId(), "Course", course.getCourseId());
        }
        
        saveAll();
        System.out.println("CourseRepository: Deleted course " + course.getCourseName());
    }

    @Override
    public List<Course> getAll() throws RepositoryException {
        return new ArrayList<>(courses);
    }

    @Override
    public List<Course> find(String criteria) throws RepositoryException {
        return courses.stream()
                .filter(course -> course.getCourseName().toLowerCase().contains(criteria.toLowerCase()) ||
                                course.getCourseId().toLowerCase().contains(criteria.toLowerCase()) ||
                                (course.getFacultyName() != null && course.getFacultyName().toLowerCase().contains(criteria.toLowerCase())))
                .collect(Collectors.toList());
    }

    private void loadAll() throws RepositoryException {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            System.out.println("Course data file does not exist. Starting with empty repository.");
            return;
        }

        try {
            List<Course> courseList = objectMapper.readValue(file, new TypeReference<List<Course>>() {});
            courses.clear();
            courses.addAll(courseList);
            System.out.println("Loaded " + courses.size() + " courses from " + DATA_FILE);
        } catch (IOException e) {
            throw new RepositoryException("Failed to load courses from file: " + e.getMessage(), "LOAD", "COURSE", e);
        }
    }

    private void saveAll() throws RepositoryException {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE), courses);
        } catch (IOException e) {
            throw new RepositoryException("Failed to save courses to file: " + e.getMessage(), "SAVE", "COURSE", e);
        }
    }

    public long count() {
        return courses.size();
    }
}