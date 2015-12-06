package com.sugan.chatapplication.MainActivity.Groups;

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

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Suganprabu on 30-05-2015.
 */
public class GroupsRecyclerAdapter extends RecyclerView.Adapter<GroupsViewHolder>{

    private ArrayList<GroupsItem> jobs;
    private Context context;

    public GroupsRecyclerAdapter(ArrayList<GroupsItem> jobs, Context context) {
        this.jobs = jobs;
        this.context = context;
    }

    @Override
    public GroupsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.main_groups_recyclerview,
                        viewGroup,
                        false);

        return new GroupsViewHolder(context, itemView);
    }

    @Override
    public void onBindViewHolder(final GroupsViewHolder viewHolder, final int position) {
        //viewHolder. = names.get(position);
        final GroupsItem item = jobs.get(position);

    }

    public int getItemCount() {
        return jobs == null ? 0 : jobs.size();
    }

    public void add(GroupsItem item){
        jobs.add(item);
        notifyItemInserted(jobs.indexOf(item));
    }
}
