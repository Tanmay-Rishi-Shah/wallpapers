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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.expance.merapaisa.Model.Category;
import com.expance.merapaisa.Model.Subcategory;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Utility.Helper;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class CategoryPickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    int categoryId;
    Context context;
    LayoutInflater inflater;
    List<Object> list = new ArrayList();
    OnItemClickListener listener;
    int subcategoryId;

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public CategoryPickerAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setList(List<Object> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<Object> getList() {
        return this.list;
    }

    public void setId(int categoryId, int subcategoryId) {
        this.categoryId = categoryId;
        this.subcategoryId = subcategoryId;
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new CategoryHolder(this.inflater.inflate(R.layout.list_category, parent, false));
        }
        return new SubcategoryHolder(this.inflater.inflate(R.layout.list_subcategory, parent, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int position) {
        return this.list.get(position) instanceof Category ? 0 : 1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 0) {
            CategoryHolder categoryHolder = (CategoryHolder) holder;
            Category category = (Category) this.list.get(position);
            categoryHolder.categoryLabel.setText(category.getName(this.context));
            if (category.getId() == 0) {
                categoryHolder.imageView.setImageResource(R.drawable.all_category);
            } else {
                categoryHolder.imageView.setImageResource(DataHelper.getCategoryIcons().get(category.getIcon()).intValue());
            }
            if (this.categoryId == category.getId() && this.subcategoryId == 0) {
                categoryHolder.doneWrapper.setBackground(this.context.getResources().getDrawable(R.drawable.background_box_checked));
                categoryHolder.doneImage.setVisibility(0);
            } else {
                ConstraintLayout constraintLayout = categoryHolder.doneWrapper;
                Context context = this.context;
                constraintLayout.setBackground(ContextCompat.getDrawable(context, Helper.getAttributeDrawable(context, R.attr.uncheckBackground)));
                categoryHolder.doneImage.setVisibility(8);
            }
            if (Build.VERSION.SDK_INT >= 29) {
                categoryHolder.colorView.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor(category.getColor()), BlendMode.SRC_OVER));
                return;
            } else {
                categoryHolder.colorView.getBackground().setColorFilter(Color.parseColor(category.getColor()), PorterDuff.Mode.SRC_OVER);
                return;
            }
        }
        SubcategoryHolder subcategoryHolder = (SubcategoryHolder) holder;
        Subcategory subcategory = (Subcategory) this.list.get(position);
        subcategoryHolder.categoryLabel.setText(subcategory.getName().trim());
        if (this.subcategoryId == subcategory.getId()) {
            subcategoryHolder.doneWrapper.setBackground(this.context.getResources().getDrawable(R.drawable.background_box_checked));
            subcategoryHolder.doneImage.setVisibility(0);
        } else {
            ConstraintLayout constraintLayout2 = subcategoryHolder.doneWrapper;
            Context context2 = this.context;
            constraintLayout2.setBackground(ContextCompat.getDrawable(context2, Helper.getAttributeDrawable(context2, R.attr.uncheckBackground)));
            subcategoryHolder.doneImage.setVisibility(8);
        }
        if (Build.VERSION.SDK_INT >= 29) {
            subcategoryHolder.colorView.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor(subcategory.getColor()), BlendMode.SRC_OVER));
        } else {
            subcategoryHolder.colorView.getBackground().setColorFilter(Color.parseColor(subcategory.getColor()), PorterDuff.Mode.SRC_OVER);
        }
    }

    /* loaded from: classes3.dex */
    public class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView categoryLabel;
        View colorView;
        ImageView doneImage;
        ConstraintLayout doneWrapper;
        ImageView imageView;

        CategoryHolder(View itemView) {
            super(itemView);
            this.colorView = itemView.findViewById(R.id.colorView);
            this.categoryLabel = (TextView) itemView.findViewById(R.id.categoryLabel);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.doneImage = (ImageView) itemView.findViewById(R.id.doneImage);
            this.doneWrapper = (ConstraintLayout) itemView.findViewById(R.id.doneWrapper);
            itemView.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (CategoryPickerAdapter.this.listener != null) {
                CategoryPickerAdapter.this.listener.onItemClick(view, getLayoutPosition());
            }
        }
    }

    /* loaded from: classes3.dex */
    public class SubcategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView categoryLabel;
        View colorView;
        ImageView doneImage;
        ConstraintLayout doneWrapper;

        public SubcategoryHolder(View itemView) {
            super(itemView);
            this.colorView = itemView.findViewById(R.id.colorView);
            this.categoryLabel = (TextView) itemView.findViewById(R.id.categoryLabel);
            this.doneImage = (ImageView) itemView.findViewById(R.id.doneImage);
            this.doneWrapper = (ConstraintLayout) itemView.findViewById(R.id.doneWrapper);
            itemView.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            if (CategoryPickerAdapter.this.listener != null) {
                CategoryPickerAdapter.this.listener.onItemClick(v, getLayoutPosition());
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
