
package com.peekaboo.controller.utils;


public class SignResponse {

    private String id;
    private String username;
    private int role;

    public String getId() {
        return id;
    }

    public SignResponse setId(String id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public SignResponse setUsername(String username) {
        this.username = username;
        return this;
    }

    public int getRole() {
        return role;
    }

    public SignResponse setRole(int role) {
        this.role = role;
        return this;
    }
}
