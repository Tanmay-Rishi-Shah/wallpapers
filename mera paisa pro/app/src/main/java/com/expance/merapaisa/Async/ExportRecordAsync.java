package com.expance.merapaisa.Async;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.expance.merapaisa.Model.Exports;
import com.expance.merapaisa.R;
import com.expance.merapaisa.Utility.DateHelper;
import com.expance.merapaisa.Utility.Helper;
import com.opencsv.CSVWriter;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes3.dex */
public class ExportRecordAsync extends AsyncTask<String, String, Boolean> {
    WeakReference<Activity> activity;
    File baseFile;
    File csvFile;
    int format;
    boolean memoryErr = false;
    List<Exports> recordList;
    File xlsFile;

    public ExportRecordAsync(Activity activity, List<Exports> list, int format) {
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

    @Override
    protected Boolean doInBackground(String... var1) {
        Activity var6 = (Activity) this.activity.get();
        Boolean var10 = false;
        if (var6 != null) {
            if ((double) ((new File(this.baseFile.getAbsoluteFile().toString())).getFreeSpace() / 1048576L) < 0.1) {
                this.memoryErr = true;
            } else if (this.recordList.size() > 0) {
                Exception var10000;
                label336:
                {
                    int var2;
                    boolean var10001;
                    try {
                        var2 = this.format;
                    } catch (Exception var55) {
                        var10000 = var55;
                        var10001 = false;
                        break label336;
                    }

                    String var4 = " -> ";
                    String var14;
                    String var57;
                    String var66;
                    if (var2 == 0) {
                        try {
                            if (this.csvFile.exists()) {
                                this.csvFile.delete();
                            }
                        } catch (Exception var43) {
                            var10000 = var43;
                            var10001 = false;
                            break label336;
                        }

                        try {
                            if (!this.csvFile.createNewFile()) {
                                return var10;
                            }
                        } catch (IOException var54) {
                            var10000 = var54;
                            var10001 = false;
                            break label336;
                        }

                        Iterator var70;
                        CSVWriter var71;
                        try {
                            FileOutputStream var62 = new FileOutputStream(this.csvFile);
                            OutputStreamWriter var60 = new OutputStreamWriter(var62, StandardCharsets.UTF_8);
                            var71 = new CSVWriter(var60);
                            var71.writeNext(new String[]{var6.getResources().getString(2131820738), var6.getResources().getString(2131821195), var6.getResources().getString(2131820654), var6.getResources().getString(2131820600), var6.getResources().getString(2131820728), var6.getResources().getString(2131821002), var6.getResources().getString(2131821211)});
                            var70 = this.recordList.iterator();
                        } catch (IOException var42) {
                            var10000 = var42;
                            var10001 = false;
                            break label336;
                        }

                        while (true) {
                            String var72;
                            Exports var73;
                            label220:
                            {
                                label324:
                                {
                                    Resources var61;
                                    label325:
                                    {
                                        label217:
                                        {
                                            try {
                                                if (!var70.hasNext()) {
                                                    break;
                                                }

                                                var73 = (Exports) var70.next();
                                                var72 = DateHelper.getFormattedDateTime(var6, var73.getDatetime());
                                                if (var73.getType() != 0) {
                                                    break label217;
                                                }

                                                var61 = var6.getResources();
                                            } catch (Exception var45) {
                                                var10000 = var45;
                                                var10001 = false;
                                                break label336;
                                            }

                                            var2 = 2131820848;
                                            break label325;
                                        }

                                        try {
                                            if (var73.getType() != 1) {
                                                break label324;
                                            }

                                            var61 = var6.getResources();
                                        } catch (Exception var44) {
                                            var10000 = var44;
                                            var10001 = false;
                                            break label336;
                                        }

                                        var2 = 2131820793;
                                    }

                                    try {
                                        var57 = var61.getString(var2);
                                        break label220;
                                    } catch (Exception var41) {
                                        var10000 = var41;
                                        var10001 = false;
                                        break label336;
                                    }
                                }

                                try {
                                    var57 = var6.getResources().getString(2131821190);
                                } catch (Exception var40) {
                                    var10000 = var40;
                                    var10001 = false;
                                    break label336;
                                }
                            }

                            label337:
                            {
                                try {
                                    if (var73.getType() == 2) {
                                        var4 = var6.getResources().getString(2131821190);
                                        break label337;
                                    }
                                } catch (Exception var46) {
                                    var10000 = var46;
                                    var10001 = false;
                                    break label336;
                                }

                                try {
                                    var4 = var73.getCategory(var6);
                                } catch (Exception var39) {
                                    var10000 = var39;
                                    var10001 = false;
                                    break label336;
                                }
                            }

                            String var74;
                            try {
                                var74 = Helper.getExportAmount(var73.getAmount());
                                var14 = var73.getMemo();
                                var66 = var73.getWallet();
                            } catch (Exception var38) {
                                var10000 = var38;
                                var10001 = false;
                                break label336;
                            }

                            String var63 = var66;

                            label327:
                            {
                                try {
                                    if (var73.getType() != 2) {
                                        break label327;
                                    }
                                } catch (Exception var48) {
                                    var10000 = var48;
                                    var10001 = false;
                                    break label336;
                                }

                                var63 = var66;

                                try {
                                    if (var73.getTransferWallet() == null) {
                                        break label327;
                                    }
                                } catch (Exception var47) {
                                    var10000 = var47;
                                    var10001 = false;
                                    break label336;
                                }

                                var63 = var66;

                                try {
                                    if (var73.getTransferWallet().length() > 0) {
                                        StringBuilder var65 = new StringBuilder();
                                        var65.append(var66);
                                        var65.append(" -> ");
                                        var65.append(var73.getTransferWallet());
                                        var63 = var65.toString();
                                    }
                                } catch (Exception var37) {
                                    var10000 = var37;
                                    var10001 = false;
                                    break label336;
                                }
                            }

                            try {
                                var71.writeNext(new String[]{var72, var57, var4, var74, var73.getCurrency(), var14, var63});
                            } catch (Exception var36) {
                                var10000 = var36;
                                var10001 = false;
                                break label336;
                            }
                        }

                        try {
                            var71.close();
                        } catch (IOException var35) {
                            var10000 = var35;
                            var10001 = false;
                            break label336;
                        }
                    } else {
                        XSSFSheet var5;
                        XSSFRow var8;
                        String var9;
                        XSSFWorkbook var11;
                        XSSFCellStyle var12;
                        XSSFCellStyle var13;
                        String var15;
                        String var16;
                        String var17;
                        String var19;
                        XSSFCellStyle var56;
                        try {
                            var11 = new XSSFWorkbook();
                            var5 = var11.createSheet();
                            XSSFFont var7 = var11.createFont();
                            var7.setBold(true);
                            var56 = var11.createCellStyle();
                            var56.setFont(var7);
                            var56.setAlignment(HorizontalAlignment.CENTER);
                            var56.setVerticalAlignment(VerticalAlignment.CENTER);
                            var56.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
                            var56.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                            var13 = var11.createCellStyle();
                            var13.setDataFormat(var11.getCreationHelper().createDataFormat().getFormat("dd/MM/yyyy"));
                            var12 = var11.createCellStyle();
                            var12.setDataFormat(var11.createDataFormat().getFormat("#,##0.0#"));
                            var15 = var6.getResources().getString(2131820738);
                            var9 = var6.getResources().getString(2131821195);
                            var16 = var6.getResources().getString(2131820654);
                            var19 = var6.getResources().getString(2131820600);
                            var17 = var6.getResources().getString(2131820728);
                            var66 = var6.getResources().getString(2131821002);
                            var14 = var6.getResources().getString(2131821211);
                            var8 = var5.createRow(0);
                        } catch (Exception var34) {
                            var10000 = var34;
                            var10001 = false;
                            break label336;
                        }

                        for (var2 = 0; var2 < 7; ++var2) {
                            try {
                                var5.setColumnWidth(var2, 4608);
                            } catch (Exception var33) {
                                var10000 = var33;
                                var10001 = false;
                                break label336;
                            }
                        }

                        for (var2 = 0; var2 < 7; ++var2) {
                            try {
                                XSSFCell var18 = var8.createCell(var2);
                                var18.setCellStyle(var56);
                                var18.setCellValue((new String[]{var15, var9, var16, var19, var17, var66, var14})[var2]);
                            } catch (Exception var32) {
                                var10000 = var32;
                                var10001 = false;
                                break label336;
                            }
                        }

                        Iterator var75;
                        try {
                            var75 = this.recordList.iterator();
                        } catch (Exception var31) {
                            var10000 = var31;
                            var10001 = false;
                            break label336;
                        }

                        var2 = 1;

                        while (true) {
                            XSSFRow var76;
                            Exports var77;
                            label331:
                            {
                                try {
                                    if (!var75.hasNext()) {
                                        break;
                                    }

                                    var77 = (Exports) var75.next();
                                    var76 = var5.createRow(var2);
                                    var17 = DateHelper.getFormattedDateTime(var6, var77.getDatetime());
                                    if (var77.getType() == 0) {
                                        var57 = var6.getResources().getString(2131820848);
                                        break label331;
                                    }
                                } catch (Exception var49) {
                                    var10000 = var49;
                                    var10001 = false;
                                    break label336;
                                }

                                try {
                                    if (var77.getType() == 1) {
                                        var57 = var6.getResources().getString(2131820793);
                                        break label331;
                                    }
                                } catch (Exception var50) {
                                    var10000 = var50;
                                    var10001 = false;
                                    break label336;
                                }

                                try {
                                    var57 = var6.getResources().getString(2131821190);
                                } catch (Exception var30) {
                                    var10000 = var30;
                                    var10001 = false;
                                    break label336;
                                }
                            }

                            label338:
                            {
                                try {
                                    if (var77.getType() == 2) {
                                        var66 = var6.getResources().getString(2131821190);
                                        break label338;
                                    }
                                } catch (Exception var51) {
                                    var10000 = var51;
                                    var10001 = false;
                                    break label336;
                                }

                                try {
                                    var66 = var77.getCategory(var6);
                                } catch (Exception var29) {
                                    var10000 = var29;
                                    var10001 = false;
                                    break label336;
                                }
                            }

                            String var78;
                            try {
                                var78 = Helper.getExportAmount(var77.getAmount());
                                var19 = var77.getMemo();
                                var9 = var77.getWallet();
                            } catch (Exception var28) {
                                var10000 = var28;
                                var10001 = false;
                                break label336;
                            }

                            String var67 = var9;

                            label332:
                            {
                                try {
                                    if (var77.getType() != 2) {
                                        break label332;
                                    }
                                } catch (Exception var53) {
                                    var10000 = var53;
                                    var10001 = false;
                                    break label336;
                                }

                                var67 = var9;

                                try {
                                    if (var77.getTransferWallet() == null) {
                                        break label332;
                                    }
                                } catch (Exception var52) {
                                    var10000 = var52;
                                    var10001 = false;
                                    break label336;
                                }

                                var67 = var9;

                                try {
                                    if (var77.getTransferWallet().length() > 0) {
                                        StringBuilder var68 = new StringBuilder();
                                        var68.append(var9);
                                        var68.append(var4);
                                        var68.append(var77.getTransferWallet());
                                        var67 = var68.toString();
                                    }
                                } catch (Exception var27) {
                                    var10000 = var27;
                                    var10001 = false;
                                    break label336;
                                }
                            }

                            String var20;
                            String[] var69;
                            try {
                                var20 = var77.getCurrency();
                                var69 = new String[7];
                            } catch (Exception var26) {
                                var10000 = var26;
                                var10001 = false;
                                break label336;
                            }

                            var69[0] = var17;
                            var69[1] = var57;
                            var69[2] = var66;
                            var69[3] = var78;
                            var69[4] = var20;
                            var69[5] = var19;
                            var69[6] = var67;

                            for (int var3 = 0; var3 < 7; ++var3) {
                                XSSFCell var58;
                                try {
                                    var58 = var76.createCell(var3);
                                } catch (Exception var25) {
                                    var10000 = var25;
                                    var10001 = false;
                                    break label336;
                                }

                                if (var3 == 0) {
                                    try {
                                        var58.setCellStyle(var13);
                                        var58.setCellValue(var77.getDatetime());
                                    } catch (Exception var24) {
                                        var10000 = var24;
                                        var10001 = false;
                                        break label336;
                                    }
                                } else if (var3 == 3) {
                                    try {
                                        var58.setCellStyle(var12);
                                        var58.setCellValue(Double.parseDouble(var69[var3]));
                                    } catch (Exception var23) {
                                        var10000 = var23;
                                        var10001 = false;
                                        break label336;
                                    }
                                } else {
                                    try {
                                        var58.setCellValue(var69[var3]);
                                    } catch (Exception var22) {
                                        var10000 = var22;
                                        var10001 = false;
                                        break label336;
                                    }
                                }
                            }

                            ++var2;
                        }

                        try {
                            FileOutputStream var59 = new FileOutputStream(this.xlsFile);
                            var11.write(var59);
                            var59.close();
                        } catch (IOException var21) {
                            var10000 = var21;
                            var10001 = false;
                            break label336;
                        }
                    }

                    return true;
                }

                Exception var64 = var10000;
                var64.printStackTrace();
            }
        }

        return var10;
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
