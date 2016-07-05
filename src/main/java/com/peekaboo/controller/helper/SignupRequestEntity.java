package com.peekaboo.controller.helper;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class SignupRequestEntity {

    @Email
    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    @Length(min = 6)
    @Pattern(regexp = "[\\d\\w_]+")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
