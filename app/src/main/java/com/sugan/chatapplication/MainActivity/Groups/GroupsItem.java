package com.sugan.chatapplication.MainActivity.Groups;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Suganprabu on 30-05-2015.
 */
public class GroupsItem implements Parcelable{

    private JSONObject groupsItem;

    public GroupsItem(JSONObject groupsItem){
        this.groupsItem = groupsItem;
    }

    protected GroupsItem(Parcel in) {
    }

    public static final Creator<GroupsItem> CREATOR = new Creator<GroupsItem>() {
        @Override
        public GroupsItem createFromParcel(Parcel in) {
            return new GroupsItem(in);
        }

        @Override
        public GroupsItem[] newArray(int size) {
            return new GroupsItem[size];
        }
    };

    public JSONObject getGroupsItem() {
        return groupsItem;
    }

    public void setGroupsItem(JSONObject groupsItem) {
        this.groupsItem = groupsItem;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(groupsItem.toString());
    }

    public void readFromParcel(Parcel in) throws JSONException {
        this.groupsItem = new JSONObject(in.readString());
    }

}
