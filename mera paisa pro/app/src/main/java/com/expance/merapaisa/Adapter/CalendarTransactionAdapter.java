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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/* loaded from: classes3.dex */
public class CalendarTransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    public List<TransList> list = new ArrayList();
    OnItemClickListener listener;

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }

    public CalendarTransactionAdapter(Context context) {
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

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int color;
        int color2;
        if (getItemViewType(position) == 0) {
            String bestDateTimePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "MMM yyyy");
            String accountSymbol = SharePreferenceHelper.getAccountSymbol(this.context);
            DailyTrans dailyTrans = this.list.get(position).getDailyTrans();
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            Date dateTime = dailyTrans.getDateTime();
            String beautifyAmount = Helper.getBeautifyAmount(accountSymbol, dailyTrans.getAmount());
            headerViewHolder.dayLabel.setText(new SimpleDateFormat("dd", Locale.getDefault()).format(dateTime));
            headerViewHolder.monthLabel.setText(new SimpleDateFormat(bestDateTimePattern, Locale.getDefault()).format(dateTime));
            headerViewHolder.amountLabel.setText(beautifyAmount);
            if (!SharePreferenceHelper.isFutureBalanceOn(this.context)) {
                headerViewHolder.weekLabel.setText(new SimpleDateFormat("EEEE", Locale.getDefault()).format(dateTime));
                return;
            } else if (DateHelper.isDatePast(dateTime)) {
                headerViewHolder.weekLabel.setText(new SimpleDateFormat("EEEE", Locale.getDefault()).format(dateTime));
                return;
            } else {
                headerViewHolder.weekLabel.setText(R.string.future);
                return;
            }
        }
        Trans trans = this.list.get(position).getTrans();
        TransactionViewHolder transactionViewHolder = (TransactionViewHolder) holder;
        if (trans.getWalletId() == 0) {
            String beautifyAmount2 = Helper.getBeautifyAmount(SharePreferenceHelper.getAccountSymbol(this.context), trans.getAmount());
            boolean z = 0 > trans.getAmount();
            transactionViewHolder.imageView.setImageResource(R.drawable.transfer);
            transactionViewHolder.nameLabel.setText(R.string.carry_over);
            transactionViewHolder.amountLabel.setText(beautifyAmount2);
            TextView textView = transactionViewHolder.amountLabel;
            if (trans.getAmount() == 0) {
                color2 = Helper.getAttributeColor(this.context, R.attr.primaryTextColor);
            } else {
                Resources resources = this.context.getResources();
                color2 = z ? resources.getColor(R.color.expense) : resources.getColor(R.color.income);
            }
            textView.setTextColor(color2);
            if (Build.VERSION.SDK_INT >= 29) {
                transactionViewHolder.colorView.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor("#66757f"), BlendMode.SRC_OVER));
            } else {
                transactionViewHolder.colorView.getBackground().setColorFilter(Color.parseColor("#66757f"), PorterDuff.Mode.SRC_OVER);
            }
            transactionViewHolder.detailLabel.setText(R.string.all_wallets);
            Calendar calendar = Calendar.getInstance();
            calendar.set(14, 0);
            calendar.set(13, 0);
            calendar.set(12, 0);
            calendar.set(10, 0);
            transactionViewHolder.timeLabel.setText(new SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), DateFormat.is24HourFormat(this.context) ? "kk:mm" : "hh:mm aa"), Locale.getDefault()).format(calendar.getTime()));
            transactionViewHolder.imageView1.setVisibility(8);
            transactionViewHolder.imageView2.setVisibility(8);
            transactionViewHolder.imageView3.setVisibility(8);
            transactionViewHolder.divider.setVisibility(8);
            transactionViewHolder.recurringImage.setVisibility(8);
            return;
        }
        String str = DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(trans.getCurrency()));
        String media = trans.getMedia();
        String note = trans.getNote(this.context);
        String color3 = trans.getColor() == null ? "#808080" : trans.getColor();
        String wallet = trans.getWallet();
        int type = trans.getType();
        Date dateTime2 = trans.getDateTime();
        String beautifyAmount3 = Helper.getBeautifyAmount(str, trans.getAmount());
        boolean z2 = 0 > trans.getAmount();
        String format = new SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), DateFormat.is24HourFormat(this.context) ? "kk:mm" : "hh:mm aa"), Locale.getDefault()).format(dateTime2);
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
        transactionViewHolder.amountLabel.setText(beautifyAmount3);
        TextView textView2 = transactionViewHolder.amountLabel;
        if (trans.getType() == 2) {
            color = Helper.getAttributeColor(this.context, R.attr.primaryTextColor);
        } else {
            color = this.context.getResources().getColor(z2 ? R.color.expense : R.color.income);
        }
        textView2.setTextColor(color);
        transactionViewHolder.nameLabel.setText(note);
        if (Build.VERSION.SDK_INT >= 29) {
            transactionViewHolder.colorView.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor(color3), BlendMode.SRC_OVER));
        } else {
            transactionViewHolder.colorView.getBackground().setColorFilter(Color.parseColor(color3), PorterDuff.Mode.SRC_OVER);
        }
        transactionViewHolder.detailLabel.setText(wallet);
        transactionViewHolder.timeLabel.setText(format);
        transactionViewHolder.divider.setVisibility(8);
        transactionViewHolder.recurringImage.setVisibility(trans.isRecurring() ? 0 : 8);
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
    private class TransactionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            CalendarTransactionAdapter.this.listener.OnItemClick(view, getLayoutPosition());
        }
    }
}
