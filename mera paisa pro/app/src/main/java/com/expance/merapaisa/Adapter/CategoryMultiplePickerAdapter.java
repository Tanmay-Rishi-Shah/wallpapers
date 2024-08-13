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
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Utility.Helper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes3.dex */
public class CategoryMultiplePickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    OnItemClickListener listener;
    public List<Category> list = new ArrayList();
    ArrayList<Integer> categoryIds = new ArrayList<>();

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public CategoryMultiplePickerAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public ArrayList<Integer> getCategoryIds() {
        return this.categoryIds;
    }

    public void setCategoryIds(ArrayList<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public void setList(List<Category> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void selectOrDeselect(int id) {
        if (this.categoryIds.contains(Integer.valueOf(id))) {
            Iterator<Integer> it = this.categoryIds.iterator();
            int i = 0;
            while (true) {
                if (!it.hasNext()) {
                    break;
                } else if (it.next().intValue() == id) {
                    this.categoryIds.remove(i);
                    break;
                } else {
                    i++;
                }
            }
        } else {
            this.categoryIds.add(Integer.valueOf(id));
        }
        notifyDataSetChanged();
    }

    public boolean selectOrDeselectAll() {
        if (this.categoryIds.size() == this.list.size()) {
            this.categoryIds.clear();
            notifyDataSetChanged();
            return false;
        }
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (Category category : this.list) {
            arrayList.add(Integer.valueOf(category.getId()));
        }
        this.categoryIds = arrayList;
        notifyDataSetChanged();
        return true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryHolder(this.inflater.inflate(R.layout.list_category_multiple, parent, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CategoryHolder categoryHolder = (CategoryHolder) holder;
        Category category = this.list.get(position);
        int id = category.getId();
        categoryHolder.categoryLabel.setText(category.getName(this.context));
        if (Build.VERSION.SDK_INT >= 29) {
            categoryHolder.colorView.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor(category.getColor()), BlendMode.SRC_OVER));
        } else {
            categoryHolder.colorView.getBackground().setColorFilter(Color.parseColor(category.getColor()), PorterDuff.Mode.SRC_OVER);
        }
        if (this.categoryIds.contains(Integer.valueOf(id))) {
            categoryHolder.doneWrapper.setBackground(this.context.getResources().getDrawable(R.drawable.background_box_checked));
            categoryHolder.doneImage.setVisibility(0);
        } else {
            ConstraintLayout constraintLayout = categoryHolder.doneWrapper;
            Context context = this.context;
            constraintLayout.setBackground(ContextCompat.getDrawable(context, Helper.getAttributeDrawable(context, R.attr.uncheckBackground)));
            categoryHolder.doneImage.setVisibility(8);
        }
        if (category.getId() == 0) {
            categoryHolder.imageView.setImageResource(R.drawable.all_category);
        } else {
            categoryHolder.imageView.setImageResource(DataHelper.getCategoryIcons().get(category.getIcon()).intValue());
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
            if (CategoryMultiplePickerAdapter.this.listener != null) {
                CategoryMultiplePickerAdapter.this.listener.onItemClick(view, getLayoutPosition());
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
