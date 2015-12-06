package com.sugan.chatapplication.MainActivity.Chats;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Suganprabu on 30-05-2015.
 */
public class ChatsItem implements Parcelable{

    private JSONObject chatItem;

    public ChatsItem(JSONObject chatItem){

        this.chatItem = chatItem;

    }

    protected ChatsItem(Parcel in) {
    }

    public static final Creator<ChatsItem> CREATOR = new Creator<ChatsItem>() {
        @Override
        public ChatsItem createFromParcel(Parcel in) {
            return new ChatsItem(in);
        }

        @Override
        public ChatsItem[] newArray(int size) {
            return new ChatsItem[size];
        }
    };

    public JSONObject getChatItem() {
        return chatItem;
    }

    public void setChatItem(JSONObject chatItem) {
        this.chatItem = chatItem;
    }

    public void readFromParcel(Parcel in) throws JSONException {
       this.chatItem = new JSONObject(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(chatItem.toString());
    }
}
