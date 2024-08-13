package com.expance.merapaisa;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.expance.merapaisa.Adapter.SpinnerColorAdapter;
import com.expance.merapaisa.Adapter.SpinnerCurrencyAdapter;
import com.expance.merapaisa.Adapter.SpinnerTextAdapter;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.Entity.AccountEntity;
import com.expance.merapaisa.Database.Entity.WalletEntity;
import com.expance.merapaisa.Database.ViewModel.CurrencyViewModel;
import com.expance.merapaisa.Model.Currencies;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;

import org.apache.commons.lang3.StringUtils;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class CreateWallet extends AppCompatActivity implements View.OnClickListener, TextWatcher, AdapterView.OnItemSelectedListener {
    private long amount;
    private EditText amountEditText;
    private TextView amountLabel;
    private SpinnerColorAdapter colorAdapter;
    private ArrayList<String> colorList;
    private Spinner colorSpinner;
    private ConstraintLayout creditDateWrapper;
    private List<Currencies> currenciesList;
    private SpinnerCurrencyAdapter currencyAdapter;
    private String currencyCode;
    private ArrayList<String> currencyList;
    private Spinner currencySpinner;
    private CurrencyViewModel currencyViewModel;
    SpinnerTextAdapter dueDateAdapter;
    private Spinner dueDateSpinner;
    private ConstraintLayout excludeWrapper;
    private int icon;
    private ImageView iconView;
    private long initialAmount;
    private boolean isComplete;
    private String mainCurrency;
    private EditText nameEditText;
    private TextView rateLabel;
    private TextView saveLabel;
    SpinnerTextAdapter statementDateAdapter;
    private Spinner statementDateSpinner;
    private Switch switchView;
    private String symbol;
    private int type;
    SpinnerTextAdapter typeAdapter;
    private TextView typeLabel;
    private Spinner typeSpinner;
    private FrameLayout typeWrapper;
    WalletEntity walletEntity;

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
    @Override
    // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("icon", this.icon);
        outState.putLong("amount", this.amount);
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
        setContentView(R.layout.activity_create_wallet);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        this.amount = 0L;
        this.icon = 0;
        this.type = getIntent().getIntExtra(JamXmlElements.TYPE, 1);
        this.isComplete = false;
        this.colorList = DataHelper.getColorList();
        this.currencyList = new ArrayList<>();
        this.colorAdapter = new SpinnerColorAdapter(this, R.layout.list_drop_down_color, R.id.label, DataHelper.getColorList());
        ArrayList arrayList = new ArrayList();
        arrayList.add(getResources().getString(R.string.general));
        arrayList.add(getResources().getString(R.string.credit_card));
        this.typeAdapter = new SpinnerTextAdapter(this, R.layout.list_drop_down_text, R.id.label, arrayList);
        this.dueDateAdapter = new SpinnerTextAdapter(this, R.layout.list_drop_down_text, R.id.label, DataHelper.getDayOfMonthData(this));
        this.statementDateAdapter = new SpinnerTextAdapter(this, R.layout.list_drop_down_text, R.id.label, DataHelper.getDayOfMonthData(this));
        if (savedInstanceState != null) {
            this.icon = savedInstanceState.getInt("icon");
            this.amount = savedInstanceState.getLong("amount");
        }
        setUpLayout(savedInstanceState);
        checkIsComplete();
    }

    private void setUpLayout(Bundle savedInstanceState) {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.create_wallet));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.typeLabel = (TextView) findViewById(R.id.typeLabel);
        this.typeWrapper = (FrameLayout) findViewById(R.id.typeWrapper);
        this.rateLabel = (TextView) findViewById(R.id.rateLabel);
        this.colorSpinner = (Spinner) findViewById(R.id.colorSpinner);
        this.currencySpinner = (Spinner) findViewById(R.id.currencySpinner);
        this.nameEditText = (EditText) findViewById(R.id.nameEditText);
        this.amountEditText = (EditText) findViewById(R.id.amountEditText);
        this.excludeWrapper = (ConstraintLayout) findViewById(R.id.repeatWrapper);
        this.switchView = (Switch) findViewById(R.id.switchView);
        this.iconView = (ImageView) findViewById(R.id.iconView);
        this.saveLabel = (TextView) findViewById(R.id.saveLabel);
        this.typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
        this.dueDateSpinner = (Spinner) findViewById(R.id.dueDateSpinner);
        this.statementDateSpinner = (Spinner) findViewById(R.id.statementDateSpinner);
        this.creditDateWrapper = (ConstraintLayout) findViewById(R.id.creditDateWrapper);
        this.amountLabel = (TextView) findViewById(R.id.amountLabel);
        this.saveLabel.setOnClickListener(this);
        this.amountEditText.setFocusable(false);
        this.amountEditText.setOnClickListener(this);
        this.amountEditText.setLongClickable(false);
        this.excludeWrapper.setOnClickListener(this);
        this.iconView.setOnClickListener(this);
        this.nameEditText.addTextChangedListener(this);
        this.currencySpinner.setOnItemSelectedListener(this);
        this.typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.ktwapps.walletmanager.CreateWallet.1
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CreateWallet.this.excludeWrapper.setVisibility(i == 0 ? 0 : 8);
                CreateWallet.this.creditDateWrapper.setVisibility(i != 1 ? 8 : 0);
                CreateWallet.this.amountLabel.setText(i == 0 ? R.string.initial_amount : R.string.credit_limit);
            }
        });
        this.colorSpinner.setAdapter((SpinnerAdapter) this.colorAdapter);
        this.dueDateSpinner.setAdapter((SpinnerAdapter) this.dueDateAdapter);
        this.statementDateSpinner.setAdapter((SpinnerAdapter) this.statementDateAdapter);
        this.typeSpinner.setAdapter((SpinnerAdapter) this.typeAdapter);
        this.iconView.setImageResource(DataHelper.getWalletIcons().get(this.icon).intValue());
        this.creditDateWrapper.setVisibility(8);
        populateData(savedInstanceState);
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

    private void populateData(final Bundle savedInstanceState) {
        CurrencyViewModel currencyViewModel = (CurrencyViewModel) new ViewModelProvider(this).get(CurrencyViewModel.class);
        this.currencyViewModel = currencyViewModel;
        currencyViewModel.getCurrencyList().observe(this, new Observer<List<Currencies>>() { // from class: com.ktwapps.walletmanager.CreateWallet.2
            @Override // androidx.lifecycle.Observer
            public void onChanged(List<Currencies> currencies) {
                CreateWallet.this.currencyList.clear();
                CreateWallet.this.currenciesList = currencies;
                for (Currencies currencies2 : currencies) {
                    CreateWallet.this.currencyList.add(currencies2.getSubCurrency());
                }
                CreateWallet.this.currencyList.add(CreateWallet.this.getResources().getString(R.string.add_currency));
                CreateWallet createWallet = CreateWallet.this;
                CreateWallet createWallet2 = CreateWallet.this;
                createWallet.currencyAdapter = new SpinnerCurrencyAdapter(createWallet2, R.layout.list_drop_down_text, R.id.label, createWallet2.currencyList);
                if (CreateWallet.this.currencySpinner.getAdapter() == null) {
                    CreateWallet.this.currencySpinner.setAdapter((SpinnerAdapter) CreateWallet.this.currencyAdapter);
                }
                if (CreateWallet.this.currencyCode != null) {
                    CreateWallet.this.currencySpinner.setSelection(CreateWallet.this.currencyAdapter.getPosition(CreateWallet.this.currencyCode));
                }
            }
        });
        int i = this.type;
        if (i != -1) {
            if (i == 1) {
                final int accountId = SharePreferenceHelper.getAccountId(this);
                Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateWallet.4
                    @Override // java.lang.Runnable
                    public void run() {
                        AccountEntity entityById = AppDatabaseObject.getInstance(CreateWallet.this.getApplicationContext()).accountDaoObject().getEntityById(accountId);
                        CreateWallet.this.mainCurrency = entityById.getCurrency();
                        Bundle bundle = savedInstanceState;
                        if (bundle != null) {
                            CreateWallet.this.currencyCode = bundle.getString("currencyCode");
                        } else {
                            CreateWallet createWallet = CreateWallet.this;
                            createWallet.currencyCode = createWallet.mainCurrency;
                        }
                        int indexOf = DataHelper.getCurrencyCode().indexOf(CreateWallet.this.currencyCode);
                        CreateWallet.this.symbol = DataHelper.getCurrencySymbolList().get(indexOf);
                        CreateWallet.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateWallet.4.1
                            @Override // java.lang.Runnable
                            public void run() {
                                CreateWallet.this.rateLabel.setVisibility(8);
                                if (CreateWallet.this.currencyAdapter != null) {
                                    CreateWallet.this.currencySpinner.setAdapter((SpinnerAdapter) CreateWallet.this.currencyAdapter);
                                    CreateWallet.this.currencySpinner.setSelection(CreateWallet.this.currencyAdapter.getPosition(CreateWallet.this.currencyCode));
                                }
                            }
                        });
                    }
                });
                return;
            }
            return;
        }
        this.typeWrapper.setVisibility(8);
        this.typeLabel.setVisibility(8);
        final int intExtra = getIntent().getIntExtra("walletId", 0);
        final int accountId2 = SharePreferenceHelper.getAccountId(this);
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateWallet.3
            @Override // java.lang.Runnable
            public void run() {
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(CreateWallet.this);
                AccountEntity entityById = appDatabaseObject.accountDaoObject().getEntityById(accountId2);
                CreateWallet.this.mainCurrency = entityById.getCurrency();
                CreateWallet.this.walletEntity = appDatabaseObject.walletDaoObject().getWalletById(intExtra);
                Bundle bundle = savedInstanceState;
                if (bundle != null) {
                    CreateWallet.this.currencyCode = bundle.getString("currencyCode");
                } else {
                    CreateWallet createWallet = CreateWallet.this;
                    createWallet.currencyCode = createWallet.walletEntity.getCurrency();
                }
                int indexOf = DataHelper.getCurrencyCode().indexOf(CreateWallet.this.currencyCode);
                CreateWallet.this.symbol = DataHelper.getCurrencySymbolList().get(indexOf);
                CreateWallet.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateWallet.3.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (savedInstanceState == null) {
                            CreateWallet.this.amount = CreateWallet.this.walletEntity.getType() == 1 ? CreateWallet.this.walletEntity.getCreditLimit() : CreateWallet.this.walletEntity.getInitialAmount();
                            CreateWallet.this.icon = CreateWallet.this.walletEntity.getIcon();
                            CreateWallet.this.nameEditText.setText(CreateWallet.this.walletEntity.getName());
                            CreateWallet.this.switchView.setChecked(CreateWallet.this.walletEntity.getExclude() == 1);
                            CreateWallet.this.colorSpinner.setSelection(CreateWallet.this.colorList.indexOf(CreateWallet.this.walletEntity.getColor()));
                            CreateWallet.this.dueDateSpinner.setSelection(CreateWallet.this.walletEntity.getDueDate() - 1);
                            CreateWallet.this.statementDateSpinner.setSelection(CreateWallet.this.walletEntity.getStatementDate() - 1);
                        }
                        if (CreateWallet.this.walletEntity.getType() == 1) {
                            CreateWallet.this.excludeWrapper.setVisibility(8);
                            CreateWallet.this.creditDateWrapper.setVisibility(0);
                            CreateWallet.this.amountLabel.setText(R.string.credit_limit);
                        }
                        CreateWallet.this.initialAmount = CreateWallet.this.walletEntity.getInitialAmount();
                        CreateWallet.this.amountEditText.setText(Helper.getBeautifyAmount(CreateWallet.this.symbol, CreateWallet.this.amount));
                        CreateWallet.this.iconView.setImageResource(DataHelper.getWalletIcons().get(CreateWallet.this.icon).intValue());
                        if (CreateWallet.this.currencyAdapter != null) {
                            CreateWallet.this.currencySpinner.setAdapter((SpinnerAdapter) CreateWallet.this.currencyAdapter);
                            CreateWallet.this.currencySpinner.setSelection(CreateWallet.this.currencyAdapter.getPosition(CreateWallet.this.currencyCode));
                        }
                        CreateWallet.this.checkIsComplete();
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

    private void createWallet() {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateWallet.5
            @Override // java.lang.Runnable
            public void run() {
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(CreateWallet.this);
                int accountId = SharePreferenceHelper.getAccountId(CreateWallet.this);
                String trim = CreateWallet.this.nameEditText.getText().toString().trim();
                String str = (String) CreateWallet.this.colorList.get(CreateWallet.this.colorSpinner.getSelectedItemPosition());
                int walletLastOrdering = appDatabaseObject.walletDaoObject().getWalletLastOrdering(accountId);
                int selectedItemPosition = CreateWallet.this.typeSpinner.getSelectedItemPosition();
                int i = (selectedItemPosition != 0 || CreateWallet.this.switchView.isChecked()) ? 1 : 0;
                int selectedItemPosition2 = CreateWallet.this.dueDateSpinner.getSelectedItemPosition() + 1;
                int selectedItemPosition3 = CreateWallet.this.statementDateSpinner.getSelectedItemPosition() + 1;
                long j = selectedItemPosition == 0 ? CreateWallet.this.amount : 0L;
                appDatabaseObject.walletDaoObject().insertWallet(new WalletEntity(trim, CreateWallet.this.icon, str, j, j, 0, accountId, walletLastOrdering, i, CreateWallet.this.currencyCode, selectedItemPosition, selectedItemPosition2, selectedItemPosition3, selectedItemPosition == 1 ? CreateWallet.this.amount : 0L));
                if (i == 0) {
                    AccountEntity entityById = appDatabaseObject.accountDaoObject().getEntityById(accountId);
                    entityById.setBalance(entityById.getBalance() + j);
                    appDatabaseObject.accountDaoObject().updateAccount(entityById);
                }
            }
        });
    }

    private void updateWallet() {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateWallet.6
            @Override // java.lang.Runnable
            public void run() {
                int accountId = SharePreferenceHelper.getAccountId(CreateWallet.this);
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(CreateWallet.this);
                int i = 0;
                WalletEntity walletById = appDatabaseObject.walletDaoObject().getWalletById(CreateWallet.this.getIntent().getIntExtra("walletId", 0));
                i = (walletById.getType() != 0 || CreateWallet.this.switchView.isChecked()) ? 1 : 1;
                AccountEntity entityById = appDatabaseObject.accountDaoObject().getEntityById(accountId);
                if (walletById.getExclude() == 0 && i == 1) {
                    entityById.setBalance(entityById.getBalance() - walletById.getAmount());
                    appDatabaseObject.accountDaoObject().updateAccount(entityById);
                } else if (walletById.getExclude() == 1 && i == 0) {
                    entityById.setBalance(((entityById.getBalance() + walletById.getAmount()) - CreateWallet.this.initialAmount) + CreateWallet.this.amount);
                    appDatabaseObject.accountDaoObject().updateAccount(entityById);
                } else if (walletById.getExclude() == 0 && i == 0) {
                    entityById.setBalance((entityById.getBalance() - CreateWallet.this.initialAmount) + CreateWallet.this.amount);
                    appDatabaseObject.accountDaoObject().updateAccount(entityById);
                }
                String trim = CreateWallet.this.nameEditText.getText().toString().trim();
                String str = (String) CreateWallet.this.colorList.get(CreateWallet.this.colorSpinner.getSelectedItemPosition());
                long j = walletById.getType() == 0 ? 0L : CreateWallet.this.amount;
                long j2 = walletById.getType() == 0 ? CreateWallet.this.amount : 0L;
                walletById.setCurrency(CreateWallet.this.currencyCode);
                walletById.setName(trim);
                walletById.setColor(str);
                walletById.setInitialAmount(j2);
                walletById.setExclude(i);
                walletById.setIcon(CreateWallet.this.icon);
                walletById.setAmount((walletById.getAmount() - CreateWallet.this.initialAmount) + j2);
                walletById.setCreditLimit(j);
                walletById.setDueDate(CreateWallet.this.dueDateSpinner.getSelectedItemPosition() + 1);
                walletById.setStatementDate(CreateWallet.this.statementDateSpinner.getSelectedItemPosition() + 1);
                appDatabaseObject.walletDaoObject().updateWallet(walletById);
            }
        });
    }

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i == this.currenciesList.size()) {
            startActivityForResult(new Intent(this, CreateCurrency.class), 7);
            overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
        } else {
            this.currencyCode = this.currencyList.get(i);
        }
        this.currencySpinner.setSelection(this.currencyAdapter.getPosition(this.currencyCode));
        this.symbol = DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(this.currencyCode));
        if (this.currencyCode.equals(this.mainCurrency)) {
            this.rateLabel.setVisibility(8);
        } else {
            double rate = this.currenciesList.get(this.currencyList.indexOf(this.currencyCode)).getRate();
            DecimalFormat decimalFormat = new DecimalFormat();
            decimalFormat.toLocalizedPattern();
            decimalFormat.setMaximumFractionDigits(8);
            decimalFormat.setMinimumFractionDigits(2);
            decimalFormat.setGroupingUsed(false);
            TextView textView = this.rateLabel;
            textView.setText("1.00 " + this.currencyCode + " = " + decimalFormat.format(rate) + StringUtils.SPACE + this.mainCurrency);
            this.rateLabel.setVisibility(0);
        }
        this.amountEditText.setText(Helper.getBeautifyAmount(this.symbol, this.amount));
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
                this.amountEditText.setText(Helper.getBeautifyAmount(this.symbol, longExtra));
                checkIsComplete();
            } else if (requestCode == 7) {
                this.currencyCode = data.getStringExtra("code");
            } else if (requestCode == 8) {
                this.icon = data.getIntExtra("icon", 0);
                this.iconView.setImageResource(DataHelper.getWalletIcons().get(this.icon).intValue());
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
        finish();
        overridePendingTransition(R.anim.scale_in, R.anim.bottom_to_top);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.amountEditText) {
            Intent intent = new Intent(getApplicationContext(), Calculator.class);
            intent.putExtra("amount", this.amount);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        } else if (view.getId() == R.id.iconView) {
            Intent intent2 = new Intent(getApplicationContext(), WalletIconPicker.class);
            intent2.putExtra(TypedValues.Custom.S_COLOR, this.colorList.get(this.colorSpinner.getSelectedItemPosition()));
            intent2.putExtra("icon", this.icon);
            startActivityForResult(intent2, 8);
            overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
        } else if (view.getId() == R.id.repeatWrapper) {
            this.switchView.toggle();
        } else if (view.getId() == R.id.saveLabel) {
            if (this.isComplete) {
                int i = this.type;
                if (i == -1) {
                    updateWallet();
                } else if (i == 1) {
                    createWallet();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CreateWallet.this.finish();
                        CreateWallet.this.overridePendingTransition(R.anim.scale_in, R.anim.bottom_to_top);
                    }
                });
            }
        }
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
        checkIsComplete();
    }
}
