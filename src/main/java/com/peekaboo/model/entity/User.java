package com.peekaboo.model.entity;

import org.neo4j.graphdb.Node;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NodeEntity
public class User implements UserDetails {

    @GraphId
    private Long id;

    @Index(unique = true)
    private String username;

    private String name;

    private String password;

    @Index(unique = true)
    private String telephone;

    @Index(unique = true)
    private String email;

    private int roles;

    //TODO: male = 0 , female = 1
    private int gender;

    private boolean enabled;

    @Relationship(type = "FRIENDS", direction = Relationship.DIRECTION)
    private List<User> friends = new ArrayList<>();

    @Relationship(type = "OWNS", direction = Relationship.DIRECTION)
    private Storage storage;

    @Relationship(type = "USE", direction = Relationship.DIRECTION)
    private List<Storage> storages = new ArrayList<>();

    public User() {}

    public User(String username, String name, String password, String telephone, String email, int roles, int gender, boolean enabled
                /*LocalDate birthdate,*/) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.telephone = telephone;
        this.email = email;
        this.roles = roles;
        this.gender = gender;
        this.enabled = enabled;
        this.friends = new ArrayList<>();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorities = new ArrayList<>();
        for (UserRole role : UserRole.values())
            if (this.hasRole(role)) {
                authorities.add(new SimpleGrantedAuthority(role.toString()));
            }
        return authorities;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
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

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
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

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public List<Storage> getStorages() {
        return storages;
    }

    public void setStorages(List<Storage> storages) {
        this.storages = storages;
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
        sb.append(", name='").append(name).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", telephone='").append(telephone).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", roles=").append(roles);
        sb.append(", gender=").append(gender);
        sb.append(", enabled=").append(enabled);
        sb.append('}');
        return sb.toString();
    }
}


