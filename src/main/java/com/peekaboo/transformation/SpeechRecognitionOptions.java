package com.peekaboo.transformation;

/**
 * Created by Oleksii on 05.08.2016.
 */
public class SpeechRecognitionOptions {
    private String audioType;
    private String languageOption;
    private String modelOption;
    private Double thresholdOption;

    public String getAudioType() {
        return audioType;
    }

    public SpeechRecognitionOptions setAudioType(String audioType) {

        this.audioType = audioType;
        return this;
    }

    public String getLanguageOption() {
        return languageOption;
    }

    public SpeechRecognitionOptions setLanguageOption(String languageOption) {

        this.languageOption = languageOption;
        return this;
    }

    public String getModelOption() {
        return modelOption;
    }

    public SpeechRecognitionOptions setModelOption(String modelOption)
    {
        this.modelOption = modelOption;
        return this;
    }

    public Double getThresholdOption() {
        return thresholdOption;
    }

    public SpeechRecognitionOptions setThresholdOption(Double thresholdOption) {
        this.thresholdOption = thresholdOption;
        return this;
    }


}
