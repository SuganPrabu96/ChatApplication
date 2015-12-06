package com.sugan.chatapplication.DataStorage.SQLiteDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Suganprabu on 23-06-2015.
 */
public class SqlHandler extends SQLiteOpenHelper{

    //Database details
    private static final int dbVersion = 1;
    private static final String dbName = "ChatApplication";

    //Database table names
    public static final String[] TABLE_NAMES = {"Groups,Chats,Contacts"};

    //Database table column names
    private static final String[][] COLUMN_NAMES = {
            {
                    "groupId","group"
            },
            {
                    "chatId","chat"
            },
            {
                    "contactId","contact"
            }
    };

    //Database table column types
    private static final String[][] COLUMN_TYPES = {
            {
                    "TEXT","TEXT"
            },
            {
                    "TEXT","TEXT"
            },
            {
                    "TEXT","TEXT"
            }

    };

    public SqlHandler(Context context){
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        int length = TABLE_NAMES.length;
        for(int i=0; i<length; i++) {
            String create = "CREATE TABLE IF NOT EXISTS " + TABLE_NAMES[i] + "(";
            int size = COLUMN_NAMES[i].length;
            for (int j=0; j < size; j++) {
                create = create + COLUMN_NAMES[i][j] + " " + COLUMN_TYPES[i][j];
                if (j < size - 1) {
                    create = create + ",";
                }
            }
            create += ")";
            db.execSQL(create);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        for(int i=0; i<TABLE_NAMES.length;i++)
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAMES[i]);
        onCreate(db);
    }

    public void dropTables(){

        SQLiteDatabase db = getWritableDatabase();
        for(int i=0; i<TABLE_NAMES.length;i++)
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAMES[i]);
    }

}
