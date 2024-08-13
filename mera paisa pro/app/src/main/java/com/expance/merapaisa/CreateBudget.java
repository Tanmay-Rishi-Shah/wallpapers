package com.expance.merapaisa;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import com.expance.merapaisa.Adapter.SpinnerColorAdapter;
import com.expance.merapaisa.Adapter.SpinnerTextAdapter;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.Entity.BudgetCategoryEntity;
import com.expance.merapaisa.Database.Entity.BudgetEntity;
import com.expance.merapaisa.Model.Budget;
import com.expance.merapaisa.Utility.BillingHelper;
import com.expance.merapaisa.Utility.BudgetHelper;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.Executors;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

import vocsy.ads.CustomAdsListener;
import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class CreateBudget extends AppCompatActivity implements View.OnClickListener, TextWatcher, AdapterView.OnItemSelectedListener, BillingHelper.BillingListener, View.OnTouchListener {
    SpinnerTextAdapter adapter;
    long amount;
    EditText amountEditText;
    BillingHelper billingHelper;
    int budgetId;
    EditText categoryEditText;
    int categoryId;
    ArrayList<Integer> categoryIds;
    SpinnerColorAdapter colorAdapter;
    Spinner colorSpinner;
    TextView dateLabel;
    SpinnerTextAdapter dayOfMonthAdapter;
    TextView dayOfMonthLabel;
    ImageView dayOfMonthProIcon;
    Spinner dayOfMonthSpinner;
    FrameLayout dayOfMonthWrapper;
    SpinnerTextAdapter dayOfQuarterAdapter;
    TextView dayOfQuarterLabel;
    ImageView dayOfQuarterProIcon;
    Spinner dayOfQuarterSpinner;
    FrameLayout dayOfQuarterWrapper;
    SpinnerTextAdapter dayOfWeekAdapter;
    TextView dayOfWeekLabel;
    ImageView dayOfWeekProIcon;
    Spinner dayOfWeekSpinner;
    FrameLayout dayOfWeekWrapper;
    SpinnerTextAdapter dayOfYearAdapter;
    TextView dayOfYearLabel;
    ImageView dayOfYearProIcon;
    Spinner dayOfYearSpinner;
    FrameLayout dayOfYearWrapper;
    boolean isComplete;
    SpinnerTextAdapter monthOfQuarterAdapter;
    TextView monthOfQuarterLabel;
    ImageView monthOfQuarterProIcon;
    Spinner monthOfQuarterSpinner;
    FrameLayout monthOfQuarterWrapper;
    SpinnerTextAdapter monthOfYearAdapter;
    TextView monthOfYearLabel;
    ImageView monthOfYearProIcon;
    Spinner monthOfYearSpinner;
    FrameLayout monthOfYearWrapper;
    EditText nameEditText;
    Spinner periodSpinner;
    TextView saveLabel;
    int type;

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onSkuReady() {
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("amount", this.amount);
        outState.putIntegerArrayList("categoryIds", this.categoryIds);
        outState.putInt("categoryId", this.categoryId);
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
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        this.billingHelper.registerBroadCast(this);
        this.billingHelper.queryPurchases();
        super.onResume();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        this.billingHelper.unregisterBroadCast(this);
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
        this.isComplete = false;
        this.amount = 0L;
        this.categoryId = -1;
        this.categoryIds = new ArrayList<>();
        this.type = getIntent().getIntExtra(JamXmlElements.TYPE, 2);
        setContentView(R.layout.activity_create_budget);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        setUpLayout();
        if (savedInstanceState != null) {
            this.categoryId = savedInstanceState.getInt("categoryId");
            this.categoryIds = savedInstanceState.getIntegerArrayList("categoryIds");
            this.amount = savedInstanceState.getLong("amount");
        }
        checkIsComplete();
        populateData(savedInstanceState != null);
        BillingHelper billingHelper = new BillingHelper(this);
        this.billingHelper = billingHelper;
        billingHelper.setListener(this);
        this.billingHelper.setUpBillingClient();
    }

    private void populateData(boolean isSavedInstanceState) {
        if (this.type == -2) {
            this.budgetId = getIntent().getIntExtra("budgetId", 0);
            if (isSavedInstanceState) {
                return;
            }
            Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateBudget.1
                @Override // java.lang.Runnable
                public void run() {
                    AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(CreateBudget.this);
                    final Budget budgetById = appDatabaseObject.budgetDaoObject().getBudgetById(CreateBudget.this.budgetId);
                    final String accountSymbol = SharePreferenceHelper.getAccountSymbol(CreateBudget.this);
                    final int period = budgetById.getPeriod();
                    CreateBudget.this.categoryIds = (ArrayList) appDatabaseObject.budgetDaoObject().getBudgetCategoryIds(CreateBudget.this.budgetId);
                    CreateBudget.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateBudget.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            CreateBudget.this.amount = budgetById.getAmount();
                            CreateBudget.this.categoryId = budgetById.getCategoryId() == 0 ? 0 : -1;
                            CreateBudget.this.categoryIds = budgetById.getCategoryId() == 0 ? new ArrayList<>() : CreateBudget.this.categoryIds;
                            CreateBudget.this.nameEditText.setText(budgetById.getName());
                            CreateBudget.this.amountEditText.setText(Helper.getBeautifyAmount(accountSymbol, CreateBudget.this.amount));
                            CreateBudget.this.colorSpinner.setSelection(CreateBudget.this.colorAdapter.getPosition(budgetById.getColor()));
                            CreateBudget.this.categoryEditText.setText(budgetById.getCategoryId() == 0 ? CreateBudget.this.getResources().getString(R.string.all_category) : TextUtils.join(", ", budgetById.getCategories(CreateBudget.this.getApplicationContext()).split(",")));
                            CreateBudget.this.periodSpinner.setSelection(BudgetHelper.getBudgetType(period));
                            CreateBudget.this.setBudgetDate(budgetById.getStartDate(), period);
                            CreateBudget.this.checkIsComplete();
                        }
                    });
                }
            });
        }
    }

    public void setUpLayout() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.create_budget));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.dayOfWeekProIcon = (ImageView) findViewById(R.id.dayOfWeekProIcon);
        this.dayOfMonthProIcon = (ImageView) findViewById(R.id.dayOfMonthProIcon);
        this.dayOfQuarterProIcon = (ImageView) findViewById(R.id.dayOfQuarterProIcon);
        this.dayOfYearProIcon = (ImageView) findViewById(R.id.dayOfYearProIcon);
        this.monthOfQuarterProIcon = (ImageView) findViewById(R.id.monthOfQuarterProIcon);
        this.monthOfYearProIcon = (ImageView) findViewById(R.id.monthOfYearProIcon);
        this.dayOfWeekLabel = (TextView) findViewById(R.id.dayOfWeekLabel);
        this.dayOfMonthLabel = (TextView) findViewById(R.id.dayOfMonthLabel);
        this.dayOfQuarterLabel = (TextView) findViewById(R.id.dayOfQuarterLabel);
        this.dayOfYearLabel = (TextView) findViewById(R.id.dayOfYearLabel);
        this.monthOfQuarterLabel = (TextView) findViewById(R.id.monthOfQuarterLabel);
        this.monthOfYearLabel = (TextView) findViewById(R.id.monthOfYearLabel);
        this.dayOfWeekWrapper = (FrameLayout) findViewById(R.id.dayOfWeekWrapper);
        this.dayOfMonthWrapper = (FrameLayout) findViewById(R.id.dayOfMonthWrapper);
        this.dayOfQuarterWrapper = (FrameLayout) findViewById(R.id.dayOfQuarterWrapper);
        this.dayOfYearWrapper = (FrameLayout) findViewById(R.id.dayOfYearWrapper);
        this.monthOfQuarterWrapper = (FrameLayout) findViewById(R.id.monthOfQuarterWrapper);
        this.monthOfYearWrapper = (FrameLayout) findViewById(R.id.monthOfYearWrapper);
        this.dayOfWeekSpinner = (Spinner) findViewById(R.id.dayOfWeekSpinner);
        this.dayOfMonthSpinner = (Spinner) findViewById(R.id.dayOfMonthSpinner);
        this.dayOfQuarterSpinner = (Spinner) findViewById(R.id.dayOfQuarterSpinner);
        this.dayOfYearSpinner = (Spinner) findViewById(R.id.dayOfYearSpinner);
        this.monthOfQuarterSpinner = (Spinner) findViewById(R.id.monthOfQuarterSpinner);
        this.monthOfYearSpinner = (Spinner) findViewById(R.id.monthOfYearSpinner);
        this.saveLabel = (TextView) findViewById(R.id.saveLabel);
        this.nameEditText = (EditText) findViewById(R.id.nameEditText);
        this.amountEditText = (EditText) findViewById(R.id.amountEditText);
        this.categoryEditText = (EditText) findViewById(R.id.categoryEditText);
        this.dateLabel = (TextView) findViewById(R.id.dateLabel);
        this.periodSpinner = (Spinner) findViewById(R.id.periodSpinner);
        this.colorSpinner = (Spinner) findViewById(R.id.colorSpinner);
        this.adapter = new SpinnerTextAdapter(this, R.layout.list_drop_down_text, R.id.label, DataHelper.getPeriodList(this));
        this.colorAdapter = new SpinnerColorAdapter(this, R.layout.list_drop_down_color, R.id.label, DataHelper.getColorList());
        this.dayOfWeekAdapter = new SpinnerTextAdapter(this, R.layout.list_drop_down_text, R.id.label, DataHelper.getDayOfWeekData(this));
        this.dayOfMonthAdapter = new SpinnerTextAdapter(this, R.layout.list_drop_down_text, R.id.label, DataHelper.getDayOfMonthData(this));
        this.dayOfQuarterAdapter = new SpinnerTextAdapter(this, R.layout.list_drop_down_text, R.id.label, DataHelper.getDayOfMonthData(this));
        this.dayOfYearAdapter = new SpinnerTextAdapter(this, R.layout.list_drop_down_text, R.id.label, DataHelper.getDayOfMonthData(this));
        this.monthOfQuarterAdapter = new SpinnerTextAdapter(this, R.layout.list_drop_down_text, R.id.label, DataHelper.getMonthOfQuarterData(this));
        this.monthOfYearAdapter = new SpinnerTextAdapter(this, R.layout.list_drop_down_text, R.id.label, DataHelper.getMonthOfYearData(this));
        this.dayOfWeekSpinner.setAdapter((SpinnerAdapter) this.dayOfWeekAdapter);
        this.dayOfMonthSpinner.setAdapter((SpinnerAdapter) this.dayOfMonthAdapter);
        this.dayOfQuarterSpinner.setAdapter((SpinnerAdapter) this.dayOfQuarterAdapter);
        this.dayOfYearSpinner.setAdapter((SpinnerAdapter) this.dayOfYearAdapter);
        this.monthOfQuarterSpinner.setAdapter((SpinnerAdapter) this.monthOfQuarterAdapter);
        this.monthOfYearSpinner.setAdapter((SpinnerAdapter) this.monthOfYearAdapter);
        this.colorSpinner.setAdapter((SpinnerAdapter) this.colorAdapter);
        this.periodSpinner.setAdapter((SpinnerAdapter) this.adapter);
        this.nameEditText.addTextChangedListener(this);
        this.amountEditText.setText(Helper.getBeautifyAmount(SharePreferenceHelper.getAccountSymbol(this), this.amount));
        this.categoryEditText.setFocusable(false);
        this.categoryEditText.setLongClickable(false);
        this.amountEditText.setFocusable(false);
        this.amountEditText.setLongClickable(false);
        this.dayOfWeekSpinner.setSelection(SharePreferenceHelper.getFirstDayOfWeek(this) - 1);
        this.periodSpinner.setOnItemSelectedListener(this);
        this.saveLabel.setOnClickListener(this);
        this.categoryEditText.setOnClickListener(this);
        this.amountEditText.setOnClickListener(this);
        if (getIntent().hasExtra(TypedValues.CycleType.S_WAVE_PERIOD)) {
            this.periodSpinner.setSelection(getIntent().getIntExtra(TypedValues.CycleType.S_WAVE_PERIOD, 0));
        }
    }

    private void createBudget() {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateBudget.2
            @Override // java.lang.Runnable
            public void run() {
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(CreateBudget.this);
                String trim = CreateBudget.this.nameEditText.getText().toString().trim();
                int budgetPeriodFromSpinner = BudgetHelper.getBudgetPeriodFromSpinner(CreateBudget.this.periodSpinner.getSelectedItemPosition());
                int accountId = SharePreferenceHelper.getAccountId(CreateBudget.this);
                String str = DataHelper.getColorList().get(CreateBudget.this.colorSpinner.getSelectedItemPosition());
                CreateBudget.this.budgetId = (int) appDatabaseObject.budgetDaoObject().insertBudget(new BudgetEntity(trim, str, CreateBudget.this.amount, 0L, 0, budgetPeriodFromSpinner, 0, accountId, CreateBudget.this.categoryId, CreateBudget.this.getBudgetDate(), java.util.Calendar.getInstance().getTime()));
                Iterator<Integer> it = CreateBudget.this.categoryIds.iterator();
                while (it.hasNext()) {
                    appDatabaseObject.budgetDaoObject().insertBudgetCategory(new BudgetCategoryEntity(it.next().intValue(), CreateBudget.this.budgetId));
                }
                CreateBudget.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateBudget.2.1
                    @Override // java.lang.Runnable
                    public void run() {
                        CreateBudget.this.finish();
                        CreateBudget.this.overridePendingTransition(R.anim.scale_in, R.anim.bottom_to_top);
                    }
                });
            }
        });
    }

    private void editBudget() {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateBudget.3
            @Override // java.lang.Runnable
            public void run() {
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(CreateBudget.this);
                BudgetEntity budgetEntityById = appDatabaseObject.budgetDaoObject().getBudgetEntityById(CreateBudget.this.budgetId);
                String trim = CreateBudget.this.nameEditText.getText().toString().trim();
                int budgetPeriodFromSpinner = BudgetHelper.getBudgetPeriodFromSpinner(CreateBudget.this.periodSpinner.getSelectedItemPosition());
                budgetEntityById.setName(trim);
                budgetEntityById.setColor(DataHelper.getColorList().get(CreateBudget.this.colorSpinner.getSelectedItemPosition()));
                budgetEntityById.setAmount(CreateBudget.this.amount);
                budgetEntityById.setPeriod(budgetPeriodFromSpinner);
                budgetEntityById.setCategoryId(CreateBudget.this.categoryId);
                budgetEntityById.setStartDate(CreateBudget.this.getBudgetDate());
                budgetEntityById.setRepeat(0);
                appDatabaseObject.budgetDaoObject().updateBudget(budgetEntityById);
                appDatabaseObject.budgetDaoObject().deleteBudgetCategory(CreateBudget.this.budgetId);
                Iterator<Integer> it = CreateBudget.this.categoryIds.iterator();
                while (it.hasNext()) {
                    appDatabaseObject.budgetDaoObject().insertBudgetCategory(new BudgetCategoryEntity(it.next().intValue(), CreateBudget.this.budgetId));
                }
                CreateBudget.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateBudget.3.1
                    @Override // java.lang.Runnable
                    public void run() {
                        CreateBudget.this.finish();
                        CreateBudget.this.overridePendingTransition(R.anim.scale_in, R.anim.bottom_to_top);
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
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            if (requestCode == 1) {
                long longExtra = data.getLongExtra("amount", 0L);
                this.amount = longExtra;
                this.amount = longExtra >= 0 ? longExtra : 0L;
                this.amountEditText.setText(Helper.getBeautifyAmount(SharePreferenceHelper.getAccountSymbol(this), this.amount));
                checkIsComplete();
            } else if (requestCode == 5) {
                this.categoryId = data.getIntExtra("id", -1);
                this.categoryIds = data.getIntegerArrayListExtra("ids");
                this.categoryEditText.setText(data.getStringExtra("name"));
                checkIsComplete();
            }
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.amountEditText) {
            Intent intent = new Intent(getApplicationContext(), Calculator.class);
            intent.putExtra("amount", this.amount);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        } else if (id == R.id.categoryEditText) {
            Intent intent2 = new Intent(this, CategoryMultiplePicker.class);
            intent2.putExtra(JamXmlElements.TYPE, 2);
            intent2.putIntegerArrayListExtra("ids", this.categoryIds);
            intent2.putExtra("id", this.categoryId);
            startActivityForResult(intent2, 5);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        } else if (id == R.id.saveLabel && this.isComplete) {
            GoogleAds.getInstance().showCounterInterstitialAd(CreateBudget.this, new CustomAdsListener() {
                @Override
                public void onFinish() {
                    if (type == 2) {
                        createBudget();
                    } else {
                        editBudget();
                    }
                }
            });

        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkIsComplete() {
        boolean z = this.nameEditText.getText().toString().trim().length() > 0 && (this.categoryId == 0 || this.categoryIds.size() > 0) && this.amount > 0;
        this.isComplete = z;
        this.saveLabel.setAlpha(z ? 1.0f : 0.35f);
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
        checkIsComplete();
    }

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        togglePeriod(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Date getBudgetDate() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(14, 0);
        calendar.set(11, 0);
        calendar.set(13, 0);
        calendar.set(12, 0);
        int selectedItemPosition = this.periodSpinner.getSelectedItemPosition();
        if (selectedItemPosition == 0) {
            calendar.set(7, this.dayOfWeekSpinner.getSelectedItemPosition() + 1);
        } else if (selectedItemPosition == 1) {
            calendar.set(5, this.dayOfMonthSpinner.getSelectedItemPosition() + 1);
        } else if (selectedItemPosition == 2) {
            calendar.set(5, this.dayOfQuarterSpinner.getSelectedItemPosition() + 1);
            calendar.set(2, this.monthOfQuarterSpinner.getSelectedItemPosition());
        } else {
            calendar.set(5, this.dayOfYearSpinner.getSelectedItemPosition() + 1);
            calendar.set(2, this.monthOfYearSpinner.getSelectedItemPosition());
        }
        return calendar.getTime();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setBudgetDate(Date date, int period) {
        int budgetType = BudgetHelper.getBudgetType(period);
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(date);
        if (budgetType == 0) {
            this.dayOfWeekSpinner.setSelection(calendar.get(7) - 1);
        } else if (budgetType == 1) {
            this.dayOfMonthSpinner.setSelection(calendar.get(5) - 1);
        } else if (budgetType == 2) {
            this.dayOfQuarterSpinner.setSelection(calendar.get(5) - 1);
            this.monthOfQuarterSpinner.setSelection(calendar.get(2));
        } else {
            this.dayOfYearSpinner.setSelection(calendar.get(5) - 1);
            this.monthOfYearSpinner.setSelection(calendar.get(2));
        }
    }

    private void togglePeriod(int i) {
        this.dayOfWeekLabel.setVisibility(8);
        this.dayOfMonthLabel.setVisibility(8);
        this.dayOfQuarterLabel.setVisibility(8);
        this.dayOfYearLabel.setVisibility(8);
        this.monthOfQuarterLabel.setVisibility(8);
        this.monthOfYearLabel.setVisibility(8);
        this.dayOfWeekWrapper.setVisibility(8);
        this.dayOfMonthWrapper.setVisibility(8);
        this.dayOfQuarterWrapper.setVisibility(8);
        this.dayOfYearWrapper.setVisibility(8);
        this.monthOfQuarterWrapper.setVisibility(8);
        this.monthOfYearWrapper.setVisibility(8);
        this.dayOfWeekProIcon.setVisibility(8);
        this.dayOfMonthProIcon.setVisibility(8);
        this.dayOfQuarterProIcon.setVisibility(8);
        this.dayOfYearProIcon.setVisibility(8);
        this.monthOfQuarterProIcon.setVisibility(8);
        this.monthOfYearProIcon.setVisibility(8);
        if (i == 0) {
            this.dayOfWeekLabel.setVisibility(0);
            this.dayOfWeekWrapper.setVisibility(0);
            if (this.billingHelper.getBillingStatus() != 2) {
                this.dayOfWeekProIcon.setVisibility(0);
            }
        } else if (i == 1) {
            this.dayOfMonthLabel.setVisibility(0);
            this.dayOfMonthWrapper.setVisibility(0);
            if (this.billingHelper.getBillingStatus() != 2) {
                this.dayOfMonthProIcon.setVisibility(0);
            }
        } else if (i == 2) {
            this.dayOfQuarterLabel.setVisibility(0);
            this.monthOfQuarterLabel.setVisibility(0);
            this.dayOfQuarterWrapper.setVisibility(0);
            this.monthOfQuarterWrapper.setVisibility(0);
            this.dayOfQuarterLabel.setVisibility(0);
            this.monthOfQuarterLabel.setVisibility(0);
            if (this.billingHelper.getBillingStatus() != 2) {
                this.dayOfQuarterProIcon.setVisibility(0);
                this.monthOfQuarterProIcon.setVisibility(0);
            }
        } else {
            this.dayOfYearLabel.setVisibility(0);
            this.monthOfYearLabel.setVisibility(0);
            this.dayOfYearWrapper.setVisibility(0);
            this.monthOfYearWrapper.setVisibility(0);
            this.dayOfYearLabel.setVisibility(0);
            this.monthOfYearLabel.setVisibility(0);
            if (this.billingHelper.getBillingStatus() != 2) {
                this.dayOfYearProIcon.setVisibility(0);
                this.monthOfYearProIcon.setVisibility(0);
            }
        }
    }

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onLoaded() {
        checkSubscription();
    }

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onPurchasedSucceed() {
        checkSubscription();
    }

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onPurchasedPending() {
        checkSubscription();
    }

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onReceiveBroadCast() {
        checkSubscription();
    }

    private void checkSubscription() {
        if (SharePreferenceHelper.getPremium(this) != 2) {
            this.dayOfWeekSpinner.setAlpha(0.35f);
            this.dayOfMonthSpinner.setAlpha(0.35f);
            this.dayOfQuarterSpinner.setAlpha(0.35f);
            this.dayOfYearSpinner.setAlpha(0.35f);
            this.monthOfQuarterSpinner.setAlpha(0.35f);
            this.monthOfYearSpinner.setAlpha(0.35f);
            this.dayOfWeekSpinner.setOnTouchListener(this);
            this.dayOfMonthSpinner.setOnTouchListener(this);
            this.dayOfQuarterSpinner.setOnTouchListener(this);
            this.dayOfYearSpinner.setOnTouchListener(this);
            this.monthOfQuarterSpinner.setOnTouchListener(this);
            this.monthOfYearSpinner.setOnTouchListener(this);
        } else {
            this.dayOfWeekSpinner.setOnTouchListener(null);
            this.dayOfMonthSpinner.setOnTouchListener(null);
            this.dayOfQuarterSpinner.setOnTouchListener(null);
            this.dayOfYearSpinner.setOnTouchListener(null);
            this.monthOfQuarterSpinner.setOnTouchListener(null);
            this.monthOfYearSpinner.setOnTouchListener(null);
            this.dayOfWeekSpinner.setAlpha(1.0f);
            this.dayOfMonthSpinner.setAlpha(1.0f);
            this.dayOfQuarterSpinner.setAlpha(1.0f);
            this.dayOfYearSpinner.setAlpha(1.0f);
            this.monthOfQuarterSpinner.setAlpha(1.0f);
            this.monthOfYearSpinner.setAlpha(1.0f);
        }
        Spinner spinner = this.periodSpinner;
        if (spinner != null) {
            togglePeriod(spinner.getSelectedItemPosition());
        }
        this.adapter.notifyDataSetChanged();
        invalidateOptionsMenu();
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == 1 && this.billingHelper.getBillingStatus() != 2) {
            startActivity(new Intent(getApplicationContext(), Premium.class));
            overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
        }
        return true;
    }
}
