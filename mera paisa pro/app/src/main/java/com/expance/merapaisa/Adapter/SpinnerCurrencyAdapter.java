package com.expance.merapaisa.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.Helper;
import java.util.List;

/* loaded from: classes3.dex */
public class SpinnerCurrencyAdapter extends ArrayAdapter<String> {
    Context context;
    LayoutInflater inflater;
    List<String> list;

    public SpinnerCurrencyAdapter(Activity context, int resourceId, int textViewId, List<String> list) {
        super(context, resourceId, textViewId, list);
        this.context = context;
        this.list = list;
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        return rowView(convertView, position);
    }

    @Override // android.widget.ArrayAdapter, android.widget.BaseAdapter, android.widget.SpinnerAdapter
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return rowView(convertView, position);
    }

    private View rowView(View convertView, int position) {
        View view;
        viewHolder viewholder;
        if (convertView == null) {
            viewholder = new viewHolder();
            LayoutInflater from = LayoutInflater.from(getContext());
            this.inflater = from;
            view = from.inflate(R.layout.list_drop_down_text, (ViewGroup) null, false);
            viewholder.label = (TextView) view.findViewById(R.id.label);
            view.setTag(viewholder);
        } else {
            view = convertView;
            viewholder = (viewHolder) convertView.getTag();
        }
        viewholder.label.setText(this.list.get(position));
        if (position == this.list.size() - 1) {
            viewholder.label.setTextColor(this.context.getResources().getColor(R.color.blue));
        } else {
            viewholder.label.setTextColor(Helper.getAttributeColor(this.context, R.attr.primaryTextColor));
        }
        return view;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class viewHolder {
        TextView label;

        private viewHolder() {
        }
    }
}
