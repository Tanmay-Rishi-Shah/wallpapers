package com.expance.merapaisa.Utility;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.QueryPurchasesParams;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class BillingHelper implements PurchasesUpdatedListener, BillingClientStateListener {
    private BillingClient billingClient;
    private int billingStatus;
    private Context context;
    private BillingListener listener;
    private PremiumBroadcastReceiver premiumBroadcastReceiver = new PremiumBroadcastReceiver(this, null);
    private ProductDetails productDetails;

    /* loaded from: classes3.dex */
    public interface BillingListener {
        void onLoaded();

        void onPurchasedPending();

        void onPurchasedSucceed();

        void onReceiveBroadCast();

        void onSkuReady();
    }

    public BillingHelper(Context context) {
        this.context = context;
    }

    public void setListener(BillingListener listener) {
        this.listener = listener;
    }

    public void setUpBillingClient() {
        this.billingStatus = 2;
        BillingClient build = BillingClient.newBuilder(this.context).enablePendingPurchases().setListener(this).build();
        this.billingClient = build;
        build.startConnection(this);
    }

    public void registerBroadCast(Context context) {
        context.registerReceiver(this.premiumBroadcastReceiver, new IntentFilter(Constant.PREMIUM_TAG));
    }

    public void unregisterBroadCast(Context context) {
        context.unregisterReceiver(this.premiumBroadcastReceiver);
    }

    public void queryPurchases() {
        if (this.billingClient.isReady()) {
            this.billingClient.queryPurchasesAsync(QueryPurchasesParams.newBuilder().setProductType("inapp").build(), new PurchasesResponseListener() { // from class: com.ktwapps.walletmanager.Utility.BillingHelper$$ExternalSyntheticLambda2
                @Override // com.android.billingclient.api.PurchasesResponseListener
                public final void onQueryPurchasesResponse(BillingResult billingResult, List list) {
                    BillingHelper.this.m244x5b82aa08(billingResult, list);
                }
            });
        } else if (SharePreferenceHelper.getPremium(this.context) != 2) {
            this.billingStatus = 2;
            this.listener.onLoaded();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$queryPurchases$3$com-ktwapps-walletmanager-Utility-BillingHelper  reason: not valid java name */
    public /* synthetic */ void m244x5b82aa08(BillingResult billingResult, final List list) {
        new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.ktwapps.walletmanager.Utility.BillingHelper$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                BillingHelper.this.m243x5bf91007(list);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$queryPurchases$2$com-ktwapps-walletmanager-Utility-BillingHelper  reason: not valid java name */
    public /* synthetic */ void m243x5bf91007(List list) {
        int premium = SharePreferenceHelper.getPremium(this.context);
        if (list.size() > 0) {
            Purchase purchase = (Purchase) list.get(0);
            if (purchase.getPurchaseState() == 1) {
                if (premium != 2) {
                    SharePreferenceHelper.setPremium(this.context, 2);
                }
                this.billingStatus = 2;
                this.listener.onLoaded();
                return;
            } else if (purchase.getPurchaseState() == 2) {
                if (premium != 4) {
                    SharePreferenceHelper.setPremium(this.context, 2);
                }
                this.billingStatus = 2;
                this.listener.onLoaded();
                return;
            } else {
                return;
            }
        }
        if (premium != 3) {
            SharePreferenceHelper.setPremium(this.context, 2);
        }
        this.billingStatus = 2;
        this.listener.onLoaded();
        if (this.productDetails == null) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(QueryProductDetailsParams.Product.newBuilder().setProductId("pro_version").setProductType("inapp").build());
            this.billingClient.queryProductDetailsAsync(QueryProductDetailsParams.newBuilder().setProductList(arrayList).build(), new ProductDetailsResponseListener() { // from class: com.ktwapps.walletmanager.Utility.BillingHelper$$ExternalSyntheticLambda1
                @Override // com.android.billingclient.api.ProductDetailsResponseListener
                public final void onProductDetailsResponse(BillingResult billingResult, List list2) {
                    BillingHelper.this.m242x5c6f7606(billingResult, list2);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$queryPurchases$1$com-ktwapps-walletmanager-Utility-BillingHelper  reason: not valid java name */
    public /* synthetic */ void m242x5c6f7606(BillingResult billingResult, List list) {
        if (billingResult.getResponseCode() == 0) {
            if (list.size() > 0) {
                this.productDetails = (ProductDetails) list.get(0);
            }
            ((Activity) this.context).runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.Utility.BillingHelper$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    BillingHelper.this.m241x5ce5dc05();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$queryPurchases$0$com-ktwapps-walletmanager-Utility-BillingHelper  reason: not valid java name */
    public /* synthetic */ void m241x5ce5dc05() {
        this.listener.onSkuReady();
    }

    @Override // com.android.billingclient.api.BillingClientStateListener
    public void onBillingSetupFinished(BillingResult billingResult) {
        queryPurchases();
    }

    @Override // com.android.billingclient.api.BillingClientStateListener
    public void onBillingServiceDisconnected() {
        if (SharePreferenceHelper.getPremium(this.context) != 2) {
            this.billingStatus = 2;
            this.listener.onLoaded();
        }
    }

    @Override // com.android.billingclient.api.PurchasesUpdatedListener
    public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
        if (billingResult.getResponseCode() != 0) {
            if (billingResult.getResponseCode() == 7) {
                this.billingStatus = 2;
                SharePreferenceHelper.setPremium(this.context, 2);
                ((Activity) this.context).runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.Utility.BillingHelper$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        BillingHelper.this.m240x9268f928();
                    }
                });
            }
        } else if (purchases != null) {
            for (final Purchase purchase : purchases) {
                new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.ktwapps.walletmanager.Utility.BillingHelper$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        BillingHelper.this.m239x92df5f27(purchase);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onPurchasesUpdated$5$com-ktwapps-walletmanager-Utility-BillingHelper  reason: not valid java name */
    public /* synthetic */ void m240x9268f928() {
        this.listener.onLoaded();
    }

    public void launchBillingFlow() {
        if (this.productDetails != null) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(this.productDetails).build());
            this.billingClient.launchBillingFlow((Activity) this.context, BillingFlowParams.newBuilder().setProductDetailsParamsList(arrayList).build());
        }
    }

    public ProductDetails getSkuDetail() {
        return this.productDetails;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: handlePurchase */
    public void m239x92df5f27(Purchase purchase) {
        if (purchase.getPurchaseState() == 1) {
            if (purchase.isAcknowledged()) {
                return;
            }
            this.billingClient.acknowledgePurchase(AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.getPurchaseToken()).build(), new AnonymousClass1());
        } else if (purchase.getPurchaseState() == 2) {
            if (SharePreferenceHelper.getPremium(this.context) != 4) {
                SharePreferenceHelper.setPremium(this.context, 2);
            }
            this.billingStatus = 2;
            this.listener.onPurchasedPending();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.ktwapps.walletmanager.Utility.BillingHelper$1  reason: invalid class name */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 implements AcknowledgePurchaseResponseListener {
        AnonymousClass1() {
        }

        @Override // com.android.billingclient.api.AcknowledgePurchaseResponseListener
        public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
            new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.ktwapps.walletmanager.Utility.BillingHelper$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    BillingHelper.AnonymousClass1.this.m245x87bc634d();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$onAcknowledgePurchaseResponse$0$com-ktwapps-walletmanager-Utility-BillingHelper$1  reason: not valid java name */
        public /* synthetic */ void m245x87bc634d() {
            SharePreferenceHelper.setPremium(BillingHelper.this.context, 2);
            BillingHelper.this.billingStatus = 2;
            BillingHelper.this.listener.onPurchasedSucceed();
            BillingHelper.this.context.sendBroadcast(new Intent(Constant.PREMIUM_TAG));
        }
    }

    public int getBillingStatus() {
        return this.billingStatus;
    }

    /* loaded from: classes3.dex */
    private class PremiumBroadcastReceiver extends BroadcastReceiver {
        private PremiumBroadcastReceiver() {
        }

        /* synthetic */ PremiumBroadcastReceiver(BillingHelper billingHelper, AnonymousClass1 anonymousClass1) {
            this();
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            BillingHelper.this.listener.onReceiveBroadCast();
        }
    }
}
