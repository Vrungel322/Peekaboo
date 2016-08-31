package com.peekaboo.model.entity.enums;

/**
 * Created by rtwnk on 8/26/16.
 */
public enum UserState {
    TEXT(1), AUDIO(2), VIDEO(3), ALL(4);
    private final int id;

    UserState(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
