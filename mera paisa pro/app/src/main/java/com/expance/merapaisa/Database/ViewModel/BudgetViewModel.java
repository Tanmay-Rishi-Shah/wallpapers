package com.expance.merapaisa.Database.ViewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Model.Budget;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.List;

/* loaded from: classes3.dex */
public class BudgetViewModel extends AndroidViewModel {
    private LiveData<List<Budget>> budgetList;

    public BudgetViewModel(Application application) {
        super(application);
        AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(getApplication());
        this.budgetList = appDatabaseObject.budgetDaoObject().getBudget(SharePreferenceHelper.getAccountId(getApplication()), 0);
    }

    public LiveData<List<Budget>> getBudgetList() {
        return this.budgetList;
    }
}
