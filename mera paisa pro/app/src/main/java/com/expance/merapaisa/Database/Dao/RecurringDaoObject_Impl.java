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

import com.expance.merapaisa.Database.Converter.DateConverter;
import com.expance.merapaisa.Database.Entity.RecurringEntity;
import com.expance.merapaisa.Model.Recurring;

import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

/* loaded from: classes3.dex */
public final class RecurringDaoObject_Impl implements RecurringDaoObject {
    private final RoomDatabase __db;
    private final EntityInsertionAdapter<RecurringEntity> __insertionAdapterOfRecurringEntity;
    private final SharedSQLiteStatement __preparedStmtOfDeleteRecurring;
    private final SharedSQLiteStatement __preparedStmtOfRemoveSubcategory;
    private final SharedSQLiteStatement __preparedStmtOfUpdateRecurringUpdateTime;
    private final EntityDeletionOrUpdateAdapter<RecurringEntity> __updateAdapterOfRecurringEntity;

    public RecurringDaoObject_Impl(RoomDatabase __db) {
        this.__db = __db;
        this.__insertionAdapterOfRecurringEntity = new EntityInsertionAdapter<RecurringEntity>(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.RecurringDaoObject_Impl.1
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "INSERT OR ABORT INTO `recurring` (`id`,`note`,`memo`,`type`,`recurring_type`,`repeat_type`,`repeat_date`,`increment`,`amount`,`date_time`,`until_time`,`last_update_time`,`account_id`,`category_id`,`wallet_id`,`subcategory_id`,`transfer_wallet_id`,`trans_amount`,`is_future`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            }

            @Override // androidx.room.EntityInsertionAdapter
            public void bind(SupportSQLiteStatement stmt, RecurringEntity value) {
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
                stmt.bindLong(5, value.getRecurringType());
                stmt.bindLong(6, value.getRepeatType());
                if (value.getRepeatDate() == null) {
                    stmt.bindNull(7);
                } else {
                    stmt.bindString(7, value.getRepeatDate());
                }
                stmt.bindLong(8, value.getIncrement());
                stmt.bindLong(9, value.getAmount());
                Long fromDate = DateConverter.fromDate(value.getDateTime());
                if (fromDate == null) {
                    stmt.bindNull(10);
                } else {
                    stmt.bindLong(10, fromDate.longValue());
                }
                Long fromDate2 = DateConverter.fromDate(value.getUntilTime());
                if (fromDate2 == null) {
                    stmt.bindNull(11);
                } else {
                    stmt.bindLong(11, fromDate2.longValue());
                }
                Long fromDate3 = DateConverter.fromDate(value.getLastUpdateTime());
                if (fromDate3 == null) {
                    stmt.bindNull(12);
                } else {
                    stmt.bindLong(12, fromDate3.longValue());
                }
                stmt.bindLong(13, value.getAccountId());
                stmt.bindLong(14, value.getCategoryId());
                stmt.bindLong(15, value.getWalletId());
                stmt.bindLong(16, value.getSubcategoryId());
                stmt.bindLong(17, value.getTransferWalletId());
                stmt.bindLong(18, value.getTransAmount());
                stmt.bindLong(19, value.getIsFuture());
            }
        };
        this.__updateAdapterOfRecurringEntity = new EntityDeletionOrUpdateAdapter<RecurringEntity>(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.RecurringDaoObject_Impl.2
            @Override
            // androidx.room.EntityDeletionOrUpdateAdapter, androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "UPDATE OR ABORT `recurring` SET `id` = ?,`note` = ?,`memo` = ?,`type` = ?,`recurring_type` = ?,`repeat_type` = ?,`repeat_date` = ?,`increment` = ?,`amount` = ?,`date_time` = ?,`until_time` = ?,`last_update_time` = ?,`account_id` = ?,`category_id` = ?,`wallet_id` = ?,`subcategory_id` = ?,`transfer_wallet_id` = ?,`trans_amount` = ?,`is_future` = ? WHERE `id` = ?";
            }

            @Override // androidx.room.EntityDeletionOrUpdateAdapter
            public void bind(SupportSQLiteStatement stmt, RecurringEntity value) {
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
                stmt.bindLong(5, value.getRecurringType());
                stmt.bindLong(6, value.getRepeatType());
                if (value.getRepeatDate() == null) {
                    stmt.bindNull(7);
                } else {
                    stmt.bindString(7, value.getRepeatDate());
                }
                stmt.bindLong(8, value.getIncrement());
                stmt.bindLong(9, value.getAmount());
                Long fromDate = DateConverter.fromDate(value.getDateTime());
                if (fromDate == null) {
                    stmt.bindNull(10);
                } else {
                    stmt.bindLong(10, fromDate.longValue());
                }
                Long fromDate2 = DateConverter.fromDate(value.getUntilTime());
                if (fromDate2 == null) {
                    stmt.bindNull(11);
                } else {
                    stmt.bindLong(11, fromDate2.longValue());
                }
                Long fromDate3 = DateConverter.fromDate(value.getLastUpdateTime());
                if (fromDate3 == null) {
                    stmt.bindNull(12);
                } else {
                    stmt.bindLong(12, fromDate3.longValue());
                }
                stmt.bindLong(13, value.getAccountId());
                stmt.bindLong(14, value.getCategoryId());
                stmt.bindLong(15, value.getWalletId());
                stmt.bindLong(16, value.getSubcategoryId());
                stmt.bindLong(17, value.getTransferWalletId());
                stmt.bindLong(18, value.getTransAmount());
                stmt.bindLong(19, value.getIsFuture());
                stmt.bindLong(20, value.getId());
            }
        };
        this.__preparedStmtOfDeleteRecurring = new SharedSQLiteStatement(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.RecurringDaoObject_Impl.3
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "DELETE FROM recurring WHERE id = ?";
            }
        };
        this.__preparedStmtOfUpdateRecurringUpdateTime = new SharedSQLiteStatement(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.RecurringDaoObject_Impl.4
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "UPDATE recurring SET last_update_time = ? WHERE id = ?";
            }
        };
        this.__preparedStmtOfRemoveSubcategory = new SharedSQLiteStatement(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.RecurringDaoObject_Impl.5
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "UPDATE recurring SET subcategory_id = 0 WHERE subcategory_id = ?";
            }
        };
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.RecurringDaoObject
    public void insetRecurring(final RecurringEntity recurringEntity) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfRecurringEntity.insert(recurringEntity);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.RecurringDaoObject
    public void updateRecurring(final RecurringEntity recurringEntity) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfRecurringEntity.handle(recurringEntity);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.RecurringDaoObject
    public void deleteRecurring(final int id) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfDeleteRecurring.acquire();
        acquire.bindLong(1, id);
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfDeleteRecurring.release(acquire);
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.RecurringDaoObject
    public void updateRecurringUpdateTime(final long lastUpdateTime, final int id) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfUpdateRecurringUpdateTime.acquire();
        acquire.bindLong(1, lastUpdateTime);
        acquire.bindLong(2, id);
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfUpdateRecurringUpdateTime.release(acquire);
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.RecurringDaoObject
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

    @Override // com.ktwapps.walletmanager.Database.Dao.RecurringDaoObject
    public RecurringEntity getRecurringEntity(final int id) {
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
        RecurringEntity recurringEntity;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM recurring WHERE id = ?", 1);
        acquire.bindLong(1, id);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
            columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "note");
            columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "memo");
            columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, JamXmlElements.TYPE);
            columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "recurring_type");
            columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "repeat_type");
            columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "repeat_date");
            columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "increment");
            columnIndexOrThrow9 = CursorUtil.getColumnIndexOrThrow(query, "amount");
            columnIndexOrThrow10 = CursorUtil.getColumnIndexOrThrow(query, "date_time");
            columnIndexOrThrow11 = CursorUtil.getColumnIndexOrThrow(query, "until_time");
            columnIndexOrThrow12 = CursorUtil.getColumnIndexOrThrow(query, "last_update_time");
            columnIndexOrThrow13 = CursorUtil.getColumnIndexOrThrow(query, "account_id");
            columnIndexOrThrow14 = CursorUtil.getColumnIndexOrThrow(query, "category_id");
            roomSQLiteQuery = acquire;
        } catch (Throwable th) {
            th = th;
            roomSQLiteQuery = acquire;
        }
        try {
            int columnIndexOrThrow15 = CursorUtil.getColumnIndexOrThrow(query, "wallet_id");
            int columnIndexOrThrow16 = CursorUtil.getColumnIndexOrThrow(query, "subcategory_id");
            int columnIndexOrThrow17 = CursorUtil.getColumnIndexOrThrow(query, "transfer_wallet_id");
            int columnIndexOrThrow18 = CursorUtil.getColumnIndexOrThrow(query, "trans_amount");
            int columnIndexOrThrow19 = CursorUtil.getColumnIndexOrThrow(query, "is_future");
            if (query.moveToFirst()) {
                recurringEntity = new RecurringEntity(query.isNull(columnIndexOrThrow2) ? null : query.getString(columnIndexOrThrow2), query.isNull(columnIndexOrThrow3) ? null : query.getString(columnIndexOrThrow3), query.getInt(columnIndexOrThrow4), query.getInt(columnIndexOrThrow5), query.getInt(columnIndexOrThrow6), query.isNull(columnIndexOrThrow7) ? null : query.getString(columnIndexOrThrow7), query.getInt(columnIndexOrThrow8), query.getLong(columnIndexOrThrow9), DateConverter.toDate(query.isNull(columnIndexOrThrow10) ? null : Long.valueOf(query.getLong(columnIndexOrThrow10))), DateConverter.toDate(query.isNull(columnIndexOrThrow11) ? null : Long.valueOf(query.getLong(columnIndexOrThrow11))), DateConverter.toDate(query.isNull(columnIndexOrThrow12) ? null : Long.valueOf(query.getLong(columnIndexOrThrow12))), query.getInt(columnIndexOrThrow13), query.getInt(columnIndexOrThrow14), query.getInt(columnIndexOrThrow15), query.getInt(columnIndexOrThrow17), query.getLong(columnIndexOrThrow18), query.getInt(columnIndexOrThrow19), query.getInt(columnIndexOrThrow16));
                recurringEntity.setId(query.getInt(columnIndexOrThrow));
            } else {
                recurringEntity = null;
            }
            query.close();
            roomSQLiteQuery.release();
            return recurringEntity;
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

    @Override // com.ktwapps.walletmanager.Database.Dao.RecurringDaoObject
    public LiveData<List<Recurring>> getLiveRecurringList(final int account_id) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT r.account_id as accountId, r.is_future as isFuture, r.id,c.color,r.note,r.memo,r.date_time as dateTime,r.last_update_time as lastUpdateTime, r.until_time as untilTime, r.amount, r.increment, r.type, r.recurring_type as recurringType, r.repeat_type as repeatType, r.repeat_date as repeatDate, r.wallet_id as walletId, w.name as wallet, c.name as category,c.default_category as categoryDefault, r.category_id as categoryId, r.subcategory_id as subcategoryId, sc.name as subcategory,w.currency as currency, c.icon as icon FROM recurring as r LEFT JOIN wallet as w ON r.wallet_id = w.id LEFT JOIN category as c ON r.category_id = c.id LEFT JOIN subcategory as sc ON r.subcategory_id = sc.id WHERE r.account_id = ?", 1);
        acquire.bindLong(1, account_id);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"recurring", "wallet", "category", "subcategory"}, false, new Callable<List<Recurring>>() { // from class: com.ktwapps.walletmanager.Database.Dao.RecurringDaoObject_Impl.6
            @Override // java.util.concurrent.Callable
            public List<Recurring> call() throws Exception {
                Cursor query = DBUtil.query(RecurringDaoObject_Impl.this.__db, acquire, false, null);
                try {
                    ArrayList arrayList = new ArrayList(query.getCount());
                    while (query.moveToNext()) {
                        int i = query.getInt(0);
                        int i2 = query.getInt(1);
                        String string = query.isNull(3) ? null : query.getString(3);
                        String string2 = query.isNull(4) ? null : query.getString(4);
                        String string3 = query.isNull(5) ? null : query.getString(5);
                        Date date = DateConverter.toDate(query.isNull(6) ? null : Long.valueOf(query.getLong(6)));
                        Date date2 = DateConverter.toDate(query.isNull(7) ? null : Long.valueOf(query.getLong(7)));
                        Date date3 = DateConverter.toDate(query.isNull(8) ? null : Long.valueOf(query.getLong(8)));
                        long j = query.getLong(9);
                        int i3 = query.getInt(10);
                        int i4 = query.getInt(11);
                        int i5 = query.getInt(12);
                        int i6 = query.getInt(13);
                        String string4 = query.isNull(14) ? null : query.getString(14);
                        int i7 = query.getInt(15);
                        Recurring recurring = new Recurring(i, string, string2, string3, date, date3, date2, j, query.isNull(16) ? null : query.getString(16), i3, i4, i5, i6, string4, i7, query.isNull(17) ? null : query.getString(17), query.getInt(18), query.getInt(19), query.isNull(22) ? null : query.getString(22), query.getInt(23), i2, query.getInt(20), query.isNull(21) ? null : query.getString(21));
                        recurring.setId(query.getInt(2));
                        arrayList.add(recurring);
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

    @Override // com.ktwapps.walletmanager.Database.Dao.RecurringDaoObject
    public Recurring getRecurringById(final int account_id, final int id) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT r.account_id as accountId, r.is_future as isFuture,r.id,c.color,r.note,r.memo,r.date_time as dateTime,r.last_update_time as lastUpdateTime, r.until_time as untilTime, r.amount, r.increment, r.type, r.recurring_type as recurringType, r.repeat_type as repeatType, r.repeat_date as repeatDate, r.wallet_id as walletId, w.name as wallet, c.name as category,c.default_category as categoryDefault, r.category_id as categoryId, r.subcategory_id as subcategoryId, sc.name as subcategory, w.currency as currency, c.icon as icon FROM recurring as r LEFT JOIN wallet as w ON r.wallet_id = w.id LEFT JOIN category as c ON r.category_id = c.id LEFT JOIN subcategory as sc ON r.subcategory_id = sc.id WHERE r.account_id = ? AND r.id = ?", 2);
        acquire.bindLong(1, account_id);
        acquire.bindLong(2, id);
        this.__db.assertNotSuspendingTransaction();
        Recurring recurring = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            if (query.moveToFirst()) {
                int i = query.getInt(0);
                int i2 = query.getInt(1);
                String string = query.isNull(3) ? null : query.getString(3);
                String string2 = query.isNull(4) ? null : query.getString(4);
                String string3 = query.isNull(5) ? null : query.getString(5);
                Date date = DateConverter.toDate(query.isNull(6) ? null : Long.valueOf(query.getLong(6)));
                Date date2 = DateConverter.toDate(query.isNull(7) ? null : Long.valueOf(query.getLong(7)));
                Date date3 = DateConverter.toDate(query.isNull(8) ? null : Long.valueOf(query.getLong(8)));
                long j = query.getLong(9);
                int i3 = query.getInt(10);
                int i4 = query.getInt(11);
                int i5 = query.getInt(12);
                int i6 = query.getInt(13);
                String string4 = query.isNull(14) ? null : query.getString(14);
                int i7 = query.getInt(15);
                recurring = new Recurring(i, string, string2, string3, date, date3, date2, j, query.isNull(16) ? null : query.getString(16), i3, i4, i5, i6, string4, i7, query.isNull(17) ? null : query.getString(17), query.getInt(18), query.getInt(19), query.isNull(22) ? null : query.getString(22), query.getInt(23), i2, query.getInt(20), query.isNull(21) ? null : query.getString(21));
                recurring.setId(query.getInt(2));
            }
            return recurring;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.RecurringDaoObject
    public LiveData<Recurring> getLiveRecurringById(final int account_id, final int id) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT r.account_id as accountId, r.is_future as isFuture,r.id,c.color,r.note,r.memo,r.date_time as dateTime,r.last_update_time as lastUpdateTime, r.until_time as untilTime, r.amount, r.increment, r.type, r.recurring_type as recurringType, r.repeat_type as repeatType, r.repeat_date as repeatDate, r.wallet_id as walletId, w.name as wallet, c.name as category,c.default_category as categoryDefault, r.category_id as categoryId, r.subcategory_id as subcategoryId, sc.name as subcategory, w.currency as currency, c.icon as icon FROM recurring as r LEFT JOIN wallet as w ON r.wallet_id = w.id LEFT JOIN category as c ON r.category_id = c.id LEFT JOIN subcategory as sc ON r.subcategory_id = sc.id WHERE r.account_id = ? AND r.id = ?", 2);
        acquire.bindLong(1, account_id);
        acquire.bindLong(2, id);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"recurring", "wallet", "category", "subcategory"}, false, new Callable<Recurring>() { // from class: com.ktwapps.walletmanager.Database.Dao.RecurringDaoObject_Impl.7
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Recurring call() throws Exception {
                Recurring recurring = null;
                Cursor query = DBUtil.query(RecurringDaoObject_Impl.this.__db, acquire, false, null);
                try {
                    if (query.moveToFirst()) {
                        int i = query.getInt(0);
                        int i2 = query.getInt(1);
                        String string = query.isNull(3) ? null : query.getString(3);
                        String string2 = query.isNull(4) ? null : query.getString(4);
                        String string3 = query.isNull(5) ? null : query.getString(5);
                        Date date = DateConverter.toDate(query.isNull(6) ? null : Long.valueOf(query.getLong(6)));
                        Date date2 = DateConverter.toDate(query.isNull(7) ? null : Long.valueOf(query.getLong(7)));
                        Date date3 = DateConverter.toDate(query.isNull(8) ? null : Long.valueOf(query.getLong(8)));
                        long j = query.getLong(9);
                        int i3 = query.getInt(10);
                        int i4 = query.getInt(11);
                        int i5 = query.getInt(12);
                        int i6 = query.getInt(13);
                        String string4 = query.isNull(14) ? null : query.getString(14);
                        int i7 = query.getInt(15);
                        recurring = new Recurring(i, string, string2, string3, date, date3, date2, j, query.isNull(16) ? null : query.getString(16), i3, i4, i5, i6, string4, i7, query.isNull(17) ? null : query.getString(17), query.getInt(18), query.getInt(19), query.isNull(22) ? null : query.getString(22), query.getInt(23), i2, query.getInt(20), query.isNull(21) ? null : query.getString(21));
                        recurring.setId(query.getInt(2));
                    }
                    return recurring;
                } finally {
                    query.close();
                }
            }

            protected void finalize() {
                acquire.release();
            }
        });
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.RecurringDaoObject
    public List<Recurring> getAllRecurringListByAccountId(final int accountId) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT r.account_id as accountId, r.is_future as isFuture, r.id,c.color,r.note,r.memo,r.date_time as dateTime,r.last_update_time as lastUpdateTime, r.until_time as untilTime, r.amount, r.increment, r.type, r.recurring_type as recurringType, r.repeat_type as repeatType, r.repeat_date as repeatDate, r.wallet_id as walletId, w.name as wallet, c.name as category,c.default_category as categoryDefault, r.category_id as categoryId, r.subcategory_id as subcategoryId, sc.name as subcategory, w.currency as currency, c.icon as icon FROM recurring as r LEFT JOIN wallet as w ON r.wallet_id = w.id LEFT JOIN category as c ON r.category_id = c.id LEFT JOIN subcategory as sc ON r.subcategory_id = sc.id WHERE r.account_id = ?", 1);
        acquire.bindLong(1, accountId);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                int i = query.getInt(0);
                int i2 = query.getInt(1);
                String string = query.isNull(3) ? null : query.getString(3);
                String string2 = query.isNull(4) ? null : query.getString(4);
                String string3 = query.isNull(5) ? null : query.getString(5);
                Date date = DateConverter.toDate(query.isNull(6) ? null : Long.valueOf(query.getLong(6)));
                Date date2 = DateConverter.toDate(query.isNull(7) ? null : Long.valueOf(query.getLong(7)));
                Date date3 = DateConverter.toDate(query.isNull(8) ? null : Long.valueOf(query.getLong(8)));
                long j = query.getLong(9);
                int i3 = query.getInt(10);
                int i4 = query.getInt(11);
                int i5 = query.getInt(12);
                int i6 = query.getInt(13);
                String string4 = query.isNull(14) ? null : query.getString(14);
                int i7 = query.getInt(15);
                Recurring recurring = new Recurring(i, string, string2, string3, date, date3, date2, j, query.isNull(16) ? null : query.getString(16), i3, i4, i5, i6, string4, i7, query.isNull(17) ? null : query.getString(17), query.getInt(18), query.getInt(19), query.isNull(22) ? null : query.getString(22), query.getInt(23), i2, query.getInt(20), query.isNull(21) ? null : query.getString(21));
                recurring.setId(query.getInt(2));
                arrayList.add(recurring);
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.RecurringDaoObject
    public List<Recurring> getAllRecurringList() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT r.account_id as accountId, r.is_future as isFuture, r.id,c.color,r.note,r.memo,r.date_time as dateTime,r.last_update_time as lastUpdateTime, r.until_time as untilTime, r.amount, r.increment, r.type, r.recurring_type as recurringType, r.repeat_type as repeatType, r.repeat_date as repeatDate, r.wallet_id as walletId, w.name as wallet, c.name as category,c.default_category as categoryDefault, r.category_id as categoryId, r.subcategory_id as subcategoryId, sc.name as subcategory, w.currency as currency, c.icon as icon FROM recurring as r LEFT JOIN wallet as w ON r.wallet_id = w.id LEFT JOIN category as c ON r.category_id = c.id LEFT JOIN subcategory as sc ON r.subcategory_id = sc.id", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                int i = query.getInt(0);
                int i2 = query.getInt(1);
                String string = query.isNull(3) ? null : query.getString(3);
                String string2 = query.isNull(4) ? null : query.getString(4);
                String string3 = query.isNull(5) ? null : query.getString(5);
                Date date = DateConverter.toDate(query.isNull(6) ? null : Long.valueOf(query.getLong(6)));
                Date date2 = DateConverter.toDate(query.isNull(7) ? null : Long.valueOf(query.getLong(7)));
                Date date3 = DateConverter.toDate(query.isNull(8) ? null : Long.valueOf(query.getLong(8)));
                long j = query.getLong(9);
                int i3 = query.getInt(10);
                int i4 = query.getInt(11);
                int i5 = query.getInt(12);
                int i6 = query.getInt(13);
                String string4 = query.isNull(14) ? null : query.getString(14);
                int i7 = query.getInt(15);
                Recurring recurring = new Recurring(i, string, string2, string3, date, date3, date2, j, query.isNull(16) ? null : query.getString(16), i3, i4, i5, i6, string4, i7, query.isNull(17) ? null : query.getString(17), query.getInt(18), query.getInt(19), query.isNull(22) ? null : query.getString(22), query.getInt(23), i2, query.getInt(20), query.isNull(21) ? null : query.getString(21));
                recurring.setId(query.getInt(2));
                arrayList.add(recurring);
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
