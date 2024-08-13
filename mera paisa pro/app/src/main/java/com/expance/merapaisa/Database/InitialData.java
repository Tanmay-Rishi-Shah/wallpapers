package com.expance.merapaisa.Database;

import android.content.Context;
import com.expance.merapaisa.Database.Entity.CategoryEntity;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class InitialData {
    public static List<CategoryEntity> getCategoryExpenseData(Context context, int accountId) {
        String[] strArr = {"#34BFFF", "#0077C5", "#00A6A4", "#00C1BF", "#7FD000", "#FFCA00", "#FFAD00", "#EE4036", "#9C005E", "#652D90", "#9457FA", "#E31C9E", "#8B4513", "#5E4138"};
        int[] iArr = {1, 2, 144, 28, 66, 111, 11, 57, 80, 78, 10, 39, 100, 146};
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (i < 14) {
            int i2 = i + 1;
            arrayList.add(new CategoryEntity("", strArr[i], iArr[i], 2, 0, i, accountId, i2));
            i = i2;
        }
        arrayList.add(new CategoryEntity("", "#FF250B", 165, 2, 1, 0, accountId, 24));
        arrayList.add(new CategoryEntity("", "#FF2861", 166, 2, 1, 0, accountId, 25));
        arrayList.add(new CategoryEntity("", "#FF5877", 167, 2, 1, 0, accountId, 26));
        return arrayList;
    }

    public static List<CategoryEntity> getCategoryIncomeData(Context context, int accountId) {
        String[] strArr = {"#34BFFF", "#016165", "#00C1BF", "#FFCA00", "#FFAD00", "#EE4036", "#9C005E", "#652D90", "#9457FA"};
        int[] iArr = {158, 160, 152, 153, 162, 159, 164, 161, 146};
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < 9; i++) {
            arrayList.add(new CategoryEntity("", strArr[i], iArr[i], 1, 0, i, accountId, i + 15));
        }
        arrayList.add(new CategoryEntity("", "#3485FF", 165, 1, 1, 0, accountId, 24));
        arrayList.add(new CategoryEntity("", "#0D8EFF", 168, 1, 1, 0, accountId, 27));
        arrayList.add(new CategoryEntity("", "#69B9FF", 169, 1, 1, 0, accountId, 28));
        return arrayList;
    }
}
