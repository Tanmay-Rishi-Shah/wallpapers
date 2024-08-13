package com.expance.merapaisa.Database.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Model.WalletDetail;
import com.expance.merapaisa.Utility.SharePreferenceHelper;

import java.util.Calendar;

/* loaded from: classes3.dex */
public class WalletNameViewModel extends AndroidViewModel {
    AppDatabaseObject appDatabase;
    private MutableLiveData<Integer> walletId;
    private final LiveData<WalletDetail> walletName;

    public WalletNameViewModel(final Application application) {
        super(application);
        this.walletId = new MutableLiveData<>();
        this.appDatabase = AppDatabaseObject.getInstance(getApplication());
        this.walletName = Transformations.switchMap(this.walletId, (Integer walletId) ->
                WalletNameViewModel.this.m202x255fbf2c(application, walletId));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-ktwapps-walletmanager-Database-ViewModel-WalletNameViewModel  reason: not valid java name */
    public /* synthetic */ LiveData m202x255fbf2c(Application application, Integer num) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(14, 0);
        calendar.set(13, 0);
        calendar.set(12, 0);
        calendar.set(11, 0);
        calendar.add(5, 1);
        if (!SharePreferenceHelper.isFutureBalanceOn(application)) {
            calendar.set(1, 10000);
        }
        return this.appDatabase.walletDaoObject().getLiveWalletById(num.intValue(), SharePreferenceHelper.getAccountId(getApplication()), calendar.getTimeInMillis());
    }

    public LiveData<WalletDetail> getWalletName() {
        return this.walletName;
    }

    public void setWalletId(int walletId) {
        this.walletId.setValue(Integer.valueOf(walletId));
    }
}
