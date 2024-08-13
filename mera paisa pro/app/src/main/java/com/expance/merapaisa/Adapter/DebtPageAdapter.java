package com.expance.merapaisa.Adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.expance.merapaisa.DebtBorrow;
import com.expance.merapaisa.DebtLend;
import com.expance.merapaisa.R;

/* loaded from: classes3.dex */
public class DebtPageAdapter extends FragmentPagerAdapter {
    Context context;

    @Override // androidx.viewpager.widget.PagerAdapter
    public int getCount() {
        return 2;
    }

    public DebtPageAdapter(Context context, FragmentManager fm) {
        super(fm, 1);
        this.context = context;
    }

    @Override // androidx.fragment.app.FragmentPagerAdapter
    public Fragment getItem(int position) {
        if (position != 0) {
            if (position == 1) {
                return new DebtBorrow();
            }
            return new Fragment();
        }
        return new DebtLend();
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public CharSequence getPageTitle(int position) {
        if (position != 0) {
            if (position != 1) {
                return null;
            }
            return this.context.getString(R.string.i_borrow);
        }
        return this.context.getString(R.string.i_lend);
    }
}
