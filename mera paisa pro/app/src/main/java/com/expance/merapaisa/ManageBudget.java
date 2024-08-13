package com.expance.merapaisa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.expance.merapaisa.Adapter.BudgetAdapter;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.ViewModel.BudgetViewModel;
import com.expance.merapaisa.Model.Budget;
import com.expance.merapaisa.Utility.BudgetHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class ManageBudget extends AppCompatActivity implements View.OnClickListener, BudgetAdapter.OnItemClickListener {
    BudgetAdapter adapter;
    BudgetViewModel budgetViewModel;
    ConstraintLayout emptyWrapper;
    RecyclerView recyclerView;

    @Override // com.ktwapps.walletmanager.Adapter.BudgetAdapter.OnItemClickListener
    public void onItemLongClick(View view, int position) {
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
        setContentView(R.layout.activity_manage_budget);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        this.budgetViewModel = (BudgetViewModel) new ViewModelProvider(this).get(BudgetViewModel.class);
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
            getSupportActionBar().setTitle(getResources().getString(R.string.budget));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.emptyWrapper = (ConstraintLayout) findViewById(R.id.emptyWrapper);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        BudgetAdapter budgetAdapter = new BudgetAdapter(this);
        this.adapter = budgetAdapter;
        budgetAdapter.setListener(this);
        this.recyclerView.setAdapter(this.adapter);
        ((ExtendedFloatingActionButton) findViewById(R.id.fab)).setOnClickListener(this);
        this.budgetViewModel.getBudgetList().observe(this, new AnonymousClass1());
    }

    public class AnonymousClass1 implements Observer<List<Budget>> {
        AnonymousClass1() {
        }

        @Override // androidx.lifecycle.Observer
        public void onChanged(final List<Budget> budgets) {
            ManageBudget manageBudget;
            int i;
            final ArrayList arrayList = new ArrayList();
            if (budgets != null) {
                Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.ManageBudget.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(ManageBudget.this.getApplicationContext());
                        for (Budget budget : budgets) {
                            Integer valueOf = Integer.valueOf(BudgetHelper.getBudgetType(budget.getPeriod()));
                            if (!arrayList.contains(valueOf)) {
                                arrayList.add(valueOf);
                            }
                            Date time = java.util.Calendar.getInstance().getTime();
                            int budgetType = BudgetHelper.getBudgetType(budget.getPeriod());
                            long budgetStartDate = BudgetHelper.getBudgetStartDate(ManageBudget.this.getApplicationContext(), time, budgetType, budget.getStartDate());
                            long budgetEndDate = BudgetHelper.getBudgetEndDate(ManageBudget.this.getApplicationContext(), time, budgetType, budget.getStartDate());
                            if (budget.getCategoryId() == 0) {
                                budget.setSpent(-appDatabaseObject.budgetDaoObject().getBudgetSpent(budgetStartDate, budgetEndDate, 1, SharePreferenceHelper.getAccountId(ManageBudget.this.getApplicationContext())));
                            } else {
                                budget.setSpent(-appDatabaseObject.budgetDaoObject().getBudgetSpent(appDatabaseObject.budgetDaoObject().getBudgetCategoryIds(budget.getId()), budgetStartDate, budgetEndDate, 1, SharePreferenceHelper.getAccountId(ManageBudget.this.getApplicationContext())));
                            }
                            budget.setTransCount(appDatabaseObject.budgetDaoObject().getBudgetTransCount(SharePreferenceHelper.getAccountId(ManageBudget.this.getApplicationContext()), budget.getId(), budgetStartDate, budgetEndDate));
                            arrayList.add(budget);
                        }
                        ManageBudget.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.ManageBudget.1.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                ManageBudget.this.adapter.setList(arrayList);
                            }
                        });
                    }
                });
            }
            ManageBudget.this.emptyWrapper.setVisibility((budgets == null || budgets.size() <= 0) ? 0 : 8);
            RecyclerView recyclerView = ManageBudget.this.recyclerView;
            if (budgets == null || budgets.size() <= 0) {
                manageBudget = ManageBudget.this;
                i = R.attr.emptyBackground;
            } else {
                manageBudget = ManageBudget.this;
                i = R.attr.primaryBackground;
            }
            recyclerView.setBackgroundColor(Helper.getAttributeColor(manageBudget, i));
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

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.fab) {
            Intent intent = new Intent(this, CreateBudget.class);
            intent.putExtra(JamXmlElements.TYPE, 2);
            startActivity(intent);
            overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
        }
    }

    @Override // com.ktwapps.walletmanager.Adapter.BudgetAdapter.OnItemClickListener
    public void onItemClick(View view, int position) {
        Object obj = this.adapter.getList().get(position);
        if (obj instanceof Integer) {
            Intent intent = new Intent(this, BudgetExpanded.class);
            intent.putExtra(JamXmlElements.TYPE, (Integer) obj);
            startActivity(intent);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
            return;
        }
        Intent intent2 = new Intent(this, BudgetDetail.class);
        intent2.putExtra("budgetId", ((Budget) this.adapter.getList().get(position)).getId());
        intent2.putExtra("date", java.util.Calendar.getInstance().getTimeInMillis());
        startActivity(intent2);
        overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
    }
}
