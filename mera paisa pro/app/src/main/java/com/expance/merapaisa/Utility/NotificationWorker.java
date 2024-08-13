package com.expance.merapaisa.Utility;

import android.content.Context;
import androidx.work.ListenableWorker;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.util.concurrent.TimeUnit;

/* loaded from: classes3.dex */
public class NotificationWorker extends Worker {
    public NotificationWorker(Context context, WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @Override // androidx.work.Worker
    public ListenableWorker.Result doWork() {
        NotificationHelper.fireNotification(getApplicationContext());
        WorkManager.getInstance(getApplicationContext()).enqueue(new OneTimeWorkRequest.Builder(NotificationWorker.class).addTag(Constant.NOTIFICATION_WORKER).setInitialDelay(NotificationHelper.getMilliseconds(getApplicationContext()), TimeUnit.MILLISECONDS).build());
        return ListenableWorker.Result.success();
    }
}
