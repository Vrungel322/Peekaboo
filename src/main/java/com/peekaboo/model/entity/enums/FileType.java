package com.peekaboo.model.entity.enums;


public enum FileType {

    AUDIO("audio/wav"), IMAGE("image/jpeg"), DOCUMENT("text/plain"), VIDEO("");

    private String type;

    FileType(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }
}

