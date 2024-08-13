package com.expance.merapaisa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.expance.merapaisa.Adapter.TransactionAdapter;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.ViewModel.TransactionViewModel;
import com.expance.merapaisa.Model.DailyTrans;
import com.expance.merapaisa.Model.Trans;
import com.expance.merapaisa.Model.TransList;
import com.expance.merapaisa.Utility.DateHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import com.expance.merapaisa.Utility.TransactionHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import org.apache.http.cookie.ClientCookie;

/* loaded from: classes3.dex */
public class Transaction extends Fragment implements TransactionAdapter.OnItemClickListener {
    Activity activity;
    TransactionAdapter adapter;
    ConstraintLayout emptyWrapper;
    RecyclerView recyclerView;
    private TransactionViewModel transactionViewModel;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_transaction, container, false);
        setUpLayout(viewGroup);
        ImageView gifView;
        gifView = viewGroup.findViewById(R.id.emptyImage);
        Glide.with(this).asGif().load(R.drawable.gif_nodata).into(gifView);
        return viewGroup;
    }

    private void setUpLayout(ViewGroup view) {
        this.recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        this.emptyWrapper = (ConstraintLayout) view.findViewById(R.id.emptyWrapper);
        TransactionAdapter transactionAdapter = new TransactionAdapter(this.activity);
        this.adapter = transactionAdapter;
        this.recyclerView.setAdapter(transactionAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.activity));
        this.adapter.setListener(this);
        populateData();
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

    private void populateData() {
        final int accountId = SharePreferenceHelper.getAccountId(this.activity);
        TransactionViewModel transactionViewModel = (TransactionViewModel) new ViewModelProvider(this).get(TransactionViewModel.class);
        this.transactionViewModel = transactionViewModel;
        transactionViewModel.getTrans().observe(getViewLifecycleOwner(), new Observer<List<DailyTrans>>() { // from class: com.ktwapps.walletmanager.Transaction.1

            /* JADX INFO: Access modifiers changed from: package-private */
            /* renamed from: com.ktwapps.walletmanager.Transaction$1$1  reason: invalid class name and collision with other inner class name */
            /* loaded from: classes3.dex */
            class RunnableC00771 implements Runnable {
                final /* synthetic */ ArrayList<DailyTrans> val$dailyTrans;

                RunnableC00771(final ArrayList<DailyTrans> val$dailyTrans) {
                    this.val$dailyTrans = val$dailyTrans;
                }

                @Override // java.lang.Runnable
                public void run() {
                    final ArrayList arrayList = new ArrayList();
                    List list = this.val$dailyTrans;
                    if (list != null && list.size() > 0) {
                        AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(Transaction.this.activity);
                        for (DailyTrans dailyTrans : this.val$dailyTrans) {
                            arrayList.add(new TransList(true, null, dailyTrans));
                            Date dateTime = dailyTrans.getDateTime();
                            for (Trans trans : appDatabaseObject.transDaoObject().getTransFromDate(accountId, DateHelper.getTransactionStartDate(dateTime), DateHelper.getTransactionEndDate(dateTime))) {
                                arrayList.add(new TransList(false, trans, null));
                            }
                        }
                    }
                    if (Transaction.this.activity != null) {
                        Transaction.this.activity.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.Transaction$1$1$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                RunnableC00771.this.m230lambda$run$0$comktwappswalletmanagerTransaction$1$1(arrayList);
                            }
                        });
                    }
                }

                /* JADX INFO: Access modifiers changed from: package-private */
                /* renamed from: lambda$run$0$com-ktwapps-walletmanager-Transaction$1$1  reason: not valid java name */
                public /* synthetic */ void m230lambda$run$0$comktwappswalletmanagerTransaction$1$1(List list) {
                    Activity activity;
                    int i;
                    Transaction.this.adapter.setList(list);
                    Transaction.this.emptyWrapper.setVisibility(Transaction.this.adapter.list.size() == 0 ? 0 : 8);
                    RecyclerView recyclerView = Transaction.this.recyclerView;
                    if (Transaction.this.adapter.list.size() == 0) {
                        activity = Transaction.this.activity;
                        i = R.attr.emptyBackground;
                    } else {
                        activity = Transaction.this.activity;
                        i = R.attr.primaryBackground;
                    }
                    recyclerView.setBackgroundColor(Helper.getAttributeColor(activity, i));
                }
            }

            @Override // androidx.lifecycle.Observer
            public void onChanged(final List<DailyTrans> dailyTrans) {
                Executors.newSingleThreadExecutor().execute(new RunnableC00771((ArrayList<DailyTrans>) dailyTrans));
            }
        });
    }

    @Override // com.ktwapps.walletmanager.Adapter.TransactionAdapter.OnItemClickListener
    public void OnItemClick(View view, int position) {
        Trans trans = this.adapter.list.get(position).getTrans();
        if (view.getId() == R.id.image1 || view.getId() == R.id.image2 || view.getId() == R.id.image3) {
            Intent intent = new Intent(this.activity, PhotoViewer.class);
            intent.putExtra(ClientCookie.PATH_ATTR, trans.getMedia());
            intent.putExtra(FirebaseAnalytics.Param.INDEX, ((Integer) view.getTag()).intValue());
            startActivity(intent);
        } else if (trans.getDebtId() != 0 && trans.getDebtTransId() == 0) {
            Intent intent2 = new Intent(getActivity(), DebtDetails.class);
            intent2.putExtra("debtId", trans.getDebtId());
            startActivity(intent2);
            this.activity.overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        } else {
            Intent intent3 = new Intent(this.activity, Details.class);
            intent3.putExtra("transId", trans.getId());
            startActivity(intent3);
            this.activity.overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        }
    }

    @Override // com.ktwapps.walletmanager.Adapter.TransactionAdapter.OnItemClickListener
    public void OnItemLongClick(View view, final int position) {
        TransactionHelper.showPopupMenu(this.activity, this.adapter.list.get(position).getTrans(), view);
    }
}
