package com.expance.merapaisa.Model;

/* loaded from: classes3.dex */
public class Account {
    private long balance;
    private String currency;
    private String currencySymbol;
    private int id;
    private String name;
    private int type;

    public Account(int id, String name, String currencySymbol, String currency, long balance, int type) {
        this.id = id;
        this.name = name;
        this.currencySymbol = currencySymbol;
        this.currency = currency;
        this.balance = balance;
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

    public String getCurrencySymbol() {
        return this.currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public long getBalance() {
        return this.balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
