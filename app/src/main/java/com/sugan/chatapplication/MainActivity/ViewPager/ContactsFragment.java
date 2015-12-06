package com.sugan.chatapplication.MainActivity.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sugan.chatapplication.Activities.MainActivity;
import com.sugan.chatapplication.MainActivity.Chats.ChatsRecyclerAdapter;
import com.sugan.chatapplication.MainActivity.Contacts.ContactsRecyclerAdapter;
import com.sugan.chatapplication.R;

import java.lang.ref.WeakReference;

/**
 * Created by sugan on 6/12/15.
 */
public class ContactsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ContactsRecyclerAdapter adapter;
    public static Handler setAdapterHandler;

    public static Fragment newInstance(Context context) {
        ChatsFragment f = new ChatsFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.main_contacts_page, null);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.chats_recyclerview);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new ContactsRecyclerAdapter(MainActivity.contactsList, rootView.getContext());

        recyclerView.setAdapter(adapter);

        setAdapterHandler = new HandlerClass(this);

        return rootView;
    }

    private static class HandlerClass extends Handler {

        private final WeakReference<ContactsFragment> mTarget;

        public HandlerClass(ContactsFragment context)
        {
            mTarget = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ContactsFragment target = mTarget.get();
            if (target != null)
                if(msg.what==1){
                    ContactsFragment fragment = mTarget.get();
                    fragment.adapter = new ContactsRecyclerAdapter(MainActivity.contactsList, fragment.getContext());
                    fragment.recyclerView.setAdapter(fragment.adapter);
                }
        }

    }
}
