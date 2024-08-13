package com.expance.merapaisa.Database;

import android.content.Context;
import android.database.Cursor;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.expance.merapaisa.Database.Dao.AccountDaoObject;
import com.expance.merapaisa.Database.Dao.BudgetDaoObject;
import com.expance.merapaisa.Database.Dao.CalenderDaoObject;
import com.expance.merapaisa.Database.Dao.CategoryDaoObject;
import com.expance.merapaisa.Database.Dao.CurrencyDaoObject;
import com.expance.merapaisa.Database.Dao.DebtDaoObject;
import com.expance.merapaisa.Database.Dao.ExportDaoObject;
import com.expance.merapaisa.Database.Dao.GoalDaoObject;
import com.expance.merapaisa.Database.Dao.RecurringDaoObject;
import com.expance.merapaisa.Database.Dao.StatisticDaoObject;
import com.expance.merapaisa.Database.Dao.TemplateDaoObject;
import com.expance.merapaisa.Database.Dao.TransDaoObject;
import com.expance.merapaisa.Database.Dao.WalletDaoObject;

import java.util.Calendar;

/* loaded from: classes3.dex */
public abstract class AppDatabaseObject extends RoomDatabase {
    private static AppDatabaseObject INSTANCE;
    private static final Migration MIGRATION_7_8 = new Migration(7, 8) { // from class: com.ktwapps.walletmanager.Database.AppDatabaseObject.1
        @Override // androidx.room.migration.Migration
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE budgetCategory (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,category_id INTEGER NOT NULL,budget_id INTEGER NOT NULL)");
            database.execSQL("INSERT INTO budgetCategory (budget_id,category_id) SELECT id, category_id FROM budget");
            database.execSQL("CREATE TABLE currency (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,code TEXT,account_id INTEGER NOT NULL,rate REAL NOT NULL)");
            database.execSQL("ALTER TABLE budget ADD color TEXT DEFAULT '#34BFFF'");
            database.execSQL("ALTER TABLE wallet ADD exclude INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE wallet ADD currency TEXT");
            database.execSQL("ALTER TABLE `category`ADD `icon` INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE `debt`ADD `type` INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE `debtTrans`ADD `type` INTEGER NOT NULL DEFAULT 0");
            Cursor query = database.query("SELECT id,currency FROM account");
            if (query.moveToFirst()) {
                do {
                    String string = query.getString(query.getColumnIndex(FirebaseAnalytics.Param.CURRENCY));
                    int i = query.getInt(query.getColumnIndex("id"));
                    String substring = string.substring(string.length() - 3);
                    if (substring.equals("CPI")) {
                        substring = "INR";
                    }
                    database.execSQL("UPDATE wallet SET currency = '" + substring + "' WHERE account_id = " + i);
                    database.execSQL("UPDATE account SET currency = '" + substring + "' WHERE id = " + i);
                    database.execSQL("INSERT INTO currency (code,account_id,rate) VALUES ('" + substring + "'," + i + ",1.00)");
                } while (query.moveToNext());
                query.close();
            }
            query.close();
        }
    };
    private static final Migration MIGRATION_8_9 = new Migration(8, 9) { // from class: com.ktwapps.walletmanager.Database.AppDatabaseObject.2
        @Override // androidx.room.migration.Migration
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE `trans`ADD `trans_amount` INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE `wallet`ADD `icon` INTEGER NOT NULL DEFAULT 0");
            Cursor query = database.query("SELECT id,amount FROM trans WHERE type = 2");
            if (query.moveToFirst()) {
                do {
                    int i = query.getInt(query.getColumnIndex("id"));
                    long j = query.getLong(query.getColumnIndex("amount"));
                    database.execSQL("UPDATE trans SET trans_amount = " + j + " WHERE id = " + i);
                } while (query.moveToNext());
                query.close();
            }
            query.close();
        }
    };
    private static final Migration MIGRATION_9_10 = new Migration(9, 10) { // from class: com.ktwapps.walletmanager.Database.AppDatabaseObject.3
        @Override // androidx.room.migration.Migration
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE recurring (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,note TEXT,type INTEGER NOT NULL,recurring_type INTEGER NOT NULL,repeat_type INTEGER NOT NULL,increment INTEGER NOT NULL,amount INTEGER NOT NULL,date_time INTEGER,repeat_date TEXT,until_time INTEGER,last_update_time INTEGER,account_id INTEGER NOT NULL,category_id INTEGER NOT NULL,wallet_id INTEGER NOT NULL,transfer_wallet_id INTEGER NOT NULL,trans_amount INTEGER NOT NULL)");
            database.execSQL("ALTER TABLE category ADD default_category INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE debtTrans ADD note TEXT");
            Cursor query = database.query("SELECT id FROM account");
            if (query.moveToFirst()) {
                do {
                    int i = query.getInt(query.getColumnIndex("id"));
                    database.execSQL("INSERT INTO category (name,color,type,active,ordering,icon,account_id,default_category) VALUES ('','#FF250B',2,1,0,165," + i + ",24)");
                    database.execSQL("INSERT INTO category (name,color,type,active,ordering,icon,account_id,default_category) VALUES ('','#3485FF',1,1,0,165," + i + ",24)");
                } while (query.moveToNext());
                query.close();
            }
            query.close();
        }
    };
    private static final Migration MIGRATION_10_11 = new Migration(10, 11) { // from class: com.ktwapps.walletmanager.Database.AppDatabaseObject.4
        @Override // androidx.room.migration.Migration
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE trans ADD fee_id INTEGER NOT NULL DEFAULT 0");
            database.execSQL("UPDATE wallet SET currency = 'JOD' WHERE currency = 'JOD '");
            database.execSQL("UPDATE wallet SET currency = 'IQD' WHERE currency = 'IQD)'");
            database.execSQL("UPDATE account SET currency = 'JOD' WHERE currency = 'JOD '");
            database.execSQL("UPDATE account SET currency = 'IQD' WHERE currency = 'IQD)'");
            database.execSQL("UPDATE currency SET code = 'JOD' WHERE code = 'JOD '");
            database.execSQL("UPDATE currency SET code = 'IQD' WHERE code = 'IQD)'");
            Calendar calendar = Calendar.getInstance();
            calendar.set(2020, 0, 1, 1, 0, 0);
            calendar.set(14, 0);
            database.execSQL("UPDATE budget SET start_date = " + calendar.getTimeInMillis() + " WHERE period IN (2,3,4,5)");
            calendar.set(2020, 0, 5, 1, 0, 0);
            database.execSQL("UPDATE budget SET start_date = " + calendar.getTimeInMillis() + " WHERE period IN (0,1)");
        }
    };
    private static final Migration MIGRATION_11_12 = new Migration(11, 12) { // from class: com.ktwapps.walletmanager.Database.AppDatabaseObject.5
        @Override
        public void migrate(SupportSQLiteDatabase db) {
            db.execSQL("ALTER TABLE trans ADD debt_id INTEGER NOT NULL DEFAULT 0");
            db.execSQL("ALTER TABLE trans ADD debt_trans_id INTEGER NOT NULL DEFAULT 0");
            Cursor cursor = db.query("SELECT id FROM account");
            try {
                while (cursor.moveToNext()) {
                    int accountId = cursor.getInt(cursor.getColumnIndex("id"));

                    db.execSQL("INSERT INTO category (name,color,type,active,ordering,icon,account_id,default_category) VALUES ('','#FF2861',2,1,0,166,?,25)", new Object[]{accountId});
                    db.execSQL("INSERT INTO category (name,color,type,active,ordering,icon,account_id,default_category) VALUES ('','#FF5877',2,1,0,167,?,26)", new Object[]{accountId});
                    db.execSQL("INSERT INTO category (name,color,type,active,ordering,icon,account_id,default_category) VALUES ('','#0D8EFF',1,1,0,168,?,27)", new Object[]{accountId});
                    db.execSQL("INSERT INTO category (name,color,type,active,ordering,icon,account_id,default_category) VALUES ('','#69B9FF',1,1,0,169,?,28)", new Object[]{accountId});
                }
            } finally {
                cursor.close();
            }

            cursor = db.query("SELECT amount,id FROM debt");
            try {
                while (cursor.moveToNext()) {
                    int debtId = cursor.getInt(cursor.getColumnIndex("id"));
                    long amount = cursor.getLong(cursor.getColumnIndex("amount"));

                    Cursor cursor1 = db.query("SELECT SUM(amount) as amount FROM debtTrans WHERE debt_id = ? AND (type == 1 OR type = 2)", new Object[]{debtId});
                    try {
                        if (cursor1.moveToFirst()) {
                            amount -= cursor1.getLong(cursor.getColumnIndex("amount"));
                            db.execSQL("UPDATE debt SET amount = ? WHERE id = ?", new Object[]{amount, debtId});
                        }
                    } finally {
                        cursor1.close();
                    }
                }
            } finally {
                cursor.close();
            }
        }
    };
    private static final Migration MIGRATION_12_13 = new Migration(12, 13) { // from class: com.ktwapps.walletmanager.Database.AppDatabaseObject.6
        @Override // androidx.room.migration.Migration
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE `recurring`ADD `is_future` INTEGER NOT NULL DEFAULT 0");
        }
    };
    private static final Migration MIGRATION_13_14 = new Migration(13, 14) { // from class: com.ktwapps.walletmanager.Database.AppDatabaseObject.7
        @Override // androidx.room.migration.Migration
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE goalTrans ADD note TEXT");
            database.execSQL("ALTER TABLE goal ADD currency TEXT");
        }
    };
    private static final Migration MIGRATION_14_15 = new Migration(14, 15) { // from class: com.ktwapps.walletmanager.Database.AppDatabaseObject.8
        @Override // androidx.room.migration.Migration
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE wallet ADD statement_date INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE wallet ADD due_date INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE wallet ADD type INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE wallet ADD credit_limit INTEGER NOT NULL DEFAULT 0");
        }
    };
    private static final Migration MIGRATION_15_16 = new Migration(15, 16) { // from class: com.ktwapps.walletmanager.Database.AppDatabaseObject.9
        @Override // androidx.room.migration.Migration
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE template (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,name TEXT,note TEXT,category_id INTEGER NOT NULL,ordering INTEGER NOT NULL,wallet_id INTEGER NOT NULL,account_id INTEGER NOT NULL,amount INTEGER NOT NULL)");
        }
    };
    private static final Migration MIGRATION_16_17 = new Migration(16, 17) { // from class: com.ktwapps.walletmanager.Database.AppDatabaseObject.10
        @Override // androidx.room.migration.Migration
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE account ADD ordering INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE wallet ADD hidden INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE trans ADD memo TEXT");
        }
    };
    private static final Migration MIGRATION_17_18 = new Migration(17, 18) { // from class: com.ktwapps.walletmanager.Database.AppDatabaseObject.11
        @Override // androidx.room.migration.Migration
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE recurring ADD memo TEXT");
            database.execSQL("ALTER TABLE template ADD memo TEXT");
        }
    };
    private static final Migration MIGRATION_18_19 = new Migration(18, 19) { // from class: com.ktwapps.walletmanager.Database.AppDatabaseObject.12
        @Override // androidx.room.migration.Migration
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE subcategory (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,category_id INTEGER NOT NULL,name TEXT,ordering INTEGER NOT NULL)");
            database.execSQL("ALTER TABLE trans ADD subcategory_id INTEGER NOT NULL DEFAULT 0");
        }
    };
    private static final Migration MIGRATION_19_20 = new Migration(19, 20) { // from class: com.ktwapps.walletmanager.Database.AppDatabaseObject.13
        @Override // androidx.room.migration.Migration
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE template ADD subcategory_id INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE recurring ADD subcategory_id INTEGER NOT NULL DEFAULT 0");
        }
    };

    public abstract AccountDaoObject accountDaoObject();

    public abstract BudgetDaoObject budgetDaoObject();

    public abstract CalenderDaoObject calenderDaoObject();

    public abstract CategoryDaoObject categoryDaoObject();

    public abstract CurrencyDaoObject currencyDaoObject();

    public abstract DebtDaoObject debtDaoObject();

    public abstract ExportDaoObject exportDaoObject();

    public abstract GoalDaoObject goalDaoObject();

    public abstract RecurringDaoObject recurringDaoObject();

    public abstract StatisticDaoObject statisticDaoObject();

    public abstract TemplateDaoObject templateDaoObject();

    public abstract TransDaoObject transDaoObject();

    public abstract WalletDaoObject walletDaoObject();

    public static synchronized AppDatabaseObject getInstance(Context context) {
        AppDatabaseObject appDatabaseObject;
        synchronized (AppDatabaseObject.class) {
            if (INSTANCE == null) {
                INSTANCE = buildDatabase(context);
            }
            appDatabaseObject = INSTANCE;
        }
        return appDatabaseObject;
    }

    public void restoreDatabase() {
        INSTANCE = null;
    }

    private static AppDatabaseObject buildDatabase(final Context context) {
        return (AppDatabaseObject) Room.databaseBuilder(context, AppDatabaseObject.class, "wallet-database").addMigrations(MIGRATION_7_8, MIGRATION_8_9, MIGRATION_9_10, MIGRATION_10_11, MIGRATION_11_12, MIGRATION_12_13, MIGRATION_13_14, MIGRATION_14_15, MIGRATION_15_16, MIGRATION_16_17, MIGRATION_17_18, MIGRATION_18_19, MIGRATION_19_20).build();
    }
}
