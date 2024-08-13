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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.expance.merapaisa.Adapter.CurrencyAdapter;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.Entity.WalletEntity;
import com.expance.merapaisa.Database.ViewModel.CurrencyViewModel;
import com.expance.merapaisa.Model.Currencies;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;

import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class Currency extends AppCompatActivity implements CurrencyAdapter.OnItemClickListener {
    CurrencyAdapter adapter;
    CurrencyViewModel currencyViewModel;
    ConstraintLayout emptyWrapper;
    RecyclerView recyclerView;

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
        setContentView(R.layout.activity_currency);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        setUpLayout();
    }

    private void setUpLayout() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.currency);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.emptyWrapper = (ConstraintLayout) findViewById(R.id.emptyWrapper);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CurrencyAdapter currencyAdapter = new CurrencyAdapter(this);
        this.adapter = currencyAdapter;
        this.recyclerView.setAdapter(currencyAdapter);
        this.adapter.setListener(this);
        CurrencyViewModel currencyViewModel = (CurrencyViewModel) new ViewModelProvider(this).get(CurrencyViewModel.class);
        this.currencyViewModel = currencyViewModel;
        currencyViewModel.getCurrencyList().observe(this, new Observer<List<Currencies>>() { // from class: com.ktwapps.walletmanager.Currency.1
            @Override // androidx.lifecycle.Observer
            public void onChanged(List<Currencies> currencies) {
                Iterator<Currencies> it = currencies.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Currencies next = it.next();
                    if (next.getMainCurrency().equals(next.getSubCurrency())) {
                        currencies.remove(next);
                        break;
                    }
                }
                if (currencies.size() > 0) {
                    Currency.this.emptyWrapper.setVisibility(8);
                } else {
                    Currency.this.emptyWrapper.setVisibility(0);
                }
                Currency.this.adapter.setList(currencies);
            }
        });
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
            startActivity(new Intent(this, CreateCurrency.class));
            overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
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

    @Override // com.ktwapps.walletmanager.Adapter.CurrencyAdapter.OnItemClickListener
    public void OnItemClick(View v, int position, int type) {
        if (type == 2) {
            final int id = this.adapter.getList().get(position).getId();
            final String subCurrency = this.adapter.getList().get(position).getSubCurrency();
            final String mainCurrency = this.adapter.getList().get(position).getMainCurrency();
            Helper.showDialog(this, "", getResources().getString(R.string.currency_delete_message), getResources().getString(R.string.delete_positive), getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.Currency.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.Currency.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(Currency.this.getApplicationContext());
                            appDatabaseObject.currencyDaoObject().deleteCurrency(id);
                            for (WalletEntity walletEntity : appDatabaseObject.walletDaoObject().getCurrencyWalletEntity(SharePreferenceHelper.getAccountId(Currency.this.getApplicationContext()), subCurrency)) {
                                walletEntity.setCurrency(mainCurrency);
                                appDatabaseObject.walletDaoObject().updateWallet(walletEntity);
                            }
                        }
                    });
                }
            });
        } else if (type == 1) {
            Intent intent = new Intent(this, CreateCurrency.class);
            intent.putExtra("currencyId", this.adapter.getList().get(position).getId());
            startActivity(intent);
            overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
        }
    }
}
