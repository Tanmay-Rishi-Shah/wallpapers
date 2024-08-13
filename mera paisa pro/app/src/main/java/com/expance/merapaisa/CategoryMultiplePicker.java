package com.expance.merapaisa;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.expance.merapaisa.Adapter.CategoryMultiplePickerAdapter;
import com.expance.merapaisa.Database.ViewModel.CategoryViewModel;
import com.expance.merapaisa.Database.ViewModel.ModelFactory;
import com.expance.merapaisa.Model.Category;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class CategoryMultiplePicker extends AppCompatActivity implements CategoryMultiplePickerAdapter.OnItemClickListener, View.OnClickListener {
    CategoryMultiplePickerAdapter adapter;
    int categoryId;
    ArrayList<Integer> categoryIds;
    CategoryViewModel categoryViewModel;
    ConstraintLayout emptyWrapper;
    RecyclerView recyclerView;
    TextView saveLabel;
    int type;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("categoryId", this.categoryId);
        if (this.categoryId == 0) {
            outState.putIntegerArrayList("categoryIds", new ArrayList<>());
        } else {
            outState.putIntegerArrayList("categoryIds", this.adapter.getCategoryIds());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStart() {
        super.onStart();
        AppEngine appEngine = (AppEngine) getApplication();
        if (appEngine.wasInBackground()) {
            appEngine.setWasInBackground(false);
            if (SharePreferenceHelper.checkPasscode(getApplicationContext())) {
                Intent intent = new Intent(getApplicationContext(), Passcode.class);
                intent.addFlags(65536);
                intent.addFlags(268435456);
                startActivity(intent);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTheme(SharePreferenceHelper.getThemeMode(this) == 1 ? R.style.AppThemeNight : R.style.AppTheme);
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (SharePreferenceHelper.getThemeMode(this) == 1) {
//                getWindow().getDecorView().setSystemUiVisibility(0);
//            } else {
//                getWindow().getDecorView().setSystemUiVisibility(8192);
//            }
//        }
//        if (Build.VERSION.SDK_INT >= 26) {
//            if (SharePreferenceHelper.getThemeMode(this) == 1) {
//                getWindow().getDecorView().setSystemUiVisibility(0);
//            } else {
//                getWindow().getDecorView().setSystemUiVisibility(8208);
//            }
//            getWindow().setNavigationBarColor(Color.parseColor(SharePreferenceHelper.getThemeMode(this) == 1 ? "#1F1F1F" : "#F8F8F8"));
//        } else {
//            getWindow().setNavigationBarColor(Color.parseColor("#000000"));
//        }
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        setContentView(R.layout.activity_category_multiple_picker);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        this.type = getIntent().getIntExtra(JamXmlElements.TYPE, 0);
        this.categoryId = getIntent().getIntExtra("id", -1);
        this.categoryIds = getIntent().getIntegerArrayListExtra("ids");
        if (savedInstanceState != null) {
            this.categoryId = savedInstanceState.getInt("categoryId");
            this.categoryIds = savedInstanceState.getIntegerArrayList("categoryIds");
        }
        setUpLayout();
    }

    private void setUpLayout() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.select_category));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.saveLabel = (TextView) findViewById(R.id.saveLabel);
        this.emptyWrapper = (ConstraintLayout) findViewById(R.id.emptyWrapper);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CategoryMultiplePickerAdapter categoryMultiplePickerAdapter = new CategoryMultiplePickerAdapter(this);
        this.adapter = categoryMultiplePickerAdapter;
        this.recyclerView.setAdapter(categoryMultiplePickerAdapter);
        populateDate();
        this.adapter.setListener(this);
        this.saveLabel.setOnClickListener(this);
        if (this.categoryId == 0 || this.categoryIds.size() > 0) {
            this.saveLabel.setAlpha(1.0f);
        } else {
            this.saveLabel.setAlpha(0.35f);
        }
    }

    public void populateDate() {
        CategoryViewModel categoryViewModel = (CategoryViewModel) new ViewModelProvider(this, new ModelFactory(getApplication(), this.type)).get(CategoryViewModel.class);
        this.categoryViewModel = categoryViewModel;
        categoryViewModel.getCategories().observe(this, new Observer<List<Category>>() { // from class: com.ktwapps.walletmanager.CategoryMultiplePicker.1
            @Override // androidx.lifecycle.Observer
            public void onChanged(List<Category> categories) {
                ArrayList<Category> arrayList = new ArrayList();
                if (categories != null) {
                    arrayList = new ArrayList(categories);
                }
                ArrayList arrayList2 = new ArrayList();
                for (Category category : arrayList) {
                    arrayList2.add(Integer.valueOf(category.getId()));
                }
                Iterator<Integer> it = CategoryMultiplePicker.this.categoryIds.iterator();
                while (it.hasNext()) {
                    if (!arrayList2.contains(it.next())) {
                        it.remove();
                    }
                }
                arrayList.add(0, new Category(0, 0, CategoryMultiplePicker.this.getResources().getString(R.string.all_category), "#66757f", 0, 0, "", "", ""));
                CategoryMultiplePicker.this.adapter.setList(arrayList);
                CategoryMultiplePicker.this.emptyWrapper.setVisibility(arrayList.size() != 0 ? 8 : 0);
                if (CategoryMultiplePicker.this.categoryIds.size() > 0) {
                    CategoryMultiplePicker.this.adapter.setCategoryIds(CategoryMultiplePicker.this.categoryIds);
                } else if (CategoryMultiplePicker.this.categoryId == 0) {
                    CategoryMultiplePicker.this.adapter.selectOrDeselectAll();
                }
                CategoryMultiplePicker.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CategoryMultiplePicker.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (CategoryMultiplePicker.this.categoryId == 0 || CategoryMultiplePicker.this.adapter.getCategoryIds().size() > 0) {
                            CategoryMultiplePicker.this.saveLabel.setAlpha(1.0f);
                        } else {
                            CategoryMultiplePicker.this.saveLabel.setAlpha(0.35f);
                        }
                    }
                });
            }
        });
    }

    @Override // androidx.appcompat.app.AppCompatActivity
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(R.anim.scale_in, R.anim.right_to_left);
        return true;
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.scale_in, R.anim.right_to_left);
    }

    @Override // com.ktwapps.walletmanager.Adapter.CategoryMultiplePickerAdapter.OnItemClickListener
    public void onItemClick(View view, int position) {
        if (position == 0) {
            this.categoryId = this.adapter.selectOrDeselectAll() ? 0 : -1;
        } else {
            if (this.categoryId == 0) {
                this.adapter.selectOrDeselect(0);
                this.categoryId = -1;
            }
            this.adapter.selectOrDeselect(this.adapter.list.get(position).getId());
        }
        if (this.categoryId == 0 || this.adapter.getCategoryIds().size() > 0) {
            this.saveLabel.setAlpha(1.0f);
        } else {
            this.saveLabel.setAlpha(0.35f);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (this.categoryId == 0 || this.adapter.getCategoryIds().size() > 0) {
            Intent intent = new Intent();
            int i = 0;
            if (this.categoryId == 0) {
                intent.putIntegerArrayListExtra("ids", new ArrayList<>());
                intent.putExtra("name", getResources().getString(R.string.all_category));
                intent.putExtra("id", 0);
            } else {
                String[] strArr = new String[this.adapter.getCategoryIds().size()];
                for (Category category : this.adapter.list) {
                    if (this.adapter.getCategoryIds().contains(Integer.valueOf(category.getId()))) {
                        strArr[i] = category.getName(getApplicationContext());
                        i++;
                    }
                }
                intent.putIntegerArrayListExtra("ids", this.adapter.getCategoryIds());
                intent.putExtra("name", TextUtils.join(", ", strArr));
                intent.putExtra("id", -1);
            }
            setResult(-1, intent);
            finish();
            overridePendingTransition(R.anim.scale_in, R.anim.right_to_left);
        }
    }
}
