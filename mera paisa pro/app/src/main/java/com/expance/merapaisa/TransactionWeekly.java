package com.expance.merapaisa;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.expance.merapaisa.Adapter.TransactionWeeklyAdapter;
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
import com.expance.merapaisa.Utility.TransactionHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import org.apache.http.cookie.ClientCookie;

import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class TransactionWeekly extends AppCompatActivity implements TransactionWeeklyAdapter.OnItemClickListener, BroadcastUpdated.OnBroadcastListener {
    TransactionWeeklyAdapter adapter;
    BroadcastUpdated broadcastUpdated;
    Date date;
    ConstraintLayout emptyWrapper;
    RecyclerView recyclerView;

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
        setContentView(R.layout.activity_transaction_weekly);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        BroadcastUpdated broadcastUpdated = new BroadcastUpdated();
        this.broadcastUpdated = broadcastUpdated;
        broadcastUpdated.setListener(this);
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeInMillis(getIntent().getLongExtra("dateTime", 0L));
        this.date = calendar.getTime();
        TransactionWeeklyAdapter transactionWeeklyAdapter = new TransactionWeeklyAdapter(this);
        this.adapter = transactionWeeklyAdapter;
        transactionWeeklyAdapter.setListener(this);
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
            getSupportActionBar().setTitle(R.string.transaction);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.emptyWrapper = (ConstraintLayout) findViewById(R.id.emptyWrapper);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setAdapter(this.adapter);
        populateData();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        populateData();
        LocalBroadcastManager.getInstance(this).registerReceiver(this.broadcastUpdated, new IntentFilter(Constant.BROADCAST_UPDATED));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.broadcastUpdated);
    }

    private void populateData() {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.TransactionWeekly.1
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r9v0 */
            /* JADX WARN: Type inference failed for: r9v1, types: [int, boolean] */
            /* JADX WARN: Type inference failed for: r9v2 */
            @Override // java.lang.Runnable
            public void run() {
                AppDatabaseObject appDatabaseObject;
                boolean z;
                boolean z2;
                AppDatabaseObject appDatabaseObject2;
                long j;
                boolean z3;
                long dailyStartDate = CalendarHelper.getDailyStartDate(TransactionWeekly.this.date);
                long dailyEndDate = CalendarHelper.getDailyEndDate(TransactionWeekly.this.date);
                AppDatabaseObject appDatabaseObject3 = AppDatabaseObject.getInstance(TransactionWeekly.this.getApplicationContext());
                int accountId = SharePreferenceHelper.getAccountId(TransactionWeekly.this.getApplicationContext());
                ArrayList arrayList = new ArrayList();
                List<DailyTrans> weeklyTrans = appDatabaseObject3.statisticDaoObject().getWeeklyTrans(accountId, dailyStartDate, dailyEndDate);
                ArrayList arrayList2 = new ArrayList();
                ArrayList<DailyTrans> arrayList3 = new ArrayList();
                int i = 1;
                int r9;
                if (dailyStartDate == 0 || dailyEndDate == 0) {
                    appDatabaseObject = appDatabaseObject3;
                    z = false;
                    r9 = 1;
                } else {
                    for (Recurring recurring : appDatabaseObject3.recurringDaoObject().getAllRecurringListByAccountId(SharePreferenceHelper.getAccountId(TransactionWeekly.this.getApplicationContext()))) {
                        if (recurring.getIsFuture() == i && recurring.getType() == i) {
                            appDatabaseObject2 = appDatabaseObject3;
                            j = dailyStartDate;
                            for (RecurringCalendarDate recurringCalendarDate : RecurringHelper.getStatisticRecurring(recurring, appDatabaseObject3.currencyDaoObject().getCurrencyRate(SharePreferenceHelper.getAccountId(TransactionWeekly.this.getApplicationContext()), recurring.getCurrency()), dailyStartDate, dailyEndDate)) {
                                Trans trans = new Trans(recurring.getNote(TransactionWeekly.this.getApplicationContext()), "", recurring.getColor(), recurring.getIcon(), recurring.getCurrency(), recurringCalendarDate.getDate(), recurring.getAmount(), recurring.getWallet(), recurring.getType(), "", recurring.getWalletId(), 0, recurring.getCategory(TransactionWeekly.this.getApplicationContext()), recurring.getCategoryId(), recurring.getCategoryDefault(), 0, "", 0L, null, 0, 0, 0, 0, "", 0);
                                trans.setRecurring(true);
                                trans.setRecurringId(recurring.getId());
                                arrayList2.add(trans);
                                Iterator it = arrayList3.iterator();
                                while (true) {
                                    if (!it.hasNext()) {
                                        z3 = false;
                                        break;
                                    }
                                    DailyTrans dailyTrans = (DailyTrans) it.next();
                                    if (dailyTrans.getYear() == recurringCalendarDate.getYear() && dailyTrans.getMonth() == recurringCalendarDate.getMonth() && dailyTrans.getDay() == recurringCalendarDate.getDay()) {
                                        dailyTrans.setAmount(dailyTrans.getAmount() + recurringCalendarDate.getAmount());
                                        z3 = true;
                                        break;
                                    }
                                }
                                if (!z3) {
                                    arrayList3.add(new DailyTrans(recurringCalendarDate.getDay(), recurringCalendarDate.getMonth(), recurringCalendarDate.getYear(), recurringCalendarDate.getAmount()));
                                }
                            }
                        } else {
                            appDatabaseObject2 = appDatabaseObject3;
                            j = dailyStartDate;
                        }
                        appDatabaseObject3 = appDatabaseObject2;
                        dailyStartDate = j;
                        i = 1;
                    }
                    appDatabaseObject = appDatabaseObject3;
                    z = false;
                    r9 = 1;
                    for (DailyTrans dailyTrans2 : arrayList3) {
                        Iterator<DailyTrans> it2 = weeklyTrans.iterator();
                        while (true) {
                            if (!it2.hasNext()) {
                                z2 = false;
                                break;
                            }
                            DailyTrans next = it2.next();
                            if (dailyTrans2.getYear() == next.getYear() && dailyTrans2.getMonth() == next.getMonth() && dailyTrans2.getDay() == next.getDay()) {
                                next.setAmount(dailyTrans2.getAmount() + next.getAmount());
                                z2 = true;
                                break;
                            }
                        }
                        if (!z2) {
                            weeklyTrans.add(dailyTrans2);
                        }
                    }
                    Collections.sort(weeklyTrans, new Comparator<DailyTrans>() { // from class: com.ktwapps.walletmanager.TransactionWeekly.1.1
                        @Override // java.util.Comparator
                        public int compare(DailyTrans t1, DailyTrans t2) {
                            if (t1.getDateTime() == null || t2.getDateTime() == null) {
                                return 0;
                            }
                            return t2.getDateTime().compareTo(t1.getDateTime());
                        }
                    });
                }
                for (DailyTrans dailyTrans3 : weeklyTrans) {
                    arrayList.add(new TransList(false, null, dailyTrans3));
                    Date dateTime = dailyTrans3.getDateTime();
                    long transactionStartDate = DateHelper.getTransactionStartDate(dateTime);
                    long transactionEndDate = DateHelper.getTransactionEndDate(dateTime);
                    ArrayList arrayList4 = arrayList;
                    ArrayList<Trans> arrayList5 = arrayList2;
                    for (Trans trans2 : appDatabaseObject.statisticDaoObject().getWeeklyTransFromDate(accountId, transactionStartDate, transactionEndDate)) {
                        arrayList4.add(new TransList(z, trans2, null));
                    }
                    for (Trans trans3 : arrayList5) {
                        java.util.Calendar calendar = java.util.Calendar.getInstance();
                        calendar.setTime(trans3.getDateTime());
                        if (calendar.get(r9) == dailyTrans3.getYear() && calendar.get(2) == dailyTrans3.getMonth() - r9 && calendar.get(5) == dailyTrans3.getDay()) {
                            arrayList4.add(new TransList(z, trans3, null));
                        }
                    }
                    arrayList = arrayList4;
                    arrayList2 = arrayList5;
                }
                final ArrayList arrayList6 = arrayList;
                TransactionWeekly.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.TransactionWeekly.1.2
                    @Override // java.lang.Runnable
                    public void run() {
                        TransactionWeekly transactionWeekly;
                        int i2;
                        TransactionWeekly.this.emptyWrapper.setVisibility(arrayList6.size() == 0 ? 0 : 8);
                        RecyclerView recyclerView = TransactionWeekly.this.recyclerView;
                        if (arrayList6.size() == 0) {
                            transactionWeekly = TransactionWeekly.this;
                            i2 = R.attr.emptyBackground;
                        } else {
                            transactionWeekly = TransactionWeekly.this;
                            i2 = R.attr.primaryBackground;
                        }
                        recyclerView.setBackgroundColor(Helper.getAttributeColor(transactionWeekly, i2));
                        TransactionWeekly.this.adapter.setList(arrayList6);
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

    @Override // com.ktwapps.walletmanager.Adapter.TransactionWeeklyAdapter.OnItemClickListener
    public void OnItemClick(View view, int position) {
        Trans trans = this.adapter.list.get(position).getTrans();
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

    @Override // com.ktwapps.walletmanager.Adapter.TransactionWeeklyAdapter.OnItemClickListener
    public void OnItemLongClick(View view, int position) {
        TransactionHelper.showPopupMenu(this, this.adapter.list.get(position).getTrans(), view);
    }

    @Override // com.ktwapps.walletmanager.Broadcast.BroadcastUpdated.OnBroadcastListener
    public void getBroadcast() {
        populateData();
    }
}
