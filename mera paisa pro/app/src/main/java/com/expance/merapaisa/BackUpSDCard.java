package com.expance.merapaisa;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.expance.merapaisa.Adapter.BackupAdapter;
import com.expance.merapaisa.Adapter.MultiChoiceImageAdapter;
import com.expance.merapaisa.BackUp.BackUpHelper;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.Entity.AccountEntity;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executors;

import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class BackUpSDCard extends AppCompatActivity implements BackupAdapter.OnItemClickListener {
    private static final int FILE_REQUEST = 2;
    BackupAdapter adapter;
    File databaseDir;
    AlertDialog dialog;
    boolean isPermissionDeny = false;
    RecyclerView recyclerView;

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
        setContentView(R.layout.activity_back_up_sdcard);
        SystemConfiguration.setStatusBarColor(this, R.color.theam, SystemConfiguration.IconColor.ICON_DARK);
        GoogleAds.getInstance().admobBanner(this, (LinearLayout) findViewById(R.id.nativeLay));
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        setUpLayout();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.dialog_progress_bar);
        this.dialog = builder.create();
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
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.backup_title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        BackupAdapter backupAdapter = new BackupAdapter(this);
        this.adapter = backupAdapter;
        backupAdapter.setListener(this);
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        if (this.isPermissionDeny) {
            return;
        }
        checkPermission();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getAllFiles() {
        if (Build.VERSION.SDK_INT >= 29) {
            this.databaseDir = new File(getExternalFilesDir(null) + "/MoneyManagers/Backup");
        } else {
            this.databaseDir = new File(Environment.getExternalStorageDirectory() + "/MoneyManagers/Backup");
        }
        if (!this.databaseDir.exists()) {
            this.databaseDir.mkdirs();
        }
        File[] listFiles = this.databaseDir.listFiles();
        ArrayList arrayList = new ArrayList();
        if (listFiles != null) {
            arrayList.addAll(Arrays.asList(listFiles));
        }
        this.adapter.setList(arrayList);
        this.adapter.notifyDataSetChanged();
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

    @Override // com.ktwapps.walletmanager.Adapter.BackupAdapter.OnItemClickListener
    public void OnItemClick(View v, int position) {
        if (v.getId() == R.id.backupButton) {
            if (!this.isPermissionDeny) {
                Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.BackUpSDCard.1
                    @Override // java.lang.Runnable
                    public void run() {
                        AppDatabaseObject.getInstance(BackUpSDCard.this.getApplicationContext()).accountDaoObject().checkpoint(new SimpleSQLiteQuery("pragma wal_checkpoint(full)"));
                        BackUpSDCard.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.BackUpSDCard.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                File databasePath = BackUpSDCard.this.getDatabasePath("wallet-database");
                                File file = new File(BackUpSDCard.this.databaseDir, BackUpHelper.generateName());
                                try {
                                    if (databasePath.exists()) {
                                        FileChannel channel = new FileInputStream(databasePath).getChannel();
                                        FileChannel channel2 = new FileOutputStream(file).getChannel();
                                        channel2.transferFrom(channel, 0L, channel.size());
                                        channel.close();
                                        channel2.close();
                                        Toast.makeText(BackUpSDCard.this.getApplicationContext(), (int) R.string.backup_success, 0).show();
                                        BackUpSDCard.this.getAllFiles();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
                return;
            } else {
                checkPermission();
                return;
            }
        }
        File file = this.adapter.getList().get(position);
        ArrayList arrayList = new ArrayList();
        arrayList.add(Integer.valueOf((int) R.drawable.restore));
        arrayList.add(Integer.valueOf((int) R.drawable.email));
        arrayList.add(Integer.valueOf((int) R.drawable.delete));
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(getResources().getString(R.string.restore));
        arrayList2.add(getResources().getString(R.string.email));
        arrayList2.add(getResources().getString(R.string.delete));
        int themeMode = SharePreferenceHelper.getThemeMode(this);
        int i = R.style.AppThemeNight;
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, themeMode == 1 ? R.style.AppThemeNight : R.style.AppTheme));
        builder.setTitle(R.string.restore_data);
        if (SharePreferenceHelper.getThemeMode(this) != 1) {
            i = R.style.AppTheme;
        }
        builder.setAdapter(new MultiChoiceImageAdapter(new ContextThemeWrapper(this, i), arrayList2, arrayList), null);
        AlertDialog create = builder.create();
        create.getListView().setOnItemClickListener(new AnonymousClass2(file, create));
        create.show();
    }

    /* renamed from: com.ktwapps.walletmanager.BackUpSDCard$2  reason: invalid class name */
    /* loaded from: classes3.dex */
    class AnonymousClass2 implements AdapterView.OnItemClickListener {
        final /* synthetic */ AlertDialog val$dialog;
        final /* synthetic */ File val$file;

        AnonymousClass2(final File val$file, final AlertDialog val$dialog) {
            this.val$file = val$file;
            this.val$dialog = val$dialog;
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (i == 0) {
                if (BackUpHelper.isValidSQLite(this.val$file)) {
                    File copyFile = BackUpHelper.copyFile(BackUpSDCard.this.getApplicationContext(), this.val$file);
                    if (copyFile == null) {
                        Toast.makeText(BackUpSDCard.this.getApplicationContext(), BackUpSDCard.this.getResources().getString(R.string.restore_fail, 40002), 1).show();
                    } else if (!BackUpHelper.validateDB(copyFile.getAbsolutePath())) {
                        Toast.makeText(BackUpSDCard.this.getApplicationContext(), BackUpSDCard.this.getResources().getString(R.string.restore_fail, 40003), 1).show();
                    } else if (BackUpHelper.restore(BackUpSDCard.this.getApplicationContext(), copyFile)) {
                        Toast.makeText(BackUpSDCard.this.getApplicationContext(), (int) R.string.restore_success, 1).show();
                        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.BackUpSDCard.2.1
                            @Override // java.lang.Runnable
                            public void run() {
                                AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(BackUpSDCard.this.getApplicationContext());
                                appDatabaseObject.accountDaoObject().checkpoint(new SimpleSQLiteQuery("pragma wal_checkpoint(full)"));
                                appDatabaseObject.restoreDatabase();
                                AppDatabaseObject appDatabaseObject2 = AppDatabaseObject.getInstance(BackUpSDCard.this.getApplicationContext());
                                appDatabaseObject2.accountDaoObject().restoreMedia();
                                AccountEntity accountEntity = appDatabaseObject2.accountDaoObject().getAllAccountEntity().get(0);
                                SharePreferenceHelper.setAccount(BackUpSDCard.this.getApplicationContext(), accountEntity.getId(), accountEntity.getCurrencySymbol(), accountEntity.getName());
                                BackUpSDCard.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.BackUpSDCard.2.1.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        BackUpSDCard.this.finishAffinity();
                                        BackUpSDCard.this.startActivity(new Intent(BackUpSDCard.this.getApplicationContext(), BaseActivity.class));
                                    }
                                });
                            }
                        });
                    }
                } else {
                    Toast.makeText(BackUpSDCard.this.getApplicationContext(), BackUpSDCard.this.getResources().getString(R.string.restore_fail, 40001), 1).show();
                }
            } else if (i == 1) {
                Intent intent = new Intent();
                intent.addFlags(1);
                intent.setAction("android.intent.action.SEND");
                Context applicationContext = BackUpSDCard.this.getApplicationContext();
                Uri uriForFile = FileProvider.getUriForFile(applicationContext, BackUpSDCard.this.getApplicationContext().getPackageName() + ".fileProvider", this.val$file);
                intent.setType("application/x-sqlite3");
                intent.putExtra("android.intent.extra.STREAM", uriForFile);
                BackUpSDCard.this.startActivity(intent);
            } else {
                BackUpSDCard backUpSDCard = BackUpSDCard.this;
                Helper.showDialog(backUpSDCard, "", backUpSDCard.getResources().getString(R.string.backup_delete_message), BackUpSDCard.this.getResources().getString(R.string.delete_positive), BackUpSDCard.this.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.BackUpSDCard.2.2
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (AnonymousClass2.this.val$file.delete()) {
                            BackUpSDCard.this.getAllFiles();
                        }
                    }
                });
            }
            this.val$dialog.dismiss();
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            if (ActivityCompat.checkSelfPermission(this, "android.permission.READ_MEDIA_IMAGES") != 0) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_MEDIA_IMAGES")) {
                    showPermissionDialog(getResources().getString(R.string.access_storage_hint), getResources().getString(R.string.allow), getResources().getString(R.string.deny), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.BackUpSDCard.3
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(BackUpSDCard.this, new String[]{"android.permission.READ_MEDIA_IMAGES"}, 0);
                        }
                    });
                    return;
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_MEDIA_IMAGES"}, 0);
                    return;
                }
            }
            getAllFiles();
            this.isPermissionDeny = false;
        } else if (ActivityCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
                showPermissionDialog(getResources().getString(R.string.access_storage_hint), getResources().getString(R.string.allow), getResources().getString(R.string.deny), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.BackUpSDCard.4
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(BackUpSDCard.this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 0);
                    }
                });
            } else {
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 0);
            }
        } else {
            getAllFiles();
            this.isPermissionDeny = false;
        }
    }

    @Override
    // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != 0) {
            return;
        }
        if (grantResults.length > 0 && grantResults[0] == 0) {
            getAllFiles();
            this.isPermissionDeny = false;
            return;
        }
        this.isPermissionDeny = true;
        if (Build.VERSION.SDK_INT >= 33) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_MEDIA_IMAGES")) {
                return;
            }
            showPermissionDialog(getResources().getString(R.string.access_storage_rational_hint), getResources().getString(R.string.app_settings), getResources().getString(R.string.not_now), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.BackUpSDCard.5
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.fromParts("package", BackUpSDCard.this.getPackageName(), null));
                    BackUpSDCard.this.startActivity(intent);
                }
            });
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
        } else {
            showPermissionDialog(getResources().getString(R.string.access_storage_rational_hint), getResources().getString(R.string.app_settings), getResources().getString(R.string.not_now), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.BackUpSDCard.6
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.fromParts("package", BackUpSDCard.this.getPackageName(), null));
                    BackUpSDCard.this.startActivity(intent);
                }
            });
        }
    }

    private void showPermissionDialog(String message, String positive, String negative, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle(getResources().getString(R.string.access_storage_title));
        builder.setPositiveButton(positive, listener);
        builder.setNegativeButton(negative, (DialogInterface.OnClickListener) null);
        final AlertDialog create = builder.create();
        create.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.ktwapps.walletmanager.BackUpSDCard.7
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface dialogInterface) {
                create.getButton(-1).setTextColor(BackUpSDCard.this.getResources().getColor(R.color.blue));
                create.getButton(-2).setTextColor(BackUpSDCard.this.getResources().getColor(R.color.blue));
            }
        });
        create.show();
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_action_file) {
            Intent intent = new Intent("android.intent.action.GET_CONTENT");
            intent.setType("*/*");
            startActivityForResult(Intent.createChooser(intent, "Choose a file"), 2);
            return true;
        }
        return false;
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_attachment, menu);
        for (int i = 0; i < menu.size(); i++) {
            Drawable icon = menu.getItem(i).getIcon();
            if (icon != null) {
                icon.mutate();
                icon.setColorFilter(Helper.getAttributeColor(this, R.attr.primaryDarkTextColor), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return true;
    }

    @Override
    // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == -1) {
            this.dialog.show();
            Uri data2 = data.getData();
            if (data2 != null) {
                Executors.newSingleThreadScheduledExecutor().execute(new AnonymousClass8(data2));
                return;
            }
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.restore_fail, 40007), 1).show();
            this.dialog.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.ktwapps.walletmanager.BackUpSDCard$8  reason: invalid class name */
    /* loaded from: classes3.dex */
    public class AnonymousClass8 implements Runnable {
        final Uri val$content_describer;

        AnonymousClass8(final Uri val$content_describer) {
            this.val$content_describer = val$content_describer;
        }

        @Override
        public void run() {
            try {
                InputStream inputStream = BackUpSDCard.this.getContentResolver().openInputStream(this.val$content_describer);
                File tempDir = new File(BackUpSDCard.this.getFilesDir(), "temp");
                File tempDbFile = new File(tempDir, "temp.db");
                if (!tempDir.exists()) {
                    tempDir.mkdirs();
                }
                FileOutputStream outputStream = new FileOutputStream(tempDbFile);
                byte[] buffer = new byte[1024];
                int readBytes;
                while ((readBytes = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, readBytes);
                }
                outputStream.close();
                inputStream.close();
                if (BackUpHelper.isValidSQLite(tempDbFile) && BackUpHelper.validateDB(tempDbFile.getAbsolutePath()) && BackUpHelper.restore(BackUpSDCard.this.getApplicationContext(), tempDbFile)) {
                    AppDatabaseObject instance = AppDatabaseObject.getInstance(BackUpSDCard.this.getApplicationContext());
                    instance.accountDaoObject().checkpoint(new SimpleSQLiteQuery("pragma wal_checkpoint(full)"));
                    instance.restoreDatabase();
                    instance.accountDaoObject().restoreMedia();
                    AccountEntity accountEntity = instance.accountDaoObject().getAllAccountEntity().get(0);
                    int id = accountEntity.getId();
                    String currencySymbol = accountEntity.getCurrencySymbol();
                    SharePreferenceHelper.setAccount(BackUpSDCard.this.getApplicationContext(), id, currencySymbol, accountEntity.getName());
                    BackUpSDCard.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            BackUpSDCard.this.dialog.dismiss();
                            Toast.makeText(BackUpSDCard.this.getApplicationContext(), R.string.restore_success, Toast.LENGTH_SHORT).show();
                            BackUpSDCard.this.finishAffinity();
                            BackUpSDCard.this.startActivity(new Intent(BackUpSDCard.this.getApplicationContext(), BaseActivity.class));
                        }
                    });
                } else {
                    Toast.makeText(BackUpSDCard.this.getApplicationContext(), BackUpSDCard.this.getResources().getString(R.string.restore_fail, 40006), 1).show();
                    BackUpSDCard.this.dialog.dismiss();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
