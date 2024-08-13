package com.expance.merapaisa;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
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
import androidx.appcompat.widget.Toolbar;
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
import com.expance.merapaisa.Database.Entity.TransEntity;
import com.expance.merapaisa.Database.Entity.WalletEntity;
import com.expance.merapaisa.Model.Trans;
import com.expance.merapaisa.Model.Wallets;
import com.expance.merapaisa.Utility.AdsHelper;
import com.expance.merapaisa.Utility.BillingHelper;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Utility.DateHelper;
import com.expance.merapaisa.Utility.FileHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.PicassoTransformation;
import com.expance.merapaisa.Utility.RateInputFilter;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import com.squareup.picasso.Picasso;

import org.apache.http.cookie.ClientCookie;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class EditTransaction extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, BillingHelper.BillingListener, CompoundButton.OnCheckedChangeListener {
    FrameLayout adView;
    long amount;
    EditText amountEditText;
    AdView bannerView;
    BillingHelper billingHelper;
    private ArrayList<String> bitmapPath;
    private ArrayList<Bitmap> bitmaps;
    String cameraPath;
    ImageView cancelImage1;
    ImageView cancelImage2;
    ImageView cancelImage3;
    EditText categoryEditText;
    TextView categoryLabel;
    Date date;
    TextView dayLabel;
    private ArrayList<String> deletePath;
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
    private ArrayList<String> imagePath;
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
    LinearLayout linearLayout;
    EditText noteEditText;
    ImageView noteImage;
    ImageView rateImageView;
    TextView rateLabel;
    CustomTextWatcher rateWatcher;
    TextView saveLabel;
    ScrollView scrollView;
    CustomTextWatcher subWatcher;
    Switch switchView;
    private Trans trans;
    long transAmount;
    private int transId;
    TextView transferLabel;
    ConstraintLayout transferWrapper;
    int type;
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
        outState.putString("expenseCategory", this.expenseCategory);
        outState.putString("incomeCategory", this.incomeCategory);
        outState.putBoolean("isFeeAmount", this.isFeeAmount);
        outState.putInt(JamXmlElements.TYPE, this.type);
        outState.putInt("expenseSubcategoryId", this.expenseSubcategoryId);
        outState.putInt("incomeSubcategoryId", this.incomeSubcategoryId);
        outState.putInt("expenseCategoryId", this.expenseCategoryId);
        outState.putInt("incomeCategoryId", this.incomeCategoryId);
        outState.putInt("fromWalletId", this.fromWalletId);
        outState.putInt("walletId", this.walletId);
        outState.putStringArrayList("imagePath", this.imagePath);
        outState.putStringArrayList("deletePath", this.deletePath);
        outState.putStringArrayList("bitmapPath", this.bitmapPath);
        outState.putLong("amount", this.amount);
        outState.putLong("feeAmount", this.feeAmount);
        outState.putLong("transAmount", this.transAmount);
        outState.putLong("date", this.date.getTime());
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
        setContentView(R.layout.activity_edit_transaction);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        this.transId = getIntent().getIntExtra("transId", 0);
        this.expenseCategory = "";
        this.incomeCategory = "";
        this.isComplete = false;
        this.expenseCategoryId = 0;
        this.incomeCategoryId = 0;
        this.expenseSubcategoryId = 0;
        this.incomeSubcategoryId = 0;
        this.fromWalletId = 0;
        this.walletId = 0;
        this.bitmaps = new ArrayList<>();
        this.bitmapPath = new ArrayList<>();
        this.imagePath = new ArrayList<>();
        this.deletePath = new ArrayList<>();
        this.walletList = new ArrayList();
        this.amount = 0L;
        this.transAmount = 0L;
        this.date = DateHelper.getCurrentDateTime();
        setUpLayout();
        BillingHelper billingHelper = new BillingHelper(this);
        this.billingHelper = billingHelper;
        billingHelper.setListener(this);
        this.billingHelper.setUpBillingClient();
        populateData(savedInstanceState != null);
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
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTimeInMillis(savedInstanceState.getLong("date"));
            this.date = calendar.getTime();
            this.deletePath = savedInstanceState.getStringArrayList("deletePath");
            this.imagePath = savedInstanceState.getStringArrayList("imagePath");
            this.bitmapPath = savedInstanceState.getStringArrayList("bitmapPath");
            Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.EditTransaction.1
                @Override // java.lang.Runnable
                public void run() {
                    Iterator it = EditTransaction.this.bitmapPath.iterator();
                    while (it.hasNext()) {
                        EditTransaction.this.bitmaps.add(BitmapFactory.decodeFile((String) it.next()));
                    }
                    EditTransaction.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.EditTransaction.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            EditTransaction.this.displayImage();
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

    /* JADX INFO: Access modifiers changed from: private */
    public void checkIsComplete() {
        int i;
        int i2;
        int i3 = this.type;
        boolean z = false;
        if (i3 == 0) {
            if (this.incomeCategoryId != 0 && this.amount != 0 && this.walletId != 0) {
                z = true;
            }
            this.isComplete = z;
        } else if (i3 == 1) {
            if (this.expenseCategoryId != 0 && this.amount != 0 && this.walletId != 0) {
                z = true;
            }
            this.isComplete = z;
        } else if (i3 == 2) {
            if (this.amount != 0 && (i = this.walletId) != 0 && (i2 = this.fromWalletId) != 0 && i2 != i) {
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
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.income));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.adView = (FrameLayout) findViewById(R.id.adView);
        this.saveLabel = (TextView) findViewById(R.id.saveLabel);
        this.rateLabel = (TextView) findViewById(R.id.rateLabel);
        this.rateImageView = (ImageView) findViewById(R.id.rateImageView);
        this.scrollView = (ScrollView) findViewById(R.id.scrollView);
        this.amountEditText = (EditText) findViewById(R.id.amountEditText);
        this.categoryEditText = (EditText) findViewById(R.id.categoryEditText);
        this.noteEditText = (EditText) findViewById(R.id.noteEditText);
        this.noteImage = (ImageView) findViewById(R.id.noteImageView);
        this.imageView1 = (ImageView) findViewById(R.id.imageView1);
        this.imageView2 = (ImageView) findViewById(R.id.imageView2);
        this.imageView3 = (ImageView) findViewById(R.id.imageView3);
        this.cancelImage1 = (ImageView) findViewById(R.id.cancelImage1);
        this.cancelImage2 = (ImageView) findViewById(R.id.cancelImage2);
        this.cancelImage3 = (ImageView) findViewById(R.id.cancelImage3);
        this.imageWrapper1 = (CardView) findViewById(R.id.imageWrapper1);
        this.imageWrapper2 = (CardView) findViewById(R.id.imageWrapper2);
        this.imageWrapper3 = (CardView) findViewById(R.id.imageWrapper3);
        this.incomeWrapper = (ConstraintLayout) findViewById(R.id.incomeWrapper);
        this.transferWrapper = (ConstraintLayout) findViewById(R.id.transferWrapper);
        this.expenseWrapper = (ConstraintLayout) findViewById(R.id.expenseWrapper);
        this.incomeLabel = (TextView) findViewById(R.id.incomeLabel);
        this.transferLabel = (TextView) findViewById(R.id.transferLabel);
        this.expenseLabel = (TextView) findViewById(R.id.expenseLabel);
        this.dayLabel = (TextView) findViewById(R.id.dayLabel);
        this.hourLabel = (TextView) findViewById(R.id.hourLabel);
        this.categoryLabel = (TextView) findViewById(R.id.categoryLabel);
        this.walletLabel = (TextView) findViewById(R.id.walletLabel);
        this.fromWalletLabel = (TextView) findViewById(R.id.fromWalletLabel);
        this.walletEditText = (EditText) findViewById(R.id.walletEditText);
        this.fromWalletEditText = (EditText) findViewById(R.id.fromWalletEditText);
        this.linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        this.descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        this.feeWrapper = (ConstraintLayout) findViewById(R.id.feeWrapper);
        this.feeEditText = (TextView) findViewById(R.id.feeEditText);
        this.switchView = (Switch) findViewById(R.id.switchView);
        this.feeImageView = (ImageView) findViewById(R.id.feeImageView);
        this.expenseWrapper.setBackgroundColor(getResources().getColor(R.color.expense));
        this.expenseLabel.setTextColor(getResources().getColor(17170443));
        this.hourLabel.setText(DateHelper.getFormattedTime(this.date, getApplicationContext()));
        this.dayLabel.setText(DateHelper.getFormattedDate(getApplicationContext(), this.date));
        this.amountEditText.setText(Helper.getBeautifyAmount(SharePreferenceHelper.getAccountSymbol(this), this.amount));
        this.feeEditText.setText(Helper.getBeautifyAmount(SharePreferenceHelper.getAccountSymbol(this), this.feeAmount));
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
    }

    private void populateData(final boolean isSavedInstanceState) {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.EditTransaction.2
            @Override // java.lang.Runnable
            public void run() {
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(EditTransaction.this);
                java.util.Calendar calendar = java.util.Calendar.getInstance();
                calendar.set(14, 0);
                calendar.set(13, 0);
                calendar.set(12, 0);
                calendar.set(11, 0);
                calendar.add(5, 1);
                if (!SharePreferenceHelper.isFutureBalanceOn(EditTransaction.this.getApplicationContext())) {
                    calendar.set(1, 10000);
                }
                EditTransaction.this.walletList = appDatabaseObject.walletDaoObject().getWallets(SharePreferenceHelper.getAccountId(EditTransaction.this.getApplicationContext()), 0, calendar.getTimeInMillis());
                EditTransaction.this.trans = appDatabaseObject.transDaoObject().getTransById(EditTransaction.this.transId, SharePreferenceHelper.getAccountId(EditTransaction.this.getApplicationContext()));
                if (EditTransaction.this.trans.getFeeId() != 0) {
                    Trans transById = appDatabaseObject.transDaoObject().getTransById(EditTransaction.this.trans.getFeeId(), SharePreferenceHelper.getAccountId(EditTransaction.this.getApplicationContext()));
                    if (!isSavedInstanceState) {
                        EditTransaction.this.feeAmount = -transById.getAmount();
                    }
                }
                if (!isSavedInstanceState) {
                    EditTransaction.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.EditTransaction.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            EditTransaction.this.type = EditTransaction.this.trans.getType();
                            EditTransaction editTransaction = EditTransaction.this;
                            int i = (EditTransaction.this.trans.getAmount() > 0L ? 1 : (EditTransaction.this.trans.getAmount() == 0L ? 0 : -1));
                            long amount = EditTransaction.this.trans.getAmount();
                            if (i < 0) {
                                amount = -amount;
                            }
                            editTransaction.amount = amount;
                            EditTransaction.this.date = EditTransaction.this.trans.getDateTime();
                            if (EditTransaction.this.type == 2) {
                                EditTransaction.this.walletId = EditTransaction.this.trans.getTransferWalletId();
                                EditTransaction.this.fromWalletId = EditTransaction.this.trans.getWalletId();
                                EditTransaction.this.setWallet(EditTransaction.this.walletId, -11);
                                EditTransaction.this.setWallet(EditTransaction.this.fromWalletId, 11);
                                EditTransaction.this.transAmount = EditTransaction.this.trans.getTransAmount();
                            } else {
                                EditTransaction.this.walletId = EditTransaction.this.trans.getWalletId();
                                EditTransaction.this.setWallet(EditTransaction.this.walletId, -11);
                            }
                            EditTransaction.this.noteEditText.setText(EditTransaction.this.trans.getMemo() != null ? EditTransaction.this.trans.getMemo() : "");
                            EditTransaction.this.descriptionEditText.setText(EditTransaction.this.trans.note);
                            EditTransaction.this.dayLabel.setText(DateHelper.getFormattedDate(EditTransaction.this.getApplicationContext(), EditTransaction.this.date));
                            EditTransaction.this.hourLabel.setText(DateHelper.getFormattedTime(EditTransaction.this.date, EditTransaction.this.getApplicationContext()));
                            if (EditTransaction.this.type == 0) {
                                EditTransaction.this.incomeCategory = EditTransaction.this.trans.getCategory(EditTransaction.this.getApplicationContext());
                                EditTransaction.this.incomeCategoryId = EditTransaction.this.trans.getCategoryId();
                                EditTransaction.this.incomeSubcategoryId = EditTransaction.this.trans.getSubcategoryId();
                            } else if (EditTransaction.this.type == 1) {
                                EditTransaction.this.expenseCategory = EditTransaction.this.trans.getCategory(EditTransaction.this.getApplicationContext());
                                EditTransaction.this.expenseCategoryId = EditTransaction.this.trans.getCategoryId();
                                EditTransaction.this.expenseSubcategoryId = EditTransaction.this.trans.getSubcategoryId();
                            }
                            if (EditTransaction.this.trans.getMedia() != null) {
                                EditTransaction.this.imagePath.addAll(Arrays.asList(EditTransaction.this.trans.getMedia().split(",")));
                                EditTransaction.this.displayImage();
                            }
                            String rateLayout = EditTransaction.this.setRateLayout();
                            EditTransaction.this.amountEditText.setText(Helper.getBeautifyAmount(rateLayout, EditTransaction.this.amount));
                            EditTransaction.this.feeEditText.setText(Helper.getBeautifyAmount(rateLayout, EditTransaction.this.feeAmount));
                            EditTransaction.this.switchView.setChecked(EditTransaction.this.trans.getFeeId() != 0);
                            EditTransaction.this.switchTransMode(EditTransaction.this.type);
                            EditTransaction.this.checkIsComplete();
                        }
                    });
                    return;
                }
                EditTransaction editTransaction = EditTransaction.this;
                editTransaction.setWallet(editTransaction.fromWalletId, 11);
                EditTransaction editTransaction2 = EditTransaction.this;
                editTransaction2.setWallet(editTransaction2.walletId, -11);
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
            deleteImage(0);
        } else if (view.getId() == R.id.cancelImage2) {
            deleteImage(1);
        } else if (view.getId() == R.id.cancelImage3) {
            deleteImage(2);
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
        } else if (view.getId() == R.id.feeEditText) {
            if (this.switchView.isChecked()) {
                this.isFeeAmount = true;
                Intent intent3 = new Intent(this, Calculator.class);
                intent3.putExtra("amount", this.feeAmount);
                startActivityForResult(intent3, 1);
                overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
                return;
            }
        } else if (view.getId() == R.id.feeImageView) {
            if (this.switchView.isChecked()) {
                this.isFeeAmount = true;
                Intent intent3 = new Intent(this, Calculator.class);
                intent3.putExtra("amount", this.feeAmount);
                startActivityForResult(intent3, 1);
                overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
                return;
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
        } else if (view.getId() == R.id.saveLabel) {
            editTransaction();
        } else if (view.getId() == R.id.transferWrapper) {
            switchTransMode(2);
        } else if (view.getId() == R.id.walletEditText) {
            Intent intent5 = new Intent(this, WalletPicker.class);
            intent5.putExtra("id", this.walletId);
            intent5.putExtra(JamXmlElements.TYPE, -11);
            startActivityForResult(intent5, 2);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        } else {
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(this.imagePath);
            arrayList.addAll(this.bitmapPath);
            if (arrayList.size() > 0) {
                Intent intent6 = new Intent(this, PhotoViewer.class);
                intent6.putExtra(ClientCookie.PATH_ATTR, TextUtils.join(",", arrayList));
                intent6.putExtra(FirebaseAnalytics.Param.INDEX, ((Integer) view.getTag()).intValue());
                startActivity(intent6);
                return;
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
            label48:
            {
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
            var8 = (String) DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(var7));
        }

        return var8;
    }

    /* JADX INFO: Access modifiers changed from: protected */
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
                        this.bitmapPath.add(this.cameraPath);
                        this.bitmaps.add(decodeFile);
                        displayImage();
                        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.EditTransaction.3
                            @Override // java.lang.Runnable
                            public void run() {
                                try {
                                    FileHelper.saveToGallery(EditTransaction.this.getApplicationContext(), decodeFile, file.getName());
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
                    Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.EditTransaction.4
                        @Override // java.lang.Runnable
                        public void run() {
                            try {
                                EditTransaction.this.bitmapPath.add(FileHelper.convertBitmaptoFile(FileHelper.createImageFile(EditTransaction.this.getApplicationContext()), bitmapFromUri));
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
                checkIsComplete();
                this.transAmount = 0L;
                String rateLayout = setRateLayout();
                this.amountEditText.setText(Helper.getBeautifyAmount(rateLayout, this.amount));
                this.feeEditText.setText(Helper.getBeautifyAmount(rateLayout, this.feeAmount));
                checkIsComplete();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void switchTransMode(int i) {
        int i2;
        String str;
        String str2;
        this.incomeWrapper.setBackgroundColor(Helper.getAttributeColor(this, R.attr.secondaryBackground));
        this.incomeLabel.setTextColor(Helper.getAttributeColor(this, R.attr.primaryTextColor));
        this.transferWrapper.setBackgroundColor(Helper.getAttributeColor(this, R.attr.secondaryBackground));
        this.transferLabel.setTextColor(Helper.getAttributeColor(this, R.attr.primaryTextColor));
        this.expenseWrapper.setBackgroundColor(Helper.getAttributeColor(this, R.attr.secondaryBackground));
        this.expenseLabel.setTextColor(Helper.getAttributeColor(this, R.attr.primaryTextColor));
        this.rateLabel.setVisibility(8);
        this.rateImageView.setVisibility(8);
        this.feeWrapper.setVisibility(8);
        String str3 = null;
        if (i == 0) {
            this.incomeWrapper.setBackgroundColor(getResources().getColor(R.color.income));
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
            i2 = R.string.income;
        } else if (i == 1) {
            this.expenseWrapper.setBackgroundColor(getResources().getColor(R.color.expense));
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
            i2 = R.string.expense;
        } else {
            this.transferWrapper.setBackgroundColor(getResources().getColor(R.color.transfer));
            this.transferLabel.setTextColor(getResources().getColor(17170443));
            this.walletLabel.setText(R.string.to_wallet);
            this.fromWalletLabel.setVisibility(0);
            this.fromWalletEditText.setVisibility(0);
            this.categoryLabel.setVisibility(4);
            this.categoryEditText.setVisibility(4);
            this.type = 2;
            String rateLayout = setRateLayout();
            this.feeEditText.setText(Helper.getBeautifyAmount(rateLayout, this.feeAmount));
            this.amountEditText.setText(Helper.getBeautifyAmount(rateLayout, this.amount));
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
        builder.setItems(new String[]{getResources().getString(R.string.camera), getResources().getString(R.string.gallery)}, new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.EditTransaction.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                int size = EditTransaction.this.imagePath.size() + EditTransaction.this.bitmaps.size();
                if (EditTransaction.this.billingHelper.getBillingStatus() == 2 || size <= 0) {
                    if (i == 0) {
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        File createImageFile = FileHelper.createImageFile(EditTransaction.this.getApplicationContext());
                        EditTransaction.this.cameraPath = createImageFile.getAbsolutePath();
                        Context applicationContext = EditTransaction.this.getApplicationContext();
                        intent.putExtra("output", FileProvider.getUriForFile(applicationContext, EditTransaction.this.getApplicationContext().getPackageName() + ".fileProvider", createImageFile));
                        intent.putExtra("return-data", true);
                        intent.addFlags(1);
                        EditTransaction.this.startActivityForResult(intent, 3);
                    } else if (i == 1) {
                        Intent intent2 = new Intent();
                        intent2.setType("image/*");
                        intent2.setAction("android.intent.action.GET_CONTENT");
                        EditTransaction.this.startActivityForResult(Intent.createChooser(intent2, ""), 4);
                    }
                    EditTransaction.this.scrollView.post(new Runnable() { // from class: com.ktwapps.walletmanager.EditTransaction.5.1
                        @Override // java.lang.Runnable
                        public void run() {
                            EditTransaction.this.scrollView.fullScroll(130);
                        }
                    });
                    return;
                }
                EditTransaction.this.startActivity(new Intent(EditTransaction.this.getApplicationContext(), Premium.class));
                EditTransaction.this.overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
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
        int size = this.imagePath.size() + this.bitmaps.size();
        if (size > 0) {
            this.linearLayout.setVisibility(0);
            if (size == 1) {
                this.imageWrapper1.setVisibility(0);
                if (this.imagePath.size() == 1) {
                    Picasso.get().load(new File(this.imagePath.get(0))).transform(new PicassoTransformation(this.imageView1)).into(this.imageView1);
                } else {
                    this.imageView1.setImageBitmap(this.bitmaps.get(0));
                }
            } else if (size == 2) {
                this.imageWrapper1.setVisibility(0);
                this.imageWrapper2.setVisibility(0);
                if (this.imagePath.size() == 1) {
                    Picasso.get().load(new File(this.imagePath.get(0))).transform(new PicassoTransformation(this.imageView1)).into(this.imageView1);
                    this.imageView2.setImageBitmap(this.bitmaps.get(0));
                } else if (this.imagePath.size() == 2) {
                    Picasso.get().load(new File(this.imagePath.get(0))).transform(new PicassoTransformation(this.imageView1)).into(this.imageView1);
                    Picasso.get().load(new File(this.imagePath.get(1))).transform(new PicassoTransformation(this.imageView2)).into(this.imageView2);
                } else {
                    this.imageView1.setImageBitmap(this.bitmaps.get(0));
                    this.imageView2.setImageBitmap(this.bitmaps.get(1));
                }
            } else if (size == 3) {
                this.noteImage.setVisibility(8);
                this.imageWrapper1.setVisibility(0);
                this.imageWrapper2.setVisibility(0);
                this.imageWrapper3.setVisibility(0);
                if (this.imagePath.size() == 1) {
                    Picasso.get().load(new File(this.imagePath.get(0))).transform(new PicassoTransformation(this.imageView1)).into(this.imageView1);
                    this.imageView2.setImageBitmap(this.bitmaps.get(0));
                    this.imageView3.setImageBitmap(this.bitmaps.get(1));
                } else if (this.imagePath.size() == 2) {
                    Picasso.get().load(new File(this.imagePath.get(0))).transform(new PicassoTransformation(this.imageView1)).into(this.imageView1);
                    Picasso.get().load(new File(this.imagePath.get(1))).transform(new PicassoTransformation(this.imageView2)).into(this.imageView2);
                    this.imageView3.setImageBitmap(this.bitmaps.get(0));
                } else if (this.imagePath.size() == 3) {
                    Picasso.get().load(new File(this.imagePath.get(0))).transform(new PicassoTransformation(this.imageView1)).into(this.imageView1);
                    Picasso.get().load(new File(this.imagePath.get(1))).transform(new PicassoTransformation(this.imageView2)).into(this.imageView2);
                    Picasso.get().load(new File(this.imagePath.get(2))).transform(new PicassoTransformation(this.imageView3)).into(this.imageView3);
                } else {
                    this.imageView1.setImageBitmap(this.bitmaps.get(0));
                    this.imageView2.setImageBitmap(this.bitmaps.get(1));
                    this.imageView3.setImageBitmap(this.bitmaps.get(2));
                }
            }
        }
    }

    private void deleteImage(int i) {
        if (this.imagePath.size() >= i + 1) {
            this.deletePath.add(this.imagePath.get(i));
            this.imagePath.remove(i);
        } else {
            this.bitmaps.remove(i - this.imagePath.size());
            this.bitmapPath.remove(i - this.imagePath.size());
        }
        displayImage();
    }

    private void editTransaction() {
        if (this.isComplete) {
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override

                public void run() {
                    int var5 = SharePreferenceHelper.getAccountId(EditTransaction.this.getApplicationContext());
                    AppDatabaseObject var11 = AppDatabaseObject.getInstance(EditTransaction.this.getApplicationContext());
                    WalletEntity var7;
                    WalletEntity var8;
                    AccountEntity var15;
                    AccountEntity var16;
                    List var18;
                    if (EditTransaction.this.trans.getType() == 2) {
                        var8 = var11.walletDaoObject().getWalletById(EditTransaction.this.trans.getWalletId());
                        var8.setAmount(var8.getAmount() + EditTransaction.this.trans.getAmount());
                        var11.walletDaoObject().updateWallet(var8);
                        var7 = var11.walletDaoObject().getWalletById(EditTransaction.this.trans.getTransferWalletId());
                        var7.setAmount(var7.getAmount() - EditTransaction.this.trans.getTransAmount());
                        var11.walletDaoObject().updateWallet(var7);
                        if (var8.getAccountId() == var7.getAccountId()) {
                            if (var8.getExclude() == 0 && var7.getExclude() == 1) {
                                var15 = var11.accountDaoObject().getEntityById(var5);
                                var15.setBalance(var15.getBalance() + EditTransaction.this.amount);
                                var11.accountDaoObject().updateAccount(var15);
                            } else if (var8.getExclude() == 1 && var7.getExclude() == 0) {
                                var15 = var11.accountDaoObject().getEntityById(var5);
                                var15.setBalance(var15.getBalance() - EditTransaction.this.amount);
                                var11.accountDaoObject().updateAccount(var15);
                            }
                        } else {
                            if (var8.getExclude() == 0) {
                                var16 = var11.accountDaoObject().getEntityById(var8.getAccountId());
                                var16.setBalance(var16.getBalance() + EditTransaction.this.trans.getAmount());
                                var11.accountDaoObject().updateAccount(var16);
                            }

                            if (var7.getExclude() == 0) {
                                var15 = var11.accountDaoObject().getEntityById(var7.getAccountId());
                                var15.setBalance(var15.getBalance() - EditTransaction.this.trans.getAmount());
                                var11.accountDaoObject().updateAccount(var15);
                            }
                        }

                        if (EditTransaction.this.trans.getFeeId() != 0) {
                            var11.transDaoObject().deleteTrans(EditTransaction.this.trans.getFeeId());
                        }
                    } else if (EditTransaction.this.trans.getType() == 1) {
                        var7 = var11.walletDaoObject().getWalletById(EditTransaction.this.trans.getWalletId());
                        var7.setAmount(var7.getAmount() - EditTransaction.this.trans.getAmount());
                        var11.walletDaoObject().updateWallet(var7);
                        if (var7.getExclude() == 0) {
                            var15 = var11.accountDaoObject().getEntityById(var5);
                            var15.setBalance(var15.getBalance() - EditTransaction.this.trans.getAmount());
                            var11.accountDaoObject().updateAccount(var15);
                        }

                        var18 = var11.budgetDaoObject().getBudgetIds(EditTransaction.this.trans.getCategoryId());
                        Iterator var17 = var11.budgetDaoObject().getBudgetEntityByCategory(var18, var5, 0).iterator();

                        while (var17.hasNext()) {
                            BudgetEntity var20 = (BudgetEntity) var17.next();
                            var20.setSpent(var20.getSpent() + EditTransaction.this.trans.getAmount());
                            var11.budgetDaoObject().updateBudget(var20);
                        }
                    } else {
                        var7 = var11.walletDaoObject().getWalletById(EditTransaction.this.trans.getWalletId());
                        var7.setAmount(var7.getAmount() - EditTransaction.this.trans.getAmount());
                        var11.walletDaoObject().updateWallet(var7);
                        if (var7.getExclude() == 0) {
                            var15 = var11.accountDaoObject().getEntityById(var5);
                            var15.setBalance(var15.getBalance() - EditTransaction.this.trans.getAmount());
                            var11.accountDaoObject().updateAccount(var15);
                        }
                    }

                    String var13 = EditTransaction.this.descriptionEditText.getText().toString().trim();
                    String var12 = EditTransaction.this.noteEditText.getText().toString().trim();
                    int var3 = EditTransaction.this.type;
                    int var4;
                    Iterator var28;
                    if (var3 != 0) {
                        if (var3 != 1) {
                            if (var3 == 2) {
                                if (EditTransaction.this.transAmount == 0L) {
                                    Iterator var14 = EditTransaction.this.walletList.iterator();
                                    Wallets var19 = null;
                                    Wallets var22 = null;

                                    while (var14.hasNext()) {
                                        Wallets var10 = (Wallets) var14.next();
                                        Wallets var9 = var19;
                                        if (EditTransaction.this.walletId == var10.getId()) {
                                            var9 = var10;
                                        }

                                        var19 = var9;
                                        if (EditTransaction.this.fromWalletId == var10.getId()) {
                                            var22 = var10;
                                            var19 = var9;
                                        }
                                    }

                                    float var1;
                                    float var2;
                                    if (var19 != null && var22 != null) {
                                        var2 = var19.getRate();
                                        var1 = var22.getRate();
                                    } else {
                                        var2 = 0.0F;
                                        var1 = 0.0F;
                                    }

                                    BigDecimal var24 = new BigDecimal((double) (var1 / var2));
                                    BigDecimal var21 = (new BigDecimal(EditTransaction.this.amount)).multiply(var24).setScale(0, 1).stripTrailingZeros();
                                    DecimalFormat var26 = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ENGLISH);
                                    var26.toLocalizedPattern();
                                    var26.setMaximumFractionDigits(0);
                                    var26.setMinimumFractionDigits(0);
                                    var26.setGroupingUsed(false);
                                    EditTransaction.this.transAmount = Long.parseLong(var26.format(var21));
                                }

                                var8 = var11.walletDaoObject().getWalletById(EditTransaction.this.fromWalletId);
                                var8.setAmount(var8.getAmount() - EditTransaction.this.amount);
                                var11.walletDaoObject().updateWallet(var8);
                                var7 = var11.walletDaoObject().getWalletById(EditTransaction.this.walletId);
                                var7.setAmount(var7.getAmount() + EditTransaction.this.transAmount);
                                var11.walletDaoObject().updateWallet(var7);
                                if (var8.getAccountId() == var7.getAccountId()) {
                                    if (var8.getExclude() == 0 && var7.getExclude() == 1) {
                                        var15 = var11.accountDaoObject().getEntityById(var5);
                                        var15.setBalance(var15.getBalance() - EditTransaction.this.amount);
                                        var11.accountDaoObject().updateAccount(var15);
                                    } else if (var8.getExclude() == 1 && var7.getExclude() == 0) {
                                        var15 = var11.accountDaoObject().getEntityById(var5);
                                        var15.setBalance(var15.getBalance() + EditTransaction.this.amount);
                                        var11.accountDaoObject().updateAccount(var15);
                                    }
                                } else {
                                    if (var8.getExclude() == 0) {
                                        var16 = var11.accountDaoObject().getEntityById(var8.getAccountId());
                                        var16.setBalance(var16.getBalance() - EditTransaction.this.amount);
                                        var11.accountDaoObject().updateAccount(var16);
                                    }

                                    if (var7.getExclude() == 0) {
                                        var15 = var11.accountDaoObject().getEntityById(var7.getAccountId());
                                        var15.setBalance(var15.getBalance() + EditTransaction.this.amount);
                                        var11.accountDaoObject().updateAccount(var15);
                                    }
                                }
                            }

                            var4 = 0;
                            var3 = 0;
                        } else {
                            var4 = EditTransaction.this.expenseCategoryId;
                            var3 = EditTransaction.this.expenseSubcategoryId;
                            var7 = var11.walletDaoObject().getWalletById(EditTransaction.this.walletId);
                            var7.setAmount(var7.getAmount() - EditTransaction.this.amount);
                            var11.walletDaoObject().updateWallet(var7);
                            if (var7.getExclude() == 0) {
                                var15 = var11.accountDaoObject().getEntityById(var5);
                                var15.setBalance(var15.getBalance() - EditTransaction.this.amount);
                                var11.accountDaoObject().updateAccount(var15);
                            }

                            var18 = var11.budgetDaoObject().getBudgetIds(var4);
                            var28 = var11.budgetDaoObject().getBudgetEntityByCategory(var18, var5, 0).iterator();

                            while (var28.hasNext()) {
                                BudgetEntity var23 = (BudgetEntity) var28.next();
                                var23.setSpent(var23.getSpent() + EditTransaction.this.amount);
                                var11.budgetDaoObject().updateBudget(var23);
                            }

                            EditTransaction.this.fromWalletId = -1;
                            EditTransaction.this.transAmount = 0L;
                            EditTransaction var29 = EditTransaction.this;
                            var29.amount = -var29.amount;
                        }
                    } else {
                        var4 = EditTransaction.this.incomeCategoryId;
                        var3 = EditTransaction.this.incomeSubcategoryId;
                        var7 = var11.walletDaoObject().getWalletById(EditTransaction.this.walletId);
                        var7.setAmount(var7.getAmount() + EditTransaction.this.amount);
                        var11.walletDaoObject().updateWallet(var7);
                        if (var7.getExclude() == 0) {
                            var15 = var11.accountDaoObject().getEntityById(var5);
                            var15.setBalance(var15.getBalance() + EditTransaction.this.amount);
                            var11.accountDaoObject().updateAccount(var15);
                        }

                        EditTransaction.this.fromWalletId = -1;
                        EditTransaction.this.transAmount = 0L;
                    }

                    int var6;
                    TransEntity var30;
                    if (EditTransaction.this.type != 1) {
                        var6 = var11.transDaoObject().getTransferTrans(EditTransaction.this.trans.getId());
                        if (var6 != 0) {
                            var30 = var11.transDaoObject().getTransEntityById(var6);
                            var30.setFeeId(0);
                            var11.transDaoObject().updateTrans(var30);
                        }
                    }

                    var30 = var11.transDaoObject().getTransEntityById(EditTransaction.this.transId);
                    var30.setNote(var13);
                    var30.setMemo(var12);
                    var30.setAmount(EditTransaction.this.amount);
                    var30.setDateTime(EditTransaction.this.date);
                    var30.setType(EditTransaction.this.type);
                    var30.setTransAmount(EditTransaction.this.transAmount);
                    var30.setFeeId(0);
                    if (EditTransaction.this.type == 2 && EditTransaction.this.switchView.isChecked() && EditTransaction.this.feeAmount > 0L) {
                        var6 = var11.categoryDaoObject().getFeeCategoryId(var5);
                        if (var6 != 0) {
                            java.util.Calendar var25 = java.util.Calendar.getInstance();
                            var25.setTime(EditTransaction.this.date);
                            var25.add(13, 1);
                            var30.setFeeId((int) var11.transDaoObject().insertTrans(new TransEntity(EditTransaction.this.getResources().getString(2131821191), "", -EditTransaction.this.feeAmount, var25.getTime(), 1, var5, var6, 0, EditTransaction.this.fromWalletId, 0, 0L, 0, 0, 0)));
                        }
                    }

                    if (EditTransaction.this.type == 2 || var4 != 0) {
                        var30.setCategoryId(var4);
                        var30.setSubcategoryId(var3);
                    }

                    if (EditTransaction.this.type == 2) {
                        var3 = EditTransaction.this.fromWalletId;
                    } else {
                        var3 = EditTransaction.this.walletId;
                    }

                    if (EditTransaction.this.type == 2) {
                        var4 = EditTransaction.this.walletId;
                    } else {
                        var4 = EditTransaction.this.fromWalletId;
                    }

                    var30.setTransferWalletId(var4);
                    var30.setWalletId(var3);
                    var11.transDaoObject().updateTrans(var30);

                    for (var3 = 0; var3 < EditTransaction.this.bitmapPath.size(); ++var3) {
                        var11.transDaoObject().insertMedia(new MediaEntity((String) EditTransaction.this.bitmapPath.get(var3), EditTransaction.this.transId));
                    }

                    var28 = EditTransaction.this.deletePath.iterator();

                    while (var28.hasNext()) {
                        String var27 = (String) var28.next();
                        (new File(var27)).delete();
                        var11.transDaoObject().deleteMedia(var27);
                    }

                    EditTransaction.this.runOnUiThread(EditTransaction.this::finish);
                }
            });
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            if (ActivityCompat.checkSelfPermission(this, "android.permission.READ_MEDIA_IMAGES") != 0) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_MEDIA_IMAGES")) {
                    showPermissionDialog(getResources().getString(R.string.access_storage_hint), getResources().getString(R.string.allow), getResources().getString(R.string.deny), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.EditTransaction.7
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(EditTransaction.this, new String[]{"android.permission.READ_MEDIA_IMAGES"}, 0);
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
                showPermissionDialog(getResources().getString(R.string.access_storage_hint), getResources().getString(R.string.allow), getResources().getString(R.string.deny), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.EditTransaction.8
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(EditTransaction.this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 0);
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
            showPermissionDialog(getResources().getString(R.string.access_storage_rational_hint), getResources().getString(R.string.app_settings), getResources().getString(R.string.not_now), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.EditTransaction.9
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.fromParts("package", EditTransaction.this.getPackageName(), null));
                    EditTransaction.this.startActivity(intent);
                }
            });
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
        } else {
            showPermissionDialog(getResources().getString(R.string.access_storage_rational_hint), getResources().getString(R.string.app_settings), getResources().getString(R.string.not_now), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.EditTransaction.10
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.fromParts("package", EditTransaction.this.getPackageName(), null));
                    EditTransaction.this.startActivity(intent);
                }
            });
        }
    }

    private void showPermissionDialog(String message, String positive, String negative, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle(getResources().getString(R.string.access_storage_title));
        builder.setPositiveButton(positive, listener);
        builder.setNegativeButton(negative, (DialogInterface.OnClickListener) null);
        builder.create().show();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        this.billingHelper.registerBroadCast(this);
        this.billingHelper.queryPurchases();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        this.billingHelper.unregisterBroadCast(this);
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
        adView.setAdUnitId(getResources().getString(R.string.ad_unit_edit_transaction));
        this.adView.addView(this.bannerView);
        this.bannerView.setAdSize(AdsHelper.getAdSize(this));
        this.bannerView.setAdListener(new AdListener() { // from class: com.ktwapps.walletmanager.EditTransaction.11
            @Override // com.google.android.gms.ads.AdListener
            public void onAdLoaded() {
                super.onAdLoaded();
                EditTransaction.this.adView.setVisibility(0);
            }
        });
        this.bannerView.loadAd(new AdRequest.Builder().build());
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
        builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.EditTransaction.12
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    BigDecimal stripTrailingZeros = new BigDecimal(EditTransaction.this.dialogSubEditText.getText().toString()).multiply(new BigDecimal(100)).setScale(0, 1).stripTrailingZeros();
                    DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ENGLISH);
                    decimalFormat.setGroupingUsed(false);
                    decimalFormat.setMaximumFractionDigits(0);
                    decimalFormat.setMinimumFractionDigits(0);
                    EditTransaction.this.transAmount = Long.parseLong(decimalFormat.format(stripTrailingZeros));
                    Wallets wallet = EditTransaction.this.getWallet();
                    Wallets fromWallet = EditTransaction.this.getFromWallet();
                    if (wallet == null || fromWallet == null) {
                        return;
                    }
                    String currency = wallet.getCurrency();
                    EditTransaction.this.rateLabel.setText(Helper.getBeautifyRate(fromWallet.getCurrency(), currency, EditTransaction.this.amount, EditTransaction.this.transAmount));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null);
        AlertDialog create = builder.create();
        this.dialog = create;
        create.show();
        this.dialogMainEditText = (EditText) this.dialog.findViewById(R.id.mainEditText);
        this.dialogMainLabel = (TextView) this.dialog.findViewById(R.id.mainLabel);
        this.dialogSubLabel = (TextView) this.dialog.findViewById(R.id.subLabel);
        this.dialogRateLabel = (TextView) this.dialog.findViewById(R.id.rateLabel);
        this.dialogSubEditText = (EditText) this.dialog.findViewById(R.id.subEditText);
        this.dialogRateEditText = (EditText) this.dialog.findViewById(R.id.rateEditText);
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
            BigDecimal stripTrailingZeros = new BigDecimal(this.transAmount / 100).divide(new BigDecimal(this.amount / 100), 9, RoundingMode.HALF_EVEN).setScale(9, 1).stripTrailingZeros();
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
        private EditText mEditText;

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
                    EditTransaction.this.dialogSubEditText.removeTextChangedListener(EditTransaction.this.subWatcher);
                    BigDecimal stripTrailingZeros = new BigDecimal(EditTransaction.this.amount / 100).multiply(new BigDecimal(EditTransaction.this.dialogRateEditText.getText().toString())).setScale(2, 1).stripTrailingZeros();
                    DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ENGLISH);
                    decimalFormat.setGroupingUsed(false);
                    decimalFormat.setMaximumFractionDigits(2);
                    decimalFormat.setMinimumFractionDigits(0);
                    EditTransaction.this.dialogSubEditText.setText(decimalFormat.format(stripTrailingZeros));
                    EditTransaction.this.dialogSubEditText.addTextChangedListener(EditTransaction.this.subWatcher);
                } else {
                    EditTransaction.this.dialogRateEditText.removeTextChangedListener(EditTransaction.this.rateWatcher);
                    BigDecimal stripTrailingZeros2 = new BigDecimal(EditTransaction.this.dialogSubEditText.getText().toString()).divide(new BigDecimal(EditTransaction.this.amount / 100), 9, RoundingMode.HALF_EVEN).setScale(9, 1).stripTrailingZeros();
                    DecimalFormat decimalFormat2 = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ENGLISH);
                    decimalFormat2.setGroupingUsed(false);
                    decimalFormat2.setMaximumFractionDigits(9);
                    decimalFormat2.setMinimumFractionDigits(0);
                    EditTransaction.this.dialogRateEditText.setText(decimalFormat2.format(stripTrailingZeros2));
                    EditTransaction.this.dialogRateEditText.addTextChangedListener(EditTransaction.this.rateWatcher);
                }
                EditTransaction.this.dialog.getButton(-1).setEnabled(true);
            } catch (NumberFormatException unused) {
                EditTransaction.this.dialog.getButton(-1).setEnabled(false);
            }
        }
    }
}
