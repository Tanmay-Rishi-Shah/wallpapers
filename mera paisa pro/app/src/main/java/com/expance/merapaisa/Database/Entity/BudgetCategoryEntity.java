package com.expance.merapaisa.Database.Entity;

/* loaded from: classes3.dex */
public class BudgetCategoryEntity {
    private int budgetId;
    private int categoryId;
    private int id;

    public BudgetCategoryEntity(int categoryId, int budgetId) {
        this.categoryId = categoryId;
        this.budgetId = budgetId;
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

    public int getBudgetId() {
        return this.budgetId;
    }

    public void setBudgetId(int budgetId) {
        this.budgetId = budgetId;
    }
}
