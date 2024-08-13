package com.expance.merapaisa;

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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.expance.merapaisa.Adapter.RecurringAdapter;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.ViewModel.RecurringViewModel;
import com.expance.merapaisa.Model.Recurring;
import com.expance.merapaisa.Utility.BillingHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.List;
import java.util.concurrent.Executors;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class ManageRecurring extends AppCompatActivity implements RecurringAdapter.OnItemClickListener, BillingHelper.BillingListener {
    RecurringAdapter adapter;
    BillingHelper billingHelper;
    ConstraintLayout emptyWrapper;
    RecurringViewModel recurringViewModel;
    RecyclerView recyclerView;

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onLoaded() {
    }

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onPurchasedPending() {
    }

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onPurchasedSucceed() {
    }

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onReceiveBroadCast() {
    }

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
        setContentView(R.layout.activity_manage_recurring);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        setUpLayout();
        BillingHelper billingHelper = new BillingHelper(this);
        this.billingHelper = billingHelper;
        billingHelper.setListener(this);
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

    public void setUpLayout() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.recurring);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.emptyWrapper = (ConstraintLayout) findViewById(R.id.emptyWrapper);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecurringAdapter recurringAdapter = new RecurringAdapter(this);
        this.adapter = recurringAdapter;
        this.recyclerView.setAdapter(recurringAdapter);
        this.adapter.setListener(this);
        RecurringViewModel recurringViewModel = (RecurringViewModel) new ViewModelProvider(this).get(RecurringViewModel.class);
        this.recurringViewModel = recurringViewModel;
        recurringViewModel.getRecurringList().observe(this, new Observer<List<Recurring>>() { // from class: com.ktwapps.walletmanager.ManageRecurring.1
            @Override // androidx.lifecycle.Observer
            public void onChanged(final List<Recurring> recurrings) {
                ManageRecurring.this.adapter.setList(recurrings);
                ManageRecurring.this.emptyWrapper.setVisibility((recurrings == null || recurrings.size() == 0) ? 0 : 8);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_action_create) {
            if (this.billingHelper.getBillingStatus() == 2) {
                Intent intent = new Intent(this, CreateRecurring.class);
                intent.putExtra(JamXmlElements.TYPE, 10);
                startActivity(intent);
                overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
                return true;
            }
            Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.ManageRecurring.2
                @Override // java.lang.Runnable
                public void run() {
                    final List<Recurring> allRecurringList = AppDatabaseObject.getInstance(ManageRecurring.this.getApplicationContext()).recurringDaoObject().getAllRecurringList();
                    ManageRecurring.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.ManageRecurring.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (allRecurringList.size() == 0) {
                                Intent intent2 = new Intent(ManageRecurring.this.getApplicationContext(), CreateRecurring.class);
                                intent2.putExtra(JamXmlElements.TYPE, 10);
                                ManageRecurring.this.startActivity(intent2);
                                ManageRecurring.this.overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
                                return;
                            }
                            ManageRecurring.this.startActivity(new Intent(ManageRecurring.this.getApplicationContext(), Premium.class));
                            ManageRecurring.this.overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
                        }
                    });
                }
            });
            return true;
        }
        return false;
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create, menu);
        for (int i = 0; i < menu.size(); i++) {
            Drawable icon = menu.getItem(i).getIcon();
            if (icon != null) {
                icon.mutate();
                icon.setColorFilter(Helper.getAttributeColor(this, R.attr.primaryDarkTextColor), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return true;
    }

    @Override // com.ktwapps.walletmanager.Adapter.RecurringAdapter.OnItemClickListener
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, RecurringDetail.class);
        intent.putExtra("recurringId", this.adapter.getList().get(position).getId());
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
    }
}
