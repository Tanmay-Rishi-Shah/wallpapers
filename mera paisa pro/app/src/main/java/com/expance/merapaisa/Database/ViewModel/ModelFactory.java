package com.expance.merapaisa.Database.ViewModel;

import android.app.Application;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/* loaded from: classes3.dex */
public class ModelFactory extends ViewModelProvider.NewInstanceFactory {
    private int id;
    private Application mApplication;

    public ModelFactory(Application application, int id) {
        this.mApplication = application;
        this.id = id;
    }

    @Override // androidx.lifecycle.ViewModelProvider.NewInstanceFactory, androidx.lifecycle.ViewModelProvider.Factory
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass == BudgetDetailViewModel.class) {
            return (T) new BudgetDetailViewModel(this.mApplication, this.id);
        }
        if (modelClass == GoalDetailViewModel.class) {
            return (T) new GoalDetailViewModel(this.mApplication, this.id);
        }
        if (modelClass == TransactionDetailViewModel.class) {
            return (T) new TransactionDetailViewModel(this.mApplication, this.id);
        }
        if (modelClass == RecurringDetailViewModel.class) {
            return (T) new RecurringDetailViewModel(this.mApplication, this.id);
        }
        if (modelClass == CategoryViewModel.class) {
            return (T) new CategoryViewModel(this.mApplication, this.id);
        }
        if (modelClass == DebtTransViewModel.class) {
            return (T) new DebtTransViewModel(this.mApplication, this.id);
        }
        if (modelClass == BudgetExpandedViewModel.class) {
            return (T) new BudgetExpandedViewModel(this.mApplication, this.id);
        }
        return (T) super.create(modelClass);
    }
}
