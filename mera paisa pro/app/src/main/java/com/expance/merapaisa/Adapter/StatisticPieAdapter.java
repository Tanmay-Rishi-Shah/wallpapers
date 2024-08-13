package com.expance.merapaisa.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.expance.merapaisa.Model.Stats;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import com.expance.merapaisa.Widget.CustomPieChartRenderer;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* loaded from: classes3.dex */
public class StatisticPieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    List<Stats> list = new ArrayList();
    OnItemClickListener listener;
    int mode;
    String symbol;

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void OnItemClick(View v, int position);
    }

    public StatisticPieAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.symbol = SharePreferenceHelper.getAccountSymbol(context);
    }

    public List<Stats> getList() {
        return this.list;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setList(List<Stats> list) {
        this.list = list;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return this.list.size() == 0 ? 2 : 1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new HeaderViewHolder(this.inflater.inflate(R.layout.list_statistic_pie_header, parent, false));
        }
        if (viewType == 1) {
            return new ItemViewHolder(this.inflater.inflate(R.layout.list_statistic_pie_item, parent, false));
        }
        return new EmptyViewHolder(this.inflater.inflate(R.layout.list_statistic_pie_empty, parent, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == 0) {
            setPieChart(((HeaderViewHolder) holder).pieChart);
        } else if (itemViewType == 1) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());
            numberFormat.setMaximumFractionDigits(1);
            numberFormat.setMinimumFractionDigits(1);
            Stats stats = this.list.get(position - 1);
            String color = stats.getColor();
            String name = stats.getName(this.context);
            String string = stats.getTrans() > 1 ? this.context.getResources().getString(R.string.total_transactions, Integer.valueOf(stats.getTrans())) : this.context.getResources().getString(R.string.total_transaction, Integer.valueOf(stats.getTrans()));
            String beautifyAmount = Helper.getBeautifyAmount(this.symbol, stats.getAmount());
            itemViewHolder.nameLabel.setText(name);
            itemViewHolder.transLabel.setText(string);
            itemViewHolder.amountLabel.setText(beautifyAmount);
            itemViewHolder.detailLabel.setText(numberFormat.format(stats.getPercent()) + "%");
            if (stats.getId() == 0) {
                itemViewHolder.imageView.setImageResource(R.drawable.transfer);
            } else {
                itemViewHolder.imageView.setImageResource(DataHelper.getCategoryIcons().get(stats.getIcon()).intValue());
            }
            itemViewHolder.amountLabel.setTextColor(ContextCompat.getColor(this.context, stats.getAmount() >= 0 ? R.color.income : R.color.expense));
            if (Build.VERSION.SDK_INT >= 29) {
                itemViewHolder.colorView.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor(color), BlendMode.SRC_OVER));
            } else {
                itemViewHolder.colorView.getBackground().setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_OVER);
            }
            itemViewHolder.divider.setVisibility(position == this.list.size() ? 8 : 0);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        if (this.list.size() == 0) {
            return 2;
        }
        return this.list.size() + 1;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /* loaded from: classes3.dex */
    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        PieChart pieChart;
        ConstraintLayout pieStat1;
        ConstraintLayout pieStat2;
        ConstraintLayout pieStat3;
        ConstraintLayout pieStat4;
        ConstraintLayout pieStat5;
        TextView pieStatLabel1;
        TextView pieStatLabel2;
        TextView pieStatLabel3;
        TextView pieStatLabel4;
        TextView pieStatLabel5;
        TextView pieStatPercentLabel1;
        TextView pieStatPercentLabel2;
        TextView pieStatPercentLabel3;
        TextView pieStatPercentLabel4;
        TextView pieStatPercentLabel5;
        View pieStatView1;
        View pieStatView2;
        View pieStatView3;
        View pieStatView4;
        View pieStatView5;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            this.pieChart = (PieChart) itemView.findViewById(R.id.pieChart);
            this.pieStat1 = (ConstraintLayout) itemView.findViewById(R.id.pieStat1);
            this.pieStat2 = (ConstraintLayout) itemView.findViewById(R.id.pieStat2);
            this.pieStat3 = (ConstraintLayout) itemView.findViewById(R.id.pieStat3);
            this.pieStat4 = (ConstraintLayout) itemView.findViewById(R.id.pieStat4);
            this.pieStat5 = (ConstraintLayout) itemView.findViewById(R.id.pieStat5);
            this.pieStatView1 = itemView.findViewById(R.id.pieStatView1);
            this.pieStatView2 = itemView.findViewById(R.id.pieStatView2);
            this.pieStatView3 = itemView.findViewById(R.id.pieStatView3);
            this.pieStatView4 = itemView.findViewById(R.id.pieStatView4);
            this.pieStatView5 = itemView.findViewById(R.id.pieStatView5);
            this.pieStatLabel1 = (TextView) itemView.findViewById(R.id.pieStatLabel1);
            this.pieStatLabel2 = (TextView) itemView.findViewById(R.id.pieStatLabel2);
            this.pieStatLabel3 = (TextView) itemView.findViewById(R.id.pieStatLabel3);
            this.pieStatLabel4 = (TextView) itemView.findViewById(R.id.pieStatLabel4);
            this.pieStatLabel5 = (TextView) itemView.findViewById(R.id.pieStatLabel5);
            this.pieStatPercentLabel1 = (TextView) itemView.findViewById(R.id.pieStatPercentLabel1);
            this.pieStatPercentLabel2 = (TextView) itemView.findViewById(R.id.pieStatPercentLabel2);
            this.pieStatPercentLabel3 = (TextView) itemView.findViewById(R.id.pieStatPercentLabel3);
            this.pieStatPercentLabel4 = (TextView) itemView.findViewById(R.id.pieStatPercentLabel4);
            this.pieStatPercentLabel5 = (TextView) itemView.findViewById(R.id.pieStatPercentLabel5);
        }
    }

    /* loaded from: classes3.dex */
    private class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView amountLabel;
        ConstraintLayout colorView;
        TextView detailLabel;
        View divider;
        ImageView imageView;
        TextView nameLabel;
        TextView transLabel;

        ItemViewHolder(View itemView) {
            super(itemView);
            this.colorView = (ConstraintLayout) itemView.findViewById(R.id.colorView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.nameLabel = (TextView) itemView.findViewById(R.id.nameLabel);
            this.detailLabel = (TextView) itemView.findViewById(R.id.detailLabel);
            this.amountLabel = (TextView) itemView.findViewById(R.id.amountLabel);
            this.transLabel = (TextView) itemView.findViewById(R.id.transLabel);
            this.divider = itemView.findViewById(R.id.divider);
            itemView.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (StatisticPieAdapter.this.listener != null) {
                StatisticPieAdapter.this.listener.OnItemClick(view, getLayoutPosition());
            }
        }
    }

    /* loaded from: classes3.dex */
    private class EmptyViewHolder extends RecyclerView.ViewHolder {
        EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    private void setPieChart(final PieChart pieChart) {
        pieChart.setRenderer(new CustomPieChartRenderer(this.context, pieChart, pieChart.getAnimator(), pieChart.getViewPortHandler()));
        pieChart.setCenterText(getCenterAmount());
        pieChart.setCenterTextColor(Helper.getAttributeColor(this.context, R.attr.primaryTextColor));
        pieChart.setUsePercentValues(true);
        pieChart.setTransparentCircleRadius(0.0f);
        pieChart.setHoleRadius(70.0f);
        pieChart.setHoleColor(Helper.getAttributeColor(this.context, R.attr.secondaryBackground));
        pieChart.getDescription().setEnabled(false);
        pieChart.setRotationEnabled(true);
        pieChart.setDrawEntryLabels(false);
        pieChart.setExtraOffsets(20.0f, 20.0f, 20.0f, 20.0f);
        pieChart.getLegend().setEnabled(false);
        ArrayList arrayList = new ArrayList();
        arrayList.add(new PieEntry(100.0f, "empty"));
        PieDataSet pieDataSet = new PieDataSet(arrayList, "");
        pieDataSet.setColors(Helper.getAttributeColor(this.context, R.attr.progressBar));
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(false);
        pieData.setValueTextSize(14.0f);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextColor(-1);
        pieChart.setData(pieData);
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() { // from class: com.ktwapps.walletmanager.Adapter.StatisticPieAdapter.1
            @Override // com.github.mikephil.charting.listener.OnChartValueSelectedListener
            public void onValueSelected(Entry e, Highlight h) {
                if (StatisticPieAdapter.this.list.size() != 0) {
                    PieEntry pieEntry = (PieEntry) e;
                    pieChart.setCenterText(StatisticPieAdapter.this.getCenterAmount((long) pieEntry.getValue(), pieEntry.getLabel()));
                }
            }

            @Override // com.github.mikephil.charting.listener.OnChartValueSelectedListener
            public void onNothingSelected() {
                pieChart.setCenterText(StatisticPieAdapter.this.getCenterAmount());
            }
        });
        setPieData(pieChart);
    }

    public void setPieData(PieChart pieChart) {
        ArrayList arrayList = new ArrayList();
        if (this.list.size() > 0) {
            ArrayList arrayList2 = new ArrayList();
            for (int i = 0; i < this.list.size(); i++) {
                Stats stats = this.list.get(i);
                if (stats.getPercent() > Utils.DOUBLE_EPSILON) {
                    arrayList2.add(Integer.valueOf(Color.parseColor(stats.getColor())));
                    int i2 = (0L > stats.getAmount() ? 1 : (0L == stats.getAmount() ? 0 : -1));
                    long amount = stats.getAmount();
                    if (i2 > 0) {
                        amount = -amount;
                    }
                    arrayList.add(new PieEntry((float) amount, stats.getName(this.context)));
                }
            }
            PieDataSet pieDataSet = new PieDataSet(arrayList, "");
            pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            pieDataSet.setValueLinePart1Length(0.4f);
            pieDataSet.setValueLinePart2Length(0.2f);
            pieDataSet.setValueLinePart1OffsetPercentage(115.0f);
            pieDataSet.setValueLineWidth(1.5f);
            pieDataSet.setUsingSliceColorAsValueLineColor(true);
            pieDataSet.setColors(arrayList2);
            pieDataSet.setValueTextColors(arrayList2);
            PieData pieData = new PieData(pieDataSet);
            pieData.setDrawValues(true);
            pieData.setValueTextSize(14.0f);
            pieData.setValueFormatter(new ValueFormatter() { // from class: com.ktwapps.walletmanager.Adapter.StatisticPieAdapter.2
                @Override // com.github.mikephil.charting.formatter.ValueFormatter
                public String getFormattedValue(float value) {
                    NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());
                    numberFormat.setMaximumFractionDigits(1);
                    numberFormat.setMinimumFractionDigits(1);
                    return numberFormat.format(value) + "%";
                }
            });
            pieChart.setData(pieData);
            return;
        }
        arrayList.add(new PieEntry(100.0f, "empty"));
        PieDataSet pieDataSet2 = new PieDataSet(arrayList, "");
        pieDataSet2.setColors(Helper.getAttributeColor(this.context, R.attr.progressBar));
        PieData pieData2 = new PieData(pieDataSet2);
        pieData2.setDrawValues(false);
        pieData2.setValueTextSize(14.0f);
        pieData2.setValueFormatter(new PercentFormatter());
        pieData2.setValueTextColor(-1);
        pieChart.setData(pieData2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public SpannableString getCenterAmount(long amount, String text) {
        long j = 0;
        for (Stats stats : this.list) {
            j += stats.getAmount();
        }
        String str = this.symbol;
        if (j < 0) {
            amount = -amount;
        }
        String beautifyAmount = Helper.getBeautifyAmount(str, amount);
        SpannableString spannableString = new SpannableString(text + "\n" + beautifyAmount);
        spannableString.setSpan(new RelativeSizeSpan(1.2f), 0, spannableString.length() - beautifyAmount.length(), 0);
        if (beautifyAmount.length() <= 10) {
            spannableString.setSpan(new RelativeSizeSpan(1.7f), spannableString.length() - beautifyAmount.length(), spannableString.length(), 0);
        } else if (beautifyAmount.length() <= 15) {
            spannableString.setSpan(new RelativeSizeSpan(1.525f), spannableString.length() - beautifyAmount.length(), spannableString.length(), 0);
        } else if (beautifyAmount.length() <= 20) {
            spannableString.setSpan(new RelativeSizeSpan(1.35f), spannableString.length() - beautifyAmount.length(), spannableString.length(), 0);
        } else {
            spannableString.setSpan(new RelativeSizeSpan(1.175f), spannableString.length() - beautifyAmount.length(), spannableString.length(), 0);
        }
        return spannableString;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public SpannableString getCenterAmount() {
        Resources resources;
        int i;
        long j = 0;
        for (Stats stats : this.list) {
            j += stats.getAmount();
        }
        String beautifyAmount = Helper.getBeautifyAmount(this.symbol, j);
        StringBuilder sb = new StringBuilder();
        if (this.mode == 1) {
            resources = this.context.getResources();
            i = R.string.expense;
        } else {
            resources = this.context.getResources();
            i = R.string.income;
        }
        sb.append(resources.getString(i));
        sb.append("\n");
        sb.append(beautifyAmount);
        SpannableString spannableString = new SpannableString(sb.toString());
        spannableString.setSpan(new RelativeSizeSpan(1.2f), 0, spannableString.length() - beautifyAmount.length(), 0);
        if (beautifyAmount.length() <= 10) {
            spannableString.setSpan(new RelativeSizeSpan(1.7f), spannableString.length() - beautifyAmount.length(), spannableString.length(), 0);
        } else if (beautifyAmount.length() <= 15) {
            spannableString.setSpan(new RelativeSizeSpan(1.525f), spannableString.length() - beautifyAmount.length(), spannableString.length(), 0);
        } else if (beautifyAmount.length() <= 20) {
            spannableString.setSpan(new RelativeSizeSpan(1.35f), spannableString.length() - beautifyAmount.length(), spannableString.length(), 0);
        } else {
            spannableString.setSpan(new RelativeSizeSpan(1.175f), spannableString.length() - beautifyAmount.length(), spannableString.length(), 0);
        }
        return spannableString;
    }
}
