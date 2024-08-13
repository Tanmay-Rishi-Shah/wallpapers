package com.expance.merapaisa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.expance.merapaisa.Adapter.WalletsAdapter;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.Entity.DebtTransEntity;
import com.expance.merapaisa.Database.ViewModel.BudgetViewModel;
import com.expance.merapaisa.Database.ViewModel.DebtViewModel;
import com.expance.merapaisa.Database.ViewModel.GoalViewModel;
import com.expance.merapaisa.Database.ViewModel.RecurringViewModel;
import com.expance.merapaisa.Database.ViewModel.WalletViewModel;
import com.expance.merapaisa.Model.Budget;
import com.expance.merapaisa.Model.Debt;
import com.expance.merapaisa.Model.Goal;
import com.expance.merapaisa.Model.Recurring;
import com.expance.merapaisa.Model.Wallets;
import com.expance.merapaisa.Utility.BillingHelper;
import com.expance.merapaisa.Utility.BudgetHelper;
import com.expance.merapaisa.Utility.Constant;
import com.expance.merapaisa.Utility.SharePreferenceHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

import vocsy.ads.CustomAdsListener;
import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class Wallet extends Fragment implements WalletsAdapter.OnItemClickListener, BillingHelper.BillingListener {
    Activity activity;
    WalletsAdapter adapter;
    BillingHelper billingHelper;
    BudgetViewModel budgetViewModel;
    DebtViewModel debtViewModel;
    GoalViewModel goalViewModel;
    RecurringViewModel recurringViewModel;
    RecyclerView recyclerView;
    WalletViewModel walletViewModel;

    @Override // com.ktwapps.walletmanager.Utility.BillingHelper.BillingListener
    public void onSkuReady() {
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) ((LayoutInflater) Objects.requireNonNull(inflater)).inflate(R.layout.activity_wallet, container, false);
        setUpLayout(viewGroup);
        return viewGroup;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int findSpanCount() {
        if (getResources().getBoolean(R.bool.isTablet)) {
            return getResources().getConfiguration().orientation == 1 ? 3 : 4;
        }
        return 2;
    }

    public void setUpLayout(ViewGroup viewGroup) {
        this.recyclerView = (RecyclerView) viewGroup.findViewById(R.id.recyclerView);
        this.adapter = new WalletsAdapter(getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), findSpanCount());
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: com.ktwapps.walletmanager.Wallet.1
            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
            public int getSpanSize(int position) {
                if (Wallet.this.adapter.getList().get(position) instanceof Wallets) {
                    return 1;
                }
                if ((Wallet.this.adapter.getList().get(position) instanceof Integer) && ((Integer) Wallet.this.adapter.getList().get(position)).intValue() == 0) {
                    return 1;
                }
                return Wallet.this.findSpanCount();
            }
        });
        this.recyclerView.setLayoutManager(gridLayoutManager);
        this.recyclerView.setAdapter(this.adapter);
        this.adapter.setListener(this);
        BillingHelper billingHelper = new BillingHelper(this.activity);
        this.billingHelper = billingHelper;
        billingHelper.setListener(this);
        this.billingHelper.setUpBillingClient();
        populateData();
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        this.adapter.notifyDataSetChanged();
        this.billingHelper.queryPurchases();
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(14, 0);
        calendar.set(13, 0);
        calendar.set(12, 0);
        calendar.set(11, 0);
        calendar.add(5, 1);
        if (!SharePreferenceHelper.isFutureBalanceOn(this.activity)) {
            calendar.set(1, 10000);
        }
        this.walletViewModel.setDate(calendar.getTimeInMillis());
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        this.activity = null;
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    private void populateData() {
        BudgetViewModel budgetViewModel = (BudgetViewModel) new ViewModelProvider(requireActivity()).get(BudgetViewModel.class);
        this.budgetViewModel = budgetViewModel;
        budgetViewModel.getBudgetList().observe(getActivity(), new Observer() { // from class: com.ktwapps.walletmanager.Wallet$$ExternalSyntheticLambda8
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                Wallet.this.m254lambda$populateData$2$comktwappswalletmanagerWallet((List) obj);
            }
        });
        DebtViewModel debtViewModel = (DebtViewModel) new ViewModelProvider(getActivity()).get(DebtViewModel.class);
        this.debtViewModel = debtViewModel;
        debtViewModel.getDebtList().observe(getActivity(), new Observer() { // from class: com.ktwapps.walletmanager.Wallet$$ExternalSyntheticLambda9
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                Wallet.this.m257lambda$populateData$5$comktwappswalletmanagerWallet((List) obj);
            }
        });
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(14, 0);
        calendar.set(13, 0);
        calendar.set(12, 0);
        calendar.set(11, 0);
        calendar.add(5, 1);
        if (!SharePreferenceHelper.isFutureBalanceOn(this.activity)) {
            calendar.set(1, 10000);
        }
        WalletViewModel walletViewModel = (WalletViewModel) new ViewModelProvider(getActivity()).get(WalletViewModel.class);
        this.walletViewModel = walletViewModel;
        walletViewModel.setDate(calendar.getTimeInMillis());
        this.walletViewModel.getWalletsList().observe(getActivity(), new Observer() { // from class: com.ktwapps.walletmanager.Wallet$$ExternalSyntheticLambda10
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                Wallet.this.m259lambda$populateData$7$comktwappswalletmanagerWallet((List) obj);
            }
        });
        GoalViewModel goalViewModel = (GoalViewModel) new ViewModelProvider(getActivity()).get(GoalViewModel.class);
        this.goalViewModel = goalViewModel;
        goalViewModel.getGoalList().observe(getActivity(), new Observer() { // from class: com.ktwapps.walletmanager.Wallet$$ExternalSyntheticLambda11
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                Wallet.this.m261lambda$populateData$9$comktwappswalletmanagerWallet((List) obj);
            }
        });
        RecurringViewModel recurringViewModel = (RecurringViewModel) new ViewModelProvider(getActivity()).get(RecurringViewModel.class);
        this.recurringViewModel = recurringViewModel;
        recurringViewModel.getRecurringList().observe(getActivity(), new Observer() { // from class: com.ktwapps.walletmanager.Wallet$$ExternalSyntheticLambda1
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                Wallet.this.m253lambda$populateData$11$comktwappswalletmanagerWallet((List) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$populateData$2$com-ktwapps-walletmanager-Wallet  reason: not valid java name */
    public /* synthetic */ void m254lambda$populateData$2$comktwappswalletmanagerWallet(final List list) {
        final ArrayList arrayList = new ArrayList();
        if (list != null) {
            Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.Wallet$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    Wallet.this.m251lambda$populateData$1$comktwappswalletmanagerWallet(list, arrayList);
                }
            });
        } else {
            this.adapter.setBudgetList(arrayList);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$populateData$1$com-ktwapps-walletmanager-Wallet  reason: not valid java name */
    public /* synthetic */ void m251lambda$populateData$1$comktwappswalletmanagerWallet(List list, final List list2) {
        AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(this.activity);
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Budget budget = (Budget) it.next();
            Date time = java.util.Calendar.getInstance().getTime();
            int budgetType = BudgetHelper.getBudgetType(budget.getPeriod());
            long budgetStartDate = BudgetHelper.getBudgetStartDate(this.activity, time, budgetType, budget.getStartDate());
            long budgetEndDate = BudgetHelper.getBudgetEndDate(this.activity, time, budgetType, budget.getStartDate());
            if (budget.getCategoryId() == 0) {
                budget.setSpent(-appDatabaseObject.budgetDaoObject().getBudgetSpent(budgetStartDate, budgetEndDate, 1, SharePreferenceHelper.getAccountId(this.activity)));
            } else {
                budget.setSpent(-appDatabaseObject.budgetDaoObject().getBudgetSpent(appDatabaseObject.budgetDaoObject().getBudgetCategoryIds(budget.getId()), budgetStartDate, budgetEndDate, 1, SharePreferenceHelper.getAccountId(this.activity)));
            }
            budget.setTransCount(appDatabaseObject.budgetDaoObject().getBudgetTransCount(SharePreferenceHelper.getAccountId(this.activity), budget.getId(), budgetStartDate, budgetEndDate));
            list2.add(budget);
        }
        this.activity.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.Wallet$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                Wallet.this.m250lambda$populateData$0$comktwappswalletmanagerWallet(list2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$populateData$0$com-ktwapps-walletmanager-Wallet  reason: not valid java name */
    public /* synthetic */ void m250lambda$populateData$0$comktwappswalletmanagerWallet(List list) {
        this.adapter.setBudgetList(list);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$populateData$5$com-ktwapps-walletmanager-Wallet  reason: not valid java name */
    public /* synthetic */ void m257lambda$populateData$5$comktwappswalletmanagerWallet(final List list) {
        final ArrayList arrayList = new ArrayList();
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.Wallet$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                Wallet.this.m256lambda$populateData$4$comktwappswalletmanagerWallet(list, arrayList);
            }
        });
        this.adapter.setDebtList(arrayList);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$populateData$4$com-ktwapps-walletmanager-Wallet  reason: not valid java name */
    public /* synthetic */ void m256lambda$populateData$4$comktwappswalletmanagerWallet(List list, final List list2) {
        int i;
        AppDatabaseObject appDatabaseObject;
        int accountId = SharePreferenceHelper.getAccountId(this.activity);
        AppDatabaseObject appDatabaseObject2 = AppDatabaseObject.getInstance(this.activity);
        if (list != null) {
            String currency = appDatabaseObject2.accountDaoObject().getEntityById(accountId).getCurrency();
            Iterator it = list.iterator();
            while (it.hasNext()) {
                Debt debt = (Debt) it.next();
                com.expance.merapaisa.Model.Currency debtCurrency = appDatabaseObject2.debtDaoObject().getDebtCurrency(accountId, debt.getId());
                if (debtCurrency == null) {
                    debt.setCurrencyCode(currency);
                    debt.setRate(1.0d);
                } else {
                    debt.setCurrencyCode(debtCurrency.getCurrency());
                    debt.setRate(debtCurrency.getRate());
                }
                long amount = debt.getAmount();
                long j = 0;
                for (DebtTransEntity debtTransEntity : appDatabaseObject2.debtDaoObject().getAllDebtTrans(debt.getId())) {
                    com.expance.merapaisa.Model.Currency debtTransCurrency = appDatabaseObject2.debtDaoObject().getDebtTransCurrency(accountId, debt.getId(), debtTransEntity.getId());
                    if (debtTransCurrency == null) {
                        if (debtTransEntity.getType() == 0) {
                            j += debtTransEntity.getAmount();
                        } else {
                            amount += debtTransEntity.getAmount();
                        }
                        i = accountId;
                        appDatabaseObject = appDatabaseObject2;
                    } else {
                        double rate = debtTransCurrency.getRate() / debt.getRate();
                        if (debtTransEntity.getType() == 0) {
                            i = accountId;
                            appDatabaseObject = appDatabaseObject2;
                            j = (long) (j + (debtTransEntity.getAmount() * rate));
                        } else {
                            i = accountId;
                            appDatabaseObject = appDatabaseObject2;
                            amount = (long) (amount + (debtTransEntity.getAmount() * rate));
                        }
                    }
                    accountId = i;
                    appDatabaseObject2 = appDatabaseObject;
                }
                debt.setAmount(amount);
                debt.setPay(j);
                list2.add(debt);
            }
        }
        this.activity.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.Wallet$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                Wallet.this.m255lambda$populateData$3$comktwappswalletmanagerWallet(list2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$populateData$3$com-ktwapps-walletmanager-Wallet  reason: not valid java name */
    public /* synthetic */ void m255lambda$populateData$3$comktwappswalletmanagerWallet(List list) {
        this.adapter.setDebtList(list);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$populateData$7$com-ktwapps-walletmanager-Wallet  reason: not valid java name */
    public /* synthetic */ void m259lambda$populateData$7$comktwappswalletmanagerWallet(final List list) {
        new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.ktwapps.walletmanager.Wallet$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                Wallet.this.m258lambda$populateData$6$comktwappswalletmanagerWallet(list);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$populateData$6$com-ktwapps-walletmanager-Wallet  reason: not valid java name */
    public /* synthetic */ void m258lambda$populateData$6$comktwappswalletmanagerWallet(List list) {
        this.adapter.setWalletList(list);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$populateData$9$com-ktwapps-walletmanager-Wallet  reason: not valid java name */
    public /* synthetic */ void m261lambda$populateData$9$comktwappswalletmanagerWallet(final List list) {
        new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.ktwapps.walletmanager.Wallet$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                Wallet.this.m260lambda$populateData$8$comktwappswalletmanagerWallet(list);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$populateData$8$com-ktwapps-walletmanager-Wallet  reason: not valid java name */
    public /* synthetic */ void m260lambda$populateData$8$comktwappswalletmanagerWallet(List list) {
        this.adapter.setGoalList(list);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$populateData$11$com-ktwapps-walletmanager-Wallet  reason: not valid java name */
    public /* synthetic */ void m253lambda$populateData$11$comktwappswalletmanagerWallet(final List list) {
        new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.ktwapps.walletmanager.Wallet$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                Wallet.this.m252lambda$populateData$10$comktwappswalletmanagerWallet(list);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$populateData$10$com-ktwapps-walletmanager-Wallet  reason: not valid java name */
    public /* synthetic */ void m252lambda$populateData$10$comktwappswalletmanagerWallet(List list) {
        this.adapter.setRecurringList(list);
    }

    @Override // com.ktwapps.walletmanager.Adapter.WalletsAdapter.OnItemClickListener
    public void onItemClick(View view, int position, int type) {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (type != -26) {
            switch (type) {
                case Constant.MANAGE_DEBT /* -24 */:
                    startActivity(new Intent(getActivity(), ManageDebt.class));
                    this.activity.overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
                    return;
                case Constant.MANAGE_GOAL /* -23 */:
                    startActivity(new Intent(getActivity(), ManageGoal.class));
                    this.activity.overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
                    return;
                case Constant.MANAGE_BUDGET /* -22 */:
                    startActivity(new Intent(getActivity(), ManageBudget.class));
                    this.activity.overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
                    return;
                case Constant.MANAGE_WALLET /* -21 */:
                    startActivity(new Intent(getActivity(), ManageWallet.class));
                    this.activity.overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
                    return;
                default:
                    switch (type) {
                        case 1:
                            GoogleAds.getInstance().showCounterInterstitialAd(requireActivity(), new CustomAdsListener() {
                                @Override
                                public void onFinish() {

                                    Intent intent = new Intent(getActivity(), CreateWallet.class);
                                    intent.putExtra(JamXmlElements.TYPE, 1);
                                    intent.putExtra("currencySymbol", SharePreferenceHelper.getAccountSymbol(requireActivity()));
                                    startActivity(intent);
                                    activity.overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);

                                }
                            });

                            return;
                        case 2:

                            GoogleAds.getInstance().showCounterInterstitialAd(requireActivity(), new CustomAdsListener() {
                                @Override
                                public void onFinish() {
                                    Intent intent2 = new Intent(getActivity(), CreateBudget.class);
                                    intent2.putExtra(JamXmlElements.TYPE, 2);
                                    startActivity(intent2);
                                    activity.overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
                                }
                            });

                            return;
                        case 3:
                            GoogleAds.getInstance().showCounterInterstitialAd(requireActivity(), new CustomAdsListener() {
                                @Override
                                public void onFinish() {

                                    Intent intent3 = new Intent(getActivity(), CreateGoal.class);
                                    intent3.putExtra(JamXmlElements.TYPE, 3);
                                    startActivity(intent3);
                                    activity.overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);

                                }
                            });

                            return;
                        case 4:
                            GoogleAds.getInstance().showCounterInterstitialAd(requireActivity(), new CustomAdsListener() {
                                @Override
                                public void onFinish() {
                                    Intent intent4 = new Intent(getActivity(), CreateDebt.class);
                                    intent4.putExtra(JamXmlElements.TYPE, 4);
                                    startActivity(intent4);
                                    activity.overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
                                }
                            });


                            return;
                        case 5:
                            Intent intent5 = new Intent(getActivity(), WalletTransaction.class);
                            intent5.putExtra("walletId", ((Wallets) this.adapter.getList().get(position)).getId());
                            startActivity(intent5);
                            this.activity.overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
                            return;
                        case 6:
                            Intent intent6 = new Intent(getActivity(), BudgetDetail.class);
                            intent6.putExtra("budgetId", ((Budget) this.adapter.getList().get(position)).getId());
                            intent6.putExtra("date", java.util.Calendar.getInstance().getTimeInMillis());
                            startActivity(intent6);
                            this.activity.overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
                            return;
                        case 7:
                            Intent intent7 = new Intent(getActivity(), GoalDetail.class);
                            intent7.putExtra("goalId", ((Goal) this.adapter.getList().get(position)).getId());
                            startActivity(intent7);
                            this.activity.overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
                            return;
                        case 8:
                            Intent intent8 = new Intent(getActivity(), DebtDetails.class);
                            intent8.putExtra("debtId", ((Debt) this.adapter.getList().get(position)).getId());
                            startActivity(intent8);
                            this.activity.overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
                            return;
                        case 9:
                            Intent intent9 = new Intent(getActivity(), RecurringDetail.class);
                            intent9.putExtra("recurringId", ((Recurring) this.adapter.getList().get(position)).getId());
                            startActivity(intent9);
                            this.activity.overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
                            return;
                        case 10:
                            if (this.billingHelper.getBillingStatus() == 2) {
                                Intent intent10 = new Intent(getActivity(), CreateRecurring.class);
                                intent10.putExtra(JamXmlElements.TYPE, 10);
                                startActivity(intent10);
                                this.activity.overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
                                return;
                            }
                            Executors.newSingleThreadExecutor().execute(new AnonymousClass2());
                            return;
                        default:
                            return;
                    }
            }
        }
        startActivity(new Intent(getActivity(), ManageRecurring.class));
        this.activity.overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.ktwapps.walletmanager.Wallet$2  reason: invalid class name */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 implements Runnable {
        AnonymousClass2() {
        }

        @Override // java.lang.Runnable
        public void run() {
            final List<Recurring> allRecurringListByAccountId = AppDatabaseObject.getInstance(Wallet.this.activity).recurringDaoObject().getAllRecurringListByAccountId(SharePreferenceHelper.getAccountId(Wallet.this.activity));
            Wallet.this.activity.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.Wallet$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    Wallet.AnonymousClass2.this.m262lambda$run$0$comktwappswalletmanagerWallet$2(allRecurringListByAccountId);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$run$0$com-ktwapps-walletmanager-Wallet$2  reason: not valid java name */
        public /* synthetic */ void m262lambda$run$0$comktwappswalletmanagerWallet$2(List list) {
            if (list.size() == 0) {
                Intent intent = new Intent(Wallet.this.getActivity(), CreateRecurring.class);
                intent.putExtra(JamXmlElements.TYPE, 10);
                Wallet.this.startActivity(intent);
                Wallet.this.activity.overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
                return;
            }
            Wallet.this.startActivity(new Intent(Wallet.this.activity, Premium.class));
            Wallet.this.activity.overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
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
        Activity activity = this.activity;
        if (activity != null) {
            if (SharePreferenceHelper.getPremium(activity) == 2) {
                this.adapter.setIsAds(false);
            } else {
                this.adapter.setIsAds(true);
            }
        }
    }
}
