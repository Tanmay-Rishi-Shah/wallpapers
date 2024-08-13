package com.expance.merapaisa.Adapter;

import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.expance.merapaisa.Model.Debt;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class DebtLendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    List<Object> list = new ArrayList();
    OnItemClickListener listener;

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public DebtLendAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setList(List<Object> list) {
        this.list = list;
    }

    public List<Object> getList() {
        return this.list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int position) {
        return this.list.get(position) instanceof Long ? 0 : 1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new HeaderHolder(this.inflater.inflate(R.layout.list_debt_header, parent, false));
        }
        return new ViewHolder(this.inflater.inflate(R.layout.list_debt, parent, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String string;
        if (getItemViewType(position) == 0) {
            HeaderHolder headerHolder = (HeaderHolder) holder;
            String beautifyAmount = Helper.getBeautifyAmount(SharePreferenceHelper.getAccountSymbol(this.context), ((Long) this.list.get(position)).longValue());
            if (((Debt) this.list.get(position + 1)).getStatus() == 0) {
                string = this.context.getResources().getString(R.string.not_pay_amount, beautifyAmount);
            } else {
                string = this.context.getResources().getString(R.string.pay_amount, beautifyAmount);
            }
            SpannableString spannableString = new SpannableString(string);
            spannableString.setSpan(new ForegroundColorSpan(this.context.getResources().getColor(R.color.income)), spannableString.length() - beautifyAmount.length(), spannableString.length(), 33);
            headerHolder.headerLabel.setText(spannableString);
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        Debt debt = (Debt) this.list.get(position);
        String str = DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(debt.getCurrencyCode()));
        String string2 = (debt.getLender() == null || debt.getLender().length() <= 0) ? this.context.getResources().getString(R.string.someone) : debt.getLender();
        String substring = (debt.getLender() == null || debt.getLender().length() <= 0) ? "?" : debt.getLender().substring(0, 1);
        String string3 = (debt.getName() == null || debt.getName().length() <= 0) ? this.context.getResources().getString(R.string.no_description) : debt.getName();
        int attributeColor = Helper.getAttributeColor(this.context, (debt.getName() == null || debt.getName().length() <= 0) ? R.attr.untitledTextColor : R.attr.primaryTextColor);
        String color = debt.getColor();
        viewHolder.circleLabel.setText(substring);
        viewHolder.nameLabel.setText(string2);
        viewHolder.detailLabel.setText(string3);
        viewHolder.detailLabel.setTextColor(attributeColor);
        viewHolder.amountLabel.setTextColor(this.context.getResources().getColor(R.color.income));
        if (Build.VERSION.SDK_INT >= 29) {
            viewHolder.circleWrapper.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor(color), BlendMode.SRC_OVER));
        } else {
            viewHolder.circleWrapper.getBackground().setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_OVER);
        }
        if (debt.getStatus() == 0) {
            String beautifyAmount2 = Helper.getBeautifyAmount(str, debt.getAmount() - debt.getPay());
            viewHolder.amountLabel.setPaintFlags(viewHolder.amountLabel.getPaintFlags() & (-17));
            viewHolder.amountLabel.setText(beautifyAmount2);
        } else {
            String beautifyAmount3 = Helper.getBeautifyAmount(str, debt.getAmount());
            viewHolder.amountLabel.setPaintFlags(viewHolder.amountLabel.getPaintFlags() | 16);
            viewHolder.amountLabel.setText(beautifyAmount3);
        }
        int i = position + 1;
        if (i == this.list.size() || (this.list.get(i) instanceof Long)) {
            viewHolder.view.setVisibility(0);
        } else {
            viewHolder.view.setVisibility(8);
        }
    }

    /* loaded from: classes3.dex */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView amountLabel;
        TextView circleLabel;
        ConstraintLayout circleWrapper;
        TextView detailLabel;
        TextView nameLabel;
        View view;

        ViewHolder(View itemView) {
            super(itemView);
            this.nameLabel = (TextView) itemView.findViewById(R.id.nameLabel);
            this.amountLabel = (TextView) itemView.findViewById(R.id.amountLabel);
            this.detailLabel = (TextView) itemView.findViewById(R.id.detailLabel);
            this.circleLabel = (TextView) itemView.findViewById(R.id.circleLabel);
            this.circleWrapper = (ConstraintLayout) itemView.findViewById(R.id.circleWrapper);
            this.view = itemView.findViewById(R.id.view);
            itemView.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (DebtLendAdapter.this.listener != null) {
                DebtLendAdapter.this.listener.onItemClick(view, getLayoutPosition());
            }
        }
    }

    /* loaded from: classes3.dex */
    public class HeaderHolder extends RecyclerView.ViewHolder {
        TextView headerLabel;

        HeaderHolder(View itemView) {
            super(itemView);
            this.headerLabel = (TextView) itemView.findViewById(R.id.headerLabel);
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
