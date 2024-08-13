package com.expance.merapaisa;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import com.expance.merapaisa.Adapter.SpinnerColorAdapter;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.Entity.CategoryEntity;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.concurrent.Executors;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

import vocsy.ads.CustomAdsListener;
import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class CreateCategory extends AppCompatActivity implements TextWatcher, View.OnClickListener {
    SpinnerColorAdapter adapter;
    int categoryId;
    String categoryName;
    Spinner colorSpinner;
    private int icon;
    private ImageView iconView;
    boolean isComplete;
    EditText nameEditText;
    TextView saveLabel;
    int type;

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TypedValues.Custom.S_COLOR, DataHelper.getColorList().get(this.colorSpinner.getSelectedItemPosition()));
        outState.putString("categoryName", this.categoryName);
        outState.putInt("icon", this.icon);
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
        setContentView(R.layout.activity_create_category);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        this.isComplete = false;
        this.icon = 1;
        this.type = getIntent().getIntExtra(JamXmlElements.TYPE, 10);
        setUpLayout();
        populateData(savedInstanceState != null);
        if (savedInstanceState != null) {
            this.icon = savedInstanceState.getInt("icon");
            this.categoryName = savedInstanceState.getString("categoryName");
            this.iconView.setImageResource(DataHelper.getCategoryIcons().get(this.icon).intValue());
            this.colorSpinner.setSelection(this.adapter.getPosition(savedInstanceState.getString(TypedValues.Custom.S_COLOR)));
        }
    }

    private void setUpLayout() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            int i = this.type;
            if (i == -12 || i == 12) {
                getSupportActionBar().setTitle(getResources().getString(R.string.income_category));
            } else {
                getSupportActionBar().setTitle(getResources().getString(R.string.expense_category));
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.nameEditText = (EditText) findViewById(R.id.nameEditText);
        this.colorSpinner = (Spinner) findViewById(R.id.colorSpinner);
        this.iconView = (ImageView) findViewById(R.id.iconView);
        this.saveLabel = (TextView) findViewById(R.id.saveLabel);
        SpinnerColorAdapter spinnerColorAdapter = new SpinnerColorAdapter(this, R.layout.list_drop_down_color, R.id.label, DataHelper.getColorList());
        this.adapter = spinnerColorAdapter;
        this.colorSpinner.setAdapter((SpinnerAdapter) spinnerColorAdapter);
        this.nameEditText.addTextChangedListener(this);
        this.iconView.setOnClickListener(this);
        this.saveLabel.setOnClickListener(this);
        this.iconView.setImageResource(DataHelper.getCategoryIcons().get(this.icon).intValue());
        checkIsComplete();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkIsComplete() {
        boolean z = this.nameEditText.getText().toString().trim().length() > 0;
        this.isComplete = z;
        this.saveLabel.setAlpha(z ? 1.0f : 0.35f);
    }

    private void populateData(boolean isSavedInstanceState) {
        int i = this.type;
        if (i == -12 || i == -10) {
            this.categoryId = getIntent().getIntExtra("categoryId", 0);
            if (isSavedInstanceState) {
                return;
            }
            Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateCategory.1
                @Override // java.lang.Runnable
                public void run() {
                    final CategoryEntity categoryById = AppDatabaseObject.getInstance(CreateCategory.this.getApplicationContext()).categoryDaoObject().getCategoryById(CreateCategory.this.categoryId);
                    CreateCategory.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateCategory.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            CreateCategory.this.icon = categoryById.getIcon();
                            CreateCategory.this.categoryName = (categoryById.getName() == null || categoryById.getName().length() == 0) ? DataHelper.getDefaultCategory(CreateCategory.this.getApplicationContext(), categoryById.getDefaultCategory()) : categoryById.getName();
                            CreateCategory.this.iconView.setImageResource(DataHelper.getCategoryIcons().get(CreateCategory.this.icon).intValue());
                            CreateCategory.this.nameEditText.setText(CreateCategory.this.categoryName);
                            CreateCategory.this.colorSpinner.setSelection(CreateCategory.this.adapter.getPosition(categoryById.getColor()));
                            CreateCategory.this.checkIsComplete();
                        }
                    });
                }
            });
        }
    }

    @Override // androidx.appcompat.app.AppCompatActivity
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(R.anim.scale_in, R.anim.bottom_to_top);
        return true;
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.scale_in, R.anim.bottom_to_top);
    }

    private void createCategory() {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateCategory$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                CreateCategory.this.m191lambda$createCategory$1$comktwappswalletmanagerCreateCategory();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$createCategory$1$com-ktwapps-walletmanager-CreateCategory  reason: not valid java name */
    public /* synthetic */ void m191lambda$createCategory$1$comktwappswalletmanagerCreateCategory() {
        AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(getApplicationContext());
        String trim = this.nameEditText.getText().toString().trim();
        String str = DataHelper.getColorList().get(this.colorSpinner.getSelectedItemPosition());
        int i = this.type == 12 ? 1 : 2;
        int accountId = SharePreferenceHelper.getAccountId(getApplicationContext());
        appDatabaseObject.categoryDaoObject().insertCategory(new CategoryEntity(trim, str, this.icon, i, 0, appDatabaseObject.categoryDaoObject().getCategoryLastOrdering(accountId, i), accountId, 0));
        runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateCategory$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                CreateCategory.this.m190lambda$createCategory$0$comktwappswalletmanagerCreateCategory();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$createCategory$0$com-ktwapps-walletmanager-CreateCategory  reason: not valid java name */
    public /* synthetic */ void m190lambda$createCategory$0$comktwappswalletmanagerCreateCategory() {
        finish();
        overridePendingTransition(R.anim.scale_in, R.anim.bottom_to_top);
    }

    private void updateCategory() {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateCategory$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                CreateCategory.this.m193lambda$updateCategory$3$comktwappswalletmanagerCreateCategory();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$updateCategory$3$com-ktwapps-walletmanager-CreateCategory  reason: not valid java name */
    public /* synthetic */ void m193lambda$updateCategory$3$comktwappswalletmanagerCreateCategory() {
        AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(getApplicationContext());
        CategoryEntity categoryById = appDatabaseObject.categoryDaoObject().getCategoryById(this.categoryId);
        final String trim = this.nameEditText.getText().toString().trim();
        final String str = DataHelper.getColorList().get(this.colorSpinner.getSelectedItemPosition());
        categoryById.setColor(str);
        categoryById.setIcon(this.icon);
        if (!trim.equals(this.categoryName) && trim.length() != 0) {
            categoryById.setName(trim);
            categoryById.setDefaultCategory(0);
        }
        appDatabaseObject.categoryDaoObject().updateCategory(categoryById);
        runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateCategory$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                CreateCategory.this.m192lambda$updateCategory$2$comktwappswalletmanagerCreateCategory(trim, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$updateCategory$2$com-ktwapps-walletmanager-CreateCategory  reason: not valid java name */
    public /* synthetic */ void m192lambda$updateCategory$2$comktwappswalletmanagerCreateCategory(String str, String str2) {
        Intent intent = new Intent();
        intent.putExtra("name", str);
        intent.putExtra(TypedValues.Custom.S_COLOR, str2);
        setResult(-1, intent);
        finish();
        overridePendingTransition(R.anim.scale_in, R.anim.bottom_to_top);
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
        checkIsComplete();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.iconView) {
            Intent intent = new Intent(getApplicationContext(), CategoryIconPicker.class);
            intent.putExtra(TypedValues.Custom.S_COLOR, DataHelper.getColorList().get(this.colorSpinner.getSelectedItemPosition()));
            intent.putExtra("icon", this.icon);
            startActivityForResult(intent, 9);
            overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
        } else if (view.getId() == R.id.saveLabel && this.isComplete) {
            GoogleAds.getInstance().showCounterInterstitialAd(CreateCategory.this, new CustomAdsListener() {
                @Override
                public void onFinish() {
                    int i = type;
                    if (i == -12 || i == -10) {
                        updateCategory();
                    } else {
                        createCategory();
                    }
                }
            });

        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 9) {
            this.icon = data.getIntExtra("icon", 0);
            this.iconView.setImageResource(DataHelper.getCategoryIcons().get(this.icon).intValue());
        }
    }
}
