package com.expance.merapaisa.Model;

/* loaded from: classes3.dex */
public class CalendarSummary {
    private long expense;
    private long income;

    public CalendarSummary(long income, long expense) {
        this.income = income;
        this.expense = expense;
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

    public long getNetIncome() {
        return this.income + this.expense;
    }
}
