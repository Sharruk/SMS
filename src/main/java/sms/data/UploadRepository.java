package sms.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import sms.domain.UploadMetadata;
import sms.exceptions.RepositoryException;
import sms.exceptions.ValidationException;
import sms.exceptions.NotFoundException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class UploadRepository implements Repository<UploadMetadata> {
    private static final String DATA_FILE = "uploads.json";
    private final List<UploadMetadata> uploads;
    private final ObjectMapper objectMapper;

    public UploadRepository() {
        this.uploads = new ArrayList<>();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            loadAll();
        } catch (RepositoryException e) {
            System.out.println("Upload data file does not exist. Starting with empty repository.");
        }
    }

    @Override
    public void add(UploadMetadata upload) throws RepositoryException, ValidationException {
        if (upload == null) {
            throw new ValidationException("Upload metadata cannot be null", "upload", "null");
        }
        if (upload.getFileName() == null || upload.getFileName().trim().isEmpty()) {
            throw new ValidationException("File name cannot be empty", "fileName", upload.getFileName());
        }
        
        uploads.add(upload);
        saveAll();
    }

    @Override
    public void update(UploadMetadata upload) throws RepositoryException, NotFoundException {
        if (upload == null) {
            throw new NotFoundException("Upload not found", "Upload", "null");
        }
        
        boolean found = false;
        for (int i = 0; i < uploads.size(); i++) {
            if (uploads.get(i).getId() == upload.getId()) {
                uploads.set(i, upload);
                found = true;
                break;
            }
        }
        
        if (!found) {
            throw new NotFoundException("Upload not found with ID: " + upload.getId(), "Upload", String.valueOf(upload.getId()));
        }
        
        saveAll();
    }

    @Override
    public void delete(UploadMetadata upload) throws RepositoryException, NotFoundException {
        if (upload == null) {
            throw new NotFoundException("Upload not found", "Upload", "null");
        }
        
        boolean removed = uploads.removeIf(u -> u.getId() == upload.getId());
        if (!removed) {
            throw new NotFoundException("Upload not found with ID: " + upload.getId(), "Upload", String.valueOf(upload.getId()));
        }
        
        saveAll();
    }

    @Override
    public List<UploadMetadata> getAll() throws RepositoryException {
        return new ArrayList<>(uploads);
    }

    @Override
    public List<UploadMetadata> find(String criteria) throws RepositoryException {
        return uploads.stream()
                .filter(upload -> upload.getFileName().toLowerCase().contains(criteria.toLowerCase()) ||
                                upload.getUploadedBy().toLowerCase().contains(criteria.toLowerCase()) ||
                                upload.getRole().toLowerCase().contains(criteria.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<UploadMetadata> sort(String criteria) throws RepositoryException {
        List<UploadMetadata> sorted = new ArrayList<>(uploads);
        
        switch (criteria.toLowerCase()) {
            case "name":
            case "filename":
                sorted.sort(Comparator.comparing(UploadMetadata::getFileName));
                break;
            case "role":
                sorted.sort(Comparator.comparing(UploadMetadata::getRole));
                break;
            case "timestamp":
            case "date":
                sorted.sort(Comparator.comparing(UploadMetadata::getTimestamp).reversed());
                break;
            default:
                sorted.sort(Comparator.comparing(UploadMetadata::getId));
        }
        
        return sorted;
    }

    public List<UploadMetadata> getUploadsByRole(String role) throws RepositoryException {
        return uploads.stream()
                .filter(upload -> upload.getRole().equalsIgnoreCase(role))
                .collect(Collectors.toList());
    }

    public List<UploadMetadata> getUploadsByUser(String userName) throws RepositoryException {
        return uploads.stream()
                .filter(upload -> upload.getUploadedBy().equalsIgnoreCase(userName))
                .collect(Collectors.toList());
    }

    public int getNextUploadId() {
        return uploads.stream()
                .mapToInt(UploadMetadata::getId)
                .max()
                .orElse(0) + 1;
    }

    private void loadAll() throws RepositoryException {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            throw new RepositoryException("Upload data file does not exist");
        }

        try {
            List<UploadMetadata> loadedUploads = objectMapper.readValue(file, 
                new TypeReference<List<UploadMetadata>>() {});
            
            uploads.clear();
            uploads.addAll(loadedUploads);
            
            System.out.println("Loaded " + uploads.size() + " uploads from " + DATA_FILE);
        } catch (IOException e) {
            throw new RepositoryException("Failed to load uploads from file: " + e.getMessage(), e);
        }
    }

    private void saveAll() throws RepositoryException {
        try {
            objectMapper.writerWithDefaultPrettyPrinter()
                       .writeValue(new File(DATA_FILE), uploads);
        } catch (IOException e) {
            throw new RepositoryException("Failed to save uploads to file: " + e.getMessage(), e);
        }
    }
}
