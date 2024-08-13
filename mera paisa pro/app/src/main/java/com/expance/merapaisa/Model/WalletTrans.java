package com.expance.merapaisa.Model;

import android.content.Context;
import com.expance.merapaisa.Utility.DataHelper;

/* loaded from: classes3.dex */
public class WalletTrans {
    long amount;
    int categoryDefault;
    String color;
    int icon;
    int id;
    String name;
    int trans;
    int type;

    public WalletTrans(int id, int type, int icon, int trans, String color, String name, long amount, int categoryDefault) {
        this.id = id;
        this.type = type;
        this.icon = icon;
        this.trans = trans;
        this.color = color;
        this.name = name;
        this.amount = amount;
        this.categoryDefault = categoryDefault;
    }

    public int getCategoryDefault() {
        return this.categoryDefault;
    }

    public void setCategoryDefault(int categoryDefault) {
        this.categoryDefault = categoryDefault;
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

    public int getIcon() {
        return this.icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getTrans() {
        return this.trans;
    }

    public void setTrans(int trans) {
        this.trans = trans;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName(Context context) {
        String str = this.name;
        if (str == null || str.length() == 0) {
            int i = this.categoryDefault;
            return i != 0 ? DataHelper.getDefaultCategory(context, i) : "";
        }
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAmount() {
        return this.amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
