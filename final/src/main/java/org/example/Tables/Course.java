package org.example.Tables;

public class Course {
    private long id;
    private String courseName;
    private String instructorName;
    private int credits;

    public Course() {
    }

    public Course(long id, String courseName, String instructorName, int credits) {
        this.id = id;
        this.courseName = courseName;
        this.instructorName = instructorName;
        this.credits = credits;
    }

    public Course(String courseName, String instructorName, int credits) {
        this.courseName = courseName;
        this.instructorName = instructorName;
        this.credits = credits;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    @Override
    public String toString() {
        return "Course{id=" + id +
                ", courseName='" + courseName + '\'' +
                ", instructorName='" + instructorName + '\'' +
                ", credits=" + credits +
                '}';
    }
}