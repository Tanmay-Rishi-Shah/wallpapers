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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.expance.merapaisa.Model.Category;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Widget.SwipeAndDragViewHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes3.dex */
public class ManageCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SwipeAndDragViewHelper.ActionCompletionContract {
    Context context;
    LayoutInflater inflater;
    public List<Category> list = new ArrayList();
    OnItemClickListener listener;
    private ItemTouchHelper touchHelper;

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void onItemClick(View view, int position, int type);
    }

    public ManageCategoryAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setList(List<Category> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<Category> getList() {
        return this.list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryHolder(this.inflater.inflate(R.layout.list_manage_category, parent, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String str;
        final CategoryHolder categoryHolder = (CategoryHolder) holder;
        Category category = this.list.get(position);
        StringBuilder sb = new StringBuilder();
        sb.append(category.getName(this.context));
        if (category.getSubcategoryCount() > 0) {
            str = " (" + category.getSubcategoryCount() + ")";
        } else {
            str = "";
        }
        sb.append(str);
        categoryHolder.nameLabel.setText(sb.toString());
        if (category.getSubcategory() != null && category.getSubcategory().length() > 0) {
            categoryHolder.subcategoryLabel.setText(category.getSubcategory());
            categoryHolder.subcategoryLabel.setVisibility(0);
        } else {
            categoryHolder.subcategoryLabel.setVisibility(8);
        }
        categoryHolder.imageView.setImageResource(DataHelper.getCategoryIcons().get(category.getIcon()).intValue());
        if (Build.VERSION.SDK_INT >= 29) {
            categoryHolder.colorView.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor(category.getColor()), BlendMode.SRC_OVER));
        } else {
            categoryHolder.colorView.getBackground().setColorFilter(Color.parseColor(category.getColor()), PorterDuff.Mode.SRC_OVER);
        }
        categoryHolder.reorderImage.setOnTouchListener(new View.OnTouchListener() { // from class: com.ktwapps.walletmanager.Adapter.ManageCategoryAdapter$$ExternalSyntheticLambda0
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                return ManageCategoryAdapter.this.m142x6bd41bce(categoryHolder, view, motionEvent);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onBindViewHolder$0$com-ktwapps-walletmanager-Adapter-ManageCategoryAdapter  reason: not valid java name */
    public /* synthetic */ boolean m142x6bd41bce(CategoryHolder categoryHolder, View view, MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 0) {
            this.touchHelper.startDrag(categoryHolder);
            return false;
        }
        return false;
    }
    public void onViewMoved(int oldPosition, int newPosition) {
        if (oldPosition < newPosition) {
            // If the oldPosition is before the newPosition, we move the item to the new position and shift the other items accordingly.
            for (int i = oldPosition; i < newPosition; i++) {
                Collections.swap(list, i, i + 1);
            }
        } else {
            // If the oldPosition is after the newPosition, we move the item to the new position and shift the other items accordingly.
            for (int i = oldPosition; i > newPosition; i--) {
                Collections.swap(list, i, i - 1);
            }
        }

        notifyItemMoved(oldPosition, newPosition);
    }

//    @Override // com.ktwapps.walletmanager.Widget.SwipeAndDragViewHelper.ActionCompletionContract
//    public void onViewMoved(int oldPosition, int newPosition) {
//        this.list.remove(oldPosition);
//        this.list.add(newPosition, this.list.get(oldPosition));
//        notifyItemMoved(oldPosition, newPosition);
//    }


    public void removeItem(int position) {
        this.list.remove(position);
        notifyItemRemoved(position);
    }

    public void setTouchHelper(ItemTouchHelper touchHelper) {
        this.touchHelper = touchHelper;
    }

    /* loaded from: classes3.dex */
    public class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View colorView;
        ImageView deleteImage;
        ImageView editImage;
        ImageView imageView;
        TextView nameLabel;
        ImageView reorderImage;
        TextView subcategoryLabel;

        CategoryHolder(View itemView) {
            super(itemView);
            this.colorView = itemView.findViewById(R.id.colorView);
            this.nameLabel = (TextView) itemView.findViewById(R.id.nameLabel);
            this.deleteImage = (ImageView) itemView.findViewById(R.id.deleteImage);
            this.editImage = (ImageView) itemView.findViewById(R.id.editImage);
            this.reorderImage = (ImageView) itemView.findViewById(R.id.reorderImage);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.subcategoryLabel = (TextView) itemView.findViewById(R.id.subcategoryLabel);
            this.editImage.setOnClickListener(this);
            this.deleteImage.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ManageCategoryAdapter.this.listener != null) {
                if (view.getId() == R.id.deleteImage) {
                    ManageCategoryAdapter.this.listener.onItemClick(view, getLayoutPosition(), -13);
                } else if (view.getId() == R.id.editImage) {
                    ManageCategoryAdapter.this.listener.onItemClick(view, getLayoutPosition(), 20);
                } else {
                    ManageCategoryAdapter.this.listener.onItemClick(view, getLayoutPosition(), 13);
                }
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
