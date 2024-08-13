package com.expance.merapaisa.Model;

/* loaded from: classes3.dex */
public class ExportReport {
    private String dateTime;
    private long expense;
    private long income;
    private long total;

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

    public String getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public long getTotal() {
        return this.income + this.expense;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
