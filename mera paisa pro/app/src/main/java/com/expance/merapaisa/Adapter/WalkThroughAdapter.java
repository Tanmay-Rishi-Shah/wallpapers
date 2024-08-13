package com.expance.merapaisa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.viewpager.widget.PagerAdapter;
import com.expance.merapaisa.R;

/* loaded from: classes3.dex */
public class WalkThroughAdapter extends PagerAdapter {
    Context context;
    LayoutInflater inflater;
    private int[] image = {R.drawable.walkthrough1, R.drawable.walkthrough2, R.drawable.walkthrough3, R.drawable.walkthrough4};
    private int[] title = {R.string.walk_through_one_title, R.string.walk_through_two_title, R.string.walk_through_three_title, R.string.walk_through_four_title};
    private int[] detail = {R.string.walk_through_one_hint, R.string.walk_through_two_hint, R.string.walk_through_three_hint, R.string.walk_through_four_hint};

    @Override // androidx.viewpager.widget.PagerAdapter
    public int getCount() {
        return 4;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public WalkThroughAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public Object instantiateItem(ViewGroup container, int position) {
        View inflate = this.inflater.inflate(R.layout.layout_walkthrough, container, false);
        ((ImageView) inflate.findViewById(R.id.imageView)).setImageDrawable(this.context.getDrawable(this.image[position]));
        ((TextView) inflate.findViewById(R.id.titleLabel)).setText(this.title[position]);
        ((TextView) inflate.findViewById(R.id.detailLabel)).setText(this.detail[position]);
        container.addView(inflate);
        return inflate;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
