package com.expance.merapaisa;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.expance.merapaisa.Adapter.SpinnerTextAdapter;
import com.expance.merapaisa.Async.ExportCategoryAsync;
import com.expance.merapaisa.Async.ExportRecordAsync;
import com.expance.merapaisa.Async.ExportReportAsync;
import com.expance.merapaisa.Async.ExportWalletAsync;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Model.ExportCategory;
import com.expance.merapaisa.Model.ExportReport;
import com.expance.merapaisa.Model.ExportTimeRange;
import com.expance.merapaisa.Model.ExportWallet;
import com.expance.merapaisa.Model.Exports;
import com.expance.merapaisa.Utility.CalendarHelper;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Utility.DateHelper;
import com.expance.merapaisa.Utility.ExportHelper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import vocsy.ads.CustomAdsListener;
import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class Export extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Date endDate;
    EditText endEditText;
    Button exportButton;
    SpinnerTextAdapter formatAdapter;
    Spinner formatSpinner;
    Date startDate;
    EditText startEditText;
    SpinnerTextAdapter timeRangeAdapter;
    TextView timeRangeLabel;
    Spinner timeRangeSpinner;
    FrameLayout timeRangeWrapper;
    SpinnerTextAdapter typeAdapter;
    Spinner typeSpinner;
    EditText walletEditText;
    int walletId;
    ArrayList<Integer> walletIds;

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("startDate", this.startDate.getTime());
        outState.putLong("endDate", this.endDate.getTime());
        outState.putIntegerArrayList("walletIds", this.walletIds);
        outState.putInt("walletId", this.walletId);
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
        setContentView(R.layout.activity_export);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        this.walletId = 0;
        this.walletIds = new ArrayList<>();
        this.startDate = ExportHelper.getInitiatStartDate();
        this.endDate = ExportHelper.getInitialEndDate();
        if (savedInstanceState != null) {
            this.walletId = savedInstanceState.getInt("walletId");
            this.walletIds = savedInstanceState.getIntegerArrayList("walletIds");
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTimeInMillis(savedInstanceState.getLong("startDate"));
            this.startDate = calendar.getTime();
            calendar.setTimeInMillis(savedInstanceState.getLong("endDate"));
            this.endDate = calendar.getTime();
        }
        setUpLayout();
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

    public void setUpLayout() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.exports));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.exportButton = (Button) findViewById(R.id.exportButton);
        this.walletEditText = (EditText) findViewById(R.id.walletEditText);
        this.startEditText = (EditText) findViewById(R.id.startEditText);
        this.endEditText = (EditText) findViewById(R.id.endEditText);
        this.formatSpinner = (Spinner) findViewById(R.id.formatSpinner);
        this.typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
        this.timeRangeSpinner = (Spinner) findViewById(R.id.timeRangeSpinner);
        this.timeRangeWrapper = (FrameLayout) findViewById(R.id.timeRangeWrapper);
        this.timeRangeLabel = (TextView) findViewById(R.id.timeRangeLabel);
        this.formatAdapter = new SpinnerTextAdapter(this, R.layout.list_drop_down_color, R.id.label, DataHelper.getFormatList());
        this.typeAdapter = new SpinnerTextAdapter(this, R.layout.list_drop_down_color, R.id.label, DataHelper.getExportTypeList(this));
        this.timeRangeAdapter = new SpinnerTextAdapter(this, R.layout.list_drop_down_color, R.id.label, DataHelper.getExportTimeRangeList(this));
        this.walletEditText.setText(R.string.all_wallets);
        this.startEditText.setText(DateHelper.getFormattedDate(getApplicationContext(), this.startDate));
        this.endEditText.setText(DateHelper.getFormattedDate(getApplicationContext(), this.endDate));
        this.formatSpinner.setAdapter((SpinnerAdapter) this.formatAdapter);
        this.timeRangeSpinner.setAdapter((SpinnerAdapter) this.timeRangeAdapter);
        this.typeSpinner.setAdapter((SpinnerAdapter) this.typeAdapter);
        this.timeRangeSpinner.setSelection(1);
        this.typeSpinner.setOnItemSelectedListener(this);
        this.timeRangeWrapper.setVisibility(8);
        this.timeRangeLabel.setVisibility(8);
        this.exportButton.setOnClickListener(this);
        this.walletEditText.setFocusable(false);
        this.walletEditText.setOnClickListener(this);
        this.walletEditText.setLongClickable(false);
        this.startEditText.setFocusable(false);
        this.startEditText.setOnClickListener(this);
        this.startEditText.setLongClickable(false);
        this.endEditText.setFocusable(false);
        this.endEditText.setOnClickListener(this);
        this.endEditText.setLongClickable(false);
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

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        if (v.getId() == R.id.endEditText /* 2131231053 */) {
            new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() { // from class: com.ktwapps.walletmanager.Export.2
                @Override // android.app.DatePickerDialog.OnDateSetListener
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    Export.this.endDate = CalendarHelper.getDateFromPicker(i, i1, i2);
                    Export.this.endEditText.setText(DateHelper.getFormattedDate(Export.this.getApplicationContext(), Export.this.endDate));
                    if (DateHelper.isBeforeDate(Export.this.startDate, Export.this.endDate)) {
                        Export.this.startDate = CalendarHelper.getDateFromPicker(i, i1, i2);
                        Export.this.startEditText.setText(DateHelper.getFormattedDate(Export.this.getApplicationContext(), Export.this.startDate));
                    }
                }
            }, CalendarHelper.getYearFromDate(this.endDate), CalendarHelper.getMonthFromDate(this.endDate), CalendarHelper.getDayFromDate(this.endDate)).show();
        } else if (v.getId() == R.id.exportButton /* 2131231076 */) {
            checkPermission();
        } else if (v.getId() == R.id.startEditText /* 2131231544 */) {
            new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() { // from class: com.ktwapps.walletmanager.Export.1
                @Override // android.app.DatePickerDialog.OnDateSetListener
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    Export.this.startDate = CalendarHelper.getDateFromPicker(i, i1, i2);
                    Export.this.startEditText.setText(DateHelper.getFormattedDate(Export.this.getApplicationContext(), Export.this.startDate));
                    if (DateHelper.isBeforeDate(Export.this.startDate, Export.this.endDate)) {
                        Export.this.endDate = CalendarHelper.getDateFromPicker(i, i1, i2);
                        Export.this.endEditText.setText(DateHelper.getFormattedDate(Export.this.getApplicationContext(), Export.this.endDate));
                    }
                }
            }, CalendarHelper.getYearFromDate(this.startDate), CalendarHelper.getMonthFromDate(this.startDate), CalendarHelper.getDayFromDate(this.startDate)).show();
        } else if (v.getId() == R.id.walletEditText /* 2131231677 */) {
            Intent intent = new Intent(this, WalletMultiplePicker.class);
            intent.putIntegerArrayListExtra("ids", this.walletIds);
            intent.putExtra("id", this.walletId);
            startActivityForResult(intent, 2);
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == -1 && data != null) {
            this.walletId = data.getIntExtra("id", -1);
            this.walletIds = data.getIntegerArrayListExtra("ids");
            this.walletEditText.setText(data.getStringExtra("name"));
        }
    }

    private void exportRecord() {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.Export.3
            @Override // java.lang.Runnable
            public void run() {
                List<ExportWallet> walletList;
                List<ExportCategory> walletCategoryList;
                List<Exports> walletExportList;
                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(Export.this);
                long time = ExportHelper.getStartDate(Export.this.startDate).getTime();
                long time2 = ExportHelper.getEndDate(Export.this.endDate).getTime();
                int selectedItemPosition = Export.this.formatSpinner.getSelectedItemPosition();
                int selectedItemPosition2 = Export.this.typeSpinner.getSelectedItemPosition();
                boolean z = Export.this.walletId == 0 || Export.this.walletIds.size() == 0;
                if (selectedItemPosition2 == 0) {
                    if (z) {
                        walletExportList = appDatabaseObject.exportDaoObject().getAllExportList(SharePreferenceHelper.getAccountId(Export.this), time, time2);
                    } else {
                        walletExportList = appDatabaseObject.exportDaoObject().getWalletExportList(SharePreferenceHelper.getAccountId(Export.this), Export.this.walletIds, time, time2);
                    }
                    new ExportRecordAsync(Export.this, walletExportList, selectedItemPosition).execute(new String[0]);
                } else if (selectedItemPosition2 == 1) {
                    ArrayList arrayList = new ArrayList();
                    Export export = Export.this;
                    for (ExportTimeRange exportTimeRange : ExportHelper.getTimeRangeList(export, export.timeRangeSpinner.getSelectedItemPosition(), time, time2)) {
                        if (z) {
                            ExportReport allReport = appDatabaseObject.exportDaoObject().getAllReport(SharePreferenceHelper.getAccountId(Export.this), exportTimeRange.getStartDate(), exportTimeRange.getEndDate());
                            allReport.setDateTime(exportTimeRange.getFormattedDate());
                            arrayList.add(allReport);
                        } else {
                            ExportReport walletReport = appDatabaseObject.exportDaoObject().getWalletReport(SharePreferenceHelper.getAccountId(Export.this), Export.this.walletIds, exportTimeRange.getStartDate(), exportTimeRange.getEndDate());
                            walletReport.setDateTime(exportTimeRange.getFormattedDate());
                            arrayList.add(walletReport);
                        }
                    }
                    new ExportReportAsync(Export.this, arrayList, selectedItemPosition).execute(new String[0]);
                } else if (selectedItemPosition2 == 2) {
                    if (z) {
                        walletCategoryList = appDatabaseObject.exportDaoObject().getAllCategoryList(SharePreferenceHelper.getAccountId(Export.this), time, time2);
                    } else {
                        walletCategoryList = appDatabaseObject.exportDaoObject().getWalletCategoryList(SharePreferenceHelper.getAccountId(Export.this), Export.this.walletIds, time, time2);
                    }
                    new ExportCategoryAsync(Export.this, walletCategoryList, selectedItemPosition).execute(new String[0]);
                } else if (selectedItemPosition2 == 3) {
                    if (z) {
                        walletList = appDatabaseObject.exportDaoObject().getAllWalletList(SharePreferenceHelper.getAccountId(Export.this), time, time2);
                    } else {
                        walletList = appDatabaseObject.exportDaoObject().getWalletList(SharePreferenceHelper.getAccountId(Export.this), Export.this.walletIds, time, time2);
                    }
                    new ExportWalletAsync(Export.this, walletList, selectedItemPosition).execute(new String[0]);
                }
            }
        });
    }

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        this.timeRangeWrapper.setVisibility(i == 1 ? 0 : 8);
        this.timeRangeLabel.setVisibility(i != 1 ? 8 : 0);
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            if (ActivityCompat.checkSelfPermission(this, "android.permission.READ_MEDIA_IMAGES") != 0) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_MEDIA_IMAGES")) {
                    showPermissionDialog(getResources().getString(R.string.access_storage_hint), getResources().getString(R.string.allow), getResources().getString(R.string.deny), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.Export$$ExternalSyntheticLambda2
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            Export.this.m206lambda$checkPermission$0$comktwappswalletmanagerExport(dialogInterface, i);
                        }
                    });
                    return;
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_MEDIA_IMAGES"}, 0);
                    return;
                }
            }
            GoogleAds.getInstance().showCounterInterstitialAd(Export.this, new CustomAdsListener() {
                @Override
                public void onFinish() {
                    exportRecord();
                }
            });

        } else if (ActivityCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
                showPermissionDialog(getResources().getString(R.string.access_storage_hint), getResources().getString(R.string.allow), getResources().getString(R.string.deny), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.Export.4
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(Export.this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 0);
                    }
                });
            } else {
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 0);
            }
        } else {
            GoogleAds.getInstance().showCounterInterstitialAd(Export.this, new CustomAdsListener() {
                @Override
                public void onFinish() {
                    exportRecord();
                }
            });


        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$checkPermission$0$com-ktwapps-walletmanager-Export  reason: not valid java name */
    public /* synthetic */ void m206lambda$checkPermission$0$comktwappswalletmanagerExport(DialogInterface dialogInterface, int i) {
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_MEDIA_IMAGES"}, 0);
    }

    @Override
    // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != 0) {
            return;
        }
        if (grantResults.length > 0 && grantResults[0] == 0) {
            exportRecord();
        } else if (Build.VERSION.SDK_INT >= 33) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_MEDIA_IMAGES")) {
                return;
            }
            showPermissionDialog(getResources().getString(R.string.access_storage_rational_hint), getResources().getString(R.string.app_settings), getResources().getString(R.string.not_now), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.Export$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    Export.this.m207xd4f8e68f(dialogInterface, i);
                }
            });
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
        } else {
            showPermissionDialog(getResources().getString(R.string.access_storage_rational_hint), getResources().getString(R.string.app_settings), getResources().getString(R.string.not_now), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.Export$$ExternalSyntheticLambda1
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    Export.this.m208x1218aaae(dialogInterface, i);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onRequestPermissionsResult$1$com-ktwapps-walletmanager-Export  reason: not valid java name */
    public /* synthetic */ void m207xd4f8e68f(DialogInterface dialogInterface, int i) {
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", getPackageName(), null));
        startActivity(intent);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onRequestPermissionsResult$2$com-ktwapps-walletmanager-Export  reason: not valid java name */
    public /* synthetic */ void m208x1218aaae(DialogInterface dialogInterface, int i) {
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", getPackageName(), null));
        startActivity(intent);
    }

    private void showPermissionDialog(String message, String positive, String negative, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle(getResources().getString(R.string.access_storage_title));
        builder.setPositiveButton(positive, listener);
        builder.setNegativeButton(negative, (DialogInterface.OnClickListener) null);
        builder.create().show();
    }
}
