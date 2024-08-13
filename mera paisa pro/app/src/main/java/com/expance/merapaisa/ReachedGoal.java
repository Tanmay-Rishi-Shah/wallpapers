package com.expance.merapaisa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.expance.merapaisa.Adapter.GoalAchieveAdapter;
import com.expance.merapaisa.Database.ViewModel.GoalAchieveViewModel;
import com.expance.merapaisa.Model.Goal;
import com.expance.merapaisa.Utility.Helper;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class ReachedGoal extends Fragment implements GoalAchieveAdapter.OnItemClickListener {
    Activity activity;
    GoalAchieveAdapter adapter;
    ConstraintLayout emptyView;
    RecyclerView recyclerView;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_reached_goal, container, false);
        this.recyclerView = (RecyclerView) viewGroup.findViewById(R.id.recyclerView);
        this.emptyView = (ConstraintLayout) viewGroup.findViewById(R.id.emptyWrapper);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        GoalAchieveAdapter goalAchieveAdapter = new GoalAchieveAdapter(getActivity());
        this.adapter = goalAchieveAdapter;
        this.recyclerView.setAdapter(goalAchieveAdapter);
        this.adapter.setListener(this);
        ((GoalAchieveViewModel) new ViewModelProvider(this).get(GoalAchieveViewModel.class)).getGoalList().observe(this, new Observer<List<Goal>>() { // from class: com.ktwapps.walletmanager.ReachedGoal.1
            @Override // androidx.lifecycle.Observer
            public void onChanged(List<Goal> goals) {
                Activity activity;
                int i;
                ArrayList arrayList = new ArrayList();
                if (goals == null || goals.size() <= 0) {
                    goals = arrayList;
                }
                ReachedGoal.this.adapter.setList(goals);
                RecyclerView recyclerView = ReachedGoal.this.recyclerView;
                if (goals.size() > 0) {
                    activity = ReachedGoal.this.activity;
                    i = R.attr.primaryBackground;
                } else {
                    activity = ReachedGoal.this.activity;
                    i = R.attr.emptyBackground;
                }
                recyclerView.setBackgroundColor(Helper.getAttributeColor(activity, i));
                ReachedGoal.this.emptyView.setVisibility(goals.size() > 0 ? 8 : 0);
                ManageGoal manageGoal = (ManageGoal) ReachedGoal.this.getActivity();
                if (manageGoal != null) {
                    manageGoal.adapter.notifyDataSetChanged();
                }
            }
        });
        return viewGroup;
    }

    public List<Goal> getGoalList() {
        return this.adapter.getList();
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        this.activity = null;
    }

    @Override // com.ktwapps.walletmanager.Adapter.GoalAchieveAdapter.OnItemClickListener
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getActivity(), GoalDetail.class);
        intent.putExtra("goalId", this.adapter.getList().get(position).getId());
        startActivity(intent);
        this.activity.overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
    }
}
