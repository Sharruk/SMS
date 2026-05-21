package sms.models;

/**
 * Represents a grade given to a student for a specific course.
 * Demonstrates ENCAPSULATION.
 */
public class Grade {

    private int    studentId;
    private String courseId;
    private String studentName;
    private String courseName;
    private String grade;

    public Grade(int studentId, String courseId, String studentName,
                 String courseName, String grade) {
        this.studentId   = studentId;
        this.courseId    = courseId;
        this.studentName = studentName;
        this.courseName  = courseName;
        this.grade       = grade;
    }

    // Getters and setters
    public int    getStudentId()              { return studentId; }
    public void   setStudentId(int id)        { this.studentId = id; }

    public String getCourseId()               { return courseId; }
    public void   setCourseId(String id)      { this.courseId = id; }

    public String getStudentName()            { return studentName; }
    public void   setStudentName(String name) { this.studentName = name; }

    public String getCourseName()             { return courseName; }
    public void   setCourseName(String name)  { this.courseName = name; }

    public String getGrade()                  { return grade; }
    public void   setGrade(String grade)      { this.grade = grade; }

    @Override
    public String toString() {
        return "Student: " + studentName +
               " | Course: " + courseName +
               " | Grade: " + grade;
    }

    // Format: studentId,courseId,studentName,courseName,grade
    public String toFileString() {
        return studentId + "," + courseId + "," + studentName + "," + courseName + "," + grade;
    }

    public static Grade fromFileString(String line) {
        String[] parts = line.split(",", -1);
        return new Grade(
            Integer.parseInt(parts[0].trim()),
            parts[1].trim(), parts[2].trim(),
            parts[3].trim(), parts[4].trim()
        );
    }
}
