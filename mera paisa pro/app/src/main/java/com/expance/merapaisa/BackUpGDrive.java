package com.expance.merapaisa;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.sqlite.db.SimpleSQLiteQuery;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.expance.merapaisa.BackUp.BackUpHelper;
import com.expance.merapaisa.BackUp.BackUpPreference;
import com.expance.merapaisa.BackUp.GDrive.DriveServiceHelper;
import com.expance.merapaisa.Database.AppDatabaseObject;
import com.expance.merapaisa.Database.Entity.AccountEntity;
import com.expance.merapaisa.Utility.Helper;
import com.expance.merapaisa.Utility.SharePreferenceHelper;
import java.io.File;
import java.util.Collections;
import java.util.concurrent.Executors;

import vocsy.ads.GoogleAds;

/* loaded from: classes3.dex */
public class BackUpGDrive extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private static final int BACKUP = 1;
    private static final int GOOGLE_DRIVE_REQUEST = 2;
    private static final int GOOGLE_REQUEST = 1;
    public static final int RESTORE = 2;
    ConstraintLayout autoBackupWrapper;
    TextView backUpLabel;
    Button backupButton;
    DriveServiceHelper driveService;
    boolean isProgressing;
    Button loginButton;
    ProgressBar progressBar;
    Button restoreButton;
    GoogleSignInClient signInClient;
    SwitchCompat switchView;
    TextView titleLabel;
    public boolean isBackup = false;
    public boolean isRestore = false;

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
        setContentView(R.layout.activity_back_up_gdrive);
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
        configureLayout();
    }

    private void configureLayout() {
        GoogleSignInAccount lastSignedInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (lastSignedInAccount == null) {
            this.titleLabel.setText(R.string.not_sign_in);
            this.loginButton.setText(R.string.login);
            this.backUpLabel.setVisibility(8);
            this.backupButton.setEnabled(false);
            this.restoreButton.setEnabled(false);
            this.autoBackupWrapper.setVisibility(8);
            this.backupButton.setBackgroundResource(R.drawable.background_button_disable_state);
            this.restoreButton.setBackgroundResource(R.drawable.background_button_disable_state);
            return;
        }
        this.autoBackupWrapper.setVisibility(0);
        this.titleLabel.setText(lastSignedInAccount.getEmail());
        this.loginButton.setText(getResources().getString(R.string.logout, lastSignedInAccount.getDisplayName()));
        this.backupButton.setEnabled(true);
        this.restoreButton.setEnabled(true);
        this.backupButton.setBackgroundResource(R.drawable.background_button_state);
        this.restoreButton.setBackgroundResource(R.drawable.background_button_state);
        boolean z = BackUpPreference.getAutoBackup(getApplicationContext()) == 1;
        long retreiveGDriveBackUpTime = BackUpPreference.retreiveGDriveBackUpTime(getApplicationContext());
        if (retreiveGDriveBackUpTime != 0) {
            this.backUpLabel.setVisibility(0);
            this.backUpLabel.setText(getResources().getString(R.string.last_backup, BackUpHelper.getLastSyncDate(this, Long.valueOf(retreiveGDriveBackUpTime))));
        } else {
            this.backUpLabel.setVisibility(8);
        }
        this.switchView.setChecked(z);
    }

    private void setUpLayout() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.backup_title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.autoBackupWrapper = (ConstraintLayout) findViewById(R.id.autoBackupWrapper);
        this.switchView = (SwitchCompat) findViewById(R.id.switchView);
        this.titleLabel = (TextView) findViewById(R.id.titleLabel);
        this.backUpLabel = (TextView) findViewById(R.id.backUpLabel);
        this.loginButton = (Button) findViewById(R.id.loginButton);
        this.backupButton = (Button) findViewById(R.id.backupButton);
        this.restoreButton = (Button) findViewById(R.id.restoreButton);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.progressBar = progressBar;
        progressBar.setVisibility(8);
        this.loginButton.setOnClickListener(this);
        this.backupButton.setOnClickListener(this);
        this.restoreButton.setOnClickListener(this);
        this.switchView.setOnCheckedChangeListener(this);
        this.signInClient = GoogleSignIn.getClient((Activity) this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestScopes(new Scope("https://www.googleapis.com/auth/drive.file"), new Scope[0]).build());
        configureLayout();
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
    public void onClick(View view) {
        GoogleSignInAccount lastSignedInAccount;
        if (this.isProgressing) {
            return;
        }
        if (view.getId() == R.id.loginButton) {
            if (GoogleSignIn.getLastSignedInAccount(this) == null) {
                startActivityForResult(this.signInClient.getSignInIntent(), 1);
            } else {
                Helper.showDialog(this, getResources().getString(R.string.logout_title), getResources().getString(R.string.logout_hint), getResources().getString(R.string.yes), getResources().getString(R.string.no), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.BackUpGDrive$$ExternalSyntheticLambda14
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        BackUpGDrive.this.m164lambda$onClick$1$comktwappswalletmanagerBackUpGDrive(dialogInterface, i);
                    }
                });
            }
        } else if (view.getId() == R.id.backupButton) {
            GoogleSignInAccount lastSignedInAccount2 = GoogleSignIn.getLastSignedInAccount(this);
            if (lastSignedInAccount2 != null) {
                if (lastSignedInAccount2.getGrantedScopes().contains(new Scope("https://www.googleapis.com/auth/drive.file"))) {
                    Helper.showDialog(this, getResources().getString(R.string.backup), getResources().getString(R.string.google_drive_backup), getResources().getString(R.string.yes), getResources().getString(R.string.no), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.BackUpGDrive$$ExternalSyntheticLambda15
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            BackUpGDrive.this.m165lambda$onClick$2$comktwappswalletmanagerBackUpGDrive(dialogInterface, i);
                        }
                    });
                    return;
                }
                this.isBackup = true;
                startActivityForResult(this.signInClient.getSignInIntent(), 1);
            }
        } else if (view.getId() != R.id.restoreButton || (lastSignedInAccount = GoogleSignIn.getLastSignedInAccount(this)) == null) {
        } else {
            if (lastSignedInAccount.getGrantedScopes().contains(new Scope("https://www.googleapis.com/auth/drive.file"))) {
                Helper.showDialog(this, getResources().getString(R.string.restore), getResources().getString(R.string.google_drive_restore), getResources().getString(R.string.yes), getResources().getString(R.string.no), new DialogInterface.OnClickListener() { // from class: com.ktwapps.walletmanager.BackUpGDrive$$ExternalSyntheticLambda16
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        BackUpGDrive.this.m166lambda$onClick$3$comktwappswalletmanagerBackUpGDrive(dialogInterface, i);
                    }
                });
                return;
            }
            this.isRestore = true;
            startActivityForResult(this.signInClient.getSignInIntent(), 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onClick$1$com-ktwapps-walletmanager-BackUpGDrive  reason: not valid java name */
    public /* synthetic */ void m164lambda$onClick$1$comktwappswalletmanagerBackUpGDrive(DialogInterface dialogInterface, int i) {
        this.signInClient.signOut().addOnCompleteListener(this, new OnCompleteListener() { // from class: com.ktwapps.walletmanager.BackUpGDrive$$ExternalSyntheticLambda12
            @Override // com.google.android.gms.tasks.OnCompleteListener
            public final void onComplete(Task task) {
                BackUpGDrive.this.m163lambda$onClick$0$comktwappswalletmanagerBackUpGDrive(task);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onClick$0$com-ktwapps-walletmanager-BackUpGDrive  reason: not valid java name */
    public /* synthetic */ void m163lambda$onClick$0$comktwappswalletmanagerBackUpGDrive(Task task) {
        configureLayout();
        BackUpPreference.setAutoBackup(getApplicationContext(), 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onClick$2$com-ktwapps-walletmanager-BackUpGDrive  reason: not valid java name */
    public /* synthetic */ void m165lambda$onClick$2$comktwappswalletmanagerBackUpGDrive(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        checkExpiry(1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onClick$3$com-ktwapps-walletmanager-BackUpGDrive  reason: not valid java name */
    public /* synthetic */ void m166lambda$onClick$3$comktwappswalletmanagerBackUpGDrive(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        checkExpiry(2);
    }

    private void createDriveService(GoogleSignInAccount account) {
        GoogleAccountCredential usingOAuth2 = GoogleAccountCredential.usingOAuth2(getApplicationContext(), Collections.singleton("https://www.googleapis.com/auth/drive.file"));
        usingOAuth2.setSelectedAccount(account.getAccount());
        this.driveService = new DriveServiceHelper(new Drive.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), usingOAuth2).setApplicationName("Money Manager").build());
    }

    private void checkExpiry(final int type) {
        GoogleSignInAccount lastSignedInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (lastSignedInAccount != null) {
            if (lastSignedInAccount.isExpired()) {
                this.isProgressing = true;
                this.progressBar.setVisibility(0);
                this.signInClient.silentSignIn().onSuccessTask(new SuccessContinuation() { // from class: com.ktwapps.walletmanager.BackUpGDrive$$ExternalSyntheticLambda21
                    @Override // com.google.android.gms.tasks.SuccessContinuation
                    public final Task then(Object obj) {
                        try {
                            return BackUpGDrive.this.m160lambda$checkExpiry$4$comktwappswalletmanagerBackUpGDrive(type, (GoogleSignInAccount) obj);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() { // from class: com.ktwapps.walletmanager.BackUpGDrive$$ExternalSyntheticLambda22
                    @Override // com.google.android.gms.tasks.OnFailureListener
                    public final void onFailure(Exception exc) {
                        BackUpGDrive.this.m162lambda$checkExpiry$6$comktwappswalletmanagerBackUpGDrive(exc);
                    }
                });
                return;
            }
            createDriveService(lastSignedInAccount);
            if (type == 2) {
                restore();
            } else {
                backup();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$checkExpiry$4$com-ktwapps-walletmanager-BackUpGDrive  reason: not valid java name */
    public /* synthetic */ Task m160lambda$checkExpiry$4$comktwappswalletmanagerBackUpGDrive(int i, GoogleSignInAccount googleSignInAccount) throws Exception {
        createDriveService(googleSignInAccount);
        if (i == 2) {
            restore();
            return null;
        }
        backup();
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$checkExpiry$6$com-ktwapps-walletmanager-BackUpGDrive  reason: not valid java name */
    public /* synthetic */ void m162lambda$checkExpiry$6$comktwappswalletmanagerBackUpGDrive(Exception exc) {
        this.progressBar.setVisibility(8);
        this.isProgressing = false;
        if (exc instanceof ApiException) {
            if (((ApiException) exc).getStatusCode() == 4) {
                this.signInClient.signOut().addOnCompleteListener(this, new OnCompleteListener() { // from class: com.ktwapps.walletmanager.BackUpGDrive$$ExternalSyntheticLambda3
                    @Override // com.google.android.gms.tasks.OnCompleteListener
                    public final void onComplete(Task task) {
                        BackUpGDrive.this.m161lambda$checkExpiry$5$comktwappswalletmanagerBackUpGDrive(task);
                    }
                });
                return;
            } else {
                Toast.makeText(this, (int) R.string.drive_known_hint, 1).show();
                return;
            }
        }
        Toast.makeText(this, (int) R.string.drive_known_hint, 1).show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$checkExpiry$5$com-ktwapps-walletmanager-BackUpGDrive  reason: not valid java name */
    public /* synthetic */ void m161lambda$checkExpiry$5$comktwappswalletmanagerBackUpGDrive(Task task) {
        configureLayout();
        BackUpPreference.setAutoBackup(getApplicationContext(), 0);
        Toast.makeText(this, (int) R.string.drive_session_hint, 1).show();
    }

    private void backup() {
        this.isProgressing = true;
        this.progressBar.setVisibility(0);
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.BackUpGDrive$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                BackUpGDrive.this.m156lambda$backup$18$comktwappswalletmanagerBackUpGDrive();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$backup$18$com-ktwapps-walletmanager-BackUpGDrive  reason: not valid java name */
    public /* synthetic */ void m156lambda$backup$18$comktwappswalletmanagerBackUpGDrive() {
        AppDatabaseObject.getInstance(getApplicationContext()).accountDaoObject().checkpoint(new SimpleSQLiteQuery("pragma wal_checkpoint(full)"));
        runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.BackUpGDrive$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                BackUpGDrive.this.m155lambda$backup$17$comktwappswalletmanagerBackUpGDrive();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$backup$17$com-ktwapps-walletmanager-BackUpGDrive  reason: not valid java name */
    public /* synthetic */ void m155lambda$backup$17$comktwappswalletmanagerBackUpGDrive() {
        this.driveService.createFolder().addOnSuccessListener(new OnSuccessListener() { // from class: com.ktwapps.walletmanager.BackUpGDrive$$ExternalSyntheticLambda0
            @Override // com.google.android.gms.tasks.OnSuccessListener
            public final void onSuccess(Object obj) {
                BackUpGDrive.this.m153lambda$backup$15$comktwappswalletmanagerBackUpGDrive((String) obj);
            }
        }).addOnFailureListener(new OnFailureListener() { // from class: com.ktwapps.walletmanager.BackUpGDrive$$ExternalSyntheticLambda11
            @Override // com.google.android.gms.tasks.OnFailureListener
            public final void onFailure(Exception exc) {
                BackUpGDrive.this.m154lambda$backup$16$comktwappswalletmanagerBackUpGDrive(exc);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$backup$15$com-ktwapps-walletmanager-BackUpGDrive  reason: not valid java name */
    public /* synthetic */ void m153lambda$backup$15$comktwappswalletmanagerBackUpGDrive(final String str) {
        final File databasePath = getDatabasePath("wallet-database");
        this.driveService.searchFile(str).addOnSuccessListener(new OnSuccessListener() { // from class: com.ktwapps.walletmanager.BackUpGDrive$$ExternalSyntheticLambda19
            @Override // com.google.android.gms.tasks.OnSuccessListener
            public final void onSuccess(Object obj) {
                BackUpGDrive.this.m151lambda$backup$13$comktwappswalletmanagerBackUpGDrive(databasePath, str, (String) obj);
            }
        }).addOnFailureListener(new OnFailureListener() { // from class: com.ktwapps.walletmanager.BackUpGDrive$$ExternalSyntheticLambda20
            @Override // com.google.android.gms.tasks.OnFailureListener
            public final void onFailure(Exception exc) {
                BackUpGDrive.this.m152lambda$backup$14$comktwappswalletmanagerBackUpGDrive(exc);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$backup$13$com-ktwapps-walletmanager-BackUpGDrive  reason: not valid java name */
    public /* synthetic */ void m151lambda$backup$13$comktwappswalletmanagerBackUpGDrive(final File file, final String str, String str2) {
        if (str2 == null) {
            this.driveService.uploadFile(file, str).addOnSuccessListener(new OnSuccessListener() { // from class: com.ktwapps.walletmanager.BackUpGDrive$$ExternalSyntheticLambda4
                @Override // com.google.android.gms.tasks.OnSuccessListener
                public final void onSuccess(Object obj) {
                    BackUpGDrive.this.m157lambda$backup$7$comktwappswalletmanagerBackUpGDrive((String) obj);
                }
            }).addOnFailureListener(new OnFailureListener() { // from class: com.ktwapps.walletmanager.BackUpGDrive$$ExternalSyntheticLambda5
                @Override // com.google.android.gms.tasks.OnFailureListener
                public final void onFailure(Exception exc) {
                    BackUpGDrive.this.m158lambda$backup$8$comktwappswalletmanagerBackUpGDrive(exc);
                }
            });
        } else {
            this.driveService.deleteFile(str2).addOnSuccessListener(new OnSuccessListener() { // from class: com.ktwapps.walletmanager.BackUpGDrive$$ExternalSyntheticLambda6
                @Override // com.google.android.gms.tasks.OnSuccessListener
                public final void onSuccess(Object obj) {
                    BackUpGDrive.this.m149lambda$backup$11$comktwappswalletmanagerBackUpGDrive(file, str, (Void) obj);
                }
            }).addOnFailureListener(new OnFailureListener() { // from class: com.ktwapps.walletmanager.BackUpGDrive$$ExternalSyntheticLambda7
                @Override // com.google.android.gms.tasks.OnFailureListener
                public final void onFailure(Exception exc) {
                    BackUpGDrive.this.m150lambda$backup$12$comktwappswalletmanagerBackUpGDrive(exc);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$backup$7$com-ktwapps-walletmanager-BackUpGDrive  reason: not valid java name */
    public /* synthetic */ void m157lambda$backup$7$comktwappswalletmanagerBackUpGDrive(String str) {
        showBackupSucceed();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$backup$8$com-ktwapps-walletmanager-BackUpGDrive  reason: not valid java name */
    public /* synthetic */ void m158lambda$backup$8$comktwappswalletmanagerBackUpGDrive(Exception exc) {
        showBackupFailed(50003);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$backup$11$com-ktwapps-walletmanager-BackUpGDrive  reason: not valid java name */
    public /* synthetic */ void m149lambda$backup$11$comktwappswalletmanagerBackUpGDrive(File file, String str, Void r3) {
        this.driveService.uploadFile(file, str).addOnSuccessListener(new OnSuccessListener() { // from class: com.ktwapps.walletmanager.BackUpGDrive$$ExternalSyntheticLambda17
            @Override // com.google.android.gms.tasks.OnSuccessListener
            public final void onSuccess(Object obj) {
                BackUpGDrive.this.m159lambda$backup$9$comktwappswalletmanagerBackUpGDrive((String) obj);
            }
        }).addOnFailureListener(new OnFailureListener() { // from class: com.ktwapps.walletmanager.BackUpGDrive$$ExternalSyntheticLambda18
            @Override // com.google.android.gms.tasks.OnFailureListener
            public final void onFailure(Exception exc) {
                BackUpGDrive.this.m148lambda$backup$10$comktwappswalletmanagerBackUpGDrive(exc);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$backup$9$com-ktwapps-walletmanager-BackUpGDrive  reason: not valid java name */
    public /* synthetic */ void m159lambda$backup$9$comktwappswalletmanagerBackUpGDrive(String str) {
        showBackupSucceed();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$backup$10$com-ktwapps-walletmanager-BackUpGDrive  reason: not valid java name */
    public /* synthetic */ void m148lambda$backup$10$comktwappswalletmanagerBackUpGDrive(Exception exc) {
        showBackupFailed(50003);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$backup$12$com-ktwapps-walletmanager-BackUpGDrive  reason: not valid java name */
    public /* synthetic */ void m150lambda$backup$12$comktwappswalletmanagerBackUpGDrive(Exception exc) {
        showBackupFailed(50004);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$backup$14$com-ktwapps-walletmanager-BackUpGDrive  reason: not valid java name */
    public /* synthetic */ void m152lambda$backup$14$comktwappswalletmanagerBackUpGDrive(Exception exc) {
        showBackupFailed(50002);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$backup$16$com-ktwapps-walletmanager-BackUpGDrive  reason: not valid java name */
    public /* synthetic */ void m154lambda$backup$16$comktwappswalletmanagerBackUpGDrive(Exception exc) {
        showBackupFailed(50001);
    }

    private void restore() {
        this.isProgressing = true;
        this.progressBar.setVisibility(0);
        this.driveService.createFolder().addOnSuccessListener(new OnSuccessListener() { // from class: com.ktwapps.walletmanager.BackUpGDrive$$ExternalSyntheticLambda1
            @Override // com.google.android.gms.tasks.OnSuccessListener
            public final void onSuccess(Object obj) {
                BackUpGDrive.this.m171lambda$restore$23$comktwappswalletmanagerBackUpGDrive((String) obj);
            }
        }).addOnFailureListener(new OnFailureListener() { // from class: com.ktwapps.walletmanager.BackUpGDrive$$ExternalSyntheticLambda2
            @Override // com.google.android.gms.tasks.OnFailureListener
            public final void onFailure(Exception exc) {
                BackUpGDrive.this.m172lambda$restore$24$comktwappswalletmanagerBackUpGDrive(exc);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$restore$23$com-ktwapps-walletmanager-BackUpGDrive  reason: not valid java name */
    public /* synthetic */ void m171lambda$restore$23$comktwappswalletmanagerBackUpGDrive(String str) {
        this.driveService.searchFile(str).addOnSuccessListener(new OnSuccessListener() { // from class: com.ktwapps.walletmanager.BackUpGDrive$$ExternalSyntheticLambda23
            @Override // com.google.android.gms.tasks.OnSuccessListener
            public final void onSuccess(Object obj) {
                BackUpGDrive.this.m169lambda$restore$21$comktwappswalletmanagerBackUpGDrive((String) obj);
            }
        }).addOnFailureListener(new OnFailureListener() { // from class: com.ktwapps.walletmanager.BackUpGDrive$$ExternalSyntheticLambda24
            @Override // com.google.android.gms.tasks.OnFailureListener
            public final void onFailure(Exception exc) {
                BackUpGDrive.this.m170lambda$restore$22$comktwappswalletmanagerBackUpGDrive(exc);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$restore$21$com-ktwapps-walletmanager-BackUpGDrive  reason: not valid java name */
    public /* synthetic */ void m169lambda$restore$21$comktwappswalletmanagerBackUpGDrive(String str) {
        if (str == null) {
            Toast.makeText(getApplicationContext(), (int) R.string.backup_empty, 0).show();
            this.progressBar.setVisibility(8);
            this.isProgressing = false;
            return;
        }
        File file = new File(getFilesDir() + "/temp");
        final File file2 = new File(file, "temp.db");
        if (!file.exists()) {
            file.mkdirs();
        }
        this.driveService.downloadFile(file2, str).addOnSuccessListener(new OnSuccessListener() { // from class: com.ktwapps.walletmanager.BackUpGDrive$$ExternalSyntheticLambda8
            @Override // com.google.android.gms.tasks.OnSuccessListener
            public final void onSuccess(Object obj) {
                BackUpGDrive.this.m167lambda$restore$19$comktwappswalletmanagerBackUpGDrive(file2, (Void) obj);
            }
        }).addOnFailureListener(new OnFailureListener() { // from class: com.ktwapps.walletmanager.BackUpGDrive$$ExternalSyntheticLambda9
            @Override // com.google.android.gms.tasks.OnFailureListener
            public final void onFailure(Exception exc) {
                BackUpGDrive.this.m168lambda$restore$20$comktwappswalletmanagerBackUpGDrive(exc);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$restore$19$com-ktwapps-walletmanager-BackUpGDrive  reason: not valid java name */
    public /* synthetic */ void m167lambda$restore$19$comktwappswalletmanagerBackUpGDrive(File file, Void r3) {
        if (BackUpHelper.isValidSQLite(file)) {
            if (BackUpHelper.validateDB(file.getAbsolutePath())) {
                if (BackUpHelper.restore(getApplicationContext(), file)) {
                    this.progressBar.setVisibility(8);
                    this.isProgressing = false;
                    Toast.makeText(getApplicationContext(), (int) R.string.restore_success, 1).show();
                    Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.BackUpGDrive.1
                        @Override // java.lang.Runnable
                        public void run() {
                            AppDatabaseObject appDatabaseObject = AppDatabaseObject.getInstance(BackUpGDrive.this.getApplicationContext());
                            appDatabaseObject.accountDaoObject().checkpoint(new SimpleSQLiteQuery("pragma wal_checkpoint(full)"));
                            appDatabaseObject.restoreDatabase();
                            AppDatabaseObject appDatabaseObject2 = AppDatabaseObject.getInstance(BackUpGDrive.this.getApplicationContext());
                            appDatabaseObject2.accountDaoObject().restoreMedia();
                            AccountEntity accountEntity = appDatabaseObject2.accountDaoObject().getAllAccountEntity().get(0);
                            SharePreferenceHelper.setAccount(BackUpGDrive.this.getApplicationContext(), accountEntity.getId(), accountEntity.getCurrencySymbol(), accountEntity.getName());
                            BackUpGDrive.this.runOnUiThread(new Runnable() { // from class: com.ktwapps.walletmanager.BackUpGDrive.1.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    BackUpGDrive.this.finishAffinity();
                                    BackUpGDrive.this.startActivity(new Intent(BackUpGDrive.this.getApplicationContext(), BaseActivity.class));
                                }
                            });
                        }
                    });
                    return;
                }
                return;
            }
            restoreFailed(60005);
            return;
        }
        restoreFailed(60004);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$restore$20$com-ktwapps-walletmanager-BackUpGDrive  reason: not valid java name */
    public /* synthetic */ void m168lambda$restore$20$comktwappswalletmanagerBackUpGDrive(Exception exc) {
        restoreFailed(60003);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$restore$22$com-ktwapps-walletmanager-BackUpGDrive  reason: not valid java name */
    public /* synthetic */ void m170lambda$restore$22$comktwappswalletmanagerBackUpGDrive(Exception exc) {
        restoreFailed(60002);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$restore$24$com-ktwapps-walletmanager-BackUpGDrive  reason: not valid java name */
    public /* synthetic */ void m172lambda$restore$24$comktwappswalletmanagerBackUpGDrive(Exception exc) {
        restoreFailed(60001);
    }

    private void restoreFailed(int code) {
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.restore_fail, Integer.valueOf(code)), 0).show();
        this.progressBar.setVisibility(8);
        this.isProgressing = false;
    }

    private void showBackupFailed(int code) {
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.backup_fail, Integer.valueOf(code)), 0).show();
        this.progressBar.setVisibility(8);
        this.isProgressing = false;
    }

    private void showBackupSucceed() {
        Toast.makeText(getApplicationContext(), (int) R.string.backup_success, 0).show();
        BackUpPreference.putGDriveBackUpTime(this, System.currentTimeMillis());
        long retreiveGDriveBackUpTime = BackUpPreference.retreiveGDriveBackUpTime(getApplicationContext());
        if (retreiveGDriveBackUpTime != 0) {
            this.backUpLabel.setVisibility(0);
            this.backUpLabel.setText(getResources().getString(R.string.last_backup, BackUpHelper.getLastSyncDate(this, Long.valueOf(retreiveGDriveBackUpTime))));
        } else {
            this.backUpLabel.setVisibility(8);
        }
        this.progressBar.setVisibility(8);
        this.isProgressing = false;
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == -1) {
                GoogleSignIn.getSignedInAccountFromIntent(data);
                configureLayout();
                if (this.isBackup) {
                    checkExpiry(1);
                } else if (this.isRestore) {
                    checkExpiry(2);
                }
            } else {
                Toast.makeText(this, (int) R.string.drive_permission_hint, 1).show();
            }
            this.isBackup = false;
            this.isRestore = false;
        }
    }

    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        BackUpPreference.setAutoBackup(this, isChecked ? 1 : 0);
    }
}
