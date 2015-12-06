package com.sugan.chatapplication.DataStorage.SQLiteDatabase;

import org.json.JSONObject;

/**
 * Created by Suganprabu on 23-06-2015.
 */
public class DatabaseItem {

    private JSONObject item;

    public DatabaseItem(JSONObject item){

        this.item = item;

    }

    public JSONObject getStoredItem(){
        return item;
    }

    public void setStoredItem(JSONObject item){
        this.item = item;
    }
}
