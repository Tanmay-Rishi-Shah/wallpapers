package com.expance.merapaisa.Model;

import android.content.Context;
import com.expance.merapaisa.Utility.DataHelper;
import java.util.Date;

/* loaded from: classes3.dex */
public class Recurring {
    int accountId;
    long amount;
    String category;
    int categoryDefault;
    int categoryId;
    String color;
    String currency;
    Date dateTime;
    int icon;
    int id;
    int increment;
    int isFuture;
    Date lastUpdateTime;
    String memo;
    public String note;
    int recurringType;
    String repeatDate;
    int repeatType;
    String subcategory;
    int subcategoryId;
    int type;
    Date untilTime;
    String wallet;
    int walletId;

    public Recurring(int accountId, String color, String note, String memo, Date dateTime, Date untilTime, Date lastUpdateTime, long amount, String wallet, int increment, int type, int recurringType, int repeatType, String repeatDate, int walletId, String category, int categoryDefault, int categoryId, String currency, int icon, int isFuture, int subcategoryId, String subcategory) {
        this.color = color;
        this.accountId = accountId;
        this.note = note;
        this.memo = memo;
        this.dateTime = dateTime;
        this.untilTime = untilTime;
        this.lastUpdateTime = lastUpdateTime;
        this.amount = amount;
        this.wallet = wallet;
        this.increment = increment;
        this.type = type;
        this.recurringType = recurringType;
        this.repeatType = repeatType;
        this.repeatDate = repeatDate;
        this.walletId = walletId;
        this.category = category;
        this.categoryId = categoryId;
        this.subcategoryId = subcategoryId;
        this.subcategory = subcategory;
        this.categoryDefault = categoryDefault;
        this.currency = currency;
        this.icon = icon;
        this.isFuture = isFuture;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getIsFuture() {
        return this.isFuture;
    }

    public void setIsFuture(int isFuture) {
        this.isFuture = isFuture;
    }

    public int getAccountId() {
        return this.accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getRepeatDate() {
        return this.repeatDate;
    }

    public void setRepeatDate(String repeatDate) {
        this.repeatDate = repeatDate;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIncrement() {
        return this.increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNote(Context context) {
        String str = this.note;
        return (str == null || str.length() == 0) ? getSubcategory().length() == 0 ? getCategory(context) : getSubcategory() : this.note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public long getAmount() {
        return this.amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getWallet() {
        return this.wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
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

    public int getWalletId() {
        return this.walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory(Context context) {
        String str = this.category;
        String str2 = "";
        if (str == null || str.length() == 0) {
            if (this.categoryDefault != 0) {
                StringBuilder sb = new StringBuilder();
                sb.append(DataHelper.getDefaultCategory(context, this.categoryDefault));
                if (getSubcategory().length() != 0) {
                    str2 = "/" + getSubcategory();
                }
                sb.append(str2);
                return sb.toString();
            }
            return "";
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.category);
        if (getSubcategory().length() != 0) {
            str2 = "/" + getSubcategory();
        }
        sb2.append(str2);
        return sb2.toString();
    }

    public int getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getCategoryDefault() {
        return this.categoryDefault;
    }

    public void setCategoryDefault(int categoryDefault) {
        this.categoryDefault = categoryDefault;
    }

    public int getIcon() {
        return this.icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getSubcategory() {
        String str = this.subcategory;
        return str == null ? "" : str;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public int getSubcategoryId() {
        return this.subcategoryId;
    }

    public void setSubcategoryId(int subcategoryId) {
        this.subcategoryId = subcategoryId;
    }
}
