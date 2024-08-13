package com.expance.merapaisa;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.github.mikephil.charting.utils.Utils;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.Entity.AccountEntity;
import com.expance.merapaisa.Database.Entity.CurrencyEntity;
import com.expance.merapaisa.Database.Entity.WalletEntity;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Utility.RateInputFilter;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import org.apache.commons.lang3.StringUtils;

import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class CreateCurrency extends AppCompatActivity implements TextWatcher, View.OnClickListener {
    private EditText amountEditText;
    ArrayList<String> currencyCodeList;
    private EditText currencyEditText;
    private int currencyId;
    ArrayList<String> currencyList;
    private boolean isComplete = false;
    private String mainCode;
    private String originalCode;
    private TextView rateLabel;
    private TextView saveLabel;
    private String subCode;

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("subCode", this.subCode);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
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
        setContentView(R.layout.activity_create_currency);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        this.currencyId = getIntent().getIntExtra("currencyId", 0);
        this.currencyList = DataHelper.getCurrencyList();
        this.currencyCodeList = DataHelper.getCurrencyCode();
        setUpLayout();
        populateData(savedInstanceState);
    }

    private void populateData(final Bundle outState) {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateCurrency.1
            @Override // java.lang.Runnable
            public void run() {
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(CreateCurrency.this.getApplicationContext());
                List<String> currencyCodes = appDatabaseObject.currencyDaoObject().getCurrencyCodes(SharePreferenceHelper.getAccountId(CreateCurrency.this.getApplicationContext()));
                AccountEntity entityById = appDatabaseObject.accountDaoObject().getEntityById(SharePreferenceHelper.getAccountId(CreateCurrency.this.getApplicationContext()));
                CreateCurrency.this.mainCode = entityById.getCurrency();
                if (CreateCurrency.this.currencyId != 0) {
                    CurrencyEntity currencyEntityById = appDatabaseObject.currencyDaoObject().getCurrencyEntityById(CreateCurrency.this.currencyId);
                    int indexOf = DataHelper.getCurrencyCode().indexOf(currencyEntityById.getCode());
                    final double rate = currencyEntityById.getRate();
                    Bundle bundle = outState;
                    if (bundle != null) {
                        CreateCurrency.this.subCode = bundle.getString("subCode");
                    } else {
                        CreateCurrency.this.subCode = DataHelper.getCurrencyCode().get(indexOf);
                    }
                    CreateCurrency.this.originalCode = DataHelper.getCurrencyCode().get(indexOf);
                    currencyCodes.remove(CreateCurrency.this.originalCode);
                    for (String str : currencyCodes) {
                        int indexOf2 = CreateCurrency.this.currencyCodeList.indexOf(str);
                        CreateCurrency.this.currencyList.remove(indexOf2);
                        CreateCurrency.this.currencyCodeList.remove(indexOf2);
                    }
                    CreateCurrency.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateCurrency.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (outState != null) {
                                CreateCurrency.this.updateRate();
                            } else {
                                DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ENGLISH);
                                decimalFormat.setMaximumFractionDigits(8);
                                decimalFormat.setMinimumFractionDigits(2);
                                decimalFormat.setGroupingUsed(false);
                                CreateCurrency.this.amountEditText.setText(decimalFormat.format(rate));
                                CreateCurrency.this.currencyEditText.setText(DataHelper.getCurrencyList().get(DataHelper.getCurrencyCode().indexOf(CreateCurrency.this.subCode)));
                                TextView textView = CreateCurrency.this.rateLabel;
                                textView.setText("1.00 " + CreateCurrency.this.subCode + " = " + decimalFormat.format(rate) + StringUtils.SPACE + CreateCurrency.this.mainCode);
                            }
                            CreateCurrency.this.isComplete = rate > Utils.DOUBLE_EPSILON;
                            CreateCurrency.this.checkIsComplete();
                        }
                    });
                    return;
                }
                for (String str2 : currencyCodes) {
                    int indexOf3 = CreateCurrency.this.currencyCodeList.indexOf(str2);
                    CreateCurrency.this.currencyList.remove(indexOf3);
                    CreateCurrency.this.currencyCodeList.remove(indexOf3);
                }
                Bundle bundle2 = outState;
                if (bundle2 != null) {
                    CreateCurrency.this.subCode = bundle2.getString("subCode");
                } else {
                    CreateCurrency createCurrency = CreateCurrency.this;
                    createCurrency.subCode = createCurrency.currencyCodeList.get(0);
                }
                CreateCurrency.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateCurrency.1.2
                    @Override // java.lang.Runnable
                    public void run() {
                        if (outState == null) {
                            CreateCurrency.this.amountEditText.setText("1.00");
                            TextView textView = CreateCurrency.this.rateLabel;
                            textView.setText("1.00 " + CreateCurrency.this.subCode + " = 1.00 " + CreateCurrency.this.mainCode);
                            CreateCurrency.this.currencyEditText.setText(DataHelper.getCurrencyList().get(DataHelper.getCurrencyCode().indexOf(CreateCurrency.this.subCode)));
                        }
                    }
                });
            }
        });
    }

    private void setUpLayout() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.add_currency));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.currencyEditText = (EditText) findViewById(R.id.currencyEditText);
        this.amountEditText = (EditText) findViewById(R.id.amountEditText);
        this.rateLabel = (TextView) findViewById(R.id.rateLabel);
        this.saveLabel = (TextView) findViewById(R.id.saveLabel);
        this.amountEditText.setFilters(new InputFilter[]{new RateInputFilter(6, 9, Double.POSITIVE_INFINITY)});
        this.amountEditText.addTextChangedListener(this);
        this.currencyEditText.setOnClickListener(this);
        this.currencyEditText.setLongClickable(false);
        this.currencyEditText.setFocusable(false);
        this.saveLabel.setOnClickListener(this);
    }

    @Override // androidx.appcompat.app.AppCompatActivity
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(R.anim.scale_in, R.anim.bottom_to_top);
        return true;
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.scale_in, R.anim.bottom_to_top);
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
        if (editable.toString().length() > 0) {
            try {
                double parseDouble = Double.parseDouble(editable.toString());
                if (parseDouble <= Utils.DOUBLE_EPSILON) {
                    this.isComplete = false;
                    TextView textView = this.rateLabel;
                    textView.setText("1.00 " + this.subCode + " = 0.00 " + this.mainCode);
                    return;
                }
                DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ENGLISH);
                decimalFormat.setMaximumFractionDigits(8);
                decimalFormat.setMinimumFractionDigits(2);
                decimalFormat.setGroupingUsed(false);
                TextView textView2 = this.rateLabel;
                textView2.setText("1.00 " + this.subCode + " = " + decimalFormat.format(parseDouble) + StringUtils.SPACE + this.mainCode);
                this.isComplete = true;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                TextView textView3 = this.rateLabel;
                textView3.setText("1.00 " + this.subCode + " = 0.00 " + this.mainCode);
                this.isComplete = false;
            }
        } else {
            this.isComplete = false;
            TextView textView4 = this.rateLabel;
            textView4.setText("1.00 " + this.subCode + " = 0.00 " + this.mainCode);
        }
        checkIsComplete();
    }

    private void updateCurrency() {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateCurrency.2
            @Override // java.lang.Runnable
            public void run() {
                double parseDouble = Double.parseDouble(CreateCurrency.this.amountEditText.getText().toString().trim());
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(CreateCurrency.this.getApplicationContext());
                CurrencyEntity currencyEntityById = appDatabaseObject.currencyDaoObject().getCurrencyEntityById(CreateCurrency.this.currencyId);
                currencyEntityById.setCode(CreateCurrency.this.subCode);
                currencyEntityById.setRate(parseDouble);
                if (!CreateCurrency.this.originalCode.equals(CreateCurrency.this.subCode)) {
                    for (WalletEntity walletEntity : appDatabaseObject.walletDaoObject().getCurrencyWalletEntity(SharePreferenceHelper.getAccountId(CreateCurrency.this.getApplicationContext()), CreateCurrency.this.originalCode)) {
                        walletEntity.setCurrency(CreateCurrency.this.subCode);
                        appDatabaseObject.walletDaoObject().updateWallet(walletEntity);
                    }
                }
                appDatabaseObject.currencyDaoObject().updateCurrency(currencyEntityById);
            }
        });
    }

    private void createCurrency() {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateCurrency.3
            @Override // java.lang.Runnable
            public void run() {
                AppDatabaseObject.getInstance(CreateCurrency.this.getApplicationContext()).currencyDaoObject().insertCurrency(new CurrencyEntity(CreateCurrency.this.subCode, Double.parseDouble(CreateCurrency.this.amountEditText.getText().toString().trim()), SharePreferenceHelper.getAccountId(CreateCurrency.this.getApplicationContext())));
            }
        });
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.saveLabel) {
            if (this.isComplete) {
                if (this.currencyId == 0) {
                    createCurrency();
                } else {
                    updateCurrency();
                }
                runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateCurrency.4
                    @Override // java.lang.Runnable
                    public void run() {
                        Intent intent = new Intent();
                        intent.putExtra("code", CreateCurrency.this.subCode);
                        CreateCurrency.this.setResult(-1, intent);
                        CreateCurrency.this.finish();
                        CreateCurrency.this.overridePendingTransition(R.anim.scale_in, R.anim.bottom_to_top);
                    }
                });
            }
        } else if (view.getId() == R.id.currencyEditText) {
            Intent intent = new Intent(this, CurrencyPicker.class);
            intent.putExtra(FirebaseAnalytics.Param.CURRENCY, this.subCode);
            intent.putExtra("currencyList", this.currencyList);
            startActivityForResult(intent, 7);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7 && resultCode == -1 && data != null) {
            this.subCode = DataHelper.getCurrencyCode().get(data.getIntExtra(FirebaseAnalytics.Param.INDEX, 0));
            this.currencyEditText.setText(DataHelper.getCurrencyList().get(DataHelper.getCurrencyCode().indexOf(this.subCode)));
            updateRate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRate() {
        if (this.amountEditText.getText().toString().length() > 0) {
            try {
                double parseDouble = Double.parseDouble(this.amountEditText.getText().toString());
                if (parseDouble <= Utils.DOUBLE_EPSILON) {
                    this.isComplete = false;
                    TextView textView = this.rateLabel;
                    textView.setText("1.00 " + this.subCode + " = 0.00 " + this.mainCode);
                    return;
                }
                DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ENGLISH);
                decimalFormat.setMaximumFractionDigits(8);
                decimalFormat.setMinimumFractionDigits(2);
                decimalFormat.setGroupingUsed(false);
                TextView textView2 = this.rateLabel;
                textView2.setText("1.00 " + this.subCode + " = " + decimalFormat.format(parseDouble) + StringUtils.SPACE + this.mainCode);
                this.isComplete = true;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                TextView textView3 = this.rateLabel;
                textView3.setText("1.00 " + this.subCode + " = 0.00 " + this.mainCode);
                this.isComplete = false;
            }
        } else {
            TextView textView4 = this.rateLabel;
            textView4.setText("1.00 " + this.subCode + " = 0.00 " + this.mainCode);
            this.isComplete = false;
        }
        checkIsComplete();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkIsComplete() {
        this.saveLabel.setAlpha(this.isComplete ? 1.0f : 0.35f);
    }
}
