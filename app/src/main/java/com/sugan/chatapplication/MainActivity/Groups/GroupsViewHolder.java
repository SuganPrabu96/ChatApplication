package com.sugan.chatapplication.MainActivity.Groups;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Suganprabu on 30-05-2015.
 */
public class GroupsViewHolder extends RecyclerView.ViewHolder{

    private Context context;
    public GroupsViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
    }
}
