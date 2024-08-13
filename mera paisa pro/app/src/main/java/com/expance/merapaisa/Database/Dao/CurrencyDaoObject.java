package com.expance.merapaisa.Database.Dao;

import androidx.lifecycle.LiveData;
import com.expance.merapaisa.Database.Entity.CurrencyEntity;
import com.expance.merapaisa.Model.Currencies;
import java.util.List;

/* loaded from: classes3.dex */
public interface CurrencyDaoObject {
    void deleteCurrency(int id);

    LiveData<List<Currencies>> getAllCurrencies(int id);

    List<String> getCurrencyCodes(int id);

    CurrencyEntity getCurrencyEntityByCode(int id, String code);

    CurrencyEntity getCurrencyEntityById(int id);

    float getCurrencyRate(int accountId, String code);

    void insertCurrency(CurrencyEntity currencyEntity);

    void updateCurrency(CurrencyEntity currencyEntity);
}
