package com.sugan.chatapplication.ServerInteraction;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sugan on 19/11/15.
 */
public class ServerRequest extends AsyncTask<JSONObject, Void, JSONObject>{

    Context context;
    JSONObject progressDialogObj, toastObj, snackbarObj;
    ProgressDialog progressDialog;
    Toast toast;
    Snackbar snackbar;
    final String LOG_TAG = "Server Request Class";
    public static Handler serverRequestHandler;
    public static final int SNACKBAR_BEGINNING = 0;
    public static final int SNACKBAR_END = 0;
    public static final int TOAST_BEGINNING = 0;
    public static final int TOAST_END = 1;


    public ServerRequest(JSONObject object) throws JSONException {
        progressDialogObj = object.optJSONObject("progressDialog");
        toastObj = object.optJSONObject("toast");
        snackbarObj = object.optJSONObject("snackbar");
        if(progressDialogObj!=null && progressDialogObj.getBoolean("required")){
            progressDialog = new ProgressDialog(context);
            JSONObject details = progressDialogObj.getJSONObject("details");
            progressDialog.setTitle(details.getString("title"));
            progressDialog.setMessage(details.getString("message"));
            progressDialog.setCancelable(details.getBoolean("cancelable"));
        }
        if(toastObj!=null && toastObj.getBoolean("required")){
            JSONObject details = toastObj.getJSONObject("details");
            toast = new Toast(context);
            if(details.getInt("duration")==Toast.LENGTH_LONG)
                toast.setDuration(Toast.LENGTH_LONG);
            else
                toast.setDuration(Toast.LENGTH_SHORT);
            toast.setText(details.getString("text"));
        }
        else if(snackbarObj!=null && snackbarObj.getBoolean("required")){
            JSONObject details = snackbarObj.getJSONObject("details");
            if(details.getInt("duration")== Snackbar.LENGTH_LONG)
                snackbar.setDuration(Snackbar.LENGTH_LONG);
            else
                snackbar.setDuration(Snackbar.LENGTH_SHORT);
            snackbar.setText(details.getString("text"));
            //TODO set action
        }

    }

    @Override
    protected void onPreExecute(){
        try {
            if(progressDialogObj.getBoolean("required")){
                progressDialog.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if(snackbarObj.getBoolean("required") && snackbarObj.getJSONObject("details").getInt("when")==SNACKBAR_BEGINNING)
                snackbar.show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if(toastObj.getBoolean("required") && toastObj.getJSONObject("details").getInt("when")==TOAST_BEGINNING){
                toast.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected JSONObject doInBackground(JSONObject... objects) {
        JSONObject obj = objects[0];
        try{
            String url = obj.getString("url");
            int method = obj.getInt("method");
            JSONObject params = obj.getJSONObject("params");
            HTTPRequestHandler requestHandler = new HTTPRequestHandler();
            String response = requestHandler.makeRequest(url, method, params);
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("respose",response);
            msg.setData(bundle);
            serverRequestHandler.sendMessage(msg);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Invalid arguments passed");
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject res){
        try {
            if(progressDialogObj.getBoolean("required") && progressDialog!=null && progressDialog.isShowing()){
                progressDialog.dismiss();
                progressDialog.hide();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if(snackbarObj.getBoolean("required") && snackbarObj.getJSONObject("details").getInt("when")==SNACKBAR_END)
                snackbar.show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if(toastObj.getBoolean("required") && toastObj.getJSONObject("details").getInt("when")==TOAST_END){
                toast.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
