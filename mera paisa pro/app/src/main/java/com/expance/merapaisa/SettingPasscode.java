package com.expance.merapaisa;

import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import com.expance.merapaisa.Utility.BillingHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;

/* loaded from: classes3.dex */
public class SettingPasscode extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, BillingHelper.BillingListener {
    Button addButton;
    BillingHelper billingHelper;
    Button changeButton;
    TextView fingerprintLabel;
    ImageView proIcon;
    Switch switchView;

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onSkuReady() {
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
        setContentView(R.layout.activity_setting_passcode);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        if (SharePreferenceHelper.checkPasscode(this)) {
            startActivity(new Intent(this, Passcode.class));
        }
        setUpLayout();
        BillingHelper billingHelper = new BillingHelper(this);
        this.billingHelper = billingHelper;
        billingHelper.setListener(this);
        this.billingHelper.setUpBillingClient();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        this.billingHelper.registerBroadCast(this);
        this.billingHelper.queryPurchases();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        super.onStop();
        this.billingHelper.unregisterBroadCast(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStart() {
        super.onStart();
        boolean checkPasscode = SharePreferenceHelper.checkPasscode(this);
        this.changeButton.setBackground(ContextCompat.getDrawable(this, checkPasscode ? R.drawable.background_button_state : R.drawable.background_button_disable_state));
        this.addButton.setText(checkPasscode ? R.string.remove_passcode : R.string.choose_passcode);
        checkFingerPrint();
        ((AppEngine) getApplication()).setWasInBackground(false);
    }

    private void setUpLayout() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.passcode));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.proIcon = (ImageView) findViewById(R.id.proIcon);
        this.addButton = (Button) findViewById(R.id.addButton);
        this.changeButton = (Button) findViewById(R.id.changeButton);
        this.fingerprintLabel = (TextView) findViewById(R.id.fingerprintLabel);
        this.switchView = (Switch) findViewById(R.id.switchView);
        this.addButton.setOnClickListener(this);
        this.changeButton.setOnClickListener(this);
        this.switchView.setOnCheckedChangeListener(this);
    }

    @Override // androidx.appcompat.app.AppCompatActivity
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(R.anim.scale_in, R.anim.right_to_left);
        return true;
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.scale_in, R.anim.right_to_left);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id != R.id.addButton) {
            if (id != R.id.changeButton) {
                if (id != R.id.fingerprintLabel) {
                    return;
                }
                this.switchView.toggle();
            } else if (SharePreferenceHelper.checkPasscode(this)) {
                startActivity(new Intent(getApplicationContext(), SetPasscode.class));
            }
        } else if (SharePreferenceHelper.checkPasscode(this)) {
            SharePreferenceHelper.removePasscode(this);
            SharePreferenceHelper.setFingerprintEnable(this, false);
            this.changeButton.setBackground(ContextCompat.getDrawable(this, R.drawable.background_button_disable_state));
            this.addButton.setText(R.string.choose_passcode);
            checkFingerPrint();
        } else {
            startActivity(new Intent(getApplicationContext(), SetPasscode.class));
        }
    }

    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        SharePreferenceHelper.setFingerprintEnable(this, b);
    }

    private void checkFingerPrint() {
        if (this.billingHelper.getBillingStatus() == 2) {
            this.proIcon.setVisibility(8);
            this.fingerprintLabel.setTextColor(Helper.getAttributeColor(this, R.attr.primaryTextColor));
        } else {
            this.proIcon.setVisibility(0);
            this.fingerprintLabel.setTextColor(Helper.getAttributeColor(this, R.attr.untitledTextColor));
        }
        if (Build.VERSION.SDK_INT >= 23) {
            FingerprintManager fingerprintManager = (FingerprintManager) getSystemService("fingerprint");
            if (fingerprintManager == null) {
                this.fingerprintLabel.setOnClickListener(null);
                this.switchView.setEnabled(false);
                this.switchView.setChecked(false);
                return;
            } else if (!fingerprintManager.isHardwareDetected()) {
                this.fingerprintLabel.setOnClickListener(null);
                this.switchView.setEnabled(false);
                this.switchView.setChecked(false);
                return;
            } else {
                boolean checkPasscode = SharePreferenceHelper.checkPasscode(this);
                if (this.billingHelper.getBillingStatus() == 2) {
                    this.fingerprintLabel.setOnClickListener(checkPasscode ? this : null);
                    this.switchView.setEnabled(checkPasscode);
                    this.switchView.setChecked(SharePreferenceHelper.isFingerprintEnable(this));
                    return;
                }
                this.fingerprintLabel.setOnClickListener(null);
                this.switchView.setEnabled(false);
                this.switchView.setChecked(false);
                return;
            }
        }
        this.fingerprintLabel.setOnClickListener(null);
        this.switchView.setEnabled(false);
        this.switchView.setChecked(false);
    }

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onLoaded() {
        checkSubscription();
    }

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onPurchasedSucceed() {
        checkSubscription();
    }

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onPurchasedPending() {
        checkSubscription();
    }

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onReceiveBroadCast() {
        checkSubscription();
    }

    public void checkSubscription() {
        checkFingerPrint();
    }
}
