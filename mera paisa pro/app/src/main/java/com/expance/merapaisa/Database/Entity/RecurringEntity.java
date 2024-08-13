package com.expance.merapaisa.Database.Entity;

import java.util.Date;

/* loaded from: classes3.dex */
public class RecurringEntity {
    private int accountId;
    private long amount;
    private int categoryId;
    private Date dateTime;
    private int id;
    private int increment;
    private int isFuture;
    private Date lastUpdateTime;
    private String memo;
    private String note;
    private int recurringType;
    private String repeatDate;
    private int repeatType;
    private int subcategoryId;
    private long transAmount;
    private int transferWalletId;
    private int type;
    private Date untilTime;
    private int walletId;

    public RecurringEntity(String note, String memo, int type, int recurringType, int repeatType, String repeatDate, int increment, long amount, Date dateTime, Date untilTime, Date lastUpdateTime, int accountId, int categoryId, int walletId, int transferWalletId, long transAmount, int isFuture, int subcategoryId) {
        this.note = note;
        this.memo = memo;
        this.type = type;
        this.recurringType = recurringType;
        this.repeatType = repeatType;
        this.repeatDate = repeatDate;
        this.increment = increment;
        this.amount = amount;
        this.dateTime = dateTime;
        this.untilTime = untilTime;
        this.lastUpdateTime = lastUpdateTime;
        this.accountId = accountId;
        this.categoryId = categoryId;
        this.walletId = walletId;
        this.transferWalletId = transferWalletId;
        this.transAmount = transAmount;
        this.isFuture = isFuture;
        this.subcategoryId = subcategoryId;
    }

    public int getIsFuture() {
        return this.isFuture;
    }

    public void setIsFuture(int isFuture) {
        this.isFuture = isFuture;
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

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRecurringType() {
        return this.recurringType;
    }

    public void setRecurringType(int recurringType) {
        this.recurringType = recurringType;
    }

    public int getRepeatType() {
        return this.repeatType;
    }

    public void setRepeatType(int repeatType) {
        this.repeatType = repeatType;
    }

    public String getRepeatDate() {
        return this.repeatDate;
    }

    public void setRepeatDate(String repeatDate) {
        this.repeatDate = repeatDate;
    }

    public int getIncrement() {
        return this.increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
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

    public Date getUntilTime() {
        return this.untilTime;
    }

    public void setUntilTime(Date untilTime) {
        this.untilTime = untilTime;
    }

    public Date getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
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

    public int getTransferWalletId() {
        return this.transferWalletId;
    }

    public void setTransferWalletId(int transferWalletId) {
        this.transferWalletId = transferWalletId;
    }

    public long getTransAmount() {
        return this.transAmount;
    }

    public void setTransAmount(long transAmount) {
        this.transAmount = transAmount;
    }

    public int getSubcategoryId() {
        return this.subcategoryId;
    }

    public void setSubcategoryId(int subcategoryId) {
        this.subcategoryId = subcategoryId;
    }
}
