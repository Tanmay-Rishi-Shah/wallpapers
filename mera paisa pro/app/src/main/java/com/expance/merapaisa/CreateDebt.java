package com.expance.merapaisa;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.expance.merapaisa.Adapter.SpinnerColorAdapter;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class CreateDebt extends AppCompatActivity implements View.OnClickListener, TextWatcher, AdapterView.OnItemSelectedListener {
    SpinnerColorAdapter adapter;
    long amount;
    EditText amountEditText;
    Spinner colorSpinner;
    Date date;
    EditText dateEditText;
    int debtId;
    int debtType;
    boolean isComplete;
    EditText nameEditText;
    EditText personEditText;
    TextView saveLabel;
    String symbol;
    EditText timeEditText;
    Spinner titleSpinner;
    int type;
    EditText walletEditText;
    int walletId;
    private List<Wallets> walletList;

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public void onNothingSelected(AdapterView<?> adapterView) {
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
        outState.putLong("date", this.date.getTime());
        outState.putInt("debtType", this.debtType);
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
        setContentView(R.layout.activity_create_debt);
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
            this.debtType = savedInstanceState.getInt("debtType");
            this.walletId = savedInstanceState.getInt("walletId");
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

    private void initialData() {
        this.symbol = SharePreferenceHelper.getAccountSymbol(this);
        this.walletList = new ArrayList();
        this.walletId = 0;
        this.isComplete = false;
        this.amount = 0L;
        this.date = DateHelper.getCurrentDateTime();
        this.debtType = getIntent().getIntExtra("debtType", 0);
        this.type = getIntent().getIntExtra(JamXmlElements.TYPE, 4);
        this.adapter = new SpinnerColorAdapter(this, R.layout.list_drop_down_color, R.id.label, DataHelper.getColorList());
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

    public void setUpLayout() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, 17367049, new String[]{getResources().getString(R.string.i_lend), getResources().getString(R.string.i_borrow)});
            Spinner spinner = new Spinner(getSupportActionBar().getThemedContext());
            this.titleSpinner = spinner;
            spinner.setAdapter((SpinnerAdapter) arrayAdapter);
            toolbar.addView(this.titleSpinner, 0);
            this.titleSpinner.setSelection(this.debtType);
            this.titleSpinner.setOnItemSelectedListener(this);
        }
        this.saveLabel = (TextView) findViewById(R.id.saveLabel);
        this.dateEditText = (EditText) findViewById(R.id.dateEditText);
        this.timeEditText = (EditText) findViewById(R.id.timeEditText);
        this.nameEditText = (EditText) findViewById(R.id.nameEditText);
        this.amountEditText = (EditText) findViewById(R.id.amountEditText);
        this.personEditText = (EditText) findViewById(R.id.personEditText);
        this.colorSpinner = (Spinner) findViewById(R.id.colorSpinner);
        this.walletEditText = (EditText) findViewById(R.id.walletEditText);
        this.colorSpinner.setAdapter((SpinnerAdapter) this.adapter);
        this.dateEditText.setText(DateHelper.getFormattedDate(getApplicationContext(), this.date));
        this.timeEditText.setText(DateHelper.getFormattedTime(this.date, getApplicationContext()));
        this.nameEditText.addTextChangedListener(this);
        this.personEditText.addTextChangedListener(this);
        this.amountEditText.setText(Helper.getBeautifyAmount(this.symbol, this.amount));
        this.dateEditText.setOnClickListener(this);
        this.dateEditText.setLongClickable(false);
        this.dateEditText.setFocusable(false);
        this.timeEditText.setOnClickListener(this);
        this.timeEditText.setLongClickable(false);
        this.timeEditText.setFocusable(false);
        this.amountEditText.setOnClickListener(this);
        this.amountEditText.setLongClickable(false);
        this.amountEditText.setFocusable(false);
        this.walletEditText.setOnClickListener(this);
        this.walletEditText.setLongClickable(false);
        this.walletEditText.setFocusable(false);
        this.saveLabel.setOnClickListener(this);
        this.personEditText.setHint(this.debtType == 0 ? R.string.lend_hint : R.string.borrow_hint);
        checkIsComplete();
    }

    private void checkIsComplete() {
        boolean z = this.personEditText.getText().toString().trim().length() > 0 && this.amount > 0;
        this.isComplete = z;
        this.saveLabel.setAlpha(z ? 1.0f : 0.35f);
    }

    private void populateData(final boolean isSavedInstanceState) {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateDebt.1
            @Override // java.lang.Runnable
            public void run() {
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(CreateDebt.this.getApplicationContext());
                java.util.Calendar calendar = java.util.Calendar.getInstance();
                calendar.set(14, 0);
                calendar.set(13, 0);
                calendar.set(12, 0);
                calendar.set(11, 0);
                calendar.add(5, 1);
                if (!SharePreferenceHelper.isFutureBalanceOn(CreateDebt.this.getApplicationContext())) {
                    calendar.set(1, 10000);
                }
                CreateDebt.this.walletList = appDatabaseObject.walletDaoObject().getWallets(SharePreferenceHelper.getAccountId(CreateDebt.this.getApplicationContext()), 0, calendar.getTimeInMillis());
                if (CreateDebt.this.type == -2) {
                    CreateDebt createDebt = CreateDebt.this;
                    createDebt.debtId = createDebt.getIntent().getIntExtra("debtId", 0);
                    TransEntity transactionFromDebtId = appDatabaseObject.debtDaoObject().getTransactionFromDebtId(CreateDebt.this.debtId);
                    if (isSavedInstanceState) {
                        return;
                    }
                    final DebtEntity debtById = appDatabaseObject.debtDaoObject().getDebtById(CreateDebt.this.debtId);
                    if (transactionFromDebtId != null) {
                        CreateDebt.this.walletId = transactionFromDebtId.getWalletId();
                    }
                    CreateDebt.this.amount = debtById.getAmount();
                    CreateDebt.this.date = debtById.getLendDate();
                    CreateDebt.this.debtType = debtById.getType();
                    CreateDebt.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateDebt.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            CreateDebt.this.nameEditText.setText(debtById.getName());
                            CreateDebt.this.personEditText.setText(debtById.getLender());
                            CreateDebt.this.amountEditText.setText(Helper.getBeautifyAmount(CreateDebt.this.symbol, CreateDebt.this.amount));
                            CreateDebt.this.titleSpinner.setSelection(CreateDebt.this.debtType);
                            CreateDebt.this.colorSpinner.setSelection(CreateDebt.this.adapter.getPosition(debtById.getColor()));
                            CreateDebt.this.timeEditText.setText(DateHelper.getFormattedTime(CreateDebt.this.date, CreateDebt.this.getApplicationContext()));
                            CreateDebt.this.dateEditText.setText(DateHelper.getFormattedDate(CreateDebt.this.getApplicationContext(), CreateDebt.this.date));
                            CreateDebt.this.personEditText.setHint(CreateDebt.this.debtType == 0 ? R.string.lend_hint : R.string.borrow_hint);
                            CreateDebt.this.setWallet(CreateDebt.this.walletId);
                        }
                    });
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
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

    private void createDebt() {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateDebt.2
            @Override // java.lang.Runnable
            public void run() {
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(CreateDebt.this);
                int accountId = SharePreferenceHelper.getAccountId(CreateDebt.this);
                String trim = CreateDebt.this.nameEditText.getText().toString().trim();
                String trim2 = CreateDebt.this.personEditText.getText().toString().trim();
                String str = DataHelper.getColorList().get(CreateDebt.this.colorSpinner.getSelectedItemPosition());
                long j = CreateDebt.this.amount;
                int insertDebt = (int) appDatabaseObject.debtDaoObject().insertDebt(new DebtEntity(trim, trim2, str, 0L, j, CreateDebt.this.date, CreateDebt.this.date, accountId, 0, CreateDebt.this.debtType));
                if (CreateDebt.this.walletId != 0) {
                    appDatabaseObject.transDaoObject().insertTrans(new TransEntity(trim, "", CreateDebt.this.debtType == 0 ? j : -j, CreateDebt.this.date, CreateDebt.this.debtType == 0 ? 0 : 1, accountId, CreateDebt.this.debtType == 0 ? appDatabaseObject.debtDaoObject().getDebtId(accountId) : appDatabaseObject.debtDaoObject().getLoanId(accountId), 0, CreateDebt.this.walletId, 0, 0L, insertDebt, 0, 0));
                }
                CreateDebt.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateDebt.2.1
                    @Override // java.lang.Runnable
                    public void run() {
                        CreateDebt.this.finish();
                        CreateDebt.this.overridePendingTransition(R.anim.scale_in, R.anim.bottom_to_top);
                    }
                });
            }
        });
    }

    private void editDebt() {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateDebt.3
            @Override // java.lang.Runnable
            public void run() {
                int loanId;
                int accountId = SharePreferenceHelper.getAccountId(CreateDebt.this.getApplicationContext());
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(CreateDebt.this);
                DebtEntity debtById = appDatabaseObject.debtDaoObject().getDebtById(CreateDebt.this.debtId);
                if (debtById.getType() != CreateDebt.this.debtType) {
                    for (TransEntity transEntity : appDatabaseObject.debtDaoObject().getTransactionFromDebtTransList(CreateDebt.this.debtId)) {
                        DebtTransEntity debtTransById = appDatabaseObject.debtDaoObject().getDebtTransById(transEntity.getDebtTransId());
                        long j = -transEntity.getAmount();
                        int i = transEntity.getType() == 1 ? 0 : 1;
                        if (CreateDebt.this.debtType == 0) {
                            if (debtTransById.getType() == 0) {
                                loanId = appDatabaseObject.debtDaoObject().getRepayId(accountId);
                            } else {
                                loanId = appDatabaseObject.debtDaoObject().getDebtId(accountId);
                            }
                        } else if (debtTransById.getType() == 0) {
                            loanId = appDatabaseObject.debtDaoObject().getReceiveId(accountId);
                        } else {
                            loanId = appDatabaseObject.debtDaoObject().getLoanId(accountId);
                        }
                        transEntity.setCategoryId(loanId);
                        transEntity.setType(i);
                        transEntity.setAmount(j);
                        appDatabaseObject.transDaoObject().updateTrans(transEntity);
                    }
                }
                String trim = CreateDebt.this.nameEditText.getText().toString().trim();
                String trim2 = CreateDebt.this.personEditText.getText().toString().trim();
                debtById.setName(trim);
                debtById.setColor(DataHelper.getColorList().get(CreateDebt.this.colorSpinner.getSelectedItemPosition()));
                debtById.setLender(trim2);
                debtById.setDueDate(CreateDebt.this.date);
                debtById.setType(CreateDebt.this.debtType);
                debtById.setLendDate(CreateDebt.this.date);
                debtById.setAmount(CreateDebt.this.amount);
                appDatabaseObject.debtDaoObject().updateDebt(debtById);
                TransEntity transactionFromDebtId = appDatabaseObject.debtDaoObject().getTransactionFromDebtId(CreateDebt.this.debtId);
                if (transactionFromDebtId != null) {
                    long j2 = CreateDebt.this.debtType == 0 ? CreateDebt.this.amount : -CreateDebt.this.amount;
                    int i2 = CreateDebt.this.debtType == 0 ? 0 : 1;
                    int debtId = CreateDebt.this.debtType == 0 ? appDatabaseObject.debtDaoObject().getDebtId(accountId) : appDatabaseObject.debtDaoObject().getLoanId(accountId);
                    transactionFromDebtId.setDateTime(CreateDebt.this.date);
                    transactionFromDebtId.setNote(trim);
                    transactionFromDebtId.setCategoryId(debtId);
                    transactionFromDebtId.setWalletId(CreateDebt.this.walletId);
                    transactionFromDebtId.setType(i2);
                    transactionFromDebtId.setAmount(j2);
                    appDatabaseObject.transDaoObject().updateTrans(transactionFromDebtId);
                } else if (CreateDebt.this.walletId != 0) {
                    appDatabaseObject.transDaoObject().insertTrans(new TransEntity(trim, "", CreateDebt.this.debtType == 0 ? CreateDebt.this.amount : -CreateDebt.this.amount, CreateDebt.this.date, CreateDebt.this.debtType == 0 ? 0 : 1, accountId, CreateDebt.this.debtType == 0 ? appDatabaseObject.debtDaoObject().getDebtId(accountId) : appDatabaseObject.debtDaoObject().getLoanId(accountId), 0, CreateDebt.this.walletId, 0, 0L, CreateDebt.this.debtId, 0, 0));
                }
                DebtEntity debtById2 = appDatabaseObject.debtDaoObject().getDebtById(CreateDebt.this.debtId);
                com.expance.merapaisa.Model.Currency debtCurrency = appDatabaseObject.debtDaoObject().getDebtCurrency(accountId, CreateDebt.this.debtId);
                if (debtCurrency == null) {
                    debtCurrency = new com.expance.merapaisa.Model.Currency(1.0d, "Empty");
                }
                List<DebtTransEntity> allDebtTrans = appDatabaseObject.debtDaoObject().getAllDebtTrans(CreateDebt.this.debtId);
                long amount = debtById2.getAmount();
                long j3 = 0;
                for (DebtTransEntity debtTransEntity : allDebtTrans) {
                    com.expance.merapaisa.Model.Currency debtTransCurrency = appDatabaseObject.debtDaoObject().getDebtTransCurrency(accountId, CreateDebt.this.debtId, debtTransEntity.getId());
                    if (debtTransCurrency == null) {
                        if (debtTransEntity.getType() == 0) {
                            j3 += debtTransEntity.getAmount();
                        } else {
                            amount += debtTransEntity.getAmount();
                        }
                    } else {
                        double rate = debtTransCurrency.getRate() / debtCurrency.getRate();
                        if (debtTransEntity.getType() == 0) {
                            j3 = (long) (j3 + (debtTransEntity.getAmount() * rate));
                        } else {
                            amount = (long) (amount + (debtTransEntity.getAmount() * rate));
                        }
                    }
                }
                if (j3 >= amount) {
                    debtById2.setStatus(1);
                } else {
                    debtById2.setStatus(0);
                }
                appDatabaseObject.debtDaoObject().updateDebt(debtById2);
                CreateDebt.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateDebt.3.1
                    @Override // java.lang.Runnable
                    public void run() {
                        CreateDebt.this.finish();
                        CreateDebt.this.overridePendingTransition(R.anim.scale_in, R.anim.bottom_to_top);
                    }
                });
            }
        });
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.amountEditText /* 2131230807 */) {
            Intent intent = new Intent(getApplicationContext(), Calculator.class);
            intent.putExtra("amount", this.amount);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        } else if (view.getId() == R.id.dateEditText /* 2131230957 */) {
            new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() { // from class: com.ktwapps.walletmanager.CreateDebt.4
                @Override // android.app.DatePickerDialog.OnDateSetListener
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    java.util.Calendar calendar = java.util.Calendar.getInstance();
                    calendar.setTime(CreateDebt.this.date);
                    calendar.set(1, i);
                    calendar.set(2, i1);
                    calendar.set(5, i2);
                    CreateDebt.this.date = calendar.getTime();
                    CreateDebt.this.dateEditText.setText(DateHelper.getDateFromPicker(CreateDebt.this.getApplicationContext(), i, i1, i2));
                }
            }, CalendarHelper.getYearFromDate(this.date), CalendarHelper.getMonthFromDate(this.date), CalendarHelper.getDayFromDate(this.date)).show();
        } else if (view.getId() == R.id.saveLabel /* 2131231463 */) {
            if (this.isComplete) {
                if (this.type == 4) {
                    createDebt();
                } else {
                    editDebt();
                }
                finish();
                overridePendingTransition(R.anim.scale_in, R.anim.bottom_to_top);
            }
        } else if (view.getId() == R.id.timeEditText /* 2131231616 */) {
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTime(this.date);
            new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() { // from class: com.ktwapps.walletmanager.CreateDebt.5
                @Override // android.app.TimePickerDialog.OnTimeSetListener
                public void onTimeSet(TimePicker timePicker, int i, int i1) {
                    java.util.Calendar calendar2 = java.util.Calendar.getInstance();
                    calendar2.setTime(CreateDebt.this.date);
                    calendar2.set(11, i);
                    calendar2.set(12, i1);
                    CreateDebt.this.date = calendar2.getTime();
                    CreateDebt.this.timeEditText.setText(DateHelper.getTimeFromPicker(CreateDebt.this.getApplicationContext(), i, i1));
                }
            }, calendar.get(11), calendar.get(12), DateFormat.is24HourFormat(getApplicationContext())).show();
        } else if (view.getId() == R.id.walletEditText /* 2131231677 */) {
            Intent intent2 = new Intent(this, WalletPicker.class);
            intent2.putExtra("id", this.walletId);
            startActivityForResult(intent2, 2);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        }
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
        checkIsComplete();
    }

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        this.debtType = i;
        this.personEditText.setHint(i == 0 ? R.string.lend_hint : R.string.borrow_hint);
    }
}
