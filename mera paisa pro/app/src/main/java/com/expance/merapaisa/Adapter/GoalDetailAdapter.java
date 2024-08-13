package com.expance.merapaisa.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
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

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.expance.merapaisa.Database.Entity.GoalEntity;
import com.expance.merapaisa.Database.Entity.GoalTransEntity;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.AdsHelper;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Utility.DateHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/* loaded from: classes3.dex */
public class GoalDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    private GoalEntity goal;
    LayoutInflater inflater;
    private boolean isAdsLoaded;
    private boolean isPremium;
    List<Object> list = new ArrayList();
    OnItemClickListener listener;

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void OnItemClick(View v, int position);

        void OnItemLongClick(View v, int position);
    }

    public GoalDetailAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setAds(boolean isPremium) {
        this.isPremium = isPremium;
        notifyDataSetChanged();
    }

    public void setGoal(GoalEntity goal) {
        this.goal = goal;
        notifyDataSetChanged();
    }

    public List<Object> getList() {
        return this.list;
    }

    public void setList(List<Object> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType != 0) {
            if (viewType == 1) {
                return new HeaderHolder(this.inflater.inflate(R.layout.list_goal_detail, parent, false));
            }
            if (viewType == 3) {
                return new HeaderViewHolder(this.inflater.inflate(R.layout.list_transaction_header, parent, false));
            }
            return new TransactionViewHolder(this.inflater.inflate(R.layout.list_transaction, parent, false));
        }
        AdsHolder adsHolder = new AdsHolder(this.inflater.inflate(R.layout.list_ad_view, parent, false));
        AdView adView = new AdView(this.context);
        adView.setAdUnitId(this.context.getResources().getString(R.string.ad_unit_goal));
        adsHolder.adView.addView(adView);
        adView.setAdSize(AdsHelper.getAdSize((Activity) this.context));
        adView.loadAd(new AdRequest.Builder().build());
        adView.setAdListener(new AdListener() { // from class: com.ktwapps.walletmanager.Adapter.GoalDetailAdapter.1
            @Override // com.google.android.gms.ads.AdListener
            public void onAdLoaded() {
                GoalDetailAdapter.this.isAdsLoaded = true;
                GoalDetailAdapter.this.notifyDataSetChanged();
            }
        });
        return adsHolder;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String formattedDate;
        int itemViewType = getItemViewType(position);
        if (itemViewType == 1) {
            HeaderHolder headerHolder = (HeaderHolder) holder;
            GoalEntity goalEntity = this.goal;
            if (goalEntity != null) {
                int status = goalEntity.getStatus();
                String accountSymbol = (this.goal.getCurrency() == null || this.goal.getCurrency().length() == 0) ? SharePreferenceHelper.getAccountSymbol(this.context) : DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(this.goal.getCurrency()));
                String name = this.goal.getName();
                String color = this.goal.getColor();
                String beautifyAmount = Helper.getBeautifyAmount(accountSymbol, this.goal.getSaved());
                String beautifyAmount2 = Helper.getBeautifyAmount(accountSymbol, this.goal.getAmount() - this.goal.getSaved());
                String beautifyAmount3 = Helper.getBeautifyAmount(accountSymbol, this.goal.getAmount());
                String progressDoubleValue = Helper.getProgressDoubleValue(this.goal.getAmount(), this.goal.getSaved());
                if (status == 1) {
                    formattedDate = DateHelper.getFormattedDate(this.context, this.goal.getAchieveDate());
                } else {
                    formattedDate = DateHelper.getFormattedDate(this.context, this.goal.getExpectDate());
                }
                String string = this.context.getString(status == 1 ? R.string.achieve_date : R.string.goal_date);
                String goalRangeDate = DateHelper.getGoalRangeDate(this.context, this.goal);
                headerHolder.percentLabel.setText(progressDoubleValue);
                headerHolder.progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor(color)));
                headerHolder.nameLabel.setText(name);
                headerHolder.saveLabel.setText(beautifyAmount);
                headerHolder.remainLabel.setText(beautifyAmount2);
                headerHolder.goalLabel.setText(beautifyAmount3);
                headerHolder.progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor(color)));
                headerHolder.withdrawLabel.setTextColor(Color.parseColor(color));
                headerHolder.depositLabel.setTextColor(Color.parseColor(color));
                headerHolder.progressBar.setProgress(Helper.getProgressValue(this.goal.getAmount(), this.goal.getSaved()));
                headerHolder.goalDateLabel.setText(formattedDate);
                headerHolder.goalDateTitleLabel.setText(string);
                headerHolder.timeLabel.setVisibility(goalRangeDate.length() == 0 ? 8 : 0);
                headerHolder.timeLabel.setText(goalRangeDate);
                if (status == 1) {
                    headerHolder.depositWrapper.setVisibility(8);
                    headerHolder.withdrawWrapper.setVisibility(8);
                    return;
                }
                headerHolder.depositWrapper.setVisibility(0);
                headerHolder.withdrawWrapper.setVisibility(0);
                if (this.goal.getSaved() >= this.goal.getAmount()) {
                    headerHolder.depositLabel.setText(this.context.getString(R.string.achieve).toUpperCase());
                    headerHolder.depositImage.setImageResource(R.drawable.acheive);
                    return;
                }
                headerHolder.depositLabel.setText(this.context.getString(R.string.deposit).toUpperCase());
                headerHolder.depositImage.setImageResource(R.drawable.deposit);
                return;
            }
            return;
        }
        int i = R.drawable.deposit;
        int i2 = R.string.deposit;
        if (itemViewType != 2) {
            if (itemViewType != 3) {
                if (itemViewType == 0) {
                    ((AdsHolder) holder).adView.setVisibility(this.isAdsLoaded ? 0 : 8);
                    return;
                }
                return;
            }
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(((Long) this.list.get(position - (this.isPremium ? 2 : 1))).longValue());
            Date time = calendar.getTime();
            String bestDateTimePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "MMM yyyy");
            headerViewHolder.dayLabel.setText(new SimpleDateFormat("dd", Locale.getDefault()).format(time));
            headerViewHolder.monthLabel.setText(new SimpleDateFormat(bestDateTimePattern, Locale.getDefault()).format(time));
            headerViewHolder.weekLabel.setText(new SimpleDateFormat("EEEE", Locale.getDefault()).format(time));
            long j = 0;
            for (int i3 = position - (this.isPremium ? 1 : 0); i3 < this.list.size() && !(this.list.get(i3) instanceof Long); i3++) {
                GoalTransEntity goalTransEntity = (GoalTransEntity) this.list.get(i3);
                if (goalTransEntity.getType() == -14) {
                    j -= goalTransEntity.getAmount();
                } else {
                    j += goalTransEntity.getAmount();
                }
            }
            GoalEntity goalEntity2 = this.goal;
            headerViewHolder.amountLabel.setText(Helper.getBeautifyAmount((goalEntity2 == null || goalEntity2.getCurrency() == null || this.goal.getCurrency().length() == 0) ? SharePreferenceHelper.getAccountSymbol(this.context) : DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(this.goal.getCurrency())), j));
            return;
        }
        GoalTransEntity goalTransEntity2 = (GoalTransEntity) this.list.get(position - (this.isPremium ? 2 : 1));
        TransactionViewHolder transactionViewHolder = (TransactionViewHolder) holder;
        if (this.goal != null) {
            String accountSymbol2 = SharePreferenceHelper.getAccountSymbol(this.context);
            if (this.goal.getCurrency() != null) {
                accountSymbol2 = DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(this.goal.getCurrency()));
            }
            String beautifyAmount4 = Helper.getBeautifyAmount(accountSymbol2, goalTransEntity2.getType() == -14 ? -goalTransEntity2.getAmount() : goalTransEntity2.getAmount());
            String note = goalTransEntity2.getNote() != null ? goalTransEntity2.getNote() : "";
            Context context = this.context;
            if (goalTransEntity2.getType() == -14) {
                i2 = R.string.withdraw;
            }
            String string2 = context.getString(i2);
            String format = new SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), DateFormat.is24HourFormat(this.context) ? "kk:mm" : "hh:mm aa"), Locale.getDefault()).format(goalTransEntity2.getDateTime());
            if (Build.VERSION.SDK_INT >= 29) {
                transactionViewHolder.colorView.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor(this.goal.getColor()), BlendMode.SRC_OVER));
            } else {
                transactionViewHolder.colorView.getBackground().setColorFilter(Color.parseColor(this.goal.getColor()), PorterDuff.Mode.SRC_OVER);
            }
            ImageView imageView = transactionViewHolder.imageView;
            if (goalTransEntity2.getType() == -14) {
                i = R.drawable.withdraw;
            }
            imageView.setImageResource(i);
            transactionViewHolder.nameLabel.setText(string2);
            transactionViewHolder.amountLabel.setText(beautifyAmount4);
            transactionViewHolder.amountLabel.setTextColor(this.context.getResources().getColor(goalTransEntity2.getType() == -14 ? R.color.expense : R.color.income));
            transactionViewHolder.timeLabel.setText(format);
            transactionViewHolder.detailLabel.setText(note);
            transactionViewHolder.imageView1.setVisibility(8);
            transactionViewHolder.imageView2.setVisibility(8);
            transactionViewHolder.imageView3.setVisibility(8);
            int size = this.list.size();
            boolean z = this.isPremium;
            if (position >= size + (z ? 1 : 0)) {
                transactionViewHolder.divider.setVisibility(0);
            } else if (this.list.get(position - (z ? 1 : 0)) instanceof Long) {
                transactionViewHolder.divider.setVisibility(0);
            } else {
                transactionViewHolder.divider.setVisibility(8);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        int offset = isPremium ? 1 : 0;
        if (isPremium && position == 0) {
            return 0;
        } else if (position == offset) {
            return 1;
        } else {
            return this.list.get(position - (isPremium ? 2 : 1)) instanceof Long ? 3 : 2;
        }
    }

    /* loaded from: classes3.dex */
    public class HeaderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView depositImage;
        TextView depositLabel;
        ConstraintLayout depositWrapper;
        TextView goalDateLabel;
        TextView goalDateTitleLabel;
        TextView goalLabel;
        TextView nameLabel;
        TextView percentLabel;
        ProgressBar progressBar;
        TextView remainLabel;
        TextView saveLabel;
        TextView timeLabel;
        TextView withdrawLabel;
        ConstraintLayout withdrawWrapper;

        HeaderHolder(View itemView) {
            super(itemView);
            this.nameLabel = (TextView) itemView.findViewById(R.id.nameLabel);
            this.remainLabel = (TextView) itemView.findViewById(R.id.remainLabel);
            this.saveLabel = (TextView) itemView.findViewById(R.id.saveLabel);
            this.goalLabel = (TextView) itemView.findViewById(R.id.goalLabel);
            this.goalDateLabel = (TextView) itemView.findViewById(R.id.goalDateLabel);
            this.goalDateTitleLabel = (TextView) itemView.findViewById(R.id.goalDateTitleLabel);
            this.progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            this.timeLabel = (TextView) itemView.findViewById(R.id.timeLabel);
            this.percentLabel = (TextView) itemView.findViewById(R.id.percentLabel);
            this.depositLabel = (TextView) itemView.findViewById(R.id.depositLabel);
            this.withdrawLabel = (TextView) itemView.findViewById(R.id.withdrawLabel);
            this.depositWrapper = (ConstraintLayout) itemView.findViewById(R.id.depositWrapper);
            this.withdrawWrapper = (ConstraintLayout) itemView.findViewById(R.id.withdrawWrapper);
            this.depositImage = (ImageView) itemView.findViewById(R.id.depositImage);
            this.depositWrapper.setOnClickListener(this);
            this.withdrawWrapper.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (GoalDetailAdapter.this.listener != null) {
                GoalDetailAdapter.this.listener.OnItemClick(view, 0);
            }
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
    public class AdsHolder extends RecyclerView.ViewHolder {
        FrameLayout adView;

        AdsHolder(View itemView) {
            super(itemView);
            this.adView = (FrameLayout) itemView.findViewById(R.id.adView);
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
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            GoalDetailAdapter.this.listener.OnItemClick(view, getLayoutPosition() - (GoalDetailAdapter.this.isPremium ? 2 : 1));
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            GoalDetailAdapter.this.listener.OnItemLongClick(view, getLayoutPosition() - (GoalDetailAdapter.this.isPremium ? 2 : 1));
            return false;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        if (this.list.size() > 0) {
            return (this.isPremium ? 2 : 1) + this.list.size();
        }
        return this.isPremium ? 2 : 1;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
