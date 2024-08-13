package com.expance.merapaisa.Widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.viewpager.widget.ViewPager;

/* loaded from: classes3.dex */
public class NoneSwipeableViewPager extends ViewPager {
    @Override // androidx.viewpager.widget.ViewPager, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }

    @Override // androidx.viewpager.widget.ViewPager, android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public NoneSwipeableViewPager(Context context) {
        super(context);
    }

    public NoneSwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
