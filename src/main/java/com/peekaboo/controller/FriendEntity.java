package com.peekaboo.controller;

import java.io.Serializable;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by Oleksii on 19.09.2016.
 */
@JsonAutoDetect
public class FriendEntity implements Serializable {

    private String name;
    private Long id;

    public FriendEntity(String name, Long id){
        this.name=name;
        this.id=id;
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
