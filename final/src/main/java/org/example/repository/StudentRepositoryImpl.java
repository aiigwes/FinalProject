package org.example.repository;

import org.example.Tables.Student;
import org.example.Tables.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentRepositoryImpl implements StudentRepository {

    private static final String url = "jdbc:sqlite:enrollment.db";

    @Override
    public boolean register(User user) {
        String sql = "INSERT INTO users(username, password_hash, role) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordHash());
            stmt.setString(3, "TEACHER");

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Register error: " + e.getMessage());
            return false;
        }
    }


    @Override
    public User login(String username, String password) {
        String  sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    return new User(
                           rs.getLong("id"),
                           rs.getString("username"),
                           rs.getString("password_hash"),
                           rs.getString("role")
                    );
                }
            }

        catch (SQLException e) {
            System.out.println("Error to login " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean addStudent(Student student) {
        String sql = "INSERT INTO students(first_name, last_name, email, score, user_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getEmail());
            stmt.setInt(4, student.getScore());
            if (student.getUserId() != null) {
                stmt.setLong(5, student.getUserId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Student insert error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY id";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Student student = new Student(
                        rs.getLong("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getInt("score")
                );
                long userId = rs.getLong("user_id");
                if (!rs.wasNull()) {
                    student.setUserId(userId);
                }
                list.add(student);
            }
        } catch (SQLException e) {
            System.out.println("Read error: " + e.getMessage());
        }

        return list;
    }

    @Override
    public Student getStudentById(long id) {
        String sql = "SELECT * FROM students WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Student student = new Student(
                        rs.getLong("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getInt("score")
                );
                long userId = rs.getLong("user_id");
                if (!rs.wasNull()) {
                    student.setUserId(userId);
                }
                return student;
            }
        } catch (SQLException e) {
            System.out.println("Search error: " + e.getMessage());
        }

        return null;
    }

    @Override
    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET first_name=?, last_name=?, email=?, score=?, user_id=? WHERE id=?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getEmail());
            stmt.setInt(4, student.getScore());
            if (student.getUserId() != null) {
                stmt.setLong(5, student.getUserId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            stmt.setLong(6, student.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Update error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteStudent(long id) {
        String sql = "DELETE FROM students WHERE id = ?";

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