package com.expance.merapaisa.Database.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Model.Wallets;
import com.expance.merapaisa.Utility.SharePreferenceHelper;

import java.util.List;

/* loaded from: classes3.dex */
public class WalletViewModel extends AndroidViewModel {
    private AppDatabaseObject appDatabase;
    private final LiveData<List<Wallets>> archiveWalletsList;
    public LiveData<Integer> archived;
    private MutableLiveData<Long> date;
    private final LiveData<List<Wallets>> walletsList;

    public WalletViewModel(Application application) {
        super(application);
        this.date = new MutableLiveData<>();
        this.appDatabase = AppDatabaseObject.getInstance(getApplication());
        this.walletsList = Transformations.switchMap(this.date, WalletViewModel.this::m203xa2925ef7);
        this.archiveWalletsList = Transformations.switchMap(this.date, WalletViewModel.this::m204x36d0ce96);
        this.archived = this.appDatabase.walletDaoObject().getArchieveWalletRow(SharePreferenceHelper.getAccountId(getApplication()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-ktwapps-walletmanager-Database-ViewModel-WalletViewModel  reason: not valid java name */
    public /* synthetic */ LiveData m203xa2925ef7(Long l) {
        return this.appDatabase.walletDaoObject().getAllWallets(SharePreferenceHelper.getAccountId(getApplication()), 0, l.longValue());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-ktwapps-walletmanager-Database-ViewModel-WalletViewModel  reason: not valid java name */
    public /* synthetic */ LiveData m204x36d0ce96(Long l) {
        return this.appDatabase.walletDaoObject().getHiddenWallets(SharePreferenceHelper.getAccountId(getApplication()), 0, l.longValue());
    }

    public LiveData<List<Wallets>> getWalletsList() {
        return this.walletsList;
    }

    public LiveData<List<Wallets>> getHiddenWalletsList() {
        return this.archiveWalletsList;
    }

    public void setDate(long date) {
        this.date.setValue(Long.valueOf(date));
    }
}
