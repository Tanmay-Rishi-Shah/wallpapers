package com.expance.merapaisa;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.Entity.DebtEntity;
import com.expance.merapaisa.Database.Entity.DebtTransEntity;
import com.expance.merapaisa.Database.Entity.TransEntity;
import com.expance.merapaisa.Model.Wallets;
import com.expance.merapaisa.Utility.CalendarHelper;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Utility.DateHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;

import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;

import vocsy.ads.CustomAdsListener;
import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class CreateDebtTrans extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    long amount;
    EditText amountEditText;
    Date date;
    EditText dateEditText;
    int debtId;
    int debtTransId;
    int debtType;
    String defaultSymbol;
    ConstraintLayout deleteWrapper;
    boolean isComplete;
    EditText noteEditText;
    TextView saveLabel;
    String symbol;
    EditText timeEditText;
    int type;
    Spinner typeSpinner;
    EditText walletEditText;
    int walletId;
    TextView walletLabel;
    private List<Wallets> walletList;

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("date", this.date.getTime());
        outState.putLong("amount", this.amount);
        outState.putInt("walletId", this.walletId);
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
        setContentView(R.layout.activity_create_debt_trans);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        initialData();
        setUpLayout();
        if (savedInstanceState != null) {
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTimeInMillis(savedInstanceState.getLong("date"));
            this.date = calendar.getTime();
            this.amount = savedInstanceState.getLong("amount");
            this.walletId = savedInstanceState.getInt("walletId");
            checkIsComplete();
        }
        populateData(savedInstanceState != null);
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

    private void initialData() {
        this.symbol = SharePreferenceHelper.getAccountSymbol(this);
        this.walletList = new ArrayList();
        this.isComplete = false;
        this.walletId = 0;
        this.amount = 0L;
        this.date = DateHelper.getCurrentDateTime();
        this.debtId = getIntent().getIntExtra("debtId", 0);
        this.debtType = getIntent().getIntExtra("debtType", 0);
        this.type = getIntent().getIntExtra(JamXmlElements.TYPE, 0);
        if (getIntent().hasExtra("debtTransId")) {
            this.debtTransId = getIntent().getIntExtra("debtTransId", 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setWallet(int id) {
        String str = "";
        for (Wallets wallets : this.walletList) {
            if (wallets.getId() == id) {
                this.symbol = DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(wallets.getCurrency()));
                str = wallets.getName() + " • " + Helper.getBeautifyAmount(this.symbol, wallets.getAmount());
            }
        }
        this.walletEditText.setText(str);
        this.amountEditText.setText(Helper.getBeautifyAmount(this.symbol, this.amount));
    }

    private void populateData(final boolean isSaveInstanceState) {
        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateDebtTrans.1
            @Override // java.lang.Runnable
            public void run() {
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(CreateDebtTrans.this.getApplicationContext());
                java.util.Calendar calendar = java.util.Calendar.getInstance();
                calendar.set(14, 0);
                calendar.set(13, 0);
                calendar.set(12, 0);
                calendar.set(11, 0);
                calendar.add(5, 1);
                if (!SharePreferenceHelper.isFutureBalanceOn(CreateDebtTrans.this.getApplicationContext())) {
                    calendar.set(1, 10000);
                }
                CreateDebtTrans.this.walletList = appDatabaseObject.walletDaoObject().getWallets(SharePreferenceHelper.getAccountId(CreateDebtTrans.this.getApplicationContext()), 0, calendar.getTimeInMillis());
                com.expance.merapaisa.Model.Currency debtCurrency = appDatabaseObject.debtDaoObject().getDebtCurrency(SharePreferenceHelper.getAccountId(CreateDebtTrans.this.getApplicationContext()), CreateDebtTrans.this.debtId);
                if (debtCurrency != null) {
                    CreateDebtTrans.this.symbol = DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(debtCurrency.getCurrency()));
                }
                CreateDebtTrans createDebtTrans = CreateDebtTrans.this;
                createDebtTrans.defaultSymbol = createDebtTrans.symbol;
                if (CreateDebtTrans.this.type == 1 && !isSaveInstanceState) {
                    DebtTransEntity debtTransById = appDatabaseObject.debtDaoObject().getDebtTransById(CreateDebtTrans.this.debtTransId);
                    CreateDebtTrans.this.date = debtTransById.getDateTime();
                    CreateDebtTrans.this.amount = debtTransById.getAmount();
                    final int type = debtTransById.getType();
                    final String note = debtTransById.getNote();
                    TransEntity transactionFromDebtTransId = appDatabaseObject.debtDaoObject().getTransactionFromDebtTransId(CreateDebtTrans.this.debtId, CreateDebtTrans.this.debtTransId);
                    if (transactionFromDebtTransId != null) {
                        CreateDebtTrans.this.walletId = transactionFromDebtTransId.getWalletId();
                    }
                    CreateDebtTrans.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateDebtTrans.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            CreateDebtTrans.this.typeSpinner.setSelection(type);
                            CreateDebtTrans.this.noteEditText.setText(note);
                            CreateDebtTrans.this.timeEditText.setText(DateHelper.getFormattedTime(CreateDebtTrans.this.date, CreateDebtTrans.this.getApplicationContext()));
                            CreateDebtTrans.this.dateEditText.setText(DateHelper.getFormattedDate(CreateDebtTrans.this.getApplicationContext(), CreateDebtTrans.this.date));
                            CreateDebtTrans.this.amountEditText.setText(Helper.getBeautifyAmount(CreateDebtTrans.this.symbol, CreateDebtTrans.this.amount));
                            CreateDebtTrans.this.setWallet(CreateDebtTrans.this.walletId);
                            CreateDebtTrans.this.checkIsComplete();
                        }
                    });
                }
                CreateDebtTrans.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateDebtTrans.1.2
                    @Override // java.lang.Runnable
                    public void run() {
                        CreateDebtTrans.this.amountEditText.setText(Helper.getBeautifyAmount(CreateDebtTrans.this.symbol, CreateDebtTrans.this.amount));
                    }
                });
            }
        });
    }

    private void setUpLayout() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.debt_transaction);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.walletLabel = (TextView) findViewById(R.id.walletLabel);
        this.saveLabel = (TextView) findViewById(R.id.saveLabel);
        this.typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
        this.timeEditText = (EditText) findViewById(R.id.timeEditText);
        this.dateEditText = (EditText) findViewById(R.id.dateEditText);
        this.noteEditText = (EditText) findViewById(R.id.noteEditText);
        this.walletEditText = (EditText) findViewById(R.id.walletEditText);
        this.amountEditText = (EditText) findViewById(R.id.amountEditText);
        this.deleteWrapper = (ConstraintLayout) findViewById(R.id.deleteWrapper);
        String[] strArr = new String[3];
        if (this.debtType == 0) {
            strArr[0] = getResources().getString(R.string.repay);
            strArr[1] = getResources().getString(R.string.debt_increase);
        } else {
            strArr[0] = getResources().getString(R.string.collect);
            strArr[1] = getResources().getString(R.string.loan_increase);
        }
        strArr[2] = getResources().getString(R.string.interest);
        this.typeSpinner.setAdapter((SpinnerAdapter) new ArrayAdapter(this, 17367049, strArr));
        this.saveLabel.setOnClickListener(this);
        this.dateEditText.setOnClickListener(this);
        this.amountEditText.setOnClickListener(this);
        this.walletEditText.setOnClickListener(this);
        this.dateEditText.setLongClickable(false);
        this.amountEditText.setLongClickable(false);
        this.walletEditText.setLongClickable(false);
        this.dateEditText.setFocusable(false);
        this.amountEditText.setFocusable(false);
        this.walletEditText.setFocusable(false);
        this.deleteWrapper.setOnClickListener(this);
        this.timeEditText.setOnClickListener(this);
        this.timeEditText.setLongClickable(false);
        this.timeEditText.setFocusable(false);
        this.typeSpinner.setOnItemSelectedListener(this);
        this.timeEditText.setText(DateHelper.getFormattedTime(this.date, getApplicationContext()));
        this.dateEditText.setText(DateHelper.getFormattedDate(getApplicationContext(), this.date));
        this.amountEditText.setText(Helper.getBeautifyAmount(this.symbol, 0L));
        if (this.type == 0) {
            this.deleteWrapper.setVisibility(8);
        } else if (getIntent().hasExtra("isFromTransaction") && getIntent().getBooleanExtra("isFromTransaction", false)) {
            this.deleteWrapper.setVisibility(8);
        }
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

    /* JADX INFO: Access modifiers changed from: private */
    public void checkIsComplete() {
        boolean z = this.amount > 0;
        this.isComplete = z;
        this.saveLabel.setAlpha(z ? 1.0f : 0.35f);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.amountEditText /* 2131230807 */) {
            Intent intent = new Intent(getApplicationContext(), Calculator.class);
            intent.putExtra("amount", this.amount);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        } else if (view.getId() == R.id.dateEditText /* 2131230957 */) {
            new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() { // from class: com.ktwapps.walletmanager.CreateDebtTrans.2
                @Override // android.app.DatePickerDialog.OnDateSetListener
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    java.util.Calendar calendar = java.util.Calendar.getInstance();
                    calendar.setTime(CreateDebtTrans.this.date);
                    calendar.set(1, i);
                    calendar.set(2, i1);
                    calendar.set(5, i2);
                    CreateDebtTrans.this.date = calendar.getTime();
                    CreateDebtTrans.this.dateEditText.setText(DateHelper.getDateFromPicker(CreateDebtTrans.this.getApplicationContext(), i, i1, i2));
                }
            }, CalendarHelper.getYearFromDate(this.date), CalendarHelper.getMonthFromDate(this.date), CalendarHelper.getDayFromDate(this.date)).show();
        } else if (view.getId() == R.id.deleteWrapper /* 2131230988 */) {
            GoogleAds.getInstance().showCounterInterstitialAd(CreateDebtTrans.this, new CustomAdsListener() {
                @Override
                public void onFinish() {
                    deleteTransaction();
                }
            });

        } else if (view.getId() == R.id.saveLabel /* 2131231463 */) {
            if (this.isComplete) {
                GoogleAds.getInstance().showCounterInterstitialAd(CreateDebtTrans.this, new CustomAdsListener() {
                    @Override
                    public void onFinish() {

                        if (type == 0) {
                            createTransaction();
                        } else {
                            editTransaction();
                        }
                    }
                });

            }
        } else if (view.getId() == R.id.timeEditText /* 2131231616 */) {
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTime(this.date);
            new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() { // from class: com.ktwapps.walletmanager.CreateDebtTrans.3
                @Override // android.app.TimePickerDialog.OnTimeSetListener
                public void onTimeSet(TimePicker timePicker, int i, int i1) {
                    java.util.Calendar calendar2 = java.util.Calendar.getInstance();
                    calendar2.setTime(CreateDebtTrans.this.date);
                    calendar2.set(11, i);
                    calendar2.set(12, i1);
                    CreateDebtTrans.this.date = calendar2.getTime();
                    CreateDebtTrans.this.timeEditText.setText(DateHelper.getTimeFromPicker(CreateDebtTrans.this.getApplicationContext(), i, i1));
                }
            }, calendar.get(11), calendar.get(12), DateFormat.is24HourFormat(getApplicationContext())).show();
        } else if (view.getId() == R.id.walletEditText /* 2131231677 */) {
            Intent intent2 = new Intent(this, WalletPicker.class);
            intent2.putExtra("id", this.walletId);
            startActivityForResult(intent2, 2);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            if (requestCode == 1) {
                long longExtra = data.getLongExtra("amount", 0L);
                this.amount = longExtra;
                long j = longExtra >= 0 ? longExtra : 0L;
                this.amount = j;
                this.amountEditText.setText(Helper.getBeautifyAmount(this.symbol, j));
                checkIsComplete();
            } else if (requestCode == 2) {
                int intExtra = data.getIntExtra("id", 0);
                setWallet(intExtra);
                this.walletId = intExtra;
                this.amountEditText.setText(Helper.getBeautifyAmount(this.symbol, this.amount));
                checkIsComplete();
            }
        }
    }

    private void createTransaction() {
        final int selectedItemPosition = this.typeSpinner.getSelectedItemPosition();
        final String obj = this.noteEditText.getText().toString();
        final int accountId = SharePreferenceHelper.getAccountId(this);
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateDebtTrans.4
            @Override // java.lang.Runnable
            public void run() {
                int loanId;
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(CreateDebtTrans.this);
                int insertDebtTrans = (int) appDatabaseObject.debtDaoObject().insertDebtTrans(new DebtTransEntity(CreateDebtTrans.this.amount, CreateDebtTrans.this.date, obj, CreateDebtTrans.this.debtId, selectedItemPosition));
                if (CreateDebtTrans.this.walletId != 0 && selectedItemPosition != 2) {
                    long transactionAmount = CreateDebtTrans.this.getTransactionAmount();
                    int transactionType = CreateDebtTrans.this.getTransactionType();
                    if (CreateDebtTrans.this.debtType == 0) {
                        if (selectedItemPosition == 0) {
                            loanId = appDatabaseObject.debtDaoObject().getRepayId(accountId);
                        } else {
                            loanId = appDatabaseObject.debtDaoObject().getDebtId(accountId);
                        }
                    } else if (selectedItemPosition == 0) {
                        loanId = appDatabaseObject.debtDaoObject().getReceiveId(accountId);
                    } else {
                        loanId = appDatabaseObject.debtDaoObject().getLoanId(accountId);
                    }
                    appDatabaseObject.transDaoObject().insertTrans(new TransEntity(obj, "", transactionAmount, CreateDebtTrans.this.date, transactionType, accountId, loanId, 0, CreateDebtTrans.this.walletId, 0, 0L, CreateDebtTrans.this.debtId, insertDebtTrans, 0));
                }
                DebtEntity debtById = appDatabaseObject.debtDaoObject().getDebtById(CreateDebtTrans.this.debtId);
                com.expance.merapaisa.Model.Currency debtCurrency = appDatabaseObject.debtDaoObject().getDebtCurrency(accountId, CreateDebtTrans.this.debtId);
                if (debtCurrency == null) {
                    debtCurrency = new com.expance.merapaisa.Model.Currency(1.0d, "Empty");
                }
                List<DebtTransEntity> allDebtTrans = appDatabaseObject.debtDaoObject().getAllDebtTrans(CreateDebtTrans.this.debtId);
                long amount = debtById.getAmount();
                long j = 0;
                for (DebtTransEntity debtTransEntity : allDebtTrans) {
                    com.expance.merapaisa.Model.Currency debtTransCurrency = appDatabaseObject.debtDaoObject().getDebtTransCurrency(accountId, CreateDebtTrans.this.debtId, debtTransEntity.getId());
                    if (debtTransCurrency == null) {
                        if (debtTransEntity.getType() == 0) {
                            j += debtTransEntity.getAmount();
                        } else {
                            amount += debtTransEntity.getAmount();
                        }
                    } else {
                        double rate = debtTransCurrency.getRate() / debtCurrency.getRate();
                        if (debtTransEntity.getType() == 0) {
                            j = (long) (j + (debtTransEntity.getAmount() * rate));
                        } else {
                            amount = (long) (amount + (debtTransEntity.getAmount() * rate));
                        }
                    }
                }
                if (j >= amount) {
                    debtById.setStatus(1);
                } else {
                    debtById.setStatus(0);
                }
                appDatabaseObject.debtDaoObject().updateDebt(debtById);
                CreateDebtTrans.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateDebtTrans.4.1
                    @Override // java.lang.Runnable
                    public void run() {
                        CreateDebtTrans.this.finish();
                        CreateDebtTrans.this.overridePendingTransition(R.anim.scale_in, R.anim.bottom_to_top);
                    }
                });
            }
        });
    }

    private void editTransaction() {
        final int selectedItemPosition = this.typeSpinner.getSelectedItemPosition();
        final String obj = this.noteEditText.getText().toString();
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                AppDatabaseObject var12 = AppDatabaseObject.getInstance(CreateDebtTrans.this);
                int var4 = SharePreferenceHelper.getAccountId(CreateDebtTrans.this.getApplicationContext());
                DebtTransEntity var10 = var12.debtDaoObject().getDebtTransById(CreateDebtTrans.this.debtTransId);
                var10.setNote(obj);
                var10.setDateTime(CreateDebtTrans.this.date);
                var10.setType(selectedItemPosition);
                var10.setAmount(CreateDebtTrans.this.amount);
                var12.debtDaoObject().updateDebtTrans(var10);
                TransEntity var16 = var12.debtDaoObject().getTransactionFromDebtTransId(CreateDebtTrans.this.debtId, CreateDebtTrans.this.debtTransId);
                int var3;
                int var5;
                long var6;
                if (var16 != null) {
                    if (selectedItemPosition == 2) {
                        var12.transDaoObject().deleteTrans(var16.getId());
                    } else {
                        var6 = CreateDebtTrans.this.getTransactionAmount();
                        var5 = CreateDebtTrans.this.getTransactionType();
                        if (CreateDebtTrans.this.debtType == 0) {
                            if (selectedItemPosition == 0) {
                                var3 = var12.debtDaoObject().getRepayId(var4);
                            } else {
                                var3 = var12.debtDaoObject().getDebtId(var4);
                            }
                        } else if (selectedItemPosition == 0) {
                            var3 = var12.debtDaoObject().getReceiveId(var4);
                        } else {
                            var3 = var12.debtDaoObject().getLoanId(var4);
                        }

                        var16.setDateTime(CreateDebtTrans.this.date);
                        var16.setNote(obj);
                        var16.setCategoryId(var3);
                        var16.setWalletId(CreateDebtTrans.this.walletId);
                        var16.setType(var5);
                        var16.setAmount(var6);
                        var12.transDaoObject().updateTrans(var16);
                    }
                } else if (CreateDebtTrans.this.walletId != 0 && selectedItemPosition != 2) {
                    var6 = CreateDebtTrans.this.getTransactionAmount();
                    var5 = CreateDebtTrans.this.getTransactionType();
                    if (CreateDebtTrans.this.debtType == 0) {
                        if (selectedItemPosition == 0) {
                            var3 = var12.debtDaoObject().getRepayId(var4);
                        } else {
                            var3 = var12.debtDaoObject().getDebtId(var4);
                        }
                    } else if (selectedItemPosition == 0) {
                        var3 = var12.debtDaoObject().getReceiveId(var4);
                    } else {
                        var3 = var12.debtDaoObject().getLoanId(var4);
                    }

                    var12.transDaoObject().insertTrans(new TransEntity(obj, "", var6, CreateDebtTrans.this.date, var5, var4, var3, 0, CreateDebtTrans.this.walletId, 0, 0L, CreateDebtTrans.this.debtId, CreateDebtTrans.this.debtTransId, 0));
                }

                DebtEntity var13 = var12.debtDaoObject().getDebtById(CreateDebtTrans.this.debtId);
                com.expance.merapaisa.Model.Currency var11 = var12.debtDaoObject().getDebtCurrency(var4, CreateDebtTrans.this.debtId);
                com.expance.merapaisa.Model.Currency var17 = var11;
                if (var11 == null) {
                    var17 = new com.expance.merapaisa.Model.Currency(1.0, "Empty");
                }

                List var18 = var12.debtDaoObject().getAllDebtTrans(CreateDebtTrans.this.debtId);
                long var8 = var13.getAmount();
                Iterator var19 = var18.iterator();
                var6 = 0L;

                while (var19.hasNext()) {
                    DebtTransEntity var15 = (DebtTransEntity) var19.next();
                    com.expance.merapaisa.Model.Currency var14 = var12.debtDaoObject().getDebtTransCurrency(var4, CreateDebtTrans.this.debtId, var15.getId());
                    if (var14 == null) {
                        if (var15.getType() == 0) {
                            var6 += var15.getAmount();
                        } else {
                            var8 += var15.getAmount();
                        }
                    } else {
                        double var1 = var14.getRate() / var17.getRate();
                        if (var15.getType() == 0) {
                            var6 = (long) ((double) var6 + (double) var15.getAmount() * var1);
                        } else {
                            var8 = (long) ((double) var8 + (double) var15.getAmount() * var1);
                        }
                    }
                }

                if (var6 >= var8) {
                    var13.setStatus(1);
                } else {
                    var13.setStatus(0);
                }

                var12.debtDaoObject().updateDebt(var13);
                CreateDebtTrans.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CreateDebtTrans.this.finish();
                        CreateDebtTrans.this.overridePendingTransition(R.anim.scale_in, 0x7f01000e);
                    }
                });
            }
        });
    }

    private void deleteTransaction() {
        Helper.showDialog(this, "", getResources().getString(R.string.transaction_delete_message), getResources().getString(R.string.delete_positive), getResources().getString(R.string.cancel), new AnonymousClass6());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.ktwapps.walletmanager.CreateDebtTrans$6  reason: invalid class name */
    /* loaded from: classes3.dex */
    public class AnonymousClass6 implements DialogInterface.OnClickListener {
        AnonymousClass6() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateDebtTrans.6.1
                @Override // java.lang.Runnable
                public void run() {
                    AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(CreateDebtTrans.this.getApplicationContext());
                    int accountId = SharePreferenceHelper.getAccountId(CreateDebtTrans.this.getApplicationContext());
                    appDatabaseObject.debtDaoObject().deleteDebtTrans(CreateDebtTrans.this.debtTransId);
                    appDatabaseObject.debtDaoObject().deleteTransactionFromDebtTransId(CreateDebtTrans.this.debtId, CreateDebtTrans.this.debtTransId);
                    DebtEntity debtById = appDatabaseObject.debtDaoObject().getDebtById(CreateDebtTrans.this.debtId);
                    com.expance.merapaisa.Model.Currency debtCurrency = appDatabaseObject.debtDaoObject().getDebtCurrency(accountId, CreateDebtTrans.this.debtId);
                    if (debtCurrency == null) {
                        debtCurrency = new com.expance.merapaisa.Model.Currency(1.0d, "Empty");
                    }
                    List<DebtTransEntity> allDebtTrans = appDatabaseObject.debtDaoObject().getAllDebtTrans(CreateDebtTrans.this.debtId);
                    long amount = debtById.getAmount();
                    long j = 0;
                    for (DebtTransEntity debtTransEntity : allDebtTrans) {
                        com.expance.merapaisa.Model.Currency debtTransCurrency = appDatabaseObject.debtDaoObject().getDebtTransCurrency(accountId, CreateDebtTrans.this.debtId, debtTransEntity.getId());
                        if (debtTransCurrency == null) {
                            if (debtTransEntity.getType() == 0) {
                                j += debtTransEntity.getAmount();
                            } else {
                                amount += debtTransEntity.getAmount();
                            }
                        } else {
                            double rate = debtTransCurrency.getRate() / debtCurrency.getRate();
                            if (debtTransEntity.getType() == 0) {
                                j = (long) (j + (debtTransEntity.getAmount() * rate));
                            } else {
                                amount = (long) (amount + (debtTransEntity.getAmount() * rate));
                            }
                        }
                    }
                    if (j >= amount) {
                        debtById.setStatus(1);
                    } else {
                        debtById.setStatus(0);
                    }
                    appDatabaseObject.debtDaoObject().updateDebt(debtById);
                    CreateDebtTrans.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateDebtTrans.6.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            CreateDebtTrans.this.finish();
                            CreateDebtTrans.this.overridePendingTransition(R.anim.scale_in, R.anim.bottom_to_top);
                        }
                    });
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getTransactionAmount() {
        long j;
        int selectedItemPosition = this.typeSpinner.getSelectedItemPosition();
        if (this.debtType == 0) {
            if (selectedItemPosition == 0) {
                j = this.amount;
                return -j;
            }
            return this.amount;
        }
        if (selectedItemPosition == 1) {
            j = this.amount;
            return -j;
        }
        return this.amount;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getTransactionType() {
        int selectedItemPosition = this.typeSpinner.getSelectedItemPosition();
        return this.debtType == 0 ? selectedItemPosition == 0 ? 1 : 0 : selectedItemPosition == 1 ? 1 : 0;
    }

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i == 2) {
            this.symbol = this.defaultSymbol;
            this.walletLabel.setVisibility(8);
            this.walletEditText.setVisibility(8);
        } else {
            setWallet(this.walletId);
            this.walletLabel.setVisibility(0);
            this.walletEditText.setVisibility(0);
        }
        this.amountEditText.setText(Helper.getBeautifyAmount(this.symbol, this.amount));
    }
}
