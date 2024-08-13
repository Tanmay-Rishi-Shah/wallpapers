package com.expance.merapaisa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.expance.merapaisa.Adapter.BudgetExpandedAdapter;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.ViewModel.BudgetExpandedViewModel;
import com.expance.merapaisa.Database.ViewModel.ModelFactory;
import com.expance.merapaisa.Model.Budget;
import com.expance.merapaisa.Utility.BudgetHelper;
import com.expance.merapaisa.Utility.CalendarHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class BudgetExpanded extends AppCompatActivity implements View.OnClickListener, BudgetExpandedAdapter.OnItemClickListener {
    private BudgetExpandedAdapter adapter;
    private BudgetExpandedViewModel budgetExpandedViewModel;
    private Date date;
    private TextView dateLabel;
    private ConstraintLayout emptyWrapper;
    private RecyclerView recyclerView;
    private int type;

    @Override // com.ktwapps.walletmanager.Adapter.BudgetExpandedAdapter.OnItemClickListener
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

        setContentView(R.layout.activity_budget_expanded);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        this.type = getIntent().getIntExtra(JamXmlElements.TYPE, 0);
        this.date = java.util.Calendar.getInstance().getTime();
        this.budgetExpandedViewModel = (BudgetExpandedViewModel) new ViewModelProvider(this, new ModelFactory(getApplication(), this.type)).get(BudgetExpandedViewModel.class);
        BudgetExpandedAdapter budgetExpandedAdapter = new BudgetExpandedAdapter(this, this.date);
        this.adapter = budgetExpandedAdapter;
        budgetExpandedAdapter.setListener(this);
        setUpLayout();
        populateData();
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

    private String getTitle(int type) {
        if (type == 0) {
            return getResources().getString(R.string.budget_weekly);
        }
        if (type == 1) {
            return getResources().getString(R.string.budget_monthly);
        }
        if (type == 2) {
            return getResources().getString(R.string.budget_quarterly);
        }
        return getResources().getString(R.string.budget_yearly);
    }

    private void setUpLayout() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getTitle(this.type));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.dateLabel = (TextView) findViewById(R.id.dateLabel);
        this.emptyWrapper = (ConstraintLayout) findViewById(R.id.emptyWrapper);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setAdapter(this.adapter);
        this.dateLabel.setText(CalendarHelper.getBudgetFormattedDate(getApplicationContext(), this.date, this.type));
        ((ExtendedFloatingActionButton) findViewById(R.id.fab)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.backImage)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.nextImage)).setOnClickListener(this);
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

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.ktwapps.walletmanager.BudgetExpanded$1  reason: invalid class name */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 implements Observer<List<Budget>> {
        AnonymousClass1() {
        }

        @Override // androidx.lifecycle.Observer
        public void onChanged(final List<Budget> budgets) {
            final ArrayList arrayList = new ArrayList();
            if (budgets != null) {
                Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.BudgetExpanded.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(BudgetExpanded.this.getApplicationContext());
                        for (Budget budget : budgets) {
                            long budgetStartDate = BudgetHelper.getBudgetStartDate(BudgetExpanded.this.getApplicationContext(), BudgetExpanded.this.date, BudgetExpanded.this.type, budget.getStartDate());
                            long budgetEndDate = BudgetHelper.getBudgetEndDate(BudgetExpanded.this.getApplicationContext(), BudgetExpanded.this.date, BudgetExpanded.this.type, budget.getStartDate());
                            if (budget.getCategoryId() == 0) {
                                budget.setSpent(-appDatabaseObject.budgetDaoObject().getBudgetSpent(budgetStartDate, budgetEndDate, 1, SharePreferenceHelper.getAccountId(BudgetExpanded.this.getApplicationContext())));
                            } else {
                                budget.setSpent(-appDatabaseObject.budgetDaoObject().getBudgetSpent(appDatabaseObject.budgetDaoObject().getBudgetCategoryIds(budget.getId()), budgetStartDate, budgetEndDate, 1, SharePreferenceHelper.getAccountId(BudgetExpanded.this.getApplicationContext())));
                            }
                            budget.setTransCount(appDatabaseObject.budgetDaoObject().getBudgetTransCount(SharePreferenceHelper.getAccountId(BudgetExpanded.this.getApplicationContext()), budget.getId(), budgetStartDate, budgetEndDate));
                            arrayList.add(budget);
                        }
                        BudgetExpanded.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.BudgetExpanded.1.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                BudgetExpanded budgetExpanded;
                                int i;
                                BudgetExpanded.this.emptyWrapper.setVisibility(arrayList.size() == 0 ? 0 : 8);
                                RecyclerView recyclerView = BudgetExpanded.this.recyclerView;
                                if (arrayList.size() == 0) {
                                    budgetExpanded = BudgetExpanded.this;
                                    i = R.attr.emptyBackground;
                                } else {
                                    budgetExpanded = BudgetExpanded.this;
                                    i = R.attr.primaryBackground;
                                }
                                recyclerView.setBackgroundColor(Helper.getAttributeColor(budgetExpanded, i));
                                BudgetExpanded.this.adapter.setList(arrayList);
                            }
                        });
                    }
                });
            }
        }
    }

    private void populateData() {
        this.budgetExpandedViewModel.getBudgetList().observe(this, new AnonymousClass1());
    }

    private void updateSpent() {
        final List<Budget> list = this.adapter.getList();
        final ArrayList arrayList = new ArrayList();
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.BudgetExpanded.2
            @Override // java.lang.Runnable
            public void run() {
                for (Budget budget : list) {
                    AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(BudgetExpanded.this.getApplicationContext());
                    long budgetStartDate = BudgetHelper.getBudgetStartDate(BudgetExpanded.this.getApplicationContext(), BudgetExpanded.this.date, BudgetExpanded.this.type, budget.getStartDate());
                    long budgetEndDate = BudgetHelper.getBudgetEndDate(BudgetExpanded.this.getApplicationContext(), BudgetExpanded.this.date, BudgetExpanded.this.type, budget.getStartDate());
                    if (budget.getCategoryId() == 0) {
                        budget.setSpent(-appDatabaseObject.budgetDaoObject().getBudgetSpent(budgetStartDate, budgetEndDate, 1, SharePreferenceHelper.getAccountId(BudgetExpanded.this.getApplicationContext())));
                    } else {
                        budget.setSpent(-appDatabaseObject.budgetDaoObject().getBudgetSpent(appDatabaseObject.budgetDaoObject().getBudgetCategoryIds(budget.getId()), budgetStartDate, budgetEndDate, 1, SharePreferenceHelper.getAccountId(BudgetExpanded.this.getApplicationContext())));
                    }
                    budget.setTransCount(appDatabaseObject.budgetDaoObject().getBudgetTransCount(SharePreferenceHelper.getAccountId(BudgetExpanded.this.getApplicationContext()), budget.getId(), budgetStartDate, budgetEndDate));
                    arrayList.add(budget);
                    BudgetExpanded.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.BudgetExpanded.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            BudgetExpanded.this.adapter.setList(arrayList);
                        }
                    });
                }
            }
        });
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.backImage) {
            this.date = BudgetHelper.getIncrementDate(this.date, this.type, -1);
            this.dateLabel.setText(CalendarHelper.getBudgetFormattedDate(getApplicationContext(), this.date, this.type));
            updateSpent();
            this.adapter.setDate(this.date);
        } else if (id == R.id.fab) {
            Intent intent = new Intent(this, CreateBudget.class);
            intent.putExtra(JamXmlElements.TYPE, 2);
            intent.putExtra(TypedValues.CycleType.S_WAVE_PERIOD, this.type);
            startActivity(intent);
            overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
        } else if (id != R.id.nextImage) {
        } else {
            this.date = BudgetHelper.getIncrementDate(this.date, this.type, 1);
            this.dateLabel.setText(CalendarHelper.getBudgetFormattedDate(getApplicationContext(), this.date, this.type));
            updateSpent();
            this.adapter.setDate(this.date);
        }
    }

    @Override // com.ktwapps.walletmanager.Adapter.BudgetExpandedAdapter.OnItemClickListener
    public void onItemClick(View view, int position) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(this.date);
        Intent intent = new Intent(getApplicationContext(), BudgetDetail.class);
        intent.putExtra("budgetId", this.adapter.getList().get(position).getId());
        intent.putExtra("date", calendar.getTimeInMillis());
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
    }
}
