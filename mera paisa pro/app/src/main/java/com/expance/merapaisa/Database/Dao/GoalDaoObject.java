package com.expance.merapaisa.Database.Dao;

import androidx.lifecycle.LiveData;
import com.expance.merapaisa.Database.Entity.GoalEntity;
import com.expance.merapaisa.Database.Entity.GoalTransEntity;
import com.expance.merapaisa.Model.Goal;
import java.util.List;

/* loaded from: classes3.dex */
public interface GoalDaoObject {
    void deleteAllGoalTrans(int id);

    void deleteGoal(int id);

    void deleteGoalTrans(int id);

    LiveData<List<Goal>> getGoal(int accountId, int status);

    LiveData<List<Goal>> getGoalAchieved(int accountId, int status);

    GoalEntity getGoalById(int id);

    LiveData<List<GoalTransEntity>> getGoalTrans(int goalId);

    GoalTransEntity getGoalTransById(int id);

    LiveData<GoalEntity> getLiveGoalById(int id);

    void insertGoal(GoalEntity goalEntity);

    void insertGoalEntity(GoalTransEntity goalTransEntity);

    void update(GoalEntity goalEntity);

    void updateAmount(int id, double saved);

    void updateTrans(GoalTransEntity goalTransEntity);
}
