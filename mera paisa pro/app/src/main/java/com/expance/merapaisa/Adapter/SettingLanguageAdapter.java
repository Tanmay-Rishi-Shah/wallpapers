package com.expance.merapaisa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.DataHelper;
import java.util.List;

/* loaded from: classes3.dex */
public class SettingLanguageAdapter extends BaseAdapter {
    private Context context;
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

    public SettingLanguageAdapter(Context context, List<String> list) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
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
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.list_setting_radio, (ViewGroup) null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String language = DataHelper.getLanguage(this.context);
        viewHolder.radioButton.setText(str);
        if (language.equals(str)) {
            viewHolder.radioGroup.check(R.id.radioButton);
        } else {
            viewHolder.radioGroup.clearCheck();
        }
        return convertView;
    }

    /* loaded from: classes3.dex */
    private class ViewHolder {
        RadioButton radioButton;
        RadioGroup radioGroup;

        ViewHolder(View itemView) {
            this.radioGroup = (RadioGroup) itemView.findViewById(R.id.radioGroup);
            this.radioButton = (RadioButton) itemView.findViewById(R.id.radioButton);
        }
    }
}
