package com.expance.merapaisa.Utility;

import android.content.Context;
import java.util.Calendar;
import java.util.Date;

/* loaded from: classes3.dex */
public class StatisticHelper {
    public static long getPieStartDate(Context context, Date date, int type) {
        if (type != 0) {
            if (type != 1) {
                if (type != 2) {
                    if (type != 3) {
                        if (type != 4) {
                            return 0L;
                        }
                        return CalendarHelper.getYearlyStartDate(date);
                    }
                    return CalendarHelper.getQuarterlyStartDate(date);
                }
                return CalendarHelper.getMonthlyStartDate(date);
            }
            return CalendarHelper.getWeeklyStartDate(context, date);
        }
        return CalendarHelper.getDailyStartDate(date);
    }

    public static long getPieEndDate(Context context, Date date, int type) {
        if (type != 0) {
            if (type != 1) {
                if (type != 2) {
                    if (type != 3) {
                        if (type != 4) {
                            return 0L;
                        }
                        return CalendarHelper.getYearlyEndDate(date);
                    }
                    return CalendarHelper.getQuarterlyEndDate(date);
                }
                return CalendarHelper.getMonthlyEndDate(date);
            }
            return CalendarHelper.getWeeklyEndDate(context, date);
        }
        return CalendarHelper.getDailyEndDate(date);
    }

    public static Date incrementPieDate(Date date, int type, int increment) {
        if (type != 0) {
            if (type != 1) {
                if (type != 2) {
                    if (type == 3) {
                        return CalendarHelper.incrementQuarter(date, increment);
                    }
                    return CalendarHelper.incrementYear(date, increment);
                }
                return CalendarHelper.incrementMonth(date, increment);
            }
            return CalendarHelper.incrementWeek(date, increment);
        }
        return CalendarHelper.incrementDay(date, increment);
    }

    public static Date getWeeklyMarkerFirstDate(Context context, Date date) {
        int firstDayOfWeek = SharePreferenceHelper.getFirstDayOfWeek(context);
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(1);
        calendar.setTime(date);
        if (firstDayOfWeek > calendar.get(7)) {
            calendar.add(3, -1);
        }
        calendar.set(7, firstDayOfWeek);
        return calendar.getTime();
    }

    public static long getWeeklySpendingItemDate(Context context, Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(1);
        calendar.setTime(date);
        int firstDayOfWeek = SharePreferenceHelper.getFirstDayOfWeek(context);
        if (firstDayOfWeek > calendar.get(7)) {
            calendar.add(3, -1);
        }
        calendar.set(7, firstDayOfWeek);
        calendar.add(7, day - 1);
        calendar.set(14, 0);
        calendar.set(13, 0);
        calendar.set(12, 0);
        calendar.set(11, 0);
        return calendar.getTimeInMillis();
    }
}
