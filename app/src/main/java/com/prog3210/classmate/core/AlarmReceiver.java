package com.prog3210.classmate.core;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Justin on 2015-12-06.
 */
public class AlarmReceiver extends BroadcastReceiver {
    public static String NOTIFICATION_ID = "classmate_event_notification";
    public static String NOTIFICATION = "notification";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);

        int id = intent.getIntExtra(NOTIFICATION_ID, 0);

        notificationManager.notify(id, notification);
    }
}