package com.peekaboo.model.entity.enums;


public enum FileType {

    AUDIO("audio/wav"), IMAGE("image/jpeg"), DOCUMENT("text/plane"), VIDEO("");
    private final String name;

    FileType(String name) {
        this.name = name;
    }
}

