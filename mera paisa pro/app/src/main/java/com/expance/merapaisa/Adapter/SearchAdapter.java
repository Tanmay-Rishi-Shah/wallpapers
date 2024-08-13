package com.expance.merapaisa.Adapter;

import android.annotation.SuppressLint;
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

import com.expance.merapaisa.Model.SearchTrans;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Utility.Helper;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/* loaded from: classes3.dex */
public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    OnItemClickListener listener;
    List<SearchTrans> transList = new ArrayList();

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void OnItemClick(View v, int position);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

    public SearchAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setTransList(List<SearchTrans> transList) {
        this.transList = transList;
    }

    public List<SearchTrans> getTransList() {
        return this.transList;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new HeaderViewHolder(this.inflater.inflate(R.layout.list_search_header, parent, false));
        }
        return new ViewHolder(this.inflater.inflate(R.layout.list_search, parent, false));
    }


    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
        if (this.getItemViewType(var2) == 0) {
            HeaderViewHolder var12 = (HeaderViewHolder) var1;
            if (this.transList.size() > 1) {
                var12.titleLabel.setText(this.context.getResources().getString(R.string.transactions_found_plural, this.transList.size()));
            } else {
                var12.titleLabel.setText(this.context.getResources().getString(R.string.transactions_found_singular));
            }
        } else {
            SearchTrans var7 = (SearchTrans) this.transList.get(var2 - 1);
            String var4 = var7.getCurrency();
            String var8 = (String) DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(var4));
            String var11 = var7.getMedia();
            String var6 = var7.getNote(this.context);
            if (var7.getColor() == null) {
                var4 = "#A7A9AB";
            } else {
                var4 = var7.getColor();
            }

            String var5 = var7.getWallet();
            int var3 = var7.getType();
            Date var10 = var7.getDateTime();
            String var9 = Helper.getBeautifyAmount(var8, var7.getAmount());
            boolean var15;
            if (0L > var7.getAmount()) {
                var15 = true;
            } else {
                var15 = false;
            }

            var8 = (new SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), "dd/MM/yyyy"), Locale.getDefault())).format(var10);
            ViewHolder var19 = (ViewHolder) var1;
            if (var11 == null) {
                var19.imageView1.setVisibility(8);
                var19.imageView2.setVisibility(8);
                var19.imageView3.setVisibility(8);
            } else {
                String[] var13 = var11.split(",");
                if (var13.length == 1) {
                    var19.imageView1.setVisibility(0);
                    var19.imageView2.setVisibility(8);
                    var19.imageView3.setVisibility(8);
                    Picasso.get().load(new File(var13[0])).centerCrop().resize(100, 100).into(var19.imageView1);
                } else if (var13.length == 2) {
                    var19.imageView1.setVisibility(0);
                    var19.imageView2.setVisibility(0);
                    var19.imageView3.setVisibility(8);
                    Picasso.get().load(new File(var13[0])).centerCrop().resize(100, 100).into(var19.imageView1);
                    Picasso.get().load(new File(var13[1])).centerCrop().resize(100, 100).into(var19.imageView2);
                } else if (var13.length == 3) {
                    var19.imageView1.setVisibility(0);
                    var19.imageView2.setVisibility(0);
                    var19.imageView3.setVisibility(0);
                    Picasso.get().load(new File(var13[0])).centerCrop().resize(100, 100).into(var19.imageView1);
                    Picasso.get().load(new File(var13[1])).centerCrop().resize(100, 100).into(var19.imageView2);
                    Picasso.get().load(new File(var13[2])).centerCrop().resize(100, 100).into(var19.imageView3);
                }
            }

            String var16;
            if (var3 == 2) {
                StringBuilder var14 = new StringBuilder();
                var14.append(var5);
                var14.append(this.context.getResources().getString(2131821192));
                var14.append(var7.getTransferWallet());
                var16 = var14.toString();
                var19.imageView.setImageResource(2131165730);
            } else {
                var19.imageView.setImageResource((Integer) DataHelper.getCategoryIcons().get(var7.getIcon()));
                var16 = var5;
            }

            var19.amountLabel.setText(var9);
            TextView var17 = var19.amountLabel;
            if (var7.getType() == 2) {
                var2 = Helper.getAttributeColor(this.context, 2130903970);
            } else {
                Resources var18 = this.context.getResources();
                if (var15) {
                    var2 = 2131034246;
                } else {
                    var2 = 2131034255;
                }

                var2 = var18.getColor(var2);
            }

            var17.setTextColor(var2);
            var19.nameLabel.setText(var6);
            if (Build.VERSION.SDK_INT >= 29) {
                var19.colorView.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor(var4), BlendMode.SRC_OVER));
            } else {
                var19.colorView.getBackground().setColorFilter(Color.parseColor(var4), PorterDuff.Mode.SRC_OVER);
            }

            var19.detailLabel.setText(var16);
            var19.timeLabel.setText(var8);
        }
    }

    /* loaded from: classes3.dex */
    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView titleLabel;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            this.titleLabel = (TextView) itemView.findViewById(R.id.titleLabel);
        }
    }

    /* loaded from: classes3.dex */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView amountLabel;
        View colorView;
        TextView detailLabel;
        ImageView imageView;
        ImageView imageView1;
        ImageView imageView2;
        ImageView imageView3;
        TextView nameLabel;
        TextView timeLabel;

        ViewHolder(View itemView) {
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
            SearchAdapter.this.listener.OnItemClick(view, getLayoutPosition() - 1);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        if (this.transList.size() == 0) {
            return 0;
        }
        return this.transList.size() + 1;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
