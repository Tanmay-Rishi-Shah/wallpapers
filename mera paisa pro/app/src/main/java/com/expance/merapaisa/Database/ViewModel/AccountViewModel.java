package com.expance.merapaisa.Database.ViewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Model.Account;
import java.util.List;

/* loaded from: classes3.dex */
public class AccountViewModel extends AndroidViewModel {
    private LiveData<List<Account>> accountList;

    public AccountViewModel(Application application) {
        super(application);
        this.accountList = AppDatabaseObject.getInstance(getApplication()).accountDaoObject().getLiveAllAccount();
    }

    public LiveData<List<Account>> getAccountList() {
        return this.accountList;
    }
}
