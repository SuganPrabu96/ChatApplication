package com.sugan.chatapplication.MainActivity.Contacts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Suganprabu on 30-05-2015.
 */
public class ContactsViewHolder extends RecyclerView.ViewHolder{

    private Context context;

    public ContactsViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;

    }
}
