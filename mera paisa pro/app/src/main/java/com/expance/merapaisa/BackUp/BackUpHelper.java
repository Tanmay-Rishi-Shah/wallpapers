package com.expance.merapaisa.BackUp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.DateHelper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.apache.commons.lang3.time.DateUtils;

/* loaded from: classes3.dex */
public class BackUpHelper {
    public static boolean isValidSQLite(File file) {
        if (file.exists() && file.canRead()) {
            try {
                FileReader fileReader = new FileReader(file);
                char[] cArr = new char[16];
                fileReader.read(cArr, 0, 16);
                String valueOf = String.valueOf(cArr);
                fileReader.close();
                return valueOf.equals("SQLite format 3\u0000");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static File copyFile(Context context, File file) {
        File file2 = new File(context.getFilesDir() + "/temp");
        if (!file2.exists()) {
            file2.mkdirs();
        }
        File file3 = new File(file2, file.getName());
        try {
            if (file.exists()) {
                FileChannel channel = new FileInputStream(file).getChannel();
                FileChannel channel2 = new FileOutputStream(file3).getChannel();
                channel2.transferFrom(channel, 0L, channel.size());
                channel.close();
                channel2.close();
                return file3;
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean restore(Context context, File file) {
        try {
            File databasePath = context.getDatabasePath("wallet-database");
            File file2 = new File(databasePath.getAbsoluteFile() + "-shm");
            File file3 = new File(databasePath.getAbsoluteFile() + "-wal");
            databasePath.delete();
            file2.delete();
            file3.delete();
            if (file.exists()) {
                FileChannel channel = new FileInputStream(file).getChannel();
                FileChannel channel2 = new FileOutputStream(databasePath).getChannel();
                channel2.transferFrom(channel, 0L, channel.size());
                channel.close();
                channel2.close();
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String generateName() {
        Date date = new Date();
        return "backup_" + new SimpleDateFormat("yyyyMMdd_hhmmss", Locale.ENGLISH).format(date) + ".db";
    }

    public static boolean validateDB(String db) {
        SQLiteDatabase openDatabase = SQLiteDatabase.openDatabase(db, null, 1);
        if (openDatabase != null) {
            try {
                openDatabase.rawQuery("SELECT * FROM account", null).close();
                openDatabase.rawQuery("SELECT * FROM budget", null).close();
                openDatabase.rawQuery("SELECT * FROM budgetCategory", null).close();
                openDatabase.rawQuery("SELECT * FROM category", null).close();
                openDatabase.rawQuery("SELECT * FROM currency", null).close();
                openDatabase.rawQuery("SELECT * FROM debt", null).close();
                openDatabase.rawQuery("SELECT * FROM debtTrans", null).close();
                openDatabase.rawQuery("SELECT * FROM goal", null).close();
                openDatabase.rawQuery("SELECT * FROM goalTrans", null).close();
                openDatabase.rawQuery("SELECT * FROM media", null).close();
                openDatabase.rawQuery("SELECT * FROM trans", null).close();
                openDatabase.rawQuery("SELECT * FROM wallet", null).close();
                openDatabase.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public static String getLastSyncDate(Context context, Long date) {
        long time = new Date().getTime() - date.longValue();
        return time <= DateUtils.MILLIS_PER_MINUTE ? context.getResources().getString(R.string.minute_ago, 1) : time <= DateUtils.MILLIS_PER_HOUR ? context.getResources().getString(R.string.minute_ago, Long.valueOf((time / 60) / 1000)) : time <= 86400000 ? context.getResources().getString(R.string.hour_ago, Long.valueOf(((time / 60) / 60) / 1000)) : time <= 604800000 ? DateHelper.getBackUpDate(date.longValue(), context) : context.getResources().getString(R.string.day_ago, Long.valueOf((((time / 24) / 60) / 60) / 1000));
    }
}
