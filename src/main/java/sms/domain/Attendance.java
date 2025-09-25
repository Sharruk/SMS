package sms.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Attendance {
    private String studentRegNo;
    private String courseCode;
    private String classDates;
    private String status; // "Present", "Absent", "Late"

    // Default constructor for Jackson
    public Attendance() {}

    @JsonCreator
    public Attendance(@JsonProperty("studentRegNo") String studentRegNo,
                     @JsonProperty("courseCode") String courseCode,
                     @JsonProperty("classDates") String classDates,
                     @JsonProperty("status") String status) {
        this.studentRegNo = studentRegNo;
        this.courseCode = courseCode;
        this.classDates = classDates;
        this.status = status;
    }

    public String getStudentRegNo() {
        return studentRegNo;
    }

    public void setStudentRegNo(String studentRegNo) {
        this.studentRegNo = studentRegNo;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getClassDates() {
        return classDates;
    }

    public void setClassDates(String classDates) {
        this.classDates = classDates;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isPresent() {
        return "Present".equalsIgnoreCase(status);
    }

    public void markPresent() {
        this.status = "Present";
    }

    public void markAbsent() {
        this.status = "Absent";
    }

    public void markLate() {
        this.status = "Late";
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "studentRegNo='" + studentRegNo + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", classDates='" + classDates + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}