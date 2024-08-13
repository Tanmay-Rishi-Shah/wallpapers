package com.expance.merapaisa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.expance.merapaisa.Adapter.CalendarAdapter;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Model.CalendarRecord;
import com.expance.merapaisa.Model.CalendarSummary;
import com.expance.merapaisa.Model.Recurring;
import com.expance.merapaisa.Utility.CalendarHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.RecurringHelper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import com.expance.merapaisa.Widget.BottomTransactionDialog;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;

/* loaded from: classes3.dex */
public class Calendar extends Fragment implements View.OnClickListener, CalendarAdapter.OnItemClickListener {
    MainActivity activity;
    private CalendarAdapter adapter;
    private TextView amountLabel;
    private ImageView backImage;
    private Date date;
    private TextView dateLabel;
    private TextView expenseLabel;
    private TextView incomeLabel;
    private ImageView nextImage;
    private RecyclerView recyclerView;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_calendar, container, false);
        this.date = CalendarHelper.getInitialDate();
        setUpLayout(viewGroup);
        return viewGroup;
    }

    private void setUpLayout(ViewGroup viewGroup) {
        this.backImage = (ImageView) viewGroup.findViewById(R.id.backImage);
        this.nextImage = (ImageView) viewGroup.findViewById(R.id.nextImage);
        this.dateLabel = (TextView) viewGroup.findViewById(R.id.dateLabel);
        this.incomeLabel = (TextView) viewGroup.findViewById(R.id.incomeLabel);
        this.expenseLabel = (TextView) viewGroup.findViewById(R.id.expenseLabel);
        this.amountLabel = (TextView) viewGroup.findViewById(R.id.amountLabel);
        this.recyclerView = (RecyclerView) viewGroup.findViewById(R.id.recyclerView);
        CalendarAdapter calendarAdapter = new CalendarAdapter(this.activity);
        this.adapter = calendarAdapter;
        calendarAdapter.setListener(this);
        this.recyclerView.setLayoutManager(new GridLayoutManager(this.activity, 7));
        this.recyclerView.setAdapter(this.adapter);
        this.backImage.setOnClickListener(this);
        this.nextImage.setOnClickListener(this);
        this.dateLabel.setOnClickListener(this);
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        this.activity = (MainActivity) context;
        super.onAttach(context);
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Activity activity) {
        this.activity = (MainActivity) activity;
        super.onAttach(activity);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        this.activity = null;
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        setUpLayoutContent(this.date);
    }

    private void setUpSummary(String symbol, CalendarSummary summary, long carryOver) {
        boolean isCarryOverOn = SharePreferenceHelper.isCarryOverOn(this.activity);
        boolean z = carryOver >= 0;
        this.incomeLabel.setText(Helper.getBeautifyAmount(symbol, summary.getIncome() + ((isCarryOverOn && z) ? carryOver : 0L)));
        this.expenseLabel.setText(Helper.getBeautifyAmount(symbol, summary.getExpense() + ((!isCarryOverOn || z) ? 0L : carryOver)));
        TextView textView = this.amountLabel;
        long income = summary.getIncome() + summary.getExpense();
        if (!isCarryOverOn) {
            carryOver = 0;
        }
        textView.setText(Helper.getBeautifyAmount(symbol, income + carryOver));
    }

    private void setUpLayoutContent(final Date date) {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.Calendar$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                Calendar.this.m177lambda$setUpLayoutContent$2$comktwappswalletmanagerCalendar(date);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$setUpLayoutContent$2$com-ktwapps-walletmanager-Calendar  reason: not valid java name */
    public /* synthetic */ void m177lambda$setUpLayoutContent$2$comktwappswalletmanagerCalendar(final Date date) {
        boolean z;
        AppDatabaseObject appDatabaseObject;
        Iterator<CalendarRecord> it;
        Iterator<Recurring> it2;
        long j;
        boolean z2;
        AppDatabaseObject appDatabaseObject2 = AppDatabaseObject.getInstance(this.activity);
        long monthlyStartDate = CalendarHelper.getMonthlyStartDate(date);
        long monthlyEndDate = CalendarHelper.getMonthlyEndDate(date);
        int accountId = SharePreferenceHelper.getAccountId(this.activity);
        final String accountSymbol = SharePreferenceHelper.getAccountSymbol(this.activity);
        final List<CalendarRecord> record = appDatabaseObject2.calenderDaoObject().getRecord(monthlyStartDate, monthlyEndDate, accountId);
        final CalendarSummary summary = appDatabaseObject2.calenderDaoObject().getSummary(monthlyStartDate, monthlyEndDate, accountId);
        long accountBalance = appDatabaseObject2.calenderDaoObject().getAccountBalance(accountId, monthlyStartDate);
        Iterator<Recurring> it3 = appDatabaseObject2.recurringDaoObject().getAllRecurringListByAccountId(SharePreferenceHelper.getAccountId(this.activity)).iterator();
        long j2 = 0;
        long j3 = 0;
        long j4 = 0;
        while (true) {
            z = false;
            if (!it3.hasNext()) {
                break;
            }
            Recurring next = it3.next();
            if (next.getIsFuture() == 1) {
                appDatabaseObject = appDatabaseObject2;
                float currencyRate = appDatabaseObject2.currencyDaoObject().getCurrencyRate(SharePreferenceHelper.getAccountId(this.activity), next.getCurrency());
                List<CalendarRecord> calendarRecurring = RecurringHelper.getCalendarRecurring(next, currencyRate, date);
                j4 += RecurringHelper.getCalendarRecurringCarryOver(next, currencyRate, date);
                Iterator<CalendarRecord> it4 = calendarRecurring.iterator();
                while (it4.hasNext()) {
                    CalendarRecord next2 = it4.next();
                    long expense = j2 + next2.getExpense();
                    j3 += next2.getIncome();
                    Iterator<CalendarRecord> it5 = record.iterator();
                    while (true) {
                        if (!it5.hasNext()) {
                            it = it4;
                            it2 = it3;
                            j = expense;
                            z2 = false;
                            break;
                        }
                        CalendarRecord next3 = it5.next();
                        it = it4;
                        it2 = it3;
                        if (next2.getDate() == next3.getDate()) {
                            j = expense;
                            next3.setExpense(next3.getExpense() + next2.getExpense());
                            next3.setIncome(next3.getIncome() + next2.getIncome());
                            z2 = true;
                            break;
                        }
                        it4 = it;
                        it3 = it2;
                    }
                    if (!z2) {
                        record.add(next2);
                    }
                    it4 = it;
                    it3 = it2;
                    j2 = j;
                }
            } else {
                appDatabaseObject = appDatabaseObject2;
            }
            appDatabaseObject2 = appDatabaseObject;
            it3 = it3;
        }
        summary.setExpense(summary.getExpense() + j2);
        summary.setIncome(summary.getIncome() + j3);
        final long j5 = accountBalance + j4;
        if (SharePreferenceHelper.isCarryOverOn(this.activity)) {
            Iterator<CalendarRecord> it6 = record.iterator();
            while (true) {
                if (!it6.hasNext()) {
                    break;
                }
                CalendarRecord next4 = it6.next();
                if (next4.getDate() == 1) {
                    int i = (j5 > 0L ? 1 : (j5 == 0L ? 0 : -1));
                    next4.setExpense(next4.getExpense() + (i > 0 ? 0L : j5));
                    next4.setIncome(next4.getIncome() + (i > 0 ? j5 : 0L));
                    z = true;
                }
            }
            if (!z) {
                int i2 = (j5 > 0L ? 1 : (j5 == 0L ? 0 : -1));
                record.add(new CalendarRecord(1, i2 > 0 ? j5 : 0L, i2 > 0 ? 0L : j5));
            }
        }
        Collections.sort(record, new Comparator() { // from class: com.ktwapps.walletmanager.Calendar$$ExternalSyntheticLambda1
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return Calendar.lambda$setUpLayoutContent$0((CalendarRecord) obj, (CalendarRecord) obj2);
            }
        });
        this.activity.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.Calendar$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                Calendar.this.m176lambda$setUpLayoutContent$1$comktwappswalletmanagerCalendar(accountSymbol, summary, j5, date, record);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$setUpLayoutContent$0(CalendarRecord calendarRecord, CalendarRecord calendarRecord2) {
        return calendarRecord.getDate() - calendarRecord2.getDate();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$setUpLayoutContent$1$com-ktwapps-walletmanager-Calendar  reason: not valid java name */
    public /* synthetic */ void m176lambda$setUpLayoutContent$1$comktwappswalletmanagerCalendar(String str, CalendarSummary calendarSummary, long j, Date date, List list) {
        setUpSummary(str, calendarSummary, j);
        this.dateLabel.setText(CalendarHelper.getFormattedMonthlyDate(date));
        this.adapter.setList(list);
        this.adapter.setDate(date);
        this.adapter.notifyDataSetChanged();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.backImage) {
            Date incrementMonth = CalendarHelper.incrementMonth(this.date, -1);
            this.date = incrementMonth;
            setUpLayoutContent(incrementMonth);
        } else if (id == R.id.dateLabel) {
            Intent intent = new Intent(this.activity, MonthlyPicker.class);
            intent.putExtra("date", this.date.getTime());
            startActivityForResult(intent, 6);
        } else if (id != R.id.nextImage) {
        } else {
            Date incrementMonth2 = CalendarHelper.incrementMonth(this.date, 1);
            this.date = incrementMonth2;
            setUpLayoutContent(incrementMonth2);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 6 && resultCode == -1 && data != null) {
            Date dateFromLong = CalendarHelper.getDateFromLong(data.getLongExtra("date", 0L));
            this.date = dateFromLong;
            setUpLayoutContent(dateFromLong);
        }
    }

    @Override // com.ktwapps.walletmanager.Adapter.CalendarAdapter.OnItemClickListener
    public void OnItemClick(View view) {
        if (view.getTag() != null) {
            Date calendarDay = CalendarHelper.getCalendarDay(this.date, ((Integer) view.getTag()).intValue());
            BottomTransactionDialog bottomTransactionDialog = new BottomTransactionDialog();
            bottomTransactionDialog.setDate(calendarDay);
            bottomTransactionDialog.show(this.activity.getSupportFragmentManager(), "Transaction");
        }
    }
}
