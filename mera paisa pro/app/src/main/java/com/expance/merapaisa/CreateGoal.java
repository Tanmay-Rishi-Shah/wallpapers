package com.expance.merapaisa;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.expance.merapaisa.Adapter.SpinnerColorAdapter;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.Entity.AccountEntity;
import com.expance.merapaisa.Database.Entity.GoalEntity;
import com.expance.merapaisa.Utility.CalendarHelper;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Utility.DateHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;

import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;

import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class CreateGoal extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TextWatcher {
    SpinnerColorAdapter adapter;
    long amount;
    EditText amountEditText;
    Spinner colorSpinner;
    String currencyCode;
    EditText currencyEditText;
    ArrayList<String> currencyList;
    ArrayList<String> currencySymbolList;
    Date date;
    EditText expectEditText;
    int goalId;
    boolean isComplete;
    EditText nameEditText;
    String originalCode;
    TextView saveLabel;
    int type;

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("date", this.date.getTime());
        outState.putLong("amount", this.amount);
        outState.putString("originalCode", this.originalCode);
        outState.putString("currencyCode", this.currencyCode);
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
        setContentView(R.layout.activity_create_goal);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        this.currencyList = DataHelper.getCurrencyList();
        this.currencySymbolList = DataHelper.getCurrencySymbolList();
        this.isComplete = false;
        this.amount = 0L;
        this.date = DateHelper.getGoalDateTime(DateHelper.getCurrentDateTime());
        this.type = getIntent().getIntExtra(JamXmlElements.TYPE, 3);
        setUpLayout();
        if (savedInstanceState != null) {
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTimeInMillis(savedInstanceState.getLong("date"));
            this.date = calendar.getTime();
            this.amount = savedInstanceState.getLong("amount");
            this.originalCode = savedInstanceState.getString("originalCode");
            this.currencyCode = savedInstanceState.getString("currencyCode");
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

    public void setUpLayout() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.create_saving_goal));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.expectEditText = (EditText) findViewById(R.id.expectEditText);
        this.nameEditText = (EditText) findViewById(R.id.nameEditText);
        this.amountEditText = (EditText) findViewById(R.id.amountEditText);
        this.currencyEditText = (EditText) findViewById(R.id.currencyEditText);
        this.colorSpinner = (Spinner) findViewById(R.id.colorSpinner);
        this.saveLabel = (TextView) findViewById(R.id.saveLabel);
        SpinnerColorAdapter spinnerColorAdapter = new SpinnerColorAdapter(this, R.layout.list_drop_down_color, R.id.label, DataHelper.getColorList());
        this.adapter = spinnerColorAdapter;
        this.colorSpinner.setAdapter((SpinnerAdapter) spinnerColorAdapter);
        this.expectEditText.setText(DateHelper.getFormattedDate(getApplicationContext(), this.date));
        this.amountEditText.setText(Helper.getBeautifyAmount(SharePreferenceHelper.getAccountSymbol(this), 0L));
        this.saveLabel.setOnClickListener(this);
        this.nameEditText.addTextChangedListener(this);
        this.amountEditText.setOnClickListener(this);
        this.amountEditText.setFocusable(false);
        this.amountEditText.setLongClickable(false);
        this.expectEditText.setOnClickListener(this);
        this.expectEditText.setFocusable(false);
        this.expectEditText.setLongClickable(false);
        this.currencyEditText.setFocusable(false);
        this.currencyEditText.setOnClickListener(this);
        this.currencyEditText.setLongClickable(false);
        checkIsComplete();
    }

    private void checkIsComplete() {
        boolean z = this.nameEditText.getText().toString().trim().length() > 0 && this.amount > 0;
        this.isComplete = z;
        this.saveLabel.setAlpha(z ? 1.0f : 0.35f);
    }

    private void populateData(boolean isSavedInstanceState) {
        if (isSavedInstanceState) {
            return;
        }
        this.goalId = getIntent().getIntExtra("goalId", 0);
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateGoal.1
            @Override // java.lang.Runnable
            public void run() {
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(CreateGoal.this.getApplicationContext());
                final AccountEntity entityById = appDatabaseObject.accountDaoObject().getEntityById(SharePreferenceHelper.getAccountId(CreateGoal.this.getApplicationContext()));
                if (CreateGoal.this.type == -3) {
                    final GoalEntity goalById = appDatabaseObject.goalDaoObject().getGoalById(CreateGoal.this.goalId);
                    CreateGoal.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateGoal.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            CreateGoal.this.amount = goalById.getAmount();
                            CreateGoal.this.date = goalById.getExpectDate();
                            CreateGoal.this.nameEditText.setText(goalById.getName());
                            CreateGoal.this.amountEditText.setText(Helper.getBeautifyAmount(SharePreferenceHelper.getAccountSymbol(CreateGoal.this.getApplicationContext()), CreateGoal.this.amount));
                            CreateGoal.this.colorSpinner.setSelection(CreateGoal.this.adapter.getPosition(goalById.getColor()));
                            CreateGoal.this.expectEditText.setText(DateHelper.getFormattedDate(CreateGoal.this.getApplicationContext(), CreateGoal.this.date));
                            String currency = entityById.getCurrency();
                            if (goalById.getCurrency() == null || goalById.getCurrency().length() == 0) {
                                CreateGoal.this.originalCode = currency;
                            } else {
                                CreateGoal.this.originalCode = goalById.getCurrency();
                            }
                            CreateGoal.this.currencyEditText.setText(DataHelper.getCurrencyList().get(DataHelper.getCurrencyCode().indexOf(CreateGoal.this.originalCode)));
                        }
                    });
                    return;
                }
                CreateGoal.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateGoal.1.2
                    @Override // java.lang.Runnable
                    public void run() {
                        CreateGoal.this.originalCode = entityById.getCurrency();
                        CreateGoal.this.currencyEditText.setText(DataHelper.getCurrencyList().get(DataHelper.getCurrencyCode().indexOf(CreateGoal.this.originalCode)));
                    }
                });
            }
        });
    }

    private void createGoal() {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateGoal.2
            @Override // java.lang.Runnable
            public void run() {
                AppDatabaseObject.getInstance(CreateGoal.this).goalDaoObject().insertGoal(new GoalEntity(CreateGoal.this.nameEditText.getText().toString().trim(), DataHelper.getColorList().get(CreateGoal.this.colorSpinner.getSelectedItemPosition()), 0L, CreateGoal.this.amount, 0, SharePreferenceHelper.getAccountId(CreateGoal.this), CreateGoal.this.date, null, CreateGoal.this.currencyCode));
                CreateGoal.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateGoal.2.1
                    @Override // java.lang.Runnable
                    public void run() {
                        CreateGoal.this.finish();
                        CreateGoal.this.overridePendingTransition(R.anim.scale_in, R.anim.bottom_to_top);
                    }
                });
            }
        });
    }

    private void editGoal() {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateGoal.3
            @Override // java.lang.Runnable
            public void run() {
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(CreateGoal.this);
                GoalEntity goalById = appDatabaseObject.goalDaoObject().getGoalById(CreateGoal.this.getIntent().getIntExtra("goalId", 0));
                long saved = goalById.getSaved();
                if (saved >= CreateGoal.this.amount) {
                    saved = CreateGoal.this.amount;
                }
                goalById.setName(CreateGoal.this.nameEditText.getText().toString().trim());
                goalById.setColor(DataHelper.getColorList().get(CreateGoal.this.colorSpinner.getSelectedItemPosition()));
                goalById.setSaved(saved);
                goalById.setAmount(CreateGoal.this.amount);
                goalById.setExpectDate(CreateGoal.this.date);
                goalById.setCurrency(CreateGoal.this.currencyCode);
                appDatabaseObject.goalDaoObject().update(goalById);
                CreateGoal.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateGoal.3.1
                    @Override // java.lang.Runnable
                    public void run() {
                        CreateGoal.this.finish();
                        CreateGoal.this.overridePendingTransition(R.anim.scale_in, R.anim.bottom_to_top);
                    }
                });
            }
        });
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

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            if (requestCode == 1) {
                long longExtra = data.getLongExtra("amount", 0L);
                this.amount = longExtra;
                this.amount = longExtra >= 0 ? longExtra : 0L;
                this.amountEditText.setText(Helper.getBeautifyAmount(SharePreferenceHelper.getAccountSymbol(this), this.amount));
                checkIsComplete();
            } else if (requestCode != 7 || data == null) {
            } else {
                int intExtra = data.getIntExtra(FirebaseAnalytics.Param.INDEX, 0);
                this.currencyCode = DataHelper.getCurrencyCode().get(intExtra);
                this.currencyEditText.setText(this.currencyList.get(intExtra));
            }
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.amountEditText /* 2131230807 */) {
            Intent intent = new Intent(getApplicationContext(), Calculator.class);
            intent.putExtra("amount", this.amount);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        } else if (view.getId() == R.id.currencyEditText /* 2131230935 */) {
            Intent intent2 = new Intent(this, CurrencyPicker.class);
            ArrayList<String> arrayList = this.currencyList;
            ArrayList<String> currencyCode = DataHelper.getCurrencyCode();
            String str = this.currencyCode;
            intent2.putExtra(FirebaseAnalytics.Param.CURRENCY, arrayList.get(currencyCode.indexOf((str == null || str.length() == 0) ? this.originalCode : this.currencyCode)));
            startActivityForResult(intent2, 7);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        } else if (view.getId() == R.id.expectEditText /* 2131231067 */) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, CalendarHelper.getYearFromDate(this.date), CalendarHelper.getMonthFromDate(this.date), CalendarHelper.getDayFromDate(this.date));
            datePickerDialog.getDatePicker().setMinDate(CalendarHelper.getCustomStartDate(DateHelper.getCurrentDateTime()));
            datePickerDialog.show();
        } else if (view.getId() == R.id.saveLabel /* 2131231463 */) {
            if (this.isComplete) {
                if (this.type == 3) {
                    createGoal();
                } else {
                    editGoal();
                }
            }
        }
    }

    @Override // android.app.DatePickerDialog.OnDateSetListener
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        this.date = CalendarHelper.getDateFromPicker(i, i1, i2);
        this.expectEditText.setText(DateHelper.getFormattedDate(getApplicationContext(), this.date));
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
        checkIsComplete();
    }
}
