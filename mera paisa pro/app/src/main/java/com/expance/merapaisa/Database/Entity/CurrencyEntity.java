package com.expance.merapaisa.Database.Entity;

/* loaded from: classes3.dex */
public class CurrencyEntity {
    private int accountId;
    private String code;
    private int id;
    private double rate;

    public CurrencyEntity(String code, double rate, int accountId) {
        this.code = code;
        this.rate = rate;
        this.accountId = accountId;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getRate() {
        return this.rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getAccountId() {
        return this.accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}
