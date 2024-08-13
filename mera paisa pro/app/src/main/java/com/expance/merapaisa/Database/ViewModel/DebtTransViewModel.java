package com.expance.merapaisa.Database.ViewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.Entity.DebtTransEntity;
import com.expance.merapaisa.Model.Debt;
import java.util.List;

/* loaded from: classes3.dex */
public class DebtTransViewModel extends AndroidViewModel {
    private LiveData<Debt> debt;
    private LiveData<List<DebtTransEntity>> debtTrans;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DebtTransViewModel(Application application, int debtId) {
        super(application);
        AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(getApplication());
        this.debt = appDatabaseObject.debtDaoObject().getLiveDebtById(debtId);
        this.debtTrans = appDatabaseObject.debtDaoObject().getDebtTrans(debtId);
    }

    public LiveData<Debt> getDebt() {
        return this.debt;
    }

    public LiveData<List<DebtTransEntity>> getDebtTrans() {
        return this.debtTrans;
    }
}
