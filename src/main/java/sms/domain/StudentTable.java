package sms.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StudentTable {
    private String studentName;
    private String studentRegNo;
    private String department;
    private String yearOfStudy;
    private String courseCode;
    private String studentEmail;

    // Default constructor for Jackson
    public StudentTable() {}

    @JsonCreator
    public StudentTable(@JsonProperty("studentName") String studentName,
                       @JsonProperty("studentRegNo") String studentRegNo,
                       @JsonProperty("department") String department,
                       @JsonProperty("yearOfStudy") String yearOfStudy,
                       @JsonProperty("courseCode") String courseCode,
                       @JsonProperty("studentEmail") String studentEmail) {
        this.studentName = studentName;
        this.studentRegNo = studentRegNo;
        this.department = department;
        this.yearOfStudy = yearOfStudy;
        this.courseCode = courseCode;
        this.studentEmail = studentEmail;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentRegNo() {
        return studentRegNo;
    }

    public void setStudentRegNo(String studentRegNo) {
        this.studentRegNo = studentRegNo;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(String yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    @Override
    public String toString() {
        return "StudentTable{" +
                "studentName='" + studentName + '\'' +
                ", studentRegNo='" + studentRegNo + '\'' +
                ", department='" + department + '\'' +
                ", yearOfStudy='" + yearOfStudy + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", studentEmail='" + studentEmail + '\'' +
                '}';
    }
}