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
import androidx.recyclerview.widget.RecyclerView;
import com.expance.merapaisa.Model.Wallets;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Utility.Helper;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class ArchivedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    public List<Wallets> list = new ArrayList();
    OnItemClickListener listener;

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void OnItemClick(View v, int position, int type);
    }

    public ArchivedAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setList(List<Wallets> walletsList) {
        this.list = walletsList;
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(this.inflater.inflate(R.layout.list_archive, parent, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Wallets wallets = this.list.get(position);
        String name = wallets.getName();
        String color = wallets.getColor();
        String beautifyAmount = Helper.getBeautifyAmount(DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(wallets.getCurrency())), wallets.getAmount());
        if (Build.VERSION.SDK_INT >= 29) {
            viewHolder.colorView.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor(color), BlendMode.SRC_OVER));
        } else {
            viewHolder.colorView.getBackground().setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_OVER);
        }
        viewHolder.nameLabel.setText(name);
        viewHolder.amountLabel.setText(beautifyAmount);
        viewHolder.imageView.setImageResource(DataHelper.getWalletIcons().get(wallets.getIcon()).intValue());
    }

    /* loaded from: classes3.dex */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView amountLabel;
        ImageView archiveImage;
        ConstraintLayout colorView;
        ImageView deleteImage;
        ImageView editImage;
        ImageView imageView;
        TextView nameLabel;

        public ViewHolder(View itemView) {
            super(itemView);
            this.colorView = (ConstraintLayout) itemView.findViewById(R.id.colorView);
            this.nameLabel = (TextView) itemView.findViewById(R.id.nameLabel);
            this.amountLabel = (TextView) itemView.findViewById(R.id.amountLabel);
            this.editImage = (ImageView) itemView.findViewById(R.id.editImage);
            this.deleteImage = (ImageView) itemView.findViewById(R.id.deleteImage);
            this.archiveImage = (ImageView) itemView.findViewById(R.id.archiveImage);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
            this.deleteImage.setOnClickListener(this);
            this.archiveImage.setOnClickListener(this);
            this.editImage.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ArchivedAdapter.this.listener != null) {
                if (view.getId() == R.id.deleteImage) {
                    ArchivedAdapter.this.listener.OnItemClick(view, getLayoutPosition(), -25);
                } else if (view.getId() == R.id.archiveImage) {
                    ArchivedAdapter.this.listener.OnItemClick(view, getLayoutPosition(), 27);
                } else if (view.getId() == R.id.editImage) {
                    ArchivedAdapter.this.listener.OnItemClick(view, getLayoutPosition(), 25);
                } else {
                    ArchivedAdapter.this.listener.OnItemClick(view, getLayoutPosition(), 2);
                }
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
