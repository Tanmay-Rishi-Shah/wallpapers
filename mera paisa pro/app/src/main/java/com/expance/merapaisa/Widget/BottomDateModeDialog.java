package com.expance.merapaisa.Widget;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.expance.merapaisa.Adapter.DateModeAdapter;
import com.expance.merapaisa.R;

/* loaded from: classes3.dex */
public class BottomDateModeDialog extends BottomSheetDialogFragment implements DateModeAdapter.OnItemClickListener {
    DateModeAdapter adapter;
    OnItemClickListener listener;
    int mode;
    RecyclerView recyclerView;

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void onItemClick(int mode);
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

    public void setMode(int mode) {
        this.mode = mode;
        DateModeAdapter dateModeAdapter = this.adapter;
        if (dateModeAdapter != null) {
            dateModeAdapter.setMode(mode);
            this.adapter.notifyDataSetChanged();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View inflate = inflater.inflate(R.layout.bottom_date_mode_layout, container, false);
        getDialog().setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.ktwapps.walletmanager.Widget.BottomDateModeDialog.1
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface dialogInterface) {
                View findViewById = ((BottomSheetDialog) dialogInterface).findViewById(R.id.design_bottom_sheet);
                BottomSheetBehavior.from(findViewById).setState(3);
                BottomSheetBehavior.from(findViewById).setPeekHeight(inflate.getHeight());
            }
        });
        DateModeAdapter dateModeAdapter = new DateModeAdapter(getActivity());
        this.adapter = dateModeAdapter;
        dateModeAdapter.setMode(this.mode);
        this.adapter.notifyDataSetChanged();
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerView);
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView.setAdapter(this.adapter);
        getDialog().setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.ktwapps.walletmanager.Widget.BottomDateModeDialog.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface dialogInterface) {
                View findViewById = ((BottomSheetDialog) dialogInterface).findViewById(R.id.design_bottom_sheet);
                BottomSheetBehavior.from(findViewById).setState(3);
                BottomSheetBehavior.from(findViewById).setPeekHeight(inflate.getHeight());
            }
        });
        this.adapter.setListener(this);
        return inflate;
    }

    @Override // com.ktwapps.walletmanager.Adapter.DateModeAdapter.OnItemClickListener
    public void onItemClick(View view, int position) {
        int i = 0;
        switch (position) {
            case 1:
                i = 1;
                break;
            case 2:
                i = 2;
                break;
            case 3:
                i = 3;
                break;
            case 4:
                i = 4;
                break;
            case 5:
                i = 5;
                break;
            case 6:
                i = 6;
                break;
        }
        this.listener.onItemClick(i);
        dismiss();
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
