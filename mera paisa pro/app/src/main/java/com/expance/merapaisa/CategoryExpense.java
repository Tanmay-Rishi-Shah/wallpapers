package com.expance.merapaisa;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.expance.merapaisa.Adapter.ManageCategoryAdapter;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Model.Category;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import com.expance.merapaisa.Widget.SwipeAndDragViewHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

/* loaded from: classes3.dex */
public class CategoryExpense extends Fragment implements ManageCategoryAdapter.OnItemClickListener {
    Activity activity;
    ManageCategoryAdapter adapter;
    ConstraintLayout emptyWrapper;
    RecyclerView recyclerView;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_category_expense, container, false);
        this.recyclerView = (RecyclerView) viewGroup.findViewById(R.id.recyclerView);
        this.emptyWrapper = (ConstraintLayout) viewGroup.findViewById(R.id.emptyWrapper);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ManageCategoryAdapter manageCategoryAdapter = new ManageCategoryAdapter(getActivity());
        this.adapter = manageCategoryAdapter;
        this.recyclerView.setAdapter(manageCategoryAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeAndDragViewHelper(this.adapter));
        this.adapter.setTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(this.recyclerView);
        this.adapter.setListener(this);
        return viewGroup;
    }

    public List<Category> getCategoryList() {
        return this.adapter.getList();
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        populateData();
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CategoryExpense$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                CategoryExpense.this.m179lambda$onPause$0$comktwappswalletmanagerCategoryExpense();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onPause$0$com-ktwapps-walletmanager-CategoryExpense  reason: not valid java name */
    public /* synthetic */ void m179lambda$onPause$0$comktwappswalletmanagerCategoryExpense() {
        AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(getActivity());
        for (int i = 0; i < this.adapter.list.size(); i++) {
            appDatabaseObject.categoryDaoObject().updateCategoryOrdering(i, this.adapter.list.get(i).getId());
        }
    }

    private void populateData() {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CategoryExpense$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                CategoryExpense.this.m181lambda$populateData$2$comktwappswalletmanagerCategoryExpense();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$populateData$2$com-ktwapps-walletmanager-CategoryExpense  reason: not valid java name */
    public /* synthetic */ void m181lambda$populateData$2$comktwappswalletmanagerCategoryExpense() {
        Category[] category = AppDatabaseObject.getInstance(getActivity()).categoryDaoObject().getCategory(2, 0, SharePreferenceHelper.getAccountId(this.activity));
        final ArrayList arrayList = new ArrayList();
        Collections.addAll(arrayList, category);
        this.activity.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CategoryExpense$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                CategoryExpense.this.m180lambda$populateData$1$comktwappswalletmanagerCategoryExpense(arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$populateData$1$com-ktwapps-walletmanager-CategoryExpense  reason: not valid java name */
    public /* synthetic */ void m180lambda$populateData$1$comktwappswalletmanagerCategoryExpense(List list) {
        this.adapter.setList(list);
        this.emptyWrapper.setVisibility(list.size() == 0 ? 0 : 8);
        ((ManageCategory) getActivity()).adapter.notifyDataSetChanged();
    }

    @Override // com.ktwapps.walletmanager.Adapter.ManageCategoryAdapter.OnItemClickListener
    public void onItemClick(View view, final int position, int type) {
        final int id = this.adapter.getList().get(position).getId();
        if (type == -13) {
            Helper.showDialog(getActivity(), "", getResources().getString(R.string.category_expense_delete_message), getResources().getString(R.string.delete_positive), getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.CategoryExpense$$ExternalSyntheticLambda2
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    CategoryExpense.this.m178lambda$onItemClick$3$comktwappswalletmanagerCategoryExpense(id, position, dialogInterface, i);
                }
            });
        } else if (type == 20) {
            Intent intent = new Intent(getActivity(), CreateCategory.class);
            intent.putExtra("categoryId", id);
            intent.putExtra(JamXmlElements.TYPE, -10);
            startActivity(intent);
            this.activity.overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
        } else {
            Intent intent2 = new Intent(getActivity(), ManageSubcategory.class);
            intent2.putExtra("categoryId", id);
            intent2.putExtra(JamXmlElements.TYPE, -10);
            intent2.putExtra(TypedValues.Custom.S_COLOR, this.adapter.getList().get(position).getColor());
            intent2.putExtra("name", this.adapter.getList().get(position).getName(getActivity()));
            startActivity(intent2);
            this.activity.overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.ktwapps.walletmanager.CategoryExpense$1  reason: invalid class name */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 implements Runnable {
        final /* synthetic */ int val$id;
        final /* synthetic */ int val$position;

        AnonymousClass1(final int val$id, final int val$position) {
            this.val$id = val$id;
            this.val$position = val$position;
        }

        @Override // java.lang.Runnable
        public void run() {
            AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(CategoryExpense.this.getActivity());
            appDatabaseObject.templateDaoObject().deleteTemplateByCategoryId(this.val$id);
            appDatabaseObject.categoryDaoObject().updateStatus(this.val$id, 1);
            Activity activity = CategoryExpense.this.activity;
            final int i = this.val$position;
            activity.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CategoryExpense$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    CategoryExpense.AnonymousClass1.this.m182lambda$run$0$comktwappswalletmanagerCategoryExpense$1(i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$run$0$com-ktwapps-walletmanager-CategoryExpense$1  reason: not valid java name */
        public /* synthetic */ void m182lambda$run$0$comktwappswalletmanagerCategoryExpense$1(int i) {
            CategoryExpense.this.adapter.removeItem(i);
            CategoryExpense.this.emptyWrapper.setVisibility(CategoryExpense.this.adapter.getList().size() == 0 ? 0 : 8);
            ((ManageCategory) CategoryExpense.this.getActivity()).adapter.notifyDataSetChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onItemClick$3$com-ktwapps-walletmanager-CategoryExpense  reason: not valid java name */
    public /* synthetic */ void m178lambda$onItemClick$3$comktwappswalletmanagerCategoryExpense(int i, int i2, DialogInterface dialogInterface, int i3) {
        Executors.newSingleThreadExecutor().execute(new AnonymousClass1(i, i2));
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
}
