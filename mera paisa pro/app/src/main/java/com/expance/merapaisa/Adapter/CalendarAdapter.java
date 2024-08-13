package com.expance.merapaisa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.expance.merapaisa.Model.CalendarRecord;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.CalendarHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes3.dex */
public class CalendarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    OnItemClickListener listener;
    Date date = new Date();
    String[] weekdays = new String[7];
    List<CalendarRecord> list = new ArrayList();

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void OnItemClick(View view);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int position) {
        return position >= 7 ? 1 : 0;
    }

    public CalendarAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.weekdays[0] = context.getResources().getString(R.string.sun);
        this.weekdays[1] = context.getResources().getString(R.string.mon);
        this.weekdays[2] = context.getResources().getString(R.string.tue);
        this.weekdays[3] = context.getResources().getString(R.string.wed);
        this.weekdays[4] = context.getResources().getString(R.string.thu);
        this.weekdays[5] = context.getResources().getString(R.string.fri);
        this.weekdays[6] = context.getResources().getString(R.string.sat);
    }

    public void setList(List<CalendarRecord> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        int dayOfMonth = CalendarHelper.getDayOfMonth(this.date);
        int dayOfWeek = CalendarHelper.getDayOfWeek(this.date) - SharePreferenceHelper.getFirstDayOfWeek(this.context);
        if (dayOfWeek < 0) {
            dayOfWeek += 7;
        }
        int i = dayOfMonth + dayOfWeek;
        return (((i / 7) + (i % 7 == 0 ? 0 : 1)) * 7) + 7;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new HeaderViewHolder(this.inflater.inflate(R.layout.list_calendar_header, parent, false));
        }
        return new ItemViewHolder(this.inflater.inflate(R.layout.list_calendar_item, parent, false), parent);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 0) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            int firstDayOfWeek = (SharePreferenceHelper.getFirstDayOfWeek(this.context) - 1) + position;
            if (firstDayOfWeek >= 7) {
                firstDayOfWeek -= 7;
            }
            headerViewHolder.weekLabel.setTextColor(firstDayOfWeek == 0 ? this.context.getResources().getColor(R.color.calendar_red_dark) : Helper.getAttributeColor(this.context, R.attr.primaryDarkTextColor));
            headerViewHolder.weekLabel.setText(this.weekdays[firstDayOfWeek]);
            return;
        }
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        float height = (itemViewHolder.parent.getHeight() - Helper.convertDpToPixel(this.context, 30.0f)) / ((getItemCount() - 7) / 7.0f);
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) itemViewHolder.itemView.getLayoutParams();
        if (height <= Helper.convertDpToPixel(this.context, 70.0f)) {
            height = Helper.convertDpToPixel(this.context, 70.0f);
        }
        layoutParams.height = (int) height;
        itemViewHolder.itemView.setLayoutParams(layoutParams);
        int dayOfWeek = CalendarHelper.getDayOfWeek(this.date) - SharePreferenceHelper.getFirstDayOfWeek(this.context);
        if (dayOfWeek < 0) {
            dayOfWeek += 7;
        }
        int dayOfMonth = CalendarHelper.getDayOfMonth(this.date);
        int i = position - 7;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.date);
        calendar.set(5, 1);
        boolean z = ((SharePreferenceHelper.getFirstDayOfWeek(this.context) - 1) + position) % 7 == 0;
        if (i >= dayOfWeek) {
            int i2 = i - dayOfWeek;
            calendar.add(5, i2);
            if (i2 >= dayOfMonth) {
                if (i2 - dayOfMonth == 0) {
                    itemViewHolder.dayLabel.setText((calendar.get(2) + 1) + "/" + calendar.get(5));
                } else {
                    itemViewHolder.dayLabel.setText(String.valueOf(calendar.get(5)));
                }
                itemViewHolder.expenseLabel.setText("");
                itemViewHolder.incomeLabel.setText("");
                itemViewHolder.totalLabel.setText("");
                itemViewHolder.dayLabel.setBackground(null);
                TextView textView = itemViewHolder.dayLabel;
                Context context = this.context;
                textView.setTextColor(z ? context.getResources().getColor(R.color.calendar_red_light) : Helper.getAttributeColor(context, R.attr.primaryDarkAlphaTextColor));
                itemViewHolder.itemView.setTag(null);
                return;
            }
            if (i2 == 0) {
                itemViewHolder.dayLabel.setText((calendar.get(2) + 1) + "/" + calendar.get(5));
            } else {
                itemViewHolder.dayLabel.setText(String.valueOf(calendar.get(5)));
            }
            int i3 = i2 + 1;
            boolean z2 = CalendarHelper.isSameMonth(this.date) == i3;
            itemViewHolder.expenseLabel.setText("");
            itemViewHolder.incomeLabel.setText("");
            itemViewHolder.totalLabel.setText("");
            Iterator<CalendarRecord> it = this.list.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                CalendarRecord next = it.next();
                if (next.getDate() == i3) {
                    long income = next.getIncome() + next.getExpense();
                    itemViewHolder.expenseLabel.setText(Helper.getBeautifyAmount("", next.getExpense()));
                    itemViewHolder.incomeLabel.setText(Helper.getBeautifyAmount("", next.getIncome()));
                    itemViewHolder.totalLabel.setText(Helper.getBeautifyAmount("", income));
                    break;
                }
            }
            if (z2) {
                itemViewHolder.dayLabel.setBackgroundColor(this.context.getResources().getColor(R.color.blue_alpha));
            } else {
                itemViewHolder.dayLabel.setBackground(null);
            }
            TextView textView2 = itemViewHolder.dayLabel;
            Context context2 = this.context;
            textView2.setTextColor(z ? context2.getResources().getColor(R.color.calendar_red_dark) : Helper.getAttributeColor(context2, R.attr.primaryTextColor));
            itemViewHolder.itemView.setTag(Integer.valueOf(i3));
            return;
        }
        calendar.add(5, -(dayOfWeek - i));
        itemViewHolder.dayLabel.setText(String.valueOf(calendar.get(5)));
        itemViewHolder.expenseLabel.setText("");
        itemViewHolder.incomeLabel.setText("");
        itemViewHolder.totalLabel.setText("");
        itemViewHolder.dayLabel.setBackground(null);
        TextView textView3 = itemViewHolder.dayLabel;
        Context context3 = this.context;
        textView3.setTextColor(z ? context3.getResources().getColor(R.color.calendar_red_light) : Helper.getAttributeColor(context3, R.attr.primaryDarkAlphaTextColor));
        itemViewHolder.itemView.setTag(null);
    }

    /* loaded from: classes3.dex */
    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView weekLabel;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            this.weekLabel = (TextView) itemView.findViewById(R.id.weekLabel);
        }
    }

    /* loaded from: classes3.dex */
    private class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView dayLabel;
        TextView expenseLabel;
        TextView incomeLabel;
        ViewGroup parent;
        TextView totalLabel;

        ItemViewHolder(View itemView, ViewGroup parent) {
            super(itemView);
            this.parent = parent;
            this.dayLabel = (TextView) itemView.findViewById(R.id.dayLabel);
            this.incomeLabel = (TextView) itemView.findViewById(R.id.incomeLabel);
            this.expenseLabel = (TextView) itemView.findViewById(R.id.expenseLabel);
            this.totalLabel = (TextView) itemView.findViewById(R.id.totalLabel);
            itemView.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (CalendarAdapter.this.listener != null) {
                CalendarAdapter.this.listener.OnItemClick(view);
            }
        }
    }
}
