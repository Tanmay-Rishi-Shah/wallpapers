package com.expance.merapaisa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.github.mikephil.charting.utils.Utils;
import com.expance.merapaisa.Adapter.StatisticAdapter;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Model.CalendarSummary;
import com.expance.merapaisa.Model.Recurring;
import com.expance.merapaisa.Model.RecurringCalendarDate;
import com.expance.merapaisa.Model.Stats;
import com.expance.merapaisa.Utility.CalendarHelper;
import com.expance.merapaisa.Utility.RecurringHelper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import com.expance.merapaisa.Utility.StatisticHelper;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;

/* loaded from: classes3.dex */
public class Statistic extends Fragment implements StatisticAdapter.OnItemClickListener {
    Activity activity;
    StatisticAdapter adapter;
    RecyclerView recyclerView;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_statistic, container, false);
        setUpLayout(viewGroup);
        return viewGroup;
    }

    public void setUpLayout(ViewGroup view) {
        this.recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        this.adapter = new StatisticAdapter(this.activity);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.activity));
        this.recyclerView.setAdapter(this.adapter);
        this.adapter.setListener(this);
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        long pieStartDate;
        long pieEndDate;
        super.onResume();
        Activity activity = this.activity;
        if (activity != null) {
            MainActivity mainActivity = (MainActivity) activity;
            if (mainActivity.dateMode == 6) {
                pieStartDate = CalendarHelper.getCustomStartDate(mainActivity.startDate);
                pieEndDate = CalendarHelper.getCustomEndDate(mainActivity.endDate);
            } else {
                pieStartDate = StatisticHelper.getPieStartDate(mainActivity, mainActivity.date, mainActivity.dateMode);
                pieEndDate = StatisticHelper.getPieEndDate(mainActivity, mainActivity.date, mainActivity.dateMode);
            }
            populateData(pieStartDate, pieEndDate);
        }
    }

    public void populateData(final long startDate, final long endDate) {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.Statistic.1
            @Override // java.lang.Runnable
            public void run() {
                Iterator<Recurring> it;
                long j;
                long j2;
                boolean z;
                int accountId = SharePreferenceHelper.getAccountId(Statistic.this.activity);
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(Statistic.this.activity);
                int i = 1;
                if (startDate == 0 && endDate == 0) {
                    long accountBalance = appDatabaseObject.statisticDaoObject().getAccountBalance(accountId, 0L);
                    long accountBalance2 = appDatabaseObject.statisticDaoObject().getAccountBalance(accountId, 253399507200000L);
                    CalendarSummary summary = appDatabaseObject.statisticDaoObject().getSummary(accountId);
                    List<Stats> allPieStats = appDatabaseObject.statisticDaoObject().getAllPieStats(accountId, 1);
                    Statistic.this.adapter.setBalance(accountBalance, accountBalance2);
                    Statistic.this.adapter.setOverviewSummary(summary);
                    Statistic.this.adapter.setPieStatsList(allPieStats);
                } else {
                    long accountBalance3 = appDatabaseObject.statisticDaoObject().getAccountBalance(accountId, startDate);
                    long accountBalance4 = appDatabaseObject.statisticDaoObject().getAccountBalance(accountId, endDate);
                    CalendarSummary summary2 = appDatabaseObject.statisticDaoObject().getSummary(startDate, endDate, accountId);
                    List<Stats> pieStats = appDatabaseObject.statisticDaoObject().getPieStats(accountId, startDate, endDate, 1);
                    Iterator<Recurring> it2 = appDatabaseObject.recurringDaoObject().getAllRecurringListByAccountId(SharePreferenceHelper.getAccountId(Statistic.this.activity)).iterator();
                    long j3 = 0;
                    long j4 = 0;
                    long j5 = 0;
                    long j6 = 0;
                    while (it2.hasNext()) {
                        Recurring next = it2.next();
                        if (next.getIsFuture() == i) {
                            float currencyRate = appDatabaseObject.currencyDaoObject().getCurrencyRate(SharePreferenceHelper.getAccountId(Statistic.this.activity), next.getCurrency());
                            j = accountBalance3;
                            List<RecurringCalendarDate> statisticRecurring = RecurringHelper.getStatisticRecurring(next, currencyRate, startDate, endDate);
                            long statisticRecurringCarryOver = j3 + RecurringHelper.getStatisticRecurringCarryOver(next, currencyRate, startDate);
                            j4 += RecurringHelper.getStatisticRecurringCarryOver(next, currencyRate, endDate);
                            for (RecurringCalendarDate recurringCalendarDate : statisticRecurring) {
                                j5 += recurringCalendarDate.getAmount() >= 0 ? 0L : recurringCalendarDate.getAmount();
                                j6 += recurringCalendarDate.getAmount() >= 0 ? recurringCalendarDate.getAmount() : 0L;
                            }
                            if (next.getType() != 1 || statisticRecurring.size() <= 0) {
                                it = it2;
                                j2 = statisticRecurringCarryOver;
                            } else {
                                it = it2;
                                j2 = statisticRecurringCarryOver;
                                long amount = statisticRecurring.get(0).getAmount() * statisticRecurring.size();
                                Iterator<Stats> it3 = pieStats.iterator();
                                while (true) {
                                    if (!it3.hasNext()) {
                                        z = false;
                                        break;
                                    }
                                    Stats next2 = it3.next();
                                    if (next2.getId() == next.getCategoryId()) {
                                        next2.setAmount(next2.getAmount() + amount);
                                        next2.setTrans(next2.getTrans() + statisticRecurring.size());
                                        z = true;
                                        break;
                                    }
                                }
                                if (!z) {
                                    pieStats.add(new Stats(next.getCategory(Statistic.this.activity), next.getColor(), next.getIcon(), amount, Utils.DOUBLE_EPSILON, next.getCategoryId(), statisticRecurring.size(), next.getCategoryDefault()));
                                }
                            }
                            j3 = j2;
                        } else {
                            it = it2;
                            j = accountBalance3;
                        }
                        it2 = it;
                        accountBalance3 = j;
                        i = 1;
                    }
                    long j7 = accountBalance3;
                    long j8 = 0;
                    for (Stats stats : pieStats) {
                        j8 += stats.getAmount();
                    }
                    for (Stats stats2 : pieStats) {
                        if (j8 >= 0) {
                            stats2.setPercent((((float) stats2.getAmount()) / ((float) j8)) * 100.0f);
                        } else {
                            stats2.setPercent((((float) (-stats2.getAmount())) / ((float) (-j8))) * 100.0f);
                        }
                    }
                    Collections.sort(pieStats, new Comparator<Stats>() { // from class: com.ktwapps.walletmanager.Statistic.1.1
                        @Override // java.util.Comparator
                        public int compare(Stats t1, Stats t2) {
                            return (int) (t2.getPercent() - t1.getPercent());
                        }
                    });
                    summary2.setExpense(summary2.getExpense() + j5);
                    summary2.setIncome(summary2.getIncome() + j6);
                    Statistic.this.adapter.setBalance(j7 + j3, accountBalance4 + j4);
                    Statistic.this.adapter.setOverviewSummary(summary2);
                    Statistic.this.adapter.setPieStatsList(pieStats);
                }
                Statistic.this.activity.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.Statistic.1.2
                    @Override // java.lang.Runnable
                    public void run() {
                        Statistic.this.adapter.notifyDataSetChanged();
                    }
                });
            }
        });
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

    @Override // com.ktwapps.walletmanager.Adapter.StatisticAdapter.OnItemClickListener
    public void OnItemClick(View v, int position) {
        Activity activity = this.activity;
        if (activity != null) {
            MainActivity mainActivity = (MainActivity) activity;
            int i = mainActivity.dateMode;
            Date date = mainActivity.date;
            Date date2 = mainActivity.startDate;
            Date date3 = mainActivity.endDate;
            if (position == 2) {
                Intent intent = new Intent(getActivity(), TransactionOverview.class);
                intent.putExtra("date", date.getTime());
                intent.putExtra("startDate", date2.getTime());
                intent.putExtra("endDate", date3.getTime());
                intent.putExtra("mode", i);
                startActivity(intent);
                mainActivity.overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
            } else if (position == 4) {
                Intent intent2 = new Intent(getActivity(), StatisticPie.class);
                intent2.putExtra("date", date.getTime());
                intent2.putExtra("startDate", date2.getTime());
                intent2.putExtra("endDate", date3.getTime());
                intent2.putExtra("mode", i);
                startActivity(intent2);
                mainActivity.overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
            }
        }
    }
}
