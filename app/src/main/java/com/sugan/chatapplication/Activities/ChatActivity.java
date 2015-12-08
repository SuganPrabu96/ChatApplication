package com.sugan.chatapplication.Activities;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.sugan.chatapplication.ChatActivity.ChatsItem;
import com.sugan.chatapplication.ChatActivity.ChatsRecyclerAdapter;
import com.sugan.chatapplication.DataStorage.FileStorage.ExternalStorage.PublicStorage;
import com.sugan.chatapplication.MediaAccess.Audio;
import com.sugan.chatapplication.MediaAccess.SpeechToText;
import com.sugan.chatapplication.R;
import com.sugan.chatapplication.util.PushNotifications;
import com.sugan.chatapplication.util.SharedPrefsAccess;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChatActivity extends AppCompatActivity{

    // Static final variables
    static final String LOG_TAG = "ChatActivity";
    static final int SHORTCUT_PREF_AUDIO = 1;
    static final int SHORTCUT_PREF_PHOTO = 2;
    static final int SHORTCUT_PREF_VIDEO = 3;
    static final int SHORTCUT_PREF_LOCATION = 4;
    static final int SHORTCUT_PREF_SPEECH_TO_TEXT = 5;

    // Views
    ImageView newEmoticonIV, sendIV, captureAudioIV, captureVideoIV, capturePhotoIV, captureLocationIV, speechToTextIV;
    EditText message;
    RecyclerView recyclerView;

    // Adapters
    ChatsRecyclerAdapter adapter;

    // Static complex variables
    static ArrayList<ChatsItem> chatsList;
    public static Handler speechToTextHandler;

    // Media variables
    MediaRecorder recorder;
    SpeechRecognizer speechRecognizer;

    // Shared preferences variables
    int shortcutPref = -1;
    JSONObject preferencesObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        newEmoticonIV = (ImageView) findViewById(R.id.new_emoticon);
        sendIV = (ImageView) findViewById(R.id.send);
        captureAudioIV = (ImageView) findViewById(R.id.capture_audio);
        capturePhotoIV = (ImageView) findViewById(R.id.capture_photo);
        captureVideoIV = (ImageView) findViewById(R.id.capture_video);
        captureLocationIV = (ImageView) findViewById(R.id.capture_location);
        speechToTextIV = (ImageView) findViewById(R.id.speech_to_text);
        message = (EditText) findViewById(R.id.new_text);

        recyclerView = (RecyclerView) findViewById(R.id.chats_recyclerview);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new ChatsRecyclerAdapter(chatsList, getApplicationContext());
        recyclerView.setAdapter(adapter);

        speechToTextHandler = new SpeechToTextHandlerClass(this);

        try {
            preferencesObj = new JSONObject(SharedPrefsAccess.retrieveData(getApplicationContext(),"Preferences"));
            shortcutPref = preferencesObj.getInt("shortcutFunction");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendIV.setVisibility(View.GONE);
        switch (shortcutPref){
            case SHORTCUT_PREF_AUDIO :
                captureAudioIV.setVisibility(View.VISIBLE);
                break;
            case SHORTCUT_PREF_VIDEO :
                captureVideoIV.setVisibility(View.VISIBLE);
                break;
            case SHORTCUT_PREF_PHOTO :
                capturePhotoIV.setVisibility(View.VISIBLE);
                break;
            case SHORTCUT_PREF_LOCATION :
                captureLocationIV.setVisibility(View.VISIBLE);
                break;
            case SHORTCUT_PREF_SPEECH_TO_TEXT :
                speechToTextIV.setVisibility(View.VISIBLE);
                break;
            default :
                captureAudioIV.setVisibility(View.VISIBLE);
                break;
        }
        speechToTextIV.setVisibility(View.VISIBLE);

        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("")) {
                    sendIV.setVisibility(View.GONE);
                    switch (shortcutPref){
                        case SHORTCUT_PREF_AUDIO :
                            captureAudioIV.setVisibility(View.VISIBLE);
                            break;
                        case SHORTCUT_PREF_VIDEO :
                            captureVideoIV.setVisibility(View.VISIBLE);
                            break;
                        case SHORTCUT_PREF_PHOTO :
                            capturePhotoIV.setVisibility(View.VISIBLE);
                            break;
                        case SHORTCUT_PREF_LOCATION :
                            captureLocationIV.setVisibility(View.VISIBLE);
                            break;
                        case SHORTCUT_PREF_SPEECH_TO_TEXT :
                            speechToTextIV.setVisibility(View.VISIBLE);
                            break;
                        default :
                            captureAudioIV.setVisibility(View.VISIBLE);
                            break;
                    }
                    speechToTextIV.setVisibility(View.VISIBLE);
                } else {
                    captureAudioIV.setVisibility(View.GONE);
                    capturePhotoIV.setVisibility(View.GONE);
                    captureVideoIV.setVisibility(View.GONE);
                    captureLocationIV.setVisibility(View.GONE);
                    speechToTextIV.setVisibility(View.GONE);
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

        captureAudioIV.setOnTouchListener(new View.OnTouchListener() {

            long time;
            File file;
            String filePath = "";

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    try {
                        JSONObject temp = new JSONObject();
                        time = System.currentTimeMillis();
                        temp.put("name", String.valueOf(time));
                        temp.put("type", "Audio");
                        file = PublicStorage.getFileStorageDir(temp);
                        filePath = file.getAbsolutePath();
                        Audio.startRecordingAudio(recorder, filePath);
                    } catch (IOException | JSONException | NullPointerException e) {
                        e.printStackTrace();
                    }
                    return true;
                } else if (action == MotionEvent.ACTION_UP) {
                    Audio.stopRecordingAudio(recorder);

                    // Upload the file

                    try {
                        Audio.playAudio(filePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
                return false;
            }
        });

        speechToTextIV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    SpeechToText.startCapturingSpeech(getApplicationContext(), speechRecognizer);
                    return true;
                } else if (action == MotionEvent.ACTION_UP) {
                    SpeechToText.stopCapturingSpeech(speechRecognizer);
                    return false;
                }
                return false;
            }
        });

        //TODO : Decide DB model
        //TODO : Load data from DB here.

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
        if(recorder!=null)
            try{
                Audio.stopRecordingAudio(recorder);
                Log.d(LOG_TAG, "Stopped recording");
            }catch (Exception e){
                e.printStackTrace();
            }
        if(speechRecognizer!=null)
            try{
                SpeechToText.stopCapturingSpeech(speechRecognizer);
            }catch (Exception e){
                e.printStackTrace();
            }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "onPause");

        if(recorder!=null)
            try{
                Audio.stopRecordingAudio(recorder);
                Log.d(LOG_TAG, "Stopped recording");
            }catch (Exception e){
                e.printStackTrace();
            }
        if(speechRecognizer!=null)
            try{
                SpeechToText.stopCapturingSpeech(speechRecognizer);
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
        if(recorder!=null)
            try{
                Audio.stopRecordingAudio(recorder);
                Log.d(LOG_TAG, "Stopped recording");
            }catch (Exception e){
                e.printStackTrace();
            }
        if(speechRecognizer!=null)
            try{
                SpeechToText.stopCapturingSpeech(speechRecognizer);
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
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState){

        //Fired only when the bundle is not null

        chatsList = savedInstanceState.getParcelableArrayList("chats");

        super.onRestoreInstanceState(savedInstanceState);

    }

    private static class SetAdapterHandlerClass extends Handler {

        private final WeakReference<ChatActivity> mTarget;

        public SetAdapterHandlerClass(ChatActivity context)
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

    private static class SpeechToTextHandlerClass extends Handler {

        private final WeakReference<ChatActivity> mTarget;

        public SpeechToTextHandlerClass(ChatActivity context)
        {
            mTarget = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ChatActivity target = mTarget.get();
            if (target != null) {
                if (msg.what == 1) {
                    Bundle bundle = msg.getData();
                    ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    Log.d(LOG_TAG + " : Matches", matches.toString());
                    String bestMatch = matches.get(0);
                    target.message.append(bestMatch);
                    JSONObject details = new JSONObject();
                    JSONObject notif = new JSONObject();
                    JSONObject led = new JSONObject();
                    JSONObject expandedLayout = new JSONObject();
                    try {
                        Log.d(LOG_TAG, "Pushing");
                        notif.put("contentTitle", "Title");
                        notif.put("contentText", bestMatch);
                        led.put("isRequired", true);
                        led.put("onTime",500);
                        led.put("offTime",500);
                        led.put("color", Color.BLUE);
                        expandedLayout.put("isRequired",true);
                        expandedLayout.put("bigContentTitle","Big Content Title");
                        expandedLayout.put("summaryText","Received 4 new messages");
                        JSONArray lines = new JSONArray();
                        lines.put(0,"Sugan");
                        lines.put(1,"Prabu");
                        lines.put(2,"test");
                        lines.put(3,"line");
                        expandedLayout.put("lines",lines);
                        details.put("notification",notif);
                        details.put("led",led);
                        details.put("expandedLayout",expandedLayout);
                        Bitmap bitmap = BitmapFactory.decodeResource(target.getResources(),R.mipmap.ic_launcher);
                        new PushNotifications(target.getApplicationContext()).pushANotification(1,details,bitmap);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(msg.what == 0){
                    Bundle bundle = msg.getData();
                    String error = bundle.getString("error");
                    Toast.makeText(target.getApplicationContext(),error,Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

}
