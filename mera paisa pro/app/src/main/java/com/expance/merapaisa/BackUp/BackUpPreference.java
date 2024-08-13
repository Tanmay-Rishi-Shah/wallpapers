package com.expance.merapaisa.BackUp;

import android.content.Context;
import android.content.SharedPreferences;
import com.expance.merapaisa.Utility.Constant;

/* loaded from: classes3.dex */
public class BackUpPreference {
    private static final String GDRIVE_PREF = "g.drive.pref_file";

    public static void putGDriveBackUpTime(Context context, long time) {
        SharedPreferences.Editor edit = context.getSharedPreferences(GDRIVE_PREF, 0).edit();
        edit.putLong("time", time);
        edit.apply();
    }

    public static long retreiveGDriveBackUpTime(Context context) {
        return context.getSharedPreferences(GDRIVE_PREF, 0).getLong("time", 0L);
    }

    public static void setAutoBackup(Context context, int backup) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.PreferenceKey, 0).edit();
        edit.putInt("autoBackup", backup);
        edit.apply();
    }

    public static int getAutoBackup(Context context) {
        return context.getSharedPreferences(Constant.PreferenceKey, 0).getInt("autoBackup", 0);
    }
}
