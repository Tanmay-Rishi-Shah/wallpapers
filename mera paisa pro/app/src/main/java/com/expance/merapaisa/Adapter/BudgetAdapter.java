package com.expance.merapaisa.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.expance.merapaisa.Model.Budget;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.BudgetHelper;
import com.expance.merapaisa.Utility.CalendarHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/* loaded from: classes3.dex */
public class BudgetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    List<Object> list = new ArrayList();
    OnItemClickListener listener;

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public BudgetAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setList(List<Object> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<Object> getList() {
        return this.list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new ViewHolder(this.inflater.inflate(R.layout.list_budget, parent, false));
        }
        return new HeaderViewHolder(this.inflater.inflate(R.layout.list_budget_header, parent, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int position) {
        return this.list.get(position) instanceof Integer ? 0 : 1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        long j;
        long j2;
        long j3 = 0;
        if (getItemViewType(position) == 0) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            Calendar calendar = Calendar.getInstance();
            String accountSymbol = SharePreferenceHelper.getAccountSymbol(this.context);
            int intValue = ((Integer) this.list.get(position)).intValue();
            if (intValue == 0) {
                headerViewHolder.titleLabel.setText(R.string.budget_weekly);
                headerViewHolder.dateLabel.setText(CalendarHelper.getFormattedWeeklyDate(this.context, calendar.getTime()));
                j = 0;
                for (int i = position + 1; i < this.list.size() && !(this.list.get(i) instanceof Integer); i++) {
                    Budget budget = (Budget) this.list.get(i);
                    if (BudgetHelper.getBudgetType(budget.getPeriod()) == 0) {
                        j3 += budget.getAmount();
                        j += budget.getSpent();
                    }
                }
            } else if (intValue == 1) {
                headerViewHolder.titleLabel.setText(R.string.budget_monthly);
                headerViewHolder.dateLabel.setText(CalendarHelper.getFormattedMonthlyDate(calendar.getTime()));
                j = 0;
                for (int i2 = position + 1; i2 < this.list.size() && !(this.list.get(i2) instanceof Integer); i2++) {
                    Budget budget2 = (Budget) this.list.get(i2);
                    if (BudgetHelper.getBudgetType(budget2.getPeriod()) == 1) {
                        j3 += budget2.getAmount();
                        j += budget2.getSpent();
                    }
                }
            } else if (intValue == 2) {
                headerViewHolder.titleLabel.setText(R.string.budget_quarterly);
                headerViewHolder.dateLabel.setText(CalendarHelper.getFormattedQuarterlyDate(calendar.getTime()));
                j = 0;
                for (int i3 = position + 1; i3 < this.list.size() && !(this.list.get(i3) instanceof Integer); i3++) {
                    Budget budget3 = (Budget) this.list.get(i3);
                    if (BudgetHelper.getBudgetType(budget3.getPeriod()) == 2) {
                        j3 += budget3.getAmount();
                        j += budget3.getSpent();
                    }
                }
            } else if (intValue == 3) {
                headerViewHolder.titleLabel.setText(R.string.budget_yearly);
                headerViewHolder.dateLabel.setText(CalendarHelper.getFormattedYearlyDate(calendar.getTime()));
                j = 0;
                for (int i4 = position + 1; i4 < this.list.size() && !(this.list.get(i4) instanceof Integer); i4++) {
                    Budget budget4 = (Budget) this.list.get(i4);
                    if (BudgetHelper.getBudgetType(budget4.getPeriod()) == 3) {
                        j3 += budget4.getAmount();
                        j += budget4.getSpent();
                    }
                }
            } else {
                j2 = 0;
                headerViewHolder.totalLabel.setText(this.context.getResources().getString(R.string.amount_slash_amount, Helper.getBeautifyAmount(accountSymbol, j3), Helper.getBeautifyAmount(accountSymbol, j2)));
                return;
            }
            long j4 = j3;
            j3 = j;
            j2 = j4;
            headerViewHolder.totalLabel.setText(this.context.getResources().getString(R.string.amount_slash_amount, Helper.getBeautifyAmount(accountSymbol, j3), Helper.getBeautifyAmount(accountSymbol, j2)));
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        Budget budget5 = (Budget) this.list.get(position);
        long amount = budget5.getAmount() - budget5.getSpent();
        String accountSymbol2 = SharePreferenceHelper.getAccountSymbol(this.context);
        String color = budget5.getColor();
        String name = budget5.getName();
        String formattedDate = BudgetHelper.getFormattedDate(this.context, budget5.getPeriod(), Calendar.getInstance().getTime(), budget5.getStartDate());
        int i5 = (amount > 0L ? 1 : (amount == 0L ? 0 : -1));
        if (i5 < 0) {
            amount = -amount;
        }
        String beautifyAmount = Helper.getBeautifyAmount(accountSymbol2, amount);
        String beautifyAmount2 = Helper.getBeautifyAmount(accountSymbol2, budget5.getSpent());
        viewHolder.remainTitleLabel.setText(i5 < 0 ? R.string.overspent : R.string.left);
        TextView textView = viewHolder.spentLabel;
        Context context = this.context;
        textView.setTextColor(i5 < 0 ? context.getResources().getColor(R.color.expense) : Helper.getAttributeColor(context, R.attr.primaryTextColor));
        viewHolder.nameLabel.setText(name);
        viewHolder.dateLabel.setText(formattedDate);
        viewHolder.remainLabel.setText(beautifyAmount);
        viewHolder.spentLabel.setText(beautifyAmount2);
        viewHolder.progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor(color)));
        viewHolder.progressBar.setProgress(Helper.getProgressValue(budget5.getAmount(), budget5.getSpent()));
    }

    /* loaded from: classes3.dex */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView dateLabel;
        TextView nameLabel;
        ProgressBar progressBar;
        TextView remainLabel;
        TextView remainTitleLabel;
        TextView spentLabel;

        public ViewHolder(View itemView) {
            super(itemView);
            this.remainTitleLabel = (TextView) itemView.findViewById(R.id.remainTitleLabel);
            this.progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            this.nameLabel = (TextView) itemView.findViewById(R.id.nameLabel);
            this.dateLabel = (TextView) itemView.findViewById(R.id.dateLabel);
            this.spentLabel = (TextView) itemView.findViewById(R.id.spentLabel);
            this.remainLabel = (TextView) itemView.findViewById(R.id.remainLabel);
            itemView.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (BudgetAdapter.this.listener != null) {
                BudgetAdapter.this.listener.onItemClick(view, getLayoutPosition());
            }
        }
    }

    /* loaded from: classes3.dex */
    public class HeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView dateLabel;
        TextView titleLabel;
        TextView totalLabel;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            this.titleLabel = (TextView) itemView.findViewById(R.id.titleLabel);
            this.dateLabel = (TextView) itemView.findViewById(R.id.dateLabel);
            this.totalLabel = (TextView) itemView.findViewById(R.id.totalLabel);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (BudgetAdapter.this.listener != null) {
                BudgetAdapter.this.listener.onItemClick(view, getLayoutPosition());
            }
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            if (BudgetAdapter.this.listener != null) {
                BudgetAdapter.this.listener.onItemLongClick(view, getLayoutPosition());
                return false;
            }
            return false;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.list.size();
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
