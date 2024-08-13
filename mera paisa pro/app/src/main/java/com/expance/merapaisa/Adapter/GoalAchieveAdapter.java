package com.expance.merapaisa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.expance.merapaisa.Model.Goal;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Utility.DateHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class GoalAchieveAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    public List<Goal> list = new ArrayList();
    OnItemClickListener listener;

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public GoalAchieveAdapter(Context context) {
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
        return new ViewHolder(this.inflater.inflate(R.layout.list_goal_achieve, parent, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Goal goal = this.list.get(position);
        String accountSymbol = (goal.getCurrency() == null || goal.getCurrency().length() == 0) ? SharePreferenceHelper.getAccountSymbol(this.context) : DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(goal.getCurrency()));
        String name = goal.getName();
        String beautifyAmount = Helper.getBeautifyAmount(accountSymbol, goal.getAmount());
        String string = this.context.getResources().getString(R.string.achieve_at, DateHelper.getSimpleFormattedDate(goal.getAchieveDate()));
        viewHolder.nameLabel.setText(name);
        viewHolder.goalLabel.setText(beautifyAmount);
        viewHolder.achieveDateLabel.setText(string);
    }

    /* loaded from: classes3.dex */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView achieveDateLabel;
        TextView goalLabel;
        TextView nameLabel;

        public ViewHolder(View itemView) {
            super(itemView);
            this.nameLabel = (TextView) itemView.findViewById(R.id.nameLabel);
            this.goalLabel = (TextView) itemView.findViewById(R.id.goalLabel);
            this.achieveDateLabel = (TextView) itemView.findViewById(R.id.achieveDateLabel);
            itemView.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (GoalAchieveAdapter.this.listener != null) {
                GoalAchieveAdapter.this.listener.onItemClick(view, getLayoutPosition());
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
