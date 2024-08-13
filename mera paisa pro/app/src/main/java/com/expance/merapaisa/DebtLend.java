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
import com.expance.merapaisa.Adapter.DebtLendAdapter;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.Entity.DebtTransEntity;
import com.expance.merapaisa.Database.ViewModel.DebtLendViewModel;
import com.expance.merapaisa.Model.Debt;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;

/* loaded from: classes3.dex */
public class DebtLend extends Fragment implements DebtLendAdapter.OnItemClickListener {
    Activity activity;
    DebtLendAdapter adapter;
    DebtLendViewModel debtViewModel;
    ConstraintLayout emptyWrapper;
    RecyclerView recyclerView;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_debt_lend, container, false);
        this.recyclerView = (RecyclerView) viewGroup.findViewById(R.id.recyclerView);
        this.emptyWrapper = (ConstraintLayout) viewGroup.findViewById(R.id.emptyWrapper);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DebtLendAdapter debtLendAdapter = new DebtLendAdapter(getActivity());
        this.adapter = debtLendAdapter;
        this.recyclerView.setAdapter(debtLendAdapter);
        this.adapter.setListener(this);
        DebtLendViewModel debtLendViewModel = (DebtLendViewModel) new ViewModelProvider(this).get(DebtLendViewModel.class);
        this.debtViewModel = debtLendViewModel;
        debtLendViewModel.getDebtList().observe(getViewLifecycleOwner(), new AnonymousClass1());
        return viewGroup;
    }

    /* renamed from: com.ktwapps.walletmanager.DebtLend$1  reason: invalid class name */
    /* loaded from: classes3.dex */
    class AnonymousClass1 implements Observer<List<Debt>> {
        AnonymousClass1() {
        }

        @Override // androidx.lifecycle.Observer
        public void onChanged(final List<Debt> debts) {
            Activity activity;
            int i;
            final ArrayList arrayList = new ArrayList();
            final ArrayList arrayList2 = new ArrayList();
            final ArrayList<Debt> arrayList3 = new ArrayList<>();
            if (debts != null) {
                Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.DebtLend.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        long j;
                        int accountId = SharePreferenceHelper.getAccountId(DebtLend.this.activity);
                        AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(DebtLend.this.activity);
                        String currency = appDatabaseObject.accountDaoObject().getEntityById(accountId).getCurrency();
                        Iterator it = debts.iterator();
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
                        DebtLend.this.activity.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.DebtLend.1.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Activity activity2;
                                int i2;
                                arrayList.addAll(arrayList2);
                                DebtLend.this.adapter.setList(arrayList);
                                DebtLend.this.adapter.notifyDataSetChanged();
                                RecyclerView recyclerView = DebtLend.this.recyclerView;
                                if (debts.size() > 0) {
                                    activity2 = DebtLend.this.activity;
                                    i2 = R.attr.primaryBackground;
                                } else {
                                    activity2 = DebtLend.this.activity;
                                    i2 = R.attr.emptyBackground;
                                }
                                recyclerView.setBackgroundColor(Helper.getAttributeColor(activity2, i2));
                                DebtLend.this.emptyWrapper.setVisibility(debts.size() > 0 ? 8 : 0);
                            }
                        });
                    }
                });
            }
            arrayList.addAll(arrayList2);
            DebtLend.this.adapter.setList(arrayList);
            DebtLend.this.adapter.notifyDataSetChanged();
            RecyclerView recyclerView = DebtLend.this.recyclerView;
            if (debts == null || debts.size() <= 0) {
                activity = DebtLend.this.activity;
                i = R.attr.emptyBackground;
            } else {
                activity = DebtLend.this.activity;
                i = R.attr.primaryBackground;
            }
            recyclerView.setBackgroundColor(Helper.getAttributeColor(activity, i));
            DebtLend.this.emptyWrapper.setVisibility((debts == null || debts.size() <= 0) ? 0 : 8);
        }
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

    @Override // com.ktwapps.walletmanager.Adapter.DebtLendAdapter.OnItemClickListener
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getActivity(), DebtDetails.class);
        intent.putExtra("debtId", ((Debt) this.adapter.getList().get(position)).getId());
        startActivity(intent);
        this.activity.overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
    }
}
