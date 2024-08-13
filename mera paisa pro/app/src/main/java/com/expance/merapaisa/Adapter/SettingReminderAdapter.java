package com.expance.merapaisa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.SharePreferenceHelper;

/* loaded from: classes3.dex */
public class SettingReminderAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;

    @Override // android.widget.Adapter
    public int getCount() {
        return 8;
    }

    @Override // android.widget.Adapter
    public Object getItem(int position) {
        return null;
    }

    @Override // android.widget.Adapter
    public long getItemId(int position) {
        return 0L;
    }

    public SettingReminderAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.list_setting_radio, (ViewGroup) null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        int reminderTime = SharePreferenceHelper.getReminderTime(this.context);
        viewHolder.radioGroup.clearCheck();
        switch (position) {
            case 0:
                viewHolder.radioButton.setText("17:00");
                if (reminderTime == 17) {
                    viewHolder.radioGroup.check(R.id.radioButton);
                    break;
                }
                break;
            case 1:
                viewHolder.radioButton.setText("18:00");
                if (reminderTime == 18) {
                    viewHolder.radioGroup.check(R.id.radioButton);
                    break;
                }
                break;
            case 2:
                viewHolder.radioButton.setText("19:00");
                if (reminderTime == 19) {
                    viewHolder.radioGroup.check(R.id.radioButton);
                    break;
                }
                break;
            case 3:
                viewHolder.radioButton.setText("20:00");
                if (reminderTime == 20) {
                    viewHolder.radioGroup.check(R.id.radioButton);
                    break;
                }
                break;
            case 4:
                viewHolder.radioButton.setText("21:00");
                if (reminderTime == 21) {
                    viewHolder.radioGroup.check(R.id.radioButton);
                    break;
                }
                break;
            case 5:
                viewHolder.radioButton.setText("22:00");
                if (reminderTime == 22) {
                    viewHolder.radioGroup.check(R.id.radioButton);
                    break;
                }
                break;
            case 6:
                viewHolder.radioButton.setText("23:00");
                if (reminderTime == 23) {
                    viewHolder.radioGroup.check(R.id.radioButton);
                    break;
                }
                break;
            default:
                viewHolder.radioButton.setText(R.string.not_set);
                if (reminderTime == 0) {
                    viewHolder.radioGroup.check(R.id.radioButton);
                    break;
                }
                break;
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
