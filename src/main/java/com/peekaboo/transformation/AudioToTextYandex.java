package com.peekaboo.transformation;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class AudioToTextYandex implements AudioToTextInterface {
    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(AudioToTextYandex.class);

    private static final String API_KEY = "2b1fb32b-5078-4ce1-bc3d-5c4c49acb3b2";
    private static final String BASE_URL = "http://asr.yandex.net/asr_xml";

    public AudioToTextYandex() {
    }

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

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");

            StringBuffer hexString = new StringBuffer();

            byte[] uuid = messageDigest.digest( (String.valueOf(new Date().getTime()) + String.valueOf(Math.random() * 10000)).getBytes());

            for (int i=0; i<uuid.length; i++){
                hexString.append(Integer.toHexString( (uuid[i]>> 4) & 0x0F ) );
                hexString.append(Integer.toHexString( uuid[i] & 0x0F ) );
            }
            System.out.println(hexString.toString());

            logger.debug("Random UUId = " + hexString);

            String postURL = BASE_URL + "?uuid=" + hexString +
                    "&key=" + API_KEY +
                    "&topic=" + options.getModelOption() +
                    "&lang=" + options.getLanguageOption();

            HttpPost post = new HttpPost(postURL);

            post.addHeader("Content-Type", options.getAudioType());
            post.setEntity(new FileEntity(file));

            HttpClient client = HttpClientBuilder.create().build();

            HttpResponse response = client.execute(post);

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();

            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            System.out.println(result);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.parse(new InputSource(new ByteArrayInputStream(result.toString().getBytes("utf-8"))));

            NodeList results = doc.getElementsByTagName("recognitionResults");

            if (results.getLength() > 0) {
                NamedNodeMap nodeMapResults = results.item(0).getAttributes();
                if (nodeMapResults.getLength() > 0 && Integer.valueOf(nodeMapResults.getNamedItem("success").getNodeValue()) == 1) {
                    NodeList variants = ((Element) results.item(0)).getElementsByTagName("variant");
                    if (variants.getLength() > 0) {
                        double maxConfidence = 0., confidence = 0.;
                        int index = -1;
                        for (int i = 0; i < variants.getLength(); i++) {
                            NamedNodeMap nodeMapVariant = variants.item(i).getAttributes();
                            confidence = Double.valueOf(nodeMapVariant.getNamedItem("confidence").getNodeValue());
                            if (Double.compare(maxConfidence, confidence) <= 0) {
                                maxConfidence = confidence;
                                index = i;
                            }
                        }

                        return variants.item(index).getTextContent();
                    }
                }
            }
        } catch (NoSuchAlgorithmException exception) {
            logger.error("Alghorithm MD5 is not found");
        } catch (ParserConfigurationException ex) {
            logger.error("Parser Configuration error");
        } catch (SAXException ex) {
            logger.error("SAX Exception " + ex.getMessage());
        } catch (IOException ex) {
            logger.error("IOException " + ex.getMessage());
        }
        return "";
    }

    public String RunServiceWithDefaults(File file) {
        SpeechRecognitionOptions options = new SpeechRecognitionOptions();
        options.setAudioType("audio/x-wav");
        options.setLanguageOption("ru-RU");
        options.setModelOption("queries");
        return this.RunService(file, options);
    }

    public List<String> LoadAudioList() {
        List<String> list = new ArrayList<String>();
        list.add("audio/x-wav");
        list.add("audio/x-mpeg-3");
        list.add("audio/x-speex");
        list.add("audio/ogg;codecs=opus");
        list.add("audio/webm;codecs=opus");
        list.add("audio/x-pcm;bit=16;rate=16000");
        list.add("audio/x-pcm;bit=16;rate=8000");
        list.add("audio/x-alaw;bit=13;rate=8000");
        return list;
    }

    public List<String> LoadLanguageList() {
        List<String> list = new ArrayList<String>();
        list.add("ru-RU");
        list.add("en-US");
        list.add("tr-TR");
        return list;
    }

    public List<String> LoadModelList() {
        List<String> list = new LinkedList<String>();
        list.add("queries");
        list.add("maps");
        return list;
    }


}