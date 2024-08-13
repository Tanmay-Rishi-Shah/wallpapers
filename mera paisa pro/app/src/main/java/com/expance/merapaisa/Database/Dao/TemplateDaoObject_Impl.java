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

import com.expance.merapaisa.Database.Entity.TemplateEntity;
import com.expance.merapaisa.Model.Template;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

/* loaded from: classes3.dex */
public final class TemplateDaoObject_Impl implements TemplateDaoObject {
    private final RoomDatabase __db;
    private final EntityInsertionAdapter<TemplateEntity> __insertionAdapterOfTemplateEntity;
    private final SharedSQLiteStatement __preparedStmtOfDeleteTemplate;
    private final SharedSQLiteStatement __preparedStmtOfDeleteTemplateByCategoryId;
    private final SharedSQLiteStatement __preparedStmtOfDeleteTemplateByWalletId;
    private final SharedSQLiteStatement __preparedStmtOfRemoveSubcategory;
    private final SharedSQLiteStatement __preparedStmtOfUpdateTemplateOrdering;
    private final EntityDeletionOrUpdateAdapter<TemplateEntity> __updateAdapterOfTemplateEntity;

    public TemplateDaoObject_Impl(RoomDatabase __db) {
        this.__db = __db;
        this.__insertionAdapterOfTemplateEntity = new EntityInsertionAdapter<TemplateEntity>(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.TemplateDaoObject_Impl.1
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "INSERT OR ABORT INTO `template` (`id`,`name`,`note`,`memo`,`amount`,`category_id`,`subcategory_id`,`wallet_id`,`account_id`,`ordering`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
            }

            @Override // androidx.room.EntityInsertionAdapter
            public void bind(SupportSQLiteStatement stmt, TemplateEntity value) {
                stmt.bindLong(1, value.getId());
                if (value.getName() == null) {
                    stmt.bindNull(2);
                } else {
                    stmt.bindString(2, value.getName());
                }
                if (value.getNote() == null) {
                    stmt.bindNull(3);
                } else {
                    stmt.bindString(3, value.getNote());
                }
                if (value.getMemo() == null) {
                    stmt.bindNull(4);
                } else {
                    stmt.bindString(4, value.getMemo());
                }
                stmt.bindLong(5, value.getAmount());
                stmt.bindLong(6, value.getCategoryId());
                stmt.bindLong(7, value.getSubcategoryId());
                stmt.bindLong(8, value.getWalletId());
                stmt.bindLong(9, value.getAccountId());
                stmt.bindLong(10, value.getOrdering());
            }
        };
        this.__updateAdapterOfTemplateEntity = new EntityDeletionOrUpdateAdapter<TemplateEntity>(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.TemplateDaoObject_Impl.2
            @Override
            // androidx.room.EntityDeletionOrUpdateAdapter, androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "UPDATE OR ABORT `template` SET `id` = ?,`name` = ?,`note` = ?,`memo` = ?,`amount` = ?,`category_id` = ?,`subcategory_id` = ?,`wallet_id` = ?,`account_id` = ?,`ordering` = ? WHERE `id` = ?";
            }

            @Override // androidx.room.EntityDeletionOrUpdateAdapter
            public void bind(SupportSQLiteStatement stmt, TemplateEntity value) {
                stmt.bindLong(1, value.getId());
                if (value.getName() == null) {
                    stmt.bindNull(2);
                } else {
                    stmt.bindString(2, value.getName());
                }
                if (value.getNote() == null) {
                    stmt.bindNull(3);
                } else {
                    stmt.bindString(3, value.getNote());
                }
                if (value.getMemo() == null) {
                    stmt.bindNull(4);
                } else {
                    stmt.bindString(4, value.getMemo());
                }
                stmt.bindLong(5, value.getAmount());
                stmt.bindLong(6, value.getCategoryId());
                stmt.bindLong(7, value.getSubcategoryId());
                stmt.bindLong(8, value.getWalletId());
                stmt.bindLong(9, value.getAccountId());
                stmt.bindLong(10, value.getOrdering());
                stmt.bindLong(11, value.getId());
            }
        };
        this.__preparedStmtOfDeleteTemplate = new SharedSQLiteStatement(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.TemplateDaoObject_Impl.3
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "DELETE FROM template WHERE id = ?";
            }
        };
        this.__preparedStmtOfUpdateTemplateOrdering = new SharedSQLiteStatement(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.TemplateDaoObject_Impl.4
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "UPDATE template SET ordering = ? WHERE id = ?";
            }
        };
        this.__preparedStmtOfRemoveSubcategory = new SharedSQLiteStatement(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.TemplateDaoObject_Impl.5
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "UPDATE template SET subcategory_id = 0 WHERE subcategory_id = ?";
            }
        };
        this.__preparedStmtOfDeleteTemplateByCategoryId = new SharedSQLiteStatement(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.TemplateDaoObject_Impl.6
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "DELETE FROM template WHERE category_id = ?";
            }
        };
        this.__preparedStmtOfDeleteTemplateByWalletId = new SharedSQLiteStatement(__db) { // from class: com.ktwapps.walletmanager.Database.Dao.TemplateDaoObject_Impl.7
            @Override // androidx.room.SharedSQLiteStatement
            public String createQuery() {
                return "DELETE FROM template WHERE wallet_id = ?";
            }
        };
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.TemplateDaoObject
    public void insertTemplate(final TemplateEntity templateEntity) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfTemplateEntity.insert(templateEntity);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.TemplateDaoObject
    public void updateTemplate(final TemplateEntity templateEntity) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfTemplateEntity.handle(templateEntity);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.TemplateDaoObject
    public void deleteTemplate(final int id) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfDeleteTemplate.acquire();
        acquire.bindLong(1, id);
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfDeleteTemplate.release(acquire);
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.TemplateDaoObject
    public void updateTemplateOrdering(final int order, final int id) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfUpdateTemplateOrdering.acquire();
        acquire.bindLong(1, order);
        acquire.bindLong(2, id);
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfUpdateTemplateOrdering.release(acquire);
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.TemplateDaoObject
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

    @Override // com.ktwapps.walletmanager.Database.Dao.TemplateDaoObject
    public void deleteTemplateByCategoryId(final int categoryId) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfDeleteTemplateByCategoryId.acquire();
        acquire.bindLong(1, categoryId);
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfDeleteTemplateByCategoryId.release(acquire);
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.TemplateDaoObject
    public void deleteTemplateByWalletId(final int walletId) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfDeleteTemplateByWalletId.acquire();
        acquire.bindLong(1, walletId);
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfDeleteTemplateByWalletId.release(acquire);
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.TemplateDaoObject
    public int getTemplateLastOrdering(final int accountId) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT ordering FROM template WHERE account_id = ? ORDER BY ordering DESC LIMIT 1", 1);
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

    @Override // com.ktwapps.walletmanager.Database.Dao.TemplateDaoObject
    public LiveData<List<Template>> getLiveTemplate(final int accountId) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT t.id,t.name,t.note,t.memo,c.color,t.amount,c.id as categoryId,c.default_category as categoryDefault,c.name as category, sc.name as subcategory, t.subcategory_id as subcategoryId,c.icon,c.type,t.wallet_id as walletId FROM template as t LEFT JOIN category as c ON t.category_id = c.id LEFT JOIN subcategory as sc ON t.subcategory_id = sc.id WHERE t.account_id = ? ORDER BY t.ordering ASC", 1);
        acquire.bindLong(1, accountId);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"template", "category", "subcategory"}, false, new Callable<List<Template>>() { // from class: com.ktwapps.walletmanager.Database.Dao.TemplateDaoObject_Impl.8
            @Override // java.util.concurrent.Callable
            public List<Template> call() throws Exception {
                Cursor query = DBUtil.query(TemplateDaoObject_Impl.this.__db, acquire, false, null);
                try {
                    ArrayList arrayList = new ArrayList(query.getCount());
                    while (query.moveToNext()) {
                        arrayList.add(new Template(query.getInt(0), query.isNull(1) ? null : query.getString(1), query.isNull(2) ? null : query.getString(2), query.isNull(3) ? null : query.getString(3), query.isNull(4) ? null : query.getString(4), query.getLong(5), query.getInt(11), query.getInt(6), query.getInt(7), query.isNull(8) ? null : query.getString(8), query.getInt(12), query.getInt(13), query.getInt(10), query.isNull(9) ? null : query.getString(9)));
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

    @Override // com.ktwapps.walletmanager.Database.Dao.TemplateDaoObject
    public Template getTemplateById(final int id) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT t.id,t.name,t.note,t.memo,c.color,t.amount,c.id as categoryId,c.default_category as categoryDefault,c.name as category, sc.name as subcategory, t.subcategory_id as subcategoryId ,c.icon,c.type,t.wallet_id as walletId FROM template as t LEFT JOIN category as c ON t.category_id = c.id LEFT JOIN subcategory as sc ON t.subcategory_id = sc.id WHERE t.id = ?", 1);
        acquire.bindLong(1, id);
        this.__db.assertNotSuspendingTransaction();
        Template template = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            if (query.moveToFirst()) {
                template = new Template(query.getInt(0), query.isNull(1) ? null : query.getString(1), query.isNull(2) ? null : query.getString(2), query.isNull(3) ? null : query.getString(3), query.isNull(4) ? null : query.getString(4), query.getLong(5), query.getInt(11), query.getInt(6), query.getInt(7), query.isNull(8) ? null : query.getString(8), query.getInt(12), query.getInt(13), query.getInt(10), query.isNull(9) ? null : query.getString(9));
            }
            return template;
        } finally {
            query.close();
            acquire.release();
        }
    }

    @Override // com.ktwapps.walletmanager.Database.Dao.TemplateDaoObject
    public TemplateEntity getTemplateEntityById(final int id) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM template WHERE id = ?", 1);
        acquire.bindLong(1, id);
        this.__db.assertNotSuspendingTransaction();
        TemplateEntity templateEntity = null;
        Cursor query = DBUtil.query(this.__db, acquire, false, null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "name");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "note");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "memo");
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "amount");
            int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "category_id");
            int columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "subcategory_id");
            int columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "wallet_id");
            int columnIndexOrThrow9 = CursorUtil.getColumnIndexOrThrow(query, "account_id");
            int columnIndexOrThrow10 = CursorUtil.getColumnIndexOrThrow(query, "ordering");
            if (query.moveToFirst()) {
                templateEntity = new TemplateEntity(query.isNull(columnIndexOrThrow2) ? null : query.getString(columnIndexOrThrow2), query.isNull(columnIndexOrThrow3) ? null : query.getString(columnIndexOrThrow3), query.isNull(columnIndexOrThrow4) ? null : query.getString(columnIndexOrThrow4), query.getLong(columnIndexOrThrow5), query.getInt(columnIndexOrThrow6), query.getInt(columnIndexOrThrow8), query.getInt(columnIndexOrThrow9), query.getInt(columnIndexOrThrow10), query.getInt(columnIndexOrThrow7));
                templateEntity.setId(query.getInt(columnIndexOrThrow));
            }
            return templateEntity;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public static List<Class<?>> getRequiredConverters() {
        return Collections.emptyList();
    }
}
