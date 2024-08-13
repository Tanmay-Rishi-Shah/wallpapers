package com.expance.merapaisa.Adapter;

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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.utils.Utils;
import com.expance.merapaisa.Database.Entity.DebtTransEntity;
import com.expance.merapaisa.Model.Debt;
import com.expance.merapaisa.R;
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
public class DebtDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    private Debt debt;
    LayoutInflater inflater;
    List<Object> list = new ArrayList();
    OnItemClickListener listener;

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void OnItemClick(View v, int position);

        void OnItemLongClick(View v, int position);
    }

    private long getAmount(int debtType, int debtTransType, long amount) {
        if (debtType == 0) {
            if (debtTransType != 0) {
                return amount;
            }
        } else if (debtTransType != 1 && debtTransType != 2 && debtTransType != 3) {
            return amount;
        }
        return -amount;
    }

    private int getAmountColor(int var1, int var2) {
        label47:
        {
            int var3 = 2131034246;
            if (var1 == 0) {
                var1 = var3;
                if (var2 == 0) {
                    return var1;
                }

                if (var2 != 1 && var2 != 2 && var2 != 3) {
                    break label47;
                }
            } else if (var2 != 0) {
                var1 = var3;
                if (var2 == 1) {
                    return var1;
                }

                var1 = var3;
                if (var2 == 2) {
                    return var1;
                }

                var1 = var3;
                if (var2 == 3) {
                    return var1;
                }
                break label47;
            }

            var1 = 2131034255;
            return var1;
        }

        var1 = 2131034808;
        return var1;
    }

    private int getIcon(int debtType, int debtTransType) {
        if (debtType == 0) {
            if (debtTransType != 0) {
                if (debtTransType == 1) {
                    return R.drawable.increase_lend;
                }
                if (debtTransType == 2) {
                    return R.drawable.interest;
                }
                if (debtTransType == 3) {
                    return R.drawable.lend;
                }
            }
        } else if (debtTransType == 0) {
            return R.drawable.receive;
        } else {
            if (debtTransType == 1) {
                return R.drawable.increase_borrow;
            }
            if (debtTransType == 2) {
                return R.drawable.interest;
            }
            if (debtTransType == 3) {
                return R.drawable.borrow;
            }
        }
        return R.drawable.repay;
    }

    public DebtDetailAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setDebt(Debt debt) {
        this.debt = debt;
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
        if (viewType == 0) {
            return new HeaderHolder(this.inflater.inflate(R.layout.list_debt_trans_header, parent, false));
        }
        if (viewType == 2) {
            return new HeaderViewHolder(this.inflater.inflate(R.layout.list_transaction_header, parent, false));
        }
        return new TransactionViewHolder(this.inflater.inflate(R.layout.list_transaction, parent, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int i = position;
        int itemViewType = getItemViewType(i);
        if (itemViewType == 0) {
            HeaderHolder headerHolder = (HeaderHolder) holder;
            if (this.debt != null) {
                String str = DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(this.debt.getCurrencyCode()));
                String beautifyAmount = Helper.getBeautifyAmount(str, this.debt.getAmount() - this.debt.getPay());
                String beautifyAmount2 = Helper.getBeautifyAmount(str, this.debt.getPay());
                String substring = (this.debt.getLender() == null || this.debt.getLender().length() <= 0) ? "?" : this.debt.getLender().substring(0, 1);
                String string = (this.debt.getLender() == null || this.debt.getLender().length() <= 0) ? this.context.getResources().getString(R.string.someone) : this.debt.getLender();
                String color = this.debt.getColor();
                String formattedDateTime = DateHelper.getFormattedDateTime(this.context, this.debt.getLendDate());
                String beautifyAmount3 = Helper.getBeautifyAmount(str, this.debt.getAmount());
                String string2 = (this.debt.getName() == null || this.debt.getName().length() == 0) ? this.context.getResources().getString(R.string.no_description) : this.debt.getName();
                String string3 = (this.debt.getWallet() == null || this.debt.getWallet().length() == 0) ? this.context.getResources().getString(R.string.no_wallet) : this.debt.getWallet();
                headerHolder.percentLabel.setText(Helper.getProgressDoubleValue(this.debt.getAmount(), this.debt.getPay()));
                headerHolder.spentTitleLabel.setText(this.debt.getType() == 0 ? R.string.pay : R.string.receive);
                headerHolder.spentLabel.setText(beautifyAmount2);
                headerHolder.remainLabel.setText(beautifyAmount);
                headerHolder.circleLabel.setText(substring);
                headerHolder.nameLabel.setText(string);
                headerHolder.dateLabel.setText(formattedDateTime);
                headerHolder.amountLabel.setText(beautifyAmount3);
                headerHolder.descriptionLabel.setText(string2);
                headerHolder.descriptionLabel.setTextColor(Helper.getAttributeColor(this.context, (this.debt.getName() == null || this.debt.getName().length() == 0) ? R.attr.untitledTextColor : R.attr.primaryTextColor));
                headerHolder.progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor(color)));
                headerHolder.progressBar.setProgress(Helper.getProgressValue(this.debt.getAmount(), this.debt.getPay()));
                headerHolder.walletLabel.setText(string3);
                if (Build.VERSION.SDK_INT >= 29) {
                    headerHolder.circleWrapper.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor(color), BlendMode.SRC_OVER));
                } else {
                    headerHolder.circleWrapper.getBackground().setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_OVER);
                }
            }
        } else if (itemViewType != 1) {
            if (itemViewType == 2) {
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(((Long) this.list.get(i - 1)).longValue());
                Date time = calendar.getTime();
                String bestDateTimePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "MMM yyyy");
                headerViewHolder.dayLabel.setText(new SimpleDateFormat("dd", Locale.getDefault()).format(time));
                headerViewHolder.monthLabel.setText(new SimpleDateFormat(bestDateTimePattern, Locale.getDefault()).format(time));
                if (!SharePreferenceHelper.isFutureBalanceOn(this.context)) {
                    headerViewHolder.weekLabel.setText(new SimpleDateFormat("EEEE", Locale.getDefault()).format(time));
                } else if (DateHelper.isDatePast(time)) {
                    headerViewHolder.weekLabel.setText(new SimpleDateFormat("EEEE", Locale.getDefault()).format(time));
                } else {
                    headerViewHolder.weekLabel.setText(R.string.future);
                }
                if (this.debt != null) {
                    long j = 0;
                    while (i < this.list.size() && !(this.list.get(i) instanceof Long)) {
                        DebtTransEntity debtTransEntity = (DebtTransEntity) this.list.get(i);
                        j = (long) (j + (getAmount(this.debt.getType(), debtTransEntity.getType(), debtTransEntity.getAmount()) * (debtTransEntity.getRate() == Utils.DOUBLE_EPSILON ? 1.0d : debtTransEntity.getRate() / this.debt.getRate())));
                        i++;
                    }
                    headerViewHolder.amountLabel.setText(Helper.getBeautifyAmount(DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(this.debt.getCurrencyCode())), j));
                }
            }
        } else {
            DebtTransEntity debtTransEntity2 = (DebtTransEntity) this.list.get(i - 1);
            TransactionViewHolder transactionViewHolder = (TransactionViewHolder) holder;
            if (this.debt != null) {
                String beautifyAmount4 = Helper.getBeautifyAmount(DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(debtTransEntity2.getCurrencyCode() == null ? this.debt.getCurrencyCode() : debtTransEntity2.getCurrencyCode())), getAmount(this.debt.getType(), debtTransEntity2.getType(), debtTransEntity2.getAmount()));
                String name = getName(this.debt.getType(), debtTransEntity2.getType(), debtTransEntity2.getNote());
                String format = new SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), DateFormat.is24HourFormat(this.context) ? "kk:mm" : "hh:mm aa"), Locale.getDefault()).format(debtTransEntity2.getDateTime());
                String string4 = (debtTransEntity2.getWallet() == null || debtTransEntity2.getWallet().length() == 0) ? this.context.getResources().getString(R.string.no_wallet) : debtTransEntity2.getWallet();
                if (Build.VERSION.SDK_INT >= 29) {
                    transactionViewHolder.colorView.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor(this.debt.getColor()), BlendMode.SRC_OVER));
                } else {
                    transactionViewHolder.colorView.getBackground().setColorFilter(Color.parseColor(this.debt.getColor()), PorterDuff.Mode.SRC_OVER);
                }
                transactionViewHolder.imageView.setImageResource(getIcon(this.debt.getType(), debtTransEntity2.getType()));
                transactionViewHolder.nameLabel.setText(name);
                transactionViewHolder.amountLabel.setText(beautifyAmount4);
                transactionViewHolder.amountLabel.setTextColor(this.context.getResources().getColor(getAmountColor(this.debt.getType(), debtTransEntity2.getType())));
                transactionViewHolder.timeLabel.setText(format);
                transactionViewHolder.detailLabel.setText(string4);
                transactionViewHolder.imageView1.setVisibility(8);
                transactionViewHolder.imageView2.setVisibility(8);
                transactionViewHolder.imageView3.setVisibility(8);
                if (i >= this.list.size()) {
                    transactionViewHolder.divider.setVisibility(0);
                } else if (this.list.get(i) instanceof Long) {
                    transactionViewHolder.divider.setVisibility(0);
                } else {
                    transactionViewHolder.divider.setVisibility(8);
                }
            }
        }
    }

    private String getName(int debtType, int debtTransType, String note) {
        String string;
        if (note == null || note.length() == 0) {
            if (debtType != 0) {
                if (debtTransType != 0) {
                    if (debtTransType != 1) {
                        if (debtTransType != 2) {
                            return debtTransType != 3 ? note : this.context.getResources().getString(R.string.loan);
                        }
                        return this.context.getResources().getString(R.string.interest);
                    }
                    return this.context.getResources().getString(R.string.loan_increase);
                }
                return this.context.getResources().getString(R.string.collect);
            }
            if (debtTransType == 0) {
                string = this.context.getResources().getString(R.string.repay);
            } else if (debtTransType == 1) {
                string = this.context.getResources().getString(R.string.debt_increase);
            } else if (debtTransType == 2) {
                string = this.context.getResources().getString(R.string.interest);
            } else if (debtTransType != 3) {
                return note;
            } else {
                string = this.context.getResources().getString(R.string.debt);
            }
            return string;
        }
        return note;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return this.list.get(position - 1) instanceof Long ? 2 : 1;
    }

    /* loaded from: classes3.dex */
    public class HeaderHolder extends RecyclerView.ViewHolder {
        TextView amountLabel;
        TextView circleLabel;
        ConstraintLayout circleWrapper;
        TextView dateLabel;
        TextView descriptionLabel;
        TextView nameLabel;
        TextView percentLabel;
        ProgressBar progressBar;
        TextView remainLabel;
        TextView spentLabel;
        TextView spentTitleLabel;
        TextView walletLabel;

        HeaderHolder(View itemView) {
            super(itemView);
            this.percentLabel = (TextView) itemView.findViewById(R.id.percentLabel);
            this.circleWrapper = (ConstraintLayout) itemView.findViewById(R.id.circleWrapper);
            this.circleLabel = (TextView) itemView.findViewById(R.id.circleLabel);
            this.remainLabel = (TextView) itemView.findViewById(R.id.remainLabel);
            this.spentLabel = (TextView) itemView.findViewById(R.id.spentLabel);
            this.spentTitleLabel = (TextView) itemView.findViewById(R.id.spentTitleLabel);
            this.nameLabel = (TextView) itemView.findViewById(R.id.nameLabel);
            this.progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            this.descriptionLabel = (TextView) itemView.findViewById(R.id.descriptionLabel);
            this.amountLabel = (TextView) itemView.findViewById(R.id.amountLabel);
            this.dateLabel = (TextView) itemView.findViewById(R.id.dateLabel);
            this.walletLabel = (TextView) itemView.findViewById(R.id.walletLabel);
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
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            DebtDetailAdapter.this.listener.OnItemClick(view, getLayoutPosition());
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            DebtDetailAdapter.this.listener.OnItemLongClick(view, getLayoutPosition());
            return false;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.list.size() + 1;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
