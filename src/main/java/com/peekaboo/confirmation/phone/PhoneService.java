package com.peekaboo.confirmation.phone;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class PhoneService {
    private static final Logger logger = LogManager.getLogger(PhoneService.class);

    public static final String BASE_URL = "https://rest.nexmo.com/sms/json";
    public static final String API_KEY = "65ad2b77";
    public static final String API_SECRET = "71788e97d572ffb0";
    public static final String DEFAULT_FROM = "Peekaboo";

    private HttpClient httpClient = null;

    public void send(String to, String body) {
        Map<String, String> requestParams = new LinkedHashMap<String, String>();
        requestParams.put("from", DEFAULT_FROM);
        requestParams.put("to", to);
        requestParams.put("text", body);

        String response;
        HttpPost method;
        try {
            method = this.getHttpPost(requestParams);
            if (this.httpClient == null)
                this.httpClient = HttpClientUtils.getInstance(5000, 30000).getNewHttpClient();
            HttpResponse httpResponse = this.httpClient.execute(method);
            int status = httpResponse.getStatusLine().getStatusCode();
            if (status != 200)
                throw new Exception("got a non-200 response [ " + status + " ] from Nexmo-HTTPS");
            response = new BasicResponseHandler().handleResponse(httpResponse);
            logger.info("HTTP Post response: " + response);
        } catch (Exception e) {
            logger.info("Communication fail: " + e);
//            method.abort();
        }
    }

    private HttpPost getHttpPost(Map<String, String> opts) throws Exception {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("api_key", this.API_KEY));
        params.add(new BasicNameValuePair("api_secret", this.API_SECRET));

        for (Map.Entry<String, String> entry: opts.entrySet())
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));

        HttpPost method = new HttpPost(this.BASE_URL);
        method.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        String url = this.BASE_URL + "?" + URLEncodedUtils.format(params, "utf-8");

        logger.info("Created HttpPost: " + url);

        return method;
    }

}

/**
 * HttpClientUtils.java<br><br>
 *
 * A Helper factory for instantiating HttpClient instances<br><br>
 *
 * Created on 5 January 2011, 17:34
 *
 * @author  Paul Cook
 * @version 1.0
 */
class HttpClientUtils {

    private final static Map<String, HttpClientUtils> instances = new HashMap<String, HttpClientUtils>();

    private final ThreadSafeClientConnManager threadSafeClientConnManager;

    private final int connectionTimeout;
    private final int soTimeout;

    private HttpClientUtils(int connectionTimeout, int soTimeout) {
        this.connectionTimeout = connectionTimeout;
        this.soTimeout = soTimeout;

        this.threadSafeClientConnManager = new ThreadSafeClientConnManager();
        this.threadSafeClientConnManager.setDefaultMaxPerRoute(200);
        this.threadSafeClientConnManager.setMaxTotal(200);
    }

    /**
     * Return an existing or instantiate a new HttpClient factory instance with explicitly specified connection and read timeout values
     *
     * @param connectionTimeout the timeout value in milliseconds to use when establishing a new http socket
     * @param soTimeout         the timeout value in milliseconds to wait for a http response before closing the socket
     * @return HttpClientUtils an instance of the HttpClient factory primed with the requested timeout values
     */
    public static HttpClientUtils getInstance(int connectionTimeout, int soTimeout) {
        String key = "c-" + connectionTimeout + "-so-" + soTimeout;
        HttpClientUtils instance = instances.get(key);
        if (instance == null) {
            instance = new HttpClientUtils(connectionTimeout, soTimeout);
            instances.put(key, instance);
        }
        return instance;
    }

    /**
     * Instantiate a new HttpClient instance that uses the timeout values associated with this factory instance
     *
     * @return HttpClient a new HttpClient instance
     */
    public HttpClient getNewHttpClient() {
        HttpParams httpClientParams = new BasicHttpParams();
        HttpProtocolParams.setUserAgent(httpClientParams, "Nexmo Java SDK 1.5");
        HttpProtocolParams.setContentCharset(httpClientParams, "UTF-8");
        HttpProtocolParams.setHttpElementCharset(httpClientParams, "UTF-8");
        HttpConnectionParams.setConnectionTimeout(httpClientParams, this.connectionTimeout);
        HttpConnectionParams.setSoTimeout(httpClientParams, this.soTimeout);
        HttpConnectionParams.setStaleCheckingEnabled(httpClientParams, true);
        HttpConnectionParams.setTcpNoDelay(httpClientParams, true);

        return new DefaultHttpClient(this.threadSafeClientConnManager, httpClientParams);
    }

}