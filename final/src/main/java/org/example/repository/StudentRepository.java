package org.example.repository;

import org.example.Tables.Student;

import java.util.List;

public interface StudentRepository {
    boolean addStudent(Student student);
    List<Student> getAllStudents();
    Student getStudentById(long id);
    boolean updateStudent(Student student);
    boolean deleteStudent(long id);
}