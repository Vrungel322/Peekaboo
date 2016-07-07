package com.peekaboo.controller.sign;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class SignupRequestEntity {

    @NotNull
    @NotEmpty
    //TODO: pattern
    private String login;

    @NotNull
    @Length(min = 6)
    @Pattern(regexp = "[\\d\\w_]+")
    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
