package com.sugan.chatapplication.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.sugan.chatapplication.Activities.ChatActivity;
import com.sugan.chatapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sugan on 8/12/15.
 */
public class PushNotifications {

    Context context;

    public final int NOTIFY_LED_DEFAULT = Notification.DEFAULT_LIGHTS;
    public final int NOTIFY_LED_BLUE = Color.BLUE;
    public final int NOTIFY_LED_RED = Color.RED;
    public final int NOTIFY_LED_GREEN = Color.GREEN;
    public final int NOTIFY_LED_WHITE = Color.WHITE;
    public final int NOTIFY_LED_YELLOW = Color.YELLOW;

    NotificationManager notificationManager;
    NotificationCompat.Builder notificationBuilder;

    private final int SDK_VERSION = Build.VERSION.SDK_INT;

    //TODO Add action buttons and activities to handle the actions for backward compatibility
    //TODO Remove pending event if app is already running in foreground

    public PushNotifications(Context context){
        this.context = context;
    }

    public void pushANotification(int notificationId, JSONObject details, Bitmap largeIcon) throws JSONException {

        notificationBuilder = new NotificationCompat.Builder(context);
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);

        final JSONObject led = details.optJSONObject("led");
        final JSONObject notificationDetails = details.getJSONObject("notification");
        final JSONObject expandedLayout = details.optJSONObject("expandedLayout");

        if(largeIcon!=null)
            notificationBuilder.setLargeIcon(largeIcon);

        notificationBuilder.setAutoCancel(true);

        final String contentTitle = notificationDetails.getString("contentTitle");
        notificationBuilder.setContentTitle(contentTitle);

        final String contentText = notificationDetails.getString("contentText");
        notificationBuilder.setContentText(contentText);

        if(led!=null) {
            boolean isRequired = led.getBoolean("isRequired");
            if (isRequired) {
                final int color = led.getInt("color");
                final int onTime = led.getInt("onTime"); // In milliseconds
                final int offTime = led.getInt("offTime"); // In milliseconds
                notificationBuilder.setLights(color,onTime,offTime);
            }
        }

        if(expandedLayout!=null) {
            //Expanded layout is supported from Android 4.1 only (Jellybean)

            final boolean isRequired = expandedLayout.getBoolean("isRequired");
            if (isRequired && SDK_VERSION>=Build.VERSION_CODES.JELLY_BEAN) {
                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

                final String bigContentTitle = expandedLayout.getString("bigContentTitle");
                final String summaryText = expandedLayout.getString("summaryText");

                final JSONArray lines = expandedLayout.getJSONArray("lines");

                int length = lines.length();

                for(int i=0; i<length; i++){
                    inboxStyle.addLine(lines.getString(i));
                }

                inboxStyle.setBigContentTitle(bigContentTitle);
                inboxStyle.setSummaryText(summaryText);

                notificationBuilder.setStyle(inboxStyle);

            }

        }

        Intent resultIntent = new Intent(context, ChatActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(ChatActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(resultPendingIntent);

        Notification notification = notificationBuilder.build();

        if(SDK_VERSION>=Build.VERSION_CODES.JELLY_BEAN) {
            notification.priority = Notification.PRIORITY_HIGH;
            notification.category = Notification.CATEGORY_SOCIAL;
        }

        else{
            notification.flags = Notification.FLAG_HIGH_PRIORITY;
        }

        notificationManager.notify(notificationId, notification);

    }

    public void cancelNotification(int notificationId) throws NullPointerException{
        notificationManager.cancel(notificationId);
    }

    public void cancelAllNotifications() throws NullPointerException{
        notificationManager.cancelAll();
    }

}
