package com.expance.merapaisa.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.expance.merapaisa.Model.DailyTrans;
import com.expance.merapaisa.Model.SubcategoryStats;
import com.expance.merapaisa.Model.Trans;
import com.expance.merapaisa.Model.TransList;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Utility.DateHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/* loaded from: classes3.dex */
public class TransactionPieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    OnItemClickListener listener;
    String symbol;
    public List<TransList> mainTransList = new ArrayList();
    public List<SubcategoryStats> stats = new ArrayList();
    int mode = 1;

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void OnItemClick(View view, int position);

        void OnItemLongClick(View view, int position);
    }

    public TransactionPieAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return 2;
        }

        if (position <= stats.size()) {
            return 3;
        }

        return mainTransList.get((position - stats.size()) - 1).isDailyHeader() ? 0 : 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new HeaderViewHolder(this.inflater.inflate(R.layout.list_transaction_header, parent, false));
            case 1:
                return new TransactionViewHolder(this.inflater.inflate(R.layout.list_transaction, parent, false));
            case 2:
                return new OverallViewHolder(this.inflater.inflate(R.layout.list_transaction_pie, parent, false));
            default:
                return new SubcategoryViewHolder(this.inflater.inflate(R.layout.list_stat_subcategory, parent, false));
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);


        if (itemViewType == 0) {
            DailyTrans dailyTrans = this.mainTransList.get((position - this.stats.size()) - 1).getDailyTrans();

            if (dailyTrans == null) {
                Log.e("@@@@@", "onBindViewHolder: " + "dayTrans null" );
            }

            String str = this.symbol;
            if (str == null) {
                str = SharePreferenceHelper.getAccountSymbol(this.context);
            }

            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            Date dateTime = dailyTrans.getDateTime();
            String beautifyAmount = Helper.getBeautifyAmount(str, dailyTrans.getAmount());
            String bestDateTimePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "MMM yyyy");
            headerViewHolder.dayLabel.setText(new SimpleDateFormat("dd", Locale.getDefault()).format(dateTime));
            headerViewHolder.monthLabel.setText(new SimpleDateFormat(bestDateTimePattern, Locale.getDefault()).format(dateTime));
            headerViewHolder.amountLabel.setText(beautifyAmount);
            if (!SharePreferenceHelper.isFutureBalanceOn(this.context)) {
                headerViewHolder.weekLabel.setText(new SimpleDateFormat("EEEE", Locale.getDefault()).format(dateTime));
            } else if (DateHelper.isDatePast(dateTime)) {
                headerViewHolder.weekLabel.setText(new SimpleDateFormat("EEEE", Locale.getDefault()).format(dateTime));
            } else {
                headerViewHolder.weekLabel.setText(R.string.future);
            }

             // dailyTrans Header
        }else if (itemViewType == 1) {

            Trans trans = this.mainTransList.get((position - this.stats.size()) - 1).getTrans();

            if (trans == null) {
                Log.e("@@@@@", "trans: onBindViewHolder: " + "null");
                return;
            }
            String str4 = DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(trans.getCurrency() != null ? trans.getCurrency() : null));
            String media = trans.getMedia();
            String note = trans.getNote(this.context);
            String color = trans.getColor() == null ? "#A7A9AB" : trans.getColor();
            String wallet = trans.getWallet();
            int type = trans.getType();
            Date dateTime2 = trans.getDateTime();
            String beautifyAmount2 = Helper.getBeautifyAmount(str4, trans.getAmount());
            boolean z = 0 > trans.getAmount();
            String format = new SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), DateFormat.is24HourFormat(this.context) ? "kk:mm" : "hh:mm aa"), Locale.getDefault()).format(dateTime2);
            TransactionViewHolder transactionViewHolder = (TransactionViewHolder) holder;
            if (media == null || media.trim().length() == 0) {
                transactionViewHolder.imageView1.setVisibility(8);
                transactionViewHolder.imageView2.setVisibility(8);
                transactionViewHolder.imageView3.setVisibility(8);
            } else {
                String[] split = media.split(",");
                if (split.length == 1) {
                    transactionViewHolder.imageView1.setVisibility(0);
                    transactionViewHolder.imageView2.setVisibility(8);
                    transactionViewHolder.imageView3.setVisibility(8);
                    Picasso.get().load(new File(split[0])).centerCrop().resize(100, 100).into(transactionViewHolder.imageView1);
                } else if (split.length == 2) {
                    transactionViewHolder.imageView1.setVisibility(0);
                    transactionViewHolder.imageView2.setVisibility(0);
                    transactionViewHolder.imageView3.setVisibility(8);
                    Picasso.get().load(new File(split[0])).centerCrop().resize(100, 100).into(transactionViewHolder.imageView1);
                    Picasso.get().load(new File(split[1])).centerCrop().resize(100, 100).into(transactionViewHolder.imageView2);
                } else if (split.length == 3) {
                    transactionViewHolder.imageView1.setVisibility(0);
                    transactionViewHolder.imageView2.setVisibility(0);
                    transactionViewHolder.imageView3.setVisibility(0);
                    Picasso.get().load(new File(split[0])).centerCrop().resize(100, 100).into(transactionViewHolder.imageView1);
                    Picasso.get().load(new File(split[1])).centerCrop().resize(100, 100).into(transactionViewHolder.imageView2);
                    Picasso.get().load(new File(split[2])).centerCrop().resize(100, 100).into(transactionViewHolder.imageView3);
                }
            }
            if (type == 2) {
                wallet = wallet + this.context.getResources().getString(R.string.transfer_to) + trans.getTransferWallet();
                transactionViewHolder.imageView.setImageResource(R.drawable.transfer);
            } else {
                transactionViewHolder.imageView.setImageResource(DataHelper.getCategoryIcons().get(trans.getIcon()).intValue());
            }
            transactionViewHolder.amountLabel.setText(beautifyAmount2);
            TextView textView = transactionViewHolder.amountLabel;
            Resources resources = this.context.getResources();
            textView.setTextColor(z ? resources.getColor(R.color.expense) : resources.getColor(R.color.income));
            transactionViewHolder.nameLabel.setText(note);
            if (Build.VERSION.SDK_INT >= 29) {
                transactionViewHolder.colorView.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor(color), BlendMode.SRC_OVER));
            } else {
                transactionViewHolder.colorView.getBackground().setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_OVER);
            }
            transactionViewHolder.detailLabel.setText(wallet);
            transactionViewHolder.timeLabel.setText(format);
            transactionViewHolder.divider.setVisibility(8);
            transactionViewHolder.recurringImage.setVisibility(trans.isRecurring() ? 0 : 8);

        }else {
            if (itemViewType == 0) {

                String str = this.symbol;
                if (str == null) {
                    str = SharePreferenceHelper.getAccountSymbol(this.context);
                }
//            DailyTrans dailyTrans = this.list.get((position - this.stats.size()) - 1).getDailyTrans();
                Trans trans = this.mainTransList.get((position - this.stats.size()) - 1).getTrans();

                HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                Date dateTime = trans.getDateTime();
                String beautifyAmount = Helper.getBeautifyAmount(str, trans.getAmount());
                String bestDateTimePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "MMM yyyy");
                headerViewHolder.dayLabel.setText(new SimpleDateFormat("dd", Locale.getDefault()).format(dateTime));
                headerViewHolder.monthLabel.setText(new SimpleDateFormat(bestDateTimePattern, Locale.getDefault()).format(dateTime));
                headerViewHolder.amountLabel.setText(beautifyAmount);
                if (!SharePreferenceHelper.isFutureBalanceOn(this.context)) {
                    headerViewHolder.weekLabel.setText(new SimpleDateFormat("EEEE", Locale.getDefault()).format(dateTime));
                } else if (DateHelper.isDatePast(dateTime)) {
                    headerViewHolder.weekLabel.setText(new SimpleDateFormat("EEEE", Locale.getDefault()).format(dateTime));
                } else {
                    headerViewHolder.weekLabel.setText(R.string.future);
                }
            } else if (itemViewType != 1) {
                if (itemViewType == 2) {
                    OverallViewHolder overallViewHolder = (OverallViewHolder) holder;
                    String str2 = this.symbol;
                    if (str2 == null) {
                        str2 = SharePreferenceHelper.getAccountSymbol(this.context);
                    }
                    overallViewHolder.expenseLabel.setText(Helper.getBeautifyAmount(str2, getTotalAmount()));
                    overallViewHolder.expenseLabel.setTextColor(ContextCompat.getColor(this.context, this.mode == 1 ? R.color.expense : R.color.income));
                    overallViewHolder.expenseTitleLabel.setText(R.string.all);
                    return;
                }
                SubcategoryStats subcategoryStats = this.stats.get(position - 1);
                SubcategoryViewHolder subcategoryViewHolder = (SubcategoryViewHolder) holder;
                String str3 = this.symbol;
                if (str3 == null) {
                    str3 = SharePreferenceHelper.getAccountSymbol(this.context);
                }
                subcategoryViewHolder.percentLabel.setText(subcategoryStats.getPercent() + "%");
                subcategoryViewHolder.amountLabel.setText(Helper.getBeautifyAmount(str3, subcategoryStats.getAmount()));
                subcategoryViewHolder.titleLabel.setText(subcategoryStats.getName());
            } else {
                Trans trans = this.mainTransList.get((position - this.stats.size()) - 1).getTrans();

                if (trans == null) {
                    Log.e("@@@", "onBindViewHolder: " + "null");
                    return;
                }
                String str4 = DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(trans.getCurrency() != null ? trans.getCurrency() : null));
                String media = trans.getMedia();
                String note = trans.getNote(this.context);
                String color = trans.getColor() == null ? "#A7A9AB" : trans.getColor();
                String wallet = trans.getWallet();
                int type = trans.getType();
                Date dateTime2 = trans.getDateTime();
                String beautifyAmount2 = Helper.getBeautifyAmount(str4, trans.getAmount());
                boolean z = 0 > trans.getAmount();
                String format = new SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), DateFormat.is24HourFormat(this.context) ? "kk:mm" : "hh:mm aa"), Locale.getDefault()).format(dateTime2);
                TransactionViewHolder transactionViewHolder = (TransactionViewHolder) holder;
                if (media == null || media.trim().length() == 0) {
                    transactionViewHolder.imageView1.setVisibility(8);
                    transactionViewHolder.imageView2.setVisibility(8);
                    transactionViewHolder.imageView3.setVisibility(8);
                } else {
                    String[] split = media.split(",");
                    if (split.length == 1) {
                        transactionViewHolder.imageView1.setVisibility(0);
                        transactionViewHolder.imageView2.setVisibility(8);
                        transactionViewHolder.imageView3.setVisibility(8);
                        Picasso.get().load(new File(split[0])).centerCrop().resize(100, 100).into(transactionViewHolder.imageView1);
                    } else if (split.length == 2) {
                        transactionViewHolder.imageView1.setVisibility(0);
                        transactionViewHolder.imageView2.setVisibility(0);
                        transactionViewHolder.imageView3.setVisibility(8);
                        Picasso.get().load(new File(split[0])).centerCrop().resize(100, 100).into(transactionViewHolder.imageView1);
                        Picasso.get().load(new File(split[1])).centerCrop().resize(100, 100).into(transactionViewHolder.imageView2);
                    } else if (split.length == 3) {
                        transactionViewHolder.imageView1.setVisibility(0);
                        transactionViewHolder.imageView2.setVisibility(0);
                        transactionViewHolder.imageView3.setVisibility(0);
                        Picasso.get().load(new File(split[0])).centerCrop().resize(100, 100).into(transactionViewHolder.imageView1);
                        Picasso.get().load(new File(split[1])).centerCrop().resize(100, 100).into(transactionViewHolder.imageView2);
                        Picasso.get().load(new File(split[2])).centerCrop().resize(100, 100).into(transactionViewHolder.imageView3);
                    }
                }
                if (type == 2) {
                    wallet = wallet + this.context.getResources().getString(R.string.transfer_to) + trans.getTransferWallet();
                    transactionViewHolder.imageView.setImageResource(R.drawable.transfer);
                } else {
                    transactionViewHolder.imageView.setImageResource(DataHelper.getCategoryIcons().get(trans.getIcon()).intValue());
                }
                transactionViewHolder.amountLabel.setText(beautifyAmount2);
                TextView textView = transactionViewHolder.amountLabel;
                Resources resources = this.context.getResources();
                textView.setTextColor(z ? resources.getColor(R.color.expense) : resources.getColor(R.color.income));
                transactionViewHolder.nameLabel.setText(note);
                if (Build.VERSION.SDK_INT >= 29) {
                    transactionViewHolder.colorView.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor(color), BlendMode.SRC_OVER));
                } else {
                    transactionViewHolder.colorView.getBackground().setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_OVER);
                }
                transactionViewHolder.detailLabel.setText(wallet);
                transactionViewHolder.timeLabel.setText(format);
                transactionViewHolder.divider.setVisibility(8);
                transactionViewHolder.recurringImage.setVisibility(trans.isRecurring() ? 0 : 8);
            }
        }
    }

    private long getTotalAmount() {
        long j = 0;
        for (TransList transList : this.mainTransList) {
            if (transList.isDailyHeader()) {
                j += transList.getDailyTrans().getAmount();
            }
        }
        return j;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        if (this.mainTransList.size() == 0) {
            return 0;
        }
        return this.mainTransList.size() + this.stats.size() + 1;
    }

    public void setMainTransList(List<TransList> mainTransList) {
        this.mainTransList = mainTransList;
        notifyDataSetChanged();
    }

    public void setSubcategoryStat(List<SubcategoryStats> stat) {
        ArrayList arrayList = new ArrayList();
        for (SubcategoryStats subcategoryStats : stat) {
            if (subcategoryStats.getId() != 0) {
                arrayList.add(subcategoryStats);
            }
        }
        this.stats = arrayList;
        notifyDataSetChanged();
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /* loaded from: classes3.dex */
    private class SubcategoryViewHolder extends RecyclerView.ViewHolder {
        TextView amountLabel;
        TextView percentLabel;
        TextView titleLabel;

        public SubcategoryViewHolder(View itemView) {
            super(itemView);
            this.titleLabel = (TextView) itemView.findViewById(R.id.titleLabel);
            this.amountLabel = (TextView) itemView.findViewById(R.id.amountLabel);
            this.percentLabel = (TextView) itemView.findViewById(R.id.percentLabel);
        }
    }

    /* loaded from: classes3.dex */
    private class OverallViewHolder extends RecyclerView.ViewHolder {
        TextView expenseLabel;
        TextView expenseTitleLabel;

        OverallViewHolder(View itemView) {
            super(itemView);
            this.expenseTitleLabel = (TextView) itemView.findViewById(R.id.expenseTitleLabel);
            this.expenseLabel = (TextView) itemView.findViewById(R.id.expenseLabel);
        }
    }

    /* loaded from: classes3.dex */
    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView amountLabel;
        TextView dayLabel;
        TextView monthLabel;
        TextView weekLabel;

        HeaderViewHolder(View itemView) {
            super(itemView);
            this.dayLabel = (TextView) itemView.findViewById(R.id.dayLabel);
            this.weekLabel = (TextView) itemView.findViewById(R.id.weekLabel);
            this.monthLabel = (TextView) itemView.findViewById(R.id.monthLabel);
            this.amountLabel = (TextView) itemView.findViewById(R.id.amountLabel);
        }
    }

    /* loaded from: classes3.dex */
    private class TransactionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView amountLabel;
        View colorView;
        TextView detailLabel;
        View divider;
        ImageView imageView;
        ImageView imageView1;
        ImageView imageView2;
        ImageView imageView3;
        TextView nameLabel;
        ImageView recurringImage;
        TextView timeLabel;

        TransactionViewHolder(View itemView) {
            super(itemView);
            this.nameLabel = (TextView) itemView.findViewById(R.id.nameLabel);
            this.detailLabel = (TextView) itemView.findViewById(R.id.detailLabel);
            this.colorView = itemView.findViewById(R.id.colorView);
            this.amountLabel = (TextView) itemView.findViewById(R.id.amountLabel);
            this.timeLabel = (TextView) itemView.findViewById(R.id.timeLabel);
            this.imageView1 = (ImageView) itemView.findViewById(R.id.image1);
            this.imageView2 = (ImageView) itemView.findViewById(R.id.image2);
            this.imageView3 = (ImageView) itemView.findViewById(R.id.image3);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.divider = itemView.findViewById(R.id.divider);
            this.recurringImage = (ImageView) itemView.findViewById(R.id.repeatImage);
            this.imageView1.setTag(0);
            this.imageView2.setTag(1);
            this.imageView3.setTag(2);
            this.imageView1.setOnClickListener(this);
            this.imageView2.setOnClickListener(this);
            this.imageView3.setOnClickListener(this);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            TransactionPieAdapter.this.listener.OnItemClick(view, (getLayoutPosition() - TransactionPieAdapter.this.stats.size()) - 1);
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            TransactionPieAdapter.this.listener.OnItemLongClick(view, (getLayoutPosition() - TransactionPieAdapter.this.stats.size()) - 1);
            return false;
        }
    }
}
