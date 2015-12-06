package com.sugan.chatapplication.util;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by sugan on 6/12/15.
 */
public class SharedPrefsAccess {

    private static final String PREFS_NAME = "chat_app";

    /*
        Params : Context and key to be retrieved from shared prefs
        Functioning : Gets value stored in shared prefs with key passed in arguments
        Return : Returns the value
    */

    public static String retrieveData(Context context, String key){

        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        return(prefs.getString(key, ""));

    }

    /*
        Params : Context and a json object.
        Functioning : The json object must contain key value pairs of the fields to be stored in shared preferences.
                      All values are converted to strings for storage
        Return : Boolean indicating task accomplishment status;
    */

    public static boolean setData(Context context, JSONObject keyValuePairs){

        Boolean success = true;

        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();

        Iterator<String> iterator = keyValuePairs.keys();

        while(iterator.hasNext()){
            String key = iterator.next();
            try{
                Object value = keyValuePairs.get(key);
                editor.putString(key, value.toString()).apply();
            } catch (JSONException e) {
                e.printStackTrace();
                success = false;
                break;
            }
        }

        return success;

    }

}
