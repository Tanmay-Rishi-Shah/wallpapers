package com.expance.merapaisa.Utility;

import android.text.InputFilter;
import android.text.Spanned;

/* loaded from: classes3.dex */
public class RateInputFilter implements InputFilter {
    private final String DOT = ".";
    private final double mMax;
    private final int mMaxDigitsAfterLength;
    private final int mMaxIntegerDigitsLength;

    public RateInputFilter(int maxDigitsBeforeDot, int maxDigitsAfterDot, double maxValue) {
        this.mMaxIntegerDigitsLength = maxDigitsBeforeDot;
        this.mMaxDigitsAfterLength = maxDigitsAfterDot;
        this.mMax = maxValue;
    }

    @Override // android.text.InputFilter
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String allText = getAllText(source, dest, dstart);
        String onlyDigitsPart = getOnlyDigitsPart(allText);
        if (allText.isEmpty()) {
            return null;
        }
        try {
            return checkMaxValueRule(Double.parseDouble(onlyDigitsPart), onlyDigitsPart);
        } catch (NumberFormatException unused) {
            return "";
        }
    }

    private CharSequence checkMaxValueRule(double enteredValue, String onlyDigitsText) {
        return enteredValue > this.mMax ? "" : handleInputRules(onlyDigitsText);
    }

    private CharSequence handleInputRules(String onlyDigitsText) {
        if (isDecimalDigit(onlyDigitsText)) {
            return checkRuleForDecimalDigits(onlyDigitsText);
        }
        return checkRuleForIntegerDigits(onlyDigitsText.length());
    }

    private boolean isDecimalDigit(String onlyDigitsText) {
        return onlyDigitsText.contains(".");
    }

    private CharSequence checkRuleForDecimalDigits(String onlyDigitsPart) {
        if (onlyDigitsPart.substring(onlyDigitsPart.indexOf("."), onlyDigitsPart.length() - 1).length() > this.mMaxDigitsAfterLength) {
            return "";
        }
        return null;
    }

    private CharSequence checkRuleForIntegerDigits(int allTextLength) {
        if (allTextLength > this.mMaxIntegerDigitsLength) {
            return "";
        }
        return null;
    }

    private String getOnlyDigitsPart(String text) {
        return text.replaceAll("[^0-9?!\\.]", "");
    }

    private String getAllText(CharSequence source, Spanned dest, int dstart) {
        if (dest.toString().isEmpty()) {
            return "";
        }
        if (source.toString().isEmpty()) {
            return deleteCharAtIndex(dest, dstart);
        }
        return new StringBuilder(dest).insert(dstart, source).toString();
    }

    private String deleteCharAtIndex(Spanned dest, int dstart) {
        StringBuilder sb = new StringBuilder(dest);
        sb.deleteCharAt(dstart);
        return sb.toString();
    }
}
