package com.expance.merapaisa;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.expance.merapaisa.Adapter.WalletTransactionAdapter;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.Entity.AccountEntity;
import com.expance.merapaisa.Database.Entity.TransEntity;
import com.expance.merapaisa.Database.Entity.WalletEntity;
import com.expance.merapaisa.Database.ViewModel.WalletNameViewModel;
import com.expance.merapaisa.Model.WalletDetail;
import com.expance.merapaisa.Model.WalletTrans;
import com.expance.merapaisa.Utility.BillingHelper;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import com.expance.merapaisa.Widget.BottomWalletPickerDialog;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class WalletTransaction extends AppCompatActivity implements WalletTransactionAdapter.OnItemClickListener, View.OnClickListener, BillingHelper.BillingListener, BottomWalletPickerDialog.OnItemClickListener {
    WalletTransactionAdapter adapter;
    private BillingHelper billingHelper;
    FloatingActionButton fab;
    int id;
    long initialBalance;
    RecyclerView recyclerView;
    WalletNameViewModel walletNameViewModel;
    String symbol = "$";
    long amount = 0;

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onSkuReady() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("walletId", this.id);
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
        setContentView(R.layout.activity_wallet_transaction);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        if (savedInstanceState != null) {
            this.id = savedInstanceState.getInt("walletId");
        } else {
            this.id = getIntent().getIntExtra("walletId", 0);
        }
        this.billingHelper = new BillingHelper(this);
        setUpLayout();
        this.billingHelper.setListener(this);
        this.billingHelper.setUpBillingClient();
    }

    private void setUpLayout() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        this.fab = floatingActionButton;
        floatingActionButton.setOnClickListener(this);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        WalletTransactionAdapter walletTransactionAdapter = new WalletTransactionAdapter(this);
        this.adapter = walletTransactionAdapter;
        walletTransactionAdapter.setListener(this);
        this.recyclerView.setAdapter(this.adapter);
        populateData();
    }

    private void populateData() {
        WalletNameViewModel walletNameViewModel = (WalletNameViewModel) new ViewModelProvider(this).get(WalletNameViewModel.class);
        this.walletNameViewModel = walletNameViewModel;
        walletNameViewModel.setWalletId(this.id);
        this.walletNameViewModel.getWalletName().observe(this, new AnonymousClass1());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.ktwapps.walletmanager.WalletTransaction$1  reason: invalid class name */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 implements Observer<WalletDetail> {
        AnonymousClass1() {
        }

        @Override // androidx.lifecycle.Observer
        public void onChanged(final WalletDetail s) {
            if (s != null) {
                WalletTransaction.this.initialBalance = s.getInitialAmount();
                WalletTransaction.this.symbol = DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(s.getCurrency()));
                WalletTransaction.this.amount = s.getAmount();
                WalletTransaction.this.adapter.setWalletDetail(s);
                WalletTransaction.this.adapter.notifyDataSetChanged();
                Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.WalletTransaction.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        final List<WalletTrans> walletTransById = AppDatabaseObject.getInstance(WalletTransaction.this.getApplicationContext()).walletDaoObject().getWalletTransById(s.getId(), SharePreferenceHelper.getAccountId(WalletTransaction.this.getApplicationContext()));
                        WalletTransaction.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.WalletTransaction.1.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                WalletTransaction.this.fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(s.getColor())));
                                ArrayList arrayList = new ArrayList();
                                for (WalletTrans walletTrans : walletTransById) {
                                    if (walletTrans.getId() == 0) {
                                        if (walletTrans.getTrans() > 0) {
                                            arrayList.add(walletTrans);
                                        }
                                    } else {
                                        arrayList.add(walletTrans);
                                    }
                                }
                                WalletTransaction.this.adapter.setList(arrayList);
                                WalletTransaction.this.adapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
            }
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

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_wallet_transaction, menu);
        for (int i = 0; i < menu.size(); i++) {
            Drawable icon = menu.getItem(i).getIcon();
            if (icon != null) {
                icon.mutate();
                icon.setColorFilter(Helper.getAttributeColor(this, R.attr.primaryDarkTextColor), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_action_edit) {
            Intent intent = new Intent(getApplicationContext(), CreateWallet.class);
            intent.putExtra(JamXmlElements.TYPE, -1);
            intent.putExtra("walletId", this.id);
            intent.putExtra("currencySymbol", SharePreferenceHelper.getAccountSymbol(getApplicationContext()));
            startActivity(intent);
            overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
            return true;
        } else if (item.getItemId() == R.id.menu_action_stat) {
            Intent intent2 = new Intent(getApplicationContext(), WalletStatistic.class);
            intent2.putExtra("walletId", this.id);
            intent2.putExtra("symbol", this.symbol);
            startActivity(intent2);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
            return false;
        } else {
            return false;
        }
    }

    @Override // com.ktwapps.walletmanager.Adapter.WalletTransactionAdapter.OnItemClickListener
    public void OnItemClick(View view, int position) {
        if (view.getId() == R.id.adjustButton) {
            Intent intent = new Intent(getApplicationContext(), Calculator.class);
            intent.putExtra("amount", this.amount);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        } else if (view.getId() == R.id.payButton) {
            Intent intent2 = new Intent(getApplicationContext(), CreateTransaction.class);
            intent2.putExtra("walletId", this.id);
            long j = this.amount;
            if (j >= 0) {
                intent2.putExtra("amount", 0);
            } else {
                intent2.putExtra("amount", -j);
            }
            intent2.putExtra("isPayment", true);
            startActivity(intent2);
            overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
        } else if (view.getId() == R.id.nameWrapper) {
            BottomWalletPickerDialog bottomWalletPickerDialog = new BottomWalletPickerDialog();
            bottomWalletPickerDialog.setWalletId(this.id);
            bottomWalletPickerDialog.setListener(this);
            bottomWalletPickerDialog.show(getSupportFragmentManager(), "walletPicker");
        } else if (view.getId() == R.id.allLabel) {
            Intent intent3 = new Intent(getApplicationContext(), WalletTransactionOverview.class);
            intent3.putExtra("walletId", this.id);
            intent3.putExtra("symbol", this.symbol);
            intent3.putExtra("date", java.util.Calendar.getInstance().getTime());
            intent3.putExtra("mode", 2);
            startActivity(intent3);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        } else {
            WalletTrans walletTrans = this.adapter.getList().get(position);
            String name = walletTrans.getName(getApplicationContext());
            Intent intent4 = new Intent(getApplicationContext(), WalletCategoryTransaction.class);
            intent4.putExtra(JamXmlElements.TYPE, walletTrans.getType());
            intent4.putExtra("categoryId", walletTrans.getId());
            intent4.putExtra("name", name);
            intent4.putExtra("walletId", this.id);
            intent4.putExtra("symbol", this.symbol);
            startActivity(intent4);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), CreateTransaction.class);
        intent.putExtra("walletId", this.id);
        startActivity(intent);
        overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 1) {
            long longExtra = data.getLongExtra("amount", 0L);
            long j = this.amount;
            if (longExtra != j) {
                final long j2 = longExtra - j;
                int i = (j2 > 0L ? 1 : (j2 == 0L ? 0 : -1));
                final boolean z = i < 0;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.adjust_balance);
                final View inflate = LayoutInflater.from(new ContextThemeWrapper(this, SharePreferenceHelper.getThemeMode(this) == 1 ? R.style.AppThemeNight : R.style.AppTheme)).inflate(R.layout.dialog_adjust_balance, (ViewGroup) null);
                TextView textView = (TextView) inflate.findViewById(R.id.amountLabel);
                final RadioGroup radioGroup = (RadioGroup) inflate.findViewById(R.id.radioGroup);
                View findViewById = inflate.findViewById(R.id.colorView);
                ImageView imageView = (ImageView) inflate.findViewById(R.id.imageView);
                textView.setText(Helper.getBeautifyAmount(this.symbol, j2));
                Resources resources = getResources();
                int i2 = R.color.income;
                textView.setTextColor(resources.getColor(i >= 0 ? R.color.income : R.color.expense));
                if (Build.VERSION.SDK_INT >= 29) {
                    Drawable background = findViewById.getBackground();
                    Resources resources2 = getResources();
                    if (i < 0) {
                        i2 = R.color.expense;
                    }
                    background.setColorFilter(new BlendModeColorFilter(resources2.getColor(i2), BlendMode.SRC_OVER));
                } else {
                    Drawable background2 = findViewById.getBackground();
                    Resources resources3 = getResources();
                    if (i < 0) {
                        i2 = R.color.expense;
                    }
                    background2.setColorFilter(resources3.getColor(i2), PorterDuff.Mode.SRC_OVER);
                }
                imageView.setImageResource(R.drawable.adjust);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.ktwapps.walletmanager.WalletTransaction.2
                    @Override // android.widget.RadioGroup.OnCheckedChangeListener
                    public void onCheckedChanged(RadioGroup radioGroup2, int i3) {
                        if (i3 == R.id.radio1) {
                            inflate.findViewById(R.id.recordWrapper).setVisibility(0);
                            inflate.findViewById(R.id.initialWrapper).setVisibility(8);
                            return;
                        }
                        inflate.findViewById(R.id.recordWrapper).setVisibility(8);
                        inflate.findViewById(R.id.initialWrapper).setVisibility(0);
                    }
                });
                builder.setView(inflate);
                builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.WalletTransaction.3
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i3) {
                        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.WalletTransaction.3.1
                            @Override // java.lang.Runnable
                            public void run() {
                                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(WalletTransaction.this.getApplicationContext());
                                if (radioGroup.getCheckedRadioButtonId() == R.id.radio1) {
                                    WalletTransaction.this.addTransaction(appDatabaseObject.categoryDaoObject().getAdjustmentId(SharePreferenceHelper.getAccountId(WalletTransaction.this.getApplicationContext()), z ? 2 : 1), j2);
                                    return;
                                }
                                WalletTransaction.this.initialBalance += j2;
                                WalletEntity walletById = appDatabaseObject.walletDaoObject().getWalletById(WalletTransaction.this.id);
                                walletById.setInitialAmount(WalletTransaction.this.initialBalance);
                                appDatabaseObject.walletDaoObject().updateWallet(walletById);
                            }
                        });
                    }
                });
                builder.setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null);
                builder.show();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addTransaction(int categoryId, long amount) {
        AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(getApplicationContext());
        int accountId = SharePreferenceHelper.getAccountId(getApplicationContext());
        int i = amount >= 0 ? 0 : 1;
        if (i == 0) {
            WalletEntity walletById = appDatabaseObject.walletDaoObject().getWalletById(this.id);
            walletById.setAmount(walletById.getAmount() + amount);
            appDatabaseObject.walletDaoObject().updateWallet(walletById);
            if (walletById.getExclude() == 0) {
                AccountEntity entityById = appDatabaseObject.accountDaoObject().getEntityById(accountId);
                entityById.setBalance(entityById.getBalance() + amount);
                appDatabaseObject.accountDaoObject().updateAccount(entityById);
            }
        } else if (i == 1) {
            WalletEntity walletById2 = appDatabaseObject.walletDaoObject().getWalletById(this.id);
            walletById2.setAmount(walletById2.getAmount() - amount);
            appDatabaseObject.walletDaoObject().updateWallet(walletById2);
            if (walletById2.getExclude() == 0) {
                AccountEntity entityById2 = appDatabaseObject.accountDaoObject().getEntityById(accountId);
                entityById2.setBalance(entityById2.getBalance() - amount);
                appDatabaseObject.accountDaoObject().updateAccount(entityById2);
            }
        }
        appDatabaseObject.transDaoObject().insertTrans(new TransEntity("", "", amount, java.util.Calendar.getInstance().getTime(), i, accountId, categoryId, 0, this.id, 0, 0L, 0, 0, 0));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        this.billingHelper.registerBroadCast(this);
        this.billingHelper.queryPurchases();
        int i = this.id;
        if (i != 0) {
            this.walletNameViewModel.setWalletId(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        this.billingHelper.unregisterBroadCast(this);
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

    @Override // com.ktwapps.walletmanager.Widget.BottomWalletPickerDialog.OnItemClickListener
    public void onItemClick(int walletId) {
        this.id = walletId;
        this.walletNameViewModel.setWalletId(walletId);
    }
}
