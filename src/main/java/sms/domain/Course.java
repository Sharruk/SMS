package sms.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Course {
    private String courseId;
    private String courseName;
    private int creditHours;
    private String facultyName;
    private String classDays;
    private String classTimes;
    private String classDates;

    // Default constructor for Jackson
    public Course() {}

    @JsonCreator
    public Course(@JsonProperty("courseId") String courseId,
                 @JsonProperty("courseName") String courseName,
                 @JsonProperty("creditHours") int creditHours,
                 @JsonProperty("facultyName") String facultyName,
                 @JsonProperty("classDays") String classDays,
                 @JsonProperty("classTimes") String classTimes,
                 @JsonProperty("classDates") String classDates) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.creditHours = creditHours;
        this.facultyName = facultyName;
        this.classDays = classDays;
        this.classTimes = classTimes;
        this.classDates = classDates;
    }

    // Simplified constructor
    public Course(String courseId, String courseName, int creditHours) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.creditHours = creditHours;
    }

    public void displayCourseInfo() {
        System.out.println("=== Course Information ===");
        System.out.println("Course ID: " + courseId);
        System.out.println("Course Name: " + courseName);
        System.out.println("Credit Hours: " + creditHours);
        System.out.println("Faculty: " + (facultyName != null ? facultyName : "Not Assigned"));
        System.out.println("Class Days: " + (classDays != null ? classDays : "TBD"));
        System.out.println("Class Times: " + (classTimes != null ? classTimes : "TBD"));
        System.out.println("Class Dates: " + (classDates != null ? classDates : "TBD"));
    }

    // Getters and Setters
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getClassDays() {
        return classDays;
    }

    public void setClassDays(String classDays) {
        this.classDays = classDays;
    }

    public String getClassTimes() {
        return classTimes;
    }

    public void setClassTimes(String classTimes) {
        this.classTimes = classTimes;
    }

    public String getClassDates() {
        return classDates;
    }

    public void setClassDates(String classDates) {
        this.classDates = classDates;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId='" + courseId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", creditHours=" + creditHours +
                ", facultyName='" + facultyName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Course course = (Course) obj;
        return courseId != null ? courseId.equals(course.courseId) : course.courseId == null;
    }

    @Override
    public int hashCode() {
        return courseId != null ? courseId.hashCode() : 0;
    }
}