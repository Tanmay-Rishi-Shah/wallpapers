package com.expance.merapaisa.Model;

/* loaded from: classes3.dex */
public class WeeklyStats {
    long amount;
    int day;
    int trans;

    public WeeklyStats(int day, long amount, int trans) {
        this.day = day;
        this.amount = amount;
        this.trans = trans;
    }

    public int getTrans() {
        return this.trans;
    }

    public void setTrans(int trans) {
        this.trans = trans;
    }

    public int getDay() {
        return this.day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public long getAmount() {
        return this.amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
