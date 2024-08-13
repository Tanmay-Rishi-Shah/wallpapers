package com.expance.merapaisa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.expance.merapaisa.Utility.DataHelper;

import java.util.Iterator;
import java.util.Locale;

/* loaded from: classes3.dex */
public class AccountCreateCurrency extends AppCompatActivity implements View.OnClickListener {
    String code;
    String currency;
    EditText currencyEditText;
    String name;
    ImageView nextButton;
    String symbol;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        String str;
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
        setContentView(R.layout.activity_acount_create_currency);
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        this.name = getIntent().getStringExtra("name");
        this.currencyEditText = (EditText) findViewById(R.id.currencyEditText);
        this.nextButton = (ImageView) findViewById(R.id.nextButton);
        this.currencyEditText.setFocusable(false);
        this.currencyEditText.setOnClickListener(this);
        this.currencyEditText.setLongClickable(false);
        this.nextButton.setOnClickListener(this);
        try {
            str = java.util.Currency.getInstance(Locale.getDefault()).getCurrencyCode();
        } catch (Exception unused) {
            str = "USD";
        }
        Iterator<String> it = DataHelper.getCurrencyCode().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            String next = it.next();
            if (str.equals(next)) {
                int indexOf = DataHelper.getCurrencyCode().indexOf(next);
                this.symbol = DataHelper.getCurrencySymbolList().get(indexOf);
                this.code = next;
                this.currency = DataHelper.getCurrencyList().get(indexOf);
                break;
            }
        }
        if (this.currency == null) {
            this.symbol = "$";
            this.code = "USD";
            this.currency = "USD - United States Dollar($)";
        }
        this.currencyEditText.setText(this.currency);
//        this.nextButton.setBackground(getResources().getDrawable(R.drawable.background_button_state));
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        if (v.getId() == R.id.currencyEditText) {
            Intent intent = new Intent(this, CurrencyPicker.class);
            intent.putExtra(FirebaseAnalytics.Param.CURRENCY, this.currency);
            startActivityForResult(intent, 7);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
            return;
        }
        String str = this.currency;
        if (str == null || str.length() <= 0) {
            return;
        }
        Intent intent2 = new Intent(this, AccountCreateAmount.class);
        intent2.putExtra("name", this.name);
        intent2.putExtra(FirebaseAnalytics.Param.CURRENCY, this.code);
        intent2.putExtra("currencySymbol", this.symbol);
        startActivity(intent2);
        overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7 && resultCode == -1 && data != null) {
            int intExtra = data.getIntExtra(FirebaseAnalytics.Param.INDEX, 0);
            this.currency = DataHelper.getCurrencyList().get(intExtra);
            this.code = DataHelper.getCurrencyCode().get(intExtra);
            this.symbol = DataHelper.getCurrencySymbolList().get(intExtra);
            this.currencyEditText.setText(this.currency);
//            this.nextButton.setBackground(getResources().getDrawable(R.drawable.background_button_state));
        }
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.scale_in, R.anim.right_to_left);
    }
}
