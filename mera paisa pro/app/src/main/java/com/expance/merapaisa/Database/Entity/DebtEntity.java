package com.expance.merapaisa.Database.Entity;

import java.util.Date;

/* loaded from: classes3.dex */
public class DebtEntity {
    private int accountId;
    private long amount;
    private String color;
    private Date dueDate;
    private int id;
    private Date lendDate;
    private String lender;
    private String name;
    private long pay;
    private int status;
    private int type;

    public DebtEntity(String name, String lender, String color, long pay, long amount, Date dueDate, Date lendDate, int accountId, int status, int type) {
        this.name = name;
        this.lender = lender;
        this.color = color;
        this.pay = pay;
        this.amount = amount;
        this.dueDate = dueDate;
        this.lendDate = lendDate;
        this.accountId = accountId;
        this.status = status;
        this.type = type;
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

    public String getLender() {
        return this.lender;
    }

    public void setLender(String lender) {
        this.lender = lender;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public long getPay() {
        return this.pay;
    }

    public void setPay(long pay) {
        this.pay = pay;
    }

    public long getAmount() {
        return this.amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Date getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getLendDate() {
        return this.lendDate;
    }

    public void setLendDate(Date lendDate) {
        this.lendDate = lendDate;
    }

    public int getAccountId() {
        return this.accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
