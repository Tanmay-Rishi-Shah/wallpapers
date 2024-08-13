package com.expance.merapaisa;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.expance.merapaisa.Utility.CalendarHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

/* loaded from: classes3.dex */
public class MonthlyPicker extends AppCompatActivity implements View.OnClickListener {
    TextView aprLabel;
    TextView augLabel;
    ImageView backImage;
    TextView cancelLabel;
    Date currentDate;
    Date date;
    TextView dateLabel;
    TextView dateTitle;
    TextView decLabel;
    TextView doneLabel;
    TextView febLabel;
    TextView janLabel;
    TextView julLabel;
    TextView junLabel;
    TextView marLabel;
    TextView mayLabel;
    ImageView nextImage;
    TextView novLabel;
    TextView octLabel;
    TextView sepLabel;
    ArrayList<TextView> textViews;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(SharePreferenceHelper.getThemeMode(this) == 1 ? R.style.Theme_Floating_Night : R.style.Theme_Floating);
        setContentView(R.layout.activity_monthly_picker);
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeInMillis(getIntent().getLongExtra("date", 0L));
        Date time = calendar.getTime();
        this.date = time;
        this.currentDate = time;
        this.dateTitle = (TextView) findViewById(R.id.dateTitle);
        this.backImage = (ImageView) findViewById(R.id.backImage);
        this.nextImage = (ImageView) findViewById(R.id.nextImage);
        this.dateLabel = (TextView) findViewById(R.id.dateLabel);
        this.cancelLabel = (TextView) findViewById(R.id.cancelLabel);
        this.doneLabel = (TextView) findViewById(R.id.doneLabel);
        this.janLabel = (TextView) findViewById(R.id.janLabel);
        this.febLabel = (TextView) findViewById(R.id.febLabel);
        this.marLabel = (TextView) findViewById(R.id.marLabel);
        this.aprLabel = (TextView) findViewById(R.id.aprLabel);
        this.mayLabel = (TextView) findViewById(R.id.mayLabel);
        this.junLabel = (TextView) findViewById(R.id.junLabel);
        this.julLabel = (TextView) findViewById(R.id.julLabel);
        this.augLabel = (TextView) findViewById(R.id.augLabel);
        this.sepLabel = (TextView) findViewById(R.id.sepLabel);
        this.octLabel = (TextView) findViewById(R.id.octLabel);
        this.novLabel = (TextView) findViewById(R.id.novLabel);
        this.decLabel = (TextView) findViewById(R.id.decLabel);
        this.dateTitle.setText(new SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), "yyyy MMMM"), Locale.getDefault()).format(this.date));
        this.dateLabel.setText(new SimpleDateFormat("yyyy", Locale.getDefault()).format(this.date));
        try {
            this.janLabel.setText(new SimpleDateFormat("MMM", Locale.getDefault()).format(new SimpleDateFormat("yyyy-MM", Locale.getDefault()).parse("2018-01")));
            this.febLabel.setText(new SimpleDateFormat("MMM", Locale.getDefault()).format(new SimpleDateFormat("yyyy-MM", Locale.getDefault()).parse("2018-02")));
            this.marLabel.setText(new SimpleDateFormat("MMM", Locale.getDefault()).format(new SimpleDateFormat("yyyy-MM", Locale.getDefault()).parse("2018-03")));
            this.aprLabel.setText(new SimpleDateFormat("MMM", Locale.getDefault()).format(new SimpleDateFormat("yyyy-MM", Locale.getDefault()).parse("2018-04")));
            this.mayLabel.setText(new SimpleDateFormat("MMM", Locale.getDefault()).format(new SimpleDateFormat("yyyy-MM", Locale.getDefault()).parse("2018-05")));
            this.junLabel.setText(new SimpleDateFormat("MMM", Locale.getDefault()).format(new SimpleDateFormat("yyyy-MM", Locale.getDefault()).parse("2018-06")));
            this.julLabel.setText(new SimpleDateFormat("MMM", Locale.getDefault()).format(new SimpleDateFormat("yyyy-MM", Locale.getDefault()).parse("2018-07")));
            this.augLabel.setText(new SimpleDateFormat("MMM", Locale.getDefault()).format(new SimpleDateFormat("yyyy-MM", Locale.getDefault()).parse("2018-08")));
            this.sepLabel.setText(new SimpleDateFormat("MMM", Locale.getDefault()).format(new SimpleDateFormat("yyyy-MM", Locale.getDefault()).parse("2018-09")));
            this.octLabel.setText(new SimpleDateFormat("MMM", Locale.getDefault()).format(new SimpleDateFormat("yyyy-MM", Locale.getDefault()).parse("2018-10")));
            this.novLabel.setText(new SimpleDateFormat("MMM", Locale.getDefault()).format(new SimpleDateFormat("yyyy-MM", Locale.getDefault()).parse("2018-11")));
            this.decLabel.setText(new SimpleDateFormat("MMM", Locale.getDefault()).format(new SimpleDateFormat("yyyy-MM", Locale.getDefault()).parse("2018-12")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ArrayList<TextView> arrayList = new ArrayList<>();
        this.textViews = arrayList;
        arrayList.add(this.janLabel);
        this.textViews.add(this.febLabel);
        this.textViews.add(this.marLabel);
        this.textViews.add(this.aprLabel);
        this.textViews.add(this.mayLabel);
        this.textViews.add(this.junLabel);
        this.textViews.add(this.julLabel);
        this.textViews.add(this.augLabel);
        this.textViews.add(this.sepLabel);
        this.textViews.add(this.octLabel);
        this.textViews.add(this.novLabel);
        this.textViews.add(this.decLabel);
        Iterator<TextView> it = this.textViews.iterator();
        int i = 0;
        while (it.hasNext()) {
            int finalI = i;
            it.next().setOnClickListener(new View.OnClickListener() { // from class: com.ktwapps.walletmanager.MonthlyPicker.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    java.util.Calendar calendar2 = java.util.Calendar.getInstance();
                    calendar2.setTime(MonthlyPicker.this.currentDate);
                    calendar2.set(2, finalI);
                    MonthlyPicker.this.date = calendar2.getTime();
                    MonthlyPicker monthlyPicker = MonthlyPicker.this;
                    monthlyPicker.currentDate = monthlyPicker.date;
                    MonthlyPicker.this.setUpMonth();
                    Intent intent = new Intent();
                    intent.putExtra("date", MonthlyPicker.this.date.getTime());
                    MonthlyPicker.this.setResult(-1, intent);
                    MonthlyPicker.this.finish();
                }
            });
            i++;
        }
        this.backImage.setOnClickListener(this);
        this.nextImage.setOnClickListener(this);
        this.cancelLabel.setOnClickListener(this);
        this.doneLabel.setOnClickListener(this);
        setUpMonth();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setUpMonth() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(this.date);
        int i = calendar.get(1);
        int i2 = calendar.get(2);
        calendar.setTime(this.currentDate);
        int i3 = calendar.get(1);
        Iterator<TextView> it = this.textViews.iterator();
        int i4 = 0;
        while (it.hasNext()) {
            TextView next = it.next();
            next.setBackground(null);
            next.setTextColor(Helper.getAttributeColor(this, R.attr.primaryTextColor));
            next.setTextSize(1, 13.0f);
            if (i == i3 && i2 == i4) {
                next.setBackground(getDrawable(R.drawable.background_monthly));
                next.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_OVER);
                next.setTextColor(Color.parseColor("#FFFFFF"));
            }
            i4++;
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        if (v.getId() == R.id.backImage) {
            this.currentDate = CalendarHelper.incrementYear(this.currentDate, -1);
            setUpMonth();
        } else if (v.getId() == R.id.nextImage) {
            this.currentDate = CalendarHelper.incrementYear(this.currentDate, 1);
            setUpMonth();
        } else if (v.getId() == R.id.doneLabel) {
            finish();
        } else {
            finish();
        }
        this.dateLabel.setText(new SimpleDateFormat("yyyy", Locale.getDefault()).format(this.currentDate));
    }
}
