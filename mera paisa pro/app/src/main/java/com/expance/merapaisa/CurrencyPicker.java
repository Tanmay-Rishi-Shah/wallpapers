package com.expance.merapaisa;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.expance.merapaisa.Adapter.CurrencyPickerAdapter;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Utility.Helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

/* loaded from: classes3.dex */
public class CurrencyPicker extends AppCompatActivity implements CurrencyPickerAdapter.OnItemClickListener, TextView.OnEditorActionListener {
    CurrencyPickerAdapter adapter;
    ArrayList<String> arrayList;
    boolean isSearch;
    RecyclerView recyclerView;
    EditText searchEditText;

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
        setContentView(R.layout.activity_currency_picker);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
//        if (!getResources().getBoolean(R.bool.isTablet)) {
//            setRequestedOrientation(1);
//        }
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.currency_picker);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        EditText editText = (EditText) findViewById(R.id.searchEditText);
        this.searchEditText = editText;
        editText.setVisibility(8);
        this.searchEditText.setOnEditorActionListener(this);
        String stringExtra = getIntent().getStringExtra(FirebaseAnalytics.Param.CURRENCY);
        if (getIntent().hasExtra("currencyList")) {
            this.arrayList = getIntent().getStringArrayListExtra("currencyList");
            stringExtra = DataHelper.getCurrencyList().get(DataHelper.getCurrencyCode().indexOf(stringExtra));
        } else {
            this.arrayList = DataHelper.getCurrencyList();
        }
        CurrencyPickerAdapter currencyPickerAdapter = new CurrencyPickerAdapter(this);
        this.adapter = currencyPickerAdapter;
        this.recyclerView.setAdapter(currencyPickerAdapter);
        this.adapter.setList(this.arrayList, stringExtra);
        this.adapter.setListener(this);
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        if (this.isSearch) {
            return false;
        }
        getMenuInflater().inflate(R.menu.menu_main, menu);
        for (int i = 0; i < menu.size(); i++) {
            Drawable icon = menu.getItem(i).getIcon();
            if (icon != null) {
                icon.mutate();
                icon.setColorFilter(Helper.getAttributeColor(this, R.attr.primaryDarkTextColor), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return true;
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_action_search) {
            this.isSearch = true;
            invalidateOptionsMenu();
            this.searchEditText.setVisibility(0);
            this.searchEditText.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService("input_method");
            if (inputMethodManager != null) {
                inputMethodManager.showSoftInput(this.searchEditText, 1);
            }
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
            return true;
        }
        return false;
    }

    @Override // androidx.appcompat.app.AppCompatActivity
    public boolean onSupportNavigateUp() {
        if (this.isSearch) {
            this.isSearch = false;
            invalidateOptionsMenu();
            this.searchEditText.setVisibility(8);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(true);
            }
            clearSearch();
            InputMethodManager inputMethodManager = (InputMethodManager) this.searchEditText.getContext().getSystemService("input_method");
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(this.searchEditText.getWindowToken(), 0);
            }
        } else {
            finish();
            overridePendingTransition(R.anim.scale_in, R.anim.right_to_left);
        }
        return true;
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (this.isSearch) {
            this.isSearch = false;
            invalidateOptionsMenu();
            this.searchEditText.setVisibility(8);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(true);
            }
            clearSearch();
            InputMethodManager inputMethodManager = (InputMethodManager) this.searchEditText.getContext().getSystemService("input_method");
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(this.searchEditText.getWindowToken(), 0);
                return;
            }
            return;
        }
        super.onBackPressed();
        overridePendingTransition(R.anim.scale_in, R.anim.right_to_left);
    }

    @Override // android.widget.TextView.OnEditorActionListener
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 3) {
            search();
            InputMethodManager inputMethodManager = (InputMethodManager) this.searchEditText.getContext().getSystemService("input_method");
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(this.searchEditText.getWindowToken(), 0);
                return true;
            }
            return true;
        }
        return false;
    }

    private void search() {
        String trim = this.searchEditText.getText().toString().trim();
        if (trim.length() > 0) {
            ArrayList arrayList = new ArrayList();
            Pattern compile = Pattern.compile(trim, 2);
            Iterator<String> it = this.arrayList.iterator();
            while (it.hasNext()) {
                String next = it.next();
                if (compile.matcher(next).find()) {
                    arrayList.add(next);
                }
            }
            this.adapter.setList(arrayList);
            return;
        }
        clearSearch();
    }

    private void clearSearch() {
        this.adapter.setList(this.arrayList);
    }

    @Override // com.ktwapps.walletmanager.Adapter.CurrencyPickerAdapter.OnItemClickListener
    public void onItemClick(View view, int position) {
        int indexOf = DataHelper.getCurrencyList().indexOf(this.adapter.getList().get(position));
        Intent intent = new Intent();
        intent.putExtra(FirebaseAnalytics.Param.INDEX, indexOf);
        setResult(-1, intent);
        finish();
        overridePendingTransition(R.anim.scale_in, R.anim.right_to_left);
    }
}
