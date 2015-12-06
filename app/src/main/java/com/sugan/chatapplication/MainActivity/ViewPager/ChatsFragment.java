package com.sugan.chatapplication.MainActivity.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sugan.chatapplication.Activities.MainActivity;
import com.sugan.chatapplication.DataStorage.SQLiteDatabase.DBStorageAccess;
import com.sugan.chatapplication.MainActivity.Chats.ChatsItem;
import com.sugan.chatapplication.MainActivity.Chats.ChatsRecyclerAdapter;
import com.sugan.chatapplication.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by sugan on 6/12/15.
 */
public class ChatsFragment extends Fragment {

    RecyclerView recyclerView;
    ChatsRecyclerAdapter adapter;
    public static Handler setAdapterHandler;

    public static Fragment newInstance(Context context) {
        ChatsFragment f = new ChatsFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.main_chats_page, null);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.chats_recyclerview);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new ChatsRecyclerAdapter(MainActivity.chatsList, rootView.getContext());

        recyclerView.setAdapter(adapter);

        setAdapterHandler = new HandlerClass(this);

        return rootView;
    }

    private static class HandlerClass extends Handler{

        private final WeakReference<ChatsFragment> mTarget;

        public HandlerClass(ChatsFragment context)
        {
            mTarget = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ChatsFragment target = mTarget.get();
            if (target != null)
                if(msg.what==1){
                    ChatsFragment fragment = mTarget.get();
                    fragment.adapter = new ChatsRecyclerAdapter(MainActivity.chatsList, fragment.getContext());
                    fragment.recyclerView.setAdapter(fragment.adapter);
                }
        }

    }

}

