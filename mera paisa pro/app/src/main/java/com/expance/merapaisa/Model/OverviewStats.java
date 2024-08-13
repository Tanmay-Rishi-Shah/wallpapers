package com.expance.merapaisa.Model;

/* loaded from: classes3.dex */
public class OverviewStats {
    long expense;
    long income;
    int month;
    int year;

    public OverviewStats(long income, long expense, int month, int year) {
        this.income = income;
        this.expense = expense;
        this.month = month;
        this.year = year;
    }

    public long getNet() {
        return this.income + this.expense;
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
}
