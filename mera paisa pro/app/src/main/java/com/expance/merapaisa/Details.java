package com.expance.merapaisa;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.Entity.AccountEntity;
import com.expance.merapaisa.Database.Entity.BudgetEntity;
import com.expance.merapaisa.Database.Entity.DebtEntity;
import com.expance.merapaisa.Database.Entity.DebtTransEntity;
import com.expance.merapaisa.Database.Entity.TransEntity;
import com.expance.merapaisa.Database.Entity.WalletEntity;
import com.expance.merapaisa.Database.ViewModel.ModelFactory;
import com.expance.merapaisa.Database.ViewModel.TransactionDetailViewModel;
import com.expance.merapaisa.Model.Trans;
import com.expance.merapaisa.Utility.AdsHelper;
import com.expance.merapaisa.Utility.BillingHelper;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Utility.DateHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.PicassoTransformation;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.util.List;
import java.util.concurrent.Executors;
import org.apache.http.cookie.ClientCookie;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

/* loaded from: classes3.dex */
public class Details extends AppCompatActivity implements View.OnClickListener, BillingHelper.BillingListener {
    FrameLayout adView;
    TextView amountLabel;
    AdView bannerAdView;
    BillingHelper billingHelper;
    TextView categoryLabel;
    View colorView;
    TextView dateLabel;
    TextView feeLabel;
    TextView feeTitleLabel;
    ImageView imageView;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    CardView imageWrapper1;
    CardView imageWrapper2;
    CardView imageWrapper3;
    boolean isAdsInit;
    LinearLayout linearLayout;
    TextView memoLabel;
    TextView memoTitleLabel;
    TextView nameLabel;
    Trans trans;
    int transId;
    TransactionDetailViewModel transactionDetailViewModel;
    TextView typeLabel;
    TextView walletLabel;

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
        setContentView(R.layout.activity_details);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);

        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        this.transId = getIntent().getIntExtra("transId", 0);
        setUpLayout();
        this.adView = (FrameLayout) findViewById(R.id.adView);
        BillingHelper billingHelper = new BillingHelper(this);
        this.billingHelper = billingHelper;
        billingHelper.setListener(this);
        this.billingHelper.setUpBillingClient();
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
            getSupportActionBar().setTitle(R.string.record);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        this.memoTitleLabel = (TextView) findViewById(R.id.memoTitleLabel);
        this.memoLabel = (TextView) findViewById(R.id.memoLabel);
        this.feeTitleLabel = (TextView) findViewById(R.id.feeTitleLabel);
        this.feeLabel = (TextView) findViewById(R.id.feeLabel);
        this.nameLabel = (TextView) findViewById(R.id.nameLabel);
        this.categoryLabel = (TextView) findViewById(R.id.categoryLabel);
        this.amountLabel = (TextView) findViewById(R.id.amountLabel);
        this.dateLabel = (TextView) findViewById(R.id.dateLabel);
        this.walletLabel = (TextView) findViewById(R.id.walletLabel);
        this.colorView = findViewById(R.id.colorView);
        this.typeLabel = (TextView) findViewById(R.id.typeLabel);
        this.imageView1 = (ImageView) findViewById(R.id.imageView1);
        this.imageView2 = (ImageView) findViewById(R.id.imageView2);
        this.imageView3 = (ImageView) findViewById(R.id.imageView3);
        this.imageView = (ImageView) findViewById(R.id.imageView);
        this.imageWrapper1 = (CardView) findViewById(R.id.imageWrapper1);
        this.imageWrapper2 = (CardView) findViewById(R.id.imageWrapper2);
        this.imageWrapper3 = (CardView) findViewById(R.id.imageWrapper3);
        this.imageView1.setTag(0);
        this.imageView2.setTag(1);
        this.imageView3.setTag(2);
        this.imageView1.setOnClickListener(this);
        this.imageView2.setOnClickListener(this);
        this.imageView3.setOnClickListener(this);
        TransactionDetailViewModel transactionDetailViewModel = (TransactionDetailViewModel) new ViewModelProvider(this, new ModelFactory(getApplication(), this.transId)).get(TransactionDetailViewModel.class);
        this.transactionDetailViewModel = transactionDetailViewModel;
        transactionDetailViewModel.getTrans().observe(this, new AnonymousClass1());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.ktwapps.walletmanager.Details$1  reason: invalid class name */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 implements Observer<Trans> {
        AnonymousClass1() {
        }

        @Override // androidx.lifecycle.Observer
        public void onChanged(final Trans trans) {
            String wallet;
            if (trans != null) {
                Details.this.trans = trans;
                String str = DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(trans.getCurrency()));
                String note = trans.getNote(Details.this.getApplicationContext());
                String string = trans.getType() == 2 ? Details.this.getResources().getString(R.string.transfer) : trans.getCategory(Details.this.getApplicationContext());
                String beautifyAmount = Helper.getBeautifyAmount(str, trans.getAmount());
                String formattedDateTime = DateHelper.getFormattedDateTime(Details.this.getApplicationContext(), trans.getDateTime());
                String type = Details.this.getType(trans.getType());
                if (trans.getType() == 2) {
                    wallet = trans.getWallet() + Details.this.getResources().getString(R.string.transfer_to) + trans.getTransferWallet();
                } else {
                    wallet = trans.getWallet();
                }
                String color = trans.getColor();
                String media = trans.getMedia();
                String memo = trans.getMemo() == null ? "" : trans.getMemo();
                final int feeId = trans.getFeeId();
                Details.this.amountLabel.setTextColor(Details.this.getAmountColor(trans.getType()));
                Details.this.nameLabel.setText(note);
                Details.this.categoryLabel.setText(string);
                Details.this.amountLabel.setText(beautifyAmount);
                Details.this.dateLabel.setText(formattedDateTime);
                Details.this.walletLabel.setText(wallet);
                Details.this.typeLabel.setText(type);
                Details.this.memoLabel.setText(memo);
                if (trans.getType() == 2) {
                    Details.this.imageView.setImageResource(R.drawable.transfer);
                } else {
                    Details.this.imageView.setImageResource(DataHelper.getCategoryIcons().get(trans.getIcon()).intValue());
                }
                Details.this.colorView.getBackground().setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_OVER);
                Details.this.imageWrapper1.setVisibility(8);
                Details.this.imageWrapper2.setVisibility(8);
                Details.this.imageWrapper3.setVisibility(8);
                Details.this.linearLayout.setVisibility(8);
                Details.this.imageView1.setImageBitmap(null);
                Details.this.imageView2.setImageBitmap(null);
                Details.this.imageView3.setImageBitmap(null);
                if (media != null) {
                    Details.this.linearLayout.setVisibility(0);
                    String[] split = media.split(",");
                    if (split.length == 1) {
                        Details.this.imageWrapper1.setVisibility(0);
                        Details.this.imageWrapper2.setVisibility(8);
                        Details.this.imageWrapper3.setVisibility(8);
                        Picasso.get().load(new File(split[0])).transform(new PicassoTransformation(Details.this.imageView1)).into(Details.this.imageView1);
                    } else if (split.length == 2) {
                        Details.this.imageWrapper1.setVisibility(0);
                        Details.this.imageWrapper2.setVisibility(0);
                        Details.this.imageWrapper3.setVisibility(8);
                        Picasso.get().load(new File(split[0])).transform(new PicassoTransformation(Details.this.imageView1)).into(Details.this.imageView1);
                        Picasso.get().load(new File(split[1])).transform(new PicassoTransformation(Details.this.imageView2)).into(Details.this.imageView2);
                    } else if (split.length == 3) {
                        Details.this.imageWrapper1.setVisibility(0);
                        Details.this.imageWrapper2.setVisibility(0);
                        Details.this.imageWrapper3.setVisibility(0);
                        Picasso.get().load(new File(split[0])).transform(new PicassoTransformation(Details.this.imageView1)).into(Details.this.imageView1);
                        Picasso.get().load(new File(split[1])).transform(new PicassoTransformation(Details.this.imageView2)).into(Details.this.imageView2);
                        Picasso.get().load(new File(split[2])).transform(new PicassoTransformation(Details.this.imageView3)).into(Details.this.imageView3);
                    }
                }
                Details.this.memoLabel.setVisibility(memo.length() > 0 ? 0 : 8);
                Details.this.memoTitleLabel.setVisibility(memo.length() <= 0 ? 8 : 0);
                Details.this.feeLabel.setVisibility(8);
                Details.this.feeTitleLabel.setVisibility(8);
                if (feeId != 0) {
                    Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.Details.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            final Trans transById = AppDatabaseObject.getInstance(Details.this).transDaoObject().getTransById(feeId, SharePreferenceHelper.getAccountId(Details.this));
                            if (transById != null) {
                                Details.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.Details.1.1.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        String beautifyAmount2 = Helper.getBeautifyAmount(DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(transById.getCurrency())), transById.getAmount());
                                        Details.this.feeTitleLabel.setVisibility(0);
                                        Details.this.feeLabel.setVisibility(0);
                                        Details.this.feeLabel.setText(beautifyAmount2);
                                        Details.this.feeLabel.setTextColor(ContextCompat.getColor(Details.this.getApplicationContext(), R.color.expense));
                                    }
                                });
                            }
                        }
                    });
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getAmountColor(int type) {
        if (type == 2) {
            return Helper.getAttributeColor(this, R.attr.primaryTextColor);
        }
        if (type == 1) {
            return getResources().getColor(R.color.expense);
        }
        return getResources().getColor(R.color.income);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getType(int type) {
        if (type == 2) {
            return getResources().getString(R.string.transfer);
        }
        if (type == 1) {
            return getResources().getString(R.string.expense);
        }
        return getResources().getString(R.string.income);
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_action_delete) {
            Helper.showDialog(this, "", getResources().getString(R.string.transaction_delete_message), getResources().getString(R.string.delete_positive), getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.Details.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Details.this.deleteTrans();
                }
            });
            return true;
        } else if (item.getItemId() == R.id.menu_action_edit) {
            if (this.trans.getDebtId() != 0) {
                if (this.trans.getDebtTransId() != 0) {
                    Intent intent = new Intent(this, CreateDebtTrans.class);
                    intent.putExtra("debtId", this.trans.getDebtId());
                    intent.putExtra("debtType", this.trans.getDebtType());
                    intent.putExtra("debtTransId", this.trans.getDebtTransId());
                    intent.putExtra("isFromTransaction", true);
                    intent.putExtra(JamXmlElements.TYPE, 1);
                    startActivity(intent);
                    overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
                }
            } else {
                Intent intent2 = new Intent(this, EditTransaction.class);
                intent2.putExtra("transId", this.transId);
                startActivity(intent2);
                overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
            }
            return true;
        } else {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deleteTrans() {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.Details.3
            @Override // java.lang.Runnable
            public void run() {
                com.expance.merapaisa.Model.Currency currency;
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(Details.this.getApplicationContext());
                if (Details.this.trans.getFeeId() != 0) {
                    appDatabaseObject.transDaoObject().deleteTrans(Details.this.trans.getFeeId());
                } else {
                    int transferTrans = appDatabaseObject.transDaoObject().getTransferTrans(Details.this.transId);
                    if (transferTrans != 0) {
                        TransEntity transEntityById = appDatabaseObject.transDaoObject().getTransEntityById(transferTrans);
                        transEntityById.setFeeId(0);
                        appDatabaseObject.transDaoObject().updateTrans(transEntityById);
                    }
                }
                appDatabaseObject.transDaoObject().deleteTrans(Details.this.transId);
                if (Details.this.trans.getMedia() != null) {
                    for (String str : Details.this.trans.getMedia().split(",")) {
                        File file = new File(str);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                }
                appDatabaseObject.transDaoObject().deleteTransMedia(Details.this.transId);
                WalletEntity walletById = appDatabaseObject.walletDaoObject().getWalletById(Details.this.trans.getWalletId());
                if (Details.this.trans.getType() == 2) {
                    WalletEntity walletById2 = appDatabaseObject.walletDaoObject().getWalletById(Details.this.trans.getTransferWalletId());
                    walletById.setAmount(walletById.getAmount() + Details.this.trans.getAmount());
                    walletById2.setAmount(walletById2.getAmount() - Details.this.trans.getTransAmount());
                    appDatabaseObject.walletDaoObject().updateWallet(walletById2);
                    if (walletById.getAccountId() == walletById2.getAccountId()) {
                        if (walletById.getExclude() == 0 && walletById2.getExclude() == 1) {
                            AccountEntity entityById = appDatabaseObject.accountDaoObject().getEntityById(SharePreferenceHelper.getAccountId(Details.this.getApplicationContext()));
                            entityById.setBalance(entityById.getBalance() + Details.this.trans.getAmount());
                            appDatabaseObject.accountDaoObject().updateAccount(entityById);
                        } else if (walletById.getExclude() == 1 && walletById2.getExclude() == 0) {
                            AccountEntity entityById2 = appDatabaseObject.accountDaoObject().getEntityById(SharePreferenceHelper.getAccountId(Details.this.getApplicationContext()));
                            entityById2.setBalance(entityById2.getBalance() - Details.this.trans.getAmount());
                            appDatabaseObject.accountDaoObject().updateAccount(entityById2);
                        }
                    } else {
                        if (walletById.getExclude() == 0) {
                            AccountEntity entityById3 = appDatabaseObject.accountDaoObject().getEntityById(walletById.getAccountId());
                            entityById3.setBalance(entityById3.getBalance() + Details.this.trans.getAmount());
                            appDatabaseObject.accountDaoObject().updateAccount(entityById3);
                        }
                        if (walletById2.getExclude() == 0) {
                            AccountEntity entityById4 = appDatabaseObject.accountDaoObject().getEntityById(walletById2.getAccountId());
                            entityById4.setBalance(entityById4.getBalance() - Details.this.trans.getAmount());
                            appDatabaseObject.accountDaoObject().updateAccount(entityById4);
                        }
                    }
                } else {
                    walletById.setAmount(walletById.getAmount() - Details.this.trans.getAmount());
                    if (walletById.getExclude() == 0) {
                        AccountEntity entityById5 = appDatabaseObject.accountDaoObject().getEntityById(SharePreferenceHelper.getAccountId(Details.this.getApplicationContext()));
                        entityById5.setBalance(entityById5.getBalance() - Details.this.trans.getAmount());
                        appDatabaseObject.accountDaoObject().updateAccount(entityById5);
                    }
                    if (Details.this.trans.getType() == 1) {
                        for (BudgetEntity budgetEntity : appDatabaseObject.budgetDaoObject().getBudgetEntityByCategory(appDatabaseObject.budgetDaoObject().getBudgetIds(Details.this.trans.getCategoryId()), SharePreferenceHelper.getAccountId(Details.this.getApplicationContext()), 0)) {
                            budgetEntity.setSpent(budgetEntity.getSpent() + Details.this.trans.getAmount());
                            appDatabaseObject.budgetDaoObject().updateBudget(budgetEntity);
                        }
                    }
                }
                if (Details.this.trans.getDebtId() != 0 && Details.this.trans.getDebtTransId() != 0) {
                    int accountId = SharePreferenceHelper.getAccountId(Details.this.getApplicationContext());
                    appDatabaseObject.debtDaoObject().deleteDebtTrans(Details.this.trans.getDebtTransId());
                    DebtEntity debtById = appDatabaseObject.debtDaoObject().getDebtById(Details.this.trans.getDebtId());
                    com.expance.merapaisa.Model.Currency debtCurrency = appDatabaseObject.debtDaoObject().getDebtCurrency(accountId, Details.this.trans.getDebtId());
                    if (debtCurrency == null) {
                        debtCurrency = new com.expance.merapaisa.Model.Currency(1.0d, "Empty");
                    }
                    List<DebtTransEntity> allDebtTrans = appDatabaseObject.debtDaoObject().getAllDebtTrans(Details.this.trans.getDebtId());
                    long amount = debtById.getAmount();
                    long j = 0;
                    for (DebtTransEntity debtTransEntity : allDebtTrans) {
                        com.expance.merapaisa.Model.Currency debtTransCurrency = appDatabaseObject.debtDaoObject().getDebtTransCurrency(accountId, Details.this.trans.getDebtId(), debtTransEntity.getId());
                        if (debtTransCurrency == null) {
                            if (debtTransEntity.getType() == 0) {
                                j += debtTransEntity.getAmount();
                            } else {
                                amount += debtTransEntity.getAmount();
                            }
                            currency = debtCurrency;
                        } else {
                            double rate = debtTransCurrency.getRate() / debtCurrency.getRate();
                            if (debtTransEntity.getType() == 0) {
                                currency = debtCurrency;
                                j = (long) (j + (debtTransEntity.getAmount() * rate));
                            } else {
                                currency = debtCurrency;
                                amount = (long) (amount + (debtTransEntity.getAmount() * rate));
                            }
                        }
                        debtCurrency = currency;
                    }
                    if (j >= amount) {
                        debtById.setStatus(1);
                    } else {
                        debtById.setStatus(0);
                    }
                    appDatabaseObject.debtDaoObject().updateDebt(debtById);
                }
                appDatabaseObject.walletDaoObject().updateWallet(walletById);
                Details.this.finish();
                Details.this.overridePendingTransition(R.anim.scale_in, R.anim.right_to_left);
            }
        });
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

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        Intent intent = new Intent(this, PhotoViewer.class);
        intent.putExtra(ClientCookie.PATH_ATTR, this.trans.getMedia());
        intent.putExtra(FirebaseAnalytics.Param.INDEX, ((Integer) view.getTag()).intValue());
        startActivity(intent);
    }

    private void initAds() {
        if (this.isAdsInit) {
            return;
        }
        this.isAdsInit = true;
        AdView adView = new AdView(this);
        this.bannerAdView = adView;
        adView.setAdUnitId(getResources().getString(R.string.ad_unit_detail));
        this.adView.addView(this.bannerAdView);
        this.bannerAdView.setAdSize(AdsHelper.getAdSize(this));
        this.bannerAdView.setAdListener(new AdListener() { // from class: com.ktwapps.walletmanager.Details.4
            @Override // com.google.android.gms.ads.AdListener
            public void onAdLoaded() {
                super.onAdLoaded();
                Details.this.adView.setVisibility(0);
            }
        });
        this.bannerAdView.loadAd(new AdRequest.Builder().build());
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
        this.billingHelper.unregisterBroadCast(this);
        super.onPause();
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
            this.adView.setVisibility(8);
        } else {
            initAds();
        }
    }
}
