package com.expance.merapaisa.Database.Entity;

/* loaded from: classes3.dex */
public class AccountEntity {
    private long balance;
    private String currency;
    private String currencySymbol;
    private int id;
    private String name;
    private int ordering;
    private int type;

    public AccountEntity(int type, String name, String currency, String currencySymbol, long balance, int ordering) {
        this.type = type;
        this.name = name;
        this.currency = currency;
        this.currencySymbol = currencySymbol;
        this.balance = balance;
        this.ordering = ordering;
    }

    public int getOrdering() {
        return this.ordering;
    }

    public void setOrdering(int ordering) {
        this.ordering = ordering;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrencySymbol() {
        return this.currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public long getBalance() {
        return this.balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }
}
