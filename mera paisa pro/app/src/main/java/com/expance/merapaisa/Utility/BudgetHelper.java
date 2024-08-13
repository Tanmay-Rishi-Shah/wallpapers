package com.expance.merapaisa.Utility;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateFormat;
import android.util.Log;

import com.expance.merapaisa.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/* loaded from: classes3.dex */
public class BudgetHelper {
    public static int getBudgetPeriodFromSpinner(int period) {
        if (period == 0) {
            return 1;
        }
        if (period == 1) {
            return 2;
        }
        if (period == 2) {
            return 3;
        }
        return period == 3 ? 5 : 1;
    }

    public static int getBudgetType(int type) {
        if (type == 0 || type == 1) {
            return 0;
        }
        if (type != 2) {
            return (type == 3 || type == 4) ? 2 : 3;
        }
        return 1;
    }

    public static String getFormattedDate(Context context, int period, Date date, Date budgetDate) {
        int budgetType = getBudgetType(period);
        if (budgetType != 0) {
            if (budgetType != 1) {
                if (budgetType == 2) {
                    return getFormattedQuarterlyDate(date, budgetDate);
                }
                return getFormattedYearlyDate(date, budgetDate);
            }
            return getFormattedMonthlyDate(date, budgetDate);
        }
        return getFormattedWeeklyDate(context, date, budgetDate);
    }

    public static String getFormattedWeeklyDate(Context context, Date date, Date budgetDate) {
        int firstDayOfWeek = SharePreferenceHelper.getFirstDayOfWeek(context);
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(1);
        calendar.setTime(date);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(budgetDate);
        calendar2.setFirstDayOfWeek(1);
        if (calendar2.getTimeInMillis() != 0) {
            firstDayOfWeek = calendar2.get(7);
        }
        int i = calendar.get(7);
        if (firstDayOfWeek > i) {
            calendar.add(3, -1);
        }
        calendar.set(7, firstDayOfWeek);
        Calendar calendar3 = Calendar.getInstance();
        calendar3.setFirstDayOfWeek(1);
        calendar3.setTime(date);
        if (firstDayOfWeek > i) {
            calendar3.add(3, -1);
        }
        calendar3.set(7, firstDayOfWeek);
        calendar3.add(7, 6);
        String str = DateHelper.isNotSameYear(calendar.getTime()) ? "dd/MM/yyyy" : "dd/MM";
        String str2 = DateHelper.isNotSameYear(calendar3.getTime()) ? "dd/MM/yyyy" : "dd/MM";
        Log.d("asd2", String.valueOf(calendar.getTime()));
        String bestDateTimePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), str);
        String bestDateTimePattern2 = DateFormat.getBestDateTimePattern(Locale.getDefault(), str2);
        String format = new SimpleDateFormat(bestDateTimePattern, Locale.getDefault()).format(calendar.getTime());
        return format + " - " + new SimpleDateFormat(bestDateTimePattern2, Locale.getDefault()).format(calendar3.getTime());
    }

    public static String getFormattedMonthlyDate(Date date, Date budgetDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(1);
        calendar.setTime(date);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setFirstDayOfWeek(1);
        calendar2.setTime(budgetDate);
        if (calendar2.getTimeInMillis() == 0) {
            calendar.set(5, 1);
        } else {
            calendar.set(5, calendar2.get(5));
        }
        int i = Calendar.getInstance().get(5);
        if (calendar.get(5) > i) {
            calendar.add(2, -1);
        }
        Calendar calendar3 = Calendar.getInstance();
        calendar3.setFirstDayOfWeek(1);
        calendar3.setTime(date);
        if (calendar2.getTimeInMillis() == 0) {
            calendar3.set(5, 1);
        } else {
            calendar3.set(5, calendar2.get(5));
        }
        if (calendar3.get(5) > i) {
            calendar3.add(2, -1);
        }
        calendar3.add(2, 1);
        calendar3.add(5, -1);
        String str = DateHelper.isNotSameYear(calendar.getTime()) ? "dd/MM/yyyy" : "dd/MM";
        String str2 = DateHelper.isNotSameYear(calendar3.getTime()) ? "dd/MM/yyyy" : "dd/MM";
        String bestDateTimePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), str);
        String bestDateTimePattern2 = DateFormat.getBestDateTimePattern(Locale.getDefault(), str2);
        String format = new SimpleDateFormat(bestDateTimePattern, Locale.getDefault()).format(calendar.getTime());
        return format + " - " + new SimpleDateFormat(bestDateTimePattern2, Locale.getDefault()).format(calendar3.getTime());
    }


    public static Date getQuarterDate() {
        Calendar var1;
        label30:
        {
            label29:
            {
                var1 = Calendar.getInstance();
                int var0 = var1.get(2);
                if (var0 != 0) {
                    if (var0 == 1) {
                        break label29;
                    }

                    if (var0 != 3) {
                        if (var0 == 4) {
                            break label29;
                        }

                        if (var0 != 6) {
                            if (var0 == 7) {
                                break label29;
                            }

                            if (var0 != 9) {
                                if (var0 != 10) {
                                    break label30;
                                }
                                break label29;
                            }
                        }
                    }
                }

                var1.set(2, 0);
            }

            var1.set(2, 1);
        }

        var1.set(2, 2);
        return var1.getTime();
    }

    public static String getFormattedQuarterlyDate(Date date, Date budgetDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(budgetDate);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date);
        if (calendar.getTimeInMillis() == 0) {
            calendar2.set(5, 1);
        } else {
            calendar2.set(5, calendar.get(5));
        }
        Calendar calendar3 = Calendar.getInstance();
        calendar3.setTime(date);
        if (calendar.getTimeInMillis() == 0) {
            calendar3.set(5, 1);
        } else {
            calendar3.set(5, calendar.get(5));
        }
        switch (calendar2.get(2)) {
            case 0:
            case 1:
            case 2:
                if (calendar.getTimeInMillis() == 0) {
                    calendar2.set(2, 0);
                    calendar3.set(2, 3);
                    break;
                } else {
                    calendar2.set(2, calendar.get(2));
                    calendar3.set(2, calendar.get(2) + 3);
                    break;
                }
            case 3:
            case 4:
            case 5:
                if (calendar.getTimeInMillis() == 0) {
                    calendar2.set(2, 3);
                    calendar3.set(2, 6);
                    break;
                } else {
                    calendar2.set(2, calendar.get(2) + 3);
                    calendar3.set(2, calendar.get(2) + 6);
                    break;
                }
            case 6:
            case 7:
            case 8:
                if (calendar.getTimeInMillis() == 0) {
                    calendar2.set(2, 6);
                    calendar3.set(2, 9);
                    break;
                } else {
                    calendar2.set(2, calendar.get(2) + 6);
                    calendar3.set(2, calendar.get(2) + 9);
                    break;
                }
            default:
                if (calendar.getTimeInMillis() == 0) {
                    calendar2.set(2, 9);
                    calendar3.set(2, 12);
                    break;
                } else {
                    calendar2.set(2, calendar.get(2) + 9);
                    calendar3.set(2, calendar.get(2) + 12);
                    break;
                }
        }
        if (calendar.getTimeInMillis() != 0) {
            Calendar calendar4 = Calendar.getInstance();
            calendar4.setTime(getQuarterDate());
            int i = calendar4.get(5);
            int i2 = calendar4.get(2);
            if (calendar.get(2) > i2) {
                calendar2.add(2, -3);
                calendar3.add(2, -3);
            } else if (calendar.get(2) == i2 && calendar.get(5) > i) {
                calendar2.add(2, -3);
                calendar3.add(2, -3);
            }
        }
        calendar3.add(5, -1);
        String str = DateHelper.isNotSameYear(calendar2.getTime()) ? "dd/MM/yyyy" : "dd/MM";
        String str2 = DateHelper.isNotSameYear(calendar3.getTime()) ? "dd/MM/yyyy" : "dd/MM";
        String bestDateTimePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), str);
        String bestDateTimePattern2 = DateFormat.getBestDateTimePattern(Locale.getDefault(), str2);
        String format = new SimpleDateFormat(bestDateTimePattern, Locale.getDefault()).format(calendar2.getTime());
        return format + " - " + new SimpleDateFormat(bestDateTimePattern2, Locale.getDefault()).format(calendar3.getTime());
    }

    public static String getFormattedYearlyDate(Date date, Date budgetDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(budgetDate);
        if (calendar2.getTimeInMillis() == 0) {
            calendar.set(5, 1);
            calendar.set(2, 0);
        } else {
            calendar.set(5, calendar2.get(5));
            calendar.set(2, calendar2.get(2));
        }
        int i = Calendar.getInstance().get(5);
        int i2 = Calendar.getInstance().get(2);
        if (calendar.get(2) > i2) {
            calendar.add(1, -1);
        } else if (calendar.get(2) == i2 && calendar.get(5) > i) {
            calendar.add(1, -1);
        }
        Calendar calendar3 = Calendar.getInstance();
        calendar3.setTime(date);
        if (calendar2.getTimeInMillis() == 0) {
            calendar3.set(5, 1);
            calendar3.set(2, 0);
        } else {
            calendar3.set(5, calendar2.get(5));
            calendar3.set(2, calendar2.get(2));
        }
        if (calendar.get(2) > i2) {
            calendar3.add(1, -1);
        } else if (calendar.get(2) == i2 && calendar.get(5) > i) {
            calendar3.add(1, -1);
        }
        calendar3.add(1, 1);
        calendar3.add(6, -1);
        String str = DateHelper.isNotSameYear(calendar.getTime()) ? "dd/MM/yyyy" : "dd/MM";
        String str2 = DateHelper.isNotSameYear(calendar3.getTime()) ? "dd/MM/yyyy" : "dd/MM";
        String bestDateTimePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), str);
        String bestDateTimePattern2 = DateFormat.getBestDateTimePattern(Locale.getDefault(), str2);
        String format = new SimpleDateFormat(bestDateTimePattern, Locale.getDefault()).format(calendar.getTime());
        return format + " - " + new SimpleDateFormat(bestDateTimePattern2, Locale.getDefault()).format(calendar3.getTime());
    }

    public static String getBudgetPeriod(Context context, int type) {
        if (type != 0) {
            if (type != 1) {
                if (type == 2) {
                    return context.getResources().getString(R.string.quarterly);
                }
                return context.getResources().getString(R.string.yearly);
            }
            return context.getResources().getString(R.string.monthly);
        }
        return context.getResources().getString(R.string.weekly);
    }

    public static String getRangeDate(Context context, int period, Date date, Date budgetDate) {
        int budgetType = getBudgetType(period);
        if (isBudgetPast(context, budgetType, date, budgetDate)) {
            return context.getResources().getString(R.string.finished);
        }
        if (isBudgetFuture(context, budgetType, date, budgetDate)) {
            return context.getResources().getString(R.string.not_start);
        }
        Calendar.getInstance().setTime(date);
        int budgetEndDate = (int) ((getBudgetEndDate(context, date, budgetType, budgetDate) - Calendar.getInstance().getTime().getTime()) / 86400000);
        if (budgetEndDate == 0) {
            budgetEndDate = 1;
        }
        Resources resources = context.getResources();
        return budgetEndDate > 1 ? resources.getString(R.string.days_left, Integer.valueOf(budgetEndDate)) : resources.getString(R.string.day_left, Integer.valueOf(budgetEndDate));
    }

    public static long getRecommended(Context context, int period, Date date, Date budgetDate, long remain) {
        int budgetType = getBudgetType(period);
        if (isBudgetPast(context, budgetType, date, budgetDate) || remain <= 0) {
            return 0L;
        }
        if (isBudgetFuture(context, budgetType, date, budgetDate)) {
            return remain / getChartMaximum(date, period, budgetDate);
        }
        int remainDay = getRemainDay(context, date, budgetType, budgetDate);
        return remainDay != 0 ? remain / remainDay : remain;
    }

    public static long getAverage(Context context, int period, Date date, Date budgetDate, long spent) {
        int budgetType = getBudgetType(period);
        if (isBudgetPast(context, budgetType, date, budgetDate)) {
            int chartMaximum = getChartMaximum(date, period, budgetDate);
            if (spent > 0) {
                return spent / chartMaximum;
            }
            return 0L;
        } else if (spent == 0 || isBudgetFuture(context, budgetType, date, budgetDate)) {
            return 0L;
        } else {
            return spent / getPastDay(context, date, budgetType, budgetDate);
        }
    }

    public static int getChartMaximum(Date date, int period, Date budgetDate) {
        int budgetType = getBudgetType(period);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (budgetType != 0) {
            if (budgetType == 1) {
                if (calendar.get(5) > Calendar.getInstance().get(5)) {
                    calendar.add(2, -1);
                }
                return calendar.getActualMaximum(5);
            } else if (budgetType == 2) {
                calendar.setTime(date);
                if (budgetDate.getTime() == 0) {
                    int i = calendar.get(2);
                    if (i != 0 && i != 1 && i != 2) {
                        return (i == 3 || i == 4 || i == 5) ? 91 : 92;
                    }
                    calendar.set(2, 1);
                    return calendar.getActualMaximum(5) == 28 ? 90 : 91;
                }
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(budgetDate);
                Calendar calendar3 = Calendar.getInstance();
                calendar3.setTime(date);
                calendar.set(5, calendar2.get(5));
                calendar3.set(5, calendar2.get(5));
                calendar.set(11, 0);
                calendar3.set(11, 0);
                calendar.set(12, 0);
                calendar3.set(12, 0);
                calendar.set(13, 0);
                calendar3.set(13, 0);
                calendar.set(14, 0);
                calendar3.set(14, 0);
                switch (calendar.get(2)) {
                    case 0:
                    case 1:
                    case 2:
                        calendar.set(2, calendar2.get(2));
                        calendar3.set(2, calendar2.get(2) + 3);
                        break;
                    case 3:
                    case 4:
                    case 5:
                        calendar.set(2, calendar2.get(2) + 3);
                        calendar3.set(2, calendar2.get(2) + 6);
                        break;
                    case 6:
                    case 7:
                    case 8:
                        calendar.set(2, calendar2.get(2) + 6);
                        calendar3.set(2, calendar2.get(2) + 9);
                        break;
                    default:
                        calendar.set(2, calendar2.get(2) + 9);
                        calendar3.set(2, calendar2.get(2) + 12);
                        break;
                }
                Calendar calendar4 = Calendar.getInstance();
                calendar4.setTime(getQuarterDate());
                int i2 = calendar4.get(5);
                int i3 = calendar4.get(2);
                if (calendar2.get(2) > i3) {
                    calendar.add(2, -3);
                    calendar3.add(2, -3);
                } else if (calendar2.get(2) == i3 && calendar2.get(5) > i2) {
                    calendar.add(2, -3);
                    calendar3.add(2, -3);
                }
                return (int) ((calendar3.getTimeInMillis() - calendar.getTimeInMillis()) / 86400000);
            } else {
                int actualMaximum = calendar.getActualMaximum(6);
                if (budgetDate.getTime() != 0) {
                    Calendar calendar5 = Calendar.getInstance();
                    calendar5.setTime(budgetDate);
                    int i4 = Calendar.getInstance().get(5);
                    int i5 = Calendar.getInstance().get(2);
                    if (calendar5.get(2) > i5) {
                        calendar.add(1, -1);
                    } else if (calendar5.get(2) == i5 && calendar5.get(5) > i4) {
                        calendar.add(1, -1);
                    }
                    if (calendar5.get(2) >= 2) {
                        calendar.add(1, 1);
                    }
                    Log.d("asd", String.valueOf(calendar.get(1)));
                    return calendar.getActualMaximum(6);
                }
                return actualMaximum;
            }
        }
        return 7;
    }

    private static boolean isBudgetPast(Context context, int type, Date date, Date budgetDate) {
        return Calendar.getInstance().getTime().getTime() >= getBudgetEndDate(context, date, type, budgetDate);
    }

    private static boolean isBudgetFuture(Context context, int type, Date date, Date budgetDate) {
        return Calendar.getInstance().getTime().getTime() < getBudgetStartDate(context, date, type, budgetDate);
    }

    private static int getPastDay(Context context, Date date, int type, Date budgetDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(14, 0);
        calendar.set(13, 0);
        calendar.set(12, 0);
        calendar.set(14, 0);
        calendar.set(11, 0);
        calendar.add(5, 1);
        return (int) ((calendar.getTimeInMillis() - getBudgetStartDate(context, date, type, budgetDate)) / 86400000);
    }

    private static int getRemainDay(Context context, Date date, int type, Date budgetDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(14, 0);
        calendar.set(13, 0);
        calendar.set(12, 0);
        calendar.set(14, 0);
        calendar.set(11, 0);
        return (int) ((getBudgetEndDate(context, date, type, budgetDate) - calendar.getTime().getTime()) / 86400000);
    }

    public static long getBudgetStartDate(Context context, Date date, int type, Date budgetDate) {
        if (type != 0) {
            if (type != 1) {
                if (type == 2) {
                    return getQuarterlyStartDate(date, budgetDate);
                }
                return getYearlyStartDate(date, budgetDate);
            }
            return getMonthlyStartDate(date, budgetDate);
        }
        return getWeeklyStartDate(context, date, budgetDate);
    }

    public static long getBudgetEndDate(Context context, Date date, int type, Date budgetDate) {
        if (type != 0) {
            if (type != 1) {
                if (type == 2) {
                    return getQuarterlyEndDate(date, budgetDate);
                }
                return getYearlyEndDate(date, budgetDate);
            }
            return getMonthlyEndDate(date, budgetDate);
        }
        return getWeeklyEndDate(context, date, budgetDate);
    }

    public static Date getIncrementDate(Date date, int type, int increment) {
        if (type != 0) {
            if (type != 1) {
                if (type == 2) {
                    return CalendarHelper.incrementQuarter(date, increment);
                }
                return CalendarHelper.incrementYear(date, increment);
            }
            return CalendarHelper.incrementMonth(date, increment);
        }
        return CalendarHelper.incrementWeek(date, increment);
    }

    public static Date getBudgetFirstDate(Context context, Date date, int period, Date budget) {
        long budgetStartDate = getBudgetStartDate(context, date, getBudgetType(period), budget);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(budgetStartDate);
        return calendar.getTime();
    }

    public static Date getBudgetLastDate(Context context, Date date, int period, Date budgetDate) {
        long budgetEndDate = getBudgetEndDate(context, date, getBudgetType(period), budgetDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(budgetEndDate);
        calendar.add(5, -1);
        return calendar.getTime();
    }

    public static Date getBudgetDateFromMilli(long milli) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milli);
        return calendar.getTime();
    }

    public static long getWeeklyStartDate(Context context, Date date, Date budgetDate) {
        int firstDayOfWeek = SharePreferenceHelper.getFirstDayOfWeek(context);
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(1);
        calendar.setTime(date);
        int i = calendar.get(7);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setFirstDayOfWeek(1);
        calendar2.setTime(budgetDate);
        if (calendar.getTimeInMillis() != 0) {
            firstDayOfWeek = calendar2.get(7);
        }
        if (firstDayOfWeek > i) {
            calendar.add(3, -1);
        }
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        calendar.set(7, firstDayOfWeek);
        return calendar.getTime().getTime();
    }

    public static long getWeeklyEndDate(Context context, Date date, Date budgetDate) {
        int firstDayOfWeek = SharePreferenceHelper.getFirstDayOfWeek(context);
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(1);
        calendar.setTime(date);
        int i = calendar.get(7);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setFirstDayOfWeek(1);
        calendar2.setTime(budgetDate);
        if (calendar.getTimeInMillis() != 0) {
            firstDayOfWeek = calendar2.get(7);
        }
        if (firstDayOfWeek > i) {
            calendar.add(3, -1);
        }
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        calendar.set(7, firstDayOfWeek);
        calendar.add(3, 1);
        return calendar.getTime().getTime();
    }

    public static long getMonthlyStartDate(Date date, Date budgetDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(budgetDate);
        if (calendar2.getTimeInMillis() == 0) {
            calendar.set(5, 1);
        } else {
            calendar.set(5, calendar2.get(5));
        }
        if (calendar.get(5) > Calendar.getInstance().get(5)) {
            calendar.add(2, -1);
        }
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTime().getTime();
    }

    public static long getMonthlyEndDate(Date date, Date budgetDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(budgetDate);
        if (calendar2.getTimeInMillis() == 0) {
            calendar.set(5, 1);
        } else {
            calendar.set(5, calendar2.get(5));
        }
        if (calendar.get(5) > Calendar.getInstance().get(5)) {
            calendar.add(2, -1);
        }
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        calendar.add(2, 1);
        return calendar.getTime().getTime();
    }

    public static long getQuarterlyStartDate(Date date, Date budgetDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(budgetDate);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date);
        if (calendar.getTimeInMillis() == 0) {
            calendar2.set(5, 1);
        } else {
            calendar2.set(5, calendar.get(5));
        }
        calendar2.set(11, 0);
        calendar2.set(12, 0);
        calendar2.set(13, 0);
        calendar2.set(14, 0);
        switch (calendar2.get(2)) {
            case 0:
            case 1:
            case 2:
                if (calendar.getTimeInMillis() == 0) {
                    calendar2.set(2, 0);
                    break;
                } else {
                    calendar2.set(2, calendar.get(2));
                    break;
                }
            case 3:
            case 4:
            case 5:
                if (calendar.getTimeInMillis() == 0) {
                    calendar2.set(2, 3);
                    break;
                } else {
                    calendar2.set(2, calendar.get(2) + 3);
                    break;
                }
            case 6:
            case 7:
            case 8:
                if (calendar.getTimeInMillis() == 0) {
                    calendar2.set(2, 6);
                    break;
                } else {
                    calendar2.set(2, calendar.get(2) + 6);
                    break;
                }
            default:
                if (calendar.getTimeInMillis() == 0) {
                    calendar2.set(2, 9);
                    break;
                } else {
                    calendar2.set(2, calendar.get(2) + 9);
                    break;
                }
        }
        if (calendar.getTimeInMillis() != 0) {
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(getQuarterDate());
            int i = calendar3.get(5);
            int i2 = calendar3.get(2);
            if (calendar.get(2) > i2) {
                calendar2.add(2, -3);
            } else if (calendar.get(2) == i2 && calendar.get(5) > i) {
                calendar2.add(2, -3);
            }
        }
        return calendar2.getTime().getTime();
    }

    public static long getQuarterlyEndDate(Date date, Date budgetDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(budgetDate);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date);
        if (calendar.getTimeInMillis() == 0) {
            calendar2.set(5, 1);
        } else {
            calendar2.set(5, calendar.get(5));
        }
        calendar2.set(11, 0);
        calendar2.set(12, 0);
        calendar2.set(13, 0);
        calendar2.set(14, 0);
        switch (calendar2.get(2)) {
            case 0:
            case 1:
            case 2:
                if (calendar.getTimeInMillis() == 0) {
                    calendar2.set(2, 0);
                    break;
                } else {
                    calendar2.set(2, calendar.get(2));
                    break;
                }
            case 3:
            case 4:
            case 5:
                if (calendar.getTimeInMillis() == 0) {
                    calendar2.set(2, 3);
                    break;
                } else {
                    calendar2.set(2, calendar.get(2) + 3);
                    break;
                }
            case 6:
            case 7:
            case 8:
                if (calendar.getTimeInMillis() == 0) {
                    calendar2.set(2, 6);
                    break;
                } else {
                    calendar2.set(2, calendar.get(2) + 6);
                    break;
                }
            default:
                if (calendar.getTimeInMillis() == 0) {
                    calendar2.set(2, 9);
                    break;
                } else {
                    calendar2.set(2, calendar.get(2) + 9);
                    break;
                }
        }
        if (calendar.getTimeInMillis() != 0) {
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(getQuarterDate());
            int i = calendar3.get(5);
            int i2 = calendar3.get(2);
            if (calendar.get(2) > i2) {
                calendar2.add(2, -3);
            } else if (calendar.get(2) == i2 && calendar.get(5) > i) {
                calendar2.add(2, -3);
            }
        }
        calendar2.add(2, 3);
        return calendar2.getTime().getTime();
    }

    public static long getYearlyStartDate(Date date, Date budgetDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(budgetDate);
        if (calendar2.getTimeInMillis() == 0) {
            calendar.set(5, 1);
            calendar.set(2, 0);
        } else {
            calendar.set(5, calendar2.get(5));
            calendar.set(2, calendar2.get(2));
        }
        int i = Calendar.getInstance().get(5);
        int i2 = Calendar.getInstance().get(2);
        if (calendar.get(2) > i2) {
            calendar.add(1, -1);
        } else if (calendar.get(2) == i2 && calendar.get(5) > i) {
            calendar.add(1, -1);
        }
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTime().getTime();
    }

    public static long getYearlyEndDate(Date date, Date budgetDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(budgetDate);
        if (calendar2.getTimeInMillis() == 0) {
            calendar.set(5, 1);
            calendar.set(2, 0);
        } else {
            calendar.set(5, calendar2.get(5));
            calendar.set(2, calendar2.get(2));
        }
        int i = Calendar.getInstance().get(5);
        int i2 = Calendar.getInstance().get(2);
        if (calendar.get(2) > i2) {
            calendar.add(1, -1);
        } else if (calendar.get(2) == i2 && calendar.get(5) > i) {
            calendar.add(1, -1);
        }
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        calendar.add(1, 1);
        return calendar.getTime().getTime();
    }
}
