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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Utility.Helper;
import java.util.ArrayList;

/* loaded from: classes3.dex */
public class WalletIconPickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Integer> arrayList = DataHelper.getWalletIcons();
    String color;
    Context context;
    int icon;
    LayoutInflater inflater;
    OnItemClickListener listener;

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void itemClick(View view, int position);
    }

    public WalletIconPickerAdapter(Context context, String color) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.color = color;
    }

    public void setIcon(int icon) {
        this.icon = icon;
        notifyDataSetChanged();
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(this.inflater.inflate(R.layout.list_icon, parent, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.imageView.setImageResource(this.arrayList.get(position).intValue());
        if (this.icon == position) {
            if (Build.VERSION.SDK_INT >= 29) {
                viewHolder.wrapper.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor(this.color), BlendMode.SRC_OVER));
                viewHolder.imageView.setColorFilter(new BlendModeColorFilter(Color.parseColor("#F8F8F8"), BlendMode.SRC_IN));
                return;
            }
            viewHolder.wrapper.getBackground().setColorFilter(Color.parseColor(this.color), PorterDuff.Mode.SRC_OVER);
            viewHolder.imageView.setColorFilter(Color.parseColor("#F8F8F8"), PorterDuff.Mode.SRC_IN);
        } else if (Build.VERSION.SDK_INT >= 29) {
            viewHolder.wrapper.getBackground().setColorFilter(new BlendModeColorFilter(Helper.getAttributeColor(this.context, R.attr.markerStroke), BlendMode.SRC_OVER));
            viewHolder.imageView.setColorFilter(new BlendModeColorFilter(Helper.getAttributeColor(this.context, R.attr.primaryDarkTextColor), BlendMode.SRC_IN));
        } else {
            viewHolder.wrapper.getBackground().setColorFilter(Helper.getAttributeColor(this.context, R.attr.markerStroke), PorterDuff.Mode.SRC_OVER);
            viewHolder.imageView.setColorFilter(Helper.getAttributeColor(this.context, R.attr.primaryDarkTextColor), PorterDuff.Mode.SRC_IN);
        }
    }

    /* loaded from: classes3.dex */
    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        ConstraintLayout wrapper;

        ViewHolder(View itemView) {
            super(itemView);
            this.wrapper = (ConstraintLayout) itemView.findViewById(R.id.wrapper);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.wrapper.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (WalletIconPickerAdapter.this.listener != null) {
                WalletIconPickerAdapter.this.listener.itemClick(view, getLayoutPosition());
            }
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.arrayList.size();
    }
}
