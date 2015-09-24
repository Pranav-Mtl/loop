package com.aggregator.loop;

/**
 * Created by Pranav Mittal on 9/23/2015.
 */

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.aggregator.Constant.Constant;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONObject;

public class GCMNotificationIntentService extends IntentService {
    // Sets an ID for the notification, so it can be updated

    //private NotificationManager mNotificationManager;

    public static final int notifyID = 9001;
    public static final int NOTIFICATION_ID = 1;
    NotificationCompat.Builder builder;

    public GCMNotificationIntentService() {
        super("GcmIntentService");
    }

    public static final String TAG = "GCMNotificationIntentService";

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {

            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                    .equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                    .equals(messageType)) {
                    sendNotification("Deleted messages on server: "
                        + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equals(messageType)) {

                try {

                    System.out.println("GCM MESSAGE" + "" + extras.get(Constant.MSG_KEY));

                    String msgArray = extras.get(Constant.MSG_KEY) + "";
                    try {
                        JSONObject jsonObj = new JSONObject(msgArray);
                        String notification_type = jsonObj.getString("notification_type");




                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                catch (NullPointerException e)
                {
                    e.printStackTrace();
                }


            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String greetMsg) {

        String id="";
        String messagees="";

        try {
            JSONObject jsonObj = new JSONObject(greetMsg);

            id=jsonObj.getString("last_id");
            messagees=jsonObj.getString("greetMsg");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Intent resultIntent = new Intent(this, BookingNew.class);
        resultIntent.putExtra("ID", id);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                resultIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager;

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("Loop")
                .setContentText(messagees)
                .setSmallIcon(R.mipmap.ic_launcher);
        // Set pending intent
        mNotifyBuilder.setContentIntent(resultPendingIntent);
        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        mNotifyBuilder.setDefaults(defaults);
        // Set the content for Notification
        mNotifyBuilder.setContentTitle("");
        mNotifyBuilder.setContentText(messagees);
        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        // Post a notification
        mNotificationManager.notify(notifyID, mNotifyBuilder.build());
    }
}

