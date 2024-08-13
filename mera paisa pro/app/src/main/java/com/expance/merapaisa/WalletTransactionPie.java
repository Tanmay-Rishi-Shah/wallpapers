package com.expance.merapaisa;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.expance.merapaisa.Adapter.TransactionPieAdapter;
import com.expance.merapaisa.Broadcast.BroadcastUpdated;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Model.DailyTrans;
import com.expance.merapaisa.Model.Recurring;
import com.expance.merapaisa.Model.RecurringCalendarDate;
import com.expance.merapaisa.Model.Trans;
import com.expance.merapaisa.Model.TransList;
import com.expance.merapaisa.Utility.CalendarHelper;
import com.expance.merapaisa.Utility.Constant;
import com.expance.merapaisa.Utility.DateHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.RecurringHelper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import com.expance.merapaisa.Utility.StatisticHelper;
import com.expance.merapaisa.Utility.TransactionHelper;
import com.expance.merapaisa.Widget.BottomDateModeDialog;

import org.apache.http.cookie.ClientCookie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;

import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class WalletTransactionPie extends AppCompatActivity implements View.OnClickListener, BottomDateModeDialog.OnItemClickListener, TransactionPieAdapter.OnItemClickListener, BroadcastUpdated.OnBroadcastListener {
    TransactionPieAdapter adapter;
    ImageView backImage;
    BroadcastUpdated broadcastUpdated;
    int categoryId;
    Date date;
    TextView dateLabel;
    ConstraintLayout emptyWrapper;
    Date endDate;
    private EditText endEditText;
    Date holderEndDate;
    Date holderStartDate;
    String name;
    ImageView nextImage;
    RecyclerView recyclerView;
    Date startDate;
    private EditText startEditText;
    String symbol;
    int walletId;
    int mode = 2;
    int transMode = 1;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("startDate", this.startDate.getTime());
        outState.putLong("endDate", this.endDate.getTime());
        outState.putLong("date", this.date.getTime());
        outState.putInt("mode", this.mode);
        outState.putInt("transMode", this.transMode);
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
        setContentView(R.layout.activity_wallet_transaction_pie);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        BroadcastUpdated broadcastUpdated = new BroadcastUpdated();
        this.broadcastUpdated = broadcastUpdated;
        broadcastUpdated.setListener(this);
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeInMillis(getIntent().getLongExtra("date", java.util.Calendar.getInstance().getTimeInMillis()));
        this.date = calendar.getTime();
        calendar.setTimeInMillis(getIntent().getLongExtra("startDate", java.util.Calendar.getInstance().getTimeInMillis()));
        this.startDate = calendar.getTime();
        calendar.setTimeInMillis(getIntent().getLongExtra("endDate", java.util.Calendar.getInstance().getTimeInMillis()));
        this.endDate = calendar.getTime();
        this.name = getIntent().getStringExtra("name");
        this.categoryId = getIntent().getIntExtra("categoryId", 0);
        this.walletId = getIntent().getIntExtra("walletId", this.walletId);
        this.symbol = getIntent().getStringExtra("symbol");
        this.mode = getIntent().getIntExtra("mode", 2);
        this.transMode = getIntent().getIntExtra("transMode", 1);
        if (savedInstanceState != null) {
            calendar.setTimeInMillis(savedInstanceState.getLong("date"));
            this.date = calendar.getTime();
            calendar.setTimeInMillis(savedInstanceState.getLong("startDate"));
            this.startDate = calendar.getTime();
            calendar.setTimeInMillis(savedInstanceState.getLong("endDate"));
            this.endDate = calendar.getTime();
            this.mode = savedInstanceState.getInt("mode");
            this.transMode = savedInstanceState.getInt("transMode");
        }
        TransactionPieAdapter transactionPieAdapter = new TransactionPieAdapter(this);
        this.adapter = transactionPieAdapter;
        transactionPieAdapter.setListener(this);
        this.adapter.setSymbol(this.symbol);
        this.adapter.setMode(this.transMode);
        setUpLayout();
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
        long pieStartDate;
        long pieEndDate;
        super.onResume();
        int i = this.mode;
        if (i == 6) {
            pieStartDate = CalendarHelper.getCustomStartDate(this.startDate);
            pieEndDate = CalendarHelper.getCustomEndDate(this.endDate);
        } else {
            pieStartDate = StatisticHelper.getPieStartDate(this, this.date, i);
            pieEndDate = StatisticHelper.getPieEndDate(this, this.date, this.mode);
        }
        populateData(pieStartDate, pieEndDate);
        LocalBroadcastManager.getInstance(this).registerReceiver(this.broadcastUpdated, new IntentFilter(Constant.BROADCAST_UPDATED));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.broadcastUpdated);
    }

    private void setUpLayout() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(this.name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.backImage = (ImageView) findViewById(R.id.backImage);
        this.nextImage = (ImageView) findViewById(R.id.nextImage);
        this.dateLabel = (TextView) findViewById(R.id.dateLabel);
        this.emptyWrapper = (ConstraintLayout) findViewById(R.id.emptyWrapper);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setAdapter(this.adapter);
        this.backImage.setOnClickListener(this);
        this.nextImage.setOnClickListener(this);
        long pieStartDate = StatisticHelper.getPieStartDate(getApplicationContext(), this.date, this.mode);
        long pieEndDate = StatisticHelper.getPieEndDate(getApplicationContext(), this.date, this.mode);
        if ((pieStartDate == 0 && pieEndDate == 0) || this.mode == 6) {
            this.backImage.setAlpha(0.25f);
            this.nextImage.setAlpha(0.25f);
            int i = this.mode;
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
        this.dateLabel.setText(CalendarHelper.getPieFormattedDate(this, this.date, this.mode));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void populateData(final long startDate, final long endDate) {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.WalletTransactionPie.1
            @Override // java.lang.Runnable
            public void run() {
                List<DailyTrans> pieTrans;
                AppDatabaseObject appDatabaseObject;
                DailyTrans dailyTrans;
                boolean z;
                ArrayList<Trans> arrayList;
                List<Trans> pieTransFromDate;
                boolean z2;
                boolean z3;
                AppDatabaseObject appDatabaseObject2 = AppDatabaseObject.getInstance(WalletTransactionPie.this.getApplicationContext());
                int accountId = SharePreferenceHelper.getAccountId(WalletTransactionPie.this.getApplicationContext());
                final ArrayList arrayList2 = new ArrayList();
                boolean z4 = true;
                if (startDate == 0 && endDate == 0) {
                    if (WalletTransactionPie.this.categoryId == 0) {
                        if (WalletTransactionPie.this.transMode == 1) {
                            pieTrans = appDatabaseObject2.walletDaoObject().getAllPieExpenseTransferTrans(accountId, WalletTransactionPie.this.walletId);
                        } else {
                            pieTrans = appDatabaseObject2.walletDaoObject().getAllPieIncomeTransferTrans(accountId, WalletTransactionPie.this.walletId);
                        }
                    } else {
                        pieTrans = appDatabaseObject2.walletDaoObject().getAllPieTrans(accountId, WalletTransactionPie.this.walletId, WalletTransactionPie.this.categoryId, WalletTransactionPie.this.transMode);
                    }
                } else if (WalletTransactionPie.this.categoryId == 0) {
                    if (WalletTransactionPie.this.transMode == 1) {
                        pieTrans = appDatabaseObject2.walletDaoObject().getPieExpenseTransferTrans(accountId, WalletTransactionPie.this.walletId, startDate, endDate);
                    } else {
                        pieTrans = appDatabaseObject2.walletDaoObject().getPieIncomeTransferTrans(accountId, WalletTransactionPie.this.walletId, startDate, endDate);
                    }
                } else {
                    pieTrans = appDatabaseObject2.walletDaoObject().getPieTrans(accountId, WalletTransactionPie.this.walletId, WalletTransactionPie.this.categoryId, startDate, endDate, WalletTransactionPie.this.transMode);
                }
                ArrayList arrayList3 = new ArrayList();
                ArrayList<DailyTrans> arrayList4 = new ArrayList();
                if (startDate != 0 && endDate != 0) {
                    for (Recurring recurring : appDatabaseObject2.recurringDaoObject().getAllRecurringListByAccountId(SharePreferenceHelper.getAccountId(WalletTransactionPie.this.getApplicationContext()))) {
                        if (recurring.getIsFuture() == 1 && recurring.getCategoryId() == WalletTransactionPie.this.categoryId && recurring.getWalletId() == WalletTransactionPie.this.walletId) {
                            ArrayList arrayList5 = arrayList3;
                            for (RecurringCalendarDate recurringCalendarDate : RecurringHelper.getStatisticRecurring(recurring, appDatabaseObject2.currencyDaoObject().getCurrencyRate(SharePreferenceHelper.getAccountId(WalletTransactionPie.this.getApplicationContext()), recurring.getCurrency()), startDate, endDate)) {
                                Trans trans = new Trans(recurring.getNote(WalletTransactionPie.this.getApplicationContext()), recurring.getMemo(), recurring.getColor(), recurring.getIcon(), recurring.getCurrency(), recurringCalendarDate.getDate(), recurring.getAmount(), recurring.getWallet(), recurring.getType(), "", recurring.getWalletId(), 0, recurring.getCategory(WalletTransactionPie.this.getApplicationContext()), recurring.getCategoryId(), recurring.getCategoryDefault(), 0, "", 0L, null, 0, 0, 0, 0, "", 0);
                                trans.setRecurring(true);
                                trans.setRecurringId(recurring.getId());
                                ArrayList arrayList6 = arrayList5;
                                arrayList6.add(trans);
                                Iterator it = arrayList4.iterator();
                                while (true) {
                                    if (!it.hasNext()) {
                                        z3 = false;
                                        break;
                                    }
                                    DailyTrans dailyTrans2 = (DailyTrans) it.next();
                                    if (dailyTrans2.getYear() == recurringCalendarDate.getYear() && dailyTrans2.getMonth() == recurringCalendarDate.getMonth() && dailyTrans2.getDay() == recurringCalendarDate.getDay()) {
                                        dailyTrans2.setAmount(dailyTrans2.getAmount() + recurringCalendarDate.getAmount());
                                        z3 = true;
                                        break;
                                    }
                                }
                                if (!z3) {
                                    arrayList4.add(new DailyTrans(recurringCalendarDate.getDay(), recurringCalendarDate.getMonth(), recurringCalendarDate.getYear(), recurringCalendarDate.getAmount()));
                                }
                                arrayList5 = arrayList6;
                            }
                            arrayList3 = arrayList5;
                        }
                    }
                    for (DailyTrans dailyTrans3 : arrayList4) {
                        Iterator<DailyTrans> it2 = pieTrans.iterator();
                        while (true) {
                            if (!it2.hasNext()) {
                                z2 = false;
                                break;
                            }
                            DailyTrans next = it2.next();
                            if (dailyTrans3.getYear() == next.getYear() && dailyTrans3.getMonth() == next.getMonth() && dailyTrans3.getDay() == next.getDay()) {
                                next.setAmount(dailyTrans3.getAmount() + next.getAmount());
                                z2 = true;
                                break;
                            }
                        }
                        if (!z2) {
                            pieTrans.add(dailyTrans3);
                        }
                    }
                    Collections.sort(pieTrans, new Comparator<DailyTrans>() { // from class: com.ktwapps.walletmanager.WalletTransactionPie.1.1
                        @Override // java.util.Comparator
                        public int compare(DailyTrans t1, DailyTrans t2) {
                            if (t1.getDateTime() == null || t2.getDateTime() == null) {
                                return 0;
                            }
                            return t2.getDateTime().compareTo(t1.getDateTime());
                        }
                    });
                }
                for (DailyTrans dailyTrans4 : pieTrans) {
                    arrayList2.add(new TransList(z4, null, dailyTrans4));
                    Date dateTime = dailyTrans4.getDateTime();
                    long transactionStartDate = DateHelper.getTransactionStartDate(dateTime);
                    long transactionEndDate = DateHelper.getTransactionEndDate(dateTime);
                    if (WalletTransactionPie.this.categoryId == 0) {
                        if (WalletTransactionPie.this.transMode == (z4 ? 1 : 0)) {
                            pieTransFromDate = appDatabaseObject2.walletDaoObject().getPieExpenseTransferTransFromDate(accountId, WalletTransactionPie.this.walletId, transactionStartDate, transactionEndDate);
                        } else {
                            pieTransFromDate = appDatabaseObject2.walletDaoObject().getPieIncomeTransferTransFromDate(accountId, WalletTransactionPie.this.walletId, transactionStartDate, transactionEndDate);
                        }
                        appDatabaseObject = appDatabaseObject2;
                        dailyTrans = null;
                        arrayList = arrayList3;
                        z = false;
                    } else {
                        appDatabaseObject = appDatabaseObject2;
                        dailyTrans = null;
                        z = false;
                        arrayList = arrayList3;
                        pieTransFromDate = appDatabaseObject2.walletDaoObject().getPieTransFromDate(accountId, WalletTransactionPie.this.walletId, WalletTransactionPie.this.categoryId, transactionStartDate, transactionEndDate, WalletTransactionPie.this.transMode);
                    }
                    for (Trans trans2 : pieTransFromDate) {
                        arrayList2.add(new TransList(z, trans2, dailyTrans));
                    }
                    for (Trans trans3 : arrayList) {
                        java.util.Calendar calendar = java.util.Calendar.getInstance();
                        calendar.setTime(trans3.getDateTime());
                        if (calendar.get(1) == dailyTrans4.getYear() && calendar.get(2) == dailyTrans4.getMonth() - 1 && calendar.get(5) == dailyTrans4.getDay()) {
                            arrayList2.add(new TransList(z, trans3, dailyTrans));
                        }
                    }
                    arrayList3 = arrayList;
                    appDatabaseObject2 = appDatabaseObject;
                    z4 = true;
                }
                WalletTransactionPie.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.WalletTransactionPie.1.2
                    @Override // java.lang.Runnable
                    public void run() {
                        WalletTransactionPie walletTransactionPie;
                        int i;
                        WalletTransactionPie.this.emptyWrapper.setVisibility(arrayList2.size() == 0 ? 0 : 8);
                        RecyclerView recyclerView = WalletTransactionPie.this.recyclerView;
                        if (arrayList2.size() == 0) {
                            walletTransactionPie = WalletTransactionPie.this;
                            i = R.attr.emptyBackground;
                        } else {
                            walletTransactionPie = WalletTransactionPie.this;
                            i = R.attr.primaryBackground;
                        }
                        recyclerView.setBackgroundColor(Helper.getAttributeColor(walletTransactionPie, i));
                        WalletTransactionPie.this.adapter.setMainTransList(arrayList2);
                    }
                });
            }
        });
    }

    @Override // androidx.appcompat.app.AppCompatActivity
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(R.anim.scale_in, R.anim.right_to_left);
        return true;
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.scale_in, R.anim.right_to_left);
    }

    @Override // com.ktwapps.walletmanager.Adapter.TransactionPieAdapter.OnItemClickListener
    public void OnItemClick(View view, int position) {
        Trans trans = this.adapter.mainTransList.get(position).getTrans();
        if (view.getId() == R.id.image1 || view.getId() == R.id.image2 || view.getId() == R.id.image3) {
            Intent intent = new Intent(this, PhotoViewer.class);
            intent.putExtra(ClientCookie.PATH_ATTR, trans.getMedia());
            intent.putExtra(FirebaseAnalytics.Param.INDEX, ((Integer) view.getTag()).intValue());
            startActivity(intent);
        } else if (trans.getDebtId() != 0 && trans.getDebtTransId() == 0) {
            Intent intent2 = new Intent(this, DebtDetails.class);
            intent2.putExtra("debtId", trans.getDebtId());
            startActivity(intent2);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        } else {
            Intent intent3 = new Intent(this, Details.class);
            intent3.putExtra("transId", trans.getId());
            startActivity(intent3);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        }
    }

    @Override // com.ktwapps.walletmanager.Adapter.TransactionPieAdapter.OnItemClickListener
    public void OnItemLongClick(View view, int position) {
        TransactionHelper.showPopupMenu(this, this.adapter.mainTransList.get(position).getTrans(), view);
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_date_mode, menu);
        for (int i = 0; i < menu.size(); i++) {
            Drawable icon = menu.getItem(i).getIcon();
            if (icon != null) {
                icon.mutate();
                icon.setColorFilter(Helper.getAttributeColor(this, R.attr.primaryDarkTextColor), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return true;
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_action_date_mode) {
            BottomDateModeDialog bottomDateModeDialog = new BottomDateModeDialog();
            bottomDateModeDialog.setMode(this.mode);
            bottomDateModeDialog.setListener(this);
            bottomDateModeDialog.show(getSupportFragmentManager(), "date_mode");
            return false;
        }
        return false;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.backImage /* 2131230831 */) {
            int i = this.mode;
            if (i != 5 && i != 6) {
                Date incrementPieDate = StatisticHelper.incrementPieDate(this.date, i, -1);
                this.date = incrementPieDate;
                this.dateLabel.setText(CalendarHelper.getPieFormattedDate(this, incrementPieDate, this.mode));
                populateData(StatisticHelper.getPieStartDate(getApplicationContext(), this.date, this.mode), StatisticHelper.getPieEndDate(getApplicationContext(), this.date, this.mode));
                return;
            }
        } else if (view.getId() == R.id.endDateEditText /* 2131231051 */) {
            new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() { // from class: com.ktwapps.walletmanager.WalletTransactionPie.3
                @Override // android.app.DatePickerDialog.OnDateSetListener
                public void onDateSet(DatePicker datePicker, int i2, int i1, int i22) {
                    WalletTransactionPie.this.holderEndDate = CalendarHelper.getDateFromPicker(i2, i1, i22);
                    WalletTransactionPie.this.endEditText.setText(DateHelper.getFormattedDate(WalletTransactionPie.this.getApplicationContext(), WalletTransactionPie.this.holderEndDate));
                    if (DateHelper.isBeforeDate(WalletTransactionPie.this.holderStartDate, WalletTransactionPie.this.holderEndDate)) {
                        WalletTransactionPie.this.holderStartDate = CalendarHelper.getDateFromPicker(i2, i1, i22);
                        WalletTransactionPie.this.startEditText.setText(DateHelper.getFormattedDate(WalletTransactionPie.this.getApplicationContext(), WalletTransactionPie.this.holderStartDate));
                    }
                }
            }, CalendarHelper.getYearFromDate(this.holderEndDate), CalendarHelper.getMonthFromDate(this.holderEndDate), CalendarHelper.getDayFromDate(this.holderEndDate)).show();
            return;
        } else if (view.getId() == R.id.nextImage /* 2131231339 */) {
            int i2 = this.mode;
            if (i2 == 5 || i2 == 6) {
                return;
            }
            Date incrementPieDate2 = StatisticHelper.incrementPieDate(this.date, i2, 1);
            this.date = incrementPieDate2;
            this.dateLabel.setText(CalendarHelper.getPieFormattedDate(this, incrementPieDate2, this.mode));
            populateData(StatisticHelper.getPieStartDate(getApplicationContext(), this.date, this.mode), StatisticHelper.getPieEndDate(getApplicationContext(), this.date, this.mode));
            return;
        } else if (view.getId() == R.id.startDateEditText /* 2131231542 */) {
            // Code for start date edit text
        } else {
            return;
        }
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() { // from class: com.ktwapps.walletmanager.WalletTransactionPie.2
            @Override // android.app.DatePickerDialog.OnDateSetListener
            public void onDateSet(DatePicker datePicker, int i3, int i1, int i22) {
                WalletTransactionPie.this.holderStartDate = CalendarHelper.getDateFromPicker(i3, i1, i22);
                WalletTransactionPie.this.startEditText.setText(DateHelper.getFormattedDate(WalletTransactionPie.this.getApplicationContext(), WalletTransactionPie.this.holderStartDate));
                if (DateHelper.isBeforeDate(WalletTransactionPie.this.holderStartDate, WalletTransactionPie.this.holderEndDate)) {
                    WalletTransactionPie.this.holderEndDate = CalendarHelper.getDateFromPicker(i3, i1, i22);
                    WalletTransactionPie.this.endEditText.setText(DateHelper.getFormattedDate(WalletTransactionPie.this.getApplicationContext(), WalletTransactionPie.this.holderEndDate));
                }
            }
        }, CalendarHelper.getYearFromDate(this.holderStartDate), CalendarHelper.getMonthFromDate(this.holderStartDate), CalendarHelper.getDayFromDate(this.holderStartDate)).show();
    }

    @Override // com.ktwapps.walletmanager.Widget.BottomDateModeDialog.OnItemClickListener
    public void onItemClick(int mode) {
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
            builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.WalletTransactionPie.4
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    WalletTransactionPie walletTransactionPie = WalletTransactionPie.this;
                    walletTransactionPie.startDate = walletTransactionPie.holderStartDate;
                    WalletTransactionPie walletTransactionPie2 = WalletTransactionPie.this;
                    walletTransactionPie2.endDate = walletTransactionPie2.holderEndDate;
                    WalletTransactionPie.this.dateLabel.setText(CalendarHelper.getFormattedCustomDate(WalletTransactionPie.this.getApplicationContext(), WalletTransactionPie.this.startDate, WalletTransactionPie.this.endDate));
                    WalletTransactionPie.this.populateData(CalendarHelper.getCustomStartDate(WalletTransactionPie.this.startDate), CalendarHelper.getCustomEndDate(WalletTransactionPie.this.endDate));
                    WalletTransactionPie.this.mode = 6;
                    WalletTransactionPie.this.backImage.setAlpha(0.25f);
                    WalletTransactionPie.this.nextImage.setAlpha(0.25f);
                }
            });
            builder.setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null);
            builder.show();
            return;
        }
        this.mode = mode;
        this.dateLabel.setText(CalendarHelper.getPieFormattedDate(this, this.date, mode));
        long pieStartDate = StatisticHelper.getPieStartDate(getApplicationContext(), this.date, mode);
        long pieEndDate = StatisticHelper.getPieEndDate(getApplicationContext(), this.date, mode);
        if (pieStartDate == 0 && pieEndDate == 0) {
            this.backImage.setAlpha(0.35f);
            this.nextImage.setAlpha(0.35f);
        } else {
            this.backImage.setAlpha(1.0f);
            this.nextImage.setAlpha(1.0f);
        }
        populateData(pieStartDate, pieEndDate);
    }

    @Override // com.ktwapps.walletmanager.Broadcast.BroadcastUpdated.OnBroadcastListener
    public void getBroadcast() {
        long pieStartDate;
        long pieEndDate;
        int i = this.mode;
        if (i == 6) {
            pieStartDate = CalendarHelper.getCustomStartDate(this.startDate);
            pieEndDate = CalendarHelper.getCustomEndDate(this.endDate);
        } else {
            pieStartDate = StatisticHelper.getPieStartDate(this, this.date, i);
            pieEndDate = StatisticHelper.getPieEndDate(this, this.date, this.mode);
        }
        populateData(pieStartDate, pieEndDate);
    }
}
