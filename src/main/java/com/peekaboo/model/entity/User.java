package com.peekaboo.model.entity;

import com.peekaboo.model.entity.enums.UserRole;
import com.peekaboo.model.entity.relations.Friendship;
import com.peekaboo.model.entity.relations.PendingFriendship;
import com.peekaboo.model.entity.relations.PendingMessages;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@NodeEntity
public class User implements UserDetails {

    @GraphId
    private Long id;
    private String username;
    private String telephone;
    private String email;
    private String name;
    private String surname;
    private String password;
    private String country;
    private String city;
    private int roles;
    private int state;
    //TODO: male = 0 , female = 1
    private int gender;
    private boolean enabled;
    @Relationship(type = "PROFILE_PHOTO", direction = Relationship.DIRECTION)
    private Storage profilePhoto;
    @Relationship(type = "FRIENDS", direction = Relationship.DIRECTION)
    private Set<Friendship> friends = new HashSet<>();
    @Relationship(type = "PENDING_MESSAGES", direction = Relationship.DIRECTION)
    private Set<PendingMessages> pendingMessages = new HashSet<>();
    @Relationship(type = "OWNS", direction = Relationship.DIRECTION)
    private List<Storage> ownStorages = new ArrayList<>();
    @Relationship(type = "USES", direction = Relationship.DIRECTION)
    private List<Storage> usesStorages = new ArrayList<>();
    @Relationship(type = "REQUEST_FRIENDSHIP", direction = Relationship.UNDIRECTED)
    private Set<PendingFriendship> requestFriends = new HashSet<>();

    public User() {
    }

    public User(String username, String name, String surname, String password, String telephone,
                String email, int roles, int gender, boolean enabled, int state
                /*LocalDate birthdate,*/) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.telephone = telephone;
        this.email = email;
        this.roles = roles;
        this.gender = gender;
        this.enabled = enabled;
        this.friends = new HashSet<>();
        this.state = state;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

    public Set<Friendship> getFriends() {
        return friends;
    }

    public void setFriends(Set<Friendship> friends) {
        this.friends = friends;
    }

    public void emptyLogin() {
        this.email = null;
        this.telephone = null;
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
        return telephone;
    }

    public void setLogin(String login) {
        if (login.contains("@")) {
            this.email = login;
        } else {
            this.telephone = login;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<Storage> getOwnStorages() {
        return ownStorages;
    }

    public void setOwnStorages(List<Storage> ownStorages) {
        this.ownStorages = ownStorages;
    }

    public List<Storage> getUsesStorages() {
        return usesStorages;
    }

    public void setUsesStorages(List<Storage> usesStorages) {
        this.usesStorages = usesStorages;
    }

    public void addOwnStorages(List<Storage> ownStorages) {
        this.ownStorages.addAll(ownStorages);
    }

    public void addUsesStorages(List<Storage> usesStorages) {
        this.usesStorages.addAll(usesStorages);
    }

    public Set<PendingMessages> getPendingMessages() {
        return pendingMessages;
    }

    public void setPendingMessages(Set<PendingMessages> pendingMessages) {
        this.pendingMessages = pendingMessages;
    }

    public Set<PendingFriendship> getRequestFriends() {
        return requestFriends;
    }

    public void setRequestFriends(Set<PendingFriendship> requestFriends) {
        this.requestFriends = requestFriends;
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
        sb.append("id=").append(id);
        sb.append(", username='").append(username).append('\'');
        sb.append(", telephone='").append(telephone).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", surname='").append(surname).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", country='").append(country).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append(", roles=").append(roles);
        sb.append(", state=").append(state);
        sb.append(", gender=").append(gender);
        sb.append(", enabled=").append(enabled);
        sb.append('}');
        return sb.toString();
    }

    public boolean wantsToSendMessages(String username) {
        HashSet<PendingMessages> pendings = (HashSet<PendingMessages>) this.getPendingMessages();
        for (PendingMessages pending : pendings) {
            if (pending.pendingTo(username)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getPendingMessagesFor(String username) {
        return this.getPendingMessages().stream().
                filter(m -> (m.getUserto().getUsername().equals(username))).findFirst().get().getMessages();
    }

    public Storage getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(Storage profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
}

