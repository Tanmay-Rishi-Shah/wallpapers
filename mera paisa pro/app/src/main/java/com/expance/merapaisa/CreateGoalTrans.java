package com.expance.merapaisa;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
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
import com.expance.merapaisa.Database.Entity.GoalEntity;
import com.expance.merapaisa.Database.Entity.GoalTransEntity;
import com.expance.merapaisa.Utility.CalendarHelper;
import com.expance.merapaisa.Utility.DateHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;

import java.util.Date;
import java.util.concurrent.Executors;

import vocsy.ads.CustomAdsListener;
import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class CreateGoalTrans extends AppCompatActivity implements View.OnClickListener {
    long amount;
    EditText amountEditText;
    Date date;
    EditText dateEditText;
    ConstraintLayout deleteWrapper;
    int goalId;
    int goalTransId;
    boolean isComplete;
    EditText noteEditText;
    TextView saveLabel;
    String symbol;
    EditText timeEditText;
    Spinner typeSpinner;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("date", this.date.getTime());
        outState.putLong("amount", this.amount);
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
//        if (!getResources().getBoolean(R.bool.isTablet)) {
//            setRequestedOrientation(1);
//        }
        setContentView(R.layout.activity_create_goal_trans);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        initialData();
        setUpLayout();
        if (savedInstanceState != null) {
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTimeInMillis(savedInstanceState.getLong("date"));
            this.date = calendar.getTime();
            this.amount = savedInstanceState.getLong("amount");
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
        this.isComplete = false;
        this.amount = 0L;
        this.symbol = getIntent().getStringExtra("symbol");
        this.goalId = getIntent().getIntExtra("goalId", 0);
        this.goalTransId = getIntent().getIntExtra("goalTransId", 0);
    }

    private void populateData(final boolean isSaveInstanceState) {
        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateGoalTrans.1
            @Override // java.lang.Runnable
            public void run() {
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(CreateGoalTrans.this.getApplicationContext());
                java.util.Calendar calendar = java.util.Calendar.getInstance();
                calendar.set(14, 0);
                calendar.set(13, 0);
                calendar.set(12, 0);
                calendar.set(11, 0);
                calendar.add(5, 1);
                if (isSaveInstanceState) {
                    return;
                }
                GoalTransEntity goalTransById = appDatabaseObject.goalDaoObject().getGoalTransById(CreateGoalTrans.this.goalTransId);
                CreateGoalTrans.this.date = goalTransById.getDateTime();
                CreateGoalTrans.this.amount = goalTransById.getAmount();
                final int type = goalTransById.getType();
                final String note = goalTransById.getNote();
                CreateGoalTrans.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateGoalTrans.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        CreateGoalTrans.this.typeSpinner.setSelection(type == 14 ? 0 : 1);
                        EditText editText = CreateGoalTrans.this.noteEditText;
                        String str = note;
                        if (str == null) {
                            str = "";
                        }
                        editText.setText(str);
                        CreateGoalTrans.this.timeEditText.setText(DateHelper.getFormattedTime(CreateGoalTrans.this.date, CreateGoalTrans.this.getApplicationContext()));
                        CreateGoalTrans.this.dateEditText.setText(DateHelper.getFormattedDate(CreateGoalTrans.this.getApplicationContext(), CreateGoalTrans.this.date));
                        CreateGoalTrans.this.amountEditText.setText(Helper.getBeautifyAmount(CreateGoalTrans.this.symbol, CreateGoalTrans.this.amount));
                        CreateGoalTrans.this.checkIsComplete();
                    }
                });
            }
        });
    }

    private void setUpLayout() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.transaction);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.saveLabel = (TextView) findViewById(R.id.saveLabel);
        this.typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
        this.timeEditText = (EditText) findViewById(R.id.timeEditText);
        this.dateEditText = (EditText) findViewById(R.id.dateEditText);
        this.noteEditText = (EditText) findViewById(R.id.noteEditText);
        this.amountEditText = (EditText) findViewById(R.id.amountEditText);
        this.deleteWrapper = (ConstraintLayout) findViewById(R.id.deleteWrapper);
        this.typeSpinner.setAdapter((SpinnerAdapter) new ArrayAdapter(this, 17367049, new String[]{getResources().getString(R.string.deposit), getResources().getString(R.string.withdraw)}));
        this.saveLabel.setOnClickListener(this);
        this.dateEditText.setOnClickListener(this);
        this.amountEditText.setOnClickListener(this);
        this.dateEditText.setLongClickable(false);
        this.amountEditText.setLongClickable(false);
        this.dateEditText.setFocusable(false);
        this.amountEditText.setFocusable(false);
        this.deleteWrapper.setOnClickListener(this);
        this.timeEditText.setOnClickListener(this);
        this.timeEditText.setLongClickable(false);
        this.timeEditText.setFocusable(false);
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
            new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() { // from class: com.ktwapps.walletmanager.CreateGoalTrans.2
                @Override // android.app.DatePickerDialog.OnDateSetListener
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    java.util.Calendar calendar = java.util.Calendar.getInstance();
                    calendar.setTime(CreateGoalTrans.this.date);
                    calendar.set(1, i);
                    calendar.set(2, i1);
                    calendar.set(5, i2);
                    CreateGoalTrans.this.date = calendar.getTime();
                    CreateGoalTrans.this.dateEditText.setText(DateHelper.getDateFromPicker(CreateGoalTrans.this.getApplicationContext(), i, i1, i2));
                }
            }, CalendarHelper.getYearFromDate(this.date), CalendarHelper.getMonthFromDate(this.date), CalendarHelper.getDayFromDate(this.date)).show();
        } else if (view.getId() == R.id.deleteWrapper /* 2131230988 */) {
            deleteTransaction();
        } else if (view.getId() == R.id.saveLabel /* 2131231463 */) {
            if (this.isComplete) {
                GoogleAds.getInstance().showCounterInterstitialAd(CreateGoalTrans.this, new CustomAdsListener() {
                    @Override
                    public void onFinish() {
                        editTransaction();
                    }
                });

            }
        } else if (view.getId() == R.id.timeEditText /* 2131231616 */) {
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTime(this.date);
            new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() { // from class: com.ktwapps.walletmanager.CreateGoalTrans.3
                @Override // android.app.TimePickerDialog.OnTimeSetListener
                public void onTimeSet(TimePicker timePicker, int i, int i1) {
                    java.util.Calendar calendar2 = java.util.Calendar.getInstance();
                    calendar2.setTime(CreateGoalTrans.this.date);
                    calendar2.set(11, i);
                    calendar2.set(12, i1);
                    CreateGoalTrans.this.date = calendar2.getTime();
                    CreateGoalTrans.this.timeEditText.setText(DateHelper.getTimeFromPicker(CreateGoalTrans.this.getApplicationContext(), i, i1));
                }
            }, calendar.get(11), calendar.get(12), DateFormat.is24HourFormat(getApplicationContext())).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 1) {
            long longExtra = data.getLongExtra("amount", 0L);
            this.amount = longExtra;
            long j = longExtra >= 0 ? longExtra : 0L;
            this.amount = j;
            this.amountEditText.setText(Helper.getBeautifyAmount(this.symbol, j));
            checkIsComplete();
        }
    }

    private void editTransaction() {
        final int i = this.typeSpinner.getSelectedItemPosition() == 0 ? 14 : -14;
        final String obj = this.noteEditText.getText().toString();
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateGoalTrans.4
            @Override // java.lang.Runnable
            public void run() {
                long j;
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(CreateGoalTrans.this);
                GoalTransEntity goalTransById = appDatabaseObject.goalDaoObject().getGoalTransById(CreateGoalTrans.this.goalTransId);
                GoalEntity goalById = appDatabaseObject.goalDaoObject().getGoalById(CreateGoalTrans.this.goalId);
                long saved = goalById.getSaved();
                long amount = goalTransById.getAmount();
                long j2 = goalTransById.getType() == -14 ? saved + amount : saved - amount;
                goalTransById.setNote(obj.length() == 0 ? null : obj);
                goalTransById.setDateTime(CreateGoalTrans.this.date);
                goalTransById.setType(i);
                goalTransById.setAmount(CreateGoalTrans.this.amount);
                appDatabaseObject.goalDaoObject().updateTrans(goalTransById);
                if (i == -14) {
                    j = j2 - CreateGoalTrans.this.amount;
                } else {
                    j = j2 + CreateGoalTrans.this.amount;
                }
                goalById.setSaved(j);
                if (goalById.getStatus() == 1 && goalById.getAmount() > goalById.getSaved()) {
                    goalById.setStatus(0);
                }
                appDatabaseObject.goalDaoObject().update(goalById);
                CreateGoalTrans.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateGoalTrans.4.1
                    @Override // java.lang.Runnable
                    public void run() {
                        CreateGoalTrans.this.finish();
                        CreateGoalTrans.this.overridePendingTransition(R.anim.scale_in, R.anim.bottom_to_top);
                    }
                });
            }
        });
    }

    private void deleteTransaction() {
        Helper.showDialog(this, "", getResources().getString(R.string.transaction_delete_message), getResources().getString(R.string.delete_positive), getResources().getString(R.string.cancel), new AnonymousClass5());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.ktwapps.walletmanager.CreateGoalTrans$5  reason: invalid class name */
    /* loaded from: classes3.dex */
    public class AnonymousClass5 implements DialogInterface.OnClickListener {
        AnonymousClass5() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateGoalTrans.5.1
                @Override // java.lang.Runnable
                public void run() {
                    AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(CreateGoalTrans.this.getApplicationContext());
                    GoalEntity goalById = appDatabaseObject.goalDaoObject().getGoalById(CreateGoalTrans.this.goalId);
                    GoalTransEntity goalTransById = appDatabaseObject.goalDaoObject().getGoalTransById(CreateGoalTrans.this.goalTransId);
                    long saved = goalById.getSaved();
                    long amount = goalTransById.getAmount();
                    goalById.setSaved(goalTransById.getType() == -14 ? saved + amount : saved - amount);
                    if (goalById.getStatus() == 1 && goalById.getAmount() > goalById.getSaved()) {
                        goalById.setStatus(0);
                    }
                    appDatabaseObject.goalDaoObject().update(goalById);
                    appDatabaseObject.goalDaoObject().deleteGoalTrans(CreateGoalTrans.this.goalTransId);
                    CreateGoalTrans.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateGoalTrans.5.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            CreateGoalTrans.this.finish();
                            CreateGoalTrans.this.overridePendingTransition(R.anim.scale_in, R.anim.bottom_to_top);
                        }
                    });
                }
            });
        }
    }
}
