package com.expance.merapaisa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.viewpager.widget.PagerAdapter;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.PicassoTransformation;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.util.List;

/* loaded from: classes3.dex */
public class PhotoAdapter extends PagerAdapter {
    Context context;
    LayoutInflater inflater;
    private List<String> path;

    @Override // androidx.viewpager.widget.PagerAdapter
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public PhotoAdapter(Context context, List<String> path) {
        this.context = context;
        this.path = path;
        this.inflater = LayoutInflater.from(context);
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public int getCount() {
        return this.path.size();
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public Object instantiateItem(ViewGroup container, int position) {
        View inflate = this.inflater.inflate(R.layout.layout_photo_viewer, container, false);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.imageView);
        Picasso.get().load(new File(this.path.get(position))).transform(new PicassoTransformation(imageView)).into(imageView);
        container.addView(inflate);
        return inflate;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
