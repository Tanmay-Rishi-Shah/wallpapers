package com.expance.merapaisa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.expance.merapaisa.Model.Currencies;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.DataHelper;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes3.dex */
public class CurrencyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    LayoutInflater layoutInflater;
    List<Currencies> list = new ArrayList();
    OnItemClickListener listener;

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void OnItemClick(View v, int position, int type);
    }

    public CurrencyAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setList(List<Currencies> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<Currencies> getList() {
        return this.list;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CurrencyHolder(this.layoutInflater.inflate(R.layout.list_currency, parent, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Currencies currencies = this.list.get(position);
        String mainCurrency = currencies.getMainCurrency();
        String subCurrency = currencies.getSubCurrency();
        double rate = currencies.getRate();
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.toLocalizedPattern();
        decimalFormat.setMaximumFractionDigits(8);
        decimalFormat.setMinimumFractionDigits(2);
        decimalFormat.setGroupingUsed(false);
        CurrencyHolder currencyHolder = (CurrencyHolder) holder;
        currencyHolder.subLabel.setText(DataHelper.getCurrencyList().get(DataHelper.getCurrencyCode().indexOf(subCurrency)));
        currencyHolder.mainLabel.setText(subCurrency + " 1.00 = " + mainCurrency + StringUtils.SPACE + decimalFormat.format(rate));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.list.size();
    }

    /* loaded from: classes3.dex */
    public class CurrencyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView deleteImage;
        TextView mainLabel;
        TextView subLabel;

        CurrencyHolder(View itemView) {
            super(itemView);
            this.mainLabel = (TextView) itemView.findViewById(R.id.mainLabel);
            this.subLabel = (TextView) itemView.findViewById(R.id.subLabel);
            this.deleteImage = (ImageView) itemView.findViewById(R.id.deleteImage);
            itemView.setOnClickListener(this);
            this.deleteImage.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (CurrencyAdapter.this.listener != null) {
                if (view.getId() == R.id.deleteImage) {
                    CurrencyAdapter.this.listener.OnItemClick(view, getLayoutPosition(), 2);
                } else {
                    CurrencyAdapter.this.listener.OnItemClick(view, getLayoutPosition(), 1);
                }
            }
        }
    }
}
