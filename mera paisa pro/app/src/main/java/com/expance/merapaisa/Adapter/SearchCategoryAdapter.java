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
import com.expance.merapaisa.Model.Category;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.DataHelper;
import java.util.List;

/* loaded from: classes3.dex */
public class SearchCategoryAdapter extends BaseAdapter {
    private Context context;
    private List<Integer> ids;
    private LayoutInflater inflater;
    private List<Category> list;

    @Override // android.widget.Adapter
    public Object getItem(int position) {
        return null;
    }

    @Override // android.widget.Adapter
    public long getItemId(int position) {
        return 0L;
    }

    public SearchCategoryAdapter(List<Category> categories, List<Integer> ids, Context context) {
        this.list = categories;
        this.ids = ids;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.list.size();
    }

    public List<Integer> getIds() {
        return this.ids;
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        Category category = this.list.get(position);
        String name = category.getName(this.context);
        String color = category.getColor();
        final Integer valueOf = Integer.valueOf(category.getId());
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.list_search_category, (ViewGroup) null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.nameLabel.setText(name);
        viewHolder.imageView.setImageResource(DataHelper.getCategoryIcons().get(category.getIcon()).intValue());
        if (Build.VERSION.SDK_INT >= 29) {
            viewHolder.colorView.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor(color), BlendMode.SRC_OVER));
        } else {
            viewHolder.colorView.getBackground().setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_OVER);
        }
        viewHolder.checkBox.setChecked(this.ids.contains(valueOf));
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() { // from class: com.ktwapps.walletmanager.Adapter.SearchCategoryAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (SearchCategoryAdapter.this.ids.contains(valueOf)) {
                    SearchCategoryAdapter.this.ids.remove(valueOf);
                } else {
                    SearchCategoryAdapter.this.ids.add(valueOf);
                }
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() { // from class: com.ktwapps.walletmanager.Adapter.SearchCategoryAdapter.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (SearchCategoryAdapter.this.ids.contains(valueOf)) {
                    SearchCategoryAdapter.this.ids.remove(valueOf);
                } else {
                    SearchCategoryAdapter.this.ids.add(valueOf);
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
