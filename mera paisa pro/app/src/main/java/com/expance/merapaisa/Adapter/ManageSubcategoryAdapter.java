package com.expance.merapaisa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.expance.merapaisa.Database.Entity.SubcategoryEntity;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Widget.SwipeAndDragViewHelper;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class ManageSubcategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SwipeAndDragViewHelper.ActionCompletionContract {
    Context context;
    LayoutInflater inflater;
    public List<SubcategoryEntity> list = new ArrayList();
    OnItemClickListener listener;
    private ItemTouchHelper touchHelper;

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void onItemClick(View view, int position, int type);
    }

    public ManageSubcategoryAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setList(List<SubcategoryEntity> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<SubcategoryEntity> getList() {
        return this.list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryHolder(this.inflater.inflate(R.layout.list_manage_subcategory, parent, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final CategoryHolder categoryHolder = (CategoryHolder) holder;
        categoryHolder.nameLabel.setText(this.list.get(position).getName());
        categoryHolder.reorderImage.setOnTouchListener(new View.OnTouchListener() { // from class: com.ktwapps.walletmanager.Adapter.ManageSubcategoryAdapter$$ExternalSyntheticLambda0
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                return ManageSubcategoryAdapter.this.m143x943fcdb4(categoryHolder, view, motionEvent);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onBindViewHolder$0$com-ktwapps-walletmanager-Adapter-ManageSubcategoryAdapter  reason: not valid java name */
    public /* synthetic */ boolean m143x943fcdb4(CategoryHolder categoryHolder, View view, MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 0) {
            this.touchHelper.startDrag(categoryHolder);
            return false;
        }
        return false;
    }

    @Override // com.ktwapps.walletmanager.Widget.SwipeAndDragViewHelper.ActionCompletionContract
    public void onViewMoved(int oldPosition, int newPosition) {
        this.list.remove(oldPosition);
        this.list.add(newPosition, this.list.get(oldPosition));
        notifyItemMoved(oldPosition, newPosition);
    }

    public void removeItem(int position) {
        this.list.remove(position);
        notifyItemRemoved(position);
    }

    public void setTouchHelper(ItemTouchHelper touchHelper) {
        this.touchHelper = touchHelper;
    }

    /* loaded from: classes3.dex */
    public class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView deleteImage;
        TextView nameLabel;
        ImageView reorderImage;

        CategoryHolder(View itemView) {
            super(itemView);
            this.nameLabel = (TextView) itemView.findViewById(R.id.nameLabel);
            this.deleteImage = (ImageView) itemView.findViewById(R.id.deleteImage);
            this.reorderImage = (ImageView) itemView.findViewById(R.id.reorderImage);
            this.deleteImage.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ManageSubcategoryAdapter.this.listener != null) {
                if (view.getId() == R.id.deleteImage) {
                    ManageSubcategoryAdapter.this.listener.onItemClick(view, getLayoutPosition(), 23);
                } else {
                    ManageSubcategoryAdapter.this.listener.onItemClick(view, getLayoutPosition(), 21);
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
