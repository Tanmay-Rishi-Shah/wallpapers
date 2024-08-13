package com.expance.merapaisa.Utility;

import com.expance.merapaisa.Model.CalendarRecord;
import com.expance.merapaisa.Model.Recurring;
import com.expance.merapaisa.Model.RecurringCalendarDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/* loaded from: classes3.dex */
public class RecurringHelper {
    public static boolean isValidRecurring(long untilTime, long nextOccurrenceTime) {
        if (untilTime != 0) {
            Calendar calendar = Calendar.getInstance();
            Calendar calendar2 = Calendar.getInstance();
            calendar.setTimeInMillis(untilTime);
            calendar2.setTimeInMillis(nextOccurrenceTime);
            long timeInMillis = calendar.getTimeInMillis();
            long timeInMillis2 = calendar2.getTimeInMillis();
            return timeInMillis2 <= timeInMillis || DateHelper.isSameDay(timeInMillis, timeInMillis2);
        }
        return true;
    }

    public static long getNextOccurrence(Date date, long lastUpdateTime, int type, int increment, int repeatType, String repeatDate) {
        Calendar calendar = Calendar.getInstance();
        int i = (lastUpdateTime > 0L ? 1 : (lastUpdateTime == 0L ? 0 : -1));
        calendar.setTimeInMillis(i <= 0 ? date.getTime() : lastUpdateTime);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        if (type != 1) {
            if (type != 2) {
                if (type != 3) {
                    if (type == 4 && i > 0) {
                        if (calendar.get(5) == 29) {
                            calendar.add(1, increment);
                            if (calendar.get(2) == 1 && calendar.getActualMaximum(5) < 29) {
                                while (true) {
                                    calendar.add(1, increment);
                                    if (calendar.get(2) == 1) {
                                        if (calendar.getActualMaximum(5) >= 29) {
                                            calendar.set(5, 29);
                                            break;
                                        }
                                    } else {
                                        calendar.set(5, 29);
                                        break;
                                    }
                                }
                            }
                        } else {
                            calendar.add(1, increment);
                        }
                    }
                } else if (i <= 0) {
                    if (repeatType == 1) {
                        calendar.set(5, calendar.getActualMaximum(5));
                    }
                } else if (repeatType == 1) {
                    calendar.set(5, calendar.getActualMaximum(5));
                    if (lastUpdateTime > calendar.getTime().getTime() || DateHelper.isSameDay(calendar.getTime().getTime(), lastUpdateTime)) {
                        calendar.set(5, 1);
                        calendar.add(2, increment);
                        calendar.set(5, calendar.getActualMaximum(5));
                    }
                } else {
                    Calendar calendar2 = Calendar.getInstance();
                    calendar2.setTime(date);
                    calendar.set(5, calendar2.get(5));
                    calendar.add(2, increment);
                    int i2 = calendar.get(5);
                    if (i2 == 31 || i2 == 30 || i2 == 29) {
                        calendar.set(5, 1);
                        while (calendar.getActualMaximum(5) < i2) {
                            calendar.add(2, increment);
                        }
                        calendar.set(5, i2);
                    }
                }
            } else if (i <= 0) {
                boolean z = false;
                while (true) {
                    calendar.set(7, 1);
                    int i3 = 0;
                    while (i3 < 7) {
                        int i4 = i3 + 1;
                        calendar.set(7, i4);
                        if (repeatDate.charAt(i3) == '1' && (calendar.getTime().getTime() > date.getTime() || DateHelper.isSameDay(date.getTime(), lastUpdateTime))) {
                            z = true;
                            break;
                        }
                        i3 = i4;
                    }
                    if (z) {
                        break;
                    }
                    calendar.add(3, increment);
                }
            } else {
                boolean z2 = false;
                while (true) {
                    calendar.set(7, 1);
                    int i5 = 0;
                    while (true) {
                        if (i5 >= 7) {
                            break;
                        }
                        int i6 = i5 + 1;
                        calendar.set(7, i6);
                        if (repeatDate.charAt(i5) == '1' && calendar.getTime().getTime() > lastUpdateTime && !DateHelper.isSameDay(calendar.getTime().getTime(), lastUpdateTime)) {
                            z2 = true;
                            break;
                        }
                        i5 = i6;
                    }
                    if (z2) {
                        break;
                    }
                    calendar.add(3, increment);
                }
            }
        } else if (i > 0) {
            calendar.add(5, increment);
        }
        return calendar.getTimeInMillis();
    }

    public static long getUntilDate(Date date, Date untilDate, int untilType, int type, int increment, int repeatTime, int repeatType, String repeatDate) {
        if (untilType == 1) {
            return getUntilTimeByDate(untilDate);
        }
        if (untilType == 2) {
            if (type != 1) {
                if (type != 2) {
                    if (type == 3) {
                        return getMonthlyUntilTimeByQuantity(date, increment, repeatTime, repeatType);
                    }
                    return getYearlyUntilTimeByQuantity(date, increment, repeatTime);
                }
                return getWeeklyUntilTimeByQuantity(date, increment, repeatTime, repeatDate);
            }
            return getDailyUntilTimeByQuantity(date, increment, repeatTime);
        }
        return 0L;
    }

    private static long getUntilTimeByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(14, 0);
        calendar.set(13, 0);
        calendar.set(12, 0);
        calendar.set(11, 0);
        return calendar.getTimeInMillis();
    }

    private static long getDailyUntilTimeByQuantity(Date date, int increment, int quantity) {
        int i = increment * quantity;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(5, i);
        calendar.set(14, 0);
        calendar.set(13, 0);
        calendar.set(12, 0);
        calendar.set(11, 0);
        return calendar.getTimeInMillis();
    }

    private static long getWeeklyUntilTimeByQuantity(Date date, int increment, int quantity, String repeatDate) {
        int i = 0;
        for (int i2 = 0; i2 < 7; i2++) {
            if (repeatDate.charAt(i2) == '1') {
                i = i2 + 1;
            }
        }
        int i3 = increment * quantity;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(7, i);
        calendar.add(3, i3);
        calendar.set(14, 0);
        calendar.set(13, 0);
        calendar.set(12, 0);
        calendar.set(11, 0);
        return calendar.getTimeInMillis();
    }

    private static long getMonthlyUntilTimeByQuantity(Date date, int increment, int quantity, int repeatType) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (repeatType == 1) {
            int i = increment * quantity;
            if (calendar.get(5) != calendar.getActualMaximum(5)) {
                i--;
            }
            calendar.set(5, 1);
            calendar.add(2, i);
            calendar.set(5, calendar.getActualMaximum(5));
        } else {
            int i2 = calendar.get(5);
            if (i2 == 31 || i2 == 30 || i2 == 29) {
                calendar.set(5, 1);
                do {
                    calendar.add(2, increment);
                    if (calendar.getActualMaximum(5) >= i2) {
                        quantity--;
                        continue;
                    }
                } while (quantity > 0);
                calendar.set(5, i2);
            } else {
                calendar.add(2, increment * quantity);
            }
        }
        calendar.set(14, 0);
        calendar.set(13, 0);
        calendar.set(12, 0);
        calendar.set(11, 0);
        return calendar.getTimeInMillis();
    }

    private static long getYearlyUntilTimeByQuantity(Date date, int increment, int quantity) {
        int i = increment * quantity;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (calendar.get(2) == 1 && calendar.get(5) == 29) {
            i *= 4;
        }
        calendar.add(1, i);
        calendar.set(14, 0);
        calendar.set(13, 0);
        calendar.set(12, 0);
        calendar.set(11, 0);
        return calendar.getTimeInMillis();
    }

    public static List getCalendarRecurring(Recurring var0, float var1, Date var2) {
        Calendar var19 = Calendar.getInstance();
        var19.setTime(var2);
        ArrayList var20 = new ArrayList();
        var19.set(14, 0);
        var19.set(13, 0);
        var19.set(12, 0);
        var19.set(11, 0);
        var19.set(5, 1);
        long var13 = var19.getTimeInMillis();
        var19.add(2, 1);
        long var17 = var19.getTimeInMillis();
        long var7 = var0.getLastUpdateTime().getTime();
        if (var0.getLastUpdateTime().getTime() <= 0L) {
            var2 = var0.getDateTime();
        } else {
            var2 = var0.getLastUpdateTime();
        }

        var19.setTimeInMillis(var2.getTime());
        long var15 = var0.getUntilTime().getTime();
        boolean var3;
        int var4;
        long var9;
        long var11;
        if (var0.getRecurringType() == 1) {
            for (var9 = var7; var19.getTime().getTime() <= var17 && !DateHelper.isSameDay(var19.getTime().getTime(), var17); var9 = var7) {
                if (var9 <= 0L) {
                    label536:
                    {
                        if (var19.getTime().getTime() <= var13) {
                            var7 = var9;
                            if (!DateHelper.isSameDay(var19.getTime().getTime(), var13)) {
                                break label536;
                            }
                        }

                        if (var15 != 0L) {
                            if (var19.getTime().getTime() >= var15 && !DateHelper.isSameDay(var19.getTime().getTime(), var15)) {
                                break;
                            }

                            if (var0.getAmount() < 0L) {
                                var3 = true;
                            } else {
                                var3 = false;
                            }

                            var9 = (long) ((float) var0.getAmount() * var1);
                            var11 = var19.getTimeInMillis();
                            var4 = var19.get(5);
                            if (var3) {
                                var7 = 0L;
                            } else {
                                var7 = var9;
                            }

                            if (!var3) {
                                var9 = 0L;
                            }

                            var20.add(new CalendarRecord(var4, var7, var9));
                            var7 = var11;
                        } else {
                            if (var0.getAmount() < 0L) {
                                var3 = true;
                            } else {
                                var3 = false;
                            }

                            var9 = (long) ((float) var0.getAmount() * var1);
                            var11 = var19.getTimeInMillis();
                            var4 = var19.get(5);
                            if (var3) {
                                var7 = 0L;
                            } else {
                                var7 = var9;
                            }

                            if (!var3) {
                                var9 = 0L;
                            }

                            var20.add(new CalendarRecord(var4, var7, var9));
                            var7 = var11;
                        }
                    }
                } else {
                    var7 = var9;
                    if (var19.getTime().getTime() > var9) {
                        var7 = var9;
                        if (!DateHelper.isSameDay(var19.getTime().getTime(), var9)) {
                            label580:
                            {
                                if (var19.getTime().getTime() <= var13) {
                                    var7 = var9;
                                    if (!DateHelper.isSameDay(var19.getTime().getTime(), var13)) {
                                        break label580;
                                    }
                                }

                                if (var15 != 0L) {
                                    if (var19.getTime().getTime() >= var15 && !DateHelper.isSameDay(var19.getTime().getTime(), var15)) {
                                        break;
                                    }

                                    if (var0.getAmount() < 0L) {
                                        var3 = true;
                                    } else {
                                        var3 = false;
                                    }

                                    var9 = (long) ((float) var0.getAmount() * var1);
                                    var11 = var19.getTimeInMillis();
                                    var4 = var19.get(5);
                                    if (var3) {
                                        var7 = 0L;
                                    } else {
                                        var7 = var9;
                                    }

                                    if (!var3) {
                                        var9 = 0L;
                                    }

                                    var20.add(new CalendarRecord(var4, var7, var9));
                                    var7 = var11;
                                } else {
                                    if (var0.getAmount() < 0L) {
                                        var3 = true;
                                    } else {
                                        var3 = false;
                                    }

                                    var9 = (long) ((float) var0.getAmount() * var1);
                                    var11 = var19.getTimeInMillis();
                                    var4 = var19.get(5);
                                    if (var3) {
                                        var7 = 0L;
                                    } else {
                                        var7 = var9;
                                    }

                                    if (!var3) {
                                        var9 = 0L;
                                    }

                                    var20.add(new CalendarRecord(var4, var7, var9));
                                    var7 = var11;
                                }
                            }
                        }
                    }
                }

                var19.add(5, var0.getIncrement());
            }
        } else {
            int var5;
            if (var0.getRecurringType() == 2) {
                String var21 = var0.getRepeatDate();
                var3 = false;

                while (true) {
                    var19.set(7, 1);
                    var4 = 0;
                    var9 = var7;

                    label515:
                    {
                        while (true) {
                            if (var4 >= 7) {
                                break label515;
                            }

                            label587:
                            {
                                var5 = var4 + 1;
                                var19.set(7, var5);
                                if (var21.charAt(var4) == '1') {
                                    if (var19.getTime().getTime() > var17 || DateHelper.isSameDay(var19.getTime().getTime(), var17)) {
                                        break;
                                    }

                                    int var6;
                                    boolean var23;
                                    if (var9 > 0L) {
                                        var7 = var9;
                                        if (var19.getTime().getTime() > var9) {
                                            var7 = var9;
                                            if (!DateHelper.isSameDay(var19.getTime().getTime(), var9)) {
                                                if (var19.getTime().getTime() <= var13) {
                                                    var7 = var9;
                                                    if (!DateHelper.isSameDay(var19.getTime().getTime(), var13)) {
                                                        break label587;
                                                    }
                                                }

                                                if (var15 != 0L) {
                                                    if (var19.getTime().getTime() >= var15 && !DateHelper.isSameDay(var19.getTime().getTime(), var15)) {
                                                        break;
                                                    }

                                                    if (var0.getAmount() < 0L) {
                                                        var23 = true;
                                                    } else {
                                                        var23 = false;
                                                    }

                                                    var9 = (long) ((float) var0.getAmount() * var1);
                                                    var11 = var19.getTimeInMillis();
                                                    var6 = var19.get(5);
                                                    if (var23) {
                                                        var7 = 0L;
                                                    } else {
                                                        var7 = var9;
                                                    }

                                                    if (!var23) {
                                                        var9 = 0L;
                                                    }

                                                    var20.add(new CalendarRecord(var6, var7, var9));
                                                    var7 = var11;
                                                } else {
                                                    if (var0.getAmount() < 0L) {
                                                        var23 = true;
                                                    } else {
                                                        var23 = false;
                                                    }

                                                    var9 = (long) ((float) var0.getAmount() * var1);
                                                    var11 = var19.getTimeInMillis();
                                                    var6 = var19.get(5);
                                                    if (var23) {
                                                        var7 = 0L;
                                                    } else {
                                                        var7 = var9;
                                                    }

                                                    if (!var23) {
                                                        var9 = 0L;
                                                    }

                                                    var20.add(new CalendarRecord(var6, var7, var9));
                                                    var7 = var11;
                                                }
                                            }
                                        }
                                        break label587;
                                    }

                                    if (var19.getTime().getTime() > var13 || DateHelper.isSameDay(var19.getTime().getTime(), var13)) {
                                        if (var19.getTime().getTime() <= var0.getDateTime().getTime() && !DateHelper.isSameDay(var19.getTime().getTime(), var0.getDateTime().getTime())) {
                                            var7 = var9;
                                            break label587;
                                        }

                                        if (var15 != 0L) {
                                            if (var19.getTime().getTime() >= var15 && !DateHelper.isSameDay(var19.getTime().getTime(), var15)) {
                                                break;
                                            }

                                            if (var0.getAmount() < 0L) {
                                                var23 = true;
                                            } else {
                                                var23 = false;
                                            }

                                            var9 = (long) ((float) var0.getAmount() * var1);
                                            var11 = var19.getTimeInMillis();
                                            var6 = var19.get(5);
                                            if (var23) {
                                                var7 = 0L;
                                            } else {
                                                var7 = var9;
                                            }

                                            if (!var23) {
                                                var9 = 0L;
                                            }

                                            var20.add(new CalendarRecord(var6, var7, var9));
                                            var7 = var11;
                                        } else {
                                            if (var0.getAmount() < 0L) {
                                                var23 = true;
                                            } else {
                                                var23 = false;
                                            }

                                            var9 = (long) ((float) var0.getAmount() * var1);
                                            var11 = var19.getTimeInMillis();
                                            var6 = var19.get(5);
                                            if (var23) {
                                                var7 = 0L;
                                            } else {
                                                var7 = var9;
                                            }

                                            if (!var23) {
                                                var9 = 0L;
                                            }

                                            var20.add(new CalendarRecord(var6, var7, var9));
                                            var7 = var11;
                                        }
                                        break label587;
                                    }
                                }

                                var7 = var9;
                            }

                            var4 = var5;
                            var9 = var7;
                        }

                        var3 = true;
                    }

                    if (var3) {
                        break;
                    }

                    var19.add(3, var0.getIncrement());
                    var7 = var9;
                }
            } else {
                var9 = var7;
                Calendar var22;
                if (var0.getRecurringType() == 3) {
                    var9 = var7;

                    while (true) {
                        var4 = var0.getRepeatType();
                        if (var4 == 1) {
                            var19.set(5, var19.getActualMaximum(5));
                        } else {
                            var22 = Calendar.getInstance();
                            var22.setTime(var0.getDateTime());
                            var19.set(5, var22.get(5));
                        }

                        if (var19.getTime().getTime() > var17 || DateHelper.isSameDay(var19.getTime().getTime(), var17)) {
                            break;
                        }

                        if (var9 <= 0L) {
                            label394:
                            {
                                if (var19.getTime().getTime() <= var13) {
                                    var7 = var9;
                                    if (!DateHelper.isSameDay(var19.getTime().getTime(), var13)) {
                                        break label394;
                                    }
                                }

                                if (var15 != 0L) {
                                    if (var19.getTime().getTime() >= var15 && !DateHelper.isSameDay(var19.getTime().getTime(), var15)) {
                                        break;
                                    }

                                    if (var0.getAmount() < 0L) {
                                        var3 = true;
                                    } else {
                                        var3 = false;
                                    }

                                    var9 = (long) ((float) var0.getAmount() * var1);
                                    var11 = var19.getTimeInMillis();
                                    var5 = var19.get(5);
                                    if (var3) {
                                        var7 = 0L;
                                    } else {
                                        var7 = var9;
                                    }

                                    if (!var3) {
                                        var9 = 0L;
                                    }

                                    var20.add(new CalendarRecord(var5, var7, var9));
                                    var7 = var11;
                                } else {
                                    if (var0.getAmount() < 0L) {
                                        var3 = true;
                                    } else {
                                        var3 = false;
                                    }

                                    var9 = (long) ((float) var0.getAmount() * var1);
                                    var11 = var19.getTimeInMillis();
                                    var5 = var19.get(5);
                                    if (var3) {
                                        var7 = 0L;
                                    } else {
                                        var7 = var9;
                                    }

                                    if (!var3) {
                                        var9 = 0L;
                                    }

                                    var20.add(new CalendarRecord(var5, var7, var9));
                                    var7 = var11;
                                }
                            }
                        } else {
                            var7 = var9;
                            if (var19.getTime().getTime() > var9) {
                                var7 = var9;
                                if (!DateHelper.isSameDay(var19.getTime().getTime(), var9)) {
                                    label584:
                                    {
                                        if (var19.getTime().getTime() <= var13) {
                                            var7 = var9;
                                            if (!DateHelper.isSameDay(var19.getTime().getTime(), var13)) {
                                                break label584;
                                            }
                                        }

                                        if (var15 != 0L) {
                                            if (var19.getTime().getTime() >= var15 && !DateHelper.isSameDay(var19.getTime().getTime(), var15)) {
                                                break;
                                            }

                                            if (var0.getAmount() < 0L) {
                                                var3 = true;
                                            } else {
                                                var3 = false;
                                            }

                                            var9 = (long) ((float) var0.getAmount() * var1);
                                            var11 = var19.getTimeInMillis();
                                            var5 = var19.get(5);
                                            if (var3) {
                                                var7 = 0L;
                                            } else {
                                                var7 = var9;
                                            }

                                            if (!var3) {
                                                var9 = 0L;
                                            }

                                            var20.add(new CalendarRecord(var5, var7, var9));
                                            var7 = var11;
                                        } else {
                                            if (var0.getAmount() < 0L) {
                                                var3 = true;
                                            } else {
                                                var3 = false;
                                            }

                                            var9 = (long) ((float) var0.getAmount() * var1);
                                            var11 = var19.getTimeInMillis();
                                            var5 = var19.get(5);
                                            if (var3) {
                                                var7 = 0L;
                                            } else {
                                                var7 = var9;
                                            }

                                            if (!var3) {
                                                var9 = 0L;
                                            }

                                            var20.add(new CalendarRecord(var5, var7, var9));
                                            var7 = var11;
                                        }
                                    }
                                }
                            }
                        }

                        if (var4 != 0) {
                            var19.add(2, var0.getIncrement());
                        } else {
                            int var24 = var19.get(5);
                            if (var24 != 31 && var24 != 30 && var24 != 29) {
                                var19.add(2, var0.getIncrement());
                            } else {
                                var19.set(5, 1);

                                do {
                                    var19.add(2, var0.getIncrement());
                                } while (var19.getActualMaximum(5) < var24);

                                var19.set(5, var24);
                            }
                        }

                        var9 = var7;
                    }
                } else {
                    while (true) {
                        label470:
                        while (true) {
                            do {
                                do {
                                    while (true) {
                                        var22 = Calendar.getInstance();
                                        var22.setTime(var0.getDateTime());
                                        var19.set(2, var22.get(2));
                                        var19.set(5, var22.get(5));
                                        if (var19.getTime().getTime() > var17 || DateHelper.isSameDay(var19.getTime().getTime(), var17)) {
                                            return var20;
                                        }

                                        label589:
                                        {
                                            if (var9 <= 0L) {
                                                if (var19.getTime().getTime() > var13 || DateHelper.isSameDay(var19.getTime().getTime(), var13)) {
                                                    if (var15 != 0L) {
                                                        if (var19.getTime().getTime() >= var15 && !DateHelper.isSameDay(var19.getTime().getTime(), var15)) {
                                                            return var20;
                                                        }

                                                        if (var0.getAmount() < 0L) {
                                                            var3 = true;
                                                        } else {
                                                            var3 = false;
                                                        }

                                                        var9 = (long) ((float) var0.getAmount() * var1);
                                                        var11 = var19.getTimeInMillis();
                                                        var4 = var19.get(5);
                                                        if (var3) {
                                                            var7 = 0L;
                                                        } else {
                                                            var7 = var9;
                                                        }

                                                        if (!var3) {
                                                            var9 = 0L;
                                                        }

                                                        var20.add(new CalendarRecord(var4, var7, var9));
                                                        var7 = var11;
                                                    } else {
                                                        if (var0.getAmount() < 0L) {
                                                            var3 = true;
                                                        } else {
                                                            var3 = false;
                                                        }

                                                        var9 = (long) ((float) var0.getAmount() * var1);
                                                        var11 = var19.getTimeInMillis();
                                                        var4 = var19.get(5);
                                                        if (var3) {
                                                            var7 = 0L;
                                                        } else {
                                                            var7 = var9;
                                                        }

                                                        if (!var3) {
                                                            var9 = 0L;
                                                        }

                                                        var20.add(new CalendarRecord(var4, var7, var9));
                                                        var7 = var11;
                                                    }
                                                    break label589;
                                                }
                                            } else if (var19.getTime().getTime() > var9 && !DateHelper.isSameDay(var19.getTime().getTime(), var9) && (var19.getTime().getTime() > var13 || DateHelper.isSameDay(var19.getTime().getTime(), var13))) {
                                                if (var15 != 0L) {
                                                    if (var19.getTime().getTime() >= var15 && !DateHelper.isSameDay(var19.getTime().getTime(), var15)) {
                                                        return var20;
                                                    }

                                                    if (var0.getAmount() < 0L) {
                                                        var3 = true;
                                                    } else {
                                                        var3 = false;
                                                    }

                                                    var9 = (long) ((float) var0.getAmount() * var1);
                                                    var11 = var19.getTimeInMillis();
                                                    var4 = var19.get(5);
                                                    if (var3) {
                                                        var7 = 0L;
                                                    } else {
                                                        var7 = var9;
                                                    }

                                                    if (!var3) {
                                                        var9 = 0L;
                                                    }

                                                    var20.add(new CalendarRecord(var4, var7, var9));
                                                    var7 = var11;
                                                } else {
                                                    if (var0.getAmount() < 0L) {
                                                        var3 = true;
                                                    } else {
                                                        var3 = false;
                                                    }

                                                    var9 = (long) ((float) var0.getAmount() * var1);
                                                    var11 = var19.getTimeInMillis();
                                                    var4 = var19.get(5);
                                                    if (var3) {
                                                        var7 = 0L;
                                                    } else {
                                                        var7 = var9;
                                                    }

                                                    if (!var3) {
                                                        var9 = 0L;
                                                    }

                                                    var20.add(new CalendarRecord(var4, var7, var9));
                                                    var7 = var11;
                                                }
                                                break label589;
                                            }

                                            var7 = var9;
                                        }

                                        if (var19.get(5) == 29) {
                                            var19.add(1, var0.getIncrement());
                                            var9 = var7;
                                            break;
                                        }

                                        var19.add(1, var0.getIncrement());
                                        var9 = var7;
                                    }
                                } while (var19.get(2) != 1);

                                var9 = var7;
                            } while (var19.getActualMaximum(5) >= 29);

                            do {
                                var19.add(1, var0.getIncrement());
                                if (var19.get(2) != 1) {
                                    var19.set(5, 29);
                                    var9 = var7;
                                    continue label470;
                                }
                            } while (var19.getActualMaximum(5) < 29);

                            var19.set(5, 29);
                            var9 = var7;
                        }
                    }
                }
            }
        }

        return var20;
    }

    public static long getCalendarRecurringCarryOver(Recurring var0, float var1, Date var2) {
        Calendar var19 = Calendar.getInstance();
        var19.setTime(var2);
        var19.set(14, 0);
        var19.set(13, 0);
        var19.set(12, 0);
        var19.set(11, 0);
        var19.set(5, 1);
        long var15 = var19.getTimeInMillis();
        long var7 = var0.getLastUpdateTime().getTime();
        if (var0.getLastUpdateTime().getTime() <= 0L) {
            var2 = var0.getDateTime();
        } else {
            var2 = var0.getLastUpdateTime();
        }

        var19.setTimeInMillis(var2.getTime());
        long var17 = var0.getUntilTime().getTime();
        float var3;
        long var9;
        long var11;
        long var13;
        if (var0.getRecurringType() == 1) {
            var9 = 0L;
            var11 = var7;

            while (true) {
                var7 = var9;
                if (var19.getTime().getTime() > var15) {
                    break;
                }

                if (DateHelper.isSameDay(var19.getTime().getTime(), var15)) {
                    var7 = var9;
                    break;
                }

                label259:
                {
                    if (var11 <= 0L) {
                        if (var17 != 0L) {
                            if (var19.getTime().getTime() >= var17) {
                                var7 = var9;
                                if (!DateHelper.isSameDay(var19.getTime().getTime(), var17)) {
                                    break;
                                }
                            }

                            var7 = var19.getTimeInMillis();
                            var3 = (float) var9;
                            var9 = var0.getAmount();
                        } else {
                            var7 = var19.getTimeInMillis();
                            var3 = (float) var9;
                            var9 = var0.getAmount();
                        }
                    } else {
                        var13 = var9;
                        var7 = var11;
                        if (var19.getTime().getTime() <= var11) {
                            break label259;
                        }

                        var13 = var9;
                        var7 = var11;
                        if (DateHelper.isSameDay(var19.getTime().getTime(), var11)) {
                            break label259;
                        }

                        if (var17 != 0L) {
                            if (var19.getTime().getTime() >= var17) {
                                var7 = var9;
                                if (!DateHelper.isSameDay(var19.getTime().getTime(), var17)) {
                                    break;
                                }
                            }

                            var7 = var19.getTimeInMillis();
                            var3 = (float) var9;
                            var9 = var0.getAmount();
                        } else {
                            var7 = var19.getTimeInMillis();
                            var3 = (float) var9;
                            var9 = var0.getAmount();
                        }
                    }

                    var13 = (long) (var3 + (float) var9 * var1);
                }

                var19.add(5, var0.getIncrement());
                var9 = var13;
                var11 = var7;
            }
        } else if (var0.getRecurringType() == 2) {
            String var20 = var0.getRepeatDate();
            var9 = 0L;
            boolean var4 = false;
            var11 = var7;

            while (true) {
                var19.set(7, 1);
                int var5 = 0;
                var7 = var9;

                boolean var6;
                label235:
                {
                    while (true) {
                        var6 = var4;
                        if (var5 >= 7) {
                            break label235;
                        }

                        int var23 = var5 + 1;
                        var19.set(7, var23);
                        var9 = var11;
                        var13 = var7;
                        if (var20.charAt(var5) == '1') {
                            label272:
                            {
                                if (var19.getTime().getTime() > var15 || DateHelper.isSameDay(var19.getTime().getTime(), var15)) {
                                    break;
                                }

                                if (var11 <= 0L) {
                                    if (var19.getTime().getTime() <= var0.getDateTime().getTime()) {
                                        var9 = var11;
                                        var13 = var7;
                                        if (!DateHelper.isSameDay(var19.getTime().getTime(), var0.getDateTime().getTime())) {
                                            break label272;
                                        }
                                    }

                                    if (var17 != 0L) {
                                        if (var19.getTime().getTime() >= var17 && !DateHelper.isSameDay(var19.getTime().getTime(), var17)) {
                                            break;
                                        }

                                        var11 = var19.getTimeInMillis();
                                        var3 = (float) var7;
                                        var9 = var0.getAmount();
                                        var7 = var11;
                                    } else {
                                        var11 = var19.getTimeInMillis();
                                        var3 = (float) var7;
                                        var9 = var0.getAmount();
                                        var7 = var11;
                                    }
                                } else {
                                    var9 = var11;
                                    var13 = var7;
                                    if (var19.getTime().getTime() <= var11) {
                                        break label272;
                                    }

                                    var9 = var11;
                                    var13 = var7;
                                    if (DateHelper.isSameDay(var19.getTime().getTime(), var11)) {
                                        break label272;
                                    }

                                    if (var17 != 0L) {
                                        if (var19.getTime().getTime() >= var17 && !DateHelper.isSameDay(var19.getTime().getTime(), var17)) {
                                            break;
                                        }

                                        var9 = var19.getTimeInMillis();
                                        var3 = (float) var7;
                                        var11 = var0.getAmount();
                                        var7 = var9;
                                        var9 = var11;
                                    } else {
                                        var9 = var19.getTimeInMillis();
                                        var3 = (float) var7;
                                        var11 = var0.getAmount();
                                        var7 = var9;
                                        var9 = var11;
                                    }
                                }

                                var13 = (long) (var3 + (float) var9 * var1);
                                var9 = var7;
                            }
                        }

                        var5 = var23;
                        var11 = var9;
                        var7 = var13;
                    }

                    var6 = true;
                }

                if (var6) {
                    break;
                }

                var19.add(3, var0.getIncrement());
                var4 = var6;
                var9 = var7;
            }
        } else {
            Calendar var21;
            if (var0.getRecurringType() == 3) {
                var9 = 0L;
                var11 = var7;

                while (true) {
                    while (true) {
                        while (true) {
                            int var22 = var0.getRepeatType();
                            if (var22 == 1) {
                                var19.set(5, var19.getActualMaximum(5));
                            } else {
                                var21 = Calendar.getInstance();
                                var21.setTime(var0.getDateTime());
                                var19.set(5, var21.get(5));
                            }

                            var7 = var9;
                            if (var19.getTime().getTime() > var15) {
                                return var7;
                            }

                            if (DateHelper.isSameDay(var19.getTime().getTime(), var15)) {
                                var7 = var9;
                                return var7;
                            }

                            label180:
                            {
                                if (var11 <= 0L) {
                                    if (var17 != 0L) {
                                        if (var19.getTime().getTime() >= var17) {
                                            var7 = var9;
                                            if (!DateHelper.isSameDay(var19.getTime().getTime(), var17)) {
                                                return var7;
                                            }
                                        }

                                        var7 = var19.getTimeInMillis();
                                        var3 = (float) var9;
                                        var9 = var0.getAmount();
                                    } else {
                                        var7 = var19.getTimeInMillis();
                                        var3 = (float) var9;
                                        var9 = var0.getAmount();
                                    }
                                } else {
                                    var13 = var9;
                                    var7 = var11;
                                    if (var19.getTime().getTime() <= var11) {
                                        break label180;
                                    }

                                    var13 = var9;
                                    var7 = var11;
                                    if (DateHelper.isSameDay(var19.getTime().getTime(), var11)) {
                                        break label180;
                                    }

                                    if (var17 != 0L) {
                                        if (var19.getTime().getTime() >= var17) {
                                            var7 = var9;
                                            if (!DateHelper.isSameDay(var19.getTime().getTime(), var17)) {
                                                return var7;
                                            }
                                        }

                                        var7 = var19.getTimeInMillis();
                                        var3 = (float) var9;
                                        var9 = var0.getAmount();
                                    } else {
                                        var7 = var19.getTimeInMillis();
                                        var3 = (float) var9;
                                        var9 = var0.getAmount();
                                    }
                                }

                                var13 = (long) (var3 + (float) var9 * var1);
                            }

                            if (var22 == 0) {
                                var22 = var19.get(5);
                                if (var22 != 31 && var22 != 30 && var22 != 29) {
                                    var19.add(2, var0.getIncrement());
                                    var9 = var13;
                                    var11 = var7;
                                } else {
                                    var19.set(5, 1);

                                    do {
                                        var19.add(2, var0.getIncrement());
                                    } while (var19.getActualMaximum(5) < var22);

                                    var19.set(5, var22);
                                    var9 = var13;
                                    var11 = var7;
                                }
                            } else {
                                var19.add(2, var0.getIncrement());
                                var9 = var13;
                                var11 = var7;
                            }
                        }
                    }
                }
            } else {
                var9 = 0L;
                var11 = var7;

                while (true) {
                    label157:
                    while (true) {
                        do {
                            do {
                                while (true) {
                                    var21 = Calendar.getInstance();
                                    var21.setTime(var0.getDateTime());
                                    var19.set(2, var21.get(2));
                                    var19.set(5, var21.get(5));
                                    var7 = var9;
                                    if (var19.getTime().getTime() > var15) {
                                        return var7;
                                    }

                                    if (DateHelper.isSameDay(var19.getTime().getTime(), var15)) {
                                        var7 = var9;
                                        return var7;
                                    }

                                    label136:
                                    {
                                        if (var11 <= 0L) {
                                            if (var17 != 0L) {
                                                if (var19.getTime().getTime() >= var17) {
                                                    var7 = var9;
                                                    if (!DateHelper.isSameDay(var19.getTime().getTime(), var17)) {
                                                        return var7;
                                                    }
                                                }

                                                var7 = var19.getTimeInMillis();
                                                var3 = (float) var9;
                                                var9 = var0.getAmount();
                                            } else {
                                                var7 = var19.getTimeInMillis();
                                                var3 = (float) var9;
                                                var9 = var0.getAmount();
                                            }
                                        } else {
                                            var13 = var9;
                                            var7 = var11;
                                            if (var19.getTime().getTime() <= var11) {
                                                break label136;
                                            }

                                            var13 = var9;
                                            var7 = var11;
                                            if (DateHelper.isSameDay(var19.getTime().getTime(), var11)) {
                                                break label136;
                                            }

                                            if (var17 != 0L) {
                                                if (var19.getTime().getTime() >= var17) {
                                                    var7 = var9;
                                                    if (!DateHelper.isSameDay(var19.getTime().getTime(), var17)) {
                                                        return var7;
                                                    }
                                                }

                                                var7 = var19.getTimeInMillis();
                                                var3 = (float) var9;
                                                var9 = var0.getAmount();
                                            } else {
                                                var7 = var19.getTimeInMillis();
                                                var3 = (float) var9;
                                                var9 = var0.getAmount();
                                            }
                                        }

                                        var13 = (long) (var3 + (float) var9 * var1);
                                    }

                                    if (var19.get(5) == 29) {
                                        var19.add(1, var0.getIncrement());
                                        var9 = var13;
                                        var11 = var7;
                                        break;
                                    }

                                    var19.add(1, var0.getIncrement());
                                    var9 = var13;
                                    var11 = var7;
                                }
                            } while (var19.get(2) != 1);

                            var9 = var13;
                            var11 = var7;
                        } while (var19.getActualMaximum(5) >= 29);

                        do {
                            var19.add(1, var0.getIncrement());
                            if (var19.get(2) != 1) {
                                var19.set(5, 29);
                                var9 = var13;
                                var11 = var7;
                                continue label157;
                            }
                        } while (var19.getActualMaximum(5) < 29);

                        var19.set(5, 29);
                        var9 = var13;
                        var11 = var7;
                    }
                }
            }
        }

        return var7;
    }

    public static  java.util.List<com.expance.merapaisa.Model.RecurringCalendarDate> getStatisticRecurring(Recurring var0, float var1, long var2, long var4) {
        ArrayList var17 = new ArrayList();
        long var9 = var0.getLastUpdateTime().getTime();
        Calendar var16 = Calendar.getInstance();
        Date var15;
        if (var0.getLastUpdateTime().getTime() <= 0L) {
            var15 = var0.getDateTime();
        } else {
            var15 = var0.getLastUpdateTime();
        }

        var16.setTimeInMillis(var15.getTime());
        long var13 = var0.getUntilTime().getTime();
        long var11;
        if (var0.getRecurringType() == 1) {
            for (var11 = var9; var16.getTime().getTime() <= var4 && !DateHelper.isSameDay(var16.getTime().getTime(), var4); var11 = var9) {
                if (var11 <= 0L) {
                    label284:
                    {
                        if (var16.getTime().getTime() <= var2) {
                            var9 = var11;
                            if (!DateHelper.isSameDay(var16.getTime().getTime(), var2)) {
                                break label284;
                            }
                        }

                        if (var13 != 0L) {
                            if (var16.getTime().getTime() >= var13 && !DateHelper.isSameDay(var16.getTime().getTime(), var13)) {
                                break;
                            }

                            var9 = var16.getTimeInMillis();
                            var17.add(new RecurringCalendarDate(var16.get(5), var16.get(2) + 1, var16.get(1), (long) ((float) var0.getAmount() * var1)));
                        } else {
                            var9 = var16.getTimeInMillis();
                            var17.add(new RecurringCalendarDate(var16.get(5), var16.get(2) + 1, var16.get(1), (long) ((float) var0.getAmount() * var1)));
                        }
                    }
                } else {
                    var9 = var11;
                    if (var16.getTime().getTime() > var11) {
                        var9 = var11;
                        if (!DateHelper.isSameDay(var16.getTime().getTime(), var11)) {
                            label315:
                            {
                                if (var16.getTime().getTime() <= var2) {
                                    var9 = var11;
                                    if (!DateHelper.isSameDay(var16.getTime().getTime(), var2)) {
                                        break label315;
                                    }
                                }

                                if (var13 != 0L) {
                                    if (var16.getTime().getTime() >= var13 && !DateHelper.isSameDay(var16.getTime().getTime(), var13)) {
                                        break;
                                    }

                                    var9 = var16.getTimeInMillis();
                                    var17.add(new RecurringCalendarDate(var16.get(5), var16.get(2) + 1, var16.get(1), (long) ((float) var0.getAmount() * var1)));
                                } else {
                                    var9 = var16.getTimeInMillis();
                                    var17.add(new RecurringCalendarDate(var16.get(5), var16.get(2) + 1, var16.get(1), (long) ((float) var0.getAmount() * var1)));
                                }
                            }
                        }
                    }
                }

                var16.add(5, var0.getIncrement());
            }
        } else if (var0.getRecurringType() == 2) {
            String var20 = var0.getRepeatDate();
            boolean var6 = false;

            while (true) {
                var16.set(7, 1);
                int var7 = 0;
                var11 = var9;

                boolean var8;
                label263:
                {
                    while (true) {
                        var8 = var6;
                        if (var7 >= 7) {
                            break label263;
                        }

                        int var19 = var7 + 1;
                        var16.set(7, var19);
                        var9 = var11;
                        if (var20.charAt(var7) == '1') {
                            if (var16.getTime().getTime() > var4 || DateHelper.isSameDay(var16.getTime().getTime(), var4)) {
                                break;
                            }

                            if (var11 <= 0L) {
                                label307:
                                {
                                    if (var16.getTime().getTime() <= var2) {
                                        var9 = var11;
                                        if (!DateHelper.isSameDay(var16.getTime().getTime(), var2)) {
                                            break label307;
                                        }
                                    }

                                    if (var16.getTime().getTime() <= var0.getDateTime().getTime()) {
                                        var9 = var11;
                                        if (!DateHelper.isSameDay(var16.getTime().getTime(), var0.getDateTime().getTime())) {
                                            break label307;
                                        }
                                    }

                                    if (var13 != 0L) {
                                        if (var16.getTime().getTime() >= var13 && !DateHelper.isSameDay(var16.getTime().getTime(), var13)) {
                                            break;
                                        }

                                        var9 = var16.getTimeInMillis();
                                        var17.add(new RecurringCalendarDate(var16.get(5), var16.get(2) + 1, var16.get(1), (long) ((float) var0.getAmount() * var1)));
                                    } else {
                                        var9 = var16.getTimeInMillis();
                                        var17.add(new RecurringCalendarDate(var16.get(5), var16.get(2) + 1, var16.get(1), (long) ((float) var0.getAmount() * var1)));
                                    }
                                }
                            } else {
                                var9 = var11;
                                if (var16.getTime().getTime() > var11) {
                                    var9 = var11;
                                    if (!DateHelper.isSameDay(var16.getTime().getTime(), var11)) {
                                        label246:
                                        {
                                            if (var16.getTime().getTime() <= var2) {
                                                var9 = var11;
                                                if (!DateHelper.isSameDay(var16.getTime().getTime(), var2)) {
                                                    break label246;
                                                }
                                            }

                                            if (var13 != 0L) {
                                                if (var16.getTime().getTime() >= var13 && !DateHelper.isSameDay(var16.getTime().getTime(), var13)) {
                                                    break;
                                                }

                                                var9 = var16.getTimeInMillis();
                                                var17.add(new RecurringCalendarDate(var16.get(5), var16.get(2) + 1, var16.get(1), (long) ((float) var0.getAmount() * var1)));
                                            } else {
                                                var9 = var16.getTimeInMillis();
                                                var17.add(new RecurringCalendarDate(var16.get(5), var16.get(2) + 1, var16.get(1), (long) ((float) var0.getAmount() * var1)));
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        var7 = var19;
                        var11 = var9;
                    }

                    var8 = true;
                }

                if (var8) {
                    break;
                }

                var16.add(3, var0.getIncrement());
                var9 = var11;
                var6 = var8;
            }
        } else {
            var11 = var9;
            Calendar var21;
            if (var0.getRecurringType() == 3) {
                var11 = var9;

                while (true) {
                    while (true) {
                        while (true) {
                            int var18 = var0.getRepeatType();
                            if (var18 == 1) {
                                var16.set(5, var16.getActualMaximum(5));
                            } else {
                                var21 = Calendar.getInstance();
                                var21.setTime(var0.getDateTime());
                                var16.set(5, var21.get(5));
                            }

                            if (var16.getTime().getTime() > var4 || DateHelper.isSameDay(var16.getTime().getTime(), var4)) {
                                return var17;
                            }

                            if (var11 <= 0L) {
                                label145:
                                {
                                    if (var16.getTime().getTime() <= var2) {
                                        var9 = var11;
                                        if (!DateHelper.isSameDay(var16.getTime().getTime(), var2)) {
                                            break label145;
                                        }
                                    }

                                    if (var13 != 0L) {
                                        if (var16.getTime().getTime() >= var13 && !DateHelper.isSameDay(var16.getTime().getTime(), var13)) {
                                            return var17;
                                        }

                                        var9 = var16.getTimeInMillis();
                                        var17.add(new RecurringCalendarDate(var16.get(5), var16.get(2) + 1, var16.get(1), (long) ((float) var0.getAmount() * var1)));
                                    } else {
                                        var9 = var16.getTimeInMillis();
                                        var17.add(new RecurringCalendarDate(var16.get(5), var16.get(2) + 1, var16.get(1), (long) ((float) var0.getAmount() * var1)));
                                    }
                                }
                            } else {
                                var9 = var11;
                                if (var16.getTime().getTime() > var11) {
                                    var9 = var11;
                                    if (!DateHelper.isSameDay(var16.getTime().getTime(), var11)) {
                                        label318:
                                        {
                                            if (var16.getTime().getTime() <= var2) {
                                                var9 = var11;
                                                if (!DateHelper.isSameDay(var16.getTime().getTime(), var2)) {
                                                    break label318;
                                                }
                                            }

                                            if (var13 != 0L) {
                                                if (var16.getTime().getTime() >= var13 && !DateHelper.isSameDay(var16.getTime().getTime(), var13)) {
                                                    return var17;
                                                }

                                                var9 = var16.getTimeInMillis();
                                                var17.add(new RecurringCalendarDate(var16.get(5), var16.get(2) + 1, var16.get(1), (long) ((float) var0.getAmount() * var1)));
                                            } else {
                                                var9 = var16.getTimeInMillis();
                                                var17.add(new RecurringCalendarDate(var16.get(5), var16.get(2) + 1, var16.get(1), (long) ((float) var0.getAmount() * var1)));
                                            }
                                        }
                                    }
                                }
                            }

                            if (var18 == 0) {
                                var18 = var16.get(5);
                                if (var18 != 31 && var18 != 30 && var18 != 29) {
                                    var16.add(2, var0.getIncrement());
                                    var11 = var9;
                                } else {
                                    var16.set(5, 1);

                                    do {
                                        var16.add(2, var0.getIncrement());
                                    } while (var16.getActualMaximum(5) < var18);

                                    var16.set(5, var18);
                                    var11 = var9;
                                }
                            } else {
                                var16.add(2, var0.getIncrement());
                                var11 = var9;
                            }
                        }
                    }
                }
            } else {
                while (true) {
                    label222:
                    while (true) {
                        do {
                            do {
                                while (true) {
                                    var21 = Calendar.getInstance();
                                    var21.setTime(var0.getDateTime());
                                    var16.set(2, var21.get(2));
                                    var16.set(5, var21.get(5));
                                    if (var16.getTime().getTime() > var4 || DateHelper.isSameDay(var16.getTime().getTime(), var4)) {
                                        return var17;
                                    }

                                    if (var11 <= 0L) {
                                        label192:
                                        {
                                            if (var16.getTime().getTime() <= var2) {
                                                var9 = var11;
                                                if (!DateHelper.isSameDay(var16.getTime().getTime(), var2)) {
                                                    break label192;
                                                }
                                            }

                                            if (var13 != 0L) {
                                                if (var16.getTime().getTime() >= var13 && !DateHelper.isSameDay(var16.getTime().getTime(), var13)) {
                                                    return var17;
                                                }

                                                var9 = var16.getTimeInMillis();
                                                var17.add(new RecurringCalendarDate(var16.get(5), var16.get(2) + 1, var16.get(1), (long) ((float) var0.getAmount() * var1)));
                                            } else {
                                                var9 = var16.getTimeInMillis();
                                                var17.add(new RecurringCalendarDate(var16.get(5), var16.get(2) + 1, var16.get(1), (long) ((float) var0.getAmount() * var1)));
                                            }
                                        }
                                    } else {
                                        var9 = var11;
                                        if (var16.getTime().getTime() > var11) {
                                            var9 = var11;
                                            if (!DateHelper.isSameDay(var16.getTime().getTime(), var11)) {
                                                label321:
                                                {
                                                    if (var16.getTime().getTime() <= var2) {
                                                        var9 = var11;
                                                        if (!DateHelper.isSameDay(var16.getTime().getTime(), var2)) {
                                                            break label321;
                                                        }
                                                    }

                                                    if (var13 != 0L) {
                                                        if (var16.getTime().getTime() >= var13 && !DateHelper.isSameDay(var16.getTime().getTime(), var13)) {
                                                            return var17;
                                                        }

                                                        var9 = var16.getTimeInMillis();
                                                        var17.add(new RecurringCalendarDate(var16.get(5), var16.get(2) + 1, var16.get(1), (long) ((float) var0.getAmount() * var1)));
                                                    } else {
                                                        var9 = var16.getTimeInMillis();
                                                        var17.add(new RecurringCalendarDate(var16.get(5), var16.get(2) + 1, var16.get(1), (long) ((float) var0.getAmount() * var1)));
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    if (var16.get(5) == 29) {
                                        var16.add(1, var0.getIncrement());
                                        var11 = var9;
                                        break;
                                    }

                                    var16.add(1, var0.getIncrement());
                                    var11 = var9;
                                }
                            } while (var16.get(2) != 1);

                            var11 = var9;
                        } while (var16.getActualMaximum(5) >= 29);

                        do {
                            var16.add(1, var0.getIncrement());
                            if (var16.get(2) != 1) {
                                var16.set(5, 29);
                                var11 = var9;
                                continue label222;
                            }
                        } while (var16.getActualMaximum(5) < 29);

                        var16.set(5, 29);
                        var11 = var9;
                    }
                }
            }
        }

        return var17;
    }

    public static long getStatisticRecurringCarryOver(Recurring recurring, float rate, long startDate) {
        long j;
        float f;
        long amount;
        float f2;
        long amount2;
        String str;
        float f3 = 0;
        long amount3 = 0;
        float f4 = 0;
        long amount4 = 0;
        long time = recurring.getLastUpdateTime().getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((recurring.getLastUpdateTime().getTime() <= 0 ? recurring.getDateTime() : recurring.getLastUpdateTime()).getTime());
        long time2 = recurring.getUntilTime().getTime();
        int i = 1;
        if (recurring.getRecurringType() == 1) {
            long j2 = 0;
            while (calendar.getTime().getTime() <= startDate && !DateHelper.isSameDay(calendar.getTime().getTime(), startDate)) {
                if (time > 0) {
                    if (calendar.getTime().getTime() > time && !DateHelper.isSameDay(calendar.getTime().getTime(), time)) {
                        if (time2 != 0) {
                            if (calendar.getTime().getTime() >= time2 && !DateHelper.isSameDay(calendar.getTime().getTime(), time2)) {
                                return j2;
                            }
                            time = calendar.getTimeInMillis();
                            f4 = (float) j2;
                            amount4 = recurring.getAmount();
                        } else {
                            time = calendar.getTimeInMillis();
                            f4 = (float) j2;
                            amount4 = recurring.getAmount();
                        }
                    }
                    calendar.add(5, recurring.getIncrement());
                } else if (time2 != 0) {
                    if (calendar.getTime().getTime() >= time2 && !DateHelper.isSameDay(calendar.getTime().getTime(), time2)) {
                        return j2;
                    }
                    time = calendar.getTimeInMillis();
                    f4 = (float) j2;
                    amount4 = recurring.getAmount();
                } else {
                    time = calendar.getTimeInMillis();
                    f4 = (float) j2;
                    amount4 = recurring.getAmount();
                }
                j2 = (long) (f4 + (((float) amount4) * rate));
                calendar.add(5, recurring.getIncrement());
            }
            return j2;
        } else if (recurring.getRecurringType() == 2) {
            String repeatDate = recurring.getRepeatDate();
            long j3 = 0;
            boolean z = false;
            while (true) {
                calendar.set(7, i);
                long j4 = j3;
                int i2 = 0;
                for (int i3 = 7; i2 < i3; i3 = 7) {
                    int i4 = i2 + 1;
                    calendar.set(i3, i4);
                    if (repeatDate.charAt(i2) == '1') {
                        if (calendar.getTime().getTime() > startDate || DateHelper.isSameDay(calendar.getTime().getTime(), startDate)) {
                            str = repeatDate;
                        } else if (time <= 0) {
                            if (calendar.getTime().getTime() <= recurring.getDateTime().getTime()) {
                                str = repeatDate;
                                if (!DateHelper.isSameDay(calendar.getTime().getTime(), recurring.getDateTime().getTime())) {
                                    continue;
                                }
                            } else {
                                str = repeatDate;
                            }
                            if (time2 != 0) {
                                if (calendar.getTime().getTime() < time2 || DateHelper.isSameDay(calendar.getTime().getTime(), time2)) {
                                    time = calendar.getTimeInMillis();
                                    f3 = (float) j4;
                                    amount3 = recurring.getAmount();
                                }
                            } else {
                                time = calendar.getTimeInMillis();
                                f3 = (float) j4;
                                amount3 = recurring.getAmount();
                            }
                            j4 = (long) (f3 + (((float) amount3) * rate));
                        } else {
                            str = repeatDate;
                            if (calendar.getTime().getTime() > time && !DateHelper.isSameDay(calendar.getTime().getTime(), time)) {
                                if (time2 != 0) {
                                    if (calendar.getTime().getTime() < time2 || DateHelper.isSameDay(calendar.getTime().getTime(), time2)) {
                                        time = calendar.getTimeInMillis();
                                        f3 = (float) j4;
                                        amount3 = recurring.getAmount();
                                    }
                                } else {
                                    time = calendar.getTimeInMillis();
                                    f3 = (float) j4;
                                    amount3 = recurring.getAmount();
                                }
                                j4 = (long) (f3 + (((float) amount3) * rate));
                            }
                        }
                        z = true;
                        break;
                    }
                    str = repeatDate;
                    i2 = i4;
                    repeatDate = str;
                }
                str = repeatDate;
                if (z) {
                    return j4;
                }
                calendar.add(3, recurring.getIncrement());
                j3 = j4;
                repeatDate = str;
                i = 1;
            }
        } else {
            if (recurring.getRecurringType() != 3) {
                j = 0;
                while (true) {
                    Calendar calendar2 = Calendar.getInstance();
                    calendar2.setTime(recurring.getDateTime());
                    calendar.set(2, calendar2.get(2));
                    calendar.set(5, calendar2.get(5));
                    if (calendar.getTime().getTime() > startDate || DateHelper.isSameDay(calendar.getTime().getTime(), startDate)) {
                        break;
                    }
                    if (time <= 0) {
                        if (time2 != 0) {
                            if (calendar.getTime().getTime() >= time2 && !DateHelper.isSameDay(calendar.getTime().getTime(), time2)) {
                                break;
                            }
                            time = calendar.getTimeInMillis();
                            f = (float) j;
                            amount = recurring.getAmount();
                        } else {
                            time = calendar.getTimeInMillis();
                            f = (float) j;
                            amount = recurring.getAmount();
                        }
                        j = (long) (f + (((float) amount) * rate));
                    } else if (calendar.getTime().getTime() > time && !DateHelper.isSameDay(calendar.getTime().getTime(), time)) {
                        if (time2 != 0) {
                            if (calendar.getTime().getTime() >= time2 && !DateHelper.isSameDay(calendar.getTime().getTime(), time2)) {
                                break;
                            }
                            time = calendar.getTimeInMillis();
                            f = (float) j;
                            amount = recurring.getAmount();
                        } else {
                            time = calendar.getTimeInMillis();
                            f = (float) j;
                            amount = recurring.getAmount();
                        }
                        j = (long) (f + (((float) amount) * rate));
                    }
                    if (calendar.get(5) == 29) {
                        calendar.add(1, recurring.getIncrement());
                        if (calendar.get(2) == 1 && calendar.getActualMaximum(5) < 29) {
                            while (true) {
                                calendar.add(1, recurring.getIncrement());
                                if (calendar.get(2) == 1) {
                                    if (calendar.getActualMaximum(5) >= 29) {
                                        calendar.set(5, 29);
                                        break;
                                    }
                                } else {
                                    calendar.set(5, 29);
                                    break;
                                }
                            }
                        }
                    } else {
                        calendar.add(1, recurring.getIncrement());
                    }
                }
            } else {
                j = 0;
                while (true) {
                    int repeatType = recurring.getRepeatType();
                    if (repeatType == 1) {
                        calendar.set(5, calendar.getActualMaximum(5));
                    } else {
                        Calendar calendar3 = Calendar.getInstance();
                        calendar3.setTime(recurring.getDateTime());
                        calendar.set(5, calendar3.get(5));
                    }
                    if (calendar.getTime().getTime() > startDate || DateHelper.isSameDay(calendar.getTime().getTime(), startDate)) {
                        break;
                    }
                    if (time <= 0) {
                        if (time2 != 0) {
                            if (calendar.getTime().getTime() >= time2 && !DateHelper.isSameDay(calendar.getTime().getTime(), time2)) {
                                break;
                            }
                            time = calendar.getTimeInMillis();
                            f2 = (float) j;
                            amount2 = recurring.getAmount();
                        } else {
                            time = calendar.getTimeInMillis();
                            f2 = (float) j;
                            amount2 = recurring.getAmount();
                        }
                        j = (long) (f2 + (((float) amount2) * rate));
                    } else if (calendar.getTime().getTime() > time && !DateHelper.isSameDay(calendar.getTime().getTime(), time)) {
                        if (time2 != 0) {
                            if (calendar.getTime().getTime() >= time2 && !DateHelper.isSameDay(calendar.getTime().getTime(), time2)) {
                                break;
                            }
                            time = calendar.getTimeInMillis();
                            f2 = (float) j;
                            amount2 = recurring.getAmount();
                        } else {
                            time = calendar.getTimeInMillis();
                            f2 = (float) j;
                            amount2 = recurring.getAmount();
                        }
                        j = (long) (f2 + (((float) amount2) * rate));
                    }
                    if (repeatType == 0) {
                        int i5 = calendar.get(5);
                        if (i5 == 31 || i5 == 30 || i5 == 29) {
                            calendar.set(5, 1);
                            do {
                                calendar.add(2, recurring.getIncrement());
                            } while (calendar.getActualMaximum(5) < i5);
                            calendar.set(5, i5);
                        } else {
                            calendar.add(2, recurring.getIncrement());
                        }
                    } else {
                        calendar.add(2, recurring.getIncrement());
                    }
                }
            }
            return j;
        }
    }
}
