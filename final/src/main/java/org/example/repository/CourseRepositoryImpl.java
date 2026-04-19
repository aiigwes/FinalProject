package org.example.repository;

import org.example.Tables.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseRepositoryImpl implements CourseRepository {

    private static final String url = "jdbc:sqlite:enrollment.db";

    @Override
    public boolean addCourse(Course course) {
        String sql = "INSERT INTO courses(course_name, instructor_name, credits) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, course.getCourseName());
            stmt.setString(2, course.getInstructorName());
            stmt.setInt(3, course.getCredits());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Course insert error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Course> getAllCourses() {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM courses ORDER BY id";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new Course(
                        rs.getLong("id"),
                        rs.getString("course_name"),
                        rs.getString("instructor_name"),
                        rs.getInt("credits")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Read error: " + e.getMessage());
        }

        return list;
    }

    @Override
    public Course getCourseById(long id) {
        String sql = "SELECT * FROM courses WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Course(
                        rs.getLong("id"),
                        rs.getString("course_name"),
                        rs.getString("instructor_name"),
                        rs.getInt("credits")
                );
            }
        } catch (SQLException e) {
            System.out.println("Search error: " + e.getMessage());
        }

        return null;
    }

    @Override
    public boolean updateCourse(Course course) {
        String sql = "UPDATE courses SET course_name=?, instructor_name=?, credits=? WHERE id=?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, course.getCourseName());
            stmt.setString(2, course.getInstructorName());
            stmt.setInt(3, course.getCredits());
            stmt.setLong(4, course.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Update error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteCourse(long id) {
        String sql = "DELETE FROM courses WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Delete error: " + e.getMessage());
            return false;
        }
    }
}