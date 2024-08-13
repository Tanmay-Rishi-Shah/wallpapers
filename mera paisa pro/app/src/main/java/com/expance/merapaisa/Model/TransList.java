package com.expance.merapaisa.Model;

/* loaded from: classes3.dex */
public class TransList {
    private DailyTrans dailyTrans;
    private boolean isDailyHeader;
    private Trans trans;

    public TransList(boolean isDailyHeader, Trans trans, DailyTrans dailyTrans) {
        this.isDailyHeader = isDailyHeader;
        this.trans = trans;
        this.dailyTrans = dailyTrans;
    }

    public boolean isDailyHeader() {
        return this.isDailyHeader;
    }

    public void setDailyHeader(boolean dailyHeader) {
        this.isDailyHeader = dailyHeader;
    }

    public Trans getTrans() {
        return this.trans;
    }

    public void setTrans(Trans trans) {
        this.trans = trans;
    }

    public DailyTrans getDailyTrans() {
        return this.dailyTrans;
    }

    public void setDailyTrans(DailyTrans dailyTrans) {
        this.dailyTrans = dailyTrans;
    }
}
