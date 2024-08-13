package com.expance.merapaisa.Utility;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Display;
import com.google.android.gms.ads.AdSize;

/* loaded from: classes3.dex */
public class AdsHelper {
    public static AdSize getAdSize(Activity activity) {
        Display defaultDisplay = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, (int) (displayMetrics.widthPixels / displayMetrics.density));
    }
}
