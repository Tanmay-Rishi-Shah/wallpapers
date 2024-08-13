package com.expance.merapaisa;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.expance.merapaisa.Adapter.TemplatePickerAdapter;
import com.expance.merapaisa.Database.ViewModel.TemplateViewModel;
import com.expance.merapaisa.Model.Template;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.util.List;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class TemplatePicker extends AppCompatActivity implements TemplatePickerAdapter.OnItemClickListener, View.OnClickListener {
    TemplatePickerAdapter adapter;
    ConstraintLayout emptyWrapper;
    RecyclerView recyclerView;
    TemplateViewModel templateViewModel;

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
        setContentView(R.layout.activity_template_picker);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        setUpLayout();
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

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.scale_in, R.anim.bottom_to_top);
    }

    @Override // androidx.appcompat.app.AppCompatActivity
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(R.anim.scale_in, R.anim.bottom_to_top);
        return true;
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_action_setting) {
            startActivity(new Intent(this, ManageTemplate.class));
            overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
            return true;
        }
        return false;
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_wallet, menu);
        for (int i = 0; i < menu.size(); i++) {
            Drawable icon = menu.getItem(i).getIcon();
            if (icon != null) {
                icon.mutate();
                icon.setColorFilter(Helper.getAttributeColor(this, R.attr.primaryDarkTextColor), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return true;
    }

    private void setUpLayout() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.template));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.emptyWrapper = (ConstraintLayout) findViewById(R.id.emptyWrapper);
        this.adapter = new TemplatePickerAdapter(this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setAdapter(this.adapter);
        this.adapter.setListener(this);
        ((FloatingActionButton) findViewById(R.id.fab)).setOnClickListener(this);
        TemplateViewModel templateViewModel = (TemplateViewModel) new ViewModelProvider(this).get(TemplateViewModel.class);
        this.templateViewModel = templateViewModel;
        templateViewModel.getTemplate().observe(this, new Observer<List<Template>>() { // from class: com.ktwapps.walletmanager.TemplatePicker.1
            @Override // androidx.lifecycle.Observer
            public void onChanged(List<Template> templates) {
                TemplatePicker.this.adapter.setList(templates);
                TemplatePicker.this.emptyWrapper.setVisibility((templates == null || templates.size() <= 0) ? 0 : 8);
            }
        });
    }

    @Override // com.ktwapps.walletmanager.Adapter.TemplatePickerAdapter.OnItemClickListener
    public void onItemClick(View view, int position) {
        Template template = this.adapter.getList().get(position);
        Intent intent = new Intent();
        intent.putExtra("note", template.getNote() == null ? "" : template.getNote());
        intent.putExtra("memo", template.getMemo() == null ? "" : template.getMemo());
        intent.putExtra("categoryId", template.getCategoryId());
        intent.putExtra("subcategoryId", template.getSubcategoryId());
        intent.putExtra("category", template.getCategory(this) != null ? template.getCategory(this) : "");
        intent.putExtra(JamXmlElements.TYPE, template.getType());
        intent.putExtra("walletId", template.getWalletId() == 0 ? -1 : template.getWalletId());
        intent.putExtra("amount", template.getAmount());
        setResult(-1, intent);
        finish();
        overridePendingTransition(R.anim.scale_in, R.anim.bottom_to_top);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.fab) {
            startActivity(new Intent(this, CreateTemplate.class));
            overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
        }
    }
}
