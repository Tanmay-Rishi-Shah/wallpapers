package com.expance.merapaisa.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.expance.merapaisa.Model.Goal;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Utility.DateHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class GoalOngoingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    public List<Goal> list = new ArrayList();
    OnItemClickListener listener;

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public GoalOngoingAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setList(List<Goal> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<Goal> getList() {
        return this.list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(this.inflater.inflate(R.layout.list_goal, parent, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Goal goal = this.list.get(position);
        String accountSymbol = (goal.getCurrency() == null || goal.getCurrency().length() == 0) ? SharePreferenceHelper.getAccountSymbol(this.context) : DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(goal.getCurrency()));
        String name = goal.getName();
        String color = goal.getColor();
        String string = this.context.getResources().getString(R.string.amount_left, Helper.getBeautifyAmount(accountSymbol, goal.getAmount() - goal.getSaved()));
        String formattedDate = DateHelper.getFormattedDate(this.context, goal.getExpectDate());
        viewHolder.nameLabel.setText(name);
        viewHolder.dateLabel.setText(formattedDate);
        viewHolder.remainingLabel.setText(string);
        viewHolder.progressBar.setTextColor(Color.parseColor(color));
        viewHolder.progressBar.setFinishedStrokeColor(Color.parseColor(color));
        viewHolder.progressBar.setProgress(Helper.getProgressValue(goal.getAmount(), goal.getSaved()));
    }

    /* loaded from: classes3.dex */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView dateLabel;
        TextView nameLabel;
        DonutProgress progressBar;
        TextView remainingLabel;

        ViewHolder(View itemView) {
            super(itemView);
            this.nameLabel = (TextView) itemView.findViewById(R.id.nameLabel);
            this.dateLabel = (TextView) itemView.findViewById(R.id.dateLabel);
            this.remainingLabel = (TextView) itemView.findViewById(R.id.remainingLabel);
            this.progressBar = (DonutProgress) itemView.findViewById(R.id.progressBar);
            itemView.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (GoalOngoingAdapter.this.listener != null) {
                GoalOngoingAdapter.this.listener.onItemClick(view, getLayoutPosition());
            }
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
