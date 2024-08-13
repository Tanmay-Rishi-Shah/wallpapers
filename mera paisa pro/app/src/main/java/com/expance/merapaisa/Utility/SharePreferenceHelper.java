package com.expance.merapaisa.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.core.app.NotificationCompat;
import com.android.billingclient.api.BillingFlowParams;
import com.google.firebase.analytics.FirebaseAnalytics;

/* loaded from: classes3.dex */
public class SharePreferenceHelper {
    public static void toggleThemeMode(Context context) {
        boolean isDarkMode = isDarkMode(context);
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.PreferenceKey, 0).edit();
        edit.putInt("theme", !isDarkMode ? 1 : 0);
        edit.apply();
    }

    public static boolean isDarkMode(Context c) {
        return c.getSharedPreferences(Constant.PreferenceKey, 0).getInt("theme", 0) == 1;
    }

    public static int getThemeMode(Context context) {
        return context.getSharedPreferences(Constant.PreferenceKey, 0).getInt("theme", 0);
    }

    public static boolean checkTransactionShowCaseKey(Context context) {
        return context.getSharedPreferences(Constant.PreferenceKey, 0).getInt("transactionShowCase", -1) == 0;
    }

    public static void setTransactionShowCaseKey(Context context, int value) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.PreferenceKey, 0).edit();
        edit.putInt("transactionShowCase", value);
        edit.apply();
    }

    public static void setWalletShowCaseKey(Context context, int value) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.PreferenceKey, 0).edit();
        edit.putInt("walletShowCase", value);
        edit.apply();
    }

    public static boolean checkAccountKey(Context context) {
        return context.getSharedPreferences(Constant.PreferenceKey, 0).contains(BillingFlowParams.EXTRA_PARAM_KEY_ACCOUNT_ID);
    }

    public static void setAccount(Context context, int id, String symbol, String name) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.PreferenceKey, 0).edit();
        edit.putInt(BillingFlowParams.EXTRA_PARAM_KEY_ACCOUNT_ID, id);
        edit.putString("accountName", name);
        edit.putString("currencySymbol", symbol);
        edit.apply();
    }

    public static void removeAccount(Context context) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.PreferenceKey, 0).edit();
        edit.remove(BillingFlowParams.EXTRA_PARAM_KEY_ACCOUNT_ID);
        edit.remove("accountName");
        edit.remove("currencySymbol");
        edit.apply();
    }

    public static String getAccountName(Context context) {
        return context.getSharedPreferences(Constant.PreferenceKey, 0).getString("accountName", "personal");
    }

    public static int getAccountId(Context context) {
        return context.getSharedPreferences(Constant.PreferenceKey, 0).getInt(BillingFlowParams.EXTRA_PARAM_KEY_ACCOUNT_ID, 0);
    }

    public static String getAccountSymbol(Context context) {
        return context.getSharedPreferences(Constant.PreferenceKey, 0).getString("currencySymbol", "$");
    }

    public static void setCurrencyCode(Context context, String currency) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.PreferenceKey, 0).edit();
        edit.putString(FirebaseAnalytics.Param.CURRENCY, currency);
        edit.apply();
    }

    public static void setFingerprintEnable(Context context, boolean value) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.PreferenceKey, 0).edit();
        edit.putBoolean("fingerprint", value);
        edit.apply();
    }

    public static boolean isFingerprintEnable(Context context) {
        return context.getSharedPreferences(Constant.PreferenceKey, 0).getBoolean("fingerprint", false);
    }

    public static void setPasscode(Context context, String value) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.PreferenceKey, 0).edit();
        edit.putString("passcode", value);
        edit.apply();
    }

    public static void removePasscode(Context context) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.PreferenceKey, 0).edit();
        edit.remove("passcode");
        edit.apply();
    }

    public static boolean checkPasscode(Context context) {
        return context.getSharedPreferences(Constant.PreferenceKey, 0).contains("passcode");
    }

    public static String getPasscode(Context context) {
        return context.getSharedPreferences(Constant.PreferenceKey, 0).getString("passcode", "0000");
    }

    public static boolean isCarryOverOn(Context c) {
        return c.getSharedPreferences(Constant.PreferenceKey, 0).getInt("carryOver", 0) != 0;
    }

    public static boolean isFutureBalanceOn(Context c) {
        return c.getSharedPreferences(Constant.PreferenceKey, 0).getInt("futureBalance", 0) != 0;
    }

    public static void setPreferLanguage(Context context, String language) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.PreferenceKey, 0).edit();
        edit.putString("languages", language);
        edit.apply();
    }

    public static String getPreferLanguage(Context c) {
        return c.getSharedPreferences(Constant.PreferenceKey, 0).getString("languages", "");
    }

    public static void setFirstDayOfWeek(Context context, int day) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.PreferenceKey, 0).edit();
        edit.putInt("dayOfWeek", day);
        edit.apply();
    }

    public static int getFirstDayOfWeek(Context c) {
        return c.getSharedPreferences(Constant.PreferenceKey, 0).getInt("dayOfWeek", 1);
    }

    public static void setStartUpScreen(Context context, int screen) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.PreferenceKey, 0).edit();
        edit.putInt("startUpScreen", screen);
        edit.apply();
    }

    public static int getStartUpScreen(Context c) {
        return c.getSharedPreferences(Constant.PreferenceKey, 0).getInt("startUpScreen", 0);
    }

    public static void toggleCarryOver(Context context) {
        boolean isCarryOverOn = isCarryOverOn(context);
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.PreferenceKey, 0).edit();
        edit.putInt("carryOver", !isCarryOverOn ? 1 : 0);
        edit.apply();
    }

    public static void toggleFutureBalance(Context context) {
        boolean isFutureBalanceOn = isFutureBalanceOn(context);
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.PreferenceKey, 0).edit();
        edit.putInt("futureBalance", !isFutureBalanceOn ? 1 : 0);
        edit.apply();
    }

    public static void setPremium(Context context, int premium) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.PreferenceKey, 0).edit();
        edit.putInt("premium", premium);
        edit.apply();
    }

    public static int getPremium(Context context) {
        return context.getSharedPreferences(Constant.PreferenceKey, 0).getInt("premium", 1);
    }

    public static int getReminderTime(Context context) {
        return context.getSharedPreferences(Constant.PreferenceKey, 0).getInt(NotificationCompat.CATEGORY_REMINDER, 19);
    }

    public static void setReminderTime(Context context, int time) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.PreferenceKey, 0).edit();
        edit.putInt(NotificationCompat.CATEGORY_REMINDER, time);
        edit.apply();
    }
}
