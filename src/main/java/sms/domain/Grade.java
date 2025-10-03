package sms.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Grade {
    private int studentId;
    private String courseId;
    private int teacherId;
    private String grade;

    public Grade() {}

    @JsonCreator
    public Grade(@JsonProperty("studentId") int studentId,
                @JsonProperty("courseId") String courseId,
                @JsonProperty("teacherId") int teacherId,
                @JsonProperty("grade") String grade) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.teacherId = teacherId;
        this.grade = grade;
    }

    public Grade(int studentId, String courseId, int teacherId, String grade) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.teacherId = teacherId;
        this.grade = grade;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
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

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "studentId=" + studentId +
                ", courseId='" + courseId + '\'' +
                ", teacherId=" + teacherId +
                ", grade='" + grade + '\'' +
                '}';
    }
}
