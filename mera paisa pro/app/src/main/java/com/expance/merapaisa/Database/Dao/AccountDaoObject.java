package com.expance.merapaisa.Database.Dao;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SupportSQLiteQuery;
import com.expance.merapaisa.Database.Entity.AccountEntity;
import com.expance.merapaisa.Model.Account;
import java.util.List;

/* loaded from: classes3.dex */
public interface AccountDaoObject {
    int checkpoint(SupportSQLiteQuery supportSQLiteQuery);

    void deleteAccount(int id);

    Long getAccountBalance(int id, int status, long date);

    int getAccountLastOrdering();

    List<AccountEntity> getAllAccountEntity();

    AccountEntity getEntityById(int id);

    LiveData<List<Account>> getLiveAllAccount();

    long insertAccount(AccountEntity accountEntity);

    void restoreMedia();

    void updateAccount(AccountEntity accountEntity);

    void updateAccountOrdering(int order, int id);
}
