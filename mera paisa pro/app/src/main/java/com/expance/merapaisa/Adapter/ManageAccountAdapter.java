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
import com.expance.merapaisa.Model.Account;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Widget.SwipeAndDragViewHelper;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class ManageAccountAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SwipeAndDragViewHelper.ActionCompletionContract {
    Context context;
    LayoutInflater inflater;
    List<Account> list = new ArrayList();
    OnItemClickListener listener;
    private ItemTouchHelper touchHelper;

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void OnItemClick(View v, int position, int type);
    }

    public ManageAccountAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setList(List<Account> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<Account> getList() {
        return this.list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(this.inflater.inflate(R.layout.list_manage_account, parent, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        Account account = this.list.get(position);
        String name = account.getName();
        String beautifyAmount = Helper.getBeautifyAmount(account.getCurrencySymbol(), account.getBalance());
        viewHolder.nameLabel.setText(name);
        viewHolder.typeLabel.setText(beautifyAmount);
        viewHolder.reorderImage.setOnTouchListener(new View.OnTouchListener() { // from class: com.ktwapps.walletmanager.Adapter.ManageAccountAdapter.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getActionMasked() == 0) {
                    ManageAccountAdapter.this.touchHelper.startDrag(viewHolder);
                    return false;
                }
                return false;
            }
        });
    }

    public void setTouchHelper(ItemTouchHelper touchHelper) {
        this.touchHelper = touchHelper;
    }

    @Override // com.ktwapps.walletmanager.Widget.SwipeAndDragViewHelper.ActionCompletionContract
    public void onViewMoved(int oldPosition, int newPosition) {
        this.list.remove(oldPosition);
        this.list.add(newPosition, this.list.get(oldPosition));
        notifyItemMoved(oldPosition, newPosition);
    }

    /* loaded from: classes3.dex */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView deleteImage;
        TextView nameLabel;
        ImageView reorderImage;
        TextView typeLabel;

        public ViewHolder(View itemView) {
            super(itemView);
            this.nameLabel = (TextView) itemView.findViewById(R.id.nameLabel);
            this.typeLabel = (TextView) itemView.findViewById(R.id.typeLabel);
            this.deleteImage = (ImageView) itemView.findViewById(R.id.deleteImage);
            this.reorderImage = (ImageView) itemView.findViewById(R.id.reorderImage);
            itemView.setOnClickListener(this);
            this.deleteImage.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ManageAccountAdapter.this.listener != null) {
                if (view.getId() == R.id.deleteImage) {
                    ManageAccountAdapter.this.listener.OnItemClick(view, getLayoutPosition(), -11);
                } else {
                    ManageAccountAdapter.this.listener.OnItemClick(view, getLayoutPosition(), 11);
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
