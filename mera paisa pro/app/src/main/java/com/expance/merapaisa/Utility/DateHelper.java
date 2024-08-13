package com.expance.merapaisa.Utility;

import android.content.Context;
import android.text.format.DateFormat;
import com.expance.merapaisa.Database.Entity.GoalEntity;
import com.expance.merapaisa.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes3.dex */
public class DateHelper {
    public static Date getCurrentDateTime() {
        return Calendar.getInstance().getTime();
    }

    public static Date getInitialExportStartDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(5, 1);
        return calendar.getTime();
    }

    public static Date getInitialExportEndDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(5, 1);
        calendar.add(2, 1);
        calendar.add(5, -1);
        return calendar.getTime();
    }

    public static Date getGoalDateTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.add(2, 1);
        return calendar.getTime();
    }

    public static String getCreditDate(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        calendar.set(5, day);
        return new SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), "dd MMM yyyy"), Locale.getDefault()).format(calendar.getTime());
    }

    public static String getGoalRangeDate(Context context, GoalEntity goal) {
        if (goal.getStatus() == 1) {
            return "";
        }
        long time = goal.getExpectDate().getTime() - Calendar.getInstance().getTimeInMillis();
        if (time <= 0) {
            return "";
        }
        int i = (int) (time / 86400000);
        if (i == 0) {
            i = 1;
        }
        if (i > 1) {
            return context.getResources().getString(R.string.days_left, Integer.valueOf(i));
        }
        context.getResources().getString(R.string.day_left, Integer.valueOf(i));
        return "";
    }

    public static String getTransFormattedDate(Date date) {
        String bestDateTimePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "dd MMM");
        return new SimpleDateFormat("EEEE", Locale.getDefault()).format(date) + ", " + new SimpleDateFormat(bestDateTimePattern, Locale.getDefault()).format(date);
    }

    public static String getBackUpDate(long time, Context context) {
        Date date = new Date();
        date.setTime(time);
        String bestDateTimePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), DateFormat.is24HourFormat(context) ? "kk:mm" : "hh:mm aa");
        return new SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), "yyyy MMM dd") + StringUtils.SPACE + bestDateTimePattern, Locale.getDefault()).format(Long.valueOf(date.getTime()));
    }

    public static String getFormattedDateTime(Context context, Date date) {
        String formattedDate = getFormattedDate(context, date);
        String bestDateTimePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), DateFormat.is24HourFormat(context) ? "kk:mm" : "hh:mm aa");
        return formattedDate + StringUtils.SPACE + new SimpleDateFormat(bestDateTimePattern, Locale.getDefault()).format(Long.valueOf(date.getTime()));
    }

    public static String getFormattedDate(Context context, Date date) {
        return new SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), "yyyy/MM/dd"), Locale.getDefault()).format(Long.valueOf(date.getTime()));
    }

    public static String getSimpleFormattedDate(Date date) {
        return new SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), "yyyy MMM dd"), Locale.getDefault()).format(Long.valueOf(date.getTime()));
    }

    public static long getTodayMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(11, 0);
        calendar.set(13, 0);
        calendar.set(12, 0);
        return calendar.getTime().getTime();
    }

    public static String getFormattedTime(Date date, Context context) {
        return new SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), DateFormat.is24HourFormat(context) ? "kk:mm" : "hh:mm aa"), Locale.getDefault()).format(Long.valueOf(date.getTime()));
    }

    public static String getDateFromPicker(Context context, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return getFormattedDate(context, calendar.getTime());
    }

    public static String getTimeFromPicker(Context context, int hour, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(11, hour);
        calendar.set(12, minutes);
        return getFormattedTime(calendar.getTime(), context);
    }

    public static long getTransactionStartDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTime().getTime();
    }

    public static long getTransactionEndDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        calendar.add(5, 1);
        return calendar.getTime().getTime();
    }

    public static boolean isNotSameYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(1) != calendar2.get(1);
    }

    public static boolean isBeforeDate(Date date1, Date date2) {
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar.setTime(date1);
        calendar2.setTime(date2);
        if (date2.before(date1)) {
            return true;
        }
        return calendar.get(6) == calendar2.get(6) && calendar.get(1) == calendar2.get(1);
    }

    public static boolean isSameDay(long date1, long date2) {
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar.setTimeInMillis(date1);
        calendar2.setTimeInMillis(date2);
        return calendar.get(1) == calendar2.get(1) && calendar.get(6) == calendar2.get(6);
    }

    public static boolean isDatePast(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(5, 1);
        calendar.set(14, 0);
        calendar.set(13, 0);
        calendar.set(12, 0);
        calendar.set(11, 0);
        long timeInMillis = calendar.getTimeInMillis();
        calendar.setTime(date);
        return timeInMillis > calendar.getTimeInMillis();
    }
}
