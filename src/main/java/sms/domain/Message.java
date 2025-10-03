package sms.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
    private int messageId;
    private int fromUserId;
    private String fromUserName;
    private String fromRole;
    private int toUserId;
    private String toUserName;
    private String toRole;
    private String message;
    private String timestamp;
    private boolean isRead;

    public Message() {
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.isRead = false;
    }

    public Message(int messageId, int fromUserId, String fromUserName, String fromRole,
                   int toUserId, String toUserName, String toRole, String message) {
        this.messageId = messageId;
        this.fromUserId = fromUserId;
        this.fromUserName = fromUserName;
        this.fromRole = fromRole;
        this.toUserId = toUserId;
        this.toUserName = toUserName;
        this.toRole = toRole;
        this.message = message;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.isRead = false;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getFromRole() {
        return fromRole;
    }

    public void setFromRole(String fromRole) {
        this.fromRole = fromRole;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getToRole() {
        return toRole;
    }

    public void setToRole(String toRole) {
        this.toRole = toRole;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + messageId +
                ", from=" + fromUserName + " (" + fromRole + ")" +
                ", to=" + toUserName + " (" + toRole + ")" +
                ", message='" + message + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", isRead=" + isRead +
                '}';
    }
}
