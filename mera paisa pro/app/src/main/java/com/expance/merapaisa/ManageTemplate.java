package com.expance.merapaisa;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.expance.merapaisa.Adapter.TemplateAdapter;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.ViewModel.TemplateViewModel;
import com.expance.merapaisa.Model.Template;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import com.expance.merapaisa.Widget.SwipeAndDragViewHelper;
import java.util.List;
import java.util.concurrent.Executors;

import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class ManageTemplate extends AppCompatActivity implements TemplateAdapter.OnItemClickListener, View.OnClickListener {
    TemplateAdapter adapter;
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
        setContentView(R.layout.activity_template);
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

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.ManageTemplate.1
            @Override // java.lang.Runnable
            public void run() {
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(ManageTemplate.this.getApplicationContext());
                for (int i = 0; i < ManageTemplate.this.adapter.list.size(); i++) {
                    appDatabaseObject.templateDaoObject().updateTemplateOrdering(i, ManageTemplate.this.adapter.list.get(i).getId());
                }
            }
        });
    }

    @Override // androidx.appcompat.app.AppCompatActivity
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(R.anim.scale_in, R.anim.bottom_to_top);
        return true;
    }

    private void setUpLayout() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.manage_template));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.adapter = new TemplateAdapter(this);
        this.emptyWrapper = (ConstraintLayout) findViewById(R.id.emptyWrapper);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setAdapter(this.adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeAndDragViewHelper(this.adapter));
        this.adapter.setTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(this.recyclerView);
        this.adapter.setListener(this);
        ((FloatingActionButton) findViewById(R.id.fab)).setOnClickListener(this);
        TemplateViewModel templateViewModel = (TemplateViewModel) new ViewModelProvider(this).get(TemplateViewModel.class);
        this.templateViewModel = templateViewModel;
        templateViewModel.getTemplate().observe(this, new Observer<List<Template>>() { // from class: com.ktwapps.walletmanager.ManageTemplate.2
            @Override // androidx.lifecycle.Observer
            public void onChanged(List<Template> templates) {
                ManageTemplate.this.adapter.setTransList(templates);
                ManageTemplate.this.emptyWrapper.setVisibility((templates == null || templates.size() <= 0) ? 0 : 8);
            }
        });
    }

    @Override // com.ktwapps.walletmanager.Adapter.TemplateAdapter.OnItemClickListener
    public void OnItemClick(View v, int position, int type) {
        if (type == 25) {
            Intent intent = new Intent(this, CreateTemplate.class);
            intent.putExtra("id", this.adapter.getList().get(position).getId());
            startActivity(intent);
            overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
            return;
        }
        final int id = this.adapter.list.get(position).getId();
        Helper.showDialog(this, "", getResources().getString(R.string.template_delete_message), getResources().getString(R.string.delete_positive), getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.ManageTemplate.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.ManageTemplate.3.1
                    @Override // java.lang.Runnable
                    public void run() {
                        AppDatabaseObject.getInstance(ManageTemplate.this.getApplicationContext()).templateDaoObject().deleteTemplate(id);
                    }
                });
            }
        });
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.fab) {
            startActivity(new Intent(this, CreateTemplate.class));
            overridePendingTransition(R.anim.top_to_bottom, R.anim.scale_out);
        }
    }
}
