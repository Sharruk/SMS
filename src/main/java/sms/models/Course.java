package sms.models;

/**
 * Represents an academic course.
 *
 * Demonstrates:
 *   - ENCAPSULATION      : private fields + getters/setters
 *   - CONSTRUCTOR OVERLOADING: two constructors with different parameters
 */
public class Course {

    private String courseId;
    private String courseName;
    private int    creditHours;
    private String teacherName;

    // CONSTRUCTOR OVERLOADING - full constructor (with teacher name)
    public Course(String courseId, String courseName, int creditHours, String teacherName) {
        this.courseId    = courseId;
        this.courseName  = courseName;
        this.creditHours = creditHours;
        this.teacherName = teacherName;
    }

    // CONSTRUCTOR OVERLOADING - simplified constructor (teacher not yet assigned)
    public Course(String courseId, String courseName, int creditHours) {
        this(courseId, courseName, creditHours, "Not Assigned");
    }

    // Getters and setters (ENCAPSULATION)
    public String getCourseId()               { return courseId; }
    public void   setCourseId(String id)      { this.courseId = id; }

    public String getCourseName()             { return courseName; }
    public void   setCourseName(String name)  { this.courseName = name; }

    public int  getCreditHours()              { return creditHours; }
    public void setCreditHours(int hours)     { this.creditHours = hours; }

    public String getTeacherName()            { return teacherName; }
    public void   setTeacherName(String name) { this.teacherName = name; }

    @Override
    public String toString() {
        return "ID: " + courseId + " | " + courseName +
               " | Credits: " + creditHours +
               " | Teacher: " + teacherName;
    }

    // Format: courseId,courseName,creditHours,teacherName
    public String toFileString() {
        return courseId + "," + courseName + "," + creditHours + "," + teacherName;
    }

    public static Course fromFileString(String line) {
        String[] parts = line.split(",", -1);
        return new Course(
            parts[0].trim(), parts[1].trim(),
            Integer.parseInt(parts[2].trim()),
            parts[3].trim()
        );
    }
}
