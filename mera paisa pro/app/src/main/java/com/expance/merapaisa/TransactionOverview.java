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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.expance.merapaisa.Adapter.TransactionOverviewAdapter;
import com.expance.merapaisa.Broadcast.BroadcastUpdated;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Model.CalendarSummary;
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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;

import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class TransactionOverview extends AppCompatActivity implements TransactionOverviewAdapter.OnItemClickListener, View.OnClickListener, BottomDateModeDialog.OnItemClickListener, BroadcastUpdated.OnBroadcastListener {
    TransactionOverviewAdapter adapter;
    ImageView backImage;
    BroadcastUpdated broadcastUpdated;
    Date date;
    TextView dateLabel;
    ConstraintLayout emptyWrapper;
    Date endDate;
    EditText endEditText;
    FloatingActionButton fab;
    Date holderEndDate;
    Date holderStartDate;
    int mode = 2;
    ImageView nextImage;
    RecyclerView recyclerView;
    Date startDate;
    EditText startEditText;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("date", this.date.getTime());
        outState.putInt("mode", this.mode);
        outState.putLong("startDate", this.startDate.getTime());
        outState.putLong("endDate", this.endDate.getTime());
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
        setContentView(R.layout.activity_transaction_overview);
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
        this.mode = getIntent().getIntExtra("mode", 2);
        calendar.setTimeInMillis(getIntent().getLongExtra("startDate", CalendarHelper.getCustomInitialStartDate(this.date).getTime()));
        this.startDate = calendar.getTime();
        calendar.setTimeInMillis(getIntent().getLongExtra("endDate", CalendarHelper.getCustomInitialEndDate(this.date).getTime()));
        this.endDate = calendar.getTime();
        if (savedInstanceState != null) {
            calendar.setTimeInMillis(savedInstanceState.getLong("date"));
            this.date = calendar.getTime();
            this.mode = savedInstanceState.getInt("mode");
            calendar.setTimeInMillis(savedInstanceState.getLong("startDate"));
            this.startDate = calendar.getTime();
            calendar.setTimeInMillis(savedInstanceState.getLong("endDate"));
            this.endDate = calendar.getTime();
        }
        TransactionOverviewAdapter transactionOverviewAdapter = new TransactionOverviewAdapter(this);
        this.adapter = transactionOverviewAdapter;
        transactionOverviewAdapter.setListener(this);
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
            getSupportActionBar().setTitle(R.string.transaction);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.fab = (FloatingActionButton) findViewById(R.id.fab);
        this.backImage = (ImageView) findViewById(R.id.backImage);
        this.nextImage = (ImageView) findViewById(R.id.nextImage);
        this.dateLabel = (TextView) findViewById(R.id.dateLabel);
        this.emptyWrapper = (ConstraintLayout) findViewById(R.id.emptyWrapper);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setAdapter(this.adapter);
        this.fab.setOnClickListener(this);
        this.backImage.setOnClickListener(this);
        this.nextImage.setOnClickListener(this);
        this.dateLabel.setText(CalendarHelper.getPieFormattedDate(this, this.date, this.mode));
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
        populateData(pieStartDate, pieEndDate);
        LocalBroadcastManager.getInstance(this).registerReceiver(this.broadcastUpdated, new IntentFilter(Constant.BROADCAST_UPDATED));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.broadcastUpdated);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void populateData(final long startDate, final long endDate) {
        Executors.newSingleThreadExecutor().execute(() -> {
            final CalendarSummary summary;
            long summaryExpanse = 0;
            long summaryIncome = 0;
            List<DailyTrans> overviewTransList = new ArrayList<>();

            ArrayList<Trans> transArrayList = new ArrayList<>();
            ArrayList<DailyTrans> dailyTransArrayList = new ArrayList<>();
            int i = 1;
            boolean z;
            Iterator<Recurring> it;
            long j3;
            Iterator<Recurring> it2;
            boolean z2;
            long j5 = 0;

            AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(TransactionOverview.this.getApplicationContext());
            int accountId = SharePreferenceHelper.getAccountId(TransactionOverview.this.getApplicationContext());
            final ArrayList<TransList> mainTransList = new ArrayList<>();

            if (startDate == 0 && endDate == 0) {
                summary = appDatabaseObject.statisticDaoObject().getSummary(accountId);
                overviewTransList = appDatabaseObject.statisticDaoObject().getAllOverviewTrans(accountId);
            } else {
                summary = appDatabaseObject.statisticDaoObject().getSummary(startDate, endDate, accountId);
                overviewTransList = appDatabaseObject.statisticDaoObject().getOverviewTrans(accountId, startDate, endDate);
            }

            if (startDate == 0 || endDate == 0) {
                summaryExpanse = 0;
                summaryIncome = 0;
            }else {
                Iterator<Recurring> it3 = appDatabaseObject.recurringDaoObject().getAllRecurringListByAccountId(SharePreferenceHelper.getAccountId(getApplicationContext())).iterator();
                summaryExpanse = 0;
                summaryIncome = 0;
                while (it3.hasNext()) {
                    Recurring next = it3.next();
                    if (next.getIsFuture() == i) {
                        for (RecurringCalendarDate recurringCalendarDate : RecurringHelper.getStatisticRecurring(next, appDatabaseObject.currencyDaoObject().getCurrencyRate(SharePreferenceHelper.getAccountId(getApplicationContext()), next.getCurrency()), startDate, endDate)) {
                            long amount = summaryExpanse + (recurringCalendarDate.getAmount() >= 0 ? 0L : recurringCalendarDate.getAmount());
                            summaryIncome += recurringCalendarDate.getAmount() >= 0 ? recurringCalendarDate.getAmount() : 0L;
                            Trans trans = new Trans(next.getNote(getApplicationContext()), next.getMemo(), next.getColor(), next.getIcon(), next.getCurrency(), recurringCalendarDate.getDate(), next.getAmount(), next.getWallet(), next.getType(), "", next.getWalletId(), 0, next.getCategory(getApplicationContext()), next.getCategoryId(), next.getCategoryDefault(), 0, "", 0L, null, 0, 0, 0, 0, "", 0);
                            trans.setRecurring(true);
                            trans.setRecurringId(next.getId());
                            transArrayList.add(trans);
                            Iterator<DailyTrans> it4 = dailyTransArrayList.iterator();
                            while (true) {
                                if (!it4.hasNext()) {
                                    it2 = it3;
                                    z2 = false;
                                    break;
                                }
                                DailyTrans dailyTrans = (DailyTrans) it4.next();
                                it2 = it3;
                                if (dailyTrans.getYear() == recurringCalendarDate.getYear() && dailyTrans.getMonth() == recurringCalendarDate.getMonth() && dailyTrans.getDay() == recurringCalendarDate.getDay()) {
                                    dailyTrans.setAmount(dailyTrans.getAmount() + recurringCalendarDate.getAmount());
                                    z2 = true;
                                    break;
                                }
                                it3 = it2;
                            }
                            if (!z2) {
                                dailyTransArrayList.add(new DailyTrans(recurringCalendarDate.getDay(), recurringCalendarDate.getMonth(), recurringCalendarDate.getYear(), recurringCalendarDate.getAmount()));
                            }
                            it3 = it2;
                            summaryExpanse = amount;
                        }
                        it = it3;
                        j3 = 0;
                    } else {
                        it = it3;
                        j3 = j5;
                    }
                    j5 = j3;
                    it3 = it;
                    i = 1;
                }
                for (DailyTrans dailyTrans : dailyTransArrayList) {
                    Iterator<DailyTrans> it5 = overviewTransList.iterator();
                    while (true) {
                        if (!it5.hasNext()) {
                            z = false;
                            break;
                        }
                        DailyTrans next2 = it5.next();
                        if (dailyTrans.getYear() == next2.getYear() && dailyTrans.getMonth() == next2.getMonth() && dailyTrans.getDay() == next2.getDay()) {
                            next2.setAmount(dailyTrans.getAmount() + next2.getAmount());
                            z = true;
                            break;
                        }
                    }
                    if (!z) {
                        overviewTransList.add(dailyTrans);
                    }
                }

                Collections.sort(overviewTransList, (t1, t2) -> {
                    if (t1.getDateTime() == null || t2.getDateTime() == null) {
                        return 0;
                    }
                    return t2.getDateTime().compareTo(t1.getDateTime());
                });
            }

            for (DailyTrans dailyTrans : overviewTransList) {
                mainTransList.add(new TransList(true, null, dailyTrans));
                Date dateTime = dailyTrans.getDateTime();
                for (Trans trans : appDatabaseObject.statisticDaoObject().getOverviewTransFromDate(accountId, DateHelper.getTransactionStartDate(dateTime), DateHelper.getTransactionEndDate(dateTime))) {
                    mainTransList.add(new TransList(false, trans, null));
                }
                for (Trans trans : transArrayList) {
                    java.util.Calendar calendar = java.util.Calendar.getInstance();
                    calendar.setTime(trans.getDateTime());
                    if (calendar.get(1) == dailyTrans.getYear() && calendar.get(2) == dailyTrans.getMonth() - 1 && calendar.get(5) == dailyTrans.getDay()) {
                        mainTransList.add(new TransList(false, trans, null));
                    }
                }
            }
            summary.setExpense(summary.getExpense() + summaryExpanse);
            summary.setIncome(summary.getIncome() + summaryIncome);
            // java.lang.Runnable
            TransactionOverview.this.runOnUiThread(() -> {
                TransactionOverview transactionOverview;
                int i2;
                TransactionOverview.this.emptyWrapper.setVisibility(mainTransList.size() == 0 ? View.VISIBLE : View.GONE);
                RecyclerView recyclerView = TransactionOverview.this.recyclerView;
                if (mainTransList.size() == 0) {
                    transactionOverview = TransactionOverview.this;
                    i2 = R.attr.emptyBackground;
                } else {
                    transactionOverview = TransactionOverview.this;
                    i2 = R.attr.primaryBackground;
                }
                recyclerView.setBackgroundColor(Helper.getAttributeColor(transactionOverview, i2));
                TransactionOverview.this.adapter.setMainTransList(mainTransList);
                TransactionOverview.this.adapter.setSummary(summary);
            });
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

    @Override // com.ktwapps.walletmanager.Adapter.TransactionOverviewAdapter.OnItemClickListener
    public void OnItemClick(View view, int position) {
        Trans trans = this.adapter.mainTransList.get(position).getTrans();
        if (view.getId() == R.id.image1 || view.getId() == R.id.image2 || view.getId() == R.id.image3) {
            Intent intent = new Intent(this, PhotoViewer.class);
            intent.putExtra(ClientCookie.PATH_ATTR, trans.getMedia());
            intent.putExtra(FirebaseAnalytics.Param.INDEX, ((Integer) view.getTag()).intValue());
            startActivity(intent);
        } else
            if (trans.isRecurring()) {
            if (trans.getRecurringId() != 0) {
                Intent intent2 = new Intent(this, RecurringDetail.class);
                intent2.putExtra("recurringId", trans.getRecurringId());
                startActivity(intent2);
                overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
            }
        } else if (trans.getDebtId() != 0 && trans.getDebtTransId() == 0) {
            Intent intent3 = new Intent(this, DebtDetails.class);
            intent3.putExtra("debtId", trans.getDebtId());
            startActivity(intent3);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        } else {
            Intent intent4 = new Intent(this, Details.class);
            intent4.putExtra("transId", trans.getId());
            startActivity(intent4);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        }
    }

    @Override // com.ktwapps.walletmanager.Adapter.TransactionOverviewAdapter.OnItemClickListener
    public void OnItemLongClick(View view, int position) {
        Trans trans = this.adapter.mainTransList.get(position).getTrans();
        if (trans.isRecurring()) {
            return;
        }
        TransactionHelper.showPopupMenu(this, trans, view);
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
        if (view.getId() == R.id.backImage) {
            int i = this.mode;
            if (i != 5 && i != 6) {
                Date incrementPieDate = StatisticHelper.incrementPieDate(this.date, i, -1);
                this.date = incrementPieDate;
                this.dateLabel.setText(CalendarHelper.getPieFormattedDate(this, incrementPieDate, this.mode));
                populateData(StatisticHelper.getPieStartDate(getApplicationContext(), this.date, this.mode), StatisticHelper.getPieEndDate(getApplicationContext(), this.date, this.mode));
            }
        } else if (view.getId() == R.id.endDateEditText) {
            new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i2, int i1, int i22) {
                    TransactionOverview.this.holderEndDate = CalendarHelper.getDateFromPicker(i2, i1, i22);
                    TransactionOverview.this.endEditText.setText(DateHelper.getFormattedDate(TransactionOverview.this.getApplicationContext(), TransactionOverview.this.holderEndDate));
                    if (DateHelper.isBeforeDate(TransactionOverview.this.holderStartDate, TransactionOverview.this.holderEndDate)) {
                        TransactionOverview.this.holderStartDate = CalendarHelper.getDateFromPicker(i2, i1, i22);
                        TransactionOverview.this.startEditText.setText(DateHelper.getFormattedDate(TransactionOverview.this.getApplicationContext(), TransactionOverview.this.holderStartDate));
                    }
                }
            }, CalendarHelper.getYearFromDate(this.holderEndDate), CalendarHelper.getMonthFromDate(this.holderEndDate), CalendarHelper.getDayFromDate(this.holderEndDate)).show();
        } else if (view.getId() == R.id.fab) {
            startActivity(new Intent(getApplicationContext(), CreateTransaction.class));
            overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
        } else if (view.getId() == R.id.nextImage) {
            int i2 = this.mode;
            if (i2 != 5 && i2 != 6) {
                Date incrementPieDate2 = StatisticHelper.incrementPieDate(this.date, i2, 1);
                this.date = incrementPieDate2;
                this.dateLabel.setText(CalendarHelper.getPieFormattedDate(this, incrementPieDate2, this.mode));
                populateData(StatisticHelper.getPieStartDate(getApplicationContext(), this.date, this.mode), StatisticHelper.getPieEndDate(getApplicationContext(), this.date, this.mode));
            }
        } else if (view.getId() == R.id.startDateEditText) {
            new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i3, int i1, int i22) {
                    TransactionOverview.this.holderStartDate = CalendarHelper.getDateFromPicker(i3, i1, i22);
                    TransactionOverview.this.startEditText.setText(DateHelper.getFormattedDate(TransactionOverview.this.getApplicationContext(), TransactionOverview.this.holderStartDate));
                    if (DateHelper.isBeforeDate(TransactionOverview.this.holderStartDate, TransactionOverview.this.holderEndDate)) {
                        TransactionOverview.this.holderEndDate = CalendarHelper.getDateFromPicker(i3, i1, i22);
                        TransactionOverview.this.endEditText.setText(DateHelper.getFormattedDate(TransactionOverview.this.getApplicationContext(), TransactionOverview.this.holderEndDate));
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
            builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.TransactionOverview.4
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    TransactionOverview transactionOverview = TransactionOverview.this;
                    transactionOverview.startDate = transactionOverview.holderStartDate;
                    TransactionOverview transactionOverview2 = TransactionOverview.this;
                    transactionOverview2.endDate = transactionOverview2.holderEndDate;
                    TransactionOverview.this.dateLabel.setText(CalendarHelper.getFormattedCustomDate(TransactionOverview.this.getApplicationContext(), TransactionOverview.this.startDate, TransactionOverview.this.endDate));
                    TransactionOverview.this.populateData(CalendarHelper.getCustomStartDate(TransactionOverview.this.startDate), CalendarHelper.getCustomEndDate(TransactionOverview.this.endDate));
                    TransactionOverview.this.mode = 6;
                    TransactionOverview.this.backImage.setAlpha(0.25f);
                    TransactionOverview.this.nextImage.setAlpha(0.25f);
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
