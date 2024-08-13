package com.expance.merapaisa.Adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.expance.merapaisa.Model.WeeklyStats;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import com.expance.merapaisa.Utility.StatisticHelper;
import com.expance.merapaisa.Widget.MarkerViewWeekly;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/* loaded from: classes3.dex */
public class StatisticWeeklyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    Date date;
    LayoutInflater inflater;
    OnItemClickListener listener;
    int mode = 2;
    List<WeeklyStats> list = new ArrayList();

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void OnItemClick(View v, int position);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return 8;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 8;
    }

    public StatisticWeeklyAdapter(Context context, Date date) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.date = date;
    }

    public void setList(List<WeeklyStats> list) {
        this.list = list;
    }

    public List<WeeklyStats> getList() {
        return this.list;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new HeaderViewHolder(this.inflater.inflate(R.layout.list_statistic_weekly_header, parent, false));
        }
        return new ItemViewHolder(this.inflater.inflate(R.layout.list_statistic_weekly_item, parent, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        long j = 0;
        if (getItemViewType(position) == 0) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            setBarChart(headerViewHolder.barChart);
            String accountSymbol = SharePreferenceHelper.getAccountSymbol(this.context);
            for (WeeklyStats weeklyStats : this.list) {
                j += weeklyStats.getAmount();
            }
            headerViewHolder.amountLabel.setText(Helper.getBeautifyAmount(accountSymbol, j));
            return;
        }
        int firstDayOfWeek = SharePreferenceHelper.getFirstDayOfWeek(this.context);
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(1);
        calendar.setTime(this.date);
        if (firstDayOfWeek > calendar.get(7)) {
            calendar.add(3, -1);
        }
        calendar.set(7, firstDayOfWeek);
        calendar.add(7, position - 1);
        String accountSymbol2 = SharePreferenceHelper.getAccountSymbol(this.context);
        String bestDateTimePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "dd MMMM yyyy");
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.dayLabel.setText(new SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.getTime()));
        itemViewHolder.dateLabel.setText(new SimpleDateFormat(bestDateTimePattern, Locale.getDefault()).format(calendar.getTime()));
        itemViewHolder.amountLabel.setTextColor(this.context.getResources().getColor(R.color.expense));
        itemViewHolder.amountLabel.setText(Helper.getBeautifyAmount(accountSymbol2, 0L));
        itemViewHolder.transLabel.setText(this.context.getResources().getString(R.string.total_transaction, 0));
        itemViewHolder.divider.setVisibility(position == 7 ? 8 : 0);
        for (WeeklyStats weeklyStats2 : this.list) {
            if (weeklyStats2.getDay() == calendar.get(5)) {
                itemViewHolder.amountLabel.setText(Helper.getBeautifyAmount(accountSymbol2, weeklyStats2.getAmount()));
                itemViewHolder.transLabel.setText(weeklyStats2.getTrans() > 1 ? this.context.getResources().getString(R.string.total_transactions, Integer.valueOf(weeklyStats2.getTrans())) : this.context.getResources().getString(R.string.total_transaction, Integer.valueOf(weeklyStats2.getTrans())));
                return;
            }
        }
    }

    private void setBarChart(final BarChart chart) {
        chart.setDrawValueAboveBar(false);
        chart.getDescription().setEnabled(false);
        chart.setScaleEnabled(false);
        chart.setPinchZoom(false);
        final MarkerViewWeekly markerViewWeekly = new MarkerViewWeekly(this.context, R.layout.marker_view_weekly);
        markerViewWeekly.setWidth(chart.getWidth());
        markerViewWeekly.setDate(StatisticHelper.getWeeklyMarkerFirstDate(this.context, this.date));
        chart.setMarker(markerViewWeekly);
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() { // from class: com.ktwapps.walletmanager.Adapter.StatisticWeeklyAdapter.1
            @Override // com.github.mikephil.charting.listener.OnChartValueSelectedListener
            public void onNothingSelected() {
            }

            @Override // com.github.mikephil.charting.listener.OnChartValueSelectedListener
            public void onValueSelected(Entry e, Highlight h) {
                if (markerViewWeekly.getChartWidth() == 0) {
                    markerViewWeekly.setWidth(chart.getWidth());
                }
            }
        });
        chart.setDrawGridBackground(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setAxisLineColor(Helper.getAttributeColor(this.context, R.attr.secondaryTextColor));
        xAxis.setTextColor(Helper.getAttributeColor(this.context, R.attr.primaryTextColor));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1.0f);
        YAxis axisLeft = chart.getAxisLeft();
        axisLeft.setAxisLineColor(Helper.getAttributeColor(this.context, R.attr.secondaryTextColor));
        axisLeft.setTextColor(Helper.getAttributeColor(this.context, R.attr.primaryTextColor));
        axisLeft.setLabelCount(6, false);
        axisLeft.enableGridDashedLine(10.0f, 10.0f, 0.0f);
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setDrawAxisLine(false);
        axisLeft.setAxisMinimum(0.0f);
        YAxis axisRight = chart.getAxisRight();
        axisRight.setDrawAxisLine(false);
        axisRight.setDrawGridLines(false);
        axisRight.setDrawLabels(false);
        chart.getLegend().setEnabled(false);
        setBarData(chart);
    }

    private void setBarData(BarChart chart) {
        ArrayList arrayList = new ArrayList();
        int firstDayOfWeek = SharePreferenceHelper.getFirstDayOfWeek(this.context);
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(1);
        calendar.setTime(this.date);
        if (firstDayOfWeek > calendar.get(7)) {
            calendar.add(3, -1);
        }
        calendar.set(7, firstDayOfWeek);
        float f = 0.0f;
        for (int i = 0; i < 7; i++) {
            long j = 0;
            for (WeeklyStats weeklyStats : this.list) {
                if (calendar.get(5) == weeklyStats.getDay()) {
                    long j2 = (-weeklyStats.getAmount()) / 100;
                    float f2 = (float) j2;
                    if (f <= f2) {
                        f = f2;
                    }
                    j = j2;
                }
            }
            calendar.add(5, 1);
            arrayList.add(new BarEntry(i, (float) j));
        }
        chart.getAxisLeft().setAxisMaximum(f == 0.0f ? 120.0f : (f / 5.0f) + f);
        XAxis xAxis = chart.getXAxis();
        String[] strArr = {this.context.getResources().getString(R.string.sun), this.context.getResources().getString(R.string.mon), this.context.getResources().getString(R.string.tue), this.context.getResources().getString(R.string.wed), this.context.getResources().getString(R.string.thu), this.context.getResources().getString(R.string.fri), this.context.getResources().getString(R.string.sat)};
        final String[] strArr2 = new String[7];
        int firstDayOfWeek2 = SharePreferenceHelper.getFirstDayOfWeek(this.context);
        int i2 = 0;
        for (int i3 = 0; i3 < 7; i3++) {
            int i4 = (firstDayOfWeek2 - 1) + i2;
            strArr2[i3] = strArr[i4];
            if (i4 >= 6) {
                firstDayOfWeek2 = 1;
                i2 = -1;
            }
            i2++;
        }
        xAxis.setValueFormatter(new ValueFormatter() { // from class: com.ktwapps.walletmanager.Adapter.StatisticWeeklyAdapter.2
            @Override // com.github.mikephil.charting.formatter.ValueFormatter
            public String getFormattedValue(float value) {
                return strArr2[(int) value];
            }
        });
        if (chart.getData() != null && ((BarData) chart.getData()).getDataSetCount() > 0) {
            ((BarDataSet) ((BarData) chart.getData()).getDataSetByIndex(0)).setValues(arrayList);
            ((BarData) chart.getData()).notifyDataChanged();
            chart.notifyDataSetChanged();
            return;
        }
        BarDataSet barDataSet = new BarDataSet(arrayList, "");
        barDataSet.setDrawIcons(false);
        barDataSet.setDrawValues(false);
        barDataSet.setColor(ContextCompat.getColor(this.context, R.color.expense));
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(barDataSet);
        BarData barData = new BarData(arrayList2);
        barData.setValueTextSize(10.0f);
        barData.setBarWidth(0.5f);
        chart.setData(barData);
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /* loaded from: classes3.dex */
    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView amountLabel;
        BarChart barChart;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            this.amountLabel = (TextView) itemView.findViewById(R.id.amountLabel);
            this.barChart = (BarChart) itemView.findViewById(R.id.barChart);
        }
    }

    /* loaded from: classes3.dex */
    private class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView amountLabel;
        TextView dateLabel;
        TextView dayLabel;
        View divider;
        TextView transLabel;

        public ItemViewHolder(View itemView) {
            super(itemView);
            this.dayLabel = (TextView) itemView.findViewById(R.id.dayLabel);
            this.dateLabel = (TextView) itemView.findViewById(R.id.dateLabel);
            this.amountLabel = (TextView) itemView.findViewById(R.id.amountLabel);
            this.transLabel = (TextView) itemView.findViewById(R.id.transLabel);
            this.divider = itemView.findViewById(R.id.divider);
            itemView.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (StatisticWeeklyAdapter.this.listener != null) {
                StatisticWeeklyAdapter.this.listener.OnItemClick(view, getLayoutPosition());
            }
        }
    }
}
