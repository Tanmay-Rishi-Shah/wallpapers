package com.expance.merapaisa.Database.ViewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.Entity.GoalEntity;
import com.expance.merapaisa.Database.Entity.GoalTransEntity;
import java.util.List;

/* loaded from: classes3.dex */
public class GoalDetailViewModel extends AndroidViewModel {
    private LiveData<GoalEntity> goal;
    private LiveData<List<GoalTransEntity>> goalTrans;

    /* JADX INFO: Access modifiers changed from: package-private */
    public GoalDetailViewModel(Application application, int goalId) {
        super(application);
        AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(getApplication());
        this.goal = appDatabaseObject.goalDaoObject().getLiveGoalById(goalId);
        this.goalTrans = appDatabaseObject.goalDaoObject().getGoalTrans(goalId);
    }

    public LiveData<GoalEntity> getGoal() {
        return this.goal;
    }

    public LiveData<List<GoalTransEntity>> getGoalTrans() {
        return this.goalTrans;
    }
}
