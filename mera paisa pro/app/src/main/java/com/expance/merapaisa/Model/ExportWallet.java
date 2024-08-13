package com.expance.merapaisa.Model;

/* loaded from: classes3.dex */
public class ExportWallet {
    private String currency;
    private long expense;
    private long income;
    private String name;
    private long total;

    public ExportWallet(long income, long expense, String currency, String name) {
        this.income = income;
        this.expense = expense;
        this.currency = currency;
        this.name = name;
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

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTotal() {
        return this.income + this.expense;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
