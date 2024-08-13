package com.expance.merapaisa;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.Entity.SubcategoryEntity;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.concurrent.Executors;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

import vocsy.ads.CustomAdsListener;
import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class CreateSubcategory extends AppCompatActivity implements TextWatcher, View.OnClickListener {
    int categoryId;
    int id;
    boolean isComplete;
    EditText nameEditText;
    TextView saveLabel;

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
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
        setContentView(R.layout.activity_create_sub_category);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        this.isComplete = false;
        setUpLayout();
        populateData();
    }

    private void setUpLayout() {
        this.categoryId = getIntent().getIntExtra("categoryId", 0);
        this.id = getIntent().getIntExtra("id", 0);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.create_subcategory));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.nameEditText = (EditText) findViewById(R.id.nameEditText);
        this.saveLabel = (TextView) findViewById(R.id.saveLabel);
        this.nameEditText.addTextChangedListener(this);
        this.saveLabel.setOnClickListener(this);
        checkIsComplete();
    }

    private void checkIsComplete() {
        boolean z = this.nameEditText.getText().toString().trim().length() > 0;
        this.isComplete = z;
        this.saveLabel.setAlpha(z ? 1.0f : 0.35f);
    }

    private void populateData() {
        if (this.id != 0) {
            Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateSubcategory$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    CreateSubcategory.this.m198x57b6c42a();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$populateData$1$com-ktwapps-walletmanager-CreateSubcategory  reason: not valid java name */
    public /* synthetic */ void m198x57b6c42a() {
        final SubcategoryEntity subcategoryById = AppDatabaseObject.getInstance(getApplicationContext()).categoryDaoObject().getSubcategoryById(this.id);
        runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateSubcategory$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                CreateSubcategory.this.m197xbd1601a9(subcategoryById);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$populateData$0$com-ktwapps-walletmanager-CreateSubcategory  reason: not valid java name */
    public /* synthetic */ void m197xbd1601a9(SubcategoryEntity subcategoryEntity) {
        this.nameEditText.setText(subcategoryEntity.getName());
        checkIsComplete();
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
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateSubcategory$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                CreateSubcategory.this.m196x64eff9d2();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$createCategory$3$com-ktwapps-walletmanager-CreateSubcategory  reason: not valid java name */
    public /* synthetic */ void m196x64eff9d2() {
        AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(getApplicationContext());
        String trim = this.nameEditText.getText().toString().trim();
        int insertSubcategory = (int) appDatabaseObject.categoryDaoObject().insertSubcategory(new SubcategoryEntity(this.categoryId, trim, appDatabaseObject.categoryDaoObject().getSubcategoryLastOrdering(this.categoryId)));
        if (insertSubcategory != 0) {
            Intent intent = new Intent();
            intent.putExtra("name", trim);
            intent.putExtra("id", insertSubcategory);
            intent.putExtra(JamXmlElements.TYPE, 22);
            setResult(-1, intent);
        }
        runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateSubcategory$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                CreateSubcategory.this.m195xca4f3751();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$createCategory$2$com-ktwapps-walletmanager-CreateSubcategory  reason: not valid java name */
    public /* synthetic */ void m195xca4f3751() {
        finish();
        overridePendingTransition(R.anim.scale_in, R.anim.bottom_to_top);
    }

    private void updateCategory() {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.CreateSubcategory$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                CreateSubcategory.this.m200x581d1ae1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$updateCategory$5$com-ktwapps-walletmanager-CreateSubcategory  reason: not valid java name */
    public /* synthetic */ void m200x581d1ae1() {
        AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(getApplicationContext());
        SubcategoryEntity subcategoryById = appDatabaseObject.categoryDaoObject().getSubcategoryById(this.id);
        final String trim = this.nameEditText.getText().toString().trim();
        if (trim.length() != 0) {
            subcategoryById.setName(trim);
        }
        appDatabaseObject.categoryDaoObject().updateSubcategory(subcategoryById);
        runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.CreateSubcategory$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                CreateSubcategory.this.m199xbd7c5860(trim);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$updateCategory$4$com-ktwapps-walletmanager-CreateSubcategory  reason: not valid java name */
    public /* synthetic */ void m199xbd7c5860(String str) {
        Intent intent = new Intent();
        intent.putExtra("name", str);
        intent.putExtra("id", this.id);
        intent.putExtra(JamXmlElements.TYPE, 21);
        intent.putExtra("position", getIntent().getIntExtra("position", -1));
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
        if (view.getId() == R.id.saveLabel && this.isComplete) {
            GoogleAds.getInstance().showCounterInterstitialAd(CreateSubcategory.this, new CustomAdsListener() {
                @Override
                public void onFinish() {
                    if (id != 0) {
                        updateCategory();
                    } else {
                        createCategory();
                    }
                }
            });

        }
    }
}
