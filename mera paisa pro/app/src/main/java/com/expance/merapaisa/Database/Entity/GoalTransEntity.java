package com.expance.merapaisa.Database.Entity;

import java.util.Date;

/* loaded from: classes3.dex */
public class GoalTransEntity {
    private long amount;
    private Date dateTime;
    private int goalId;
    private int id;
    private String note;
    private int type;

    public GoalTransEntity(long amount, Date dateTime, int goalId, int type, String note) {
        this.amount = amount;
        this.dateTime = dateTime;
        this.goalId = goalId;
        this.type = type;
        this.note = note;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getAmount() {
        return this.amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Date getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public int getGoalId() {
        return this.goalId;
    }

    public void setGoalId(int goalId) {
        this.goalId = goalId;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
