package com.expance.merapaisa.Utility;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.expance.merapaisa.BaseActivity;
import com.expance.merapaisa.R;

import java.util.Calendar;
import java.util.UUID;

/* loaded from: classes3.dex */
public class NotificationHelper {
    public static void fireNotification(Context context) {
        PendingIntent activity;
        if (SharePreferenceHelper.getReminderTime(context) != 0) {
            if (Build.VERSION.SDK_INT >= 26) {
                String string = context.getResources().getString(R.string.setting_notification_title);
                String string2 = context.getResources().getString(R.string.notification_hint);
                NotificationChannel notificationChannel = new NotificationChannel(Constant.CHANNEL_ID, string, 3);
                notificationChannel.setDescription(string2);
                notificationChannel.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, new AudioAttributes.Builder().setContentType(4).setUsage(6).build());
                NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(notificationChannel);
                }
            }
            Intent intent = new Intent(context, BaseActivity.class);
            intent.setFlags(268468224);
            if (Build.VERSION.SDK_INT >= 23) {
                activity = PendingIntent.getActivity(context, UUID.randomUUID().hashCode(), intent, 201326592);
            } else {
                activity = PendingIntent.getActivity(context, UUID.randomUUID().hashCode(), intent, 134217728);
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constant.CHANNEL_ID);
            builder.setSmallIcon(R.drawable.notification);
            builder.setContentTitle(context.getResources().getString(R.string.notification_title));
            builder.setContentText(context.getResources().getString(R.string.notification_hint));
            builder.setPriority(2);
            builder.setAutoCancel(true);
            builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
            builder.setDefaults(-1);
            builder.setContentIntent(activity);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            NotificationManagerCompat.from(context).notify(100, builder.build());
        }
    }

    public static long getMilliseconds(Context context) {
        Calendar calendar = Calendar.getInstance();
        long time = calendar.getTime().getTime();
        calendar.set(11, SharePreferenceHelper.getReminderTime(context));
        calendar.set(13, 0);
        calendar.set(12, 0);
        calendar.set(14, 0);
        if (isTodayPast(context)) {
            calendar.add(5, 1);
        }
        return calendar.getTime().getTime() - time;
    }

    private static boolean isTodayPast(Context context) {
        return Calendar.getInstance().get(11) >= SharePreferenceHelper.getReminderTime(context);
    }
}
