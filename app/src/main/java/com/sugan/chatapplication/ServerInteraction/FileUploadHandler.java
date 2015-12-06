package com.sugan.chatapplication.ServerInteraction;

import android.os.Build;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sugan on 13/11/15.
 */
public class FileUploadHandler {

    public FileUploadHandler(){

    }

    public JSONObject uploadFile(String url, File file, String fileName){
        return uploadFile(url,file,fileName,null);
    }

    public JSONObject uploadFile(String urlString, File sourceFile, String fileKey, String token){

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "------";
        int bytesRead, bytesAvailable, bufferSize;
        int serverResponseCode = 999;
        byte[] buffer;
        int maxBufferSize = 1024 * 1024;
        int status = 999;
        JSONObject json = new JSONObject();
        String fileName = sourceFile.getName();
        try {
            json.put("status", status);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!sourceFile.isFile()) {
            return json;
        }
        else {
            try {

                Long time = System.currentTimeMillis();
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(urlString);
                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setConnectTimeout(30000);
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                final int SDK_VERSION = Build.VERSION.SDK_INT;
                if(SDK_VERSION==Build.VERSION_CODES.JELLY_BEAN || SDK_VERSION==Build.VERSION_CODES.JELLY_BEAN_MR1 || SDK_VERSION==Build.VERSION_CODES.JELLY_BEAN_MR2)
                    conn.setRequestProperty("Connection","Close");
                else
                    conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("Authorization", "Bearer " + token);
                conn.setRequestProperty("file", fileKey);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data;name=\"" + fileKey + "\";" + "filename=\"" + sourceFile.getName() + "\"" +
                        lineEnd + "Content-Type: text/plain" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                json.put("status", serverResponseCode);

                if (serverResponseCode == 200) {
                    //Get Response
                    InputStream is = conn.getInputStream();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                    String line;
                    StringBuffer response = new StringBuffer();
                    while ((line = rd.readLine()) != null) {
                        response.append(line);
                        response.append('\r');
                    }
                    rd.close();
                    json.put("data", new JSONObject(response.toString()));
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (Exception e) {
                e.printStackTrace();

            }

            return json;

        }
    }


}
