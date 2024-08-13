package com.expance.merapaisa.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.expance.merapaisa.Model.Budget;
import com.expance.merapaisa.Model.BudgetStats;
import com.expance.merapaisa.Model.BudgetTrans;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.AdsHelper;
import com.expance.merapaisa.Utility.BudgetHelper;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import com.expance.merapaisa.Widget.MarkerViewBudget;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/* loaded from: classes3.dex */
public class BudgetDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Budget budget;
    private Context context;
    private Date date;
    private LayoutInflater inflater;
    private boolean isAdsLoaded;
    private boolean isPremium;
    private OnItemClickListener listener;
    private List<BudgetTrans> list = new ArrayList();
    private List<BudgetStats> statsList = new ArrayList();

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void OnItemClick(View v, int position);
    }

    public BudgetDetailAdapter(Context context, Date date) {
        this.date = date;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    public void setBudgetTransList(List<BudgetTrans> list) {
        this.list = list;
    }

    public void setStatsList(List<BudgetStats> statsList) {
        this.statsList = statsList;
        notifyDataSetChanged();
    }

    public void setAds(boolean isPremium) {
        this.isPremium = isPremium;
        notifyDataSetChanged();
    }

    public List<BudgetTrans> getTransList() {
        return this.list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType != 0) {
            if (viewType == 1) {
                return new HeaderHolder(this.inflater.inflate(R.layout.list_budget_detail_header, parent, false));
            }
            if (viewType == 2) {
                return new TitleHolder(this.inflater.inflate(R.layout.list_budget_detail_title, parent, false));
            }
            return new ItemHolder(this.inflater.inflate(R.layout.list_budget_detail_item, parent, false));
        }
        AdsHolder adsHolder = new AdsHolder(this.inflater.inflate(R.layout.list_ad_view, parent, false));
        AdView adView = new AdView(this.context);
        adView.setAdUnitId(this.context.getResources().getString(R.string.ad_unit_budget));
        adsHolder.adView.addView(adView);
        adView.setAdSize(AdsHelper.getAdSize((Activity) this.context));
        adView.loadAd(new AdRequest.Builder().build());
        adView.setAdListener(new AdListener() { // from class: com.ktwapps.walletmanager.Adapter.BudgetDetailAdapter.1
            @Override // com.google.android.gms.ads.AdListener
            public void onAdLoaded() {
                BudgetDetailAdapter.this.isAdsLoaded = true;
                BudgetDetailAdapter.this.notifyDataSetChanged();
            }
        });
        return adsHolder;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType != 1) {
            if (itemViewType != 3) {
                if (itemViewType == 0) {
                    ((AdsHolder) holder).adView.setVisibility(this.isAdsLoaded ? 0 : 8);
                    return;
                }
                return;
            }
            ItemHolder itemHolder = (ItemHolder) holder;
            BudgetTrans budgetTrans = this.list.get(position - (this.isPremium ? 3 : 2));
            String color = budgetTrans.getColor() == null ? "#A7A9AB" : budgetTrans.getColor();
            String name = budgetTrans.getName(this.context);
            String string = budgetTrans.getTrans() > 1 ? this.context.getResources().getString(R.string.total_transactions, Integer.valueOf(budgetTrans.getTrans())) : this.context.getResources().getString(R.string.total_transaction, Integer.valueOf(budgetTrans.getTrans()));
            String beautifyAmount = Helper.getBeautifyAmount(SharePreferenceHelper.getAccountSymbol(this.context), budgetTrans.getAmount());
            itemHolder.imageView.setImageResource(DataHelper.getCategoryIcons().get(budgetTrans.getIcon()).intValue());
            itemHolder.amountLabel.setText(beautifyAmount);
            itemHolder.amountLabel.setTextColor(this.context.getResources().getColor(R.color.expense));
            itemHolder.nameLabel.setText(name);
            itemHolder.transLabel.setText(string);
            if (Build.VERSION.SDK_INT >= 29) {
                itemHolder.colorView.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor(color), BlendMode.SRC_OVER));
            } else {
                itemHolder.colorView.getBackground().setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_OVER);
            }
            if (position - (this.isPremium ? 2 : 1) == this.list.size()) {
                itemHolder.divider.setVisibility(0);
                return;
            } else {
                itemHolder.divider.setVisibility(8);
                return;
            }
        }
        HeaderHolder headerHolder = (HeaderHolder) holder;
        Budget budget = this.budget;
        if (budget != null) {
            long amount = budget.getAmount() - this.budget.getSpent();
            String accountSymbol = SharePreferenceHelper.getAccountSymbol(this.context);
            String name2 = this.budget.getName();
            int i = (amount > 0L ? 1 : (amount == 0L ? 0 : -1));
            if (i < 0) {
                amount = -amount;
            }
            String beautifyAmount2 = Helper.getBeautifyAmount(accountSymbol, amount);
            String beautifyAmount3 = Helper.getBeautifyAmount(accountSymbol, this.budget.getSpent());
            String beautifyAmount4 = Helper.getBeautifyAmount(accountSymbol, this.budget.getAmount());
            String string2 = this.budget.getCategoryId() == 0 ? this.context.getResources().getString(R.string.all_category) : TextUtils.join(", ", this.budget.getCategories(this.context).split(","));
            String color2 = this.budget.getColor();
            String formattedDate = BudgetHelper.getFormattedDate(this.context, this.budget.getPeriod(), this.date, this.budget.getStartDate());
            String rangeDate = BudgetHelper.getRangeDate(this.context, this.budget.getPeriod(), this.date, this.budget.getStartDate());
            String beautifyAmount5 = Helper.getBeautifyAmount(accountSymbol, BudgetHelper.getAverage(this.context, this.budget.getPeriod(), this.date, this.budget.getStartDate(), this.budget.getSpent()));
            String beautifyAmount6 = Helper.getBeautifyAmount(accountSymbol, BudgetHelper.getRecommended(this.context, this.budget.getPeriod(), this.date, this.budget.getStartDate(), this.budget.getAmount() - this.budget.getSpent()));
            headerHolder.percentLabel.setText(Helper.getProgressDoubleValue(this.budget.getAmount(), this.budget.getSpent()));
            headerHolder.remainTitleLabel.setText(i < 0 ? R.string.overspent : R.string.left);
            headerHolder.recommendLabel.setText(beautifyAmount6);
            headerHolder.averageLabel.setText(beautifyAmount5);
            headerHolder.nameLabel.setText(name2);
            headerHolder.spentLabel.setText(beautifyAmount3);
            headerHolder.remainLabel.setText(beautifyAmount2);
            headerHolder.categoryLabel.setText(string2);
            headerHolder.budgetLabel.setText(beautifyAmount4);
            headerHolder.periodLabel.setText(formattedDate);
            headerHolder.timeLabel.setText(rangeDate);
            TextView textView = headerHolder.spentLabel;
            Context context = this.context;
            textView.setTextColor(i < 0 ? context.getResources().getColor(R.color.expense) : Helper.getAttributeColor(context, R.attr.primaryTextColor));
            headerHolder.progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor(color2)));
            headerHolder.progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor(color2)));
            headerHolder.progressBar.setProgress(Helper.getProgressValue(this.budget.getAmount(), this.budget.getSpent()));
            setLineChart(headerHolder.lineChart);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int position) {
        int offset = isPremium ? 1 : 0;
        if (position == offset) {
            return 1;
        } else if (position == offset + 1) {
            return 2;
        } else {
            return isPremium && position == 0 ? 0 : 3;
        }
    }

    /* loaded from: classes3.dex */
    public class AdsHolder extends RecyclerView.ViewHolder {
        FrameLayout adView;

        AdsHolder(View itemView) {
            super(itemView);
            this.adView = (FrameLayout) itemView.findViewById(R.id.adView);
        }
    }

    /* loaded from: classes3.dex */
    public class HeaderHolder extends RecyclerView.ViewHolder {
        TextView averageLabel;
        TextView budgetLabel;
        TextView categoryLabel;
        LineChart lineChart;
        TextView nameLabel;
        TextView percentLabel;
        TextView periodLabel;
        ProgressBar progressBar;
        TextView recommendLabel;
        TextView remainLabel;
        TextView remainTitleLabel;
        TextView spentLabel;
        TextView timeLabel;

        HeaderHolder(View itemView) {
            super(itemView);
            this.percentLabel = (TextView) itemView.findViewById(R.id.percentLabel);
            this.remainTitleLabel = (TextView) itemView.findViewById(R.id.remainTitleLabel);
            this.nameLabel = (TextView) itemView.findViewById(R.id.nameLabel);
            this.spentLabel = (TextView) itemView.findViewById(R.id.spentLabel);
            this.remainLabel = (TextView) itemView.findViewById(R.id.remainLabel);
            this.categoryLabel = (TextView) itemView.findViewById(R.id.categoryLabel);
            this.budgetLabel = (TextView) itemView.findViewById(R.id.budgetLabel);
            this.periodLabel = (TextView) itemView.findViewById(R.id.periodLabel);
            this.timeLabel = (TextView) itemView.findViewById(R.id.timeLabel);
            this.recommendLabel = (TextView) itemView.findViewById(R.id.recommendLabel);
            this.averageLabel = (TextView) itemView.findViewById(R.id.averageLabel);
            this.progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            this.lineChart = (LineChart) itemView.findViewById(R.id.lineChart);
        }
    }

    /* loaded from: classes3.dex */
    public class TitleHolder extends RecyclerView.ViewHolder {
        TitleHolder(View itemView) {
            super(itemView);
        }
    }

    /* loaded from: classes3.dex */
    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView amountLabel;
        ConstraintLayout colorView;
        View divider;
        ImageView imageView;
        TextView nameLabel;
        TextView transLabel;

        ItemHolder(View itemView) {
            super(itemView);
            this.nameLabel = (TextView) itemView.findViewById(R.id.nameLabel);
            this.transLabel = (TextView) itemView.findViewById(R.id.transLabel);
            this.amountLabel = (TextView) itemView.findViewById(R.id.amountLabel);
            this.colorView = (ConstraintLayout) itemView.findViewById(R.id.colorView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.divider = itemView.findViewById(R.id.divider);
            itemView.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (BudgetDetailAdapter.this.listener != null) {
                BudgetDetailAdapter.this.listener.OnItemClick(view, getLayoutPosition() - (BudgetDetailAdapter.this.isPremium ? 3 : 2));
            }
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        if (this.list.size() > 0) {
            return (this.isPremium ? 3 : 2) + this.list.size();
        }
        return this.isPremium ? 2 : 1;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private void setLineChart(LineChart chart) {
        chart.getDescription().setEnabled(false);
        chart.setScaleEnabled(false);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        MarkerViewBudget markerViewBudget = new MarkerViewBudget(this.context, R.layout.marker_view_budget);
        markerViewBudget.setWidth(chart.getWidth());
        markerViewBudget.setDate(BudgetHelper.getBudgetFirstDate(this.context, this.date, this.budget.getPeriod(), this.budget.getStartDate()));
        chart.setMarker(markerViewBudget);
        XAxis xAxis = chart.getXAxis();
        xAxis.setAxisLineColor(Helper.getAttributeColor(this.context, R.attr.secondaryTextColor));
        xAxis.setTextColor(Helper.getAttributeColor(this.context, R.attr.primaryTextColor));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        YAxis axisLeft = chart.getAxisLeft();
        axisLeft.setLabelCount(6);
        axisLeft.setAxisLineColor(Helper.getAttributeColor(this.context, R.attr.secondaryTextColor));
        axisLeft.setTextColor(Helper.getAttributeColor(this.context, R.attr.primaryTextColor));
        axisLeft.enableGridDashedLine(10.0f, 10.0f, 0.0f);
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeft.setDrawAxisLine(false);
        axisLeft.setAxisMinimum(0.0f);
        Budget budget = this.budget;
        if (budget != null) {
            float amount = (float) ((budget.getAmount() + (this.budget.getAmount() / 5)) / 100);
            if (this.budget.getAmount() - this.budget.getSpent() < 0) {
                amount = (float) ((this.budget.getSpent() + (this.budget.getSpent() / 5)) / 100);
            }
            axisLeft.setAxisMaximum(amount);
        }
        YAxis axisRight = chart.getAxisRight();
        axisRight.setDrawAxisLine(false);
        axisRight.setDrawGridLines(false);
        axisRight.setDrawLabels(false);
        LimitLine limitLine = new LimitLine((float) (this.budget.getAmount() / 100), "");
        limitLine.setLineWidth(1.0f);
        limitLine.setLineColor(this.context.getResources().getColor(R.color.expense));
        limitLine.setTextColor(this.context.getResources().getColor(R.color.expense));
        limitLine.enableDashedLine(10.0f, 5.0f, 0.0f);
        limitLine.setLabel(Helper.getBeautifyAmount(SharePreferenceHelper.getAccountSymbol(this.context), this.budget.getAmount()));
        limitLine.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        limitLine.setTypeface(Typeface.DEFAULT);
        limitLine.setTextSize(11.0f);
        axisLeft.removeAllLimitLines();
        axisLeft.addLimitLine(limitLine);
        chart.getLegend().setEnabled(false);
        setLineData(chart, this.date, this.budget.getPeriod());
    }

    private void setLineData(LineChart chart, final Date date, final int period) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        if (chart.getData() != null && ((LineData) chart.getData()).getEntryCount() != 0) {
            ((LineData) chart.getData()).clearValues();
            chart.notifyDataSetChanged();
            chart.invalidate();
        }
        final int chartMaximum = BudgetHelper.getChartMaximum(date, period, this.budget.getStartDate());
        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1.0f);
        xAxis.setLabelCount(chartMaximum, false);
        xAxis.setValueFormatter(new ValueFormatter() { // from class: com.ktwapps.walletmanager.Adapter.BudgetDetailAdapter.2
            @Override // com.github.mikephil.charting.formatter.ValueFormatter
            public String getFormattedValue(float value) {
                int round = Math.round(value);
                String bestDateTimePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "MM-dd");
                if (round == 0) {
                    return new SimpleDateFormat(bestDateTimePattern, Locale.getDefault()).format(Long.valueOf(BudgetHelper.getBudgetFirstDate(BudgetDetailAdapter.this.context, date, period, BudgetDetailAdapter.this.budget.getStartDate()).getTime()));
                }
                return round == chartMaximum + (-1) ? new SimpleDateFormat(bestDateTimePattern, Locale.getDefault()).format(Long.valueOf(BudgetHelper.getBudgetLastDate(BudgetDetailAdapter.this.context, date, period, BudgetDetailAdapter.this.budget.getStartDate()).getTime())) : "";
            }
        });
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(BudgetHelper.getBudgetFirstDate(this.context, date, period, this.budget.getStartDate()));
        int i = 0;
        float f = 0.0f;
        for (int i2 = 0; i2 < chartMaximum; i2++) {
            if (this.statsList.size() > i) {
                BudgetStats budgetStats = this.statsList.get(i);
                if (calendar.get(2) == budgetStats.getMonth() - 1 && calendar.get(5) == budgetStats.getDay()) {
                    f += ((float) (-budgetStats.getAmount())) / 100.0f;
                    i++;
                }
            }
            if (calendar.getTime().getTime() > Calendar.getInstance().getTime().getTime() && (calendar.get(6) != Calendar.getInstance().get(6) || calendar.get(1) != Calendar.getInstance().get(1))) {
                if (arrayList2.size() == 0 && arrayList.size() != 0) {
                    arrayList2.add(new Entry(i2 - 1, ((Entry) arrayList.get(arrayList.size() - 1)).getY()));
                }
                arrayList2.add(new Entry(i2, f));
            } else {
                arrayList.add(new Entry(i2, f));
            }
            calendar.add(5, 1);
        }
        if (chart.getData() != null && ((LineData) chart.getData()).getDataSetCount() > 0) {
            LineDataSet lineDataSet = (LineDataSet) ((LineData) chart.getData()).getDataSetByIndex(0);
            lineDataSet.setValues(arrayList);
            lineDataSet.notifyDataSetChanged();
            LineDataSet lineDataSet2 = (LineDataSet) ((LineData) chart.getData()).getDataSetByIndex(1);
            lineDataSet2.setValues(arrayList);
            lineDataSet2.notifyDataSetChanged();
            ((LineData) chart.getData()).notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            LineDataSet lineDataSet3 = new LineDataSet(arrayList, "DataSet 1");
            LineDataSet lineDataSet4 = new LineDataSet(arrayList2, "DataSet 1");
            lineDataSet3.setDrawIcons(false);
            lineDataSet4.setDrawIcons(false);
            lineDataSet4.enableDashedLine(10.0f, 5.0f, 0.0f);
            lineDataSet3.setColor(Color.parseColor(this.budget.getColor()));
            lineDataSet3.setFillColor(Color.parseColor("#80" + this.budget.getColor().substring(1)));
            lineDataSet4.setColor(Color.parseColor("#AAAAAA"));
            lineDataSet4.setFillColor(Color.parseColor("#80AAAAAA"));
            lineDataSet3.setLineWidth(1.0f);
            lineDataSet4.setLineWidth(1.0f);
            lineDataSet3.setDrawCircles(false);
            lineDataSet4.setDrawCircles(false);
            lineDataSet3.setDrawValues(false);
            lineDataSet4.setDrawValues(false);
            lineDataSet3.setDrawFilled(true);
            lineDataSet4.setDrawFilled(true);
            lineDataSet3.setHighLightColor(Helper.getAttributeColor(this.context, R.attr.primaryDarkTextColor));
            lineDataSet3.setHighlightLineWidth(1.0f);
            lineDataSet3.enableDashedHighlightLine(15.0f, 5.0f, 0.0f);
            lineDataSet3.setDrawHorizontalHighlightIndicator(false);
            lineDataSet4.setHighLightColor(Helper.getAttributeColor(this.context, R.attr.primaryDarkTextColor));
            lineDataSet4.setHighlightLineWidth(1.0f);
            lineDataSet4.enableDashedHighlightLine(15.0f, 5.0f, 0.0f);
            lineDataSet4.setDrawHorizontalHighlightIndicator(false);
            ArrayList arrayList3 = new ArrayList();
            arrayList3.add(lineDataSet3);
            arrayList3.add(lineDataSet4);
            chart.setData(new LineData(arrayList3));
        }
        float f2 = chartMaximum - 1;
        chart.setVisibleXRangeMinimum(f2);
        chart.setVisibleXRangeMaximum(f2);
    }
}
