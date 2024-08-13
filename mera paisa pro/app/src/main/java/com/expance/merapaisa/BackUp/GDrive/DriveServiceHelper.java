package com.expance.merapaisa.BackUp.GDrive;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.apache.http.protocol.HTTP;

/* loaded from: classes3.dex */
public class DriveServiceHelper {
    private static final String DIR_NAME = "Money Managers";
    private Drive drive;
    private Executor executor = Executors.newSingleThreadExecutor();

    public DriveServiceHelper(Drive driveService) {
        this.drive = driveService;
    }

    public Task<String> uploadFile(final File localFile, final String folderId) {
        return Tasks.call(this.executor, new Callable<String>() { // from class: com.ktwapps.walletmanager.BackUp.GDrive.DriveServiceHelper.1
            @Override // java.util.concurrent.Callable
            public String call() throws Exception {
                List<String> singletonList;
                String str = folderId;
                if (str == null) {
                    singletonList = Collections.singletonList("root");
                } else {
                    singletonList = Collections.singletonList(str);
                }
                return DriveServiceHelper.this.drive.files().create(new com.google.api.services.drive.model.File().setParents(singletonList).setMimeType(HTTP.PLAIN_TEXT_TYPE).setName("wallet-database"), new FileContent("application/vnd.google-apps.file", localFile)).execute().getId();
            }
        });
    }

    public Task<Void> deleteFile(final String fileId) {
        return Tasks.call(this.executor, new Callable<Void>() { // from class: com.ktwapps.walletmanager.BackUp.GDrive.DriveServiceHelper.2
            @Override // java.util.concurrent.Callable
            public Void call() throws Exception {
                if (fileId != null) {
                    DriveServiceHelper.this.drive.files().delete(fileId).execute();
                    return null;
                }
                return null;
            }
        });
    }

    public Task<String> createFolder() {
        return Tasks.call(this.executor, new Callable<String>() { // from class: com.ktwapps.walletmanager.BackUp.GDrive.DriveServiceHelper.3
            @Override // java.util.concurrent.Callable
            public String call() throws Exception {
                List<String> singletonList = Collections.singletonList("root");
                FileList execute = DriveServiceHelper.this.drive.files().list().setQ("mimeType='application/vnd.google-apps.folder' and trashed=false and name = 'Money Managers'").execute();
                if (execute.getFiles().size() > 0) {
                    return execute.getFiles().get(0).getId();
                }
                return DriveServiceHelper.this.drive.files().create(new com.google.api.services.drive.model.File().setParents(singletonList).setMimeType("application/vnd.google-apps.folder").setName(DriveServiceHelper.DIR_NAME)).execute().getId();
            }
        });
    }

    public Task<Void> downloadFile(final File targetFile, final String fileId) {
        return Tasks.call(this.executor, new Callable<Void>() { // from class: com.ktwapps.walletmanager.BackUp.GDrive.DriveServiceHelper.4
            @Override // java.util.concurrent.Callable
            public Void call() throws Exception {
                DriveServiceHelper.this.drive.files().get(fileId).executeMediaAndDownloadTo(new FileOutputStream(targetFile));
                return null;
            }
        });
    }

    public Task<String> searchFile(final String fileId) {
        return Tasks.call(this.executor, new Callable<String>() { // from class: com.ktwapps.walletmanager.BackUp.GDrive.DriveServiceHelper.5
            @Override // java.util.concurrent.Callable
            public String call() throws Exception {
                Drive.Files.List list = DriveServiceHelper.this.drive.files().list();
                FileList execute = list.setQ("'" + fileId + "' in parents and name = 'wallet-database' and mimeType ='text/plain' and trashed=false").execute();
                if (execute.getFiles().size() > 0) {
                    return execute.getFiles().get(0).getId();
                }
                return null;
            }
        });
    }
}
