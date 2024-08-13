package com.expance.merapaisa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.Helper;

/* loaded from: classes3.dex */
public class DateModeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private OnItemClickListener listener;
    private int mode = 6;
    int[] date = {R.string.daily, R.string.weekly, R.string.monthly, R.string.quarterly, R.string.yearly, R.string.all, R.string.custom};
    int[] drawable = {R.drawable.daily, R.drawable.weekly, R.drawable.monthly, R.drawable.quarterly, R.drawable.yearly, R.drawable.all, R.drawable.edit};

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return 7;
    }

    public DateModeAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public OnItemClickListener getListener() {
        return this.listener;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(this.inflater.inflate(R.layout.list_date_mode, parent, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.imageView.setImageResource(this.drawable[position]);
        viewHolder.textView.setText(this.date[position]);
        if (position == this.mode) {
            viewHolder.doneWrapper.setBackground(this.context.getResources().getDrawable(R.drawable.background_box_checked));
            viewHolder.done.setVisibility(0);
            return;
        }
        ConstraintLayout constraintLayout = viewHolder.doneWrapper;
        Context context = this.context;
        constraintLayout.setBackground(ContextCompat.getDrawable(context, Helper.getAttributeDrawable(context, R.attr.uncheckBackground)));
        viewHolder.done.setVisibility(8);
    }

    /* loaded from: classes3.dex */
    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView done;
        ConstraintLayout doneWrapper;
        ImageView imageView;
        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.textView);
            this.done = (ImageView) itemView.findViewById(R.id.doneImage);
            this.doneWrapper = (ConstraintLayout) itemView.findViewById(R.id.doneWrapper);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (DateModeAdapter.this.listener != null) {
                DateModeAdapter.this.listener.onItemClick(view, getLayoutPosition());
            }
        }
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
