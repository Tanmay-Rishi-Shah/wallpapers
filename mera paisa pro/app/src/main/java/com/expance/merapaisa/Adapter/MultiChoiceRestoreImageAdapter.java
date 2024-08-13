package com.expance.merapaisa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.expance.merapaisa.R;
import java.util.List;

/* loaded from: classes3.dex */
public class MultiChoiceRestoreImageAdapter extends BaseAdapter {
    private Context context;
    private List<Integer> icon;
    private LayoutInflater inflater;
    private List<String> list;

    @Override // android.widget.Adapter
    public Object getItem(int position) {
        return null;
    }

    @Override // android.widget.Adapter
    public long getItemId(int position) {
        return 0L;
    }

    public MultiChoiceRestoreImageAdapter(Context context, List<String> list, List<Integer> icon) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.icon = icon;
        this.context = context;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.list.size();
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        String str = this.list.get(position);
        int intValue = this.icon.get(position).intValue();
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.list_multi_choice_image, (ViewGroup) null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(str);
        viewHolder.imageView.setImageResource(intValue);
        return convertView;
    }

    /* loaded from: classes3.dex */
    private class ViewHolder {
        ImageView imageView;
        TextView textView;

        ViewHolder(View itemView) {
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.textView = (TextView) itemView.findViewById(R.id.textView);
        }
    }
}
