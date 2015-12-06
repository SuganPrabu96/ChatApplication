package com.sugan.chatapplication.DataStorage.SQLiteDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by sugan on 4/11/15.
 */
public class DBStorageAccess extends SqlHandler {

    int tableIndex = -1;
    SQLiteDatabase db;

    public DBStorageAccess(Context context, int tableIndex) {
        super(context);
        this.tableIndex = tableIndex;
    }

    public void addItem(JSONObject object) throws ArrayIndexOutOfBoundsException{

        db = getWritableDatabase();

        db.beginTransaction();

        try {

            ContentValues values = new ContentValues();

            Iterator<String> iterator = object.keys();

            while(iterator.hasNext()){
                String key = iterator.next();
                try{
                    String value = object.get(key).toString();
                    values.put(key,value);
                    db.insert(SqlHandler.TABLE_NAMES[tableIndex], null, values);
                } catch (JSONException e) {
                    e.printStackTrace();
                    break;
                }
            }

            db.setTransactionSuccessful();

        }catch (SQLiteException e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
            db.close();
        }
    }

    public void addItem(JSONArray array) throws ArrayIndexOutOfBoundsException{

        db = getWritableDatabase();

        db.beginTransaction();

        try {

            ContentValues values = new ContentValues();

            int length = array.length();

            for(int i=0; i<length; i++) {

                JSONObject object = array.getJSONObject(i);

                Iterator<String> iterator = object.keys();

                while (iterator.hasNext()) {
                    String key = iterator.next();
                    try {
                        String value = object.get(key).toString();
                        values.put(key, value);
                        db.insert(SqlHandler.TABLE_NAMES[tableIndex], null, values);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        break;
                    }
                }

            }

            db.setTransactionSuccessful();

        }catch (SQLiteException e){
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            db.endTransaction();
            db.close();
        }
    }

    public JSONArray getItems() throws ArrayIndexOutOfBoundsException {

        JSONArray array = new JSONArray();

        db = getWritableDatabase();

        db.beginTransaction();

        try {
            String select = "SELECT * FROM " + SqlHandler.TABLE_NAMES[tableIndex];

            Cursor cursor = db.rawQuery(select, null);

            if (cursor.moveToFirst()) {
                do {
                    String key, value;

                    key = cursor.getString(0);
                    value = cursor.getString(1);

                    JSONObject obj = new JSONObject();
                    obj.put(key,value);

                    array.put(obj);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.setTransactionSuccessful();

        }catch (SQLiteException e){
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            db.endTransaction();
            db.close();
        }

        return array;
    }

    public JSONArray getItems(JSONObject object) throws ArrayIndexOutOfBoundsException {

        JSONArray array = new JSONArray();

        db = getWritableDatabase();

        db.beginTransaction();

        try {
            String select = "SELECT FROM " + SqlHandler.TABLE_NAMES[tableIndex];

            Iterator<String> iterator = object.keys();

            int count = 0;

            while (iterator.hasNext()) {
                String key = iterator.next();
                try {
                    String value = object.get(key).toString();
                    if (count == 0)
                        select += " WHERE " + key + " = \"" + value + "\"";
                    else
                        select += " AND " + key + " = \"" + value + "\"";
                    count++;
                } catch (JSONException e) {
                    e.printStackTrace();
                    break;
                }
            }

            Cursor cursor = db.rawQuery(select, null);

            if (cursor.moveToFirst()) {
                do {
                    String key, value;

                    key = cursor.getString(0);
                    value = cursor.getString(1);

                    JSONObject obj = new JSONObject();
                    obj.put(key, value);

                    array.put(obj);
                } while (cursor.moveToNext());
            }

            db.execSQL(select);
            db.setTransactionSuccessful();

        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            db.endTransaction();
            db.close();
        }

        return array;

    }

    public void updateItems(JSONArray array) throws ArrayIndexOutOfBoundsException{

        db = getWritableDatabase();

        db.beginTransaction();

        try {
            int length = array.length();

            for(int i=0; i<length; i++) {

                String updateString = "UPDATE " + SqlHandler.TABLE_NAMES[tableIndex];

                JSONObject object = array.getJSONObject(i);

                JSONObject originalData = object.getJSONObject("original");
                JSONObject newData = object.getJSONObject("new");

                updateString += " SET ";

                Iterator<String> iterNew = newData.keys();

                while (iterNew.hasNext()) {
                    String key = iterNew.next();
                    try {
                        String value = object.get(key).toString();
                        updateString += key + " = \"" + value + "\", ";
                    } catch (JSONException e) {
                        e.printStackTrace();
                        break;
                    }
                }

                updateString = updateString.substring(0,updateString.length()-2);

                updateString += " WHERE ";

                Iterator<String> iterOld = originalData.keys();

                while (iterOld.hasNext()) {
                    String key = iterOld.next();
                    try {
                        String value = object.get(key).toString();
                        updateString += key + " = \"" + value + "\"; ";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break; // Breaks after one where clause itself
                }

                db.execSQL(updateString);
                db.setTransactionSuccessful();

            }

        }catch (SQLiteException e){
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            db.endTransaction();
            db.close();
        }

    }

    public void deleteItems() throws ArrayIndexOutOfBoundsException{

        db = getWritableDatabase();
        db.beginTransaction();

        try {
            String deleteString = "DELETE FROM " + SqlHandler.TABLE_NAMES[tableIndex];

            db.execSQL(deleteString);
            db.setTransactionSuccessful();

        }catch (SQLiteException e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
            db.close();
        }
    }

    public void deleteItems(JSONObject object) throws ArrayIndexOutOfBoundsException{

        db = getWritableDatabase();

        try {
            String deleteString = "DELETE FROM " + SqlHandler.TABLE_NAMES[tableIndex];

            Iterator<String> iterator = object.keys();

            int count = 0;

            while (iterator.hasNext()) {
                String key = iterator.next();
                try {
                    String value = object.get(key).toString();
                    if(count==0)
                        deleteString += " WHERE " + key + " = \"" + value + "\"";
                    else
                        deleteString += " AND " + key + " = \"" + value + "\"";
                    count++;
                } catch (JSONException e) {
                    e.printStackTrace();
                    break;
                }
            }

            db.execSQL(deleteString);
            db.setTransactionSuccessful();

        }catch (SQLiteException e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
            db.close();
        }
    }

}
