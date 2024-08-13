package com.expance.merapaisa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.expance.merapaisa.Model.SettingItem;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.ArrayList;

/* loaded from: classes3.dex */
public class SettingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    private boolean isPremium;
    ArrayList<Object> list;
    OnItemClickListener listener;

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public SettingAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setPremium(boolean premium) {
        this.isPremium = premium;
    }

    public void setList(ArrayList<Object> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int position) {
        if (this.list.get(position) instanceof String) {
            return 0;
        }
        return ((SettingItem) this.list.get(position)).isCheckBox() ? 2 : 1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new HeaderHolder(this.inflater.inflate(R.layout.list_header, parent, false));
        }
        if (viewType == 1) {
            return new SettingHolder(this.inflater.inflate(R.layout.list_setting, parent, false));
        }
        return new SwitchHolder(this.inflater.inflate(R.layout.list_setting_passcode, parent, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == 0) {
            ((HeaderHolder) holder).headerLabel.setText((String) this.list.get(position));
            return;
        }
        if (itemViewType == 1) {
            SettingItem settingItem = (SettingItem) this.list.get(position);
            SettingHolder settingHolder = (SettingHolder) holder;
            settingHolder.settingLabel.setText(settingItem.getName());
            if (settingItem.getDetail().length() > 0) {
                settingHolder.detailLabel.setVisibility(0);
                settingHolder.detailLabel.setText(settingItem.getDetail());
            } else {
                settingHolder.detailLabel.setVisibility(8);
            }
            int i = position + 1;
            settingHolder.seperator.setVisibility(this.list.get(i) instanceof String ? 0 : 8);
            settingHolder.divider.setVisibility(this.list.get(i) instanceof String ? 8 : 0);
            settingHolder.proIcon.setVisibility(8);
            if (position == 10) {
                settingHolder.proIcon.setVisibility(this.isPremium ? 8 : 0);
                settingHolder.detailLabel.setText(DataHelper.getFirstDayOfWeek(this.context));
                return;
            } else if (position == 11) {
                settingHolder.proIcon.setVisibility(this.isPremium ? 8 : 0);
                settingHolder.detailLabel.setText(DataHelper.getStartupScreen(this.context));
                return;
            } else if (position == 12) {
                settingHolder.detailLabel.setText(DataHelper.getLanguage(this.context));
                return;
            } else if (position != 13) {
                if (position == 14) {
                    settingHolder.detailLabel.setText(DataHelper.getReminderTime(this.context));
                    return;
                }
                return;
            } else {
                String string = this.context.getString(R.string.not_set);
                if (SharePreferenceHelper.checkPasscode(this.context)) {
                    string = this.context.getString(R.string.pin_code);
                    if (SharePreferenceHelper.isFingerprintEnable(this.context)) {
                        string = string + ", " + this.context.getString(R.string.fingerprint);
                    }
                }
                settingHolder.detailLabel.setText(string);
                return;
            }
        }
        SwitchHolder switchHolder = (SwitchHolder) holder;
        switchHolder.settingLabel.setText(((SettingItem) this.list.get(position)).getName());
        switchHolder.proIcon.setVisibility(8);
        if (position == 16) {
            switchHolder.detailLabel.setVisibility(0);
            switchHolder.switchView.setChecked(SharePreferenceHelper.isCarryOverOn(this.context));
            switchHolder.detailLabel.setText(R.string.carry_over_hint);
        } else if (position == 15) {
            switchHolder.detailLabel.setVisibility(8);
            switchHolder.proIcon.setVisibility(this.isPremium ? 8 : 0);
            switchHolder.switchView.setChecked(SharePreferenceHelper.isDarkMode(this.context));
        } else if (position == 17) {
            switchHolder.detailLabel.setVisibility(0);
            switchHolder.switchView.setChecked(SharePreferenceHelper.isFutureBalanceOn(this.context));
            switchHolder.detailLabel.setText(R.string.future_hint);
        }
        int i2 = position + 1;
        switchHolder.seperator.setVisibility(this.list.get(i2) instanceof String ? 0 : 8);
        switchHolder.divider.setVisibility(this.list.get(i2) instanceof String ? 8 : 0);
    }

    /* loaded from: classes3.dex */
    public class HeaderHolder extends RecyclerView.ViewHolder {
        TextView headerLabel;

        HeaderHolder(View itemView) {
            super(itemView);
            this.headerLabel = (TextView) itemView.findViewById(R.id.headerLabel);
        }
    }

    /* loaded from: classes3.dex */
    public class SwitchHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView detailLabel;
        View divider;
        ImageView proIcon;
        View seperator;
        TextView settingLabel;
        Switch switchView;

        SwitchHolder(View itemView) {
            super(itemView);
            this.switchView = (Switch) itemView.findViewById(R.id.switchView);
            this.settingLabel = (TextView) itemView.findViewById(R.id.settingLabel);
            this.detailLabel = (TextView) itemView.findViewById(R.id.detailLabel);
            this.divider = itemView.findViewById(R.id.divider);
            this.seperator = itemView.findViewById(R.id.seperator);
            this.proIcon = (ImageView) itemView.findViewById(R.id.proIcon);
            itemView.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            if (SettingAdapter.this.listener != null) {
                SettingAdapter.this.listener.onItemClick(v, getLayoutPosition());
                this.switchView.toggle();
            }
        }
    }

    /* loaded from: classes3.dex */
    public class SettingHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView detailLabel;
        View divider;
        ImageView proIcon;
        View seperator;
        TextView settingLabel;

        SettingHolder(View itemView) {
            super(itemView);
            this.settingLabel = (TextView) itemView.findViewById(R.id.settingLabel);
            this.detailLabel = (TextView) itemView.findViewById(R.id.detailLabel);
            this.divider = itemView.findViewById(R.id.divider);
            this.seperator = itemView.findViewById(R.id.seperator);
            this.proIcon = (ImageView) itemView.findViewById(R.id.proIcon);
            itemView.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (SettingAdapter.this.listener != null) {
                SettingAdapter.this.listener.onItemClick(view, getLayoutPosition());
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
