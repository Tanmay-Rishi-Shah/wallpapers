package com.expance.merapaisa.Adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.expance.merapaisa.CategoryExpense;
import com.expance.merapaisa.CategoryIncome;
import com.expance.merapaisa.R;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes3.dex */
public class CategoryPageAdapter extends FragmentPagerAdapter {
    public CategoryExpense categoryExpense;
    public CategoryIncome categoryIncome;
    Context context;

    @Override // androidx.viewpager.widget.PagerAdapter
    public int getCount() {
        return 2;
    }

    public CategoryPageAdapter(Context context, FragmentManager fm) {
        super(fm, 1);
        this.context = context;
    }

    @Override // androidx.fragment.app.FragmentPagerAdapter
    public Fragment getItem(int position) {
        if (position == 0) {
            CategoryIncome categoryIncome = new CategoryIncome();
            this.categoryIncome = categoryIncome;
            return categoryIncome;
        } else if (position == 1) {
            CategoryExpense categoryExpense = new CategoryExpense();
            this.categoryExpense = categoryExpense;
            return categoryExpense;
        } else {
            return new Fragment();
        }
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public CharSequence getPageTitle(int position) {
        String str = "0 ";
        if (position == 0) {
            StringBuilder sb = new StringBuilder();
            if (this.categoryIncome != null) {
                str = this.categoryIncome.getCategoryList().size() + StringUtils.SPACE;
            }
            sb.append(str);
            sb.append(this.context.getString(R.string.income).toUpperCase());
            return sb.toString();
        } else if (position != 1) {
            return null;
        } else {
            StringBuilder sb2 = new StringBuilder();
            if (this.categoryExpense != null) {
                str = this.categoryExpense.getCategoryList().size() + StringUtils.SPACE;
            }
            sb2.append(str);
            sb2.append(this.context.getString(R.string.expense).toUpperCase());
            return sb2.toString();
        }
    }
}
