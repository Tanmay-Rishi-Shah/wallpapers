package com.expance.merapaisa.Database.Entity;

import java.util.Date;

/* loaded from: classes3.dex */
public class BudgetEntity {
    private int accountId;
    private long amount;
    private int categoryId;
    private String color;
    private Date endDate;
    private int id;
    private String name;
    private int period;
    private int repeat;
    private long spent;
    private Date startDate;
    private int status;

    public BudgetEntity(String name, String color, long amount, long spent, int status, int period, int repeat, int accountId, int categoryId, Date startDate, Date endDate) {
        this.name = name;
        this.color = color;
        this.amount = amount;
        this.spent = spent;
        this.status = status;
        this.period = period;
        this.repeat = repeat;
        this.accountId = accountId;
        this.categoryId = categoryId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAmount() {
        return this.amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getSpent() {
        return this.spent;
    }

    public void setSpent(long spent) {
        this.spent = spent;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPeriod() {
        return this.period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getRepeat() {
        return this.repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public int getAccountId() {
        return this.accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
