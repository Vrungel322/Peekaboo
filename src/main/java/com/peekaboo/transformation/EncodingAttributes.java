package com.peekaboo.transformation;

/**
 * Created by Oleksii on 06.09.2016.
 */

import java.io.Serializable;

public class EncodingAttributes implements Serializable {
    private static final long serialVersionUID = 1L;
    private String format = null;
    private Float offset = null;
    private Float duration = null;
    private AudioAttributes audioAttributes = null;


    public EncodingAttributes() {
    }

    String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    Float getOffset() {
        return this.offset;
    }

    public void setOffset(Float offset) {
        this.offset = offset;
    }

    Float getDuration() {
        return this.duration;
    }

    public void setDuration(Float duration) {
        this.duration = duration;
    }

    AudioAttributes getAudioAttributes() {
        return this.audioAttributes;
    }

    public void setAudioAttributes(AudioAttributes audioAttributes) {
        this.audioAttributes = audioAttributes;
    }


    public String toString() {
        return this.getClass().getName() + "(format=" + this.format + ", offset=" + this.offset + ", duration=" + this.duration + ", audioAttributes=" + this.audioAttributes + ", videoAttributes="  + ")";
    }
}
