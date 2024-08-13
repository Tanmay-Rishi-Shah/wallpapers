package com.expance.merapaisa;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.os.LocaleListCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import com.expance.merapaisa.Adapter.SettingAdapter;
import com.expance.merapaisa.Adapter.SettingLanguageAdapter;
import com.expance.merapaisa.Adapter.SettingReminderAdapter;
import com.expance.merapaisa.Adapter.SettingStartupScreenAdapter;
import com.expance.merapaisa.Adapter.SettingWeekDayAdapter;
import com.expance.merapaisa.Model.SettingItem;
import com.expance.merapaisa.Utility.BillingHelper;
import com.expance.merapaisa.Utility.Constant;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Utility.NotificationHelper;
import com.expance.merapaisa.Utility.NotificationWorker;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class Setting extends AppCompatActivity implements SettingAdapter.OnItemClickListener, BillingHelper.BillingListener {
    SettingAdapter adapter;
    BillingHelper billingHelper;
    private boolean hideAds = false;
    RecyclerView recyclerView;

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
        setContentView(R.layout.activity_setting);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        setUpLayout();
        BillingHelper billingHelper = new BillingHelper(this);
        this.billingHelper = billingHelper;
        billingHelper.setListener(this);
        this.billingHelper.setUpBillingClient();
    }

    private void setUpLayout() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.settings));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SettingAdapter settingAdapter = new SettingAdapter(this);
        this.adapter = settingAdapter;
        settingAdapter.setListener(this);
        this.recyclerView.setAdapter(this.adapter);
        populateData();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        populateData();
        this.adapter.notifyDataSetChanged();
        this.billingHelper.registerBroadCast(this);
        this.billingHelper.queryPurchases();
        super.onResume();
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_action_pro) {
            startActivity(new Intent(getApplicationContext(), Premium.class));
            overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
            return true;
        }
        return false;
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        if (this.billingHelper.getBillingStatus() != 2) {
            getMenuInflater().inflate(R.menu.menu_premium, menu);
            return true;
        }
        return false;
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
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        this.billingHelper.unregisterBroadCast(this);
    }

    private void populateData() {
        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add(getResources().getString(R.string.management_capital));
        arrayList.add(new SettingItem(getResources().getString(R.string.account), "", false));
        arrayList.add(new SettingItem(getResources().getString(R.string.wallet), "", false));
        arrayList.add(new SettingItem(getResources().getString(R.string.setting_currency_title), "", false));
        arrayList.add(new SettingItem(getResources().getString(R.string.category), "", false));
        arrayList.add(new SettingItem(getResources().getString(R.string.budget), "", false));
        arrayList.add(new SettingItem(getResources().getString(R.string.saving_goal), "", false));
        arrayList.add(new SettingItem(getResources().getString(R.string.debt), "", false));
        arrayList.add(new SettingItem(getResources().getString(R.string.recurring), "", false));
        arrayList.add(getResources().getString(R.string.configuration_capital));
        arrayList.add(new SettingItem(getResources().getString(R.string.set_first_day_of_week), "NOT NULL", false));
        arrayList.add(new SettingItem(getResources().getString(R.string.startup_screen), "NOT NULL", false));
        arrayList.add(new SettingItem(getResources().getString(R.string.language), "NOT NULL", false));
        arrayList.add(new SettingItem(getResources().getString(R.string.passcode), "NOT NULL", false));
        arrayList.add(new SettingItem(getResources().getString(R.string.setting_notification_title), getResources().getString(R.string.setting_notification_hint), false));
//        arrayList.add(new SettingItem(getResources().getString(R.string.dark_mode), "", true));
        arrayList.add(new SettingItem(getResources().getString(R.string.carry_over), "", true));
        arrayList.add(new SettingItem(getResources().getString(R.string.future_balance), "", true));
        arrayList.add(getResources().getString(R.string.backup_capital));
//        arrayList.add(new SettingItem(getResources().getString(R.string.google_drive), "", false));
        arrayList.add(new SettingItem(getResources().getString(R.string.sd_card), "", false));
//        arrayList.add(new SettingItem(getResources().getString(R.string.exports), "", false));
        arrayList.add(getResources().getString(R.string.other_capital));
        arrayList.add(new SettingItem(getResources().getString(R.string.rate_us), "", false));
        arrayList.add(new SettingItem(getResources().getString(R.string.privacy_policy), "", false));
        arrayList.add(new SettingItem(getResources().getString(R.string.version), getResources().getString(R.string.version_hint), false));
        arrayList.add("");
        this.adapter.setList(arrayList);
    }

    @Override // androidx.appcompat.app.AppCompatActivity
    public boolean onSupportNavigateUp() {
        if (this.hideAds) {
            setResult(-1);
        }
        finish();
        overridePendingTransition(R.anim.scale_in, R.anim.bottom_to_top);
        return true;
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (this.hideAds) {
            setResult(-1);
        }
        super.onBackPressed();
        overridePendingTransition(R.anim.scale_in, R.anim.bottom_to_top);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onItemClick$0$com-ktwapps-walletmanager-Setting  reason: not valid java name */
    public /* synthetic */ void m229lambda$onItemClick$0$comktwappswalletmanagerSetting(AlertDialog alertDialog, AdapterView adapterView, View view, int i, long j) {
        String str;
        switch (i) {
            case 1:
                str = "en";
                break;
            case 2:
                str = "de";
                break;
            case 3:
                str = "es";
                break;
            case 4:
                str = "fr";
                break;
            case 5:
                str = "in";
                break;
            case 6:
                str = "ms";
                break;
            case 7:
                str = "it";
                break;
            case 8:
                str = "pt";
                break;
            case 9:
                str = "ru";
                break;
            case 10:
                str = "ja";
                break;
            case 11:
                str = "zh";
                break;
            case 12:
                str = "zh-TW";
                break;
            default:
                str = "";
                break;
        }
        alertDialog.dismiss();
        SharePreferenceHelper.setPreferLanguage(getApplicationContext(), str);
        if (str.isEmpty()) {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList());
        } else {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(str));
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // com.ktwapps.walletmanager.Adapter.SettingAdapter.OnItemClickListener
    public void onItemClick(View view, int position) {
        int i = R.style.AppThemeNight;
        switch (position) {
            case 1:
                startActivity(new Intent(this, ManageAccount.class));
                overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
                return;
            case 2:
                startActivity(new Intent(this, ManageWallet.class));
                overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
                return;
            case 3:
                startActivity(new Intent(this, Currency.class));
                overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
                return;
            case 4:
                startActivity(new Intent(this, ManageCategory.class));
                overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
                return;
            case 5:
                startActivity(new Intent(this, ManageBudget.class));
                overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
                return;
            case 6:
                startActivity(new Intent(this, ManageGoal.class));
                overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
                return;
            case 7:
                startActivity(new Intent(this, ManageDebt.class));
                overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
                return;
            case 8:
                startActivity(new Intent(this, ManageRecurring.class));
                overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
                return;
            case 9:
            case 17:
            case 19:
            default:
                return;
            case 10:
                if (this.billingHelper.getBillingStatus() == 2) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, SharePreferenceHelper.getThemeMode(this) == 1 ? R.style.AppThemeNight : R.style.AppTheme));
                    builder.setTitle(R.string.set_first_day_of_week);
                    builder.setPositiveButton(R.string.done, (DialogInterface.OnClickListener) null);
                    builder.setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null);
                    if (SharePreferenceHelper.getThemeMode(this) != 1) {
                        i = R.style.AppTheme;
                    }
                    builder.setAdapter(new SettingWeekDayAdapter(new ContextThemeWrapper(this, i), DataHelper.getFirstDayOfWeekData(this)), null);
                    final AlertDialog create = builder.create();
                    create.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.ktwapps.walletmanager.Setting.1
                        @Override // android.widget.AdapterView.OnItemClickListener
                        public void onItemClick(AdapterView<?> adapterView, View view2, int i2, long l) {
                            SharePreferenceHelper.setFirstDayOfWeek(Setting.this.getApplicationContext(), i2 + 1);
                            Setting.this.adapter.notifyDataSetChanged();
                            create.dismiss();
                        }
                    });
                    create.show();
                    return;
                }
                startActivity(new Intent(getApplicationContext(), Premium.class));
                overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
                return;
            case 11:
                if (this.billingHelper.getBillingStatus() == 2) {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(new ContextThemeWrapper(this, SharePreferenceHelper.getThemeMode(this) == 1 ? R.style.AppThemeNight : R.style.AppTheme));
                    builder2.setTitle(R.string.startup_screen);
                    builder2.setPositiveButton(R.string.done, (DialogInterface.OnClickListener) null);
                    builder2.setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null);
                    if (SharePreferenceHelper.getThemeMode(this) != 1) {
                        i = R.style.AppTheme;
                    }
                    builder2.setAdapter(new SettingStartupScreenAdapter(new ContextThemeWrapper(this, i), DataHelper.getStartupScreenData(this)), null);
                    final AlertDialog create2 = builder2.create();
                    create2.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.ktwapps.walletmanager.Setting.2
                        @Override // android.widget.AdapterView.OnItemClickListener
                        public void onItemClick(AdapterView<?> adapterView, View view2, int i2, long l) {
                            SharePreferenceHelper.setStartUpScreen(Setting.this.getApplicationContext(), i2);
                            Setting.this.adapter.notifyDataSetChanged();
                            create2.dismiss();
                        }
                    });
                    create2.show();
                    return;
                }
                startActivity(new Intent(getApplicationContext(), Premium.class));
                overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
                return;
            case 12:
                AlertDialog.Builder builder3 = new AlertDialog.Builder(new ContextThemeWrapper(this, SharePreferenceHelper.getThemeMode(this) == 1 ? R.style.AppThemeNight : R.style.AppTheme));
                builder3.setTitle(R.string.select_language);
                builder3.setPositiveButton(R.string.done, (DialogInterface.OnClickListener) null);
                builder3.setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null);
                if (SharePreferenceHelper.getThemeMode(this) != 1) {
                    i = R.style.AppTheme;
                }
                builder3.setAdapter(new SettingLanguageAdapter(new ContextThemeWrapper(this, i), DataHelper.getLanguageData(getApplicationContext())), null);
                final AlertDialog create3 = builder3.create();
                create3.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.ktwapps.walletmanager.Setting$$ExternalSyntheticLambda0
                    @Override // android.widget.AdapterView.OnItemClickListener
                    public final void onItemClick(AdapterView adapterView, View view2, int i2, long j) {
                        Setting.this.m229lambda$onItemClick$0$comktwappswalletmanagerSetting(create3, adapterView, view2, i2, j);
                    }
                });
                create3.show();
                return;
            case 13:
                startActivity(new Intent(getApplicationContext(), SettingPasscode.class));
                overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
                return;
            case 14:
                AlertDialog.Builder builder4 = new AlertDialog.Builder(new ContextThemeWrapper(this, SharePreferenceHelper.getThemeMode(this) == 1 ? R.style.AppThemeNight : R.style.AppTheme));
                builder4.setTitle(R.string.setting_notification_title);
                builder4.setPositiveButton(R.string.done, (DialogInterface.OnClickListener) null);
                builder4.setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null);
                if (SharePreferenceHelper.getThemeMode(this) != 1) {
                    i = R.style.AppTheme;
                }
                builder4.setAdapter(new SettingReminderAdapter(new ContextThemeWrapper(this, i)), null);
                final AlertDialog create4 = builder4.create();
                create4.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.ktwapps.walletmanager.Setting.3
                    @Override // android.widget.AdapterView.OnItemClickListener
                    public void onItemClick(AdapterView<?> adapterView, View view2, int i2, long l) {
                        SharePreferenceHelper.setReminderTime(Setting.this.getApplicationContext(), i2 == 7 ? 0 : i2 + 17);
                        Setting.this.adapter.notifyDataSetChanged();
                        create4.dismiss();
                        WorkManager workManager = WorkManager.getInstance(Setting.this.getApplicationContext());
                        workManager.cancelAllWorkByTag(Constant.NOTIFICATION_WORKER);
                        workManager.enqueue(new OneTimeWorkRequest.Builder(NotificationWorker.class).addTag(Constant.NOTIFICATION_WORKER).setInitialDelay(NotificationHelper.getMilliseconds(Setting.this.getApplicationContext()), TimeUnit.MILLISECONDS).build());
                    }
                });
                create4.show();
                return;
            /*case 15:
                if (this.billingHelper.getBillingStatus() == 2) {
                    SharePreferenceHelper.toggleThemeMode(this);
                    Intent intent = new Intent(getApplicationContext(), BaseActivity.class);
                    intent.setFlags(268468224);
                    startActivity(intent);
                    return;
                }
                startActivity(new Intent(getApplicationContext(), Premium.class));
                overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
                return;*/
            case 15:
//                Toast.makeText(this, "15", Toast.LENGTH_SHORT).show();
                SharePreferenceHelper.toggleCarryOver(this);
                return;
            case 16:
//                Toast.makeText(this, "16", Toast.LENGTH_SHORT).show();
                SharePreferenceHelper.toggleFutureBalance(this);
                return;
            /*case 19:
                startActivity(new Intent(this, BackUpGDrive.class));
                overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
                return;*/
            case 18:
//                Toast.makeText(this, "17", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, BackUpSDCard.class));
                overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
                return;
            /*case 21:
                startActivity(new Intent(this, Export.class));
                overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
                return;*/
            case 20:
//                Toast.makeText(this, "19", Toast.LENGTH_SHORT).show();
                try {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                    return;
                } catch (ActivityNotFoundException unused) {
                    return;
                }
            case 21:
//                Toast.makeText(this, "21", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent("android.intent.action.VIEW", Uri.parse(Constant.POLICY_URL));
                if (intent2.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent2);
                    return;
                }
                return;
        }
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
            this.adapter.setPremium(true);
        } else {
            this.adapter.setPremium(false);
        }
        this.adapter.notifyDataSetChanged();
        invalidateOptionsMenu();
    }
}
