package com.expance.merapaisa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.Dao.WalletDaoObject;
import com.expance.merapaisa.Database.Entity.AccountEntity;
import com.expance.merapaisa.Database.Entity.CategoryEntity;
import com.expance.merapaisa.Database.Entity.CurrencyEntity;
import com.expance.merapaisa.Database.Entity.WalletEntity;
import com.expance.merapaisa.Database.InitialData;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

/* loaded from: classes3.dex */
public class AccountCreateAmount extends AppCompatActivity implements View.OnClickListener {
    String amount;
    EditText amountEditText;
    ImageButton clearButton;
    String currency;
    String currencySymbol;
    Button decimalButton;
    ImageView doneButton;
    Button eightButton;
    Button fiveButton;
    Button fourButton;
    String name;
    Button nineButton;
    Button oneButton;
    Button sevenButton;
    Button sixButton;
    TextView skipButton;
    Button threeButton;
    Button twoButton;
    Button zeroButton;

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
        setContentView(R.layout.activity_acount_create_amount);
        SystemConfiguration.setStatusBarColor(this, R.color.white, SystemConfiguration.IconColor.ICON_DARK);
//        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        this.amount = "0";
        this.currencySymbol = getIntent().getStringExtra("currencySymbol");
        this.currency = getIntent().getStringExtra(FirebaseAnalytics.Param.CURRENCY);
        this.name = getIntent().getStringExtra("name");
        setUpLayout();
    }

    private void setUpLayout() {
        this.amountEditText = (EditText) findViewById(R.id.amountEditText);
        this.skipButton = (TextView) findViewById(R.id.skip);
        this.clearButton = (ImageButton) findViewById(R.id.clear);
        this.doneButton = (ImageView) findViewById(R.id.done);
        this.decimalButton = (Button) findViewById(R.id.decimal);
        this.zeroButton = (Button) findViewById(R.id.zero);
        this.oneButton = (Button) findViewById(R.id.one);
        this.twoButton = (Button) findViewById(R.id.two);
        this.threeButton = (Button) findViewById(R.id.three);
        this.fourButton = (Button) findViewById(R.id.four);
        this.fiveButton = (Button) findViewById(R.id.five);
        this.sixButton = (Button) findViewById(R.id.six);
        this.sevenButton = (Button) findViewById(R.id.seven);
        this.eightButton = (Button) findViewById(R.id.eight);
        this.nineButton = (Button) findViewById(R.id.nine);
        Button button = this.decimalButton;
        button.setText(new DecimalFormatSymbols(Locale.getDefault()).getDecimalSeparator() + "");
        this.amountEditText.setFocusable(false);
        this.amountEditText.setOnClickListener(this);
        this.amountEditText.setLongClickable(false);
        this.skipButton.setOnClickListener(this);
        this.clearButton.setOnClickListener(this);
        this.doneButton.setOnClickListener(this);
        this.decimalButton.setOnClickListener(this);
        this.zeroButton.setOnClickListener(this);
        this.oneButton.setOnClickListener(this);
        this.twoButton.setOnClickListener(this);
        this.threeButton.setOnClickListener(this);
        this.fourButton.setOnClickListener(this);
        this.fiveButton.setOnClickListener(this);
        this.sixButton.setOnClickListener(this);
        this.sevenButton.setOnClickListener(this);
        this.eightButton.setOnClickListener(this);
        this.nineButton.setOnClickListener(this);
        setAmount();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        if (v.getId() == R.id.clear /* 2131230901 */) {
            this.amount = validateEscape(this.amount);
            setAmount();
        } else if (v.getId() == R.id.decimal /* 2131230984 */) {
            this.amount = validateDot(this.amount);
            setAmount();
        } else if (v.getId() == R.id.done /* 2131231016 */) {
            createAccount(Helper.getLongFromString(this.amount));
        } else if (v.getId() == R.id.skip /* 2131231517 */) {
            createAccount(Helper.getLongFromString("0"));
        } else {
            if (v.getTag() != null) {
                this.amount = validateDigit((String) v.getTag(), this.amount);
                setAmount();
            }
        }
    }

    private String getFormattedAmount(String s) {
        BigDecimal bigDecimal = new BigDecimal(s);
        if (s.contains(".")) {
            if (s.indexOf(".") != s.length() - 1) {
                return s.indexOf(".") == s.length() + (-2) ? String.format(Locale.getDefault(), "%,.1f", bigDecimal) : String.format(Locale.getDefault(), "%,.2f", bigDecimal);
            }
            String format = String.format(Locale.getDefault(), "%,.1f", bigDecimal);
            return format.substring(0, format.length() - 1);
        }
        String format2 = String.format(Locale.getDefault(), "%,.1f", bigDecimal);
        return format2.substring(0, format2.length() - 2);
    }

    private void setAmount() {
        this.amountEditText.setText(this.currencySymbol + StringUtils.SPACE + getFormattedAmount(this.amount));
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.scale_in, R.anim.right_to_left);
    }

    private void createAccount(final long amount) {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.AccountCreateAmount.1
            @Override // java.lang.Runnable
            public void run() {
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(AccountCreateAmount.this.getApplicationContext());
                final int insertAccount = (int) appDatabaseObject.accountDaoObject().insertAccount(new AccountEntity(0, AccountCreateAmount.this.name, AccountCreateAmount.this.currency, AccountCreateAmount.this.currencySymbol, amount, appDatabaseObject.accountDaoObject().getAccountLastOrdering()));
                List<CategoryEntity> categoryExpenseData = InitialData.getCategoryExpenseData(AccountCreateAmount.this.getApplicationContext(), insertAccount);
                List<CategoryEntity> categoryIncomeData = InitialData.getCategoryIncomeData(AccountCreateAmount.this.getApplicationContext(), insertAccount);
                appDatabaseObject.categoryDaoObject().insertCategory((CategoryEntity[]) categoryExpenseData.toArray(new CategoryEntity[categoryExpenseData.size()]));
                appDatabaseObject.categoryDaoObject().insertCategory((CategoryEntity[]) categoryIncomeData.toArray(new CategoryEntity[categoryIncomeData.size()]));
                WalletDaoObject walletDaoObject = appDatabaseObject.walletDaoObject();
                String string = AccountCreateAmount.this.getResources().getString(R.string.cash);
                long j = amount;
                walletDaoObject.insertWallet(new WalletEntity(string, 0, "#0097E6", j, j, 0, insertAccount, 0, 0, AccountCreateAmount.this.currency, 0, 0, 0, 0L));
                appDatabaseObject.currencyDaoObject().insertCurrency(new CurrencyEntity(AccountCreateAmount.this.currency, 1.0d, insertAccount));
                AccountCreateAmount.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.AccountCreateAmount.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        SharePreferenceHelper.setAccount(AccountCreateAmount.this.getApplicationContext(), insertAccount, AccountCreateAmount.this.currencySymbol, AccountCreateAmount.this.name);
                        SharePreferenceHelper.setCurrencyCode(AccountCreateAmount.this.getApplicationContext(), AccountCreateAmount.this.currency);
                        Intent intent = new Intent(AccountCreateAmount.this.getApplicationContext(), GetStart.class);
                        intent.setFlags(268468224);
                        AccountCreateAmount.this.startActivity(intent);
                        AccountCreateAmount.this.finish();
                    }
                });
            }
        });
    }

    private String validateDigit(String digit, String amount) {
        if (amount.equals("0")) {
            return digit;
        }
        if (amount.contains(".")) {
            if (amount.indexOf(".") > amount.length() - 3) {
                return amount + digit;
            }
            return amount;
        }
        String str = amount + digit;
        return Long.parseLong(str) > 1000000000000L ? escapeCharacter(str) : str;
    }

    private String validateDot(String amount) {
        if (amount.contains(".")) {
            return amount;
        }
        return amount + ".";
    }

    private String validateEscape(String amount) {
        String escapeCharacter = escapeCharacter(amount);
        return escapeCharacter.length() == 0 ? "0" : escapeCharacter;
    }

    private String escapeCharacter(String s) {
        return s.substring(0, s.length() - 1);
    }
}
