package sms.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import sms.domain.Admin;
import sms.exceptions.RepositoryException;
import sms.exceptions.ValidationException;
import sms.exceptions.NotFoundException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdminRepository implements Repository<Admin> {
    private static final String DATA_FILE = "admins.json";
    private final List<Admin> admins;
    private final ObjectMapper objectMapper;

    public AdminRepository() {
        this.admins = new ArrayList<>();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            loadAll();
        } catch (RepositoryException e) {
            System.out.println("Warning: Could not load existing admin data: " + e.getMessage());
        }
    }

    @Override
    public void add(Admin admin) throws RepositoryException, ValidationException {
        if (admin == null) {
            throw new ValidationException("Admin cannot be null", "admin", "null");
        }
        if (admin.getName() == null || admin.getName().trim().isEmpty()) {
            throw new ValidationException("Admin name cannot be empty", "name", admin.getName());
        }
        
        admins.add(admin);
        saveAll();
        System.out.println("AdminRepository: Added admin " + admin.getName());
    }

    @Override
    public void update(Admin admin) throws RepositoryException, NotFoundException {
        if (admin == null) {
            throw new NotFoundException("Admin not found", "Admin", "null");
        }
        
        boolean found = false;
        for (int i = 0; i < admins.size(); i++) {
            if (admins.get(i).getUserId() == admin.getUserId()) {
                admins.set(i, admin);
                found = true;
                break;
            }
        }
        
        if (!found) {
            throw new NotFoundException("Admin not found with ID: " + admin.getUserId(), "Admin", String.valueOf(admin.getUserId()));
        }
        
        saveAll();
        System.out.println("AdminRepository: Updated admin " + admin.getName());
    }

    @Override
    public void delete(Admin admin) throws RepositoryException, NotFoundException {
        if (admin == null) {
            throw new NotFoundException("Admin not found", "Admin", "null");
        }
        
        boolean removed = admins.removeIf(a -> a.getUserId() == admin.getUserId());
        if (!removed) {
            throw new NotFoundException("Admin not found with ID: " + admin.getUserId(), "Admin", String.valueOf(admin.getUserId()));
        }
        
        saveAll();
        System.out.println("AdminRepository: Deleted admin " + admin.getName());
    }

    @Override
    public List<Admin> getAll() throws RepositoryException {
        return new ArrayList<>(admins);
    }

    @Override
    public List<Admin> find(String criteria) throws RepositoryException {
        return admins.stream()
                .filter(admin -> admin.getName().toLowerCase().contains(criteria.toLowerCase()) ||
                               admin.getEmail().toLowerCase().contains(criteria.toLowerCase()) ||
                               String.valueOf(admin.getUserId()).contains(criteria))
                .collect(Collectors.toList());
    }

    private void loadAll() throws RepositoryException {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            System.out.println("Admin data file does not exist. Starting with empty repository.");
            return;
        }

        try {
            List<Admin> adminList = objectMapper.readValue(file, new TypeReference<List<Admin>>() {});
            admins.clear();
            admins.addAll(adminList);
            System.out.println("Loaded " + admins.size() + " admins from " + DATA_FILE);
        } catch (IOException e) {
            throw new RepositoryException("Failed to load admins from file: " + e.getMessage(), "LOAD", "ADMIN", e);
        }
    }

    private void saveAll() throws RepositoryException {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE), admins);
        } catch (IOException e) {
            throw new RepositoryException("Failed to save admins to file: " + e.getMessage(), "SAVE", "ADMIN", e);
        }
    }

    public long count() {
        return admins.size();
    }

    @Override
    public List<Admin> sort(String criteria) throws RepositoryException {
        List<Admin> sortedList = new ArrayList<>(admins);
        if ("name".equalsIgnoreCase(criteria)) {
            sortedList.sort((a1, a2) -> a1.getName().compareToIgnoreCase(a2.getName()));
        } else if ("id".equalsIgnoreCase(criteria)) {
            sortedList.sort((a1, a2) -> Integer.compare(a1.getUserId(), a2.getUserId()));
        } else {
            sortedList.sort((a1, a2) -> a1.getName().compareToIgnoreCase(a2.getName()));
        }
        return sortedList;
    }
}