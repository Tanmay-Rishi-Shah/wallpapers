package com.expance.merapaisa;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.expance.merapaisa.Adapter.ArchivedAdapter;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.Entity.AccountEntity;
import com.expance.merapaisa.Database.ViewModel.WalletViewModel;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.List;
import java.util.concurrent.Executors;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class ArchivedActivity extends AppCompatActivity implements ArchivedAdapter.OnItemClickListener {
    ArchivedAdapter adapter;
    ConstraintLayout emptyWrapper;
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
        setContentView(R.layout.activity_archived);
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
            getSupportActionBar().setTitle(getResources().getString(R.string.archived));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.emptyWrapper = (ConstraintLayout) findViewById(R.id.emptyWrapper);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArchivedAdapter archivedAdapter = new ArchivedAdapter(this);
        this.adapter = archivedAdapter;
        this.recyclerView.setAdapter(archivedAdapter);
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
        this.walletViewModel.getHiddenWalletsList().observe(this, new Observer() { // from class: com.ktwapps.walletmanager.ArchivedActivity$$ExternalSyntheticLambda0
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                ArchivedActivity.this.m147lambda$setUpLayout$0$comktwappswalletmanagerArchivedActivity((List) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$setUpLayout$0$com-ktwapps-walletmanager-ArchivedActivity  reason: not valid java name */
    public /* synthetic */ void m147lambda$setUpLayout$0$comktwappswalletmanagerArchivedActivity(List list) {
        this.adapter.setList(list);
        this.emptyWrapper.setVisibility(this.adapter.list.size() == 0 ? 0 : 8);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.ArchivedActivity.1
            @Override // java.lang.Runnable
            public void run() {
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(ArchivedActivity.this.getApplicationContext());
                for (int i = 0; i < ArchivedActivity.this.adapter.list.size(); i++) {
                    appDatabaseObject.walletDaoObject().updateWalletOrdering(i, ArchivedActivity.this.adapter.list.get(i).getId());
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

    @Override // com.ktwapps.walletmanager.Adapter.ArchivedAdapter.OnItemClickListener
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
            Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.ArchivedActivity$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    ArchivedActivity.this.m144lambda$OnItemClick$1$comktwappswalletmanagerArchivedActivity(id2);
                }
            });
            Toast.makeText(this, (int) R.string.unarchive, 0).show();
        } else if (type == -25) {
            final int id3 = this.adapter.list.get(position).getId();
            final int exclude = this.adapter.list.get(position).getExclude();
            final long initialAmount = this.adapter.list.get(position).getInitialAmount();
            Helper.showDialog(this, "", getResources().getString(R.string.wallet_delete_message), getResources().getString(R.string.delete_positive), getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.ArchivedActivity$$ExternalSyntheticLambda2
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    ArchivedActivity.this.m146lambda$OnItemClick$3$comktwappswalletmanagerArchivedActivity(id3, exclude, initialAmount, dialogInterface, i);
                }
            });
        } else {
            Intent intent2 = new Intent(getApplicationContext(), WalletTransaction.class);
            intent2.putExtra("walletId", this.adapter.list.get(position).getId());
            startActivity(intent2);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$OnItemClick$1$com-ktwapps-walletmanager-ArchivedActivity  reason: not valid java name */
    public /* synthetic */ void m144lambda$OnItemClick$1$comktwappswalletmanagerArchivedActivity(int i) {
        AppDatabaseObject.getInstance(getApplicationContext()).walletDaoObject().unArchiveWallet(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$OnItemClick$3$com-ktwapps-walletmanager-ArchivedActivity  reason: not valid java name */
    public /* synthetic */ void m146lambda$OnItemClick$3$comktwappswalletmanagerArchivedActivity(final int i, final int i2, final long j, DialogInterface dialogInterface, int i3) {
        dialogInterface.dismiss();
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.ArchivedActivity$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                ArchivedActivity.this.m145lambda$OnItemClick$2$comktwappswalletmanagerArchivedActivity(i, i2, j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$OnItemClick$2$com-ktwapps-walletmanager-ArchivedActivity  reason: not valid java name */
    public /* synthetic */ void m145lambda$OnItemClick$2$comktwappswalletmanagerArchivedActivity(int i, int i2, long j) {
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
}
