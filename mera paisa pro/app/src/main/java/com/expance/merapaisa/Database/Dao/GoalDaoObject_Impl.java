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

import com.google.firebase.analytics.FirebaseAnalytics;
import com.expance.merapaisa.Database.Converter.DateConverter;
import com.expance.merapaisa.Database.Entity.GoalEntity;
import com.expance.merapaisa.Database.Entity.GoalTransEntity;
import com.expance.merapaisa.Model.Goal;

import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

/* loaded from: classes3.dex */
public final class GoalDaoObject_Impl implements GoalDaoObject {
    private final RoomDatabase __db;
    private final EntityInsertionAdapter<GoalEntity> __insertionAdapterOfGoalEntity;
    private final EntityInsertionAdapter<GoalTransEntity> __insertionAdapterOfGoalTransEntity;
    private final SharedSQLiteStatement __preparedStmtOfDeleteAllGoalTrans;
    private final SharedSQLiteStatement __preparedStmtOfDeleteGoal;
    private final SharedSQLiteStatement __preparedStmtOfDeleteGoalTrans;
    private final SharedSQLiteStatement __preparedStmtOfUpdateAmount;
    private final EntityDeletionOrUpdateAdapter<GoalEntity> __updateAdapterOfGoalEntity;
    private final EntityDeletionOrUpdateAdapter<GoalTransEntity> __updateAdapterOfGoalTransEntity;

    public GoalDaoObject_Impl(RoomDatabase __db) {
        this.__db = __db;
        this.__insertionAdapterOfGoalEntity = new EntityInsertionAdapter<GoalEntity>(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.GoalDaoObject_Impl.1
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "INSERT OR ABORT INTO `goal` (`id`,`name`,`color`,`saved`,`amount`,`status`,`account_id`,`expect_date`,`achieve_date`,`currency`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
            }

            @Override // androidx.room.EntityInsertionAdapter
            public void bind(SupportSQLiteStatement stmt, GoalEntity value) {
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
                stmt.bindLong(4, value.getSaved());
                stmt.bindLong(5, value.getAmount());
                stmt.bindLong(6, value.getStatus());
                stmt.bindLong(7, value.getAccountId());
                Long fromDate = DateConverter.fromDate(value.getExpectDate());
                if (fromDate == null) {
                    stmt.bindNull(8);
                } else {
                    stmt.bindLong(8, fromDate.longValue());
                }
                Long fromDate2 = DateConverter.fromDate(value.getAchieveDate());
                if (fromDate2 == null) {
                    stmt.bindNull(9);
                } else {
                    stmt.bindLong(9, fromDate2.longValue());
                }
                if (value.getCurrency() == null) {
                    stmt.bindNull(10);
                } else {
                    stmt.bindString(10, value.getCurrency());
                }
            }
        };
        this.__insertionAdapterOfGoalTransEntity = new EntityInsertionAdapter<GoalTransEntity>(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.GoalDaoObject_Impl.2
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "INSERT OR ABORT INTO `goalTrans` (`id`,`amount`,`date_time`,`goal_id`,`type`,`note`) VALUES (nullif(?, 0),?,?,?,?,?)";
            }

            @Override // androidx.room.EntityInsertionAdapter
            public void bind(SupportSQLiteStatement stmt, GoalTransEntity value) {
                stmt.bindLong(1, value.getId());
                stmt.bindLong(2, value.getAmount());
                Long fromDate = DateConverter.fromDate(value.getDateTime());
                if (fromDate == null) {
                    stmt.bindNull(3);
                } else {
                    stmt.bindLong(3, fromDate.longValue());
                }
                stmt.bindLong(4, value.getGoalId());
                stmt.bindLong(5, value.getType());
                if (value.getNote() == null) {
                    stmt.bindNull(6);
                } else {
                    stmt.bindString(6, value.getNote());
                }
            }
        };
        this.__updateAdapterOfGoalEntity = new EntityDeletionOrUpdateAdapter<GoalEntity>(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.GoalDaoObject_Impl.3
            @Override
            // androidx.room.EntityDeletionOrUpdateAdapter, androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "UPDATE OR ABORT `goal` SET `id` = ?,`name` = ?,`color` = ?,`saved` = ?,`amount` = ?,`status` = ?,`account_id` = ?,`expect_date` = ?,`achieve_date` = ?,`currency` = ? WHERE `id` = ?";
            }

            @Override // androidx.room.EntityDeletionOrUpdateAdapter
            public void bind(SupportSQLiteStatement stmt, GoalEntity value) {
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
                stmt.bindLong(4, value.getSaved());
                stmt.bindLong(5, value.getAmount());
                stmt.bindLong(6, value.getStatus());
                stmt.bindLong(7, value.getAccountId());
                Long fromDate = DateConverter.fromDate(value.getExpectDate());
                if (fromDate == null) {
                    stmt.bindNull(8);
                } else {
                    stmt.bindLong(8, fromDate.longValue());
                }
                Long fromDate2 = DateConverter.fromDate(value.getAchieveDate());
                if (fromDate2 == null) {
                    stmt.bindNull(9);
                } else {
                    stmt.bindLong(9, fromDate2.longValue());
                }
                if (value.getCurrency() == null) {
                    stmt.bindNull(10);
                } else {
                    stmt.bindString(10, value.getCurrency());
                }
                stmt.bindLong(11, value.getId());
            }
        };
        this.__updateAdapterOfGoalTransEntity = new EntityDeletionOrUpdateAdapter<GoalTransEntity>(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.GoalDaoObject_Impl.4
            @Override
            // androidx.room.EntityDeletionOrUpdateAdapter, androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "UPDATE OR ABORT `goalTrans` SET `id` = ?,`amount` = ?,`date_time` = ?,`goal_id` = ?,`type` = ?,`note` = ? WHERE `id` = ?";
            }

            @Override // androidx.room.EntityDeletionOrUpdateAdapter
            public void bind(SupportSQLiteStatement stmt, GoalTransEntity value) {
                stmt.bindLong(1, value.getId());
                stmt.bindLong(2, value.getAmount());
                Long fromDate = DateConverter.fromDate(value.getDateTime());
                if (fromDate == null) {
                    stmt.bindNull(3);
                } else {
                    stmt.bindLong(3, fromDate.longValue());
                }
                stmt.bindLong(4, value.getGoalId());
                stmt.bindLong(5, value.getType());
                if (value.getNote() == null) {
                    stmt.bindNull(6);
                } else {
                    stmt.bindString(6, value.getNote());
                }
                stmt.bindLong(7, value.getId());
            }
        };
        this.__preparedStmtOfDeleteGoal = new SharedSQLiteStatement(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.GoalDaoObject_Impl.5
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "DELETE FROM goal WHERE id = ?";
            }
        };
        this.__preparedStmtOfDeleteGoalTrans = new SharedSQLiteStatement(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.GoalDaoObject_Impl.6
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "DELETE FROM goalTrans WHERE id = ?";
            }
        };
        this.__preparedStmtOfDeleteAllGoalTrans = new SharedSQLiteStatement(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.GoalDaoObject_Impl.7
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "DELETE FROM goalTrans WHERE goal_id = ?";
            }
        };
        this.__preparedStmtOfUpdateAmount = new SharedSQLiteStatement(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.GoalDaoObject_Impl.8
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "UPDATE goal SET saved = ? WHERE id = ?";
            }
        };
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.GoalDaoObject
    public void insertGoal(final GoalEntity goalEntity) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfGoalEntity.insert(goalEntity);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.GoalDaoObject
    public void insertGoalEntity(final GoalTransEntity goalTransEntity) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfGoalTransEntity.insert(goalTransEntity);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.GoalDaoObject
    public void update(final GoalEntity goalEntity) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfGoalEntity.handle(goalEntity);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.GoalDaoObject
    public void updateTrans(final GoalTransEntity goalTransEntity) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfGoalTransEntity.handle(goalTransEntity);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.GoalDaoObject
    public void deleteGoal(final int id) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfDeleteGoal.acquire();
        acquire.bindLong(1, id);
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfDeleteGoal.release(acquire);
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.GoalDaoObject
    public void deleteGoalTrans(final int id) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfDeleteGoalTrans.acquire();
        acquire.bindLong(1, id);
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfDeleteGoalTrans.release(acquire);
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.GoalDaoObject
    public void deleteAllGoalTrans(final int id) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfDeleteAllGoalTrans.acquire();
        acquire.bindLong(1, id);
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfDeleteAllGoalTrans.release(acquire);
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.GoalDaoObject
    public void updateAmount(final int id, final double saved) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfUpdateAmount.acquire();
        acquire.bindDouble(1, saved);
        acquire.bindLong(2, id);
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfUpdateAmount.release(acquire);
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.GoalDaoObject
    public LiveData<List<Goal>> getGoal(final int accountId, final int status) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT id,currency,name,color,saved,amount,expect_date as expectDate,achieve_date as achieveDate, status FROM goal WHERE account_id = ? AND status = ?", 2);
        acquire.bindLong(1, accountId);
        acquire.bindLong(2, status);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"goal"}, false, new Callable<List<Goal>>() { // from class: com.ktwapps.walletmanager.Database.Dao.GoalDaoObject_Impl.9
            @Override // java.util.concurrent.Callable
            public List<Goal> call() throws Exception {
                Cursor query = DBUtil.query(GoalDaoObject_Impl.this.__db, acquire, false, null);
                try {
                    ArrayList arrayList = new ArrayList(query.getCount());
                    while (query.moveToNext()) {
                        int i = query.getInt(0);
                        String string = query.isNull(1) ? null : query.getString(1);
                        arrayList.add(new Goal(i, query.getInt(8), query.isNull(2) ? null : query.getString(2), query.isNull(3) ? null : query.getString(3), query.getLong(4), query.getLong(5), DateConverter.toDate(query.isNull(6) ? null : Long.valueOf(query.getLong(6))), DateConverter.toDate(query.isNull(7) ? null : Long.valueOf(query.getLong(7))), string));
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

    @Override // com.ktwapps.walletmanager.Database.Dao.GoalDaoObject
    public LiveData<List<Goal>> getGoalAchieved(final int accountId, final int status) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT id,currency,name,color,saved,amount,expect_date as expectDate,achieve_date as achieveDate, status FROM goal WHERE account_id = ? AND status = ? ORDER BY achieve_date DESC", 2);
        acquire.bindLong(1, accountId);
        acquire.bindLong(2, status);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"goal"}, false, new Callable<List<Goal>>() { // from class: com.ktwapps.walletmanager.Database.Dao.GoalDaoObject_Impl.10
            @Override // java.util.concurrent.Callable
            public List<Goal> call() throws Exception {
                Cursor query = DBUtil.query(GoalDaoObject_Impl.this.__db, acquire, false, null);
                try {
                    ArrayList arrayList = new ArrayList(query.getCount());
                    while (query.moveToNext()) {
                        int i = query.getInt(0);
                        String string = query.isNull(1) ? null : query.getString(1);
                        arrayList.add(new Goal(i, query.getInt(8), query.isNull(2) ? null : query.getString(2), query.isNull(3) ? null : query.getString(3), query.getLong(4), query.getLong(5), DateConverter.toDate(query.isNull(6) ? null : Long.valueOf(query.getLong(6))), DateConverter.toDate(query.isNull(7) ? null : Long.valueOf(query.getLong(7))), string));
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

    @Override // com.ktwapps.walletmanager.Database.Dao.GoalDaoObject
    public GoalEntity getGoalById(final int id) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM goal WHERE id = ?", 1);
        acquire.bindLong(1, id);
        this.__db.assertNotSuspendingTransaction();
        GoalEntity goalEntity = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "name");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, TypedValues.Custom.S_COLOR);
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "saved");
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "amount");
            int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, NotificationCompat.CATEGORY_STATUS);
            int columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "account_id");
            int columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "expect_date");
            int columnIndexOrThrow9 = CursorUtil.getColumnIndexOrThrow(query, "achieve_date");
            int columnIndexOrThrow10 = CursorUtil.getColumnIndexOrThrow(query, FirebaseAnalytics.Param.CURRENCY);
            if (query.moveToFirst()) {
                goalEntity = new GoalEntity(query.isNull(columnIndexOrThrow2) ? null : query.getString(columnIndexOrThrow2), query.isNull(columnIndexOrThrow3) ? null : query.getString(columnIndexOrThrow3), query.getLong(columnIndexOrThrow4), query.getLong(columnIndexOrThrow5), query.getInt(columnIndexOrThrow6), query.getInt(columnIndexOrThrow7), DateConverter.toDate(query.isNull(columnIndexOrThrow8) ? null : Long.valueOf(query.getLong(columnIndexOrThrow8))), DateConverter.toDate(query.isNull(columnIndexOrThrow9) ? null : Long.valueOf(query.getLong(columnIndexOrThrow9))), query.isNull(columnIndexOrThrow10) ? null : query.getString(columnIndexOrThrow10));
                goalEntity.setId(query.getInt(columnIndexOrThrow));
            }
            return goalEntity;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.GoalDaoObject
    public LiveData<GoalEntity> getLiveGoalById(final int id) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM goal WHERE id = ?", 1);
        acquire.bindLong(1, id);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"goal"}, false, new Callable<GoalEntity>() { // from class: com.ktwapps.walletmanager.Database.Dao.GoalDaoObject_Impl.11
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public GoalEntity call() throws Exception {
                GoalEntity goalEntity = null;
                Cursor query = DBUtil.query(GoalDaoObject_Impl.this.__db, acquire, false, null);
                try {
                    int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
                    int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "name");
                    int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, TypedValues.Custom.S_COLOR);
                    int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "saved");
                    int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "amount");
                    int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, NotificationCompat.CATEGORY_STATUS);
                    int columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "account_id");
                    int columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "expect_date");
                    int columnIndexOrThrow9 = CursorUtil.getColumnIndexOrThrow(query, "achieve_date");
                    int columnIndexOrThrow10 = CursorUtil.getColumnIndexOrThrow(query, FirebaseAnalytics.Param.CURRENCY);
                    if (query.moveToFirst()) {
                        goalEntity = new GoalEntity(query.isNull(columnIndexOrThrow2) ? null : query.getString(columnIndexOrThrow2), query.isNull(columnIndexOrThrow3) ? null : query.getString(columnIndexOrThrow3), query.getLong(columnIndexOrThrow4), query.getLong(columnIndexOrThrow5), query.getInt(columnIndexOrThrow6), query.getInt(columnIndexOrThrow7), DateConverter.toDate(query.isNull(columnIndexOrThrow8) ? null : Long.valueOf(query.getLong(columnIndexOrThrow8))), DateConverter.toDate(query.isNull(columnIndexOrThrow9) ? null : Long.valueOf(query.getLong(columnIndexOrThrow9))), query.isNull(columnIndexOrThrow10) ? null : query.getString(columnIndexOrThrow10));
                        goalEntity.setId(query.getInt(columnIndexOrThrow));
                    }
                    return goalEntity;
                } finally {
                    query.close();
                }
            }

            protected void finalize() {
                acquire.release();
            }
        });
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.GoalDaoObject
    public LiveData<List<GoalTransEntity>> getGoalTrans(final int goalId) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM goalTrans WHERE goal_id = ? ORDER BY date_time DESC", 1);
        acquire.bindLong(1, goalId);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"goalTrans"}, false, new Callable<List<GoalTransEntity>>() { // from class: com.ktwapps.walletmanager.Database.Dao.GoalDaoObject_Impl.12
            @Override // java.util.concurrent.Callable
            public List<GoalTransEntity> call() throws Exception {
                Cursor query = DBUtil.query(GoalDaoObject_Impl.this.__db, acquire, false, null);
                try {
                    int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
                    int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "amount");
                    int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "date_time");
                    int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "goal_id");
                    int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, JamXmlElements.TYPE);
                    int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "note");
                    ArrayList arrayList = new ArrayList(query.getCount());
                    while (query.moveToNext()) {
                        GoalTransEntity goalTransEntity = new GoalTransEntity(query.getLong(columnIndexOrThrow2), DateConverter.toDate(query.isNull(columnIndexOrThrow3) ? null : Long.valueOf(query.getLong(columnIndexOrThrow3))), query.getInt(columnIndexOrThrow4), query.getInt(columnIndexOrThrow5), query.isNull(columnIndexOrThrow6) ? null : query.getString(columnIndexOrThrow6));
                        goalTransEntity.setId(query.getInt(columnIndexOrThrow));
                        arrayList.add(goalTransEntity);
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

    @Override // com.ktwapps.walletmanager.Database.Dao.GoalDaoObject
    public GoalTransEntity getGoalTransById(final int id) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM goalTrans WHERE id = ?", 1);
        acquire.bindLong(1, id);
        this.__db.assertNotSuspendingTransaction();
        GoalTransEntity goalTransEntity = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "amount");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "date_time");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "goal_id");
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, JamXmlElements.TYPE);
            int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "note");
            if (query.moveToFirst()) {
                goalTransEntity = new GoalTransEntity(query.getLong(columnIndexOrThrow2), DateConverter.toDate(query.isNull(columnIndexOrThrow3) ? null : Long.valueOf(query.getLong(columnIndexOrThrow3))), query.getInt(columnIndexOrThrow4), query.getInt(columnIndexOrThrow5), query.isNull(columnIndexOrThrow6) ? null : query.getString(columnIndexOrThrow6));
                goalTransEntity.setId(query.getInt(columnIndexOrThrow));
            }
            return goalTransEntity;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public static List<Class<?>> getRequiredConverters() {
        return Collections.emptyList();
    }
}
