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
import com.expance.merapaisa.Database.Entity.CurrencyEntity;
import com.expance.merapaisa.Model.Currencies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

/* loaded from: classes3.dex */
public final class CurrencyDaoObject_Impl implements CurrencyDaoObject {
    private final RoomDatabase __db;
    private final EntityInsertionAdapter<CurrencyEntity> __insertionAdapterOfCurrencyEntity;
    private final SharedSQLiteStatement __preparedStmtOfDeleteCurrency;
    private final EntityDeletionOrUpdateAdapter<CurrencyEntity> __updateAdapterOfCurrencyEntity;

    public CurrencyDaoObject_Impl(RoomDatabase __db) {
        this.__db = __db;
        this.__insertionAdapterOfCurrencyEntity = new EntityInsertionAdapter<CurrencyEntity>(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.CurrencyDaoObject_Impl.1
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "INSERT OR ABORT INTO `currency` (`id`,`code`,`rate`,`account_id`) VALUES (nullif(?, 0),?,?,?)";
            }

            @Override // androidx.room.EntityInsertionAdapter
            public void bind(SupportSQLiteStatement stmt, CurrencyEntity value) {
                stmt.bindLong(1, value.getId());
                if (value.getCode() == null) {
                    stmt.bindNull(2);
                } else {
                    stmt.bindString(2, value.getCode());
                }
                stmt.bindDouble(3, value.getRate());
                stmt.bindLong(4, value.getAccountId());
            }
        };
        this.__updateAdapterOfCurrencyEntity = new EntityDeletionOrUpdateAdapter<CurrencyEntity>(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.CurrencyDaoObject_Impl.2
            @Override
            // androidx.room.EntityDeletionOrUpdateAdapter, androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "UPDATE OR ABORT `currency` SET `id` = ?,`code` = ?,`rate` = ?,`account_id` = ? WHERE `id` = ?";
            }

            @Override // androidx.room.EntityDeletionOrUpdateAdapter
            public void bind(SupportSQLiteStatement stmt, CurrencyEntity value) {
                stmt.bindLong(1, value.getId());
                if (value.getCode() == null) {
                    stmt.bindNull(2);
                } else {
                    stmt.bindString(2, value.getCode());
                }
                stmt.bindDouble(3, value.getRate());
                stmt.bindLong(4, value.getAccountId());
                stmt.bindLong(5, value.getId());
            }
        };
        this.__preparedStmtOfDeleteCurrency = new SharedSQLiteStatement(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.CurrencyDaoObject_Impl.3
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "DELETE FROM currency WHERE id = ?";
            }
        };
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.CurrencyDaoObject
    public void insertCurrency(final CurrencyEntity currencyEntity) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfCurrencyEntity.insert(currencyEntity);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.CurrencyDaoObject
    public void updateCurrency(final CurrencyEntity currencyEntity) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfCurrencyEntity.handle(currencyEntity);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.CurrencyDaoObject
    public void deleteCurrency(final int id) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfDeleteCurrency.acquire();
        acquire.bindLong(1, id);
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfDeleteCurrency.release(acquire);
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.CurrencyDaoObject
    public float getCurrencyRate(final int accountId, final String code) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT rate FROM currency WHERE account_id = ? AND code = ?", 2);
        acquire.bindLong(1, accountId);
        if (code == null) {
            acquire.bindNull(2);
        } else {
            acquire.bindString(2, code);
        }
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            return query.moveToFirst() ? query.getFloat(0) : 0.0f;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.CurrencyDaoObject
    public CurrencyEntity getCurrencyEntityById(final int id) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM currency WHERE id = ?", 1);
        acquire.bindLong(1, id);
        this.__db.assertNotSuspendingTransaction();
        CurrencyEntity currencyEntity = null;
        String string = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "code");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "rate");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "account_id");
            if (query.moveToFirst()) {
                if (!query.isNull(columnIndexOrThrow2)) {
                    string = query.getString(columnIndexOrThrow2);
                }
                CurrencyEntity currencyEntity2 = new CurrencyEntity(string, query.getDouble(columnIndexOrThrow3), query.getInt(columnIndexOrThrow4));
                currencyEntity2.setId(query.getInt(columnIndexOrThrow));
                currencyEntity = currencyEntity2;
            }
            return currencyEntity;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.CurrencyDaoObject
    public List<String> getCurrencyCodes(final int id) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT code FROM currency WHERE account_id = ?", 1);
        acquire.bindLong(1, id);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                arrayList.add(query.isNull(0) ? null : query.getString(0));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.CurrencyDaoObject
    public CurrencyEntity getCurrencyEntityByCode(final int id, final String code) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM currency WHERE account_id = ? AND code = ?", 2);
        acquire.bindLong(1, id);
        if (code == null) {
            acquire.bindNull(2);
        } else {
            acquire.bindString(2, code);
        }
        this.__db.assertNotSuspendingTransaction();
        CurrencyEntity currencyEntity = null;
        String string = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "code");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "rate");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "account_id");
            if (query.moveToFirst()) {
                if (!query.isNull(columnIndexOrThrow2)) {
                    string = query.getString(columnIndexOrThrow2);
                }
                CurrencyEntity currencyEntity2 = new CurrencyEntity(string, query.getDouble(columnIndexOrThrow3), query.getInt(columnIndexOrThrow4));
                currencyEntity2.setId(query.getInt(columnIndexOrThrow));
                currencyEntity = currencyEntity2;
            }
            return currencyEntity;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.CurrencyDaoObject
    public LiveData<List<Currencies>> getAllCurrencies(final int id) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT a.currency as mainCurrency, c.code as subCurrency, c.rate, c.id FROM currency as c LEFT JOIN account as a ON c.account_id = a.id WHERE c.account_id = ?", 1);
        acquire.bindLong(1, id);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{FirebaseAnalytics.Param.CURRENCY, "account"}, false, new Callable<List<Currencies>>() { // from class: com.ktwapps.walletmanager.Database.Dao.CurrencyDaoObject_Impl.4
            @Override // java.util.concurrent.Callable
            public List<Currencies> call() throws Exception {
                Cursor query = DBUtil.query(CurrencyDaoObject_Impl.this.__db, acquire, false, null);
                try {
                    ArrayList arrayList = new ArrayList(query.getCount());
                    while (query.moveToNext()) {
                        arrayList.add(new Currencies(query.getInt(3), query.getDouble(2), query.isNull(0) ? null : query.getString(0), query.isNull(1) ? null : query.getString(1)));
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

    public static List<Class<?>> getRequiredConverters() {
        return Collections.emptyList();
    }
}
