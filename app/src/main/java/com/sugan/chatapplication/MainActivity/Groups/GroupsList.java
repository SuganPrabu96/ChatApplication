package com.sugan.chatapplication.MainActivity.Groups;

import android.os.Parcel;
import android.os.Parcelable;

import com.sugan.chatapplication.MainActivity.Chats.ChatsItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sugan on 6/12/15.
 */
public class GroupsList extends ArrayList<GroupsItem> implements Parcelable{
    protected GroupsList(Parcel in) {
    }

    public static final Creator<GroupsList> CREATOR = new Creator<GroupsList>() {
        @Override
        public GroupsList createFromParcel(Parcel in) {
            return new GroupsList(in);
        }

        @Override
        public GroupsList[] newArray(int size) {
            return new GroupsList[size];
        }
    };

    public void readFromParcel(Parcel in) throws JSONException {

        this.clear();

        int size = in.readInt();

        for(int i=0; i<size; i++){
            GroupsItem item = new GroupsItem(new JSONObject(in.readString()));
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
            GroupsItem item = this.get(j);

            parcel.writeString(item.getGroupsItem().toString());
        }

    }

}
