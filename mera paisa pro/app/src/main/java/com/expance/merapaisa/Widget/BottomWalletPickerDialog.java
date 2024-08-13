package com.expance.merapaisa.Widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.expance.merapaisa.Adapter.WalletAdapter;
import com.expance.merapaisa.CreateWallet;
import com.expance.merapaisa.Database.ViewModel.WalletViewModel;
import com.expance.merapaisa.Model.Wallets;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

/* loaded from: classes3.dex */
public class BottomWalletPickerDialog extends BottomSheetDialogFragment implements WalletAdapter.OnItemClickListener {
    OnItemClickListener listener;
    RecyclerView recyclerView;
    WalletAdapter walletAdapter;
    int walletId;
    WalletViewModel walletViewModel;

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void onItemClick(int walletId);
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetDialogFragment, androidx.appcompat.app.AppCompatDialogFragment, androidx.fragment.app.DialogFragment
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            dismiss();
        }
        return super.onCreateDialog(savedInstanceState);
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
        WalletAdapter walletAdapter = this.walletAdapter;
        if (walletAdapter != null) {
            walletAdapter.notifyDataSetChanged();
        }
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.bottom_wallet_picker_layout, container, false);
        WalletAdapter walletAdapter = new WalletAdapter(getActivity());
        this.walletAdapter = walletAdapter;
        walletAdapter.setWalletId(this.walletId);
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerView);
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView.setAdapter(this.walletAdapter);
        this.walletAdapter.setListener(this);
        Calendar calendar = Calendar.getInstance();
        calendar.set(14, 0);
        calendar.set(13, 0);
        calendar.set(12, 0);
        calendar.set(11, 0);
        calendar.add(5, 1);
        if (!SharePreferenceHelper.isFutureBalanceOn(getActivity())) {
            calendar.set(1, 10000);
        }
        WalletViewModel walletViewModel = (WalletViewModel) new ViewModelProvider(this).get(WalletViewModel.class);
        this.walletViewModel = walletViewModel;
        walletViewModel.setDate(calendar.getTimeInMillis());
        this.walletViewModel.getWalletsList().observe(this, new Observer<List<Wallets>>() { // from class: com.ktwapps.walletmanager.Widget.BottomWalletPickerDialog.1
            @Override // androidx.lifecycle.Observer
            public void onChanged(final List<Wallets> wallets) {
                BottomWalletPickerDialog.this.walletAdapter.setList(wallets);
                BottomWalletPickerDialog.this.walletAdapter.notifyDataSetChanged();
            }
        });
        return inflate;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override // com.ktwapps.walletmanager.Adapter.WalletAdapter.OnItemClickListener
    public void onItemClick(View view, int position) {
        if (this.walletAdapter.getList().size() + 1 == position) {
            Intent intent = new Intent(getActivity(), CreateWallet.class);
            intent.putExtra(JamXmlElements.TYPE, 1);
            intent.putExtra("currencySymbol", SharePreferenceHelper.getAccountSymbol((Context) Objects.requireNonNull(getActivity())));
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
            return;
        }
        dismiss();
        OnItemClickListener onItemClickListener = this.listener;
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(this.walletAdapter.getList().get(position - 1).getId());
        }
    }
}
