package com.expance.merapaisa;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.billingclient.api.ProductDetails;
import com.expance.merapaisa.Adapter.ProAdapter;
import com.expance.merapaisa.Utility.BillingHelper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;

/* loaded from: classes3.dex */
public class Premium extends AppCompatActivity implements BillingHelper.BillingListener, View.OnClickListener {
    BillingHelper billingHelper;
    ProAdapter proAdapter;
    Button proButton;
    RecyclerView recyclerView;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        if (Build.VERSION.SDK_INT >= 23) {
            if (SharePreferenceHelper.getThemeMode(this) == 1) {
                getWindow().getDecorView().setSystemUiVisibility(0);
            } else {
                getWindow().getDecorView().setSystemUiVisibility(8192);
            }
        }
        if (Build.VERSION.SDK_INT >= 26) {
            if (SharePreferenceHelper.getThemeMode(this) == 1) {
                getWindow().getDecorView().setSystemUiVisibility(0);
            } else {
                getWindow().getDecorView().setSystemUiVisibility(8208);
            }
            getWindow().setNavigationBarColor(Color.parseColor(SharePreferenceHelper.getThemeMode(this) == 1 ? "#1F1F1F" : "#F8F8F8"));
        } else {
            getWindow().setNavigationBarColor(Color.parseColor("#000000"));
        }
        setUpLayout();
        BillingHelper billingHelper = new BillingHelper(this);
        this.billingHelper = billingHelper;
        billingHelper.setListener(this);
        this.billingHelper.setUpBillingClient();
    }

    private void setUpLayout() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.proButton = (Button) findViewById(R.id.premiumButton);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ProAdapter proAdapter = new ProAdapter(this);
        this.proAdapter = proAdapter;
        this.recyclerView.setAdapter(proAdapter);
        this.proButton.setOnClickListener(this);
    }

    @Override // androidx.appcompat.app.AppCompatActivity
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(R.anim.scale_in, R.anim.bottom_to_top);
        return true;
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.scale_in, R.anim.bottom_to_top);
    }

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onLoaded() {
        checkSubscription();
    }

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onPurchasedSucceed() {
        finish();
    }

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onPurchasedPending() {
        checkSubscription();
    }

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onReceiveBroadCast() {
        checkSubscription();
    }

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onSkuReady() {
        ProductDetails skuDetail = this.billingHelper.getSkuDetail();
        if (skuDetail != null) {
            if (this.billingHelper.getBillingStatus() == 4) {
                this.proButton.setText(R.string.pending);
            } else if (skuDetail.getOneTimePurchaseOfferDetails() != null) {
                this.proButton.setText(getResources().getString(R.string.pro_buy, skuDetail.getOneTimePurchaseOfferDetails().getFormattedPrice()));
            }
        }
    }

    private void checkSubscription() {
        if (this.billingHelper.getBillingStatus() == 4) {
            this.proButton.setText(R.string.pending);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        this.billingHelper.launchBillingFlow();
    }
}
