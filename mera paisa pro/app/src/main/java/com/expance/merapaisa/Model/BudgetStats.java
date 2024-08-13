package com.expance.merapaisa.Model;

/* loaded from: classes3.dex */
public class BudgetStats {
    private long amount;
    private int day;
    private int month;

    public BudgetStats(int day, int month, long amount) {
        this.day = day;
        this.month = month;
        this.amount = amount;
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

    public long getAmount() {
        return this.amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
