package com.sugan.chatapplication.MainActivity.Contacts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sugan.chatapplication.R;

import java.util.ArrayList;

/**
 * Created by Suganprabu on 30-05-2015.
 */
public class ContactsRecyclerAdapter extends RecyclerView.Adapter<ContactsViewHolder>{

    private ArrayList<ContactsItem> jobs;
    private Context context;

    public ContactsRecyclerAdapter(ArrayList<ContactsItem> jobs, Context context) {
        this.jobs = jobs;
        this.context = context;
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.main_contacts_recyclerview,
                        viewGroup,
                        false);

        return new ContactsViewHolder(context, itemView);
    }

    @Override
    public void onBindViewHolder(final ContactsViewHolder viewHolder, final int position) {
        //viewHolder. = names.get(position);
        final ContactsItem item = jobs.get(position);


    }

    public int getItemCount() {
        return jobs == null ? 0 : jobs.size();
    }

    public void add(ContactsItem item){
        jobs.add(item);
        notifyItemInserted(jobs.indexOf(item));
    }
}
