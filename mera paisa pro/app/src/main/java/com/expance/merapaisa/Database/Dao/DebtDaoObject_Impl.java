package com.expance.merapaisa.Database.Dao;

import android.database.Cursor;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.core.app.NotificationCompat;
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
import com.expance.merapaisa.Database.Entity.DebtEntity;
import com.expance.merapaisa.Database.Entity.DebtTransEntity;
import com.expance.merapaisa.Database.Entity.TransEntity;
import com.expance.merapaisa.Model.Currency;
import com.expance.merapaisa.Model.Debt;

import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

/* loaded from: classes3.dex */
public final class DebtDaoObject_Impl implements DebtDaoObject {
    private final RoomDatabase __db;
    private final EntityInsertionAdapter<DebtEntity> __insertionAdapterOfDebtEntity;
    private final EntityInsertionAdapter<DebtTransEntity> __insertionAdapterOfDebtTransEntity;
    private final SharedSQLiteStatement __preparedStmtOfDeleteAllDebtTrans;
    private final SharedSQLiteStatement __preparedStmtOfDeleteAllTransaction;
    private final SharedSQLiteStatement __preparedStmtOfDeleteDebt;
    private final SharedSQLiteStatement __preparedStmtOfDeleteDebtTrans;
    private final SharedSQLiteStatement __preparedStmtOfDeleteTransactionFromDebtTransId;
    private final EntityDeletionOrUpdateAdapter<DebtEntity> __updateAdapterOfDebtEntity;
    private final EntityDeletionOrUpdateAdapter<DebtTransEntity> __updateAdapterOfDebtTransEntity;

    public DebtDaoObject_Impl(RoomDatabase __db) {
        this.__db = __db;
        this.__insertionAdapterOfDebtEntity = new EntityInsertionAdapter<DebtEntity>(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.DebtDaoObject_Impl.1
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "INSERT OR ABORT INTO `debt` (`id`,`name`,`lender`,`color`,`pay`,`amount`,`due_date`,`lend_date`,`account_id`,`status`,`type`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?)";
            }

            @Override // androidx.room.EntityInsertionAdapter
            public void bind(SupportSQLiteStatement stmt, DebtEntity value) {
                stmt.bindLong(1, value.getId());
                if (value.getName() == null) {
                    stmt.bindNull(2);
                } else {
                    stmt.bindString(2, value.getName());
                }
                if (value.getLender() == null) {
                    stmt.bindNull(3);
                } else {
                    stmt.bindString(3, value.getLender());
                }
                if (value.getColor() == null) {
                    stmt.bindNull(4);
                } else {
                    stmt.bindString(4, value.getColor());
                }
                stmt.bindLong(5, value.getPay());
                stmt.bindLong(6, value.getAmount());
                Long fromDate = DateConverter.fromDate(value.getDueDate());
                if (fromDate == null) {
                    stmt.bindNull(7);
                } else {
                    stmt.bindLong(7, fromDate.longValue());
                }
                Long fromDate2 = DateConverter.fromDate(value.getLendDate());
                if (fromDate2 == null) {
                    stmt.bindNull(8);
                } else {
                    stmt.bindLong(8, fromDate2.longValue());
                }
                stmt.bindLong(9, value.getAccountId());
                stmt.bindLong(10, value.getStatus());
                stmt.bindLong(11, value.getType());
            }
        };
        this.__insertionAdapterOfDebtTransEntity = new EntityInsertionAdapter<DebtTransEntity>(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.DebtDaoObject_Impl.2
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "INSERT OR ABORT INTO `debtTrans` (`id`,`amount`,`date_time`,`debt_id`,`note`,`type`) VALUES (nullif(?, 0),?,?,?,?,?)";
            }

            @Override // androidx.room.EntityInsertionAdapter
            public void bind(SupportSQLiteStatement stmt, DebtTransEntity value) {
                stmt.bindLong(1, value.getId());
                stmt.bindLong(2, value.getAmount());
                Long fromDate = DateConverter.fromDate(value.getDateTime());
                if (fromDate == null) {
                    stmt.bindNull(3);
                } else {
                    stmt.bindLong(3, fromDate.longValue());
                }
                stmt.bindLong(4, value.getDebtId());
                if (value.getNote() == null) {
                    stmt.bindNull(5);
                } else {
                    stmt.bindString(5, value.getNote());
                }
                stmt.bindLong(6, value.getType());
            }
        };
        this.__updateAdapterOfDebtEntity = new EntityDeletionOrUpdateAdapter<DebtEntity>(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.DebtDaoObject_Impl.3
            @Override
            // androidx.room.EntityDeletionOrUpdateAdapter, androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "UPDATE OR ABORT `debt` SET `id` = ?,`name` = ?,`lender` = ?,`color` = ?,`pay` = ?,`amount` = ?,`due_date` = ?,`lend_date` = ?,`account_id` = ?,`status` = ?,`type` = ? WHERE `id` = ?";
            }

            @Override // androidx.room.EntityDeletionOrUpdateAdapter
            public void bind(SupportSQLiteStatement stmt, DebtEntity value) {
                stmt.bindLong(1, value.getId());
                if (value.getName() == null) {
                    stmt.bindNull(2);
                } else {
                    stmt.bindString(2, value.getName());
                }
                if (value.getLender() == null) {
                    stmt.bindNull(3);
                } else {
                    stmt.bindString(3, value.getLender());
                }
                if (value.getColor() == null) {
                    stmt.bindNull(4);
                } else {
                    stmt.bindString(4, value.getColor());
                }
                stmt.bindLong(5, value.getPay());
                stmt.bindLong(6, value.getAmount());
                Long fromDate = DateConverter.fromDate(value.getDueDate());
                if (fromDate == null) {
                    stmt.bindNull(7);
                } else {
                    stmt.bindLong(7, fromDate.longValue());
                }
                Long fromDate2 = DateConverter.fromDate(value.getLendDate());
                if (fromDate2 == null) {
                    stmt.bindNull(8);
                } else {
                    stmt.bindLong(8, fromDate2.longValue());
                }
                stmt.bindLong(9, value.getAccountId());
                stmt.bindLong(10, value.getStatus());
                stmt.bindLong(11, value.getType());
                stmt.bindLong(12, value.getId());
            }
        };
        this.__updateAdapterOfDebtTransEntity = new EntityDeletionOrUpdateAdapter<DebtTransEntity>(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.DebtDaoObject_Impl.4
            @Override
            // androidx.room.EntityDeletionOrUpdateAdapter, androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "UPDATE OR ABORT `debtTrans` SET `id` = ?,`amount` = ?,`date_time` = ?,`debt_id` = ?,`note` = ?,`type` = ? WHERE `id` = ?";
            }

            @Override // androidx.room.EntityDeletionOrUpdateAdapter
            public void bind(SupportSQLiteStatement stmt, DebtTransEntity value) {
                stmt.bindLong(1, value.getId());
                stmt.bindLong(2, value.getAmount());
                Long fromDate = DateConverter.fromDate(value.getDateTime());
                if (fromDate == null) {
                    stmt.bindNull(3);
                } else {
                    stmt.bindLong(3, fromDate.longValue());
                }
                stmt.bindLong(4, value.getDebtId());
                if (value.getNote() == null) {
                    stmt.bindNull(5);
                } else {
                    stmt.bindString(5, value.getNote());
                }
                stmt.bindLong(6, value.getType());
                stmt.bindLong(7, value.getId());
            }
        };
        this.__preparedStmtOfDeleteDebt = new SharedSQLiteStatement(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.DebtDaoObject_Impl.5
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "DELETE FROM debt WHERE id = ?";
            }
        };
        this.__preparedStmtOfDeleteDebtTrans = new SharedSQLiteStatement(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.DebtDaoObject_Impl.6
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "DELETE FROM debtTrans WHERE id = ?";
            }
        };
        this.__preparedStmtOfDeleteTransactionFromDebtTransId = new SharedSQLiteStatement(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.DebtDaoObject_Impl.7
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "DELETE FROM trans WHERE debt_id = ? AND debt_trans_id = ?";
            }
        };
        this.__preparedStmtOfDeleteAllTransaction = new SharedSQLiteStatement(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.DebtDaoObject_Impl.8
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "DELETE FROM trans WHERE debt_id = ?";
            }
        };
        this.__preparedStmtOfDeleteAllDebtTrans = new SharedSQLiteStatement(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.DebtDaoObject_Impl.9
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "DELETE FROM debtTrans WHERE debt_id = ?";
            }
        };
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.DebtDaoObject
    public long insertDebt(final DebtEntity debtEntity) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            long insertAndReturnId = this.__insertionAdapterOfDebtEntity.insertAndReturnId(debtEntity);
            this.__db.setTransactionSuccessful();
            return insertAndReturnId;
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.DebtDaoObject
    public long insertDebtTrans(final DebtTransEntity debtTransEntity) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            long insertAndReturnId = this.__insertionAdapterOfDebtTransEntity.insertAndReturnId(debtTransEntity);
            this.__db.setTransactionSuccessful();
            return insertAndReturnId;
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.DebtDaoObject
    public void updateDebt(final DebtEntity debtEntity) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfDebtEntity.handle(debtEntity);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.DebtDaoObject
    public void updateDebtTrans(final DebtTransEntity debtTransEntity) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfDebtTransEntity.handle(debtTransEntity);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.DebtDaoObject
    public void deleteDebt(final int id) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfDeleteDebt.acquire();
        acquire.bindLong(1, id);
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfDeleteDebt.release(acquire);
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.DebtDaoObject
    public void deleteDebtTrans(final int id) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfDeleteDebtTrans.acquire();
        acquire.bindLong(1, id);
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfDeleteDebtTrans.release(acquire);
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.DebtDaoObject
    public void deleteTransactionFromDebtTransId(final int debtId, final int debtTransId) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfDeleteTransactionFromDebtTransId.acquire();
        acquire.bindLong(1, debtId);
        acquire.bindLong(2, debtTransId);
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfDeleteTransactionFromDebtTransId.release(acquire);
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.DebtDaoObject
    public void deleteAllTransaction(final int id) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfDeleteAllTransaction.acquire();
        acquire.bindLong(1, id);
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfDeleteAllTransaction.release(acquire);
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.DebtDaoObject
    public void deleteAllDebtTrans(final int id) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfDeleteAllDebtTrans.acquire();
        acquire.bindLong(1, id);
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfDeleteAllDebtTrans.release(acquire);
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.DebtDaoObject
    public LiveData<List<Debt>> getAllDebt(final int accountId, final int status) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT id,name,type,lender,color,pay,amount,due_date as dueDate,lend_date as lendDate, status FROM debt WHERE account_id = ? AND status = ?", 2);
        acquire.bindLong(1, accountId);
        acquire.bindLong(2, status);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"debt"}, false, new Callable<List<Debt>>() { // from class: com.ktwapps.walletmanager.Database.Dao.DebtDaoObject_Impl.10
            @Override // java.util.concurrent.Callable
            public List<Debt> call() throws Exception {
                Cursor query = DBUtil.query(DebtDaoObject_Impl.this.__db, acquire, false, null);
                try {
                    ArrayList arrayList = new ArrayList(query.getCount());
                    while (query.moveToNext()) {
                        arrayList.add(new Debt(query.getInt(0), query.getInt(2), query.isNull(1) ? null : query.getString(1), query.isNull(3) ? null : query.getString(3), query.isNull(4) ? null : query.getString(4), query.getLong(5), query.getLong(6), DateConverter.toDate(query.isNull(7) ? null : Long.valueOf(query.getLong(7))), DateConverter.toDate(query.isNull(8) ? null : Long.valueOf(query.getLong(8))), query.getInt(9)));
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

    @Override // com.ktwapps.walletmanager.Database.Dao.DebtDaoObject
    public LiveData<List<Debt>> getDebt(final int accountId, final int type) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT id,name,type,lender,color,pay,amount,due_date as dueDate,lend_date as lendDate, status FROM debt WHERE account_id = ? AND type = ? ORDER BY id DESC, status ASC", 2);
        acquire.bindLong(1, accountId);
        acquire.bindLong(2, type);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"debt"}, false, new Callable<List<Debt>>() { // from class: com.ktwapps.walletmanager.Database.Dao.DebtDaoObject_Impl.11
            @Override // java.util.concurrent.Callable
            public List<Debt> call() throws Exception {
                Cursor query = DBUtil.query(DebtDaoObject_Impl.this.__db, acquire, false, null);
                try {
                    ArrayList arrayList = new ArrayList(query.getCount());
                    while (query.moveToNext()) {
                        arrayList.add(new Debt(query.getInt(0), query.getInt(2), query.isNull(1) ? null : query.getString(1), query.isNull(3) ? null : query.getString(3), query.isNull(4) ? null : query.getString(4), query.getLong(5), query.getLong(6), DateConverter.toDate(query.isNull(7) ? null : Long.valueOf(query.getLong(7))), DateConverter.toDate(query.isNull(8) ? null : Long.valueOf(query.getLong(8))), query.getInt(9)));
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

    @Override // com.ktwapps.walletmanager.Database.Dao.DebtDaoObject
    public LiveData<Debt> getLiveDebtById(final int id) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT id,name,type,lender,color,pay,amount,due_date as dueDate,lend_date as lendDate, status FROM debt WHERE id = ?", 1);
        acquire.bindLong(1, id);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"debt"}, false, new Callable<Debt>() { // from class: com.ktwapps.walletmanager.Database.Dao.DebtDaoObject_Impl.12
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Debt call() throws Exception {
                Debt debt = null;
                Long valueOf = null;
                Cursor query = DBUtil.query(DebtDaoObject_Impl.this.__db, acquire, false, null);
                try {
                    if (query.moveToFirst()) {
                        int i = query.getInt(0);
                        String string = query.isNull(1) ? null : query.getString(1);
                        int i2 = query.getInt(2);
                        String string2 = query.isNull(3) ? null : query.getString(3);
                        String string3 = query.isNull(4) ? null : query.getString(4);
                        long j = query.getLong(5);
                        long j2 = query.getLong(6);
                        Date date = DateConverter.toDate(query.isNull(7) ? null : Long.valueOf(query.getLong(7)));
                        if (!query.isNull(8)) {
                            valueOf = Long.valueOf(query.getLong(8));
                        }
                        debt = new Debt(i, i2, string, string2, string3, j, j2, date, DateConverter.toDate(valueOf), query.getInt(9));
                    }
                    return debt;
                } finally {
                    query.close();
                }
            }

            protected void finalize() {
                acquire.release();
            }
        });
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.DebtDaoObject
    public DebtEntity getDebtById(final int id) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM debt WHERE id = ?", 1);
        acquire.bindLong(1, id);
        this.__db.assertNotSuspendingTransaction();
        DebtEntity debtEntity = null;
        Long valueOf = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "name");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "lender");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, TypedValues.Custom.S_COLOR);
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "pay");
            int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "amount");
            int columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "due_date");
            int columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "lend_date");
            int columnIndexOrThrow9 = CursorUtil.getColumnIndexOrThrow(query, "account_id");
            int columnIndexOrThrow10 = CursorUtil.getColumnIndexOrThrow(query, NotificationCompat.CATEGORY_STATUS);
            int columnIndexOrThrow11 = CursorUtil.getColumnIndexOrThrow(query, JamXmlElements.TYPE);
            if (query.moveToFirst()) {
                String string = query.isNull(columnIndexOrThrow2) ? null : query.getString(columnIndexOrThrow2);
                String string2 = query.isNull(columnIndexOrThrow3) ? null : query.getString(columnIndexOrThrow3);
                String string3 = query.isNull(columnIndexOrThrow4) ? null : query.getString(columnIndexOrThrow4);
                long j = query.getLong(columnIndexOrThrow5);
                long j2 = query.getLong(columnIndexOrThrow6);
                Date date = DateConverter.toDate(query.isNull(columnIndexOrThrow7) ? null : Long.valueOf(query.getLong(columnIndexOrThrow7)));
                if (!query.isNull(columnIndexOrThrow8)) {
                    valueOf = Long.valueOf(query.getLong(columnIndexOrThrow8));
                }
                debtEntity = new DebtEntity(string, string2, string3, j, j2, date, DateConverter.toDate(valueOf), query.getInt(columnIndexOrThrow9), query.getInt(columnIndexOrThrow10), query.getInt(columnIndexOrThrow11));
                debtEntity.setId(query.getInt(columnIndexOrThrow));
            }
            return debtEntity;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.DebtDaoObject
    public LiveData<List<DebtTransEntity>> getDebtTrans(final int debtId) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM debtTrans WHERE debt_id = ? ORDER BY date_time DESC", 1);
        acquire.bindLong(1, debtId);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"debtTrans"}, false, new Callable<List<DebtTransEntity>>() { // from class: com.ktwapps.walletmanager.Database.Dao.DebtDaoObject_Impl.13
            @Override // java.util.concurrent.Callable
            public List<DebtTransEntity> call() throws Exception {
                Cursor query = DBUtil.query(DebtDaoObject_Impl.this.__db, acquire, false, null);
                try {
                    int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
                    int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "amount");
                    int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "date_time");
                    int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "debt_id");
                    int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "note");
                    int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, JamXmlElements.TYPE);
                    ArrayList arrayList = new ArrayList(query.getCount());
                    while (query.moveToNext()) {
                        DebtTransEntity debtTransEntity = new DebtTransEntity(query.getLong(columnIndexOrThrow2), DateConverter.toDate(query.isNull(columnIndexOrThrow3) ? null : Long.valueOf(query.getLong(columnIndexOrThrow3))), query.isNull(columnIndexOrThrow5) ? null : query.getString(columnIndexOrThrow5), query.getInt(columnIndexOrThrow4), query.getInt(columnIndexOrThrow6));
                        debtTransEntity.setId(query.getInt(columnIndexOrThrow));
                        arrayList.add(debtTransEntity);
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

    @Override // com.ktwapps.walletmanager.Database.Dao.DebtDaoObject
    public List<DebtTransEntity> getAllDebtTrans(final int debtId) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM debtTrans WHERE debt_id = ? ORDER BY date_time DESC", 1);
        acquire.bindLong(1, debtId);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "amount");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "date_time");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "debt_id");
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "note");
            int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, JamXmlElements.TYPE);
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                DebtTransEntity debtTransEntity = new DebtTransEntity(query.getLong(columnIndexOrThrow2), DateConverter.toDate(query.isNull(columnIndexOrThrow3) ? null : Long.valueOf(query.getLong(columnIndexOrThrow3))), query.isNull(columnIndexOrThrow5) ? null : query.getString(columnIndexOrThrow5), query.getInt(columnIndexOrThrow4), query.getInt(columnIndexOrThrow6));
                debtTransEntity.setId(query.getInt(columnIndexOrThrow));
                arrayList.add(debtTransEntity);
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.DebtDaoObject
    public DebtTransEntity getDebtTransById(final int id) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM debtTrans WHERE id = ?", 1);
        acquire.bindLong(1, id);
        this.__db.assertNotSuspendingTransaction();
        DebtTransEntity debtTransEntity = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "amount");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "date_time");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "debt_id");
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "note");
            int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, JamXmlElements.TYPE);
            if (query.moveToFirst()) {
                debtTransEntity = new DebtTransEntity(query.getLong(columnIndexOrThrow2), DateConverter.toDate(query.isNull(columnIndexOrThrow3) ? null : Long.valueOf(query.getLong(columnIndexOrThrow3))), query.isNull(columnIndexOrThrow5) ? null : query.getString(columnIndexOrThrow5), query.getInt(columnIndexOrThrow4), query.getInt(columnIndexOrThrow6));
                debtTransEntity.setId(query.getInt(columnIndexOrThrow));
            }
            return debtTransEntity;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.DebtDaoObject
    public int getLoanId(final int accountId) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT id FROM category WHERE account_id = ? AND default_category = 25", 1);
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

    @Override // com.ktwapps.walletmanager.Database.Dao.DebtDaoObject
    public int getDebtId(final int accountId) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT id FROM category WHERE account_id = ? AND default_category = 27", 1);
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

    @Override // com.ktwapps.walletmanager.Database.Dao.DebtDaoObject
    public int getRepayId(final int accountId) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT id FROM category WHERE account_id = ? AND default_category = 26", 1);
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

    @Override // com.ktwapps.walletmanager.Database.Dao.DebtDaoObject
    public int getReceiveId(final int accountId) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT id FROM category WHERE account_id = ? AND default_category = 28", 1);
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

    @Override // com.ktwapps.walletmanager.Database.Dao.DebtDaoObject
    public TransEntity getTransactionFromDebtId(final int id) {
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
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM trans WHERE debt_id = ? and debt_trans_id = 0", 1);
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

    @Override // com.ktwapps.walletmanager.Database.Dao.DebtDaoObject
    public TransEntity getTransactionFromDebtTransId(final int debtId, final int debtTransId) {
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
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM trans WHERE debt_id = ? and debt_trans_id = ?", 2);
        acquire.bindLong(1, debtId);
        acquire.bindLong(2, debtTransId);
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

    @Override // com.ktwapps.walletmanager.Database.Dao.DebtDaoObject
    public List<TransEntity> getTransactionFromDebtTransList(final int debtId) {
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
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM trans WHERE debt_id = ? and debt_trans_id != 0", 1);
        acquire.bindLong(1, debtId);
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
            int i = columnIndexOrThrow;
            int columnIndexOrThrow15 = CursorUtil.getColumnIndexOrThrow(query, "debt_trans_id");
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                String string = query.isNull(columnIndexOrThrow2) ? null : query.getString(columnIndexOrThrow2);
                String string2 = query.isNull(columnIndexOrThrow3) ? null : query.getString(columnIndexOrThrow3);
                int i2 = query.getInt(columnIndexOrThrow4);
                long j = query.getLong(columnIndexOrThrow5);
                int i3 = columnIndexOrThrow15;
                int i4 = columnIndexOrThrow14;
                TransEntity transEntity = new TransEntity(string, string2, j, DateConverter.toDate(query.isNull(columnIndexOrThrow6) ? null : Long.valueOf(query.getLong(columnIndexOrThrow6))), i2, query.getInt(columnIndexOrThrow7), query.getInt(columnIndexOrThrow9), query.getInt(columnIndexOrThrow8), query.getInt(columnIndexOrThrow11), query.getInt(columnIndexOrThrow12), query.getLong(columnIndexOrThrow13), query.getInt(columnIndexOrThrow14), query.getInt(i3), query.getInt(columnIndexOrThrow10));
                int i5 = i;
                int i6 = columnIndexOrThrow13;
                transEntity.setId(query.getInt(i5));
                arrayList.add(transEntity);
                columnIndexOrThrow13 = i6;
                columnIndexOrThrow14 = i4;
                columnIndexOrThrow15 = i3;
                i = i5;
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

    @Override // com.ktwapps.walletmanager.Database.Dao.DebtDaoObject
    public Currency getDebtCurrency(final int accountId, final int debtId) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT rate,currency FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as c ON w.currency = c.code WHERE t.debt_id = ? AND t.debt_trans_id = 0 AND c.account_id = ? AND w.account_id = ?", 3);
        acquire.bindLong(1, debtId);
        long j = accountId;
        acquire.bindLong(2, j);
        acquire.bindLong(3, j);
        this.__db.assertNotSuspendingTransaction();
        Currency currency = null;
        String string = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            if (query.moveToFirst()) {
                double d = query.getDouble(0);
                if (!query.isNull(1)) {
                    string = query.getString(1);
                }
                currency = new Currency(d, string);
            }
            return currency;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.DebtDaoObject
    public Currency getDebtTransCurrency(final int accountId, final int debtId, final int debtTransId) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT rate,currency FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as c ON w.currency = c.code WHERE t.debt_id = ? AND t.debt_trans_id = ? AND c.account_id = ? AND w.account_id = ?", 4);
        acquire.bindLong(1, debtId);
        acquire.bindLong(2, debtTransId);
        long j = accountId;
        acquire.bindLong(3, j);
        acquire.bindLong(4, j);
        this.__db.assertNotSuspendingTransaction();
        Currency currency = null;
        String string = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            if (query.moveToFirst()) {
                double d = query.getDouble(0);
                if (!query.isNull(1)) {
                    string = query.getString(1);
                }
                currency = new Currency(d, string);
            }
            return currency;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.DebtDaoObject
    public String getDebtWallet(final int debtId) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT w.name FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id WHERE t.debt_id = ? AND t.debt_trans_id = 0", 1);
        acquire.bindLong(1, debtId);
        this.__db.assertNotSuspendingTransaction();
        String str = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            if (query.moveToFirst() && !query.isNull(0)) {
                str = query.getString(0);
            }
            return str;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.DebtDaoObject
    public String getDebtTransWallet(final int debtId, final int debtTransId) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT w.name FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id WHERE t.debt_id = ? AND t.debt_trans_id = ?", 2);
        acquire.bindLong(1, debtId);
        acquire.bindLong(2, debtTransId);
        this.__db.assertNotSuspendingTransaction();
        String str = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            if (query.moveToFirst() && !query.isNull(0)) {
                str = query.getString(0);
            }
            return str;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public static List<Class<?>> getRequiredConverters() {
        return Collections.emptyList();
    }
}
