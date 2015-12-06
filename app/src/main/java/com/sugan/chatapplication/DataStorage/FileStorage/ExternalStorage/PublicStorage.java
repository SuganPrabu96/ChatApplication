package com.sugan.chatapplication.DataStorage.FileStorage.ExternalStorage;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by sugan on 20/11/15.
 */
public class PublicStorage {

    final static String LOG_TAG = "PublicStorage";
    final static String APP_NAME = "ChatApplication";

    public static File getFileStorageDir(JSONObject details) throws JSONException {
        // Get the directory for the user's public pictures directory.
        String fileName = details.getString("name");
        String fileType = details.getString("type");
        File file = null;
        if(isExternalStorageWritable()) {
            File directory = new File(Environment.getExternalStorageDirectory(), APP_NAME);
            File subDir = new File(directory, fileType + File.separator + "Sent");
            if (!subDir.mkdirs()) {
                Log.e(LOG_TAG, "File not created");
            }
            file = new File(subDir, fileName);
        }
        return file;
    }

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    private static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

}
