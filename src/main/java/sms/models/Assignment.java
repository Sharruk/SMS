package sms.models;

/**
 * Represents an assignment created by a teacher for a course.
 * Demonstrates ENCAPSULATION.
 */
public class Assignment {

    private int    id;
    private String courseId;
    private String courseName;
    private String title;
    private String dueDate;

    public Assignment(int id, String courseId, String courseName,
                      String title, String dueDate) {
        this.id         = id;
        this.courseId   = courseId;
        this.courseName = courseName;
        this.title      = title;
        this.dueDate    = dueDate;
    }

    // Getters and setters
    public int    getId()                  { return id; }
    public void   setId(int id)            { this.id = id; }

    public String getCourseId()            { return courseId; }
    public void   setCourseId(String id)   { this.courseId = id; }

    public String getCourseName()          { return courseName; }
    public void   setCourseName(String n)  { this.courseName = n; }

    public String getTitle()               { return title; }
    public void   setTitle(String title)   { this.title = title; }

    public String getDueDate()             { return dueDate; }
    public void   setDueDate(String date)  { this.dueDate = date; }

    @Override
    public String toString() {
        return "Assignment #" + id + ": " + title +
               " | Course: " + courseName +
               " | Due: " + dueDate;
    }

    // Format: id,courseId,courseName,title,dueDate
    public String toFileString() {
        return id + "," + courseId + "," + courseName + "," + title + "," + dueDate;
    }

    public static Assignment fromFileString(String line) {
        String[] parts = line.split(",", -1);
        return new Assignment(
            Integer.parseInt(parts[0].trim()),
            parts[1].trim(), parts[2].trim(),
            parts[3].trim(), parts[4].trim()
        );
    }
}
