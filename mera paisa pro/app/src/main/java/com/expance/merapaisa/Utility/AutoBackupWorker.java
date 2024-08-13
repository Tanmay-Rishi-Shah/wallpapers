package com.expance.merapaisa.Utility;

import android.content.Context;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.expance.merapaisa.BackUp.BackUpPreference;
import com.expance.merapaisa.BackUp.GDrive.DriveServiceHelper;
import com.expance.merapaisa.Database.AppDatabaseObject;
import java.io.File;
import java.util.Collections;
import java.util.concurrent.Executors;

/* loaded from: classes3.dex */
public class AutoBackupWorker extends Worker {
    Context context;
    DriveServiceHelper driveService;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$backup$1(Exception exc) {
    }

    public AutoBackupWorker(Context context, WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @Override // androidx.work.Worker
    public ListenableWorker.Result doWork() {
        backup();
        return ListenableWorker.Result.success();
    }

    private void backup() {
        GoogleSignInAccount lastSignedInAccount = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (lastSignedInAccount != null && lastSignedInAccount.getGrantedScopes().contains(new Scope("https://www.googleapis.com/auth/drive.file"))) {
            GoogleSignInClient client = GoogleSignIn.getClient(this.context, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestScopes(new Scope("https://www.googleapis.com/auth/drive.file"), new Scope[0]).build());
            if (lastSignedInAccount.isExpired()) {
                client.silentSignIn().onSuccessTask(new SuccessContinuation() { // from class: com.ktwapps.walletmanager.Utility.AutoBackupWorker$$ExternalSyntheticLambda5
                    @Override // com.google.android.gms.tasks.SuccessContinuation
                    public final Task then(Object obj) {
                        try {
                            return AutoBackupWorker.this.m232x8470a79b((GoogleSignInAccount) obj);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() { // from class: com.ktwapps.walletmanager.Utility.AutoBackupWorker$$ExternalSyntheticLambda6
                    @Override // com.google.android.gms.tasks.OnFailureListener
                    public final void onFailure(Exception exc) {
                        AutoBackupWorker.lambda$backup$1(exc);
                    }
                });
            } else {
                backupToDrive(lastSignedInAccount);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$backup$0$com-ktwapps-walletmanager-Utility-AutoBackupWorker  reason: not valid java name */
    public /* synthetic */ Task m232x8470a79b(GoogleSignInAccount googleSignInAccount) throws Exception {
        backupToDrive(googleSignInAccount);
        return null;
    }

    private void backupToDrive(GoogleSignInAccount account) {
        GoogleAccountCredential usingOAuth2 = GoogleAccountCredential.usingOAuth2(getApplicationContext(), Collections.singleton("https://www.googleapis.com/auth/drive.file"));
        usingOAuth2.setSelectedAccount(account.getAccount());
        this.driveService = new DriveServiceHelper(new Drive.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), usingOAuth2).setApplicationName("Money Manager").build());
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.ktwapps.walletmanager.Utility.AutoBackupWorker$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                AutoBackupWorker.this.m238x8adaf083();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$backupToDrive$7$com-ktwapps-walletmanager-Utility-AutoBackupWorker  reason: not valid java name */
    public /* synthetic */ void m238x8adaf083() {
        AppDatabaseObject.getInstance(getApplicationContext()).accountDaoObject().checkpoint(new SimpleSQLiteQuery("pragma wal_checkpoint(full)"));
        this.driveService.createFolder().addOnSuccessListener(new OnSuccessListener() { // from class: com.ktwapps.walletmanager.Utility.AutoBackupWorker$$ExternalSyntheticLambda0
            @Override // com.google.android.gms.tasks.OnSuccessListener
            public final void onSuccess(Object obj) {
                AutoBackupWorker.this.m237x5d025624((String) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$backupToDrive$6$com-ktwapps-walletmanager-Utility-AutoBackupWorker  reason: not valid java name */
    public /* synthetic */ void m237x5d025624(final String str) {
        final File databasePath = getApplicationContext().getDatabasePath("wallet-database");
        this.driveService.searchFile(str).addOnSuccessListener(new OnSuccessListener() { // from class: com.ktwapps.walletmanager.Utility.AutoBackupWorker$$ExternalSyntheticLambda1
            @Override // com.google.android.gms.tasks.OnSuccessListener
            public final void onSuccess(Object obj) {
                AutoBackupWorker.this.m236x2f29bbc5(databasePath, str, (String) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$backupToDrive$2$com-ktwapps-walletmanager-Utility-AutoBackupWorker  reason: not valid java name */
    public /* synthetic */ void m233xa59feca8(String str) {
        BackUpPreference.putGDriveBackUpTime(getApplicationContext(), System.currentTimeMillis());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$backupToDrive$5$com-ktwapps-walletmanager-Utility-AutoBackupWorker  reason: not valid java name */
    public /* synthetic */ void m236x2f29bbc5(final File file, final String str, String str2) {
        if (str2 == null) {
            this.driveService.uploadFile(file, str).addOnSuccessListener(new OnSuccessListener() { // from class: com.ktwapps.walletmanager.Utility.AutoBackupWorker$$ExternalSyntheticLambda3
                @Override // com.google.android.gms.tasks.OnSuccessListener
                public final void onSuccess(Object obj) {
                    AutoBackupWorker.this.m233xa59feca8((String) obj);
                }
            });
        } else {
            this.driveService.deleteFile(str2).addOnSuccessListener(new OnSuccessListener() { // from class: com.ktwapps.walletmanager.Utility.AutoBackupWorker$$ExternalSyntheticLambda4
                @Override // com.google.android.gms.tasks.OnSuccessListener
                public final void onSuccess(Object obj) {
                    AutoBackupWorker.this.m235x1512166(file, str, (Void) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$backupToDrive$4$com-ktwapps-walletmanager-Utility-AutoBackupWorker  reason: not valid java name */
    public /* synthetic */ void m235x1512166(File file, String str, Void r3) {
        this.driveService.uploadFile(file, str).addOnSuccessListener(new OnSuccessListener() { // from class: com.ktwapps.walletmanager.Utility.AutoBackupWorker$$ExternalSyntheticLambda2
            @Override // com.google.android.gms.tasks.OnSuccessListener
            public final void onSuccess(Object obj) {
                AutoBackupWorker.this.m234xd3788707((String) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$backupToDrive$3$com-ktwapps-walletmanager-Utility-AutoBackupWorker  reason: not valid java name */
    public /* synthetic */ void m234xd3788707(String str) {
        BackUpPreference.putGDriveBackUpTime(getApplicationContext(), System.currentTimeMillis());
    }
}
