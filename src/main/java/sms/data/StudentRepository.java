package sms.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import sms.domain.Student;
import sms.exceptions.RepositoryException;
import sms.exceptions.ValidationException;
import sms.exceptions.NotFoundException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StudentRepository implements Repository<Student> {
    private static final String DATA_FILE = "students.json";
    private final List<Student> students;
    private final ObjectMapper objectMapper;

    public StudentRepository() {
        this.students = new ArrayList<>();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            loadAll();
        } catch (RepositoryException e) {
            System.out.println("Warning: Could not load existing student data: " + e.getMessage());
        }
    }

    @Override
    public void add(Student student) throws RepositoryException, ValidationException {
        if (student == null) {
            throw new ValidationException("Student cannot be null", "student", "null");
        }
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            throw new ValidationException("Student name cannot be empty", "name", student.getName());
        }
        
        students.add(student);
        saveAll();
        System.out.println("StudentRepository: Added student " + student.getName());
    }

    @Override
    public void update(Student student) throws RepositoryException, NotFoundException {
        if (student == null) {
            throw new NotFoundException("Student not found", "Student", "null");
        }
        
        boolean found = false;
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getUserId() == student.getUserId()) {
                students.set(i, student);
                found = true;
                break;
            }
        }
        
        if (!found) {
            throw new NotFoundException("Student not found with ID: " + student.getUserId(), "Student", String.valueOf(student.getUserId()));
        }
        
        saveAll();
        System.out.println("StudentRepository: Updated student " + student.getName());
    }

    @Override
    public void delete(Student student) throws RepositoryException, NotFoundException {
        if (student == null) {
            throw new NotFoundException("Student not found", "Student", "null");
        }
        
        boolean removed = students.removeIf(s -> s.getUserId() == student.getUserId());
        if (!removed) {
            throw new NotFoundException("Student not found with ID: " + student.getUserId(), "Student", String.valueOf(student.getUserId()));
        }
        
        saveAll();
        System.out.println("StudentRepository: Deleted student " + student.getName());
    }

    @Override
    public List<Student> getAll() throws RepositoryException {
        return new ArrayList<>(students);
    }

    @Override
    public List<Student> find(String criteria) throws RepositoryException {
        return students.stream()
                .filter(student -> student.getName().toLowerCase().contains(criteria.toLowerCase()) ||
                                 student.getEmail().toLowerCase().contains(criteria.toLowerCase()) ||
                                 String.valueOf(student.getUserId()).contains(criteria))
                .collect(Collectors.toList());
    }

    private void loadAll() throws RepositoryException {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            System.out.println("Student data file does not exist. Starting with empty repository.");
            return;
        }

        try {
            List<Student> studentList = objectMapper.readValue(file, new TypeReference<List<Student>>() {});
            students.clear();
            students.addAll(studentList);
            System.out.println("Loaded " + students.size() + " students from " + DATA_FILE);
        } catch (IOException e) {
            throw new RepositoryException("Failed to load students from file: " + e.getMessage(), "LOAD", "STUDENT", e);
        }
    }

    private void saveAll() throws RepositoryException {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE), students);
        } catch (IOException e) {
            throw new RepositoryException("Failed to save students to file: " + e.getMessage(), "SAVE", "STUDENT", e);
        }
    }

    public long count() {
        return students.size();
    }
}