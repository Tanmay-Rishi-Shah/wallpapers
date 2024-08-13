package com.expance.merapaisa.Model;

/* loaded from: classes3.dex */
public class Currencies {
    private int id;
    private String mainCurrency;
    private double rate;
    private String subCurrency;

    public Currencies(int id, double rate, String mainCurrency, String subCurrency) {
        this.id = id;
        this.rate = rate;
        this.mainCurrency = mainCurrency;
        this.subCurrency = subCurrency;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getRate() {
        return this.rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getMainCurrency() {
        return this.mainCurrency;
    }

    public void setMainCurrency(String mainCurrency) {
        this.mainCurrency = mainCurrency;
    }

    public String getSubCurrency() {
        return this.subCurrency;
    }

    public void setSubCurrency(String subCurrency) {
        this.subCurrency = subCurrency;
    }
}
