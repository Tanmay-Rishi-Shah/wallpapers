package com.expance.merapaisa.Model;

import android.content.Context;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.DataHelper;

/* loaded from: classes3.dex */
public class Stats {
    private long amount;
    private int categoryDefault;
    private String color;
    private int icon;
    private int id;
    private String name;
    private double percent;
    private int trans;

    public Stats(String name, String color, int icon, long amount, double percent, int id, int trans, int categoryDefault) {
        this.name = name;
        this.color = color;
        this.amount = amount;
        this.icon = icon;
        this.percent = percent;
        this.id = id;
        this.trans = trans;
        this.categoryDefault = categoryDefault;
    }

    public int getCategoryDefault() {
        return this.categoryDefault;
    }

    public void setCategoryDefault(int categoryDefault) {
        this.categoryDefault = categoryDefault;
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

    public String getName(Context context) {
        if (this.id != 0) {
            String str = this.name;
            if (str == null || str.length() == 0) {
                int i = this.categoryDefault;
                return i != 0 ? DataHelper.getDefaultCategory(context, i) : "";
            }
            return this.name;
        }
        return context.getResources().getString(R.string.transfer);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        String str = this.color;
        return (str == null || str.length() == 0) ? "#808080" : this.color;
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

    public double getPercent() {
        return this.percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
