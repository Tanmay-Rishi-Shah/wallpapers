package com.expance.merapaisa.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/* loaded from: classes3.dex */
public class BroadcastUpdated extends BroadcastReceiver {
    OnBroadcastListener listener;

    /* loaded from: classes3.dex */
    public interface OnBroadcastListener {
        void getBroadcast();
    }

    public void setListener(OnBroadcastListener listener) {
        this.listener = listener;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        OnBroadcastListener onBroadcastListener = this.listener;
        if (onBroadcastListener != null) {
            onBroadcastListener.getBroadcast();
        }
    }
}
