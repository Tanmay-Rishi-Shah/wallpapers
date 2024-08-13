package com.expance.merapaisa.Adapter;

import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.expance.merapaisa.Model.Template;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Widget.SwipeAndDragViewHelper;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class TemplateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SwipeAndDragViewHelper.ActionCompletionContract {
    Context context;
    LayoutInflater inflater;
    public List<Template> list = new ArrayList();
    OnItemClickListener listener;
    private ItemTouchHelper touchHelper;

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void OnItemClick(View v, int position, int type);
    }

    public TemplateAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setTransList(List<Template> templateList) {
        this.list = templateList;
        notifyDataSetChanged();
    }

    public List<Template> getList() {
        return this.list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(this.inflater.inflate(R.layout.list_template, parent, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        Template template = this.list.get(position);
        String name = template.getName();
        String color = (template.getColor() == null || template.getColor().length() == 0) ? "#A7A9AB" : template.getColor();
        int icon = template.getIcon();
        viewHolder.nameLabel.setText(name);
        viewHolder.imageView.setImageResource(DataHelper.getCategoryIcons().get(icon).intValue());
        if (Build.VERSION.SDK_INT >= 29) {
            viewHolder.colorView.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor(color), BlendMode.SRC_OVER));
        } else {
            viewHolder.colorView.getBackground().setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_OVER);
        }
        viewHolder.reorderImage.setOnTouchListener(new View.OnTouchListener() { // from class: com.ktwapps.walletmanager.Adapter.TemplateAdapter.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getActionMasked() == 0) {
                    TemplateAdapter.this.touchHelper.startDrag(viewHolder);
                    return false;
                }
                return false;
            }
        });
    }

    /* loaded from: classes3.dex */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView amountLabel;
        ConstraintLayout colorView;
        ImageView deleteImage;
        ImageView imageView;
        TextView nameLabel;
        ImageView reorderImage;

        public ViewHolder(View itemView) {
            super(itemView);
            this.colorView = (ConstraintLayout) itemView.findViewById(R.id.colorView);
            this.nameLabel = (TextView) itemView.findViewById(R.id.nameLabel);
            this.amountLabel = (TextView) itemView.findViewById(R.id.amountLabel);
            this.deleteImage = (ImageView) itemView.findViewById(R.id.deleteImage);
            this.reorderImage = (ImageView) itemView.findViewById(R.id.reorderImage);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
            this.deleteImage.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (TemplateAdapter.this.listener != null) {
                if (view.getId() == R.id.deleteImage) {
                    TemplateAdapter.this.listener.OnItemClick(view, getLayoutPosition(), -25);
                } else {
                    TemplateAdapter.this.listener.OnItemClick(view, getLayoutPosition(), 25);
                }
            }
        }
    }

    @Override // com.ktwapps.walletmanager.Widget.SwipeAndDragViewHelper.ActionCompletionContract
    public void onViewMoved(int oldPosition, int newPosition) {
        this.list.remove(oldPosition);
        this.list.add(newPosition, this.list.get(oldPosition));
        notifyItemMoved(oldPosition, newPosition);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.list.size();
    }

    public void setTouchHelper(ItemTouchHelper touchHelper) {
        this.touchHelper = touchHelper;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
