package com.expance.merapaisa.Adapter;

import android.app.Activity;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.expance.merapaisa.R;
import java.util.List;

/* loaded from: classes3.dex */
public class SpinnerColorAdapter extends ArrayAdapter<String> {
    LayoutInflater inflater;
    List<String> list;

    public SpinnerColorAdapter(Activity context, int resourceId, int textViewId, List<String> list) {
        super(context, resourceId, textViewId, list);
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
            view = from.inflate(R.layout.list_drop_down_color, (ViewGroup) null, false);
            viewholder.label = (TextView) view.findViewById(R.id.label);
            viewholder.colorView = view.findViewById(R.id.colorView);
            view.setTag(viewholder);
        } else {
            view = convertView;
            viewholder = (viewHolder) convertView.getTag();
        }
        if (Build.VERSION.SDK_INT >= 29) {
            viewholder.colorView.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor(this.list.get(position)), BlendMode.SRC_OVER));
        } else {
            viewholder.colorView.getBackground().setColorFilter(Color.parseColor(this.list.get(position)), PorterDuff.Mode.SRC_OVER);
        }
        return view;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class viewHolder {
        View colorView;
        TextView label;

        private viewHolder() {
        }
    }
}
