package com.expance.merapaisa.Widget;

import android.content.Context;
import android.text.format.DateFormat;
import android.widget.TextView;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/* loaded from: classes3.dex */
public class MarkerViewBudget extends MarkerView {
    private TextView amountLabel;
    private Date date;
    private TextView dateLabel;
    private String symbol;
    private int width;

    public MarkerViewBudget(Context context, int layoutResource) {
        super(context, layoutResource);
        this.dateLabel = (TextView) findViewById(R.id.dateLabel);
        this.amountLabel = (TextView) findViewById(R.id.amountLabel);
        this.symbol = SharePreferenceHelper.getAccountSymbol(context);
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override // com.github.mikephil.charting.components.MarkerView, com.github.mikephil.charting.components.IMarker
    public void refreshContent(Entry e, Highlight highlight) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.date);
        calendar.add(5, (int) e.getX());
        this.amountLabel.setText(Helper.getBeautifyAmount(this.symbol, (long) (e.getY() * 100.0f)));
        this.dateLabel.setText(new SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), "dd/MM"), Locale.getDefault()).format(calendar.getTime()));
        super.refreshContent(e, highlight);
    }

    @Override // com.github.mikephil.charting.components.MarkerView, com.github.mikephil.charting.components.IMarker
    public MPPointF getOffsetForDrawingAtPoint(float posX, float posY) {
        float f = -(getWidth() / 2.0f);
        if (getWidth() + posX > this.width) {
            f = (-getWidth()) + (this.width - posX);
        }
        return new MPPointF(f, -posY);
    }
}
