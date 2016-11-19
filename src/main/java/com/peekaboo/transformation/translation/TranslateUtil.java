package com.peekaboo.transformation.translation;


import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.peekaboo.transformation.model.entity.enums.Language;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class TranslateUtil {

    private String YANDEX_API_KEY = "trnsl.1.1.20161029T142217Z.fc9adeb623f1cd44.630bacbf0a3db33920c2358fb2b9891dcb4d5340";

    public TranslateUtil() {
    }

    public TranslateUtil(String YANDEX_API_KEY) {
        this.YANDEX_API_KEY = YANDEX_API_KEY;
    }

    public Language detect(String text) throws IOException {
        String urlStr = "https://translate.yandex.net/api/v1.5/tr.json/detect?key=" + YANDEX_API_KEY;
        URL url = new URL(urlStr);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod(RequestMethod.POST.name());
        connection.setDoOutput(true);
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

        outputStream.writeBytes("text=" + URLEncoder.encode(text, "UTF-8"));
        InputStream response = connection.getInputStream();
        String json = new Scanner(response).nextLine();

        String detected = new JsonParser().parse(json).getAsJsonObject().get("lang").getAsString();

        Language result;
        if (detected.equals("ru")) {
            result = Language.RUSSIAN;
        } else if (detected.equals("en")) {
            result = Language.ENGLISH;
        } else if (detected.equals("fr")) {
            result = Language.FRENCH;
        } else if (detected.equals("es")) {
            result = Language.SPANISH;
        } else {
            result = null;
        }

        return result;
    }

    public String translate(String text, Language target) throws IOException {
        String urlStr = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=" + YANDEX_API_KEY;
        URL url = new URL(urlStr);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod(RequestMethod.POST.name());
        connection.setDoOutput(true);
        DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());

        dataOutputStream.writeBytes("text=" + URLEncoder.encode(text, "UTF-8") + "&lang=" + target.name());

        InputStream response = connection.getInputStream();

        String json = new Scanner(response).nextLine();

        JsonParser parser = new JsonParser();

        JsonElement element = parser.parse(json);

        String translated = element.getAsJsonObject().get("text").getAsString();

        return translated;

    }

    public String getLangList(Language ui) throws IOException {
        String strUrl = "https://translate.yandex.net/api/v1.5/tr.json/getLangs?key=" + YANDEX_API_KEY;
        URL url = new URL(strUrl);

        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod(RequestMethod.POST.name());
        connection.setDoOutput(true);
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

        outputStream.writeBytes("&ui=" + ui.name());

        InputStream response = connection.getInputStream();

        String json = new Scanner(response).nextLine();
        String list = new JsonParser().parse(json).getAsJsonObject().get("langs").toString();

        return list;
    }

}
