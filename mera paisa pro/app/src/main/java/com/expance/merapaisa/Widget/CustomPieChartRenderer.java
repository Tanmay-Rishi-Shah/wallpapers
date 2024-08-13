package com.expance.merapaisa.Widget;

import android.content.Context;
import android.graphics.Canvas;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.renderer.PieChartRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.Helper;
import java.util.List;

/* loaded from: classes3.dex */
public class CustomPieChartRenderer extends PieChartRenderer {
    ChartAnimator animator;
    PieChart chart;
    Context context;

    public CustomPieChartRenderer(Context context, PieChart chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
        this.chart = chart;
        this.animator = animator;
        this.context = context;
    }

    @Override // com.github.mikephil.charting.renderer.PieChartRenderer, com.github.mikephil.charting.renderer.DataRenderer
    public void drawValues(Canvas c) {
        float f;
        float f2;
        float f3;
        super.drawValues(c);
        MPPointF centerCircleBox = this.chart.getCenterCircleBox();
        float radius = this.chart.getRadius();
        float rotationAngle = this.chart.getRotationAngle();
        float[] drawAngles = this.chart.getDrawAngles();
        float[] absoluteAngles = this.chart.getAbsoluteAngles();
        float phaseX = this.animator.getPhaseX();
        float phaseY = this.animator.getPhaseY();
        float f4 = 2.0f;
        float holeRadius = (radius - ((this.chart.getHoleRadius() * radius) / 100.0f)) / 2.0f;
        float holeRadius2 = this.chart.getHoleRadius() / 100.0f;
        float f5 = (radius / 10.0f) * 3.6f;
        if (this.chart.isDrawHoleEnabled()) {
            f5 = (radius - (radius * holeRadius2)) / 2.0f;
            if (!this.chart.isDrawSlicesUnderHoleEnabled() && this.chart.isDrawRoundedSlicesEnabled()) {
                rotationAngle += (holeRadius * 360.0f) / (6.2831855f * radius);
            }
        }
        float f6 = radius - f5;
        List<IPieDataSet> dataSets = ((PieData) this.chart.getData()).getDataSets();
        c.save();
        int i = 0;
        for (IPieDataSet iPieDataSet : dataSets) {
            float sliceSpace = getSliceSpace(iPieDataSet);
            int i2 = 0;
            while (i2 < iPieDataSet.getEntryCount()) {
                float f7 = (i == 0 ? 0.0f : absoluteAngles[i - 1] * phaseX) + ((drawAngles[i] - ((sliceSpace / (0.017453292f * f6)) / f4)) / f4);
                float[] fArr = drawAngles;
                if (iPieDataSet.getValueLineColor() != 1122867) {
                    double d = ((f7 * phaseY) + rotationAngle) * 0.01745329238474369d;
                    f2 = rotationAngle;
                    float cos = (float) Math.cos(d);
                    float sin = (float) Math.sin(d);
                    float valueLinePart1OffsetPercentage = iPieDataSet.getValueLinePart1OffsetPercentage() / 100.0f;
                    if (this.mChart.isDrawHoleEnabled()) {
                        float f8 = radius * holeRadius2;
                        f3 = ((radius - f8) * valueLinePart1OffsetPercentage) + f8;
                    } else {
                        f3 = radius * valueLinePart1OffsetPercentage;
                    }
                    float f9 = (cos * f3) + centerCircleBox.x;
                    float f10 = (f3 * sin) + centerCircleBox.y;
                    if (iPieDataSet.isUsingSliceColorAsValueLineColor()) {
                        this.mRenderPaint.setColor(iPieDataSet.getColor(i2));
                    }
                    f = radius;
                    c.drawCircle(f9, f10, Helper.convertDpToPixel(this.context, 3.0f), this.mRenderPaint);
                } else {
                    f = radius;
                    f2 = rotationAngle;
                }
                i++;
                i2++;
                drawAngles = fArr;
                rotationAngle = f2;
                radius = f;
                f4 = 2.0f;
            }
            radius = radius;
        }
        this.chart.setHoleColor(Helper.getAttributeColor(this.context, R.attr.secondaryBackground));
        MPPointF.recycleInstance(centerCircleBox);
        c.restore();
    }
}
