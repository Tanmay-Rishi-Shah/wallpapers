package com.expance.merapaisa.Widget;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/* loaded from: classes3.dex */
public class BottomRecurringTransactionDialog extends BottomSheetDialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, View.OnTouchListener, RadioGroup.OnCheckedChangeListener {
    private ConstraintLayout[] bottomWrapper;
    private Button cancelButton;
    private Date date;
    private ArrayAdapter<String> dateAdapter;
    private List<String> dateList;
    private Button doneButton;
    private ImageView[] doneImage;
    private ConstraintLayout[] doneWrapper;
    private EditText[] editText;
    private int increment;
    OnItemClickListener listener;
    RadioButton radio1;
    RadioButton radio2;
    private RadioGroup radioGroup;
    private String repeatDate;
    private EditText[] repeatEditText;
    private int repeatTime;
    private int repeatType;
    private Spinner[] spinner;
    private TextView[] timeLabel;
    private int type;
    private Date untilDate;
    private Spinner[] untilSpinner;
    private int untilType;
    private int weekDay;
    private TextView[] weekDayLabel;
    private ConstraintLayout[] weekDayWrapper;
    private ConstraintLayout[] wrapper;

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        void OnItemClick(int increment, int repeatTime, int untilType, int repeatType, int type, Date untilDate, String repeatDate);
    }

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public void onNothingSelected(AdapterView<?> adapterView) {
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

    public void setData(Date date, Date untilDate, String repeatDate, int type, int repeatType, int repeatTime, int untilType, int increment) {
        this.date = date;
        this.untilDate = untilDate;
        this.repeatDate = repeatDate;
        this.repeatTime = repeatTime;
        this.type = type;
        this.repeatType = repeatType;
        this.untilType = untilType;
        this.increment = increment;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        this.weekDay = calendar.get(7);
        if (getActivity() != null) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(getActivity().getString(R.string.forever));
            arrayList.add(getActivity().getString(R.string.until));
            arrayList.add(getActivity().getString(R.string.for_));
            ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), 17367049, arrayList);
            calendar.setTime(untilDate);
            this.dateList = new ArrayList();
            this.dateList.add(new SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), "dd MMM yyyy"), Locale.getDefault()).format(calendar.getTime()));
            this.dateAdapter = new ArrayAdapter<>(getActivity(), 17367049, this.dateList);
            for (int i = 0; i < 4; i++) {
                this.spinner[i].setAdapter((SpinnerAdapter) arrayAdapter);
                this.spinner[i].setOnItemSelectedListener(this);
                this.untilSpinner[i].setAdapter((SpinnerAdapter) this.dateAdapter);
                this.untilSpinner[i].setOnTouchListener(this);
                this.editText[i].setText(String.valueOf(increment));
                this.repeatEditText[i].setText(String.valueOf(repeatTime));
            }
            setWrapper();
            setUntil();
            setWeekly();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.cloneInContext(new ContextThemeWrapper(getActivity(), SharePreferenceHelper.getThemeMode(getActivity()) == 1 ? R.style.AppThemeNight : R.style.AppTheme)).inflate(R.layout.bottom_recurring_transacion_layout, container, false);
        getDialog().setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.ktwapps.walletmanager.Widget.BottomRecurringTransactionDialog.1
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface dialogInterface) {
                View findViewById = ((BottomSheetDialog) dialogInterface).findViewById(R.id.design_bottom_sheet);
                BottomSheetBehavior.from(findViewById).setPeekHeight(Helper.getScreenHeight());
                BottomSheetBehavior.from(findViewById).setState(3);
            }
        });
        ConstraintLayout[] constraintLayoutArr = new ConstraintLayout[5];
        this.doneWrapper = constraintLayoutArr;
        constraintLayoutArr[0] = (ConstraintLayout) inflate.findViewById(R.id.noneDoneWrapper);
        this.doneWrapper[1] = (ConstraintLayout) inflate.findViewById(R.id.dailyDoneWrapper);
        this.doneWrapper[2] = (ConstraintLayout) inflate.findViewById(R.id.weeklyDoneWrapper);
        this.doneWrapper[3] = (ConstraintLayout) inflate.findViewById(R.id.monthlyDoneWrapper);
        this.doneWrapper[4] = (ConstraintLayout) inflate.findViewById(R.id.yearlyDoneWrapper);
        ImageView[] imageViewArr = new ImageView[5];
        this.doneImage = imageViewArr;
        imageViewArr[0] = (ImageView) inflate.findViewById(R.id.noneDoneImage);
        this.doneImage[1] = (ImageView) inflate.findViewById(R.id.dailyDoneImage);
        this.doneImage[2] = (ImageView) inflate.findViewById(R.id.weeklyDoneImage);
        this.doneImage[3] = (ImageView) inflate.findViewById(R.id.monthlyDoneImage);
        this.doneImage[4] = (ImageView) inflate.findViewById(R.id.yearlyDoneImage);
        ConstraintLayout[] constraintLayoutArr2 = new ConstraintLayout[5];
        this.bottomWrapper = constraintLayoutArr2;
        constraintLayoutArr2[0] = (ConstraintLayout) inflate.findViewById(R.id.noneBottomWrapper);
        this.bottomWrapper[1] = (ConstraintLayout) inflate.findViewById(R.id.dailyBottomWrapper);
        this.bottomWrapper[2] = (ConstraintLayout) inflate.findViewById(R.id.weeklyBottomWrapper);
        this.bottomWrapper[3] = (ConstraintLayout) inflate.findViewById(R.id.monthlyBottomWrapper);
        this.bottomWrapper[4] = (ConstraintLayout) inflate.findViewById(R.id.yearlyBottomWrapper);
        ConstraintLayout[] constraintLayoutArr3 = new ConstraintLayout[7];
        this.weekDayWrapper = constraintLayoutArr3;
        constraintLayoutArr3[0] = (ConstraintLayout) inflate.findViewById(R.id.sunWrapper);
        this.weekDayWrapper[1] = (ConstraintLayout) inflate.findViewById(R.id.monWrapper);
        this.weekDayWrapper[2] = (ConstraintLayout) inflate.findViewById(R.id.tueWrapper);
        this.weekDayWrapper[3] = (ConstraintLayout) inflate.findViewById(R.id.wedWrapper);
        this.weekDayWrapper[4] = (ConstraintLayout) inflate.findViewById(R.id.thuWrapper);
        this.weekDayWrapper[5] = (ConstraintLayout) inflate.findViewById(R.id.friWrapper);
        this.weekDayWrapper[6] = (ConstraintLayout) inflate.findViewById(R.id.satWrapper);
        TextView[] textViewArr = new TextView[7];
        this.weekDayLabel = textViewArr;
        textViewArr[0] = (TextView) inflate.findViewById(R.id.sunLabel);
        this.weekDayLabel[1] = (TextView) inflate.findViewById(R.id.monLabel);
        this.weekDayLabel[2] = (TextView) inflate.findViewById(R.id.tueLabel);
        this.weekDayLabel[3] = (TextView) inflate.findViewById(R.id.wedLabel);
        this.weekDayLabel[4] = (TextView) inflate.findViewById(R.id.thuLabel);
        this.weekDayLabel[5] = (TextView) inflate.findViewById(R.id.friLabel);
        this.weekDayLabel[6] = (TextView) inflate.findViewById(R.id.satLabel);
        ConstraintLayout[] constraintLayoutArr4 = new ConstraintLayout[5];
        this.wrapper = constraintLayoutArr4;
        constraintLayoutArr4[0] = (ConstraintLayout) inflate.findViewById(R.id.noneWrapper);
        this.wrapper[1] = (ConstraintLayout) inflate.findViewById(R.id.dailyWrapper);
        this.wrapper[2] = (ConstraintLayout) inflate.findViewById(R.id.weeklyWrapper);
        this.wrapper[3] = (ConstraintLayout) inflate.findViewById(R.id.monthlyWrapper);
        this.wrapper[4] = (ConstraintLayout) inflate.findViewById(R.id.yearlyWrapper);
        EditText[] editTextArr = new EditText[4];
        this.editText = editTextArr;
        editTextArr[0] = (EditText) inflate.findViewById(R.id.dailyEditText);
        this.editText[1] = (EditText) inflate.findViewById(R.id.weeklyEditText);
        this.editText[2] = (EditText) inflate.findViewById(R.id.monthlyEditText);
        this.editText[3] = (EditText) inflate.findViewById(R.id.yearlyEditText);
        EditText[] editTextArr2 = new EditText[4];
        this.repeatEditText = editTextArr2;
        editTextArr2[0] = (EditText) inflate.findViewById(R.id.dailyRepeatEditText);
        this.repeatEditText[1] = (EditText) inflate.findViewById(R.id.weeklyRepeatEditText);
        this.repeatEditText[2] = (EditText) inflate.findViewById(R.id.monthlyRepeatEditText);
        this.repeatEditText[3] = (EditText) inflate.findViewById(R.id.yearlyRepeatEditText);
        Spinner[] spinnerArr = new Spinner[4];
        this.spinner = spinnerArr;
        spinnerArr[0] = (Spinner) inflate.findViewById(R.id.dailySpinner);
        this.spinner[1] = (Spinner) inflate.findViewById(R.id.weeklySpinner);
        this.spinner[2] = (Spinner) inflate.findViewById(R.id.monthlySpinner);
        this.spinner[3] = (Spinner) inflate.findViewById(R.id.yearlySpinner);
        Spinner[] spinnerArr2 = new Spinner[4];
        this.untilSpinner = spinnerArr2;
        spinnerArr2[0] = (Spinner) inflate.findViewById(R.id.dailyUntilSpinner);
        this.untilSpinner[1] = (Spinner) inflate.findViewById(R.id.weeklyUntilSpinner);
        this.untilSpinner[2] = (Spinner) inflate.findViewById(R.id.monthlyUntilSpinner);
        this.untilSpinner[3] = (Spinner) inflate.findViewById(R.id.yearlyUntilSpinner);
        TextView[] textViewArr2 = new TextView[4];
        this.timeLabel = textViewArr2;
        textViewArr2[0] = (TextView) inflate.findViewById(R.id.dailyTimeLabel);
        this.timeLabel[1] = (TextView) inflate.findViewById(R.id.weeklyTimeLabel);
        this.timeLabel[2] = (TextView) inflate.findViewById(R.id.monthlyTimeLabel);
        this.timeLabel[3] = (TextView) inflate.findViewById(R.id.yearlyTimeLabel);
        this.radioGroup = (RadioGroup) inflate.findViewById(R.id.radioGroup);
        this.radio1 = (RadioButton) inflate.findViewById(R.id.radio1);
        this.radio2 = (RadioButton) inflate.findViewById(R.id.radio2);
        this.cancelButton = (Button) inflate.findViewById(R.id.cancelButton);
        this.doneButton = (Button) inflate.findViewById(R.id.doneButton);
        for (ConstraintLayout constraintLayout : this.wrapper) {
            constraintLayout.setOnClickListener(this);
        }
        for (ConstraintLayout constraintLayout2 : this.weekDayWrapper) {
            constraintLayout2.setOnClickListener(this);
        }
        this.cancelButton.setOnClickListener(this);
        this.doneButton.setOnClickListener(this);
        if (this.date != null && this.untilDate != null && this.repeatDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(this.date);
            this.radio1.setText(getResources().getString(R.string.same_day_each_month, Integer.valueOf(calendar.get(5))));
            if (this.repeatType == 1) {
                this.radio2.setChecked(true);
            } else {
                this.radio1.setChecked(true);
            }
            this.radioGroup.setOnCheckedChangeListener(this);
            ArrayList arrayList = new ArrayList();
            arrayList.add(getActivity().getString(R.string.forever));
            arrayList.add(getActivity().getString(R.string.until));
            arrayList.add(getActivity().getString(R.string.for_));
            ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), 17367049, arrayList);
            calendar.setTime(this.untilDate);
            this.dateList = new ArrayList();
            this.dateList.add(new SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), "dd MMM yyyy"), Locale.getDefault()).format(calendar.getTime()));
            this.dateAdapter = new ArrayAdapter<>(getActivity(), 17367049, this.dateList);
            for (int i = 0; i < 4; i++) {
                this.spinner[i].setAdapter((SpinnerAdapter) arrayAdapter);
                this.spinner[i].setOnItemSelectedListener(this);
                this.untilSpinner[i].setAdapter((SpinnerAdapter) this.dateAdapter);
                this.untilSpinner[i].setOnTouchListener(this);
                this.editText[i].setText(String.valueOf(this.increment));
                this.repeatEditText[i].setText(String.valueOf(this.repeatTime));
            }
            setWrapper();
            setUntil();
            setWeekly();
        }
        return inflate;
    }

    private void onDoneClick() {
        int i = this.type;
        if (i != 0) {
            EditText editText = this.editText[i - 1];
            EditText editText2 = this.repeatEditText[i - 1];
            this.increment = (editText.getText().toString().length() == 0 || editText.getText().toString().equals("0")) ? 1 : Integer.parseInt(editText.getText().toString());
            int parseInt = (editText2.getText().toString().length() == 0 || editText2.getText().toString().equals("0")) ? 1 : Integer.parseInt(editText2.getText().toString());
            this.repeatTime = parseInt;
            this.listener.OnItemClick(this.increment, parseInt, this.untilType, this.repeatType, this.type, this.untilDate, this.repeatDate);
            return;
        }
        this.listener.OnItemClick(1, 1, this.untilType, this.repeatType, i, this.untilDate, this.repeatDate);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        String str;
        if (view.getId() == R.id.cancelButton /* 2131230873 */) {
            dismiss();
        } else if (view.getId() == R.id.dailyWrapper /* 2131230955 */) {
            this.type = 1;
        } else if (view.getId() == R.id.doneButton /* 2131231017 */) {
            onDoneClick();
            dismiss();
        } else if (view.getId() == R.id.monthlyWrapper /* 2131231296 */) {
            this.type = 3;
        } else if (view.getId() == R.id.noneWrapper /* 2131231348 */) {
            this.type = 0;
        } else if (view.getId() == R.id.weeklyWrapper /* 2131231699 */) {
            this.type = 2;
        } else if (view.getId() == R.id.yearlyWrapper /* 2131231725 */) {
            this.type = 4;
        } else {
            if (view.getId() == R.id.sunWrapper) {
                str = this.repeatDate.substring(0, 1).equals("0") ? "1" : "0";
                this.repeatDate = str + this.repeatDate.substring(1);
                this.weekDay = 1;
            } else if (view.getId() == R.id.monWrapper) {
                str = this.repeatDate.substring(1, 2).equals("0") ? "1" : "0";
                this.repeatDate = this.repeatDate.substring(0, 1) + str + this.repeatDate.substring(2);
                this.weekDay = 2;
            } else if (view.getId() == R.id.tueWrapper) {
                str = this.repeatDate.substring(2, 3).equals("0") ? "1" : "0";
                this.repeatDate = this.repeatDate.substring(0, 2) + str + this.repeatDate.substring(3);
                this.weekDay = 3;
            } else if (view.getId() == R.id.wedWrapper) {
                str = this.repeatDate.substring(3, 4).equals("0") ? "1" : "0";
                this.repeatDate = this.repeatDate.substring(0, 3) + str + this.repeatDate.substring(4);
                this.weekDay = 4;
            } else if (view.getId() == R.id.thuWrapper) {
                str = this.repeatDate.substring(4, 5).equals("0") ? "1" : "0";
                this.repeatDate = this.repeatDate.substring(0, 4) + str + this.repeatDate.substring(5);
                this.weekDay = 5;
            } else if (view.getId() == R.id.friWrapper) {
                str = this.repeatDate.substring(5, 6).equals("0") ? "1" : "0";
                this.repeatDate = this.repeatDate.substring(0, 5) + str + this.repeatDate.substring(6);
                this.weekDay = 6;
            } else if (view.getId() == R.id.satWrapper) {
                str = this.repeatDate.substring(6, 7).equals("0") ? "1" : "0";
                this.repeatDate = this.repeatDate.substring(0, 6) + str;
                this.weekDay = 7;
            }
        }
        setWeekly();
        setWrapper();
    }

    private void setUntil() {
        for (int i = 0; i < 4; i++) {
            this.untilSpinner[i].setVisibility(8);
            this.repeatEditText[i].setVisibility(8);
            this.timeLabel[i].setVisibility(8);
        }
        int i2 = this.untilType;
        if (i2 == 1) {
            for (int i3 = 0; i3 < 4; i3++) {
                this.untilSpinner[i3].setVisibility(0);
            }
        } else if (i2 == 2) {
            for (int i4 = 0; i4 < 4; i4++) {
                this.repeatEditText[i4].setVisibility(0);
                this.timeLabel[i4].setVisibility(0);
            }
        }
    }

    private void setWeekly() {
        if (this.repeatDate.equals("0000000")) {
            int i = this.weekDay;
            if (i == 1) {
                this.repeatDate = "1000000";
            } else if (i == 2) {
                this.repeatDate = "0100000";
            } else if (i == 3) {
                this.repeatDate = "0010000";
            } else if (i == 4) {
                this.repeatDate = "0001000";
            } else if (i == 5) {
                this.repeatDate = "0000100";
            } else if (i == 6) {
                this.repeatDate = "0000010";
            } else if (i == 7) {
                this.repeatDate = "0000001";
            }
        }
        int i2 = 0;
        while (i2 < this.weekDayWrapper.length) {
            int i3 = i2 + 1;
            String substring = this.repeatDate.substring(i2, i3);
            this.weekDayWrapper[i2].setBackground(getResources().getDrawable(substring.equals("0") ? R.drawable.background_recurring_weekly_uncheck : R.drawable.background_recurring_weekly_checked));
            this.weekDayLabel[i2].setTextColor(substring.equals("0") ? Helper.getAttributeColor(getActivity(), R.attr.primaryTextColor) : Color.parseColor("#FFFFFF"));
            i2 = i3;
        }
    }

    private void setWrapper() {
        int i = 0;
        while (i < 5) {
            this.doneWrapper[i].setBackground(this.type == i ? getActivity().getResources().getDrawable(R.drawable.background_box_checked) : ContextCompat.getDrawable(getActivity(), Helper.getAttributeDrawable(getActivity(), R.attr.uncheckBackground)));
            int i2 = 8;
            this.doneImage[i].setVisibility(this.type == i ? 0 : 8);
            ConstraintLayout constraintLayout = this.bottomWrapper[i];
            if (this.type == i) {
                i2 = 0;
            }
            constraintLayout.setVisibility(i2);
            i++;
        }
        for (int i3 = 0; i3 < 4; i3++) {
            this.spinner[i3].setSelection(this.untilType);
        }
        setUntil();
    }

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i == 0) {
            this.untilType = 0;
        } else if (i == 1) {
            this.untilType = 1;
        } else {
            this.untilType = 2;
        }
        setUntil();
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == 1) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(this.untilDate);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() { // from class: com.ktwapps.walletmanager.Widget.BottomRecurringTransactionDialog.2
                @Override // android.app.DatePickerDialog.OnDateSetListener
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    Calendar calendar2 = Calendar.getInstance();
                    calendar2.set(1, i);
                    calendar2.set(2, i1);
                    calendar2.set(5, i2);
                    BottomRecurringTransactionDialog.this.untilDate = calendar2.getTime();
                    BottomRecurringTransactionDialog.this.dateList.clear();
                    BottomRecurringTransactionDialog.this.dateList.add(new SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), "dd MMM yyyy"), Locale.getDefault()).format(calendar2.getTime()));
                    for (int i3 = 0; i3 < 4; i3++) {
                        BottomRecurringTransactionDialog.this.untilSpinner[i3].setAdapter((SpinnerAdapter) BottomRecurringTransactionDialog.this.dateAdapter);
                    }
                }
            }, calendar.get(1), calendar.get(2), calendar.get(5));
            datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
            datePickerDialog.show();
            return true;
        }
        return false;
    }

    @Override // android.widget.RadioGroup.OnCheckedChangeListener
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        this.repeatType = i == R.id.radio1 ? 0 : 1;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
