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
public class TransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    public List<TransList> list = new ArrayList();
    OnItemClickListener listener;

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void OnItemClick(View view, int position);

        void OnItemLongClick(View view, int position);
    }

    public TransactionAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new HeaderViewHolder(this.inflater.inflate(R.layout.list_transaction_header, parent, false));
        }
        return new TransactionViewHolder(this.inflater.inflate(R.layout.list_transaction, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
        int var3 = this.getItemViewType(var2);
        String var6;
        if (var3 == 0) {
            var6 = SharePreferenceHelper.getAccountSymbol(this.context);
            DailyTrans var7 = ((TransList) this.list.get(var2)).getDailyTrans();
            HeaderViewHolder var5 = (HeaderViewHolder) var1;
            Date var13 = var7.getDateTime();
            String var21 = Helper.getBeautifyAmount(var6, var7.getAmount());
            var6 = DateFormat.getBestDateTimePattern(Locale.getDefault(), "MMM yyyy");
            var5.dayLabel.setText((new SimpleDateFormat("dd", Locale.getDefault())).format(var13));
            var5.monthLabel.setText((new SimpleDateFormat(var6, Locale.getDefault())).format(var13));
            var5.amountLabel.setText(var21);
            if (!SharePreferenceHelper.isFutureBalanceOn(this.context)) {
                var5.weekLabel.setText((new SimpleDateFormat("EEEE", Locale.getDefault())).format(var13));
            } else if (DateHelper.isDatePast(var13)) {
                var5.weekLabel.setText((new SimpleDateFormat("EEEE", Locale.getDefault())).format(var13));
            } else {
                var5.weekLabel.setText(2131820818);
            }
        } else if (var3 == 1) {
            Trans var8 = ((TransList) this.list.get(var2)).getTrans();
            TransactionViewHolder var22 = (TransactionViewHolder) var1;
            String var14 = var8.getCurrency();
            String var17 = (String) DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(var14));
            String var11 = var8.getMedia();
            String var9 = var8.getNote(this.context);
            if (var8.getColor() == null) {
                var14 = "#A7A9AB";
            } else {
                var14 = var8.getColor();
            }

            var6 = var8.getWallet();
            int var4 = var8.getType();
            Date var12 = var8.getDateTime();
            String var10 = Helper.getBeautifyAmount(var17, var8.getAmount());
            boolean var15;
            if (0L > var8.getAmount()) {
                var15 = true;
            } else {
                var15 = false;
            }

            if (DateFormat.is24HourFormat(this.context)) {
                var17 = "kk:mm";
            } else {
                var17 = "hh:mm aa";
            }

            String var24 = (new SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), var17), Locale.getDefault())).format(var12);
            if (var11 == null) {
                var22.imageView1.setVisibility(8);
                var22.imageView2.setVisibility(8);
                var22.imageView3.setVisibility(8);
            } else {
                String[] var18 = var11.split(",");
                if (var18.length == 1) {
                    var22.imageView1.setVisibility(0);
                    var22.imageView2.setVisibility(8);
                    var22.imageView3.setVisibility(8);
                    Picasso.get().load(new File(var18[0])).centerCrop().resize(100, 100).into(var22.imageView1);
                } else if (var18.length == 2) {
                    var22.imageView1.setVisibility(0);
                    var22.imageView2.setVisibility(0);
                    var22.imageView3.setVisibility(8);
                    Picasso.get().load(new File(var18[0])).centerCrop().resize(100, 100).into(var22.imageView1);
                    Picasso.get().load(new File(var18[1])).centerCrop().resize(100, 100).into(var22.imageView2);
                } else if (var18.length == 3) {
                    var22.imageView1.setVisibility(0);
                    var22.imageView2.setVisibility(0);
                    var22.imageView3.setVisibility(0);
                    Picasso.get().load(new File(var18[0])).centerCrop().resize(100, 100).into(var22.imageView1);
                    Picasso.get().load(new File(var18[1])).centerCrop().resize(100, 100).into(var22.imageView2);
                    Picasso.get().load(new File(var18[2])).centerCrop().resize(100, 100).into(var22.imageView3);
                }
            }

            if (var4 == 2) {
                StringBuilder var19 = new StringBuilder();
                var19.append(var6);
                var19.append(this.context.getResources().getString(2131821192));
                var19.append(var8.getTransferWallet());
                var17 = var19.toString();
                var22.imageView.setImageResource(2131165730);
            } else {
                var22.imageView.setImageResource((Integer) DataHelper.getCategoryIcons().get(var8.getIcon()));
                var17 = var6;
            }

            var22.amountLabel.setText(var10);
            TextView var20 = var22.amountLabel;
            if (var8.getType() == 2) {
                var3 = Helper.getAttributeColor(this.context, 2130903970);
            } else {
                Resources var23 = this.context.getResources();
                if (var15) {
                    var3 = 2131034246;
                } else {
                    var3 = 2131034255;
                }

                var3 = var23.getColor(var3);
            }

            var20.setTextColor(var3);
            var22.nameLabel.setText(var9);
            if (Build.VERSION.SDK_INT >= 29) {
                var22.colorView.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor(var14), BlendMode.SRC_OVER));
            } else {
                var22.colorView.getBackground().setColorFilter(Color.parseColor(var14), PorterDuff.Mode.SRC_OVER);
            }

            var22.detailLabel.setText(var17);
            var22.timeLabel.setText(var24);
            ++var2;
            if (var2 >= this.list.size()) {
                var22.divider.setVisibility(0);
            } else {
                TransList var16 = (TransList) this.list.get(var2);
                if (!var16.isDailyHeader() && var16.getTrans() != null) {
                    var22.divider.setVisibility(8);
                } else {
                    var22.divider.setVisibility(0);
                }
            }
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.list.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int position) {
        return this.list.get(position).isDailyHeader() ? 0 : 1;
    }

    public void setList(List<TransList> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
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
            TransactionAdapter.this.listener.OnItemClick(view, getLayoutPosition());
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            TransactionAdapter.this.listener.OnItemLongClick(view, getLayoutPosition());
            return false;
        }
    }
}
