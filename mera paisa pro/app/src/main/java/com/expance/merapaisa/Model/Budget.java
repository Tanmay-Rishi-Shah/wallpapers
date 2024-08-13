package com.expance.merapaisa.Model;

import android.content.Context;
import android.text.TextUtils;
import com.expance.merapaisa.Utility.DataHelper;
import java.util.Date;

/* loaded from: classes3.dex */
public class Budget {
    private int accountId;
    private long amount;
    private String categories;
    private String categoriesDefault;
    private int categoryId;
    private String color;
    private Date endDate;
    private int id;
    private String name;
    private int period;
    private int repeat;
    private long spent;
    private Date startDate;
    private int status;
    private int transCount;

    public Budget(String name, String color, String categories, String categoriesDefault, int categoryId, long spent, long amount, Date endDate, Date startDate, int repeat, int period, int accountId, int status) {
        this.name = name;
        this.color = color;
        this.categories = categories;
        this.categoriesDefault = categoriesDefault;
        this.categoryId = categoryId;
        this.spent = spent;
        this.amount = amount;
        this.endDate = endDate;
        this.startDate = startDate;
        this.repeat = repeat;
        this.period = period;
        this.accountId = accountId;
        this.status = status;
    }

    public int getTransCount() {
        return this.transCount;
    }

    public void setTransCount(int transCount) {
        this.transCount = transCount;
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

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCategories(Context context) {
        String[] split = this.categories.split(",", -1);
        String[] split2 = this.categoriesDefault.split(",", -1);
        String[] strArr = new String[split.length];
        for (int i = 0; i < split.length; i++) {
            String trim = split[i].trim();
            if (trim.length() == 0) {
                strArr[i] = DataHelper.getDefaultCategory(context, Integer.parseInt(split2[i].trim()));
            } else {
                strArr[i] = trim;
            }
        }
        return TextUtils.join(",", strArr);
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getCategoriesDefault() {
        return this.categoriesDefault;
    }

    public void setCategoriesDefault(String categoriesDefault) {
        this.categoriesDefault = categoriesDefault;
    }

    public int getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public long getSpent() {
        return this.spent;
    }

    public void setSpent(long spent) {
        this.spent = spent;
    }

    public long getAmount() {
        return this.amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getRepeat() {
        return this.repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public int getPeriod() {
        return this.period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getAccountId() {
        return this.accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
