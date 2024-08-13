package com.expance.merapaisa.Utility;

import android.content.Context;
import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.Entity.DebtEntity;
import com.expance.merapaisa.Database.Entity.DebtTransEntity;
import com.expance.merapaisa.Database.Entity.TransEntity;
import com.expance.merapaisa.Model.Currency;
import com.expance.merapaisa.Model.Trans;
import java.io.File;
import java.util.List;
import java.util.concurrent.Executors;

/* loaded from: classes3.dex */
public class DatabaseTaskHelper {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void deleteTransaction(final Context context, final Trans trans) {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.Utility.DatabaseTaskHelper.1
            @Override // java.lang.Runnable
            public void run() {
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(context);
                if (trans.getFeeId() != 0) {
                    appDatabaseObject.transDaoObject().deleteTrans(trans.getFeeId());
                } else {
                    int transferTrans = appDatabaseObject.transDaoObject().getTransferTrans(trans.getId());
                    if (transferTrans != 0) {
                        TransEntity transEntityById = appDatabaseObject.transDaoObject().getTransEntityById(transferTrans);
                        transEntityById.setFeeId(0);
                        appDatabaseObject.transDaoObject().updateTrans(transEntityById);
                    }
                }
                appDatabaseObject.transDaoObject().deleteTrans(trans.getId());
                if (trans.getMedia() != null) {
                    for (String str : trans.getMedia().split(",")) {
                        File file = new File(str);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                }
                appDatabaseObject.transDaoObject().deleteTransMedia(trans.getId());
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(Constant.BROADCAST_UPDATED));
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void deleteDebtTrans(final Context context, final int transId, final int debtId, final int debtTransId) {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.Utility.DatabaseTaskHelper.2
            @Override // java.lang.Runnable
            public void run() {
                int accountId = SharePreferenceHelper.getAccountId(context);
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(context);
                appDatabaseObject.transDaoObject().deleteTrans(transId);
                appDatabaseObject.debtDaoObject().deleteDebtTrans(debtTransId);
                DebtEntity debtById = appDatabaseObject.debtDaoObject().getDebtById(debtId);
                Currency debtCurrency = appDatabaseObject.debtDaoObject().getDebtCurrency(accountId, debtId);
                if (debtCurrency == null) {
                    debtCurrency = new Currency(1.0d, "Empty");
                }
                List<DebtTransEntity> allDebtTrans = appDatabaseObject.debtDaoObject().getAllDebtTrans(debtId);
                long amount = debtById.getAmount();
                long j = 0;
                for (DebtTransEntity debtTransEntity : allDebtTrans) {
                    Currency debtTransCurrency = appDatabaseObject.debtDaoObject().getDebtTransCurrency(accountId, debtId, debtTransEntity.getId());
                    if (debtTransCurrency == null) {
                        if (debtTransEntity.getType() == 0) {
                            j += debtTransEntity.getAmount();
                        } else {
                            amount += debtTransEntity.getAmount();
                        }
                    } else {
                        double rate = debtTransCurrency.getRate() / debtCurrency.getRate();
                        if (debtTransEntity.getType() == 0) {
                            j = (long) (j + (debtTransEntity.getAmount() * rate));
                        } else {
                            amount = (long) (amount + (debtTransEntity.getAmount() * rate));
                        }
                    }
                }
                if (j >= amount) {
                    debtById.setStatus(1);
                } else {
                    debtById.setStatus(0);
                }
                appDatabaseObject.debtDaoObject().updateDebt(debtById);
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(Constant.BROADCAST_UPDATED));
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void deleteDebt(final Context context, final int debtId) {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.Utility.DatabaseTaskHelper.3
            @Override // java.lang.Runnable
            public void run() {
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(context);
                appDatabaseObject.debtDaoObject().deleteDebt(debtId);
                appDatabaseObject.debtDaoObject().deleteAllTransaction(debtId);
                appDatabaseObject.debtDaoObject().deleteAllDebtTrans(debtId);
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(Constant.BROADCAST_UPDATED));
            }
        });
    }
}
