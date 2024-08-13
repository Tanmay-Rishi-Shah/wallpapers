package com.expance.merapaisa;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.expance.merapaisa.Adapter.ViewPagerAdapter;
import com.expance.merapaisa.BackUp.BackUpPreference;
import com.expance.merapaisa.Broadcast.BroadcastUpdated;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Utility.AutoBackupWorker;
import com.expance.merapaisa.Utility.BillingHelper;
import com.expance.merapaisa.Utility.CalendarHelper;
import com.expance.merapaisa.Utility.Constant;
import com.expance.merapaisa.Utility.DateHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.NotificationHelper;
import com.expance.merapaisa.Utility.NotificationWorker;
import com.expance.merapaisa.Utility.RecurringWorker;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import com.expance.merapaisa.Utility.StatisticHelper;
import com.expance.merapaisa.Widget.BottomAccountPickerDialog;
import com.expance.merapaisa.Widget.BottomDateModeDialog;
import com.expance.merapaisa.Widget.NoneSwipeableViewPager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Stack;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import vocsy.ads.CustomAdsListener;
import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, BottomNavigationView.OnNavigationItemSelectedListener, BillingHelper.BillingListener, BottomDateModeDialog.OnItemClickListener, BroadcastUpdated.OnBroadcastListener {
    private boolean backEnable;
    private ImageView backImage;
    private Stack<Integer> backStack;
    private TextView balanceLabel;
    private ConstraintLayout balanceWrapper;
    private BillingHelper billingHelper;
    private BottomNavigationView bottomNavigationView;
    BroadcastUpdated broadcastUpdated;
    Date date;
    private TextView dateLabel;
    int dateMode;
    private ConstraintLayout dateWrapper;
    Date endDate;
    private EditText endEditText;
    private FloatingActionButton fab;
    Date holderEndDate;
    Date holderStartDate;
    private InterstitialAd interstitialAd;
    private boolean isAdsInit;
    private ImageView nextImage;
    Date startDate;
    private EditText startEditText;
    private TextView titleLabel;
    private ConstraintLayout toolbarWrapper;
    private NoneSwipeableViewPager viewPager;
    private boolean isToastShow = false;
    private boolean isPendingToastShow = false;

    private int changeBottomBar(int i) {
        return i != 1 ? i != 2 ? i != 3 ? R.id.action_transaction : R.id.action_wallet : R.id.action_statistic : R.id.action_calender;
    }

    @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
    public void onPageScrollStateChanged(int state) {
    }

    @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onSkuReady() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("startDate", this.startDate.getTime());
        outState.putLong("endDate", this.endDate.getTime());
        outState.putLong("date", this.date.getTime());
        outState.putInt("mode", this.dateMode);
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
        setContentView(R.layout.activity_main);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        MobileAds.initialize(this);
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        this.dateMode = getIntent().getIntExtra("mode", 2);
        Date time = calendar.getTime();
        this.date = time;
        this.startDate = CalendarHelper.getCustomInitialStartDate(time);
        this.endDate = CalendarHelper.getCustomInitialEndDate(this.date);
        if (savedInstanceState != null) {
            calendar.setTimeInMillis(savedInstanceState.getLong("date"));
            this.date = calendar.getTime();
            calendar.setTimeInMillis(savedInstanceState.getLong("startDate"));
            this.startDate = calendar.getTime();
            calendar.setTimeInMillis(savedInstanceState.getLong("endDate"));
            this.endDate = calendar.getTime();
            this.dateMode = savedInstanceState.getInt("mode");
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add("344EDCD2CBEF665536D6543B4EDF8E79");
        arrayList.add("B3EEABB8EE11C2BE770B684D95219ECB");
        arrayList.add("4C7649FBDD3333A4F2A48F80A6FC9B7D");
        arrayList.add("77E632844A2DFA1B80102222BA459641");
        MobileAds.setRequestConfiguration(new RequestConfiguration.Builder().setTestDeviceIds(arrayList).build());
        initData();
        initWorker();
        setUpLayout();
        setUpShortcut();
        BroadcastUpdated broadcastUpdated = new BroadcastUpdated();
        this.broadcastUpdated = broadcastUpdated;
        broadcastUpdated.setListener(this);
        BillingHelper billingHelper = new BillingHelper(this);
        this.billingHelper = billingHelper;
        billingHelper.setListener(this);
        this.billingHelper.setUpBillingClient();
        getBalance();
        if (shouldShowRating()) {
            showRatingDialog(false);
        }
        if (Build.VERSION.SDK_INT >= 33 && ContextCompat.checkSelfPermission(this, "android.permission.POST_NOTIFICATIONS") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.POST_NOTIFICATIONS"}, 0);
        }
        checkAutoBackup();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        checkAutoBackup();
        super.onDestroy();
    }

    private void checkAutoBackup() {
        if (SharePreferenceHelper.checkAccountKey(this) && BackUpPreference.getAutoBackup(this) == 1 && GoogleSignIn.getLastSignedInAccount(this) != null) {
            WorkManager workManager = WorkManager.getInstance(this);
            workManager.cancelAllWorkByTag(Constant.BACKUP_WORKER);
            workManager.enqueue(new OneTimeWorkRequest.Builder(AutoBackupWorker.class).addTag(Constant.BACKUP_WORKER).build());
        }
    }

    private void setUpShortcut() {
        if (Build.VERSION.SDK_INT >= 25) {
            try {
                ShortcutInfo build = new ShortcutInfo.Builder(this, "compose").setShortLabel(getResources().getString(R.string.create_transaction)).setIcon(Icon.createWithResource(this, (int) R.drawable.shortcut_create)).setIntents(new Intent[]{new Intent(this, BaseActivity.class).setAction("shortcut")}).build();
                ArrayList arrayList = new ArrayList();
                arrayList.add(build);
                ((ShortcutManager) getSystemService(ShortcutManager.class)).setDynamicShortcuts(Collections.unmodifiableList(arrayList));
            } catch (Exception unused) {
            }
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

    private void initData() {
        this.backStack = new Stack<>();
        this.backEnable = true;
    }

    private void initWorker() {
        WorkManager workManager = WorkManager.getInstance(this);
        workManager.cancelAllWorkByTag(Constant.DAILY_WORKER);
        workManager.enqueue(new OneTimeWorkRequest.Builder(RecurringWorker.class).addTag(Constant.DAILY_WORKER).build());
        workManager.cancelAllWorkByTag(Constant.NOTIFICATION_WORKER);
        workManager.enqueue(new OneTimeWorkRequest.Builder(NotificationWorker.class).addTag(Constant.NOTIFICATION_WORKER).setInitialDelay(NotificationHelper.getMilliseconds(getApplicationContext()), TimeUnit.MILLISECONDS).build());
    }

    private void setUpLayout() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        this.bottomNavigationView = bottomNavigationView;
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        this.balanceWrapper = (ConstraintLayout) findViewById(R.id.balanceWrapper);
        this.balanceLabel = (TextView) findViewById(R.id.balanceLabel);
        this.fab = (FloatingActionButton) findViewById(R.id.fab);
        this.toolbarWrapper = (ConstraintLayout) findViewById(R.id.toolbarWrapper);
        this.backImage = (ImageView) findViewById(R.id.backImage);
        this.nextImage = (ImageView) findViewById(R.id.nextImage);
        this.dateLabel = (TextView) findViewById(R.id.dateLabel);
        this.dateWrapper = (ConstraintLayout) findViewById(R.id.dateWrapper);
        this.viewPager = (NoneSwipeableViewPager) findViewById(R.id.viewPager);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        this.titleLabel = textView;
        textView.setText(SharePreferenceHelper.getAccountName(getApplicationContext()));
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        this.viewPager.setOffscreenPageLimit(4);
        this.viewPager.setAdapter(viewPagerAdapter);
        this.viewPager.addOnPageChangeListener(this);
        int startUpScreen = SharePreferenceHelper.getStartUpScreen(this);
        this.viewPager.setCurrentItem(startUpScreen);
        this.bottomNavigationView.setSelectedItemId(changeBottomBar(startUpScreen));
        ((ConstraintLayout) findViewById(R.id.titleWrapper)).setOnClickListener(this);
        this.fab.setOnClickListener(this);
        this.backImage.setOnClickListener(this);
        this.nextImage.setOnClickListener(this);
        long pieStartDate = StatisticHelper.getPieStartDate(getApplicationContext(), this.date, this.dateMode);
        long pieEndDate = StatisticHelper.getPieEndDate(getApplicationContext(), this.date, this.dateMode);
        if ((pieStartDate == 0 && pieEndDate == 0) || this.dateMode == 6) {
            this.backImage.setAlpha(0.25f);
            this.nextImage.setAlpha(0.25f);
            int i = this.dateMode;
            if (i == 6) {
                this.dateLabel.setText(CalendarHelper.getFormattedCustomDate(this, this.startDate, this.endDate));
                return;
            } else {
                this.dateLabel.setText(CalendarHelper.getPieFormattedDate(this, this.date, i));
                return;
            }
        }
        this.backImage.setAlpha(1.0f);
        this.nextImage.setAlpha(1.0f);
        this.dateLabel.setText(CalendarHelper.getPieFormattedDate(this, this.date, this.dateMode));
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        Statistic statistic;
        if (view.getId() == R.id.backImage) {
            int i = this.dateMode;
            if (i == 5 || i == 6) {
                return;
            }
            java.util.Calendar.getInstance().setTime(this.date);
            Date incrementPieDate = StatisticHelper.incrementPieDate(this.date, this.dateMode, -1);
            this.date = incrementPieDate;
            this.dateLabel.setText(CalendarHelper.getPieFormattedDate(this, incrementPieDate, this.dateMode));
            long pieStartDate = StatisticHelper.getPieStartDate(getApplicationContext(), this.date, this.dateMode);
            long pieEndDate = StatisticHelper.getPieEndDate(getApplicationContext(), this.date, this.dateMode);
            if (getSupportFragmentManager().getFragments().size() >= 3) {
                Iterator<Fragment> it = getSupportFragmentManager().getFragments().iterator();
                while (it.hasNext()) {
                    Fragment next = it.next();
                    statistic = next instanceof Statistic ? (Statistic) next : null;
                    if (statistic != null) {
                        statistic.populateData(pieStartDate, pieEndDate);
                        return;
                    }
                }
            }
        } else if (view.getId() == R.id.endDateEditText) {
            new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() { // from class: com.ktwapps.walletmanager.MainActivity.2
                @Override // android.app.DatePickerDialog.OnDateSetListener
                public void onDateSet(DatePicker datePicker, int i2, int i1, int i22) {
                    MainActivity.this.holderEndDate = CalendarHelper.getDateFromPicker(i2, i1, i22);
                    MainActivity.this.endEditText.setText(DateHelper.getFormattedDate(MainActivity.this.getApplicationContext(), MainActivity.this.holderEndDate));
                    if (DateHelper.isBeforeDate(MainActivity.this.holderStartDate, MainActivity.this.holderEndDate)) {
                        MainActivity.this.holderStartDate = CalendarHelper.getDateFromPicker(i2, i1, i22);
                        MainActivity.this.startEditText.setText(DateHelper.getFormattedDate(MainActivity.this.getApplicationContext(), MainActivity.this.holderStartDate));
                    }
                }
            }, CalendarHelper.getYearFromDate(this.holderEndDate), CalendarHelper.getMonthFromDate(this.holderEndDate), CalendarHelper.getDayFromDate(this.holderEndDate)).show();
        } else if (view.getId() == R.id.fab) {
            GoogleAds.getInstance().showCounterInterstitialAd(MainActivity.this, new CustomAdsListener() {
                @Override
                public void onFinish() {
                    startActivity(new Intent(getApplicationContext(), CreateTransaction.class));
                    overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
                }
            });

        } else if (view.getId() == R.id.nextImage) {
            int i2 = this.dateMode;
            if (i2 == 5 || i2 == 6) {
                return;
            }
            java.util.Calendar.getInstance().setTime(this.date);
            Date incrementPieDate2 = StatisticHelper.incrementPieDate(this.date, this.dateMode, 1);
            this.date = incrementPieDate2;
            this.dateLabel.setText(CalendarHelper.getPieFormattedDate(this, incrementPieDate2, this.dateMode));
            long pieStartDate2 = StatisticHelper.getPieStartDate(getApplicationContext(), this.date, this.dateMode);
            long pieEndDate2 = StatisticHelper.getPieEndDate(getApplicationContext(), this.date, this.dateMode);
            if (getSupportFragmentManager().getFragments().size() >= 3) {
                Iterator<Fragment> it2 = getSupportFragmentManager().getFragments().iterator();
                while (it2.hasNext()) {
                    Fragment next2 = it2.next();
                    statistic = next2 instanceof Statistic ? (Statistic) next2 : null;
                    if (statistic != null) {
                        statistic.populateData(pieStartDate2, pieEndDate2);
                        break;
                    }
                }
            }
        } else if (view.getId() == R.id.startDateEditText) {
            new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() { // from class: com.ktwapps.walletmanager.MainActivity.1
                @Override // android.app.DatePickerDialog.OnDateSetListener
                public void onDateSet(DatePicker datePicker, int i3, int i1, int i22) {
                    MainActivity.this.holderStartDate = CalendarHelper.getDateFromPicker(i3, i1, i22);
                    MainActivity.this.startEditText.setText(DateHelper.getFormattedDate(MainActivity.this.getApplicationContext(), MainActivity.this.holderStartDate));
                    if (DateHelper.isBeforeDate(MainActivity.this.holderStartDate, MainActivity.this.holderEndDate)) {
                        MainActivity.this.holderEndDate = CalendarHelper.getDateFromPicker(i3, i1, i22);
                        MainActivity.this.endEditText.setText(DateHelper.getFormattedDate(MainActivity.this.getApplicationContext(), MainActivity.this.holderEndDate));
                    }
                }
            }, CalendarHelper.getYearFromDate(this.holderStartDate), CalendarHelper.getMonthFromDate(this.holderStartDate), CalendarHelper.getDayFromDate(this.holderStartDate)).show();
        } else if (view.getId() == R.id.titleWrapper) {
            new BottomAccountPickerDialog().show(getSupportFragmentManager(), "accountPicker");
        }
    }

    @Override // com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener
    public boolean onNavigationItemSelected(MenuItem item) {
        int i = 0;
        if (item.getItemId() == R.id.action_calender) {
            i = 1;
        } else if (item.getItemId() == R.id.action_statistic) {
            i = 2;
        } else if (item.getItemId() == R.id.action_transaction) {
            i = 0;
        } else if (item.getItemId() == R.id.action_wallet) {
            i = 3;
        }
        if (this.viewPager.getCurrentItem() != i) {
            if (this.backEnable) {
                Integer valueOf = Integer.valueOf(this.viewPager.getCurrentItem());
                if (this.backStack.contains(valueOf)) {
                    this.backStack.remove(valueOf);
                }
                this.backStack.push(valueOf);
            }
            this.viewPager.setCurrentItem(i, false);
            invalidateOptionsMenu();

        }
        return true;
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (this.backStack.isEmpty()) {
            if (shouldShowRating()) {
                showRatingDialog(true);
                return;
            } else {
                startActivity(new Intent(MainActivity.this,GetStart.class));
                return;
            }
        }
        this.backEnable = false;
        Integer pop = this.backStack.pop();
        this.viewPager.setCurrentItem(pop.intValue(), false);
        this.bottomNavigationView.setSelectedItemId(changeBottomBar(pop.intValue()));
        this.backEnable = true;
        invalidateOptionsMenu();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 16) {
            this.interstitialAd = null;
        }
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_action_date_mode) {
            BottomDateModeDialog bottomDateModeDialog = new BottomDateModeDialog();
            bottomDateModeDialog.setMode(this.dateMode);
            bottomDateModeDialog.setListener(this);
            bottomDateModeDialog.show(getSupportFragmentManager(), "date_mode");
            return true;
        } else if (itemId == R.id.menu_action_weekly_spending) {
            startActivity(new Intent(getApplicationContext(), StatisticWeekly.class));
            overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
            return true;
        } else {
            if (itemId == R.id.menu_action_list) {
                startActivity(new Intent(getApplicationContext(), TransactionOverview.class));
                overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
                return true;
            } else if (itemId == R.id.menu_action_pro) {
                startActivity(new Intent(getApplicationContext(), Premium.class));
                overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
                return true;
            } else if (itemId == R.id.menu_action_search) {
                startActivity(new Intent(getApplicationContext(), Search.class));
                overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
                return true;
            } else if (itemId == R.id.menu_action_setting) {
                startActivityForResult(new Intent(getApplicationContext(), Setting.class), 16);
                overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
                return true;
            } else {
                return true;
            }
        }
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        int currentItem = this.viewPager.getCurrentItem();
        int i = 0;
        if (currentItem != 0) {
            if (currentItem != 1) {
                if (currentItem != 2) {
                    if (currentItem == 3) {
                        if (SharePreferenceHelper.getPremium(this) == 2) {
                            getMenuInflater().inflate(R.menu.menu_wallet, menu);
                            while (i < menu.size()) {
                                Drawable icon = menu.getItem(i).getIcon();
                                if (icon != null) {
                                    icon.mutate();
                                    icon.setColorFilter(Helper.getAttributeColor(this, R.attr.primaryDarkTextColor), PorterDuff.Mode.SRC_ATOP);
                                }
                                i++;
                            }
                        } else {
                            getMenuInflater().inflate(R.menu.menu_wallet_premium, menu);
                        }
                    }
                } else if (SharePreferenceHelper.getPremium(this) == 2) {
                    getMenuInflater().inflate(R.menu.menu_statistic, menu);
                    while (i < menu.size()) {
                        Drawable icon2 = menu.getItem(i).getIcon();
                        if (icon2 != null) {
                            icon2.mutate();
                            icon2.setColorFilter(Helper.getAttributeColor(this, R.attr.primaryDarkTextColor), PorterDuff.Mode.SRC_ATOP);
                        }
                        i++;
                    }
                } else {
                    getMenuInflater().inflate(R.menu.menu_statistic_premium, menu);
                }
            } else if (SharePreferenceHelper.getPremium(this) == 2) {
                getMenuInflater().inflate(R.menu.menu_main, menu);
                while (i < menu.size()) {
                    Drawable icon3 = menu.getItem(i).getIcon();
                    if (icon3 != null) {
                        icon3.mutate();
                        icon3.setColorFilter(Helper.getAttributeColor(this, R.attr.primaryDarkTextColor), PorterDuff.Mode.SRC_ATOP);
                    }
                    i++;
                }
            } else {
                getMenuInflater().inflate(R.menu.menu_main_premium, menu);
            }
        } else if (SharePreferenceHelper.getPremium(this) == 2) {
            getMenuInflater().inflate(R.menu.menu_transaction, menu);
            while (i < menu.size()) {
                Drawable icon4 = menu.getItem(i).getIcon();
                if (icon4 != null) {
                    icon4.mutate();
                    icon4.setColorFilter(Helper.getAttributeColor(this, R.attr.primaryDarkTextColor), PorterDuff.Mode.SRC_ATOP);
                }
                i++;
            }
        } else {
            getMenuInflater().inflate(R.menu.menu_transaction_premium, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
    public void onPageSelected(int position) {
        if (position == 0) {
            this.fab.show();
            this.balanceWrapper.setVisibility(0);
        } else {
            this.fab.hide();
            this.balanceWrapper.setVisibility(8);
        }
        if (position == 2) {
            this.dateWrapper.setVisibility(0);
        } else {
            this.dateWrapper.setVisibility(8);
        }
        if (position == 1) {
            this.toolbarWrapper.setElevation(Helper.convertDpToPixel(this, 0.0f));
        } else {
            this.toolbarWrapper.setElevation(Helper.convertDpToPixel(this, 4.0f));
        }
    }

    public void showAds() {
        InterstitialAd interstitialAd;
        Constant.popUpAds++;
        if (Constant.popUpAds < 10 || (interstitialAd = this.interstitialAd) == null) {
            return;
        }
        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() { // from class: com.ktwapps.walletmanager.MainActivity.3
            @Override // com.google.android.gms.ads.FullScreenContentCallback
            public void onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent();
                Constant.popUpAds = 0;
                MainActivity.this.interstitialAd = null;
                MainActivity.this.loadInterstitialAds();
            }
        });
        this.interstitialAd.show(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadInterstitialAds() {
        InterstitialAd.load(this, "ca-app-pub-1062315604133356/3484735702", new AdRequest.Builder().build(), new InterstitialAdLoadCallback() { // from class: com.ktwapps.walletmanager.MainActivity.4
            @Override // com.google.android.gms.ads.AdLoadCallback
            public void onAdLoaded(InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                MainActivity.this.interstitialAd = interstitialAd;
            }

            @Override // com.google.android.gms.ads.AdLoadCallback
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                MainActivity.this.interstitialAd = null;
            }
        });
    }

    private void initAds() {
        if (this.isAdsInit) {
            return;
        }
        this.isAdsInit = true;
        loadInterstitialAds();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        this.billingHelper.registerBroadCast(this);
        this.billingHelper.queryPurchases();
        getBalance();
        LocalBroadcastManager.getInstance(this).registerReceiver(this.broadcastUpdated, new IntentFilter(Constant.BROADCAST_UPDATED));
    }

    private void getBalance() {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.MainActivity.5
            @Override // java.lang.Runnable
            public void run() {
                java.util.Calendar calendar = java.util.Calendar.getInstance();
                calendar.set(14, 0);
                calendar.set(13, 0);
                calendar.set(12, 0);
                calendar.set(11, 0);
                calendar.add(5, 1);
                if (!SharePreferenceHelper.isFutureBalanceOn(MainActivity.this)) {
                    calendar.set(1, 10000);
                }
                Long accountBalance = AppDatabaseObject.getInstance(MainActivity.this.getApplicationContext()).accountDaoObject().getAccountBalance(SharePreferenceHelper.getAccountId(MainActivity.this.getApplicationContext()), 0, calendar.getTimeInMillis());
                if (accountBalance == null) {
                    accountBalance = 0L;
                }
                final long longValue = accountBalance.longValue();
                MainActivity.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.MainActivity.5.1
                    @Override // java.lang.Runnable
                    public void run() {
                        MainActivity.this.balanceLabel.setText(MainActivity.this.getResources().getString(R.string.balance) + ": " + Helper.getBeautifyAmount(SharePreferenceHelper.getAccountSymbol(MainActivity.this.getApplicationContext()), longValue));
                    }
                });
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        this.billingHelper.unregisterBroadCast(this);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.broadcastUpdated);
    }

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onLoaded() {
        checkSubscription();
    }

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onPurchasedSucceed() {
        if (!this.isToastShow) {
            this.isToastShow = true;
            Toast.makeText(this, (int) R.string.premium_subscribed, 1).show();
        }
        checkSubscription();
    }

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onPurchasedPending() {
        if (!this.isPendingToastShow) {
            this.isPendingToastShow = true;
            Toast.makeText(this, (int) R.string.premium_pending, 1).show();
        }
        checkSubscription();
    }

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onReceiveBroadCast() {
        checkSubscription();
    }

    private void checkSubscription() {
        if (SharePreferenceHelper.getPremium(this) == 2) {
            this.interstitialAd = null;
        } else {
            initAds();
        }
        invalidateOptionsMenu();
    }

    @Override // com.ktwapps.walletmanager.Widget.BottomDateModeDialog.OnItemClickListener
    public void onItemClick(int mode) {
        Statistic statistic = null;
        if (mode == 6) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_custom_date, (ViewGroup) null);
            this.startEditText = (EditText) inflate.findViewById(R.id.startDateEditText);
            this.endEditText = (EditText) inflate.findViewById(R.id.endDateEditText);
            this.startEditText.setText(DateHelper.getFormattedDate(this, this.startDate));
            this.endEditText.setText(DateHelper.getFormattedDate(this, this.endDate));
            this.startEditText.setFocusable(false);
            this.startEditText.setLongClickable(false);
            this.startEditText.setOnClickListener(this);
            this.endEditText.setFocusable(false);
            this.endEditText.setLongClickable(false);
            this.endEditText.setOnClickListener(this);
            this.holderStartDate = this.startDate;
            this.holderEndDate = this.endDate;
            builder.setView(inflate);
            builder.setTitle(R.string.select_date);
            builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.MainActivity.6
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    Statistic statistic2;
                    MainActivity mainActivity = MainActivity.this;
                    mainActivity.startDate = mainActivity.holderStartDate;
                    MainActivity mainActivity2 = MainActivity.this;
                    mainActivity2.endDate = mainActivity2.holderEndDate;
                    MainActivity.this.dateLabel.setText(CalendarHelper.getFormattedCustomDate(MainActivity.this.getApplicationContext(), MainActivity.this.startDate, MainActivity.this.endDate));
                    long customStartDate = CalendarHelper.getCustomStartDate(MainActivity.this.startDate);
                    long customEndDate = CalendarHelper.getCustomEndDate(MainActivity.this.endDate);
                    if (MainActivity.this.getSupportFragmentManager().getFragments().size() >= 3) {
                        Iterator<Fragment> it = MainActivity.this.getSupportFragmentManager().getFragments().iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                statistic2 = null;
                                break;
                            }
                            Fragment next = it.next();
                            if (next instanceof Statistic) {
                                statistic2 = (Statistic) next;
                                break;
                            }
                        }
                        if (statistic2 != null) {
                            statistic2.populateData(customStartDate, customEndDate);
                        }
                    }
                    MainActivity.this.dateMode = 6;
                    MainActivity.this.backImage.setAlpha(0.25f);
                    MainActivity.this.nextImage.setAlpha(0.25f);
                }
            });
            builder.setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null);
            builder.show();
            return;
        }
        this.dateMode = mode;
        this.dateLabel.setText(CalendarHelper.getPieFormattedDate(this, this.date, mode));
        long pieStartDate = StatisticHelper.getPieStartDate(getApplicationContext(), this.date, mode);
        long pieEndDate = StatisticHelper.getPieEndDate(getApplicationContext(), this.date, mode);
        if (pieStartDate == 0 && pieEndDate == 0) {
            this.backImage.setAlpha(0.25f);
            this.nextImage.setAlpha(0.25f);
        } else {
            this.backImage.setAlpha(1.0f);
            this.nextImage.setAlpha(1.0f);
        }
        if (getSupportFragmentManager().getFragments().size() >= 3) {
            Iterator<Fragment> it = getSupportFragmentManager().getFragments().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Fragment next = it.next();
                if (next instanceof Statistic) {
                    statistic = (Statistic) next;
                    break;
                }
            }
            if (statistic != null) {
                statistic.populateData(pieStartDate, pieEndDate);
            }
        }
    }

    @Override // com.ktwapps.walletmanager.Broadcast.BroadcastUpdated.OnBroadcastListener
    public void getBroadcast() {
        getBalance();
    }

    private void showRatingDialog(final boolean isExit) {
        View inflate = LayoutInflater.from(new ContextThemeWrapper(this, (int) R.style.Theme_Rating_Dialog)).inflate(R.layout.dialog_rating, (ViewGroup) null);
        final RatingBar ratingBar = (RatingBar) inflate.findViewById(R.id.ratingBar);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, (int) R.style.Theme_Rating_Dialog));
        builder.setView(inflate);
        final AlertDialog show = builder.show();
        ((ConstraintLayout) inflate.findViewById(R.id.firstActionWrapper)).setOnClickListener(new View.OnClickListener() { // from class: com.ktwapps.walletmanager.MainActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.m209lambda$showRatingDialog$0$comktwappswalletmanagerMainActivity(show, isExit, view);
            }
        });
        ((ConstraintLayout) inflate.findViewById(R.id.secondActionWrapper)).setOnClickListener(new View.OnClickListener() { // from class: com.ktwapps.walletmanager.MainActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MainActivity.this.m210lambda$showRatingDialog$1$comktwappswalletmanagerMainActivity(show, ratingBar, isExit, view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$showRatingDialog$0$com-ktwapps-walletmanager-MainActivity  reason: not valid java name */
    public /* synthetic */ void m209lambda$showRatingDialog$0$comktwappswalletmanagerMainActivity(Dialog dialog, boolean z, View view) {
        dialog.dismiss();
        if (z) {
            super.onBackPressed();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$showRatingDialog$1$com-ktwapps-walletmanager-MainActivity  reason: not valid java name */
    public /* synthetic */ void m210lambda$showRatingDialog$1$comktwappswalletmanagerMainActivity(Dialog dialog, RatingBar ratingBar, boolean z, View view) {
        dialog.dismiss();
        SharedPreferences.Editor edit = getSharedPreferences("PREF_FILE", 0).edit();
        edit.putInt("rating", -1);
        edit.apply();
        if (ratingBar.getRating() >= 4.0f || ratingBar.getRating() == 0.0f) {
            try {
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, (int) R.string.rating_feedback, 0).show();
        }
        if (z) {
            super.onBackPressed();
        }
    }

    private boolean shouldShowRating() {
        int i = getSharedPreferences("PREF_FILE", 0).getInt("rating", 8);
        if (i == 10) {
            SharedPreferences.Editor edit = getSharedPreferences("PREF_FILE", 0).edit();
            edit.putInt("rating", 0);
            edit.apply();
        } else if (i != -1) {
            SharedPreferences.Editor edit2 = getSharedPreferences("PREF_FILE", 0).edit();
            edit2.putInt("rating", i + 1);
            edit2.apply();
        }
        return i == 10;
    }
}
