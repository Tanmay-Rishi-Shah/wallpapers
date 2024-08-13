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
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.tabs.TabLayout;
import com.expance.merapaisa.Adapter.StatisticPieAdapter;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Model.Recurring;
import com.expance.merapaisa.Model.RecurringCalendarDate;
import com.expance.merapaisa.Model.Stats;
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

/* loaded from: classes3.dex */
public class StatisticPie extends AppCompatActivity implements View.OnClickListener, BottomDateModeDialog.OnItemClickListener, StatisticPieAdapter.OnItemClickListener, TabLayout.OnTabSelectedListener {
    StatisticPieAdapter adapter;
    ImageView backImage;
    Date date;
    TextView dateLabel;
    Date endDate;
    EditText endEditText;
    Date holderEndDate;
    Date holderStartDate;
    ImageView nextImage;
    RecyclerView recyclerView;
    Date startDate;
    EditText startEditText;
    TabLayout tabLayout;
    int mode = 2;
    int transMode = 1;

    @Override // com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
    public void onTabReselected(TabLayout.Tab tab) {
    }

    @Override // com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
    public void onTabUnselected(TabLayout.Tab tab) {
    }

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
        setContentView(R.layout.activity_statistic_pie);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeInMillis(getIntent().getLongExtra("date", java.util.Calendar.getInstance().getTimeInMillis()));
        this.mode = getIntent().getIntExtra("mode", 2);
        this.date = calendar.getTime();
        calendar.setTimeInMillis(getIntent().getLongExtra("startDate", java.util.Calendar.getInstance().getTimeInMillis()));
        this.startDate = calendar.getTime();
        calendar.setTimeInMillis(getIntent().getLongExtra("endDate", java.util.Calendar.getInstance().getTimeInMillis()));
        this.endDate = calendar.getTime();
        StatisticPieAdapter statisticPieAdapter = new StatisticPieAdapter(this);
        this.adapter = statisticPieAdapter;
        statisticPieAdapter.setMode(1);
        this.adapter.setListener(this);
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

    private void setUpLayout() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.structure);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.backImage = (ImageView) findViewById(R.id.backImage);
        this.nextImage = (ImageView) findViewById(R.id.nextImage);
        this.dateLabel = (TextView) findViewById(R.id.dateLabel);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        this.tabLayout = tabLayout;
        tabLayout.getTabAt(1).select();
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.backImage.setOnClickListener(this);
        this.nextImage.setOnClickListener(this);
        this.tabLayout.addOnTabSelectedListener((TabLayout.OnTabSelectedListener) this);
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
        populateData(pieStartDate, pieEndDate, this.transMode);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void populateData(final long startDate, final long endDate, final int transMode) {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.StatisticPie.1
            @Override // java.lang.Runnable
            public void run() {
                List<RecurringCalendarDate> statisticRecurring;
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(StatisticPie.this.getApplicationContext());
                if (startDate == 0 && endDate == 0) {
                    final List<Stats> allPieStats = appDatabaseObject.statisticDaoObject().getAllPieStats(SharePreferenceHelper.getAccountId(StatisticPie.this.getApplicationContext()), transMode);
                    StatisticPie.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.StatisticPie.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            StatisticPie.this.adapter.setList(allPieStats);
                            StatisticPie.this.adapter.notifyDataSetChanged();
                        }
                    });
                    return;
                }
                final List<Stats> pieStats = appDatabaseObject.statisticDaoObject().getPieStats(SharePreferenceHelper.getAccountId(StatisticPie.this.getApplicationContext()), startDate, endDate, transMode);
                for (Recurring recurring : appDatabaseObject.recurringDaoObject().getAllRecurringListByAccountId(SharePreferenceHelper.getAccountId(StatisticPie.this.getApplicationContext()))) {
                    boolean z = true;
                    if (recurring.getIsFuture() == 1 && recurring.getType() == transMode && (statisticRecurring = RecurringHelper.getStatisticRecurring(recurring, appDatabaseObject.currencyDaoObject().getCurrencyRate(SharePreferenceHelper.getAccountId(StatisticPie.this.getApplicationContext()), recurring.getCurrency()), startDate, endDate)) != null && statisticRecurring.size() > 0) {
                        long amount = statisticRecurring.get(0).getAmount() * statisticRecurring.size();
                        Iterator<Stats> it = pieStats.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                z = false;
                                break;
                            }
                            Stats next = it.next();
                            if (next.getId() == recurring.getCategoryId()) {
                                next.setAmount(next.getAmount() + amount);
                                next.setTrans(next.getTrans() + statisticRecurring.size());
                                break;
                            }
                        }
                        if (!z) {
                            pieStats.add(new Stats(recurring.getCategory(StatisticPie.this.getApplicationContext()), recurring.getColor(), recurring.getIcon(), amount, Utils.DOUBLE_EPSILON, recurring.getCategoryId(), statisticRecurring.size(), recurring.getCategoryDefault()));
                        }
                    }
                }
                long j = 0;
                for (Stats stats : pieStats) {
                    j += stats.getAmount();
                }
                for (Stats stats2 : pieStats) {
                    if (j >= 0) {
                        stats2.setPercent((((float) stats2.getAmount()) / ((float) j)) * 100.0f);
                    } else {
                        stats2.setPercent((((float) (-stats2.getAmount())) / ((float) (-j))) * 100.0f);
                    }
                }
                Collections.sort(pieStats, new Comparator<Stats>() { // from class: com.ktwapps.walletmanager.StatisticPie.1.2
                    @Override // java.util.Comparator
                    public int compare(Stats t1, Stats t2) {
                        return (int) (t2.getPercent() - t1.getPercent());
                    }
                });
                StatisticPie.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.StatisticPie.1.3
                    @Override // java.lang.Runnable
                    public void run() {
                        StatisticPie.this.adapter.setList(pieStats);
                        StatisticPie.this.adapter.notifyDataSetChanged();
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
                populateData(StatisticHelper.getPieStartDate(getApplicationContext(), this.date, this.mode), StatisticHelper.getPieEndDate(getApplicationContext(), this.date, this.mode), this.transMode);
            }
        } else if (view.getId() == R.id.endDateEditText /* 2131231051 */) {
            new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i2, int i1, int i22) {
                    StatisticPie.this.holderEndDate = CalendarHelper.getDateFromPicker(i2, i1, i22);
                    StatisticPie.this.endEditText.setText(DateHelper.getFormattedDate(StatisticPie.this.getApplicationContext(), StatisticPie.this.holderEndDate));
                    if (DateHelper.isBeforeDate(StatisticPie.this.holderStartDate, StatisticPie.this.holderEndDate)) {
                        StatisticPie.this.holderStartDate = CalendarHelper.getDateFromPicker(i2, i1, i22);
                        StatisticPie.this.startEditText.setText(DateHelper.getFormattedDate(StatisticPie.this.getApplicationContext(), StatisticPie.this.holderStartDate));
                    }
                }
            }, CalendarHelper.getYearFromDate(this.holderEndDate), CalendarHelper.getMonthFromDate(this.holderEndDate), CalendarHelper.getDayFromDate(this.holderEndDate)).show();
        } else if (view.getId() == R.id.nextImage /* 2131231339 */) {
            int i2 = this.mode;
            if (i2 != 5 && i2 != 6) {
                Date incrementPieDate2 = StatisticHelper.incrementPieDate(this.date, i2, 1);
                this.date = incrementPieDate2;
                this.dateLabel.setText(CalendarHelper.getPieFormattedDate(this, incrementPieDate2, this.mode));
                populateData(StatisticHelper.getPieStartDate(getApplicationContext(), this.date, this.mode), StatisticHelper.getPieEndDate(getApplicationContext(), this.date, this.mode), this.transMode);
            }
        } else if (view.getId() == R.id.startDateEditText /* 2131231542 */) {
            new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i3, int i1, int i22) {
                    StatisticPie.this.holderStartDate = CalendarHelper.getDateFromPicker(i3, i1, i22);
                    StatisticPie.this.startEditText.setText(DateHelper.getFormattedDate(StatisticPie.this.getApplicationContext(), StatisticPie.this.holderStartDate));
                    if (DateHelper.isBeforeDate(StatisticPie.this.holderStartDate, StatisticPie.this.holderEndDate)) {
                        StatisticPie.this.holderEndDate = CalendarHelper.getDateFromPicker(i3, i1, i22);
                        StatisticPie.this.endEditText.setText(DateHelper.getFormattedDate(StatisticPie.this.getApplicationContext(), StatisticPie.this.holderEndDate));
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
            builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.StatisticPie.4
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    StatisticPie statisticPie = StatisticPie.this;
                    statisticPie.startDate = statisticPie.holderStartDate;
                    StatisticPie statisticPie2 = StatisticPie.this;
                    statisticPie2.endDate = statisticPie2.holderEndDate;
                    StatisticPie.this.dateLabel.setText(CalendarHelper.getFormattedCustomDate(StatisticPie.this.getApplicationContext(), StatisticPie.this.startDate, StatisticPie.this.endDate));
                    long customStartDate = CalendarHelper.getCustomStartDate(StatisticPie.this.startDate);
                    long customEndDate = CalendarHelper.getCustomEndDate(StatisticPie.this.endDate);
                    StatisticPie statisticPie3 = StatisticPie.this;
                    statisticPie3.populateData(customStartDate, customEndDate, statisticPie3.transMode);
                    StatisticPie.this.mode = 6;
                    StatisticPie.this.backImage.setAlpha(0.25f);
                    StatisticPie.this.nextImage.setAlpha(0.25f);
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
        populateData(pieStartDate, pieEndDate, this.transMode);
    }

    @Override // com.ktwapps.walletmanager.Adapter.StatisticPieAdapter.OnItemClickListener
    public void OnItemClick(View v, int position) {
        Stats stats = this.adapter.getList().get(position - 1);
        Intent intent = new Intent(this, TransactionPie.class);
        intent.putExtra("name", stats.getName(this));
        intent.putExtra("date", this.date.getTime());
        intent.putExtra("startDate", this.startDate.getTime());
        intent.putExtra("endDate", this.endDate.getTime());
        intent.putExtra("mode", this.mode);
        intent.putExtra("transMode", this.transMode);
        intent.putExtra("categoryId", stats.getId());
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
    }

    @Override // com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
    public void onTabSelected(TabLayout.Tab tab) {
        long pieStartDate;
        long pieEndDate;
        int i = tab.getPosition() == 0 ? 0 : 1;
        this.transMode = i;
        this.adapter.setMode(i);
        int i2 = this.mode;
        if (i2 == 6) {
            pieStartDate = CalendarHelper.getCustomStartDate(this.startDate);
            pieEndDate = CalendarHelper.getCustomEndDate(this.endDate);
        } else {
            pieStartDate = StatisticHelper.getPieStartDate(this, this.date, i2);
            pieEndDate = StatisticHelper.getPieEndDate(this, this.date, this.mode);
        }
        populateData(pieStartDate, pieEndDate, this.transMode);
    }
}
