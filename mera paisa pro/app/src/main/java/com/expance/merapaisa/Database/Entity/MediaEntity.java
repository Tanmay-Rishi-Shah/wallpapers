package com.expance.merapaisa.Database.Entity;

/* loaded from: classes3.dex */
public class MediaEntity {
    private int id;
    private String path;
    private int transId;

    public MediaEntity(String path, int transId) {
        this.path = path;
        this.transId = transId;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getTransId() {
        return this.transId;
    }

    public void setTransId(int transId) {
        this.transId = transId;
    }
}
