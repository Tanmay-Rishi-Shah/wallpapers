package com.expance.merapaisa.Widget;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.expance.merapaisa.Adapter.CalendarTransactionAdapter;
import com.expance.merapaisa.CreateTransaction;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.DebtDetails;
import com.expance.merapaisa.Details;
import com.expance.merapaisa.Model.CalendarRecord;
import com.expance.merapaisa.Model.DailyTrans;
import com.expance.merapaisa.Model.Recurring;
import com.expance.merapaisa.Model.Trans;
import com.expance.merapaisa.Model.TransList;
import com.expance.merapaisa.R;
import com.expance.merapaisa.RecurringDetail;
import com.expance.merapaisa.Utility.CalendarHelper;
import com.expance.merapaisa.Utility.DateHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.RecurringHelper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

/* loaded from: classes3.dex */
public class BottomTransactionDialog extends BottomSheetDialogFragment implements View.OnClickListener, CalendarTransactionAdapter.OnItemClickListener {
    CalendarTransactionAdapter adapter;
    TextView amountLabel;
    ImageView backImage;
    Button button;
    Date date;
    TextView dateLabel;
    TextView emptyLabel;
    ImageView nextImage;
    RecyclerView recyclerView;

    @Override // com.google.android.material.bottomsheet.BottomSheetDialogFragment, androidx.appcompat.app.AppCompatDialogFragment, androidx.fragment.app.DialogFragment
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            long j = savedInstanceState.getLong("date");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(j);
            this.date = calendar.getTime();
        }
        return super.onCreateDialog(savedInstanceState);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("date", this.date.getTime());
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.bottom_transaction_layout, container, false);
        getDialog().setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.ktwapps.walletmanager.Widget.BottomTransactionDialog$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnShowListener
            public final void onShow(DialogInterface dialogInterface) {
                BottomTransactionDialog.lambda$onCreateView$0(dialogInterface);
            }
        });
        this.adapter = new CalendarTransactionAdapter(getActivity());
        this.dateLabel = (TextView) inflate.findViewById(R.id.dateLabel);
        this.amountLabel = (TextView) inflate.findViewById(R.id.amountLabel);
        this.emptyLabel = (TextView) inflate.findViewById(R.id.emptyLabel);
        this.backImage = (ImageView) inflate.findViewById(R.id.backImage);
        this.nextImage = (ImageView) inflate.findViewById(R.id.nextImage);
        this.button = (Button) inflate.findViewById(R.id.button);
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerView);
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView.setAdapter(this.adapter);
        this.adapter.setListener(this);
        this.backImage.setOnClickListener(this);
        this.nextImage.setOnClickListener(this);
        this.button.setOnClickListener(this);
        getList();
        return inflate;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$onCreateView$0(DialogInterface dialogInterface) {
        View findViewById = ((BottomSheetDialog) dialogInterface).findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior.from(findViewById).setPeekHeight(Helper.getScreenHeight());
        BottomSheetBehavior.from(findViewById).setState(3);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        getList();
    }

    private void getList() {
        if (this.date == null) {
            return;
        }
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.Widget.BottomTransactionDialog.1
            @Override // java.lang.Runnable
            public void run() {
                char c;
                final String beautifyAmount;
                Calendar calendar;
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(BottomTransactionDialog.this.getActivity());
                long dailyStartDate = CalendarHelper.getDailyStartDate(BottomTransactionDialog.this.date);
                long dailyEndDate = CalendarHelper.getDailyEndDate(BottomTransactionDialog.this.date);
                int accountId = SharePreferenceHelper.getAccountId(BottomTransactionDialog.this.getActivity());
                final ArrayList arrayList = new ArrayList();
                List<Recurring> allRecurringListByAccountId = appDatabaseObject.recurringDaoObject().getAllRecurringListByAccountId(SharePreferenceHelper.getAccountId(BottomTransactionDialog.this.getActivity()));
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(BottomTransactionDialog.this.date);
                boolean z = SharePreferenceHelper.isCarryOverOn(BottomTransactionDialog.this.getActivity()) && calendar2.get(5) == 1;
                long accountBalance = appDatabaseObject.calenderDaoObject().getAccountBalance(accountId, dailyStartDate);
                DailyTrans[] dailyTrans = appDatabaseObject.calenderDaoObject().getDailyTrans(accountId, dailyStartDate, dailyEndDate);
                int length = dailyTrans.length;
                int i = 0;
                while (i < length) {
                    Date dateTime = dailyTrans[i].getDateTime();
                    int i2 = accountId;
                    Calendar calendar3 = calendar2;
                    for (Trans trans : appDatabaseObject.calenderDaoObject().getTransFromDate(accountId, DateHelper.getTransactionStartDate(dateTime), DateHelper.getTransactionEndDate(dateTime))) {
                        arrayList.add(new TransList(false, trans, null));
                    }
                    i++;
                    accountId = i2;
                    calendar2 = calendar3;
                }
                Calendar calendar4 = calendar2;
                long j = 0;
                long j2 = 0;
                for (Recurring recurring : allRecurringListByAccountId) {
                    if (recurring.getIsFuture() == 1) {
                        float currencyRate = appDatabaseObject.currencyDaoObject().getCurrencyRate(SharePreferenceHelper.getAccountId(BottomTransactionDialog.this.getActivity()), recurring.getCurrency());
                        List<CalendarRecord> calendarRecurring = RecurringHelper.getCalendarRecurring(recurring, currencyRate, BottomTransactionDialog.this.date);
                        j += RecurringHelper.getCalendarRecurringCarryOver(recurring, currencyRate, BottomTransactionDialog.this.date);
                        for (CalendarRecord calendarRecord : calendarRecurring) {
                            calendar = calendar4;
                            if (calendarRecord.getDate() == calendar.get(5)) {
                                Calendar calendar5 = Calendar.getInstance();
                                calendar5.setTime(BottomTransactionDialog.this.date);
                                calendar5.set(14, 0);
                                calendar5.set(13, 0);
                                calendar5.set(12, 0);
                                calendar5.set(11, 0);
                                Trans trans2 = new Trans(recurring.getNote(BottomTransactionDialog.this.getActivity()), "", recurring.getColor(), recurring.getIcon(), recurring.getCurrency(), calendar5.getTime(), recurring.getAmount(), recurring.getWallet(), recurring.getType(), "", recurring.getWalletId(), 0, recurring.getCategory(BottomTransactionDialog.this.getActivity()), recurring.getCategoryId(), recurring.getCategoryDefault(), 0, "", 0L, null, 0, 0, 0, 0, "", 0);
                                trans2.setRecurring(true);
                                trans2.setRecurringId(recurring.getId());
                                arrayList.add(0, new TransList(false, trans2, null));
                                j2 = (long) (((float) j2) + (((float) recurring.getAmount()) * currencyRate));
                                break;
                            }
                            calendar4 = calendar;
                        }
                    }
                    calendar = calendar4;
                    calendar4 = calendar;
                }
                if (z) {
                    c = 0;
                    arrayList.add(0, new TransList(false, new Trans(BottomTransactionDialog.this.getResources().getString(R.string.carry_over), "", "#808080", 0, "AED", BottomTransactionDialog.this.date, accountBalance + j, "", 0, "", 0, 0, "", 0, 0, 0, "", 0L, null, 0, 0, 0, 0, "", 0), null));
                } else {
                    c = 0;
                }
                if (dailyTrans.length != 0) {
                    beautifyAmount = Helper.getBeautifyAmount(SharePreferenceHelper.getAccountSymbol(BottomTransactionDialog.this.getActivity()), dailyTrans[c].getAmount() + (z ? accountBalance + j : 0L) + j2);
                } else {
                    beautifyAmount = Helper.getBeautifyAmount(SharePreferenceHelper.getAccountSymbol(BottomTransactionDialog.this.getActivity()), (z ? accountBalance + j : 0L) + j2);
                }
                BottomTransactionDialog.this.getActivity().runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.Widget.BottomTransactionDialog.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        BottomTransactionDialog.this.adapter.setList(arrayList);
                        BottomTransactionDialog.this.dateLabel.setText(DateHelper.getTransFormattedDate(BottomTransactionDialog.this.date));
                        BottomTransactionDialog.this.emptyLabel.setVisibility(arrayList.size() == 0 ? 0 : 8);
                        BottomTransactionDialog.this.amountLabel.setText(beautifyAmount);
                    }
                });
            }
        });
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.backImage) {
            this.date = CalendarHelper.incrementDay(this.date, -1);
            getList();
        } else if (view.getId() == R.id.nextImage) {
            this.date = CalendarHelper.incrementDay(this.date, 1);
            getList();
        } else if (view.getId() == R.id.button) {
            Intent intent = new Intent(getActivity(), CreateTransaction.class);
            intent.putExtra("dateTime", this.date.getTime());
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
        }
    }

    @Override // com.ktwapps.walletmanager.Adapter.CalendarTransactionAdapter.OnItemClickListener
    public void OnItemClick(View view, int position) {
        Trans trans = this.adapter.list.get(position).getTrans();
        if (trans.getWalletId() != 0) {
            if (trans.isRecurring()) {
                if (trans.getRecurringId() != 0) {
                    Intent intent = new Intent(getActivity(), RecurringDetail.class);
                    intent.putExtra("recurringId", trans.getRecurringId());
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
                }
            } else if (trans.getDebtId() != 0 && trans.getDebtTransId() == 0) {
                Intent intent2 = new Intent(getActivity(), DebtDetails.class);
                intent2.putExtra("debtId", trans.getDebtId());
                startActivity(intent2);
                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
            } else {
                Intent intent3 = new Intent(getActivity(), Details.class);
                intent3.putExtra("transId", trans.getId());
                startActivity(intent3);
                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
            }
        }
    }
}
