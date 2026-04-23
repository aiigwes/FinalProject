package org.example.Tables;

public class User {
    private Long id;
    private String username;
    private String passwordHash;
    private Role role;

    public User() {
    }

    public User(String roleS) {
        if (roleS.equals("student")) {
            this.role = Role.STUDENT;
        }
    }
    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }


    public User(Long id, String username, String passwordHash, String roleS) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        if (roleS.equals("TEACHER")) {
            this.role = Role.TEACHER;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password=" + passwordHash +
                ", role=" + role +
                '}';
    }
}
