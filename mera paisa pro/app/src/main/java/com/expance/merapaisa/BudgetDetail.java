package com.expance.merapaisa;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.expance.merapaisa.Adapter.BudgetDetailAdapter;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.ViewModel.BudgetDetailViewModel;
import com.expance.merapaisa.Database.ViewModel.ModelFactory;
import com.expance.merapaisa.Model.Budget;
import com.expance.merapaisa.Model.BudgetStats;
import com.expance.merapaisa.Model.BudgetTrans;
import com.expance.merapaisa.Utility.BillingHelper;
import com.expance.merapaisa.Utility.BudgetHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class BudgetDetail extends AppCompatActivity implements BudgetDetailAdapter.OnItemClickListener, BillingHelper.BillingListener {
    private BudgetDetailAdapter adapter;
    private BillingHelper billingHelper;
    private Budget budget;
    private BudgetDetailViewModel budgetDetailViewModel;
    private int budgetId;
    private Date date;

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onSkuReady() {
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
        setContentView(R.layout.activity_budget_detail);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        this.budgetId = getIntent().getIntExtra("budgetId", 0);
        this.date = BudgetHelper.getBudgetDateFromMilli(getIntent().getLongExtra("date", java.util.Calendar.getInstance().getTime().getTime()));
        this.adapter = new BudgetDetailAdapter(this, this.date);
        this.budgetDetailViewModel = (BudgetDetailViewModel) new ViewModelProvider(this, new ModelFactory(getApplication(), this.budgetId)).get(BudgetDetailViewModel.class);
        this.billingHelper = new BillingHelper(this);
        setUpLayout();
        this.billingHelper.setListener(this);
        this.billingHelper.setUpBillingClient();
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

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        this.billingHelper.registerBroadCast(this);
        this.billingHelper.queryPurchases();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        this.billingHelper.unregisterBroadCast(this);
    }

    private void setUpLayout() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.budget);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(this.adapter);
        this.adapter.setListener(this);
        populateData();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.ktwapps.walletmanager.BudgetDetail$1  reason: invalid class name */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 implements Observer<Budget> {
        AnonymousClass1() {
        }

        @Override // androidx.lifecycle.Observer
        public void onChanged(final Budget budget) {
            BudgetDetail.this.budget = budget;
            if (budget != null) {
                Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.BudgetDetail.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        final List<BudgetStats> budgetStats;
                        final List<BudgetTrans> budgetTrans;
                        AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(BudgetDetail.this.getApplicationContext());
                        int budgetType = BudgetHelper.getBudgetType(budget.getPeriod());
                        long budgetStartDate = BudgetHelper.getBudgetStartDate(BudgetDetail.this.getApplicationContext(), BudgetDetail.this.date, budgetType, budget.getStartDate());
                        long budgetEndDate = BudgetHelper.getBudgetEndDate(BudgetDetail.this.getApplicationContext(), BudgetDetail.this.date, budgetType, budget.getStartDate());
                        if (budget.getCategoryId() != 0) {
                            budget.setSpent(-appDatabaseObject.budgetDaoObject().getBudgetSpent(appDatabaseObject.budgetDaoObject().getBudgetCategoryIds(BudgetDetail.this.budgetId), budgetStartDate, budgetEndDate, 1, SharePreferenceHelper.getAccountId(BudgetDetail.this.getApplicationContext())));
                        } else {
                            budget.setSpent(-appDatabaseObject.budgetDaoObject().getBudgetSpent(budgetStartDate, budgetEndDate, 1, SharePreferenceHelper.getAccountId(BudgetDetail.this.getApplicationContext())));
                        }
                        if (budget.getCategoryId() != 0) {
                            budgetStats = appDatabaseObject.budgetDaoObject().getBudgetStats(BudgetDetail.this.budgetId, SharePreferenceHelper.getAccountId(BudgetDetail.this.getApplicationContext()), budgetStartDate, budgetEndDate);
                            budgetTrans = appDatabaseObject.budgetDaoObject().getBudgetTrans(SharePreferenceHelper.getAccountId(BudgetDetail.this.getApplicationContext()), BudgetDetail.this.budgetId, budgetStartDate, budgetEndDate);
                        } else {
                            budgetStats = appDatabaseObject.budgetDaoObject().getBudgetStats(SharePreferenceHelper.getAccountId(BudgetDetail.this.getApplicationContext()), budgetStartDate, budgetEndDate);
                            budgetTrans = appDatabaseObject.budgetDaoObject().getBudgetTrans(SharePreferenceHelper.getAccountId(BudgetDetail.this.getApplicationContext()), budgetStartDate, budgetEndDate);
                        }
                        BudgetDetail.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.BudgetDetail.1.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                BudgetDetail.this.adapter.setBudget(budget);
                                BudgetDetail.this.adapter.setStatsList(budgetStats);
                                BudgetDetail.this.adapter.setBudgetTransList(budgetTrans);
                                BudgetDetail.this.adapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
            }
        }
    }

    private void populateData() {
        this.budgetDetailViewModel.getBudget().observe(this, new AnonymousClass1());
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

    private void editBudget() {
        Intent intent = new Intent(this, CreateBudget.class);
        intent.putExtra(JamXmlElements.TYPE, -2);
        intent.putExtra("budgetId", this.budgetId);
        startActivity(intent);
        overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
    }

    private void deleteBudget() {
        Helper.showDialog(this, "", getResources().getString(R.string.budget_delete_message), getResources().getString(R.string.delete_positive), getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.BudgetDetail.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.BudgetDetail.2.1
                    @Override // java.lang.Runnable
                    public void run() {
                        AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(BudgetDetail.this.getApplicationContext());
                        appDatabaseObject.budgetDaoObject().deleteBudget(BudgetDetail.this.budgetId);
                        appDatabaseObject.budgetDaoObject().deleteBudgetCategory(BudgetDetail.this.budgetId);
                        BudgetDetail.this.finish();
                        BudgetDetail.this.overridePendingTransition(R.anim.scale_in, R.anim.right_to_left);
                    }
                });
            }
        });
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_action_edit) {
            editBudget();
            return false;
        } else if (item.getItemId() == R.id.menu_action_delete) {
            deleteBudget();
            return false;
        } else {
            return false;
        }
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        for (int i = 0; i < menu.size(); i++) {
            Drawable icon = menu.getItem(i).getIcon();
            if (icon != null) {
                icon.mutate();
                icon.setColorFilter(Helper.getAttributeColor(this, R.attr.primaryDarkTextColor), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return true;
    }

    @Override // com.ktwapps.walletmanager.Adapter.BudgetDetailAdapter.OnItemClickListener
    public void OnItemClick(View v, int position) {
        BudgetTrans budgetTrans = this.adapter.getTransList().get(position);
        int budgetType = BudgetHelper.getBudgetType(this.budget.getPeriod());
        long budgetStartDate = BudgetHelper.getBudgetStartDate(getApplicationContext(), this.date, budgetType, this.budget.getStartDate());
        long budgetEndDate = BudgetHelper.getBudgetEndDate(getApplicationContext(), this.date, budgetType, this.budget.getStartDate());
        int id = budgetTrans.getId();
        String name = budgetTrans.getName(getApplicationContext());
        Intent intent = new Intent(getApplicationContext(), BudgetTransaction.class);
        intent.putExtra("startDate", budgetStartDate);
        intent.putExtra("endDate", budgetEndDate);
        intent.putExtra("categoryId", id);
        intent.putExtra("name", name);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
    }

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onLoaded() {
        checkSubscription();
    }

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onPurchasedSucceed() {
        checkSubscription();
    }

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onPurchasedPending() {
        checkSubscription();
    }

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onReceiveBroadCast() {
        checkSubscription();
    }

    private void checkSubscription() {
        if (SharePreferenceHelper.getPremium(this) == 2) {
            this.adapter.setAds(false);
        } else {
            this.adapter.setAds(true);
        }
    }
}
