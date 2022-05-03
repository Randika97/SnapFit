package com.example.snapfit.model;

public class Users {
    private String id;
    private String name;
    private  String Email;

    public Users() {}

    public Users(String id, String name, String email) {
        this.id = id;
        this.name = name;
        Email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
