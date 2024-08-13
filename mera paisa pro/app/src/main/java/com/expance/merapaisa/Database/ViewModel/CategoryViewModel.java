package com.expance.merapaisa.Database.ViewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Model.Category;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.List;

/* loaded from: classes3.dex */
public class CategoryViewModel extends AndroidViewModel {
    private LiveData<List<Category>> categories;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CategoryViewModel(Application application, int type) {
        super(application);
        this.categories = AppDatabaseObject.getInstance(getApplication()).categoryDaoObject().getLiveCategory(type, 0, SharePreferenceHelper.getAccountId(getApplication()));
    }

    public LiveData<List<Category>> getCategories() {
        return this.categories;
    }
}
