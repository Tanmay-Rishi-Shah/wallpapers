package com.expance.merapaisa.Utility;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import androidx.core.app.ActivityCompat;

/* loaded from: classes3.dex */
public class FingerprintHelper extends FingerprintManager.AuthenticationCallback {
    private Context appContext;
    private CancellationSignal cancellationSignal;
    FingerPrintListener listener;

    /* loaded from: classes3.dex */
    public interface FingerPrintListener {
        void onFailed();

        void onSucceed();
    }

    public FingerprintHelper(Context context) {
        this.appContext = context;
    }

    public void setListener(FingerPrintListener listener) {
        this.listener = listener;
    }

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        this.cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(this.appContext, "android.permission.USE_FINGERPRINT") != 0) {
            return;
        }
        manager.authenticate(cryptoObject, this.cancellationSignal, 0, this, null);
    }

    public void stopAuth() {
        CancellationSignal cancellationSignal = this.cancellationSignal;
        if (cancellationSignal == null || cancellationSignal.isCanceled()) {
            return;
        }
        this.cancellationSignal.cancel();
    }

    @Override // android.hardware.fingerprint.FingerprintManager.AuthenticationCallback
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        FingerPrintListener fingerPrintListener = this.listener;
        if (fingerPrintListener != null) {
            fingerPrintListener.onFailed();
        }
    }

    @Override // android.hardware.fingerprint.FingerprintManager.AuthenticationCallback
    public void onAuthenticationFailed() {
        FingerPrintListener fingerPrintListener = this.listener;
        if (fingerPrintListener != null) {
            fingerPrintListener.onFailed();
        }
    }

    @Override // android.hardware.fingerprint.FingerprintManager.AuthenticationCallback
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        FingerPrintListener fingerPrintListener = this.listener;
        if (fingerPrintListener != null) {
            fingerPrintListener.onSucceed();
        }
    }
}
