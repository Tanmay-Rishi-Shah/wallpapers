package com.expance.merapaisa.Database.Dao;

import com.expance.merapaisa.Model.ExportCategory;
import com.expance.merapaisa.Model.ExportReport;
import com.expance.merapaisa.Model.ExportWallet;
import com.expance.merapaisa.Model.Exports;
import java.util.List;

/* loaded from: classes3.dex */
public interface ExportDaoObject {
    List<ExportCategory> getAllCategoryList(int accountId, long startDate, long endDate);

    List<Exports> getAllExportList(int accountId, long startDate, long endDate);

    ExportReport getAllReport(int accountId, long startDate, long endDate);

    List<ExportWallet> getAllWalletList(int accountId, long startDate, long endDate);

    List<ExportCategory> getWalletCategoryList(int accountId, List<Integer> walletIds, long startDate, long endDate);

    List<Exports> getWalletExportList(int accountId, List<Integer> walletIds, long startDate, long endDate);

    List<ExportWallet> getWalletList(int accountId, List<Integer> walletIds, long startDate, long endDate);

    ExportReport getWalletReport(int accountId, List<Integer> walletIds, long startDate, long endDate);
}
