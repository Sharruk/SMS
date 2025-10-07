package sms.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import sms.domain.Teacher;
import sms.exceptions.RepositoryException;
import sms.exceptions.ValidationException;
import sms.exceptions.NotFoundException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TeacherRepository implements Repository<Teacher> {
    private static final String DATA_FILE = "teachers.json";
    private final List<Teacher> teachers;
    private final ObjectMapper objectMapper;

    public TeacherRepository() {
        this.teachers = new ArrayList<>();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            loadAll();
        } catch (RepositoryException e) {
            System.out.println("Warning: Could not load existing teacher data: " + e.getMessage());
        }
    }

    @Override
    public void add(Teacher teacher) throws RepositoryException, ValidationException {
        if (teacher == null) {
            throw new ValidationException("Teacher cannot be null", "teacher", "null");
        }
        if (teacher.getName() == null || teacher.getName().trim().isEmpty()) {
            throw new ValidationException("Teacher name cannot be empty", "name", teacher.getName());
        }
        
        teachers.add(teacher);
        saveAll();
        System.out.println("TeacherRepository: Added teacher " + teacher.getName());
    }

    @Override
    public void update(Teacher teacher) throws RepositoryException, NotFoundException {
        if (teacher == null) {
            throw new NotFoundException("Teacher not found", "Teacher", "null");
        }
        
        boolean found = false;
        for (int i = 0; i < teachers.size(); i++) {
            if (teachers.get(i).getUserId() == teacher.getUserId()) {
                teachers.set(i, teacher);
                found = true;
                break;
            }
        }
        
        if (!found) {
            throw new NotFoundException("Teacher not found with ID: " + teacher.getUserId(), "Teacher", String.valueOf(teacher.getUserId()));
        }
        
        saveAll();
        System.out.println("TeacherRepository: Updated teacher " + teacher.getName());
    }

    @Override
    public void delete(Teacher teacher) throws RepositoryException, NotFoundException {
        if (teacher == null) {
            throw new NotFoundException("Teacher not found", "Teacher", "null");
        }
        
        boolean removed = teachers.removeIf(t -> t.getUserId() == teacher.getUserId());
        if (!removed) {
            throw new NotFoundException("Teacher not found with ID: " + teacher.getUserId(), "Teacher", String.valueOf(teacher.getUserId()));
        }
        
        saveAll();
        System.out.println("TeacherRepository: Deleted teacher " + teacher.getName());
    }

    @Override
    public List<Teacher> getAll() throws RepositoryException {
        return new ArrayList<>(teachers);
    }

    @Override
    public List<Teacher> find(String criteria) throws RepositoryException {
        return teachers.stream()
                .filter(teacher -> teacher.getName().toLowerCase().contains(criteria.toLowerCase()) ||
                                 teacher.getEmail().toLowerCase().contains(criteria.toLowerCase()) ||
                                 String.valueOf(teacher.getUserId()).contains(criteria))
                .collect(Collectors.toList());
    }

    private void loadAll() throws RepositoryException {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            System.out.println("Teacher data file does not exist. Starting with empty repository.");
            return;
        }

        try {
            List<Teacher> teacherList = objectMapper.readValue(file, new TypeReference<List<Teacher>>() {});
            teachers.clear();
            teachers.addAll(teacherList);
            System.out.println("Loaded " + teachers.size() + " teachers from " + DATA_FILE);
        } catch (IOException e) {
            throw new RepositoryException("Failed to load teachers from file: " + e.getMessage(), "LOAD", "TEACHER", e);
        }
    }

    private void saveAll() throws RepositoryException {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE), teachers);
        } catch (IOException e) {
            throw new RepositoryException("Failed to save teachers to file: " + e.getMessage(), "SAVE", "TEACHER", e);
        }
    }

    public long count() {
        return teachers.size();
    }

    public List<Teacher> sort(String criteria) throws RepositoryException {
        List<Teacher> sortedList = new ArrayList<>(teachers);
        
        if ("name".equalsIgnoreCase(criteria)) {
            sortedList.sort((t1, t2) -> t1.getName().compareToIgnoreCase(t2.getName()));
        } else if ("id".equalsIgnoreCase(criteria)) {
            sortedList.sort((t1, t2) -> Integer.compare(t1.getUserId(), t2.getUserId()));
        } else {
            sortedList.sort((t1, t2) -> t1.getName().compareToIgnoreCase(t2.getName()));
        }
        
        return sortedList;
    }
}