package com.peekaboo.model.entity.enums;

public enum UserGender {

    MALE(0), FEMALE(1);

    private final int id;

    UserGender(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
