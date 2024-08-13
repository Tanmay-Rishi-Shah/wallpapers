package com.expance.merapaisa;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import vocsy.ads.AdsApplication;

/* loaded from: classes3.dex */
public class AppEngine extends AdsApplication {
    private boolean inBackground;

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");
        setWasInBackground(true);
        setupLifecycleListener();
    }

    private void setupLifecycleListener() {
        ProcessLifecycleOwner.get().getLifecycle().addObserver(new AppLifecycleListener());
    }

    public boolean wasInBackground() {
        return this.inBackground;
    }

    public void setWasInBackground(boolean inBackground) {
        this.inBackground = inBackground;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class AppLifecycleListener implements LifecycleObserver {
        AppLifecycleListener() {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        public void onMoveToBackground() {
            AppEngine.this.setWasInBackground(true);
        }
    }
}
