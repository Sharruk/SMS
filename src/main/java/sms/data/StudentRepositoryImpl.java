package sms.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import sms.domain.Student;
import sms.exceptions.RepositoryException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StudentRepositoryImpl implements Repository<Student> {
    private static final String DATA_FILE = "students.json";
    private final Map<String, Student> students;
    private final ObjectMapper objectMapper;

    public StudentRepositoryImpl() {
        this.students = new HashMap<>();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            loadAll();
        } catch (RepositoryException e) {
            System.out.println("Warning: Could not load existing student data: " + e.getMessage());
        }
    }

    @Override
    public Student save(Student student) throws RepositoryException {
        if (student == null || student.getUserId() == null) {
            throw new RepositoryException("Student or Student ID cannot be null", "SAVE", "STUDENT");
        }
        
        students.put(student.getUserId(), student);
        saveAll();
        return student;
    }

    @Override
    public Optional<Student> findById(String id) throws RepositoryException {
        if (id == null) {
            throw new RepositoryException("ID cannot be null", "FIND", "STUDENT");
        }
        return Optional.ofNullable(students.get(id));
    }

    @Override
    public List<Student> findAll() throws RepositoryException {
        return new ArrayList<>(students.values());
    }

    @Override
    public Student update(Student student) throws RepositoryException {
        if (student == null || student.getUserId() == null) {
            throw new RepositoryException("Student or Student ID cannot be null", "UPDATE", "STUDENT");
        }
        
        if (!students.containsKey(student.getUserId())) {
            throw new RepositoryException("Student not found with ID: " + student.getUserId(), "UPDATE", "STUDENT");
        }
        
        students.put(student.getUserId(), student);
        saveAll();
        return student;
    }

    @Override
    public boolean deleteById(String id) throws RepositoryException {
        if (id == null) {
            throw new RepositoryException("ID cannot be null", "DELETE", "STUDENT");
        }
        
        boolean removed = students.remove(id) != null;
        if (removed) {
            saveAll();
        }
        return removed;
    }

    @Override
    public void loadAll() throws RepositoryException {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            System.out.println("Student data file does not exist. Starting with empty repository.");
            return;
        }

        try {
            List<Student> studentList = objectMapper.readValue(file, new TypeReference<List<Student>>() {});
            students.clear();
            for (Student student : studentList) {
                students.put(student.getUserId(), student);
            }
            System.out.println("Loaded " + students.size() + " students from " + DATA_FILE);
        } catch (IOException e) {
            throw new RepositoryException("Failed to load students from file: " + e.getMessage(), "LOAD", "STUDENT", e);
        }
    }

    @Override
    public void saveAll() throws RepositoryException {
        try {
            List<Student> studentList = new ArrayList<>(students.values());
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE), studentList);
        } catch (IOException e) {
            throw new RepositoryException("Failed to save students to file: " + e.getMessage(), "SAVE", "STUDENT", e);
        }
    }

    @Override
    public long count() {
        return students.size();
    }

    // Additional methods for Student-specific operations
    public Optional<Student> findByStudentId(String studentId) throws RepositoryException {
        return students.values().stream()
                .filter(student -> studentId.equals(student.getStudentId()))
                .findFirst();
    }

    public List<Student> findByMajor(String major) throws RepositoryException {
        return students.values().stream()
                .filter(student -> major.equals(student.getMajor()))
                .toList();
    }

    public List<Student> findByYear(int year) throws RepositoryException {
        return students.values().stream()
                .filter(student -> student.getYear() == year)
                .toList();
    }
}