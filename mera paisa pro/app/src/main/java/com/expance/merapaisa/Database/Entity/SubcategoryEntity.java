package com.expance.merapaisa.Database.Entity;

/* loaded from: classes3.dex */
public class SubcategoryEntity {
    private int categoryId;
    private int id;
    private String name;
    private int ordering;

    public SubcategoryEntity(int categoryId, String name, int ordering) {
        this.categoryId = categoryId;
        this.name = name;
        this.ordering = ordering;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrdering() {
        return this.ordering;
    }

    public void setOrdering(int ordering) {
        this.ordering = ordering;
    }
}
