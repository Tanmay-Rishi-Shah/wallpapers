package com.expance.merapaisa.Model;

/* loaded from: classes3.dex */
public class Currency {
    private String currency;
    private double rate;

    public Currency(double rate, String currency) {
        this.rate = rate;
        this.currency = currency;
    }

    public double getRate() {
        return this.rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
