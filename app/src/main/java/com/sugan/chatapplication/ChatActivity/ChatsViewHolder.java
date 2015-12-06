package com.sugan.chatapplication.ChatActivity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by sugan on 6/12/15.
 */
public class ChatsViewHolder extends RecyclerView.ViewHolder{

    private Context context;

    public ChatsViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;

    }
}
