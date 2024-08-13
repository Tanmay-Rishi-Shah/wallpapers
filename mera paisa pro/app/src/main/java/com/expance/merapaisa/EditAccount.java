package com.expance.merapaisa;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.android.billingclient.api.BillingFlowParams;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.Entity.AccountEntity;
import com.expance.merapaisa.Database.Entity.CurrencyEntity;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.ArrayList;
import java.util.concurrent.Executors;

import vocsy.ads.CustomAdsListener;
import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class EditAccount extends AppCompatActivity implements TextWatcher, View.OnClickListener {
    int accountId;
    String currencyCode;
    EditText currencyEditText;
    ArrayList<String> currencyList;
    ArrayList<String> currencySymbolList;
    boolean isComplete;
    EditText nameEditText;
    String originalCode;
    TextView saveLabel;

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
        outState.putString("originalCode", this.originalCode);
        outState.putString("currencyCode", this.currencyCode);
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
        setContentView(R.layout.activity_edit_account);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        this.isComplete = true;
        this.accountId = getIntent().getIntExtra(BillingFlowParams.EXTRA_PARAM_KEY_ACCOUNT_ID, 0);
        this.currencyList = DataHelper.getCurrencyList();
        this.currencySymbolList = DataHelper.getCurrencySymbolList();
        setUpLayout();
        if (savedInstanceState != null) {
            this.originalCode = savedInstanceState.getString("originalCode");
            this.currencyCode = savedInstanceState.getString("currencyCode");
        }
        populateData(savedInstanceState != null);
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

    private void populateData(boolean isSavedInstanceState) {
        if (isSavedInstanceState) {
            return;
        }
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.EditAccount.1
            @Override // java.lang.Runnable
            public void run() {
                final AccountEntity entityById = AppDatabaseObject.getInstance(EditAccount.this.getApplicationContext()).accountDaoObject().getEntityById(EditAccount.this.accountId);
                EditAccount.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.EditAccount.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        EditAccount.this.nameEditText.setText(entityById.getName());
                        String currency = entityById.getCurrency();
                        EditAccount.this.originalCode = currency;
                        EditAccount.this.currencyCode = EditAccount.this.originalCode;
                        EditAccount.this.currencyEditText.setText(DataHelper.getCurrencyList().get(DataHelper.getCurrencyCode().indexOf(currency)));
                    }
                });
            }
        });
    }

    private void setUpLayout() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.edit_account));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.currencyEditText = (EditText) findViewById(R.id.currencyEditText);
        this.nameEditText = (EditText) findViewById(R.id.nameEditText);
        this.saveLabel = (TextView) findViewById(R.id.saveLabel);
        this.nameEditText.addTextChangedListener(this);
        this.currencyEditText.setFocusable(false);
        this.currencyEditText.setOnClickListener(this);
        this.currencyEditText.setLongClickable(false);
        this.saveLabel.setOnClickListener(this);
        checkIsComplete();
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
        checkIsComplete();
    }

    @Override // androidx.appcompat.app.AppCompatActivity
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(R.anim.scale_in, R.anim.bottom_to_top);
        return true;
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.scale_in, R.anim.bottom_to_top);
    }

    private void updateAccount() {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.EditAccount.2
            @Override // java.lang.Runnable
            public void run() {
                String str = EditAccount.this.currencySymbolList.get(DataHelper.getCurrencyCode().indexOf(EditAccount.this.currencyCode));
                String trim = EditAccount.this.nameEditText.getText().toString().trim();
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(EditAccount.this.getApplicationContext());
                AccountEntity entityById = appDatabaseObject.accountDaoObject().getEntityById(EditAccount.this.accountId);
                entityById.setCurrency(EditAccount.this.currencyCode);
                entityById.setCurrencySymbol(str);
                entityById.setName(trim);
                appDatabaseObject.accountDaoObject().updateAccount(entityById);
                if (SharePreferenceHelper.getAccountId(EditAccount.this.getApplicationContext()) == EditAccount.this.accountId) {
                    SharePreferenceHelper.setAccount(EditAccount.this.getApplicationContext(), EditAccount.this.accountId, str, trim);
                }
                if (!EditAccount.this.currencyCode.equals(EditAccount.this.originalCode)) {
                    CurrencyEntity currencyEntityByCode = appDatabaseObject.currencyDaoObject().getCurrencyEntityByCode(EditAccount.this.accountId, EditAccount.this.currencyCode);
                    if (currencyEntityByCode != null) {
                        currencyEntityByCode.setRate(1.0d);
                        appDatabaseObject.currencyDaoObject().updateCurrency(currencyEntityByCode);
                    } else {
                        appDatabaseObject.currencyDaoObject().insertCurrency(new CurrencyEntity(EditAccount.this.currencyCode, 1.0d, EditAccount.this.accountId));
                    }
                }
                EditAccount.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.EditAccount.2.1
                    @Override // java.lang.Runnable
                    public void run() {
                        EditAccount.this.finish();
                        EditAccount.this.overridePendingTransition(R.anim.scale_in, R.anim.bottom_to_top);
                    }
                });
            }
        });
    }

    private void checkIsComplete() {
        boolean z = this.nameEditText.getText().toString().trim().length() > 0;
        this.isComplete = z;
        this.saveLabel.setAlpha(z ? 1.0f : 0.35f);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.saveLabel) {
            if (this.isComplete) {
                GoogleAds.getInstance().showCounterInterstitialAd(EditAccount.this, new CustomAdsListener() {
                    @Override
                    public void onFinish() {
                        updateAccount();
                    }
                });

            }
        } else if (view.getId() == R.id.currencyEditText) {
            Intent intent = new Intent(this, CurrencyPicker.class);
            intent.putExtra(FirebaseAnalytics.Param.CURRENCY, this.currencyList.get(DataHelper.getCurrencyCode().indexOf(this.currencyCode)));
            startActivityForResult(intent, 7);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7 && resultCode == -1 && data != null) {
            int intExtra = data.getIntExtra(FirebaseAnalytics.Param.INDEX, 0);
            this.currencyCode = DataHelper.getCurrencyCode().get(intExtra);
            this.currencyEditText.setText(this.currencyList.get(intExtra));
        }
    }
}
