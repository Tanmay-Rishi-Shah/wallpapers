package com.expance.merapaisa;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.expance.merapaisa.Adapter.CategoryPageAdapter;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

/* loaded from: classes3.dex */
public class ManageCategory extends AppCompatActivity {
    CategoryPageAdapter adapter;
    TabLayout tabLayout;
    ViewPager viewPager;

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
        setContentView(R.layout.activity_manage_category);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
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

    private void setUpLayout() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.manage_category));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.viewPager = (ViewPager) findViewById(R.id.viewPager);
        this.tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        CategoryPageAdapter categoryPageAdapter = new CategoryPageAdapter(this, getSupportFragmentManager());
        this.adapter = categoryPageAdapter;
        this.viewPager.setAdapter(categoryPageAdapter);
        this.tabLayout.setupWithViewPager(this.viewPager);
        int intExtra = getIntent().getIntExtra(JamXmlElements.TYPE, 0);
        if (intExtra == 0 || intExtra != 2) {
            return;
        }
        this.viewPager.setCurrentItem(1);
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_action_create) {
            Intent intent = new Intent(getApplicationContext(), CreateCategory.class);
            if (this.viewPager.getCurrentItem() == 0) {
                intent.putExtra(JamXmlElements.TYPE, 12);
            } else {
                intent.putExtra(JamXmlElements.TYPE, 10);
            }
            startActivity(intent);
            overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
            return true;
        }
        return false;
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create, menu);
        for (int i = 0; i < menu.size(); i++) {
            Drawable icon = menu.getItem(i).getIcon();
            if (icon != null) {
                icon.mutate();
                icon.setColorFilter(Helper.getAttributeColor(this, R.attr.primaryDarkTextColor), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return true;
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
}
