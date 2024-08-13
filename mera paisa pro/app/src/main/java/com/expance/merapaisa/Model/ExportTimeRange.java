package com.expance.merapaisa.Model;

/* loaded from: classes3.dex */
public class ExportTimeRange {
    private long endDate;
    private String formattedDate;
    private long startDate;

    public ExportTimeRange(String formattedDate, long startDate, long endDate) {
        this.formattedDate = formattedDate;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getFormattedDate() {
        return this.formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }

    public long getStartDate() {
        return this.startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return this.endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }
}
