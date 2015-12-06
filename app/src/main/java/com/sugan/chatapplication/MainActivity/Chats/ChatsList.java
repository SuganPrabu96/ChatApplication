package com.sugan.chatapplication.MainActivity.Chats;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sugan on 6/12/15.
 */
public class ChatsList extends ArrayList<ChatsItem> implements Parcelable{

    protected ChatsList(Parcel in) {
    }

    public static final Creator<ChatsList> CREATOR = new Creator<ChatsList>() {
        @Override
        public ChatsList createFromParcel(Parcel in) {
            return new ChatsList(in);
        }

        @Override
        public ChatsList[] newArray(int size) {
            return new ChatsList[size];
        }
    };

    public void readFromParcel(Parcel in) throws JSONException {

        this.clear();

        int size = in.readInt();

        for(int i=0; i<size; i++){
            ChatsItem item = new ChatsItem(new JSONObject(in.readString()));
            this.add(item);
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        int size = this.size();

        for(int j=0; j<size; j++){
            ChatsItem item = this.get(j);

            parcel.writeString(item.getChatItem().toString());
        }

    }
}
