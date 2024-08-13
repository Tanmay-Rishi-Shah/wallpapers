package com.expance.merapaisa.Model;

import android.content.Context;
import com.expance.merapaisa.Utility.DataHelper;

/* loaded from: classes3.dex */
public class BudgetTrans {
    long amount;
    int categoryDefault;
    String color;
    int icon;
    int id;
    String name;
    int trans;

    public BudgetTrans(int id, int trans, int icon, long amount, String color, String name, int categoryDefault) {
        this.id = id;
        this.trans = trans;
        this.icon = icon;
        this.amount = amount;
        this.color = color;
        this.name = name;
        this.categoryDefault = categoryDefault;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTrans() {
        return this.trans;
    }

    public void setTrans(int trans) {
        this.trans = trans;
    }

    public int getIcon() {
        return this.icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public long getAmount() {
        return this.amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
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

    public int getCategoryDefault() {
        return this.categoryDefault;
    }

    public void setCategoryDefault(int categoryDefault) {
        this.categoryDefault = categoryDefault;
    }
}
