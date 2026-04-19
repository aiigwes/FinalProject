package org.example.Tables;

public class Student extends Person {
    private long id;
    private int score;
    private Long userId;

    public Student() {
    }

    public Student(long id, String firstName, String lastName, String email, int score) {
        super(firstName, lastName, email);
        this.id = id;
        this.score = score;
    }

    public Student(long id, String firstName, String lastName, String email, int score, Long userId) {
        super(firstName, lastName, email);
        this.id = id;
        this.score = score;
        this.userId = userId;
    }

    public Student(String firstName, String lastName, String email, int score) {
        super(firstName, lastName, email);
        this.score = score;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "ID: " + id +
                " | Name: " + getFirstName() +
                " | Last name: " + getLastName() +
                " | Email: " + getEmail() +
                " | Score: " + score;
    }
}