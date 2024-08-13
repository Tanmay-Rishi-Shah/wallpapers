package com.expance.merapaisa.Database.Entity;

/* loaded from: classes3.dex */
public class TemplateEntity {
    private int accountId;
    private long amount;
    private int categoryId;
    private int id;
    private String memo;
    private String name;
    private String note;
    private int ordering;
    private int subcategoryId;
    private int walletId;

    public TemplateEntity(String name, String note, String memo, long amount, int categoryId, int walletId, int accountId, int ordering, int subcategoryId) {
        this.name = name;
        this.note = note;
        this.memo = memo;
        this.amount = amount;
        this.categoryId = categoryId;
        this.walletId = walletId;
        this.accountId = accountId;
        this.ordering = ordering;
        this.subcategoryId = subcategoryId;
    }

    public int getAccountId() {
        return this.accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
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

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public long getAmount() {
        return this.amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public int getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getWalletId() {
        return this.walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public int getOrdering() {
        return this.ordering;
    }

    public void setOrdering(int ordering) {
        this.ordering = ordering;
    }

    public int getSubcategoryId() {
        return this.subcategoryId;
    }

    public void setSubcategoryId(int subcategoryId) {
        this.subcategoryId = subcategoryId;
    }
}
