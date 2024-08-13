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
import com.expance.merapaisa.Adapter.WalletCategoryTransactionAdapter;
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
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class WalletCategoryTransaction extends AppCompatActivity implements WalletCategoryTransactionAdapter.OnItemClickListener, BroadcastUpdated.OnBroadcastListener {
    WalletCategoryTransactionAdapter adapter;
    BroadcastUpdated broadcastUpdated;
    int categoryId;
    ConstraintLayout emptyWrapper;
    String name;
    RecyclerView recyclerView;
    String symbol;
    int type;
    int walletId;

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
        setContentView(R.layout.activity_wallet_category_transaction);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        BroadcastUpdated broadcastUpdated = new BroadcastUpdated();
        this.broadcastUpdated = broadcastUpdated;
        broadcastUpdated.setListener(this);
        this.categoryId = getIntent().getIntExtra("categoryId", 0);
        this.walletId = getIntent().getIntExtra("walletId", 0);
        this.symbol = getIntent().getStringExtra("symbol");
        this.type = getIntent().getIntExtra(JamXmlElements.TYPE, 0);
        this.name = getIntent().getStringExtra("name");
        WalletCategoryTransactionAdapter walletCategoryTransactionAdapter = new WalletCategoryTransactionAdapter(this);
        this.adapter = walletCategoryTransactionAdapter;
        walletCategoryTransactionAdapter.setSymbol(this.symbol);
        this.adapter.setWalletId(this.walletId);
        this.adapter.setListener(this);
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

    private String getName() {
        return this.type == 2 ? getResources().getString(R.string.transfer) : this.name;
    }

    private void setUpLayout() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getName());
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
        if (this.type == 2) {
            Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.WalletCategoryTransaction.1
                @Override // java.lang.Runnable
                public void run() {
                    AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(WalletCategoryTransaction.this.getApplicationContext());
                    int accountId = SharePreferenceHelper.getAccountId(WalletCategoryTransaction.this.getApplicationContext());
                    final ArrayList arrayList = new ArrayList();
                    for (DailyTrans dailyTrans : appDatabaseObject.walletDaoObject().getDailyTrans(accountId, WalletCategoryTransaction.this.walletId)) {
                        arrayList.add(new TransList(true, null, dailyTrans));
                        Date dateTime = dailyTrans.getDateTime();
                        for (Trans trans : appDatabaseObject.walletDaoObject().getTransFromDate(accountId, WalletCategoryTransaction.this.walletId, DateHelper.getTransactionStartDate(dateTime), DateHelper.getTransactionEndDate(dateTime))) {
                            arrayList.add(new TransList(false, trans, null));
                        }
                    }
                    WalletCategoryTransaction.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.WalletCategoryTransaction.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            WalletCategoryTransaction walletCategoryTransaction;
                            int i;
                            WalletCategoryTransaction.this.emptyWrapper.setVisibility(arrayList.size() == 0 ? 0 : 8);
                            RecyclerView recyclerView = WalletCategoryTransaction.this.recyclerView;
                            if (arrayList.size() == 0) {
                                walletCategoryTransaction = WalletCategoryTransaction.this;
                                i = R.attr.emptyBackground;
                            } else {
                                walletCategoryTransaction = WalletCategoryTransaction.this;
                                i = R.attr.primaryBackground;
                            }
                            recyclerView.setBackgroundColor(Helper.getAttributeColor(walletCategoryTransaction, i));
                            WalletCategoryTransaction.this.adapter.setList(arrayList);
                        }
                    });
                }
            });
        } else {
            Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.WalletCategoryTransaction.2
                @Override // java.lang.Runnable
                public void run() {
                    AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(WalletCategoryTransaction.this.getApplicationContext());
                    int accountId = SharePreferenceHelper.getAccountId(WalletCategoryTransaction.this.getApplicationContext());
                    final ArrayList arrayList = new ArrayList();
                    for (DailyTrans dailyTrans : appDatabaseObject.walletDaoObject().getDailyTrans(accountId, WalletCategoryTransaction.this.walletId, WalletCategoryTransaction.this.categoryId)) {
                        arrayList.add(new TransList(true, null, dailyTrans));
                        Date dateTime = dailyTrans.getDateTime();
                        for (Trans trans : appDatabaseObject.walletDaoObject().getTransFromDate(accountId, WalletCategoryTransaction.this.walletId, WalletCategoryTransaction.this.categoryId, DateHelper.getTransactionStartDate(dateTime), DateHelper.getTransactionEndDate(dateTime))) {
                            arrayList.add(new TransList(false, trans, null));
                        }
                    }
                    WalletCategoryTransaction.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.WalletCategoryTransaction.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            WalletCategoryTransaction walletCategoryTransaction;
                            int i;
                            WalletCategoryTransaction.this.emptyWrapper.setVisibility(arrayList.size() == 0 ? 0 : 8);
                            RecyclerView recyclerView = WalletCategoryTransaction.this.recyclerView;
                            if (arrayList.size() == 0) {
                                walletCategoryTransaction = WalletCategoryTransaction.this;
                                i = R.attr.emptyBackground;
                            } else {
                                walletCategoryTransaction = WalletCategoryTransaction.this;
                                i = R.attr.primaryBackground;
                            }
                            recyclerView.setBackgroundColor(Helper.getAttributeColor(walletCategoryTransaction, i));
                            WalletCategoryTransaction.this.adapter.setList(arrayList);
                        }
                    });
                }
            });
        }
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

    @Override // com.ktwapps.walletmanager.Adapter.WalletCategoryTransactionAdapter.OnItemClickListener, com.ktwapps.walletmanager.Adapter.TransactionPieAdapter.OnItemClickListener
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

    @Override // com.ktwapps.walletmanager.Adapter.WalletCategoryTransactionAdapter.OnItemClickListener, com.ktwapps.walletmanager.Adapter.TransactionPieAdapter.OnItemClickListener
    public void OnItemLongClick(View view, int position) {
        TransactionHelper.showPopupMenu(this, this.adapter.list.get(position).getTrans(), view);
    }

    @Override // com.ktwapps.walletmanager.Broadcast.BroadcastUpdated.OnBroadcastListener
    public void getBroadcast() {
        populateData();
    }
}
