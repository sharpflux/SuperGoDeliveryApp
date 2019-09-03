package com.sharpflux.supergodeliveryapp;

public class User {

    private int id;
    private String username, email,mobile;

    public User(int id, String username, String email, String mobile) {
        this.id = id;
        this.username = username;
        this.email = email;

        this.mobile = mobile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
