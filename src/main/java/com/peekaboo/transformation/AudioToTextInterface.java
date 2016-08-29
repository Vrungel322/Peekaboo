package com.peekaboo.transformation;

import java.io.File;
import java.util.List;

/**
 * Created by Oleksii on 05.08.2016.
 */
public interface AudioToTextInterface {
    public String RunService(File file, SpeechRecognitionOptions options);
    public String RunServiceWithDefaults(File file);
    public List<String> LoadAudioList();
    public List<String> LoadLanguageList();
    public List<String> LoadModelList();

}
