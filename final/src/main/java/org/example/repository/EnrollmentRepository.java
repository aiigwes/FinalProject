package org.example.repository;

import java.util.List;

public interface EnrollmentRepository {
    boolean registerStudentToCourse(long studentId, long courseId);
    boolean dropEnrollment(long enrollmentId);
    boolean updateEnrollment(long enrollmentId, long newCourseId);
    List<String> getAllEnrollments();
}