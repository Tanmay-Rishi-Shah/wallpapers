package com.expance.merapaisa;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.expance.merapaisa.Adapter.WalletMultiplePickerAdapter;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.ViewModel.WalletViewModel;
import com.expance.merapaisa.Model.Wallets;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class WalletMultiplePicker extends AppCompatActivity implements View.OnClickListener, WalletMultiplePickerAdapter.OnItemClickListener {
    WalletMultiplePickerAdapter adapter;
    RecyclerView recyclerView;
    TextView saveLabel;
    int type;
    int walletId;
    ArrayList<Integer> walletIds;
    WalletViewModel walletViewModel;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("walletId", this.walletId);
        if (this.walletId == 0) {
            outState.putIntegerArrayList("walletIds", new ArrayList<>());
        } else {
            outState.putIntegerArrayList("walletIds", this.adapter.getWalletIds());
        }
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
        setContentView(R.layout.activity_wallet_multiple_picker);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        this.type = getIntent().getIntExtra(JamXmlElements.TYPE, 0);
        this.walletId = getIntent().getIntExtra("id", -1);
        this.walletIds = getIntent().getIntegerArrayListExtra("ids");
        if (savedInstanceState != null) {
            this.walletId = savedInstanceState.getInt("walletId");
            this.walletIds = savedInstanceState.getIntegerArrayList("walletIds");
        }
        setUpLayout();
    }

    private void setUpLayout() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.select_wallet);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.saveLabel = (TextView) findViewById(R.id.saveLabel);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        WalletMultiplePickerAdapter walletMultiplePickerAdapter = new WalletMultiplePickerAdapter(this);
        this.adapter = walletMultiplePickerAdapter;
        this.recyclerView.setAdapter(walletMultiplePickerAdapter);
        populateDate();
        this.adapter.setListener(this);
        this.saveLabel.setOnClickListener(this);
        if (this.walletId == 0 || this.adapter.getWalletIds().size() > 0) {
            this.saveLabel.setAlpha(1.0f);
        } else {
            this.saveLabel.setAlpha(0.35f);
        }
    }

    public void populateDate() {
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
        this.walletViewModel.getWalletsList().observe(this, new AnonymousClass1());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.ktwapps.walletmanager.WalletMultiplePicker$1  reason: invalid class name */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 implements Observer<List<Wallets>> {
        AnonymousClass1() {
        }

        @Override // androidx.lifecycle.Observer
        public void onChanged(final List<Wallets> wallets) {
            Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.WalletMultiplePicker.1.1
                @Override // java.lang.Runnable
                public void run() {
                    ArrayList<Wallets> arrayList = new ArrayList();
                    if (wallets != null) {
                        arrayList = new ArrayList(wallets);
                    }
                    ArrayList arrayList2 = new ArrayList();
                    for (Wallets wallets2 : arrayList) {
                        arrayList2.add(Integer.valueOf(wallets2.getId()));
                    }
                    Iterator<Integer> it = WalletMultiplePicker.this.walletIds.iterator();
                    while (it.hasNext()) {
                        if (!arrayList2.contains(it.next())) {
                            it.remove();
                        }
                    }
                    AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(WalletMultiplePicker.this.getApplicationContext());
                    java.util.Calendar calendar = java.util.Calendar.getInstance();
                    calendar.set(14, 0);
                    calendar.set(13, 0);
                    calendar.set(12, 0);
                    calendar.set(11, 0);
                    calendar.add(5, 1);
                    if (!SharePreferenceHelper.isFutureBalanceOn(WalletMultiplePicker.this.getApplicationContext())) {
                        calendar.set(1, 10000);
                    }
                    Long accountBalance = appDatabaseObject.accountDaoObject().getAccountBalance(SharePreferenceHelper.getAccountId(WalletMultiplePicker.this.getApplicationContext()), 0, calendar.getTimeInMillis());
                    if (accountBalance == null) {
                        accountBalance = 0L;
                    }
                    arrayList.add(0, new Wallets(WalletMultiplePicker.this.getResources().getString(R.string.all_wallets), "#66757f", 0.0f, null, accountBalance.longValue(), 0L, 0, 0, 0, 0, 0, 0, 0L));
                    ArrayList<Wallets> finalArrayList = arrayList;
                    WalletMultiplePicker.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.WalletMultiplePicker.1.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            WalletMultiplePicker.this.adapter.setList(finalArrayList);
                            if (WalletMultiplePicker.this.walletIds.size() > 0) {
                                WalletMultiplePicker.this.adapter.setWalletIds(WalletMultiplePicker.this.walletIds);
                            } else if (WalletMultiplePicker.this.walletId == 0) {
                                WalletMultiplePicker.this.adapter.selectOrDeselectAll();
                            }
                            if (WalletMultiplePicker.this.walletId == 0 || WalletMultiplePicker.this.adapter.getWalletIds().size() > 0) {
                                WalletMultiplePicker.this.saveLabel.setAlpha(1.0f);
                            } else {
                                WalletMultiplePicker.this.saveLabel.setAlpha(0.35f);
                            }
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

    @Override // com.ktwapps.walletmanager.Adapter.WalletMultiplePickerAdapter.OnItemClickListener
    public void onItemClick(View view, int position) {
        if (position == 0) {
            this.walletId = this.adapter.selectOrDeselectAll() ? 0 : -1;
        } else {
            if (this.walletId == 0) {
                this.adapter.selectOrDeselect(0);
                this.walletId = -1;
            }
            this.adapter.selectOrDeselect(this.adapter.list.get(position).getId());
        }
        if (this.walletId == 0 || this.adapter.getWalletIds().size() > 0) {
            this.saveLabel.setAlpha(1.0f);
        } else {
            this.saveLabel.setAlpha(0.35f);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (this.walletId == 0 || this.adapter.getWalletIds().size() > 0) {
            Intent intent = new Intent();
            int i = 0;
            if (this.walletId == 0) {
                intent.putIntegerArrayListExtra("ids", new ArrayList<>());
                intent.putExtra("name", "All wallets");
                intent.putExtra("id", 0);
            } else {
                String[] strArr = new String[this.adapter.getWalletIds().size()];
                for (Wallets wallets : this.adapter.list) {
                    if (this.adapter.getWalletIds().contains(Integer.valueOf(wallets.getId()))) {
                        strArr[i] = wallets.getName();
                        i++;
                    }
                }
                intent.putIntegerArrayListExtra("ids", this.adapter.getWalletIds());
                intent.putExtra("name", TextUtils.join(", ", strArr));
                intent.putExtra("id", -1);
            }
            setResult(-1, intent);
            finish();
            overridePendingTransition(R.anim.scale_in, R.anim.right_to_left);
        }
    }
}
