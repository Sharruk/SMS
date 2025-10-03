package sms.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import sms.domain.Message;
import sms.exceptions.RepositoryException;
import sms.exceptions.ValidationException;
import sms.exceptions.NotFoundException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MessageRepository implements Repository<Message> {
    private static final String DATA_FILE = "messages.json";
    private final List<Message> messages;
    private final ObjectMapper objectMapper;

    public MessageRepository() {
        this.messages = new ArrayList<>();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            loadAll();
        } catch (RepositoryException e) {
            System.out.println("Warning: Could not load existing message data: " + e.getMessage());
        }
    }

    @Override
    public void add(Message message) throws RepositoryException, ValidationException {
        if (message == null) {
            throw new ValidationException("Message cannot be null", "message", "null");
        }
        if (message.getMessage() == null || message.getMessage().trim().isEmpty()) {
            throw new ValidationException("Message content cannot be empty", "message", message.getMessage());
        }
        
        messages.add(message);
        saveAll();
        System.out.println("MessageRepository: Added message from " + message.getFromUserName() + " to " + message.getToUserName());
    }

    @Override
    public void update(Message message) throws RepositoryException, NotFoundException {
        if (message == null) {
            throw new NotFoundException("Message not found", "Message", "null");
        }
        
        boolean found = false;
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getMessageId() == message.getMessageId()) {
                messages.set(i, message);
                found = true;
                break;
            }
        }
        
        if (!found) {
            throw new NotFoundException("Message not found with ID: " + message.getMessageId(), "Message", String.valueOf(message.getMessageId()));
        }
        
        saveAll();
        System.out.println("MessageRepository: Updated message " + message.getMessageId());
    }

    @Override
    public void delete(Message message) throws RepositoryException, NotFoundException {
        if (message == null) {
            throw new NotFoundException("Message not found", "Message", "null");
        }
        
        boolean removed = messages.removeIf(m -> m.getMessageId() == message.getMessageId());
        if (!removed) {
            throw new NotFoundException("Message not found with ID: " + message.getMessageId(), "Message", String.valueOf(message.getMessageId()));
        }
        
        saveAll();
        System.out.println("MessageRepository: Deleted message " + message.getMessageId());
    }

    @Override
    public List<Message> getAll() throws RepositoryException {
        return new ArrayList<>(messages);
    }

    @Override
    public List<Message> find(String criteria) throws RepositoryException {
        return messages.stream()
                .filter(message -> message.getMessage().toLowerCase().contains(criteria.toLowerCase()) ||
                                 message.getFromUserName().toLowerCase().contains(criteria.toLowerCase()) ||
                                 message.getToUserName().toLowerCase().contains(criteria.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Message> getMessagesForUser(int userId, String role) throws RepositoryException {
        return messages.stream()
                .filter(message -> message.getToUserId() == userId && message.getToRole().equalsIgnoreCase(role))
                .collect(Collectors.toList());
    }

    public List<Message> getUnreadMessagesForUser(int userId, String role) throws RepositoryException {
        return messages.stream()
                .filter(message -> message.getToUserId() == userId && 
                                 message.getToRole().equalsIgnoreCase(role) && 
                                 !message.isRead())
                .collect(Collectors.toList());
    }

    public Message findById(int messageId) throws NotFoundException {
        return messages.stream()
                .filter(m -> m.getMessageId() == messageId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Message not found with ID: " + messageId, "Message", String.valueOf(messageId)));
    }

    private void loadAll() throws RepositoryException {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            System.out.println("Message data file does not exist. Starting with empty repository.");
            return;
        }

        try {
            List<Message> loadedMessages = objectMapper.readValue(file, new TypeReference<List<Message>>() {});
            messages.clear();
            messages.addAll(loadedMessages);
            System.out.println("Loaded " + messages.size() + " messages from " + DATA_FILE);
        } catch (IOException e) {
            throw new RepositoryException("Failed to load messages from file: " + e.getMessage(), "LOAD", "Message");
        }
    }

    private void saveAll() throws RepositoryException {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE), messages);
        } catch (IOException e) {
            throw new RepositoryException("Failed to save messages to file: " + e.getMessage(), "SAVE", "Message");
        }
    }

    public int getNextMessageId() {
        return messages.stream()
                .mapToInt(Message::getMessageId)
                .max()
                .orElse(0) + 1;
    }
}
