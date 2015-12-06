package com.sugan.chatapplication.MainActivity.Contacts;

import android.os.Parcel;
import android.os.Parcelable;

import com.sugan.chatapplication.MainActivity.Chats.ChatsItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sugan on 6/12/15.
 */
public class ContactsList extends ArrayList<ContactsItem> implements Parcelable {

    protected ContactsList(Parcel in) {
    }

    public static final Creator<ContactsList> CREATOR = new Creator<ContactsList>() {
        @Override
        public ContactsList createFromParcel(Parcel in) {
            return new ContactsList(in);
        }

        @Override
        public ContactsList[] newArray(int size) {
            return new ContactsList[size];
        }
    };

    public void readFromParcel(Parcel in) throws JSONException {

        this.clear();

        int size = in.readInt();

        for(int i=0; i<size; i++){
            ContactsItem item = new ContactsItem(new JSONObject(in.readString()));
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
            ContactsItem item = this.get(j);

            parcel.writeString(item.getContactItem().toString());
        }

    }

}
