package com.peekaboo.controller.confirmation;

import javax.validation.constraints.NotNull;

public class ConfirmationRequestEntity {
    @NotNull
    private String id;

    @NotNull
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
}
