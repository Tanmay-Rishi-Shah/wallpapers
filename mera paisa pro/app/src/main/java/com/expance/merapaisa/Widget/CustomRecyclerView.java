package com.expance.merapaisa.Widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.expance.merapaisa.Utility.Helper;

/* loaded from: classes3.dex */
public class CustomRecyclerView extends RecyclerView {
    public CustomRecyclerView(Context context) {
        super(context);
    }

    public CustomRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.recyclerview.widget.RecyclerView, android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int screenHeight = (int) (Helper.getScreenHeight() * 0.75d);
        if (View.MeasureSpec.getSize(heightMeasureSpec) > screenHeight) {
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(screenHeight, Integer.MIN_VALUE);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
