package com.expance.merapaisa.Widget;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.CalendarHelper;
import com.expance.merapaisa.Utility.DateHelper;
import java.util.Calendar;
import java.util.Date;

/* loaded from: classes3.dex */
public class BottomDateDialog extends BottomSheetDialogFragment implements View.OnClickListener {
    Button cancelButton;
    Button doneButton;
    Date endDate;
    EditText endDateEditText;
    OnItemClickListener listener;
    Date startDate;
    EditText startDateEditText;

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void onItemClick(long startDate, long endDate);
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
        final View inflate = inflater.inflate(R.layout.bottom_date_layout, container, false);
        getDialog().setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.ktwapps.walletmanager.Widget.BottomDateDialog.1
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface dialogInterface) {
                View findViewById = ((BottomSheetDialog) dialogInterface).findViewById(R.id.design_bottom_sheet);
                BottomSheetBehavior.from(findViewById).setState(3);
                BottomSheetBehavior.from(findViewById).setPeekHeight(inflate.getHeight());
            }
        });
        this.startDateEditText = (EditText) inflate.findViewById(R.id.startDateEditText);
        this.endDateEditText = (EditText) inflate.findViewById(R.id.endDateEditText);
        this.doneButton = (Button) inflate.findViewById(R.id.doneButton);
        this.cancelButton = (Button) inflate.findViewById(R.id.cancelButton);
        this.startDateEditText.setOnClickListener(this);
        this.endDateEditText.setOnClickListener(this);
        this.doneButton.setOnClickListener(this);
        this.cancelButton.setOnClickListener(this);
        this.startDateEditText.setFocusable(false);
        this.endDateEditText.setFocusable(false);
        this.startDateEditText.setLongClickable(false);
        this.endDateEditText.setLongClickable(false);
        if (this.startDate != null && this.endDate != null) {
            this.startDateEditText.setText(DateHelper.getFormattedDate(getActivity(), this.startDate));
            this.endDateEditText.setText(DateHelper.getFormattedDate(getActivity(), this.endDate));
        }
        return inflate;
    }

    public void setTime(long startDate, long endDate) {
        Calendar calendar = Calendar.getInstance();
        if (startDate == 0) {
            startDate = DateHelper.getInitialExportStartDate().getTime();
        }
        if (endDate == 0) {
            endDate = DateHelper.getInitialExportEndDate().getTime();
        }
        calendar.setTimeInMillis(startDate);
        this.startDate = calendar.getTime();
        calendar.setTimeInMillis(endDate);
        this.endDate = calendar.getTime();
        EditText editText = this.startDateEditText;
        if (editText == null || this.endDateEditText == null) {
            return;
        }
        editText.setText(DateHelper.getFormattedDate(getActivity(), this.startDate));
        this.endDateEditText.setText(DateHelper.getFormattedDate(getActivity(), this.endDate));
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.cancelButton /* 2131230873 */) {
            dismiss();
        } else if (view.getId() == R.id.doneButton /* 2131231017 */) {
            OnItemClickListener onItemClickListener = this.listener;
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(this.startDate.getTime(), this.endDate.getTime());
            }
            dismiss();
        } else if (view.getId() == R.id.endDateEditText /* 2131231051 */) {
            new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() { // from class: com.ktwapps.walletmanager.Widget.BottomDateDialog.3
                @Override // android.app.DatePickerDialog.OnDateSetListener
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    BottomDateDialog.this.endDate = CalendarHelper.getDateFromPicker(i, i1, i2);
                    BottomDateDialog.this.endDateEditText.setText(DateHelper.getFormattedDate(BottomDateDialog.this.getActivity(), BottomDateDialog.this.endDate));
                    if (DateHelper.isBeforeDate(BottomDateDialog.this.startDate, BottomDateDialog.this.endDate)) {
                        BottomDateDialog.this.startDate = CalendarHelper.getDateFromPicker(i, i1, i2);
                        BottomDateDialog.this.startDateEditText.setText(DateHelper.getFormattedDate(BottomDateDialog.this.getActivity(), BottomDateDialog.this.startDate));
                    }
                }
            }, CalendarHelper.getYearFromDate(this.endDate), CalendarHelper.getMonthFromDate(this.endDate), CalendarHelper.getDayFromDate(this.endDate)).show();
        } else if (view.getId() == R.id.startDateEditText /* 2131231542 */) {
            new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() { // from class: com.ktwapps.walletmanager.Widget.BottomDateDialog.2
                @Override // android.app.DatePickerDialog.OnDateSetListener
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    BottomDateDialog.this.startDate = CalendarHelper.getDateFromPicker(i, i1, i2);
                    BottomDateDialog.this.startDateEditText.setText(DateHelper.getFormattedDate(BottomDateDialog.this.getActivity(), BottomDateDialog.this.startDate));
                    if (DateHelper.isBeforeDate(BottomDateDialog.this.startDate, BottomDateDialog.this.endDate)) {
                        BottomDateDialog.this.endDate = CalendarHelper.getDateFromPicker(i, i1, i2);
                        BottomDateDialog.this.endDateEditText.setText(DateHelper.getFormattedDate(BottomDateDialog.this.getActivity(), BottomDateDialog.this.endDate));
                    }
                }
            }, CalendarHelper.getYearFromDate(this.startDate), CalendarHelper.getMonthFromDate(this.startDate), CalendarHelper.getDayFromDate(this.startDate)).show();
        }
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
