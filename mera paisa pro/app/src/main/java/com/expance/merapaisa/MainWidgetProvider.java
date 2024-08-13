package com.expance.merapaisa;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;
import java.util.UUID;

/* loaded from: classes3.dex */
public class MainWidgetProvider extends AppWidgetProvider {
    @Override // android.appwidget.AppWidgetProvider
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        PendingIntent activity;
        PendingIntent activity2;
        for (int i : appWidgetIds) {
            Intent intent = new Intent(context, BaseActivity.class);
            intent.setFlags(872448000);
            intent.putExtra("widget", "expense");
            if (Build.VERSION.SDK_INT >= 23) {
                activity = PendingIntent.getActivity(context, UUID.randomUUID().hashCode(), intent, 67108864);
            } else {
                activity = PendingIntent.getActivity(context, UUID.randomUUID().hashCode(), intent, 134217728);
            }
            Intent intent2 = new Intent(context, BaseActivity.class);
            intent2.setFlags(872448000);
            intent2.putExtra("widget", "income");
            if (Build.VERSION.SDK_INT >= 23) {
                activity2 = PendingIntent.getActivity(context, UUID.randomUUID().hashCode(), intent2, 67108864);
            } else {
                activity2 = PendingIntent.getActivity(context, UUID.randomUUID().hashCode(), intent2, 134217728);
            }
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), (int) R.layout.widget_main);
            remoteViews.setOnClickPendingIntent(R.id.incomeButton, activity2);
            remoteViews.setOnClickPendingIntent(R.id.expenseButton, activity);
            appWidgetManager.updateAppWidget(i, remoteViews);
        }
    }
}
