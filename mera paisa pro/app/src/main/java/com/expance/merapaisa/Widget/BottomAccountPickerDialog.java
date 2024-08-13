package com.expance.merapaisa.Widget;

import android.app.Dialog;
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
import com.expance.merapaisa.AccountCreateName;
import com.expance.merapaisa.Adapter.AccountAdapter;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.ViewModel.AccountViewModel;
import com.expance.merapaisa.MainActivity;
import com.expance.merapaisa.Model.Account;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

/* loaded from: classes3.dex */
public class BottomAccountPickerDialog extends BottomSheetDialogFragment implements AccountAdapter.OnItemClickListener {
    AccountAdapter accountAdapter;
    AccountViewModel accountViewModel;
    OnItemClickListener listener;
    RecyclerView recyclerView;

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override // com.google.android.material.bottomsheet.BottomSheetDialogFragment, androidx.appcompat.app.AppCompatDialogFragment, androidx.fragment.app.DialogFragment
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            dismiss();
        }
        return super.onCreateDialog(savedInstanceState);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.bottom_account_picker_layout, container, false);
        this.accountAdapter = new AccountAdapter(getActivity());
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerView);
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView.setAdapter(this.accountAdapter);
        this.accountAdapter.setListener(this);
        AccountViewModel accountViewModel = (AccountViewModel) new ViewModelProvider(this).get(AccountViewModel.class);
        this.accountViewModel = accountViewModel;
        accountViewModel.getAccountList().observe(this, new AnonymousClass1());
        return inflate;
    }

    /* renamed from: com.ktwapps.walletmanager.Widget.BottomAccountPickerDialog$1  reason: invalid class name */
    /* loaded from: classes3.dex */
    class AnonymousClass1 implements Observer<List<Account>> {
        AnonymousClass1() {
        }

        @Override // androidx.lifecycle.Observer
        public void onChanged(final List<Account> accounts) {
            Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.Widget.BottomAccountPickerDialog.1.1
                @Override // java.lang.Runnable
                public void run() {
                    AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(BottomAccountPickerDialog.this.getActivity());
                    final ArrayList arrayList = new ArrayList();
                    for (Account account : accounts) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(14, 0);
                        calendar.set(13, 0);
                        calendar.set(12, 0);
                        calendar.set(11, 0);
                        calendar.add(5, 1);
                        Long accountBalance = appDatabaseObject.accountDaoObject().getAccountBalance(account.getId(), 0, calendar.getTimeInMillis());
                        if (accountBalance == null) {
                            accountBalance = 0L;
                        }
                        account.setBalance(accountBalance.longValue());
                        arrayList.add(account);
                    }
                    BottomAccountPickerDialog.this.getActivity().runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.Widget.BottomAccountPickerDialog.1.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            BottomAccountPickerDialog.this.accountAdapter.setList(arrayList);
                        }
                    });
                }
            });
        }
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override // com.ktwapps.walletmanager.Adapter.AccountAdapter.OnItemClickListener
    public void onItemClick(View view, int position) {
        if (this.accountAdapter.getList().size() + 1 == position) {
            startActivity(new Intent(getActivity(), AccountCreateName.class));
            getActivity().overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
            dismiss();
            return;
        }
        int accountId = SharePreferenceHelper.getAccountId(getActivity());
        int i = position - 1;
        int id = this.accountAdapter.getList().get(i).getId();
        String currencySymbol = this.accountAdapter.getList().get(i).getCurrencySymbol();
        String name = this.accountAdapter.getList().get(i).getName();
        if (accountId != id) {
            SharePreferenceHelper.setAccount(getActivity(), id, currencySymbol, name);
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(268468224);
            startActivity(intent);
        }
        dismiss();
    }
}
