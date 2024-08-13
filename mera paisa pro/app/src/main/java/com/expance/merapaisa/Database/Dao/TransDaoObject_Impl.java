package com.expance.merapaisa.Database.Dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.expance.merapaisa.Database.Converter.DateConverter;
import com.expance.merapaisa.Database.Entity.MediaEntity;
import com.expance.merapaisa.Database.Entity.TransEntity;
import com.expance.merapaisa.Model.DailyTrans;
import com.expance.merapaisa.Model.SearchTrans;
import com.expance.merapaisa.Model.Trans;

import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

/* loaded from: classes3.dex */
public final class TransDaoObject_Impl implements TransDaoObject {
    private final RoomDatabase __db;
    private final EntityInsertionAdapter<MediaEntity> __insertionAdapterOfMediaEntity;
    private final EntityInsertionAdapter<TransEntity> __insertionAdapterOfTransEntity;
    private final SharedSQLiteStatement __preparedStmtOfDeleteMedia;
    private final SharedSQLiteStatement __preparedStmtOfDeleteTrans;
    private final SharedSQLiteStatement __preparedStmtOfDeleteTransMedia;
    private final SharedSQLiteStatement __preparedStmtOfRemoveSubcategory;
    private final EntityDeletionOrUpdateAdapter<TransEntity> __updateAdapterOfTransEntity;

    public TransDaoObject_Impl(RoomDatabase __db) {
        this.__db = __db;
        this.__insertionAdapterOfTransEntity = new EntityInsertionAdapter<TransEntity>(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.TransDaoObject_Impl.1
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "INSERT OR ABORT INTO `trans` (`id`,`note`,`memo`,`type`,`amount`,`date_time`,`account_id`,`fee_id`,`category_id`,`subcategory_id`,`wallet_id`,`transfer_wallet_id`,`trans_amount`,`debt_id`,`debt_trans_id`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            }

            @Override // androidx.room.EntityInsertionAdapter
            public void bind(SupportSQLiteStatement stmt, TransEntity value) {
                stmt.bindLong(1, value.getId());
                if (value.getNote() == null) {
                    stmt.bindNull(2);
                } else {
                    stmt.bindString(2, value.getNote());
                }
                if (value.getMemo() == null) {
                    stmt.bindNull(3);
                } else {
                    stmt.bindString(3, value.getMemo());
                }
                stmt.bindLong(4, value.getType());
                stmt.bindLong(5, value.getAmount());
                Long fromDate = DateConverter.fromDate(value.getDateTime());
                if (fromDate == null) {
                    stmt.bindNull(6);
                } else {
                    stmt.bindLong(6, fromDate.longValue());
                }
                stmt.bindLong(7, value.getAccountId());
                stmt.bindLong(8, value.getFeeId());
                stmt.bindLong(9, value.getCategoryId());
                stmt.bindLong(10, value.getSubcategoryId());
                stmt.bindLong(11, value.getWalletId());
                stmt.bindLong(12, value.getTransferWalletId());
                stmt.bindLong(13, value.getTransAmount());
                stmt.bindLong(14, value.getDebtId());
                stmt.bindLong(15, value.getDebtTransId());
            }
        };
        this.__insertionAdapterOfMediaEntity = new EntityInsertionAdapter<MediaEntity>(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.TransDaoObject_Impl.2
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "INSERT OR ABORT INTO `media` (`id`,`path`,`trans_id`) VALUES (nullif(?, 0),?,?)";
            }

            @Override // androidx.room.EntityInsertionAdapter
            public void bind(SupportSQLiteStatement stmt, MediaEntity value) {
                stmt.bindLong(1, value.getId());
                if (value.getPath() == null) {
                    stmt.bindNull(2);
                } else {
                    stmt.bindString(2, value.getPath());
                }
                stmt.bindLong(3, value.getTransId());
            }
        };
        this.__updateAdapterOfTransEntity = new EntityDeletionOrUpdateAdapter<TransEntity>(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.TransDaoObject_Impl.3
            @Override
            // androidx.room.EntityDeletionOrUpdateAdapter, androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "UPDATE OR ABORT `trans` SET `id` = ?,`note` = ?,`memo` = ?,`type` = ?,`amount` = ?,`date_time` = ?,`account_id` = ?,`fee_id` = ?,`category_id` = ?,`subcategory_id` = ?,`wallet_id` = ?,`transfer_wallet_id` = ?,`trans_amount` = ?,`debt_id` = ?,`debt_trans_id` = ? WHERE `id` = ?";
            }

            @Override // androidx.room.EntityDeletionOrUpdateAdapter
            public void bind(SupportSQLiteStatement stmt, TransEntity value) {
                stmt.bindLong(1, value.getId());
                if (value.getNote() == null) {
                    stmt.bindNull(2);
                } else {
                    stmt.bindString(2, value.getNote());
                }
                if (value.getMemo() == null) {
                    stmt.bindNull(3);
                } else {
                    stmt.bindString(3, value.getMemo());
                }
                stmt.bindLong(4, value.getType());
                stmt.bindLong(5, value.getAmount());
                Long fromDate = DateConverter.fromDate(value.getDateTime());
                if (fromDate == null) {
                    stmt.bindNull(6);
                } else {
                    stmt.bindLong(6, fromDate.longValue());
                }
                stmt.bindLong(7, value.getAccountId());
                stmt.bindLong(8, value.getFeeId());
                stmt.bindLong(9, value.getCategoryId());
                stmt.bindLong(10, value.getSubcategoryId());
                stmt.bindLong(11, value.getWalletId());
                stmt.bindLong(12, value.getTransferWalletId());
                stmt.bindLong(13, value.getTransAmount());
                stmt.bindLong(14, value.getDebtId());
                stmt.bindLong(15, value.getDebtTransId());
                stmt.bindLong(16, value.getId());
            }
        };
        this.__preparedStmtOfDeleteTrans = new SharedSQLiteStatement(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.TransDaoObject_Impl.4
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "DELETE FROM trans WHERE id = ?";
            }
        };
        this.__preparedStmtOfDeleteTransMedia = new SharedSQLiteStatement(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.TransDaoObject_Impl.5
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "DELETE FROM media WHERE trans_id = ?";
            }
        };
        this.__preparedStmtOfDeleteMedia = new SharedSQLiteStatement(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.TransDaoObject_Impl.6
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "DELETE FROM media WHERE path = ?";
            }
        };
        this.__preparedStmtOfRemoveSubcategory = new SharedSQLiteStatement(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.TransDaoObject_Impl.7
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "UPDATE trans SET subcategory_id = 0 WHERE subcategory_id = ?";
            }
        };
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.TransDaoObject
    public long insertTrans(final TransEntity transEntity) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            long insertAndReturnId = this.__insertionAdapterOfTransEntity.insertAndReturnId(transEntity);
            this.__db.setTransactionSuccessful();
            return insertAndReturnId;
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.TransDaoObject
    public void insertMedia(final MediaEntity mediaEntity) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfMediaEntity.insert(mediaEntity);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.TransDaoObject
    public void updateTrans(final TransEntity transEntity) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfTransEntity.handle(transEntity);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.TransDaoObject
    public void deleteTrans(final int id) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfDeleteTrans.acquire();
        acquire.bindLong(1, id);
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfDeleteTrans.release(acquire);
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.TransDaoObject
    public void deleteTransMedia(final int id) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfDeleteTransMedia.acquire();
        acquire.bindLong(1, id);
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfDeleteTransMedia.release(acquire);
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.TransDaoObject
    public void deleteMedia(final String path) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfDeleteMedia.acquire();
        if (path == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, path);
        }
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfDeleteMedia.release(acquire);
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.TransDaoObject
    public void removeSubcategory(final int id) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfRemoveSubcategory.acquire();
        acquire.bindLong(1, id);
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfRemoveSubcategory.release(acquire);
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.TransDaoObject
    public LiveData<List<DailyTrans>> getDailyTrans(final int accountId) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT SUM(amount) as amount, day, month, year FROM (SELECT * FROM (SELECT SUM(ROUND(t.amount*c.rate)) as amount, CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as day,CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as month,CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as year FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as c ON w.currency = c.code WHERE t.account_id = ? AND t.type != 2 AND w.account_id = ? AND c.account_id = ? GROUP BY CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) ORDER BY CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC) as t1 UNION ALL SELECT * FROM (SELECT 0 as amount, CAST(strftime('%d', datetime(date_time/1000, 'unixepoch','localtime')) AS int) as day,CAST(strftime('%m', datetime(date_time/1000, 'unixepoch','localtime')) AS int) as month,CAST(strftime('%Y', datetime(date_time/1000, 'unixepoch','localtime')) AS int) as year FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN wallet as tw ON t.transfer_wallet_id = tw.id LEFT JOIN currency as c1 ON w.currency = c1.code LEFT JOIN currency as c2 ON tw.currency = c2.code WHERE t.account_id = ? AND w.account_id = ? AND tw.account_id = ? AND c1.account_id = ? AND c2.account_id = ? AND t.type = 2 GROUP BY CAST(strftime('%d', datetime(date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%m', datetime(date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%Y', datetime(date_time/1000, 'unixepoch','localtime')) AS int) ORDER BY CAST(strftime('%Y', datetime(date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%m', datetime(date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%d', datetime(date_time/1000, 'unixepoch','localtime')) AS int) DESC) as t2 ) as t1 GROUP BY day, month, year ORDER BY year DESC,month DESC,day DESC", 8);
        long j = accountId;
        acquire.bindLong(1, j);
        acquire.bindLong(2, j);
        acquire.bindLong(3, j);
        acquire.bindLong(4, j);
        acquire.bindLong(5, j);
        acquire.bindLong(6, j);
        acquire.bindLong(7, j);
        acquire.bindLong(8, j);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"trans", "wallet", FirebaseAnalytics.Param.CURRENCY}, false, new Callable<List<DailyTrans>>() { // from class: com.ktwapps.walletmanager.Database.Dao.TransDaoObject_Impl.8
            @Override // java.util.concurrent.Callable
            public List<DailyTrans> call() throws Exception {
                Cursor query = DBUtil.query(TransDaoObject_Impl.this.__db, acquire, false, null);
                try {
                    ArrayList arrayList = new ArrayList(query.getCount());
                    while (query.moveToNext()) {
                        arrayList.add(new DailyTrans(query.getInt(1), query.getInt(2), query.getInt(3), query.getLong(0)));
                    }
                    return arrayList;
                } finally {
                    query.close();
                }
            }

            protected void finalize() {
                acquire.release();
            }
        });
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.TransDaoObject
    public Trans[] getTransFromDate(final int accountId, final long startDate, final long endDate) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT d.id as debtId, d.color as debtColor,d.type as debtType,dt.id as debtTransId, dt.type as debtTransType, t.id,t.note, t.memo,t.trans_amount as transAmount, t.subcategory_id as subcategoryId, sc.name as subcategory,cu.code as currency,c.icon,c.default_category as categoryDefault,t.fee_id as feeId,c.color,t.amount,t.date_time as dateTime,t.type,w.name as wallet, tw.name as transferWallet, t.wallet_id as walletId, t.transfer_wallet_id as transferWalletId ,c.name as category, c.id as categoryId, GROUP_CONCAT(m.path) as media FROM trans t LEFT JOIN category as c ON t.category_id = c.id LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as cu ON w.currency = cu.code LEFT JOIN media as m ON m.trans_id = t.id LEFT JOIN wallet as tw ON t.transfer_wallet_id = tw.id LEFT JOIN debt as d ON t.debt_id = d.id LEFT JOIN debtTrans as dt ON t.debt_trans_id = dt.id LEFT JOIN subcategory as sc ON t.subcategory_id = sc.id WHERE t.date_time >= ? AND t.date_time < ? AND t.account_id = ? AND cu.account_id = ? GROUP BY t.id ORDER BY t.date_time DESC", 4);
        acquire.bindLong(1, startDate);
        acquire.bindLong(2, endDate);
        long j = accountId;
        acquire.bindLong(3, j);
        acquire.bindLong(4, j);
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
                String string5 = query.isNull(11) ? null : query.getString(11);
                int i7 = query.getInt(12);
                int i8 = query.getInt(13);
                int i9 = query.getInt(14);
                Trans trans = new Trans(string2, string3, query.isNull(15) ? null : query.getString(15), i7, string5, DateConverter.toDate(query.isNull(17) ? null : Long.valueOf(query.getLong(17))), query.getLong(16), query.isNull(19) ? null : query.getString(19), query.getInt(18), query.isNull(20) ? null : query.getString(20), query.getInt(21), query.getInt(22), query.isNull(23) ? null : query.getString(23), query.getInt(24), i8, i9, query.isNull(25) ? null : query.getString(25), j2, string, i2, i3, i4, i5, string4, i6);
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

    @Override // com.ktwapps.walletmanager.Database.Dao.TransDaoObject
    public LiveData<Trans> getLiveTransById(final int id, final int accountId) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT d.id as debtId, d.color as debtColor,d.type as debtType,dt.id as debtTransId, dt.type as debtTransType,t.id,t.note, t.memo,t.trans_amount as transAmount, t.subcategory_id as subcategoryId, sc.name as subcategory,cu.code as currency,c.icon,c.default_category as categoryDefault,t.fee_id as feeId,c.color,t.amount,t.date_time as dateTime,t.type,w.name as wallet, tw.name as transferWallet, t.wallet_id as walletId, t.transfer_wallet_id as transferWalletId ,c.name as category, c.id as categoryId, GROUP_CONCAT(m.path) as media FROM trans t LEFT JOIN category as c ON t.category_id = c.id LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as cu ON w.currency = cu.code LEFT JOIN wallet as tw ON t.transfer_wallet_id = tw.id LEFT JOIN media as m ON m.trans_id = t.id LEFT JOIN debt as d ON t.debt_id = d.id LEFT JOIN debtTrans as dt ON t.debt_trans_id = dt.id LEFT JOIN subcategory as sc ON t.subcategory_id = sc.id WHERE t.id = ? AND cu.account_id = ? GROUP BY t.id", 2);
        acquire.bindLong(1, id);
        acquire.bindLong(2, accountId);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"trans", "category", "wallet", FirebaseAnalytics.Param.CURRENCY, "media", "debt", "debtTrans", "subcategory"}, false, new Callable<Trans>() { // from class: com.ktwapps.walletmanager.Database.Dao.TransDaoObject_Impl.9
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Trans call() throws Exception {
                Trans trans = null;
                Cursor query = DBUtil.query(TransDaoObject_Impl.this.__db, acquire, false, null);
                try {
                    if (query.moveToFirst()) {
                        int i = query.getInt(0);
                        String string = query.isNull(1) ? null : query.getString(1);
                        int i2 = query.getInt(2);
                        int i3 = query.getInt(3);
                        int i4 = query.getInt(4);
                        String string2 = query.isNull(6) ? null : query.getString(6);
                        String string3 = query.isNull(7) ? null : query.getString(7);
                        long j = query.getLong(8);
                        int i5 = query.getInt(9);
                        String string4 = query.isNull(10) ? null : query.getString(10);
                        String string5 = query.isNull(11) ? null : query.getString(11);
                        int i6 = query.getInt(12);
                        int i7 = query.getInt(13);
                        int i8 = query.getInt(14);
                        String string6 = query.isNull(15) ? null : query.getString(15);
                        long j2 = query.getLong(16);
                        trans = new Trans(string2, string3, string6, i6, string5, DateConverter.toDate(query.isNull(17) ? null : Long.valueOf(query.getLong(17))), j2, query.isNull(19) ? null : query.getString(19), query.getInt(18), query.isNull(20) ? null : query.getString(20), query.getInt(21), query.getInt(22), query.isNull(23) ? null : query.getString(23), query.getInt(24), i7, i8, query.isNull(25) ? null : query.getString(25), j, string, i, i2, i3, i4, string4, i5);
                        trans.setId(query.getInt(5));
                    }
                    return trans;
                } finally {
                    query.close();
                }
            }

            protected void finalize() {
                acquire.release();
            }
        });
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.TransDaoObject
    public Trans getTransById(final int id, final int accountId) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT d.id as debtId, d.color as debtColor,d.type as debtType,dt.id as debtTransId, dt.type as debtTransType,t.id,t.note, t.memo,t.trans_amount as transAmount, t.subcategory_id as subcategoryId, sc.name as subcategory,cu.code as currency,c.icon,c.default_category as categoryDefault,t.fee_id as feeId,c.color,t.amount,t.date_time as dateTime,t.type,w.name as wallet, tw.name as transferWallet, t.wallet_id as walletId, t.transfer_wallet_id as transferWalletId ,c.name as category, c.id as categoryId, GROUP_CONCAT(m.path) as media FROM trans t LEFT JOIN category as c ON t.category_id = c.id LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as cu ON w.currency = cu.code LEFT JOIN wallet as tw ON t.transfer_wallet_id = tw.id LEFT JOIN media as m ON m.trans_id = t.id LEFT JOIN debt as d ON t.debt_id = d.id LEFT JOIN debtTrans as dt ON t.debt_trans_id = dt.id LEFT JOIN subcategory as sc ON t.subcategory_id = sc.id WHERE t.id = ? AND cu.account_id = ? GROUP BY t.id", 2);
        acquire.bindLong(1, id);
        acquire.bindLong(2, accountId);
        this.__db.assertNotSuspendingTransaction();
        Trans trans = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            if (query.moveToFirst()) {
                int i = query.getInt(0);
                String string = query.isNull(1) ? null : query.getString(1);
                int i2 = query.getInt(2);
                int i3 = query.getInt(3);
                int i4 = query.getInt(4);
                String string2 = query.isNull(6) ? null : query.getString(6);
                String string3 = query.isNull(7) ? null : query.getString(7);
                long j = query.getLong(8);
                int i5 = query.getInt(9);
                String string4 = query.isNull(10) ? null : query.getString(10);
                String string5 = query.isNull(11) ? null : query.getString(11);
                int i6 = query.getInt(12);
                int i7 = query.getInt(13);
                int i8 = query.getInt(14);
                String string6 = query.isNull(15) ? null : query.getString(15);
                long j2 = query.getLong(16);
                trans = new Trans(string2, string3, string6, i6, string5, DateConverter.toDate(query.isNull(17) ? null : Long.valueOf(query.getLong(17))), j2, query.isNull(19) ? null : query.getString(19), query.getInt(18), query.isNull(20) ? null : query.getString(20), query.getInt(21), query.getInt(22), query.isNull(23) ? null : query.getString(23), query.getInt(24), i7, i8, query.isNull(25) ? null : query.getString(25), j, string, i, i2, i3, i4, string4, i5);
                trans.setId(query.getInt(5));
            }
            return trans;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.TransDaoObject
    public TransEntity getTransEntityById(final int id) {
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
        TransEntity transEntity;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM trans WHERE id = ?", 1);
        acquire.bindLong(1, id);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
            columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "note");
            columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "memo");
            columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, JamXmlElements.TYPE);
            columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "amount");
            columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "date_time");
            columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "account_id");
            columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "fee_id");
            columnIndexOrThrow9 = CursorUtil.getColumnIndexOrThrow(query, "category_id");
            columnIndexOrThrow10 = CursorUtil.getColumnIndexOrThrow(query, "subcategory_id");
            columnIndexOrThrow11 = CursorUtil.getColumnIndexOrThrow(query, "wallet_id");
            columnIndexOrThrow12 = CursorUtil.getColumnIndexOrThrow(query, "transfer_wallet_id");
            columnIndexOrThrow13 = CursorUtil.getColumnIndexOrThrow(query, "trans_amount");
            columnIndexOrThrow14 = CursorUtil.getColumnIndexOrThrow(query, "debt_id");
            roomSQLiteQuery = acquire;
        } catch (Throwable th) {
            th = th;
            roomSQLiteQuery = acquire;
        }
        try {
            int columnIndexOrThrow15 = CursorUtil.getColumnIndexOrThrow(query, "debt_trans_id");
            if (query.moveToFirst()) {
                String string = query.isNull(columnIndexOrThrow2) ? null : query.getString(columnIndexOrThrow2);
                String string2 = query.isNull(columnIndexOrThrow3) ? null : query.getString(columnIndexOrThrow3);
                int i = query.getInt(columnIndexOrThrow4);
                long j = query.getLong(columnIndexOrThrow5);
                transEntity = new TransEntity(string, string2, j, DateConverter.toDate(query.isNull(columnIndexOrThrow6) ? null : Long.valueOf(query.getLong(columnIndexOrThrow6))), i, query.getInt(columnIndexOrThrow7), query.getInt(columnIndexOrThrow9), query.getInt(columnIndexOrThrow8), query.getInt(columnIndexOrThrow11), query.getInt(columnIndexOrThrow12), query.getLong(columnIndexOrThrow13), query.getInt(columnIndexOrThrow14), query.getInt(columnIndexOrThrow15), query.getInt(columnIndexOrThrow10));
                transEntity.setId(query.getInt(columnIndexOrThrow));
            } else {
                transEntity = null;
            }
            query.close();
            roomSQLiteQuery.release();
            return transEntity;
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

    @Override // com.ktwapps.walletmanager.Database.Dao.TransDaoObject
    public int getTransferTrans(final int id) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT id FROM trans WHERE fee_id = ? LIMIT 1", 1);
        acquire.bindLong(1, id);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            return query.moveToFirst() ? query.getInt(0) : 0;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.TransDaoObject
    public List<SearchTrans> searchAllTrans(final int accountId, final long startDate, final long endDate) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT d.id as debtId, d.color as debtColor,d.type as debtType,dt.id as debtTransId, dt.type as debtTransType,t.id,t.note, t.memo,t.trans_amount as transAmount, t.subcategory_id as subcategoryId, sc.name as subcategory,cu.code as currency,cu.rate as rate,c.icon,c.default_category as categoryDefault,t.fee_id as feeId,c.color,t.amount,t.date_time as dateTime,t.type,w.name as wallet, tw.name as transferWallet, t.wallet_id as walletId, t.transfer_wallet_id as transferWalletId ,c.name as category, c.id as categoryId, GROUP_CONCAT(m.path) as media FROM trans t LEFT JOIN category as c ON t.category_id = c.id LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as cu ON w.currency = cu.code LEFT JOIN wallet as tw ON t.transfer_wallet_id = tw.id LEFT JOIN media as m ON m.trans_id = t.id LEFT JOIN debt as d ON t.debt_id = d.id LEFT JOIN debtTrans as dt ON t.debt_trans_id = dt.id LEFT JOIN subcategory as sc ON t.subcategory_id = sc.id WHERE t.account_id = ? AND cu.account_id = ? AND t.date_time >= ? AND t.date_time < ? GROUP BY t.id ORDER BY t.date_time DESC", 4);
        long j = accountId;
        acquire.bindLong(1, j);
        acquire.bindLong(2, j);
        acquire.bindLong(3, startDate);
        acquire.bindLong(4, endDate);
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
                float f = query.getFloat(12);
                int i6 = query.getInt(13);
                int i7 = query.getInt(14);
                int i8 = query.getInt(15);
                SearchTrans searchTrans = new SearchTrans(f, string2, string3, query.isNull(16) ? null : query.getString(16), i6, string5, DateConverter.toDate(query.isNull(18) ? null : Long.valueOf(query.getLong(18))), query.getLong(17), query.isNull(20) ? null : query.getString(20), query.getInt(19), query.isNull(21) ? null : query.getString(21), query.getInt(22), query.getInt(23), query.isNull(24) ? null : query.getString(24), query.getInt(25), i7, i8, query.isNull(26) ? null : query.getString(26), j2, string, i, i2, i3, i4, i5, string4);
                searchTrans.setId(query.getInt(5));
                arrayList.add(searchTrans);
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.TransDaoObject
    public List<SearchTrans> searchAllTrans(final int accountId) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT d.id as debtId, d.color as debtColor,d.type as debtType,dt.id as debtTransId, dt.type as debtTransType,t.id,t.note, t.memo,t.trans_amount as transAmount, t.subcategory_id as subcategoryId, sc.name as subcategory,cu.code as currency,cu.rate as rate,c.icon,c.default_category as categoryDefault,t.fee_id as feeId,c.color,t.amount,t.date_time as dateTime,t.type,w.name as wallet, tw.name as transferWallet, t.wallet_id as walletId, t.transfer_wallet_id as transferWalletId ,c.name as category, c.id as categoryId, GROUP_CONCAT(m.path) as media FROM trans t LEFT JOIN category as c ON t.category_id = c.id LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as cu ON w.currency = cu.code LEFT JOIN wallet as tw ON t.transfer_wallet_id = tw.id LEFT JOIN media as m ON m.trans_id = t.id LEFT JOIN debt as d ON t.debt_id = d.id LEFT JOIN debtTrans as dt ON t.debt_trans_id = dt.id LEFT JOIN subcategory as sc ON t.subcategory_id = sc.id WHERE t.account_id = ? AND cu.account_id = ? GROUP BY t.id ORDER BY t.date_time DESC", 2);
        long j = accountId;
        acquire.bindLong(1, j);
        acquire.bindLong(2, j);
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
                float f = query.getFloat(12);
                int i6 = query.getInt(13);
                int i7 = query.getInt(14);
                int i8 = query.getInt(15);
                SearchTrans searchTrans = new SearchTrans(f, string2, string3, query.isNull(16) ? null : query.getString(16), i6, string5, DateConverter.toDate(query.isNull(18) ? null : Long.valueOf(query.getLong(18))), query.getLong(17), query.isNull(20) ? null : query.getString(20), query.getInt(19), query.isNull(21) ? null : query.getString(21), query.getInt(22), query.getInt(23), query.isNull(24) ? null : query.getString(24), query.getInt(25), i7, i8, query.isNull(26) ? null : query.getString(26), j2, string, i, i2, i3, i4, i5, string4);
                searchTrans.setId(query.getInt(5));
                arrayList.add(searchTrans);
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.TransDaoObject
    public List<SearchTrans> searchAllTrans(final String note, final int accountId) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT d.id as debtId, d.color as debtColor,d.type as debtType,dt.id as debtTransId, dt.type as debtTransType,t.id,t.note, t.memo,t.trans_amount as transAmount, t.subcategory_id as subcategoryId, sc.name as subcategory,cu.code as currency,cu.rate as rate,c.icon,c.default_category as categoryDefault,t.fee_id as feeId,c.color,t.amount,t.date_time as dateTime,t.type,w.name as wallet, tw.name as transferWallet, t.wallet_id as walletId, t.transfer_wallet_id as transferWalletId ,c.name as category, c.id as categoryId, GROUP_CONCAT(m.path) as media FROM trans t LEFT JOIN category as c ON t.category_id = c.id LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as cu ON w.currency = cu.code LEFT JOIN wallet as tw ON t.transfer_wallet_id = tw.id LEFT JOIN media as m ON m.trans_id = t.id LEFT JOIN debt as d ON t.debt_id = d.id LEFT JOIN debtTrans as dt ON t.debt_trans_id = dt.id LEFT JOIN subcategory as sc ON t.subcategory_id = sc.id WHERE t.note LIKE ? AND t.account_id = ? AND cu.account_id = ? GROUP BY t.id ORDER BY t.date_time DESC", 3);
        if (note == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, note);
        }
        long j = accountId;
        acquire.bindLong(2, j);
        acquire.bindLong(3, j);
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
                float f = query.getFloat(12);
                int i6 = query.getInt(13);
                int i7 = query.getInt(14);
                int i8 = query.getInt(15);
                SearchTrans searchTrans = new SearchTrans(f, string2, string3, query.isNull(16) ? null : query.getString(16), i6, string5, DateConverter.toDate(query.isNull(18) ? null : Long.valueOf(query.getLong(18))), query.getLong(17), query.isNull(20) ? null : query.getString(20), query.getInt(19), query.isNull(21) ? null : query.getString(21), query.getInt(22), query.getInt(23), query.isNull(24) ? null : query.getString(24), query.getInt(25), i7, i8, query.isNull(26) ? null : query.getString(26), j2, string, i, i2, i3, i4, i5, string4);
                searchTrans.setId(query.getInt(5));
                arrayList.add(searchTrans);
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.TransDaoObject
    public List<SearchTrans> searchAllTrans(final String note, final int accountId, final long startDate, final long endDate) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT d.id as debtId, d.color as debtColor,d.type as debtType,dt.id as debtTransId, dt.type as debtTransType,t.id,t.note, t.memo,t.trans_amount as transAmount, t.subcategory_id as subcategoryId, sc.name as subcategory,cu.code as currency,cu.rate as rate,c.icon,c.default_category as categoryDefault,t.fee_id as feeId,c.color,t.amount,t.date_time as dateTime,t.type,w.name as wallet, tw.name as transferWallet, t.wallet_id as walletId, t.transfer_wallet_id as transferWalletId ,c.name as category, c.id as categoryId, GROUP_CONCAT(m.path) as media FROM trans t LEFT JOIN category as c ON t.category_id = c.id LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as cu ON w.currency = cu.code LEFT JOIN wallet as tw ON t.transfer_wallet_id = tw.id LEFT JOIN media as m ON m.trans_id = t.id LEFT JOIN debt as d ON t.debt_id = d.id LEFT JOIN debtTrans as dt ON t.debt_trans_id = dt.id LEFT JOIN subcategory as sc ON t.subcategory_id = sc.id WHERE t.note LIKE ? AND t.account_id = ? AND cu.account_id = ? AND t.date_time >= ? AND t.date_time < ? GROUP BY t.id ORDER BY t.date_time DESC", 5);
        if (note == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, note);
        }
        long j = accountId;
        acquire.bindLong(2, j);
        acquire.bindLong(3, j);
        acquire.bindLong(4, startDate);
        acquire.bindLong(5, endDate);
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
                float f = query.getFloat(12);
                int i6 = query.getInt(13);
                int i7 = query.getInt(14);
                int i8 = query.getInt(15);
                SearchTrans searchTrans = new SearchTrans(f, string2, string3, query.isNull(16) ? null : query.getString(16), i6, string5, DateConverter.toDate(query.isNull(18) ? null : Long.valueOf(query.getLong(18))), query.getLong(17), query.isNull(20) ? null : query.getString(20), query.getInt(19), query.isNull(21) ? null : query.getString(21), query.getInt(22), query.getInt(23), query.isNull(24) ? null : query.getString(24), query.getInt(25), i7, i8, query.isNull(26) ? null : query.getString(26), j2, string, i, i2, i3, i4, i5, string4);
                searchTrans.setId(query.getInt(5));
                arrayList.add(searchTrans);
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
