package com.expance.merapaisa.Database.ViewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Model.Recurring;
import com.expance.merapaisa.Utility.SharePreferenceHelper;

/* loaded from: classes3.dex */
public class RecurringDetailViewModel extends AndroidViewModel {
    private LiveData<Recurring> recurring;

    /* JADX INFO: Access modifiers changed from: package-private */
    public RecurringDetailViewModel(Application application, int recurringId) {
        super(application);
        AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(getApplication());
        this.recurring = appDatabaseObject.recurringDaoObject().getLiveRecurringById(SharePreferenceHelper.getAccountId(application), recurringId);
    }

    public LiveData<Recurring> getRecurring() {
        return this.recurring;
    }
}
