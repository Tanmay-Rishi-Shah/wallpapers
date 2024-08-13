package com.expance.merapaisa.Async;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;
import androidx.core.content.FileProvider;
import com.expance.merapaisa.Model.ExportCategory;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.Helper;
import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/* loaded from: classes3.dex */
public class ExportCategoryAsync extends AsyncTask<String, String, Boolean> {
    WeakReference<Activity> activity;
    File baseFile;
    File csvFile;
    int format;
    boolean memoryErr = false;
    List<ExportCategory> recordList;
    File xlsFile;

    public ExportCategoryAsync(Activity activity, List<ExportCategory> list, int format) {
        this.activity = new WeakReference<>(activity);
        this.recordList = list;
        this.format = format;
    }

    @Override // android.os.AsyncTask
    protected void onPreExecute() {
        super.onPreExecute();
        Activity activity = this.activity.get();
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 29) {
            if ("mounted".equals(Environment.getExternalStorageState())) {
                File externalFilesDir = activity.getExternalFilesDir(null);
                if (externalFilesDir == null) {
                    this.baseFile = new File(activity.getFilesDir().getAbsolutePath() + File.separator + activity.getPackageName());
                } else {
                    this.baseFile = new File(externalFilesDir.getAbsolutePath() + File.separator + activity.getPackageName());
                }
            } else {
                this.baseFile = new File(activity.getFilesDir().getAbsolutePath() + File.separator + activity.getPackageName());
            }
        } else {
            this.baseFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + activity.getPackageName());
        }
        if (!this.baseFile.exists()) {
            this.baseFile.mkdir();
        }
        File file = this.baseFile;
        this.csvFile = new File(file, Helper.getExportName(0) + ".csv");
        File file2 = this.baseFile;
        this.xlsFile = new File(file2, Helper.getExportName(1) + ".xls");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public Boolean doInBackground(final String... args) {
        Activity activity = this.activity.get();
        char c = 0;
        if (activity != null) {
            if (new File(this.baseFile.getAbsoluteFile().toString()).getFreeSpace() / 1048576 < 0.1d) {
                this.memoryErr = true;
            } else if (this.recordList.size() > 0) {
                try {
                    char c2 = 2;
                    int i = 3;
                    if (this.format == 0) {
                        if ((!this.csvFile.exists() || this.csvFile.delete()) && this.csvFile.createNewFile()) {
                            CSVWriter cSVWriter = new CSVWriter(new OutputStreamWriter(new FileOutputStream(this.csvFile), StandardCharsets.UTF_8));
                            cSVWriter.writeNext(new String[]{activity.getResources().getString(R.string.category), activity.getResources().getString(R.string.total), activity.getResources().getString(R.string.type)});
                            for (ExportCategory exportCategory : this.recordList) {
                                cSVWriter.writeNext(new String[]{exportCategory.getName(activity), Helper.getExportAmount(exportCategory.getAmount()), exportCategory.getType() == 1 ? activity.getResources().getString(R.string.income) : activity.getResources().getString(R.string.expense)});
                            }
                            cSVWriter.close();
                        } else {
                            return false;
                        }
                    } else {
                        XSSFWorkbook xSSFWorkbook = new XSSFWorkbook();
                        XSSFSheet createSheet = xSSFWorkbook.createSheet();
                        XSSFFont createFont = xSSFWorkbook.createFont();
                        createFont.setBold(true);
                        XSSFCellStyle createCellStyle = xSSFWorkbook.createCellStyle();
                        createCellStyle.setFont(createFont);
                        createCellStyle.setAlignment(HorizontalAlignment.CENTER);
                        createCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                        createCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
                        createCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        XSSFCellStyle createCellStyle2 = xSSFWorkbook.createCellStyle();
                        createCellStyle2.setDataFormat(xSSFWorkbook.createDataFormat().getFormat("#,##0.0#"));
                        String[] strArr = {activity.getResources().getString(R.string.category), activity.getResources().getString(R.string.total), activity.getResources().getString(R.string.type)};
                        XSSFRow createRow = createSheet.createRow(0);
                        for (int i2 = 0; i2 < 3; i2++) {
                            createSheet.setColumnWidth(i2, 4608);
                        }
                        for (int i3 = 0; i3 < 3; i3++) {
                            XSSFCell createCell = createRow.createCell(i3);
                            createCell.setCellStyle(createCellStyle);
                            createCell.setCellValue(strArr[i3]);
                        }
                        int i4 = 1;
                        for (ExportCategory exportCategory2 : this.recordList) {
                            XSSFRow createRow2 = createSheet.createRow(i4);
                            String string = exportCategory2.getType() == 1 ? activity.getResources().getString(R.string.income) : activity.getResources().getString(R.string.expense);
                            String name = exportCategory2.getName(activity);
                            String exportAmount = Helper.getExportAmount(exportCategory2.getAmount());
                            String[] strArr2 = new String[i];
                            strArr2[c] = name;
                            strArr2[1] = exportAmount;
                            strArr2[c2] = string;
                            int i5 = 0;
                            while (i5 < i) {
                                XSSFCell createCell2 = createRow2.createCell(i5);
                                String str = strArr2[i5];
                                if (i5 == 1) {
                                    createCell2.setCellStyle(createCellStyle2);
                                    createCell2.setCellValue(Double.parseDouble(str));
                                } else {
                                    createCell2.setCellValue(str);
                                }
                                i5++;
                                i = 3;
                            }
                            i4++;
                            c = 0;
                            c2 = 2;
                            i = 3;
                        }
                        FileOutputStream fileOutputStream = new FileOutputStream(this.xlsFile);
                        xSSFWorkbook.write(fileOutputStream);
                        fileOutputStream.close();
                    }
                    return true;
                } catch (IOException unused) {
                }
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(Boolean success) {
        Uri uriForFile;
        Activity activity = this.activity.get();
        if (activity != null) {
            if (success.booleanValue()) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                if (this.format == 1) {
                    Context applicationContext = activity.getApplicationContext();
                    uriForFile = FileProvider.getUriForFile(applicationContext, activity.getApplicationContext().getPackageName() + ".fileProvider", this.xlsFile);
                    intent.setType("application/xls");
                } else {
                    Context applicationContext2 = activity.getApplicationContext();
                    uriForFile = FileProvider.getUriForFile(applicationContext2, activity.getApplicationContext().getPackageName() + ".fileProvider", this.csvFile);
                    intent.setType("application/csv");
                }
                intent.putExtra("android.intent.extra.STREAM", uriForFile);
                activity.startActivity(intent);
                return;
            }
            Toast.makeText(activity, (int) R.string.export_error, 1).show();
        }
    }
}
