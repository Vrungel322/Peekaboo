package com.peekaboo.controller.confirmation;

public class ConfirmationRequestEntity {

    private String id;

    private String key;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "ConfirmationRequestEntity{" +
                "id='" + id + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
