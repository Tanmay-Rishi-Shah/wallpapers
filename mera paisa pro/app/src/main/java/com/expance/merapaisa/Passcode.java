package com.expance.merapaisa;

import android.app.KeyguardManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.expance.merapaisa.Utility.FingerprintHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/* loaded from: classes3.dex */
public class Passcode extends AppCompatActivity implements View.OnClickListener, FingerprintHelper.FingerPrintListener {
    private static final String KEY_NAME = "example_key";
    View bullet1;
    View bullet2;
    View bullet3;
    View bullet4;
    private Cipher cipher;
    private FingerprintManager.CryptoObject cryptoObject;
    FingerprintHelper fingerprintHelper;
    ImageView fingerprintImageView;
    private FingerprintManager fingerprintManager;
    private Handler handler;
    TextView hintLabel;
    KeyGenerator keyGenerator;
    KeyStore keyStore;
    private KeyguardManager keyguardManager;
    String matchPassword;
    String password;
    private Runnable runnable;

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTheme(SharePreferenceHelper.getThemeMode(this) == 1 ? R.style.AppThemeNight : R.style.AppTheme);
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (SharePreferenceHelper.getThemeMode(this) == 1) {
//                getWindow().getDecorView().setSystemUiVisibility(0);
//            } else {
//                getWindow().getDecorView().setSystemUiVisibility(8192);
//            }
//        }
//        if (Build.VERSION.SDK_INT >= 26) {
//            if (SharePreferenceHelper.getThemeMode(this) == 1) {
//                getWindow().getDecorView().setSystemUiVisibility(0);
//            } else {
//                getWindow().getDecorView().setSystemUiVisibility(8208);
//            }
//            getWindow().setNavigationBarColor(Color.parseColor(SharePreferenceHelper.getThemeMode(this) == 1 ? "#1F1F1F" : "#F8F8F8"));
//        } else {
//            getWindow().setNavigationBarColor(Color.parseColor("#000000"));
//        }
        setContentView(R.layout.activity_passcode);
        SystemConfiguration.setStatusBarColor(this, R.color.white, SystemConfiguration.IconColor.ICON_DARK);
//        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        this.handler = new Handler();
        this.password = "";
        this.matchPassword = SharePreferenceHelper.getPasscode(this);
        setUpLayout();
        if (Build.VERSION.SDK_INT >= 23) {
            if (!SharePreferenceHelper.isFingerprintEnable(this)) {
                this.fingerprintImageView.setVisibility(8);
                return;
            }
            this.keyguardManager = (KeyguardManager) getSystemService("keyguard");
            FingerprintManager fingerprintManager = (FingerprintManager) getSystemService("fingerprint");
            this.fingerprintManager = fingerprintManager;
            if (fingerprintManager == null) {
                this.fingerprintImageView.setVisibility(8);
                return;
            } else if (!fingerprintManager.isHardwareDetected()) {
                this.fingerprintImageView.setVisibility(8);
                return;
            } else if (!this.keyguardManager.isKeyguardSecure()) {
                this.fingerprintImageView.setVisibility(8);
                return;
            } else if (ActivityCompat.checkSelfPermission(this, "android.permission.USE_FINGERPRINT") != 0) {
                this.fingerprintImageView.setVisibility(8);
                return;
            } else if (!this.fingerprintManager.hasEnrolledFingerprints()) {
                this.fingerprintImageView.setVisibility(8);
                return;
            } else {
                generateKey();
                if (cipherInit()) {
                    this.cryptoObject = new FingerprintManager.CryptoObject(this.cipher);
                    FingerprintHelper fingerprintHelper = new FingerprintHelper(this);
                    this.fingerprintHelper = fingerprintHelper;
                    fingerprintHelper.setListener(this);
                    this.fingerprintHelper.startAuth(this.fingerprintManager, this.cryptoObject);
                    return;
                }
                return;
            }
        }
        this.fingerprintImageView.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStart() {
        FingerprintHelper fingerprintHelper;
        super.onStart();
        AppEngine appEngine = (AppEngine) getApplication();
        if (appEngine.wasInBackground()) {
            appEngine.setWasInBackground(false);
            if (Build.VERSION.SDK_INT < 23 || (fingerprintHelper = this.fingerprintHelper) == null) {
                return;
            }
            fingerprintHelper.startAuth(this.fingerprintManager, this.cryptoObject);
        }
    }

    protected void generateKey() {
        try {
            this.keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.keyGenerator = KeyGenerator.getInstance("AES", "AndroidKeyStore");
            try {
                this.keyStore.load(null);
                this.keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME, 3).setBlockModes("CBC").setUserAuthenticationRequired(true).setEncryptionPaddings("PKCS7Padding").build());
                this.keyGenerator.generateKey();
            } catch (IOException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | CertificateException e2) {
                throw new RuntimeException(e2);
            }
        } catch (NoSuchAlgorithmException | NoSuchProviderException e3) {
            throw new RuntimeException("Failed to get KeyGenerator instance", e3);
        }
    }

    public boolean cipherInit() {
        try {
            this.cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            Exception e;
            try {
                this.keyStore.load(null);
                this.cipher.init(1, (SecretKey) this.keyStore.getKey(KEY_NAME, null));
                return true;
            } catch (KeyPermanentlyInvalidatedException unused) {
                return false;
            } catch (IOException exception) {
                e = exception;
                throw new RuntimeException("Failed to init Cipher", e);
            } catch (InvalidKeyException e2) {
                e = e2;
                throw new RuntimeException("Failed to init Cipher", e);
            } catch (KeyStoreException e3) {
                e = e3;
                throw new RuntimeException("Failed to init Cipher", e);
            } catch (NoSuchAlgorithmException e4) {
                e = e4;
                throw new RuntimeException("Failed to init Cipher", e);
            } catch (UnrecoverableKeyException e5) {
                e = e5;
                throw new RuntimeException("Failed to init Cipher", e);
            } catch (CertificateException e6) {
                e = e6;
                throw new RuntimeException("Failed to init Cipher", e);
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e7) {
            throw new RuntimeException("Failed to get Cipher", e7);
        }
    }

    private void setUpLayout() {
        this.fingerprintImageView = (ImageView) findViewById(R.id.fingerprintImageView);
        this.hintLabel = (TextView) findViewById(R.id.hintLabel);
        this.bullet1 = findViewById(R.id.bullet1);
        this.bullet2 = findViewById(R.id.bullet2);
        this.bullet3 = findViewById(R.id.bullet3);
        this.bullet4 = findViewById(R.id.bullet4);
        ((Button) findViewById(R.id.nine)).setOnClickListener(this);
        ((Button) findViewById(R.id.eight)).setOnClickListener(this);
        ((Button) findViewById(R.id.seven)).setOnClickListener(this);
        ((Button) findViewById(R.id.six)).setOnClickListener(this);
        ((Button) findViewById(R.id.five)).setOnClickListener(this);
        ((Button) findViewById(R.id.four)).setOnClickListener(this);
        ((Button) findViewById(R.id.three)).setOnClickListener(this);
        ((Button) findViewById(R.id.two)).setOnClickListener(this);
        ((Button) findViewById(R.id.one)).setOnClickListener(this);
        ((Button) findViewById(R.id.zero)).setOnClickListener(this);
        ((ImageButton) findViewById(R.id.clear)).setOnClickListener(this);
        bulletChanged();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        String str = null;
        if (view.getId() == R.id.clear) {
            if (this.password.length() > 0) {
                this.password = this.password.substring(0, str.length() - 1);
            }
        } else if (view.getTag() != null) {
            String str2 = (String) view.getTag();
            if (this.password.length() != 4) {
                this.password += str2;
            }
        }
        bulletChanged();
    }

    private void bulletMax() {
        View[] viewArr = {this.bullet1, this.bullet2, this.bullet3, this.bullet4};
        for (int i = 0; i < 4; i++) {
            viewArr[i].getBackground().setColorFilter(Color.parseColor("#3897F0"), PorterDuff.Mode.SRC_OVER);
        }
    }

    private void bulletChanged() {
        FingerprintHelper fingerprintHelper;
        View[] viewArr = {this.bullet1, this.bullet2, this.bullet3, this.bullet4};
        for (int i = 0; i < 4; i++) {
            viewArr[i].getBackground().setColorFilter(Helper.getAttributeColor(this, R.attr.buttonDisabled), PorterDuff.Mode.SRC_OVER);
        }
        for (int i2 = 0; i2 < this.password.length(); i2++) {
            viewArr[i2].getBackground().setColorFilter(Color.parseColor("#3897F0"), PorterDuff.Mode.SRC_OVER);
        }
        if (this.password.length() == 4) {
            if (this.matchPassword.equals(this.password)) {
                if (Build.VERSION.SDK_INT >= 23 && (fingerprintHelper = this.fingerprintHelper) != null) {
                    fingerprintHelper.stopAuth();
                }
                finish();
                return;
            }
            this.password = "";
            bulletChanged();
            this.hintLabel.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            this.hintLabel.setText(R.string.wrong_passcode);
            this.hintLabel.setTextColor(Color.parseColor("#FF4500"));
            Vibrator vibrator = (Vibrator) getSystemService("vibrator");
            if (vibrator != null) {
                vibrator.vibrate(50L);
            }
        }
    }

    @Override // com.ktwapps.walletmanager.Utility.FingerprintHelper.FingerPrintListener
    public void onFailed() {
        this.fingerprintImageView.setImageResource(R.drawable.fingerprint_error);
        this.fingerprintImageView.setBackgroundResource(R.drawable.background_fingerprint_error);
        Runnable runnable = this.runnable;
        if (runnable != null) {
            this.handler.removeCallbacks(runnable);
        }
        Runnable runnable2 = new Runnable() { // from class: com.ktwapps.walletmanager.Passcode$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                Passcode.this.m225lambda$onFailed$1$comktwappswalletmanagerPasscode();
            }
        };
        this.runnable = runnable2;
        this.handler.postDelayed(runnable2, 1500L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onFailed$1$com-ktwapps-walletmanager-Passcode  reason: not valid java name */
    public /* synthetic */ void m225lambda$onFailed$1$comktwappswalletmanagerPasscode() {
        runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.Passcode$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                Passcode.this.m224lambda$onFailed$0$comktwappswalletmanagerPasscode();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onFailed$0$com-ktwapps-walletmanager-Passcode  reason: not valid java name */
    public /* synthetic */ void m224lambda$onFailed$0$comktwappswalletmanagerPasscode() {
        this.fingerprintImageView.setImageResource(R.drawable.fingerprint);
        this.fingerprintImageView.setBackgroundResource(R.drawable.background_fingerprint);
    }

    @Override // com.ktwapps.walletmanager.Utility.FingerprintHelper.FingerPrintListener
    public void onSucceed() {
        Runnable runnable;
        this.fingerprintImageView.setImageResource(R.drawable.fingerprint_done);
        this.fingerprintImageView.setBackgroundResource(R.drawable.background_fingerprint);
        this.hintLabel.setText(R.string.enter_passcode);
        this.hintLabel.setTextColor(Helper.getAttributeColor(this, R.attr.primaryTextColor));
        bulletMax();
        Handler handler = this.handler;
        if (handler != null && (runnable = this.runnable) != null) {
            handler.removeCallbacks(runnable);
        }
        new Handler().postDelayed(new Runnable() { // from class: com.ktwapps.walletmanager.Passcode$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                Passcode.this.m227lambda$onSucceed$3$comktwappswalletmanagerPasscode();
            }
        }, 500L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onSucceed$2$com-ktwapps-walletmanager-Passcode  reason: not valid java name */
    public /* synthetic */ void m226lambda$onSucceed$2$comktwappswalletmanagerPasscode() {
        finish();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onSucceed$3$com-ktwapps-walletmanager-Passcode  reason: not valid java name */
    public /* synthetic */ void m227lambda$onSucceed$3$comktwappswalletmanagerPasscode() {
        runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.Passcode$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                Passcode.this.m226lambda$onSucceed$2$comktwappswalletmanagerPasscode();
            }
        });
    }
}
