package com.expance.merapaisa.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.expance.merapaisa.Model.WalletDetail;
import com.expance.merapaisa.Model.WalletTrans;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.AdsHelper;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Utility.DateHelper;
import com.expance.merapaisa.Utility.Helper;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class WalletTransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private boolean isAdsLoaded;
    private boolean isPremium;
    private OnItemClickListener listener;
    private WalletDetail walletDetail;
    private String symbol = "$";
    private List<WalletTrans> list = new ArrayList();

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }

    public WalletTransactionAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setAds(boolean isPremium) {
        this.isPremium = isPremium;
        notifyDataSetChanged();
    }

    public void setList(List<WalletTrans> list) {
        this.list = list;
    }

    public List<WalletTrans> getList() {
        return this.list;
    }

    public void setWalletDetail(WalletDetail walletDetail) {
        this.walletDetail = walletDetail;
        this.symbol = DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(walletDetail.getCurrency()));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType != 0) {
            if (viewType == 1) {
                return new HeaderViewHolder(this.inflater.inflate(R.layout.list_wallet_header, parent, false));
            }
            if (viewType == 2) {
                return new TitleViewHolder(this.inflater.inflate(R.layout.list_wallet_title, parent, false));
            }
            if (viewType == 3) {
                return new TransactionViewHolder(this.inflater.inflate(R.layout.list_wallet_item, parent, false));
            }
            return new CreditHeaderViewHolder(this.inflater.inflate(R.layout.list_wallet_credit_header, parent, false));
        }
        AdsHolder adsHolder = new AdsHolder(this.inflater.inflate(R.layout.list_ad_view, parent, false));
        AdView adView = new AdView(this.context);
        adView.setAdUnitId(this.context.getResources().getString(R.string.ad_unit_debt));
        adsHolder.adView.addView(adView);
        adView.setAdSize(AdsHelper.getAdSize((Activity) this.context));
        adView.loadAd(new AdRequest.Builder().build());
        adView.setAdListener(new AdListener() { // from class: com.ktwapps.walletmanager.Adapter.WalletTransactionAdapter.1
            @Override // com.google.android.gms.ads.AdListener
            public void onAdLoaded() {
                WalletTransactionAdapter.this.isAdsLoaded = true;
                WalletTransactionAdapter.this.notifyDataSetChanged();
            }
        });
        return adsHolder;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == 1) {
            if (this.walletDetail != null) {
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                headerViewHolder.nameLabel.setText(this.walletDetail.getName());
//                if (Build.VERSION.SDK_INT >= 29) {
//                    headerViewHolder.imageWrapper.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor(this.walletDetail.getColor()), BlendMode.SRC_OVER));
//                    headerViewHolder.adjustButton.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor(this.walletDetail.getColor()), BlendMode.SRC_OVER));
//                } else {
//                    headerViewHolder.imageWrapper.getBackground().setColorFilter(Color.parseColor(this.walletDetail.getColor()), PorterDuff.Mode.SRC_OVER);
//                    headerViewHolder.adjustButton.getBackground().setColorFilter(Color.parseColor(this.walletDetail.getColor()), PorterDuff.Mode.SRC_OVER);
//                }
                headerViewHolder.imageView.setImageResource(DataHelper.getWalletIcons().get(this.walletDetail.getIcon()).intValue());
                headerViewHolder.nameLabel.setText(this.walletDetail.getName());
                headerViewHolder.amountLabel.setText(Helper.getBeautifyAmount(this.symbol, this.walletDetail.getAmount()));
                headerViewHolder.expenseLabel.setText(this.walletDetail.getExpense() > 1 ? this.context.getResources().getString(R.string.total_transactions, Integer.valueOf(this.walletDetail.getExpense())) : this.context.getResources().getString(R.string.total_transaction, Integer.valueOf(this.walletDetail.getExpense())));
                headerViewHolder.incomeLabel.setText(this.walletDetail.getIncome() > 1 ? this.context.getResources().getString(R.string.total_transactions, Integer.valueOf(this.walletDetail.getIncome())) : this.context.getResources().getString(R.string.total_transaction, Integer.valueOf(this.walletDetail.getIncome())));
                headerViewHolder.transferLabel.setText(this.walletDetail.getTransfer() > 1 ? this.context.getResources().getString(R.string.total_transactions, Integer.valueOf(this.walletDetail.getTransfer())) : this.context.getResources().getString(R.string.total_transaction, Integer.valueOf(this.walletDetail.getTransfer())));
                headerViewHolder.initialLabel.setText(Helper.getBeautifyAmount(this.symbol, this.walletDetail.getInitialAmount()));
            }
        } else if (itemViewType == 4) {
            if (this.walletDetail != null) {
                CreditHeaderViewHolder creditHeaderViewHolder = (CreditHeaderViewHolder) holder;
                creditHeaderViewHolder.nameLabel.setText(this.walletDetail.getName());
//                if (Build.VERSION.SDK_INT >= 29) {
//                    creditHeaderViewHolder.imageWrapper.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor(this.walletDetail.getColor()), BlendMode.SRC_OVER));
//                    creditHeaderViewHolder.adjustButton.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor(this.walletDetail.getColor()), BlendMode.SRC_OVER));
//                } else {
//                    creditHeaderViewHolder.imageWrapper.getBackground().setColorFilter(Color.parseColor(this.walletDetail.getColor()), PorterDuff.Mode.SRC_OVER);
//                    creditHeaderViewHolder.adjustButton.getBackground().setColorFilter(Color.parseColor(this.walletDetail.getColor()), PorterDuff.Mode.SRC_OVER);
//                }
                creditHeaderViewHolder.imageView.setImageResource(DataHelper.getWalletIcons().get(this.walletDetail.getIcon()).intValue());
                creditHeaderViewHolder.nameLabel.setText(this.walletDetail.getName());
                creditHeaderViewHolder.amountLabel.setText(Helper.getBeautifyAmount(this.symbol, this.walletDetail.getAmount()));
                creditHeaderViewHolder.expenseLabel.setText(this.walletDetail.getExpense() > 1 ? this.context.getResources().getString(R.string.total_transactions, Integer.valueOf(this.walletDetail.getExpense())) : this.context.getResources().getString(R.string.total_transaction, Integer.valueOf(this.walletDetail.getExpense())));
                creditHeaderViewHolder.availableLabel.setText(Helper.getBeautifyAmount(this.symbol, this.walletDetail.getCreditLimit() + this.walletDetail.getAmount()));
                creditHeaderViewHolder.creditLabel.setText(Helper.getBeautifyAmount(this.symbol, this.walletDetail.getCreditLimit()));
                creditHeaderViewHolder.statementLabel.setText(DateHelper.getCreditDate(this.walletDetail.getStatementDate()));
                creditHeaderViewHolder.dueLabel.setText(DateHelper.getCreditDate(this.walletDetail.getDueDate()));
            }
        } else {
            if (itemViewType != 3) {
                if (itemViewType == 0) {
                    ((AdsHolder) holder).adView.setVisibility(this.isAdsLoaded ? 0 : 8);
                    return;
                }
                return;
            }
            TransactionViewHolder transactionViewHolder = (TransactionViewHolder) holder;
            WalletTrans walletTrans = this.list.get(position - (this.isPremium ? 3 : 2));
            String color = walletTrans.getColor();
            String name = walletTrans.getName(this.context);
            String beautifyAmount = Helper.getBeautifyAmount(this.symbol, walletTrans.getAmount());
            int type = walletTrans.getType();
            String string = walletTrans.getTrans() > 1 ? this.context.getResources().getString(R.string.total_transactions, Integer.valueOf(walletTrans.getTrans())) : this.context.getResources().getString(R.string.total_transaction, Integer.valueOf(walletTrans.getTrans()));
            if (type == 2) {
                transactionViewHolder.imageView.setImageResource(R.drawable.transfer);
                if (Build.VERSION.SDK_INT >= 29) {
                    transactionViewHolder.colorView.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor("#66757f"), BlendMode.SRC_OVER));
                } else {
                    transactionViewHolder.colorView.getBackground().setColorFilter(Color.parseColor("#66757f"), PorterDuff.Mode.SRC_OVER);
                }
                transactionViewHolder.amountLabel.setTextColor(walletTrans.getAmount() < 0 ? this.context.getResources().getColor(R.color.expense) : this.context.getResources().getColor(R.color.income));
                transactionViewHolder.amountLabel.setText(beautifyAmount);
                transactionViewHolder.nameLabel.setText(R.string.transfer);
            } else {
                transactionViewHolder.imageView.setImageResource(DataHelper.getCategoryIcons().get(walletTrans.getIcon()).intValue());
                transactionViewHolder.amountLabel.setText(beautifyAmount);
                transactionViewHolder.amountLabel.setTextColor(type == 1 ? this.context.getResources().getColor(R.color.expense) : this.context.getResources().getColor(R.color.income));
                if (Build.VERSION.SDK_INT >= 29) {
                    transactionViewHolder.colorView.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor(color), BlendMode.SRC_OVER));
                } else {
                    transactionViewHolder.colorView.getBackground().setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_OVER);
                }
                transactionViewHolder.nameLabel.setText(name);
            }
            transactionViewHolder.transLabel.setText(string);
            if (this.list.size() == position - (this.isPremium ? 2 : 1)) {
                transactionViewHolder.divider.setVisibility(0);
            } else {
                transactionViewHolder.divider.setVisibility(8);
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

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int position) {
        boolean z = this.isPremium;
        if (position == 0) {
            WalletDetail walletDetail = this.walletDetail;
            return (walletDetail == null || walletDetail.getType() != 1) ? 1 : 4;
        } else if (position == (z ? 1 : 0) + 1) {
            return 2;
        } else {
            return 3;
        }
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /* loaded from: classes3.dex */
    private class HeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button adjustButton;
        TextView amountLabel;
        TextView expenseLabel;
        ImageView imageView;
        ConstraintLayout imageWrapper;
        TextView incomeLabel;
        TextView initialLabel;
        TextView nameLabel;
        ConstraintLayout nameWrapper;
        TextView transferLabel;

        HeaderViewHolder(View itemView) {
            super(itemView);
            this.nameWrapper = (ConstraintLayout) itemView.findViewById(R.id.nameWrapper);
            this.imageWrapper = (ConstraintLayout) itemView.findViewById(R.id.imageWrapper);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.nameLabel = (TextView) itemView.findViewById(R.id.nameLabel);
            this.amountLabel = (TextView) itemView.findViewById(R.id.amountLabel);
            this.incomeLabel = (TextView) itemView.findViewById(R.id.incomeLabel);
            this.expenseLabel = (TextView) itemView.findViewById(R.id.expenseLabel);
            this.transferLabel = (TextView) itemView.findViewById(R.id.transferLabel);
            this.initialLabel = (TextView) itemView.findViewById(R.id.initialLabel);
            Button button = (Button) itemView.findViewById(R.id.adjustButton);
            this.adjustButton = button;
            button.setOnClickListener(this);
            this.nameWrapper.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (WalletTransactionAdapter.this.listener != null) {
                WalletTransactionAdapter.this.listener.OnItemClick(view, 0);
            }
        }
    }

    /* loaded from: classes3.dex */
    private class CreditHeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button adjustButton;
        TextView amountLabel;
        TextView availableLabel;
        TextView creditLabel;
        TextView dueLabel;
        TextView expenseLabel;
        ImageView imageView;
        ConstraintLayout imageWrapper;
        TextView nameLabel;
        ConstraintLayout nameWrapper;
        TextView statementLabel;

        CreditHeaderViewHolder(View itemView) {
            super(itemView);
            this.nameWrapper = (ConstraintLayout) itemView.findViewById(R.id.nameWrapper);
            this.imageWrapper = (ConstraintLayout) itemView.findViewById(R.id.imageWrapper);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.nameLabel = (TextView) itemView.findViewById(R.id.nameLabel);
            this.amountLabel = (TextView) itemView.findViewById(R.id.amountLabel);
            this.availableLabel = (TextView) itemView.findViewById(R.id.availableLabel);
            this.creditLabel = (TextView) itemView.findViewById(R.id.creditLabel);
            this.dueLabel = (TextView) itemView.findViewById(R.id.dueLabel);
            this.statementLabel = (TextView) itemView.findViewById(R.id.statementLabel);
            this.adjustButton = (Button) itemView.findViewById(R.id.payButton);
            this.expenseLabel = (TextView) itemView.findViewById(R.id.expenseLabel);
            this.adjustButton.setOnClickListener(this);
            this.nameWrapper.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (WalletTransactionAdapter.this.listener != null) {
                WalletTransactionAdapter.this.listener.OnItemClick(view, 0);
            }
        }
    }

    /* loaded from: classes3.dex */
    private class TitleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView allLabel;

        public TitleViewHolder(View itemView) {
            super(itemView);
            TextView textView = (TextView) itemView.findViewById(R.id.allLabel);
            this.allLabel = textView;
            textView.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (WalletTransactionAdapter.this.listener != null) {
                WalletTransactionAdapter.this.listener.OnItemClick(view, 1);
            }
        }
    }

    /* loaded from: classes3.dex */
    private class TransactionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView amountLabel;
        ConstraintLayout colorView;
        View divider;
        ImageView imageView;
        TextView nameLabel;
        TextView transLabel;

        TransactionViewHolder(View itemView) {
            super(itemView);
            this.colorView = (ConstraintLayout) itemView.findViewById(R.id.colorView);
            this.nameLabel = (TextView) itemView.findViewById(R.id.nameLabel);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.amountLabel = (TextView) itemView.findViewById(R.id.amountLabel);
            this.transLabel = (TextView) itemView.findViewById(R.id.transLabel);
            this.divider = itemView.findViewById(R.id.divider);
            itemView.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (WalletTransactionAdapter.this.listener != null) {
                WalletTransactionAdapter.this.listener.OnItemClick(view, getLayoutPosition() - (WalletTransactionAdapter.this.isPremium ? 3 : 2));
            }
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
}