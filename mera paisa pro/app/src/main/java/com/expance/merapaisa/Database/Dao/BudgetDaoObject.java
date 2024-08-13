package com.expance.merapaisa.Database.Dao;

import androidx.lifecycle.LiveData;
import com.expance.merapaisa.Database.Entity.BudgetCategoryEntity;
import com.expance.merapaisa.Database.Entity.BudgetEntity;
import com.expance.merapaisa.Model.Budget;
import com.expance.merapaisa.Model.BudgetStats;
import com.expance.merapaisa.Model.BudgetTrans;
import java.util.List;

/* loaded from: classes3.dex */
public interface BudgetDaoObject {
    void deleteBudget(int id);

    void deleteBudgetCategory(int id);

    LiveData<List<Budget>> getBudget(int accountId, int status);

    Budget getBudgetById(int id);

    List<Integer> getBudgetCategoryIds(int id);

    List<BudgetEntity> getBudgetEntityByCategory(List<Integer> budgetIds, int accountId, int status);

    BudgetEntity getBudgetEntityById(int id);

    List<Integer> getBudgetIds(int id);

    long getBudgetSpent(long startDate, long endDate, int type, int accountId);

    long getBudgetSpent(List<Integer> categoryId, long startDate, long endDate, int type, int accountId);

    List<BudgetStats> getBudgetStats(int budgetId, int accountId, long startDate, long endDate);

    List<BudgetStats> getBudgetStats(int accountId, long startDate, long endDate);

    List<BudgetTrans> getBudgetTrans(int accountId, int budgetId, long startDate, long endDate);

    List<BudgetTrans> getBudgetTrans(int accountId, long startDate, long endDate);

    int getBudgetTransCount(int accountId, int budgetId, long startDate, long endDate);

    LiveData<Budget> getLiveBudgetById(int id);

    LiveData<List<Budget>> getPeriodBudget(int accountId, List<Integer> periods);

    long insertBudget(BudgetEntity budgetEntity);

    void insertBudgetCategory(BudgetCategoryEntity budgetCategoryEntity);

    void updateBudget(BudgetEntity budgetEntity);
}
