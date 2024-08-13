package com.expance.merapaisa.Database.Dao;

import androidx.lifecycle.LiveData;
import com.expance.merapaisa.Database.Entity.DebtEntity;
import com.expance.merapaisa.Database.Entity.DebtTransEntity;
import com.expance.merapaisa.Database.Entity.TransEntity;
import com.expance.merapaisa.Model.Currency;
import com.expance.merapaisa.Model.Debt;
import java.util.List;

/* loaded from: classes3.dex */
public interface DebtDaoObject {
    void deleteAllDebtTrans(int id);

    void deleteAllTransaction(int id);

    void deleteDebt(int id);

    void deleteDebtTrans(int id);

    void deleteTransactionFromDebtTransId(int debtId, int debtTransId);

    LiveData<List<Debt>> getAllDebt(int accountId, int status);

    List<DebtTransEntity> getAllDebtTrans(int debtId);

    LiveData<List<Debt>> getDebt(int accountId, int type);

    DebtEntity getDebtById(int id);

    Currency getDebtCurrency(int accountId, int debtId);

    int getDebtId(int accountId);

    LiveData<List<DebtTransEntity>> getDebtTrans(int debtId);

    DebtTransEntity getDebtTransById(int id);

    Currency getDebtTransCurrency(int accountId, int debtId, int debtTransId);

    String getDebtTransWallet(int debtId, int debtTransId);

    String getDebtWallet(int debtId);

    LiveData<Debt> getLiveDebtById(int id);

    int getLoanId(int accountId);

    int getReceiveId(int accountId);

    int getRepayId(int accountId);

    TransEntity getTransactionFromDebtId(int id);

    TransEntity getTransactionFromDebtTransId(int debtId, int debtTransId);

    List<TransEntity> getTransactionFromDebtTransList(int debtId);

    long insertDebt(DebtEntity debtEntity);

    long insertDebtTrans(DebtTransEntity debtTransEntity);

    void updateDebt(DebtEntity debtEntity);

    void updateDebtTrans(DebtTransEntity debtTransEntity);
}
