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
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.utils.Utils;
import com.expance.merapaisa.Adapter.GoalDetailAdapter;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.Entity.GoalEntity;
import com.expance.merapaisa.Database.Entity.GoalTransEntity;
import com.expance.merapaisa.Database.ViewModel.GoalDetailViewModel;
import com.expance.merapaisa.Database.ViewModel.ModelFactory;
import com.expance.merapaisa.Utility.BillingHelper;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;

import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class GoalDetail extends AppCompatActivity implements BillingHelper.BillingListener, GoalDetailAdapter.OnItemClickListener {
    BillingHelper billingHelper;
    GoalDetailAdapter goalDetailAdapter;
    GoalDetailViewModel goalDetailViewModel;
    int goalId;
    boolean isAchieve;
    RecyclerView recyclerView;
    int status;
    String symbol;
    int type;

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onSkuReady() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(JamXmlElements.TYPE, this.type);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
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
        setContentView(R.layout.activity_goal_detail);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        this.goalId = getIntent().getIntExtra("goalId", 0);
        setUpLayout();
        BillingHelper billingHelper = new BillingHelper(this);
        this.billingHelper = billingHelper;
        billingHelper.setListener(this);
        this.billingHelper.setUpBillingClient();
        if (savedInstanceState != null) {
            this.type = savedInstanceState.getInt(JamXmlElements.TYPE);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
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
            getSupportActionBar().setTitle(R.string.saving_goal);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        GoalDetailAdapter goalDetailAdapter = new GoalDetailAdapter(this);
        this.goalDetailAdapter = goalDetailAdapter;
        goalDetailAdapter.setListener(this);
        this.recyclerView.setAdapter(this.goalDetailAdapter);
        GoalDetailViewModel goalDetailViewModel = (GoalDetailViewModel) new ViewModelProvider(this, new ModelFactory(getApplication(), this.goalId)).get(GoalDetailViewModel.class);
        this.goalDetailViewModel = goalDetailViewModel;
        goalDetailViewModel.getGoal().observe(this, new Observer<GoalEntity>() { // from class: com.ktwapps.walletmanager.GoalDetail.1
            @Override // androidx.lifecycle.Observer
            public void onChanged(final GoalEntity goal) {
                if (goal != null) {
                    GoalDetail.this.status = goal.getStatus();
                    GoalDetail.this.symbol = (goal.getCurrency() == null || goal.getCurrency().length() == 0) ? SharePreferenceHelper.getAccountSymbol(GoalDetail.this.getApplicationContext()) : DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(goal.getCurrency()));
                    GoalDetail goalDetail = GoalDetail.this;
                    boolean z = true;
                    if (goalDetail.status != 1 && goal.getSaved() < goal.getAmount()) {
                        z = false;
                    }
                    goalDetail.isAchieve = z;
                    GoalDetail.this.goalDetailAdapter.setGoal(goal);
                }
            }
        });
        this.goalDetailViewModel.getGoalTrans().observe(this, new Observer<List<GoalTransEntity>>() { // from class: com.ktwapps.walletmanager.GoalDetail.2
            @Override // androidx.lifecycle.Observer
            public void onChanged(List<GoalTransEntity> goalTransEntities) {
                ArrayList arrayList = new ArrayList();
                final ArrayList arrayList2 = new ArrayList();
                if (goalTransEntities != null && goalTransEntities.size() > 0) {
                    for (GoalTransEntity goalTransEntity : goalTransEntities) {
                        java.util.Calendar calendar = java.util.Calendar.getInstance();
                        calendar.setTime(goalTransEntity.getDateTime());
                        calendar.set(14, 0);
                        calendar.set(13, 0);
                        calendar.set(12, 0);
                        calendar.set(11, 0);
                        if (!arrayList2.contains(Long.valueOf(calendar.getTime().getTime()))) {
                            arrayList2.add(Long.valueOf(calendar.getTime().getTime()));
                        }
                        arrayList2.add(goalTransEntity);
                    }
                    GoalDetail.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.GoalDetail.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            GoalDetail.this.goalDetailAdapter.setList(arrayList2);
                        }
                    });
                    return;
                }
                GoalDetail.this.goalDetailAdapter.setList(arrayList);
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
        if (item.getItemId() == R.id.menu_action_delete) {
            deleteGoal();
            return true;
        } else if (item.getItemId() == R.id.menu_action_edit) {
            editGoal();
            return true;
        } else {
            return false;
        }
    }

    private void deleteGoal() {
        Helper.showDialog(this, "", getResources().getString(R.string.goal_delete_message), getResources().getString(R.string.delete_positive), getResources().getString(R.string.cancel), new AnonymousClass3());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.ktwapps.walletmanager.GoalDetail$3  reason: invalid class name */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 implements DialogInterface.OnClickListener {
        AnonymousClass3() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.GoalDetail.3.1
                @Override // java.lang.Runnable
                public void run() {
                    AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(GoalDetail.this.getApplicationContext());
                    appDatabaseObject.goalDaoObject().deleteGoal(GoalDetail.this.goalId);
                    appDatabaseObject.goalDaoObject().deleteAllGoalTrans(GoalDetail.this.goalId);
                    GoalDetail.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.GoalDetail.3.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            GoalDetail.this.finish();
                            GoalDetail.this.overridePendingTransition(R.anim.scale_in, R.anim.right_to_left);
                        }
                    });
                }
            });
        }
    }

    private void editGoal() {
        Intent intent = new Intent(getApplicationContext(), CreateGoal.class);
        intent.putExtra(JamXmlElements.TYPE, -3);
        intent.putExtra("goalId", this.goalId);
        startActivity(intent);
        overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
    }

    private void withdrawGoal() {
        this.type = -14;
        Intent intent = new Intent(this, Calculator.class);
        intent.putExtra("amount", Utils.DOUBLE_EPSILON);
        startActivityForResult(intent, 1);
        overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
    }

    private void depositGoal() {
        if (this.isAchieve) {
            Helper.showDialog(this, "", getResources().getString(R.string.goal_achieve_message), getResources().getString(R.string.settle_positive), getResources().getString(R.string.settle_negative), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.GoalDetail.4
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.GoalDetail.4.1
                        @Override // java.lang.Runnable
                        public void run() {
                            AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(GoalDetail.this.getApplicationContext());
                            GoalEntity goalById = appDatabaseObject.goalDaoObject().getGoalById(GoalDetail.this.goalId);
                            goalById.setStatus(1);
                            goalById.setAchieveDate(java.util.Calendar.getInstance().getTime());
                            appDatabaseObject.goalDaoObject().update(goalById);
                        }
                    });
                }
            });
            return;
        }
        this.type = 14;
        Intent intent = new Intent(this, Calculator.class);
        intent.putExtra("amount", Utils.DOUBLE_EPSILON);
        startActivityForResult(intent, 1);
        overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
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

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 1) {
            final long longExtra = data.getLongExtra("amount", 0L) > 0 ? data.getLongExtra("amount", 0L) : 0L;
            if (longExtra != 0) {
                Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.GoalDetail.5
                    @Override // java.lang.Runnable
                    public void run() {
                        AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(GoalDetail.this.getApplicationContext());
                        GoalEntity goalById = appDatabaseObject.goalDaoObject().getGoalById(GoalDetail.this.goalId);
                        appDatabaseObject.goalDaoObject().insertGoalEntity(new GoalTransEntity(longExtra, java.util.Calendar.getInstance().getTime(), GoalDetail.this.goalId, GoalDetail.this.type, null));
                        appDatabaseObject.goalDaoObject().updateAmount(GoalDetail.this.goalId, goalById.getSaved() + (GoalDetail.this.type == 14 ? longExtra : -longExtra));
                    }
                });
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
            this.goalDetailAdapter.setAds(false);
        } else {
            this.goalDetailAdapter.setAds(true);
        }
    }

    @Override // com.ktwapps.walletmanager.Adapter.GoalDetailAdapter.OnItemClickListener
    public void OnItemClick(View v, int position) {
        if (v.getId() == R.id.depositWrapper) {
            depositGoal();
        } else if (v.getId() == R.id.withdrawWrapper) {
            withdrawGoal();
        } else {
            Intent intent = new Intent(this, CreateGoalTrans.class);
            intent.putExtra("goalId", this.goalId);
            intent.putExtra("goalTransId", ((GoalTransEntity) this.goalDetailAdapter.getList().get(position)).getId());
            intent.putExtra("symbol", this.symbol);
            startActivity(intent);
            overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
        }
    }

    @Override // com.ktwapps.walletmanager.Adapter.GoalDetailAdapter.OnItemClickListener
    public void OnItemLongClick(View v, int position) {
        showPopupMenu((GoalTransEntity) this.goalDetailAdapter.getList().get(position), v);
    }

    public void showPopupMenu(final GoalTransEntity trans, View view) {
        PopupMenu popupMenu = new PopupMenu(this, view, GravityCompat.END);
        popupMenu.inflate(R.menu.menu_detail);
        forcePopupIcon(popupMenu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // from class: com.ktwapps.walletmanager.GoalDetail.6
            @Override // androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_action_delete) {
                    GoalDetail.this.delete(trans);
                    return false;
                } else if (item.getItemId() == R.id.menu_action_edit) {
                    GoalDetail.this.edit(trans);
                    return false;
                } else {
                    return false;
                }
            }
        });
        popupMenu.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void delete(final GoalTransEntity transEntity) {
        Helper.showDialog(this, "", getResources().getString(R.string.transaction_delete_message), getResources().getString(R.string.delete_positive), getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.GoalDetail.7
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.GoalDetail.7.1
                    @Override // java.lang.Runnable
                    public void run() {
                        AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(GoalDetail.this.getApplicationContext());
                        GoalEntity goalById = appDatabaseObject.goalDaoObject().getGoalById(GoalDetail.this.goalId);
                        GoalTransEntity goalTransById = appDatabaseObject.goalDaoObject().getGoalTransById(transEntity.getId());
                        long saved = goalById.getSaved();
                        long amount = goalTransById.getAmount();
                        goalById.setSaved(goalTransById.getType() == -14 ? saved + amount : saved - amount);
                        if (goalById.getStatus() == 1 && goalById.getAmount() > goalById.getSaved()) {
                            goalById.setStatus(0);
                        }
                        appDatabaseObject.goalDaoObject().update(goalById);
                        appDatabaseObject.goalDaoObject().deleteGoalTrans(transEntity.getId());
                    }
                });
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void edit(GoalTransEntity trans) {
        Intent intent = new Intent(this, CreateGoalTrans.class);
        intent.putExtra("goalId", this.goalId);
        intent.putExtra("goalTransId", trans.getId());
        intent.putExtra("symbol", this.symbol);
        startActivity(intent);
        overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
    }

    private void forcePopupIcon(PopupMenu var1) {
        byte var3 = 0;

        int var2;
        label58:
        {
            Exception var10000;
            label63:
            {
                int var4;
                Field[] var5;
                boolean var10001;
                try {
                    var5 = var1.getClass().getDeclaredFields();
                    var4 = var5.length;
                } catch (Exception var8) {
                    var10000 = var8;
                    var10001 = false;
                    break label63;
                }

                var2 = 0;

                while (true) {
                    if (var2 >= var4) {
                        break label58;
                    }

                    Field var6 = var5[var2];

                    Object var10;
                    label53:
                    {
                        try {
                            if ("mPopup".equals(var6.getName())) {
                                var6.setAccessible(true);
                                var10 = var6.get(var1);
                                break label53;
                            }
                        } catch (Exception var9) {
                            var10000 = var9;
                            var10001 = false;
                            break;
                        }

                        ++var2;
                        continue;
                    }

                    var2 = var3;
                    if (var10 == null) {
                        break label58;
                    }

                    try {
                        Class.forName(var10.getClass().getName()).getMethod("setForceShowIcon", Boolean.TYPE).invoke(var10, Boolean.TRUE);
                    } catch (Exception var7) {
                        var10000 = var7;
                        var10001 = false;
                        break;
                    }

                    var2 = var3;
                    break label58;
                }
            }

            Exception var11 = var10000;
            var11.printStackTrace();
            var2 = var3;
        }

        for (var2 = var3; var2 < var1.getMenu().size(); ++var2) {
            Drawable var12 = var1.getMenu().getItem(var2).getIcon();
            if (var12 != null) {
                var12.mutate();
                var12.setColorFilter(Helper.getAttributeColor(this, 2130903968), PorterDuff.Mode.SRC_ATOP);
            }
        }
    }
}
