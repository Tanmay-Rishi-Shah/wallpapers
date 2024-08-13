package com.expance.merapaisa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.expance.merapaisa.Adapter.WalletPickerAdapter;
import com.expance.merapaisa.Database.ViewModel.WalletViewModel;
import com.expance.merapaisa.Model.Wallets;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.ArrayList;
import java.util.List;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class WalletPicker extends AppCompatActivity implements WalletPickerAdapter.OnItemClickListener {
    WalletPickerAdapter adapter;
    RecyclerView recyclerView;
    int type;
    int walletId;
    WalletViewModel walletViewModel;

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
        setContentView(R.layout.activity_wallet_picker);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        this.walletId = getIntent().getIntExtra("id", 0);
        this.type = getIntent().getIntExtra(JamXmlElements.TYPE, 11);
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
            getSupportActionBar().setTitle(R.string.select_wallet);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        WalletPickerAdapter walletPickerAdapter = new WalletPickerAdapter(this);
        this.adapter = walletPickerAdapter;
        walletPickerAdapter.setWalletId(this.walletId);
        this.recyclerView.setAdapter(this.adapter);
        this.adapter.setListener(this);
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(14, 0);
        calendar.set(13, 0);
        calendar.set(12, 0);
        calendar.set(11, 0);
        calendar.add(5, 1);
        if (!SharePreferenceHelper.isFutureBalanceOn(getApplicationContext())) {
            calendar.set(1, 10000);
        }
        WalletViewModel walletViewModel = (WalletViewModel) new ViewModelProvider(this).get(WalletViewModel.class);
        this.walletViewModel = walletViewModel;
        walletViewModel.setDate(calendar.getTimeInMillis());
        this.walletViewModel.getWalletsList().observe(this, new Observer<List<Wallets>>() { // from class: com.ktwapps.walletmanager.WalletPicker.1
            @Override // androidx.lifecycle.Observer
            public void onChanged(List<Wallets> wallets) {
                ArrayList arrayList = new ArrayList();
                ArrayList arrayList2 = new ArrayList();
                ArrayList arrayList3 = new ArrayList();
                for (Wallets wallets2 : wallets) {
                    if (wallets2.getExclude() == 0) {
                        arrayList2.add(wallets2);
                    } else {
                        arrayList3.add(wallets2);
                    }
                }
                if (arrayList2.size() > 0) {
                    arrayList.add(0);
                    arrayList.addAll(arrayList2);
                }
                if (arrayList3.size() > 0) {
                    arrayList.add(1);
                    arrayList.addAll(arrayList3);
                }
                WalletPicker.this.adapter.setList(arrayList);
                WalletPicker.this.adapter.notifyDataSetChanged();
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

    @Override // com.ktwapps.walletmanager.Adapter.WalletPickerAdapter.OnItemClickListener
    public void onItemClick(View view, int position) {
        Intent intent = new Intent();
        intent.putExtra("id", ((Wallets) this.adapter.getList().get(position)).getId());
        intent.putExtra(JamXmlElements.TYPE, this.type);
        setResult(-1, intent);
        finish();
        overridePendingTransition(R.anim.scale_in, R.anim.right_to_left);
    }
}
