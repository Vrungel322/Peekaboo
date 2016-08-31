package com.peekaboo.transformation;

import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.AudioFormat;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;
import com.ibm.watson.developer_cloud.text_to_speech.v1.util.WaveUtils;
import com.peekaboo.miscellaneous.PropertiesParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Oleksii on 18.08.2016.
 */
public class TextToAudioWatson implements TextToAudioInterface {
    private Logger logger = LogManager.getLogger(this);
    TextToSpeech service = new TextToSpeech();
    public TextToAudioWatson(){
        this.service = new TextToSpeech();
        this.service.setUsernameAndPassword(PropertiesParser.getValue("WatsonTextToAudioLogin" ), PropertiesParser.getValue("WatsonTextToAudioPassword"));
        this.service.setEndPoint(PropertiesParser.getValue("WatsonTextToAudioEndPoint"));
    }
    public InputStream RunService(String text, SpeechRecognitionOptions options) throws IOException {
        InputStream out;
        String voice = options.getLanguageOption() + "_" + options.getModelOption();
        String audioformat = options.getAudioType();
        InputStream stream = service.synthesize(text, Voice.getByName(voice), AudioFormat.valueOf(audioformat)).execute();
        out = WaveUtils.reWriteWaveHeader(stream);

        return out;
    }
    public List<String> LoadAudioList(){
        List <String> list=new LinkedList<>();
        list.add("wav");
        list.add("flac");
        list.add("ogg");
        return list;
    }
    public List<String> LoadLanguageList(){
        List <String> list=new LinkedList<>();
        list.add("de-DE");
        list.add("en-US");
        list.add("es-ES");
        list.add("fr-FR");
        list.add("en-GB");
        list.add("it-IT");
        list.add("es-US");
        return list;
    }
    public List<String> LoadVoiceList(String language){
        List <String> list=new LinkedList<>();
        switch (language) {
            case "de-DE":
                list.add("DieterVoice");
                list.add("BirgitVoice");
                break;
            case "en-US":
                list.add("AllisonVoice");
                list.add("LisaVoice");
                break;
            case "es-ES":
                list.add("EnriqueVoice");
                list.add("LauraVoice");
                break;
            case "es-US":
                list.add("SofiaVoice");
            case "fr-FR":
                list.add("ReneeVoice");
                break;
            case "en-GB":
                list.add("KateVoice");
                break;
            case "it-IT":
                list.add("FrancescaVoice");
                break;
        }
        return list;

    }
    public InputStream RunServiceWithDefaults(String text) throws IOException{
        InputStream out;
        InputStream stream = service.synthesize(text, Voice.EN_MICHAEL, AudioFormat.WAV).execute();
        out = WaveUtils.reWriteWaveHeader(stream);
        return out;
    }

}