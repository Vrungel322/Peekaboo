package com.peekaboo.transformation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Oleksii on 18.08.2016.
 */
public interface TextToAudioInterface {
    public InputStream RunService(String text, SpeechRecognitionOptions options) throws IOException;
    public List<String> LoadAudioList();
    public List<String> LoadLanguageList();
    public List<String> LoadVoiceList(String language);
    public InputStream RunServiceWithDefaults(String text) throws IOException;
}
