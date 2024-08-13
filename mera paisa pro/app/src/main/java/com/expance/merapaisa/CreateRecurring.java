package com.expance.merapaisa;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.Entity.RecurringEntity;
import com.expance.merapaisa.Database.Entity.TransEntity;
import com.expance.merapaisa.Model.Recurring;
import com.expance.merapaisa.Model.Wallets;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Utility.DateHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.RecurringHelper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import com.expance.merapaisa.Widget.BottomRecurringTransactionDialog;

import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class CreateRecurring extends AppCompatActivity implements View.OnClickListener, BottomRecurringTransactionDialog.OnItemClickListener, DatePickerDialog.OnDateSetListener {
    long amount;
    EditText amountEditText;
    EditText categoryEditText;
    Date date;
    EditText dateEditText;
    EditText descriptionEditText;
    private String expenseCategory;
    int expenseCategoryId;
    TextView expenseLabel;
    int expenseSubcategoryId;
    ConstraintLayout expenseWrapper;
    private String incomeCategory;
    int incomeCategoryId;
    TextView incomeLabel;
    int incomeSubcategoryId;
    ConstraintLayout incomeWrapper;
    boolean isRecurring;
    EditText noteEditText;
    Recurring recurring;
    EditText recurringEditText;
    int recurringIncrement;
    String recurringRepeatDate;
    int recurringRepeatTime;
    int recurringRepeatType;
    int recurringType;
    Date recurringUntilDate;
    int recurringUntilType;
    TextView saveLabel;
    Switch switchView;
    EditText walletEditText;
    int walletId;
    private List<Wallets> walletList;
    int recurringId = 0;
    int mode = 10;
    int type = 1;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("expenseCategory", this.expenseCategory);
        outState.putString("incomeCategory", this.incomeCategory);
        outState.putInt(JamXmlElements.TYPE, this.type);
        outState.putInt("expenseCategoryId", this.expenseCategoryId);
        outState.putInt("incomeCategoryId", this.incomeCategoryId);
        outState.putInt("incomeSubcategoryId", this.incomeSubcategoryId);
        outState.putInt("expenseSubcategoryId", this.expenseSubcategoryId);
        outState.putInt("walletId", this.walletId);
        outState.putLong("amount", this.amount);
        outState.putLong("date", this.date.getTime());
        outState.putLong("recurringUntilDate", this.recurringUntilDate.getTime());
        outState.putString("recurringRepeatDate", this.recurringRepeatDate);
        outState.putInt("recurringType", this.recurringType);
        outState.putInt("recurringRepeatType", this.recurringRepeatType);
        outState.putInt("recurringUntilType", this.recurringUntilType);
        outState.putInt("recurringIncrement", this.recurringIncrement);
        outState.putInt("recurringRepeatTime", this.recurringRepeatTime);
    }

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
        setContentView(R.layout.activity_create_recurring);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        this.recurringUntilDate = java.util.Calendar.getInstance().getTime();
        this.recurringRepeatDate = "0000000";
        this.recurringType = 0;
        this.recurringRepeatType = 0;
        this.recurringUntilType = 0;
        this.recurringIncrement = 1;
        this.recurringRepeatTime = 1;
        this.date = DateHelper.getCurrentDateTime();
        this.expenseCategory = "";
        this.incomeCategory = "";
        this.expenseCategoryId = 0;
        this.incomeCategoryId = 0;
        this.incomeSubcategoryId = 0;
        this.expenseSubcategoryId = 0;
        this.walletId = -1;
        this.amount = 0L;
        int intExtra = getIntent().getIntExtra(JamXmlElements.TYPE, 10);
        this.mode = intExtra;
        if (intExtra == -10) {
            this.recurringId = getIntent().getIntExtra("recurringId", -1);
            populateData(savedInstanceState != null);
        }
        populateWallet();
        setUpLayout();
        checkIsComplete();
        if (savedInstanceState != null) {
            this.expenseCategory = savedInstanceState.getString("expenseCategory");
            this.incomeCategory = savedInstanceState.getString("incomeCategory");
            this.type = savedInstanceState.getInt(JamXmlElements.TYPE);
            this.expenseCategoryId = savedInstanceState.getInt("expenseCategoryId");
            this.incomeCategoryId = savedInstanceState.getInt("incomeCategoryId");
            this.expenseSubcategoryId = savedInstanceState.getInt("expenseSubcategoryId");
            this.incomeSubcategoryId = savedInstanceState.getInt("incomeSubcategoryId");
            this.walletId = savedInstanceState.getInt("walletId");
            this.amount = savedInstanceState.getLong("amount");
            this.recurringRepeatDate = savedInstanceState.getString("recurringRepeatDate");
            this.recurringType = savedInstanceState.getInt("recurringType");
            this.recurringRepeatType = savedInstanceState.getInt("recurringRepeatType");
            this.recurringUntilType = savedInstanceState.getInt("recurringUntilType");
            this.recurringIncrement = savedInstanceState.getInt("recurringIncrement");
            this.recurringRepeatTime = savedInstanceState.getInt("recurringRepeatTime");
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTimeInMillis(savedInstanceState.getLong("date"));
            this.date = calendar.getTime();
            calendar.setTimeInMillis(savedInstanceState.getLong("recurringUntilDate"));
            this.recurringUntilDate = calendar.getTime();
            this.dateEditText.setText(DateHelper.getFormattedDate(getApplicationContext(), this.date));
            this.categoryEditText.setText(this.type == 1 ? this.expenseCategory : this.incomeCategory);
            setWallet(this.walletId);
            switchTransMode(this.type);
            validateRecurring();
            checkIsComplete();
        }
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

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.ktwapps.walletmanager.CreateRecurring$1  reason: invalid class name */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 implements Runnable {
        final /* synthetic */ boolean val$isSavedInstanceState;

        AnonymousClass1(final boolean val$isSavedInstanceState) {
            this.val$isSavedInstanceState = val$isSavedInstanceState;
        }

        @Override // java.lang.Runnable
        public void run() {
            CreateRecurring.this.recurring = AppDatabaseObject.getInstance(CreateRecurring.this.getApplicationContext()).recurringDaoObject().getRecurringById(SharePreferenceHelper.getAccountId(CreateRecurring.this.getApplicationContext()), CreateRecurring.this.recurringId);
            if (this.val$isSavedInstanceState) {
                return;
            }
            final String str = CreateRecurring.this.recurring.note;
            final String memo = CreateRecurring.this.recurring.getMemo();
            CreateRecurring createRecurring = CreateRecurring.this;
            createRecurring.date = createRecurring.recurring.getDateTime();
            CreateRecurring createRecurring2 = CreateRecurring.this;
            createRecurring2.walletId = createRecurring2.recurring.getWalletId();
            CreateRecurring createRecurring3 = CreateRecurring.this;
            int i = (createRecurring3.recurring.getAmount() > 0L ? 1 : (createRecurring3.recurring.getAmount() == 0L ? 0 : -1));
            long amount = CreateRecurring.this.recurring.getAmount();
            if (i < 0) {
                amount = -amount;
            }
            createRecurring3.amount = amount;
            CreateRecurring createRecurring4 = CreateRecurring.this;
            createRecurring4.type = createRecurring4.recurring.getType();
            CreateRecurring createRecurring5 = CreateRecurring.this;
            createRecurring5.recurringUntilDate = createRecurring5.recurring.getUntilTime().getTime() == 0 ? java.util.Calendar.getInstance().getTime() : CreateRecurring.this.recurring.getUntilTime();
            CreateRecurring createRecurring6 = CreateRecurring.this;
            createRecurring6.recurringRepeatDate = createRecurring6.recurring.getRepeatDate();
            CreateRecurring createRecurring7 = CreateRecurring.this;
            createRecurring7.recurringType = createRecurring7.recurring.getRecurringType();
            CreateRecurring createRecurring8 = CreateRecurring.this;
            createRecurring8.recurringRepeatType = createRecurring8.recurring.getRepeatType();
            CreateRecurring createRecurring9 = CreateRecurring.this;
            createRecurring9.recurringUntilType = createRecurring9.recurring.getUntilTime().getTime() != 0 ? 1 : 0;
            CreateRecurring createRecurring10 = CreateRecurring.this;
            createRecurring10.recurringIncrement = createRecurring10.recurring.getIncrement();
            if (CreateRecurring.this.type == 1) {
                CreateRecurring createRecurring11 = CreateRecurring.this;
                createRecurring11.expenseCategoryId = createRecurring11.recurring.getCategoryId();
                CreateRecurring createRecurring12 = CreateRecurring.this;
                createRecurring12.expenseCategory = createRecurring12.recurring.getCategory(CreateRecurring.this.getApplicationContext());
                CreateRecurring createRecurring13 = CreateRecurring.this;
                createRecurring13.expenseSubcategoryId = createRecurring13.recurring.getSubcategoryId();
            } else {
                CreateRecurring createRecurring14 = CreateRecurring.this;
                createRecurring14.incomeCategoryId = createRecurring14.recurring.getCategoryId();
                CreateRecurring createRecurring15 = CreateRecurring.this;
                createRecurring15.incomeCategory = createRecurring15.recurring.getCategory(CreateRecurring.this.getApplicationContext());
                CreateRecurring createRecurring16 = CreateRecurring.this;
                createRecurring16.incomeSubcategoryId = createRecurring16.recurring.getSubcategoryId();
            }
            CreateRecurring.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateRecurring$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    CreateRecurring.AnonymousClass1.this.m194lambda$run$0$comktwappswalletmanagerCreateRecurring$1(memo, str);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$run$0$com-ktwapps-walletmanager-CreateRecurring$1  reason: not valid java name */
        public /* synthetic */ void m194lambda$run$0$comktwappswalletmanagerCreateRecurring$1(String str, String str2) {
            String str3;
            CreateRecurring.this.switchView.setChecked(CreateRecurring.this.recurring.getIsFuture() == 1);
            CreateRecurring.this.dateEditText.setText(DateHelper.getFormattedDate(CreateRecurring.this.getApplicationContext(), CreateRecurring.this.date));
            CreateRecurring.this.noteEditText.setText(str);
            CreateRecurring.this.descriptionEditText.setText(str2);
            CreateRecurring.this.categoryEditText.setText(CreateRecurring.this.recurring.getType() == 1 ? CreateRecurring.this.expenseCategory : CreateRecurring.this.incomeCategory);
            CreateRecurring createRecurring = CreateRecurring.this;
            createRecurring.switchTransMode(createRecurring.type);
            CreateRecurring createRecurring2 = CreateRecurring.this;
            createRecurring2.setWallet(createRecurring2.walletId);
            if (CreateRecurring.this.getWallet() != null) {
                str3 = DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(CreateRecurring.this.getWallet().getCurrency()));
            } else {
                str3 = SharePreferenceHelper.getAccountSymbol(CreateRecurring.this.getApplicationContext());
            }
            CreateRecurring.this.amountEditText.setText(Helper.getBeautifyAmount(str3, CreateRecurring.this.amount));
            CreateRecurring.this.validateRecurring();
            CreateRecurring.this.checkIsComplete();
        }
    }

    private void populateData(final boolean isSavedInstanceState) {
        Executors.newSingleThreadExecutor().execute(new AnonymousClass1(isSavedInstanceState));
    }

    private void populateWallet() {
        this.walletList = new ArrayList();
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateRecurring.2
            @Override // java.lang.Runnable
            public void run() {
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(CreateRecurring.this.getApplicationContext());
                java.util.Calendar calendar = java.util.Calendar.getInstance();
                calendar.set(14, 0);
                calendar.set(13, 0);
                calendar.set(12, 0);
                calendar.set(11, 0);
                calendar.add(5, 1);
                if (!SharePreferenceHelper.isFutureBalanceOn(CreateRecurring.this.getApplicationContext())) {
                    calendar.set(1, 10000);
                }
                CreateRecurring.this.walletList = appDatabaseObject.walletDaoObject().getWallets(SharePreferenceHelper.getAccountId(CreateRecurring.this.getApplicationContext()), 0, calendar.getTimeInMillis());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Wallets getWallet() {
        for (Wallets wallets : this.walletList) {
            if (this.walletId == wallets.getId()) {
                return wallets;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setWallet(int id) {
        String str = "";
        for (Wallets wallets : this.walletList) {
            if (wallets.getId() == id) {
                str = wallets.getName() + " • " + Helper.getBeautifyAmount(DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(wallets.getCurrency())), wallets.getAmount());
            }
        }
        this.walletEditText.setText(str);
    }

    private void setUpLayout() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.expense));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.incomeWrapper = (ConstraintLayout) findViewById(R.id.incomeWrapper);
        this.expenseWrapper = (ConstraintLayout) findViewById(R.id.expenseWrapper);
        this.incomeLabel = (TextView) findViewById(R.id.incomeLabel);
        this.expenseLabel = (TextView) findViewById(R.id.expenseLabel);
        this.saveLabel = (TextView) findViewById(R.id.saveLabel);
        this.categoryEditText = (EditText) findViewById(R.id.categoryEditText);
        this.walletEditText = (EditText) findViewById(R.id.walletEditText);
        this.amountEditText = (EditText) findViewById(R.id.amountEditText);
        this.noteEditText = (EditText) findViewById(R.id.noteEditText);
        this.dateEditText = (EditText) findViewById(R.id.dateEditText);
        this.recurringEditText = (EditText) findViewById(R.id.recurringEditText);
        this.switchView = (Switch) findViewById(R.id.switchView);
        this.descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        this.amountEditText.setText(Helper.getBeautifyAmount(SharePreferenceHelper.getAccountSymbol(this), this.amount));
        this.dateEditText.setText(DateHelper.getFormattedDate(getApplicationContext(), this.date));
        this.saveLabel.setOnClickListener(this);
        this.incomeWrapper.setOnClickListener(this);
        this.expenseWrapper.setOnClickListener(this);
        this.dateEditText.setFocusable(false);
        this.dateEditText.setOnClickListener(this);
        this.dateEditText.setLongClickable(false);
        this.recurringEditText.setFocusable(false);
        this.recurringEditText.setOnClickListener(this);
        this.recurringEditText.setLongClickable(false);
        this.walletEditText.setFocusable(false);
        this.walletEditText.setOnClickListener(this);
        this.walletEditText.setLongClickable(false);
        this.amountEditText.setFocusable(false);
        this.amountEditText.setOnClickListener(this);
        this.amountEditText.setLongClickable(false);
        this.categoryEditText.setFocusable(false);
        this.categoryEditText.setOnClickListener(this);
        this.categoryEditText.setLongClickable(false);
        switchTransMode(this.type);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void switchTransMode(int i) {
        this.type = i;
        this.incomeWrapper.setBackgroundColor(i == 0 ? getResources().getColor(R.color.income) : Helper.getAttributeColor(this, R.attr.secondaryBackground));
        this.expenseWrapper.setBackgroundColor(i == 1 ? getResources().getColor(R.color.expense) : Helper.getAttributeColor(this, R.attr.secondaryBackground));
        this.incomeLabel.setTextColor(i == 0 ? getResources().getColor(17170443) : Helper.getAttributeColor(this, R.attr.secondaryTextColor));
        this.expenseLabel.setTextColor(i == 1 ? getResources().getColor(17170443) : Helper.getAttributeColor(this, R.attr.secondaryTextColor));
        this.categoryEditText.setText(i == 1 ? this.expenseCategory : this.incomeCategory);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(i == 0 ? R.string.income : R.string.expense);
        }
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

    private boolean isComplete() {
        return this.type == 1 ? (this.amount == 0 || this.expenseCategoryId == 0 || this.walletId == -1 || !this.isRecurring) ? false : true : (this.amount == 0 || this.incomeCategoryId == 0 || this.walletId == -1 || !this.isRecurring) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkIsComplete() {
        if (isComplete()) {
            this.saveLabel.setAlpha(1.0f);
        } else {
            this.saveLabel.setAlpha(0.35f);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.amountEditText /* 2131230807 */) {
            Intent intent = new Intent(this, Calculator.class);
            intent.putExtra("amount", this.amount);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        } else if (view.getId() == R.id.categoryEditText /* 2131230881 */) {
            Intent intent2 = new Intent(this, CategoryPicker.class);
            intent2.putExtra("id", this.type == 0 ? this.incomeCategoryId : this.expenseCategoryId);
            intent2.putExtra("subcategoryId", this.type == 0 ? this.incomeSubcategoryId : this.expenseSubcategoryId);
            intent2.putExtra(JamXmlElements.TYPE, this.type == 0 ? 1 : 2);
            startActivityForResult(intent2, 5);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        } else if (view.getId() == R.id.dateEditText /* 2131230957 */) {
            openDateDialog();
        } else if (view.getId() == R.id.expenseWrapper /* 2131231073 */) {
            switchTransMode(1);
        } else if (view.getId() == R.id.incomeWrapper /* 2131231182 */) {
            switchTransMode(0);
        } else if (view.getId() == R.id.recurringEditText /* 2131231437 */) {
            BottomRecurringTransactionDialog bottomRecurringTransactionDialog = new BottomRecurringTransactionDialog();
            bottomRecurringTransactionDialog.setData(this.date, this.recurringUntilDate, this.recurringRepeatDate, this.recurringType, this.recurringRepeatType, this.recurringRepeatTime, this.recurringUntilType, this.recurringIncrement);
            bottomRecurringTransactionDialog.setListener(this);
            bottomRecurringTransactionDialog.show(getSupportFragmentManager(), "recurring");
        } else if (view.getId() == R.id.saveLabel /* 2131231463 */) {
            if (isComplete()) {
                final String obj = this.noteEditText.getText().toString();
                final String obj2 = this.descriptionEditText.getText().toString();
                int i = this.type;
                final int i2 = i == 1 ? this.expenseCategoryId : this.incomeCategoryId;
                final int i3 = i == 1 ? this.expenseSubcategoryId : this.incomeSubcategoryId;
                int accountId = SharePreferenceHelper.getAccountId(this);
                if (this.isRecurring) {
                    if (this.type == 1) {
                        this.amount = -this.amount;
                    }
                    final long untilDate = RecurringHelper.getUntilDate(this.date, this.recurringUntilDate, this.recurringUntilType, this.recurringType, this.recurringIncrement, this.recurringRepeatTime, this.recurringRepeatType, this.recurringRepeatDate);
                    final long addRecurringTransaction = addRecurringTransaction(untilDate, this.mode == 10 ? 0L : this.recurring.getLastUpdateTime().getTime(), obj2, obj, this.date, accountId, i2, i3);
                    if (this.mode == 10) {
                        if (this.isRecurring) {
                            insertRecurring(obj2, obj, this.type, i2, i3, addRecurringTransaction);
                        }
                    } else {
                        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateRecurring.3
                            @Override // java.lang.Runnable
                            public void run() {
                                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(CreateRecurring.this.getApplicationContext());
                                if (CreateRecurring.this.isRecurring) {
                                    java.util.Calendar calendar = java.util.Calendar.getInstance();
                                    calendar.setTimeInMillis(untilDate);
                                    Date time = calendar.getTime();
                                    calendar.setTimeInMillis(addRecurringTransaction);
                                    Date time2 = calendar.getTime();
                                    RecurringEntity recurringEntity = appDatabaseObject.recurringDaoObject().getRecurringEntity(CreateRecurring.this.recurring.getId());
                                    recurringEntity.setNote(obj2);
                                    recurringEntity.setMemo(obj);
                                    recurringEntity.setType(CreateRecurring.this.type);
                                    recurringEntity.setIsFuture(CreateRecurring.this.switchView.isChecked() ? 1 : 0);
                                    recurringEntity.setRecurringType(CreateRecurring.this.recurringType);
                                    recurringEntity.setRepeatType(CreateRecurring.this.recurringRepeatType);
                                    recurringEntity.setRepeatDate(CreateRecurring.this.recurringRepeatDate);
                                    recurringEntity.setIncrement(CreateRecurring.this.recurringIncrement);
                                    recurringEntity.setAmount(CreateRecurring.this.amount);
                                    recurringEntity.setDateTime(CreateRecurring.this.date);
                                    recurringEntity.setUntilTime(time);
                                    recurringEntity.setLastUpdateTime(time2);
                                    recurringEntity.setCategoryId(i2);
                                    recurringEntity.setWalletId(CreateRecurring.this.walletId);
                                    recurringEntity.setSubcategoryId(i3);
                                    appDatabaseObject.recurringDaoObject().updateRecurring(recurringEntity);
                                    return;
                                }
                                appDatabaseObject.recurringDaoObject().deleteRecurring(CreateRecurring.this.recurring.getId());
                            }
                        });
                    }
                    runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateRecurring.4
                        @Override // java.lang.Runnable
                        public void run() {
                            CreateRecurring.this.finish();
                        }
                    });
                }
            }
        } else if (view.getId() == R.id.walletEditText) {
            Intent intent3 = new Intent(this, WalletPicker.class);
            intent3.putExtra("id", this.walletId);
            intent3.putExtra(JamXmlElements.TYPE, -11);
            startActivityForResult(intent3, 2);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        }
        checkIsComplete();
    }

    public void openDateDialog() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(this.date);
        new DatePickerDialog(this, this, calendar.get(1), calendar.get(2), calendar.get(5)).show();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String str;
        String str2;
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            if (requestCode == 1) {
                long longExtra = data.getLongExtra("amount", 0L);
                this.amount = longExtra;
                this.amount = longExtra > 0 ? longExtra : 0L;
                if (getWallet() == null) {
                    str2 = SharePreferenceHelper.getAccountSymbol(this);
                } else {
                    str2 = DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(getWallet().getCurrency()));
                }
                this.amountEditText.setText(Helper.getBeautifyAmount(str2, this.amount));
            } else if (requestCode == 5) {
                String stringExtra = data.getStringExtra("name");
                int intExtra = data.getIntExtra("id", 0);
                int intExtra2 = data.getIntExtra("subcategoryId", 0);
                int intExtra3 = data.getIntExtra(JamXmlElements.TYPE, 1);
                if (intExtra3 == 1) {
                    this.expenseCategory = stringExtra;
                    this.expenseCategoryId = intExtra;
                    this.expenseSubcategoryId = intExtra2;
                } else {
                    this.incomeCategory = stringExtra;
                    this.incomeCategoryId = intExtra;
                    this.incomeSubcategoryId = intExtra2;
                }
                this.categoryEditText.setText(stringExtra);
                switchTransMode(intExtra3);
            } else if (requestCode == 2) {
                int intExtra4 = data.getIntExtra("id", -1);
                setWallet(intExtra4);
                this.walletId = intExtra4;
                if (getWallet() == null) {
                    str = SharePreferenceHelper.getAccountSymbol(this);
                } else {
                    str = DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(getWallet().getCurrency()));
                }
                this.amountEditText.setText(Helper.getBeautifyAmount(str, this.amount));
            }
            checkIsComplete();
        }
    }

    @Override
    // com.ktwapps.walletmanager.Widget.BottomRecurringTransactionDialog.OnItemClickListener
    public void OnItemClick(int increment, int repeatTime, int untilType, int repeatType, int type, Date untilDate, String repeatDate) {
        this.recurringIncrement = increment;
        this.recurringRepeatTime = repeatTime;
        this.recurringUntilType = untilType;
        this.recurringRepeatType = repeatType;
        this.recurringType = type;
        this.recurringUntilDate = untilDate;
        this.recurringRepeatDate = repeatDate;
        validateRecurring();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void validateRecurring() {
        int i = this.recurringType;
        if (i != 0) {
            long untilDate = RecurringHelper.getUntilDate(this.date, this.recurringUntilDate, this.recurringUntilType, i, this.recurringIncrement, this.recurringRepeatTime, this.recurringRepeatType, this.recurringRepeatDate);
            Date date = this.date;
            if (RecurringHelper.isValidRecurring(untilDate, RecurringHelper.getNextOccurrence(date, date.getTime(), this.recurringType, this.recurringIncrement, this.recurringRepeatType, this.recurringRepeatDate))) {
                this.isRecurring = true;
                int i2 = this.recurringType;
                if (i2 == 1) {
                    this.recurringEditText.setText(R.string.repeat_daily);
                } else if (i2 == 2) {
                    this.recurringEditText.setText(R.string.repeat_weekly);
                } else if (i2 == 3) {
                    this.recurringEditText.setText(R.string.repeat_monthly);
                } else if (i2 == 4) {
                    this.recurringEditText.setText(R.string.repeat_yearly);
                }
                checkIsComplete();
                return;
            }
            this.isRecurring = false;
            checkIsComplete();
            this.recurringEditText.setText("");
            return;
        }
        this.isRecurring = false;
        checkIsComplete();
        this.recurringEditText.setText("");
    }

    private void addTransaction(final String note, final String memo, final Date date, final int accountId, final int categoryId, final int subcategoryId) {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateRecurring.5
            @Override // java.lang.Runnable
            public void run() {
                AppDatabaseObject.getInstance(CreateRecurring.this.getApplicationContext()).transDaoObject().insertTrans(new TransEntity(note, memo, CreateRecurring.this.amount, date, CreateRecurring.this.type, accountId, categoryId, 0, CreateRecurring.this.walletId, -1, 0L, 0, 0, subcategoryId));
            }
        });
    }

    private void insertRecurring(final String note, final String memo, final int type, final int categoryId, final int subcategoryId, final long lastUpdateMilli) {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateRecurring.6
            @Override // java.lang.Runnable
            public void run() {
                if (CreateRecurring.this.recurringType != 0) {
                    AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(CreateRecurring.this.getApplicationContext());
                    long untilDate = RecurringHelper.getUntilDate(CreateRecurring.this.date, CreateRecurring.this.recurringUntilDate, CreateRecurring.this.recurringUntilType, CreateRecurring.this.recurringType, CreateRecurring.this.recurringIncrement, CreateRecurring.this.recurringRepeatTime, CreateRecurring.this.recurringRepeatType, CreateRecurring.this.recurringRepeatDate);
                    java.util.Calendar calendar = java.util.Calendar.getInstance();
                    calendar.setTimeInMillis(untilDate);
                    Date time = calendar.getTime();
                    calendar.setTimeInMillis(lastUpdateMilli);
                    Date time2 = calendar.getTime();
                    String str = note;
                    String str2 = memo;
                    int i = type;
                    int i2 = CreateRecurring.this.recurringType;
                    int i3 = CreateRecurring.this.recurringRepeatType;
                    String str3 = CreateRecurring.this.recurringRepeatDate;
                    int i4 = CreateRecurring.this.recurringIncrement;
                    long j = CreateRecurring.this.amount;
                    Date date = CreateRecurring.this.date;
                    int accountId = SharePreferenceHelper.getAccountId(CreateRecurring.this.getApplicationContext());
                    int i5 = categoryId;
                    int i6 = CreateRecurring.this.walletId;
                    boolean isChecked = CreateRecurring.this.switchView.isChecked();
                    appDatabaseObject.recurringDaoObject().insetRecurring(new RecurringEntity(str, str2, i, i2, i3, str3, i4, j, date, time, time2, accountId, i5, i6, -1, 0L, isChecked ? 1 : 0, subcategoryId));
                }
            }
        });
    }

    private long addRecurringTransaction(long untilTime, long lastUpdateMilli, String note, String memo, Date date, int accountId, int categoryId, int subcategoryId) {
        long j;
        long time;
        long time2;
        int i;
        int i2;
        long time3;
        int i3;
        boolean z;
        int i4;
        boolean z2;
        long time4 = 0;
        long time5;
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(date);
        long todayMillis = DateHelper.getTodayMillis();
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        int i5 = this.recurringType;
        int i6 = 1;
        if (i5 == 1) {
            j = lastUpdateMilli;
            while (true) {
                if (j <= 0) {
                    if (calendar.getTime().getTime() >= todayMillis && !DateHelper.isSameDay(calendar.getTime().getTime(), todayMillis)) {
                        break;
                    }
                    if (untilTime != 0) {
                        if (calendar.getTime().getTime() >= untilTime && !DateHelper.isSameDay(calendar.getTime().getTime(), untilTime)) {
                            break;
                        }
                        time5 = calendar.getTime().getTime();
                        addTransaction(note, memo, calendar.getTime(), accountId, categoryId, subcategoryId);
                    } else {
                        time5 = calendar.getTime().getTime();
                        addTransaction(note, memo, calendar.getTime(), accountId, categoryId, subcategoryId);
                    }
                    j = time5;
                } else if (calendar.getTime().getTime() > j && !DateHelper.isSameDay(calendar.getTime().getTime(), j)) {
                    if (calendar.getTime().getTime() >= todayMillis && !DateHelper.isSameDay(calendar.getTime().getTime(), todayMillis)) {
                        break;
                    }
                    if (untilTime != 0) {
                        if (calendar.getTime().getTime() >= untilTime && !DateHelper.isSameDay(calendar.getTime().getTime(), untilTime)) {
                            break;
                        }
                        time5 = calendar.getTime().getTime();
                        addTransaction(note, memo, calendar.getTime(), accountId, categoryId, subcategoryId);
                    } else {
                        time5 = calendar.getTime().getTime();
                        addTransaction(note, memo, calendar.getTime(), accountId, categoryId, subcategoryId);
                    }
                    j = time5;
                }
                calendar.add(5, this.recurringIncrement);
            }
        } else if (i5 == 2) {
            j = lastUpdateMilli;
            boolean z3 = false;
            while (true) {
                calendar.set(7, i6);
                int i7 = 0;
                for (int i8 = 7; i7 < i8; i8 = 7) {
                    int i9 = i7 + 1;
                    calendar.set(i8, i9);
                    if (this.recurringRepeatDate.charAt(i7) != '1') {
                        i4 = i9;
                        z2 = z3;
                    } else if (j == 0) {
                        if (calendar.getTime().getTime() < todayMillis || DateHelper.isSameDay(calendar.getTime().getTime(), todayMillis)) {
                            if (calendar.getTime().getTime() <= date.getTime()) {
                                z2 = z3;
                                if (!DateHelper.isSameDay(calendar.getTime().getTime(), date.getTime())) {
                                    i4 = i9;
                                }
                            } else {
                                z2 = z3;
                            }
                            if (untilTime != 0) {
                                if (calendar.getTime().getTime() < untilTime || DateHelper.isSameDay(calendar.getTime().getTime(), untilTime)) {
                                    long time6 = calendar.getTime().getTime();
                                    i4 = i9;
                                    addTransaction(note, memo, calendar.getTime(), accountId, categoryId, subcategoryId);
                                    j = time6;
                                    z3 = z2;
                                    i7 = i4;
                                }
                            } else {
                                i4 = i9;
                                time4 = calendar.getTime().getTime();
                                addTransaction(note, memo, calendar.getTime(), accountId, categoryId, subcategoryId);
                                j = time4;
                                z3 = z2;
                                i7 = i4;
                            }
                        }
                        i3 = 3;
                        z = true;
                        break;
                    } else {
                        i4 = i9;
                        z2 = z3;
                        i3 = 3;
                        if (calendar.getTime().getTime() > j && !DateHelper.isSameDay(calendar.getTime().getTime(), j)) {
                            if (calendar.getTime().getTime() < todayMillis || DateHelper.isSameDay(calendar.getTime().getTime(), todayMillis)) {
                                if (untilTime != 0) {
                                    if (calendar.getTime().getTime() < untilTime || DateHelper.isSameDay(calendar.getTime().getTime(), untilTime)) {
                                        time4 = calendar.getTime().getTime();
                                        addTransaction(note, memo, calendar.getTime(), accountId, categoryId, subcategoryId);
                                    }
                                } else {
                                    time4 = calendar.getTime().getTime();
                                    addTransaction(note, memo, calendar.getTime(), accountId, categoryId, subcategoryId);
                                }
                                j = time4;
                            }
                            z = true;
                            break;
                        }
                        z3 = z2;
                        i7 = i4;
                    }
                    z3 = z2;
                    i7 = i4;
                }
                boolean z4 = z3;
                i3 = 3;
                z = z4;
                if (z) {
                    break;
                }
                calendar.add(i3, this.recurringIncrement);
                z3 = z;
                i6 = 1;
            }
        } else {
            int i10 = 5;
            int i11 = 1;
            if (i5 == 3) {
                j = lastUpdateMilli;
                while (true) {
                    if (this.recurringRepeatType == i11) {
                        calendar.set(i10, calendar.getActualMaximum(i10));
                    }
                    if (j == 0) {
                        if (calendar.getTime().getTime() >= todayMillis && !DateHelper.isSameDay(calendar.getTime().getTime(), todayMillis)) {
                            break;
                        }
                        if (untilTime != 0) {
                            if (calendar.getTime().getTime() >= untilTime && !DateHelper.isSameDay(calendar.getTime().getTime(), untilTime)) {
                                break;
                            }
                            time3 = calendar.getTime().getTime();
                            i2 = 2;
                            i = 5;
                            addTransaction(note, memo, calendar.getTime(), accountId, categoryId, subcategoryId);
                        } else {
                            i = 5;
                            i2 = 2;
                            time3 = calendar.getTime().getTime();
                            addTransaction(note, memo, calendar.getTime(), accountId, categoryId, subcategoryId);
                        }
                        j = time3;
                    } else {
                        i = 5;
                        i2 = 2;
                        if (calendar.getTime().getTime() > j && !DateHelper.isSameDay(calendar.getTime().getTime(), j)) {
                            if (calendar.getTime().getTime() >= todayMillis && !DateHelper.isSameDay(calendar.getTime().getTime(), todayMillis)) {
                                break;
                            }
                            if (untilTime != 0) {
                                if (calendar.getTime().getTime() >= untilTime && !DateHelper.isSameDay(calendar.getTime().getTime(), untilTime)) {
                                    break;
                                }
                                time3 = calendar.getTime().getTime();
                                addTransaction(note, memo, calendar.getTime(), accountId, categoryId, subcategoryId);
                            } else {
                                time3 = calendar.getTime().getTime();
                                addTransaction(note, memo, calendar.getTime(), accountId, categoryId, subcategoryId);
                            }
                            j = time3;
                        }
                    }
                    if (this.recurringRepeatType == 0) {
                        int i12 = calendar.get(i);
                        if (i12 == 31 || i12 == 30 || i12 == 29) {
                            calendar.set(i, 1);
                            do {
                                calendar.add(i2, this.recurringIncrement);
                            } while (calendar.getActualMaximum(i) < i12);
                            calendar.set(i, i12);
                        } else {
                            calendar.add(i2, this.recurringIncrement);
                        }
                    } else {
                        calendar.add(i2, this.recurringIncrement);
                    }
                    i10 = 5;
                    i11 = 1;
                }
            } else {
                j = lastUpdateMilli;
                while (true) {
                    if (j == 0) {
                        if (calendar.getTime().getTime() >= todayMillis && !DateHelper.isSameDay(calendar.getTime().getTime(), todayMillis)) {
                            break;
                        }
                        if (untilTime != 0) {
                            if (calendar.getTime().getTime() >= untilTime && !DateHelper.isSameDay(calendar.getTime().getTime(), this.recurring.getUntilTime().getTime())) {
                                break;
                            }
                            time2 = calendar.getTime().getTime();
                            addTransaction(note, memo, calendar.getTime(), accountId, categoryId, subcategoryId);
                        } else {
                            time2 = calendar.getTime().getTime();
                            addTransaction(note, memo, calendar.getTime(), accountId, categoryId, subcategoryId);
                        }
                        j = time2;
                    } else if (calendar.getTime().getTime() > j && !DateHelper.isSameDay(calendar.getTime().getTime(), j)) {
                        if (calendar.getTime().getTime() >= todayMillis && !DateHelper.isSameDay(calendar.getTime().getTime(), todayMillis)) {
                            break;
                        }
                        if (untilTime != 0) {
                            if (calendar.getTime().getTime() >= untilTime && !DateHelper.isSameDay(calendar.getTime().getTime(), untilTime)) {
                                break;
                            }
                            time = calendar.getTime().getTime();
                            addTransaction(note, memo, calendar.getTime(), accountId, categoryId, subcategoryId);
                        } else {
                            time = calendar.getTime().getTime();
                            addTransaction(note, memo, calendar.getTime(), accountId, categoryId, subcategoryId);
                        }
                        j = time;
                    }
                    if (calendar.get(5) == 29) {
                        calendar.add(1, this.recurringIncrement);
                        if (calendar.get(2) == 1 && calendar.getActualMaximum(5) < 29) {
                            while (true) {
                                calendar.add(1, this.recurringIncrement);
                                if (calendar.get(2) == 1) {
                                    if (calendar.getActualMaximum(5) >= 29) {
                                        calendar.set(5, 29);
                                        break;
                                    }
                                } else {
                                    calendar.set(5, 29);
                                    break;
                                }
                            }
                        }
                    } else {
                        calendar.add(1, this.recurringIncrement);
                    }
                }
            }
        }
        if (untilTime != 0 && calendar.getTime().getTime() > untilTime && !DateHelper.isSameDay(calendar.getTime().getTime(), untilTime)) {
            this.isRecurring = false;
        }
        this.isRecurring = true;
        return j;
    }

    @Override // android.app.DatePickerDialog.OnDateSetListener
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        this.dateEditText.setText(DateHelper.getDateFromPicker(getApplicationContext(), i, i1, i2));
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(this.date);
        calendar.set(1, i);
        calendar.set(2, i1);
        calendar.set(5, i2);
        this.date = calendar.getTime();
        checkIsComplete();
        validateRecurring();
    }
}
