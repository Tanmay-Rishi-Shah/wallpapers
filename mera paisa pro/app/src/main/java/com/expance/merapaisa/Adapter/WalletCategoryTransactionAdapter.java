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
public class WalletCategoryTransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    OnItemClickListener listener;
    String symbol;
    int walletId;
    public List<TransList> list = new ArrayList();
    String amount = "";

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void OnItemClick(View view, int position);

        void OnItemLongClick(View view, int position);
    }

    public WalletCategoryTransactionAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setSymbol(String s) {
        this.symbol = s;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new HeaderViewHolder(this.inflater.inflate(R.layout.list_transaction_header, parent, false));
        }
        if (viewType == 1) {
            return new TransactionViewHolder(this.inflater.inflate(R.layout.list_transaction, parent, false));
        }
        return new OverallViewHolder(this.inflater.inflate(R.layout.list_category_overview, parent, false));
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
            var24 = Helper.getBeautifyAmount(this.symbol, var8.getAmount());
            var9 = DateFormat.getBestDateTimePattern(Locale.getDefault(), "MMM yyyy");
            var7.dayLabel.setText((new SimpleDateFormat("dd", Locale.getDefault())).format(var15));
            var7.monthLabel.setText((new SimpleDateFormat(var9, Locale.getDefault())).format(var15));
            var7.amountLabel.setText(var24);
            if (!SharePreferenceHelper.isFutureBalanceOn(this.context)) {
                var7.weekLabel.setText((new SimpleDateFormat("EEEE", Locale.getDefault())).format(var15));
            } else if (!SharePreferenceHelper.isFutureBalanceOn(this.context)) {
                var7.weekLabel.setText((new SimpleDateFormat("EEEE", Locale.getDefault())).format(var15));
            } else if (DateHelper.isDatePast(var15)) {
                var7.weekLabel.setText((new SimpleDateFormat("EEEE", Locale.getDefault())).format(var15));
            } else {
                var7.weekLabel.setText(2131820818);
            }
        } else {
            long var5;
            if (var3 == 1) {
                Trans var12 = ((TransList) this.list.get(var2 - 1)).getTrans();
                String var13 = var12.getMedia();
                String var11 = var12.getNote(this.context);
                if (var12.getColor() == null) {
                    var24 = "#A7A9AB";
                } else {
                    var24 = var12.getColor();
                }

                String var10 = var12.getWallet();
                int var4 = var12.getType();
                Date var14 = var12.getDateTime();
                String var23;
                if (var4 == 2) {
                    if (var12.getWalletId() != this.walletId) {
                        var5 = var12.getTransAmount();
                        var23 = Helper.getBeautifyAmount(this.symbol, var12.getTransAmount());
                    } else {
                        var5 = -var12.getAmount();
                        var23 = Helper.getBeautifyAmount(this.symbol, -var12.getAmount());
                    }
                } else {
                    var5 = var12.getAmount();
                    var23 = Helper.getBeautifyAmount(this.symbol, var12.getAmount());
                }

                boolean var19;
                if (0L > var5) {
                    var19 = true;
                } else {
                    var19 = false;
                }

                if (DateFormat.is24HourFormat(this.context)) {
                    var9 = "kk:mm";
                } else {
                    var9 = "hh:mm aa";
                }

                var9 = (new SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), var9), Locale.getDefault())).format(var14);
                TransactionViewHolder var29 = (TransactionViewHolder) var1;
                if (var13 == null) {
                    var29.imageView1.setVisibility(8);
                    var29.imageView2.setVisibility(8);
                    var29.imageView3.setVisibility(8);
                } else {
                    String[] var16 = var13.split(",");
                    if (var16.length == 1) {
                        var29.imageView1.setVisibility(0);
                        var29.imageView2.setVisibility(8);
                        var29.imageView3.setVisibility(8);
                        Picasso.get().load(new File(var16[0])).centerCrop().resize(100, 100).into(var29.imageView1);
                    } else if (var16.length == 2) {
                        var29.imageView1.setVisibility(0);
                        var29.imageView2.setVisibility(0);
                        var29.imageView3.setVisibility(8);
                        Picasso.get().load(new File(var16[0])).centerCrop().resize(100, 100).into(var29.imageView1);
                        Picasso.get().load(new File(var16[1])).centerCrop().resize(100, 100).into(var29.imageView2);
                    } else if (var16.length == 3) {
                        var29.imageView1.setVisibility(0);
                        var29.imageView2.setVisibility(0);
                        var29.imageView3.setVisibility(0);
                        Picasso.get().load(new File(var16[0])).centerCrop().resize(100, 100).into(var29.imageView1);
                        Picasso.get().load(new File(var16[1])).centerCrop().resize(100, 100).into(var29.imageView2);
                        Picasso.get().load(new File(var16[2])).centerCrop().resize(100, 100).into(var29.imageView3);
                    }
                }

                String var18;
                if (var4 == 2) {
                    StringBuilder var17 = new StringBuilder();
                    var17.append(var10);
                    var17.append(this.context.getResources().getString(2131821192));
                    var17.append(var12.getTransferWallet());
                    var18 = var17.toString();
                    var29.imageView.setImageResource(2131165730);
                } else {
                    var29.imageView.setImageResource((Integer) DataHelper.getCategoryIcons().get(var12.getIcon()));
                    var18 = var10;
                }

                var29.amountLabel.setText(var23);
                TextView var28 = var29.amountLabel;
                Resources var25 = this.context.getResources();
                if (var19) {
                    var3 = var25.getColor(2131034246);
                } else {
                    var3 = var25.getColor(2131034255);
                }

                var28.setTextColor(var3);
                var29.nameLabel.setText(var11);
                if (Build.VERSION.SDK_INT >= 29) {
                    var29.colorView.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor(var24), BlendMode.SRC_OVER));
                } else {
                    var29.colorView.getBackground().setColorFilter(Color.parseColor(var24), PorterDuff.Mode.SRC_OVER);
                }

                var29.detailLabel.setText(var18);
                var29.timeLabel.setText(var9);
                if (var2 >= this.list.size()) {
                    var29.divider.setVisibility(0);
                } else {
                    TransList var20 = (TransList) this.list.get(var2);
                    if (!var20.isDailyHeader() && var20.getTrans() != null) {
                        var29.divider.setVisibility(8);
                    } else {
                        var29.divider.setVisibility(0);
                    }
                }
            } else {
                var2 = 2131034246;
                if (var3 == 2) {
                    OverallViewHolder var26 = (OverallViewHolder) var1;
                    var5 = this.getTotalAmount();
                    var26.expenseLabel.setText(Helper.getBeautifyAmount(this.symbol, var5));
                    TextView var27 = var26.expenseLabel;
                    Resources var21 = this.context.getResources();
                    long var30;
                    var3 = (var30 = var5 - 0L) == 0L ? 0 : (var30 < 0L ? -1 : 1);
                    if (var3 >= 0) {
                        var2 = 2131034255;
                    }

                    var27.setTextColor(var21.getColor(var2));
                    TextView var22 = var26.expenseTitleLabel;
                    if (var3 < 0) {
                        var2 = 2131821177;
                    } else {
                        var2 = 2131821178;
                    }

                    var22.setText(var2);
                }
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
        TextView expenseTitleLabel;

        OverallViewHolder(View itemView) {
            super(itemView);
            this.expenseLabel = (TextView) itemView.findViewById(R.id.expenseLabel);
            this.expenseTitleLabel = (TextView) itemView.findViewById(R.id.expenseTitleLabel);
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
            WalletCategoryTransactionAdapter.this.listener.OnItemClick(view, getLayoutPosition() - 1);
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            WalletCategoryTransactionAdapter.this.listener.OnItemLongClick(view, getLayoutPosition() - 1);
            return false;
        }
    }
}
