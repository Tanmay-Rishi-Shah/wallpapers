package com.expance.merapaisa.Database.Dao;

import android.database.Cursor;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;

import com.google.android.gms.measurement.api.AppMeasurementSdk;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.expance.merapaisa.Database.Converter.DateConverter;
import com.expance.merapaisa.Database.Entity.WalletEntity;
import com.expance.merapaisa.Model.CalendarSummary;
import com.expance.merapaisa.Model.DailyTrans;
import com.expance.merapaisa.Model.Stats;
import com.expance.merapaisa.Model.Trans;
import com.expance.merapaisa.Model.WalletDetail;
import com.expance.merapaisa.Model.WalletTrans;
import com.expance.merapaisa.Model.Wallets;

import org.apache.poi.ss.util.CellUtil;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

/* loaded from: classes3.dex */
public final class WalletDaoObject_Impl implements WalletDaoObject {
    private final RoomDatabase __db;
    private final EntityInsertionAdapter<WalletEntity> __insertionAdapterOfWalletEntity;
    private final SharedSQLiteStatement __preparedStmtOfArchiveWallet;
    private final SharedSQLiteStatement __preparedStmtOfDeleteWallet;
    private final SharedSQLiteStatement __preparedStmtOfUnArchiveWallet;
    private final SharedSQLiteStatement __preparedStmtOfUpdateWalletOrdering;
    private final EntityDeletionOrUpdateAdapter<WalletEntity> __updateAdapterOfWalletEntity;

    public WalletDaoObject_Impl(RoomDatabase __db) {
        this.__db = __db;
        this.__insertionAdapterOfWalletEntity = new EntityInsertionAdapter<WalletEntity>(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.WalletDaoObject_Impl.1
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "INSERT OR ABORT INTO `wallet` (`id`,`name`,`color`,`amount`,`initial_amount`,`active`,`account_id`,`ordering`,`exclude`,`currency`,`icon`,`type`,`due_date`,`statement_date`,`credit_limit`,`hidden`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            }

            @Override // androidx.room.EntityInsertionAdapter
            public void bind(SupportSQLiteStatement stmt, WalletEntity value) {
                stmt.bindLong(1, value.getId());
                if (value.getName() == null) {
                    stmt.bindNull(2);
                } else {
                    stmt.bindString(2, value.getName());
                }
                if (value.getColor() == null) {
                    stmt.bindNull(3);
                } else {
                    stmt.bindString(3, value.getColor());
                }
                stmt.bindLong(4, value.getAmount());
                stmt.bindLong(5, value.getInitialAmount());
                stmt.bindLong(6, value.getActive());
                stmt.bindLong(7, value.getAccountId());
                stmt.bindLong(8, value.getOrdering());
                stmt.bindLong(9, value.getExclude());
                if (value.getCurrency() == null) {
                    stmt.bindNull(10);
                } else {
                    stmt.bindString(10, value.getCurrency());
                }
                stmt.bindLong(11, value.getIcon());
                stmt.bindLong(12, value.getType());
                stmt.bindLong(13, value.getDueDate());
                stmt.bindLong(14, value.getStatementDate());
                stmt.bindLong(15, value.getCreditLimit());
                stmt.bindLong(16, value.getHidden());
            }
        };
        this.__updateAdapterOfWalletEntity = new EntityDeletionOrUpdateAdapter<WalletEntity>(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.WalletDaoObject_Impl.2
            @Override
            // androidx.room.EntityDeletionOrUpdateAdapter, androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "UPDATE OR ABORT `wallet` SET `id` = ?,`name` = ?,`color` = ?,`amount` = ?,`initial_amount` = ?,`active` = ?,`account_id` = ?,`ordering` = ?,`exclude` = ?,`currency` = ?,`icon` = ?,`type` = ?,`due_date` = ?,`statement_date` = ?,`credit_limit` = ?,`hidden` = ? WHERE `id` = ?";
            }

            @Override // androidx.room.EntityDeletionOrUpdateAdapter
            public void bind(SupportSQLiteStatement stmt, WalletEntity value) {
                stmt.bindLong(1, value.getId());
                if (value.getName() == null) {
                    stmt.bindNull(2);
                } else {
                    stmt.bindString(2, value.getName());
                }
                if (value.getColor() == null) {
                    stmt.bindNull(3);
                } else {
                    stmt.bindString(3, value.getColor());
                }
                stmt.bindLong(4, value.getAmount());
                stmt.bindLong(5, value.getInitialAmount());
                stmt.bindLong(6, value.getActive());
                stmt.bindLong(7, value.getAccountId());
                stmt.bindLong(8, value.getOrdering());
                stmt.bindLong(9, value.getExclude());
                if (value.getCurrency() == null) {
                    stmt.bindNull(10);
                } else {
                    stmt.bindString(10, value.getCurrency());
                }
                stmt.bindLong(11, value.getIcon());
                stmt.bindLong(12, value.getType());
                stmt.bindLong(13, value.getDueDate());
                stmt.bindLong(14, value.getStatementDate());
                stmt.bindLong(15, value.getCreditLimit());
                stmt.bindLong(16, value.getHidden());
                stmt.bindLong(17, value.getId());
            }
        };
        this.__preparedStmtOfDeleteWallet = new SharedSQLiteStatement(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.WalletDaoObject_Impl.3
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "UPDATE wallet SET active = ? WHERE id = ?";
            }
        };
        this.__preparedStmtOfArchiveWallet = new SharedSQLiteStatement(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.WalletDaoObject_Impl.4
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "UPDATE wallet SET hidden = 1 WHERE id = ?";
            }
        };
        this.__preparedStmtOfUnArchiveWallet = new SharedSQLiteStatement(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.WalletDaoObject_Impl.5
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "UPDATE wallet SET hidden = 0 WHERE id = ?";
            }
        };
        this.__preparedStmtOfUpdateWalletOrdering = new SharedSQLiteStatement(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.WalletDaoObject_Impl.6
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "UPDATE wallet SET ordering = ? WHERE id = ?";
            }
        };
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public void insertWallet(final WalletEntity walletEntity) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfWalletEntity.insert(walletEntity);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public void updateWallet(final WalletEntity walletEntity) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfWalletEntity.handle(walletEntity);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public void deleteWallet(final int id, final int active) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfDeleteWallet.acquire();
        acquire.bindLong(1, active);
        acquire.bindLong(2, id);
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfDeleteWallet.release(acquire);
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public void archiveWallet(final int id) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfArchiveWallet.acquire();
        acquire.bindLong(1, id);
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfArchiveWallet.release(acquire);
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public void unArchiveWallet(final int id) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfUnArchiveWallet.acquire();
        acquire.bindLong(1, id);
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfUnArchiveWallet.release(acquire);
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public void updateWalletOrdering(final int order, final int id) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfUpdateWalletOrdering.acquire();
        acquire.bindLong(1, order);
        acquire.bindLong(2, id);
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfUpdateWalletOrdering.release(acquire);
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public List<WalletEntity> getCurrencyWalletEntity(final int id, final String code) {
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
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM wallet WHERE account_id = ? AND currency = ?", 2);
        acquire.bindLong(1, id);
        if (code == null) {
            acquire.bindNull(2);
        } else {
            acquire.bindString(2, code);
        }
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
            columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "name");
            columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, TypedValues.Custom.S_COLOR);
            columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "amount");
            columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "initial_amount");
            columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, AppMeasurementSdk.ConditionalUserProperty.ACTIVE);
            columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "account_id");
            columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "ordering");
            columnIndexOrThrow9 = CursorUtil.getColumnIndexOrThrow(query, "exclude");
            columnIndexOrThrow10 = CursorUtil.getColumnIndexOrThrow(query, FirebaseAnalytics.Param.CURRENCY);
            columnIndexOrThrow11 = CursorUtil.getColumnIndexOrThrow(query, "icon");
            columnIndexOrThrow12 = CursorUtil.getColumnIndexOrThrow(query, JamXmlElements.TYPE);
            columnIndexOrThrow13 = CursorUtil.getColumnIndexOrThrow(query, "due_date");
            columnIndexOrThrow14 = CursorUtil.getColumnIndexOrThrow(query, "statement_date");
            roomSQLiteQuery = acquire;
        } catch (Throwable th) {
            th = th;
            roomSQLiteQuery = acquire;
        }
        try {
            int columnIndexOrThrow15 = CursorUtil.getColumnIndexOrThrow(query, "credit_limit");
            int i = columnIndexOrThrow;
            int columnIndexOrThrow16 = CursorUtil.getColumnIndexOrThrow(query, CellUtil.HIDDEN);
            int i2 = columnIndexOrThrow15;
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                int i3 = i2;
                int i4 = columnIndexOrThrow14;
                WalletEntity walletEntity = new WalletEntity(query.isNull(columnIndexOrThrow2) ? null : query.getString(columnIndexOrThrow2), query.getInt(columnIndexOrThrow11), query.isNull(columnIndexOrThrow3) ? null : query.getString(columnIndexOrThrow3), query.getLong(columnIndexOrThrow4), query.getLong(columnIndexOrThrow5), query.getInt(columnIndexOrThrow6), query.getInt(columnIndexOrThrow7), query.getInt(columnIndexOrThrow8), query.getInt(columnIndexOrThrow9), query.isNull(columnIndexOrThrow10) ? null : query.getString(columnIndexOrThrow10), query.getInt(columnIndexOrThrow12), query.getInt(columnIndexOrThrow13), query.getInt(columnIndexOrThrow14), query.getLong(i3));
                int i5 = i;
                int i6 = columnIndexOrThrow13;
                walletEntity.setId(query.getInt(i5));
                int i7 = columnIndexOrThrow16;
                walletEntity.setHidden(query.getInt(i7));
                arrayList.add(walletEntity);
                columnIndexOrThrow14 = i4;
                i2 = i3;
                columnIndexOrThrow13 = i6;
                i = i5;
                columnIndexOrThrow16 = i7;
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

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public LiveData<Integer> getArchieveWalletRow(final int accountId) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT COUNT(id) FROM wallet WHERE account_id = ? AND hidden = 1", 1);
        acquire.bindLong(1, accountId);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"wallet"}, false, new Callable<Integer>() { // from class: com.ktwapps.walletmanager.Database.Dao.WalletDaoObject_Impl.7
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                Integer num = null;
                Cursor query = DBUtil.query(WalletDaoObject_Impl.this.__db, acquire, false, null);
                try {
                    if (query.moveToFirst() && !query.isNull(0)) {
                        num = Integer.valueOf(query.getInt(0));
                    }
                    return num;
                } finally {
                    query.close();
                }
            }

            protected void finalize() {
                acquire.release();
            }
        });
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public int getWalletLastOrdering(final int accountId) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT ordering FROM wallet WHERE account_id = ? ORDER BY ordering DESC LIMIT 1", 1);
        acquire.bindLong(1, accountId);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            return query.moveToFirst() ? query.getInt(0) : 0;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public WalletEntity getWalletById(final int id) {
        RoomSQLiteQuery roomSQLiteQuery;
        WalletEntity walletEntity = null;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM wallet WHERE id = ?", 1);
        acquire.bindLong(1, id);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "name");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, TypedValues.Custom.S_COLOR);
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "amount");
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "initial_amount");
            int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, AppMeasurementSdk.ConditionalUserProperty.ACTIVE);
            int columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "account_id");
            int columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "ordering");
            int columnIndexOrThrow9 = CursorUtil.getColumnIndexOrThrow(query, "exclude");
            int columnIndexOrThrow10 = CursorUtil.getColumnIndexOrThrow(query, FirebaseAnalytics.Param.CURRENCY);
            int columnIndexOrThrow11 = CursorUtil.getColumnIndexOrThrow(query, "icon");
            int columnIndexOrThrow12 = CursorUtil.getColumnIndexOrThrow(query, JamXmlElements.TYPE);
            int columnIndexOrThrow13 = CursorUtil.getColumnIndexOrThrow(query, "due_date");
            int columnIndexOrThrow14 = CursorUtil.getColumnIndexOrThrow(query, "statement_date");
            roomSQLiteQuery = acquire;
            try {
                int columnIndexOrThrow15 = CursorUtil.getColumnIndexOrThrow(query, "credit_limit");
                int columnIndexOrThrow16 = CursorUtil.getColumnIndexOrThrow(query, CellUtil.HIDDEN);
                if (query.moveToFirst()) {
                    walletEntity = new WalletEntity(query.isNull(columnIndexOrThrow2) ? null : query.getString(columnIndexOrThrow2), query.getInt(columnIndexOrThrow11), query.isNull(columnIndexOrThrow3) ? null : query.getString(columnIndexOrThrow3), query.getLong(columnIndexOrThrow4), query.getLong(columnIndexOrThrow5), query.getInt(columnIndexOrThrow6), query.getInt(columnIndexOrThrow7), query.getInt(columnIndexOrThrow8), query.getInt(columnIndexOrThrow9), query.isNull(columnIndexOrThrow10) ? null : query.getString(columnIndexOrThrow10), query.getInt(columnIndexOrThrow12), query.getInt(columnIndexOrThrow13), query.getInt(columnIndexOrThrow14), query.getLong(columnIndexOrThrow15));
                    walletEntity.setId(query.getInt(columnIndexOrThrow));
                    walletEntity.setHidden(query.getInt(columnIndexOrThrow16));
                } else {
                    walletEntity = null;
                }
                query.close();
                roomSQLiteQuery.release();
                return walletEntity;
            } catch (Throwable th) {
                th = th;
                query.close();
                roomSQLiteQuery.release();
                throw th;
            }
        } catch (Throwable th2) {
            Throwable th = th2;
            roomSQLiteQuery = acquire;
        }
        return walletEntity;
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public LiveData<List<Wallets>> getAllWallets(final int accountId, final int active, final long date) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT t1.id, (SELECT SUM (amount) FROM(   SELECT SUM(amount) as amount FROM trans WHERE wallet_id = t1.id AND type != 2 AND date_time < ?   UNION ALL   SELECT SUM(-amount) as amount FROM trans WHERE wallet_id = t1.id AND type = 2 AND date_time < ?   UNION ALL   SELECT initial_amount FROM wallet WHERE id = t1.id   UNION ALL   SELECT SUM(trans_amount) as amount FROM trans WHERE transfer_wallet_id = t1.id AND type = 2 AND date_time < ?) as subt1) as amount,t1.currency,t1.due_date as dueDate,t1.statement_date as statementDate,t1.type,t1.credit_limit as creditLimit,t1.icon,t1.color,t1.name,t1.initial_amount as initialAmount,t1.exclude, c.rate FROM wallet as t1 LEFT JOIN currency as c ON t1.currency = c.code\nWHERE t1.account_id = ? AND c.account_id = ? AND t1.active = ? AND t1.hidden = 0 ORDER BY t1.ordering ASC", 6);
        acquire.bindLong(1, date);
        acquire.bindLong(2, date);
        acquire.bindLong(3, date);
        long j = accountId;
        acquire.bindLong(4, j);
        acquire.bindLong(5, j);
        acquire.bindLong(6, active);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"trans", "wallet", FirebaseAnalytics.Param.CURRENCY}, false, new Callable<List<Wallets>>() { // from class: com.ktwapps.walletmanager.Database.Dao.WalletDaoObject_Impl.8
            @Override // java.util.concurrent.Callable
            public List<Wallets> call() throws Exception {
                Cursor query = DBUtil.query(WalletDaoObject_Impl.this.__db, acquire, false, null);
                try {
                    ArrayList arrayList = new ArrayList(query.getCount());
                    while (query.moveToNext()) {
                        int i = query.getInt(0);
                        long j2 = query.getLong(1);
                        String string = query.isNull(2) ? null : query.getString(2);
                        int i2 = query.getInt(3);
                        int i3 = query.getInt(4);
                        int i4 = query.getInt(5);
                        long j3 = query.getLong(6);
                        int i5 = query.getInt(7);
                        String string2 = query.isNull(8) ? null : query.getString(8);
                        arrayList.add(new Wallets(query.isNull(9) ? null : query.getString(9), string2, query.getFloat(12), string, j2, query.getLong(10), i, query.getInt(11), i5, i4, i2, i3, j3));
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

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public LiveData<List<Wallets>> getHiddenWallets(final int accountId, final int active, final long date) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT t1.id, (SELECT SUM (amount) FROM(   SELECT SUM(amount) as amount FROM trans WHERE wallet_id = t1.id AND type != 2 AND date_time < ?   UNION ALL   SELECT SUM(-amount) as amount FROM trans WHERE wallet_id = t1.id AND type = 2 AND date_time < ?   UNION ALL   SELECT initial_amount FROM wallet WHERE id = t1.id   UNION ALL   SELECT SUM(trans_amount) as amount FROM trans WHERE transfer_wallet_id = t1.id AND type = 2 AND date_time < ?) as subt1) as amount,t1.currency,t1.due_date as dueDate,t1.statement_date as statementDate,t1.type,t1.credit_limit as creditLimit,t1.icon,t1.color,t1.name,t1.initial_amount as initialAmount,t1.exclude, c.rate FROM wallet as t1 LEFT JOIN currency as c ON t1.currency = c.code\nWHERE t1.account_id = ? AND c.account_id = ? AND t1.active = ? AND t1.hidden = 1 ORDER BY t1.ordering ASC", 6);
        acquire.bindLong(1, date);
        acquire.bindLong(2, date);
        acquire.bindLong(3, date);
        long j = accountId;
        acquire.bindLong(4, j);
        acquire.bindLong(5, j);
        acquire.bindLong(6, active);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"trans", "wallet", FirebaseAnalytics.Param.CURRENCY}, false, new Callable<List<Wallets>>() { // from class: com.ktwapps.walletmanager.Database.Dao.WalletDaoObject_Impl.9
            @Override // java.util.concurrent.Callable
            public List<Wallets> call() throws Exception {
                Cursor query = DBUtil.query(WalletDaoObject_Impl.this.__db, acquire, false, null);
                try {
                    ArrayList arrayList = new ArrayList(query.getCount());
                    while (query.moveToNext()) {
                        int i = query.getInt(0);
                        long j2 = query.getLong(1);
                        String string = query.isNull(2) ? null : query.getString(2);
                        int i2 = query.getInt(3);
                        int i3 = query.getInt(4);
                        int i4 = query.getInt(5);
                        long j3 = query.getLong(6);
                        int i5 = query.getInt(7);
                        String string2 = query.isNull(8) ? null : query.getString(8);
                        arrayList.add(new Wallets(query.isNull(9) ? null : query.getString(9), string2, query.getFloat(12), string, j2, query.getLong(10), i, query.getInt(11), i5, i4, i2, i3, j3));
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

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public LiveData<WalletDetail> getLiveWalletById(final int id, final int accountId, final long date) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT t1.id, (SELECT SUM (amount) FROM(   SELECT SUM(amount) as amount FROM trans WHERE wallet_id = t1.id AND type != 2 AND date_time < ?   UNION ALL   SELECT SUM(-amount) as amount FROM trans WHERE wallet_id = t1.id AND type = 2 AND date_time < ?   UNION ALL   SELECT initial_amount FROM wallet WHERE id = t1.id   UNION ALL   SELECT SUM(trans_amount) as amount FROM trans WHERE transfer_wallet_id = t1.id AND type = 2 AND date_time < ?) as subt1) as amount,(SELECT COUNT(id) FROM trans WHERE type = 0 AND wallet_id = ? AND account_id = ?) as income, (SELECT COUNT(id) FROM trans WHERE type = 1 AND wallet_id = ? AND account_id = ?) as expense, (SELECT COUNT(id) FROM trans WHERE type = 2 AND (wallet_id = ? OR transfer_wallet_id = ?) AND account_id = ?) as transfer, t1.currency,t1.due_date as dueDate,t1.statement_date as statementDate,t1.type,t1.credit_limit as creditLimit,t1.icon,t1.color,t1.name,t1.initial_amount as initialAmount,t1.exclude, c.rate\nFROM wallet as t1 LEFT JOIN currency as c ON t1.currency = c.code WHERE t1.id = ? AND t1.account_id = ? AND c.account_id = ?", 13);
        acquire.bindLong(1, date);
        acquire.bindLong(2, date);
        acquire.bindLong(3, date);
        long j = id;
        acquire.bindLong(4, j);
        long j2 = accountId;
        acquire.bindLong(5, j2);
        acquire.bindLong(6, j);
        acquire.bindLong(7, j2);
        acquire.bindLong(8, j);
        acquire.bindLong(9, j);
        acquire.bindLong(10, j2);
        acquire.bindLong(11, j);
        acquire.bindLong(12, j2);
        acquire.bindLong(13, j2);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"trans", "wallet", FirebaseAnalytics.Param.CURRENCY}, false, new Callable<WalletDetail>() { // from class: com.ktwapps.walletmanager.Database.Dao.WalletDaoObject_Impl.10
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public WalletDetail call() throws Exception {
                WalletDetail walletDetail = null;
                Cursor query = DBUtil.query(WalletDaoObject_Impl.this.__db, acquire, false, null);
                try {
                    if (query.moveToFirst()) {
                        int i = query.getInt(0);
                        long j3 = query.getLong(1);
                        int i2 = query.getInt(2);
                        int i3 = query.getInt(3);
                        int i4 = query.getInt(4);
                        String string = query.isNull(5) ? null : query.getString(5);
                        int i5 = query.getInt(6);
                        int i6 = query.getInt(7);
                        int i7 = query.getInt(8);
                        long j4 = query.getLong(9);
                        int i8 = query.getInt(10);
                        String string2 = query.isNull(11) ? null : query.getString(11);
                        walletDetail = new WalletDetail(query.isNull(12) ? null : query.getString(12), string2, query.getFloat(15), string, j3, query.getLong(13), i, query.getInt(14), i8, i2, i3, i4, i7, i5, i6, j4);
                    }
                    return walletDetail;
                } finally {
                    query.close();
                }
            }

            protected void finalize() {
                acquire.release();
            }
        });
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public List<WalletTrans> getWalletTransById(final int id, final int accountId) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT c.id,c.color,c.icon,c.name,c.default_category as categoryDefault,COUNT(t.id) as trans,t.type, SUM(t.amount) as amount FROM trans as t LEFT JOIN category as c ON t.category_id = c.id LEFT JOIN wallet as w ON t.wallet_id = w.id WHERE t.account_id = ? AND t.type != 2 AND w.id = ? GROUP BY c.id UNION ALL SELECT 0 as id,\"\" as color,\"\" as icon,\"\" as name,0 as categoryDefault,SUM(trans) as trans, 2 as type,SUM(amount) as amount FROM(   SELECT COUNT(t.id) as trans,SUM(-t.amount) as amount FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id WHERE t.account_id = ? AND t.type = 2 AND w.id = ? GROUP BY w.id   UNION ALL   SELECT COUNT(t.id) as trans,SUM(t.trans_amount) as amount FROM trans as t LEFT JOIN wallet as w ON t.transfer_wallet_id = w.id WHERE t.account_id = ? AND t.type = 2 AND w.id = ? GROUP BY w.id) as t1", 6);
        long j = accountId;
        acquire.bindLong(1, j);
        long j2 = id;
        acquire.bindLong(2, j2);
        acquire.bindLong(3, j);
        acquire.bindLong(4, j2);
        acquire.bindLong(5, j);
        acquire.bindLong(6, j2);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                int i = query.getInt(0);
                String string = query.isNull(1) ? null : query.getString(1);
                arrayList.add(new WalletTrans(i, query.getInt(6), query.getInt(2), query.getInt(5), string, query.isNull(3) ? null : query.getString(3), query.getLong(7), query.getInt(4)));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public List<Wallets> getWallets(final int accountId, final int active, final long date) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT \n\tt1.id, (\n\t\tSELECT SUM (amount) FROM(\n\t\t\tSELECT SUM(amount)  as amount\n\t\t\tFROM trans\n\t\t\tWHERE wallet_id = t1.id AND type != 2 AND date_time < ?\n\t\t\t\tUNION ALL \n\t\t\tSELECT SUM(-amount) as amount\n\t\t\tFROM trans\n\t\t\tWHERE wallet_id = t1.id AND type = 2 AND date_time < ?\n\t\t\t\tUNION ALL\n\t\t\tSELECT initial_amount FROM wallet WHERE id = t1.id\n\t\t\t\tUNION ALL\n\t\t\tSELECT SUM(trans_amount) as amount\n\t\t\tFROM trans\n\t\t\tWHERE transfer_wallet_id = t1.id AND type = 2 AND date_time < ?\n\t\t)  as subt1\n\t) as amount,t1.currency,t1.due_date as dueDate,t1.statement_date as statementDate,t1.type,t1.credit_limit as creditLimit,t1.icon,t1.color,t1.name,t1.initial_amount as initialAmount,t1.exclude, c.rate\nFROM wallet as t1\nLEFT JOIN currency as c \nON t1.currency = c.code\nWHERE t1.account_id = ? AND c.account_id = ? AND t1.active = ? AND t1.hidden = 0 ORDER BY t1.ordering ASC", 6);
        acquire.bindLong(1, date);
        acquire.bindLong(2, date);
        acquire.bindLong(3, date);
        long j = accountId;
        acquire.bindLong(4, j);
        acquire.bindLong(5, j);
        acquire.bindLong(6, active);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                int i = query.getInt(0);
                long j2 = query.getLong(1);
                String string = query.isNull(2) ? null : query.getString(2);
                int i2 = query.getInt(3);
                int i3 = query.getInt(4);
                int i4 = query.getInt(5);
                long j3 = query.getLong(6);
                int i5 = query.getInt(7);
                arrayList.add(new Wallets(query.isNull(9) ? null : query.getString(9), query.isNull(8) ? null : query.getString(8), query.getFloat(12), string, j2, query.getLong(10), i, query.getInt(11), i5, i4, i2, i3, j3));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public List<DailyTrans> getDailyTrans(final int accountId, final int walletId, final int categoryId) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT SUM(amount) as amount, day, month, year FROM (SELECT * FROM (SELECT SUM(amount) as amount, CAST(strftime('%d', datetime(date_time/1000, 'unixepoch','localtime')) AS int) as day,CAST(strftime('%m', datetime(date_time/1000, 'unixepoch','localtime')) AS int) as month,CAST(strftime('%Y', datetime(date_time/1000, 'unixepoch','localtime')) AS int) as year FROM trans WHERE account_id = ? AND category_id = ? AND wallet_id = ? GROUP BY CAST(strftime('%d', datetime(date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%m', datetime(date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%Y', datetime(date_time/1000, 'unixepoch','localtime')) AS int) ORDER BY CAST(strftime('%Y', datetime(date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%m', datetime(date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%d', datetime(date_time/1000, 'unixepoch','localtime')) AS int) DESC) as t1 ) as t1 GROUP BY day, month, year ORDER BY year DESC, month DESC, day DESC", 3);
        acquire.bindLong(1, accountId);
        acquire.bindLong(2, categoryId);
        acquire.bindLong(3, walletId);
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

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public List<DailyTrans> getDailyTrans(final int accountId, final int walletId) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT SUM(amount) as amount, day, month, year FROM (SELECT * FROM (SELECT SUM(-amount) as amount, CAST(strftime('%d', datetime(date_time/1000, 'unixepoch','localtime')) AS int) as day,CAST(strftime('%m', datetime(date_time/1000, 'unixepoch','localtime')) AS int) as month,CAST(strftime('%Y', datetime(date_time/1000, 'unixepoch','localtime')) AS int) as year FROM trans WHERE account_id = ? AND type = 2 AND wallet_id = ? GROUP BY CAST(strftime('%d', datetime(date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%m', datetime(date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%Y', datetime(date_time/1000, 'unixepoch','localtime')) AS int) ORDER BY CAST(strftime('%Y', datetime(date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%m', datetime(date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%d', datetime(date_time/1000, 'unixepoch','localtime')) AS int) DESC) as t2 UNION ALL SELECT * FROM (SELECT SUM(trans_amount) as amount, CAST(strftime('%d', datetime(date_time/1000, 'unixepoch','localtime')) AS int) as day,CAST(strftime('%m', datetime(date_time/1000, 'unixepoch','localtime')) AS int) as month,CAST(strftime('%Y', datetime(date_time/1000, 'unixepoch','localtime')) AS int) as year FROM trans WHERE account_id = ? AND type = 2 AND transfer_wallet_id = ? GROUP BY CAST(strftime('%d', datetime(date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%m', datetime(date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%Y', datetime(date_time/1000, 'unixepoch','localtime')) AS int) ORDER BY CAST(strftime('%Y', datetime(date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%m', datetime(date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%d', datetime(date_time/1000, 'unixepoch','localtime')) AS int) DESC) as t2 ) as t1 GROUP BY day, month, year ORDER BY year DESC, month DESC, day DESC", 4);
        long j = accountId;
        acquire.bindLong(1, j);
        long j2 = walletId;
        acquire.bindLong(2, j2);
        acquire.bindLong(3, j);
        acquire.bindLong(4, j2);
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

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public List<Trans> getTransFromDate(final int accountId, final int walletId, final int categoryId, final long startDate, final long endDate) {
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
        int i;
        String str;
        String string2;
        int i2;
        String string3;
        int i3;
        String string4;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM(SELECT d.id as debtId, d.color as debtColor,d.type as debtType,dt.id as debtTransId, dt.type as debtTransType,t.id,t.note, t.memo,t.trans_amount as transAmount,w.currency as currency, t.subcategory_id as subcategoryId, sc.name as subcategory,c.icon,c.default_category as categoryDefault,t.fee_id as feeId,c.color,t.amount,t.date_time as dateTime,t.type,w.name as wallet, tw.name as transferWallet, t.wallet_id as walletId, t.transfer_wallet_id as transferWalletId ,c.name as category, c.id as categoryId, GROUP_CONCAT(m.path) as media FROM trans t LEFT JOIN category as c ON t.category_id = c.id LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN wallet as tw ON t.transfer_wallet_id = tw.id LEFT JOIN media as m ON m.trans_id = t.id LEFT JOIN debt as d ON t.debt_id = d.id LEFT JOIN debtTrans as dt ON t.debt_trans_id = dt.id LEFT JOIN subcategory as sc ON t.subcategory_id = sc.id WHERE t.date_time >= ? AND t.date_time < ? AND t.account_id = ? AND t.wallet_id = ? AND t.category_id = ? GROUP BY t.id ) as t1 ORDER BY dateTime DESC", 5);
        acquire.bindLong(1, startDate);
        acquire.bindLong(2, endDate);
        acquire.bindLong(3, accountId);
        acquire.bindLong(4, walletId);
        acquire.bindLong(5, categoryId);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "debtId");
            columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "debtColor");
            columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "debtType");
            columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "debtTransId");
            columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "debtTransType");
            columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "id");
            columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "note");
            columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "memo");
            columnIndexOrThrow9 = CursorUtil.getColumnIndexOrThrow(query, "transAmount");
            columnIndexOrThrow10 = CursorUtil.getColumnIndexOrThrow(query, FirebaseAnalytics.Param.CURRENCY);
            columnIndexOrThrow11 = CursorUtil.getColumnIndexOrThrow(query, "subcategoryId");
            columnIndexOrThrow12 = CursorUtil.getColumnIndexOrThrow(query, "subcategory");
            columnIndexOrThrow13 = CursorUtil.getColumnIndexOrThrow(query, "icon");
            columnIndexOrThrow14 = CursorUtil.getColumnIndexOrThrow(query, "categoryDefault");
            roomSQLiteQuery = acquire;
        } catch (Throwable th) {
            th = th;
            roomSQLiteQuery = acquire;
        }
        try {
            int columnIndexOrThrow15 = CursorUtil.getColumnIndexOrThrow(query, "feeId");
            int i4 = columnIndexOrThrow6;
            int columnIndexOrThrow16 = CursorUtil.getColumnIndexOrThrow(query, TypedValues.Custom.S_COLOR);
            int columnIndexOrThrow17 = CursorUtil.getColumnIndexOrThrow(query, "amount");
            int columnIndexOrThrow18 = CursorUtil.getColumnIndexOrThrow(query, "dateTime");
            int columnIndexOrThrow19 = CursorUtil.getColumnIndexOrThrow(query, JamXmlElements.TYPE);
            int columnIndexOrThrow20 = CursorUtil.getColumnIndexOrThrow(query, "wallet");
            int columnIndexOrThrow21 = CursorUtil.getColumnIndexOrThrow(query, "transferWallet");
            int columnIndexOrThrow22 = CursorUtil.getColumnIndexOrThrow(query, "walletId");
            int columnIndexOrThrow23 = CursorUtil.getColumnIndexOrThrow(query, "transferWalletId");
            int columnIndexOrThrow24 = CursorUtil.getColumnIndexOrThrow(query, "category");
            int columnIndexOrThrow25 = CursorUtil.getColumnIndexOrThrow(query, "categoryId");
            int columnIndexOrThrow26 = CursorUtil.getColumnIndexOrThrow(query, "media");
            int i5 = columnIndexOrThrow15;
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                int i6 = query.getInt(columnIndexOrThrow);
                String string5 = query.isNull(columnIndexOrThrow2) ? null : query.getString(columnIndexOrThrow2);
                int i7 = query.getInt(columnIndexOrThrow3);
                int i8 = query.getInt(columnIndexOrThrow4);
                int i9 = query.getInt(columnIndexOrThrow5);
                String string6 = query.isNull(columnIndexOrThrow7) ? null : query.getString(columnIndexOrThrow7);
                String string7 = query.isNull(columnIndexOrThrow8) ? null : query.getString(columnIndexOrThrow8);
                long j = query.getLong(columnIndexOrThrow9);
                String string8 = query.isNull(columnIndexOrThrow10) ? null : query.getString(columnIndexOrThrow10);
                int i10 = query.getInt(columnIndexOrThrow11);
                String string9 = query.isNull(columnIndexOrThrow12) ? null : query.getString(columnIndexOrThrow12);
                int i11 = query.getInt(columnIndexOrThrow13);
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
                long j2 = query.getLong(i17);
                columnIndexOrThrow17 = i17;
                int i18 = columnIndexOrThrow18;
                Date date = DateConverter.toDate(query.isNull(i18) ? null : Long.valueOf(query.getLong(i18)));
                columnIndexOrThrow18 = i18;
                int i19 = columnIndexOrThrow19;
                int i20 = query.getInt(i19);
                columnIndexOrThrow19 = i19;
                int i21 = columnIndexOrThrow20;
                if (query.isNull(i21)) {
                    columnIndexOrThrow20 = i21;
                    i = columnIndexOrThrow21;
                    str = null;
                } else {
                    String string10 = query.getString(i21);
                    columnIndexOrThrow20 = i21;
                    i = columnIndexOrThrow21;
                    str = string10;
                }
                if (query.isNull(i)) {
                    columnIndexOrThrow21 = i;
                    i2 = columnIndexOrThrow22;
                    string2 = null;
                } else {
                    string2 = query.getString(i);
                    columnIndexOrThrow21 = i;
                    i2 = columnIndexOrThrow22;
                }
                int i22 = query.getInt(i2);
                columnIndexOrThrow22 = i2;
                int i23 = columnIndexOrThrow23;
                int i24 = query.getInt(i23);
                columnIndexOrThrow23 = i23;
                int i25 = columnIndexOrThrow24;
                if (query.isNull(i25)) {
                    columnIndexOrThrow24 = i25;
                    i3 = columnIndexOrThrow25;
                    string3 = null;
                } else {
                    string3 = query.getString(i25);
                    columnIndexOrThrow24 = i25;
                    i3 = columnIndexOrThrow25;
                }
                int i26 = query.getInt(i3);
                columnIndexOrThrow25 = i3;
                int i27 = columnIndexOrThrow26;
                if (query.isNull(i27)) {
                    columnIndexOrThrow26 = i27;
                    string4 = null;
                } else {
                    string4 = query.getString(i27);
                    columnIndexOrThrow26 = i27;
                }
                Trans trans = new Trans(string6, string7, string, i11, string8, date, j2, str, i20, string2, i22, i24, string3, i26, i12, i14, string4, j, string5, i6, i7, i8, i9, string9, i10);
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

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public List<Trans> getTransFromDate(final int accountId, final int walletId, final long startDate, final long endDate) {
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
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM(SELECT d.id as debtId, d.color as debtColor,d.type as debtType,dt.id as debtTransId, dt.type as debtTransType,t.id,t.note, t.memo,t.trans_amount as transAmount, t.subcategory_id as subcategoryId, sc.name as subcategory,w.currency as currency,c.icon,c.default_category as categoryDefault,t.fee_id as feeId,c.color,t.amount,t.date_time as dateTime,t.type,w.name as wallet, tw.name as transferWallet, t.wallet_id as walletId, t.transfer_wallet_id as transferWalletId ,c.name as category, c.id as categoryId, GROUP_CONCAT(m.path) as media FROM trans t LEFT JOIN category as c ON t.category_id = c.id LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN wallet as tw ON t.transfer_wallet_id = tw.id LEFT JOIN media as m ON m.trans_id = t.id LEFT JOIN debt as d ON t.debt_id = d.id LEFT JOIN debtTrans as dt ON t.debt_trans_id = dt.id LEFT JOIN subcategory as sc ON t.subcategory_id = sc.id WHERE t.date_time >= ? AND t.date_time < ? AND t.account_id = ? AND (t.wallet_id = ? OR t.transfer_wallet_id = ?) AND t.type = 2 GROUP BY t.id ) as t1 ORDER BY dateTime DESC", 5);
        acquire.bindLong(1, startDate);
        acquire.bindLong(2, endDate);
        acquire.bindLong(3, accountId);
        long j = walletId;
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
            columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "id");
            columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "note");
            columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "memo");
            columnIndexOrThrow9 = CursorUtil.getColumnIndexOrThrow(query, "transAmount");
            columnIndexOrThrow10 = CursorUtil.getColumnIndexOrThrow(query, "subcategoryId");
            columnIndexOrThrow11 = CursorUtil.getColumnIndexOrThrow(query, "subcategory");
            columnIndexOrThrow12 = CursorUtil.getColumnIndexOrThrow(query, FirebaseAnalytics.Param.CURRENCY);
            columnIndexOrThrow13 = CursorUtil.getColumnIndexOrThrow(query, "icon");
            columnIndexOrThrow14 = CursorUtil.getColumnIndexOrThrow(query, "categoryDefault");
            roomSQLiteQuery = acquire;
        } catch (Throwable th) {
            th = th;
            roomSQLiteQuery = acquire;
        }
        try {
            int columnIndexOrThrow15 = CursorUtil.getColumnIndexOrThrow(query, "feeId");
            int i4 = columnIndexOrThrow6;
            int columnIndexOrThrow16 = CursorUtil.getColumnIndexOrThrow(query, TypedValues.Custom.S_COLOR);
            int columnIndexOrThrow17 = CursorUtil.getColumnIndexOrThrow(query, "amount");
            int columnIndexOrThrow18 = CursorUtil.getColumnIndexOrThrow(query, "dateTime");
            int columnIndexOrThrow19 = CursorUtil.getColumnIndexOrThrow(query, JamXmlElements.TYPE);
            int columnIndexOrThrow20 = CursorUtil.getColumnIndexOrThrow(query, "wallet");
            int columnIndexOrThrow21 = CursorUtil.getColumnIndexOrThrow(query, "transferWallet");
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
                String string7 = query.isNull(columnIndexOrThrow7) ? null : query.getString(columnIndexOrThrow7);
                String string8 = query.isNull(columnIndexOrThrow8) ? null : query.getString(columnIndexOrThrow8);
                long j2 = query.getLong(columnIndexOrThrow9);
                int i10 = query.getInt(columnIndexOrThrow10);
                String string9 = query.isNull(columnIndexOrThrow11) ? null : query.getString(columnIndexOrThrow11);
                String string10 = query.isNull(columnIndexOrThrow12) ? null : query.getString(columnIndexOrThrow12);
                int i11 = query.getInt(columnIndexOrThrow13);
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
                int i20 = query.getInt(i19);
                columnIndexOrThrow19 = i19;
                int i21 = columnIndexOrThrow20;
                if (query.isNull(i21)) {
                    columnIndexOrThrow20 = i21;
                    i = columnIndexOrThrow21;
                    string2 = null;
                } else {
                    string2 = query.getString(i21);
                    columnIndexOrThrow20 = i21;
                    i = columnIndexOrThrow21;
                }
                if (query.isNull(i)) {
                    columnIndexOrThrow21 = i;
                    i2 = columnIndexOrThrow22;
                    string3 = null;
                } else {
                    string3 = query.getString(i);
                    columnIndexOrThrow21 = i;
                    i2 = columnIndexOrThrow22;
                }
                int i22 = query.getInt(i2);
                columnIndexOrThrow22 = i2;
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
                Trans trans = new Trans(string7, string8, string, i11, string10, date, j3, string2, i20, string3, i22, i24, string4, i26, i12, i14, string5, j2, string6, i6, i7, i8, i9, string9, i10);
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

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public long getWalletBalance(final int id, final long date) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT SUM (amount) as amount FROM( SELECT SUM(amount) as amount FROM trans WHERE wallet_id = ? AND type != 2 AND date_time < ? UNION ALL SELECT SUM(-amount) as amount FROM trans WHERE wallet_id = ? AND type = 2 AND date_time < ? UNION ALL SELECT SUM(trans_amount) as amount FROM trans WHERE transfer_wallet_id = ? AND type = 2 AND date_time < ? UNION ALL SELECT initial_amount FROM wallet WHERE id = ?) as t1", 7);
        long j = id;
        acquire.bindLong(1, j);
        acquire.bindLong(2, date);
        acquire.bindLong(3, j);
        acquire.bindLong(4, date);
        acquire.bindLong(5, j);
        acquire.bindLong(6, date);
        acquire.bindLong(7, j);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            return query.moveToFirst() ? query.getLong(0) : 0L;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public CalendarSummary getWalletOverview(final int id, final long startDate, final long endDate) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT (SELECT SUM(amount) as income FROM (   SELECT SUM(amount) as amount FROM trans WHERE type = 0 AND wallet_id = ? AND date_time >= ? AND date_time < ?    UNION ALL    SELECT SUM(trans_amount) as amount FROM trans WHERE type = 2 AND transfer_wallet_id = ? AND date_time >= ? AND date_time < ?)) as income, (SELECT SUM(amount) as expense FROM (SELECT SUM(amount) as amount FROM trans WHERE type = 1 AND wallet_id = ? AND date_time >= ? AND date_time < ?    UNION ALL    SELECT SUM(-amount) as amount FROM trans WHERE type = 2 AND wallet_id = ? AND date_time >= ? AND date_time < ?)) as expense", 12);
        long j = id;
        acquire.bindLong(1, j);
        acquire.bindLong(2, startDate);
        acquire.bindLong(3, endDate);
        acquire.bindLong(4, j);
        acquire.bindLong(5, startDate);
        acquire.bindLong(6, endDate);
        acquire.bindLong(7, j);
        acquire.bindLong(8, startDate);
        acquire.bindLong(9, endDate);
        acquire.bindLong(10, j);
        acquire.bindLong(11, startDate);
        acquire.bindLong(12, endDate);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            return query.moveToFirst() ? new CalendarSummary(query.getLong(0), query.getLong(1)) : null;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public CalendarSummary getWalletOverview(final int id) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT (SELECT SUM(amount) as income FROM (   SELECT SUM(amount) as amount FROM trans WHERE type = 0 AND wallet_id = ?   UNION ALL    SELECT SUM(trans_amount) as amount FROM trans WHERE type = 2 AND transfer_wallet_id = ?)) as income, (SELECT SUM(amount) as expense FROM (SELECT SUM(amount) as amount FROM trans WHERE type = 1 AND wallet_id = ?   UNION ALL    SELECT SUM(-amount) as amount FROM trans WHERE type = 2 AND wallet_id = ?)) as expense", 4);
        long j = id;
        acquire.bindLong(1, j);
        acquire.bindLong(2, j);
        acquire.bindLong(3, j);
        acquire.bindLong(4, j);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            return query.moveToFirst() ? new CalendarSummary(query.getLong(0), query.getLong(1)) : null;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public List<Trans> getTopFiveSpending(final int walletId, final long startDate, final long endDate) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT d.id as debtId, d.color as debtColor,d.type as debtType,dt.id as debtTransId, dt.type as debtTransType,t.id,t.note, t.memo,t.trans_amount as transAmount, t.subcategory_id as subcategoryId, sc.name as subcategory, w.currency as currency,c.icon,c.default_category as categoryDefault,t.fee_id as feeId,c.color,t1.amount,t.date_time as dateTime,t.type,w.name as wallet, tw.name as transferWallet, t.wallet_id as walletId, t.transfer_wallet_id as transferWalletId ,c.name as category, c.id as categoryId, GROUP_CONCAT(m.path) as media FROM (\nSELECT id, amount FROM (\nSELECT id, amount as amount FROM trans WHERE date_time >= ? AND date_time < ? AND wallet_id = ? AND type = 1\nUNION ALL\nSELECT id, -amount as amount FROM trans WHERE date_time >= ? AND date_time < ? AND wallet_id = ? AND type = 2\n) ORDER BY amount ASC LIMIT 5\n) as t1 LEFT JOIN trans as t ON t1.id = t.id\nLEFT JOIN category as c ON t.category_id = c.id\nLEFT JOIN wallet as w ON t.wallet_id = w.id\nLEFT JOIN wallet as tw ON t.transfer_wallet_id = tw.id\nLEFT JOIN media as m ON m.trans_id = t.id \nLEFT JOIN debt as d ON t.debt_id = d.id LEFT JOIN debtTrans as dt ON t.debt_trans_id = dt.id LEFT JOIN subcategory as sc ON t.subcategory_id = sc.id GROUP BY t.id ORDER BY t1.amount ASC", 6);
        acquire.bindLong(1, startDate);
        acquire.bindLong(2, endDate);
        long j = walletId;
        acquire.bindLong(3, j);
        acquire.bindLong(4, startDate);
        acquire.bindLong(5, endDate);
        acquire.bindLong(6, j);
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

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public List<Trans> getTopFiveSpending(final int walletId) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT d.id as debtId, d.color as debtColor,d.type as debtType,dt.id as debtTransId, dt.type as debtTransType,t.id,t.note, t.memo,t.trans_amount as transAmount, t.subcategory_id as subcategoryId, sc.name as subcategory, w.currency as currency,c.icon,c.default_category as categoryDefault,t.fee_id as feeId,c.color,t1.amount,t.date_time as dateTime,t.type,w.name as wallet, tw.name as transferWallet, t.wallet_id as walletId, t.transfer_wallet_id as transferWalletId ,c.name as category, c.id as categoryId, GROUP_CONCAT(m.path) as media FROM (\nSELECT id, amount FROM (\nSELECT id, amount as amount FROM trans WHERE wallet_id = ? AND type = 1\nUNION ALL\nSELECT id, -amount as amount FROM trans WHERE wallet_id = ? AND type = 2\n) ORDER BY amount ASC LIMIT 5\n) as t1 LEFT JOIN trans as t ON t1.id = t.id\nLEFT JOIN category as c ON t.category_id = c.id\nLEFT JOIN wallet as w ON t.wallet_id = w.id\nLEFT JOIN wallet as tw ON t.transfer_wallet_id = tw.id\nLEFT JOIN media as m ON m.trans_id = t.id \nLEFT JOIN debt as d ON t.debt_id = d.id LEFT JOIN debtTrans as dt ON t.debt_trans_id = dt.id LEFT JOIN subcategory as sc ON t.subcategory_id = sc.id GROUP BY t.id ORDER BY t1.amount ASC", 2);
        long j = walletId;
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

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public List<DailyTrans> getAllOverviewTrans(final int accountId, final int walletId) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT SUM(amount) as amount, day, month, year FROM (SELECT * FROM (SELECT SUM(amount) as amount, CAST(strftime('%d', datetime(date_time/1000, 'unixepoch','localtime')) AS int) as day,CAST(strftime('%m', datetime(date_time/1000, 'unixepoch','localtime')) AS int) as month,CAST(strftime('%Y', datetime(date_time/1000, 'unixepoch','localtime')) AS int) as year FROM trans WHERE account_id = ? AND type != 2 AND wallet_id = ? GROUP BY CAST(strftime('%d', datetime(date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%m', datetime(date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%Y', datetime(date_time/1000, 'unixepoch','localtime')) AS int) ORDER BY CAST(strftime('%Y', datetime(date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%m', datetime(date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%d', datetime(date_time/1000, 'unixepoch','localtime')) AS int) DESC) as t1 UNION ALL SELECT * FROM (SELECT SUM(-amount) as amount, CAST(strftime('%d', datetime(date_time/1000, 'unixepoch','localtime')) AS int) as day,CAST(strftime('%m', datetime(date_time/1000, 'unixepoch','localtime')) AS int) as month,CAST(strftime('%Y', datetime(date_time/1000, 'unixepoch','localtime')) AS int) as year FROM trans WHERE account_id = ? AND type = 2 AND wallet_id = ? GROUP BY CAST(strftime('%d', datetime(date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%m', datetime(date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%Y', datetime(date_time/1000, 'unixepoch','localtime')) AS int) ORDER BY CAST(strftime('%Y', datetime(date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%m', datetime(date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%d', datetime(date_time/1000, 'unixepoch','localtime')) AS int) DESC) as t2 UNION ALL SELECT * FROM (SELECT SUM(trans_amount) as amount, CAST(strftime('%d', datetime(date_time/1000, 'unixepoch','localtime')) AS int) as day,CAST(strftime('%m', datetime(date_time/1000, 'unixepoch','localtime')) AS int) as month,CAST(strftime('%Y', datetime(date_time/1000, 'unixepoch','localtime')) AS int) as year FROM trans WHERE account_id = ? AND type = 2 AND transfer_wallet_id = ? GROUP BY CAST(strftime('%d', datetime(date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%m', datetime(date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%Y', datetime(date_time/1000, 'unixepoch','localtime')) AS int) ORDER BY CAST(strftime('%Y', datetime(date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%m', datetime(date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%d', datetime(date_time/1000, 'unixepoch','localtime')) AS int) DESC) as t3 ) as t1 GROUP BY day, month, year ORDER BY year DESC, month DESC, day DESC", 6);
        long j = accountId;
        acquire.bindLong(1, j);
        long j2 = walletId;
        acquire.bindLong(2, j2);
        acquire.bindLong(3, j);
        acquire.bindLong(4, j2);
        acquire.bindLong(5, j);
        acquire.bindLong(6, j2);
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

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public List<DailyTrans> getOverviewTrans(final int accountId, final int walletId, final long startDate, final long endDate) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT SUM(amount) as amount, day, month, year FROM (SELECT * FROM (SELECT SUM(amount) as amount, CAST(strftime('%d', datetime(date_time/1000, 'unixepoch','localtime')) AS int) as day,CAST(strftime('%m', datetime(date_time/1000, 'unixepoch','localtime')) AS int) as month,CAST(strftime('%Y', datetime(date_time/1000, 'unixepoch','localtime')) AS int) as year FROM trans WHERE account_id = ? AND type != 2 AND wallet_id = ? AND date_time >= ? AND date_time < ? GROUP BY CAST(strftime('%d', datetime(date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%m', datetime(date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%Y', datetime(date_time/1000, 'unixepoch','localtime')) AS int) ORDER BY CAST(strftime('%Y', datetime(date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%m', datetime(date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%d', datetime(date_time/1000, 'unixepoch','localtime')) AS int) DESC) as t1 UNION ALL SELECT * FROM (SELECT SUM(-amount) as amount, CAST(strftime('%d', datetime(date_time/1000, 'unixepoch','localtime')) AS int) as day,CAST(strftime('%m', datetime(date_time/1000, 'unixepoch','localtime')) AS int) as month,CAST(strftime('%Y', datetime(date_time/1000, 'unixepoch','localtime')) AS int) as year FROM trans WHERE account_id = ? AND type = 2 AND wallet_id = ? AND date_time >= ? AND date_time < ?  GROUP BY CAST(strftime('%d', datetime(date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%m', datetime(date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%Y', datetime(date_time/1000, 'unixepoch','localtime')) AS int) ORDER BY CAST(strftime('%Y', datetime(date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%m', datetime(date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%d', datetime(date_time/1000, 'unixepoch','localtime')) AS int) DESC) as t2 UNION ALL SELECT * FROM (SELECT SUM(trans_amount) as amount, CAST(strftime('%d', datetime(date_time/1000, 'unixepoch','localtime')) AS int) as day,CAST(strftime('%m', datetime(date_time/1000, 'unixepoch','localtime')) AS int) as month,CAST(strftime('%Y', datetime(date_time/1000, 'unixepoch','localtime')) AS int) as year FROM trans WHERE account_id = ? AND type = 2 AND transfer_wallet_id = ? AND date_time >= ? AND date_time < ?  GROUP BY CAST(strftime('%d', datetime(date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%m', datetime(date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%Y', datetime(date_time/1000, 'unixepoch','localtime')) AS int) ORDER BY CAST(strftime('%Y', datetime(date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%m', datetime(date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%d', datetime(date_time/1000, 'unixepoch','localtime')) AS int) DESC) as t3 ) as t1 GROUP BY day, month, year ORDER BY year DESC, month DESC, day DESC", 12);
        long j = accountId;
        acquire.bindLong(1, j);
        long j2 = walletId;
        acquire.bindLong(2, j2);
        acquire.bindLong(3, startDate);
        acquire.bindLong(4, endDate);
        acquire.bindLong(5, j);
        acquire.bindLong(6, j2);
        acquire.bindLong(7, startDate);
        acquire.bindLong(8, endDate);
        acquire.bindLong(9, j);
        acquire.bindLong(10, j2);
        acquire.bindLong(11, startDate);
        acquire.bindLong(12, endDate);
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

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public List<Trans> getOverviewTransFromDate(final int accountId, final int walletId, final long startDate, final long endDate) {
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
        int i;
        int i2;
        String str;
        String string2;
        int i3;
        String string3;
        int i4;
        String string4;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM(SELECT * FROM (SELECT d.id as debtId, d.color as debtColor,d.type as debtType,dt.id as debtTransId, dt.type as debtTransType,t.id,t.note, t.memo,t.trans_amount as transAmount,c.icon,c.color,c.default_category as categoryDefault,t.fee_id as feeId, t.subcategory_id as subcategoryId, sc.name as subcategory,cu.code as currency,t.amount,t.date_time as dateTime,w.name as wallet, tw.name as transferWallet,t.type , t.wallet_id as walletId, t.transfer_wallet_id as transferWalletId, c.name as category, c.id as categoryId, GROUP_CONCAT(m.path) as media FROM trans t LEFT JOIN category as c ON t.category_id = c.id LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as cu ON w.currency = cu.code LEFT JOIN wallet as tw ON t.transfer_wallet_id = tw.id LEFT JOIN media as m ON m.trans_id = t.id LEFT JOIN debt as d ON t.debt_id = d.id LEFT JOIN debtTrans as dt ON t.debt_trans_id = dt.id LEFT JOIN subcategory as sc ON t.subcategory_id = sc.id WHERE t.date_time >= ? AND t.date_time < ? AND t.account_id = ? AND t.wallet_id = ? GROUP BY t.id ) as t1 UNION ALL SELECT * FROM (SELECT d.id as debtId, d.color as debtColor,d.type as debtType,dt.id as debtTransId, dt.type as debtTransType,t.id,t.note, t.memo,t.trans_amount as transAmount,c.icon,c.color,c.default_category as categoryDefault,t.fee_id as feeId, t.subcategory_id as subcategoryId, sc.name as subcategory,cu.code as currency,t.amount,t.date_time as dateTime,w.name as wallet, tw.name as transferWallet,t.type , t.wallet_id as walletId, t.transfer_wallet_id as transferWalletId, c.name as category, c.id as categoryId, GROUP_CONCAT(m.path) as media FROM trans t LEFT JOIN category as c ON t.category_id = c.id LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as cu ON w.currency = cu.code LEFT JOIN wallet as tw ON t.transfer_wallet_id = tw.id LEFT JOIN media as m ON m.trans_id = t.id LEFT JOIN debt as d ON t.debt_id = d.id LEFT JOIN debtTrans as dt ON t.debt_trans_id = dt.id LEFT JOIN subcategory as sc ON t.subcategory_id = sc.id WHERE t.date_time >= ? AND t.date_time < ? AND t.account_id = ? AND t.transfer_wallet_id = ? AND t.type = 2  GROUP BY t.id) as t2) as t1 ORDER BY dateTime DESC", 8);
        acquire.bindLong(1, startDate);
        acquire.bindLong(2, endDate);
        long j = accountId;
        acquire.bindLong(3, j);
        long j2 = walletId;
        acquire.bindLong(4, j2);
        acquire.bindLong(5, startDate);
        acquire.bindLong(6, endDate);
        acquire.bindLong(7, j);
        acquire.bindLong(8, j2);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "debtId");
            columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "debtColor");
            columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "debtType");
            columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "debtTransId");
            columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "debtTransType");
            columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "id");
            columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "note");
            columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "memo");
            columnIndexOrThrow9 = CursorUtil.getColumnIndexOrThrow(query, "transAmount");
            columnIndexOrThrow10 = CursorUtil.getColumnIndexOrThrow(query, "icon");
            columnIndexOrThrow11 = CursorUtil.getColumnIndexOrThrow(query, TypedValues.Custom.S_COLOR);
            columnIndexOrThrow12 = CursorUtil.getColumnIndexOrThrow(query, "categoryDefault");
            columnIndexOrThrow13 = CursorUtil.getColumnIndexOrThrow(query, "feeId");
            columnIndexOrThrow14 = CursorUtil.getColumnIndexOrThrow(query, "subcategoryId");
            roomSQLiteQuery = acquire;
        } catch (Throwable th) {
            th = th;
            roomSQLiteQuery = acquire;
        }
        try {
            int columnIndexOrThrow15 = CursorUtil.getColumnIndexOrThrow(query, "subcategory");
            int i5 = columnIndexOrThrow6;
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
            int i6 = columnIndexOrThrow15;
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                int i7 = query.getInt(columnIndexOrThrow);
                String string5 = query.isNull(columnIndexOrThrow2) ? null : query.getString(columnIndexOrThrow2);
                int i8 = query.getInt(columnIndexOrThrow3);
                int i9 = query.getInt(columnIndexOrThrow4);
                int i10 = query.getInt(columnIndexOrThrow5);
                String string6 = query.isNull(columnIndexOrThrow7) ? null : query.getString(columnIndexOrThrow7);
                String string7 = query.isNull(columnIndexOrThrow8) ? null : query.getString(columnIndexOrThrow8);
                long j3 = query.getLong(columnIndexOrThrow9);
                int i11 = query.getInt(columnIndexOrThrow10);
                String string8 = query.isNull(columnIndexOrThrow11) ? null : query.getString(columnIndexOrThrow11);
                int i12 = query.getInt(columnIndexOrThrow12);
                int i13 = query.getInt(columnIndexOrThrow13);
                int i14 = query.getInt(columnIndexOrThrow14);
                int i15 = i6;
                String string9 = query.isNull(i15) ? null : query.getString(i15);
                int i16 = columnIndexOrThrow;
                int i17 = columnIndexOrThrow16;
                if (query.isNull(i17)) {
                    i = i17;
                    string = null;
                } else {
                    string = query.getString(i17);
                    i = i17;
                }
                int i18 = columnIndexOrThrow17;
                long j4 = query.getLong(i18);
                columnIndexOrThrow17 = i18;
                int i19 = columnIndexOrThrow18;
                Date date = DateConverter.toDate(query.isNull(i19) ? null : Long.valueOf(query.getLong(i19)));
                columnIndexOrThrow18 = i19;
                int i20 = columnIndexOrThrow19;
                if (query.isNull(i20)) {
                    columnIndexOrThrow19 = i20;
                    i2 = columnIndexOrThrow20;
                    str = null;
                } else {
                    String string10 = query.getString(i20);
                    columnIndexOrThrow19 = i20;
                    i2 = columnIndexOrThrow20;
                    str = string10;
                }
                if (query.isNull(i2)) {
                    columnIndexOrThrow20 = i2;
                    i3 = columnIndexOrThrow21;
                    string2 = null;
                } else {
                    string2 = query.getString(i2);
                    columnIndexOrThrow20 = i2;
                    i3 = columnIndexOrThrow21;
                }
                int i21 = query.getInt(i3);
                columnIndexOrThrow21 = i3;
                int i22 = columnIndexOrThrow22;
                int i23 = query.getInt(i22);
                columnIndexOrThrow22 = i22;
                int i24 = columnIndexOrThrow23;
                int i25 = query.getInt(i24);
                columnIndexOrThrow23 = i24;
                int i26 = columnIndexOrThrow24;
                if (query.isNull(i26)) {
                    columnIndexOrThrow24 = i26;
                    i4 = columnIndexOrThrow25;
                    string3 = null;
                } else {
                    string3 = query.getString(i26);
                    columnIndexOrThrow24 = i26;
                    i4 = columnIndexOrThrow25;
                }
                int i27 = query.getInt(i4);
                columnIndexOrThrow25 = i4;
                int i28 = columnIndexOrThrow26;
                if (query.isNull(i28)) {
                    columnIndexOrThrow26 = i28;
                    string4 = null;
                } else {
                    string4 = query.getString(i28);
                    columnIndexOrThrow26 = i28;
                }
                Trans trans = new Trans(string6, string7, string8, i11, string, date, j4, str, i21, string2, i23, i25, string3, i27, i12, i13, string4, j3, string5, i7, i8, i9, i10, string9, i14);
                int i29 = columnIndexOrThrow14;
                int i30 = i5;
                int i31 = columnIndexOrThrow13;
                trans.setId(query.getInt(i30));
                arrayList.add(trans);
                columnIndexOrThrow13 = i31;
                columnIndexOrThrow = i16;
                i5 = i30;
                columnIndexOrThrow16 = i;
                columnIndexOrThrow14 = i29;
                i6 = i15;
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

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public List<Stats> getAllExpensePieStats(final int walletId) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT e.*, CAST(printf('%.1f', ((e.amount*1.0)*100/(SELECT SUM(amount)*1.0 FROM (SELECT SUM(amount) as amount FROM trans WHERE wallet_id = ? AND type = 1 UNION ALL SELECT SUM(-amount) as amount FROM trans WHERE wallet_id = ? AND type = 2)))) AS double) as percent FROM (SELECT c.name,c.default_category as categoryDefault,c.color,c.icon,c.id,SUM(ROUND(t.amount)) as amount, COUNT(t.id) as trans FROM trans as t LEFT JOIN category as c ON t.category_id = c.id WHERE t.wallet_id = ? AND t.type = 1 GROUP BY c.id UNION ALL SELECT \"\" as name,0 as categoryDefault,\"\" as color,0 as icon,0 as id,SUM(ROUND(-amount)) as amount, COUNT(id) as trans FROM trans WHERE wallet_id = ? AND type = 2 GROUP BY wallet_id) as e ORDER BY percent DESC", 4);
        long j = walletId;
        acquire.bindLong(1, j);
        acquire.bindLong(2, j);
        acquire.bindLong(3, j);
        acquire.bindLong(4, j);
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

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public List<Stats> getAllIncomePieStats(final int walletId) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT e.*, CAST(printf('%.1f', ((e.amount*1.0)*100/(SELECT SUM(amount)*1.0 FROM (SELECT SUM(amount) as amount FROM trans WHERE wallet_id = ? AND type = 0 UNION ALL SELECT SUM(trans_amount) as amount FROM trans WHERE transfer_wallet_id = ? AND type = 2)))) AS double) as percent FROM (SELECT c.name,c.default_category as categoryDefault,c.color,c.icon,c.id,SUM(ROUND(t.amount)) as amount, COUNT(t.id) as trans FROM trans as t LEFT JOIN category as c ON t.category_id = c.id WHERE t.wallet_id = ? AND t.type = 0 GROUP BY c.id UNION ALL SELECT \"\" as name,0 as categoryDefault,\"\" as color,0 as icon,0 as id,SUM(ROUND(trans_amount)) as amount, COUNT(id) as trans FROM trans WHERE transfer_wallet_id = ? AND type = 2 GROUP BY wallet_id) as e ORDER BY percent DESC", 4);
        long j = walletId;
        acquire.bindLong(1, j);
        acquire.bindLong(2, j);
        acquire.bindLong(3, j);
        acquire.bindLong(4, j);
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

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public List<Stats> getExpensePieStats(final int walletId, final long startDate, final long endDate) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT e.*,  CAST(printf('%.1f', ((e.amount*1.0)*100/(SELECT SUM(amount)*1.0 FROM (SELECT SUM(amount) as amount FROM trans WHERE wallet_id = ? AND date_time >= ? AND date_time < ? AND type = 1 UNION ALL SELECT SUM(-amount) as amount FROM trans WHERE wallet_id = ? AND date_time >= ? AND date_time < ? AND type = 2)))) AS double) as percent FROM (SELECT c.name,c.default_category as categoryDefault,c.color,c.icon,c.id,SUM(ROUND(t.amount)) as amount, COUNT(t.id) as trans FROM trans as t LEFT JOIN category as c ON t.category_id = c.id WHERE t.wallet_id = ? AND t.date_time >= ? AND t.date_time < ? AND t.type = 1 GROUP BY c.id UNION ALL SELECT \"\" as name,0 as categoryDefault,\"\" as color,0 as icon,0 as id,SUM(ROUND(-amount)) as amount, COUNT(id) as trans FROM trans WHERE wallet_id = ? AND date_time >= ? AND date_time < ? AND type = 2 GROUP BY wallet_id) as e ORDER BY percent DESC", 12);
        long j = walletId;
        acquire.bindLong(1, j);
        acquire.bindLong(2, startDate);
        acquire.bindLong(3, endDate);
        acquire.bindLong(4, j);
        acquire.bindLong(5, startDate);
        acquire.bindLong(6, endDate);
        acquire.bindLong(7, j);
        acquire.bindLong(8, startDate);
        acquire.bindLong(9, endDate);
        acquire.bindLong(10, j);
        acquire.bindLong(11, startDate);
        acquire.bindLong(12, endDate);
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

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public List<Stats> getIncomePieStats(final int walletId, final long startDate, final long endDate) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT e.*,  CAST(printf('%.1f', ((e.amount*1.0)*100/(SELECT SUM(amount)*1.0 FROM (SELECT SUM(amount) as amount FROM trans WHERE wallet_id = ? AND date_time >= ? AND date_time < ? AND type = 0 UNION ALL SELECT SUM(trans_amount) as amount FROM trans WHERE transfer_wallet_id = ? AND date_time >= ? AND date_time < ? AND type = 2)))) AS double) as percent FROM (SELECT c.name,c.default_category as categoryDefault,c.color,c.icon,c.id,SUM(ROUND(t.amount)) as amount, COUNT(t.id) as trans FROM trans as t LEFT JOIN category as c ON t.category_id = c.id WHERE t.wallet_id = ? AND t.date_time >= ? AND t.date_time < ? AND t.type = 0 GROUP BY c.id UNION ALL SELECT \"\" as name,0 as categoryDefault,\"\" as color,0 as icon,0 as id,SUM(ROUND(trans_amount)) as amount, COUNT(id) as trans FROM trans WHERE transfer_wallet_id = ? AND date_time >= ? AND date_time < ? AND type = 2 GROUP BY wallet_id) as e ORDER BY percent DESC", 12);
        long j = walletId;
        acquire.bindLong(1, j);
        acquire.bindLong(2, startDate);
        acquire.bindLong(3, endDate);
        acquire.bindLong(4, j);
        acquire.bindLong(5, startDate);
        acquire.bindLong(6, endDate);
        acquire.bindLong(7, j);
        acquire.bindLong(8, startDate);
        acquire.bindLong(9, endDate);
        acquire.bindLong(10, j);
        acquire.bindLong(11, startDate);
        acquire.bindLong(12, endDate);
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

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public List<DailyTrans> getAllPieTrans(final int accountId, final int walletId, final int categoryId, final int type) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT SUM(amount) as amount, day, month, year FROM (SELECT * FROM (SELECT SUM(t.amount) as amount, CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as day,CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as month,CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as year FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id WHERE t.account_id = ? AND t.type = ? AND t.wallet_id = ? AND w.account_id = ? AND t.category_id = ? GROUP BY CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) ORDER BY CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC) as t1 ) as t1 GROUP BY day, month, year ORDER BY year DESC,month DESC,day DESC", 5);
        long j = accountId;
        acquire.bindLong(1, j);
        acquire.bindLong(2, type);
        acquire.bindLong(3, walletId);
        acquire.bindLong(4, j);
        acquire.bindLong(5, categoryId);
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

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public List<DailyTrans> getPieTrans(final int accountId, final int walletId, final int categoryId, final long startDate, final long endDate, final int type) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT SUM(amount) as amount, day, month, year FROM (SELECT * FROM (SELECT SUM(t.amount) as amount, CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as day,CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as month,CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as year FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id WHERE t.account_id = ? AND t.type = ? AND t.wallet_id = ? AND t.date_time >= ? AND t.date_time < ? AND w.account_id = ? AND t.category_id = ? GROUP BY CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) ORDER BY CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC) as t1 ) as t1 GROUP BY day, month, year ORDER BY year DESC,month DESC,day DESC", 7);
        long j = accountId;
        acquire.bindLong(1, j);
        acquire.bindLong(2, type);
        acquire.bindLong(3, walletId);
        acquire.bindLong(4, startDate);
        acquire.bindLong(5, endDate);
        acquire.bindLong(6, j);
        acquire.bindLong(7, categoryId);
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

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public List<Trans> getPieTransFromDate(final int accountId, final int walletId, final int categoryId, final long startDate, final long endDate, final int type) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT d.id as debtId, d.color as debtColor,d.type as debtType,dt.id as debtTransId, dt.type as debtTransType,t.id,t.note, t.memo,t.trans_amount as transAmount,c.icon,c.color,c.default_category as categoryDefault,t.fee_id as feeId, t.subcategory_id as subcategoryId, sc.name as subcategory,cu.code as currency,t.amount,t.date_time as dateTime,w.name as wallet, tw.name as transferWallet,t.type , t.wallet_id as walletId, t.transfer_wallet_id as transferWalletId, c.name as category, c.id as categoryId, GROUP_CONCAT(m.path) as media FROM trans t LEFT JOIN category as c ON t.category_id = c.id LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as cu ON w.currency = cu.code LEFT JOIN wallet as tw ON t.transfer_wallet_id = tw.id LEFT JOIN media as m ON m.trans_id = t.id LEFT JOIN debt as d ON t.debt_id = d.id LEFT JOIN debtTrans as dt ON t.debt_trans_id = dt.id LEFT JOIN subcategory as sc ON t.subcategory_id = sc.id WHERE t.date_time >= ? AND t.date_time < ? AND t.account_id = ? AND cu.account_id = ? AND t.wallet_id = ? AND t.category_id = ? AND t.type = ? GROUP BY t.id ORDER BY t.date_time DESC", 7);
        acquire.bindLong(1, startDate);
        acquire.bindLong(2, endDate);
        long j = accountId;
        acquire.bindLong(3, j);
        acquire.bindLong(4, j);
        acquire.bindLong(5, walletId);
        acquire.bindLong(6, categoryId);
        acquire.bindLong(7, type);
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
                int i7 = query.getInt(12);
                int i8 = query.getInt(13);
                String string5 = query.isNull(14) ? null : query.getString(14);
                Trans trans = new Trans(string2, string3, string4, i5, query.isNull(15) ? null : query.getString(15), DateConverter.toDate(query.isNull(17) ? null : Long.valueOf(query.getLong(17))), query.getLong(16), query.isNull(18) ? null : query.getString(18), query.getInt(20), query.isNull(19) ? null : query.getString(19), query.getInt(21), query.getInt(22), query.isNull(23) ? null : query.getString(23), query.getInt(24), i6, i7, query.isNull(25) ? null : query.getString(25), j2, string, i, i2, i3, i4, string5, i8);
                trans.setId(query.getInt(5));
                arrayList.add(trans);
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public List<DailyTrans> getAllPieExpenseTransferTrans(final int accountId, final int walletId) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT SUM(amount) as amount, day, month, year FROM (SELECT * FROM (SELECT SUM(-t.amount) as amount, CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as day,CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as month,CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as year FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id WHERE t.account_id = ? AND t.type = 2 AND t.wallet_id = ? AND w.account_id = ? GROUP BY CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) ORDER BY CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC) as t1 ) as t1 GROUP BY day, month, year ORDER BY year DESC,month DESC,day DESC", 3);
        long j = accountId;
        acquire.bindLong(1, j);
        acquire.bindLong(2, walletId);
        acquire.bindLong(3, j);
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

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public List<DailyTrans> getAllPieIncomeTransferTrans(final int accountId, final int walletId) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT SUM(amount) as amount, day, month, year FROM (SELECT * FROM (SELECT SUM(t.trans_amount) as amount, CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as day,CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as month,CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as year FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id WHERE t.account_id = ? AND t.type = 2 AND t.transfer_wallet_id = ? AND w.account_id = ? GROUP BY CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) ORDER BY CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC) as t1 ) as t1 GROUP BY day, month, year ORDER BY year DESC,month DESC,day DESC", 3);
        long j = accountId;
        acquire.bindLong(1, j);
        acquire.bindLong(2, walletId);
        acquire.bindLong(3, j);
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

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public List<DailyTrans> getPieExpenseTransferTrans(final int accountId, final int walletId, final long startDate, final long endDate) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT SUM(amount) as amount, day, month, year FROM (SELECT * FROM (SELECT SUM(-t.amount) as amount, CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as day,CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as month,CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as year FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id WHERE t.account_id = ? AND t.type = 2 AND t.wallet_id = ? AND t.date_time >= ? AND t.date_time < ? AND w.account_id = ? GROUP BY CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) ORDER BY CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC) as t1 ) as t1 GROUP BY day, month, year ORDER BY year DESC,month DESC,day DESC", 5);
        long j = accountId;
        acquire.bindLong(1, j);
        acquire.bindLong(2, walletId);
        acquire.bindLong(3, startDate);
        acquire.bindLong(4, endDate);
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

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public List<DailyTrans> getPieIncomeTransferTrans(final int accountId, final int walletId, final long startDate, final long endDate) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT SUM(amount) as amount, day, month, year FROM (SELECT * FROM (SELECT SUM(t.trans_amount) as amount, CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as day,CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as month,CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as year FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id WHERE t.account_id = ? AND t.type = 2 AND t.transfer_wallet_id = ? AND t.date_time >= ? AND t.date_time < ? AND w.account_id = ? GROUP BY CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) ORDER BY CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC) as t1 ) as t1 GROUP BY day, month, year ORDER BY year DESC,month DESC,day DESC", 5);
        long j = accountId;
        acquire.bindLong(1, j);
        acquire.bindLong(2, walletId);
        acquire.bindLong(3, startDate);
        acquire.bindLong(4, endDate);
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

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public List<Trans> getPieExpenseTransferTransFromDate(final int accountId, final int walletId, final long startDate, final long endDate) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT d.id as debtId, d.color as debtColor,d.type as debtType,dt.id as debtTransId, dt.type as debtTransType,t.id,t.note, t.memo,t.trans_amount as transAmount,c.icon,c.color,c.default_category as categoryDefault,t.fee_id as feeId, t.subcategory_id as subcategoryId, sc.name as subcategory,cu.code as currency,-t.amount as amount,t.date_time as dateTime,w.name as wallet, tw.name as transferWallet,t.type , t.wallet_id as walletId, t.transfer_wallet_id as transferWalletId, c.name as category, c.id as categoryId, GROUP_CONCAT(m.path) as media FROM trans t LEFT JOIN category as c ON t.category_id = c.id LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as cu ON w.currency = cu.code LEFT JOIN wallet as tw ON t.transfer_wallet_id = tw.id LEFT JOIN media as m ON m.trans_id = t.id LEFT JOIN debt as d ON t.debt_id = d.id LEFT JOIN debtTrans as dt ON t.debt_trans_id = dt.id LEFT JOIN subcategory as sc ON t.subcategory_id = sc.id WHERE t.date_time >= ? AND t.date_time < ? AND t.account_id = ? AND cu.account_id = ? AND t.wallet_id = ? AND t.type = 2 GROUP BY t.id ORDER BY t.date_time DESC", 5);
        acquire.bindLong(1, startDate);
        acquire.bindLong(2, endDate);
        long j = accountId;
        acquire.bindLong(3, j);
        acquire.bindLong(4, j);
        acquire.bindLong(5, walletId);
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
                int i7 = query.getInt(12);
                int i8 = query.getInt(13);
                String string5 = query.isNull(14) ? null : query.getString(14);
                Trans trans = new Trans(string2, string3, string4, i5, query.isNull(15) ? null : query.getString(15), DateConverter.toDate(query.isNull(17) ? null : Long.valueOf(query.getLong(17))), query.getLong(16), query.isNull(18) ? null : query.getString(18), query.getInt(20), query.isNull(19) ? null : query.getString(19), query.getInt(21), query.getInt(22), query.isNull(23) ? null : query.getString(23), query.getInt(24), i6, i7, query.isNull(25) ? null : query.getString(25), j2, string, i, i2, i3, i4, string5, i8);
                trans.setId(query.getInt(5));
                arrayList.add(trans);
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.WalletDaoObject
    public List<Trans> getPieIncomeTransferTransFromDate(final int accountId, final int walletId, final long startDate, final long endDate) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT d.id as debtId, d.color as debtColor,d.type as debtType,dt.id as debtTransId, dt.type as debtTransType,t.id,t.note, t.memo,t.trans_amount as transAmount,c.icon,c.color,c.default_category as categoryDefault,t.fee_id as feeId, t.subcategory_id as subcategoryId, sc.name as subcategory,cu.code as currency,t.amount as amount,t.date_time as dateTime,w.name as wallet, tw.name as transferWallet,t.type , t.wallet_id as walletId, t.transfer_wallet_id as transferWalletId, c.name as category, c.id as categoryId, GROUP_CONCAT(m.path) as media FROM trans t LEFT JOIN category as c ON t.category_id = c.id LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as cu ON w.currency = cu.code LEFT JOIN wallet as tw ON t.transfer_wallet_id = tw.id LEFT JOIN media as m ON m.trans_id = t.id LEFT JOIN debt as d ON t.debt_id = d.id LEFT JOIN debtTrans as dt ON t.debt_trans_id = dt.id LEFT JOIN subcategory as sc ON t.subcategory_id = sc.id WHERE t.date_time >= ? AND t.date_time < ? AND t.account_id = ? AND cu.account_id = ? AND t.transfer_wallet_id = ? AND t.type = 2 GROUP BY t.id ORDER BY t.date_time DESC", 5);
        acquire.bindLong(1, startDate);
        acquire.bindLong(2, endDate);
        long j = accountId;
        acquire.bindLong(3, j);
        acquire.bindLong(4, j);
        acquire.bindLong(5, walletId);
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
                int i7 = query.getInt(12);
                int i8 = query.getInt(13);
                String string5 = query.isNull(14) ? null : query.getString(14);
                Trans trans = new Trans(string2, string3, string4, i5, query.isNull(15) ? null : query.getString(15), DateConverter.toDate(query.isNull(17) ? null : Long.valueOf(query.getLong(17))), query.getLong(16), query.isNull(18) ? null : query.getString(18), query.getInt(20), query.isNull(19) ? null : query.getString(19), query.getInt(21), query.getInt(22), query.isNull(23) ? null : query.getString(23), query.getInt(24), i6, i7, query.isNull(25) ? null : query.getString(25), j2, string, i, i2, i3, i4, string5, i8);
                trans.setId(query.getInt(5));
                arrayList.add(trans);
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
