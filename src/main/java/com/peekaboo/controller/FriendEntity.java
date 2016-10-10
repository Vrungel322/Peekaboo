package com.peekaboo.controller;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by Oleksii on 19.09.2016.
 */
@JsonAutoDetect
public class FriendEntity implements Serializable {

    private Long id;
    private String name;
    private String surname;
    private String nickname;
    private int state;
    private String imgUri;

    public FriendEntity(Long id, String name, String surname, String nickname, int state) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.nickname = nickname;
        this.state = state;

    }

    @JsonProperty
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @JsonProperty
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @JsonProperty
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }


    @JsonProperty
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
