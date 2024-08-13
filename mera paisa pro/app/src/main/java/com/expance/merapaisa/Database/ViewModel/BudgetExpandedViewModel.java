package com.expance.merapaisa.Database.ViewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Model.Budget;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class BudgetExpandedViewModel extends AndroidViewModel {
    private LiveData<List<Budget>> budgetList;

    public BudgetExpandedViewModel(Application application, int period) {
        super(application);
        AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(getApplication());
        this.budgetList = appDatabaseObject.budgetDaoObject().getPeriodBudget(SharePreferenceHelper.getAccountId(getApplication()), getPeriods(period));
    }

    public LiveData<List<Budget>> getBudgetList() {
        return this.budgetList;
    }

    private List<Integer> getPeriods(int period) {
        ArrayList arrayList = new ArrayList();
        if (period == 0) {
            arrayList.add(0);
            arrayList.add(1);
        } else if (period == 1) {
            arrayList.add(2);
        } else if (period == 2) {
            arrayList.add(3);
            arrayList.add(4);
        } else if (period == 3) {
            arrayList.add(5);
        }
        return arrayList;
    }
}
