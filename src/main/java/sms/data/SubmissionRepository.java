package sms.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import sms.domain.Submission;
import sms.exceptions.RepositoryException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SubmissionRepository implements Repository<Submission> {
    private static final String FILE_PATH = "submissions.json";
    private final ObjectMapper objectMapper;
    private List<Submission> submissions;

    public SubmissionRepository() {
        this.objectMapper = new ObjectMapper();
        this.submissions = new ArrayList<>();
        loadFromFile();
    }

    private void loadFromFile() {
        try {
            File file = new File(FILE_PATH);
            if (file.exists()) {
                submissions = objectMapper.readValue(file, new TypeReference<List<Submission>>() {});
                System.out.println("Loaded " + submissions.size() + " submissions from " + FILE_PATH);
            } else {
                System.out.println("Submission data file does not exist. Starting with empty repository.");
                submissions = new ArrayList<>();
            }
        } catch (IOException e) {
            System.err.println("Error loading submissions from file: " + e.getMessage());
            submissions = new ArrayList<>();
        }
    }

    private void saveToFile() throws RepositoryException {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), submissions);
        } catch (IOException e) {
            throw new RepositoryException("Failed to save submissions to file", e);
        }
    }

    @Override
    public void add(Submission submission) throws RepositoryException {
        submissions.add(submission);
        saveToFile();
    }

    @Override
    public void update(Submission submission) throws RepositoryException {
        for (int i = 0; i < submissions.size(); i++) {
            if (submissions.get(i).getSubmissionId() == submission.getSubmissionId()) {
                submissions.set(i, submission);
                saveToFile();
                return;
            }
        }
        throw new RepositoryException("Submission not found for update");
    }

    @Override
    public void delete(Submission submission) throws RepositoryException {
        if (submissions.remove(submission)) {
            saveToFile();
        } else {
            throw new RepositoryException("Submission not found for deletion");
        }
    }

    @Override
    public Submission getById(int id) throws RepositoryException {
        return submissions.stream()
                .filter(s -> s.getSubmissionId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Submission> getAll() throws RepositoryException {
        return new ArrayList<>(submissions);
    }

    public int getNextSubmissionId() {
        return submissions.stream()
                .mapToInt(Submission::getSubmissionId)
                .max()
                .orElse(0) + 1;
    }

    public List<Submission> getSubmissionsByStudentId(int studentId) throws RepositoryException {
        return submissions.stream()
                .filter(submission -> submission.getStudentId() == studentId)
                .collect(Collectors.toList());
    }

    public List<Submission> getSubmissionsByAssignmentId(int assignmentId) throws RepositoryException {
        return submissions.stream()
                .filter(submission -> submission.getAssignmentId() == assignmentId)
                .collect(Collectors.toList());
    }

    public Submission getSubmissionByStudentAndAssignment(int studentId, int assignmentId) throws RepositoryException {
        return submissions.stream()
                .filter(submission -> submission.getStudentId() == studentId && 
                                    submission.getAssignmentId() == assignmentId)
                .findFirst()
                .orElse(null);
    }

    public void deleteBySubmissionId(int submissionId) throws RepositoryException {
        Submission submission = getById(submissionId);
        if (submission != null) {
            delete(submission);
        } else {
            throw new RepositoryException("Submission not found with ID: " + submissionId);
        }
    }
}
