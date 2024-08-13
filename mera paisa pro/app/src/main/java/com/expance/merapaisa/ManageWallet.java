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
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.expance.merapaisa.Adapter.ManageWalletAdapter;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.Entity.AccountEntity;
import com.expance.merapaisa.Database.ViewModel.WalletViewModel;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import com.expance.merapaisa.Widget.SwipeAndDragViewHelper;
import java.util.List;
import java.util.concurrent.Executors;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class ManageWallet extends AppCompatActivity implements ManageWalletAdapter.OnItemClickListener {
    ManageWalletAdapter adapter;
    RecyclerView recyclerView;
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
        setContentView(R.layout.activity_manage_wallet);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
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
            getSupportActionBar().setTitle(getResources().getString(R.string.wallet));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ManageWalletAdapter manageWalletAdapter = new ManageWalletAdapter(this);
        this.adapter = manageWalletAdapter;
        this.recyclerView.setAdapter(manageWalletAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeAndDragViewHelper(this.adapter));
        this.adapter.setTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(this.recyclerView);
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
        this.walletViewModel.getWalletsList().observe(this, new Observer() { // from class: com.ktwapps.walletmanager.ManageWallet$$ExternalSyntheticLambda3
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                ManageWallet.this.m222lambda$setUpLayout$0$comktwappswalletmanagerManageWallet((List) obj);
            }
        });
        this.walletViewModel.archived.observe(this, new Observer() { // from class: com.ktwapps.walletmanager.ManageWallet$$ExternalSyntheticLambda4
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                ManageWallet.this.m223lambda$setUpLayout$1$comktwappswalletmanagerManageWallet((Integer) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$setUpLayout$0$com-ktwapps-walletmanager-ManageWallet  reason: not valid java name */
    public /* synthetic */ void m222lambda$setUpLayout$0$comktwappswalletmanagerManageWallet(List list) {
        this.adapter.setTransList(list);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$setUpLayout$1$com-ktwapps-walletmanager-ManageWallet  reason: not valid java name */
    public /* synthetic */ void m223lambda$setUpLayout$1$comktwappswalletmanagerManageWallet(Integer num) {
        this.adapter.setNumberOfArchive(num.intValue());
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_action_create) {
            Intent intent = new Intent(getApplicationContext(), CreateWallet.class);
            intent.putExtra(JamXmlElements.TYPE, 1);
            intent.putExtra("currencySymbol", SharePreferenceHelper.getAccountSymbol(getApplicationContext()));
            startActivity(intent);
            overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.ManageWallet.1
            @Override // java.lang.Runnable
            public void run() {
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(ManageWallet.this.getApplicationContext());
                for (int i = 0; i < ManageWallet.this.adapter.list.size(); i++) {
                    appDatabaseObject.walletDaoObject().updateWalletOrdering(i, ManageWallet.this.adapter.list.get(i).getId());
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(14, 0);
        calendar.set(13, 0);
        calendar.set(12, 0);
        calendar.set(11, 0);
        calendar.add(5, 1);
        if (!SharePreferenceHelper.isFutureBalanceOn(getApplicationContext())) {
            calendar.set(1, 10000);
        }
        this.walletViewModel.setDate(calendar.getTimeInMillis());
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

    @Override // com.ktwapps.walletmanager.Adapter.ManageWalletAdapter.OnItemClickListener
    public void OnItemClick(View v, int position, int type) {
        if (type == 25) {
            int id = this.adapter.list.get(position).getId();
            Intent intent = new Intent(getApplicationContext(), CreateWallet.class);
            intent.putExtra(JamXmlElements.TYPE, -1);
            intent.putExtra("walletId", id);
            intent.putExtra("currencySymbol", SharePreferenceHelper.getAccountSymbol(getApplicationContext()));
            startActivity(intent);
            overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
        } else if (type == 27) {
            final int id2 = this.adapter.list.get(position).getId();
            Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.ManageWallet$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    ManageWallet.this.m219lambda$OnItemClick$2$comktwappswalletmanagerManageWallet(id2);
                }
            });
            Toast.makeText(this, (int) R.string.archived, 0).show();
        } else {
            final int id3 = this.adapter.list.get(position).getId();
            final int exclude = this.adapter.list.get(position).getExclude();
            final long initialAmount = this.adapter.list.get(position).getInitialAmount();
            Helper.showDialog(this, "", getResources().getString(R.string.wallet_delete_message), getResources().getString(R.string.delete_positive), getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.ManageWallet$$ExternalSyntheticLambda2
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    ManageWallet.this.m221lambda$OnItemClick$4$comktwappswalletmanagerManageWallet(id3, exclude, initialAmount, dialogInterface, i);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$OnItemClick$2$com-ktwapps-walletmanager-ManageWallet  reason: not valid java name */
    public /* synthetic */ void m219lambda$OnItemClick$2$comktwappswalletmanagerManageWallet(int i) {
        AppDatabaseObject.getInstance(getApplicationContext()).walletDaoObject().archiveWallet(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$OnItemClick$4$com-ktwapps-walletmanager-ManageWallet  reason: not valid java name */
    public /* synthetic */ void m221lambda$OnItemClick$4$comktwappswalletmanagerManageWallet(final int i, final int i2, final long j, DialogInterface dialogInterface, int i3) {
        dialogInterface.dismiss();
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.ManageWallet$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ManageWallet.this.m220lambda$OnItemClick$3$comktwappswalletmanagerManageWallet(i, i2, j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$OnItemClick$3$com-ktwapps-walletmanager-ManageWallet  reason: not valid java name */
    public /* synthetic */ void m220lambda$OnItemClick$3$comktwappswalletmanagerManageWallet(int i, int i2, long j) {
        int accountId = SharePreferenceHelper.getAccountId(getApplicationContext());
        AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(getApplicationContext());
        AccountEntity entityById = appDatabaseObject.accountDaoObject().getEntityById(accountId);
        appDatabaseObject.templateDaoObject().deleteTemplateByWalletId(i);
        appDatabaseObject.walletDaoObject().deleteWallet(i, 1);
        if (i2 == 0) {
            entityById.setBalance(entityById.getBalance() - j);
            appDatabaseObject.accountDaoObject().updateAccount(entityById);
        }
    }

    @Override // com.ktwapps.walletmanager.Adapter.ManageWalletAdapter.OnItemClickListener
    public void OnArchiveClick() {
        startActivity(new Intent(getApplicationContext(), ArchivedActivity.class));
        overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
    }
}
