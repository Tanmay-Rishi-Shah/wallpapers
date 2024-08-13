package com.expance.merapaisa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.expance.merapaisa.R;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class BackupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    List<File> list = new ArrayList();
    OnItemClickListener listener;

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void OnItemClick(View v, int position);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

    public BackupAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public List<File> getList() {
        return this.list;
    }

    public void setList(List<File> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new HeaderHolder(this.inflater.inflate(R.layout.list_backup_header, parent, false));
        }
        return new ViewHolder(this.inflater.inflate(R.layout.list_backup_item, parent, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 0) {
            HeaderHolder headerHolder = (HeaderHolder) holder;
            if (this.list.size() > 0) {
                headerHolder.titleLabel.setText(this.context.getResources().getString(R.string.backup_found, Integer.valueOf(this.list.size())));
                return;
            } else {
                headerHolder.titleLabel.setText(R.string.backup_empty);
                return;
            }
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.label.setText(this.list.get(position - 1).getName());
        viewHolder.divider.setVisibility(position == this.list.size() ? 8 : 0);
        viewHolder.seperator.setVisibility(position != this.list.size() ? 8 : 0);
    }

    /* loaded from: classes3.dex */
    public class HeaderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button backUpButton;
        TextView titleLabel;

        HeaderHolder(View itemView) {
            super(itemView);
            this.backUpButton = (Button) itemView.findViewById(R.id.backupButton);
            this.titleLabel = (TextView) itemView.findViewById(R.id.titleLabel);
            this.backUpButton.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (BackupAdapter.this.listener != null) {
                BackupAdapter.this.listener.OnItemClick(view, 0);
            }
        }
    }

    /* loaded from: classes3.dex */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View divider;
        TextView label;
        View seperator;
        ConstraintLayout wrapper;

        public ViewHolder(View itemView) {
            super(itemView);
            this.label = (TextView) itemView.findViewById(R.id.label);
            this.wrapper = (ConstraintLayout) itemView.findViewById(R.id.wrapper);
            this.divider = itemView.findViewById(R.id.view);
            this.seperator = itemView.findViewById(R.id.divider);
            itemView.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (BackupAdapter.this.listener != null) {
                BackupAdapter.this.listener.OnItemClick(view, getLayoutPosition() - 1);
            }
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.list.size() + 1;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
