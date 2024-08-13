package com.expance.merapaisa.Adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.expance.merapaisa.OngoingGoal;
import com.expance.merapaisa.R;
import com.expance.merapaisa.ReachedGoal;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes3.dex */
public class GoalPageAdapter extends FragmentPagerAdapter {
    Context context;
    public ReachedGoal goalAchieved;
    public OngoingGoal goalOngoing;

    @Override // androidx.viewpager.widget.PagerAdapter
    public int getCount() {
        return 2;
    }

    public GoalPageAdapter(Context context, FragmentManager fm) {
        super(fm, 1);
        this.context = context;
    }

    @Override // androidx.fragment.app.FragmentPagerAdapter
    public Fragment getItem(int position) {
        if (position == 0) {
            OngoingGoal ongoingGoal = new OngoingGoal();
            this.goalOngoing = ongoingGoal;
            return ongoingGoal;
        } else if (position == 1) {
            ReachedGoal reachedGoal = new ReachedGoal();
            this.goalAchieved = reachedGoal;
            return reachedGoal;
        } else {
            return new Fragment();
        }
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public CharSequence getPageTitle(int position) {
        String str = "0 ";
        if (position == 0) {
            StringBuilder sb = new StringBuilder();
            if (this.goalOngoing != null) {
                str = this.goalOngoing.getGoalList().size() + StringUtils.SPACE;
            }
            sb.append(str);
            sb.append(this.context.getString(R.string.ongoing).toUpperCase());
            return sb.toString();
        } else if (position != 1) {
            return null;
        } else {
            StringBuilder sb2 = new StringBuilder();
            if (this.goalAchieved != null) {
                str = this.goalAchieved.getGoalList().size() + StringUtils.SPACE;
            }
            sb2.append(str);
            sb2.append(this.context.getString(R.string.achieve).toUpperCase());
            return sb2.toString();
        }
    }
}
