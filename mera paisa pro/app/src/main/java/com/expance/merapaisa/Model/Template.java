package com.expance.merapaisa.Model;

import android.content.Context;
import com.expance.merapaisa.Utility.DataHelper;

/* loaded from: classes3.dex */
public class Template {
    private long amount;
    private String category;
    private int categoryDefault;
    private int categoryId;
    private String color;
    private int icon;
    private int id;
    private String memo;
    private String name;
    private String note;
    private String subcategory;
    private int subcategoryId;
    private int type;
    private int walletId;

    public Template(int id, String name, String note, String memo, String color, long amount, int icon, int categoryId, int categoryDefault, String category, int type, int walletId, int subcategoryId, String subcategory) {
        this.id = id;
        this.name = name;
        this.note = note;
        this.memo = memo;
        this.color = color;
        this.amount = amount;
        this.icon = icon;
        this.categoryId = categoryId;
        this.categoryDefault = categoryDefault;
        this.category = category;
        this.type = type;
        this.walletId = walletId;
        this.subcategory = subcategory;
        this.subcategoryId = subcategoryId;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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

    public int getIcon() {
        return this.icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCategoryDefault() {
        return this.categoryDefault;
    }

    public void setCategoryDefault(int categoryDefault) {
        this.categoryDefault = categoryDefault;
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

    public void setCategory(String category) {
        this.category = category;
    }

    public int getType() {
        return this.type == 2 ? 1 : 0;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getWalletId() {
        return this.walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public int getSubcategoryId() {
        return this.subcategoryId;
    }

    public void setSubcategoryId(int subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public String getSubcategory() {
        String str = this.subcategory;
        return str == null ? "" : str;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }
}
