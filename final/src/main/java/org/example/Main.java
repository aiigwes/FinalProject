package org.example;

import org.example.Tables.*;
import org.example.repository.*;
import org.example.service.AuthService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.util.List;

public class Main extends JFrame {
    private final StudentRepository studentRepository = new StudentRepositoryImpl();
    private final CourseRepository courseRepository = new CourseRepositoryImpl();
    private final EnrollmentRepository enrollmentRepository = new EnrollmentRepositoryImpl();
    private final AuthService authService = new AuthService();

    private JPanel buttonPanel;

    private final Color BG_COLOR = new Color(255, 241, 224, 255);
    private final Color BUTTON_COLOR = new Color(89, 153, 61, 255);
    private final Color BUTTON_HOVER = new Color(144, 197, 70);
    private final Color TEXT_COLOR = new Color(255, 100, 151);
    private final Color HEADER_COLOR = new Color(89, 153, 61, 255);
    private final Color STATUS_COLOR = new Color(178, 255, 134);

    public Main() {
        if (!showLoginDialog()) {
            System.exit(0);
        }

        setTitle("Student Enrollment System - " + authService.getCurrentUser().getRole());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        setUIFont();
        initUI();
    }

    private void setUIFont() {
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 18);
        Font regularFont = new Font("Segoe UI", Font.PLAIN, 15);

        UIManager.put("Button.font", buttonFont);
        UIManager.put("Label.font", regularFont);
        UIManager.put("Table.font", regularFont);
        UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 16));
        UIManager.put("TextField.font", regularFont);
        UIManager.put("TextArea.font", regularFont);
        UIManager.put("OptionPane.messageFont", regularFont);
        UIManager.put("OptionPane.buttonFont", buttonFont);
    }

    private boolean showRegisterDialog() {
        JDialog registerDialog = new JDialog(this, "Register", true);
        registerDialog.setSize(450, 500);
        registerDialog.setLocationRelativeTo(null);
        registerDialog.setBackground(BG_COLOR);

        JPanel registerPanel = new JPanel(new GridBagLayout());
        registerPanel.setBackground(BG_COLOR);
        registerPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("CREATE ACCOUNT");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(HEADER_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        registerPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;

        gbc.gridy = 1;
        gbc.gridx = 0;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(TEXT_COLOR);
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        registerPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        JTextField usernameField = new JTextField(15);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        usernameField.setBackground(Color.WHITE);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 190)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        registerPanel.add(usernameField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(TEXT_COLOR);
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        registerPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordField.setBackground(Color.WHITE);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 190)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        registerPanel.add(passwordField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        JLabel confirmLabel = new JLabel("Confirm:");
        confirmLabel.setForeground(TEXT_COLOR);
        confirmLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        registerPanel.add(confirmLabel, gbc);

        gbc.gridx = 1;
        JPasswordField confirmField = new JPasswordField(15);
        confirmField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        confirmField.setBackground(Color.WHITE);
        confirmField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 190)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        registerPanel.add(confirmField, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        JButton registerBtn = new JButton("REGISTER");
        registerBtn.setBackground(BUTTON_COLOR);
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        registerBtn.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        registerBtn.setFocusPainted(false);
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        registerBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                registerBtn.setBackground(BUTTON_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                registerBtn.setBackground(BUTTON_COLOR);
            }
        });

        final boolean[] success = {false};

        registerBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String confirm = new String(confirmField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(registerDialog,
                        "Fill all fields!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(confirm)) {
                JOptionPane.showMessageDialog(registerDialog,
                        "Passwords do not match!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (authService.register(username, password)) {
                authService.login(username, password);
                success[0] = true;
                JOptionPane.showMessageDialog(registerDialog,
                        "Registration successful!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                registerDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(registerDialog,
                        "User already exists!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        registerPanel.add(registerBtn, gbc);

        registerDialog.add(registerPanel);
        registerDialog.setVisible(true);

        return success[0];
    }

    private boolean showLoginDialog() {
        JDialog loginDialog = new JDialog(this, "Login", true);
        loginDialog.setSize(450, 500);
        loginDialog.setLocationRelativeTo(null);
        loginDialog.setBackground(BG_COLOR);

        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(BG_COLOR);
        loginPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("WELCOME BACK");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(HEADER_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;

        gbc.gridy = 1;
        gbc.gridx = 0;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(TEXT_COLOR);
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        loginPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        JTextField usernameField = new JTextField(15);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        usernameField.setBackground(Color.WHITE);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 190)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        loginPanel.add(usernameField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(TEXT_COLOR);
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        loginPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordField.setBackground(Color.WHITE);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 190)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        loginPanel.add(passwordField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        JButton loginBtn = new JButton("LOGIN");
        loginBtn.setBackground(BUTTON_COLOR);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loginBtn.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginBtn.setBackground(BUTTON_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginBtn.setBackground(BUTTON_COLOR);
            }
        });

        final boolean[] success = {false};

        loginBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (authService.login(username, password)) {
                success[0] = true;
                loginDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(loginDialog,
                        "Invalid credentials!",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        loginPanel.add(loginBtn, gbc);

        gbc.gridy = 4;

        JButton registerBtn = new JButton("CREATE ACCOUNT");
        registerBtn.setBackground(new Color(161, 218, 95));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        registerBtn.setFocusPainted(false);
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        registerBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                registerBtn.setBackground(new Color(177, 225, 141));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                registerBtn.setBackground(new Color(161, 218, 95));
            }
        });

        registerBtn.addActionListener(e -> {
            loginDialog.dispose();

            boolean registered = showRegisterDialog();
            if (registered) {
                success[0] = true;
            } else {
                showLoginDialog();
            }
        });

        loginPanel.add(registerBtn, gbc);

        gbc.gridy = 5;
        JLabel infoLabel = new JLabel("Student: student/student");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoLabel.setForeground(new Color(150, 160, 140));
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginPanel.add(infoLabel, gbc);

        loginDialog.add(loginPanel);
        loginDialog.setVisible(true);

        return success[0];
    }

    private void initUI() {
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(BG_COLOR);
        setContentPane(contentPane);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_COLOR);
        headerPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel titleLabel = new JLabel("STUDENT COURSE ENROLLMENT SYSTEM");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        contentPane.add(headerPanel, BorderLayout.NORTH);

        buttonPanel = new JPanel(new GridLayout(0, 2, 20, 20));
        buttonPanel.setBackground(BG_COLOR);
        buttonPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        if (authService.isTeacher()) {
            addMenuButton(" ADD STUDENT", e -> addStudent());
            addMenuButton(" ALL STUDENTS", e -> showStudents());
            addMenuButton(" UPDATE STUDENT", e -> updateStudent());
            addMenuButton(" DELETE STUDENT", e -> deleteStudent());

            addMenuButton(" ADD COURSE", e -> addCourse());
            addMenuButton(" ALL COURSES", e -> showCourses());
            addMenuButton(" UPDATE COURSE", e -> updateCourse());
            addMenuButton(" DELETE COURSE", e -> deleteCourse());

            addMenuButton(" ENROLL STUDENT", e -> enrollStudent());
            addMenuButton(" ALL ENROLLMENTS", e -> showEnrollments());
            addMenuButton(" CHANGE ENROLLMENT", e -> changeEnrollment());
            addMenuButton(" DROP ENROLLMENT", e -> dropEnrollment());

            addMenuButton(" EXPORT CSV", e -> exportToCSV());
            addMenuButton(" IMPORT CSV", e -> importFromCSV());

        } else if (authService.isStudent()) {
            addStudentButton(" AVAILABLE COURSES", e -> showAvailableCourses());
            addStudentButton(" MY ENROLLMENTS", e -> showMyEnrollments());
        }

        addMenuButton(" LOGOUT", e -> logout());

        JScrollPane scrollPane = new JScrollPane(buttonPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BG_COLOR);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(STATUS_COLOR);
        statusPanel.setBorder(new EmptyBorder(12, 20, 12, 20));

        JLabel statusBar = new JLabel(" Logged in as: " + authService.getCurrentUser().getUsername() +
                " | Role: " + (authService.isTeacher() ? "Teacher" : "Student"));
        statusBar.setForeground(TEXT_COLOR);
        statusBar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusPanel.add(statusBar, BorderLayout.WEST);

        contentPane.add(statusPanel, BorderLayout.SOUTH);
    }

    private void addMenuButton(String text, java.awt.event.ActionListener listener) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setBackground(BUTTON_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(BUTTON_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(BUTTON_COLOR);
            }
        });

        btn.addActionListener(listener);
        buttonPanel.add(btn);
    }

    private void addStudentButton(String text, java.awt.event.ActionListener listener) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btn.setBackground(new Color(100, 130, 110));
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.CENTER);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(80, 110, 90));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(100, 130, 110));
            }
        });

        btn.addActionListener(listener);
        buttonPanel.add(btn);
    }

    private void addStudent() {
        Student student = showStudentDialog(null);
        if (student != null && studentRepository.addStudent(student)) {
            showSuccess("Student added successfully!");
        }
    }

    private void showStudents() {
        List<Student> students = studentRepository.getAllStudents();
        if (students.isEmpty()) {
            showWarning("No students found.");
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
                showWarning("Student not found!");
                return;
            }
            Student updated = showStudentDialog(student);
            if (updated != null && studentRepository.updateStudent(updated)) {
                showSuccess("Student updated successfully!");
            }
        } catch (NumberFormatException e) {
            showError("Invalid ID!");
        }
    }

    private void deleteStudent() {
        String idStr = JOptionPane.showInputDialog(this, "Enter student ID:");
        if (idStr == null) return;

        try {
            long id = Long.parseLong(idStr);
            int confirm = JOptionPane.showConfirmDialog(this, "Delete student?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION && studentRepository.deleteStudent(id)) {
                showSuccess("Student deleted!");
            }
        } catch (NumberFormatException e) {
            showError("Invalid ID!");
        }
    }

    private Student showStudentDialog(Student existing) {
        JDialog dialog = new JDialog(this, existing == null ? "Add Student" : "Edit Student", true);
        dialog.setBackground(BG_COLOR);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_COLOR);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField firstNameField = createStyledTextField();
        JTextField lastNameField = createStyledTextField();
        JTextField emailField = createStyledTextField();
        JTextField scoreField = createStyledTextField();

        if (existing != null) {
            firstNameField.setText(existing.getFirstName());
            lastNameField.setText(existing.getLastName());
            emailField.setText(existing.getEmail());
            scoreField.setText(String.valueOf(existing.getScore()));
        }

        final Student[] result = {null};

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("First name:"), gbc);
        gbc.gridx = 1; panel.add(firstNameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Last name:"), gbc);
        gbc.gridx = 1; panel.add(lastNameField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; panel.add(emailField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Score (0-100):"), gbc);
        gbc.gridx = 1; panel.add(scoreField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(BG_COLOR);

        JButton saveBtn = createStyledButton("Save");
        JButton cancelBtn = createStyledButton("Cancel");

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
                showErrorDialog(dialog, ex.getMessage());
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        return result[0];
    }

    private void addCourse() {
        Course course = showCourseDialog(null);
        if (course != null && courseRepository.addCourse(course)) {
            showSuccess("Course added successfully!");
        }
    }

    private void showCourses() {
        List<Course> courses = courseRepository.getAllCourses();
        if (courses.isEmpty()) {
            showWarning("No courses found.");
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
                showWarning("Course not found!");
                return;
            }
            Course updated = showCourseDialog(course);
            if (updated != null && courseRepository.updateCourse(updated)) {
                showSuccess("Course updated successfully!");
            }
        } catch (NumberFormatException e) {
            showError("Invalid ID!");
        }
    }

    private void deleteCourse() {
        String idStr = JOptionPane.showInputDialog(this, "Enter course ID:");
        if (idStr == null) return;

        try {
            long id = Long.parseLong(idStr);
            int confirm = JOptionPane.showConfirmDialog(this, "Delete course? All enrollments will be deleted!", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION && courseRepository.deleteCourse(id)) {
                showSuccess("Course deleted!");
            }
        } catch (NumberFormatException e) {
            showError("Invalid ID!");
        }
    }

    private Course showCourseDialog(Course existing) {
        JDialog dialog = new JDialog(this, existing == null ? "Add Course" : "Edit Course", true);
        dialog.setBackground(BG_COLOR);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_COLOR);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = createStyledTextField();
        JTextField instructorField = createStyledTextField();
        JTextField creditsField = createStyledTextField();

        if (existing != null) {
            nameField.setText(existing.getCourseName());
            instructorField.setText(existing.getInstructorName());
            creditsField.setText(String.valueOf(existing.getCredits()));
        }

        final Course[] result = {null};

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Course name:"), gbc);
        gbc.gridx = 1; panel.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Instructor:"), gbc);
        gbc.gridx = 1; panel.add(instructorField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Credits (1-6):"), gbc);
        gbc.gridx = 1; panel.add(creditsField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(BG_COLOR);

        JButton saveBtn = createStyledButton("Save");
        JButton cancelBtn = createStyledButton("Cancel");

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
                showErrorDialog(dialog, ex.getMessage());
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        return result[0];
    }

    private void enrollStudent() {
        List<Student> students = studentRepository.getAllStudents();
        List<Course> courses = courseRepository.getAllCourses();

        if (students.isEmpty() || courses.isEmpty()) {
            showWarning("Need both students and courses!");
            return;
        }

        String[] studentNames = students.stream().map(s -> s.getId() + " - " + s.getFirstName() + " " + s.getLastName()).toArray(String[]::new);
        String[] courseNames = courses.stream().map(c -> c.getId() + " - " + c.getCourseName()).toArray(String[]::new);

        String studentChoice = (String) JOptionPane.showInputDialog(this, "Select student:", "Enroll",
                JOptionPane.QUESTION_MESSAGE, null, studentNames, studentNames[0]);
        String courseChoice = (String) JOptionPane.showInputDialog(this, "Select course:", "Enroll",
                JOptionPane.QUESTION_MESSAGE, null, courseNames, courseNames[0]);

        if (studentChoice != null && courseChoice != null) {
            long sId = Long.parseLong(studentChoice.split(" - ")[0]);
            long cId = Long.parseLong(courseChoice.split(" - ")[0]);
            if (enrollmentRepository.registerStudentToCourse(sId, cId)) {
                showSuccess("Student enrolled successfully!");
            } else {
                showError("Enrollment failed!");
            }
        }
    }

    private void showEnrollments() {
        List<String> enrollments = enrollmentRepository.getAllEnrollments();
        if (enrollments.isEmpty()) {
            showWarning("No enrollments found.");
            return;
        }

        JTextArea textArea = new JTextArea(20, 60);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textArea.setBackground(BG_COLOR);
        textArea.setForeground(TEXT_COLOR);
        for (String e : enrollments) {
            textArea.append(e + "\n");
        }
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.getViewport().setBackground(BG_COLOR);
        JOptionPane.showMessageDialog(this, scrollPane, "Enrollments", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showMyEnrollments() {
        showEnrollments();
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
                    showSuccess("Enrollment updated!");
                } else {
                    showError("Update failed!");
                }
            }
        } catch (NumberFormatException e) {
            showError("Invalid ID!");
        }
    }

    private void dropEnrollment() {
        String idStr = JOptionPane.showInputDialog(this, "Enter enrollment ID:");
        if (idStr == null) return;

        try {
            long id = Long.parseLong(idStr);
            int confirm = JOptionPane.showConfirmDialog(this, "Drop enrollment?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION && enrollmentRepository.dropEnrollment(id)) {
                showSuccess("Enrollment dropped!");
            }
        } catch (NumberFormatException e) {
            showError("Invalid ID!");
        }
    }

    private void exportToCSV() {
        List<Student> students = studentRepository.getAllStudents();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("students_export.csv"))) {
            writer.write("first_name,last_name,email,score");
            writer.newLine();
            for (Student s : students) {
                writer.write(s.getFirstName() + "," + s.getLastName() + "," + s.getEmail() + "," + s.getScore());
                writer.newLine();
            }
            showSuccess("Exported to students_export.csv");
        } catch (IOException e) {
            showError("Export failed: " + e.getMessage());
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
            showSuccess("Import completed! Added: " + added + " students.");
        } catch (IOException e) {
            showError("Import failed: " + e.getMessage());
        }
    }

    private void showAvailableCourses() {
        showCourses();
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setBackground(Color.WHITE);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 190)),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        return field;
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(BUTTON_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(BUTTON_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(BUTTON_COLOR);
            }
        });

        return btn;
    }

    private void showTableDialog(String title, DefaultTableModel model) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setBackground(BG_COLOR);

        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(HEADER_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setBackground(BG_COLOR);
        table.setForeground(TEXT_COLOR);
        table.setSelectionBackground(new Color(200, 215, 180));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(BG_COLOR);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 190)));

        JButton closeBtn = createStyledButton("Close");
        closeBtn.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BG_COLOR);
        buttonPanel.add(closeBtn);

        dialog.setLayout(new BorderLayout());
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setSize(800, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }

    private void showErrorDialog(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Logout?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new Main().setVisible(true);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}