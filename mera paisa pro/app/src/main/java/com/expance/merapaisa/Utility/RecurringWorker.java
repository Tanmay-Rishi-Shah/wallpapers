package com.expance.merapaisa.Utility;

import android.content.Context;

import androidx.work.ListenableWorker;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.Entity.TransEntity;
import com.expance.merapaisa.Model.Recurring;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/* loaded from: classes3.dex */
public class RecurringWorker extends Worker {
    public RecurringWorker(Context context, WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @Override // androidx.work.Worker
    public ListenableWorker.Result doWork() {
        checkRecurring();
        WorkManager workManager = WorkManager.getInstance(getApplicationContext());
        workManager.cancelAllWorkByTag(Constant.DAILY_WORKER);
        workManager.enqueue(new OneTimeWorkRequest.Builder(RecurringWorker.class).addTag(Constant.DAILY_WORKER).setInitialDelay(getMilliseconds(), TimeUnit.MILLISECONDS).build());
        return ListenableWorker.Result.success();
    }

    private void checkRecurring() {
        for (Recurring recurring : AppDatabaseObject.getInstance(getApplicationContext()).recurringDaoObject().getAllRecurringList()) {
            addRecurringTransaction(recurring);
        }
    }

    private void deleteRecurring(int id) {
        AppDatabaseObject.getInstance(getApplicationContext()).recurringDaoObject().deleteRecurring(id);
    }

    private void updateLastUpdateMilli(int id, long time) {
        AppDatabaseObject.getInstance(getApplicationContext()).recurringDaoObject().updateRecurringUpdateTime(time, id);
    }

    private void addTransaction(final String note, final String memo, final long amount, final Date date, final int type, final int accountId, final int categoryId, final int walletId) {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.Utility.RecurringWorker.1
            @Override // java.lang.Runnable
            public void run() {
                AppDatabaseObject.getInstance(RecurringWorker.this.getApplicationContext()).transDaoObject().insertTrans(new TransEntity(note, memo, amount, date, type, accountId, categoryId, 0, walletId, -1, 0L, 0, 0, 0));
            }
        });
    }

    private void addRecurringTransaction(Recurring var1) {
        Calendar var24 = Calendar.getInstance();
        Date var23;
        if (var1.getLastUpdateTime().getTime() <= 0L) {
            var23 = var1.getDateTime();
        } else {
            var23 = var1.getLastUpdateTime();
        }

        var24.setTime(var23);
        long var9 = var1.getLastUpdateTime().getTime();
        long var21 = DateHelper.getTodayMillis();
        long var11 = var1.getUntilTime().getTime();
        var24.set(11, 0);
        var24.set(12, 0);
        var24.set(13, 0);
        var24.set(14, 0);
        long var19 = var1.getAmount();
        int var5 = var1.getType();
        int var8 = var1.getWalletId();
        int var6 = var1.getAccountId();
        int var7 = var1.getCategoryId();
        String var25 = var1.getNote(this.getApplicationContext());
        String var28 = var1.getMemo();
        long var13;
        long var15;
        if (var1.getRecurringType() == 1) {
            var15 = var9;

            while (true) {
                label314:
                {
                    if (var15 <= 0L) {
                        if (var24.getTime().getTime() >= var21) {
                            var13 = var15;
                            var9 = var11;
                            if (!DateHelper.isSameDay(var24.getTime().getTime(), var21)) {
                                break;
                            }
                        }

                        if (var11 != 0L) {
                            if (var24.getTime().getTime() >= var11) {
                                var13 = var15;
                                var9 = var11;
                                if (!DateHelper.isSameDay(var24.getTime().getTime(), var11)) {
                                    break;
                                }
                            }

                            var9 = var24.getTime().getTime();
                            this.addTransaction(var25, var28, var19, var24.getTime(), var5, var6, var7, var8);
                        } else {
                            var9 = var24.getTime().getTime();
                            this.addTransaction(var25, var28, var19, var24.getTime(), var5, var6, var7, var8);
                        }
                    } else {
                        var9 = var15;
                        if (var24.getTime().getTime() > var15) {
                            var9 = var15;
                            if (!DateHelper.isSameDay(var24.getTime().getTime(), var15)) {
                                if (var24.getTime().getTime() >= var21 && !DateHelper.isSameDay(var24.getTime().getTime(), var21)) {
                                    var9 = var11;
                                    var13 = var15;
                                    break;
                                }

                                if (var11 != 0L) {
                                    if (var24.getTime().getTime() >= var11) {
                                        var13 = var15;
                                        var9 = var11;
                                        if (!DateHelper.isSameDay(var24.getTime().getTime(), var11)) {
                                            break;
                                        }
                                    }

                                    var9 = var24.getTime().getTime();
                                    this.addTransaction(var25, var28, var19, var24.getTime(), var5, var6, var7, var8);
                                } else {
                                    var9 = var24.getTime().getTime();
                                    this.addTransaction(var25, var28, var19, var24.getTime(), var5, var6, var7, var8);
                                }

                                var15 = var9;
                                break label314;
                            }
                        }
                    }

                    var15 = var9;
                }

                var24.add(5, var1.getIncrement());
            }
        } else {
            label293:
            {
                label337:
                {
                    if (var1.getRecurringType() == 2) {
                        boolean var27 = false;
                        var13 = var9;

                        while (true) {
                            var24.set(7, 1);
                            int var3 = 0;
                            var9 = var11;

                            int var4;
                            for (var11 = var13; var3 < 7; var3 = var4) {
                                label218:
                                {
                                    label217:
                                    {
                                        var4 = var3 + 1;
                                        var24.set(7, var4);
                                        if (var1.getRepeatDate().charAt(var3) == '1') {
                                            if (var11 != 0L) {
                                                var13 = var11;
                                                if (var24.getTime().getTime() > var11) {
                                                    var13 = var11;
                                                    if (!DateHelper.isSameDay(var24.getTime().getTime(), var11)) {
                                                        if (var24.getTime().getTime() < var21 || DateHelper.isSameDay(var24.getTime().getTime(), var21)) {
                                                            if (var9 != 0L) {
                                                                if (var24.getTime().getTime() < var9 || DateHelper.isSameDay(var24.getTime().getTime(), var9)) {
                                                                    var11 = var24.getTime().getTime();
                                                                    this.addTransaction(var25, var28, var19, var24.getTime(), var5, var6, var7, var8);
                                                                    continue;
                                                                }
                                                            } else {
                                                                var11 = var24.getTime().getTime();
                                                                this.addTransaction(var25, var28, var19, var24.getTime(), var5, var6, var7, var8);
                                                                continue;
                                                            }
                                                        }

                                                        var27 = true;
                                                        break;
                                                    }
                                                }
                                                break label218;
                                            }

                                            if (var24.getTime().getTime() >= var21 && !DateHelper.isSameDay(var24.getTime().getTime(), var21)) {
                                                break label217;
                                            }

                                            if (var24.getTime().getTime() > var1.getDateTime().getTime() || DateHelper.isSameDay(var24.getTime().getTime(), var1.getDateTime().getTime())) {
                                                if (var9 != 0L) {
                                                    if (var24.getTime().getTime() >= var9 && !DateHelper.isSameDay(var24.getTime().getTime(), var9)) {
                                                        break label217;
                                                    }

                                                    var11 = var24.getTime().getTime();
                                                    this.addTransaction(var25, var28, var19, var24.getTime(), var5, var6, var7, var8);
                                                } else {
                                                    var11 = var24.getTime().getTime();
                                                    this.addTransaction(var25, var28, var19, var24.getTime(), var5, var6, var7, var8);
                                                }

                                                var13 = var11;
                                                break label218;
                                            }
                                        }
                                        continue;
                                    }

                                    var27 = true;
                                    break;
                                }

                                var11 = var13;
                            }

                            if (var27) {
                                var15 = var9;
                                var13 = var11;
                                break;
                            }

                            var24.add(3, var1.getIncrement());
                            var13 = var11;
                            var11 = var9;
                        }
                    } else {
                        if (var1.getRecurringType() != 3) {
                            var13 = var9;

                            while (true) {
                                if (var13 == 0L) {
                                    if (var24.getTime().getTime() >= var21 && !DateHelper.isSameDay(var24.getTime().getTime(), var21)) {
                                        var9 = var11;
                                        break label293;
                                    }

                                    if (var11 != 0L) {
                                        if (var24.getTime().getTime() >= var11) {
                                            var9 = var13;
                                            var13 = var11;
                                            if (!DateHelper.isSameDay(var24.getTime().getTime(), var1.getUntilTime().getTime())) {
                                                break label337;
                                            }
                                        }

                                        var9 = var24.getTime().getTime();
                                        this.addTransaction(var25, var28, var19, var24.getTime(), var5, var6, var7, var8);
                                    } else {
                                        var9 = var24.getTime().getTime();
                                        this.addTransaction(var25, var28, var19, var24.getTime(), var5, var6, var7, var8);
                                    }
                                } else {
                                    var9 = var13;
                                    if (var24.getTime().getTime() > var13) {
                                        var9 = var13;
                                        if (!DateHelper.isSameDay(var24.getTime().getTime(), var13)) {
                                            if (var24.getTime().getTime() >= var21 && !DateHelper.isSameDay(var24.getTime().getTime(), var21)) {
                                                var9 = var11;
                                                break label293;
                                            }

                                            if (var11 != 0L) {
                                                if (var24.getTime().getTime() >= var11) {
                                                    var9 = var11;
                                                    if (!DateHelper.isSameDay(var24.getTime().getTime(), var11)) {
                                                        break label293;
                                                    }
                                                }

                                                var9 = var24.getTime().getTime();
                                                this.addTransaction(var25, var28, var19, var24.getTime(), var5, var6, var7, var8);
                                            } else {
                                                var9 = var24.getTime().getTime();
                                                this.addTransaction(var25, var28, var19, var24.getTime(), var5, var6, var7, var8);
                                            }
                                        }
                                    }
                                }

                                if (var24.get(5) == 29) {
                                    var24.add(1, var1.getIncrement());
                                    if (var24.get(2) == 1 && var24.getActualMaximum(5) < 29) {
                                        while (true) {
                                            var24.add(1, var1.getIncrement());
                                            if (var24.get(2) != 1) {
                                                var24.set(5, 29);
                                                break;
                                            }

                                            if (var24.getActualMaximum(5) >= 29) {
                                                var24.set(5, 29);
                                                break;
                                            }
                                        }
                                    }
                                } else {
                                    var24.add(1, var1.getIncrement());
                                }

                                var13 = var9;
                            }
                        }

                        label290:
                        while (true) {
                            while (true) {
                                while (true) {
                                    if (var1.getRepeatType() == 1) {
                                        var24.set(5, var24.getActualMaximum(5));
                                    } else {
                                        Calendar var26 = Calendar.getInstance();
                                        var26.setTime(var1.getDateTime());
                                        var24.set(5, var26.get(5));
                                    }

                                    label270:
                                    {
                                        if (var9 == 0L) {
                                            if (var24.getTime().getTime() >= var21) {
                                                var13 = var9;
                                                var15 = var11;
                                                if (!DateHelper.isSameDay(var24.getTime().getTime(), var21)) {
                                                    break label290;
                                                }
                                            }

                                            if (var11 != 0L) {
                                                if (var24.getTime().getTime() >= var11) {
                                                    var13 = var11;
                                                    if (!DateHelper.isSameDay(var24.getTime().getTime(), var11)) {
                                                        break label337;
                                                    }
                                                }

                                                var13 = var24.getTime().getTime();
                                                this.addTransaction(var25, var28, var19, var24.getTime(), var5, var6, var7, var8);
                                            } else {
                                                var13 = var24.getTime().getTime();
                                                this.addTransaction(var25, var28, var19, var24.getTime(), var5, var6, var7, var8);
                                            }
                                        } else {
                                            var13 = var9;
                                            if (var24.getTime().getTime() > var9) {
                                                var13 = var9;
                                                if (!DateHelper.isSameDay(var24.getTime().getTime(), var9)) {
                                                    if (var24.getTime().getTime() >= var21 && !DateHelper.isSameDay(var24.getTime().getTime(), var21)) {
                                                        var13 = var9;
                                                        var9 = var11;
                                                        break label293;
                                                    }

                                                    if (var11 != 0L) {
                                                        if (var24.getTime().getTime() >= var11) {
                                                            var13 = var9;
                                                            var9 = var11;
                                                            if (!DateHelper.isSameDay(var24.getTime().getTime(), var11)) {
                                                                break label293;
                                                            }
                                                        }

                                                        var9 = var24.getTime().getTime();
                                                        this.addTransaction(var25, var28, var19, var24.getTime(), var5, var6, var7, var8);
                                                    } else {
                                                        var9 = var24.getTime().getTime();
                                                        this.addTransaction(var25, var28, var19, var24.getTime(), var5, var6, var7, var8);
                                                    }
                                                    break label270;
                                                }
                                            }
                                        }

                                        var9 = var13;
                                    }

                                    if (var1.getRepeatType() == 0) {
                                        int var2 = var24.get(5);
                                        if (var2 != 31 && var2 != 30 && var2 != 29) {
                                            var24.add(2, var1.getIncrement());
                                        } else {
                                            var24.set(5, 1);

                                            do {
                                                var24.add(2, var1.getIncrement());
                                            } while (var24.getActualMaximum(5) < var2);

                                            var24.set(5, var2);
                                        }
                                    } else {
                                        var24.add(2, var1.getIncrement());
                                    }
                                }
                            }
                        }
                    }

                    var9 = var15;
                    break label293;
                }

                var11 = var13;
                var13 = var9;
                var9 = var11;
            }
        }

        if (var9 != 0L) {
            if (var24.getTime().getTime() > var9 && !DateHelper.isSameDay(var24.getTime().getTime(), var9)) {
                this.deleteRecurring(var1.getId());
            } else {
                this.updateLastUpdateMilli(var1.getId(), var13);
            }
        } else {
            this.updateLastUpdateMilli(var1.getId(), var13);
        }
    }

    private static long getMilliseconds() {
        Calendar calendar = Calendar.getInstance();
        long time = calendar.getTime().getTime();
        calendar.set(11, 0);
        calendar.set(13, 5);
        calendar.set(12, 0);
        calendar.add(5, 1);
        return calendar.getTime().getTime() - time;
    }
}
