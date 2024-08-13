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

import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.Entity.TemplateEntity;
import com.expance.merapaisa.Model.Template;
import com.expance.merapaisa.Model.Wallets;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.List;
import java.util.concurrent.Executors;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

import vocsy.ads.CustomAdsListener;
import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class CreateTemplate extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    long amount;
    EditText amountEditText;
    String category = "";
    EditText categoryEditText;
    int categoryId;
    EditText descriptionEditText;
    int id;
    boolean isComplete;
    EditText nameEditText;
    EditText noteEditText;
    TextView saveLabel;
    int subcategoryId;
    int type;
    EditText walletEditText;
    int walletId;
    List<Wallets> walletList;

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
        outState.putLong("amount", this.amount);
        outState.putInt("walletId", this.walletId);
        outState.putInt("categoryId", this.categoryId);
        outState.putInt("subcategoryId", this.subcategoryId);
        outState.putString("category", this.category);
        outState.putInt(JamXmlElements.TYPE, this.type);
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
        setContentView(R.layout.activity_create_template);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        this.id = getIntent().getIntExtra("id", 0);
        setUpLayout();
        if (savedInstanceState != null) {
            this.amount = savedInstanceState.getLong("amount");
            this.walletId = savedInstanceState.getInt("walletId");
            this.categoryId = savedInstanceState.getInt("categoryId");
            this.subcategoryId = savedInstanceState.getInt("subcategoryId");
            this.category = savedInstanceState.getString("category");
            this.type = savedInstanceState.getInt(JamXmlElements.TYPE);
        }
        populateData(savedInstanceState != null);
        checkIsComplete();
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

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.ktwapps.walletmanager.CreateTemplate$1  reason: invalid class name */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 implements Runnable {
        final /* synthetic */ boolean val$isSavedInstanceState;

        AnonymousClass1(final boolean val$isSavedInstanceState) {
            this.val$isSavedInstanceState = val$isSavedInstanceState;
        }

        @Override // java.lang.Runnable
        public void run() {
            AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(CreateTemplate.this.getApplicationContext());
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.set(14, 0);
            calendar.set(13, 0);
            calendar.set(12, 0);
            calendar.set(11, 0);
            calendar.add(5, 1);
            if (!SharePreferenceHelper.isFutureBalanceOn(CreateTemplate.this.getApplicationContext())) {
                calendar.set(1, 10000);
            }
            CreateTemplate.this.walletList = appDatabaseObject.walletDaoObject().getWallets(SharePreferenceHelper.getAccountId(CreateTemplate.this.getApplicationContext()), 0, calendar.getTimeInMillis());
            if (CreateTemplate.this.id == 0 || this.val$isSavedInstanceState) {
                return;
            }
            Template templateById = appDatabaseObject.templateDaoObject().getTemplateById(CreateTemplate.this.id);
            CreateTemplate.this.amount = templateById.getAmount();
            CreateTemplate.this.walletId = templateById.getWalletId();
            CreateTemplate.this.categoryId = templateById.getCategoryId();
            CreateTemplate.this.subcategoryId = templateById.getSubcategoryId();
            CreateTemplate createTemplate = CreateTemplate.this;
            createTemplate.category = templateById.getCategory(createTemplate.getApplicationContext());
            CreateTemplate.this.type = templateById.getType();
            final String name = templateById.getName();
            final String memo = templateById.getMemo();
            final String note = templateById.getNote();
            CreateTemplate.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateTemplate$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    CreateTemplate.AnonymousClass1.this.m201lambda$run$0$comktwappswalletmanagerCreateTemplate$1(name, note, memo);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$run$0$com-ktwapps-walletmanager-CreateTemplate$1  reason: not valid java name */
        public /* synthetic */ void m201lambda$run$0$comktwappswalletmanagerCreateTemplate$1(String str, String str2, String str3) {
            CreateTemplate.this.nameEditText.setText(str);
            CreateTemplate.this.descriptionEditText.setText(str2);
            EditText editText = CreateTemplate.this.noteEditText;
            if (str3 == null) {
                str3 = "";
            }
            editText.setText(str3);
            CreateTemplate.this.categoryEditText.setText(CreateTemplate.this.category);
            CreateTemplate createTemplate = CreateTemplate.this;
            createTemplate.setWallet(createTemplate.walletId);
            CreateTemplate.this.checkIsComplete();
        }
    }

    private void populateData(final boolean isSavedInstanceState) {
        Executors.newSingleThreadExecutor().execute(new AnonymousClass1(isSavedInstanceState));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setWallet(int id) {
        String str = null;
        String str2 = "";
        String str3 = null;
        for (Wallets wallets : this.walletList) {
            if (wallets.getId() == id) {
                str3 = DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(wallets.getCurrency()));
                str2 = wallets.getName() + " • " + Helper.getBeautifyAmount(str, wallets.getAmount());
            }
        }
        if (str3 == null) {
            str3 = SharePreferenceHelper.getAccountSymbol(this);
        }
        this.walletEditText.setText(str2);
        this.amountEditText.setText(Helper.getBeautifyAmount(str3, this.amount));
    }

    public void setUpLayout() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.create_template);
        }
        this.saveLabel = (TextView) findViewById(R.id.saveLabel);
        this.nameEditText = (EditText) findViewById(R.id.nameEditText);
        this.amountEditText = (EditText) findViewById(R.id.amountEditText);
        this.categoryEditText = (EditText) findViewById(R.id.categoryEditText);
        this.walletEditText = (EditText) findViewById(R.id.walletEditText);
        this.noteEditText = (EditText) findViewById(R.id.noteEditText);
        this.descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        this.amountEditText.setOnClickListener(this);
        this.amountEditText.setLongClickable(false);
        this.amountEditText.setFocusable(false);
        this.categoryEditText.setOnClickListener(this);
        this.categoryEditText.setLongClickable(false);
        this.categoryEditText.setFocusable(false);
        this.walletEditText.setOnClickListener(this);
        this.walletEditText.setLongClickable(false);
        this.walletEditText.setFocusable(false);
        this.nameEditText.addTextChangedListener(this);
        this.saveLabel.setOnClickListener(this);
        this.amountEditText.setText(Helper.getBeautifyAmount(SharePreferenceHelper.getAccountSymbol(this), this.amount));
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

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.amountEditText /* 2131230807 */) {
            Intent intent = new Intent(getApplicationContext(), Calculator.class);
            intent.putExtra("amount", this.amount);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        } else if (view.getId() == R.id.categoryEditText /* 2131230881 */) {
            Intent intent2 = new Intent(this, CategoryPicker.class);
            intent2.putExtra("id", this.categoryId);
            intent2.putExtra("subcategoryId", this.subcategoryId);
            intent2.putExtra(JamXmlElements.TYPE, this.type == 0 ? 1 : 2);
            startActivityForResult(intent2, 5);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        } else if (view.getId() == R.id.saveLabel /* 2131231463 */) {
            if (this.isComplete) {
                GoogleAds.getInstance().showCounterInterstitialAd(CreateTemplate.this, new CustomAdsListener() {
                    @Override
                    public void onFinish() {
                        if (id != 0) {
                            editTemplate();
                        } else {
                            createTemplate();
                        }
                    }
                });

            }
        } else if (view.getId() == R.id.walletEditText /* 2131231677 */) {
            Intent intent3 = new Intent(this, WalletPicker.class);
            intent3.putExtra("id", this.walletId);
            startActivityForResult(intent3, 2);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            if (requestCode == 1) {
                long longExtra = data.getLongExtra("amount", 0L);
                this.amount = longExtra;
                this.amount = longExtra >= 0 ? longExtra : 0L;
                setWallet(this.walletId);
            } else if (requestCode == 5) {
                this.category = data.getStringExtra("name");
                this.categoryId = data.getIntExtra("id", 0);
                this.subcategoryId = data.getIntExtra("subcategoryId", 0);
                this.type = data.getIntExtra(JamXmlElements.TYPE, 1);
                this.categoryEditText.setText(this.category);
            } else if (requestCode == 2) {
                int intExtra = data.getIntExtra("id", 0);
                this.walletId = intExtra;
                setWallet(intExtra);
            }
        }
    }

    private void createTemplate() {
        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateTemplate.2
            @Override // java.lang.Runnable
            public void run() {
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(CreateTemplate.this);
                String trim = CreateTemplate.this.nameEditText.getText().toString().trim();
                String trim2 = CreateTemplate.this.descriptionEditText.getText().toString().trim();
                String trim3 = CreateTemplate.this.noteEditText.getText().toString().trim();
                int accountId = SharePreferenceHelper.getAccountId(CreateTemplate.this.getApplicationContext());
                appDatabaseObject.templateDaoObject().insertTemplate(new TemplateEntity(trim, trim2, trim3, CreateTemplate.this.amount, CreateTemplate.this.categoryId, CreateTemplate.this.walletId, accountId, appDatabaseObject.templateDaoObject().getTemplateLastOrdering(accountId), CreateTemplate.this.subcategoryId));
                CreateTemplate.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateTemplate.2.1
                    @Override // java.lang.Runnable
                    public void run() {
                        CreateTemplate.this.finish();
                        CreateTemplate.this.overridePendingTransition(R.anim.scale_in, R.anim.bottom_to_top);
                    }
                });
            }
        });
    }

    private void editTemplate() {
        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateTemplate.3
            @Override // java.lang.Runnable
            public void run() {
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(CreateTemplate.this);
                TemplateEntity templateEntityById = appDatabaseObject.templateDaoObject().getTemplateEntityById(CreateTemplate.this.id);
                String trim = CreateTemplate.this.nameEditText.getText().toString().trim();
                String trim2 = CreateTemplate.this.descriptionEditText.getText().toString().trim();
                String trim3 = CreateTemplate.this.noteEditText.getText().toString().trim();
                templateEntityById.setAmount(CreateTemplate.this.amount);
                templateEntityById.setCategoryId(CreateTemplate.this.categoryId);
                templateEntityById.setWalletId(CreateTemplate.this.walletId);
                templateEntityById.setSubcategoryId(CreateTemplate.this.subcategoryId);
                templateEntityById.setName(trim);
                templateEntityById.setNote(trim2);
                templateEntityById.setMemo(trim3);
                appDatabaseObject.templateDaoObject().updateTemplate(templateEntityById);
                CreateTemplate.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateTemplate.3.1
                    @Override // java.lang.Runnable
                    public void run() {
                        CreateTemplate.this.finish();
                        CreateTemplate.this.overridePendingTransition(R.anim.scale_in, R.anim.bottom_to_top);
                    }
                });
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkIsComplete() {
        boolean z = this.nameEditText.getText().toString().trim().length() > 0;
        this.isComplete = z;
        this.saveLabel.setAlpha(z ? 1.0f : 0.35f);
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
        checkIsComplete();
    }
}
