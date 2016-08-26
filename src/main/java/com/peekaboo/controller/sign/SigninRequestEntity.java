package com.peekaboo.controller.sign;

;

public class SigninRequestEntity {

    private String login;

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

        for (char ch : login.toCharArray())
            if (Character.isAlphabetic(ch))
                return false;

        return true;

//        return !login.chars().anyMatch(Character::isAlphabetic);
    }
}
