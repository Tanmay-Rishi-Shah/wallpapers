package com.expance.merapaisa.Model;

import android.content.Context;
import com.expance.merapaisa.Utility.DataHelper;

/* loaded from: classes3.dex */
public class ExportCategory {
    private long amount;
    private int categoryDefault;
    private String name;
    private int type;

    public ExportCategory(String name, int categoryDefault, int type, long amount) {
        this.name = name;
        this.type = type;
        this.categoryDefault = categoryDefault;
        this.amount = amount;
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
}
