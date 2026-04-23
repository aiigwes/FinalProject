package org.example.repository;

import org.example.Tables.Student;
import org.example.Tables.User;

import java.util.List;

public interface StudentRepository {
    boolean register(User user);
    User login(String username, String password);
    boolean addStudent(Student student);
    List<Student> getAllStudents();
    Student getStudentById(long id);
    boolean updateStudent(Student student);
    boolean deleteStudent(long id);
}