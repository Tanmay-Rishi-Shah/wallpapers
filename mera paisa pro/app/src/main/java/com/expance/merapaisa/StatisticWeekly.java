package com.expance.merapaisa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.expance.merapaisa.Adapter.StatisticWeeklyAdapter;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Model.Recurring;
import com.expance.merapaisa.Model.RecurringCalendarDate;
import com.expance.merapaisa.Model.WeeklyStats;
import com.expance.merapaisa.Utility.CalendarHelper;
import com.expance.merapaisa.Utility.RecurringHelper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import com.expance.merapaisa.Utility.StatisticHelper;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;

import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class StatisticWeekly extends AppCompatActivity implements View.OnClickListener, StatisticWeeklyAdapter.OnItemClickListener {
    StatisticWeeklyAdapter adapter;
    ImageView backImage;
    Date date;
    TextView dateLabel;
    ImageView nextImage;
    RecyclerView recyclerView;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("date", this.date.getTime());
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
        setContentView(R.layout.activity_statistic_weekly);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        this.date = java.util.Calendar.getInstance().getTime();
        StatisticWeeklyAdapter statisticWeeklyAdapter = new StatisticWeeklyAdapter(this, this.date);
        this.adapter = statisticWeeklyAdapter;
        statisticWeeklyAdapter.setListener(this);
        if (savedInstanceState != null) {
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTimeInMillis(savedInstanceState.getLong("date"));
            Date time = calendar.getTime();
            this.date = time;
            this.adapter.setDate(time);
        }
        setUpLayout();
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

    private void setUpLayout() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.weekly_spending);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.backImage = (ImageView) findViewById(R.id.backImage);
        this.nextImage = (ImageView) findViewById(R.id.nextImage);
        this.dateLabel = (TextView) findViewById(R.id.dateLabel);
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.backImage.setOnClickListener(this);
        this.nextImage.setOnClickListener(this);
        this.dateLabel.setText(CalendarHelper.getWeeklySpendingFormattedDate(getApplicationContext(), this.date));
        populateData(CalendarHelper.getWeeklyStartDate(getApplicationContext(), this.date), CalendarHelper.getWeeklyEndDate(getApplicationContext(), this.date));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        populateData(CalendarHelper.getWeeklyStartDate(getApplicationContext(), this.date), CalendarHelper.getWeeklyEndDate(getApplicationContext(), this.date));
    }

    private void populateData(final long startDate, final long endDate) {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.StatisticWeekly.1
            @Override // java.lang.Runnable
            public void run() {
                boolean z;
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(StatisticWeekly.this.getApplicationContext());
                final List<WeeklyStats> weeklyStat = appDatabaseObject.statisticDaoObject().getWeeklyStat(SharePreferenceHelper.getAccountId(StatisticWeekly.this.getApplicationContext()), startDate, endDate);
                for (Recurring recurring : appDatabaseObject.recurringDaoObject().getAllRecurringListByAccountId(SharePreferenceHelper.getAccountId(StatisticWeekly.this.getApplicationContext()))) {
                    if (recurring.getIsFuture() == 1 && recurring.getType() == 1) {
                        for (RecurringCalendarDate recurringCalendarDate : RecurringHelper.getStatisticRecurring(recurring, appDatabaseObject.currencyDaoObject().getCurrencyRate(SharePreferenceHelper.getAccountId(StatisticWeekly.this.getApplicationContext()), recurring.getCurrency()), startDate, endDate)) {
                            Iterator<WeeklyStats> it = weeklyStat.iterator();
                            while (true) {
                                if (!it.hasNext()) {
                                    z = false;
                                    break;
                                }
                                WeeklyStats next = it.next();
                                if (next.getDay() == recurringCalendarDate.getDay()) {
                                    next.setTrans(next.getTrans() + 1);
                                    next.setAmount(next.getAmount() + recurringCalendarDate.getAmount());
                                    z = true;
                                    break;
                                }
                            }
                            if (!z) {
                                weeklyStat.add(new WeeklyStats(recurringCalendarDate.getDay(), recurringCalendarDate.getAmount(), 1));
                            }
                        }
                    }
                }
                StatisticWeekly.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.StatisticWeekly.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        StatisticWeekly.this.adapter.setList(weeklyStat);
                        StatisticWeekly.this.adapter.notifyDataSetChanged();
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

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.backImage) {
            Date incrementWeek = CalendarHelper.incrementWeek(this.date, -1);
            this.date = incrementWeek;
            this.adapter.setDate(incrementWeek);
        } else if (id == R.id.nextImage) {
            Date incrementWeek2 = CalendarHelper.incrementWeek(this.date, 1);
            this.date = incrementWeek2;
            this.adapter.setDate(incrementWeek2);
        }
        this.dateLabel.setText(CalendarHelper.getWeeklySpendingFormattedDate(getApplicationContext(), this.date));
        populateData(CalendarHelper.getWeeklyStartDate(getApplicationContext(), this.date), CalendarHelper.getWeeklyEndDate(getApplicationContext(), this.date));
    }

    @Override // com.ktwapps.walletmanager.Adapter.StatisticWeeklyAdapter.OnItemClickListener
    public void OnItemClick(View v, int position) {
        if (position != 0) {
            Intent intent = new Intent(this, TransactionWeekly.class);
            intent.putExtra("dateTime", StatisticHelper.getWeeklySpendingItemDate(this, this.date, position));
            startActivity(intent);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        }
    }
}
