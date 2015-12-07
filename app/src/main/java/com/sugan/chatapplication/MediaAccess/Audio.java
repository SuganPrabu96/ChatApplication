package com.sugan.chatapplication.MediaAccess;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by sugan on 6/12/15.
 */
public class Audio {

    final static String LOG_TAG = "Audio";

    public static final int SAMPLING_AMRNB = 0; // Sample rate is 8kHz
    public static final int SAMPLING_AMRWB = 1; // Sample rate is 16kHx
    public static final int SAMPLING_AAC = 2; // Sample rate is 8-96kHz

    public static final int MONO = 1; // Single Audio Channel
    public static final int STEREO = 2; // Dual Audio Channel

    public static void startRecordingAudio(MediaRecorder recorder, JSONObject details) throws JSONException, IOException {

        int audioChannels, maxDuration;

        String filePath = details.getString("filePath");
        audioChannels = details.optInt("audioChannels", -1);
        maxDuration = details.optInt("maxDuration",-1); //maxDuration in milliseconds

        recorder = new MediaRecorder();


        if(audioChannels!=-1){
            recorder.setAudioChannels(audioChannels);
        }
        if(maxDuration!=-1){
            recorder.setMaxDuration(maxDuration);
        }

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_WB);
        recorder.setOutputFile(filePath);
        Log.d(LOG_TAG, "Starting to record");

        recorder.prepare();
        recorder.start();

    }

    // Call this function in onPause(), onStop() and onDestroy() also

    public static void stopRecordingAudio(MediaRecorder recorder){

        recorder.stop();
        recorder.release();
        recorder = null;

    }

    public static void playAudio(JSONObject details) throws JSONException, IOException {

        String filePath = details.getString("filePath");

        MediaPlayer player = new MediaPlayer();
        player.setDataSource(filePath);
        player.prepare();
        player.start();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.reset();
                mediaPlayer.release();
            }
        });
        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                Log.d(LOG_TAG, "Error playing media");
                return false;
            }
        });

    }

    // Following methods use default params

    public static void startRecordingAudio(MediaRecorder recorder, String filePath) throws IOException {

        recorder = new MediaRecorder();

        final int MAX_DURATION = 300000; // 5 minutes

        recorder.setAudioChannels(2);
        recorder.setMaxDuration(MAX_DURATION);

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setOutputFile(filePath);
        Log.d(LOG_TAG, "Starting to record");

        recorder.prepare();
        recorder.start();

    }

    public static void playAudio(String filePath) throws IOException {

        MediaPlayer player = new MediaPlayer();
        player.setDataSource(filePath);
        player.prepare();
        player.start();

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.reset();
                mediaPlayer.release();
            }
        });

        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                Log.d(LOG_TAG, "Error playing media");
                return false;
            }
        });

    }

}
