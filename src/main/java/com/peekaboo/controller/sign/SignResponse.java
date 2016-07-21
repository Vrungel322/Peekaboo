
package com.peekaboo.controller.sign;


public class SignResponse {

    private String id;
    private String username;
    private int role;
    private boolean enabled;

    public String getId() {
        return id;
    }

    public SignResponse setId(String id) {
        this.id = id;
        return this;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public SignResponse setEnabled(boolean enabled) {
        this.enabled = enabled;
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
