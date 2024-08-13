package com.expance.merapaisa.Model;

/* loaded from: classes3.dex */
public class SettingItem {
    private String detail;
    private boolean isCheckBox;
    private String name;

    public SettingItem(String name, String detail, boolean isCheckBox) {
        this.name = name;
        this.detail = detail;
        this.isCheckBox = isCheckBox;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return this.detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public boolean isCheckBox() {
        return this.isCheckBox;
    }

    public void setCheckBox(boolean checkBox) {
        this.isCheckBox = checkBox;
    }
}
