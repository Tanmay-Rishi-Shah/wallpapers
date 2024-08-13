package com.expance.merapaisa;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
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
import com.expance.merapaisa.Adapter.WalletCategoryTransactionAdapter;
import com.expance.merapaisa.Broadcast.BroadcastUpdated;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Model.DailyTrans;
import com.expance.merapaisa.Model.Recurring;
import com.expance.merapaisa.Model.RecurringCalendarDate;
import com.expance.merapaisa.Model.SubcategoryStats;
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
public class TransactionPie extends AppCompatActivity implements WalletCategoryTransactionAdapter.OnItemClickListener, View.OnClickListener, BottomDateModeDialog.OnItemClickListener, TransactionPieAdapter.OnItemClickListener, BroadcastUpdated.OnBroadcastListener {
    TransactionPieAdapter adapter;
    ImageView backImage;
    BroadcastUpdated broadcastUpdated;
    int categoryId;
    Date date;
    TextView dateLabel;
    ConstraintLayout emptyWrapper;
    Date endDate;
    EditText endEditText;
    Date holderEndDate;
    Date holderStartDate;
    String name;
    ImageView nextImage;
    RecyclerView recyclerView;
    Date startDate;
    EditText startEditText;
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
        setContentView(R.layout.activity_transaction_pie);
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
        this.name = getIntent().getStringExtra("name");
        this.categoryId = getIntent().getIntExtra("categoryId", 0);
        this.mode = getIntent().getIntExtra("mode", 2);
        this.transMode = getIntent().getIntExtra("transMode", 1);
        calendar.setTimeInMillis(getIntent().getLongExtra("startDate", java.util.Calendar.getInstance().getTimeInMillis()));
        this.startDate = calendar.getTime();
        calendar.setTimeInMillis(getIntent().getLongExtra("endDate", java.util.Calendar.getInstance().getTimeInMillis()));
        this.endDate = calendar.getTime();
        if (savedInstanceState != null) {
            calendar.setTimeInMillis(savedInstanceState.getLong("date"));
            this.date = calendar.getTime();
            this.mode = savedInstanceState.getInt("mode");
            this.transMode = savedInstanceState.getInt("transMode");
            calendar.setTimeInMillis(savedInstanceState.getLong("startDate"));
            this.startDate = calendar.getTime();
            calendar.setTimeInMillis(savedInstanceState.getLong("endDate"));
            this.endDate = calendar.getTime();
        }
        TransactionPieAdapter transactionPieAdapter = new TransactionPieAdapter(this);
        this.adapter = transactionPieAdapter;
        transactionPieAdapter.setListener(this);
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

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.ktwapps.walletmanager.TransactionPie$1  reason: invalid class name */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 implements Runnable {
        final /* synthetic */ long startDate;
        final /* synthetic */ long endDate;

        private TransactionPie context = null;

        AnonymousClass1(final long val$startDate, final long val$endDate) {
            this.context = TransactionPie.this;
            this.startDate = val$startDate;
            this.endDate = val$endDate;
        }

        @Override
        public void run() {
            final List<SubcategoryStats> subcategoryStats;
            List<DailyTrans> pieTrans;
            ArrayList<TransList> arrayList = new ArrayList<>();

            ArrayList<Trans> transList = new ArrayList();
            ArrayList<DailyTrans> dailyTransList = new ArrayList();
            boolean isDailyHeader = true;


            boolean z;
            boolean z2;

            AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(context);
            int accountId = SharePreferenceHelper.getAccountId(context);

            if (startDate == 0 && endDate == 0) {
                List<SubcategoryStats> allSubcategoryStats = appDatabaseObject.statisticDaoObject().getAllSubcategoryStats(accountId, categoryId);
                pieTrans = appDatabaseObject.statisticDaoObject().getAllPieTrans(accountId, categoryId, transMode);
                subcategoryStats = allSubcategoryStats;
            } else {
                subcategoryStats = appDatabaseObject.statisticDaoObject().getSubcategoryStats(accountId, this.startDate, this.endDate, categoryId);
                pieTrans = appDatabaseObject.statisticDaoObject().getPieTrans(accountId, categoryId, this.startDate, this.endDate, transMode);
            }

            if (startDate != 0 && endDate != 0) {

                for (Recurring recurring : appDatabaseObject.recurringDaoObject().getAllRecurringListByAccountId(SharePreferenceHelper.getAccountId(context))) {
                    if (recurring.getIsFuture() == 1 && recurring.getCategoryId() == categoryId) {
                        for (RecurringCalendarDate recurringCalendarDate : RecurringHelper.getStatisticRecurring(recurring, appDatabaseObject.currencyDaoObject().getCurrencyRate(SharePreferenceHelper.getAccountId(context), recurring.getCurrency()), this.startDate, this.endDate)) {
                            Trans trans = new Trans(recurring.getNote(context), recurring.getMemo(), recurring.getColor(), recurring.getIcon(), recurring.getCurrency(), recurringCalendarDate.getDate(), recurring.getAmount(), recurring.getWallet(), recurring.getType(), "", recurring.getWalletId(), 0, recurring.getCategory(context), recurring.getCategoryId(), recurring.getCategoryDefault(), 0, "", 0L, null, 0, 0, 0, 0, "", 0);
                            trans.setRecurring(true);
                            trans.setRecurringId(recurring.getId());
                            transList.add(trans);
                            Iterator<DailyTrans> it = dailyTransList.iterator();
                            while (true) {
                                if (!it.hasNext()) {
                                    z2 = false;
                                    break;
                                }
                                DailyTrans dailyTrans = (DailyTrans) it.next();
                                if (dailyTrans.getYear() == recurringCalendarDate.getYear() && dailyTrans.getMonth() == recurringCalendarDate.getMonth() && dailyTrans.getDay() == recurringCalendarDate.getDay()) {
                                    dailyTrans.setAmount(dailyTrans.getAmount() + recurringCalendarDate.getAmount());
                                    z2 = true;
                                    break;
                                }
                            }
                            if (!z2) {
                                dailyTransList.add(new DailyTrans(recurringCalendarDate.getDay(), recurringCalendarDate.getMonth(), recurringCalendarDate.getYear(), recurringCalendarDate.getAmount()));
                            }
                        }
                    }
                }

                for (DailyTrans dailyTrans : dailyTransList) {
                    Iterator<DailyTrans> it2 = pieTrans.iterator();
                    while (true) {
                        if (!it2.hasNext()) {
                            z = false;
                            break;
                        }
                        DailyTrans next = it2.next();
                        if (dailyTrans.getYear() == next.getYear() && dailyTrans.getMonth() == next.getMonth() && dailyTrans.getDay() == next.getDay()) {
                            next.setAmount(dailyTrans.getAmount() + next.getAmount());
                            z = true;
                            break;
                        }
                    }
                    if (!z) {
                        pieTrans.add(dailyTrans);
                    }

                    Collections.sort(pieTrans, (t1, t2) -> {
                        if (t1.getDateTime() == null || t2.getDateTime() == null) {
                            return 0;
                        }
                        return t2.getDateTime().compareTo(t1.getDateTime());
                    });
                }

                for (DailyTrans dailyTrans : pieTrans) {
                    arrayList.add(new TransList(true, null, dailyTrans));
                    Log.e("@@@@@", "onBindViewHolder: " + dailyTrans.getDateTime() );

                    Date dateTime = dailyTrans.getDateTime();
                    for (Trans trans : appDatabaseObject.statisticDaoObject().getPieTransFromDate(accountId, categoryId, DateHelper.getTransactionStartDate(dateTime), DateHelper.getTransactionEndDate(dateTime), transMode)) {
                        arrayList.add(new TransList(false, trans, null));
                    }
                    for (Trans trans : transList) {
                        java.util.Calendar calendar = java.util.Calendar.getInstance();
                        calendar.setTime(trans.getDateTime());
                        if (calendar.get(1) == dailyTrans.getYear() && calendar.get(2) == dailyTrans.getMonth() - 1 && calendar.get(5) == dailyTrans.getDay()) {
                            arrayList.add(new TransList(false, trans, null));
                        }
                    }

                    Log.e("@@@", "run: " + "5461" );
                    isDailyHeader = true;
                }
            }

            Log.e("@@@", "run: " + "gtgjnob mjvh" );
            runOnUiThread(() -> {
                TransactionPie.AnonymousClass1.this.m231lambda$run$0$comktwappswalletmanagerTransactionPie$1(arrayList, subcategoryStats);
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$run$0$com-ktwapps-walletmanager-TransactionPie$1  reason: not valid java name */
        public /* synthetic */ void m231lambda$run$0$comktwappswalletmanagerTransactionPie$1(List<TransList> list, List<SubcategoryStats> list2) {
            TransactionPie transactionPie;
            int i;
            TransactionPie.this.emptyWrapper.setVisibility(list.size() == 0 ? View.VISIBLE : View.GONE);
            RecyclerView recyclerView = TransactionPie.this.recyclerView;
            if (list.size() == 0) {
                transactionPie = TransactionPie.this;
                i = R.attr.emptyBackground;
            } else {
                transactionPie = TransactionPie.this;
                i = R.attr.primaryBackground;
            }
            recyclerView.setBackgroundColor(Helper.getAttributeColor(transactionPie, i));
            TransactionPie.this.adapter.setMainTransList(list);
            TransactionPie.this.adapter.setSubcategoryStat(list2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void populateData(final long startDate, final long endDate) {
        Executors.newSingleThreadExecutor().execute(new AnonymousClass1(startDate, endDate));
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

    @Override
    // com.ktwapps.walletmanager.Adapter.WalletCategoryTransactionAdapter.OnItemClickListener, com.ktwapps.walletmanager.Adapter.TransactionPieAdapter.OnItemClickListener
    public void OnItemClick(View view, int position) {
        Trans trans = this.adapter.mainTransList.get(position).getTrans();
        if (view.getId() == R.id.image1 || view.getId() == R.id.image2 || view.getId() == R.id.image3) {
            if (trans!=null) {
                Intent intent = new Intent(this, PhotoViewer.class);
                intent.putExtra(ClientCookie.PATH_ATTR, trans.getMedia());
                intent.putExtra(FirebaseAnalytics.Param.INDEX, ((Integer) view.getTag()).intValue());
                startActivity(intent);
            }
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

    @Override
    // com.ktwapps.walletmanager.Adapter.WalletCategoryTransactionAdapter.OnItemClickListener, com.ktwapps.walletmanager.Adapter.TransactionPieAdapter.OnItemClickListener
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
            new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i2, int i1, int i22) {
                    TransactionPie.this.holderEndDate = CalendarHelper.getDateFromPicker(i2, i1, i22);
                    TransactionPie.this.endEditText.setText(DateHelper.getFormattedDate(TransactionPie.this.getApplicationContext(), TransactionPie.this.holderEndDate));
                    if (DateHelper.isBeforeDate(TransactionPie.this.holderStartDate, TransactionPie.this.holderEndDate)) {
                        TransactionPie.this.holderStartDate = CalendarHelper.getDateFromPicker(i2, i1, i22);
                        TransactionPie.this.startEditText.setText(DateHelper.getFormattedDate(TransactionPie.this.getApplicationContext(), TransactionPie.this.holderStartDate));
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
            new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i3, int i1, int i22) {
                    TransactionPie.this.holderStartDate = CalendarHelper.getDateFromPicker(i3, i1, i22);
                    TransactionPie.this.startEditText.setText(DateHelper.getFormattedDate(TransactionPie.this.getApplicationContext(), TransactionPie.this.holderStartDate));
                    if (DateHelper.isBeforeDate(TransactionPie.this.holderStartDate, TransactionPie.this.holderEndDate)) {
                        TransactionPie.this.holderEndDate = CalendarHelper.getDateFromPicker(i3, i1, i22);
                        TransactionPie.this.endEditText.setText(DateHelper.getFormattedDate(TransactionPie.this.getApplicationContext(), TransactionPie.this.holderEndDate));
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
            builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.TransactionPie.4
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    TransactionPie transactionPie = TransactionPie.this;
                    transactionPie.startDate = transactionPie.holderStartDate;
                    TransactionPie transactionPie2 = TransactionPie.this;
                    transactionPie2.endDate = transactionPie2.holderEndDate;
                    TransactionPie.this.dateLabel.setText(CalendarHelper.getFormattedCustomDate(TransactionPie.this.getApplicationContext(), TransactionPie.this.startDate, TransactionPie.this.endDate));
                    TransactionPie.this.populateData(CalendarHelper.getCustomStartDate(TransactionPie.this.startDate), CalendarHelper.getCustomEndDate(TransactionPie.this.endDate));
                    TransactionPie.this.mode = 6;
                    TransactionPie.this.backImage.setAlpha(0.25f);
                    TransactionPie.this.nextImage.setAlpha(0.25f);
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
