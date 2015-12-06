package com.sugan.chatapplication.MainActivity.Contacts;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Suganprabu on 30-05-2015.
 */
public class ContactsItem implements Parcelable{

    private JSONObject contactItem;

    public ContactsItem(JSONObject contactItem){
        this.contactItem = contactItem;
    }

    protected ContactsItem(Parcel in) {
    }

    public static final Creator<ContactsItem> CREATOR = new Creator<ContactsItem>() {
        @Override
        public ContactsItem createFromParcel(Parcel in) {
            return new ContactsItem(in);
        }

        @Override
        public ContactsItem[] newArray(int size) {
            return new ContactsItem[size];
        }
    };

    public JSONObject getContactItem() {
        return contactItem;
    }

    public void setContactItem(JSONObject contactItem) {
        this.contactItem = contactItem;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(contactItem.toString());
    }

    public void readFromParcel(Parcel in) throws JSONException {
        this.contactItem = new JSONObject(in.readString());
    }

}
