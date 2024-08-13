package com.expance.merapaisa.Database.ViewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Model.Goal;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.List;

/* loaded from: classes3.dex */
public class GoalAchieveViewModel extends AndroidViewModel {
    private LiveData<List<Goal>> goalList;

    public GoalAchieveViewModel(Application application) {
        super(application);
        AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(getApplication());
        this.goalList = appDatabaseObject.goalDaoObject().getGoalAchieved(SharePreferenceHelper.getAccountId(getApplication()), 1);
    }

    public LiveData<List<Goal>> getGoalList() {
        return this.goalList;
    }
}
