package com.expance.merapaisa.Database.Converter;

import java.util.Date;

/* loaded from: classes3.dex */
public class DateConverter {
    public static Date toDate(Long dateLong) {
        if (dateLong == null) {
            return null;
        }
        return new Date(dateLong.longValue());
    }

    public static Long fromDate(Date date) {
        if (date == null) {
            return null;
        }
        return Long.valueOf(date.getTime());
    }
}
