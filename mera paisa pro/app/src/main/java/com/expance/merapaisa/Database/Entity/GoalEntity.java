package com.expance.merapaisa.Database.Entity;

import java.util.Date;

/* loaded from: classes3.dex */
public class GoalEntity {
    private int accountId;
    private Date achieveDate;
    private long amount;
    private String color;
    private String currency;
    private Date expectDate;
    private int id;
    private String name;
    private long saved;
    private int status;

    public GoalEntity(String name, String color, long saved, long amount, int status, int accountId, Date expectDate, Date achieveDate, String currency) {
        this.name = name;
        this.color = color;
        this.saved = saved;
        this.amount = amount;
        this.status = status;
        this.accountId = accountId;
        this.expectDate = expectDate;
        this.achieveDate = achieveDate;
        this.currency = currency;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public long getSaved() {
        return this.saved;
    }

    public void setSaved(long saved) {
        this.saved = saved;
    }

    public long getAmount() {
        return this.amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAccountId() {
        return this.accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public Date getExpectDate() {
        return this.expectDate;
    }

    public void setExpectDate(Date expectDate) {
        this.expectDate = expectDate;
    }

    public Date getAchieveDate() {
        return this.achieveDate;
    }

    public void setAchieveDate(Date achieveDate) {
        this.achieveDate = achieveDate;
    }
}
