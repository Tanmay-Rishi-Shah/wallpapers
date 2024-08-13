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
import com.expance.merapaisa.Database.Converter.DateConverter;
import com.expance.merapaisa.Database.Entity.CategoryEntity;
import com.expance.merapaisa.Database.Entity.SubcategoryEntity;
import com.expance.merapaisa.Model.Category;
import com.expance.merapaisa.Model.DailyTrans;
import com.expance.merapaisa.Model.Trans;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

/* loaded from: classes3.dex */
public final class CategoryDaoObject_Impl implements CategoryDaoObject {
    private final RoomDatabase __db;
    private final EntityInsertionAdapter<CategoryEntity> __insertionAdapterOfCategoryEntity;
    private final EntityInsertionAdapter<SubcategoryEntity> __insertionAdapterOfSubcategoryEntity;
    private final SharedSQLiteStatement __preparedStmtOfDeleteSubcategoryById;
    private final SharedSQLiteStatement __preparedStmtOfUpdateCategoryOrdering;
    private final SharedSQLiteStatement __preparedStmtOfUpdateStatus;
    private final SharedSQLiteStatement __preparedStmtOfUpdateSubcategoryOrdering;
    private final EntityDeletionOrUpdateAdapter<CategoryEntity> __updateAdapterOfCategoryEntity;
    private final EntityDeletionOrUpdateAdapter<SubcategoryEntity> __updateAdapterOfSubcategoryEntity;

    public CategoryDaoObject_Impl(RoomDatabase __db) {
        this.__db = __db;
        this.__insertionAdapterOfCategoryEntity = new EntityInsertionAdapter<CategoryEntity>(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.CategoryDaoObject_Impl.1
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "INSERT OR ABORT INTO `category` (`id`,`name`,`color`,`type`,`active`,`ordering`,`icon`,`account_id`,`default_category`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
            }

            @Override // androidx.room.EntityInsertionAdapter
            public void bind(SupportSQLiteStatement stmt, CategoryEntity value) {
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
                stmt.bindLong(4, value.getType());
                stmt.bindLong(5, value.getActive());
                stmt.bindLong(6, value.getOrdering());
                stmt.bindLong(7, value.getIcon());
                stmt.bindLong(8, value.getAccountId());
                stmt.bindLong(9, value.getDefaultCategory());
            }
        };
        this.__insertionAdapterOfSubcategoryEntity = new EntityInsertionAdapter<SubcategoryEntity>(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.CategoryDaoObject_Impl.2
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "INSERT OR ABORT INTO `subcategory` (`id`,`category_id`,`name`,`ordering`) VALUES (nullif(?, 0),?,?,?)";
            }

            @Override // androidx.room.EntityInsertionAdapter
            public void bind(SupportSQLiteStatement stmt, SubcategoryEntity value) {
                stmt.bindLong(1, value.getId());
                stmt.bindLong(2, value.getCategoryId());
                if (value.getName() == null) {
                    stmt.bindNull(3);
                } else {
                    stmt.bindString(3, value.getName());
                }
                stmt.bindLong(4, value.getOrdering());
            }
        };
        this.__updateAdapterOfCategoryEntity = new EntityDeletionOrUpdateAdapter<CategoryEntity>(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.CategoryDaoObject_Impl.3
            @Override // androidx.room.EntityDeletionOrUpdateAdapter, androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "UPDATE OR ABORT `category` SET `id` = ?,`name` = ?,`color` = ?,`type` = ?,`active` = ?,`ordering` = ?,`icon` = ?,`account_id` = ?,`default_category` = ? WHERE `id` = ?";
            }

            @Override // androidx.room.EntityDeletionOrUpdateAdapter
            public void bind(SupportSQLiteStatement stmt, CategoryEntity value) {
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
                stmt.bindLong(4, value.getType());
                stmt.bindLong(5, value.getActive());
                stmt.bindLong(6, value.getOrdering());
                stmt.bindLong(7, value.getIcon());
                stmt.bindLong(8, value.getAccountId());
                stmt.bindLong(9, value.getDefaultCategory());
                stmt.bindLong(10, value.getId());
            }
        };
        this.__updateAdapterOfSubcategoryEntity = new EntityDeletionOrUpdateAdapter<SubcategoryEntity>(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.CategoryDaoObject_Impl.4
            @Override // androidx.room.EntityDeletionOrUpdateAdapter, androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "UPDATE OR ABORT `subcategory` SET `id` = ?,`category_id` = ?,`name` = ?,`ordering` = ? WHERE `id` = ?";
            }

            @Override // androidx.room.EntityDeletionOrUpdateAdapter
            public void bind(SupportSQLiteStatement stmt, SubcategoryEntity value) {
                stmt.bindLong(1, value.getId());
                stmt.bindLong(2, value.getCategoryId());
                if (value.getName() == null) {
                    stmt.bindNull(3);
                } else {
                    stmt.bindString(3, value.getName());
                }
                stmt.bindLong(4, value.getOrdering());
                stmt.bindLong(5, value.getId());
            }
        };
        this.__preparedStmtOfDeleteSubcategoryById = new SharedSQLiteStatement(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.CategoryDaoObject_Impl.5
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "DELETE FROM subcategory WHERE id = ?";
            }
        };
        this.__preparedStmtOfUpdateSubcategoryOrdering = new SharedSQLiteStatement(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.CategoryDaoObject_Impl.6
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "UPDATE subcategory SET ordering = ? WHERE id = ?";
            }
        };
        this.__preparedStmtOfUpdateCategoryOrdering = new SharedSQLiteStatement(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.CategoryDaoObject_Impl.7
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "UPDATE category SET ordering = ? WHERE id = ?";
            }
        };
        this.__preparedStmtOfUpdateStatus = new SharedSQLiteStatement(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.CategoryDaoObject_Impl.8
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "UPDATE category SET active = ? WHERE id = ?";
            }
        };
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.CategoryDaoObject
    public long[] insertCategory(final CategoryEntity... categoryEntities) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            long[] insertAndReturnIdsArray = this.__insertionAdapterOfCategoryEntity.insertAndReturnIdsArray(categoryEntities);
            this.__db.setTransactionSuccessful();
            return insertAndReturnIdsArray;
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.CategoryDaoObject
    public long insertSubcategory(final SubcategoryEntity subCategoryEntity) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            long insertAndReturnId = this.__insertionAdapterOfSubcategoryEntity.insertAndReturnId(subCategoryEntity);
            this.__db.setTransactionSuccessful();
            return insertAndReturnId;
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.CategoryDaoObject
    public void updateCategory(final CategoryEntity categoryEntity) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfCategoryEntity.handle(categoryEntity);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.CategoryDaoObject
    public void updateSubcategory(final SubcategoryEntity subCategoryEntity) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfSubcategoryEntity.handle(subCategoryEntity);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.CategoryDaoObject
    public void deleteSubcategoryById(final int id) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfDeleteSubcategoryById.acquire();
        acquire.bindLong(1, id);
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfDeleteSubcategoryById.release(acquire);
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.CategoryDaoObject
    public void updateSubcategoryOrdering(final int ordering, final int id) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfUpdateSubcategoryOrdering.acquire();
        acquire.bindLong(1, ordering);
        acquire.bindLong(2, id);
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfUpdateSubcategoryOrdering.release(acquire);
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.CategoryDaoObject
    public void updateCategoryOrdering(final int ordering, final int id) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfUpdateCategoryOrdering.acquire();
        acquire.bindLong(1, ordering);
        acquire.bindLong(2, id);
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfUpdateCategoryOrdering.release(acquire);
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.CategoryDaoObject
    public void updateStatus(final int id, final int status) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfUpdateStatus.acquire();
        acquire.bindLong(1, status);
        acquire.bindLong(2, id);
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfUpdateStatus.release(acquire);
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.CategoryDaoObject
    public Category[] getCategory(final int type, final int active, final int accountId) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT id, name, icon ,color, categoryDefault, COUNT(subcategoryId) as subcategoryCount, GROUP_CONCAT(subcategoryName,', ') as subcategory, GROUP_CONCAT(subcategoryId) as subcategoryIds, GROUP_CONCAT(subcategoryOrdering) as subcategoryOrdering FROM (SELECT c.id, c.name, c.icon, c.color, c.ordering, c.default_category as categoryDefault, sc.name as subcategoryName, sc.id as subcategoryId, sc.ordering as subcategoryOrdering FROM category as c LEFT JOIN (SELECT * FROM subcategory ORDER BY ordering) as sc ON c.id = sc.category_id WHERE c.type = ? AND c.active = ? AND c.account_id = ? ORDER BY c.ordering ASC, sc.ordering ASC) GROUP BY id ORDER BY ordering", 3);
        acquire.bindLong(1, type);
        acquire.bindLong(2, active);
        acquire.bindLong(3, accountId);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            Category[] categoryArr = new Category[query.getCount()];
            int i = 0;
            while (query.moveToNext()) {
                categoryArr[i] = new Category(query.getInt(0), query.getInt(2), query.isNull(1) ? null : query.getString(1), query.isNull(3) ? null : query.getString(3), query.getInt(4), query.getInt(5), query.isNull(6) ? null : query.getString(6), query.isNull(7) ? null : query.getString(7), query.isNull(8) ? null : query.getString(8));
                i++;
            }
            return categoryArr;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.CategoryDaoObject
    public int getFeeCategoryId(final int accountId) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT id FROM category WHERE type = 1 AND active = 0 AND account_id = ? ORDER BY ordering DESC LIMIT 1", 1);
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

    @Override // com.ktwapps.walletmanager.Database.Dao.CategoryDaoObject
    public LiveData<List<Category>> getLiveCategory(final int type, final int active, final int accountId) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT id, name, icon ,color, categoryDefault, COUNT(subcategoryId) as subcategoryCount, GROUP_CONCAT(subcategoryName,', ') as subcategory, GROUP_CONCAT(subcategoryId) as subcategoryIds, GROUP_CONCAT(subcategoryOrdering) as subcategoryOrdering FROM (SELECT c.id, c.name, c.icon, c.color, c.ordering, c.default_category as categoryDefault, sc.name as subcategoryName, sc.id as subcategoryId, sc.ordering as subcategoryOrdering FROM category as c LEFT JOIN (SELECT * FROM subcategory ORDER BY ordering) as sc ON c.id = sc.category_id WHERE c.type = ? AND c.active = ? AND c.account_id = ? ORDER BY c.ordering ASC, sc.ordering ASC) GROUP BY id ORDER BY ordering", 3);
        acquire.bindLong(1, type);
        acquire.bindLong(2, active);
        acquire.bindLong(3, accountId);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"category", "subcategory"}, false, new Callable<List<Category>>() { // from class: com.ktwapps.walletmanager.Database.Dao.CategoryDaoObject_Impl.9
            @Override // java.util.concurrent.Callable
            public List<Category> call() throws Exception {
                Cursor query = DBUtil.query(CategoryDaoObject_Impl.this.__db, acquire, false, null);
                try {
                    ArrayList arrayList = new ArrayList(query.getCount());
                    while (query.moveToNext()) {
                        arrayList.add(new Category(query.getInt(0), query.getInt(2), query.isNull(1) ? null : query.getString(1), query.isNull(3) ? null : query.getString(3), query.getInt(4), query.getInt(5), query.isNull(6) ? null : query.getString(6), query.isNull(7) ? null : query.getString(7), query.isNull(8) ? null : query.getString(8)));
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

    @Override // com.ktwapps.walletmanager.Database.Dao.CategoryDaoObject
    public List<Category> getSearchCategory(final int active, final int accountId) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT c.id, c.name, c.icon ,c.color, c.default_category as categoryDefault, COUNT(sc.id) as subcategoryCount, GROUP_CONCAT(sc.name,', ') as subcategory, GROUP_CONCAT(sc.id) as subcategoryIds, GROUP_CONCAT(sc.ordering) as subcategoryOrdering FROM category as c LEFT JOIN (SELECT * FROM subcategory ORDER BY ordering DESC) as sc ON c.id = sc.category_id WHERE c.active = ? AND c.account_id = ? GROUP BY c.id ORDER BY c.type ASC, c.ordering ASC", 2);
        acquire.bindLong(1, active);
        acquire.bindLong(2, accountId);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                arrayList.add(new Category(query.getInt(0), query.getInt(2), query.isNull(1) ? null : query.getString(1), query.isNull(3) ? null : query.getString(3), query.getInt(4), query.getInt(5), query.isNull(6) ? null : query.getString(6), query.isNull(7) ? null : query.getString(7), query.isNull(8) ? null : query.getString(8)));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.CategoryDaoObject
    public List<SubcategoryEntity> getSubCategoryByCategoryId(final int id) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM subcategory WHERE category_id = ? ORDER BY ordering", 1);
        acquire.bindLong(1, id);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "category_id");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "name");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "ordering");
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                SubcategoryEntity subcategoryEntity = new SubcategoryEntity(query.getInt(columnIndexOrThrow2), query.isNull(columnIndexOrThrow3) ? null : query.getString(columnIndexOrThrow3), query.getInt(columnIndexOrThrow4));
                subcategoryEntity.setId(query.getInt(columnIndexOrThrow));
                arrayList.add(subcategoryEntity);
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.CategoryDaoObject
    public SubcategoryEntity getSubcategoryById(final int id) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM subcategory WHERE id = ?", 1);
        acquire.bindLong(1, id);
        this.__db.assertNotSuspendingTransaction();
        SubcategoryEntity subcategoryEntity = null;
        String string = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "category_id");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "name");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "ordering");
            if (query.moveToFirst()) {
                int i = query.getInt(columnIndexOrThrow2);
                if (!query.isNull(columnIndexOrThrow3)) {
                    string = query.getString(columnIndexOrThrow3);
                }
                SubcategoryEntity subcategoryEntity2 = new SubcategoryEntity(i, string, query.getInt(columnIndexOrThrow4));
                subcategoryEntity2.setId(query.getInt(columnIndexOrThrow));
                subcategoryEntity = subcategoryEntity2;
            }
            return subcategoryEntity;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.CategoryDaoObject
    public int getTotalSubcategoryByCategoryId(final int categoryId) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT COUNT(*) FROM subcategory WHERE category_id = ?", 1);
        acquire.bindLong(1, categoryId);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            return query.moveToFirst() ? query.getInt(0) : 0;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.CategoryDaoObject
    public int getSubcategoryLastOrdering(final int id) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT ordering FROM subcategory WHERE category_id = ? ORDER BY ordering DESC LIMIT 1", 1);
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

    @Override // com.ktwapps.walletmanager.Database.Dao.CategoryDaoObject
    public int getCategoryLastOrdering(final int accountId, final int type) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT ordering FROM category WHERE account_id = ? AND type = ? ORDER BY ordering DESC LIMIT 1", 2);
        acquire.bindLong(1, accountId);
        acquire.bindLong(2, type);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            return query.moveToFirst() ? query.getInt(0) : 0;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.CategoryDaoObject
    public CategoryEntity getCategoryById(final int id) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM category WHERE id = ?", 1);
        acquire.bindLong(1, id);
        this.__db.assertNotSuspendingTransaction();
        CategoryEntity categoryEntity = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "name");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, TypedValues.Custom.S_COLOR);
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, JamXmlElements.TYPE);
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, AppMeasurementSdk.ConditionalUserProperty.ACTIVE);
            int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "ordering");
            int columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "icon");
            int columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "account_id");
            int columnIndexOrThrow9 = CursorUtil.getColumnIndexOrThrow(query, "default_category");
            if (query.moveToFirst()) {
                categoryEntity = new CategoryEntity(query.isNull(columnIndexOrThrow2) ? null : query.getString(columnIndexOrThrow2), query.isNull(columnIndexOrThrow3) ? null : query.getString(columnIndexOrThrow3), query.getInt(columnIndexOrThrow7), query.getInt(columnIndexOrThrow4), query.getInt(columnIndexOrThrow5), query.getInt(columnIndexOrThrow6), query.getInt(columnIndexOrThrow8), query.getInt(columnIndexOrThrow9));
                categoryEntity.setId(query.getInt(columnIndexOrThrow));
            }
            return categoryEntity;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.CategoryDaoObject
    public int getAdjustmentId(final int accountId, final int type) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT id FROM category WHERE account_id = ? AND default_category = 24 AND type = ?", 2);
        acquire.bindLong(1, accountId);
        acquire.bindLong(2, type);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            return query.moveToFirst() ? query.getInt(0) : 0;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.CategoryDaoObject
    public List<DailyTrans> getDailyTrans(final int accountId, final int categoryId, final long startDate, final long endDate) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT SUM(ROUND(t.amount*c.rate)) as amount, CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as day,CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as month,CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) as year FROM trans as t LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as c ON w.currency = c.code WHERE t.account_id = ? AND t.date_time >= ? AND t.date_time < ? AND t.category_id = ? AND c.account_id = ? AND w.account_id = ? GROUP BY CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int),  CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) ORDER BY CAST(strftime('%Y', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%m', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC, CAST(strftime('%d', datetime(t.date_time/1000, 'unixepoch','localtime')) AS int) DESC", 6);
        long j = accountId;
        acquire.bindLong(1, j);
        acquire.bindLong(2, startDate);
        acquire.bindLong(3, endDate);
        acquire.bindLong(4, categoryId);
        acquire.bindLong(5, j);
        acquire.bindLong(6, j);
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

    @Override // com.ktwapps.walletmanager.Database.Dao.CategoryDaoObject
    public List<Trans> getTransFromDate(final int accountId, final int categoryId, final long startDate, final long endDate) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT d.id as debtId, d.color as debtColor,d.type as debtType,dt.id as debtTransId, dt.type as debtTransType,t.id,t.note,t.memo,t.trans_amount as transAmount, t.subcategory_id as subcategoryId, sc.name as subcategory,c.icon,c.color,c.default_category as categoryDefault,t.fee_id as feeId,cu.code as currency,t.amount,t.date_time as dateTime,t.type,w.name as wallet, tw.name as transferWallet, t.wallet_id as walletId, t.transfer_wallet_id as transferWalletId ,c.name as category, c.id as categoryId, GROUP_CONCAT(m.path) as media FROM trans t LEFT JOIN category as c ON t.category_id = c.id LEFT JOIN wallet as w ON t.wallet_id = w.id LEFT JOIN currency as cu ON w.currency = cu.code LEFT JOIN wallet as tw ON t.transfer_wallet_id = tw.id LEFT JOIN media as m ON m.trans_id = t.id LEFT JOIN debt as d ON t.debt_id = d.id LEFT JOIN debtTrans as dt ON t.debt_trans_id = dt.id LEFT JOIN subcategory as sc ON t.subcategory_id = sc.id WHERE t.date_time >= ? AND t.date_time < ? AND t.account_id = ? AND t.category_id = ? AND cu.account_id = ? GROUP BY t.id ORDER BY t.date_time DESC", 5);
        acquire.bindLong(1, startDate);
        acquire.bindLong(2, endDate);
        long j = accountId;
        acquire.bindLong(3, j);
        acquire.bindLong(4, categoryId);
        acquire.bindLong(5, j);
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
                Trans trans = new Trans(string2, string3, string5, i6, query.isNull(15) ? null : query.getString(15), DateConverter.toDate(query.isNull(17) ? null : Long.valueOf(query.getLong(17))), query.getLong(16), query.isNull(19) ? null : query.getString(19), query.getInt(18), query.isNull(20) ? null : query.getString(20), query.getInt(21), query.getInt(22), query.isNull(23) ? null : query.getString(23), query.getInt(24), i7, i8, query.isNull(25) ? null : query.getString(25), j2, string, i, i2, i3, i4, string4, i5);
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
