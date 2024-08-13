package com.expance.merapaisa.Model;

import java.util.Calendar;
import java.util.Date;

/* loaded from: classes3.dex */
public class RecurringCalendarDate {
    private long amount;
    private int day;
    private int month;
    private int year;

    public RecurringCalendarDate(int day, int month, int year, long amount) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.amount = amount;
    }

    public Date getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(this.year, this.month - 1, this.day, 0, 0, 0);
        return calendar.getTime();
    }

    public int getDay() {
        return this.day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return this.month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public long getAmount() {
        return this.amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
