package com.expance.merapaisa;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.Entity.AccountEntity;
import com.expance.merapaisa.Database.Entity.BudgetEntity;
import com.expance.merapaisa.Database.Entity.MediaEntity;
import com.expance.merapaisa.Database.Entity.RecurringEntity;
import com.expance.merapaisa.Database.Entity.TransEntity;
import com.expance.merapaisa.Database.Entity.WalletEntity;
import com.expance.merapaisa.Model.Recurring;
import com.expance.merapaisa.Model.Wallets;
import com.expance.merapaisa.Utility.AdsHelper;
import com.expance.merapaisa.Utility.BillingHelper;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Utility.DateHelper;
import com.expance.merapaisa.Utility.FileHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.RateInputFilter;
import com.expance.merapaisa.Utility.RecurringHelper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import com.expance.merapaisa.Widget.BottomRecurringTransactionDialog;

import org.apache.http.cookie.ClientCookie;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.FocusShape;
import me.toptas.fancyshowcase.listener.OnViewInflateListener;
import vocsy.ads.CustomAdsListener;
import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class CreateTransaction extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, BottomRecurringTransactionDialog.OnItemClickListener, BillingHelper.BillingListener, CompoundButton.OnCheckedChangeListener {
    FrameLayout adView;
    long amount;
    EditText amountEditText;
    AdView bannerView;
    BillingHelper billingHelper;
    ArrayList<Bitmap> bitmaps;
    ArrayList<String> bitmapsPath;
    String cameraPath;
    ImageView cancelImage1;
    ImageView cancelImage2;
    ImageView cancelImage3;
    EditText categoryEditText;
    TextView categoryLabel;
    ConstraintLayout constrantLayout;
    Date date;
    TextView dayLabel;
    EditText descriptionEditText;
    AlertDialog dialog;
    EditText dialogMainEditText;
    TextView dialogMainLabel;
    EditText dialogRateEditText;
    TextView dialogRateLabel;
    EditText dialogSubEditText;
    TextView dialogSubLabel;
    private String expenseCategory;
    int expenseCategoryId;
    TextView expenseLabel;
    int expenseSubcategoryId;
    ConstraintLayout expenseWrapper;
    long feeAmount;
    TextView feeEditText;
    ImageView feeImageView;
    ConstraintLayout feeWrapper;
    EditText fromWalletEditText;
    int fromWalletId;
    TextView fromWalletLabel;
    TextView hourLabel;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    CardView imageWrapper1;
    CardView imageWrapper2;
    CardView imageWrapper3;
    private String incomeCategory;
    int incomeCategoryId;
    TextView incomeLabel;
    int incomeSubcategoryId;
    ConstraintLayout incomeWrapper;
    boolean isAdsInit;
    private boolean isComplete;
    boolean isFeeAmount;
    boolean isRecurring;
    boolean isShowCase;
    LinearLayout linearLayout;
    EditText noteEditText;
    ImageView noteImage;
    ImageView rateImageView;
    TextView rateLabel;
    CustomTextWatcher rateWatcher;
    ImageView recurringImage;
    int recurringIncrement;
    TextView recurringLabel;
    String recurringRepeatDate;
    int recurringRepeatTime;
    int recurringRepeatType;
    int recurringType;
    Date recurringUntilDate;
    int recurringUntilType;
    ConstraintLayout recurringWrapper;
    TextView saveLabel;
    ScrollView scrollView;
    FancyShowCaseView showCaseView;
    CustomTextWatcher subWatcher;
    Switch switchView;
    long transAmount;
    TextView transferLabel;
    ConstraintLayout transferWrapper;
    int type = 1;
    EditText walletEditText;
    int walletId;
    TextView walletLabel;
    private List<Wallets> walletList;

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onSkuReady() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isFeeAmount", this.isFeeAmount);
        outState.putString("expenseCategory", this.expenseCategory);
        outState.putString("incomeCategory", this.incomeCategory);
        outState.putInt(JamXmlElements.TYPE, this.type);
        outState.putInt("expenseCategoryId", this.expenseCategoryId);
        outState.putInt("incomeCategoryId", this.incomeCategoryId);
        outState.putInt("expenseSubcategoryId", this.expenseSubcategoryId);
        outState.putInt("incomeSubcategoryId", this.incomeSubcategoryId);
        outState.putInt("fromWalletId", this.fromWalletId);
        outState.putInt("walletId", this.walletId);
        outState.putStringArrayList("bitmapsPath", this.bitmapsPath);
        outState.putLong("amount", this.amount);
        outState.putLong("feeAmount", this.feeAmount);
        outState.putLong("transAmount", this.transAmount);
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
        setContentView(R.layout.activity_create_transaction);
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
        this.expenseCategory = "";
        this.incomeCategory = "";
        this.isComplete = false;
        this.isShowCase = false;
        this.expenseCategoryId = 0;
        this.incomeCategoryId = 0;
        this.expenseSubcategoryId = 0;
        this.incomeSubcategoryId = 0;
        this.fromWalletId = -1;
        this.walletId = -1;
        this.walletList = new ArrayList();
        this.bitmaps = new ArrayList<>();
        this.bitmapsPath = new ArrayList<>();
        this.amount = 0L;
        this.transAmount = 0L;
        this.feeAmount = 0L;
        this.date = DateHelper.getCurrentDateTime();
        if (getIntent().hasExtra("dateTime")) {
            long longExtra = getIntent().getLongExtra("dateTime", this.date.getTime());
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTimeInMillis(longExtra);
            this.date = calendar.getTime();
        }
        if (getIntent().hasExtra("amount")) {
            this.amount = getIntent().getLongExtra("amount", 0L);
        }
        setUpLayout();
        populateData(savedInstanceState != null);
        BillingHelper billingHelper = new BillingHelper(this);
        this.billingHelper = billingHelper;
        billingHelper.setListener(this);
        this.billingHelper.setUpBillingClient();
        if (getIntent().hasExtra("widget")) {
            int i = getIntent().getStringExtra("widget").equals("income") ? 1 : 0;
            this.type = i;
            switchTransMode(i);
        }
        if (getIntent().hasExtra("isPayment")) {
            this.type = 2;
            switchTransMode(2);
            this.noteEditText.setText(R.string.payment);
        }
        if (savedInstanceState != null) {
            this.isFeeAmount = savedInstanceState.getBoolean("isFeeAmount");
            this.expenseCategory = savedInstanceState.getString("expenseCategory");
            this.incomeCategory = savedInstanceState.getString("incomeCategory");
            this.type = savedInstanceState.getInt(JamXmlElements.TYPE);
            this.expenseCategoryId = savedInstanceState.getInt("expenseCategoryId");
            this.incomeCategoryId = savedInstanceState.getInt("incomeCategoryId");
            this.expenseSubcategoryId = savedInstanceState.getInt("expenseSubcategoryId");
            this.incomeSubcategoryId = savedInstanceState.getInt("incomeSubcategoryId");
            this.fromWalletId = savedInstanceState.getInt("fromWalletId");
            this.walletId = savedInstanceState.getInt("walletId");
            this.amount = savedInstanceState.getLong("amount");
            this.feeAmount = savedInstanceState.getLong("feeAmount");
            this.transAmount = savedInstanceState.getLong("transAmount");
            this.recurringRepeatDate = savedInstanceState.getString("recurringRepeatDate");
            this.recurringType = savedInstanceState.getInt("recurringType");
            this.recurringRepeatType = savedInstanceState.getInt("recurringRepeatType");
            this.recurringUntilType = savedInstanceState.getInt("recurringUntilType");
            this.recurringIncrement = savedInstanceState.getInt("recurringIncrement");
            this.recurringRepeatTime = savedInstanceState.getInt("recurringRepeatTime");
            java.util.Calendar calendar2 = java.util.Calendar.getInstance();
            calendar2.setTimeInMillis(savedInstanceState.getLong("date"));
            this.date = calendar2.getTime();
            calendar2.setTimeInMillis(savedInstanceState.getLong("recurringUntilDate"));
            this.recurringUntilDate = calendar2.getTime();
            this.bitmapsPath = savedInstanceState.getStringArrayList("bitmapsPath");
            Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateTransaction.1
                @Override // java.lang.Runnable
                public void run() {
                    Iterator<String> it = CreateTransaction.this.bitmapsPath.iterator();
                    while (it.hasNext()) {
                        CreateTransaction.this.bitmaps.add(BitmapFactory.decodeFile(it.next()));
                    }
                    CreateTransaction.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateTransaction.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            CreateTransaction.this.displayImage();
                        }
                    });
                }
            });
            String rateLayout = setRateLayout();
            switchTransMode(this.type);
            this.amountEditText.setText(Helper.getBeautifyAmount(rateLayout, this.amount));
            this.feeEditText.setText(Helper.getBeautifyAmount(rateLayout, this.feeAmount));
            this.categoryEditText.setText(this.type == 1 ? this.expenseCategory : this.incomeCategory);
            this.hourLabel.setText(DateHelper.getFormattedTime(this.date, getApplicationContext()));
            this.dayLabel.setText(DateHelper.getFormattedDate(getApplicationContext(), this.date));
            setWallet(this.fromWalletId, 11);
            setWallet(this.walletId, -11);
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

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        this.billingHelper.registerBroadCast(this);
        this.billingHelper.queryPurchases();
        if (SharePreferenceHelper.checkTransactionShowCaseKey(this)) {
            new Handler().postDelayed(new AnonymousClass2(), 100L);
        }
    }

    /* renamed from: com.ktwapps.walletmanager.CreateTransaction$2  reason: invalid class name */
    /* loaded from: classes3.dex */
    class AnonymousClass2 implements Runnable {
        AnonymousClass2() {
        }

        @Override // java.lang.Runnable
        public void run() {
            CreateTransaction.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateTransaction.2.1
                @Override // java.lang.Runnable
                public void run() {
                    CreateTransaction.this.isShowCase = true;
                    SharePreferenceHelper.setTransactionShowCaseKey(CreateTransaction.this.getApplicationContext(), 1);
                    Animation loadAnimation = AnimationUtils.loadAnimation(CreateTransaction.this, R.anim.alpha_in);
                    Animation loadAnimation2 = AnimationUtils.loadAnimation(CreateTransaction.this, R.anim.alpha_out);
                    FancyShowCaseView.Builder builder = new FancyShowCaseView.Builder(CreateTransaction.this);
                    builder.focusOn(CreateTransaction.this.constrantLayout);
                    builder.focusShape(FocusShape.ROUNDED_RECTANGLE);
                    builder.disableFocusAnimation();
                    builder.enterAnimation(loadAnimation);
                    builder.exitAnimation(loadAnimation2);
                    builder.customView(R.layout.show_case_transaction, new OnViewInflateListener() { // from class: com.ktwapps.walletmanager.CreateTransaction.2.1.1
                        @Override // me.toptas.fancyshowcase.listener.OnViewInflateListener
                        public void onViewInflated(View view) {
                        }
                    });
                    CreateTransaction.this.showCaseView = builder.build();
                    CreateTransaction.this.showCaseView.show();
                    loadAnimation2.setAnimationListener(new Animation.AnimationListener() { // from class: com.ktwapps.walletmanager.CreateTransaction.2.1.2
                        @Override // android.view.animation.Animation.AnimationListener
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override // android.view.animation.Animation.AnimationListener
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override // android.view.animation.Animation.AnimationListener
                        public void onAnimationEnd(Animation animation) {
                            CreateTransaction.this.showCaseView.removeView();
                        }
                    });
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkIsComplete() {
        int i;
        int i2;
        int i3 = this.type;
        boolean z = false;
        if (i3 == 0) {
            if (this.incomeCategoryId != 0 && this.amount != 0 && this.walletId != -1) {
                z = true;
            }
            this.isComplete = z;
        } else if (i3 == 1) {
            if (this.expenseCategoryId != 0 && this.amount != 0 && this.walletId != -1) {
                z = true;
            }
            this.isComplete = z;
        } else if (i3 == 2) {
            if (this.amount != 0 && (i = this.walletId) != -1 && (i2 = this.fromWalletId) != -1 && i2 != i) {
                z = true;
            }
            this.isComplete = z;
        }
        if (this.isComplete) {
            this.saveLabel.setAlpha(1.0f);
        } else {
            this.saveLabel.setAlpha(0.35f);
        }
    }

    private void setUpLayout() {
        setSupportActionBar(findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.expense));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.adView = findViewById(R.id.adView);
        this.saveLabel = findViewById(R.id.saveLabel);
        this.rateLabel = findViewById(R.id.rateLabel);
        this.rateImageView = findViewById(R.id.rateImageView);
        this.constrantLayout = findViewById(R.id.constraintLayout);
        this.scrollView = findViewById(R.id.scrollView);
        this.amountEditText = findViewById(R.id.amountEditText);
        this.categoryEditText = findViewById(R.id.categoryEditText);
        this.noteEditText = findViewById(R.id.noteEditText);
        this.noteImage = findViewById(R.id.noteImageView);
        this.imageView1 = findViewById(R.id.imageView1);
        this.imageView2 = findViewById(R.id.imageView2);
        this.imageView3 = findViewById(R.id.imageView3);
        this.cancelImage1 = findViewById(R.id.cancelImage1);
        this.cancelImage2 = findViewById(R.id.cancelImage2);
        this.cancelImage3 = findViewById(R.id.cancelImage3);
        this.imageWrapper1 = findViewById(R.id.imageWrapper1);
        this.imageWrapper2 = findViewById(R.id.imageWrapper2);
        this.imageWrapper3 = findViewById(R.id.imageWrapper3);
        this.incomeWrapper = findViewById(R.id.incomeWrapper);
        this.transferWrapper = findViewById(R.id.transferWrapper);
        this.expenseWrapper = findViewById(R.id.expenseWrapper);
        this.incomeLabel = findViewById(R.id.incomeLabel);
        this.transferLabel = findViewById(R.id.transferLabel);
        this.expenseLabel = findViewById(R.id.expenseLabel);
        this.dayLabel = findViewById(R.id.dayLabel);
        this.hourLabel = findViewById(R.id.hourLabel);
        this.categoryLabel = findViewById(R.id.categoryLabel);
        this.walletLabel = findViewById(R.id.walletLabel);
        this.fromWalletLabel = findViewById(R.id.fromWalletLabel);
        this.walletEditText = findViewById(R.id.walletEditText);
        this.fromWalletEditText = findViewById(R.id.fromWalletEditText);
        this.recurringImage = findViewById(R.id.recurringImage);
        this.recurringLabel = findViewById(R.id.recurringLabel);
        this.recurringWrapper = findViewById(R.id.recurringWrapper);
        this.linearLayout = findViewById(R.id.linearLayout);
        this.descriptionEditText = findViewById(R.id.descriptionEditText);
        this.feeWrapper = findViewById(R.id.feeWrapper);
        this.feeEditText = findViewById(R.id.feeEditText);
        this.switchView = findViewById(R.id.switchView);
        this.feeImageView = findViewById(R.id.feeImageView);
        this.expenseWrapper.setBackgroundColor(getResources().getColor(R.color.expense));
        this.expenseLabel.setTextColor(getResources().getColor(17170443));
        this.hourLabel.setText(DateHelper.getFormattedTime(this.date, getApplicationContext()));
        this.dayLabel.setText(DateHelper.getFormattedDate(getApplicationContext(), this.date));
        this.amountEditText.setText(Helper.getBeautifyAmount(SharePreferenceHelper.getAccountSymbol(this), this.amount));
        this.feeEditText.setText(Helper.getBeautifyAmount(SharePreferenceHelper.getAccountSymbol(this), this.feeAmount));
        findViewById(R.id.fab).setOnClickListener(this);
        this.feeEditText.setOnClickListener(this);
        this.feeImageView.setOnClickListener(this);
        this.switchView.setOnCheckedChangeListener(this);
        this.saveLabel.setOnClickListener(this);
        this.incomeWrapper.setOnClickListener(this);
        this.expenseWrapper.setOnClickListener(this);
        this.transferWrapper.setOnClickListener(this);
        this.dayLabel.setOnClickListener(this);
        this.hourLabel.setOnClickListener(this);
        this.noteImage.setOnClickListener(this);
        this.cancelImage1.setOnClickListener(this);
        this.cancelImage2.setOnClickListener(this);
        this.cancelImage3.setOnClickListener(this);
        this.imageView1.setOnClickListener(this);
        this.imageView2.setOnClickListener(this);
        this.imageView3.setOnClickListener(this);
        this.rateImageView.setOnClickListener(this);
        this.recurringWrapper.setOnClickListener(this);
        this.imageView1.setTag(0);
        this.imageView2.setTag(1);
        this.imageView3.setTag(2);
        this.walletEditText.setFocusable(false);
        this.walletEditText.setOnClickListener(this);
        this.walletEditText.setLongClickable(false);
        this.fromWalletEditText.setFocusable(false);
        this.fromWalletEditText.setOnClickListener(this);
        this.fromWalletEditText.setLongClickable(false);
        this.amountEditText.setFocusable(false);
        this.amountEditText.setOnClickListener(this);
        this.amountEditText.setLongClickable(false);
        this.categoryEditText.setFocusable(false);
        this.categoryEditText.setOnClickListener(this);
        this.categoryEditText.setLongClickable(false);
        checkIsComplete();
    }

    private void populateData(final boolean isSavedInstanceState) {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateTransaction.3
            @Override // java.lang.Runnable
            public void run() {
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(CreateTransaction.this);
                java.util.Calendar calendar = java.util.Calendar.getInstance();
                calendar.set(14, 0);
                calendar.set(13, 0);
                calendar.set(12, 0);
                calendar.set(11, 0);
                calendar.add(5, 1);
                if (!SharePreferenceHelper.isFutureBalanceOn(CreateTransaction.this.getApplicationContext())) {
                    calendar.set(1, 10000);
                }
                CreateTransaction.this.walletList = appDatabaseObject.walletDaoObject().getWallets(SharePreferenceHelper.getAccountId(CreateTransaction.this.getApplicationContext()), 0, calendar.getTimeInMillis());
                if (!isSavedInstanceState) {
                    if (!CreateTransaction.this.getIntent().hasExtra("walletId")) {
                        if (CreateTransaction.this.walletList == null || CreateTransaction.this.walletList.size() <= 0) {
                            return;
                        }
                        CreateTransaction createTransaction = CreateTransaction.this;
                        createTransaction.walletId = createTransaction.walletList.get(0).getId();
                        CreateTransaction.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateTransaction.3.2
                            @Override // java.lang.Runnable
                            public void run() {
                                CreateTransaction.this.setWallet(CreateTransaction.this.walletId, -11);
                                CreateTransaction.this.checkIsComplete();
                                CreateTransaction.this.transAmount = 0L;
                                String rateLayout = CreateTransaction.this.setRateLayout();
                                CreateTransaction.this.amountEditText.setText(Helper.getBeautifyAmount(rateLayout, CreateTransaction.this.amount));
                                CreateTransaction.this.feeEditText.setText(Helper.getBeautifyAmount(rateLayout, CreateTransaction.this.feeAmount));
                                CreateTransaction.this.checkIsComplete();
                            }
                        });
                        return;
                    }
                    CreateTransaction createTransaction2 = CreateTransaction.this;
                    createTransaction2.walletId = createTransaction2.getIntent().getIntExtra("walletId", -1);
                    CreateTransaction.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateTransaction.3.1
                        @Override // java.lang.Runnable
                        public void run() {
                            CreateTransaction.this.setWallet(CreateTransaction.this.walletId, -11);
                            CreateTransaction.this.checkIsComplete();
                            CreateTransaction.this.transAmount = 0L;
                            String rateLayout = CreateTransaction.this.setRateLayout();
                            CreateTransaction.this.amountEditText.setText(Helper.getBeautifyAmount(rateLayout, CreateTransaction.this.amount));
                            CreateTransaction.this.feeEditText.setText(Helper.getBeautifyAmount(rateLayout, CreateTransaction.this.feeAmount));
                            CreateTransaction.this.checkIsComplete();
                        }
                    });
                    return;
                }
                CreateTransaction createTransaction3 = CreateTransaction.this;
                createTransaction3.setWallet(createTransaction3.fromWalletId, 11);
                CreateTransaction createTransaction4 = CreateTransaction.this;
                createTransaction4.setWallet(createTransaction4.walletId, -11);
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
        if (this.isShowCase) {
            this.showCaseView.hide();
            this.isShowCase = false;
            return;
        }
        super.onBackPressed();
        overridePendingTransition(R.anim.scale_in, R.anim.bottom_to_top);
    }

    public void openDateDialog() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(this.date);
        new DatePickerDialog(this, this, calendar.get(1), calendar.get(2), calendar.get(5)).show();
    }

    public void openHourDialog() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(this.date);
        new TimePickerDialog(this, this, calendar.get(11), calendar.get(12), DateFormat.is24HourFormat(getApplicationContext())).show();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.amountEditText) {
            this.isFeeAmount = false;
            Intent intent = new Intent(this, Calculator.class);
            intent.putExtra("amount", this.amount);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        } else if (view.getId() == R.id.cancelImage1) {
            this.bitmaps.remove(0);
            this.bitmapsPath.remove(0);
            displayImage();
        } else if (view.getId() == R.id.cancelImage2) {
            this.bitmaps.remove(1);
            this.bitmapsPath.remove(1);
            displayImage();
        } else if (view.getId() == R.id.cancelImage3) {
            this.bitmaps.remove(2);
            this.bitmapsPath.remove(2);
            displayImage();
        } else if (view.getId() == R.id.categoryEditText) {
            Intent intent2 = new Intent(this, CategoryPicker.class);
            intent2.putExtra("id", this.type == 0 ? this.incomeCategoryId : this.expenseCategoryId);
            intent2.putExtra("subcategoryId", this.type == 0 ? this.incomeSubcategoryId : this.expenseSubcategoryId);
            intent2.putExtra(JamXmlElements.TYPE, this.type == 0 ? 1 : 2);
            startActivityForResult(intent2, 5);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        } else if (view.getId() == R.id.dayLabel) {
            openDateDialog();
        } else if (view.getId() == R.id.expenseWrapper) {
            switchTransMode(1);
        } else if (view.getId() == R.id.fab) {
            startActivityForResult(new Intent(this, TemplatePicker.class), 17);
            overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
        } else if (view.getId() == R.id.feeEditText) {
            if (this.switchView.isChecked()) {
                this.isFeeAmount = true;
                Intent intent3 = new Intent(this, Calculator.class);
                intent3.putExtra("amount", this.feeAmount);
                startActivityForResult(intent3, 1);
                overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
            }
        } else if (view.getId() == R.id.feeImageView) {
            if (this.switchView.isChecked()) {
                this.isFeeAmount = true;
                Intent intent3 = new Intent(this, Calculator.class);
                intent3.putExtra("amount", this.feeAmount);
                startActivityForResult(intent3, 1);
                overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
            }
        } else if (view.getId() == R.id.fromWalletEditText) {
            Intent intent4 = new Intent(this, WalletPicker.class);
            intent4.putExtra("id", this.fromWalletId);
            intent4.putExtra(JamXmlElements.TYPE, 11);
            startActivityForResult(intent4, 2);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        } else if (view.getId() == R.id.hourLabel) {
            openHourDialog();
        } else if (view.getId() == R.id.incomeWrapper) {
            switchTransMode(0);
        } else if (view.getId() == R.id.noteImageView) {
            checkPermission();
        } else if (view.getId() == R.id.rateImageView) {
            showRateDialog();
        } else if (view.getId() == R.id.recurringWrapper) {
            if (this.billingHelper.getBillingStatus() == 2) {
                launchRecurringDialog();
            } else {
                Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateTransaction.4
                    @Override // java.lang.Runnable
                    public void run() {
                        final List<Recurring> allRecurringListByAccountId = AppDatabaseObject.getInstance(CreateTransaction.this.getApplicationContext()).recurringDaoObject().getAllRecurringListByAccountId(SharePreferenceHelper.getAccountId(CreateTransaction.this.getApplicationContext()));
                        CreateTransaction.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateTransaction.4.1
                            @Override // java.lang.Runnable
                            public void run() {
                                if (allRecurringListByAccountId.size() == 0) {
                                    CreateTransaction.this.launchRecurringDialog();
                                    return;
                                }
                                CreateTransaction.this.startActivity(new Intent(CreateTransaction.this.getApplicationContext(), Premium.class));
                                CreateTransaction.this.overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
                            }
                        });
                    }
                });
            }
        } else if (view.getId() == R.id.saveLabel) {
            GoogleAds.getInstance().showCounterInterstitialAd(CreateTransaction.this, new CustomAdsListener() {
                @Override
                public void onFinish() {
                    createTransaction();
                }
            });

        } else if (view.getId() == R.id.transferWrapper) {
            switchTransMode(2);
        } else if (view.getId() == R.id.walletEditText) {
            Intent intent5 = new Intent(this, WalletPicker.class);
            intent5.putExtra("id", this.walletId);
            intent5.putExtra(JamXmlElements.TYPE, -11);
            startActivityForResult(intent5, 2);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        } else {
            Intent intent6 = new Intent(getApplicationContext(), PhotoViewer.class);
            intent6.putExtra(ClientCookie.PATH_ATTR, TextUtils.join(",", this.bitmapsPath));
            intent6.putExtra(FirebaseAnalytics.Param.INDEX, ((Integer) view.getTag()).intValue());
            startActivity(intent6);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void launchRecurringDialog() {
        BottomRecurringTransactionDialog bottomRecurringTransactionDialog = new BottomRecurringTransactionDialog();
        bottomRecurringTransactionDialog.setData(this.date, this.recurringUntilDate, this.recurringRepeatDate, this.recurringType, this.recurringRepeatType, this.recurringRepeatTime, this.recurringUntilType, this.recurringIncrement);
        bottomRecurringTransactionDialog.setListener(this);
        bottomRecurringTransactionDialog.show(getSupportFragmentManager(), "recurring");
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
    public Wallets getFromWallet() {
        for (Wallets wallets : this.walletList) {
            if (this.fromWalletId == wallets.getId()) {
                return wallets;
            }
        }
        return null;
    }

    private String setRateLayout() {
        Wallets var5 = this.getWallet();
        Wallets var6 = this.getFromWallet();
        float var2 = 0.0F;
        String var7 = null;
        float var1;
        String var8;
        if (var5 != null) {
            var1 = var5.getRate();
            var8 = var5.getCurrency();
        } else {
            var8 = null;
            var1 = 0.0F;
        }

        String var9;
        if (var6 != null) {
            var2 = var6.getRate();
            var9 = var6.getCurrency();
        } else {
            var9 = null;
        }

        if (this.type == 2) {
            label48: {
                if (var8 != null && var9 != null) {
                    if (var8.equals(var9)) {
                        this.rateLabel.setVisibility(8);
                        this.rateImageView.setVisibility(8);
                    } else if (this.amount > 0L) {
                        this.rateLabel.setVisibility(0);
                        this.rateImageView.setVisibility(0);
                        var1 = var2 / var1;
                        long var3 = this.transAmount;
                        if (var3 != 0L) {
                            this.rateLabel.setText(Helper.getBeautifyRate(var9, var8, this.amount, var3));
                        } else {
                            this.rateLabel.setText(Helper.getBeautifyRate(var9, var8, var1, this.amount));
                        }
                    } else {
                        this.rateLabel.setVisibility(8);
                        this.rateImageView.setVisibility(8);
                    }
                } else {
                    this.rateLabel.setVisibility(8);
                    this.rateImageView.setVisibility(8);
                    if (var9 == null) {
                        break label48;
                    }
                }

                var7 = var9;
            }
        } else {
            this.rateLabel.setVisibility(8);
            this.rateImageView.setVisibility(8);
            if (var8 != null) {
                var7 = var8;
            }
        }

        if (var7 == null) {
            var8 = SharePreferenceHelper.getAccountSymbol(this);
        } else {
            var8 = DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(var7));
        }

        return var8;
    }

    @Override
    // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            if (requestCode == 3) {
                if (this.cameraPath != null) {
                    final File file = new File(this.cameraPath);
                    final Bitmap decodeFile = BitmapFactory.decodeFile(file.getAbsolutePath());
                    if (decodeFile != null) {
                        this.bitmapsPath.add(this.cameraPath);
                        this.bitmaps.add(decodeFile);
                        displayImage();
                        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateTransaction.5
                            @Override // java.lang.Runnable
                            public void run() {
                                try {
                                    FileHelper.saveToGallery(CreateTransaction.this.getApplicationContext(), decodeFile, file.getName());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            } else if (requestCode == 4) {
                final Bitmap bitmapFromUri = FileHelper.getBitmapFromUri(getApplicationContext(), data.getData());
                if (bitmapFromUri != null) {
                    this.bitmaps.add(bitmapFromUri);
                    displayImage();
                    Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateTransaction.6
                        @Override // java.lang.Runnable
                        public void run() {
                            try {
                                CreateTransaction.this.bitmapsPath.add(FileHelper.convertBitmaptoFile(FileHelper.createImageFile(CreateTransaction.this.getApplicationContext()), bitmapFromUri));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            } else if (requestCode == 1) {
                long longExtra = data.getLongExtra("amount", 0L);
                if (longExtra < 0) {
                    longExtra = 0;
                }
                if (this.isFeeAmount) {
                    this.feeAmount = longExtra;
                    this.feeEditText.setText(Helper.getBeautifyAmount(setRateLayout(), longExtra));
                    checkIsComplete();
                    return;
                }
                this.amount = longExtra;
                this.transAmount = 0L;
                this.amountEditText.setText(Helper.getBeautifyAmount(setRateLayout(), longExtra));
                checkIsComplete();
            } else if (requestCode == 5) {
                String stringExtra = data.getStringExtra("name");
                int intExtra = data.getIntExtra("subcategoryId", 0);
                int intExtra2 = data.getIntExtra("id", 0);
                int intExtra3 = data.getIntExtra(JamXmlElements.TYPE, 1);
                if (intExtra3 == 1) {
                    this.expenseSubcategoryId = intExtra;
                    this.expenseCategory = stringExtra;
                    this.expenseCategoryId = intExtra2;
                } else {
                    this.incomeSubcategoryId = intExtra;
                    this.incomeCategory = stringExtra;
                    this.incomeCategoryId = intExtra2;
                }
                this.categoryEditText.setText(stringExtra);
                switchTransMode(intExtra3);
                checkIsComplete();
            } else if (requestCode == 2) {
                int intExtra4 = data.getIntExtra("id", -1);
                int intExtra5 = data.getIntExtra(JamXmlElements.TYPE, 11);
                setWallet(intExtra4, intExtra5);
                if (intExtra5 == 11) {
                    this.fromWalletId = intExtra4;
                } else {
                    this.walletId = intExtra4;
                }
                this.transAmount = 0L;
                String rateLayout = setRateLayout();
                this.amountEditText.setText(Helper.getBeautifyAmount(rateLayout, this.amount));
                this.feeEditText.setText(Helper.getBeautifyAmount(rateLayout, this.feeAmount));
                checkIsComplete();
            } else if (requestCode == 17) {
                String stringExtra2 = data.getStringExtra("note");
                String stringExtra3 = data.getStringExtra("memo");
                String stringExtra4 = data.getStringExtra("category");
                int intExtra6 = data.getIntExtra("categoryId", 0);
                int intExtra7 = data.getIntExtra("subcategoryId", 0);
                long longExtra2 = data.getLongExtra("amount", 0L);
                int intExtra8 = data.getIntExtra(JamXmlElements.TYPE, 0);
                int intExtra9 = data.getIntExtra("walletId", -1);
                if (stringExtra2 != null && stringExtra2.length() > 0) {
                    this.descriptionEditText.setText(stringExtra2);
                }
                if (stringExtra3 != null && stringExtra3.length() > 0) {
                    this.noteEditText.setText(stringExtra3);
                }
                if (intExtra6 != 0) {
                    if (intExtra8 == 0) {
                        this.incomeCategory = stringExtra4;
                        this.incomeCategoryId = intExtra6;
                        this.incomeSubcategoryId = intExtra7;
                    } else {
                        this.expenseCategory = stringExtra4;
                        this.expenseCategoryId = intExtra6;
                        this.expenseSubcategoryId = intExtra7;
                    }
                    this.categoryEditText.setText(stringExtra4);
                    switchTransMode(intExtra8);
                }
                if (intExtra9 != -1) {
                    this.walletId = intExtra9;
                    setWallet(intExtra9, -11);
                }
                if (longExtra2 > 0) {
                    this.amount = longExtra2;
                }
                this.amountEditText.setText(Helper.getBeautifyAmount(setRateLayout(), this.amount));
                checkIsComplete();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setWallet(int id, int type) {
        String str = "";
        for (Wallets wallets : this.walletList) {
            if (wallets.getId() == id) {
                str = wallets.getName() + " • " + Helper.getBeautifyAmount(DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(wallets.getCurrency())), wallets.getAmount());
            }
        }
        if (type == 11) {
            this.fromWalletEditText.setText(str);
        } else {
            this.walletEditText.setText(str);
        }
    }

    private void switchTransMode(int i) {
        int i2;
        String str;
        String str2;
//        this.incomeWrapper.setBackgroundColor(Helper.getAttributeColor(this, R.attr.secondaryBackground));
        this.incomeLabel.setTextColor(Helper.getAttributeColor(this, R.attr.primaryTextColor));
//        this.transferWrapper.setBackgroundColor(Helper.getAttributeColor(this, R.attr.secondaryBackground));
        this.transferLabel.setTextColor(Helper.getAttributeColor(this, R.attr.primaryTextColor));
//        this.expenseWrapper.setBackgroundColor(Helper.getAttributeColor(this, R.attr.secondaryBackground));
        this.expenseLabel.setTextColor(Helper.getAttributeColor(this, R.attr.primaryTextColor));
        this.rateLabel.setVisibility(8);
        this.rateImageView.setVisibility(8);
        this.recurringWrapper.setVisibility(8);
        this.feeWrapper.setVisibility(8);
        String str3 = null;
        if (i == 0) {
            this.incomeWrapper.setBackgroundColor(getResources().getColor(R.color.income));
            this.transferWrapper.setBackgroundColor(getResources().getColor(R.color.theam));
            this.expenseWrapper.setBackgroundColor(getResources().getColor(R.color.theam));
            this.incomeLabel.setTextColor(getResources().getColor(17170443));
            this.walletLabel.setText(R.string.wallet);
            this.fromWalletLabel.setVisibility(4);
            this.fromWalletEditText.setVisibility(4);
            this.categoryLabel.setVisibility(0);
            this.categoryEditText.setVisibility(0);
            this.type = 0;
            this.categoryEditText.setText(this.incomeCategory);
            Iterator<Wallets> it = this.walletList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Wallets next = it.next();
                if (this.walletId == next.getId()) {
                    str3 = next.getCurrency();
                    break;
                }
            }
            if (str3 == null) {
                str2 = SharePreferenceHelper.getAccountSymbol(this);
            } else {
                str2 = DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(str3));
            }
            this.amountEditText.setText(Helper.getBeautifyAmount(str2, this.amount));
            this.rateLabel.setVisibility(8);
            this.rateImageView.setVisibility(8);
            this.recurringWrapper.setVisibility(0);
            i2 = R.string.income;
        } else if (i == 1) {
            this.expenseWrapper.setBackgroundColor(getResources().getColor(R.color.expense));
            this.transferWrapper.setBackgroundColor(getResources().getColor(R.color.theam));
            this.incomeWrapper.setBackgroundColor(getResources().getColor(R.color.theam));
            this.expenseLabel.setTextColor(getResources().getColor(17170443));
            this.walletLabel.setText(R.string.wallet);
            this.fromWalletLabel.setVisibility(4);
            this.fromWalletEditText.setVisibility(4);
            this.categoryLabel.setVisibility(0);
            this.categoryEditText.setVisibility(0);
            this.type = 1;
            this.categoryEditText.setText(this.expenseCategory);
            Iterator<Wallets> it2 = this.walletList.iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                Wallets next2 = it2.next();
                if (this.walletId == next2.getId()) {
                    str3 = next2.getCurrency();
                    break;
                }
            }
            if (str3 == null) {
                str = SharePreferenceHelper.getAccountSymbol(this);
            } else {
                str = DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(str3));
            }
            this.amountEditText.setText(Helper.getBeautifyAmount(str, this.amount));
            this.rateLabel.setVisibility(8);
            this.rateImageView.setVisibility(8);
            this.recurringWrapper.setVisibility(0);
            i2 = R.string.expense;
        } else {
            this.transferWrapper.setBackgroundColor(getResources().getColor(R.color.transfer));

            this.expenseWrapper.setBackgroundColor(getResources().getColor(R.color.theam));
            
            this.incomeWrapper.setBackgroundColor(getResources().getColor(R.color.theam));
            this.transferLabel.setTextColor(getResources().getColor(17170443));
            this.walletLabel.setText(R.string.to_wallet);
            this.fromWalletLabel.setVisibility(0);
            this.fromWalletEditText.setVisibility(0);
            this.categoryLabel.setVisibility(4);
            this.categoryEditText.setVisibility(4);
            this.type = 2;
            String rateLayout = setRateLayout();
            this.amountEditText.setText(Helper.getBeautifyAmount(rateLayout, this.amount));
            this.feeEditText.setText(Helper.getBeautifyAmount(rateLayout, this.feeAmount));
            this.feeWrapper.setVisibility(0);
            i2 = R.string.transfer;
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(i2);
        }
        checkIsComplete();
    }

    private void showMediaPickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(new String[]{getResources().getString(R.string.camera), getResources().getString(R.string.gallery)}, new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.CreateTransaction.7
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (CreateTransaction.this.billingHelper.getBillingStatus() == 2 || CreateTransaction.this.bitmapsPath.size() <= 0) {
                    if (i == 0) {
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        File createImageFile = FileHelper.createImageFile(CreateTransaction.this.getApplicationContext());
                        CreateTransaction.this.cameraPath = createImageFile.getAbsolutePath();
                        Context applicationContext = CreateTransaction.this.getApplicationContext();
                        intent.putExtra("output", FileProvider.getUriForFile(applicationContext, CreateTransaction.this.getApplicationContext().getPackageName() + ".fileProvider", createImageFile));
                        intent.putExtra("return-data", true);
                        intent.addFlags(1);
                        CreateTransaction.this.startActivityForResult(intent, 3);
                    } else if (i == 1) {
                        Intent intent2 = new Intent();
                        intent2.setType("image/*");
                        intent2.setAction("android.intent.action.GET_CONTENT");
                        CreateTransaction.this.startActivityForResult(Intent.createChooser(intent2, ""), 4);
                    }
                    CreateTransaction.this.scrollView.post(new Runnable() { // from class: com.ktwapps.walletmanager.CreateTransaction.7.1
                        @Override // java.lang.Runnable
                        public void run() {
                            CreateTransaction.this.scrollView.fullScroll(130);
                        }
                    });
                } else {
                    CreateTransaction.this.startActivity(new Intent(CreateTransaction.this.getApplicationContext(), Premium.class));
                    CreateTransaction.this.overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
                }
                CreateTransaction.this.scrollView.post(new Runnable() { // from class: com.ktwapps.walletmanager.CreateTransaction.7.2
                    @Override // java.lang.Runnable
                    public void run() {
                        CreateTransaction.this.scrollView.fullScroll(130);
                    }
                });
            }
        });
        builder.show();
    }

    @Override // android.app.DatePickerDialog.OnDateSetListener
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        this.dayLabel.setText(DateHelper.getDateFromPicker(getApplicationContext(), i, i1, i2));
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(this.date);
        calendar.set(1, i);
        calendar.set(2, i1);
        calendar.set(5, i2);
        this.date = calendar.getTime();
        validateRecurring();
    }

    @Override // android.app.TimePickerDialog.OnTimeSetListener
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        this.hourLabel.setText(DateHelper.getTimeFromPicker(this, i, i1));
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(this.date);
        calendar.set(11, i);
        calendar.set(12, i1);
        this.date = calendar.getTime();
    }

    public void displayImage() {
        this.linearLayout.setVisibility(8);
        this.imageWrapper1.setVisibility(8);
        this.imageWrapper2.setVisibility(8);
        this.imageWrapper3.setVisibility(8);
        this.noteImage.setVisibility(0);
        this.imageView1.setImageBitmap(null);
        this.imageView2.setImageBitmap(null);
        this.imageView3.setImageBitmap(null);
        if (this.bitmaps.size() > 0) {
            this.linearLayout.setVisibility(0);
            if (this.bitmaps.size() == 1) {
                this.imageWrapper1.setVisibility(0);
                this.imageView1.setImageBitmap(this.bitmaps.get(0));
            } else if (this.bitmaps.size() == 2) {
                this.imageWrapper1.setVisibility(0);
                this.imageWrapper2.setVisibility(0);
                this.imageView1.setImageBitmap(this.bitmaps.get(0));
                this.imageView2.setImageBitmap(this.bitmaps.get(1));
            } else {
                this.noteImage.setVisibility(8);
                this.imageWrapper1.setVisibility(0);
                this.imageWrapper2.setVisibility(0);
                this.imageWrapper3.setVisibility(0);
                this.imageView1.setImageBitmap(this.bitmaps.get(0));
                this.imageView2.setImageBitmap(this.bitmaps.get(1));
                this.imageView3.setImageBitmap(this.bitmaps.get(2));
            }
        }
    }

    private void createTransaction() {
        if (this.isComplete) {
            Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateTransaction.8
                @Override // java.lang.Runnable
                public void run() {
                    int i;
                    int i2;
                    int i3;
                    float f;
                    float f2;
                    AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(CreateTransaction.this.getApplicationContext());
                    String trim = CreateTransaction.this.descriptionEditText.getText().toString().trim();
                    String trim2 = CreateTransaction.this.noteEditText.getText().toString().trim();
                    int accountId = SharePreferenceHelper.getAccountId(CreateTransaction.this.getApplicationContext());
                    int i4 = CreateTransaction.this.type;
                    if (i4 == 0) {
                        int i5 = CreateTransaction.this.incomeCategoryId;
                        int i6 = CreateTransaction.this.incomeSubcategoryId;
                        WalletEntity walletById = appDatabaseObject.walletDaoObject().getWalletById(CreateTransaction.this.walletId);
                        walletById.setAmount(walletById.getAmount() + CreateTransaction.this.amount);
                        appDatabaseObject.walletDaoObject().updateWallet(walletById);
                        if (walletById.getExclude() == 0) {
                            accountId = accountId;
                            AccountEntity entityById = appDatabaseObject.accountDaoObject().getEntityById(accountId);
                            entityById.setBalance(entityById.getBalance() + CreateTransaction.this.amount);
                            appDatabaseObject.accountDaoObject().updateAccount(entityById);
                        } else {
                            accountId = accountId;
                        }
                        CreateTransaction.this.fromWalletId = -1;
                        CreateTransaction.this.transAmount = 0L;
                        i = i5;
                        i2 = i6;
                    } else if (i4 != 1) {
                        if (i4 == 2) {
                            if (CreateTransaction.this.transAmount == 0) {
                                Wallets wallets = null;
                                Wallets wallets2 = null;
                                for (Wallets wallets3 : CreateTransaction.this.walletList) {
                                    if (CreateTransaction.this.walletId == wallets3.getId()) {
                                        wallets = wallets3;
                                    }
                                    if (CreateTransaction.this.fromWalletId == wallets3.getId()) {
                                        wallets2 = wallets3;
                                    }
                                }
                                if (wallets == null || wallets2 == null) {
                                    f = 0.0f;
                                    f2 = 0.0f;
                                } else {
                                    f = wallets.getRate();
                                    f2 = wallets2.getRate();
                                }
                                BigDecimal stripTrailingZeros = new BigDecimal(CreateTransaction.this.amount).multiply(new BigDecimal(f2 / f)).setScale(0, RoundingMode.DOWN).stripTrailingZeros();
                                DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ENGLISH);
                                decimalFormat.toLocalizedPattern();
                                decimalFormat.setMaximumFractionDigits(0);
                                decimalFormat.setMinimumFractionDigits(0);
                                decimalFormat.setGroupingUsed(false);
                                CreateTransaction.this.transAmount = Long.parseLong(decimalFormat.format(stripTrailingZeros));
                            }
                            WalletEntity walletById2 = appDatabaseObject.walletDaoObject().getWalletById(CreateTransaction.this.fromWalletId);
                            walletById2.setAmount(walletById2.getAmount() - CreateTransaction.this.amount);
                            appDatabaseObject.walletDaoObject().updateWallet(walletById2);
                            WalletEntity walletById3 = appDatabaseObject.walletDaoObject().getWalletById(CreateTransaction.this.walletId);
                            walletById3.setAmount(walletById3.getAmount() + CreateTransaction.this.transAmount);
                            appDatabaseObject.walletDaoObject().updateWallet(walletById3);
                            if (walletById2.getAccountId() == walletById3.getAccountId()) {
                                if (walletById2.getExclude() == 0 && walletById3.getExclude() == 1) {
                                    AccountEntity entityById2 = appDatabaseObject.accountDaoObject().getEntityById(accountId);
                                    entityById2.setBalance(entityById2.getBalance() - CreateTransaction.this.amount);
                                    appDatabaseObject.accountDaoObject().updateAccount(entityById2);
                                } else if (walletById2.getExclude() == 1 && walletById3.getExclude() == 0) {
                                    AccountEntity entityById3 = appDatabaseObject.accountDaoObject().getEntityById(accountId);
                                    entityById3.setBalance(entityById3.getBalance() + CreateTransaction.this.amount);
                                    appDatabaseObject.accountDaoObject().updateAccount(entityById3);
                                }
                            } else {
                                if (walletById2.getExclude() == 0) {
                                    AccountEntity entityById4 = appDatabaseObject.accountDaoObject().getEntityById(walletById2.getAccountId());
                                    entityById4.setBalance(entityById4.getBalance() - CreateTransaction.this.amount);
                                    appDatabaseObject.accountDaoObject().updateAccount(entityById4);
                                }
                                if (walletById3.getExclude() == 0) {
                                    AccountEntity entityById5 = appDatabaseObject.accountDaoObject().getEntityById(walletById3.getAccountId());
                                    entityById5.setBalance(entityById5.getBalance() + CreateTransaction.this.amount);
                                    appDatabaseObject.accountDaoObject().updateAccount(entityById5);
                                }
                            }
                        }
                        i = 0;
                        i2 = 0;
                    } else {
                        int i7 = CreateTransaction.this.expenseCategoryId;
                        int i8 = CreateTransaction.this.expenseSubcategoryId;
                        WalletEntity walletById4 = appDatabaseObject.walletDaoObject().getWalletById(CreateTransaction.this.walletId);
                        walletById4.setAmount(walletById4.getAmount() - CreateTransaction.this.amount);
                        appDatabaseObject.walletDaoObject().updateWallet(walletById4);
                        if (walletById4.getExclude() == 0) {
                            AccountEntity entityById6 = appDatabaseObject.accountDaoObject().getEntityById(accountId);
                            entityById6.setBalance(entityById6.getBalance() - CreateTransaction.this.amount);
                            appDatabaseObject.accountDaoObject().updateAccount(entityById6);
                        }
                        for (BudgetEntity budgetEntity : appDatabaseObject.budgetDaoObject().getBudgetEntityByCategory(appDatabaseObject.budgetDaoObject().getBudgetIds(i7), accountId, 0)) {
                            budgetEntity.setSpent(budgetEntity.getSpent() + CreateTransaction.this.amount);
                            appDatabaseObject.budgetDaoObject().updateBudget(budgetEntity);
                            accountId = accountId;
                        }
                        CreateTransaction.this.fromWalletId = -1;
                        CreateTransaction.this.transAmount = 0L;
                        CreateTransaction createTransaction = CreateTransaction.this;
                        createTransaction.amount = -createTransaction.amount;
                        i = i7;
                        i2 = i8;
                    }
                    int i9 = accountId;
                    int insertTrans = (int) appDatabaseObject.transDaoObject().insertTrans(new TransEntity(trim, trim2, CreateTransaction.this.amount, CreateTransaction.this.date, CreateTransaction.this.type, accountId, i, 0, CreateTransaction.this.type == 2 ? CreateTransaction.this.fromWalletId : CreateTransaction.this.walletId, CreateTransaction.this.type == 2 ? CreateTransaction.this.walletId : CreateTransaction.this.fromWalletId, CreateTransaction.this.transAmount, 0, 0, i2));
                    if (CreateTransaction.this.type == 2 && CreateTransaction.this.switchView.isChecked() && CreateTransaction.this.feeAmount > 0) {
                        i3 = i9;
                        int feeCategoryId = appDatabaseObject.categoryDaoObject().getFeeCategoryId(i3);
                        if (feeCategoryId != 0) {
                            java.util.Calendar calendar = java.util.Calendar.getInstance();
                            calendar.setTime(CreateTransaction.this.date);
                            calendar.add(13, 1);
                            int insertTrans2 = (int) appDatabaseObject.transDaoObject().insertTrans(new TransEntity(CreateTransaction.this.getResources().getString(R.string.transfer_fee), "", -CreateTransaction.this.feeAmount, calendar.getTime(), 1, i3, feeCategoryId, 0, CreateTransaction.this.fromWalletId, 0, 0L, 0, 0, 0));
                            TransEntity transEntityById = appDatabaseObject.transDaoObject().getTransEntityById(insertTrans);
                            transEntityById.setFeeId(insertTrans2);
                            appDatabaseObject.transDaoObject().updateTrans(transEntityById);
                        }
                    } else {
                        i3 = i9;
                    }
                    for (int i10 = 0; i10 < CreateTransaction.this.bitmapsPath.size(); i10++) {
                        appDatabaseObject.transDaoObject().insertMedia(new MediaEntity(CreateTransaction.this.bitmapsPath.get(i10), insertTrans));
                    }
                    if (CreateTransaction.this.type != 2 && CreateTransaction.this.isRecurring) {
                        long untilDate = RecurringHelper.getUntilDate(CreateTransaction.this.date, CreateTransaction.this.recurringUntilDate, CreateTransaction.this.recurringUntilType, CreateTransaction.this.recurringType, CreateTransaction.this.recurringIncrement, CreateTransaction.this.recurringRepeatTime, CreateTransaction.this.recurringRepeatType, CreateTransaction.this.recurringRepeatDate);
                        CreateTransaction createTransaction2 = CreateTransaction.this;
                        long addRecurringTransaction = createTransaction2.addRecurringTransaction(untilDate, trim, trim2, createTransaction2.date, i3, i, i2);
                        if (CreateTransaction.this.isRecurring) {
                            CreateTransaction createTransaction3 = CreateTransaction.this;
                            createTransaction3.insertRecurring(trim, trim2, createTransaction3.type, i, i2, addRecurringTransaction);
                        }
                    }
                    CreateTransaction.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateTransaction.8.1
                        @Override // java.lang.Runnable
                        public void run() {
                            CreateTransaction.this.finish();
                        }
                    });
                }
            });
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            if (ActivityCompat.checkSelfPermission(this, "android.permission.READ_MEDIA_IMAGES") != 0) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_MEDIA_IMAGES")) {
                    showPermissionDialog(getResources().getString(R.string.access_storage_hint), getResources().getString(R.string.allow), getResources().getString(R.string.deny), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.CreateTransaction.9
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(CreateTransaction.this, new String[]{"android.permission.READ_MEDIA_IMAGES"}, 0);
                        }
                    });
                    return;
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_MEDIA_IMAGES"}, 0);
                    return;
                }
            }
            showMediaPickerDialog();
        } else if (ActivityCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
                showPermissionDialog(getResources().getString(R.string.access_storage_hint), getResources().getString(R.string.allow), getResources().getString(R.string.deny), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.CreateTransaction.10
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(CreateTransaction.this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 0);
                    }
                });
            } else {
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 0);
            }
        } else {
            showMediaPickerDialog();
        }
    }

    @Override
    // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != 0) {
            return;
        }
        if (grantResults.length > 0 && grantResults[0] == 0) {
            showMediaPickerDialog();
        } else if (Build.VERSION.SDK_INT >= 33) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_MEDIA_IMAGES")) {
                return;
            }
            showPermissionDialog(getResources().getString(R.string.access_storage_rational_hint), getResources().getString(R.string.app_settings), getResources().getString(R.string.not_now), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.CreateTransaction.11
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.fromParts("package", CreateTransaction.this.getPackageName(), null));
                    CreateTransaction.this.startActivity(intent);
                }
            });
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
        } else {
            showPermissionDialog(getResources().getString(R.string.access_storage_rational_hint), getResources().getString(R.string.app_settings), getResources().getString(R.string.not_now), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.CreateTransaction.12
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.fromParts("package", CreateTransaction.this.getPackageName(), null));
                    CreateTransaction.this.startActivity(intent);
                }
            });
        }
    }

    private void showPermissionDialog(String message, String positive, String negative, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle(getResources().getString(R.string.access_storage_title));
        builder.setPositiveButton(positive, listener);
        builder.setNegativeButton(negative, null);
        builder.create().show();
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

    private void validateRecurring() {
        int i = this.recurringType;
        if (i != 0) {
            long untilDate = RecurringHelper.getUntilDate(this.date, this.recurringUntilDate, this.recurringUntilType, i, this.recurringIncrement, this.recurringRepeatTime, this.recurringRepeatType, this.recurringRepeatDate);
            Date date = this.date;
            if (RecurringHelper.isValidRecurring(untilDate, RecurringHelper.getNextOccurrence(date, date.getTime(), this.recurringType, this.recurringIncrement, this.recurringRepeatType, this.recurringRepeatDate))) {
                this.isRecurring = true;
                this.recurringImage.setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_IN);
                this.recurringLabel.setTextColor(getResources().getColor(R.color.blue));
                int i2 = this.recurringType;
                if (i2 == 1) {
                    this.recurringLabel.setText(R.string.daily);
                    return;
                } else if (i2 == 2) {
                    this.recurringLabel.setText(R.string.weekly);
                    return;
                } else if (i2 == 3) {
                    this.recurringLabel.setText(R.string.monthly);
                    return;
                } else if (i2 == 4) {
                    this.recurringLabel.setText(R.string.yearly);
                    return;
                } else {
                    return;
                }
            }
            this.isRecurring = false;
            this.recurringLabel.setTextColor(Helper.getAttributeColor(this, R.attr.primaryTextColor));
            this.recurringImage.setColorFilter(Helper.getAttributeColor(this, R.attr.primaryTextColor), PorterDuff.Mode.SRC_IN);
            this.recurringLabel.setText(R.string.recurring);
            return;
        }
        this.isRecurring = false;
        this.recurringLabel.setTextColor(Helper.getAttributeColor(this, R.attr.primaryTextColor));
        this.recurringImage.setColorFilter(Helper.getAttributeColor(this, R.attr.primaryTextColor), PorterDuff.Mode.SRC_IN);
        this.recurringLabel.setText(R.string.recurring);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void insertRecurring(String note, String memo, int type, int categoryId, int subcategoryId, long lastUpdateMilli) {
        if (this.recurringType != 0) {
            AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(getApplicationContext());
            long untilDate = RecurringHelper.getUntilDate(this.date, this.recurringUntilDate, this.recurringUntilType, this.recurringType, this.recurringIncrement, this.recurringRepeatTime, this.recurringRepeatType, this.recurringRepeatDate);
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTimeInMillis(untilDate);
            Date time = calendar.getTime();
            calendar.setTimeInMillis(lastUpdateMilli);
            appDatabaseObject.recurringDaoObject().insetRecurring(new RecurringEntity(note, memo, type, this.recurringType, this.recurringRepeatType, this.recurringRepeatDate, this.recurringIncrement, this.amount, this.date, time, calendar.getTime(), SharePreferenceHelper.getAccountId(getApplicationContext()), categoryId, this.walletId, -1, 0L, 0, subcategoryId));
        }
    }

    private void addTransaction(final String note, final String memo, final Date date, final int accountId, final int categoryId, final int subcategoryId) {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateTransaction.13
            @Override // java.lang.Runnable
            public void run() {
                AppDatabaseObject.getInstance(CreateTransaction.this.getApplicationContext()).transDaoObject().insertTrans(new TransEntity(note, memo, CreateTransaction.this.amount, date, CreateTransaction.this.type, accountId, categoryId, 0, CreateTransaction.this.walletId, CreateTransaction.this.fromWalletId, CreateTransaction.this.transAmount, 0, 0, subcategoryId));
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long addRecurringTransaction(long untilTime, String note, String memo, Date date, int accountId, int categoryId, int subcategoryId) {
        long time;
        int i;
        int i2 = 0;
        int i3 = 0;
        long time2;
        long j;
        int i4;
        long time3 = 0;
        int i5;
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(date);
        long time4 = date.getTime();
        long todayMillis = DateHelper.getTodayMillis();
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        int i6 = this.recurringType;
        long j2 = 0;
        if (i6 == 1) {
            while (true) {
                if (calendar.getTime().getTime() <= time4 || DateHelper.isSameDay(calendar.getTime().getTime(), time4)) {
                    i5 = 5;
                } else if (calendar.getTime().getTime() >= todayMillis && !DateHelper.isSameDay(calendar.getTime().getTime(), todayMillis)) {
                    break;
                } else if (untilTime != j2) {
                    if (calendar.getTime().getTime() >= untilTime && !DateHelper.isSameDay(calendar.getTime().getTime(), untilTime)) {
                        break;
                    }
                    long time5 = calendar.getTime().getTime();
                    i5 = 5;
                    addTransaction(note, memo, calendar.getTime(), accountId, categoryId, subcategoryId);
                    time4 = time5;
                } else {
                    i5 = 5;
                    long time6 = calendar.getTime().getTime();
                    addTransaction(note, memo, calendar.getTime(), accountId, categoryId, subcategoryId);
                    time4 = time6;
                }
                calendar.add(i5, this.recurringIncrement);
                j2 = 0;
            }
            j = j2;
        } else {
            int i7 = 1;
            if (i6 == 2) {
                boolean z = false;
                while (true) {
                    calendar.set(7, 1);
                    int i8 = 0;
                    for (int i9 = 7; i8 < i9; i9 = 7) {
                        int i10 = i8 + 1;
                        calendar.set(i9, i10);
                        if (this.recurringRepeatDate.charAt(i8) == '1' && calendar.getTime().getTime() > time4 && !DateHelper.isSameDay(calendar.getTime().getTime(), time4)) {
                            if (calendar.getTime().getTime() < todayMillis || DateHelper.isSameDay(calendar.getTime().getTime(), todayMillis)) {
                                if (untilTime != 0) {
                                    if (calendar.getTime().getTime() < untilTime || DateHelper.isSameDay(calendar.getTime().getTime(), untilTime)) {
                                        time3 = calendar.getTime().getTime();
                                        i4 = i10;
                                        addTransaction(note, memo, calendar.getTime(), accountId, categoryId, subcategoryId);
                                    }
                                } else {
                                    i4 = i10;
                                    time3 = calendar.getTime().getTime();
                                    addTransaction(note, memo, calendar.getTime(), accountId, categoryId, subcategoryId);
                                }
                                time4 = time3;
                            }
                            z = true;
                            break;
                        }
                        i4 = i10;
                        i8 = i4;
                    }
                    if (z) {
                        break;
                    }
                    calendar.add(3, this.recurringIncrement);
                }
            } else if (i6 == 3) {
                while (true) {
                    if (this.recurringRepeatType == i7) {
                        calendar.set(5, calendar.getActualMaximum(5));
                    }
                    if (calendar.getTime().getTime() > time4 && !DateHelper.isSameDay(calendar.getTime().getTime(), time4)) {
                        if (calendar.getTime().getTime() >= todayMillis && !DateHelper.isSameDay(calendar.getTime().getTime(), todayMillis)) {
                            break;
                        }
                        if (untilTime != 0) {
                            if (calendar.getTime().getTime() >= untilTime && !DateHelper.isSameDay(calendar.getTime().getTime(), untilTime)) {
                                break;
                            }
                            time2 = calendar.getTime().getTime();
                            i = 29;
                            addTransaction(note, memo, calendar.getTime(), accountId, categoryId, subcategoryId);
                        } else {
                            i = 29;
                            time2 = calendar.getTime().getTime();
                            addTransaction(note, memo, calendar.getTime(), accountId, categoryId, subcategoryId);
                        }
                        time4 = time2;
                    } else {
                        i = 29;
                    }
                    if (this.recurringRepeatType == 0) {
                        int i11 = calendar.get(5);
                        if (i11 == 31 || i11 == 30) {
                            i2 = 2;
                            i3 = 1;
                        } else if (i11 == i) {
                            i3 = 1;
                            i2 = 2;
                        } else {
                            calendar.add(2, this.recurringIncrement);
                        }
                        calendar.set(5, i3);
                        do {
                            calendar.add(i2, this.recurringIncrement);
                        } while (calendar.getActualMaximum(5) < i11);
                        calendar.set(5, i11);
                    } else {
                        calendar.add(2, this.recurringIncrement);
                    }
                    i7 = 1;
                }
            } else {
                while (true) {
                    if (calendar.getTime().getTime() > time4 && !DateHelper.isSameDay(calendar.getTime().getTime(), time4)) {
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
                        time4 = time;
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
            j = 0;
        }
        if (untilTime != j) {
            if (calendar.getTime().getTime() > untilTime && !DateHelper.isSameDay(calendar.getTime().getTime(), untilTime)) {
                this.isRecurring = false;
            }
        } else {
            this.isRecurring = true;
        }
        return time4;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        this.billingHelper.unregisterBroadCast(this);
        super.onPause();
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

    private void initAds() {
        if (this.isAdsInit) {
            return;
        }
        this.isAdsInit = true;
        AdView adView = new AdView(this);
        this.bannerView = adView;
        adView.setAdUnitId(getResources().getString(R.string.ad_unit_create_transaction));
        this.adView.addView(this.bannerView);
        this.bannerView.setAdSize(AdsHelper.getAdSize(this));
        this.bannerView.loadAd(new AdRequest.Builder().build());
        this.bannerView.setAdListener(new AdListener() { // from class: com.ktwapps.walletmanager.CreateTransaction.14
            @Override // com.google.android.gms.ads.AdListener
            public void onAdLoaded() {
                super.onAdLoaded();
                CreateTransaction.this.adView.setVisibility(0);
            }
        });
    }

    private void checkSubscription() {
        if (SharePreferenceHelper.getPremium(this) == 2) {
            this.adView.setVisibility(8);
        } else {
            initAds();
        }
    }

    private void showRateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.dialog_currency_rate);
        builder.setTitle(R.string.exchange_rate);
        builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.CreateTransaction.15
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    BigDecimal stripTrailingZeros = new BigDecimal(CreateTransaction.this.dialogSubEditText.getText().toString()).multiply(new BigDecimal(100)).setScale(0, RoundingMode.DOWN).stripTrailingZeros();
                    DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ENGLISH);
                    decimalFormat.setGroupingUsed(false);
                    decimalFormat.setMaximumFractionDigits(0);
                    decimalFormat.setMinimumFractionDigits(0);
                    CreateTransaction.this.transAmount = Long.parseLong(decimalFormat.format(stripTrailingZeros));
                    Wallets wallet = CreateTransaction.this.getWallet();
                    Wallets fromWallet = CreateTransaction.this.getFromWallet();
                    if (wallet == null || fromWallet == null) {
                        return;
                    }
                    String currency = wallet.getCurrency();
                    CreateTransaction.this.rateLabel.setText(Helper.getBeautifyRate(fromWallet.getCurrency(), currency, CreateTransaction.this.amount, CreateTransaction.this.transAmount));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        AlertDialog create = builder.create();
        this.dialog = create;
        create.show();
        this.dialogMainEditText = this.dialog.findViewById(R.id.mainEditText);
        this.dialogMainLabel = this.dialog.findViewById(R.id.mainLabel);
        this.dialogSubLabel = this.dialog.findViewById(R.id.subLabel);
        this.dialogRateLabel = this.dialog.findViewById(R.id.rateLabel);
        this.dialogSubEditText = this.dialog.findViewById(R.id.subEditText);
        this.dialogRateEditText = this.dialog.findViewById(R.id.rateEditText);
        Wallets wallet = getWallet();
        Wallets fromWallet = getFromWallet();
        if (wallet == null || fromWallet == null) {
            return;
        }
        float rate = wallet.getRate();
        float rate2 = fromWallet.getRate();
        String currency = wallet.getCurrency();
        float f = rate2 / rate;
        this.dialogMainLabel.setText(fromWallet.getCurrency());
        this.dialogSubLabel.setText(currency);
        this.dialogMainEditText.setText(Helper.getBeautifyAmount(this.amount).trim());
        if (this.transAmount == 0) {
            EditText editText = this.dialogRateEditText;
            editText.setText(f + "");
            this.dialogSubEditText.setText(Helper.getBeautifyAmount((long) (((float) this.amount) * f)).trim());
        } else {
            BigDecimal stripTrailingZeros = new BigDecimal(this.transAmount / 100).divide(new BigDecimal(this.amount / 100), 9, RoundingMode.HALF_EVEN).setScale(9, RoundingMode.DOWN).stripTrailingZeros();
            DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ENGLISH);
            decimalFormat.setGroupingUsed(false);
            decimalFormat.setMaximumFractionDigits(9);
            decimalFormat.setMinimumFractionDigits(0);
            this.dialogRateEditText.setText(decimalFormat.format(stripTrailingZeros));
            this.dialogSubEditText.setText(Helper.getBeautifyAmount(this.transAmount).trim());
        }
        this.dialogMainEditText.setLongClickable(false);
        this.dialogMainEditText.setFocusable(false);
        this.dialogRateEditText.setFilters(new InputFilter[]{new RateInputFilter(6, 9, Double.POSITIVE_INFINITY)});
        this.dialogSubEditText.setFilters(new InputFilter[]{new RateInputFilter(15, 2, Double.POSITIVE_INFINITY)});
        this.rateWatcher = new CustomTextWatcher(this.dialogRateEditText, this);
        this.subWatcher = new CustomTextWatcher(this.dialogSubEditText, this);
        this.dialogRateEditText.addTextChangedListener(this.rateWatcher);
        this.dialogSubEditText.addTextChangedListener(this.subWatcher);
    }

    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            this.feeImageView.setVisibility(0);
            this.feeEditText.setTextColor(Helper.getAttributeColor(this, R.attr.primaryTextColor));
            return;
        }
        this.feeImageView.setVisibility(8);
        this.feeEditText.setTextColor(Helper.getAttributeColor(this, R.attr.untitledTextColor));
    }

    /* loaded from: classes3.dex */
    public class CustomTextWatcher implements TextWatcher {
        Context context;
        private final EditText mEditText;

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public CustomTextWatcher(EditText e, Context context) {
            this.mEditText = e;
            this.context = context;
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable s) {
            try {
                if (this.mEditText.getId() == R.id.rateEditText) {
                    CreateTransaction.this.dialogSubEditText.removeTextChangedListener(CreateTransaction.this.subWatcher);
                    BigDecimal stripTrailingZeros = new BigDecimal(CreateTransaction.this.amount / 100).multiply(new BigDecimal(CreateTransaction.this.dialogRateEditText.getText().toString())).setScale(2, RoundingMode.DOWN).stripTrailingZeros();
                    DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ENGLISH);
                    decimalFormat.setGroupingUsed(false);
                    decimalFormat.setMaximumFractionDigits(2);
                    decimalFormat.setMinimumFractionDigits(0);
                    CreateTransaction.this.dialogSubEditText.setText(decimalFormat.format(stripTrailingZeros));
                    CreateTransaction.this.dialogSubEditText.addTextChangedListener(CreateTransaction.this.subWatcher);
                } else {
                    CreateTransaction.this.dialogRateEditText.removeTextChangedListener(CreateTransaction.this.rateWatcher);
                    BigDecimal stripTrailingZeros2 = new BigDecimal(CreateTransaction.this.dialogSubEditText.getText().toString()).divide(new BigDecimal(CreateTransaction.this.amount / 100), 9, RoundingMode.HALF_EVEN).setScale(9, RoundingMode.DOWN).stripTrailingZeros();
                    DecimalFormat decimalFormat2 = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ENGLISH);
                    decimalFormat2.setGroupingUsed(false);
                    decimalFormat2.setMaximumFractionDigits(9);
                    decimalFormat2.setMinimumFractionDigits(0);
                    CreateTransaction.this.dialogRateEditText.setText(decimalFormat2.format(stripTrailingZeros2));
                    CreateTransaction.this.dialogRateEditText.addTextChangedListener(CreateTransaction.this.rateWatcher);
                }
                CreateTransaction.this.dialog.getButton(-1).setEnabled(true);
            } catch (NumberFormatException unused) {
                CreateTransaction.this.dialog.getButton(-1).setEnabled(false);
            }
        }
    }
}
