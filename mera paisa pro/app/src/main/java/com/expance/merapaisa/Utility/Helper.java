package com.expance.merapaisa.Utility;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.util.TypedValue;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.TextViewCompat;
import com.expance.merapaisa.R;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes3.dex */
public class Helper {
    public static float convertDpToPixel(Context context, float dp) {
        return dp * (context.getResources().getDisplayMetrics().densityDpi / 160.0f);
    }

    private static String getFormattedAmount(long amount) {
        if (amount == 0) {
            return "0";
        }
        BigDecimal stripTrailingZeros = new BigDecimal(amount).divide(new BigDecimal(100)).setScale(2, RoundingMode.DOWN).stripTrailingZeros();
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(Locale.getDefault());
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(0);
        decimalFormat.setGroupingUsed(false);
        String format = decimalFormat.format(stripTrailingZeros);
        if (format.contains(".") || format.contains(",")) {
            return String.format(Locale.getDefault(), "%,.2f", stripTrailingZeros);
        }
        decimalFormat.setGroupingUsed(true);
        return decimalFormat.format(stripTrailingZeros);
    }

    public static int getAttributeColor(Context context, int resource) {
        try {
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(resource, typedValue, true);
            return typedValue.data;
        } catch (Exception e) {
            e.printStackTrace();
            return R.color.backgroundSecondary;
        }
    }

    public static int getAttributeDrawable(Context context, int resource) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(resource, typedValue, true);
        return typedValue.resourceId;
    }

    public static String getExportName(int type) {
        Date time = Calendar.getInstance().getTime();
        StringBuilder sb = new StringBuilder();
        sb.append(type == 0 ? "CSV_" : "EXCEL_");
        sb.append(new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.ENGLISH).format(time));
        return sb.toString();
    }

    public static String getBeautifyAmount(String symbol, long amount) {
        boolean z = 0 > amount;
        StringBuilder sb = new StringBuilder();
        sb.append(z ? "-" : "");
        sb.append(symbol);
        sb.append(StringUtils.SPACE);
        if (z) {
            amount = -amount;
        }
        sb.append(getFormattedAmount(amount));
        return sb.toString();
    }

    public static String getBeautifyAmount(long amount) {
        BigDecimal stripTrailingZeros = new BigDecimal(amount).divide(new BigDecimal(100)).setScale(2, 1).stripTrailingZeros();
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ENGLISH);
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(0);
        decimalFormat.setGroupingUsed(false);
        String format = decimalFormat.format(stripTrailingZeros);
        return format.contains(".") ? String.format(Locale.ENGLISH, "%,.2f", stripTrailingZeros) : format;
    }

    public static String getBeautifyRate(String code1, String code2, float rate, long amount) {
        return getFormattedAmount(amount) + StringUtils.SPACE + code1 + " = " + getFormattedAmount((long) (((float) amount) * rate)) + StringUtils.SPACE + code2;
    }

    public static String getBeautifyRate(String code1, String code2, long amount1, long amount2) {
        return getFormattedAmount(amount1) + StringUtils.SPACE + code1 + " = " + getFormattedAmount(amount2) + StringUtils.SPACE + code2;
    }

    public static String getExportAmount(long amount) {
        return String.format(Locale.ENGLISH, "%.2f", new BigDecimal(amount).divide(new BigDecimal(100)));
    }

    public static int getProgressValue(long amount, long deduct) {
        if (deduct >= amount) {
            return 100;
        }
        if (deduct <= 0) {
            return 0;
        }
        return (int) Math.round((deduct / amount) * 100.0d);
    }

    public static String getProgressDoubleValue(long amount, long deduct) {
        if (deduct >= amount) {
            return "100.00%";
        }
        if (deduct <= 0) {
            return "0.00%";
        }
        return String.format(Locale.getDefault(), "%.2f", Double.valueOf((deduct / amount) * 100.0d)) + "%";
    }

    public static long getLongFromString(String s) {
        return new BigDecimal(s).multiply(new BigDecimal(100)).longValue();
    }

    public static void showDialog(final Context context, String title, String message, String positive, String negative, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        if (title.length() > 0) {
            builder.setTitle(title);
        }
        builder.setPositiveButton(positive, listener);
        builder.setNegativeButton(negative, (DialogInterface.OnClickListener) null);
        final AlertDialog create = builder.create();
        create.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.ktwapps.walletmanager.Utility.Helper.1
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface dialogInterface) {
                create.getButton(-1).setTextColor(context.getResources().getColor(R.color.blue));
                create.getButton(-2).setTextColor(context.getResources().getColor(R.color.blue));
            }
        });
        create.show();
    }

    public static void shrinkTextView(int dp, TextView textView) {
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(textView, 1, dp, 1, 2);
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}
