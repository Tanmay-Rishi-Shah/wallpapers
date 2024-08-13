package com.expance.merapaisa.Database.ViewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Model.Debt;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.List;

/* loaded from: classes3.dex */
public class DebtBorrowViewModel extends AndroidViewModel {
    private LiveData<List<Debt>> debtList;

    public DebtBorrowViewModel(Application application) {
        super(application);
        AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(getApplication());
        this.debtList = appDatabaseObject.debtDaoObject().getDebt(SharePreferenceHelper.getAccountId(getApplication()), 1);
    }

    public LiveData<List<Debt>> getDebtList() {
        return this.debtList;
    }
}
