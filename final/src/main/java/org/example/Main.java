package org.example;

import org.example.Tables.*;
import org.example.repository.*;
import org.example.service.AuthService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.List;

public class Main extends JFrame {
    private final StudentRepository studentRepository = new StudentRepositoryImpl();
    private final CourseRepository courseRepository = new CourseRepositoryImpl();
    private final EnrollmentRepository enrollmentRepository = new EnrollmentRepositoryImpl();
    private final AuthService authService = new AuthService();

    private JPanel buttonPanel;

    public Main() {
        if (!showLoginDialog()) {
            System.exit(0);
        }

        setTitle("Student Enrollment System - " + authService.getCurrentUser().getRole());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        initUI();
    }

    private boolean showLoginDialog() {
        JDialog loginDialog = new JDialog(this, "Login", true);
        loginDialog.setSize(350, 180);
        loginDialog.setLocationRelativeTo(null);
        loginDialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JButton loginBtn = new JButton("Login");

        final boolean[] success = {false};

        loginBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (authService.login(username, password)) {
                success[0] = true;
                loginDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(loginDialog, "Invalid credentials!\nTry: admin/admin, teacher/teacher, student/student");
            }
        });

        gbc.gridx = 0; gbc.gridy = 0; loginDialog.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; loginDialog.add(usernameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; loginDialog.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; loginDialog.add(passwordField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; loginDialog.add(loginBtn, gbc);

        loginDialog.setVisible(true);
        return success[0];
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("STUDENT COURSE ENROLLMENT SYSTEM", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        buttonPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        if (authService.isAdmin() || authService.isTeacher()) {
            addButton("Add Student", e -> addStudent());
            addButton("Show Students", e -> showStudents());
            addButton("Update Student", e -> updateStudent());
            addButton("Delete Student", e -> deleteStudent());
            addButton("Add Course", e -> addCourse());
            addButton("Show Courses", e -> showCourses());
            addButton("Update Course", e -> updateCourse());
            addButton("Delete Course", e -> deleteCourse());
            addButton("Enroll Student", e -> enrollStudent());
            addButton("Show Enrollments", e -> showEnrollments());
            addButton("Change Enrollment", e -> changeEnrollment());
            addButton("Drop Enrollment", e -> dropEnrollment());
            addButton("Export Students CSV", e -> exportToCSV());
            addButton("Import Students CSV", e -> importFromCSV());
        } else if (authService.isStudent()) {
            addButton("My Info", e -> showMyInfo());
            addButton("Available Courses", e -> showAvailableCourses());
            addButton("My Enrollments", e -> showEnrollments());
        }
        addButton("Logout", e -> logout());

        add(new JScrollPane(buttonPanel), BorderLayout.CENTER);

        JLabel statusBar = new JLabel("Logged in as: " + authService.getCurrentUser().getUsername() +
                " | Role: " + authService.getCurrentUser().getRole());
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        add(statusBar, BorderLayout.SOUTH);
    }

    private void addButton(String text, java.awt.event.ActionListener listener) {
        JButton btn = new JButton(text);
        btn.addActionListener(listener);
        buttonPanel.add(btn);
    }

    // === СТУДЕНТЫ ===

    private void addStudent() {
        Student student = showStudentDialog(null);
        if (student != null && studentRepository.addStudent(student)) {
            JOptionPane.showMessageDialog(this, "Student added!");
        }
    }

    private void showStudents() {
        List<Student> students = studentRepository.getAllStudents();
        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No students found.");
            return;
        }

        String[] columns = {"ID", "First Name", "Last Name", "Email", "Score"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        for (Student s : students) {
            model.addRow(new Object[]{s.getId(), s.getFirstName(), s.getLastName(), s.getEmail(), s.getScore()});
        }
        showTableDialog("Students List", model);
    }

    private void updateStudent() {
        String idStr = JOptionPane.showInputDialog(this, "Enter student ID:");
        if (idStr == null) return;

        try {
            long id = Long.parseLong(idStr);
            Student student = studentRepository.getStudentById(id);
            if (student == null) {
                JOptionPane.showMessageDialog(this, "Student not found!");
                return;
            }
            Student updated = showStudentDialog(student);
            if (updated != null && studentRepository.updateStudent(updated)) {
                JOptionPane.showMessageDialog(this, "Student updated!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID!");
        }
    }

    private void deleteStudent() {
        String idStr = JOptionPane.showInputDialog(this, "Enter student ID:");
        if (idStr == null) return;

        try {
            long id = Long.parseLong(idStr);
            int confirm = JOptionPane.showConfirmDialog(this, "Delete student?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION && studentRepository.deleteStudent(id)) {
                JOptionPane.showMessageDialog(this, "Student deleted!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID!");
        }
    }

    private Student showStudentDialog(Student existing) {
        JDialog dialog = new JDialog(this, existing == null ? "Add Student" : "Edit Student", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField firstNameField = new JTextField(15);
        JTextField lastNameField = new JTextField(15);
        JTextField emailField = new JTextField(15);
        JTextField scoreField = new JTextField(5);

        if (existing != null) {
            firstNameField.setText(existing.getFirstName());
            lastNameField.setText(existing.getLastName());
            emailField.setText(existing.getEmail());
            scoreField.setText(String.valueOf(existing.getScore()));
        }

        final Student[] result = {null};

        gbc.gridx = 0; gbc.gridy = 0; dialog.add(new JLabel("First name:"), gbc);
        gbc.gridx = 1; dialog.add(firstNameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; dialog.add(new JLabel("Last name:"), gbc);
        gbc.gridx = 1; dialog.add(lastNameField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; dialog.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; dialog.add(emailField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; dialog.add(new JLabel("Score (0-100):"), gbc);
        gbc.gridx = 1; dialog.add(scoreField, gbc);

        JPanel buttonPanel = new JPanel();
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.addActionListener(e -> {
            try {
                String firstName = firstNameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                String email = emailField.getText().trim();
                int score = Integer.parseInt(scoreField.getText().trim());

                if (!firstName.matches("[A-Za-z]{2,50}")) throw new Exception("Invalid first name");
                if (!lastName.matches("[A-Za-z]{2,50}")) throw new Exception("Invalid last name");
                if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) throw new Exception("Invalid email");
                if (score < 0 || score > 100) throw new Exception("Score must be 0-100");

                if (existing == null) {
                    result[0] = new Student(firstName, lastName, email, score);
                } else {
                    existing.setFirstName(firstName);
                    existing.setLastName(lastName);
                    existing.setEmail(email);
                    existing.setScore(score);
                    result[0] = existing;
                }
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage());
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        dialog.add(buttonPanel, gbc);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        return result[0];
    }

    // === КУРСЫ ===

    private void addCourse() {
        Course course = showCourseDialog(null);
        if (course != null && courseRepository.addCourse(course)) {
            JOptionPane.showMessageDialog(this, "Course added!");
        }
    }

    private void showCourses() {
        List<Course> courses = courseRepository.getAllCourses();
        if (courses.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No courses found.");
            return;
        }

        String[] columns = {"ID", "Course Name", "Instructor", "Credits"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        for (Course c : courses) {
            model.addRow(new Object[]{c.getId(), c.getCourseName(), c.getInstructorName(), c.getCredits()});
        }
        showTableDialog("Courses List", model);
    }

    private void updateCourse() {
        String idStr = JOptionPane.showInputDialog(this, "Enter course ID:");
        if (idStr == null) return;

        try {
            long id = Long.parseLong(idStr);
            Course course = courseRepository.getCourseById(id);
            if (course == null) {
                JOptionPane.showMessageDialog(this, "Course not found!");
                return;
            }
            Course updated = showCourseDialog(course);
            if (updated != null && courseRepository.updateCourse(updated)) {
                JOptionPane.showMessageDialog(this, "Course updated!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID!");
        }
    }

    private void deleteCourse() {
        if (!authService.isAdmin()) {
            JOptionPane.showMessageDialog(this, "Only ADMIN can delete courses!");
            return;
        }

        String idStr = JOptionPane.showInputDialog(this, "Enter course ID:");
        if (idStr == null) return;

        try {
            long id = Long.parseLong(idStr);
            int confirm = JOptionPane.showConfirmDialog(this, "Delete course?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION && courseRepository.deleteCourse(id)) {
                JOptionPane.showMessageDialog(this, "Course deleted!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID!");
        }
    }

    private Course showCourseDialog(Course existing) {
        JDialog dialog = new JDialog(this, existing == null ? "Add Course" : "Edit Course", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField(15);
        JTextField instructorField = new JTextField(15);
        JTextField creditsField = new JTextField(5);

        if (existing != null) {
            nameField.setText(existing.getCourseName());
            instructorField.setText(existing.getInstructorName());
            creditsField.setText(String.valueOf(existing.getCredits()));
        }

        final Course[] result = {null};

        gbc.gridx = 0; gbc.gridy = 0; dialog.add(new JLabel("Course name:"), gbc);
        gbc.gridx = 1; dialog.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; dialog.add(new JLabel("Instructor:"), gbc);
        gbc.gridx = 1; dialog.add(instructorField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; dialog.add(new JLabel("Credits (1-6):"), gbc);
        gbc.gridx = 1; dialog.add(creditsField, gbc);

        JPanel buttonPanel = new JPanel();
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String instructor = instructorField.getText().trim();
                int credits = Integer.parseInt(creditsField.getText().trim());

                if (name.isEmpty()) throw new Exception("Course name required");
                if (instructor.isEmpty()) throw new Exception("Instructor name required");
                if (credits < 1 || credits > 6) throw new Exception("Credits must be 1-6");

                if (existing == null) {
                    result[0] = new Course(name, instructor, credits);
                } else {
                    existing.setCourseName(name);
                    existing.setInstructorName(instructor);
                    existing.setCredits(credits);
                    result[0] = existing;
                }
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage());
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        dialog.add(buttonPanel, gbc);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        return result[0];
    }

    // === ENROLLMENT ===

    private void enrollStudent() {
        List<Student> students = studentRepository.getAllStudents();
        List<Course> courses = courseRepository.getAllCourses();

        if (students.isEmpty() || courses.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Need both students and courses!");
            return;
        }

        String[] studentNames = students.stream().map(s -> s.getId() + " - " + s.getFirstName() + " " + s.getLastName()).toArray(String[]::new);
        String[] courseNames = courses.stream().map(c -> c.getId() + " - " + c.getCourseName()).toArray(String[]::new);

        String studentChoice = (String) JOptionPane.showInputDialog(this, "Select student:", "Enroll", JOptionPane.QUESTION_MESSAGE, null, studentNames, studentNames[0]);
        String courseChoice = (String) JOptionPane.showInputDialog(this, "Select course:", "Enroll", JOptionPane.QUESTION_MESSAGE, null, courseNames, courseNames[0]);

        if (studentChoice != null && courseChoice != null) {
            long sId = Long.parseLong(studentChoice.split(" - ")[0]);
            long cId = Long.parseLong(courseChoice.split(" - ")[0]);
            if (enrollmentRepository.registerStudentToCourse(sId, cId)) {
                JOptionPane.showMessageDialog(this, "Student enrolled!");
            } else {
                JOptionPane.showMessageDialog(this, "Enrollment failed!");
            }
        }
    }

    private void showEnrollments() {
        List<String> enrollments = enrollmentRepository.getAllEnrollments();
        if (enrollments.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No enrollments found.");
            return;
        }

        JTextArea textArea = new JTextArea(20, 50);
        for (String e : enrollments) {
            textArea.append(e + "\n");
        }
        textArea.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Enrollments", JOptionPane.INFORMATION_MESSAGE);
    }

    private void changeEnrollment() {
        String idStr = JOptionPane.showInputDialog(this, "Enter enrollment ID:");
        if (idStr == null) return;

        try {
            long enrollmentId = Long.parseLong(idStr);
            String newCourseIdStr = JOptionPane.showInputDialog(this, "Enter new course ID:");
            if (newCourseIdStr != null) {
                long newCourseId = Long.parseLong(newCourseIdStr);
                if (enrollmentRepository.updateEnrollment(enrollmentId, newCourseId)) {
                    JOptionPane.showMessageDialog(this, "Enrollment updated!");
                } else {
                    JOptionPane.showMessageDialog(this, "Update failed!");
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID!");
        }
    }

    private void dropEnrollment() {
        String idStr = JOptionPane.showInputDialog(this, "Enter enrollment ID:");
        if (idStr == null) return;

        try {
            long id = Long.parseLong(idStr);
            int confirm = JOptionPane.showConfirmDialog(this, "Drop enrollment?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION && enrollmentRepository.dropEnrollment(id)) {
                JOptionPane.showMessageDialog(this, "Enrollment dropped!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID!");
        }
    }

    // === CSV ===

    private void exportToCSV() {
        List<Student> students = studentRepository.getAllStudents();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("students_export.csv"))) {
            writer.write("first_name,last_name,email,score");
            writer.newLine();
            for (Student s : students) {
                writer.write(s.getFirstName() + "," + s.getLastName() + "," + s.getEmail() + "," + s.getScore());
                writer.newLine();
            }
            JOptionPane.showMessageDialog(this, "Exported to students_export.csv");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Export failed: " + e.getMessage());
        }
    }

    private void importFromCSV() {
        String filename = JOptionPane.showInputDialog(this, "Enter CSV filename:");
        if (filename == null) return;

        int added = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    String firstName = data[0].trim();
                    String lastName = data[1].trim();
                    String email = data[2].trim();
                    try {
                        int score = Integer.parseInt(data[3].trim());
                        if (firstName.matches("[A-Za-z]{2,50}") && lastName.matches("[A-Za-z]{2,50}") &&
                                email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$") && score >= 0 && score <= 100) {
                            if (studentRepository.addStudent(new Student(firstName, lastName, email, score))) {
                                added++;
                            }
                        }
                    } catch (NumberFormatException ignored) {}
                }
            }
            JOptionPane.showMessageDialog(this, "Import completed! Added: " + added + " students.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Import failed: " + e.getMessage());
        }
    }

    private void showMyInfo() {
        JOptionPane.showMessageDialog(this, "Student info - needs linking with user account");
    }

    private void showAvailableCourses() {
        showCourses();
    }

    private void showTableDialog(String title, DefaultTableModel model) {
        JDialog dialog = new JDialog(this, title, true);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dialog.dispose());

        dialog.setLayout(new BorderLayout());
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(closeBtn, BorderLayout.SOUTH);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Logout?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new Main().setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}