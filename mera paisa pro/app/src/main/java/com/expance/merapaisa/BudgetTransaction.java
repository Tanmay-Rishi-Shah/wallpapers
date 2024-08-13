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
import com.expance.merapaisa.Adapter.BudgetTransactionAdapter;
import com.expance.merapaisa.Broadcast.BroadcastUpdated;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Model.DailyTrans;
import com.expance.merapaisa.Model.Trans;
import com.expance.merapaisa.Model.TransList;
import com.expance.merapaisa.Utility.Constant;
import com.expance.merapaisa.Utility.DateHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import com.expance.merapaisa.Utility.TransactionHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;
import org.apache.http.cookie.ClientCookie;

import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class BudgetTransaction extends AppCompatActivity implements BudgetTransactionAdapter.OnItemClickListener, BroadcastUpdated.OnBroadcastListener {
    BudgetTransactionAdapter adapter;
    BroadcastUpdated broadcastUpdated;
    int categoryId;
    ConstraintLayout emptyWrapper;
    long endDate;
    String name;
    RecyclerView recyclerView;
    long startDate;

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
        setContentView(R.layout.activity_budget_transaction);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        BroadcastUpdated broadcastUpdated = new BroadcastUpdated();
        this.broadcastUpdated = broadcastUpdated;
        broadcastUpdated.setListener(this);
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        this.categoryId = getIntent().getIntExtra("categoryId", 0);
        this.startDate = getIntent().getLongExtra("startDate", 0L);
        this.endDate = getIntent().getLongExtra("endDate", 0L);
        this.name = getIntent().getStringExtra("name");
        BudgetTransactionAdapter budgetTransactionAdapter = new BudgetTransactionAdapter(this);
        this.adapter = budgetTransactionAdapter;
        budgetTransactionAdapter.setListener(this);
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
            getSupportActionBar().setTitle(this.name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setAdapter(this.adapter);
        this.emptyWrapper = (ConstraintLayout) findViewById(R.id.emptyWrapper);
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
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.BudgetTransaction.1
            @Override // java.lang.Runnable
            public void run() {
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(BudgetTransaction.this.getApplicationContext());
                int accountId = SharePreferenceHelper.getAccountId(BudgetTransaction.this.getApplicationContext());
                final ArrayList arrayList = new ArrayList();
                for (DailyTrans dailyTrans : appDatabaseObject.categoryDaoObject().getDailyTrans(accountId, BudgetTransaction.this.categoryId, BudgetTransaction.this.startDate, BudgetTransaction.this.endDate)) {
                    arrayList.add(new TransList(true, null, dailyTrans));
                    Date dateTime = dailyTrans.getDateTime();
                    for (Trans trans : appDatabaseObject.categoryDaoObject().getTransFromDate(accountId, BudgetTransaction.this.categoryId, DateHelper.getTransactionStartDate(dateTime), DateHelper.getTransactionEndDate(dateTime))) {
                        arrayList.add(new TransList(false, trans, null));
                    }
                }
                BudgetTransaction.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.BudgetTransaction.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        BudgetTransaction budgetTransaction;
                        int i;
                        BudgetTransaction.this.adapter.setList(arrayList);
                        BudgetTransaction.this.emptyWrapper.setVisibility(arrayList.size() == 0 ? 0 : 8);
                        RecyclerView recyclerView = BudgetTransaction.this.recyclerView;
                        if (arrayList.size() == 0) {
                            budgetTransaction = BudgetTransaction.this;
                            i = R.attr.emptyBackground;
                        } else {
                            budgetTransaction = BudgetTransaction.this;
                            i = R.attr.primaryBackground;
                        }
                        recyclerView.setBackgroundColor(Helper.getAttributeColor(budgetTransaction, i));
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

    @Override // com.ktwapps.walletmanager.Adapter.BudgetTransactionAdapter.OnItemClickListener
    public void OnItemClick(View view, int position) {
        Trans trans = this.adapter.getList().get(position).getTrans();
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

    @Override // com.ktwapps.walletmanager.Adapter.BudgetTransactionAdapter.OnItemClickListener
    public void onItemLongClick(View view, int position) {
        TransactionHelper.showPopupMenu(this, this.adapter.getList().get(position).getTrans(), view);
    }

    @Override // com.ktwapps.walletmanager.Broadcast.BroadcastUpdated.OnBroadcastListener
    public void getBroadcast() {
        populateData();
    }
}
