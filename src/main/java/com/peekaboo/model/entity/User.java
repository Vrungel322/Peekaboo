package com.peekaboo.model.entity;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "user_id")
    private String id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column
    private String displayName;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String telephone;

    @Column(unique = true)
    private String email;

    @Column
    private LocalDate birthdate;

    @Column
    private int roles;

    //TODO: male = 0 , female = 1
    @Column
    private int gender;

    @Column
    private boolean enabled;

    public User() {
    }

    public User(String username, String displayName, String password, String telephone,
                String email, LocalDate birthdate, int roles, int gender, boolean enabled) {
        this.username = username;
        this.displayName = displayName;
        this.password = password;
        this.telephone = telephone;
        this.email = email;
        this.birthdate = birthdate;
        this.roles = roles;
        this.gender = gender;
        this.enabled = enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(UserRole.values()) //iterate through all possible UserRoles
                .filter(this::hasRole) //leave only those one, which belongs to user
                .map(userRole -> new SimpleGrantedAuthority(userRole.toString())) //map them to authorities
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    //todo: make private?
    public boolean hasRole(UserRole userRole) {
        return (this.roles & userRole.getId()) != 0; //apply bit map to check existence of role
    }

    public void addRole(UserRole userRole) {
        this.roles |= userRole.getId();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public int getRoles() {
        return roles;
    }

    public void setRoles(int roles) {
        this.roles = roles;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setLogin(String login) {
        if (login.contains("@")) {
            this.email = login;
        } else {
            this.telephone = login;
        }
    }

    public void emptyLogin() {
        this.email = null;
        this.telephone = null;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean hasLogin(String login) {
        if (login.contains("@")) {
            return login.equals(this.email);
        } else {
            return login.equals(this.telephone);
        }
    }

    public String getLogin() {
        return email == null ? telephone : email;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        if (username != null ? !username.equals(user.username) : user.username != null) return false;
        if (telephone != null ? !telephone.equals(user.telephone) : user.telephone != null) return false;
        return email != null ? email.equals(user.email) : user.email == null;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (telephone != null ? telephone.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("id='").append(id).append('\'');
        sb.append(", username='").append(username).append('\'');
        sb.append(", displayName='").append(displayName).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", telephone='").append(telephone).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", birthdate=").append(birthdate);
        sb.append(", roles=").append(roles);
        sb.append(", gender=").append(gender);
        sb.append(", enabled=").append(enabled);
        sb.append('}');
        return sb.toString();
    }
}