package com.sugan.chatapplication.MainActivity.Chats;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sugan.chatapplication.R;

import java.util.ArrayList;

/**
 * Created by Suganprabu on 30-05-2015.
 */
public class ChatsRecyclerAdapter extends RecyclerView.Adapter<ChatsViewHolder>{

    private ArrayList<ChatsItem> chats;
    private Context context;

    public ChatsRecyclerAdapter(ArrayList<ChatsItem> chats, Context context) {
        this.chats = chats;
        this.context = context;
    }

    @Override
    public ChatsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.main_chats_recyclerview,
                        viewGroup,
                        false);

        return new ChatsViewHolder(context, itemView);
    }

    @Override
    public void onBindViewHolder(final ChatsViewHolder viewHolder, final int position) {
        final ChatsItem item = chats.get(position);

    }

    public int getItemCount() {
        return chats == null ? 0 : chats.size();
    }

    public void add(ChatsItem item){
        chats.add(item);
        notifyItemInserted(chats.indexOf(item));
    }
}
