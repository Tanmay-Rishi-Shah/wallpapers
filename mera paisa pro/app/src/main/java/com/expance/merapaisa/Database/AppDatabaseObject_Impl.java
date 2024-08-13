package com.expance.merapaisa.Database;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.core.app.NotificationCompat;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomMasterTable;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.expance.merapaisa.Database.Dao.AccountDaoObject;
import com.expance.merapaisa.Database.Dao.AccountDaoObject_Impl;
import com.expance.merapaisa.Database.Dao.BudgetDaoObject;
import com.expance.merapaisa.Database.Dao.BudgetDaoObject_Impl;
import com.expance.merapaisa.Database.Dao.CalenderDaoObject;
import com.expance.merapaisa.Database.Dao.CalenderDaoObject_Impl;
import com.expance.merapaisa.Database.Dao.CategoryDaoObject;
import com.expance.merapaisa.Database.Dao.CategoryDaoObject_Impl;
import com.expance.merapaisa.Database.Dao.CurrencyDaoObject;
import com.expance.merapaisa.Database.Dao.CurrencyDaoObject_Impl;
import com.expance.merapaisa.Database.Dao.DebtDaoObject;
import com.expance.merapaisa.Database.Dao.DebtDaoObject_Impl;
import com.expance.merapaisa.Database.Dao.ExportDaoObject;
import com.expance.merapaisa.Database.Dao.ExportDaoObject_Impl;
import com.expance.merapaisa.Database.Dao.GoalDaoObject;
import com.expance.merapaisa.Database.Dao.GoalDaoObject_Impl;
import com.expance.merapaisa.Database.Dao.RecurringDaoObject;
import com.expance.merapaisa.Database.Dao.RecurringDaoObject_Impl;
import com.expance.merapaisa.Database.Dao.StatisticDaoObject;
import com.expance.merapaisa.Database.Dao.StatisticDaoObject_Impl;
import com.expance.merapaisa.Database.Dao.TemplateDaoObject;
import com.expance.merapaisa.Database.Dao.TemplateDaoObject_Impl;
import com.expance.merapaisa.Database.Dao.TransDaoObject;
import com.expance.merapaisa.Database.Dao.TransDaoObject_Impl;
import com.expance.merapaisa.Database.Dao.WalletDaoObject;
import com.expance.merapaisa.Database.Dao.WalletDaoObject_Impl;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.http.cookie.ClientCookie;
import org.apache.poi.ss.util.CellUtil;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

/* loaded from: classes3.dex */
public final class AppDatabaseObject_Impl extends AppDatabaseObject {
    private volatile AccountDaoObject _accountDaoObject;
    private volatile BudgetDaoObject _budgetDaoObject;
    private volatile CalenderDaoObject _calenderDaoObject;
    private volatile CategoryDaoObject _categoryDaoObject;
    private volatile CurrencyDaoObject _currencyDaoObject;
    private volatile DebtDaoObject _debtDaoObject;
    private volatile ExportDaoObject _exportDaoObject;
    private volatile GoalDaoObject _goalDaoObject;
    private volatile RecurringDaoObject _recurringDaoObject;
    private volatile StatisticDaoObject _statisticDaoObject;
    private volatile TemplateDaoObject _templateDaoObject;
    private volatile TransDaoObject _transDaoObject;
    private volatile WalletDaoObject _walletDaoObject;

    @Override // androidx.room.RoomDatabase
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
        return configuration.sqliteOpenHelperFactory.create(SupportSQLiteOpenHelper.Configuration.builder(configuration.context).name(configuration.name).callback(new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(20) { // from class: com.ktwapps.walletmanager.Database.AppDatabaseObject_Impl.1
            @Override // androidx.room.RoomOpenHelper.Delegate
            public void onPostMigrate(SupportSQLiteDatabase _db) {
            }

            @Override // androidx.room.RoomOpenHelper.Delegate
            public void createAllTables(SupportSQLiteDatabase _db) {
                _db.execSQL("CREATE TABLE IF NOT EXISTS `account` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `type` INTEGER NOT NULL, `name` TEXT, `currency` TEXT, `ordering` INTEGER NOT NULL, `currency_symbol` TEXT, `balance` INTEGER NOT NULL)");
                _db.execSQL("CREATE TABLE IF NOT EXISTS `budget` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `amount` INTEGER NOT NULL, `spent` INTEGER NOT NULL, `status` INTEGER NOT NULL, `period` INTEGER NOT NULL, `color` TEXT, `repeat` INTEGER NOT NULL, `account_id` INTEGER NOT NULL, `category_id` INTEGER NOT NULL, `start_date` INTEGER, `end_date` INTEGER)");
                _db.execSQL("CREATE TABLE IF NOT EXISTS `budgetCategory` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `budget_id` INTEGER NOT NULL, `category_id` INTEGER NOT NULL)");
                _db.execSQL("CREATE TABLE IF NOT EXISTS `currency` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `code` TEXT, `rate` REAL NOT NULL, `account_id` INTEGER NOT NULL)");
                _db.execSQL("CREATE TABLE IF NOT EXISTS `category` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `color` TEXT, `type` INTEGER NOT NULL, `active` INTEGER NOT NULL, `ordering` INTEGER NOT NULL, `icon` INTEGER NOT NULL, `account_id` INTEGER NOT NULL, `default_category` INTEGER NOT NULL)");
                _db.execSQL("CREATE TABLE IF NOT EXISTS `subcategory` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `category_id` INTEGER NOT NULL, `name` TEXT, `ordering` INTEGER NOT NULL)");
                _db.execSQL("CREATE TABLE IF NOT EXISTS `debt` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `lender` TEXT, `color` TEXT, `pay` INTEGER NOT NULL, `amount` INTEGER NOT NULL, `due_date` INTEGER, `lend_date` INTEGER, `account_id` INTEGER NOT NULL, `status` INTEGER NOT NULL, `type` INTEGER NOT NULL)");
                _db.execSQL("CREATE TABLE IF NOT EXISTS `debtTrans` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `amount` INTEGER NOT NULL, `date_time` INTEGER, `debt_id` INTEGER NOT NULL, `note` TEXT, `type` INTEGER NOT NULL)");
                _db.execSQL("CREATE TABLE IF NOT EXISTS `goal` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `color` TEXT, `saved` INTEGER NOT NULL, `amount` INTEGER NOT NULL, `status` INTEGER NOT NULL, `account_id` INTEGER NOT NULL, `expect_date` INTEGER, `achieve_date` INTEGER, `currency` TEXT)");
                _db.execSQL("CREATE TABLE IF NOT EXISTS `goalTrans` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `amount` INTEGER NOT NULL, `date_time` INTEGER, `goal_id` INTEGER NOT NULL, `type` INTEGER NOT NULL, `note` TEXT)");
                _db.execSQL("CREATE TABLE IF NOT EXISTS `media` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `path` TEXT, `trans_id` INTEGER NOT NULL)");
                _db.execSQL("CREATE TABLE IF NOT EXISTS `trans` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `note` TEXT, `memo` TEXT, `type` INTEGER NOT NULL, `amount` INTEGER NOT NULL, `date_time` INTEGER, `account_id` INTEGER NOT NULL, `fee_id` INTEGER NOT NULL, `category_id` INTEGER NOT NULL, `subcategory_id` INTEGER NOT NULL, `wallet_id` INTEGER NOT NULL, `transfer_wallet_id` INTEGER NOT NULL, `trans_amount` INTEGER NOT NULL, `debt_id` INTEGER NOT NULL, `debt_trans_id` INTEGER NOT NULL)");
                _db.execSQL("CREATE TABLE IF NOT EXISTS `wallet` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `color` TEXT, `amount` INTEGER NOT NULL, `initial_amount` INTEGER NOT NULL, `active` INTEGER NOT NULL, `account_id` INTEGER NOT NULL, `ordering` INTEGER NOT NULL, `exclude` INTEGER NOT NULL, `currency` TEXT, `icon` INTEGER NOT NULL, `type` INTEGER NOT NULL, `due_date` INTEGER NOT NULL, `statement_date` INTEGER NOT NULL, `credit_limit` INTEGER NOT NULL, `hidden` INTEGER NOT NULL)");
                _db.execSQL("CREATE TABLE IF NOT EXISTS `recurring` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `note` TEXT, `memo` TEXT, `type` INTEGER NOT NULL, `recurring_type` INTEGER NOT NULL, `repeat_type` INTEGER NOT NULL, `repeat_date` TEXT, `increment` INTEGER NOT NULL, `amount` INTEGER NOT NULL, `date_time` INTEGER, `until_time` INTEGER, `last_update_time` INTEGER, `account_id` INTEGER NOT NULL, `category_id` INTEGER NOT NULL, `wallet_id` INTEGER NOT NULL, `subcategory_id` INTEGER NOT NULL, `transfer_wallet_id` INTEGER NOT NULL, `trans_amount` INTEGER NOT NULL, `is_future` INTEGER NOT NULL)");
                _db.execSQL("CREATE TABLE IF NOT EXISTS `template` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `note` TEXT, `memo` TEXT, `amount` INTEGER NOT NULL, `category_id` INTEGER NOT NULL, `subcategory_id` INTEGER NOT NULL, `wallet_id` INTEGER NOT NULL, `account_id` INTEGER NOT NULL, `ordering` INTEGER NOT NULL)");
                _db.execSQL(RoomMasterTable.CREATE_QUERY);
                _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '709dab0ae391589be05def1ad6a86070')");
            }

            @Override // androidx.room.RoomOpenHelper.Delegate
            public void dropAllTables(SupportSQLiteDatabase _db) {
                _db.execSQL("DROP TABLE IF EXISTS `account`");
                _db.execSQL("DROP TABLE IF EXISTS `budget`");
                _db.execSQL("DROP TABLE IF EXISTS `budgetCategory`");
                _db.execSQL("DROP TABLE IF EXISTS `currency`");
                _db.execSQL("DROP TABLE IF EXISTS `category`");
                _db.execSQL("DROP TABLE IF EXISTS `subcategory`");
                _db.execSQL("DROP TABLE IF EXISTS `debt`");
                _db.execSQL("DROP TABLE IF EXISTS `debtTrans`");
                _db.execSQL("DROP TABLE IF EXISTS `goal`");
                _db.execSQL("DROP TABLE IF EXISTS `goalTrans`");
                _db.execSQL("DROP TABLE IF EXISTS `media`");
                _db.execSQL("DROP TABLE IF EXISTS `trans`");
                _db.execSQL("DROP TABLE IF EXISTS `wallet`");
                _db.execSQL("DROP TABLE IF EXISTS `recurring`");
                _db.execSQL("DROP TABLE IF EXISTS `template`");
                if (AppDatabaseObject_Impl.this.mCallbacks != null) {
                    int size = AppDatabaseObject_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((RoomDatabase.Callback) AppDatabaseObject_Impl.this.mCallbacks.get(i)).onDestructiveMigration(_db);
                    }
                }
            }

            @Override // androidx.room.RoomOpenHelper.Delegate
            public void onCreate(SupportSQLiteDatabase _db) {
                if (AppDatabaseObject_Impl.this.mCallbacks != null) {
                    int size = AppDatabaseObject_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((RoomDatabase.Callback) AppDatabaseObject_Impl.this.mCallbacks.get(i)).onCreate(_db);
                    }
                }
            }

            @Override // androidx.room.RoomOpenHelper.Delegate
            public void onOpen(SupportSQLiteDatabase _db) {
                AppDatabaseObject_Impl.this.mDatabase = _db;
                AppDatabaseObject_Impl.this.internalInitInvalidationTracker(_db);
                if (AppDatabaseObject_Impl.this.mCallbacks != null) {
                    int size = AppDatabaseObject_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((RoomDatabase.Callback) AppDatabaseObject_Impl.this.mCallbacks.get(i)).onOpen(_db);
                    }
                }
            }

            @Override // androidx.room.RoomOpenHelper.Delegate
            public void onPreMigrate(SupportSQLiteDatabase _db) {
                DBUtil.dropFtsSyncTriggers(_db);
            }

            @Override // androidx.room.RoomOpenHelper.Delegate
            public RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
                HashMap hashMap = new HashMap(7);
                hashMap.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, 1));
                hashMap.put(JamXmlElements.TYPE, new TableInfo.Column(JamXmlElements.TYPE, "INTEGER", true, 0, null, 1));
                hashMap.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, 1));
                hashMap.put(FirebaseAnalytics.Param.CURRENCY, new TableInfo.Column(FirebaseAnalytics.Param.CURRENCY, "TEXT", false, 0, null, 1));
                hashMap.put("ordering", new TableInfo.Column("ordering", "INTEGER", true, 0, null, 1));
                hashMap.put("currency_symbol", new TableInfo.Column("currency_symbol", "TEXT", false, 0, null, 1));
                hashMap.put("balance", new TableInfo.Column("balance", "INTEGER", true, 0, null, 1));
                TableInfo tableInfo = new TableInfo("account", hashMap, new HashSet(0), new HashSet(0));
                TableInfo read = TableInfo.read(_db, "account");
                if (!tableInfo.equals(read)) {
                    return new RoomOpenHelper.ValidationResult(false, "account(com.ktwapps.walletmanager.Database.Entity.AccountEntity).\n Expected:\n" + tableInfo + "\n Found:\n" + read);
                }
                HashMap hashMap2 = new HashMap(12);
                hashMap2.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, 1));
                hashMap2.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, 1));
                hashMap2.put("amount", new TableInfo.Column("amount", "INTEGER", true, 0, null, 1));
                hashMap2.put("spent", new TableInfo.Column("spent", "INTEGER", true, 0, null, 1));
                hashMap2.put(NotificationCompat.CATEGORY_STATUS, new TableInfo.Column(NotificationCompat.CATEGORY_STATUS, "INTEGER", true, 0, null, 1));
                hashMap2.put(TypedValues.CycleType.S_WAVE_PERIOD, new TableInfo.Column(TypedValues.CycleType.S_WAVE_PERIOD, "INTEGER", true, 0, null, 1));
                hashMap2.put(TypedValues.Custom.S_COLOR, new TableInfo.Column(TypedValues.Custom.S_COLOR, "TEXT", false, 0, null, 1));
                hashMap2.put("repeat", new TableInfo.Column("repeat", "INTEGER", true, 0, null, 1));
                hashMap2.put("account_id", new TableInfo.Column("account_id", "INTEGER", true, 0, null, 1));
                hashMap2.put("category_id", new TableInfo.Column("category_id", "INTEGER", true, 0, null, 1));
                hashMap2.put(FirebaseAnalytics.Param.START_DATE, new TableInfo.Column(FirebaseAnalytics.Param.START_DATE, "INTEGER", false, 0, null, 1));
                hashMap2.put(FirebaseAnalytics.Param.END_DATE, new TableInfo.Column(FirebaseAnalytics.Param.END_DATE, "INTEGER", false, 0, null, 1));
                TableInfo tableInfo2 = new TableInfo("budget", hashMap2, new HashSet(0), new HashSet(0));
                TableInfo read2 = TableInfo.read(_db, "budget");
                if (!tableInfo2.equals(read2)) {
                    return new RoomOpenHelper.ValidationResult(false, "budget(com.ktwapps.walletmanager.Database.Entity.BudgetEntity).\n Expected:\n" + tableInfo2 + "\n Found:\n" + read2);
                }
                HashMap hashMap3 = new HashMap(3);
                hashMap3.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, 1));
                hashMap3.put("budget_id", new TableInfo.Column("budget_id", "INTEGER", true, 0, null, 1));
                hashMap3.put("category_id", new TableInfo.Column("category_id", "INTEGER", true, 0, null, 1));
                TableInfo tableInfo3 = new TableInfo("budgetCategory", hashMap3, new HashSet(0), new HashSet(0));
                TableInfo read3 = TableInfo.read(_db, "budgetCategory");
                if (!tableInfo3.equals(read3)) {
                    return new RoomOpenHelper.ValidationResult(false, "budgetCategory(com.ktwapps.walletmanager.Database.Entity.BudgetCategoryEntity).\n Expected:\n" + tableInfo3 + "\n Found:\n" + read3);
                }
                HashMap hashMap4 = new HashMap(4);
                hashMap4.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, 1));
                hashMap4.put("code", new TableInfo.Column("code", "TEXT", false, 0, null, 1));
                hashMap4.put("rate", new TableInfo.Column("rate", "REAL", true, 0, null, 1));
                hashMap4.put("account_id", new TableInfo.Column("account_id", "INTEGER", true, 0, null, 1));
                TableInfo tableInfo4 = new TableInfo(FirebaseAnalytics.Param.CURRENCY, hashMap4, new HashSet(0), new HashSet(0));
                TableInfo read4 = TableInfo.read(_db, FirebaseAnalytics.Param.CURRENCY);
                if (!tableInfo4.equals(read4)) {
                    return new RoomOpenHelper.ValidationResult(false, "currency(com.ktwapps.walletmanager.Database.Entity.CurrencyEntity).\n Expected:\n" + tableInfo4 + "\n Found:\n" + read4);
                }
                HashMap hashMap5 = new HashMap(9);
                hashMap5.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, 1));
                hashMap5.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, 1));
                hashMap5.put(TypedValues.Custom.S_COLOR, new TableInfo.Column(TypedValues.Custom.S_COLOR, "TEXT", false, 0, null, 1));
                hashMap5.put(JamXmlElements.TYPE, new TableInfo.Column(JamXmlElements.TYPE, "INTEGER", true, 0, null, 1));
                hashMap5.put(AppMeasurementSdk.ConditionalUserProperty.ACTIVE, new TableInfo.Column(AppMeasurementSdk.ConditionalUserProperty.ACTIVE, "INTEGER", true, 0, null, 1));
                hashMap5.put("ordering", new TableInfo.Column("ordering", "INTEGER", true, 0, null, 1));
                hashMap5.put("icon", new TableInfo.Column("icon", "INTEGER", true, 0, null, 1));
                hashMap5.put("account_id", new TableInfo.Column("account_id", "INTEGER", true, 0, null, 1));
                hashMap5.put("default_category", new TableInfo.Column("default_category", "INTEGER", true, 0, null, 1));
                TableInfo tableInfo5 = new TableInfo("category", hashMap5, new HashSet(0), new HashSet(0));
                TableInfo read5 = TableInfo.read(_db, "category");
                if (!tableInfo5.equals(read5)) {
                    return new RoomOpenHelper.ValidationResult(false, "category(com.ktwapps.walletmanager.Database.Entity.CategoryEntity).\n Expected:\n" + tableInfo5 + "\n Found:\n" + read5);
                }
                HashMap hashMap6 = new HashMap(4);
                hashMap6.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, 1));
                hashMap6.put("category_id", new TableInfo.Column("category_id", "INTEGER", true, 0, null, 1));
                hashMap6.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, 1));
                hashMap6.put("ordering", new TableInfo.Column("ordering", "INTEGER", true, 0, null, 1));
                TableInfo tableInfo6 = new TableInfo("subcategory", hashMap6, new HashSet(0), new HashSet(0));
                TableInfo read6 = TableInfo.read(_db, "subcategory");
                if (!tableInfo6.equals(read6)) {
                    return new RoomOpenHelper.ValidationResult(false, "subcategory(com.ktwapps.walletmanager.Database.Entity.SubcategoryEntity).\n Expected:\n" + tableInfo6 + "\n Found:\n" + read6);
                }
                HashMap hashMap7 = new HashMap(11);
                hashMap7.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, 1));
                hashMap7.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, 1));
                hashMap7.put("lender", new TableInfo.Column("lender", "TEXT", false, 0, null, 1));
                hashMap7.put(TypedValues.Custom.S_COLOR, new TableInfo.Column(TypedValues.Custom.S_COLOR, "TEXT", false, 0, null, 1));
                hashMap7.put("pay", new TableInfo.Column("pay", "INTEGER", true, 0, null, 1));
                hashMap7.put("amount", new TableInfo.Column("amount", "INTEGER", true, 0, null, 1));
                hashMap7.put("due_date", new TableInfo.Column("due_date", "INTEGER", false, 0, null, 1));
                hashMap7.put("lend_date", new TableInfo.Column("lend_date", "INTEGER", false, 0, null, 1));
                hashMap7.put("account_id", new TableInfo.Column("account_id", "INTEGER", true, 0, null, 1));
                hashMap7.put(NotificationCompat.CATEGORY_STATUS, new TableInfo.Column(NotificationCompat.CATEGORY_STATUS, "INTEGER", true, 0, null, 1));
                hashMap7.put(JamXmlElements.TYPE, new TableInfo.Column(JamXmlElements.TYPE, "INTEGER", true, 0, null, 1));
                TableInfo tableInfo7 = new TableInfo("debt", hashMap7, new HashSet(0), new HashSet(0));
                TableInfo read7 = TableInfo.read(_db, "debt");
                if (!tableInfo7.equals(read7)) {
                    return new RoomOpenHelper.ValidationResult(false, "debt(com.ktwapps.walletmanager.Database.Entity.DebtEntity).\n Expected:\n" + tableInfo7 + "\n Found:\n" + read7);
                }
                HashMap hashMap8 = new HashMap(6);
                hashMap8.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, 1));
                hashMap8.put("amount", new TableInfo.Column("amount", "INTEGER", true, 0, null, 1));
                hashMap8.put("date_time", new TableInfo.Column("date_time", "INTEGER", false, 0, null, 1));
                hashMap8.put("debt_id", new TableInfo.Column("debt_id", "INTEGER", true, 0, null, 1));
                hashMap8.put("note", new TableInfo.Column("note", "TEXT", false, 0, null, 1));
                hashMap8.put(JamXmlElements.TYPE, new TableInfo.Column(JamXmlElements.TYPE, "INTEGER", true, 0, null, 1));
                TableInfo tableInfo8 = new TableInfo("debtTrans", hashMap8, new HashSet(0), new HashSet(0));
                TableInfo read8 = TableInfo.read(_db, "debtTrans");
                if (!tableInfo8.equals(read8)) {
                    return new RoomOpenHelper.ValidationResult(false, "debtTrans(com.ktwapps.walletmanager.Database.Entity.DebtTransEntity).\n Expected:\n" + tableInfo8 + "\n Found:\n" + read8);
                }
                HashMap hashMap9 = new HashMap(10);
                hashMap9.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, 1));
                hashMap9.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, 1));
                hashMap9.put(TypedValues.Custom.S_COLOR, new TableInfo.Column(TypedValues.Custom.S_COLOR, "TEXT", false, 0, null, 1));
                hashMap9.put("saved", new TableInfo.Column("saved", "INTEGER", true, 0, null, 1));
                hashMap9.put("amount", new TableInfo.Column("amount", "INTEGER", true, 0, null, 1));
                hashMap9.put(NotificationCompat.CATEGORY_STATUS, new TableInfo.Column(NotificationCompat.CATEGORY_STATUS, "INTEGER", true, 0, null, 1));
                hashMap9.put("account_id", new TableInfo.Column("account_id", "INTEGER", true, 0, null, 1));
                hashMap9.put("expect_date", new TableInfo.Column("expect_date", "INTEGER", false, 0, null, 1));
                hashMap9.put("achieve_date", new TableInfo.Column("achieve_date", "INTEGER", false, 0, null, 1));
                hashMap9.put(FirebaseAnalytics.Param.CURRENCY, new TableInfo.Column(FirebaseAnalytics.Param.CURRENCY, "TEXT", false, 0, null, 1));
                TableInfo tableInfo9 = new TableInfo("goal", hashMap9, new HashSet(0), new HashSet(0));
                TableInfo read9 = TableInfo.read(_db, "goal");
                if (!tableInfo9.equals(read9)) {
                    return new RoomOpenHelper.ValidationResult(false, "goal(com.ktwapps.walletmanager.Database.Entity.GoalEntity).\n Expected:\n" + tableInfo9 + "\n Found:\n" + read9);
                }
                HashMap hashMap10 = new HashMap(6);
                hashMap10.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, 1));
                hashMap10.put("amount", new TableInfo.Column("amount", "INTEGER", true, 0, null, 1));
                hashMap10.put("date_time", new TableInfo.Column("date_time", "INTEGER", false, 0, null, 1));
                hashMap10.put("goal_id", new TableInfo.Column("goal_id", "INTEGER", true, 0, null, 1));
                hashMap10.put(JamXmlElements.TYPE, new TableInfo.Column(JamXmlElements.TYPE, "INTEGER", true, 0, null, 1));
                hashMap10.put("note", new TableInfo.Column("note", "TEXT", false, 0, null, 1));
                TableInfo tableInfo10 = new TableInfo("goalTrans", hashMap10, new HashSet(0), new HashSet(0));
                TableInfo read10 = TableInfo.read(_db, "goalTrans");
                if (!tableInfo10.equals(read10)) {
                    return new RoomOpenHelper.ValidationResult(false, "goalTrans(com.ktwapps.walletmanager.Database.Entity.GoalTransEntity).\n Expected:\n" + tableInfo10 + "\n Found:\n" + read10);
                }
                HashMap hashMap11 = new HashMap(3);
                hashMap11.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, 1));
                hashMap11.put(ClientCookie.PATH_ATTR, new TableInfo.Column(ClientCookie.PATH_ATTR, "TEXT", false, 0, null, 1));
                hashMap11.put("trans_id", new TableInfo.Column("trans_id", "INTEGER", true, 0, null, 1));
                TableInfo tableInfo11 = new TableInfo("media", hashMap11, new HashSet(0), new HashSet(0));
                TableInfo read11 = TableInfo.read(_db, "media");
                if (!tableInfo11.equals(read11)) {
                    return new RoomOpenHelper.ValidationResult(false, "media(com.ktwapps.walletmanager.Database.Entity.MediaEntity).\n Expected:\n" + tableInfo11 + "\n Found:\n" + read11);
                }
                HashMap hashMap12 = new HashMap(15);
                hashMap12.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, 1));
                hashMap12.put("note", new TableInfo.Column("note", "TEXT", false, 0, null, 1));
                hashMap12.put("memo", new TableInfo.Column("memo", "TEXT", false, 0, null, 1));
                hashMap12.put(JamXmlElements.TYPE, new TableInfo.Column(JamXmlElements.TYPE, "INTEGER", true, 0, null, 1));
                hashMap12.put("amount", new TableInfo.Column("amount", "INTEGER", true, 0, null, 1));
                hashMap12.put("date_time", new TableInfo.Column("date_time", "INTEGER", false, 0, null, 1));
                hashMap12.put("account_id", new TableInfo.Column("account_id", "INTEGER", true, 0, null, 1));
                hashMap12.put("fee_id", new TableInfo.Column("fee_id", "INTEGER", true, 0, null, 1));
                hashMap12.put("category_id", new TableInfo.Column("category_id", "INTEGER", true, 0, null, 1));
                hashMap12.put("subcategory_id", new TableInfo.Column("subcategory_id", "INTEGER", true, 0, null, 1));
                hashMap12.put("wallet_id", new TableInfo.Column("wallet_id", "INTEGER", true, 0, null, 1));
                hashMap12.put("transfer_wallet_id", new TableInfo.Column("transfer_wallet_id", "INTEGER", true, 0, null, 1));
                hashMap12.put("trans_amount", new TableInfo.Column("trans_amount", "INTEGER", true, 0, null, 1));
                hashMap12.put("debt_id", new TableInfo.Column("debt_id", "INTEGER", true, 0, null, 1));
                hashMap12.put("debt_trans_id", new TableInfo.Column("debt_trans_id", "INTEGER", true, 0, null, 1));
                TableInfo tableInfo12 = new TableInfo("trans", hashMap12, new HashSet(0), new HashSet(0));
                TableInfo read12 = TableInfo.read(_db, "trans");
                if (!tableInfo12.equals(read12)) {
                    return new RoomOpenHelper.ValidationResult(false, "trans(com.ktwapps.walletmanager.Database.Entity.TransEntity).\n Expected:\n" + tableInfo12 + "\n Found:\n" + read12);
                }
                HashMap hashMap13 = new HashMap(16);
                hashMap13.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, 1));
                hashMap13.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, 1));
                hashMap13.put(TypedValues.Custom.S_COLOR, new TableInfo.Column(TypedValues.Custom.S_COLOR, "TEXT", false, 0, null, 1));
                hashMap13.put("amount", new TableInfo.Column("amount", "INTEGER", true, 0, null, 1));
                hashMap13.put("initial_amount", new TableInfo.Column("initial_amount", "INTEGER", true, 0, null, 1));
                hashMap13.put(AppMeasurementSdk.ConditionalUserProperty.ACTIVE, new TableInfo.Column(AppMeasurementSdk.ConditionalUserProperty.ACTIVE, "INTEGER", true, 0, null, 1));
                hashMap13.put("account_id", new TableInfo.Column("account_id", "INTEGER", true, 0, null, 1));
                hashMap13.put("ordering", new TableInfo.Column("ordering", "INTEGER", true, 0, null, 1));
                hashMap13.put("exclude", new TableInfo.Column("exclude", "INTEGER", true, 0, null, 1));
                hashMap13.put(FirebaseAnalytics.Param.CURRENCY, new TableInfo.Column(FirebaseAnalytics.Param.CURRENCY, "TEXT", false, 0, null, 1));
                hashMap13.put("icon", new TableInfo.Column("icon", "INTEGER", true, 0, null, 1));
                hashMap13.put(JamXmlElements.TYPE, new TableInfo.Column(JamXmlElements.TYPE, "INTEGER", true, 0, null, 1));
                hashMap13.put("due_date", new TableInfo.Column("due_date", "INTEGER", true, 0, null, 1));
                hashMap13.put("statement_date", new TableInfo.Column("statement_date", "INTEGER", true, 0, null, 1));
                hashMap13.put("credit_limit", new TableInfo.Column("credit_limit", "INTEGER", true, 0, null, 1));
                hashMap13.put(CellUtil.HIDDEN, new TableInfo.Column(CellUtil.HIDDEN, "INTEGER", true, 0, null, 1));
                TableInfo tableInfo13 = new TableInfo("wallet", hashMap13, new HashSet(0), new HashSet(0));
                TableInfo read13 = TableInfo.read(_db, "wallet");
                if (!tableInfo13.equals(read13)) {
                    return new RoomOpenHelper.ValidationResult(false, "wallet(com.ktwapps.walletmanager.Database.Entity.WalletEntity).\n Expected:\n" + tableInfo13 + "\n Found:\n" + read13);
                }
                HashMap hashMap14 = new HashMap(19);
                hashMap14.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, 1));
                hashMap14.put("note", new TableInfo.Column("note", "TEXT", false, 0, null, 1));
                hashMap14.put("memo", new TableInfo.Column("memo", "TEXT", false, 0, null, 1));
                hashMap14.put(JamXmlElements.TYPE, new TableInfo.Column(JamXmlElements.TYPE, "INTEGER", true, 0, null, 1));
                hashMap14.put("recurring_type", new TableInfo.Column("recurring_type", "INTEGER", true, 0, null, 1));
                hashMap14.put("repeat_type", new TableInfo.Column("repeat_type", "INTEGER", true, 0, null, 1));
                hashMap14.put("repeat_date", new TableInfo.Column("repeat_date", "TEXT", false, 0, null, 1));
                hashMap14.put("increment", new TableInfo.Column("increment", "INTEGER", true, 0, null, 1));
                hashMap14.put("amount", new TableInfo.Column("amount", "INTEGER", true, 0, null, 1));
                hashMap14.put("date_time", new TableInfo.Column("date_time", "INTEGER", false, 0, null, 1));
                hashMap14.put("until_time", new TableInfo.Column("until_time", "INTEGER", false, 0, null, 1));
                hashMap14.put("last_update_time", new TableInfo.Column("last_update_time", "INTEGER", false, 0, null, 1));
                hashMap14.put("account_id", new TableInfo.Column("account_id", "INTEGER", true, 0, null, 1));
                hashMap14.put("category_id", new TableInfo.Column("category_id", "INTEGER", true, 0, null, 1));
                hashMap14.put("wallet_id", new TableInfo.Column("wallet_id", "INTEGER", true, 0, null, 1));
                hashMap14.put("subcategory_id", new TableInfo.Column("subcategory_id", "INTEGER", true, 0, null, 1));
                hashMap14.put("transfer_wallet_id", new TableInfo.Column("transfer_wallet_id", "INTEGER", true, 0, null, 1));
                hashMap14.put("trans_amount", new TableInfo.Column("trans_amount", "INTEGER", true, 0, null, 1));
                hashMap14.put("is_future", new TableInfo.Column("is_future", "INTEGER", true, 0, null, 1));
                TableInfo tableInfo14 = new TableInfo("recurring", hashMap14, new HashSet(0), new HashSet(0));
                TableInfo read14 = TableInfo.read(_db, "recurring");
                if (!tableInfo14.equals(read14)) {
                    return new RoomOpenHelper.ValidationResult(false, "recurring(com.ktwapps.walletmanager.Database.Entity.RecurringEntity).\n Expected:\n" + tableInfo14 + "\n Found:\n" + read14);
                }
                HashMap hashMap15 = new HashMap(10);
                hashMap15.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, 1));
                hashMap15.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, 1));
                hashMap15.put("note", new TableInfo.Column("note", "TEXT", false, 0, null, 1));
                hashMap15.put("memo", new TableInfo.Column("memo", "TEXT", false, 0, null, 1));
                hashMap15.put("amount", new TableInfo.Column("amount", "INTEGER", true, 0, null, 1));
                hashMap15.put("category_id", new TableInfo.Column("category_id", "INTEGER", true, 0, null, 1));
                hashMap15.put("subcategory_id", new TableInfo.Column("subcategory_id", "INTEGER", true, 0, null, 1));
                hashMap15.put("wallet_id", new TableInfo.Column("wallet_id", "INTEGER", true, 0, null, 1));
                hashMap15.put("account_id", new TableInfo.Column("account_id", "INTEGER", true, 0, null, 1));
                hashMap15.put("ordering", new TableInfo.Column("ordering", "INTEGER", true, 0, null, 1));
                TableInfo tableInfo15 = new TableInfo("template", hashMap15, new HashSet(0), new HashSet(0));
                TableInfo read15 = TableInfo.read(_db, "template");
                if (!tableInfo15.equals(read15)) {
                    return new RoomOpenHelper.ValidationResult(false, "template(com.ktwapps.walletmanager.Database.Entity.TemplateEntity).\n Expected:\n" + tableInfo15 + "\n Found:\n" + read15);
                }
                return new RoomOpenHelper.ValidationResult(true, null);
            }
        }, "709dab0ae391589be05def1ad6a86070", "7711b861cda2e57eb31787f9b5193607")).build());
    }

    @Override // androidx.room.RoomDatabase
    protected InvalidationTracker createInvalidationTracker() {
        return new InvalidationTracker(this, new HashMap(0), new HashMap(0), "account", "budget", "budgetCategory", FirebaseAnalytics.Param.CURRENCY, "category", "subcategory", "debt", "debtTrans", "goal", "goalTrans", "media", "trans", "wallet", "recurring", "template");
    }

    @Override // androidx.room.RoomDatabase
    public void clearAllTables() {
        super.assertNotMainThread();
        SupportSQLiteDatabase writableDatabase = super.getOpenHelper().getWritableDatabase();
        try {
            super.beginTransaction();
            writableDatabase.execSQL("DELETE FROM `account`");
            writableDatabase.execSQL("DELETE FROM `budget`");
            writableDatabase.execSQL("DELETE FROM `budgetCategory`");
            writableDatabase.execSQL("DELETE FROM `currency`");
            writableDatabase.execSQL("DELETE FROM `category`");
            writableDatabase.execSQL("DELETE FROM `subcategory`");
            writableDatabase.execSQL("DELETE FROM `debt`");
            writableDatabase.execSQL("DELETE FROM `debtTrans`");
            writableDatabase.execSQL("DELETE FROM `goal`");
            writableDatabase.execSQL("DELETE FROM `goalTrans`");
            writableDatabase.execSQL("DELETE FROM `media`");
            writableDatabase.execSQL("DELETE FROM `trans`");
            writableDatabase.execSQL("DELETE FROM `wallet`");
            writableDatabase.execSQL("DELETE FROM `recurring`");
            writableDatabase.execSQL("DELETE FROM `template`");
            super.setTransactionSuccessful();
        } finally {
            super.endTransaction();
            writableDatabase.query("PRAGMA wal_checkpoint(FULL)").close();
            if (!writableDatabase.inTransaction()) {
                writableDatabase.execSQL("VACUUM");
            }
        }
    }

    @Override // androidx.room.RoomDatabase
    protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
        HashMap hashMap = new HashMap();
        hashMap.put(AccountDaoObject.class, AccountDaoObject_Impl.getRequiredConverters());
        hashMap.put(BudgetDaoObject.class, BudgetDaoObject_Impl.getRequiredConverters());
        hashMap.put(CategoryDaoObject.class, CategoryDaoObject_Impl.getRequiredConverters());
        hashMap.put(CalenderDaoObject.class, CalenderDaoObject_Impl.getRequiredConverters());
        hashMap.put(DebtDaoObject.class, DebtDaoObject_Impl.getRequiredConverters());
        hashMap.put(GoalDaoObject.class, GoalDaoObject_Impl.getRequiredConverters());
        hashMap.put(StatisticDaoObject.class, StatisticDaoObject_Impl.getRequiredConverters());
        hashMap.put(TransDaoObject.class, TransDaoObject_Impl.getRequiredConverters());
        hashMap.put(WalletDaoObject.class, WalletDaoObject_Impl.getRequiredConverters());
        hashMap.put(ExportDaoObject.class, ExportDaoObject_Impl.getRequiredConverters());
        hashMap.put(CurrencyDaoObject.class, CurrencyDaoObject_Impl.getRequiredConverters());
        hashMap.put(RecurringDaoObject.class, RecurringDaoObject_Impl.getRequiredConverters());
        hashMap.put(TemplateDaoObject.class, TemplateDaoObject_Impl.getRequiredConverters());
        return hashMap;
    }

    @Override // androidx.room.RoomDatabase
    public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
        return new HashSet();
    }

    @Override // androidx.room.RoomDatabase
    public List<Migration> getAutoMigrations(Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecsMap) {
        return Arrays.asList(new Migration[0]);
    }

    @Override // com.ktwapps.walletmanager.Database.AppDatabaseObject
    public AccountDaoObject accountDaoObject() {
        AccountDaoObject accountDaoObject;
        if (this._accountDaoObject != null) {
            return this._accountDaoObject;
        }
        synchronized (this) {
            if (this._accountDaoObject == null) {
                this._accountDaoObject = new AccountDaoObject_Impl(this);
            }
            accountDaoObject = this._accountDaoObject;
        }
        return accountDaoObject;
    }

    @Override // com.ktwapps.walletmanager.Database.AppDatabaseObject
    public BudgetDaoObject budgetDaoObject() {
        BudgetDaoObject budgetDaoObject;
        if (this._budgetDaoObject != null) {
            return this._budgetDaoObject;
        }
        synchronized (this) {
            if (this._budgetDaoObject == null) {
                this._budgetDaoObject = new BudgetDaoObject_Impl(this);
            }
            budgetDaoObject = this._budgetDaoObject;
        }
        return budgetDaoObject;
    }

    @Override // com.ktwapps.walletmanager.Database.AppDatabaseObject
    public CategoryDaoObject categoryDaoObject() {
        CategoryDaoObject categoryDaoObject;
        if (this._categoryDaoObject != null) {
            return this._categoryDaoObject;
        }
        synchronized (this) {
            if (this._categoryDaoObject == null) {
                this._categoryDaoObject = new CategoryDaoObject_Impl(this);
            }
            categoryDaoObject = this._categoryDaoObject;
        }
        return categoryDaoObject;
    }

    @Override // com.ktwapps.walletmanager.Database.AppDatabaseObject
    public CalenderDaoObject calenderDaoObject() {
        CalenderDaoObject calenderDaoObject;
        if (this._calenderDaoObject != null) {
            return this._calenderDaoObject;
        }
        synchronized (this) {
            if (this._calenderDaoObject == null) {
                this._calenderDaoObject = new CalenderDaoObject_Impl(this);
            }
            calenderDaoObject = this._calenderDaoObject;
        }
        return calenderDaoObject;
    }

    @Override // com.ktwapps.walletmanager.Database.AppDatabaseObject
    public DebtDaoObject debtDaoObject() {
        DebtDaoObject debtDaoObject;
        if (this._debtDaoObject != null) {
            return this._debtDaoObject;
        }
        synchronized (this) {
            if (this._debtDaoObject == null) {
                this._debtDaoObject = new DebtDaoObject_Impl(this);
            }
            debtDaoObject = this._debtDaoObject;
        }
        return debtDaoObject;
    }

    @Override // com.ktwapps.walletmanager.Database.AppDatabaseObject
    public GoalDaoObject goalDaoObject() {
        GoalDaoObject goalDaoObject;
        if (this._goalDaoObject != null) {
            return this._goalDaoObject;
        }
        synchronized (this) {
            if (this._goalDaoObject == null) {
                this._goalDaoObject = new GoalDaoObject_Impl(this);
            }
            goalDaoObject = this._goalDaoObject;
        }
        return goalDaoObject;
    }

    @Override // com.ktwapps.walletmanager.Database.AppDatabaseObject
    public StatisticDaoObject statisticDaoObject() {
        StatisticDaoObject statisticDaoObject;
        if (this._statisticDaoObject != null) {
            return this._statisticDaoObject;
        }
        synchronized (this) {
            if (this._statisticDaoObject == null) {
                this._statisticDaoObject = new StatisticDaoObject_Impl(this);
            }
            statisticDaoObject = this._statisticDaoObject;
        }
        return statisticDaoObject;
    }

    @Override // com.ktwapps.walletmanager.Database.AppDatabaseObject
    public TransDaoObject transDaoObject() {
        TransDaoObject transDaoObject;
        if (this._transDaoObject != null) {
            return this._transDaoObject;
        }
        synchronized (this) {
            if (this._transDaoObject == null) {
                this._transDaoObject = new TransDaoObject_Impl(this);
            }
            transDaoObject = this._transDaoObject;
        }
        return transDaoObject;
    }

    @Override // com.ktwapps.walletmanager.Database.AppDatabaseObject
    public WalletDaoObject walletDaoObject() {
        WalletDaoObject walletDaoObject;
        if (this._walletDaoObject != null) {
            return this._walletDaoObject;
        }
        synchronized (this) {
            if (this._walletDaoObject == null) {
                this._walletDaoObject = new WalletDaoObject_Impl(this);
            }
            walletDaoObject = this._walletDaoObject;
        }
        return walletDaoObject;
    }

    @Override // com.ktwapps.walletmanager.Database.AppDatabaseObject
    public ExportDaoObject exportDaoObject() {
        ExportDaoObject exportDaoObject;
        if (this._exportDaoObject != null) {
            return this._exportDaoObject;
        }
        synchronized (this) {
            if (this._exportDaoObject == null) {
                this._exportDaoObject = new ExportDaoObject_Impl(this);
            }
            exportDaoObject = this._exportDaoObject;
        }
        return exportDaoObject;
    }

    @Override // com.ktwapps.walletmanager.Database.AppDatabaseObject
    public CurrencyDaoObject currencyDaoObject() {
        CurrencyDaoObject currencyDaoObject;
        if (this._currencyDaoObject != null) {
            return this._currencyDaoObject;
        }
        synchronized (this) {
            if (this._currencyDaoObject == null) {
                this._currencyDaoObject = new CurrencyDaoObject_Impl(this);
            }
            currencyDaoObject = this._currencyDaoObject;
        }
        return currencyDaoObject;
    }

    @Override // com.ktwapps.walletmanager.Database.AppDatabaseObject
    public RecurringDaoObject recurringDaoObject() {
        RecurringDaoObject recurringDaoObject;
        if (this._recurringDaoObject != null) {
            return this._recurringDaoObject;
        }
        synchronized (this) {
            if (this._recurringDaoObject == null) {
                this._recurringDaoObject = new RecurringDaoObject_Impl(this);
            }
            recurringDaoObject = this._recurringDaoObject;
        }
        return recurringDaoObject;
    }

    @Override // com.ktwapps.walletmanager.Database.AppDatabaseObject
    public TemplateDaoObject templateDaoObject() {
        TemplateDaoObject templateDaoObject;
        if (this._templateDaoObject != null) {
            return this._templateDaoObject;
        }
        synchronized (this) {
            if (this._templateDaoObject == null) {
                this._templateDaoObject = new TemplateDaoObject_Impl(this);
            }
            templateDaoObject = this._templateDaoObject;
        }
        return templateDaoObject;
    }
}
