package com.example.assignment3;

public class UserRecord {
    int id;
    String Username;
    String Password;

    public String getPassword() {
        return Password;
    }

    public String getUsername() {
        return Username;
    }

    public int getId() {
        return id;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public void setId(int id) {
        this.id = id;
    }
}
