package org.example.repository;

import org.example.Tables.Course;

import java.util.List;

public interface CourseRepository {
    boolean addCourse(Course course);
    List<Course> getAllCourses();
    Course getCourseById(long id);
    boolean updateCourse(Course course);
    boolean deleteCourse(long id);
}