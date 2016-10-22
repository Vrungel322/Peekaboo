package com.peekaboo.transformation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project ImageReader
 * Created by igor, 21.10.16 17:09
 */


public class ImageReader {
    private static final String BASE_URL = "https://api.projectoxford.ai/vision/v1.0/analyze";
    private static final String API_KEY = "1dba2fac3d5f4516933f8ded74e28bd2";

    private static final Logger logger = LogManager.getLogger(ImageReader.class);

    private File file;

    public ImageReader(File imageFile) {
        logger.debug("ImageReader is created");
        this.file = imageFile;
    }

    public String detect() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        //params.add(new BasicNameValuePair("subscription-key", ImageReader.API_KEY));
        params.add(new BasicNameValuePair("visualFeatures", "Description,Tags"));

        try {
            HttpPost post = new HttpPost(BASE_URL + "?visualFeatures=Description,Tags");
            post.addHeader("Content-Type", "application/octet-stream");
            post.addHeader("User-Agent", "Mozilla");
            post.addHeader("Ocp-Apim-Subscription-Key", ImageReader.API_KEY);
//            builder.add(new UrlEncodedFormEntity(params));
            post.setEntity(new FileEntity(file));
//            builder.addPart("data", new FileBody(file));
//            post.setEntity(builder.build());

            HttpClient client = HttpClientBuilder.create().build();
            logger.debug("Make request");
            HttpResponse response = client.execute(post);
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            HashMap<String,Map> res =
                    new ObjectMapper().readValue(result.toString(), HashMap.class);
            logger.error(res.toString());
            String r=((ArrayList<Map>)res.get("description").get("captions")).get(0).get("text").toString();
            logger.error(r);
            return r;
        } catch (Exception e) {
            e.printStackTrace();;
        }
return "";
    }

}
