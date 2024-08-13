package com.expance.merapaisa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.expance.merapaisa.Adapter.DebtBorrowAdapter;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.Entity.DebtTransEntity;
import com.expance.merapaisa.Database.ViewModel.DebtBorrowViewModel;
import com.expance.merapaisa.Model.Debt;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;

/* loaded from: classes3.dex */
public class DebtBorrow extends Fragment implements DebtBorrowAdapter.OnItemClickListener {
    Activity activity;
    DebtBorrowAdapter adapter;
    DebtBorrowViewModel debtViewModel;
    ConstraintLayout emptyWrapper;
    RecyclerView recyclerView;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_debt_borrow, container, false);
        this.recyclerView = (RecyclerView) viewGroup.findViewById(R.id.recyclerView);
        this.emptyWrapper = (ConstraintLayout) viewGroup.findViewById(R.id.emptyWrapper);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DebtBorrowAdapter debtBorrowAdapter = new DebtBorrowAdapter(getActivity());
        this.adapter = debtBorrowAdapter;
        this.recyclerView.setAdapter(debtBorrowAdapter);
        this.adapter.setListener(this);
        DebtBorrowViewModel debtBorrowViewModel = (DebtBorrowViewModel) new ViewModelProvider(this).get(DebtBorrowViewModel.class);
        this.debtViewModel = debtBorrowViewModel;
        debtBorrowViewModel.getDebtList().observe(getViewLifecycleOwner(), new Observer() { // from class: com.ktwapps.walletmanager.DebtBorrow$$ExternalSyntheticLambda0
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                DebtBorrow.this.m205lambda$onCreateView$0$comktwappswalletmanagerDebtBorrow((List) obj);
            }
        });
        return viewGroup;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreateView$0$com-ktwapps-walletmanager-DebtBorrow  reason: not valid java name */
    public /* synthetic */ void m205lambda$onCreateView$0$comktwappswalletmanagerDebtBorrow(final List list) {
        Activity activity;
        int i;
        final ArrayList arrayList = new ArrayList();
        final ArrayList arrayList2 = new ArrayList();
        final ArrayList<Debt> arrayList3 = new ArrayList();
        if (list != null) {
            Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.DebtBorrow.1
                @Override // java.lang.Runnable
                public void run() {
                    long j;
                    int accountId = SharePreferenceHelper.getAccountId(DebtBorrow.this.activity);
                    AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(DebtBorrow.this.activity);
                    String currency = appDatabaseObject.accountDaoObject().getEntityById(accountId).getCurrency();
                    Iterator it = list.iterator();
                    while (true) {
                        j = 0;
                        if (!it.hasNext()) {
                            break;
                        }
                        Debt debt = (Debt) it.next();
                        com.expance.merapaisa.Model.Currency debtCurrency = appDatabaseObject.debtDaoObject().getDebtCurrency(accountId, debt.getId());
                        if (debtCurrency == null) {
                            debt.setCurrencyCode(currency);
                            debt.setRate(1.0d);
                        } else {
                            debt.setCurrencyCode(debtCurrency.getCurrency());
                            debt.setRate(debtCurrency.getRate());
                        }
                        long amount = debt.getAmount();
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
                        arrayList3.add(debt);
                    }
                    long j2 = 0;
                    for (Debt debt2 : arrayList3) {
                        if (debt2.getStatus() == 0) {
                            j2 = (long) (j2 + ((debt2.getAmount() - debt2.getPay()) * debt2.getRate()));
                            if (arrayList.size() == 0) {
                                arrayList.add(Long.valueOf(j2));
                            } else {
                                arrayList.set(0, Long.valueOf(j2));
                            }
                            arrayList.add(debt2);
                        } else {
                            long amount2 = (long) (j + (debt2.getAmount() * debt2.getRate()));
                            if (arrayList2.size() == 0) {
                                arrayList2.add(Long.valueOf(amount2));
                            } else {
                                arrayList2.set(0, Long.valueOf(amount2));
                            }
                            arrayList2.add(debt2);
                            j = amount2;
                        }
                    }
                    DebtBorrow.this.activity.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.DebtBorrow.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Activity activity2;
                            int i2;
                            arrayList.addAll(arrayList2);
                            DebtBorrow.this.adapter.setList(arrayList);
                            DebtBorrow.this.adapter.notifyDataSetChanged();
                            RecyclerView recyclerView = DebtBorrow.this.recyclerView;
                            if (list.size() > 0) {
                                activity2 = DebtBorrow.this.activity;
                                i2 = R.attr.primaryBackground;
                            } else {
                                activity2 = DebtBorrow.this.activity;
                                i2 = R.attr.emptyBackground;
                            }
                            recyclerView.setBackgroundColor(Helper.getAttributeColor(activity2, i2));
                            DebtBorrow.this.emptyWrapper.setVisibility(list.size() > 0 ? 8 : 0);
                        }
                    });
                }
            });
        }
        arrayList.addAll(arrayList2);
        this.adapter.setList(arrayList);
        this.adapter.notifyDataSetChanged();
        RecyclerView recyclerView = this.recyclerView;
        if (list == null || list.size() <= 0) {
            activity = this.activity;
            i = R.attr.emptyBackground;
        } else {
            activity = this.activity;
            i = R.attr.primaryBackground;
        }
        recyclerView.setBackgroundColor(Helper.getAttributeColor(activity, i));
        this.emptyWrapper.setVisibility((list == null || list.size() <= 0) ? 0 : 8);
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

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        this.activity = null;
    }

    @Override // com.ktwapps.walletmanager.Adapter.DebtBorrowAdapter.OnItemClickListener
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getActivity(), DebtDetails.class);
        intent.putExtra("debtId", ((Debt) this.adapter.getList().get(position)).getId());
        startActivity(intent);
        this.activity.overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
    }
}
