package com.expance.merapaisa.Utility;

import android.content.Context;
import android.text.format.DateFormat;
import com.expance.merapaisa.Model.ExportTimeRange;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/* loaded from: classes3.dex */
public class ExportHelper {
    public static Date getInitiatStartDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        calendar.set(5, 1);
        return calendar.getTime();
    }

    public static Date getInitialEndDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        calendar.set(5, 1);
        calendar.add(2, 1);
        calendar.add(5, -1);
        return calendar.getTime();
    }

    public static Date getStartDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTime();
    }

    public static Date getEndDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        calendar.add(5, 1);
        return calendar.getTime();
    }

    public static List<ExportTimeRange> getTimeRangeList(Context context, int type, long startDate, long endDate) {
        long timeInMillis;
        long timeInMillis2;
        long timeInMillis3;
        ArrayList arrayList = new ArrayList();
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        int i = 1;
        calendar.setFirstDayOfWeek(1);
        calendar2.setFirstDayOfWeek(1);
        calendar.setTimeInMillis(startDate);
        calendar2.setTimeInMillis(endDate);
        if (type == 0) {
            int i2 = (calendar2.get(1) - calendar.get(1)) + 1;
            int i3 = 0;
            while (i3 < i2) {
                long timeInMillis4 = calendar.getTimeInMillis();
                calendar.set(2, 0);
                calendar.set(5, i);
                calendar.set(11, 0);
                calendar.set(12, 0);
                calendar.set(13, 0);
                calendar.set(14, 0);
                calendar.add(i, i);
                if (calendar2.getTimeInMillis() > calendar.getTimeInMillis()) {
                    timeInMillis = calendar.getTimeInMillis();
                } else {
                    timeInMillis = calendar2.getTimeInMillis();
                }
                arrayList.add(new ExportTimeRange(getYearlyFormattedDate(timeInMillis4), timeInMillis4, timeInMillis));
                i3++;
                i = 1;
            }
        } else if (type == 1) {
            while (true) {
                timeInMillis2 = calendar.getTimeInMillis();
                calendar.set(5, 1);
                calendar.set(11, 0);
                calendar.set(12, 0);
                calendar.set(13, 0);
                calendar.set(14, 0);
                calendar.add(2, 1);
                if (calendar2.getTimeInMillis() <= calendar.getTimeInMillis()) {
                    break;
                }
                arrayList.add(new ExportTimeRange(getMonthlyFormattedDate(timeInMillis2), timeInMillis2, calendar.getTimeInMillis()));
            }
            arrayList.add(new ExportTimeRange(getMonthlyFormattedDate(timeInMillis2), timeInMillis2, calendar2.getTimeInMillis()));
        } else if (type == 2) {
            int firstDayOfWeek = SharePreferenceHelper.getFirstDayOfWeek(context);
            if (firstDayOfWeek > calendar.get(7)) {
                calendar.add(3, -1);
            }
            calendar.set(7, firstDayOfWeek);
            do {
                long timeInMillis5 = calendar.getTimeInMillis();
                calendar.set(11, 0);
                calendar.set(12, 0);
                calendar.set(13, 0);
                calendar.set(14, 0);
                calendar.add(5, 7);
                arrayList.add(new ExportTimeRange(getWeeklyFormattedDate(context, timeInMillis5), timeInMillis5, calendar.getTimeInMillis()));
            } while (calendar.getTimeInMillis() < calendar2.getTimeInMillis());
        } else if (type == 3) {
            while (true) {
                timeInMillis3 = calendar.getTimeInMillis();
                calendar.set(11, 0);
                calendar.set(12, 0);
                calendar.set(13, 0);
                calendar.set(14, 0);
                calendar.add(5, 1);
                if (calendar2.getTimeInMillis() <= calendar.getTimeInMillis()) {
                    break;
                }
                arrayList.add(new ExportTimeRange(getDailyFormattedDate(timeInMillis3), timeInMillis3, calendar.getTimeInMillis()));
            }
            arrayList.add(new ExportTimeRange(getDailyFormattedDate(timeInMillis3), timeInMillis3, calendar2.getTimeInMillis()));
        }
        return arrayList;
    }

    private static String getYearlyFormattedDate(long date) {
        return new SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), "yyyy"), Locale.getDefault()).format(Long.valueOf(date));
    }

    private static String getMonthlyFormattedDate(long date) {
        return new SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), "yyyy MMMM"), Locale.getDefault()).format(Long.valueOf(date));
    }

    private static String getWeeklyFormattedDate(Context context, long date) {
        int firstDayOfWeek = SharePreferenceHelper.getFirstDayOfWeek(context);
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(1);
        calendar.setTimeInMillis(date);
        int i = calendar.get(7);
        if (firstDayOfWeek > i) {
            calendar.add(3, -1);
        }
        calendar.set(7, firstDayOfWeek);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setFirstDayOfWeek(1);
        calendar2.setTimeInMillis(date);
        if (firstDayOfWeek > i) {
            calendar2.add(3, -1);
        }
        calendar2.set(7, firstDayOfWeek);
        calendar2.add(7, 6);
        String bestDateTimePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "dd/MM/yyyy");
        String bestDateTimePattern2 = DateFormat.getBestDateTimePattern(Locale.getDefault(), "dd/MM/yyyy");
        String format = new SimpleDateFormat(bestDateTimePattern, Locale.getDefault()).format(calendar.getTime());
        return format + " - " + new SimpleDateFormat(bestDateTimePattern2, Locale.getDefault()).format(calendar2.getTime());
    }

    private static String getDailyFormattedDate(long date) {
        return new SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(), "yyyy/MM/dd"), Locale.getDefault()).format(Long.valueOf(date));
    }
}
