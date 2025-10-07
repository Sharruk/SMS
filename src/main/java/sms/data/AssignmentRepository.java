package sms.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import sms.domain.Assignment;
import sms.exceptions.RepositoryException;
import sms.exceptions.ValidationException;
import sms.exceptions.NotFoundException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AssignmentRepository implements Repository<Assignment> {
    private static final String DATA_FILE = "assignments.json";
    private final List<Assignment> assignments;
    private final ObjectMapper objectMapper;

    public AssignmentRepository() {
        this.assignments = new ArrayList<>();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            loadAll();
        } catch (RepositoryException e) {
            System.out.println("Warning: Could not load existing assignment data: " + e.getMessage());
        }
    }

    @Override
    public void add(Assignment assignment) throws RepositoryException, ValidationException {
        if (assignment == null) {
            throw new ValidationException("Assignment cannot be null", "assignment", "null");
        }
        if (assignment.getTitle() == null || assignment.getTitle().trim().isEmpty()) {
            throw new ValidationException("Assignment title cannot be empty", "title", assignment.getTitle());
        }
        
        assignments.add(assignment);
        saveAll();
        System.out.println("AssignmentRepository: Added assignment '" + assignment.getTitle() + "' for course " + assignment.getCourseId());
    }

    @Override
    public void update(Assignment assignment) throws RepositoryException, NotFoundException {
        if (assignment == null) {
            throw new NotFoundException("Assignment not found", "Assignment", "null");
        }
        
        boolean found = false;
        for (int i = 0; i < assignments.size(); i++) {
            if (assignments.get(i).getId() == assignment.getId()) {
                assignments.set(i, assignment);
                found = true;
                break;
            }
        }
        
        if (!found) {
            throw new NotFoundException("Assignment not found with ID: " + assignment.getId(), "Assignment", String.valueOf(assignment.getId()));
        }
        
        saveAll();
        System.out.println("AssignmentRepository: Updated assignment ID " + assignment.getId());
    }

    @Override
    public void delete(Assignment assignment) throws RepositoryException, NotFoundException {
        if (assignment == null) {
            throw new NotFoundException("Assignment not found", "Assignment", "null");
        }
        
        boolean removed = assignments.removeIf(a -> a.getId() == assignment.getId());
        
        if (!removed) {
            throw new NotFoundException("Assignment not found with ID: " + assignment.getId(), "Assignment", String.valueOf(assignment.getId()));
        }
        
        saveAll();
        System.out.println("AssignmentRepository: Deleted assignment ID " + assignment.getId());
    }

    @Override
    public List<Assignment> getAll() throws RepositoryException {
        return new ArrayList<>(assignments);
    }

    @Override
    public List<Assignment> find(String criteria) throws RepositoryException {
        return assignments.stream()
                .filter(a -> a.getTitle().toLowerCase().contains(criteria.toLowerCase()) ||
                           a.getCourseId().toLowerCase().contains(criteria.toLowerCase()) ||
                           String.valueOf(a.getId()).contains(criteria))
                .collect(Collectors.toList());
    }

    public List<Assignment> getAssignmentsByCourseId(String courseId) {
        return assignments.stream()
                .filter(a -> a.getCourseId().equals(courseId))
                .collect(Collectors.toList());
    }

    public List<Assignment> getAssignmentsByTeacherId(int teacherId) {
        return assignments.stream()
                .filter(a -> a.getTeacherId() == teacherId)
                .collect(Collectors.toList());
    }

    public Assignment getAssignmentById(int id) throws NotFoundException {
        return assignments.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Assignment not found with ID: " + id, "Assignment", String.valueOf(id)));
    }

    private void loadAll() throws RepositoryException {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            System.out.println("Assignment data file does not exist. Starting with empty repository.");
            return;
        }

        try {
            List<Assignment> loadedAssignments = objectMapper.readValue(file, new TypeReference<List<Assignment>>() {});
            assignments.clear();
            assignments.addAll(loadedAssignments);
            System.out.println("Loaded " + assignments.size() + " assignments from " + DATA_FILE);
        } catch (IOException e) {
            throw new RepositoryException("Failed to load assignments from file: " + e.getMessage(), "LOAD", "Assignment");
        }
    }

    private void saveAll() throws RepositoryException {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE), assignments);
        } catch (IOException e) {
            throw new RepositoryException("Failed to save assignments to file: " + e.getMessage(), "SAVE", "Assignment");
        }
    }

    public int getNextAssignmentId() {
        return assignments.stream()
                .mapToInt(Assignment::getId)
                .max()
                .orElse(0) + 1;
    }

    @Override
    public List<Assignment> sort(String criteria) throws RepositoryException {
        List<Assignment> sortedList = new ArrayList<>(assignments);
        if ("title".equalsIgnoreCase(criteria) || "name".equalsIgnoreCase(criteria)) {
            sortedList.sort((a1, a2) -> a1.getTitle().compareToIgnoreCase(a2.getTitle()));
        } else if ("id".equalsIgnoreCase(criteria)) {
            sortedList.sort((a1, a2) -> Integer.compare(a1.getId(), a2.getId()));
        } else {
            sortedList.sort((a1, a2) -> Integer.compare(a1.getId(), a2.getId()));
        }
        return sortedList;
    }
}
