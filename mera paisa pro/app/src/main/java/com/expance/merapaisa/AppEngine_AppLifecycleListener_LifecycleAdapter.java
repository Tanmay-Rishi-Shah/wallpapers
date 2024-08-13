package com.expance.merapaisa;

import androidx.lifecycle.GeneratedAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MethodCallsLogger;

/* loaded from: classes3.dex */
public class AppEngine_AppLifecycleListener_LifecycleAdapter implements GeneratedAdapter {
    final AppEngine.AppLifecycleListener mReceiver;

    AppEngine_AppLifecycleListener_LifecycleAdapter(AppEngine.AppLifecycleListener receiver) {
        this.mReceiver = receiver;
    }

    @Override // androidx.lifecycle.GeneratedAdapter
    public void callMethods(LifecycleOwner owner, Lifecycle.Event event, boolean onAny, MethodCallsLogger logger) {
        boolean z = logger != null;
        if (!onAny && event == Lifecycle.Event.ON_STOP) {
            if (!z || logger.approveCall("onMoveToBackground", 1)) {
                this.mReceiver.onMoveToBackground();
            }
        }
    }
}
