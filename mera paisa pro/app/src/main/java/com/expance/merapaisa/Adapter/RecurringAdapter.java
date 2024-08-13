package com.expance.merapaisa.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.expance.merapaisa.Model.Recurring;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.RecurringHelper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/* loaded from: classes3.dex */
public class RecurringAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<Recurring> list = new ArrayList();
    private OnItemClickListener listener;

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public RecurringAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setList(List<Recurring> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public OnItemClickListener getListener() {
        return this.listener;
    }

    public List<Recurring> getList() {
        return this.list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(this.inflater.inflate(R.layout.list_recurring, parent, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Holder holder2 = (Holder) holder;
        Recurring recurring = this.list.get(position);
        String str = DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(recurring.getCurrency()));
        long nextOccurrence = RecurringHelper.getNextOccurrence(recurring.getDateTime(), recurring.getLastUpdateTime().getTime(), recurring.getRecurringType(), recurring.getIncrement(), recurring.getRepeatType(), recurring.getRepeatDate());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(nextOccurrence);
        String bestDateTimePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "dd MMM yyyy");
        Resources resources = this.context.getResources();
        String string = resources.getString(R.string.recurring_occurrence, new SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.getTime()) + ", " + new SimpleDateFormat(bestDateTimePattern, Locale.getDefault()).format(calendar.getTime()));
        if (Build.VERSION.SDK_INT >= 29) {
            holder2.colorView.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor(recurring.getColor()), BlendMode.SRC_OVER));
        } else {
            holder2.colorView.getBackground().setColorFilter(Color.parseColor(recurring.getColor()), PorterDuff.Mode.SRC_OVER);
        }
        holder2.imageView.setImageResource(DataHelper.getCategoryIcons().get(recurring.getIcon()).intValue());
        holder2.nameLabel.setText(recurring.getNote(this.context));
        holder2.amountLabel.setText(Helper.getBeautifyAmount(str, recurring.getAmount()));
        holder2.amountLabel.setTextColor(this.context.getResources().getColor(recurring.getType() == 1 ? R.color.expense : R.color.income));
        holder2.detailLabel.setText(string);
    }

    /* loaded from: classes3.dex */
    private class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView amountLabel;
        ConstraintLayout colorView;
        TextView detailLabel;
        ImageView imageView;
        TextView nameLabel;

        Holder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.colorView = (ConstraintLayout) itemView.findViewById(R.id.colorView);
            this.nameLabel = (TextView) itemView.findViewById(R.id.nameLabel);
            this.detailLabel = (TextView) itemView.findViewById(R.id.detailLabel);
            this.amountLabel = (TextView) itemView.findViewById(R.id.amountLabel);
            itemView.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (RecurringAdapter.this.listener != null) {
                RecurringAdapter.this.listener.onItemClick(view, getLayoutPosition());
            }
        }
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.list.size();
    }
}
