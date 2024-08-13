package com.expance.merapaisa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.expance.merapaisa.R;

/* loaded from: classes3.dex */
public class ProAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    LayoutInflater layoutInflater;
    int[] title = {R.string.pro_title_1, R.string.pro_title_2, R.string.pro_title_4, R.string.pro_title_5, R.string.pro_title_7, R.string.pro_title_6};
    int[] detail = {R.string.pro_detail_1, R.string.pro_detail_2, R.string.pro_detail_4, R.string.pro_detail_5, R.string.pro_detail_7, R.string.pro_detail_6};
    int[] image = {R.drawable.pro_fingerprint, R.drawable.pro_no_ads, R.drawable.pro_recurring, R.drawable.pro_photo, R.drawable.pro_theme, R.drawable.pro_other};

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return 6;
    }

    public ProAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(this.layoutInflater.inflate(R.layout.list_pro_item, parent, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.titleLabel.setText(this.title[position]);
        viewHolder.detailLabel.setText(this.detail[position]);
        viewHolder.imageView.setImageResource(this.image[position]);
    }

    /* loaded from: classes3.dex */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView detailLabel;
        ImageView imageView;
        TextView titleLabel;

        ViewHolder(View itemView) {
            super(itemView);
            this.titleLabel = (TextView) itemView.findViewById(R.id.titleLabel);
            this.detailLabel = (TextView) itemView.findViewById(R.id.detailLabel);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
