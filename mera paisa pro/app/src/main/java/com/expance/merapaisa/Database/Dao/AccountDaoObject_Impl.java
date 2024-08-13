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
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.expance.merapaisa.Database.Entity.AccountEntity;
import com.expance.merapaisa.Model.Account;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

/* loaded from: classes3.dex */
public final class AccountDaoObject_Impl implements AccountDaoObject {
    private final RoomDatabase __db;
    private final EntityInsertionAdapter<AccountEntity> __insertionAdapterOfAccountEntity;
    private final SharedSQLiteStatement __preparedStmtOfDeleteAccount;
    private final SharedSQLiteStatement __preparedStmtOfRestoreMedia;
    private final SharedSQLiteStatement __preparedStmtOfUpdateAccountOrdering;
    private final EntityDeletionOrUpdateAdapter<AccountEntity> __updateAdapterOfAccountEntity;

    public AccountDaoObject_Impl(RoomDatabase __db) {
        this.__db = __db;
        this.__insertionAdapterOfAccountEntity = new EntityInsertionAdapter<AccountEntity>(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.AccountDaoObject_Impl.1
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "INSERT OR ABORT INTO `account` (`id`,`type`,`name`,`currency`,`ordering`,`currency_symbol`,`balance`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
            }

            @Override // androidx.room.EntityInsertionAdapter
            public void bind(SupportSQLiteStatement stmt, AccountEntity value) {
                stmt.bindLong(1, value.getId());
                stmt.bindLong(2, value.getType());
                if (value.getName() == null) {
                    stmt.bindNull(3);
                } else {
                    stmt.bindString(3, value.getName());
                }
                if (value.getCurrency() == null) {
                    stmt.bindNull(4);
                } else {
                    stmt.bindString(4, value.getCurrency());
                }
                stmt.bindLong(5, value.getOrdering());
                if (value.getCurrencySymbol() == null) {
                    stmt.bindNull(6);
                } else {
                    stmt.bindString(6, value.getCurrencySymbol());
                }
                stmt.bindLong(7, value.getBalance());
            }
        };
        this.__updateAdapterOfAccountEntity = new EntityDeletionOrUpdateAdapter<AccountEntity>(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.AccountDaoObject_Impl.2
            @Override // androidx.room.EntityDeletionOrUpdateAdapter, androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "UPDATE OR ABORT `account` SET `id` = ?,`type` = ?,`name` = ?,`currency` = ?,`ordering` = ?,`currency_symbol` = ?,`balance` = ? WHERE `id` = ?";
            }

            @Override // androidx.room.EntityDeletionOrUpdateAdapter
            public void bind(SupportSQLiteStatement stmt, AccountEntity value) {
                stmt.bindLong(1, value.getId());
                stmt.bindLong(2, value.getType());
                if (value.getName() == null) {
                    stmt.bindNull(3);
                } else {
                    stmt.bindString(3, value.getName());
                }
                if (value.getCurrency() == null) {
                    stmt.bindNull(4);
                } else {
                    stmt.bindString(4, value.getCurrency());
                }
                stmt.bindLong(5, value.getOrdering());
                if (value.getCurrencySymbol() == null) {
                    stmt.bindNull(6);
                } else {
                    stmt.bindString(6, value.getCurrencySymbol());
                }
                stmt.bindLong(7, value.getBalance());
                stmt.bindLong(8, value.getId());
            }
        };
        this.__preparedStmtOfUpdateAccountOrdering = new SharedSQLiteStatement(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.AccountDaoObject_Impl.3
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "UPDATE account SET ordering = ? WHERE id = ?";
            }
        };
        this.__preparedStmtOfRestoreMedia = new SharedSQLiteStatement(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.AccountDaoObject_Impl.4
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "DELETE FROM media";
            }
        };
        this.__preparedStmtOfDeleteAccount = new SharedSQLiteStatement(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.AccountDaoObject_Impl.5
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "DELETE FROM Account WHERE id = ?";
            }
        };
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.AccountDaoObject
    public long insertAccount(final AccountEntity accountEntity) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            long insertAndReturnId = this.__insertionAdapterOfAccountEntity.insertAndReturnId(accountEntity);
            this.__db.setTransactionSuccessful();
            return insertAndReturnId;
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.AccountDaoObject
    public void updateAccount(final AccountEntity accountEntity) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfAccountEntity.handle(accountEntity);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.AccountDaoObject
    public void updateAccountOrdering(final int order, final int id) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfUpdateAccountOrdering.acquire();
        acquire.bindLong(1, order);
        acquire.bindLong(2, id);
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfUpdateAccountOrdering.release(acquire);
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.AccountDaoObject
    public void restoreMedia() {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfRestoreMedia.acquire();
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfRestoreMedia.release(acquire);
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.AccountDaoObject
    public void deleteAccount(final int id) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfDeleteAccount.acquire();
        acquire.bindLong(1, id);
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfDeleteAccount.release(acquire);
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.AccountDaoObject
    public LiveData<List<Account>> getLiveAllAccount() {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT id,name,currency,currency_symbol as currencySymbol,type,balance FROM account ORDER BY ordering", 0);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"account"}, false, new Callable<List<Account>>() { // from class: com.ktwapps.walletmanager.Database.Dao.AccountDaoObject_Impl.6
            @Override // java.util.concurrent.Callable
            public List<Account> call() throws Exception {
                Cursor query = DBUtil.query(AccountDaoObject_Impl.this.__db, acquire, false, null);
                try {
                    ArrayList arrayList = new ArrayList(query.getCount());
                    while (query.moveToNext()) {
                        int i = query.getInt(0);
                        String string = query.isNull(1) ? null : query.getString(1);
                        String string2 = query.isNull(2) ? null : query.getString(2);
                        arrayList.add(new Account(i, string, query.isNull(3) ? null : query.getString(3), string2, query.getLong(5), query.getInt(4)));
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

    @Override // com.ktwapps.walletmanager.Database.Dao.AccountDaoObject
    public List<AccountEntity> getAllAccountEntity() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM account", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, JamXmlElements.TYPE);
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "name");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, FirebaseAnalytics.Param.CURRENCY);
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "ordering");
            int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "currency_symbol");
            int columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "balance");
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                AccountEntity accountEntity = new AccountEntity(query.getInt(columnIndexOrThrow2), query.isNull(columnIndexOrThrow3) ? null : query.getString(columnIndexOrThrow3), query.isNull(columnIndexOrThrow4) ? null : query.getString(columnIndexOrThrow4), query.isNull(columnIndexOrThrow6) ? null : query.getString(columnIndexOrThrow6), query.getLong(columnIndexOrThrow7), query.getInt(columnIndexOrThrow5));
                accountEntity.setId(query.getInt(columnIndexOrThrow));
                arrayList.add(accountEntity);
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.AccountDaoObject
    public int getAccountLastOrdering() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT ordering FROM account ORDER BY ordering DESC LIMIT 1", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            return query.moveToFirst() ? query.getInt(0) : 0;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.AccountDaoObject
    public Long getAccountBalance(final int id, final int status, final long date) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT SUM(ROUND(w.amount*c.rate)) FROM    (SELECT  t1.id, (       SELECT SUM(amount) as amount FROM(           SELECT SUM(amount) as amount FROM trans as t WHERE t.wallet_id = t1.id AND t.type != 2 AND t.date_time < ?           UNION ALL           SELECT SUM(-amount) as amount FROM trans as t WHERE t.wallet_id = t1.id AND t.type = 2 AND t.date_time < ?           UNION ALL           SELECT initial_amount FROM wallet as w WHERE w.id = t1.id           UNION ALL           SELECT SUM(trans_amount) as amount FROM trans as t WHERE t.transfer_wallet_id = t1.id AND t.type = 2 AND t.date_time < ?       ) as subt1   ) as amount,t1.currency,t1.account_id,t1.active,t1.exclude FROM wallet as t1) as w LEFT JOIN currency as c ON w.currency = c.code WHERE w.account_id = ? AND w.active = ? AND w.exclude = 0 AND c.account_id = ?", 6);
        acquire.bindLong(1, date);
        acquire.bindLong(2, date);
        acquire.bindLong(3, date);
        long j = id;
        acquire.bindLong(4, j);
        acquire.bindLong(5, status);
        acquire.bindLong(6, j);
        this.__db.assertNotSuspendingTransaction();
        Long l = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            if (query.moveToFirst() && !query.isNull(0)) {
                l = Long.valueOf(query.getLong(0));
            }
            return l;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.AccountDaoObject
    public AccountEntity getEntityById(final int id) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM account WHERE id = ?", 1);
        acquire.bindLong(1, id);
        this.__db.assertNotSuspendingTransaction();
        AccountEntity accountEntity = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, JamXmlElements.TYPE);
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "name");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, FirebaseAnalytics.Param.CURRENCY);
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "ordering");
            int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "currency_symbol");
            int columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "balance");
            if (query.moveToFirst()) {
                accountEntity = new AccountEntity(query.getInt(columnIndexOrThrow2), query.isNull(columnIndexOrThrow3) ? null : query.getString(columnIndexOrThrow3), query.isNull(columnIndexOrThrow4) ? null : query.getString(columnIndexOrThrow4), query.isNull(columnIndexOrThrow6) ? null : query.getString(columnIndexOrThrow6), query.getLong(columnIndexOrThrow7), query.getInt(columnIndexOrThrow5));
                accountEntity.setId(query.getInt(columnIndexOrThrow));
            }
            return accountEntity;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.AccountDaoObject
    public int checkpoint(final SupportSQLiteQuery supportSQLiteQuery) {
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, supportSQLiteQuery, false, null);
        try {
            return query.moveToFirst() ? query.getInt(0) : 0;
        } finally {
            query.close();
        }
    }

    public static List<Class<?>> getRequiredConverters() {
        return Collections.emptyList();
    }
}
