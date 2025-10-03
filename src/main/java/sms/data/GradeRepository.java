package sms.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import sms.domain.Grade;
import sms.exceptions.RepositoryException;
import sms.exceptions.ValidationException;
import sms.exceptions.NotFoundException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GradeRepository implements Repository<Grade> {
    private static final String DATA_FILE = "grades.json";
    private final List<Grade> grades;
    private final ObjectMapper objectMapper;

    public GradeRepository() {
        this.grades = new ArrayList<>();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            loadAll();
        } catch (RepositoryException e) {
            System.out.println("Warning: Could not load existing grade data: " + e.getMessage());
        }
    }

    @Override
    public void add(Grade grade) throws RepositoryException, ValidationException {
        if (grade == null) {
            throw new ValidationException("Grade cannot be null", "grade", "null");
        }
        if (grade.getGrade() == null || grade.getGrade().trim().isEmpty()) {
            throw new ValidationException("Grade value cannot be empty", "grade", grade.getGrade());
        }
        
        grades.add(grade);
        saveAll();
        System.out.println("GradeRepository: Added grade '" + grade.getGrade() + "' for student " + grade.getStudentId() + " in course " + grade.getCourseId());
    }

    @Override
    public void update(Grade grade) throws RepositoryException, NotFoundException {
        if (grade == null) {
            throw new NotFoundException("Grade not found", "Grade", "null");
        }
        
        boolean found = false;
        for (int i = 0; i < grades.size(); i++) {
            Grade g = grades.get(i);
            if (g.getStudentId() == grade.getStudentId() && 
                g.getCourseId().equals(grade.getCourseId())) {
                grades.set(i, grade);
                found = true;
                break;
            }
        }
        
        if (!found) {
            throw new NotFoundException("Grade not found for student " + grade.getStudentId() + " in course " + grade.getCourseId(), "Grade", grade.getCourseId());
        }
        
        saveAll();
        System.out.println("GradeRepository: Updated grade for student " + grade.getStudentId() + " in course " + grade.getCourseId());
    }

    @Override
    public void delete(Grade grade) throws RepositoryException, NotFoundException {
        if (grade == null) {
            throw new NotFoundException("Grade not found", "Grade", "null");
        }
        
        boolean removed = grades.removeIf(g -> 
            g.getStudentId() == grade.getStudentId() && 
            g.getCourseId().equals(grade.getCourseId()));
        
        if (!removed) {
            throw new NotFoundException("Grade not found for student " + grade.getStudentId() + " in course " + grade.getCourseId(), "Grade", grade.getCourseId());
        }
        
        saveAll();
        System.out.println("GradeRepository: Deleted grade for student " + grade.getStudentId() + " in course " + grade.getCourseId());
    }

    @Override
    public List<Grade> getAll() throws RepositoryException {
        return new ArrayList<>(grades);
    }

    @Override
    public List<Grade> find(String criteria) throws RepositoryException {
        return grades.stream()
                .filter(g -> g.getCourseId().toLowerCase().contains(criteria.toLowerCase()) ||
                           g.getGrade().toLowerCase().contains(criteria.toLowerCase()) ||
                           String.valueOf(g.getStudentId()).contains(criteria))
                .collect(Collectors.toList());
    }

    public List<Grade> getGradesByCourseId(String courseId) {
        return grades.stream()
                .filter(g -> g.getCourseId().equals(courseId))
                .collect(Collectors.toList());
    }

    public List<Grade> getGradesByStudentId(int studentId) {
        return grades.stream()
                .filter(g -> g.getStudentId() == studentId)
                .collect(Collectors.toList());
    }

    public List<Grade> getGradesByTeacherId(int teacherId) {
        return grades.stream()
                .filter(g -> g.getTeacherId() == teacherId)
                .collect(Collectors.toList());
    }

    public Grade getGradeByStudentAndCourse(int studentId, String courseId) throws NotFoundException {
        return grades.stream()
                .filter(g -> g.getStudentId() == studentId && g.getCourseId().equals(courseId))
                .findFirst()
                .orElse(null);
    }

    private void loadAll() throws RepositoryException {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            System.out.println("Grade data file does not exist. Starting with empty repository.");
            return;
        }

        try {
            List<Grade> loadedGrades = objectMapper.readValue(file, new TypeReference<List<Grade>>() {});
            grades.clear();
            grades.addAll(loadedGrades);
            System.out.println("Loaded " + grades.size() + " grades from " + DATA_FILE);
        } catch (IOException e) {
            throw new RepositoryException("Failed to load grades from file: " + e.getMessage(), "LOAD", "Grade");
        }
    }

    private void saveAll() throws RepositoryException {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE), grades);
        } catch (IOException e) {
            throw new RepositoryException("Failed to save grades to file: " + e.getMessage(), "SAVE", "Grade");
        }
    }
}
