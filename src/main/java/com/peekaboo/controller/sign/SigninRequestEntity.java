package com.peekaboo.controller.sign;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class SigninRequestEntity {

    @NotNull
    @NotEmpty
    private String login;

    @NotNull
    @Pattern(regexp = "[\\d\\w_]+")
    @Length(min = 6)
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

    public boolean isEmail() {
        return login.contains("@");
    }

    public boolean isPhone() {
        return !login.chars().anyMatch(Character::isAlphabetic);
    }
}