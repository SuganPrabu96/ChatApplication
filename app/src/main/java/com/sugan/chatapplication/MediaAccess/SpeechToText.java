package com.sugan.chatapplication.MediaAccess;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import com.sugan.chatapplication.Activities.ChatActivity;

import java.util.ArrayList;

/**
 * Created by sugan on 7/12/15.
 */
public class SpeechToText {

    final static String LOG_TAG = "SpeechToText";

    static AudioManager audioManager;
    static int mStreamVolume = 0;

    public static void startCapturingSpeech(Context context, SpeechRecognizer speechRecognizer) {

        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mStreamVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,0,0);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        speechRecognizer.setRecognitionListener(listener);

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        intent.putExtra(
                RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 3000L);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 5000L);

        speechRecognizer.startListening(intent);

    }

    public static void stopCapturingSpeech(SpeechRecognizer speechRecognizer) {
        if(speechRecognizer!=null)
           speechRecognizer.stopListening();
    }

    static RecognitionListener listener = new RecognitionListener() {

        @Override
        public void onReadyForSpeech(Bundle bundle) {
            Log.d(LOG_TAG, "onReadyForSpeech");
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,0,0);
        }

        @Override
        public void onBeginningOfSpeech() {
            Log.d(LOG_TAG, "onBeginningOfSpeech");
        }

        @Override
        public void onRmsChanged(float v) {
            Log.d(LOG_TAG, "onRmsChanged");
        }

        @Override
        public void onBufferReceived(byte[] bytes) {
            Log.d(LOG_TAG, "onBufferReceived");
        }

        @Override
        public void onEndOfSpeech() {
            Log.d(LOG_TAG, "onEndOfSpeech");
        }

        @Override
        public void onError(int i) {
            Log.d(LOG_TAG, "onError");
            String mError = "";
            switch (i) {
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    mError = "Network Timeout";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    mError = "Network Error";
                    return;
                case SpeechRecognizer.ERROR_AUDIO:
                    mError = "Audio Error";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    mError = "Server Error";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    mError = "Client Error";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    mError = "Speech Timed Out";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    mError = "No Match";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    mError = "Recogniser Busy";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    mError = "Insufficient Permissions";
                    break;
            }
            Message msg = new Message();
            msg.what = 0;
            Bundle bundle = new Bundle();
            bundle.putString("error", mError);
            msg.setData(bundle);
            ChatActivity.speechToTextHandler.sendMessage(msg);
            Log.d(LOG_TAG, mError);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mStreamVolume, 0);
        }

        @Override
        public void onResults(Bundle bundle) {
            Log.d(LOG_TAG, "onResults");
            Message msg = new Message();
            msg.what = 1;
            msg.setData(bundle);
            ChatActivity.speechToTextHandler.sendMessage(msg);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,mStreamVolume,0);
        }

        @Override
        public void onPartialResults(Bundle bundle) {
            Log.d(LOG_TAG, "onPartialResults");
            Message msg = new Message();
            msg.what = 1;
            msg.setData(bundle);
            ChatActivity.speechToTextHandler.sendMessage(msg);
        }

        @Override
        public void onEvent(int i, Bundle bundle) {
            Log.d(LOG_TAG, "onEvent");
        }
    };
}
