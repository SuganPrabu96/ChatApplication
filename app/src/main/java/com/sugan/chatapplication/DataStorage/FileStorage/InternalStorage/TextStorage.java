package com.sugan.chatapplication.DataStorage.FileStorage.InternalStorage;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by sugan on 20/11/15.
 */

// No need to add any permission for this class

public class TextStorage {

    public boolean saveText(Context context, String fileName, JSONObject fileObj){

        File file = new File(context.getFilesDir(), fileName);

        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(file.getPath(), Context.MODE_PRIVATE);
            outputStream.write(fileObj.toString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }

    public JSONObject readText(Context context, String fileName){

        File file = new File(context.getFilesDir(), fileName);

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        try {
            return new JSONObject(text.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

}
