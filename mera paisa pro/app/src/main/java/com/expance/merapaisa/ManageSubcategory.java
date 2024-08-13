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
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.expance.merapaisa.Adapter.ManageSubcategoryAdapter;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.Entity.SubcategoryEntity;
import com.expance.merapaisa.Utility.BillingHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import com.expance.merapaisa.Widget.SwipeAndDragViewHelper;
import java.util.List;
import java.util.concurrent.Executors;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class ManageSubcategory extends AppCompatActivity implements ManageSubcategoryAdapter.OnItemClickListener, View.OnClickListener, BillingHelper.BillingListener {
    ManageSubcategoryAdapter adapter;
    BillingHelper billingHelper;
    int categoryId;
    ConstraintLayout emptyWrapper;
    FloatingActionButton fab;
    RecyclerView recyclerView;

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onLoaded() {
    }

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onPurchasedPending() {
    }

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onPurchasedSucceed() {
    }

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onReceiveBroadCast() {
    }

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onSkuReady() {
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
        setContentView(R.layout.activity_sub_category);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        this.categoryId = getIntent().getIntExtra("categoryId", 0);
        setUpLayout();
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.scale_in, R.anim.right_to_left);
    }

    @Override // androidx.appcompat.app.AppCompatActivity
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(R.anim.scale_in, R.anim.right_to_left);
        return true;
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
        populateData();
        this.billingHelper.queryPurchases();
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_action_edit) {
            Intent intent = new Intent(this, CreateCategory.class);
            intent.putExtra("categoryId", getIntent().getIntExtra("categoryId", 0));
            intent.putExtra(JamXmlElements.TYPE, getIntent().getIntExtra(JamXmlElements.TYPE, 0));
            startActivityForResult(intent, 5);
            overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
            return true;
        }
        return false;
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        for (int i = 0; i < menu.size(); i++) {
            Drawable icon = menu.getItem(i).getIcon();
            if (icon != null) {
                icon.mutate();
                icon.setColorFilter(Helper.getAttributeColor(this, R.attr.primaryDarkTextColor), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return true;
    }

    private void setUpLayout() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getIntent().getStringExtra("name"));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.fab = (FloatingActionButton) findViewById(R.id.fab);
        this.emptyWrapper = (ConstraintLayout) findViewById(R.id.emptyWrapper);
        this.adapter = new ManageSubcategoryAdapter(this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setAdapter(this.adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeAndDragViewHelper(this.adapter));
        this.adapter.setTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(this.recyclerView);
        this.adapter.setListener(this);
        this.fab.setOnClickListener(this);
        this.fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(getIntent().getStringExtra(TypedValues.Custom.S_COLOR))));
        BillingHelper billingHelper = new BillingHelper(this);
        this.billingHelper = billingHelper;
        billingHelper.setListener(this);
        this.billingHelper.setUpBillingClient();
    }

    private void populateData() {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.ManageSubcategory$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                ManageSubcategory.this.m218x8fb91c21();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$populateData$1$com-ktwapps-walletmanager-ManageSubcategory  reason: not valid java name */
    public /* synthetic */ void m218x8fb91c21() {
        final List<SubcategoryEntity> subCategoryByCategoryId = AppDatabaseObject.getInstance(this).categoryDaoObject().getSubCategoryByCategoryId(this.categoryId);
        runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.ManageSubcategory$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ManageSubcategory.this.m217xf51859a0(subCategoryByCategoryId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$populateData$0$com-ktwapps-walletmanager-ManageSubcategory  reason: not valid java name */
    public /* synthetic */ void m217xf51859a0(List list) {
        this.adapter.setList(list);
        this.emptyWrapper.setVisibility(list.size() == 0 ? 0 : 8);
        this.adapter.notifyDataSetChanged();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.fab) {
            if (this.billingHelper.getBillingStatus() == 2) {
                Intent intent = new Intent(this, CreateSubcategory.class);
                intent.putExtra("categoryId", this.categoryId);
                startActivity(intent);
                overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
                return;
            }
            Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.ManageSubcategory$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    ManageSubcategory.this.m212lambda$onClick$3$comktwappswalletmanagerManageSubcategory();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onClick$3$com-ktwapps-walletmanager-ManageSubcategory  reason: not valid java name */
    public /* synthetic */ void m212lambda$onClick$3$comktwappswalletmanagerManageSubcategory() {
        final int totalSubcategoryByCategoryId = AppDatabaseObject.getInstance(getApplicationContext()).categoryDaoObject().getTotalSubcategoryByCategoryId(this.categoryId);
        runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.ManageSubcategory$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                ManageSubcategory.this.m211lambda$onClick$2$comktwappswalletmanagerManageSubcategory(totalSubcategoryByCategoryId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onClick$2$com-ktwapps-walletmanager-ManageSubcategory  reason: not valid java name */
    public /* synthetic */ void m211lambda$onClick$2$comktwappswalletmanagerManageSubcategory(int i) {
        if (i >= 3) {
            startActivity(new Intent(getApplicationContext(), Premium.class));
            overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
            return;
        }
        Intent intent = new Intent(getApplicationContext(), CreateSubcategory.class);
        intent.putExtra("categoryId", this.categoryId);
        startActivity(intent);
        overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.ManageSubcategory$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                ManageSubcategory.this.m216lambda$onPause$4$comktwappswalletmanagerManageSubcategory();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onPause$4$com-ktwapps-walletmanager-ManageSubcategory  reason: not valid java name */
    public /* synthetic */ void m216lambda$onPause$4$comktwappswalletmanagerManageSubcategory() {
        AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(getApplicationContext());
        for (int i = 0; i < this.adapter.list.size(); i++) {
            appDatabaseObject.categoryDaoObject().updateSubcategoryOrdering(i, this.adapter.list.get(i).getId());
        }
    }

    @Override // com.ktwapps.walletmanager.Adapter.ManageSubcategoryAdapter.OnItemClickListener
    public void onItemClick(View view, final int position, int type) {
        if (type == 23) {
            Helper.showDialog(this, "", getResources().getString(R.string.subcategory_delete_message), getResources().getString(R.string.delete_positive), getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.ManageSubcategory$$ExternalSyntheticLambda5
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    ManageSubcategory.this.m215lambda$onItemClick$7$comktwappswalletmanagerManageSubcategory(position, dialogInterface, i);
                }
            });
            return;
        }
        Intent intent = new Intent(this, CreateSubcategory.class);
        intent.putExtra("categoryId", this.categoryId);
        intent.putExtra("id", this.adapter.getList().get(position).getId());
        startActivity(intent);
        overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onItemClick$7$com-ktwapps-walletmanager-ManageSubcategory  reason: not valid java name */
    public /* synthetic */ void m215lambda$onItemClick$7$comktwappswalletmanagerManageSubcategory(final int i, DialogInterface dialogInterface, int i2) {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.ManageSubcategory$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                ManageSubcategory.this.m214lambda$onItemClick$6$comktwappswalletmanagerManageSubcategory(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onItemClick$6$com-ktwapps-walletmanager-ManageSubcategory  reason: not valid java name */
    public /* synthetic */ void m214lambda$onItemClick$6$comktwappswalletmanagerManageSubcategory(final int i) {
        AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(getApplicationContext());
        appDatabaseObject.transDaoObject().removeSubcategory(this.adapter.getList().get(i).getId());
        appDatabaseObject.recurringDaoObject().removeSubcategory(this.adapter.getList().get(i).getId());
        appDatabaseObject.templateDaoObject().removeSubcategory(this.adapter.getList().get(i).getId());
        appDatabaseObject.categoryDaoObject().deleteSubcategoryById(this.adapter.getList().get(i).getId());
        runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.ManageSubcategory$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                ManageSubcategory.this.m213lambda$onItemClick$5$comktwappswalletmanagerManageSubcategory(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onItemClick$5$com-ktwapps-walletmanager-ManageSubcategory  reason: not valid java name */
    public /* synthetic */ void m213lambda$onItemClick$5$comktwappswalletmanagerManageSubcategory(int i) {
        this.adapter.removeItem(i);
        this.emptyWrapper.setVisibility(this.adapter.getList().size() == 0 ? 0 : 8);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5 && resultCode == -1) {
            getSupportActionBar().setTitle(data.getStringExtra("name"));
            this.fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(data.getStringExtra(TypedValues.Custom.S_COLOR))));
        }
    }
}
