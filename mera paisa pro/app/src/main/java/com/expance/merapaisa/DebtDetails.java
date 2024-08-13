package com.expance.merapaisa;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.expance.merapaisa.Adapter.DebtDetailAdapter;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.Entity.DebtTransEntity;
import com.expance.merapaisa.Database.ViewModel.DebtTransViewModel;
import com.expance.merapaisa.Database.ViewModel.ModelFactory;
import com.expance.merapaisa.Model.Debt;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class DebtDetails extends AppCompatActivity implements View.OnClickListener, DebtDetailAdapter.OnItemClickListener {
    DebtDetailAdapter adapter;
    Debt debt;
    int debtId;
    DebtTransViewModel debtTransViewModel;
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;

    @Override // com.ktwapps.walletmanager.Adapter.DebtDetailAdapter.OnItemClickListener
    public void OnItemLongClick(View v, int position) {
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
        setContentView(R.layout.activity_debt_details);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        this.debtId = getIntent().getIntExtra("debtId", 0);
        DebtDetailAdapter debtDetailAdapter = new DebtDetailAdapter(this);
        this.adapter = debtDetailAdapter;
        debtDetailAdapter.setListener(this);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        this.floatingActionButton = floatingActionButton;
        floatingActionButton.setOnClickListener(this);
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

    private void setUpLayout() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.debt);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setAdapter(this.adapter);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.ktwapps.walletmanager.DebtDetails$1  reason: invalid class name */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 implements Observer<Debt> {
        AnonymousClass1() {
        }

        @Override // androidx.lifecycle.Observer
        public void onChanged(final Debt debt) {
            Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.DebtDetails.1.1
                @Override // java.lang.Runnable
                public void run() {
                    int accountId = SharePreferenceHelper.getAccountId(DebtDetails.this.getApplicationContext());
                    AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(DebtDetails.this.getApplicationContext());
                    if (debt != null) {
                        String currency = appDatabaseObject.accountDaoObject().getEntityById(accountId).getCurrency();
                        com.expance.merapaisa.Model.Currency debtCurrency = appDatabaseObject.debtDaoObject().getDebtCurrency(accountId, debt.getId());
                        if (debtCurrency == null) {
                            debt.setCurrencyCode(currency);
                            debt.setRate(1.0d);
                        } else {
                            debt.setCurrencyCode(debtCurrency.getCurrency());
                            debt.setRate(debtCurrency.getRate());
                        }
                        long amount = debt.getAmount();
                        long j = 0;
                        for (DebtTransEntity debtTransEntity : appDatabaseObject.debtDaoObject().getAllDebtTrans(debt.getId())) {
                            com.expance.merapaisa.Model.Currency debtTransCurrency = appDatabaseObject.debtDaoObject().getDebtTransCurrency(accountId, debt.getId(), debtTransEntity.getId());
                            if (debtTransCurrency == null) {
                                if (debtTransEntity.getType() == 0) {
                                    j += debtTransEntity.getAmount();
                                } else {
                                    amount += debtTransEntity.getAmount();
                                }
                            } else {
                                double rate = debtTransCurrency.getRate() / debt.getRate();
                                if (debtTransEntity.getType() == 0) {
                                    j = (long) (j + (debtTransEntity.getAmount() * rate));
                                } else {
                                    amount = (long) (amount + (debtTransEntity.getAmount() * rate));
                                }
                            }
                        }
                        debt.setAmount(amount);
                        debt.setPay(j);
                        String debtWallet = appDatabaseObject.debtDaoObject().getDebtWallet(debt.getId());
                        if (debtWallet == null || debtWallet.length() == 0) {
                            debt.setWallet("");
                        } else {
                            debt.setWallet(debtWallet);
                        }
                    }
                    DebtDetails.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.DebtDetails.1.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            DebtDetails.this.debt = debt;
                            DebtDetails.this.adapter.setDebt(debt);
                            if (debt != null) {
                                DebtDetails.this.floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(debt.getColor())));
                            }
                        }
                    });
                }
            });
        }
    }

    private void populateData() {
        DebtTransViewModel debtTransViewModel = (DebtTransViewModel) new ViewModelProvider(this, new ModelFactory(getApplication(), this.debtId)).get(DebtTransViewModel.class);
        this.debtTransViewModel = debtTransViewModel;
        debtTransViewModel.getDebt().observe(this, new AnonymousClass1());
        this.debtTransViewModel.getDebtTrans().observe(this, new AnonymousClass2());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.ktwapps.walletmanager.DebtDetails$2  reason: invalid class name */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 implements Observer<List<DebtTransEntity>> {
        AnonymousClass2() {
        }

        @Override // androidx.lifecycle.Observer
        public void onChanged(final List<DebtTransEntity> debtTransEntities) {
            ArrayList arrayList = new ArrayList();
            final ArrayList arrayList2 = new ArrayList();
            if (debtTransEntities != null && debtTransEntities.size() > 0) {
                Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.DebtDetails.2.1
                    @Override // java.lang.Runnable
                    public void run() {
                        int accountId = SharePreferenceHelper.getAccountId(DebtDetails.this.getApplicationContext());
                        AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(DebtDetails.this.getApplicationContext());
                        for (DebtTransEntity debtTransEntity : debtTransEntities) {
                            com.expance.merapaisa.Model.Currency debtTransCurrency = appDatabaseObject.debtDaoObject().getDebtTransCurrency(accountId, DebtDetails.this.debtId, debtTransEntity.getId());
                            String debtTransWallet = appDatabaseObject.debtDaoObject().getDebtTransWallet(DebtDetails.this.debtId, debtTransEntity.getId());
                            if (debtTransCurrency == null) {
                                debtTransEntity.setRate(Utils.DOUBLE_EPSILON);
                                debtTransEntity.setCurrencyCode(null);
                            } else {
                                debtTransEntity.setRate(debtTransCurrency.getRate());
                                debtTransEntity.setCurrencyCode(debtTransCurrency.getCurrency());
                            }
                            if (debtTransWallet == null || debtTransWallet.length() == 0) {
                                debtTransEntity.setWallet("");
                            } else {
                                debtTransEntity.setWallet(debtTransWallet);
                            }
                            java.util.Calendar calendar = java.util.Calendar.getInstance();
                            calendar.setTime(debtTransEntity.getDateTime());
                            calendar.set(14, 0);
                            calendar.set(13, 0);
                            calendar.set(12, 0);
                            calendar.set(11, 0);
                            if (!arrayList2.contains(Long.valueOf(calendar.getTime().getTime()))) {
                                arrayList2.add(Long.valueOf(calendar.getTime().getTime()));
                            }
                            arrayList2.add(debtTransEntity);
                        }
                        DebtDetails.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.DebtDetails.2.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                DebtDetails.this.adapter.setList(arrayList2);
                            }
                        });
                    }
                });
            }
            DebtDetails.this.adapter.setList(arrayList);
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
        if (item.getItemId() == R.id.menu_action_delete) {
            delete();
            return true;
        } else if (item.getItemId() == R.id.menu_action_edit) {
            edit();
            return true;
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

    private void edit() {
        Intent intent = new Intent(this, CreateDebt.class);
        intent.putExtra(JamXmlElements.TYPE, -2);
        intent.putExtra("debtId", this.debtId);
        startActivity(intent);
        overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
    }

    private void delete() {
        Helper.showDialog(this, "", getResources().getString(R.string.debt_delete_message), getResources().getString(R.string.delete_positive), getResources().getString(R.string.cancel), new AnonymousClass3());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.ktwapps.walletmanager.DebtDetails$3  reason: invalid class name */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 implements DialogInterface.OnClickListener {
        AnonymousClass3() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.DebtDetails.3.1
                @Override // java.lang.Runnable
                public void run() {
                    AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(DebtDetails.this.getApplicationContext());
                    appDatabaseObject.debtDaoObject().deleteDebt(DebtDetails.this.debtId);
                    appDatabaseObject.debtDaoObject().deleteAllTransaction(DebtDetails.this.debtId);
                    appDatabaseObject.debtDaoObject().deleteAllDebtTrans(DebtDetails.this.debtId);
                    DebtDetails.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.DebtDetails.3.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            DebtDetails.this.finish();
                            DebtDetails.this.overridePendingTransition(R.anim.scale_in, R.anim.right_to_left);
                        }
                    });
                }
            });
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.fab) {
            Intent intent = new Intent(this, CreateDebtTrans.class);
            intent.putExtra("debtId", this.debtId);
            intent.putExtra("debtType", this.debt.getType());
            intent.putExtra(JamXmlElements.TYPE, 0);
            startActivity(intent);
            overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
        }
    }

    @Override // com.ktwapps.walletmanager.Adapter.DebtDetailAdapter.OnItemClickListener
    public void OnItemClick(View v, int position) {
        int id = ((DebtTransEntity) this.adapter.getList().get(position - 1)).getId();
        Intent intent = new Intent(this, CreateDebtTrans.class);
        intent.putExtra("debtId", this.debtId);
        intent.putExtra("debtType", this.debt.getType());
        intent.putExtra("debtTransId", id);
        intent.putExtra(JamXmlElements.TYPE, 1);
        startActivity(intent);
        overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
    }
}
