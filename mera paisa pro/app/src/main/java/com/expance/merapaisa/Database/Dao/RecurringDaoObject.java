package com.expance.merapaisa.Database.Dao;

import androidx.lifecycle.LiveData;
import com.expance.merapaisa.Database.Entity.RecurringEntity;
import com.expance.merapaisa.Model.Recurring;
import java.util.List;

/* loaded from: classes3.dex */
public interface RecurringDaoObject {
    void deleteRecurring(int id);

    List<Recurring> getAllRecurringList();

    List<Recurring> getAllRecurringListByAccountId(int accountId);

    LiveData<Recurring> getLiveRecurringById(int account_id, int id);

    LiveData<List<Recurring>> getLiveRecurringList(int account_id);

    Recurring getRecurringById(int account_id, int id);

    RecurringEntity getRecurringEntity(int id);

    void insetRecurring(RecurringEntity recurringEntity);

    void removeSubcategory(int id);

    void updateRecurring(RecurringEntity recurringEntity);

    void updateRecurringUpdateTime(long lastUpdateTime, int id);
}
