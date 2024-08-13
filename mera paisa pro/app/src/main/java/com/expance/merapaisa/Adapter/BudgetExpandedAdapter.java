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
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/* loaded from: classes3.dex */
public class BudgetExpandedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private Date date;
    private LayoutInflater inflater;
    private List<Budget> list = new ArrayList();
    private OnItemClickListener listener;

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public BudgetExpandedAdapter(Context context, Date date) {
        this.context = context;
        this.date = date;
        this.inflater = LayoutInflater.from(context);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setList(List<Budget> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<Budget> getList() {
        return this.list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(this.inflater.inflate(R.layout.list_budget, parent, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Budget budget = this.list.get(position);
        long amount = budget.getAmount() - budget.getSpent();
        String accountSymbol = SharePreferenceHelper.getAccountSymbol(this.context);
        String color = budget.getColor();
        String name = budget.getName();
        String formattedDate = BudgetHelper.getFormattedDate(this.context, budget.getPeriod(), this.date, budget.getStartDate());
        int i = (amount > 0L ? 1 : (amount == 0L ? 0 : -1));
        if (i < 0) {
            amount = -amount;
        }
        String beautifyAmount = Helper.getBeautifyAmount(accountSymbol, amount);
        String beautifyAmount2 = Helper.getBeautifyAmount(accountSymbol, budget.getSpent());
        viewHolder.remainTitleLabel.setText(i < 0 ? R.string.overspent : R.string.left);
        TextView textView = viewHolder.spentLabel;
        Context context = this.context;
        textView.setTextColor(i < 0 ? context.getResources().getColor(R.color.expense) : Helper.getAttributeColor(context, R.attr.primaryTextColor));
        viewHolder.nameLabel.setText(name);
        viewHolder.dateLabel.setText(formattedDate);
        viewHolder.remainLabel.setText(beautifyAmount);
        viewHolder.spentLabel.setText(beautifyAmount2);
        viewHolder.progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor(color)));
        viewHolder.progressBar.setProgress(Helper.getProgressValue(budget.getAmount(), budget.getSpent()));
    }

    /* loaded from: classes3.dex */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
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
            itemView.setOnLongClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (BudgetExpandedAdapter.this.listener != null) {
                BudgetExpandedAdapter.this.listener.onItemClick(view, getLayoutPosition());
            }
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            if (BudgetExpandedAdapter.this.listener != null) {
                BudgetExpandedAdapter.this.listener.onItemLongClick(view, getLayoutPosition());
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
