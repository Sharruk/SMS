package sms.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Assignment {
    private int id;
    private String courseId;
    private int teacherId;
    private String title;
    private String description;
    private String dueDate;

    public Assignment() {}

    @JsonCreator
    public Assignment(@JsonProperty("id") int id,
                     @JsonProperty("courseId") String courseId,
                     @JsonProperty("teacherId") int teacherId,
                     @JsonProperty("title") String title,
                     @JsonProperty("description") String description,
                     @JsonProperty("dueDate") String dueDate) {
        this.id = id;
        this.courseId = courseId;
        this.teacherId = teacherId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "id=" + id +
                ", courseId='" + courseId + '\'' +
                ", teacherId=" + teacherId +
                ", title='" + title + '\'' +
                ", dueDate='" + dueDate + '\'' +
                '}';
    }
}
