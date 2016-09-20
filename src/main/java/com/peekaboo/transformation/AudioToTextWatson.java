package com.peekaboo.transformation;

import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechModel;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechWordConfidence;
import com.peekaboo.miscellaneous.JavaPropertiesParser;
import org.apache.commons.lang3.text.WordUtils;

import java.io.File;
import java.util.*;

/**
 * Created by Oleksii on 05.08.2016.
 */
public class AudioToTextWatson extends SpeechToText implements AudioToTextInterface {
    private String messageText;
    private boolean generated;
    private double confidenceValue;

    public AudioToTextWatson() {
        this.setUsernameAndPassword(JavaPropertiesParser.PARSER.getValue("WatsonAudioToTextLogin"), JavaPropertiesParser.PARSER.getValue("WatsonAudioToTextPassword"));
    }

    @Override
    public String RunService(File file, SpeechRecognitionOptions options) {
        if (!LoadLanguageList().contains(options.getLanguageOption())) {
            throw new IllegalArgumentException("Invalid Language for Speech Recognition is set");
        }

        if (!LoadModelList().contains(options.getModelOption())) {
            throw new IllegalArgumentException("Invalid Model for Speech Recognition is set");
        }

        if (!LoadAudioList().contains(options.getAudioType())) {
            throw new IllegalArgumentException("Invalid Audio type for Speech Recognition is set" + options.getAudioType());
        }

        RecognizeOptions.Builder build = new RecognizeOptions.Builder();
        build.contentType(options.getAudioType())
                .model(options.getLanguageOption() + "_" + options.getModelOption())
                .contentType(options.getAudioType())
                .wordConfidence(true);
        if ((options.getThresholdOption() < 1) && (options.getThresholdOption() > 0)) {
            build.wordAlternativesThreshold(options.getThresholdOption());
        } else {
            throw new IllegalArgumentException("Invalid threshold for Speech Recognition is set" + options.getThresholdOption());
        }

        RecognizeOptions recognizeOptions = build.build();

        String result = new String();
        StringBuilder w = new StringBuilder(result);
        SpeechResults results = this.recognize(file, recognizeOptions).execute();
        Double confidenceLimit = recognizeOptions.wordAlternativesThreshold();
        boolean first = true;
        for (SpeechWordConfidence word : results.getResults().get(0).getAlternatives().get(0).getWordConfidences()) {
            if (!first) {
                if (word.getConfidence() >= confidenceLimit) {
                    w.append(word.getWord() + " ");
                } else {
                    w.append("<u>" + word.getWord() + "</u>" + " ");
                }

            }
            if (first) {
                if (word.getConfidence() >= confidenceLimit) {
                    w.append(WordUtils.capitalize(word.getWord()) + " ");
                } else {
                    w.append("<u>" + WordUtils.capitalize(word.getWord()) + "</u>" + " ");
                }
                first = false;
            }
        }
        w.append(".");
        return w.toString();
    }

    @Override
    public String RunServiceWithDefaults(File file) {
        SpeechRecognitionOptions options = new SpeechRecognitionOptions();
        options.setAudioType(JavaPropertiesParser.PARSER.getValue("WatsonAudioToTextDefaultAudioType"));
        options.setLanguageOption(JavaPropertiesParser.PARSER.getValue("WatsonAudioToTextDefaultWatsonlanguageOption"));
        options.setModelOption(JavaPropertiesParser.PARSER.getValue("WatsonAudioToTextDefaultModelOption"));
        options.setThresholdOption(Double.parseDouble(JavaPropertiesParser.PARSER.getValue("WatsonAudioToTextDefaultThresholdOption")));
        return this.RunService(file, options);
    }

    public List<String> LoadAudioList() {
        List<String> list = new ArrayList<>();
        list.add("audio/flac");
        list.add("audio/l16");
        list.add("audio/wav");
        list.add("audio/ogg");
        list.add("audio/basic");
        return list;
    }

    public List<String> LoadLanguageList() {
        List<String> list = new ArrayList<>();
        for (SpeechModel model : this.getModels().execute()) {
            list.add(model.getName().substring(0, 5));
        }
        return list;
    }

    public List<String> LoadModelList() {
        List<String> list = new LinkedList<>();
        list.add("BroadbandModel");
        list.add("NarrowbandModel");
        return list;
    }


}