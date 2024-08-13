package com.expance.merapaisa.Database.Dao;

import android.database.Cursor;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.DBUtil;
import com.expance.merapaisa.Database.Converter.DateConverter;
import com.expance.merapaisa.Model.CalendarRecord;
import com.expance.merapaisa.Model.CalendarSummary;
import com.expance.merapaisa.Model.DailyTrans;
import com.expance.merapaisa.Model.Trans;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes3.dex */
public final class CalenderDaoObject_Impl implements CalenderDaoObject {
    private final RoomDatabase __db;

    public CalenderDaoObject_Impl(RoomDatabase __db) {
        this.__db = __db;
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.CalenderDaoObject
    public List<CalendarRecord> getRecord(final long startDate, final long endDate, final int accountId) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT SUM(income) as income, SUM(expense) as expense, date FROM (SELECT SUM(income) as income, 0 as expense, date FROM (   SELECT SUM(ROUND(t.amount*c.rate)) as income, CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as date FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as c ON w.currency = c.code WHERE t.type = 0 AND t.date_time >= ? AND t.date_time < ? AND t.account_id = ? AND w.account_id = ? AND c.account_id = ? GROUP BY date   UNION ALL    SELECT SUM(income) as income, date FROM (       SELECT 0 as income, CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as date FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN wallet as tw ON t.transfer_wallet_id = tw.id LEFT JOIN currency as c1 ON w.currency = c1.code LEFT JOIN currency as c2 ON tw.currency = c2.code WHERE t.account_id = ? AND w.account_id = ? AND tw.account_id = ? AND c1.account_id = ? AND c2.account_id = ? AND t.type = 2 AND t.date_time >= ? AND t.date_time < ?   ) as t3 GROUP BY date) GROUP BY date UNION ALL SELECT 0 as income, SUM(expense) as expense, date FROM(   SELECT SUM(ROUND(t.amount*c.rate)) as expense, CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as date FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as c ON w.currency = c.code WHERE t.type = 1 AND t.date_time >= ? AND t.date_time < ? AND t.account_id = ? AND w.account_id = ? AND c.account_id = ? GROUP BY date   UNION ALL    SELECT SUM(expense) as expense,date FROM (       SELECT 0 as expense, CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as date FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN wallet as tw ON t.transfer_wallet_id = tw.id LEFT JOIN currency as c1 ON w.currency = c1.code LEFT JOIN currency as c2 ON tw.currency = c2.code WHERE t.account_id = ? AND w.account_id = ? AND tw.account_id = ? AND c1.account_id = ? AND c2.account_id = ? AND t.type = 2 AND t.date_time >= ? AND t.date_time < ?   ) as t3 GROUP BY date) GROUP BY date ) GROUP BY date", 24);
        acquire.bindLong(1, startDate);
        acquire.bindLong(2, endDate);
        long j = accountId;
        acquire.bindLong(3, j);
        acquire.bindLong(4, j);
        acquire.bindLong(5, j);
        acquire.bindLong(6, j);
        acquire.bindLong(7, j);
        acquire.bindLong(8, j);
        acquire.bindLong(9, j);
        acquire.bindLong(10, j);
        acquire.bindLong(11, startDate);
        acquire.bindLong(12, endDate);
        acquire.bindLong(13, startDate);
        acquire.bindLong(14, endDate);
        acquire.bindLong(15, j);
        acquire.bindLong(16, j);
        acquire.bindLong(17, j);
        acquire.bindLong(18, j);
        acquire.bindLong(19, j);
        acquire.bindLong(20, j);
        acquire.bindLong(21, j);
        acquire.bindLong(22, j);
        acquire.bindLong(23, startDate);
        acquire.bindLong(24, endDate);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                arrayList.add(new CalendarRecord(query.getInt(2), query.getLong(0), query.getLong(1)));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.CalenderDaoObject
    public CalendarSummary getSummary(final long startDate, final long endDate, final int accountId) {
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
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            return query.moveToFirst() ? new CalendarSummary(query.getLong(0), query.getLong(1)) : null;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.CalenderDaoObject
    public DailyTrans[] getDailyTrans(final int accountId, final long startDate, final long endDate) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT SUM(amount) as amount, day, month, year FROM (SELECT * FROM (SELECT SUM(ROUND(t.amount*c.rate)) as amount, CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as day,CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as month,CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as year FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as c ON w.currency = c.code WHERE t.account_id = ? AND t.type != 2 AND t.date_time >= ? AND t.date_time < ? AND w.account_id = ? AND c.account_id = ? GROUP BY CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) ORDER BY CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC) as t1 UNION ALL SELECT * FROM (SELECT 0 as amount, CAST(strftime('%d', datetime(date_time/1000, 'unixepoch','localtime')) AS int) as day,CAST(strftime('%m', datetime(date_time/1000, 'unixepoch','localtime')) AS int) as month,CAST(strftime('%Y', datetime(date_time/1000, 'unixepoch','localtime')) AS int) as year FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN wallet as tw ON t.transfer_wallet_id = tw.id LEFT JOIN currency as c1 ON w.currency = c1.code LEFT JOIN currency as c2 ON tw.currency = c2.code WHERE t.account_id = ? AND t.date_time >= ? AND t.date_time < ?  AND w.account_id = ? AND tw.account_id = ? AND c1.account_id = ? AND c2.account_id = ? AND t.type = 2 GROUP BY CAST(strftime('%d', datetime(date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%m', datetime(date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%Y', datetime(date_time/1000, 'unixepoch','localtime')) AS int) ORDER BY CAST(strftime('%Y', datetime(date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%m', datetime(date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%d', datetime(date_time/1000, 'unixepoch','localtime')) AS int) DESC) as t2 ) as t1 GROUP BY day, month, year ORDER BY year DESC,month DESC,day DESC", 12);
        long j = accountId;
        acquire.bindLong(1, j);
        acquire.bindLong(2, startDate);
        acquire.bindLong(3, endDate);
        acquire.bindLong(4, j);
        acquire.bindLong(5, j);
        acquire.bindLong(6, j);
        acquire.bindLong(7, startDate);
        acquire.bindLong(8, endDate);
        acquire.bindLong(9, j);
        acquire.bindLong(10, j);
        acquire.bindLong(11, j);
        acquire.bindLong(12, j);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            DailyTrans[] dailyTransArr = new DailyTrans[query.getCount()];
            int i = 0;
            while (query.moveToNext()) {
                dailyTransArr[i] = new DailyTrans(query.getInt(1), query.getInt(2), query.getInt(3), query.getLong(0));
                i++;
            }
            return dailyTransArr;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.CalenderDaoObject
    public Trans[] getTransFromDate(final int accountId, final long startDate, final long endDate) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT d.id as debtId, d.color as debtColor,d.type as debtType,dt.id as debtTransId, dt.type as debtTransType, t.id,t.note, t.memo,t.trans_amount as transAmount, t.subcategory_id as subcategoryId, sc.name as subcategory,c.icon,c.color,c.default_category as categoryDefault,t.fee_id as feeId,cu.code as currency,t.amount,t.date_time as dateTime,w.name as wallet, tw.name as transferWallet,t.type , t.wallet_id as walletId, t.transfer_wallet_id as transferWalletId, c.name as category, c.id as categoryId, GROUP_CONCAT(m.path) as media FROM trans t LEFT JOIN category as c ON t.category_id = c.id LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as cu ON w.currency = cu.code LEFT JOIN wallet as tw ON t.transfer_wallet_id = tw.id LEFT JOIN media as m ON m.trans_id = t.id LEFT JOIN debt as d ON t.debt_id = d.id LEFT JOIN debtTrans as dt ON t.debt_trans_id = dt.id LEFT JOIN subcategory as sc ON t.subcategory_id = sc.id WHERE t.date_time >= ? AND t.date_time < ? AND t.account_id = ? AND cu.account_id = ? AND w.account_id = ? GROUP BY t.id ORDER BY t.date_time DESC", 5);
        acquire.bindLong(1, startDate);
        acquire.bindLong(2, endDate);
        long j = accountId;
        acquire.bindLong(3, j);
        acquire.bindLong(4, j);
        acquire.bindLong(5, j);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            Trans[] transArr = new Trans[query.getCount()];
            int i = 0;
            while (query.moveToNext()) {
                int i2 = query.getInt(0);
                String string = query.isNull(1) ? null : query.getString(1);
                int i3 = query.getInt(2);
                int i4 = query.getInt(3);
                int i5 = query.getInt(4);
                String string2 = query.isNull(6) ? null : query.getString(6);
                String string3 = query.isNull(7) ? null : query.getString(7);
                long j2 = query.getLong(8);
                int i6 = query.getInt(9);
                String string4 = query.isNull(10) ? null : query.getString(10);
                int i7 = query.getInt(11);
                String string5 = query.isNull(12) ? null : query.getString(12);
                int i8 = query.getInt(13);
                int i9 = query.getInt(14);
                Trans trans = new Trans(string2, string3, string5, i7, query.isNull(15) ? null : query.getString(15), DateConverter.toDate(query.isNull(17) ? null : Long.valueOf(query.getLong(17))), query.getLong(16), query.isNull(18) ? null : query.getString(18), query.getInt(20), query.isNull(19) ? null : query.getString(19), query.getInt(21), query.getInt(22), query.isNull(23) ? null : query.getString(23), query.getInt(24), i8, i9, query.isNull(25) ? null : query.getString(25), j2, string, i2, i3, i4, i5, string4, i6);
                trans.setId(query.getInt(5));
                transArr[i] = trans;
                i++;
            }
            return transArr;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.CalenderDaoObject
    public long getAccountBalance(final int id, final long date) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT SUM(ROUND(w.amount*c.rate)) FROM    (SELECT  w.id, (       SELECT SUM(amount) as amount FROM(           SELECT SUM(amount) as amount FROM trans WHERE wallet_id = w.id AND type != 2 AND date_time < ?           UNION ALL           SELECT SUM(-amount) as amount FROM trans WHERE wallet_id = w.id AND type = 2 AND date_time < ?           UNION ALL           SELECT SUM(trans_amount) as amount FROM trans WHERE transfer_wallet_id = w.id AND type = 2 AND date_time < ?       ) as subt1   ) as amount,w.currency,w.account_id,w.active,w.exclude FROM wallet as w) as w LEFT JOIN currency as c ON w.currency = c.code WHERE w.account_id = ? AND c.account_id = ?", 5);
        acquire.bindLong(1, date);
        acquire.bindLong(2, date);
        acquire.bindLong(3, date);
        long j = id;
        acquire.bindLong(4, j);
        acquire.bindLong(5, j);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            return query.moveToFirst() ? query.getLong(0) : 0L;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public static List<Class<?>> getRequiredConverters() {
        return Collections.emptyList();
    }
}
