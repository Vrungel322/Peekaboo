package com.peekaboo.model.entity.enums;

/**
 * Created by rtwnk on 8/26/16.
 */
public enum UserState {
    TEXT(1), AUDIO(2), VIDEO(3), ALL(0);
    private final int id;

    UserState(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public String getName(){
        if (id==0) return "all";
        if (id==1) return "text";
        if (id==2) return "audio";
        if (id==3) return "video";
        return "all";
    }
}
