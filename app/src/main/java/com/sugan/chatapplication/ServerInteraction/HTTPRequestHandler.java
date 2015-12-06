package com.sugan.chatapplication.ServerInteraction;

import android.os.Build;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * Created by sugan on 12/11/15.
 */
public class HTTPRequestHandler {

    public final static int GET = 1;
    public final static int POST = 2;
    public final static int PUT = 3;
    public final static int DELETE = 4;

    public HTTPRequestHandler(){

    }

    private String getQueryString(JSONObject params){
        final char PARAMETER_DELIMITER = '&';
        final char PARAMETER_EQUALS_CHAR = '=';
        StringBuilder parameters = new StringBuilder();
        if (params != null) {
            boolean firstParameter = true;

            Iterator<String> iterator = params.keys();

            while(iterator.hasNext()){
                String key = iterator.next();
                try{
                    String value = String.valueOf(params.get(key));

                    if (!firstParameter) {
                        parameters.append(PARAMETER_DELIMITER);
                    }
                    parameters.append(key)
                        .append(PARAMETER_EQUALS_CHAR)
                        .append(URLEncoder.encode(value));
                    firstParameter = false;

                } catch (JSONException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
        return parameters.toString();
    }

    public String makeRequest(String url, int method) {
        return this.makeRequest(url, method, null);
    }

    public String makeRequest(String url, int method,
                                  JSONObject params) {

        final int CONNECTION_TIMEOUT = 30000;
        final int DATA_RETRIEVAL_TIMEOUT = 30000;

        String paramsString = getQueryString(params);
        HttpURLConnection urlConnection = null;

        try {
            final int SDK_VERSION = Build.VERSION.SDK_INT;

            if(method == GET){
                URL requestURL = new URL(url+"?"+paramsString);
                urlConnection = (HttpURLConnection) requestURL.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(false);
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                urlConnection.setReadTimeout(DATA_RETRIEVAL_TIMEOUT);
                urlConnection.setUseCaches(false);
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                if(SDK_VERSION!=Build.VERSION_CODES.JELLY_BEAN && SDK_VERSION!=Build.VERSION_CODES.JELLY_BEAN_MR1 && SDK_VERSION!=Build.VERSION_CODES.JELLY_BEAN_MR2)
                    urlConnection.setFixedLengthStreamingMode(paramsString.getBytes().length);
            }

            else if (method == POST) {
                URL requestURL = new URL(url);
                urlConnection = (HttpURLConnection) requestURL.openConnection();

                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestMethod("POST");

                urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                urlConnection.setReadTimeout(DATA_RETRIEVAL_TIMEOUT);
                urlConnection.setUseCaches(false);

                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setFixedLengthStreamingMode(paramsString.getBytes().length);

                if(SDK_VERSION== Build.VERSION_CODES.JELLY_BEAN || SDK_VERSION==Build.VERSION_CODES.JELLY_BEAN_MR1 || SDK_VERSION==Build.VERSION_CODES.JELLY_BEAN_MR2)
                    urlConnection.setRequestProperty("Connection","Close");
                else
                    urlConnection.setRequestProperty("Connection", "Keep-Alive");

                //Write parameters to stream
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(paramsString);
                writer.flush();
                writer.close();
            }

            if(urlConnection!=null && urlConnection.getResponseCode()==HttpURLConnection.HTTP_OK) {
                //Read response from stream
                InputStream is = urlConnection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                return response.toString();
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;

    }

}
