package com.peekaboo.transformation;

import com.peekaboo.transformation.SpeechRecognitionOptions;
import com.peekaboo.transformation.TextToAudioInterface;
import com.peekaboo.transformation.model.entity.enums.Language;
import com.peekaboo.transformation.translation.TranslateUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

public class TextToAudioYandex implements TextToAudioInterface {
    private static final String API_KEY = "2b1fb32b-5078-4ce1-bc3d-5c4c49acb3b2";
    private static final String BASE_URL = "https://tts.voicetech.yandex.net/generate";

    public InputStream RunService(String text, SpeechRecognitionOptions options) throws IOException {
        InputStream inputStream;

        if (!LoadLanguageList().contains(options.getLanguageOption())) {
            throw new IllegalArgumentException("Invalid Language for Speech Recognition is set");
        }

        if (!LoadVoiceList(options.getLanguageOption()).contains(options.getModelOption())) {
            throw new IllegalArgumentException("Invalid Voice for Speech Recognition is set");
        }

        if (!LoadAudioList().contains(options.getAudioType())) {
            throw new IllegalArgumentException("Invalid Audio type for Speech Recognition is set" + options.getAudioType());
        }

        if (options.getLanguageOption().equals("auto")) {
            TranslateUtil translate = new TranslateUtil();
            Language lang = translate.detect(text);
            if (lang == Language.RUSSIAN) {
                options.setLanguageOption("ru-RU");
            } else if (lang == Language.ENGLISH) {
                options.setLanguageOption("en-US");
            } else {
                options.setLanguageOption("en-US");
            }
//            System.out.println(options.getLanguageOption());
        }

        String postURL = BASE_URL + "?&key=" + API_KEY +
                "&text=" + URLEncoder.encode(text, "UTF-8") +
                "&lang=" + options.getLanguageOption() +
                "&speaker=" + options.getModelOption() +
                "&format=" + options.getAudioType();


        HttpGet getRequest = new HttpGet(postURL);


        HttpClient client = HttpClientBuilder.create().build();

        HttpResponse response = client.execute(getRequest);

//        BufferedReader rd = new BufferedReader(
//                new InputStreamReader(response.getEntity().getContent()));
//        StringBuffer result = new StringBuffer();
//
//        String line = "";
//        while ((line = rd.readLine()) != null) {
//            result.append(line);
//        }
//        System.out.println(result);

        return response.getEntity().getContent();
    }

    public InputStream RunServiceWithDefaults(String text) throws IOException {
        SpeechRecognitionOptions options = new SpeechRecognitionOptions();
        options.setAudioType("wav");
        options.setLanguageOption("auto");
        options.setModelOption("oksana");
        return RunService(text, options);
    }

    public List<String> LoadAudioList() {
        List<String> list = new LinkedList<String>();
        list.add("wav");
        list.add("mp3");
        return list;
    }

    public List<String> LoadLanguageList() {
        List<String> list = new LinkedList<String>();
        list.add("ru-RU");
        list.add("en-US");
        list.add("auto");
        return list;
    }

    public List<String> LoadVoiceList(String lang) {
        List <String> list = new LinkedList<String>();
        list.add("jane");
        list.add("oksana");
        list.add("alyss");
        list.add("omazh");
        list.add("zahar");
        list.add("ermil");
        return list;

    }

}
