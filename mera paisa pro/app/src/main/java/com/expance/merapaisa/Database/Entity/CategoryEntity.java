package com.expance.merapaisa.Database.Entity;

/* loaded from: classes3.dex */
public class CategoryEntity {
    private int accountId;
    private int active;
    private String color;
    private int defaultCategory;
    private int icon;
    private int id;
    private String name;
    private int ordering;
    private int type;

    public CategoryEntity(String name, String color, int icon, int type, int active, int ordering, int accountId, int defaultCategory) {
        this.name = name;
        this.color = color;
        this.type = type;
        this.active = active;
        this.icon = icon;
        this.ordering = ordering;
        this.accountId = accountId;
        this.defaultCategory = defaultCategory;
    }

    public int getDefaultCategory() {
        return this.defaultCategory;
    }

    public void setDefaultCategory(int defaultCategory) {
        this.defaultCategory = defaultCategory;
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

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getActive() {
        return this.active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getOrdering() {
        return this.ordering;
    }

    public void setOrdering(int ordering) {
        this.ordering = ordering;
    }

    public String getName() {
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

    public int getAccountId() {
        return this.accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}
