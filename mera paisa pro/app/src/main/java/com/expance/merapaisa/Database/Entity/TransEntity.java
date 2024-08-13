package com.expance.merapaisa.Database.Entity;

import java.util.Date;

/* loaded from: classes3.dex */
public class TransEntity {
    private int accountId;
    private long amount;
    private int categoryId;
    private Date dateTime;
    private int debtId;
    private int debtTransId;
    private int feeId;
    private int id;
    private String memo;
    private String note;
    private int subcategoryId;
    private long transAmount;
    private int transferWalletId;
    private int type;
    private int walletId;

    public TransEntity(String note, String memo, long amount, Date dateTime, int type, int accountId, int categoryId, int feeId, int walletId, int transferWalletId, long transAmount, int debtId, int debtTransId, int subcategoryId) {
        this.note = note;
        this.memo = memo;
        this.amount = amount;
        this.dateTime = dateTime;
        this.accountId = accountId;
        this.categoryId = categoryId;
        this.feeId = feeId;
        this.walletId = walletId;
        this.type = type;
        this.transferWalletId = transferWalletId;
        this.transAmount = transAmount;
        this.debtId = debtId;
        this.debtTransId = debtTransId;
        this.subcategoryId = subcategoryId;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getDebtId() {
        return this.debtId;
    }

    public void setDebtId(int debtId) {
        this.debtId = debtId;
    }

    public int getDebtTransId() {
        return this.debtTransId;
    }

    public void setDebtTransId(int debtTransId) {
        this.debtTransId = debtTransId;
    }

    public long getTransAmount() {
        return this.transAmount;
    }

    public void setTransAmount(long transAmount) {
        this.transAmount = transAmount;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
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

    public int getWalletId() {
        return this.walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public int getFeeId() {
        return this.feeId;
    }

    public void setFeeId(int feeId) {
        this.feeId = feeId;
    }

    public int getTransferWalletId() {
        return this.transferWalletId;
    }

    public void setTransferWalletId(int transferWalletId) {
        this.transferWalletId = transferWalletId;
    }

    public int getSubcategoryId() {
        return this.subcategoryId;
    }

    public void setSubcategoryId(int subcategoryId) {
        this.subcategoryId = subcategoryId;
    }
}
