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

import androidx.recyclerview.widget.RecyclerView;

import com.expance.merapaisa.Model.DailyTrans;
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
public class TransactionWeeklyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    public List<TransList> list = new ArrayList();
    OnItemClickListener listener;

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void OnItemClick(View view, int position);

        void OnItemLongClick(View view, int position);
    }

    public TransactionWeeklyAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new HeaderViewHolder(this.inflater.inflate(R.layout.list_transaction_header, parent, false));
        }
        if (viewType == 1) {
            return new TransactionViewHolder(this.inflater.inflate(R.layout.list_transaction, parent, false));
        }
        return new OverallViewHolder(this.inflater.inflate(R.layout.list_transaction_weekly, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
        int var3 = this.getItemViewType(var2);
        String var9;
        String var24;
        if (var3 == 0) {
            DailyTrans var8 = ((TransList) this.list.get(var2 - 1)).getDailyTrans();
            HeaderViewHolder var7 = (HeaderViewHolder) var1;
            Date var15 = var8.getDateTime();
            var9 = Helper.getBeautifyAmount(SharePreferenceHelper.getAccountSymbol(this.context), var8.getAmount());
            var24 = DateFormat.getBestDateTimePattern(Locale.getDefault(), "MMM yyyy");
            var7.dayLabel.setText((new SimpleDateFormat("dd", Locale.getDefault())).format(var15));
            var7.monthLabel.setText((new SimpleDateFormat(var24, Locale.getDefault())).format(var15));
            var7.amountLabel.setText(var9);
            if (!SharePreferenceHelper.isFutureBalanceOn(this.context)) {
                var7.weekLabel.setText((new SimpleDateFormat("EEEE", Locale.getDefault())).format(var15));
            } else if (DateHelper.isDatePast(var15)) {
                var7.weekLabel.setText((new SimpleDateFormat("EEEE", Locale.getDefault())).format(var15));
            } else {
                var7.weekLabel.setText(2131820818);
            }
        } else {
            String var18;
            if (var3 == 1) {
                Trans var10 = ((TransList) this.list.get(var2 - 1)).getTrans();
                String var23 = var10.getCurrency();
                var24 = (String) DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(var23));
                String var13 = var10.getMedia();
                String var11 = var10.getNote(this.context);
                if (var10.getColor() == null) {
                    var23 = "#A7A9AB";
                } else {
                    var23 = var10.getColor();
                }

                var9 = var10.getWallet();
                int var4 = var10.getType();
                Date var14 = var10.getDateTime();
                String var12 = Helper.getBeautifyAmount(var24, var10.getAmount());
                boolean var20;
                if (0L > var10.getAmount()) {
                    var20 = true;
                } else {
                    var20 = false;
                }

                if (DateFormat.is24HourFormat(this.context)) {
                    var24 = "kk:mm";
                } else {
                    var24 = "hh:mm aa";
                }

                var24 = (new SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), var24), Locale.getDefault())).format(var14);
                TransactionViewHolder var28 = (TransactionViewHolder) var1;
                if (var13 != null && var13.trim().length() != 0) {
                    String[] var16 = var13.split(",");
                    if (var16.length == 1) {
                        var28.imageView1.setVisibility(0);
                        var28.imageView2.setVisibility(8);
                        var28.imageView3.setVisibility(8);
                        Picasso.get().load(new File(var16[0])).centerCrop().resize(100, 100).into(var28.imageView1);
                    } else if (var16.length == 2) {
                        var28.imageView1.setVisibility(0);
                        var28.imageView2.setVisibility(0);
                        var28.imageView3.setVisibility(8);
                        Picasso.get().load(new File(var16[0])).centerCrop().resize(100, 100).into(var28.imageView1);
                        Picasso.get().load(new File(var16[1])).centerCrop().resize(100, 100).into(var28.imageView2);
                    } else if (var16.length == 3) {
                        var28.imageView1.setVisibility(0);
                        var28.imageView2.setVisibility(0);
                        var28.imageView3.setVisibility(0);
                        Picasso.get().load(new File(var16[0])).centerCrop().resize(100, 100).into(var28.imageView1);
                        Picasso.get().load(new File(var16[1])).centerCrop().resize(100, 100).into(var28.imageView2);
                        Picasso.get().load(new File(var16[2])).centerCrop().resize(100, 100).into(var28.imageView3);
                    }
                } else {
                    var28.imageView1.setVisibility(8);
                    var28.imageView2.setVisibility(8);
                    var28.imageView3.setVisibility(8);
                }

                if (var4 == 2) {
                    StringBuilder var17 = new StringBuilder();
                    var17.append(var9);
                    var17.append(this.context.getResources().getString(2131821192));
                    var17.append(var10.getTransferWallet());
                    var18 = var17.toString();
                    var28.imageView.setImageResource(2131165730);
                } else {
                    var28.imageView.setImageResource((Integer) DataHelper.getCategoryIcons().get(var10.getIcon()));
                    var18 = var9;
                }

                var28.amountLabel.setText(var12);
                TextView var27 = var28.amountLabel;
                if (var10.getType() == 2) {
                    var3 = Helper.getAttributeColor(this.context, 2130903970);
                } else {
                    Resources var26 = this.context.getResources();
                    if (var20) {
                        var3 = 2131034246;
                    } else {
                        var3 = 2131034255;
                    }

                    var3 = var26.getColor(var3);
                }

                var27.setTextColor(var3);
                var28.nameLabel.setText(var11);
                if (Build.VERSION.SDK_INT >= 29) {
                    var28.colorView.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor(var23), BlendMode.SRC_OVER));
                } else {
                    var28.colorView.getBackground().setColorFilter(Color.parseColor(var23), PorterDuff.Mode.SRC_OVER);
                }

                var28.detailLabel.setText(var18);
                var28.timeLabel.setText(var24);
                if (var2 >= this.list.size()) {
                    var28.divider.setVisibility(0);
                } else {
                    TransList var21 = (TransList) this.list.get(var2);
                    if (!var21.isDailyHeader() && var21.getTrans() != null) {
                        var28.divider.setVisibility(8);
                    } else {
                        var28.divider.setVisibility(0);
                    }
                }

                byte var19 = 0;
                ImageView var22 = var28.recurringImage;
                if (!var10.isRecurring()) {
                    var19 = 8;
                }

                var22.setVisibility(var19);
            } else if (var3 == 2) {
                OverallViewHolder var25 = (OverallViewHolder) var1;
                long var5 = this.getTotalAmount();
                var18 = SharePreferenceHelper.getAccountSymbol(this.context);
                var25.expenseLabel.setText(Helper.getBeautifyAmount(var18, var5));
                var25.expenseLabel.setTextColor(this.context.getResources().getColor(2131034246));
            }
        }
    }

    private long getTotalAmount() {
        long j = 0;
        for (TransList transList : this.list) {
            if (transList.isDailyHeader()) {
                j += transList.getDailyTrans().getAmount();
            }
        }
        return j;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        if (this.list.size() == 0) {
            return 0;
        }
        return this.list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 2;
        }
        return this.list.get(position - 1).isDailyHeader() ? 1 : 0;
    }

    public void setList(List<TransList> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /* loaded from: classes3.dex */
    private class OverallViewHolder extends RecyclerView.ViewHolder {
        TextView expenseLabel;

        OverallViewHolder(View itemView) {
            super(itemView);
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
            TransactionWeeklyAdapter.this.listener.OnItemClick(view, getLayoutPosition() - 1);
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            TransactionWeeklyAdapter.this.listener.OnItemLongClick(view, getLayoutPosition() - 1);
            return false;
        }
    }
}
