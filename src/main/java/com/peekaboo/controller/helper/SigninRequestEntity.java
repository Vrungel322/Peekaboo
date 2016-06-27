package com.peekaboo.controller.helper;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class SigninRequestEntity {

    @NotNull
    @NotEmpty
    @Length(min = 6, max = 25)
    private String username;

    @NotNull
    @Pattern(regexp = "[\\d|\\w|_]+")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
