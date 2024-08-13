package com.expance.merapaisa.Model;

import android.content.Context;
import com.expance.merapaisa.Utility.DataHelper;
import java.util.Date;

/* loaded from: classes3.dex */
public class Exports {
    private long amount;
    private String category;
    private int categoryDefault;
    private String currency;
    private Date datetime;
    private String memo;
    private String transferWallet;
    private int type;
    private String wallet;

    public Exports(Date datetime, long amount, int type, String currency, String category, String wallet, String transferWallet, String memo, int categoryDefault) {
        this.datetime = datetime;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.wallet = wallet;
        this.transferWallet = transferWallet;
        this.memo = memo;
        this.currency = currency;
        this.categoryDefault = categoryDefault;
    }

    public String getTransferWallet() {
        return this.transferWallet;
    }

    public void setTransferWallet(String transferWallet) {
        this.transferWallet = transferWallet;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getDatetime() {
        return this.datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public long getAmount() {
        return this.amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCategory(Context context) {
        String str = this.category;
        if (str == null || str.length() == 0) {
            int i = this.categoryDefault;
            return i != 0 ? DataHelper.getDefaultCategory(context, i) : "";
        }
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getWallet() {
        return this.wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getCategoryDefault() {
        return this.categoryDefault;
    }

    public void setCategoryDefault(int categoryDefault) {
        this.categoryDefault = categoryDefault;
    }
}
