package com.expance.merapaisa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.expance.merapaisa.Adapter.CategoryPickerAdapter;
import com.expance.merapaisa.Database.ViewModel.CategoryViewModel;
import com.expance.merapaisa.Database.ViewModel.ModelFactory;
import com.expance.merapaisa.Model.Category;
import com.expance.merapaisa.Model.Subcategory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

/* loaded from: classes3.dex */
public class CategoryIncomePicker extends Fragment implements CategoryPickerAdapter.OnItemClickListener {
    Activity activity;
    CategoryPickerAdapter adapter;
    CategoryViewModel categoryViewModel;
    ConstraintLayout emptyWrapper;
    int id;
    RecyclerView recyclerView;
    int subcategoryId;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_category_income_picker, container, false);
        this.adapter = new CategoryPickerAdapter(this.activity);
        this.id = this.activity.getIntent().getIntExtra("id", 0);
        this.subcategoryId = this.activity.getIntent().getIntExtra("subcategoryId", 0);
        this.recyclerView = (RecyclerView) viewGroup.findViewById(R.id.recyclerView);
        this.emptyWrapper = (ConstraintLayout) viewGroup.findViewById(R.id.emptyWrapper);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.activity));
        this.recyclerView.setAdapter(this.adapter);
        this.adapter.setListener(this);
        this.adapter.setId(this.id, this.subcategoryId);
        populateData();
        return viewGroup;
    }

    public void populateData() {
        CategoryViewModel categoryViewModel = (CategoryViewModel) new ViewModelProvider(this, new ModelFactory(this.activity.getApplication(), 1)).get(CategoryViewModel.class);
        this.categoryViewModel = categoryViewModel;
        categoryViewModel.getCategories().observe(getViewLifecycleOwner(), new Observer() { // from class: com.ktwapps.walletmanager.CategoryIncomePicker$$ExternalSyntheticLambda0
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                CategoryIncomePicker.this.m189xad66914e((List) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$populateData$0$com-ktwapps-walletmanager-CategoryIncomePicker  reason: not valid java name */
    public /* synthetic */ void m189xad66914e(List list) {
        ArrayList arrayList = new ArrayList();
        if (list != null) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                Category category = (Category) it.next();
                arrayList.add(category);
                if (category.getSubcategoryCount() > 0) {
                    String[] split = category.getSubcategoryIds().split(",");
                    String[] split2 = category.getSubcategory().split(",");
                    for (int i = 0; i < split.length; i++) {
                        arrayList.add(new Subcategory(Integer.parseInt(split[i]), category.getId(), split2[i], category.getName(this.activity), category.getColor()));
                    }
                }
            }
        }
        this.adapter.setList(arrayList);
        this.emptyWrapper.setVisibility(arrayList.size() != 0 ? 8 : 0);
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        this.activity = null;
    }

    @Override // com.ktwapps.walletmanager.Adapter.CategoryPickerAdapter.OnItemClickListener
    public void onItemClick(View view, int position) {
        if (this.adapter.getList().get(position) instanceof Category) {
            Category category = (Category) this.adapter.getList().get(position);
            int id = category.getId();
            String name = category.getName(this.activity);
            Intent intent = new Intent();
            intent.putExtra("id", id);
            intent.putExtra("subcategoryId", 0);
            intent.putExtra("name", name);
            intent.putExtra(JamXmlElements.TYPE, 0);
            this.activity.setResult(-1, intent);
        } else {
            Subcategory subcategory = (Subcategory) this.adapter.getList().get(position);
            int categoryId = subcategory.getCategoryId();
            int id2 = subcategory.getId();
            Intent intent2 = new Intent();
            intent2.putExtra("id", categoryId);
            intent2.putExtra("subcategoryId", id2);
            intent2.putExtra("name", subcategory.getCategoryName() + "/" + subcategory.getName());
            intent2.putExtra(JamXmlElements.TYPE, 0);
            this.activity.setResult(-1, intent2);
        }
        this.activity.finish();
        this.activity.overridePendingTransition(R.anim.scale_in, R.anim.right_to_left);
    }
}
