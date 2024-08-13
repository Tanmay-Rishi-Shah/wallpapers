package com.expance.merapaisa.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.Helper;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class CurrencyPickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    String currency;
    LayoutInflater inflater;
    List<String> list = new ArrayList();
    OnItemClickListener listener;

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public CurrencyPickerAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setList(List<String> list, String currency) {
        this.list = list;
        this.currency = currency;
        notifyDataSetChanged();
    }

    public void setList(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<String> getList() {
        return this.list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CurrencyHolder(this.inflater.inflate(R.layout.list_currency_picker, parent, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Drawable drawable;
        CurrencyHolder currencyHolder = (CurrencyHolder) holder;
        String str = this.list.get(position);
        currencyHolder.currencyLabel.setText(str);
        currencyHolder.doneImage.setVisibility(str.equals(this.currency) ? 0 : 8);
        ConstraintLayout constraintLayout = currencyHolder.doneWrapper;
        if (str.equals(this.currency)) {
            drawable = this.context.getResources().getDrawable(R.drawable.background_box_checked);
        } else {
            Context context = this.context;
            drawable = ContextCompat.getDrawable(context, Helper.getAttributeDrawable(context, R.attr.uncheckBackground));
        }
        constraintLayout.setBackground(drawable);
    }

    /* loaded from: classes3.dex */
    public class CurrencyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView currencyLabel;
        ImageView doneImage;
        ConstraintLayout doneWrapper;

        CurrencyHolder(View itemView) {
            super(itemView);
            this.currencyLabel = (TextView) itemView.findViewById(R.id.currencyLabel);
            this.doneImage = (ImageView) itemView.findViewById(R.id.doneImage);
            this.doneWrapper = (ConstraintLayout) itemView.findViewById(R.id.doneWrapper);
            itemView.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (CurrencyPickerAdapter.this.listener != null) {
                CurrencyPickerAdapter.this.listener.onItemClick(view, getLayoutPosition());
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
