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
import com.expance.merapaisa.Model.Wallets;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Widget.SwipeAndDragViewHelper;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class ManageWalletAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SwipeAndDragViewHelper.ActionCompletionContract {
    Context context;
    LayoutInflater inflater;
    OnItemClickListener listener;
    private ItemTouchHelper touchHelper;
    public List<Wallets> list = new ArrayList();
    int numberOfArchive = 0;

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void OnArchiveClick();

        void OnItemClick(View v, int position, int type);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

    public ManageWalletAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setTransList(List<Wallets> walletsList) {
        this.list = walletsList;
        notifyDataSetChanged();
    }

    public void setNumberOfArchive(int num) {
        this.numberOfArchive = num;
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new ArchiveViewHolder(this.inflater.inflate(R.layout.list_manage_wallet_archived, parent, false));
        }
        return new ViewHolder(this.inflater.inflate(R.layout.list_manage_wallet, parent, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 0) {
            ((ArchiveViewHolder) holder).nameLabel.setText(this.context.getResources().getString(R.string.archived_num, Integer.valueOf(this.numberOfArchive)));
            return;
        }
        final ViewHolder viewHolder = (ViewHolder) holder;
        Wallets wallets = this.list.get(position - 1);
        String name = wallets.getName();
        String color = wallets.getColor();
        String beautifyAmount = Helper.getBeautifyAmount(DataHelper.getCurrencySymbolList().get(DataHelper.getCurrencyCode().indexOf(wallets.getCurrency())), wallets.getAmount());
        viewHolder.archiveImage.setVisibility(this.list.size() == 1 ? 8 : 0);
        viewHolder.deleteImage.setVisibility(this.list.size() == 1 ? 8 : 0);
        if (Build.VERSION.SDK_INT >= 29) {
            viewHolder.colorView.getBackground().setColorFilter(new BlendModeColorFilter(Color.parseColor(color), BlendMode.SRC_OVER));
        } else {
            viewHolder.colorView.getBackground().setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_OVER);
        }
        viewHolder.nameLabel.setText(name);
        viewHolder.amountLabel.setText(beautifyAmount);
        viewHolder.imageView.setImageResource(DataHelper.getWalletIcons().get(wallets.getIcon()).intValue());
        viewHolder.reorderImage.setOnTouchListener(new View.OnTouchListener() { // from class: com.ktwapps.walletmanager.Adapter.ManageWalletAdapter.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getActionMasked() == 0) {
                    ManageWalletAdapter.this.touchHelper.startDrag(viewHolder);
                    return false;
                }
                return false;
            }
        });
    }

    /* loaded from: classes3.dex */
    public class ArchiveViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameLabel;

        public ArchiveViewHolder(View itemView) {
            super(itemView);
            this.nameLabel = (TextView) itemView.findViewById(R.id.nameLabel);
            itemView.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            ManageWalletAdapter.this.listener.OnArchiveClick();
        }
    }

    /* loaded from: classes3.dex */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView amountLabel;
        ImageView archiveImage;
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
            this.archiveImage = (ImageView) itemView.findViewById(R.id.archiveImage);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
            this.deleteImage.setOnClickListener(this);
            this.archiveImage.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (ManageWalletAdapter.this.listener != null) {
                if (view.getId() == R.id.deleteImage) {
                    ManageWalletAdapter.this.listener.OnItemClick(view, getLayoutPosition() - 1, -25);
                } else if (view.getId() == R.id.archiveImage) {
                    ManageWalletAdapter.this.listener.OnItemClick(view, getLayoutPosition() - 1, 27);
                } else {
                    ManageWalletAdapter.this.listener.OnItemClick(view, getLayoutPosition() - 1, 25);
                }
            }
        }
    }

    @Override // com.ktwapps.walletmanager.Widget.SwipeAndDragViewHelper.ActionCompletionContract
    public void onViewMoved(int oldPosition, int newPosition) {
//        if (newPosition != 0) {
//            int i = oldPosition - 1;
//            this.list.remove(i);
//            this.list.add(newPosition - 1, this.list.get(i));
//            notifyItemMoved(oldPosition, newPosition);
//        }
        if (newPosition != 0 && oldPosition != newPosition) {
            int i = oldPosition - 1;
            Object itemToMove = this.list.get(i);
            this.list.remove(i);
            this.list.add(newPosition - 1, (Wallets) itemToMove);
            notifyItemMoved(oldPosition, newPosition);
        }

    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.list.size() + 1;
    }

    public void setTouchHelper(ItemTouchHelper touchHelper) {
        this.touchHelper = touchHelper;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
