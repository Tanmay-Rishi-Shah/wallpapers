package com.expance.merapaisa.Utility;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.apache.poi.openxml4j.opc.ContentTypes;

/* loaded from: classes3.dex */
public class FileHelper {
    public static File createImageFile(Context context) {
        String format = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        return new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "JPEG_" + format + ".jpg");
    }

    public static String convertBitmaptoFile(File file, Bitmap bitmap) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(byteArray);
        fileOutputStream.flush();
        fileOutputStream.close();
        return file.getAbsolutePath();
    }

    public static void saveToGallery(Context context, Bitmap bitmap, String path) throws IOException {
        OutputStream outputStream;
        if (Build.VERSION.SDK_INT >= 29) {
            ContentResolver contentResolver = context.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put("_display_name", path);
            contentValues.put("mime_type", ContentTypes.IMAGE_PNG);
            contentValues.put("relative_path", "DCIM/WalletManagers");
            outputStream = contentResolver.openOutputStream(contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues));
        } else {
            String str = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + File.separator + "WalletManagers";
            File file = new File(str);
            if (!file.exists()) {
                file.mkdir();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(new File(str, path + ".png"));
            ContentValues contentValues2 = new ContentValues();
            contentValues2.put("mime_type", ContentTypes.IMAGE_PNG);
            contentValues2.put("_data", file.getAbsolutePath());
            context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues2);
            outputStream = fileOutputStream;
        }
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        outputStream.flush();
        outputStream.close();
    }

    public static Bitmap getBitmapFromUri(Context context, Uri uri) {
        if (uri != null) {
            try {
                new BitmapFactory.Options().inPreferredConfig = Bitmap.Config.ARGB_8888;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);
                int i = options.outWidth;
                int i2 = options.outHeight;
                options.inJustDecodeBounds = false;
                options.inSampleSize = Math.min(i / 1000, i2 / 1000);
                return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
