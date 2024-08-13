package com.expance.merapaisa.Utility;

import android.graphics.Bitmap;
import android.widget.ImageView;
import com.squareup.picasso.Transformation;

/* loaded from: classes3.dex */
public class PicassoTransformation implements Transformation {
    private ImageView imageView;

    @Override // com.squareup.picasso.Transformation
    public String key() {
        return "Width Based";
    }

    public PicassoTransformation(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override // com.squareup.picasso.Transformation
    public Bitmap transform(Bitmap source) {
        int width = this.imageView.getWidth();
        int height = (int) (width * (source.getHeight() / source.getWidth()));
        if (width <= 0 || height <= 0) {
            return source;
        }
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(source, width, height, false);
        if (source != createScaledBitmap) {
            source.recycle();
        }
        return createScaledBitmap;
    }
}
