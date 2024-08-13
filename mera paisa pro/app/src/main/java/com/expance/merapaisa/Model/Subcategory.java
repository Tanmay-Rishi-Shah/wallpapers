package com.expance.merapaisa.Model;

/* loaded from: classes3.dex */
public class Subcategory {
    int categoryId;
    String categoryName;
    String color;
    int id;
    String name;

    public Subcategory(int id, int categoryId, String name, String categoryName, String color) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.categoryName = categoryName;
        this.color = color;
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

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
