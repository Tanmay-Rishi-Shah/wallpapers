package com.expance.merapaisa.Database.ViewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Model.DailyTrans;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.List;

/* loaded from: classes3.dex */
public class TransactionViewModel extends AndroidViewModel {
    private LiveData<List<DailyTrans>> trans;

    public TransactionViewModel(Application application) {
        super(application);
        this.trans = AppDatabaseObject.getInstance(getApplication()).transDaoObject().getDailyTrans(SharePreferenceHelper.getAccountId(getApplication()));
    }

    public LiveData<List<DailyTrans>> getTrans() {
        return this.trans;
    }
}
