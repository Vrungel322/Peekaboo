package com.peekaboo.transformation;

/**
 * Created by Oleksii on 06.09.2016.
 */

public class MultimediaInfo {
    private String format = null;
    private long duration = -1L;
    private AudioInfo audio = null;


    public MultimediaInfo() {
    }

    public String getFormat() {
        return this.format;
    }

    void setFormat(String format) {
        this.format = format;
    }

    public long getDuration() {
        return this.duration;
    }

    void setDuration(long duration) {
        this.duration = duration;
    }

    public AudioInfo getAudio() {
        return this.audio;
    }

    void setAudio(AudioInfo audio) {
        this.audio = audio;
    }



    public String toString() {
        return this.getClass().getName() + " (format=" + this.format + ", duration=" + this.duration + ", video="  + ", audio=" + this.audio + ")";
    }
}

