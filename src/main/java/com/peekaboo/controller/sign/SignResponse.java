package com.peekaboo.controller.sign;


public class SignResponse {

    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String country;
    private String city;
    private String phone;
    private String email;
    private int role;
    private int state;
    private boolean enabled;
    private String token;


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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public SignResponse setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public SignResponse setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public SignResponse setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public SignResponse setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getCity() {
        return city;
    }

    public SignResponse setCity(String city) {
        this.city = city;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public SignResponse setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public SignResponse setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SignResponse{");
        sb.append("id='").append(id).append('\'');
        sb.append(", username='").append(username).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", country='").append(country).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", role=").append(role);
        sb.append(", state=").append(state);
        sb.append(", enabled=").append(enabled);
        sb.append(", token='").append(token).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
