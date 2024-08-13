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
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes3.dex */
public class WalletMultiplePickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    OnItemClickListener listener;
    public List<Wallets> list = new ArrayList();
    ArrayList<Integer> walletIds = new ArrayList<>();

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public WalletMultiplePickerAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public ArrayList<Integer> getWalletIds() {
        return this.walletIds;
    }

    public void setWalletIds(ArrayList<Integer> walletIds) {
        this.walletIds = walletIds;
    }

    public void setList(List<Wallets> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void selectOrDeselect(int id) {
        if (this.walletIds.contains(Integer.valueOf(id))) {
            Iterator<Integer> it = this.walletIds.iterator();
            int i = 0;
            while (true) {
                if (!it.hasNext()) {
                    break;
                } else if (it.next().intValue() == id) {
                    this.walletIds.remove(i);
                    break;
                } else {
                    i++;
                }
            }
        } else {
            this.walletIds.add(Integer.valueOf(id));
        }
        notifyDataSetChanged();
    }

    public boolean selectOrDeselectAll() {
        if (this.walletIds.size() == this.list.size()) {
            this.walletIds.clear();
            notifyDataSetChanged();
            return false;
        }
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (Wallets wallets : this.list) {
            arrayList.add(Integer.valueOf(wallets.getId()));
        }
        this.walletIds = arrayList;
        notifyDataSetChanged();
        return true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WalletHolder(this.inflater.inflate(R.layout.list_wallet_picker, parent, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String str;
        WalletHolder walletHolder = (WalletHolder) holder;
        Wallets wallets = this.list.get(position);
        int id = wallets.getId();
        String name = wallets.getName();
        String color = wallets.getColor();
        if (wallets.getCurrency() == null) {
            str = SharePreferenceHelper.getAccountSymbol(this.context);
        } else {
            str = DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(wallets.getCurrency()));
        }
        String beautifyAmount = Helper.getBeautifyAmount(str, wallets.getAmount());
        if (Build.VERSION.SDK_INT >= 29) {
            walletHolder.colorView.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor(color), BlendMode.SRC_OVER));
        } else {
            walletHolder.colorView.getBackground().setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_OVER);
        }
        walletHolder.nameLabel.setText(name);
        walletHolder.amountLabel.setText(beautifyAmount);
        walletHolder.imageView.setImageResource(DataHelper.getWalletIcons().get(wallets.getIcon()).intValue());
        if (this.walletIds.contains(Integer.valueOf(id))) {
            walletHolder.doneWrapper.setBackground(this.context.getResources().getDrawable(R.drawable.background_box_checked));
            walletHolder.doneImage.setVisibility(0);
        } else {
            ConstraintLayout constraintLayout = walletHolder.doneWrapper;
            Context context = this.context;
            constraintLayout.setBackground(ContextCompat.getDrawable(context, Helper.getAttributeDrawable(context, R.attr.uncheckBackground)));
            walletHolder.doneImage.setVisibility(8);
        }
        if (wallets.getId() == 0) {
            walletHolder.imageView.setImageResource(R.drawable.all);
        } else {
            walletHolder.imageView.setImageResource(DataHelper.getWalletIcons().get(wallets.getIcon()).intValue());
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
            if (WalletMultiplePickerAdapter.this.listener != null) {
                WalletMultiplePickerAdapter.this.listener.onItemClick(view, getLayoutPosition());
            }
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.list.size();
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
