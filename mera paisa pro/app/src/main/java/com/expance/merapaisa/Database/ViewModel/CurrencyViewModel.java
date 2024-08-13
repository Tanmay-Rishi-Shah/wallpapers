package com.expance.merapaisa.Database.ViewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Model.Currencies;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.List;

/* loaded from: classes3.dex */
public class CurrencyViewModel extends AndroidViewModel {
    private final LiveData<List<Currencies>> currencyList;

    public CurrencyViewModel(Application application) {
        super(application);
        AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(getApplication());
        this.currencyList = appDatabaseObject.currencyDaoObject().getAllCurrencies(SharePreferenceHelper.getAccountId(getApplication()));
    }

    public LiveData<List<Currencies>> getCurrencyList() {
        return this.currencyList;
    }
}
