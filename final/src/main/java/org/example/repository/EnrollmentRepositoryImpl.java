package org.example.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentRepositoryImpl implements EnrollmentRepository {

    private static final String url = "jdbc:sqlite:enrollment.db";


    @Override
    public boolean registerStudentToCourse(long studentId, long courseId) {
        String sql = "INSERT INTO enrollments(student_id, course_id) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, studentId);
            stmt.setLong(2, courseId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Enrollment error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean dropEnrollment(long id) {
        String sql = "DELETE FROM enrollments WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Delete error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateEnrollment(long id, long newCourseId) {
        String sql = "UPDATE enrollments SET course_id=? WHERE id=?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, newCourseId);
            stmt.setLong(2, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Update error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<String> getAllEnrollments() {
        List<String> list = new ArrayList<>();

        String sql = """
                SELECT e.id, s.first_name, s.last_name, c.course_name, e.enrollment_date
                FROM enrollments e
                JOIN students s ON e.student_id = s.id
                JOIN courses c ON e.course_id = c.id
                """;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add("Enrollment ID: " + rs.getLong("id") +
                        " | Student: " + rs.getString("first_name") +
                        " | Course: " + rs.getString("course_name"));
            }
        } catch (SQLException e) {
            System.out.println("Read error: " + e.getMessage());
        }

        return list;
    }
}