package com.expance.merapaisa.Database.Entity;

import java.util.Date;

/* loaded from: classes3.dex */
public class DebtTransEntity {
    private long amount;
    private String currencyCode;
    private Date dateTime;
    private int debtId;
    private int id;
    private String note;
    private double rate;
    private int type;
    private String wallet;

    public DebtTransEntity(long amount, Date dateTime, String note, int debtId, int type) {
        this.amount = amount;
        this.dateTime = dateTime;
        this.note = note;
        this.debtId = debtId;
        this.type = type;
    }

    public String getWallet() {
        return this.wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public double getRate() {
        return this.rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getAmount() {
        return this.amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Date getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public int getDebtId() {
        return this.debtId;
    }

    public void setDebtId(int debtId) {
        this.debtId = debtId;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
