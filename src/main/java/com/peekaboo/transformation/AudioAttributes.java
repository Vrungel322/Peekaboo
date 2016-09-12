package com.peekaboo.transformation;

import java.io.Serializable;

public class AudioAttributes implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String DIRECT_STREAM_COPY = "copy";
    private String codec = null;
    private Integer bitRate = null;
    private Integer samplingRate = null;
    private Integer channels = null;
    private Integer volume = null;

    public AudioAttributes() {
    }

    String getCodec() {
        return this.codec;
    }

    public void setCodec(String codec) {
        this.codec = codec;
    }

    Integer getBitRate() {
        return this.bitRate;
    }

    public void setBitRate(Integer bitRate) {
        this.bitRate = bitRate;
    }

    Integer getSamplingRate() {
        return this.samplingRate;
    }

    public void setSamplingRate(Integer samplingRate) {
        this.samplingRate = samplingRate;
    }

    Integer getChannels() {
        return this.channels;
    }

    public void setChannels(Integer channels) {
        this.channels = channels;
    }

    Integer getVolume() {
        return this.volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public String toString() {
        return this.getClass().getName() + "(codec=" + this.codec + ", bitRate=" + this.bitRate + ", samplingRate=" + this.samplingRate + ", channels=" + this.channels + ", volume=" + this.volume + ")";
    }
}