package com.expance.merapaisa.Adapter;

import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.expance.merapaisa.Model.Wallets;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Utility.Helper;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class WalletAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<Wallets> list = new ArrayList();
    private OnItemClickListener listener;
    private int walletId;

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public WalletAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setList(List<Wallets> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<Wallets> getList() {
        return this.list;
    }

    public void setWalletId(int id) {
        this.walletId = id;
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return position == this.list.size() + 1 ? 1 : 2;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new HeaderViewHolder(this.inflater.inflate(R.layout.list_account_header, parent, false));
        }
        if (viewType == 1) {
            return new CreateViewHolder(this.inflater.inflate(R.layout.list_account_create, parent, false));
        }
        return new WalletHolder(this.inflater.inflate(R.layout.list_wallet_picker, parent, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == 0) {
            ((HeaderViewHolder) holder).titleLabel.setText(R.string.select_wallet);
        } else if (itemViewType == 1) {
            ((CreateViewHolder) holder).createLabel.setText(R.string.create_wallet);
        } else {
            WalletHolder walletHolder = (WalletHolder) holder;
            Wallets wallets = this.list.get(position - 1);
            String name = wallets.getName();
            String color = wallets.getColor();
            String beautifyAmount = Helper.getBeautifyAmount(DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(wallets.getCurrency())), wallets.getAmount());
            if (Build.VERSION.SDK_INT >= 29) {
                walletHolder.colorView.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor(color), BlendMode.SRC_OVER));
            } else {
                walletHolder.colorView.getBackground().setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_OVER);
            }
            walletHolder.nameLabel.setText(name);
            walletHolder.amountLabel.setText(beautifyAmount);
            walletHolder.imageView.setImageResource(DataHelper.getWalletIcons().get(wallets.getIcon()).intValue());
            if (this.walletId == wallets.getId()) {
                walletHolder.doneWrapper.setBackground(this.context.getResources().getDrawable(R.drawable.background_box_checked));
                walletHolder.doneImage.setVisibility(0);
                return;
            }
            ConstraintLayout constraintLayout = walletHolder.doneWrapper;
            Context context = this.context;
            constraintLayout.setBackground(ContextCompat.getDrawable(context, Helper.getAttributeDrawable(context, R.attr.uncheckBackground)));
            walletHolder.doneImage.setVisibility(8);
        }
    }

    /* loaded from: classes3.dex */
    public class WalletHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView amountLabel;
        View colorView;
        ImageView doneImage;
        ConstraintLayout doneWrapper;
        ImageView imageView;
        TextView nameLabel;

        WalletHolder(View itemView) {
            super(itemView);
            this.colorView = itemView.findViewById(R.id.colorView);
            this.nameLabel = (TextView) itemView.findViewById(R.id.nameLabel);
            this.amountLabel = (TextView) itemView.findViewById(R.id.amountLabel);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.doneImage = (ImageView) itemView.findViewById(R.id.doneImage);
            this.doneWrapper = (ConstraintLayout) itemView.findViewById(R.id.doneWrapper);
            itemView.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (WalletAdapter.this.listener != null) {
                WalletAdapter.this.listener.onItemClick(view, getLayoutPosition());
            }
        }
    }

    /* loaded from: classes3.dex */
    private class CreateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView createLabel;

        CreateViewHolder(View itemView) {
            super(itemView);
            this.createLabel = (TextView) itemView.findViewById(R.id.createLabel);
            itemView.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            WalletAdapter.this.listener.onItemClick(view, getLayoutPosition());
        }
    }

    /* loaded from: classes3.dex */
    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView titleLabel;

        HeaderViewHolder(View itemView) {
            super(itemView);
            this.titleLabel = (TextView) itemView.findViewById(R.id.titleLabel);
        }
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.list.size() + 2;
    }
}
