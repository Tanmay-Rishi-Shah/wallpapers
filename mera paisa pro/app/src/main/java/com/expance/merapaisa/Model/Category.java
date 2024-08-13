package com.expance.merapaisa.Model;

import android.content.Context;
import com.expance.merapaisa.Utility.DataHelper;

/* loaded from: classes3.dex */
public class Category {
    private int categoryDefault;
    private String color;
    private int icon;
    private int id;
    private String name;
    private String subcategory;
    private int subcategoryCount;
    private String subcategoryIds;
    private String subcategoryOrdering;

    public Category(int id, int icon, String name, String color, int categoryDefault, int subcategoryCount, String subcategory, String subcategoryIds, String subcategoryOrdering) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.icon = icon;
        this.categoryDefault = categoryDefault;
        this.subcategory = subcategory;
        this.subcategoryCount = subcategoryCount;
        this.subcategoryIds = subcategoryIds;
        this.subcategoryOrdering = subcategoryOrdering;
    }

    public int getIcon() {
        return this.icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getCategoryDefault() {
        return this.categoryDefault;
    }

    public void setCategoryDefault(int categoryDefault) {
        this.categoryDefault = categoryDefault;
    }

    public int getSubcategoryCount() {
        return this.subcategoryCount;
    }

    public void setSubcategoryCount(int subcategoryCount) {
        this.subcategoryCount = subcategoryCount;
    }

    public String getSubcategory() {
        return this.subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getSubcategoryIds() {
        return this.subcategoryIds;
    }

    public void setSubcategoryIds(String subcategoryIds) {
        this.subcategoryIds = subcategoryIds;
    }

    public String getSubcategoryOrdering() {
        return this.subcategoryOrdering;
    }

    public void setSubcategoryOrdering(String subcategoryOrdering) {
        this.subcategoryOrdering = subcategoryOrdering;
    }
}
