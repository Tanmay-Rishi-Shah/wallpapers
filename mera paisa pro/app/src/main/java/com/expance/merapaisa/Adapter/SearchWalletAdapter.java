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
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.expance.merapaisa.Model.Wallets;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.DataHelper;
import java.util.List;

/* loaded from: classes3.dex */
public class SearchWalletAdapter extends BaseAdapter {
    private List<Integer> ids;
    private LayoutInflater inflater;
    private List<Wallets> list;

    @Override // android.widget.Adapter
    public Object getItem(int position) {
        return null;
    }

    @Override // android.widget.Adapter
    public long getItemId(int position) {
        return 0L;
    }

    public SearchWalletAdapter(List<Wallets> walletsList, List<Integer> ids, Context context) {
        this.list = walletsList;
        this.ids = ids;
        this.inflater = LayoutInflater.from(context);
    }

    public List<Integer> getIds() {
        return this.ids;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.list.size();
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        Wallets wallets = this.list.get(position);
        String name = wallets.getName();
        String color = wallets.getColor();
        final Integer valueOf = Integer.valueOf(wallets.getId());
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.list_search_wallet, (ViewGroup) null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.nameLabel.setText(name);
        viewHolder.imageView.setImageResource(DataHelper.getWalletIcons().get(wallets.getIcon()).intValue());
        if (Build.VERSION.SDK_INT >= 29) {
            viewHolder.colorView.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor(color), BlendMode.SRC_OVER));
        } else {
            viewHolder.colorView.getBackground().setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_OVER);
        }
        viewHolder.checkBox.setChecked(this.ids.contains(valueOf));
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() { // from class: com.ktwapps.walletmanager.Adapter.SearchWalletAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (SearchWalletAdapter.this.ids.contains(valueOf)) {
                    SearchWalletAdapter.this.ids.remove(valueOf);
                } else {
                    SearchWalletAdapter.this.ids.add(valueOf);
                }
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() { // from class: com.ktwapps.walletmanager.Adapter.SearchWalletAdapter.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (SearchWalletAdapter.this.ids.contains(valueOf)) {
                    SearchWalletAdapter.this.ids.remove(valueOf);
                } else {
                    SearchWalletAdapter.this.ids.add(valueOf);
                }
                viewHolder.checkBox.toggle();
            }
        });
        return convertView;
    }

    /* loaded from: classes3.dex */
    private class ViewHolder {
        CheckBox checkBox;
        View colorView;
        ImageView imageView;
        TextView nameLabel;

        ViewHolder(View itemView) {
            this.colorView = itemView.findViewById(R.id.colorView);
            this.nameLabel = (TextView) itemView.findViewById(R.id.nameLabel);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
        }
    }
}
