package com.expance.merapaisa.Database.Dao;

import com.expance.merapaisa.Model.CalendarRecord;
import com.expance.merapaisa.Model.CalendarSummary;
import com.expance.merapaisa.Model.DailyTrans;
import com.expance.merapaisa.Model.Trans;
import java.util.List;

/* loaded from: classes3.dex */
public interface CalenderDaoObject {
    long getAccountBalance(int id, long date);

    DailyTrans[] getDailyTrans(int accountId, long startDate, long endDate);

    List<CalendarRecord> getRecord(long startDate, long endDate, int accountId);

    CalendarSummary getSummary(long startDate, long endDate, int accountId);

    Trans[] getTransFromDate(int accountId, long startDate, long endDate);
}
