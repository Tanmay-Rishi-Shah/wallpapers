package com.expance.merapaisa;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.utils.Utils;
import com.expance.merapaisa.Adapter.WalletStatisticAdapter;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Model.CalendarSummary;
import com.expance.merapaisa.Model.Recurring;
import com.expance.merapaisa.Model.RecurringCalendarDate;
import com.expance.merapaisa.Model.Stats;
import com.expance.merapaisa.Model.Trans;
import com.expance.merapaisa.Utility.CalendarHelper;
import com.expance.merapaisa.Utility.DateHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.RecurringHelper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import com.expance.merapaisa.Utility.StatisticHelper;
import com.expance.merapaisa.Widget.BottomDateModeDialog;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;

import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class WalletStatistic extends AppCompatActivity implements View.OnClickListener, WalletStatisticAdapter.OnItemClickListener, BottomDateModeDialog.OnItemClickListener {
    WalletStatisticAdapter adapter;
    ImageView backImage;
    Date date;
    TextView dateLabel;
    Date endDate;
    private EditText endEditText;
    Date holderEndDate;
    Date holderStartDate;
    int mode = 2;
    ImageView nextImage;
    RecyclerView recyclerView;
    Date startDate;
    private EditText startEditText;
    String symbol;
    int walletId;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("startDate", this.startDate.getTime());
        outState.putLong("endDate", this.endDate.getTime());
        outState.putLong("date", this.date.getTime());
        outState.putInt("mode", this.mode);
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
        setContentView(R.layout.activity_wallet_statistic);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        this.walletId = getIntent().getIntExtra("walletId", 0);
        this.symbol = getIntent().getStringExtra("symbol");
        Date time = java.util.Calendar.getInstance().getTime();
        this.date = time;
        this.startDate = CalendarHelper.getCustomInitialStartDate(time);
        this.endDate = CalendarHelper.getCustomInitialEndDate(this.date);
        if (savedInstanceState != null) {
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTimeInMillis(savedInstanceState.getLong("date"));
            this.date = calendar.getTime();
            this.mode = savedInstanceState.getInt("mode");
            calendar.setTimeInMillis(savedInstanceState.getLong("startDate"));
            this.startDate = calendar.getTime();
            calendar.setTimeInMillis(savedInstanceState.getLong("endDate"));
            this.endDate = calendar.getTime();
        }
        setUpLayout();
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
    }

    private void setUpLayout() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.statistic);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.backImage = (ImageView) findViewById(R.id.backImage);
        this.nextImage = (ImageView) findViewById(R.id.nextImage);
        this.dateLabel = (TextView) findViewById(R.id.dateLabel);
        WalletStatisticAdapter walletStatisticAdapter = new WalletStatisticAdapter(this, this.symbol);
        this.adapter = walletStatisticAdapter;
        walletStatisticAdapter.setListener(this);
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.WalletStatistic.1
            @Override // java.lang.Runnable
            public void run() {
                CalendarSummary calendarSummary;
                AppDatabaseObject appDatabaseObject;
                long j;
                List<Trans> list;
                boolean z;
                AppDatabaseObject appDatabaseObject2 = AppDatabaseObject.getInstance(WalletStatistic.this.getApplicationContext());
                if (startDate == 0 && endDate == 0) {
                    long walletBalance = appDatabaseObject2.walletDaoObject().getWalletBalance(WalletStatistic.this.walletId, 0L);
                    long walletBalance2 = appDatabaseObject2.walletDaoObject().getWalletBalance(WalletStatistic.this.walletId, 253399507200000L);
                    CalendarSummary walletOverview = appDatabaseObject2.walletDaoObject().getWalletOverview(WalletStatistic.this.walletId);
                    List<Stats> allExpensePieStats = appDatabaseObject2.walletDaoObject().getAllExpensePieStats(WalletStatistic.this.walletId);
                    List<Trans> topFiveSpending = appDatabaseObject2.walletDaoObject().getTopFiveSpending(WalletStatistic.this.walletId);
                    WalletStatistic.this.adapter.setBalance(walletBalance, walletBalance2);
                    WalletStatistic.this.adapter.setOverview(walletOverview);
                    WalletStatistic.this.adapter.setPieStatsList(allExpensePieStats);
                    WalletStatistic.this.adapter.setTransList(topFiveSpending);
                } else {
                    long walletBalance3 = appDatabaseObject2.walletDaoObject().getWalletBalance(WalletStatistic.this.walletId, startDate);
                    long walletBalance4 = appDatabaseObject2.walletDaoObject().getWalletBalance(WalletStatistic.this.walletId, endDate);
                    CalendarSummary walletOverview2 = appDatabaseObject2.walletDaoObject().getWalletOverview(WalletStatistic.this.walletId, startDate, endDate);
                    List<Stats> expensePieStats = appDatabaseObject2.walletDaoObject().getExpensePieStats(WalletStatistic.this.walletId, startDate, endDate);
                    List<Trans> topFiveSpending2 = appDatabaseObject2.walletDaoObject().getTopFiveSpending(WalletStatistic.this.walletId, startDate, endDate);
                    Iterator<Recurring> it = appDatabaseObject2.recurringDaoObject().getAllRecurringListByAccountId(SharePreferenceHelper.getAccountId(WalletStatistic.this.getApplicationContext())).iterator();
                    long j2 = 0;
                    long j3 = 0;
                    long j4 = 0;
                    long j5 = 0;
                    while (it.hasNext()) {
                        Recurring next = it.next();
                        Iterator<Recurring> it2 = it;
                        if (next.getIsFuture() == 1 && next.getWalletId() == WalletStatistic.this.walletId) {
                            appDatabaseObject = appDatabaseObject2;
                            float currencyRate = appDatabaseObject2.currencyDaoObject().getCurrencyRate(SharePreferenceHelper.getAccountId(WalletStatistic.this.getApplicationContext()), next.getCurrency());
                            List<Trans> list2 = topFiveSpending2;
                            j = walletBalance4;
                            List<RecurringCalendarDate> statisticRecurring = RecurringHelper.getStatisticRecurring(next, currencyRate, startDate, endDate);
                            j2 += RecurringHelper.getStatisticRecurringCarryOver(next, currencyRate, startDate);
                            j3 += RecurringHelper.getStatisticRecurringCarryOver(next, currencyRate, endDate);
                            for (RecurringCalendarDate recurringCalendarDate : statisticRecurring) {
                                j4 += recurringCalendarDate.getAmount() >= 0 ? 0L : recurringCalendarDate.getAmount();
                                j5 += recurringCalendarDate.getAmount() >= 0 ? recurringCalendarDate.getAmount() : 0L;
                            }
                            if (next.getType() != 1 || statisticRecurring.size() <= 0) {
                                list = list2;
                            } else {
                                list = list2;
                                long amount = statisticRecurring.get(0).getAmount() * statisticRecurring.size();
                                Iterator<Stats> it3 = expensePieStats.iterator();
                                while (true) {
                                    if (!it3.hasNext()) {
                                        z = false;
                                        break;
                                    }
                                    Stats next2 = it3.next();
                                    if (next2.getId() == next.getCategoryId()) {
                                        next2.setAmount(next2.getAmount() + amount);
                                        next2.setTrans(next2.getTrans() + statisticRecurring.size());
                                        z = true;
                                        break;
                                    }
                                }
                                if (!z) {
                                    expensePieStats.add(new Stats(next.getCategory(WalletStatistic.this.getApplicationContext()), next.getColor(), next.getIcon(), amount, Utils.DOUBLE_EPSILON, next.getCategoryId(), statisticRecurring.size(), next.getCategoryDefault()));
                                }
                            }
                        } else {
                            appDatabaseObject = appDatabaseObject2;
                            j = walletBalance4;
                            list = topFiveSpending2;
                        }
                        it = it2;
                        topFiveSpending2 = list;
                        appDatabaseObject2 = appDatabaseObject;
                        walletBalance4 = j;
                    }
                    long j6 = walletBalance4;
                    List<Trans> list3 = topFiveSpending2;
                    long j7 = 0;
                    for (Stats stats : expensePieStats) {
                        j7 += stats.getAmount();
                    }
                    for (Stats stats2 : expensePieStats) {
                        if (j7 >= 0) {
                            stats2.setPercent((((float) stats2.getAmount()) / ((float) j7)) * 100.0f);
                            calendarSummary = walletOverview2;
                        } else {
                            calendarSummary = walletOverview2;
                            stats2.setPercent((((float) (-stats2.getAmount())) / ((float) (-j7))) * 100.0f);
                        }
                        walletOverview2 = calendarSummary;
                    }
                    CalendarSummary calendarSummary2 = walletOverview2;
                    Collections.sort(expensePieStats, new Comparator<Stats>() { // from class: com.ktwapps.walletmanager.WalletStatistic.1.1
                        @Override // java.util.Comparator
                        public int compare(Stats t1, Stats t2) {
                            return (int) (t2.getPercent() - t1.getPercent());
                        }
                    });
                    calendarSummary2.setExpense(calendarSummary2.getExpense() + j4);
                    calendarSummary2.setIncome(calendarSummary2.getIncome() + j5);
                    WalletStatistic.this.adapter.setBalance(walletBalance3 + j2, j6 + j3);
                    WalletStatistic.this.adapter.setOverview(calendarSummary2);
                    WalletStatistic.this.adapter.setPieStatsList(expensePieStats);
                    WalletStatistic.this.adapter.setTransList(list3);
                }
                WalletStatistic.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.WalletStatistic.1.2
                    @Override // java.lang.Runnable
                    public void run() {
                        WalletStatistic.this.adapter.notifyDataSetChanged();
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

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.backImage /* 2131230831 */) {
            int i = this.mode;
            if (i != 5 && i != 6) {
                Date incrementPieDate = StatisticHelper.incrementPieDate(this.date, i, -1);
                this.date = incrementPieDate;
                this.dateLabel.setText(CalendarHelper.getPieFormattedDate(this, incrementPieDate, this.mode));
                populateData(StatisticHelper.getPieStartDate(getApplicationContext(), this.date, this.mode), StatisticHelper.getPieEndDate(getApplicationContext(), this.date, this.mode));
            }
        } else if (view.getId() == R.id.endDateEditText /* 2131231051 */) {
            new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() { // from class: com.ktwapps.walletmanager.WalletStatistic.3
                @Override // android.app.DatePickerDialog.OnDateSetListener
                public void onDateSet(DatePicker datePicker, int i2, int i1, int i22) {
                    WalletStatistic.this.holderEndDate = CalendarHelper.getDateFromPicker(i2, i1, i22);
                    WalletStatistic.this.endEditText.setText(DateHelper.getFormattedDate(WalletStatistic.this.getApplicationContext(), WalletStatistic.this.holderEndDate));
                    if (DateHelper.isBeforeDate(WalletStatistic.this.holderStartDate, WalletStatistic.this.holderEndDate)) {
                        WalletStatistic.this.holderStartDate = CalendarHelper.getDateFromPicker(i2, i1, i22);
                        WalletStatistic.this.startEditText.setText(DateHelper.getFormattedDate(WalletStatistic.this.getApplicationContext(), WalletStatistic.this.holderStartDate));
                    }
                }
            }, CalendarHelper.getYearFromDate(this.holderEndDate), CalendarHelper.getMonthFromDate(this.holderEndDate), CalendarHelper.getDayFromDate(this.holderEndDate)).show();
        } else if (view.getId() == R.id.nextImage /* 2131231339 */) {
            int i2 = this.mode;
            if (i2 != 5 && i2 != 6) {
                Date incrementPieDate2 = StatisticHelper.incrementPieDate(this.date, i2, 1);
                this.date = incrementPieDate2;
                this.dateLabel.setText(CalendarHelper.getPieFormattedDate(this, incrementPieDate2, this.mode));
                populateData(StatisticHelper.getPieStartDate(getApplicationContext(), this.date, this.mode), StatisticHelper.getPieEndDate(getApplicationContext(), this.date, this.mode));
            }
        } else if (view.getId() == R.id.startDateEditText /* 2131231542 */) {
            new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() { // from class: com.ktwapps.walletmanager.WalletStatistic.2
                @Override // android.app.DatePickerDialog.OnDateSetListener
                public void onDateSet(DatePicker datePicker, int i3, int i1, int i22) {
                    WalletStatistic.this.holderStartDate = CalendarHelper.getDateFromPicker(i3, i1, i22);
                    WalletStatistic.this.startEditText.setText(DateHelper.getFormattedDate(WalletStatistic.this.getApplicationContext(), WalletStatistic.this.holderStartDate));
                    if (DateHelper.isBeforeDate(WalletStatistic.this.holderStartDate, WalletStatistic.this.holderEndDate)) {
                        WalletStatistic.this.holderEndDate = CalendarHelper.getDateFromPicker(i3, i1, i22);
                        WalletStatistic.this.endEditText.setText(DateHelper.getFormattedDate(WalletStatistic.this.getApplicationContext(), WalletStatistic.this.holderEndDate));
                    }
                }
            }, CalendarHelper.getYearFromDate(this.holderStartDate), CalendarHelper.getMonthFromDate(this.holderStartDate), CalendarHelper.getDayFromDate(this.holderStartDate)).show();
        }
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
            builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.WalletStatistic.4
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    WalletStatistic walletStatistic = WalletStatistic.this;
                    walletStatistic.startDate = walletStatistic.holderStartDate;
                    WalletStatistic walletStatistic2 = WalletStatistic.this;
                    walletStatistic2.endDate = walletStatistic2.holderEndDate;
                    WalletStatistic.this.dateLabel.setText(CalendarHelper.getFormattedCustomDate(WalletStatistic.this.getApplicationContext(), WalletStatistic.this.startDate, WalletStatistic.this.endDate));
                    WalletStatistic.this.populateData(CalendarHelper.getCustomStartDate(WalletStatistic.this.startDate), CalendarHelper.getCustomEndDate(WalletStatistic.this.endDate));
                    WalletStatistic.this.mode = 6;
                    WalletStatistic.this.backImage.setAlpha(0.25f);
                    WalletStatistic.this.nextImage.setAlpha(0.25f);
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
            this.backImage.setAlpha(0.25f);
            this.nextImage.setAlpha(0.25f);
        } else {
            this.backImage.setAlpha(1.0f);
            this.nextImage.setAlpha(1.0f);
        }
        populateData(pieStartDate, pieEndDate);
    }

    @Override // com.ktwapps.walletmanager.Adapter.WalletStatisticAdapter.OnItemClickListener
    public void OnItemClick(View v, int position) {
        if (position == 2) {
            Intent intent = new Intent(getApplicationContext(), WalletTransactionOverview.class);
            intent.putExtra("walletId", this.walletId);
            intent.putExtra("symbol", this.symbol);
            intent.putExtra("startDate", this.startDate.getTime());
            intent.putExtra("endDate", this.endDate.getTime());
            intent.putExtra("date", this.date.getTime());
            intent.putExtra("mode", this.mode);
            startActivity(intent);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        } else if (position == 4) {
            Intent intent2 = new Intent(getApplicationContext(), WalletStatisticPie.class);
            intent2.putExtra("walletId", this.walletId);
            intent2.putExtra("symbol", this.symbol);
            intent2.putExtra("startDate", this.startDate.getTime());
            intent2.putExtra("endDate", this.endDate.getTime());
            intent2.putExtra("date", this.date.getTime());
            intent2.putExtra("mode", this.mode);
            startActivity(intent2);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        } else if (position >= 6) {
            Trans trans = this.adapter.getTransList().get(position - 6);
            if (trans.getDebtId() != 0 && trans.getDebtTransId() == 0) {
                Intent intent3 = new Intent(this, DebtDetails.class);
                intent3.putExtra("debtId", trans.getDebtId());
                startActivity(intent3);
                overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
                return;
            }
            Intent intent4 = new Intent(this, Details.class);
            intent4.putExtra("transId", trans.getId());
            startActivity(intent4);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        }
    }
}
