package com.peekaboo.model.entity;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class VerificationToken {

    @GraphId
    private String id;

    private String value;

    private User user;

    public VerificationToken() {
    }

    public VerificationToken(String token, User user) {
        this.value = token;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String token) {
        this.value = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        VerificationToken that = (VerificationToken) obj;
        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("VerificationToken{");
        sb.append("id='").append(id).append('\'');
        sb.append(", token='").append(value).append('\'');
        sb.append(", user=").append(user);
        sb.append('}');
        return sb.toString();
    }
}
