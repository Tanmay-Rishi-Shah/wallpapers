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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.billingclient.api.BillingFlowParams;
import com.expance.merapaisa.Adapter.ManageAccountAdapter;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.ViewModel.AccountViewModel;
import com.expance.merapaisa.Model.Account;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import com.expance.merapaisa.Widget.SwipeAndDragViewHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class ManageAccount extends AppCompatActivity implements ManageAccountAdapter.OnItemClickListener {
    AccountViewModel accountViewModel;
    ManageAccountAdapter adapter;
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
        setContentView(R.layout.activity_manage_account);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        this.accountViewModel = (AccountViewModel) new ViewModelProvider(this).get(AccountViewModel.class);
        setUpLayout();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.ManageAccount.1
            @Override // java.lang.Runnable
            public void run() {
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(ManageAccount.this.getApplicationContext());
                for (int i = 0; i < ManageAccount.this.adapter.getList().size(); i++) {
                    appDatabaseObject.accountDaoObject().updateAccountOrdering(i, ManageAccount.this.adapter.getList().get(i).getId());
                }
            }
        });
    }

    public void setUpLayout() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.account));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ManageAccountAdapter manageAccountAdapter = new ManageAccountAdapter(this);
        this.adapter = manageAccountAdapter;
        this.recyclerView.setAdapter(manageAccountAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeAndDragViewHelper(this.adapter));
        this.adapter.setTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(this.recyclerView);
        this.adapter.setListener(this);
        this.accountViewModel.getAccountList().observe(this, new AnonymousClass2());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.ktwapps.walletmanager.ManageAccount$2  reason: invalid class name */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 implements Observer<List<Account>> {
        AnonymousClass2() {
        }

        @Override // androidx.lifecycle.Observer
        public void onChanged(final List<Account> accounts) {
            Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.ManageAccount.2.1
                @Override // java.lang.Runnable
                public void run() {
                    AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(ManageAccount.this.getApplicationContext());
                    final ArrayList arrayList = new ArrayList();
                    for (Account account : accounts) {
                        java.util.Calendar calendar = java.util.Calendar.getInstance();
                        calendar.set(14, 0);
                        calendar.set(13, 0);
                        calendar.set(12, 0);
                        calendar.set(11, 0);
                        calendar.add(5, 1);
                        if (!SharePreferenceHelper.isFutureBalanceOn(ManageAccount.this)) {
                            calendar.set(1, 10000);
                        }
                        Long accountBalance = appDatabaseObject.accountDaoObject().getAccountBalance(account.getId(), 0, calendar.getTimeInMillis());
                        if (accountBalance == null) {
                            accountBalance = 0L;
                        }
                        account.setBalance(accountBalance.longValue());
                        arrayList.add(account);
                    }
                    ManageAccount.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.ManageAccount.2.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            ManageAccount.this.adapter.setList(arrayList);
                        }
                    });
                }
            });
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
            startActivity(new Intent(getApplicationContext(), AccountCreateName.class));
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

    @Override // com.ktwapps.walletmanager.Adapter.ManageAccountAdapter.OnItemClickListener
    public void OnItemClick(View v, final int position, int type) {
        if (type == -11) {
            final int id = this.adapter.getList().get(position).getId();
            Helper.showDialog(this, "", getResources().getString(R.string.account_delete_message), getResources().getString(R.string.delete_positive), getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.ManageAccount.3
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.ManageAccount.3.1
                        @Override // java.lang.Runnable
                        public void run() {
                            AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(ManageAccount.this.getApplicationContext());
                            ManageAccount.this.adapter.getList().remove(position);
                            appDatabaseObject.accountDaoObject().deleteAccount(id);
                            if (id == SharePreferenceHelper.getAccountId(ManageAccount.this.getApplicationContext())) {
                                if (ManageAccount.this.adapter.getList().size() == 0) {
                                    SharePreferenceHelper.removeAccount(ManageAccount.this.getApplicationContext());
                                    ManageAccount.this.startActivity(new Intent(ManageAccount.this.getApplicationContext(), BaseActivity.class));
                                } else {
                                    SharePreferenceHelper.setAccount(ManageAccount.this.getApplicationContext(), ManageAccount.this.adapter.getList().get(0).getId(), ManageAccount.this.adapter.getList().get(0).getCurrencySymbol(), ManageAccount.this.adapter.getList().get(0).getName());
                                    ManageAccount.this.startActivity(new Intent(ManageAccount.this.getApplicationContext(), MainActivity.class));
                                }
                                ManageAccount.this.finishAffinity();
                            }
                        }
                    });
                }
            });
            return;
        }
        Intent intent = new Intent(this, EditAccount.class);
        intent.putExtra(BillingFlowParams.EXTRA_PARAM_KEY_ACCOUNT_ID, this.adapter.getList().get(position).getId());
        startActivity(intent);
        overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
    }
}
