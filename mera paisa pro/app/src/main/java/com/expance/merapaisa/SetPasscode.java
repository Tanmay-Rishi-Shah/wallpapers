package com.expance.merapaisa;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;

/* loaded from: classes3.dex */
public class SetPasscode extends AppCompatActivity implements View.OnClickListener {
    View bullet1;
    View bullet2;
    View bullet3;
    View bullet4;
    TextView hintLabel;
    String matchPassword;
    String password;
    int type;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
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
        setContentView(R.layout.activity_set_passcode);
        SystemConfiguration.setStatusBarColor(this, R.color.white, SystemConfiguration.IconColor.ICON_DARK);

        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        this.password = "";
        this.matchPassword = "";
        setUpLayout();
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.scale_in, R.anim.right_to_left);
    }

    private void setUpLayout() {
        this.bullet1 = findViewById(R.id.bullet1);
        this.bullet2 = findViewById(R.id.bullet2);
        this.bullet3 = findViewById(R.id.bullet3);
        this.bullet4 = findViewById(R.id.bullet4);
        this.hintLabel = (TextView) findViewById(R.id.hintLabel);
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
        this.type = 0;
        this.hintLabel.setText(getResources().getString(R.string.choose_passcode));
        bulletChanged();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        String str = ""; // initialize to an empty string
        if (view.getId() == R.id.clear) {
            if (this.password.length() > 0) {
                this.password = this.password.substring(0, this.password.length() - 1); // use this.password instead of str
            }
        } else if (view.getTag() != null) {
            String str2 = (String) view.getTag();
            if (this.password.length() != 4) {
                this.password += str2;
            }
        }
        bulletChanged();
    }

    private void bulletChanged() {
        View[] viewArr = {this.bullet1, this.bullet2, this.bullet3, this.bullet4};
        for (int i = 0; i < 4; i++) {
            viewArr[i].getBackground().setColorFilter(Helper.getAttributeColor(this, R.attr.buttonDisabled), PorterDuff.Mode.SRC_OVER);
        }
        for (int i2 = 0; i2 < this.password.length(); i2++) {
            viewArr[i2].getBackground().setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_OVER);
        }
        if (this.password.length() == 4) {
            if (this.matchPassword.length() == 4) {
                if (this.password.equals(this.matchPassword)) {
                    SharePreferenceHelper.setPasscode(this, this.password);
                    finish();
                    return;
                }
                this.password = "";
                this.matchPassword = "";
                Toast.makeText(this, (int) R.string.mismatch_passcode, 0).show();
                this.hintLabel.setText(getResources().getString(R.string.choose_passcode));
                bulletChanged();
                return;
            }
            this.matchPassword = this.password;
            this.password = "";
            this.hintLabel.setText(getResources().getString(R.string.confirm_passcode));
            bulletChanged();
        }
    }
}
