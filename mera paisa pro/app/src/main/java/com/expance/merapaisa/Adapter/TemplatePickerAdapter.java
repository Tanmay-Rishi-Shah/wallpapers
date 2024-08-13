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
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.expance.merapaisa.Model.Template;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.DataHelper;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class TemplatePickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    List<Template> list = new ArrayList();
    OnItemClickListener listener;

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public TemplatePickerAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setList(List<Template> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<Template> getList() {
        return this.list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(this.inflater.inflate(R.layout.list_template_picker, parent, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Template template = this.list.get(position);
        String name = template.getName();
        String color = (template.getColor() == null || template.getColor().length() == 0) ? "#A7A9AB" : template.getColor();
        int icon = template.getIcon();
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.nameLabel.setText(name);
        viewHolder.imageView.setImageResource(DataHelper.getCategoryIcons().get(icon).intValue());
        if (Build.VERSION.SDK_INT >= 29) {
            viewHolder.colorView.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor(color), BlendMode.SRC_OVER));
        } else {
            viewHolder.colorView.getBackground().setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_OVER);
        }
    }

    /* loaded from: classes3.dex */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ConstraintLayout colorView;
        ImageView imageView;
        TextView nameLabel;

        ViewHolder(View itemView) {
            super(itemView);
            this.nameLabel = (TextView) itemView.findViewById(R.id.nameLabel);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.colorView = (ConstraintLayout) itemView.findViewById(R.id.colorView);
            itemView.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (TemplatePickerAdapter.this.listener != null) {
                TemplatePickerAdapter.this.listener.onItemClick(view, getLayoutPosition());
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
