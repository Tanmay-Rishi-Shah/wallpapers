package com.expance.merapaisa.Model;

/* loaded from: classes3.dex */
public class CalendarDate {
    private long amount;
    private int date;

    public CalendarDate(int date, long amount) {
        this.date = date;
        this.amount = amount;
    }

    public int getDate() {
        return this.date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public long getAmount() {
        return this.amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
