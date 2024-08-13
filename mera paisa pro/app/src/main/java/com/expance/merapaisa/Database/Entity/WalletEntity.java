package com.expance.merapaisa.Database.Entity;

/* loaded from: classes3.dex */
public class WalletEntity {
    private int accountId;
    private int active;
    private long amount;
    private String color;
    private long creditLimit;
    private String currency;
    private int dueDate;
    private int exclude;
    private int hidden;
    private int icon;
    private int id;
    private long initialAmount;
    private String name;
    private int ordering;
    private int statementDate;
    private int type;

    public WalletEntity(String name, int icon, String color, long amount, long initialAmount, int active, int accountId, int ordering, int exclude, String currency, int type, int dueDate, int statementDate, long creditLimit) {
        this.name = name;
        this.color = color;
        this.amount = amount;
        this.initialAmount = initialAmount;
        this.active = active;
        this.accountId = accountId;
        this.ordering = ordering;
        this.exclude = exclude;
        this.currency = currency;
        this.icon = icon;
        this.type = type;
        this.dueDate = dueDate;
        this.statementDate = statementDate;
        this.creditLimit = creditLimit;
    }

    public int getHidden() {
        return this.hidden;
    }

    public void setHidden(int hidden) {
        this.hidden = hidden;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(int dueDate) {
        this.dueDate = dueDate;
    }

    public int getStatementDate() {
        return this.statementDate;
    }

    public void setStatementDate(int statementDate) {
        this.statementDate = statementDate;
    }

    public long getCreditLimit() {
        return this.creditLimit;
    }

    public void setCreditLimit(long creditLimit) {
        this.creditLimit = creditLimit;
    }

    public int getIcon() {
        return this.icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
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

    public long getAmount() {
        return this.amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getInitialAmount() {
        return this.initialAmount;
    }

    public void setInitialAmount(long initialAmount) {
        this.initialAmount = initialAmount;
    }

    public int getActive() {
        return this.active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getAccountId() {
        return this.accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getOrdering() {
        return this.ordering;
    }

    public void setOrdering(int ordering) {
        this.ordering = ordering;
    }

    public int getExclude() {
        return this.exclude;
    }

    public void setExclude(int exclude) {
        this.exclude = exclude;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
