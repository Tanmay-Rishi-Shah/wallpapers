package com.expance.merapaisa.Adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.expance.merapaisa.CategoryExpensePicker;
import com.expance.merapaisa.CategoryIncomePicker;
import com.expance.merapaisa.R;

/* loaded from: classes3.dex */
public class CategoryPickerPageAdapter extends FragmentPagerAdapter {
    Context context;

    @Override // androidx.viewpager.widget.PagerAdapter
    public int getCount() {
        return 2;
    }

    public CategoryPickerPageAdapter(Context context, FragmentManager fm) {
        super(fm, 1);
        this.context = context;
    }

    @Override // androidx.fragment.app.FragmentPagerAdapter
    public Fragment getItem(int position) {
        if (position != 0) {
            if (position == 1) {
                return new CategoryExpensePicker();
            }
            return new Fragment();
        }
        return new CategoryIncomePicker();
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public CharSequence getPageTitle(int position) {
        if (position != 0) {
            if (position != 1) {
                return null;
            }
            return this.context.getString(R.string.expense).toUpperCase();
        }
        return this.context.getString(R.string.income).toUpperCase();
    }
}
