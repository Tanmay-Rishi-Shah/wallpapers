package com.expance.merapaisa.Database.ViewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Model.Template;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.List;

/* loaded from: classes3.dex */
public class TemplateViewModel extends AndroidViewModel {
    private LiveData<List<Template>> template;

    public TemplateViewModel(Application application) {
        super(application);
        AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(getApplication());
        this.template = appDatabaseObject.templateDaoObject().getLiveTemplate(SharePreferenceHelper.getAccountId(application));
    }

    public LiveData<List<Template>> getTemplate() {
        return this.template;
    }
}
