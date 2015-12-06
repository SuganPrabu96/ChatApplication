package com.sugan.chatapplication.Activities;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.sugan.chatapplication.ChatActivity.ChatsItem;
import com.sugan.chatapplication.ChatActivity.ChatsRecyclerAdapter;
import com.sugan.chatapplication.R;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    static final String LOG_TAG = "ChatActivity";

    ImageView newEmoticonIV, sendIV, attachIV;
    EditText message;
    static boolean captureAudio = false;
    MediaRecorder recorder;

    RecyclerView recyclerView;
    ChatsRecyclerAdapter adapter;

    static ArrayList<ChatsItem> chatsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        newEmoticonIV = (ImageView) findViewById(R.id.new_emoticon);
        sendIV = (ImageView) findViewById(R.id.send);
        attachIV = (ImageView) findViewById(R.id.capture_audio);

        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().equals("")){
                    sendIV.setVisibility(View.GONE);
                    attachIV.setVisibility(View.VISIBLE);
                }
                else{
                    attachIV.setVisibility(View.GONE);
                    sendIV.setVisibility(View.VISIBLE);
                }
            }
        });

        sendIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Send the message
            }
        });

        //TODO : Display a popup and ask for image/video/audio etc

        attachIV.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                int action = keyEvent.getAction();
                if(action == KeyEvent.ACTION_DOWN)
                    try {

                        startRecordingAudio("");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                else if(action == KeyEvent.ACTION_UP)
                    stopRecordingAudio();
                return false;
            }
        });

        //TODO : Decide DB model
        //TODO : Load data from DB here.

    }

    void startRecordingAudio(String filePath) throws IOException {

        recorder = new MediaRecorder();

        final int MAX_DURATION = 300000; // 5 minutes

        recorder.setAudioChannels(2);
        recorder.setMaxDuration(MAX_DURATION);

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_WB);
        recorder.setOutputFile(filePath);
        Log.d(LOG_TAG, "Starting to record");

        recorder.prepare();
        recorder.start();

    }

    void stopRecordingAudio(){

        recorder.stop();
        recorder.release();
        recorder = null;

        Log.d(LOG_TAG, "Stopped recording");

    }

    void playAudio(String filePath) throws IOException {

        MediaPlayer player = new MediaPlayer();
        player.setDataSource(filePath);

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "onStop");

        try{
            recorder.stop();
            recorder.release();
            recorder = null;
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "onPause");

        try{
            recorder.stop();
            recorder.release();
            recorder = null;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(LOG_TAG, "onResume");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");

        try{
            recorder.stop();
            recorder.release();
            recorder = null;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){

        //Save the necessary data here

        savedInstanceState.putParcelableArrayList("chats",chatsList);

        super.onSaveInstanceState(savedInstanceState);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){

        //Fired only when the bundle is not null

        chatsList = savedInstanceState.getParcelableArrayList("chats");

        super.onRestoreInstanceState(savedInstanceState);

    }

    private static class HandlerClass extends Handler {

        private final WeakReference<ChatActivity> mTarget;

        public HandlerClass(ChatActivity context)
        {
            mTarget = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ChatActivity target = mTarget.get();
            if (target != null)
                if(msg.what==1){
                    ChatActivity activity = mTarget.get();
                    activity.adapter = new ChatsRecyclerAdapter(chatsList, activity.getApplicationContext());
                    activity.recyclerView.setAdapter(activity.adapter);
                }
        }

    }

}
