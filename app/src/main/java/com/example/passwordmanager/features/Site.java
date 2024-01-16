package com.example.passwordmanager.features;

import androidx.annotation.NonNull;

public class Site {
    private String name;
    private String password;
    private String username;

    public Site(String name, String password, String username) {
        this.name = name;
        this.password = password;
        this.username = username;
    }

    public Site(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {

        return name.replaceFirst("^.", name.substring(0, 1).toUpperCase());
//        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Site{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
