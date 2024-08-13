package com.expance.merapaisa;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/* loaded from: classes3.dex */
public class AccountCreateName extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    TextView detailLabel;
    EditText nameEditText;
    ImageView nextButton;
    TextView titleLabel;
    LinearLayout wrapper;

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemConfiguration.setStatusBarColor(this, R.color.white, SystemConfiguration.IconColor.ICON_DARK);
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
        setContentView(R.layout.activity_acount_create_name);
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        setUpLayout();
    }

    private void setUpLayout() {
        this.wrapper = (LinearLayout) findViewById(R.id.wrapper);
        this.nextButton = (ImageView) findViewById(R.id.nextButton);
        this.nameEditText = (EditText) findViewById(R.id.nameEditText);
        this.titleLabel = (TextView) findViewById(R.id.titleLabel);
        this.detailLabel = (TextView) findViewById(R.id.detailLabel);
        this.nameEditText.addTextChangedListener(this);
        this.nameEditText.requestFocus();
        this.nextButton.setOnClickListener(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStart() {
        super.onStart();
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.scale_in, R.anim.right_to_left);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        String trim = this.nameEditText.getText().toString().trim();
        if (trim.length() > 0) {
            Intent intent = new Intent(this, AccountCreateCurrency.class);
            intent.putExtra("name", trim);
            startActivity(intent);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        }else {
            Toast.makeText(this, "Enter Name First...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable s) {
        if (s.toString().trim().length() > 0) {
//            this.nextButton.setBackground(getResources().getDrawable(R.drawable.background_button_state));
        } else {
//            this.nextButton.setBackground(getResources().getDrawable(R.drawable.background_color_light));
        }
    }
}
