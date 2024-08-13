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

import com.expance.merapaisa.Model.Account;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class AccountAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<Account> list = new ArrayList();
    private OnItemClickListener listener;

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public AccountAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setList(List<Account> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public OnItemClickListener getListener() {
        return this.listener;
    }

    public List<Account> getList() {
        return this.list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return position == this.list.size() + 1 ? 1 : 2;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new HeaderViewHolder(this.inflater.inflate(R.layout.list_account_header, parent, false));
        }
        if (viewType == 1) {
            return new CreateViewHolder(this.inflater.inflate(R.layout.list_account_create, parent, false));
        }
        return new AccountViewHolder(this.inflater.inflate(R.layout.list_account, parent, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 2) {
            AccountViewHolder accountViewHolder = (AccountViewHolder) holder;
            Account account = this.list.get(position - 1);
            int id = account.getId();
            String currencySymbol = account.getCurrencySymbol();
            String name = account.getName();
            String beautifyAmount = Helper.getBeautifyAmount(currencySymbol, account.getBalance());
            accountViewHolder.name.setText(name);
            accountViewHolder.type.setText(beautifyAmount);
            if (id == SharePreferenceHelper.getAccountId(this.context)) {
                accountViewHolder.doneWrapper.setBackground(this.context.getResources().getDrawable(R.drawable.background_box_checked));
                accountViewHolder.done.setVisibility(0);
                return;
            }
            ConstraintLayout constraintLayout = accountViewHolder.doneWrapper;
            Context context = this.context;
            constraintLayout.setBackground(ContextCompat.getDrawable(context, Helper.getAttributeDrawable(context, R.attr.uncheckBackground)));
            accountViewHolder.done.setVisibility(8);
        }
    }

    /* loaded from: classes3.dex */
    private class AccountViewHolder extends RecyclerView.ViewHolder {
        ImageView done;
        ConstraintLayout doneWrapper;
        TextView name;
        TextView type;

        AccountViewHolder(View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.nameLabel);
            this.type = (TextView) itemView.findViewById(R.id.typeLabel);
            this.done = (ImageView) itemView.findViewById(R.id.doneImage);
            this.doneWrapper = (ConstraintLayout) itemView.findViewById(R.id.doneWrapper);
            itemView.setOnClickListener(new View.OnClickListener() { // from class: com.ktwapps.walletmanager.Adapter.AccountAdapter.AccountViewHolder.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    AccountAdapter.this.listener.onItemClick(view, AccountViewHolder.this.getLayoutPosition());
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class CreateViewHolder extends RecyclerView.ViewHolder {
        CreateViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() { // from class: com.ktwapps.walletmanager.Adapter.AccountAdapter$CreateViewHolder$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    AccountAdapter.CreateViewHolder.this.m141x686aea(view);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$new$0$com-ktwapps-walletmanager-Adapter-AccountAdapter$CreateViewHolder  reason: not valid java name */
        public /* synthetic */ void m141x686aea(View view) {
            AccountAdapter.this.listener.onItemClick(view, getLayoutPosition());
        }
    }

    /* loaded from: classes3.dex */
    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.list.size() + 2;
    }
}
