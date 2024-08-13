package com.expance.merapaisa;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.actions.SearchIntents;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.expance.merapaisa.Adapter.SearchAdapter;
import com.expance.merapaisa.Adapter.SearchCategoryAdapter;
import com.expance.merapaisa.Adapter.SearchWalletAdapter;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.ViewModel.WalletViewModel;
import com.expance.merapaisa.Model.Category;
import com.expance.merapaisa.Model.SearchTrans;
import com.expance.merapaisa.Model.Wallets;
import com.expance.merapaisa.Utility.DateHelper;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import com.expance.merapaisa.Widget.BottomDateDialog;

import org.apache.http.cookie.ClientCookie;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class Search extends AppCompatActivity implements TextView.OnEditorActionListener, View.OnClickListener, BottomDateDialog.OnItemClickListener, SearchAdapter.OnItemClickListener {
    SearchAdapter adapter;
    List<Category> categoriesList;
    SearchCategoryAdapter categoryAdapter;
    ArrayList<Integer> categoryIds;
    ImageView categoryImageView;
    TextView categoryLabel;
    ConstraintLayout categoryWrapper;
    ImageView dateImageView;
    TextView dateLabel;
    ConstraintLayout dateWrapper;
    TextView emptyLabel;
    ConstraintLayout emptyWrapper;
    long endDate;
    RecyclerView recyclerView;
    EditText searchEditText;
    long startDate;
    TextView summaryLabel;
    SearchWalletAdapter walletAdapter;
    ArrayList<Integer> walletIds;
    ImageView walletImageView;
    TextView walletLabel;
    WalletViewModel walletViewModel;
    ConstraintLayout walletWrapper;
    List<Wallets> walletsList;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList("walletIds", this.walletIds);
        outState.putIntegerArrayList("categoryIds", this.categoryIds);
        outState.putLong("startDate", this.startDate);
        outState.putLong("endDate", this.endDate);
        outState.putString(SearchIntents.EXTRA_QUERY, this.searchEditText.getText().toString().trim());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
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
        setContentView(R.layout.activity_search);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        SearchAdapter searchAdapter = new SearchAdapter(this);
        this.adapter = searchAdapter;
        searchAdapter.setListener(this);
        this.summaryLabel = (TextView) findViewById(R.id.summaryLabel);
        this.emptyWrapper = (ConstraintLayout) findViewById(R.id.emptyWrapper);
        this.emptyLabel = (TextView) findViewById(R.id.emptyLabel);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setAdapter(this.adapter);
        setUpLayout();
        if (savedInstanceState != null) {
            this.walletIds = savedInstanceState.getIntegerArrayList("walletIds");
            this.categoryIds = savedInstanceState.getIntegerArrayList("categoryIds");
            this.startDate = savedInstanceState.getLong("startDate");
            this.endDate = savedInstanceState.getLong("endDate");
            if (this.walletIds.size() > 0) {
                this.walletWrapper.setOnClickListener(this);
                this.walletLabel.setText(this.walletIds.size() > 1 ? getResources().getString(R.string.total_wallets, Integer.valueOf(this.walletIds.size())) : getResources().getString(R.string.total_wallet, Integer.valueOf(this.walletIds.size())));
                this.walletLabel.setTextColor(Color.parseColor("#FFFFFF"));
                if (Build.VERSION.SDK_INT >= 29) {
                    this.walletWrapper.getBackground().setColorFilter(new BlendModeColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue), BlendMode.SRC_OVER));
                } else {
                    this.walletWrapper.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue), PorterDuff.Mode.SRC_OVER);
                }
                this.walletImageView.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
                this.walletImageView.setImageResource(R.drawable.search_clear);
            }
            if (this.categoryIds.size() > 0) {
                this.categoryImageView.setOnClickListener(this);
                this.categoryLabel.setText(this.categoryIds.size() > 1 ? getResources().getString(R.string.total_categories, Integer.valueOf(this.categoryIds.size())) : getResources().getString(R.string.total_category, Integer.valueOf(this.categoryIds.size())));
                this.categoryLabel.setTextColor(Color.parseColor("#FFFFFF"));
                if (Build.VERSION.SDK_INT >= 29) {
                    this.categoryWrapper.getBackground().setColorFilter(new BlendModeColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue), BlendMode.SRC_OVER));
                } else {
                    this.categoryWrapper.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue), PorterDuff.Mode.SRC_OVER);
                }
                this.categoryImageView.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
                this.categoryImageView.setImageResource(R.drawable.search_clear);
            }
            if (this.startDate > 0 && this.endDate > 0) {
                String bestDateTimePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "dd MMM");
                java.util.Calendar calendar = java.util.Calendar.getInstance();
                calendar.setTimeInMillis(this.startDate);
                String format = new SimpleDateFormat(bestDateTimePattern, Locale.getDefault()).format(calendar.getTime());
                if (!DateHelper.isSameDay(this.startDate, this.endDate)) {
                    calendar.setTimeInMillis(this.endDate);
                    format = format + " - " + new SimpleDateFormat(bestDateTimePattern, Locale.getDefault()).format(calendar.getTime());
                }
                this.dateLabel.setText(format);
                this.dateLabel.setTextColor(Color.parseColor("#FFFFFF"));
                if (Build.VERSION.SDK_INT >= 29) {
                    this.dateWrapper.getBackground().setColorFilter(new BlendModeColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue), BlendMode.SRC_OVER));
                } else {
                    this.dateWrapper.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue), PorterDuff.Mode.SRC_OVER);
                }
                this.dateImageView.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
                this.dateImageView.setImageResource(R.drawable.search_clear);
                this.dateImageView.setOnClickListener(this);
            }
            search(savedInstanceState.getString(SearchIntents.EXTRA_QUERY));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
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
        this.searchEditText = (EditText) findViewById(R.id.searchEditText);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.search));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.dateImageView = (ImageView) findViewById(R.id.dateImageView);
        this.categoryImageView = (ImageView) findViewById(R.id.categoryImageView);
        this.walletImageView = (ImageView) findViewById(R.id.walletImageView);
        this.dateWrapper = (ConstraintLayout) findViewById(R.id.dateWrapper);
        this.categoryWrapper = (ConstraintLayout) findViewById(R.id.categoryWrapper);
        this.walletWrapper = (ConstraintLayout) findViewById(R.id.walletWrapper);
        this.dateLabel = (TextView) findViewById(R.id.dateLabel);
        this.categoryLabel = (TextView) findViewById(R.id.categoryLabel);
        this.walletLabel = (TextView) findViewById(R.id.walletLabel);
        this.searchEditText.setOnEditorActionListener(this);
        this.categoryWrapper.setOnClickListener(this);
        this.walletWrapper.setOnClickListener(this);
        this.dateWrapper.setOnClickListener(this);
        populateData();
    }

    private void populateData() {
        this.walletIds = new ArrayList<>();
        this.categoryIds = new ArrayList<>();
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.Search.1
            @Override // java.lang.Runnable
            public void run() {
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(Search.this.getApplicationContext());
                Search.this.categoriesList = appDatabaseObject.categoryDaoObject().getSearchCategory(0, SharePreferenceHelper.getAccountId(Search.this.getApplicationContext()));
            }
        });
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(14, 0);
        calendar.set(13, 0);
        calendar.set(12, 0);
        calendar.set(11, 0);
        calendar.add(5, 1);
        if (!SharePreferenceHelper.isFutureBalanceOn(getApplicationContext())) {
            calendar.set(1, 10000);
        }
        WalletViewModel walletViewModel = (WalletViewModel) new ViewModelProvider(this).get(WalletViewModel.class);
        this.walletViewModel = walletViewModel;
        walletViewModel.setDate(calendar.getTimeInMillis());
        this.walletViewModel.getWalletsList().observe(this, new Observer<List<Wallets>>() { // from class: com.ktwapps.walletmanager.Search.2
            @Override // androidx.lifecycle.Observer
            public void onChanged(List<Wallets> wallets) {
                Search.this.walletsList = wallets;
            }
        });
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

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        int i = R.style.AppThemeNight;
        if (id == R.id.categoryImageView) {
            this.categoryIds = new ArrayList<>();
            this.categoryLabel.setText(R.string.category);
            this.categoryLabel.setTextColor(Helper.getAttributeColor(this, R.attr.primaryTextColor));
            if (Build.VERSION.SDK_INT >= 29) {
                this.categoryWrapper.getBackground().setColorFilter(new BlendModeColorFilter(Helper.getAttributeColor(this, R.attr.progressBar), BlendMode.SRC_OVER));
            } else {
                this.categoryWrapper.getBackground().setColorFilter(Helper.getAttributeColor(this, R.attr.progressBar), PorterDuff.Mode.SRC_OVER);
            }
            this.categoryImageView.setColorFilter(Helper.getAttributeColor(this, R.attr.primaryTextColor), PorterDuff.Mode.SRC_IN);
            this.categoryImageView.setImageResource(R.drawable.account_caret);
            this.categoryImageView.setOnClickListener(null);
            search();
        } else if (id == R.id.categoryWrapper) {
            this.categoryAdapter = new SearchCategoryAdapter(this.categoriesList, this.categoryIds, this);
            if (SharePreferenceHelper.getThemeMode(this) != 1) {
                i = R.style.AppTheme;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, i));
            builder.setAdapter(this.categoryAdapter, null);
            builder.setTitle(R.string.category);
            builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.Search$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i2) {
                    Search.this.m228lambda$onClick$0$comktwappswalletmanagerSearch(dialogInterface, i2);
                }
            });
            builder.create();
            builder.show();
        } else if (id == R.id.dateImageView) {
            this.startDate = 0L;
            this.endDate = 0L;
            this.dateLabel.setText(R.string.date);
            this.dateLabel.setTextColor(Helper.getAttributeColor(this, R.attr.primaryTextColor));
            if (Build.VERSION.SDK_INT >= 29) {
                this.dateWrapper.getBackground().setColorFilter(new BlendModeColorFilter(Helper.getAttributeColor(this, R.attr.progressBar), BlendMode.SRC_OVER));
            } else {
                this.dateWrapper.getBackground().setColorFilter(Helper.getAttributeColor(this, R.attr.progressBar), PorterDuff.Mode.SRC_OVER);
            }
            this.dateImageView.setColorFilter(Helper.getAttributeColor(this, R.attr.primaryTextColor), PorterDuff.Mode.SRC_IN);
            this.dateImageView.setImageResource(R.drawable.account_caret);
            this.dateImageView.setOnClickListener(null);
            search();
        } else if (id == R.id.dateWrapper) {
            BottomDateDialog bottomDateDialog = new BottomDateDialog();
            bottomDateDialog.setTime(this.startDate, this.endDate);
            bottomDateDialog.setListener(this);
            bottomDateDialog.show(getSupportFragmentManager(), "date");
        } else if (id == R.id.walletImageView) {
            this.walletIds = new ArrayList<>();
            this.walletLabel.setText(R.string.wallet);
            this.walletLabel.setTextColor(Helper.getAttributeColor(this, R.attr.primaryTextColor));
            if (Build.VERSION.SDK_INT >= 29) {
                this.walletWrapper.getBackground().setColorFilter(new BlendModeColorFilter(Helper.getAttributeColor(this, R.attr.progressBar), BlendMode.SRC_OVER));
            } else {
                this.walletWrapper.getBackground().setColorFilter(Helper.getAttributeColor(this, R.attr.progressBar), PorterDuff.Mode.SRC_OVER);
            }
            this.walletImageView.setColorFilter(Helper.getAttributeColor(this, R.attr.primaryTextColor), PorterDuff.Mode.SRC_IN);
            this.walletImageView.setImageResource(R.drawable.account_caret);
            this.walletImageView.setOnClickListener(null);
            search();
        } else if (id == R.id.walletWrapper) {
            this.walletAdapter = new SearchWalletAdapter(this.walletsList, this.walletIds, this);
            if (SharePreferenceHelper.getThemeMode(this) != 1) {
                i = R.style.AppTheme;
            }
            AlertDialog.Builder builder2 = new AlertDialog.Builder(new ContextThemeWrapper(this, i));
            builder2.setAdapter(this.walletAdapter, null);
            builder2.setTitle(R.string.wallet);
            builder2.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.Search.3
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    if (Search.this.walletIds.size() == 0) {
                        Search.this.walletIds = new ArrayList<>();
                        Search.this.walletLabel.setText(R.string.wallet);
                        Search.this.walletLabel.setTextColor(Helper.getAttributeColor(Search.this, R.attr.primaryTextColor));
                        if (Build.VERSION.SDK_INT >= 29) {
                            Search.this.walletWrapper.getBackground().setColorFilter(new BlendModeColorFilter(Helper.getAttributeColor(Search.this, R.attr.progressBar), BlendMode.SRC_OVER));
                        } else {
                            Search.this.walletWrapper.getBackground().setColorFilter(Helper.getAttributeColor(Search.this, R.attr.progressBar), PorterDuff.Mode.SRC_OVER);
                        }
                        Search.this.walletImageView.setColorFilter(Helper.getAttributeColor(Search.this, R.attr.primaryTextColor), PorterDuff.Mode.SRC_IN);
                        Search.this.walletImageView.setImageResource(R.drawable.account_caret);
                        Search.this.walletImageView.setOnClickListener(null);
                    } else {
                        Search search = Search.this;
                        search.walletIds = (ArrayList) search.walletAdapter.getIds();
                        Search.this.walletLabel.setText(Search.this.walletIds.size() > 1 ? Search.this.getResources().getString(R.string.total_wallets, Integer.valueOf(Search.this.walletIds.size())) : Search.this.getResources().getString(R.string.total_wallet, Integer.valueOf(Search.this.walletIds.size())));
                        Search.this.walletLabel.setTextColor(Color.parseColor("#FFFFFF"));
                        if (Build.VERSION.SDK_INT >= 29) {
                            Search.this.walletWrapper.getBackground().setColorFilter(new BlendModeColorFilter(ContextCompat.getColor(Search.this.getApplicationContext(), R.color.blue), BlendMode.SRC_OVER));
                        } else {
                            Search.this.walletWrapper.getBackground().setColorFilter(ContextCompat.getColor(Search.this.getApplicationContext(), R.color.blue), PorterDuff.Mode.SRC_OVER);
                        }
                        Search.this.walletImageView.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
                        Search.this.walletImageView.setImageResource(R.drawable.search_clear);
                        Search.this.walletImageView.setOnClickListener(Search.this);
                    }
                    Search.this.search();
                }
            });
            builder2.create();
            builder2.show();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onClick$0$com-ktwapps-walletmanager-Search  reason: not valid java name */
    public /* synthetic */ void m228lambda$onClick$0$comktwappswalletmanagerSearch(DialogInterface dialogInterface, int i) {
        if (this.categoryIds.size() == 0) {
            this.categoryIds = new ArrayList<>();
            this.categoryLabel.setText(R.string.category);
            this.categoryLabel.setTextColor(Helper.getAttributeColor(this, R.attr.primaryTextColor));
            if (Build.VERSION.SDK_INT >= 29) {
                this.categoryWrapper.getBackground().setColorFilter(new BlendModeColorFilter(Helper.getAttributeColor(this, R.attr.progressBar), BlendMode.SRC_OVER));
            } else {
                this.categoryWrapper.getBackground().setColorFilter(Helper.getAttributeColor(this, R.attr.progressBar), PorterDuff.Mode.SRC_OVER);
            }
            this.categoryImageView.setColorFilter(Helper.getAttributeColor(this, R.attr.primaryTextColor), PorterDuff.Mode.SRC_IN);
            this.categoryImageView.setImageResource(R.drawable.account_caret);
            this.categoryImageView.setOnClickListener(null);
        } else {
            ArrayList<Integer> arrayList = (ArrayList) this.categoryAdapter.getIds();
            this.categoryIds = arrayList;
            this.categoryLabel.setText(arrayList.size() > 1 ? getResources().getString(R.string.total_categories, Integer.valueOf(this.categoryIds.size())) : getResources().getString(R.string.total_category, Integer.valueOf(this.categoryIds.size())));
            this.categoryLabel.setTextColor(Color.parseColor("#FFFFFF"));
            if (Build.VERSION.SDK_INT >= 29) {
                this.categoryWrapper.getBackground().setColorFilter(new BlendModeColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue), BlendMode.SRC_OVER));
            } else {
                this.categoryWrapper.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue), PorterDuff.Mode.SRC_OVER);
            }
            this.categoryImageView.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
            this.categoryImageView.setImageResource(R.drawable.search_clear);
            this.categoryImageView.setOnClickListener(this);
        }
        search();
    }

    @Override // com.ktwapps.walletmanager.Widget.BottomDateDialog.OnItemClickListener
    public void onItemClick(long startDate, long endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        String bestDateTimePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "dd MMM");
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeInMillis(startDate);
        String format = new SimpleDateFormat(bestDateTimePattern, Locale.getDefault()).format(calendar.getTime());
        if (!DateHelper.isSameDay(startDate, endDate)) {
            calendar.setTimeInMillis(endDate);
            format = format + " - " + new SimpleDateFormat(bestDateTimePattern, Locale.getDefault()).format(calendar.getTime());
        }
        this.dateLabel.setText(format);
        this.dateLabel.setTextColor(Color.parseColor("#FFFFFF"));
        if (Build.VERSION.SDK_INT >= 29) {
            this.dateWrapper.getBackground().setColorFilter(new BlendModeColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue), BlendMode.SRC_OVER));
        } else {
            this.dateWrapper.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue), PorterDuff.Mode.SRC_OVER);
        }
        this.dateImageView.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
        this.dateImageView.setImageResource(R.drawable.search_clear);
        this.dateImageView.setOnClickListener(this);
        search();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void search() {
        search("");
    }

    private void search(final String text) {
        final long time;
        long time1;
        final long j;
        long j1;
        long j2 = this.startDate;
        if (j2 == 0) {
            long j3 = this.endDate;
            if (j3 == 0) {
                time1 = j3;
                j1 = j2;
                long finalJ = j1;
                long finalTime = time1;
                Executors.newSingleThreadScheduledExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.Search.4
                    @Override // java.lang.Runnable
                    public void run() {
                        List<SearchTrans> searchAllTrans;
                        String trim = text.length() > 0 ? text : Search.this.searchEditText.getText().toString().trim();
                        int accountId = SharePreferenceHelper.getAccountId(Search.this.getApplicationContext());
                        AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(Search.this.getApplicationContext());
                        if (trim.length() != 0 && finalJ != 0 && finalTime != 0) {
                            searchAllTrans = appDatabaseObject.transDaoObject().searchAllTrans("%" + trim + "%", accountId, finalJ, finalTime);
                        } else if (trim.length() == 0 && finalJ == 0 && finalTime == 0) {
                            searchAllTrans = appDatabaseObject.transDaoObject().searchAllTrans(accountId);
                        } else if (trim.length() != 0) {
                            searchAllTrans = appDatabaseObject.transDaoObject().searchAllTrans("%" + trim + "%", accountId);
                        } else {
                            searchAllTrans = appDatabaseObject.transDaoObject().searchAllTrans(accountId, finalJ, finalTime);
                        }
                        final List<SearchTrans> filterTrans = Search.this.filterTrans(searchAllTrans);
                        Search.this.adapter.setTransList(filterTrans);
                        Search.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.Search.4.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Search search;
                                int i;
                                String summary = Search.this.getSummary(filterTrans);
                                Search.this.emptyLabel.setText(R.string.search_result);
                                Search.this.emptyWrapper.setVisibility(filterTrans.size() == 0 ? 0 : 8);
                                RecyclerView recyclerView = Search.this.recyclerView;
                                if (filterTrans.size() == 0) {
                                    search = Search.this;
                                    i = R.attr.emptyBackground;
                                } else {
                                    search = Search.this;
                                    i = R.attr.primaryBackground;
                                }
                                recyclerView.setBackgroundColor(Helper.getAttributeColor(search, i));
                                Search.this.adapter.notifyDataSetChanged();
                                Search.this.summaryLabel.setText(summary);
                                Search.this.summaryLabel.setVisibility(summary.length() <= 0 ? 8 : 0);
                            }
                        });
                    }
                });
            }
        }
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeInMillis(this.startDate);
        calendar.set(14, 0);
        calendar.set(13, 0);
        calendar.set(12, 0);
        calendar.set(11, 0);
        long time2 = calendar.getTime().getTime();
        calendar.setTimeInMillis(this.endDate);
        calendar.add(5, 1);
        calendar.set(14, 0);
        calendar.set(13, 0);
        calendar.set(12, 0);
        calendar.set(11, 0);
        time1 = calendar.getTime().getTime();
        time = time1;
        j1 = time2;
        j = j1;
        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.Search.4
            @Override // java.lang.Runnable
            public void run() {
                List<SearchTrans> searchAllTrans;
                String trim = text.length() > 0 ? text : Search.this.searchEditText.getText().toString().trim();
                int accountId = SharePreferenceHelper.getAccountId(Search.this.getApplicationContext());
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(Search.this.getApplicationContext());
                if (trim.length() != 0 && j != 0 && time != 0) {
                    searchAllTrans = appDatabaseObject.transDaoObject().searchAllTrans("%" + trim + "%", accountId, j, time);
                } else if (trim.length() == 0 && j == 0 && time == 0) {
                    searchAllTrans = appDatabaseObject.transDaoObject().searchAllTrans(accountId);
                } else if (trim.length() != 0) {
                    searchAllTrans = appDatabaseObject.transDaoObject().searchAllTrans("%" + trim + "%", accountId);
                } else {
                    searchAllTrans = appDatabaseObject.transDaoObject().searchAllTrans(accountId, j, time);
                }
                final List filterTrans = Search.this.filterTrans(searchAllTrans);
                Search.this.adapter.setTransList(filterTrans);
                Search.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.Search.4.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Search search;
                        int i;
                        String summary = Search.this.getSummary(filterTrans);
                        Search.this.emptyLabel.setText(R.string.search_result);
                        Search.this.emptyWrapper.setVisibility(filterTrans.size() == 0 ? 0 : 8);
                        RecyclerView recyclerView = Search.this.recyclerView;
                        if (filterTrans.size() == 0) {
                            search = Search.this;
                            i = R.attr.emptyBackground;
                        } else {
                            search = Search.this;
                            i = R.attr.primaryBackground;
                        }
                        recyclerView.setBackgroundColor(Helper.getAttributeColor(search, i));
                        Search.this.adapter.notifyDataSetChanged();
                        Search.this.summaryLabel.setText(summary);
                        Search.this.summaryLabel.setVisibility(summary.length() <= 0 ? 8 : 0);
                    }
                });
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getSummary(List<SearchTrans> trans) {
        long j = 0;
        long j2 = 0;
        int i = 0;
        int i2 = 0;
        for (SearchTrans searchTrans : trans) {
            if (searchTrans.getType() == 1) {
                j += ((float) searchTrans.getAmount()) * searchTrans.getRate();
                i2++;
            } else if (searchTrans.getType() == 0) {
                j2 += ((float) searchTrans.getAmount()) * searchTrans.getRate();
                i++;
            }
        }
        if (j <= 0) {
            j = -j;
        }
        String accountSymbol = SharePreferenceHelper.getAccountSymbol(this);
        return (i == 0 && i2 == 0) ? "" : (i != 0 || i2 <= 0) ? (i <= 0 || i2 != 0) ? getResources().getString(R.string.search_summary, Integer.valueOf(i), Helper.getBeautifyAmount(accountSymbol, j2), Integer.valueOf(i2), Helper.getBeautifyAmount(accountSymbol, j)) : getResources().getString(R.string.search_summary_income, Integer.valueOf(i), Helper.getBeautifyAmount(accountSymbol, j2)) : getResources().getString(R.string.search_summary_expense, Integer.valueOf(i2), Helper.getBeautifyAmount(accountSymbol, j));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<SearchTrans> filterTrans(List<SearchTrans> searchResult) {
        ArrayList arrayList = new ArrayList();
        for (SearchTrans searchTrans : searchResult) {
            if (this.categoryIds.size() > 0 && this.walletIds.size() > 0) {
                if (this.categoryIds.contains(Integer.valueOf(searchTrans.getCategoryId())) && (this.walletIds.contains(Integer.valueOf(searchTrans.getWalletId())) || this.walletIds.contains(Integer.valueOf(searchTrans.getTransferWalletId())))) {
                    arrayList.add(searchTrans);
                }
            } else if (this.categoryIds.size() > 0) {
                if (this.categoryIds.contains(Integer.valueOf(searchTrans.getCategoryId()))) {
                    arrayList.add(searchTrans);
                }
            } else if (this.walletIds.size() > 0) {
                if (this.walletIds.contains(Integer.valueOf(searchTrans.getWalletId())) || this.walletIds.contains(Integer.valueOf(searchTrans.getTransferWalletId()))) {
                    arrayList.add(searchTrans);
                }
            } else {
                arrayList.add(searchTrans);
            }
        }
        return arrayList;
    }

    @Override // com.ktwapps.walletmanager.Adapter.SearchAdapter.OnItemClickListener
    public void OnItemClick(View v, int position) {
        SearchTrans searchTrans = this.adapter.getTransList().get(position);
        if (v.getId() == R.id.image1 || v.getId() == R.id.image2 || v.getId() == R.id.image3) {
            Intent intent = new Intent(getApplicationContext(), PhotoViewer.class);
            intent.putExtra(ClientCookie.PATH_ATTR, searchTrans.getMedia());
            intent.putExtra(FirebaseAnalytics.Param.INDEX, ((Integer) v.getTag()).intValue());
            startActivity(intent);
        } else if (searchTrans.getDebtId() != 0 && searchTrans.getDebtTransId() == 0) {
            Intent intent2 = new Intent(this, DebtDetails.class);
            intent2.putExtra("debtId", searchTrans.getDebtId());
            startActivity(intent2);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        } else {
            Intent intent3 = new Intent(this, Details.class);
            intent3.putExtra("transId", searchTrans.getId());
            startActivity(intent3);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        }
    }
}
