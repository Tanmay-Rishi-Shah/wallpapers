package com.expance.merapaisa.Model;

/* loaded from: classes3.dex */
public class CalendarRecord {
    private int date;
    private long expense;
    private long income;

    public CalendarRecord(int date, long income, long expense) {
        this.date = date;
        this.income = income;
        this.expense = expense;
    }

    public int getDate() {
        return this.date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public long getIncome() {
        return this.income;
    }

    public void setIncome(long income) {
        this.income = income;
    }

    public long getExpense() {
        return this.expense;
    }

    public void setExpense(long expense) {
        this.expense = expense;
    }
}
