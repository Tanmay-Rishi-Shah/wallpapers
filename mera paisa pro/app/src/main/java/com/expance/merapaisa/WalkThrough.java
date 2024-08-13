package com.expance.merapaisa;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.expance.merapaisa.Adapter.MultiChoiceRestoreImageAdapter;
import com.expance.merapaisa.BackUp.BackUpHelper;
import com.expance.merapaisa.BackUp.BackUpPreference;
import com.expance.merapaisa.BackUp.GDrive.DriveServiceHelper;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.Entity.AccountEntity;
import com.expance.merapaisa.Utility.DataHelper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.concurrent.Executors;

import vocsy.ads.ExitScreen;

/* loaded from: classes3.dex */
public class WalkThrough extends AppCompatActivity implements View.OnClickListener {
    private static final int FILE_REQUEST = 2;
    private static final int GOOGLE_REQUEST = 1;
    AlertDialog dialog;
    ImageView getStartedButton;
    ImageView getStartedButton1;
    private MyViewPagerAdapter myViewPagerAdapter;
    private int[] layouts;
    TextView restoreButton;
    TabLayout tabLayout;
    ViewPager viewPager;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(1);
        }
        setContentView(R.layout.activity_walk_through);
        SystemConfiguration.setTransparentStatusBar(this, SystemConfiguration.IconColor.ICON_DARK);
//        if (Build.VERSION.SDK_INT >= 23) {
//            getWindow().getDecorView().setSystemUiVisibility(8192);
//        }
//        if (Build.VERSION.SDK_INT >= 26) {
//            getWindow().getDecorView().setSystemUiVisibility(8208);
//            getWindow().setNavigationBarColor(Color.parseColor("#F8F8F8"));
//        } else {
//            getWindow().setNavigationBarColor(Color.parseColor("#000000"));
//        }
        viewPager = findViewById(R.id.view_pager);
        layouts = new int[]{R.layout.welcome_slide1, R.layout.welcome_slide2, R.layout.welcome_slide3};

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        this.getStartedButton = (ImageView) findViewById(R.id.getStartedButton);
        this.getStartedButton1 = (ImageView) findViewById(R.id.getStartedButton1);
        getStartedButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(+1);
                if (current < layouts.length) {
                    viewPager.setCurrentItem(current);
                } else {
                    getStartedButton.setOnClickListener(this);
                }
            }
        });

        this.restoreButton = (TextView) findViewById(R.id.restoreButton);
        this.getStartedButton.setOnClickListener(this);
        SharePreferenceHelper.setTransactionShowCaseKey(this, 0);
        SharePreferenceHelper.setWalletShowCaseKey(this, 0);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.dialog_progress_bar);
        this.dialog = builder.create();
        this.restoreButton.setOnClickListener(this);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        if (v.getId() == R.id.getStartedButton) {
            startActivity(new Intent(getApplicationContext(), AccountCreateName.class));
            overridePendingTransition(R.anim.left_to_right, R.anim.scale_out);
            return;
        }
        int themeMode = SharePreferenceHelper.getThemeMode(this);
        int i = R.style.AppThemeNight;
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, themeMode == 1 ? R.style.AppThemeNight : R.style.AppTheme));
        builder.setTitle(R.string.restore_data);
        if (SharePreferenceHelper.getThemeMode(this) != 1) {
            i = R.style.AppTheme;
        }
        builder.setAdapter(new MultiChoiceRestoreImageAdapter(new ContextThemeWrapper(this, i), DataHelper.getRestoreData(this), DataHelper.getRestoreIconData()), null);
        final AlertDialog create = builder.create();
        create.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.ktwapps.walletmanager.WalkThrough.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i2, long l) {
                if (i2 == 0) {
                    WalkThrough.this.checkPermission();
                } else if (i2 == 1) {
                    WalkThrough.this.restoreFromGDrive();
                }
                create.dismiss();
            }
        });
        create.show();
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {


    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            if (position == 2) {
                getStartedButton1.setVisibility(View.GONE);
                getStartedButton.setVisibility(View.VISIBLE);
            }
            if (position == layouts.length - 1) {
//                btnNext.setText(getString(R.string.start));
//                btnSkip.setVisibility(View.GONE);

            } else {
//                btnNext.setText(getString(R.string.next));
//                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    @Override
    // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == -1) {
                restoreFromGDrive();
            } else {
                Toast.makeText(this, (int) R.string.drive_permission_hint, 1).show();
            }
        } else if (requestCode == 2 && resultCode == -1) {
            this.dialog.show();
            final Uri data2 = data.getData();
            if (data2 != null) {
                Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        InputStream inputStream = null;
                        FileOutputStream fileOutputStream = null;
                        try {
                            File tempDir = new File(WalkThrough.this.getFilesDir(), "temp");
                            if (!tempDir.exists()) {
                                tempDir.mkdirs();
                            }
                            File tempDbFile = new File(tempDir, "temp.db");
                            inputStream = WalkThrough.this.getContentResolver().openInputStream(data2);
                            fileOutputStream = new FileOutputStream(tempDbFile);
                            byte[] buffer = new byte[1024];
                            int bytesRead;
                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                fileOutputStream.write(buffer, 0, bytesRead);
                            }
                            if (!BackUpHelper.isValidSQLite(tempDbFile)) {
                                WalkThrough.this.runOnUiThread(() -> {
                                    Toast.makeText(WalkThrough.this.getApplicationContext(), R.string.restore_fail, 1).show();
                                    WalkThrough.this.dialog.dismiss();
                                });
                                return;
                            }
                            if (!BackUpHelper.validateDB(tempDbFile.getAbsolutePath())) {
                                WalkThrough.this.runOnUiThread(() -> {
                                    Toast.makeText(WalkThrough.this.getApplicationContext(), R.string.restore_fail, 1).show();
                                    WalkThrough.this.dialog.dismiss();
                                });
                                return;
                            }
                            if (!BackUpHelper.restore(WalkThrough.this.getApplicationContext(), tempDbFile)) {
                                WalkThrough.this.runOnUiThread(() -> {
                                    Toast.makeText(WalkThrough.this.getApplicationContext(), R.string.restore_fail, 1).show();
                                    WalkThrough.this.dialog.dismiss();
                                });
                                return;
                            }
                            AppDatabaseObject instance = AppDatabaseObject.getInstance(WalkThrough.this.getApplicationContext());
                            instance.accountDaoObject().checkpoint(new SimpleSQLiteQuery("pragma wal_checkpoint(full)"));
                            instance.restoreDatabase();
                            instance.accountDaoObject().restoreMedia();
                            AccountEntity accountEntity = instance.accountDaoObject().getAllAccountEntity().get(0);
                            SharePreferenceHelper.setAccount(WalkThrough.this.getApplicationContext(), accountEntity.getId(), accountEntity.getCurrencySymbol(), accountEntity.getName());
                            WalkThrough.this.runOnUiThread(() -> {
                                WalkThrough.this.dialog.dismiss();
                                Toast.makeText(WalkThrough.this.getApplicationContext(), R.string.restore_success, 1).show();
                                WalkThrough.this.finishAffinity();
                                WalkThrough.this.startActivity(new Intent(WalkThrough.this.getApplicationContext(), BaseActivity.class));
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                            WalkThrough.this.runOnUiThread(() -> {
                                Toast.makeText(WalkThrough.this.getApplicationContext(), R.string.restore_fail, 1).show();
                                WalkThrough.this.dialog.dismiss();
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            WalkThrough.this.runOnUiThread(() -> {
                                Toast.makeText(WalkThrough.this.getApplicationContext(), R.string.restore_fail, 1).show();
                                WalkThrough.this.dialog.dismiss();
                            });
                        } finally {
                            try {
                                if (inputStream != null) {
                                    inputStream.close();
                                }
                                if (fileOutputStream != null) {
                                    fileOutputStream.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                return;
            }
            Toast.makeText(getApplicationContext(), (int) R.string.restore_fail, 1).show();
            this.dialog.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void restoreFromGDrive() {
        GoogleSignInAccount lastSignedInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (lastSignedInAccount == null) {
            startActivityForResult(GoogleSignIn.getClient((Activity) this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestScopes(new Scope("https://www.googleapis.com/auth/drive.file"), new Scope[0]).build()).getSignInIntent(), 1);
        } else if (lastSignedInAccount.getGrantedScopes().contains(new Scope("https://www.googleapis.com/auth/drive.file"))) {
            restoreFromGDrive(lastSignedInAccount);
        } else {
            startActivityForResult(GoogleSignIn.getClient((Activity) this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestScopes(new Scope("https://www.googleapis.com/auth/drive.file"), new Scope[0]).build()).getSignInIntent(), 1);
        }
    }

    private void restoreFromGDrive(GoogleSignInAccount account) {
        this.dialog.show();
        GoogleAccountCredential usingOAuth2 = GoogleAccountCredential.usingOAuth2(getApplicationContext(), Collections.singleton("https://www.googleapis.com/auth/drive.file"));
        usingOAuth2.setSelectedAccount(account.getAccount());
        DriveServiceHelper driveServiceHelper = new DriveServiceHelper(new Drive.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), usingOAuth2).setApplicationName("Money Manager").build());
        driveServiceHelper.createFolder().addOnSuccessListener(new AnonymousClass4(driveServiceHelper)).addOnFailureListener(new OnFailureListener() { // from class: com.ktwapps.walletmanager.WalkThrough.3
            @Override // com.google.android.gms.tasks.OnFailureListener
            public void onFailure(Exception e) {
                WalkThrough.this.dialog.dismiss();
                Toast.makeText(WalkThrough.this.getApplicationContext(), (int) R.string.restore_fail, 0).show();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.ktwapps.walletmanager.WalkThrough$4  reason: invalid class name */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 implements OnSuccessListener<String> {
        final /* synthetic */ DriveServiceHelper val$driveService;

        AnonymousClass4(final DriveServiceHelper val$driveService) {
            this.val$driveService = val$driveService;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: com.ktwapps.walletmanager.WalkThrough$4$2  reason: invalid class name */
        /* loaded from: classes3.dex */
        public class AnonymousClass2 implements OnSuccessListener<String> {
            AnonymousClass2() {
            }

            @Override // com.google.android.gms.tasks.OnSuccessListener
            public void onSuccess(String s) {
                if (s == null) {
                    WalkThrough.this.dialog.dismiss();
                    Toast.makeText(WalkThrough.this.getApplicationContext(), (int) R.string.backup_empty, 0).show();
                    GoogleSignIn.getClient((Activity) WalkThrough.this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()).signOut().addOnCompleteListener(WalkThrough.this, new OnCompleteListener() { // from class: com.ktwapps.walletmanager.WalkThrough$4$2$$ExternalSyntheticLambda0
                        @Override // com.google.android.gms.tasks.OnCompleteListener
                        public final void onComplete(Task task) {
                            WalkThrough.AnonymousClass4.AnonymousClass2.this.m249lambda$onSuccess$0$comktwappswalletmanagerWalkThrough$4$2(task);
                        }
                    });
                    return;
                }
                File file = new File(WalkThrough.this.getFilesDir() + "/temp");
                File file2 = new File(file, "temp.db");
                if (!file.exists()) {
                    file.mkdirs();
                }
                AnonymousClass4.this.val$driveService.downloadFile(file2, s).addOnSuccessListener(new C00822(file2)).addOnFailureListener(new OnFailureListener() { // from class: com.ktwapps.walletmanager.WalkThrough.4.2.1
                    @Override // com.google.android.gms.tasks.OnFailureListener
                    public void onFailure(Exception e) {
                        WalkThrough.this.dialog.dismiss();
                        Toast.makeText(WalkThrough.this.getApplicationContext(), (int) R.string.restore_fail, 0).show();
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* renamed from: lambda$onSuccess$0$com-ktwapps-walletmanager-WalkThrough$4$2  reason: not valid java name */
            public /* synthetic */ void m249lambda$onSuccess$0$comktwappswalletmanagerWalkThrough$4$2(Task task) {
                BackUpPreference.setAutoBackup(WalkThrough.this.getApplicationContext(), 0);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* renamed from: com.ktwapps.walletmanager.WalkThrough$4$2$2  reason: invalid class name and collision with other inner class name */
            /* loaded from: classes3.dex */
            public class C00822 implements OnSuccessListener<Void> {
                final /* synthetic */ File val$file;

                C00822(final File val$file) {
                    this.val$file = val$file;
                }

                @Override // com.google.android.gms.tasks.OnSuccessListener
                public void onSuccess(Void aVoid) {
                    if (BackUpHelper.isValidSQLite(this.val$file)) {
                        if (BackUpHelper.validateDB(this.val$file.getAbsolutePath())) {
                            if (BackUpHelper.restore(WalkThrough.this.getApplicationContext(), this.val$file)) {
                                WalkThrough.this.dialog.dismiss();
                                Toast.makeText(WalkThrough.this.getApplicationContext(), (int) R.string.restore_success, 1).show();
                                Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.WalkThrough.4.2.2.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(WalkThrough.this.getApplicationContext());
                                        appDatabaseObject.accountDaoObject().checkpoint(new SimpleSQLiteQuery("pragma wal_checkpoint(full)"));
                                        appDatabaseObject.restoreDatabase();
                                        AppDatabaseObject appDatabaseObject2 = AppDatabaseObject.getInstance(WalkThrough.this.getApplicationContext());
                                        appDatabaseObject2.accountDaoObject().restoreMedia();
                                        AccountEntity accountEntity = appDatabaseObject2.accountDaoObject().getAllAccountEntity().get(0);
                                        SharePreferenceHelper.setAccount(WalkThrough.this.getApplicationContext(), accountEntity.getId(), accountEntity.getCurrencySymbol(), accountEntity.getName());
                                        WalkThrough.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.WalkThrough.4.2.2.1.1
                                            @Override // java.lang.Runnable
                                            public void run() {
                                                WalkThrough.this.finishAffinity();
                                                WalkThrough.this.startActivity(new Intent(WalkThrough.this.getApplicationContext(), BaseActivity.class));
                                            }
                                        });
                                    }
                                });
                                return;
                            }
                            return;
                        }
                        WalkThrough.this.dialog.dismiss();
                        Toast.makeText(WalkThrough.this.getApplicationContext(), (int) R.string.restore_fail, 1).show();
                        return;
                    }
                    WalkThrough.this.dialog.dismiss();
                    Toast.makeText(WalkThrough.this.getApplicationContext(), (int) R.string.restore_fail, 1).show();
                }
            }
        }

        @Override // com.google.android.gms.tasks.OnSuccessListener
        public void onSuccess(String s) {
            this.val$driveService.searchFile(s).addOnSuccessListener(new AnonymousClass2()).addOnFailureListener(new OnFailureListener() { // from class: com.ktwapps.walletmanager.WalkThrough.4.1
                @Override // com.google.android.gms.tasks.OnFailureListener
                public void onFailure(Exception e) {
                    WalkThrough.this.dialog.dismiss();
                    Toast.makeText(WalkThrough.this.getApplicationContext(), (int) R.string.restore_fail, 0).show();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            if (ActivityCompat.checkSelfPermission(this, "android.permission.READ_MEDIA_IMAGES") != 0) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_MEDIA_IMAGES")) {
                    showPermissionDialog(getResources().getString(R.string.access_storage_hint), getResources().getString(R.string.allow), getResources().getString(R.string.deny), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.WalkThrough$$ExternalSyntheticLambda2
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            WalkThrough.this.m246lambda$checkPermission$0$comktwappswalletmanagerWalkThrough(dialogInterface, i);
                        }
                    });
                    return;
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_MEDIA_IMAGES"}, 0);
                    return;
                }
            }
            Intent intent = new Intent("android.intent.action.GET_CONTENT");
            intent.setType("*/*");
            startActivityForResult(Intent.createChooser(intent, "Choose a file"), 2);
        } else if (ActivityCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
                showPermissionDialog(getResources().getString(R.string.access_storage_hint), getResources().getString(R.string.allow), getResources().getString(R.string.deny), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.WalkThrough.5
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(WalkThrough.this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 0);
                    }
                });
            } else {
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 0);
            }
        } else {
            Intent intent2 = new Intent("android.intent.action.GET_CONTENT");
            intent2.setType("*/*");
            startActivityForResult(Intent.createChooser(intent2, "Choose a file"), 2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$checkPermission$0$com-ktwapps-walletmanager-WalkThrough  reason: not valid java name */
    public /* synthetic */ void m246lambda$checkPermission$0$comktwappswalletmanagerWalkThrough(DialogInterface dialogInterface, int i) {
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_MEDIA_IMAGES"}, 0);
    }

    @Override
    // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == 0) {
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("*/*");
                startActivityForResult(Intent.createChooser(intent, null), 2);
            } else if (Build.VERSION.SDK_INT >= 33) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_MEDIA_IMAGES")) {
                    return;
                }
                showPermissionDialog(getResources().getString(R.string.access_storage_rational_hint), getResources().getString(R.string.app_settings), getResources().getString(R.string.not_now), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.WalkThrough$$ExternalSyntheticLambda0
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        WalkThrough.this.m247x48966ac1(dialogInterface, i);
                    }
                });
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            } else {
                showPermissionDialog(getResources().getString(R.string.access_storage_rational_hint), getResources().getString(R.string.app_settings), getResources().getString(R.string.not_now), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.WalkThrough$$ExternalSyntheticLambda1
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        WalkThrough.this.m248x11976202(dialogInterface, i);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onRequestPermissionsResult$1$com-ktwapps-walletmanager-WalkThrough  reason: not valid java name */
    public /* synthetic */ void m247x48966ac1(DialogInterface dialogInterface, int i) {
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", getPackageName(), null));
        startActivity(intent);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onRequestPermissionsResult$2$com-ktwapps-walletmanager-WalkThrough  reason: not valid java name */
    public /* synthetic */ void m248x11976202(DialogInterface dialogInterface, int i) {
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
        final AlertDialog create = builder.create();
        create.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.ktwapps.walletmanager.WalkThrough.6
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface dialogInterface) {
                create.getButton(-1).setTextColor(WalkThrough.this.getResources().getColor(R.color.blue));
                create.getButton(-2).setTextColor(WalkThrough.this.getResources().getColor(R.color.blue));
            }
        });
        create.show();
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(WalkThrough.this, ExitScreen.class));
    }
}
