package com.expance.merapaisa.Model;

import android.content.Context;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.DataHelper;
import java.util.Date;

/* loaded from: classes3.dex */
public class Trans {
    private long amount;
    private String category;
    private int categoryDefault;
    private int categoryId;
    private String color;
    private String currency;
    private Date dateTime;
    private String debtColor;
    private int debtId;
    private int debtTransId;
    private int debtTransType;
    private int debtType;
    private int feeId;
    private int icon;
    private int id;
    private boolean isRecurring;
    private String media;
    private String memo;
    public String note;
    private int recurringId;
    private String subcategory;
    private int subcategoryId;
    private long transAmount;
    private String transferWallet;
    private int transferWalletId;
    private int type;
    private String wallet;
    private int walletId;

    public Trans(String note, String memo, String color, int icon, String currency, Date dateTime, long amount, String wallet, int type, String transferWallet, int walletId, int transferWalletId, String category, int categoryId, int categoryDefault, int feeId, String media, long transAmount, String debtColor, int debtId, int debtType, int debtTransId, int debtTransType, String subcategory, int subcategoryId) {
        this.note = note;
        this.memo = memo;
        this.color = color;
        this.currency = currency;
        this.dateTime = dateTime;
        this.amount = amount;
        this.wallet = wallet;
        this.type = type;
        this.transferWallet = transferWallet;
        this.walletId = walletId;
        this.transferWalletId = transferWalletId;
        this.category = category;
        this.categoryId = categoryId;
        this.media = media;
        this.transAmount = transAmount;
        this.icon = icon;
        this.feeId = feeId;
        this.categoryDefault = categoryDefault;
        this.debtColor = debtColor;
        this.debtId = debtId;
        this.debtType = debtType;
        this.debtTransId = debtTransId;
        this.debtTransType = debtTransType;
        this.subcategory = subcategory;
        this.subcategoryId = subcategoryId;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public boolean isRecurring() {
        return this.isRecurring;
    }

    public void setRecurring(boolean recurring) {
        this.isRecurring = recurring;
    }

    public int getRecurringId() {
        return this.recurringId;
    }

    public void setRecurringId(int recurringId) {
        this.recurringId = recurringId;
    }

    public int getFeeId() {
        return this.feeId;
    }

    public void setFeeId(int feeId) {
        this.feeId = feeId;
    }

    public int getCategoryDefault() {
        return this.categoryDefault;
    }

    public void setCategoryDefault(int categoryDefault) {
        this.categoryDefault = categoryDefault;
    }

    public int getIcon() {
        if (this.debtId == 0 || this.debtTransId == 0 || this.debtTransType != 1) {
            return this.icon;
        }
        return this.debtType == 0 ? 171 : 170;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public long getTransAmount() {
        return this.transAmount;
    }

    public void setTransAmount(long transAmount) {
        this.transAmount = transAmount;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote(Context context) {
        return this.note.length() == 0 ? this.type == 2 ? context.getString(R.string.transfer) : getSubcategory().length() == 0 ? getCategory(context) : getSubcategory() : this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getColor() {
        String str = this.color;
        return str == null ? "#A7A9AB" : this.debtId != 0 ? this.debtColor : str;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Date getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
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

    public String getTransferWallet() {
        return this.transferWallet;
    }

    public void setTransferWallet(String transferWallet) {
        this.transferWallet = transferWallet;
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

    public int getDebtTransType() {
        return this.debtTransType;
    }

    public void setDebtTransType(int debtTransType) {
        this.debtTransType = debtTransType;
    }

    public String getDebtColor() {
        return this.debtColor;
    }

    public void setDebtColor(String debtColor) {
        this.debtColor = debtColor;
    }

    public int getDebtId() {
        return this.debtId;
    }

    public void setDebtId(int debtId) {
        this.debtId = debtId;
    }

    public int getDebtType() {
        return this.debtType;
    }

    public void setDebtType(int debtType) {
        this.debtType = debtType;
    }

    public int getDebtTransId() {
        return this.debtTransId;
    }

    public void setDebtTransId(int debtTransId) {
        this.debtTransId = debtTransId;
    }

    public String getCategory(Context context) {
        String str = this.category;
        String str2 = "";
        if (str == null || str.length() == 0) {
            if (this.categoryDefault != 0) {
                if (this.debtId != 0 && this.debtTransId != 0 && this.debtTransType == 1) {
                    if (this.debtType == 0) {
                        return context.getResources().getString(R.string.debt_increase);
                    }
                    return context.getResources().getString(R.string.loan_increase);
                }
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

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMedia() {
        return this.media;
    }

    public void setMedia(String media) {
        this.media = media;
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
