package com.expance.merapaisa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.expance.merapaisa.Utility.CalculatorHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;

import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/* loaded from: classes3.dex */
public class Calculator extends AppCompatActivity implements View.OnClickListener {
    private String equation;
    private TextView total;

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
        setContentView(R.layout.activity_calculator);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        this.equation = CalculatorHelper.getPlainAmount(new BigDecimal(getIntent().getLongExtra("amount", 0L)).divide(new BigDecimal(100), 2, 1));
        setUpLayout();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStart() {
        super.onStart();
        AppEngine appEngine = (AppEngine) getApplication();
        if (appEngine.wasInBackground()) {
            appEngine.setWasInBackground(false);
            if (SharePreferenceHelper.checkPasscode(getApplicationContext())) {
                Intent intent = new Intent(getApplicationContext(), Passcode.class);
                intent.addFlags(65536);
                intent.addFlags(268435456);
                startActivity(intent);
            }
        }
    }

    private void setUpLayout() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.enter_amount);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.total = (TextView) findViewById(R.id.total);
        Button button = (Button) findViewById(R.id.multiply);
        Button button2 = (Button) findViewById(R.id.divide);
        Button button3 = (Button) findViewById(R.id.zero);
        Button button4 = (Button) findViewById(R.id.dZero);
        Button button5 = (Button) findViewById(R.id.dot);
        Button button6 = (Button) findViewById(R.id.equal);
        Button button7 = (Button) findViewById(R.id.clear);
        ImageButton imageButton = (ImageButton) findViewById(R.id.escape);
        ImageButton imageButton2 = (ImageButton) findViewById(R.id.done);
        button5.setText(new DecimalFormatSymbols(Locale.getDefault()).getDecimalSeparator() + "");
        ((Button) findViewById(R.id.nine)).setOnClickListener(this);
        ((Button) findViewById(R.id.eight)).setOnClickListener(this);
        ((Button) findViewById(R.id.seven)).setOnClickListener(this);
        ((Button) findViewById(R.id.six)).setOnClickListener(this);
        ((Button) findViewById(R.id.five)).setOnClickListener(this);
        ((Button) findViewById(R.id.four)).setOnClickListener(this);
        ((Button) findViewById(R.id.three)).setOnClickListener(this);
        ((Button) findViewById(R.id.two)).setOnClickListener(this);
        ((Button) findViewById(R.id.one)).setOnClickListener(this);
        ((Button) findViewById(R.id.plus)).setOnClickListener(this);
        ((Button) findViewById(R.id.minus)).setOnClickListener(this);
        button.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        imageButton2.setOnClickListener(this);
        button7.setOnClickListener(this);
        imageButton.setOnClickListener(this);
        this.total.setText(CalculatorHelper.getDisplayAmount(this.equation));
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.clear /* 2131230901 */) {
            clear();
        } else if (view.getId() == R.id.divide /* 2131231010 */) {
            divide();
        } else if (view.getId() == R.id.done /* 2131231016 */) {
            done();
        } else if (view.getId() == R.id.dot /* 2131231021 */) {
            dot();
        } else if (view.getId() == R.id.equal /* 2131231062 */) {
            equal();
        } else if (view.getId() == R.id.escape /* 2131231063 */) {
            escape();
        } else if (view.getId() == R.id.minus /* 2131231267 */) {
            minus();
        } else if (view.getId() == R.id.multiply /* 2131231322 */) {
            multiply();
        } else if (view.getId() == R.id.plus /* 2131231410 */) {
            plus();
        } else {
            if (view.getTag() != null) {
                digit((String) view.getTag());
            }
        }
    }

    private void divide() {
        String validateDivide = CalculatorHelper.validateDivide(this.equation);
        this.equation = validateDivide;
        this.total.setText(CalculatorHelper.getFormattedNumber(validateDivide));
    }

    private void multiply() {
        String validateMultiply = CalculatorHelper.validateMultiply(this.equation);
        this.equation = validateMultiply;
        this.total.setText(CalculatorHelper.getFormattedNumber(validateMultiply));
    }

    private void plus() {
        String validatePlus = CalculatorHelper.validatePlus(this.equation);
        this.equation = validatePlus;
        this.total.setText(CalculatorHelper.getFormattedNumber(validatePlus));
    }

    private void minus() {
        String validateMinus = CalculatorHelper.validateMinus(this.equation);
        this.equation = validateMinus;
        this.total.setText(CalculatorHelper.getFormattedNumber(validateMinus));
    }

    private void clear() {
        this.equation = "0";
        this.total.setText(CalculatorHelper.getFormattedNumber("0"));
    }

    private void equal() {
        String validateEqual = CalculatorHelper.validateEqual(this.equation);
        this.equation = validateEqual;
        this.total.setText(CalculatorHelper.getFormattedNumber(validateEqual));
    }

    private void done() {
        equal();
        Intent intent = new Intent();
        long longFromString = Helper.getLongFromString(this.equation);
        if (longFromString > 100000000000000L) {
            longFromString = 99999999999999L;
        }
        intent.putExtra("amount", longFromString);
        setResult(-1, intent);
        finish();
    }

    private void escape() {
        String validateEscape = CalculatorHelper.validateEscape(this.equation);
        this.equation = validateEscape;
        this.total.setText(CalculatorHelper.getFormattedNumber(validateEscape));
    }

    private void dot() {
        String validateDot = CalculatorHelper.validateDot(this.equation);
        this.equation = validateDot;
        this.total.setText(CalculatorHelper.getFormattedNumber(validateDot));
    }

    private void digit(String s) {
        String validateDigit = CalculatorHelper.validateDigit(s, this.equation, false);
        this.equation = validateDigit;
        this.total.setText(CalculatorHelper.getFormattedNumber(validateDigit));
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
}
