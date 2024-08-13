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
public class CategoryIncome extends Fragment implements ManageCategoryAdapter.OnItemClickListener {
    Activity activity;
    ManageCategoryAdapter adapter;
    ConstraintLayout emptyWrapper;
    RecyclerView recyclerView;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_category_income, container, false);
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

    private void populateData() {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CategoryIncome$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                CategoryIncome.this.m187lambda$populateData$1$comktwappswalletmanagerCategoryIncome();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$populateData$1$com-ktwapps-walletmanager-CategoryIncome  reason: not valid java name */
    public /* synthetic */ void m187lambda$populateData$1$comktwappswalletmanagerCategoryIncome() {
        Category[] category = AppDatabaseObject.getInstance(getActivity()).categoryDaoObject().getCategory(1, 0, SharePreferenceHelper.getAccountId(this.activity));
        final ArrayList arrayList = new ArrayList();
        Collections.addAll(arrayList, category);
        this.activity.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CategoryIncome$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                CategoryIncome.this.m186lambda$populateData$0$comktwappswalletmanagerCategoryIncome(arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$populateData$0$com-ktwapps-walletmanager-CategoryIncome  reason: not valid java name */
    public /* synthetic */ void m186lambda$populateData$0$comktwappswalletmanagerCategoryIncome(List list) {
        this.adapter.setList(list);
        this.emptyWrapper.setVisibility(list.size() == 0 ? 0 : 8);
        ((ManageCategory) getActivity()).adapter.notifyDataSetChanged();
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
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CategoryIncome$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                CategoryIncome.this.m185lambda$onPause$2$comktwappswalletmanagerCategoryIncome();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onPause$2$com-ktwapps-walletmanager-CategoryIncome  reason: not valid java name */
    public /* synthetic */ void m185lambda$onPause$2$comktwappswalletmanagerCategoryIncome() {
        AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(getActivity());
        for (int i = 0; i < this.adapter.list.size(); i++) {
            appDatabaseObject.categoryDaoObject().updateCategoryOrdering(i, this.adapter.list.get(i).getId());
        }
    }

    @Override // com.ktwapps.walletmanager.Adapter.ManageCategoryAdapter.OnItemClickListener
    public void onItemClick(View view, final int position, int type) {
        final int id = this.adapter.getList().get(position).getId();
        if (type == -13) {
            Helper.showDialog(getActivity(), "", getResources().getString(R.string.category_income_delete_message), getResources().getString(R.string.delete_positive), getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.CategoryIncome$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    CategoryIncome.this.m184lambda$onItemClick$3$comktwappswalletmanagerCategoryIncome(id, position, dialogInterface, i);
                }
            });
        } else if (type == 20) {
            Intent intent = new Intent(getActivity(), CreateCategory.class);
            intent.putExtra("categoryId", id);
            intent.putExtra(JamXmlElements.TYPE, -12);
            startActivity(intent);
            this.activity.overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
        } else {
            Intent intent2 = new Intent(getActivity(), ManageSubcategory.class);
            intent2.putExtra("categoryId", id);
            intent2.putExtra(JamXmlElements.TYPE, -12);
            intent2.putExtra(TypedValues.Custom.S_COLOR, this.adapter.getList().get(position).getColor());
            intent2.putExtra("name", this.adapter.getList().get(position).getName(getActivity()));
            startActivity(intent2);
            this.activity.overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.ktwapps.walletmanager.CategoryIncome$1  reason: invalid class name */
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
            AppDatabaseObject.getInstance(CategoryIncome.this.getActivity()).categoryDaoObject().updateStatus(this.val$id, 1);
            Activity activity = CategoryIncome.this.activity;
            final int i = this.val$position;
            activity.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CategoryIncome$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    CategoryIncome.AnonymousClass1.this.m188lambda$run$0$comktwappswalletmanagerCategoryIncome$1(i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$run$0$com-ktwapps-walletmanager-CategoryIncome$1  reason: not valid java name */
        public /* synthetic */ void m188lambda$run$0$comktwappswalletmanagerCategoryIncome$1(int i) {
            CategoryIncome.this.adapter.removeItem(i);
            CategoryIncome.this.emptyWrapper.setVisibility(CategoryIncome.this.adapter.getList().size() == 0 ? 0 : 8);
            ((ManageCategory) CategoryIncome.this.getActivity()).adapter.notifyDataSetChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onItemClick$3$com-ktwapps-walletmanager-CategoryIncome  reason: not valid java name */
    public /* synthetic */ void m184lambda$onItemClick$3$comktwappswalletmanagerCategoryIncome(int i, int i2, DialogInterface dialogInterface, int i3) {
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
