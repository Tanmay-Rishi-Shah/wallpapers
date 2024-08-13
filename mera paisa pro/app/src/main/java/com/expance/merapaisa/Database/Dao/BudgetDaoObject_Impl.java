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
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.expance.merapaisa.Database.Converter.DateConverter;
import com.expance.merapaisa.Database.Entity.BudgetCategoryEntity;
import com.expance.merapaisa.Database.Entity.BudgetEntity;
import com.expance.merapaisa.Model.Budget;
import com.expance.merapaisa.Model.BudgetStats;
import com.expance.merapaisa.Model.BudgetTrans;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

/* loaded from: classes3.dex */
public final class BudgetDaoObject_Impl implements BudgetDaoObject {
    private final RoomDatabase __db;
    private final EntityInsertionAdapter<BudgetCategoryEntity> __insertionAdapterOfBudgetCategoryEntity;
    private final EntityInsertionAdapter<BudgetEntity> __insertionAdapterOfBudgetEntity;
    private final SharedSQLiteStatement __preparedStmtOfDeleteBudget;
    private final SharedSQLiteStatement __preparedStmtOfDeleteBudgetCategory;
    private final EntityDeletionOrUpdateAdapter<BudgetEntity> __updateAdapterOfBudgetEntity;

    public BudgetDaoObject_Impl(RoomDatabase __db) {
        this.__db = __db;
        this.__insertionAdapterOfBudgetEntity = new EntityInsertionAdapter<BudgetEntity>(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.BudgetDaoObject_Impl.1
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "INSERT OR ABORT INTO `budget` (`id`,`name`,`amount`,`spent`,`status`,`period`,`color`,`repeat`,`account_id`,`category_id`,`start_date`,`end_date`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?)";
            }

            @Override // androidx.room.EntityInsertionAdapter
            public void bind(SupportSQLiteStatement stmt, BudgetEntity value) {
                stmt.bindLong(1, value.getId());
                if (value.getName() == null) {
                    stmt.bindNull(2);
                } else {
                    stmt.bindString(2, value.getName());
                }
                stmt.bindLong(3, value.getAmount());
                stmt.bindLong(4, value.getSpent());
                stmt.bindLong(5, value.getStatus());
                stmt.bindLong(6, value.getPeriod());
                if (value.getColor() == null) {
                    stmt.bindNull(7);
                } else {
                    stmt.bindString(7, value.getColor());
                }
                stmt.bindLong(8, value.getRepeat());
                stmt.bindLong(9, value.getAccountId());
                stmt.bindLong(10, value.getCategoryId());
                Long fromDate = DateConverter.fromDate(value.getStartDate());
                if (fromDate == null) {
                    stmt.bindNull(11);
                } else {
                    stmt.bindLong(11, fromDate.longValue());
                }
                Long fromDate2 = DateConverter.fromDate(value.getEndDate());
                if (fromDate2 == null) {
                    stmt.bindNull(12);
                } else {
                    stmt.bindLong(12, fromDate2.longValue());
                }
            }
        };
        this.__insertionAdapterOfBudgetCategoryEntity = new EntityInsertionAdapter<BudgetCategoryEntity>(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.BudgetDaoObject_Impl.2
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "INSERT OR ABORT INTO `budgetCategory` (`id`,`budget_id`,`category_id`) VALUES (nullif(?, 0),?,?)";
            }

            @Override // androidx.room.EntityInsertionAdapter
            public void bind(SupportSQLiteStatement stmt, BudgetCategoryEntity value) {
                stmt.bindLong(1, value.getId());
                stmt.bindLong(2, value.getBudgetId());
                stmt.bindLong(3, value.getCategoryId());
            }
        };
        this.__updateAdapterOfBudgetEntity = new EntityDeletionOrUpdateAdapter<BudgetEntity>(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.BudgetDaoObject_Impl.3
            @Override // androidx.room.EntityDeletionOrUpdateAdapter, androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "UPDATE OR ABORT `budget` SET `id` = ?,`name` = ?,`amount` = ?,`spent` = ?,`status` = ?,`period` = ?,`color` = ?,`repeat` = ?,`account_id` = ?,`category_id` = ?,`start_date` = ?,`end_date` = ? WHERE `id` = ?";
            }

            @Override // androidx.room.EntityDeletionOrUpdateAdapter
            public void bind(SupportSQLiteStatement stmt, BudgetEntity value) {
                stmt.bindLong(1, value.getId());
                if (value.getName() == null) {
                    stmt.bindNull(2);
                } else {
                    stmt.bindString(2, value.getName());
                }
                stmt.bindLong(3, value.getAmount());
                stmt.bindLong(4, value.getSpent());
                stmt.bindLong(5, value.getStatus());
                stmt.bindLong(6, value.getPeriod());
                if (value.getColor() == null) {
                    stmt.bindNull(7);
                } else {
                    stmt.bindString(7, value.getColor());
                }
                stmt.bindLong(8, value.getRepeat());
                stmt.bindLong(9, value.getAccountId());
                stmt.bindLong(10, value.getCategoryId());
                Long fromDate = DateConverter.fromDate(value.getStartDate());
                if (fromDate == null) {
                    stmt.bindNull(11);
                } else {
                    stmt.bindLong(11, fromDate.longValue());
                }
                Long fromDate2 = DateConverter.fromDate(value.getEndDate());
                if (fromDate2 == null) {
                    stmt.bindNull(12);
                } else {
                    stmt.bindLong(12, fromDate2.longValue());
                }
                stmt.bindLong(13, value.getId());
            }
        };
        this.__preparedStmtOfDeleteBudget = new SharedSQLiteStatement(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.BudgetDaoObject_Impl.4
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "DELETE FROM budget WHERE id = ?";
            }
        };
        this.__preparedStmtOfDeleteBudgetCategory = new SharedSQLiteStatement(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.BudgetDaoObject_Impl.5
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "DELETE FROM budgetCategory WHERE budget_id = ?";
            }
        };
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.BudgetDaoObject
    public long insertBudget(final BudgetEntity budgetEntity) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            long insertAndReturnId = this.__insertionAdapterOfBudgetEntity.insertAndReturnId(budgetEntity);
            this.__db.setTransactionSuccessful();
            return insertAndReturnId;
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.BudgetDaoObject
    public void insertBudgetCategory(final BudgetCategoryEntity budgetCategoryEntity) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfBudgetCategoryEntity.insert(budgetCategoryEntity);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.BudgetDaoObject
    public void updateBudget(final BudgetEntity budgetEntity) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfBudgetEntity.handle(budgetEntity);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.BudgetDaoObject
    public void deleteBudget(final int id) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfDeleteBudget.acquire();
        acquire.bindLong(1, id);
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfDeleteBudget.release(acquire);
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.BudgetDaoObject
    public void deleteBudgetCategory(final int id) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfDeleteBudgetCategory.acquire();
        acquire.bindLong(1, id);
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfDeleteBudgetCategory.release(acquire);
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.BudgetDaoObject
    public LiveData<List<Budget>> getBudget(final int accountId, final int status) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT b.id,b.name,b.amount,b.spent,b.account_id as accountId,b.color,GROUP_CONCAT(c.name) as categories,GROUP_CONCAT(c.default_category) as categoriesDefault,b.category_id as categoryId,b.period,b.repeat ,b.status,b.start_date as startDate, b.end_date as endDate FROM budget as b LEFT JOIN budgetCategory as bc ON b.id = bc.budget_id LEFT JOIN category as c ON bc.category_id = c.id WHERE b.account_id = ? AND b.status = ? GROUP BY b.id ORDER BY b.period", 2);
        acquire.bindLong(1, accountId);
        acquire.bindLong(2, status);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"budget", "budgetCategory", "category"}, false, new Callable<List<Budget>>() { // from class: com.ktwapps.walletmanager.Database.Dao.BudgetDaoObject_Impl.6
            @Override // java.util.concurrent.Callable
            public List<Budget> call() throws Exception {
                Cursor query = DBUtil.query(BudgetDaoObject_Impl.this.__db, acquire, false, null);
                try {
                    ArrayList arrayList = new ArrayList(query.getCount());
                    while (query.moveToNext()) {
                        String string = query.isNull(1) ? null : query.getString(1);
                        long j = query.getLong(2);
                        long j2 = query.getLong(3);
                        int i = query.getInt(4);
                        String string2 = query.isNull(5) ? null : query.getString(5);
                        String string3 = query.isNull(6) ? null : query.getString(6);
                        String string4 = query.isNull(7) ? null : query.getString(7);
                        int i2 = query.getInt(8);
                        int i3 = query.getInt(9);
                        Budget budget = new Budget(string, string2, string3, string4, i2, j2, j, DateConverter.toDate(query.isNull(13) ? null : Long.valueOf(query.getLong(13))), DateConverter.toDate(query.isNull(12) ? null : Long.valueOf(query.getLong(12))), query.getInt(10), i3, i, query.getInt(11));
                        budget.setId(query.getInt(0));
                        arrayList.add(budget);
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

    @Override // com.ktwapps.walletmanager.Database.Dao.BudgetDaoObject
    public LiveData<List<Budget>> getPeriodBudget(final int accountId, final List<Integer> periods) {
        StringBuilder newStringBuilder = StringUtil.newStringBuilder();
        newStringBuilder.append("SELECT b.id,b.name,b.amount,b.spent,b.account_id as accountId,b.color,GROUP_CONCAT(c.name) as categories,GROUP_CONCAT(c.default_category) as categoriesDefault,b.category_id as categoryId,b.period,b.repeat ,b.status,b.start_date as startDate, b.end_date as endDate FROM budget as b LEFT JOIN budgetCategory as bc ON b.id = bc.budget_id LEFT JOIN category as c ON bc.category_id = c.id WHERE b.account_id = ");
        newStringBuilder.append("?");
        newStringBuilder.append(" AND b.period IN (");
        int size = periods.size();
        StringUtil.appendPlaceholders(newStringBuilder, size);
        newStringBuilder.append(") GROUP BY b.id ORDER BY b.period");
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire(newStringBuilder.toString(), size + 1);
        acquire.bindLong(1, accountId);
        int i = 2;
        for (Integer num : periods) {
            if (num == null) {
                acquire.bindNull(i);
            } else {
                acquire.bindLong(i, num.intValue());
            }
            i++;
        }
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"budget", "budgetCategory", "category"}, false, new Callable<List<Budget>>() { // from class: com.ktwapps.walletmanager.Database.Dao.BudgetDaoObject_Impl.7
            @Override // java.util.concurrent.Callable
            public List<Budget> call() throws Exception {
                Cursor query = DBUtil.query(BudgetDaoObject_Impl.this.__db, acquire, false, null);
                try {
                    ArrayList arrayList = new ArrayList(query.getCount());
                    while (query.moveToNext()) {
                        String string = query.isNull(1) ? null : query.getString(1);
                        long j = query.getLong(2);
                        long j2 = query.getLong(3);
                        int i2 = query.getInt(4);
                        String string2 = query.isNull(5) ? null : query.getString(5);
                        String string3 = query.isNull(6) ? null : query.getString(6);
                        String string4 = query.isNull(7) ? null : query.getString(7);
                        int i3 = query.getInt(8);
                        int i4 = query.getInt(9);
                        Budget budget = new Budget(string, string2, string3, string4, i3, j2, j, DateConverter.toDate(query.isNull(13) ? null : Long.valueOf(query.getLong(13))), DateConverter.toDate(query.isNull(12) ? null : Long.valueOf(query.getLong(12))), query.getInt(10), i4, i2, query.getInt(11));
                        budget.setId(query.getInt(0));
                        arrayList.add(budget);
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

    @Override // com.ktwapps.walletmanager.Database.Dao.BudgetDaoObject
    public int getBudgetTransCount(final int accountId, final int budgetId, final long startDate, final long endDate) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT COUNT(t.id) FROM budgetCategory as bc LEFT JOIN category as c ON bc.category_id = c.id LEFT JOIN trans as t ON t.category_id = c.id WHERE t.account_id = ? AND bc.budget_id = ? AND t.date_time >= ? AND t.date_time < ? GROUP BY bc.budget_id", 4);
        acquire.bindLong(1, accountId);
        acquire.bindLong(2, budgetId);
        acquire.bindLong(3, startDate);
        acquire.bindLong(4, endDate);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            return query.moveToFirst() ? query.getInt(0) : 0;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.BudgetDaoObject
    public Budget getBudgetById(final int id) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT b.id,b.name,b.amount,b.spent,b.account_id as accountId,b.color,GROUP_CONCAT(c.name) as categories,GROUP_CONCAT(c.default_category) as categoriesDefault,b.category_id as categoryId,b.period,b.repeat ,b.status,b.start_date as startDate, b.end_date as endDate FROM budget as b LEFT JOIN budgetCategory as bc ON b.id = bc.budget_id LEFT JOIN category as c ON bc.category_id = c.id WHERE b.id = ? GROUP BY b.id", 1);
        acquire.bindLong(1, id);
        this.__db.assertNotSuspendingTransaction();
        Budget budget = null;
        Long valueOf = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            if (query.moveToFirst()) {
                String string = query.isNull(1) ? null : query.getString(1);
                long j = query.getLong(2);
                long j2 = query.getLong(3);
                int i = query.getInt(4);
                String string2 = query.isNull(5) ? null : query.getString(5);
                String string3 = query.isNull(6) ? null : query.getString(6);
                String string4 = query.isNull(7) ? null : query.getString(7);
                int i2 = query.getInt(8);
                int i3 = query.getInt(9);
                int i4 = query.getInt(10);
                int i5 = query.getInt(11);
                Date date = DateConverter.toDate(query.isNull(12) ? null : Long.valueOf(query.getLong(12)));
                if (!query.isNull(13)) {
                    valueOf = Long.valueOf(query.getLong(13));
                }
                budget = new Budget(string, string2, string3, string4, i2, j2, j, DateConverter.toDate(valueOf), date, i4, i3, i, i5);
                budget.setId(query.getInt(0));
            }
            return budget;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.BudgetDaoObject
    public LiveData<Budget> getLiveBudgetById(final int id) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT b.id,b.name,b.amount,b.spent,b.account_id as accountId,b.color,GROUP_CONCAT(c.name) as categories,GROUP_CONCAT(c.default_category) as categoriesDefault,b.category_id as categoryId,b.period,b.repeat ,b.status,b.start_date as startDate, b.end_date as endDate FROM budget as b LEFT JOIN budgetCategory as bc ON b.id = bc.budget_id LEFT JOIN category as c ON bc.category_id = c.id WHERE b.id = ? GROUP BY b.id", 1);
        acquire.bindLong(1, id);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"budget", "budgetCategory", "category"}, false, new Callable<Budget>() { // from class: com.ktwapps.walletmanager.Database.Dao.BudgetDaoObject_Impl.8
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Budget call() throws Exception {
                Budget budget = null;
                Long valueOf = null;
                Cursor query = DBUtil.query(BudgetDaoObject_Impl.this.__db, acquire, false, null);
                try {
                    if (query.moveToFirst()) {
                        String string = query.isNull(1) ? null : query.getString(1);
                        long j = query.getLong(2);
                        long j2 = query.getLong(3);
                        int i = query.getInt(4);
                        String string2 = query.isNull(5) ? null : query.getString(5);
                        String string3 = query.isNull(6) ? null : query.getString(6);
                        String string4 = query.isNull(7) ? null : query.getString(7);
                        int i2 = query.getInt(8);
                        int i3 = query.getInt(9);
                        int i4 = query.getInt(10);
                        int i5 = query.getInt(11);
                        Date date = DateConverter.toDate(query.isNull(12) ? null : Long.valueOf(query.getLong(12)));
                        if (!query.isNull(13)) {
                            valueOf = Long.valueOf(query.getLong(13));
                        }
                        budget = new Budget(string, string2, string3, string4, i2, j2, j, DateConverter.toDate(valueOf), date, i4, i3, i, i5);
                        budget.setId(query.getInt(0));
                    }
                    return budget;
                } finally {
                    query.close();
                }
            }

            protected void finalize() {
                acquire.release();
            }
        });
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.BudgetDaoObject
    public List<Integer> getBudgetCategoryIds(final int id) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT category_id FROM budgetCategory WHERE budget_id = ?", 1);
        acquire.bindLong(1, id);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                arrayList.add(query.isNull(0) ? null : Integer.valueOf(query.getInt(0)));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.BudgetDaoObject
    public List<Integer> getBudgetIds(final int id) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT budget_id FROM budgetCategory WHERE category_id = ?", 1);
        acquire.bindLong(1, id);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                arrayList.add(query.isNull(0) ? null : Integer.valueOf(query.getInt(0)));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.BudgetDaoObject
    public List<BudgetEntity> getBudgetEntityByCategory(final List<Integer> budgetIds, final int accountId, final int status) {
        StringBuilder newStringBuilder = StringUtil.newStringBuilder();
        newStringBuilder.append("SELECT * FROM budget WHERE (id IN (");
        int size = budgetIds.size();
        StringUtil.appendPlaceholders(newStringBuilder, size);
        newStringBuilder.append(") OR category_id = 0) AND account_id = ");
        newStringBuilder.append("?");
        newStringBuilder.append(" AND status = ");
        newStringBuilder.append("?");
        int i = size + 2;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire(newStringBuilder.toString(), i);
        int i2 = 1;
        for (Integer num : budgetIds) {
            if (num == null) {
                acquire.bindNull(i2);
            } else {
                acquire.bindLong(i2, num.intValue());
            }
            i2++;
        }
        acquire.bindLong(size + 1, accountId);
        acquire.bindLong(i, status);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "name");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "amount");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "spent");
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, NotificationCompat.CATEGORY_STATUS);
            int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, TypedValues.CycleType.S_WAVE_PERIOD);
            int columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, TypedValues.Custom.S_COLOR);
            int columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "repeat");
            int columnIndexOrThrow9 = CursorUtil.getColumnIndexOrThrow(query, "account_id");
            int columnIndexOrThrow10 = CursorUtil.getColumnIndexOrThrow(query, "category_id");
            int columnIndexOrThrow11 = CursorUtil.getColumnIndexOrThrow(query, FirebaseAnalytics.Param.START_DATE);
            int columnIndexOrThrow12 = CursorUtil.getColumnIndexOrThrow(query, FirebaseAnalytics.Param.END_DATE);
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                BudgetEntity budgetEntity = new BudgetEntity(query.isNull(columnIndexOrThrow2) ? null : query.getString(columnIndexOrThrow2), query.isNull(columnIndexOrThrow7) ? null : query.getString(columnIndexOrThrow7), query.getLong(columnIndexOrThrow3), query.getLong(columnIndexOrThrow4), query.getInt(columnIndexOrThrow5), query.getInt(columnIndexOrThrow6), query.getInt(columnIndexOrThrow8), query.getInt(columnIndexOrThrow9), query.getInt(columnIndexOrThrow10), DateConverter.toDate(query.isNull(columnIndexOrThrow11) ? null : Long.valueOf(query.getLong(columnIndexOrThrow11))), DateConverter.toDate(query.isNull(columnIndexOrThrow12) ? null : Long.valueOf(query.getLong(columnIndexOrThrow12))));
                int i3 = columnIndexOrThrow2;
                budgetEntity.setId(query.getInt(columnIndexOrThrow));
                arrayList.add(budgetEntity);
                columnIndexOrThrow2 = i3;
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.BudgetDaoObject
    public BudgetEntity getBudgetEntityById(final int id) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM budget WHERE id = ?", 1);
        acquire.bindLong(1, id);
        this.__db.assertNotSuspendingTransaction();
        BudgetEntity budgetEntity = null;
        Long valueOf = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "name");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "amount");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "spent");
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, NotificationCompat.CATEGORY_STATUS);
            int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, TypedValues.CycleType.S_WAVE_PERIOD);
            int columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, TypedValues.Custom.S_COLOR);
            int columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "repeat");
            int columnIndexOrThrow9 = CursorUtil.getColumnIndexOrThrow(query, "account_id");
            int columnIndexOrThrow10 = CursorUtil.getColumnIndexOrThrow(query, "category_id");
            int columnIndexOrThrow11 = CursorUtil.getColumnIndexOrThrow(query, FirebaseAnalytics.Param.START_DATE);
            int columnIndexOrThrow12 = CursorUtil.getColumnIndexOrThrow(query, FirebaseAnalytics.Param.END_DATE);
            if (query.moveToFirst()) {
                String string = query.isNull(columnIndexOrThrow2) ? null : query.getString(columnIndexOrThrow2);
                long j = query.getLong(columnIndexOrThrow3);
                long j2 = query.getLong(columnIndexOrThrow4);
                int i = query.getInt(columnIndexOrThrow5);
                int i2 = query.getInt(columnIndexOrThrow6);
                String string2 = query.isNull(columnIndexOrThrow7) ? null : query.getString(columnIndexOrThrow7);
                int i3 = query.getInt(columnIndexOrThrow8);
                int i4 = query.getInt(columnIndexOrThrow9);
                int i5 = query.getInt(columnIndexOrThrow10);
                Date date = DateConverter.toDate(query.isNull(columnIndexOrThrow11) ? null : Long.valueOf(query.getLong(columnIndexOrThrow11)));
                if (!query.isNull(columnIndexOrThrow12)) {
                    valueOf = Long.valueOf(query.getLong(columnIndexOrThrow12));
                }
                budgetEntity = new BudgetEntity(string, string2, j, j2, i, i2, i3, i4, i5, date, DateConverter.toDate(valueOf));
                budgetEntity.setId(query.getInt(columnIndexOrThrow));
            }
            return budgetEntity;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.BudgetDaoObject
    public List<BudgetStats> getBudgetStats(final int accountId, final long startDate, final long endDate) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT SUM(ROUND(t.amount*cu.rate)) as amount, CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as day, CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as month FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as cu ON w.currency = cu.code WHERE t.account_id = ? AND t.type = 1 AND w.account_id = ? AND cu.account_id = ? AND t.date_time >= ? AND t.date_time < ? GROUP BY CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int),CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) ORDER BY CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int),CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int)\n", 5);
        long j = accountId;
        acquire.bindLong(1, j);
        acquire.bindLong(2, j);
        acquire.bindLong(3, j);
        acquire.bindLong(4, startDate);
        acquire.bindLong(5, endDate);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                arrayList.add(new BudgetStats(query.getInt(1), query.getInt(2), query.getLong(0)));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.BudgetDaoObject
    public List<BudgetStats> getBudgetStats(final int budgetId, final int accountId, final long startDate, final long endDate) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT SUM(ROUND(t.amount*cu.rate)) as amount, CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as day, CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as month FROM budgetCategory as bc LEFT JOIN category as c ON bc.category_id = c.id LEFT JOIN trans as t ON t.category_id = c.id LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as cu ON w.currency = cu.code WHERE bc.budget_id = ? AND t.account_id = ? AND t.type = 1 AND w.account_id = ? AND cu.account_id = ? AND t.date_time >= ? AND t.date_time < ? GROUP BY CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int),CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) ORDER BY CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int),CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int)\n", 6);
        acquire.bindLong(1, budgetId);
        long j = accountId;
        acquire.bindLong(2, j);
        acquire.bindLong(3, j);
        acquire.bindLong(4, j);
        acquire.bindLong(5, startDate);
        acquire.bindLong(6, endDate);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                arrayList.add(new BudgetStats(query.getInt(1), query.getInt(2), query.getLong(0)));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.BudgetDaoObject
    public List<BudgetTrans> getBudgetTrans(final int accountId, final long startDate, final long endDate) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT c.id,c.color,c.icon,c.default_category as categoryDefault,c.name,COUNT(t.id) as trans,SUM(ROUND(t.amount*cu.rate)) as amount FROM trans as t LEFT JOIN category as c ON t.category_id = c.id LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as cu ON w.currency = cu.code WHERE t.account_id = ? AND t.type = 1 AND w.account_id = ? AND cu.account_id = ? AND t.date_time >= ? AND t.date_time < ? GROUP BY c.id", 5);
        long j = accountId;
        acquire.bindLong(1, j);
        acquire.bindLong(2, j);
        acquire.bindLong(3, j);
        acquire.bindLong(4, startDate);
        acquire.bindLong(5, endDate);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                arrayList.add(new BudgetTrans(query.getInt(0), query.getInt(5), query.getInt(2), query.getLong(6), query.isNull(1) ? null : query.getString(1), query.isNull(4) ? null : query.getString(4), query.getInt(3)));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.BudgetDaoObject
    public List<BudgetTrans> getBudgetTrans(final int accountId, final int budgetId, final long startDate, final long endDate) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT c.id,c.color,c.icon,c.default_category as categoryDefault,c.name,COUNT(t.id) as trans,SUM(ROUND(t.amount*cu.rate)) as amount FROM budgetCategory as bc LEFT JOIN category as c ON bc.category_id = c.id LEFT JOIN trans as t ON t.category_id = c.id LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as cu ON w.currency = cu.code WHERE t.account_id = ? AND bc.budget_id = ? AND t.type = 1 AND w.account_id = ? AND cu.account_id = ? AND t.date_time >= ? AND t.date_time < ? GROUP BY bc.category_id", 6);
        long j = accountId;
        acquire.bindLong(1, j);
        acquire.bindLong(2, budgetId);
        acquire.bindLong(3, j);
        acquire.bindLong(4, j);
        acquire.bindLong(5, startDate);
        acquire.bindLong(6, endDate);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                arrayList.add(new BudgetTrans(query.getInt(0), query.getInt(5), query.getInt(2), query.getLong(6), query.isNull(1) ? null : query.getString(1), query.isNull(4) ? null : query.getString(4), query.getInt(3)));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.BudgetDaoObject
    public long getBudgetSpent(final List<Integer> categoryId, final long startDate, final long endDate, final int type, final int accountId) {
        StringBuilder newStringBuilder = StringUtil.newStringBuilder();
        newStringBuilder.append("SELECT SUM(ROUND(amount*rate)) FROM (SELECT t.amount,c.rate FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as c ON w.currency = c.code WHERE t.category_id IN (");
        int size = categoryId.size();
        StringUtil.appendPlaceholders(newStringBuilder, size);
        newStringBuilder.append(") AND t.date_time >= ");
        newStringBuilder.append("?");
        newStringBuilder.append(" AND t.date_time < ");
        newStringBuilder.append("?");
        newStringBuilder.append(" AND t.type = ");
        newStringBuilder.append("?");
        newStringBuilder.append(" AND t.account_id = ");
        newStringBuilder.append("?");
        newStringBuilder.append(" AND w.account_id = ");
        newStringBuilder.append("?");
        newStringBuilder.append(" AND c.account_id = ");
        newStringBuilder.append("?");
        newStringBuilder.append(") as t1");
        int i = size + 6;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire(newStringBuilder.toString(), i);
        int i2 = 1;
        for (Integer num : categoryId) {
            if (num == null) {
                acquire.bindNull(i2);
            } else {
                acquire.bindLong(i2, num.intValue());
            }
            i2++;
        }
        acquire.bindLong(size + 1, startDate);
        acquire.bindLong(size + 2, endDate);
        acquire.bindLong(size + 3, type);
        long j = accountId;
        acquire.bindLong(size + 4, j);
        acquire.bindLong(size + 5, j);
        acquire.bindLong(i, j);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            return query.moveToFirst() ? query.getLong(0) : 0L;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.BudgetDaoObject
    public long getBudgetSpent(final long startDate, final long endDate, final int type, final int accountId) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT SUM(ROUND(amount*rate)) FROM (SELECT t.amount,c.rate FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as c ON w.currency = c.code WHERE t.date_time >= ? AND t.date_time < ? AND t.type = ? AND t.account_id = ? AND w.account_id = ? AND c.account_id = ?) as t1", 6);
        acquire.bindLong(1, startDate);
        acquire.bindLong(2, endDate);
        acquire.bindLong(3, type);
        long j = accountId;
        acquire.bindLong(4, j);
        acquire.bindLong(5, j);
        acquire.bindLong(6, j);
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
