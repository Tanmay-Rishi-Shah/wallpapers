package com.expance.merapaisa.Database.Dao;

import android.database.Cursor;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import com.expance.merapaisa.Database.Converter.DateConverter;
import com.expance.merapaisa.Model.ExportCategory;
import com.expance.merapaisa.Model.ExportReport;
import com.expance.merapaisa.Model.ExportWallet;
import com.expance.merapaisa.Model.Exports;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes3.dex */
public final class ExportDaoObject_Impl implements ExportDaoObject {
    private final RoomDatabase __db;

    public ExportDaoObject_Impl(RoomDatabase __db) {
        this.__db = __db;
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.ExportDaoObject
    public List<Exports> getAllExportList(final int accountId, final long startDate, final long endDate) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT t.date_time as datetime,t.amount,t.type,c.name as category,c.default_category as categoryDefault,w.name as wallet, tw.name as transferWallet, w.currency,t.note as memo FROM trans as t LEFT JOIN category as c ON t.category_id = c.id LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN wallet as tw ON t.transfer_wallet_id = tw.id WHERE t.date_time >= ? AND t.date_time < ? AND t.account_id = ? ORDER BY t.date_time DESC", 3);
        acquire.bindLong(1, startDate);
        acquire.bindLong(2, endDate);
        acquire.bindLong(3, accountId);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                Date date = DateConverter.toDate(query.isNull(0) ? null : Long.valueOf(query.getLong(0)));
                long j = query.getLong(1);
                int i = query.getInt(2);
                String string = query.isNull(3) ? null : query.getString(3);
                int i2 = query.getInt(4);
                arrayList.add(new Exports(date, j, i, query.isNull(7) ? null : query.getString(7), string, query.isNull(5) ? null : query.getString(5), query.isNull(6) ? null : query.getString(6), query.isNull(8) ? null : query.getString(8), i2));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.ExportDaoObject
    public List<Exports> getWalletExportList(final int accountId, final List<Integer> walletIds, final long startDate, final long endDate) {
        StringBuilder newStringBuilder = StringUtil.newStringBuilder();
        newStringBuilder.append("SELECT t.date_time as datetime,t.amount,t.type,c.name as category,c.default_category as categoryDefault,w.name as wallet, tw.name as transferWallet, w.currency,t.note as memo FROM trans as t LEFT JOIN category as c ON t.category_id = c.id LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN wallet as tw ON t.transfer_wallet_id = tw.id WHERE t.date_time >= ");
        newStringBuilder.append("?");
        newStringBuilder.append(" AND t.date_time < ");
        newStringBuilder.append("?");
        newStringBuilder.append(" AND t.account_id = ");
        newStringBuilder.append("?");
        newStringBuilder.append(" AND (t.wallet_id IN (");
        int size = walletIds.size();
        StringUtil.appendPlaceholders(newStringBuilder, size);
        newStringBuilder.append(") OR t.transfer_wallet_id IN (");
        int size2 = walletIds.size();
        StringUtil.appendPlaceholders(newStringBuilder, size2);
        newStringBuilder.append(")) ORDER BY t.date_time DESC");
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire(newStringBuilder.toString(), size + 3 + size2);
        acquire.bindLong(1, startDate);
        acquire.bindLong(2, endDate);
        acquire.bindLong(3, accountId);
        int i = 4;
        for (Integer num : walletIds) {
            if (num == null) {
                acquire.bindNull(i);
            } else {
                acquire.bindLong(i, num.intValue());
            }
            i++;
        }
        int i2 = size + 4;
        for (Integer num2 : walletIds) {
            if (num2 == null) {
                acquire.bindNull(i2);
            } else {
                acquire.bindLong(i2, num2.intValue());
            }
            i2++;
        }
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                Date date = DateConverter.toDate(query.isNull(0) ? null : Long.valueOf(query.getLong(0)));
                long j = query.getLong(1);
                int i3 = query.getInt(2);
                String string = query.isNull(3) ? null : query.getString(3);
                int i4 = query.getInt(4);
                arrayList.add(new Exports(date, j, i3, query.isNull(7) ? null : query.getString(7), string, query.isNull(5) ? null : query.getString(5), query.isNull(6) ? null : query.getString(6), query.isNull(8) ? null : query.getString(8), i4));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.ExportDaoObject
    public List<ExportCategory> getAllCategoryList(final int accountId, final long startDate, final long endDate) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT c.name, c.default_category as categoryDefault, t.amount, c.type FROM category as c LEFT JOIN (SELECT c.id,SUM(ROUND(t.amount * cu.rate)) as amount FROM trans as t LEFT JOIN category as c ON t.category_id = c.id LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as cu ON w.currency = cu.code WHERE t.account_id = ? AND t.date_time >= ? AND t.date_time < ? AND c.account_id = ? AND w.account_id = ? AND t.type != 2 GROUP BY c.id) as t ON c.id = t.id WHERE c.account_id = ?", 6);
        long j = accountId;
        acquire.bindLong(1, j);
        acquire.bindLong(2, startDate);
        acquire.bindLong(3, endDate);
        acquire.bindLong(4, j);
        acquire.bindLong(5, j);
        acquire.bindLong(6, j);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                arrayList.add(new ExportCategory(query.isNull(0) ? null : query.getString(0), query.getInt(1), query.getInt(3), query.getLong(2)));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.ExportDaoObject
    public List<ExportCategory> getWalletCategoryList(final int accountId, final List<Integer> walletIds, final long startDate, final long endDate) {
        StringBuilder newStringBuilder = StringUtil.newStringBuilder();
        newStringBuilder.append("SELECT c.name, c.default_category as categoryDefault, t.amount, c.type FROM category as c LEFT JOIN (SELECT c.id,SUM(ROUND(t.amount * cu.rate)) as amount FROM trans as t LEFT JOIN category as c ON t.category_id = c.id LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as cu ON w.currency = cu.code WHERE t.account_id = ");
        newStringBuilder.append("?");
        newStringBuilder.append(" AND t.date_time >= ");
        newStringBuilder.append("?");
        newStringBuilder.append(" AND t.date_time < ");
        newStringBuilder.append("?");
        newStringBuilder.append(" AND c.account_id = ");
        newStringBuilder.append("?");
        newStringBuilder.append(" AND w.account_id = ");
        newStringBuilder.append("?");
        newStringBuilder.append(" AND t.type != 2 AND t.wallet_id IN (");
        int size = walletIds.size();
        StringUtil.appendPlaceholders(newStringBuilder, size);
        newStringBuilder.append(") GROUP BY c.id) as t ON c.id = t.id WHERE c.account_id = ");
        newStringBuilder.append("?");
        newStringBuilder.append(StringUtils.SPACE);
        int i = 6;
        int i2 = size + 6;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire(newStringBuilder.toString(), i2);
        long j = accountId;
        acquire.bindLong(1, j);
        acquire.bindLong(2, startDate);
        acquire.bindLong(3, endDate);
        acquire.bindLong(4, j);
        acquire.bindLong(5, j);
        for (Integer num : walletIds) {
            if (num == null) {
                acquire.bindNull(i);
            } else {
                acquire.bindLong(i, num.intValue());
            }
            i++;
        }
        acquire.bindLong(i2, j);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                arrayList.add(new ExportCategory(query.isNull(0) ? null : query.getString(0), query.getInt(1), query.getInt(3), query.getLong(2)));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.ExportDaoObject
    public List<ExportWallet> getAllWalletList(final int accountId, final long startDate, final long endDate) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT w.name, w.currency, SUM(t1.income) as income, SUM(t1.expense) as expense FROM (   SELECT 0 as income, SUM(amount) as expense,wallet_id as id FROM trans WHERE account_id = ? AND date_time >= ? AND date_time < ? AND type = 1 GROUP BY wallet_id    UNION ALL    SELECT SUM(amount) as income, 0 as expense,wallet_id as id FROM trans WHERE account_id = ? AND date_time >= ? AND date_time < ? AND type = 0 GROUP BY wallet_id ) as t1 LEFT JOIN wallet as w ON t1.id = w.id GROUP BY w.id", 6);
        long j = accountId;
        acquire.bindLong(1, j);
        acquire.bindLong(2, startDate);
        acquire.bindLong(3, endDate);
        acquire.bindLong(4, j);
        acquire.bindLong(5, startDate);
        acquire.bindLong(6, endDate);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                arrayList.add(new ExportWallet(query.getLong(2), query.getLong(3), query.isNull(1) ? null : query.getString(1), query.isNull(0) ? null : query.getString(0)));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.ExportDaoObject
    public List<ExportWallet> getWalletList(final int accountId, final List<Integer> walletIds, final long startDate, final long endDate) {
        Integer next = null;
        Iterator<Integer> it;
        StringBuilder newStringBuilder = StringUtil.newStringBuilder();
        newStringBuilder.append("SELECT w.name, w.currency, SUM(t1.income) as income, SUM(t1.expense) as expense FROM (   SELECT 0 as income, SUM(amount) as expense,wallet_id as id FROM trans WHERE account_id = ");
        newStringBuilder.append("?");
        newStringBuilder.append(" AND date_time >= ");
        newStringBuilder.append("?");
        newStringBuilder.append(" AND date_time < ");
        newStringBuilder.append("?");
        newStringBuilder.append(" AND type = 1 AND wallet_id IN (");
        int size = walletIds.size();
        StringUtil.appendPlaceholders(newStringBuilder, size);
        newStringBuilder.append(") GROUP BY wallet_id    UNION ALL    SELECT SUM(amount) as income, 0 as expense,wallet_id as id FROM trans WHERE account_id = ");
        newStringBuilder.append("?");
        newStringBuilder.append(" AND date_time >= ");
        newStringBuilder.append("?");
        newStringBuilder.append(" AND date_time < ");
        newStringBuilder.append("?");
        newStringBuilder.append(" AND type = 0 AND wallet_id IN (");
        int size2 = walletIds.size();
        StringUtil.appendPlaceholders(newStringBuilder, size2);
        newStringBuilder.append(") GROUP BY wallet_id ) as t1 LEFT JOIN wallet as w ON t1.id = w.id GROUP BY w.id");
        int i = size + 6;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire(newStringBuilder.toString(), size2 + i);
        long j = accountId;
        acquire.bindLong(1, j);
        acquire.bindLong(2, startDate);
        acquire.bindLong(3, endDate);
        Iterator<Integer> it2 = walletIds.iterator();
        int i2 = 4;
        while (it2.hasNext()) {
            if (it2.next() == null) {
                acquire.bindNull(i2);
                it = it2;
            } else {
                it = it2;
                acquire.bindLong(i2, next.intValue());
            }
            i2++;
            it2 = it;
        }
        acquire.bindLong(size + 4, j);
        acquire.bindLong(size + 5, startDate);
        acquire.bindLong(i, endDate);
        int i3 = size + 7;
        for (Integer num : walletIds) {
            if (num == null) {
                acquire.bindNull(i3);
            } else {
                acquire.bindLong(i3, num.intValue());
            }
            i3++;
        }
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                arrayList.add(new ExportWallet(query.getLong(2), query.getLong(3), query.isNull(1) ? null : query.getString(1), query.isNull(0) ? null : query.getString(0)));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.ExportDaoObject
    public ExportReport getAllReport(final int accountId, final long startDate, final long endDate) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT (SELECT SUM(income) FROM (   SELECT * FROM (SELECT SUM(ROUND(t.amount*c.rate)) as income FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as c ON w.currency = c.code WHERE t.type = 0 AND t.date_time >= ? AND t.date_time < ? AND t.account_id = ? AND w.account_id = ? AND c.account_id = ?) as t1 )) as income,(SELECT SUM(expense) FROM(   SELECT * FROM (SELECT SUM(ROUND(t.amount*c.rate)) as expense FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as c ON w.currency = c.code WHERE t.type = 1 AND t.date_time >= ? AND t.date_time < ? AND t.account_id = ? AND w.account_id = ? AND c.account_id = ?) as t1))as expense", 10);
        acquire.bindLong(1, startDate);
        acquire.bindLong(2, endDate);
        long j = accountId;
        acquire.bindLong(3, j);
        acquire.bindLong(4, j);
        acquire.bindLong(5, j);
        acquire.bindLong(6, startDate);
        acquire.bindLong(7, endDate);
        acquire.bindLong(8, j);
        acquire.bindLong(9, j);
        acquire.bindLong(10, j);
        this.__db.assertNotSuspendingTransaction();
        ExportReport exportReport = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            if (query.moveToFirst()) {
                exportReport = new ExportReport();
                exportReport.setIncome(query.getLong(0));
                exportReport.setExpense(query.getLong(1));
            }
            return exportReport;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.ExportDaoObject
    public ExportReport getWalletReport(final int accountId, final List<Integer> walletIds, final long startDate, final long endDate) {
        StringBuilder newStringBuilder = StringUtil.newStringBuilder();
        newStringBuilder.append("SELECT (SELECT SUM(income) FROM (   SELECT * FROM (SELECT SUM(ROUND(t.amount*c.rate)) as income FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as c ON w.currency = c.code WHERE t.type = 0 AND t.date_time >= ");
        newStringBuilder.append("?");
        newStringBuilder.append(" AND t.date_time < ");
        newStringBuilder.append("?");
        newStringBuilder.append(" AND t.account_id = ");
        newStringBuilder.append("?");
        newStringBuilder.append(" AND w.account_id = ");
        newStringBuilder.append("?");
        newStringBuilder.append(" AND c.account_id = ");
        newStringBuilder.append("?");
        newStringBuilder.append(" AND t.wallet_id IN (");
        int size = walletIds.size();
        StringUtil.appendPlaceholders(newStringBuilder, size);
        newStringBuilder.append(")) as t1 )) as income,(SELECT SUM(expense) FROM(   SELECT * FROM (SELECT SUM(ROUND(t.amount*c.rate)) as expense FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as c ON w.currency = c.code WHERE t.type = 1 AND t.date_time >= ");
        newStringBuilder.append("?");
        newStringBuilder.append(" AND t.date_time < ");
        newStringBuilder.append("?");
        newStringBuilder.append(" AND t.account_id = ");
        newStringBuilder.append("?");
        newStringBuilder.append(" AND w.account_id = ");
        newStringBuilder.append("?");
        newStringBuilder.append(" AND c.account_id = ");
        newStringBuilder.append("?");
        newStringBuilder.append(" AND t.wallet_id IN (");
        int size2 = walletIds.size();
        StringUtil.appendPlaceholders(newStringBuilder, size2);
        newStringBuilder.append(")) as t1))as expense");
        int i = size + 10;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire(newStringBuilder.toString(), size2 + i);
        acquire.bindLong(1, startDate);
        acquire.bindLong(2, endDate);
        long j = accountId;
        acquire.bindLong(3, j);
        acquire.bindLong(4, j);
        acquire.bindLong(5, j);
        int i2 = 6;
        for (Integer num : walletIds) {
            if (num == null) {
                acquire.bindNull(i2);
            } else {
                acquire.bindLong(i2, num.intValue());
            }
            i2++;
        }
        acquire.bindLong(size + 6, startDate);
        acquire.bindLong(size + 7, endDate);
        acquire.bindLong(size + 8, j);
        acquire.bindLong(size + 9, j);
        acquire.bindLong(i, j);
        int i3 = size + 11;
        for (Integer num2 : walletIds) {
            if (num2 == null) {
                acquire.bindNull(i3);
            } else {
                acquire.bindLong(i3, num2.intValue());
            }
            i3++;
        }
        this.__db.assertNotSuspendingTransaction();
        ExportReport exportReport = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            if (query.moveToFirst()) {
                exportReport = new ExportReport();
                exportReport.setIncome(query.getLong(0));
                exportReport.setExpense(query.getLong(1));
            }
            return exportReport;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public static List<Class<?>> getRequiredConverters() {
        return Collections.emptyList();
    }
}
