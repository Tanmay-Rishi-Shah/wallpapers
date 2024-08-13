package com.expance.merapaisa.Database.Dao;

import androidx.lifecycle.LiveData;
import com.expance.merapaisa.Database.Entity.TemplateEntity;
import com.expance.merapaisa.Model.Template;
import java.util.List;

/* loaded from: classes3.dex */
public interface TemplateDaoObject {
    void deleteTemplate(int id);

    void deleteTemplateByCategoryId(int categoryId);

    void deleteTemplateByWalletId(int walletId);

    LiveData<List<Template>> getLiveTemplate(int accountId);

    Template getTemplateById(int id);

    TemplateEntity getTemplateEntityById(int id);

    int getTemplateLastOrdering(int accountId);

    void insertTemplate(TemplateEntity templateEntity);

    void removeSubcategory(int id);

    void updateTemplate(TemplateEntity templateEntity);

    void updateTemplateOrdering(int order, int id);
}
