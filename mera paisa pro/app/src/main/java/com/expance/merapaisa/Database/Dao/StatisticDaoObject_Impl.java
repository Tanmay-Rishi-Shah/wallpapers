package com.expance.merapaisa.Database.Dao;

import android.database.Cursor;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.expance.merapaisa.Database.Converter.DateConverter;
import com.expance.merapaisa.Model.CalendarSummary;
import com.expance.merapaisa.Model.DailyTrans;
import com.expance.merapaisa.Model.Stats;
import com.expance.merapaisa.Model.SubcategoryStats;
import com.expance.merapaisa.Model.Trans;
import com.expance.merapaisa.Model.WeeklyStats;

import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/* loaded from: classes3.dex */
public final class StatisticDaoObject_Impl implements StatisticDaoObject {
    private final RoomDatabase __db;

    public StatisticDaoObject_Impl(RoomDatabase __db) {
        this.__db = __db;
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.StatisticDaoObject
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

    @Override // com.ktwapps.walletmanager.Database.Dao.StatisticDaoObject
    public CalendarSummary getSummary(final int accountId) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT (SELECT SUM(income) FROM (   SELECT * FROM (SELECT SUM(ROUND(t.amount*c.rate)) as income FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as c ON w.currency = c.code WHERE t.type = 0  AND t.account_id = ? AND w.account_id = ? AND c.account_id = ?) as t1 )) as income,(SELECT SUM(expense) FROM(   SELECT * FROM (SELECT SUM(ROUND(t.amount*c.rate)) as expense FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as c ON w.currency = c.code WHERE t.type = 1 AND t.account_id = ? AND w.account_id = ? AND c.account_id = ?) as t1))as expense", 6);
        long j = accountId;
        acquire.bindLong(1, j);
        acquire.bindLong(2, j);
        acquire.bindLong(3, j);
        acquire.bindLong(4, j);
        acquire.bindLong(5, j);
        acquire.bindLong(6, j);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            return query.moveToFirst() ? new CalendarSummary(query.getLong(0), query.getLong(1)) : null;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.StatisticDaoObject
    public List<Stats> getPieStats(final int accountId, final long startDate, final long endDate, final int type) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT e.*,     CAST(printf('%.1f', ((e.amount*1.0)*100/(SELECT SUM(ROUND(t.amount*c.rate))*1.0 FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as c ON w.currency = c.code WHERE t.account_id = ? AND t.date_time >= ? AND t.date_time < ? AND t.type = ? AND c.account_id = ? AND w.account_id = ?))) AS double) as percent FROM (SELECT name,categoryDefault,color,icon,id,SUM(amount) as amount,SUM(trans) as trans FROM (SELECT c.name,c.default_category as categoryDefault,c.color,c.icon,c.id,SUM(ROUND(t.amount*cu.rate)) as amount, COUNT(t.id) as trans FROM trans as t LEFT JOIN category as c ON t.category_id = c.id LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as cu ON w.currency = cu.code WHERE t.account_id = ? AND t.date_time >= ? AND t.date_time < ? AND t.type = ? AND w.account_id = ? AND cu.account_id = ? GROUP BY c.id ) as t1 GROUP BY id) as e ORDER BY percent DESC", 12);
        long j = accountId;
        acquire.bindLong(1, j);
        acquire.bindLong(2, startDate);
        acquire.bindLong(3, endDate);
        long j2 = type;
        acquire.bindLong(4, j2);
        acquire.bindLong(5, j);
        acquire.bindLong(6, j);
        acquire.bindLong(7, j);
        acquire.bindLong(8, startDate);
        acquire.bindLong(9, endDate);
        acquire.bindLong(10, j2);
        acquire.bindLong(11, j);
        acquire.bindLong(12, j);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "name");
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "categoryDefault");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, TypedValues.Custom.S_COLOR);
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "icon");
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "id");
            int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "amount");
            int columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "trans");
            int columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "percent");
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                String string = query.isNull(columnIndexOrThrow) ? null : query.getString(columnIndexOrThrow);
                int i = query.getInt(columnIndexOrThrow2);
                arrayList.add(new Stats(string, query.isNull(columnIndexOrThrow3) ? null : query.getString(columnIndexOrThrow3), query.getInt(columnIndexOrThrow4), query.getLong(columnIndexOrThrow6), query.getDouble(columnIndexOrThrow8), query.getInt(columnIndexOrThrow5), query.getInt(columnIndexOrThrow7), i));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.StatisticDaoObject
    public List<Stats> getAllPieStats(final int accountId, final int type) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT e.*,     CAST(printf('%.1f', ((e.amount*1.0)*100/(SELECT SUM(ROUND(t.amount*c.rate))*1.0 FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as c ON w.currency = c.code WHERE t.account_id = ? AND t.type = ? AND c.account_id = ? AND w.account_id = ?))) AS double) as percent FROM (SELECT name,categoryDefault,color,icon,id,SUM(amount) as amount,SUM(trans) as trans FROM (SELECT c.name,c.default_category as categoryDefault,c.color,c.icon,c.id,SUM(ROUND(t.amount*cu.rate)) as amount, COUNT(t.id) as trans FROM trans as t LEFT JOIN category as c ON t.category_id = c.id LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as cu ON w.currency = cu.code WHERE t.account_id = ? AND t.type = ? AND w.account_id = ? AND cu.account_id = ? GROUP BY c.id ) as t1 GROUP BY id) as e ORDER BY percent DESC", 8);
        long j = accountId;
        acquire.bindLong(1, j);
        long j2 = type;
        acquire.bindLong(2, j2);
        acquire.bindLong(3, j);
        acquire.bindLong(4, j);
        acquire.bindLong(5, j);
        acquire.bindLong(6, j2);
        acquire.bindLong(7, j);
        acquire.bindLong(8, j);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "name");
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "categoryDefault");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, TypedValues.Custom.S_COLOR);
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "icon");
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "id");
            int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "amount");
            int columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "trans");
            int columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "percent");
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                String string = query.isNull(columnIndexOrThrow) ? null : query.getString(columnIndexOrThrow);
                int i = query.getInt(columnIndexOrThrow2);
                arrayList.add(new Stats(string, query.isNull(columnIndexOrThrow3) ? null : query.getString(columnIndexOrThrow3), query.getInt(columnIndexOrThrow4), query.getLong(columnIndexOrThrow6), query.getDouble(columnIndexOrThrow8), query.getInt(columnIndexOrThrow5), query.getInt(columnIndexOrThrow7), i));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.StatisticDaoObject
    public List<SubcategoryStats> getSubcategoryStats(final int accountId, final long startDate, final long endDate, final int categoryId) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT e.*,     CAST(printf('%.1f', ((e.amount*1.0)*100/(SELECT SUM(ROUND(t.amount*cu.rate))*1.0 FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as cu ON w.currency = cu.code WHERE t.account_id = ? AND t.date_time >= ? AND t.date_time < ? AND cu.account_id = ? AND w.account_id = ? AND t.category_id = ?))) AS double) as percent FROM(SELECT sc.name,sc.id,SUM(ROUND(t.amount*cu.rate)) as amount, COUNT(t.id) as trans FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as cu ON w.currency = cu.code LEFT JOIN subcategory as sc ON t.subcategory_id = sc.id WHERE t.account_id = ? AND t.date_time >= ? AND t.date_time < ? AND w.account_id = ? AND cu.account_id = ? AND t.category_id = ? GROUP BY sc.id ) as e ORDER BY percent DESC", 12);
        long j = accountId;
        acquire.bindLong(1, j);
        acquire.bindLong(2, startDate);
        acquire.bindLong(3, endDate);
        acquire.bindLong(4, j);
        acquire.bindLong(5, j);
        long j2 = categoryId;
        acquire.bindLong(6, j2);
        acquire.bindLong(7, j);
        acquire.bindLong(8, startDate);
        acquire.bindLong(9, endDate);
        acquire.bindLong(10, j);
        acquire.bindLong(11, j);
        acquire.bindLong(12, j2);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "name");
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "id");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "amount");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "trans");
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "percent");
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                SubcategoryStats subcategoryStats = new SubcategoryStats();
                subcategoryStats.setName(query.isNull(columnIndexOrThrow) ? null : query.getString(columnIndexOrThrow));
                subcategoryStats.setId(query.getInt(columnIndexOrThrow2));
                subcategoryStats.setAmount(query.getLong(columnIndexOrThrow3));
                subcategoryStats.setTrans(query.getInt(columnIndexOrThrow4));
                subcategoryStats.setPercent(query.getDouble(columnIndexOrThrow5));
                arrayList.add(subcategoryStats);
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.StatisticDaoObject
    public List<SubcategoryStats> getAllSubcategoryStats(final int accountId, final int categoryId) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT e.*,     CAST(printf('%.1f', ((e.amount*1.0)*100/(SELECT SUM(ROUND(t.amount*cu.rate))*1.0 FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as cu ON w.currency = cu.code WHERE t.account_id = ? AND cu.account_id = ? AND w.account_id = ? AND t.category_id = ?))) AS double) as percent FROM(SELECT sc.name,sc.id,SUM(ROUND(t.amount*cu.rate)) as amount, COUNT(t.id) as trans FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as cu ON w.currency = cu.code LEFT JOIN subcategory as sc ON t.subcategory_id = sc.id WHERE t.account_id = ? AND w.account_id = ? AND cu.account_id = ? AND t.category_id = ? GROUP BY sc.id ) as e ORDER BY percent DESC", 8);
        long j = accountId;
        acquire.bindLong(1, j);
        acquire.bindLong(2, j);
        acquire.bindLong(3, j);
        long j2 = categoryId;
        acquire.bindLong(4, j2);
        acquire.bindLong(5, j);
        acquire.bindLong(6, j);
        acquire.bindLong(7, j);
        acquire.bindLong(8, j2);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "name");
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "id");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "amount");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "trans");
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "percent");
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                SubcategoryStats subcategoryStats = new SubcategoryStats();
                subcategoryStats.setName(query.isNull(columnIndexOrThrow) ? null : query.getString(columnIndexOrThrow));
                subcategoryStats.setId(query.getInt(columnIndexOrThrow2));
                subcategoryStats.setAmount(query.getLong(columnIndexOrThrow3));
                subcategoryStats.setTrans(query.getInt(columnIndexOrThrow4));
                subcategoryStats.setPercent(query.getDouble(columnIndexOrThrow5));
                arrayList.add(subcategoryStats);
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.StatisticDaoObject
    public long getAccountBalance(final int id, final long date) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT SUM(ROUND(w.amount*c.rate)) FROM    (SELECT  w.id, (       SELECT SUM(amount) as amount FROM(           SELECT SUM(amount) as amount FROM trans WHERE wallet_id = w.id AND type != 2 AND date_time < ?           UNION ALL           SELECT SUM(-amount) as amount FROM trans WHERE wallet_id = w.id AND type = 2 AND date_time < ?           UNION ALL           SELECT initial_amount FROM wallet WHERE id = w.id           UNION ALL           SELECT SUM(trans_amount) as amount FROM trans WHERE transfer_wallet_id = w.id AND type = 2 AND date_time < ?       ) as subt1   ) as amount,w.currency,w.account_id,w.active,w.exclude FROM wallet as w) as w LEFT JOIN currency as c ON w.currency = c.code WHERE w.account_id = ? AND w.active = 0 AND w.exclude = 0 AND c.account_id = ?", 5);
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

    @Override // com.ktwapps.walletmanager.Database.Dao.StatisticDaoObject
    public List<DailyTrans> getAllPieTrans(final int accountId, final int categoryId, final int type) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT SUM(amount) as amount, day, month, year FROM (SELECT * FROM (SELECT SUM(ROUND(t.amount*c.rate)) as amount, CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as day,CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as month,CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as year FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as c ON w.currency = c.code WHERE t.account_id = ? AND t.type = ? AND w.account_id = ? AND t.category_id = ? AND c.account_id = ? GROUP BY CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) ORDER BY CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC) as t1 ) as t1 GROUP BY day, month, year ORDER BY year DESC,month DESC,day DESC", 5);
        long j = accountId;
        acquire.bindLong(1, j);
        acquire.bindLong(2, type);
        acquire.bindLong(3, j);
        acquire.bindLong(4, categoryId);
        acquire.bindLong(5, j);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                arrayList.add(new DailyTrans(query.getInt(1), query.getInt(2), query.getInt(3), query.getLong(0)));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.StatisticDaoObject
    public List<DailyTrans> getPieTrans(final int accountId, final int categoryId, final long startDate, final long endDate, final int type) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT SUM(amount) as amount, day, month, year FROM (SELECT * FROM (SELECT SUM(ROUND(t.amount*c.rate)) as amount, CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as day,CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as month,CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as year FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as c ON w.currency = c.code WHERE t.account_id = ? AND t.type = ? AND t.date_time >= ? AND t.date_time < ? AND w.account_id = ? AND t.category_id = ? AND c.account_id = ? GROUP BY CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) ORDER BY CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC) as t1 ) as t1 GROUP BY day, month, year ORDER BY year DESC,month DESC,day DESC", 7);
        long j = accountId;
        acquire.bindLong(1, j);
        acquire.bindLong(2, type);
        acquire.bindLong(3, startDate);
        acquire.bindLong(4, endDate);
        acquire.bindLong(5, j);
        acquire.bindLong(6, categoryId);
        acquire.bindLong(7, j);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                arrayList.add(new DailyTrans(query.getInt(1), query.getInt(2), query.getInt(3), query.getLong(0)));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.StatisticDaoObject
    public List<Trans> getPieTransFromDate(final int accountId, final int categoryId, final long startDate, final long endDate, final int type) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT d.id as debtId, d.color as debtColor,d.type as debtType,dt.id as debtTransId, dt.type as debtTransType,t.id,t.note, t.memo,t.trans_amount as transAmount, t.subcategory_id as subcategoryId, sc.name as subcategory,c.icon,c.color,c.default_category as categoryDefault,t.fee_id as feeId,cu.code as currency,t.amount,t.date_time as dateTime,w.name as wallet, tw.name as transferWallet,t.type , t.wallet_id as walletId, t.transfer_wallet_id as transferWalletId, c.name as category, c.id as categoryId, GROUP_CONCAT(m.path) as media FROM trans t LEFT JOIN category as c ON t.category_id = c.id LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as cu ON w.currency = cu.code LEFT JOIN wallet as tw ON t.transfer_wallet_id = tw.id LEFT JOIN media as m ON m.trans_id = t.id LEFT JOIN debt as d ON t.debt_id = d.id LEFT JOIN debtTrans as dt ON t.debt_trans_id = dt.id LEFT JOIN subcategory as sc ON t.subcategory_id = sc.id WHERE t.date_time >= ? AND t.date_time < ? AND t.account_id = ? AND cu.account_id = ? AND t.category_id = ? AND t.type = ? GROUP BY t.id ORDER BY t.date_time DESC", 6);
        acquire.bindLong(1, startDate);
        acquire.bindLong(2, endDate);
        long j = accountId;
        acquire.bindLong(3, j);
        acquire.bindLong(4, j);
        acquire.bindLong(5, categoryId);
        acquire.bindLong(6, type);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                int i = query.getInt(0);
                String string = query.isNull(1) ? null : query.getString(1);
                int i2 = query.getInt(2);
                int i3 = query.getInt(3);
                int i4 = query.getInt(4);
                String string2 = query.isNull(6) ? null : query.getString(6);
                String string3 = query.isNull(7) ? null : query.getString(7);
                long j2 = query.getLong(8);
                int i5 = query.getInt(9);
                String string4 = query.isNull(10) ? null : query.getString(10);
                int i6 = query.getInt(11);
                String string5 = query.isNull(12) ? null : query.getString(12);
                int i7 = query.getInt(13);
                int i8 = query.getInt(14);
                Trans trans = new Trans(string2, string3, string5, i6, query.isNull(15) ? null : query.getString(15), DateConverter.toDate(query.isNull(17) ? null : Long.valueOf(query.getLong(17))), query.getLong(16), query.isNull(18) ? null : query.getString(18), query.getInt(20), query.isNull(19) ? null : query.getString(19), query.getInt(21), query.getInt(22), query.isNull(23) ? null : query.getString(23), query.getInt(24), i7, i8, query.isNull(25) ? null : query.getString(25), j2, string, i, i2, i3, i4, string4, i5);
                trans.setId(query.getInt(5));
                arrayList.add(trans);
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.StatisticDaoObject
    public List<DailyTrans> getAllOverviewTrans(final int accountId) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT SUM(amount) as amount, day, month, year FROM (SELECT * FROM (SELECT SUM(ROUND(t.amount*c.rate)) as amount, CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as day,CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as month,CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as year FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as c ON w.currency = c.code WHERE t.account_id = ? AND t.type != 2 AND w.account_id = ? AND c.account_id = ? GROUP BY CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) ORDER BY CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC) as t1 UNION ALL SELECT * FROM (SELECT 0 as amount, CAST(strftime('%d', datetime(date_time/1000, 'unixepoch','localtime')) AS int) as day,CAST(strftime('%m', datetime(date_time/1000, 'unixepoch','localtime')) AS int) as month,CAST(strftime('%Y', datetime(date_time/1000, 'unixepoch','localtime')) AS int) as year FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN wallet as tw ON t.transfer_wallet_id = tw.id LEFT JOIN currency as c1 ON w.currency = c1.code LEFT JOIN currency as c2 ON tw.currency = c2.code WHERE t.account_id = ? AND w.account_id = ? AND tw.account_id = ? AND c1.account_id = ? AND c2.account_id = ? AND t.type = 2 GROUP BY CAST(strftime('%d', datetime(date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%m', datetime(date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%Y', datetime(date_time/1000, 'unixepoch','localtime')) AS int) ORDER BY CAST(strftime('%Y', datetime(date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%m', datetime(date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%d', datetime(date_time/1000, 'unixepoch','localtime')) AS int) DESC) as t2 ) as t1 GROUP BY day, month, year ORDER BY year DESC,month DESC,day DESC", 8);
        long j = accountId;
        acquire.bindLong(1, j);
        acquire.bindLong(2, j);
        acquire.bindLong(3, j);
        acquire.bindLong(4, j);
        acquire.bindLong(5, j);
        acquire.bindLong(6, j);
        acquire.bindLong(7, j);
        acquire.bindLong(8, j);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                arrayList.add(new DailyTrans(query.getInt(1), query.getInt(2), query.getInt(3), query.getLong(0)));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.StatisticDaoObject
    public List<DailyTrans> getOverviewTrans(final int accountId, final long startDate, final long endDate) {
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
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                arrayList.add(new DailyTrans(query.getInt(1), query.getInt(2), query.getInt(3), query.getLong(0)));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.StatisticDaoObject
    public List<Trans> getOverviewTransFromDate(final int accountId, final long startDate, final long endDate) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT d.id as debtId, d.color as debtColor,d.type as debtType,dt.id as debtTransId, dt.type as debtTransType,t.id,t.note, t.memo,t.trans_amount as transAmount, t.subcategory_id as subcategoryId, sc.name as subcategory,cu.code as currency,c.icon,c.default_category as categoryDefault,t.fee_id as feeId,c.color,t.amount,t.date_time as dateTime,t.type,w.name as wallet, tw.name as transferWallet, t.wallet_id as walletId, t.transfer_wallet_id as transferWalletId ,c.name as category, c.id as categoryId, GROUP_CONCAT(m.path) as media FROM trans t LEFT JOIN category as c ON t.category_id = c.id LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as cu ON w.currency = cu.code LEFT JOIN media as m ON m.trans_id = t.id LEFT JOIN wallet as tw ON t.transfer_wallet_id = tw.id LEFT JOIN debt as d ON t.debt_id = d.id LEFT JOIN debtTrans as dt ON t.debt_trans_id = dt.id LEFT JOIN subcategory as sc ON t.subcategory_id = sc.id WHERE t.date_time >= ? AND t.date_time < ? AND t.account_id = ? AND cu.account_id = ? GROUP BY t.id ORDER BY t.date_time DESC", 4);
        acquire.bindLong(1, startDate);
        acquire.bindLong(2, endDate);
        long j = accountId;
        acquire.bindLong(3, j);
        acquire.bindLong(4, j);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                int i = query.getInt(0);
                String string = query.isNull(1) ? null : query.getString(1);
                int i2 = query.getInt(2);
                int i3 = query.getInt(3);
                int i4 = query.getInt(4);
                String string2 = query.isNull(6) ? null : query.getString(6);
                String string3 = query.isNull(7) ? null : query.getString(7);
                long j2 = query.getLong(8);
                int i5 = query.getInt(9);
                String string4 = query.isNull(10) ? null : query.getString(10);
                String string5 = query.isNull(11) ? null : query.getString(11);
                int i6 = query.getInt(12);
                int i7 = query.getInt(13);
                int i8 = query.getInt(14);
                Trans trans = new Trans(string2, string3, query.isNull(15) ? null : query.getString(15), i6, string5, DateConverter.toDate(query.isNull(17) ? null : Long.valueOf(query.getLong(17))), query.getLong(16), query.isNull(19) ? null : query.getString(19), query.getInt(18), query.isNull(20) ? null : query.getString(20), query.getInt(21), query.getInt(22), query.isNull(23) ? null : query.getString(23), query.getInt(24), i7, i8, query.isNull(25) ? null : query.getString(25), j2, string, i, i2, i3, i4, string4, i5);
                trans.setId(query.getInt(5));
                arrayList.add(trans);
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.StatisticDaoObject
    public List<DailyTrans> getWeeklyTrans(final int accountId, final long startDate, final long endDate) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT SUM(amount) as amount, day, month, year FROM (SELECT * FROM (SELECT SUM(ROUND(t.amount*c.rate)) as amount, CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as day,CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as month,CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as year FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as c ON w.currency = c.code WHERE t.account_id = ? AND t.date_time >= ? AND t.date_time < ? AND t.type = 1 AND w.account_id = ? AND c.account_id = ? GROUP BY CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) ORDER BY CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC) as t1 ) as t1 GROUP BY day, month, year ORDER BY year DESC,month DESC,day DESC", 5);
        long j = accountId;
        acquire.bindLong(1, j);
        acquire.bindLong(2, startDate);
        acquire.bindLong(3, endDate);
        acquire.bindLong(4, j);
        acquire.bindLong(5, j);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                arrayList.add(new DailyTrans(query.getInt(1), query.getInt(2), query.getInt(3), query.getLong(0)));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.StatisticDaoObject
    public List<Trans> getWeeklyTransFromDate(final int accountId, final long startDate, final long endDate) {
        RoomSQLiteQuery roomSQLiteQuery;
        int columnIndexOrThrow = 0;
        int columnIndexOrThrow2 = 0;
        int columnIndexOrThrow3 = 0;
        int columnIndexOrThrow4 = 0;
        int columnIndexOrThrow5 = 0;
        int columnIndexOrThrow6 = 0;
        int columnIndexOrThrow7 = 0;
        int columnIndexOrThrow8 = 0;
        int columnIndexOrThrow9 = 0;
        int columnIndexOrThrow10 = 0;
        int columnIndexOrThrow11 = 0;
        int columnIndexOrThrow12 = 0;
        int columnIndexOrThrow13 = 0;
        int columnIndexOrThrow14 = 0;
        String string;
        String string2;
        int i;
        String string3;
        int i2;
        String string4;
        int i3;
        String string5;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM (   SELECT d.id as debtId, d.color as debtColor,d.type as debtType,dt.id as debtTransId, dt.type as debtTransType, t.subcategory_id as subcategoryId, sc.name as subcategory,t.id,t.note, t.memo,t.trans_amount as transAmount,c.icon,c.color,c.default_category as categoryDefault,t.fee_id as feeId,cu.code as currency,t.amount,t.date_time as dateTime,w.name as wallet, tw.name as transferWallet,t.type , t.wallet_id as walletId, t.transfer_wallet_id as transferWalletId, c.name as category, c.id as categoryId, GROUP_CONCAT(m.path) as media FROM trans t    LEFT JOIN category as c ON t.category_id = c.id    LEFT JOIN wallet as w ON t.wallet_id = w.id    LEFT JOIN currency as cu ON w.currency = cu.code    LEFT JOIN wallet as tw ON t.transfer_wallet_id = tw.id    LEFT JOIN media as m ON m.trans_id = t.id    LEFT JOIN debt as d ON t.debt_id = d.id    LEFT JOIN debtTrans as dt ON t.debt_trans_id = dt.id    LEFT JOIN subcategory as sc ON t.subcategory_id = sc.id    WHERE t.date_time >= ? AND t.date_time < ? AND t.account_id = ? AND cu.account_id = ? AND w.account_id = ? AND t.type = 1 GROUP BY t.id) as t1 ORDER BY dateTime DESC", 5);
        acquire.bindLong(1, startDate);
        acquire.bindLong(2, endDate);
        long j = accountId;
        acquire.bindLong(3, j);
        acquire.bindLong(4, j);
        acquire.bindLong(5, j);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "debtId");
            columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "debtColor");
            columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "debtType");
            columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "debtTransId");
            columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "debtTransType");
            columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "subcategoryId");
            columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "subcategory");
            columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "id");
            columnIndexOrThrow9 = CursorUtil.getColumnIndexOrThrow(query, "note");
            columnIndexOrThrow10 = CursorUtil.getColumnIndexOrThrow(query, "memo");
            columnIndexOrThrow11 = CursorUtil.getColumnIndexOrThrow(query, "transAmount");
            columnIndexOrThrow12 = CursorUtil.getColumnIndexOrThrow(query, "icon");
            columnIndexOrThrow13 = CursorUtil.getColumnIndexOrThrow(query, TypedValues.Custom.S_COLOR);
            columnIndexOrThrow14 = CursorUtil.getColumnIndexOrThrow(query, "categoryDefault");
            roomSQLiteQuery = acquire;
        } catch (Throwable th) {
            th = th;
            roomSQLiteQuery = acquire;
        }
        try {
            int columnIndexOrThrow15 = CursorUtil.getColumnIndexOrThrow(query, "feeId");
            int i4 = columnIndexOrThrow8;
            int columnIndexOrThrow16 = CursorUtil.getColumnIndexOrThrow(query, FirebaseAnalytics.Param.CURRENCY);
            int columnIndexOrThrow17 = CursorUtil.getColumnIndexOrThrow(query, "amount");
            int columnIndexOrThrow18 = CursorUtil.getColumnIndexOrThrow(query, "dateTime");
            int columnIndexOrThrow19 = CursorUtil.getColumnIndexOrThrow(query, "wallet");
            int columnIndexOrThrow20 = CursorUtil.getColumnIndexOrThrow(query, "transferWallet");
            int columnIndexOrThrow21 = CursorUtil.getColumnIndexOrThrow(query, JamXmlElements.TYPE);
            int columnIndexOrThrow22 = CursorUtil.getColumnIndexOrThrow(query, "walletId");
            int columnIndexOrThrow23 = CursorUtil.getColumnIndexOrThrow(query, "transferWalletId");
            int columnIndexOrThrow24 = CursorUtil.getColumnIndexOrThrow(query, "category");
            int columnIndexOrThrow25 = CursorUtil.getColumnIndexOrThrow(query, "categoryId");
            int columnIndexOrThrow26 = CursorUtil.getColumnIndexOrThrow(query, "media");
            int i5 = columnIndexOrThrow15;
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                int i6 = query.getInt(columnIndexOrThrow);
                String string6 = query.isNull(columnIndexOrThrow2) ? null : query.getString(columnIndexOrThrow2);
                int i7 = query.getInt(columnIndexOrThrow3);
                int i8 = query.getInt(columnIndexOrThrow4);
                int i9 = query.getInt(columnIndexOrThrow5);
                int i10 = query.getInt(columnIndexOrThrow6);
                String string7 = query.isNull(columnIndexOrThrow7) ? null : query.getString(columnIndexOrThrow7);
                String string8 = query.isNull(columnIndexOrThrow9) ? null : query.getString(columnIndexOrThrow9);
                String string9 = query.isNull(columnIndexOrThrow10) ? null : query.getString(columnIndexOrThrow10);
                long j2 = query.getLong(columnIndexOrThrow11);
                int i11 = query.getInt(columnIndexOrThrow12);
                String string10 = query.isNull(columnIndexOrThrow13) ? null : query.getString(columnIndexOrThrow13);
                int i12 = query.getInt(columnIndexOrThrow14);
                int i13 = i5;
                int i14 = query.getInt(i13);
                int i15 = columnIndexOrThrow;
                int i16 = columnIndexOrThrow16;
                if (query.isNull(i16)) {
                    columnIndexOrThrow16 = i16;
                    string = null;
                } else {
                    columnIndexOrThrow16 = i16;
                    string = query.getString(i16);
                }
                int i17 = columnIndexOrThrow17;
                long j3 = query.getLong(i17);
                columnIndexOrThrow17 = i17;
                int i18 = columnIndexOrThrow18;
                Date date = DateConverter.toDate(query.isNull(i18) ? null : Long.valueOf(query.getLong(i18)));
                columnIndexOrThrow18 = i18;
                int i19 = columnIndexOrThrow19;
                if (query.isNull(i19)) {
                    columnIndexOrThrow19 = i19;
                    i = columnIndexOrThrow20;
                    string2 = null;
                } else {
                    string2 = query.getString(i19);
                    columnIndexOrThrow19 = i19;
                    i = columnIndexOrThrow20;
                }
                if (query.isNull(i)) {
                    columnIndexOrThrow20 = i;
                    i2 = columnIndexOrThrow21;
                    string3 = null;
                } else {
                    string3 = query.getString(i);
                    columnIndexOrThrow20 = i;
                    i2 = columnIndexOrThrow21;
                }
                int i20 = query.getInt(i2);
                columnIndexOrThrow21 = i2;
                int i21 = columnIndexOrThrow22;
                int i22 = query.getInt(i21);
                columnIndexOrThrow22 = i21;
                int i23 = columnIndexOrThrow23;
                int i24 = query.getInt(i23);
                columnIndexOrThrow23 = i23;
                int i25 = columnIndexOrThrow24;
                if (query.isNull(i25)) {
                    columnIndexOrThrow24 = i25;
                    i3 = columnIndexOrThrow25;
                    string4 = null;
                } else {
                    string4 = query.getString(i25);
                    columnIndexOrThrow24 = i25;
                    i3 = columnIndexOrThrow25;
                }
                int i26 = query.getInt(i3);
                columnIndexOrThrow25 = i3;
                int i27 = columnIndexOrThrow26;
                if (query.isNull(i27)) {
                    columnIndexOrThrow26 = i27;
                    string5 = null;
                } else {
                    string5 = query.getString(i27);
                    columnIndexOrThrow26 = i27;
                }
                Trans trans = new Trans(string8, string9, string10, i11, string, date, j3, string2, i20, string3, i22, i24, string4, i26, i12, i14, string5, j2, string6, i6, i7, i8, i9, string7, i10);
                int i28 = columnIndexOrThrow14;
                int i29 = i4;
                trans.setId(query.getInt(i29));
                arrayList.add(trans);
                i4 = i29;
                columnIndexOrThrow = i15;
                columnIndexOrThrow14 = i28;
                i5 = i13;
            }
            query.close();
            roomSQLiteQuery.release();
            return arrayList;
        } catch (Throwable th2) {
            Throwable th = th2;
            query.close();
            roomSQLiteQuery.release();
            try {
                throw th;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.StatisticDaoObject
    public List<WeeklyStats> getWeeklyStat(final int accountId, final long startDate, final long endDate) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT SUM(amount) as amount, SUM(trans) as trans, day FROM(   SELECT SUM(ROUND(t.amount*c.rate)) as amount,COUNT(t.id) as trans, CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as day FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as c ON w.currency = c.code WHERE t.account_id = ? AND t.date_time >= ? AND t.date_time < ? AND t.type = 1 AND w.account_id = ? AND c.account_id = ? GROUP BY CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int)) as t1 GROUP BY day", 5);
        long j = accountId;
        acquire.bindLong(1, j);
        acquire.bindLong(2, startDate);
        acquire.bindLong(3, endDate);
        acquire.bindLong(4, j);
        acquire.bindLong(5, j);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                arrayList.add(new WeeklyStats(query.getInt(2), query.getLong(0), query.getInt(1)));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public static List<Class<?>> getRequiredConverters() {
        return Collections.emptyList();
    }
}
