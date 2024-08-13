package com.expance.merapaisa.Utility;

import android.content.Context;
import android.content.res.Resources;
import androidx.exifinterface.media.ExifInterface;
import com.google.android.gms.ads.RequestConfiguration;
import com.expance.merapaisa.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import org.apache.commons.codec.language.bm.Rule;

/* loaded from: classes3.dex */
public class DataHelper {
    public static ArrayList<String> getColorList() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("#34BFFF");
        arrayList.add("#0097E6");
        arrayList.add("#0077C5");
        arrayList.add("#055393");
        arrayList.add("#008481");
        arrayList.add("#00A6A4");
        arrayList.add("#00C1BF");
        arrayList.add("#29B473");
        arrayList.add("#7FD000");
        arrayList.add("#FFCA00");
        arrayList.add("#FFBB00");
        arrayList.add("#FFAD00");
        arrayList.add("#FF8000");
        arrayList.add("#F95700");
        arrayList.add("#EE4036");
        arrayList.add("#D52B1E");
        arrayList.add("#B80000");
        arrayList.add("#9C005E");
        arrayList.add("#90278E");
        arrayList.add("#652D90");
        arrayList.add("#4E2B8F");
        arrayList.add("#6436AF");
        arrayList.add("#7A3DD8");
        arrayList.add("#9457FA");
        arrayList.add("#FF59CC");
        arrayList.add("#E31C9E");
        arrayList.add("#C9007A");
        arrayList.add("#810035");
        arrayList.add("#A52A2A");
        arrayList.add("#8B4513");
        arrayList.add("#7A5649");
        arrayList.add("#5E4138");
        arrayList.add("#424243");
        arrayList.add("#455A64");
        arrayList.add("#66757f");
        return arrayList;
    }

    public static String getDefaultCategory(Context context, int i) {
        return new String[]{getResourceString(context, R.string.category_bills), getResourceString(context, R.string.category_clothing), getResourceString(context, R.string.category_education), getResourceString(context, R.string.category_entertainment), getResourceString(context, R.string.category_fitness), getResourceString(context, R.string.category_food_and_beverages), getResourceString(context, R.string.category_gifts), getResourceString(context, R.string.category_health_and_beauty), getResourceString(context, R.string.category_furniture), getResourceString(context, R.string.category_pet), getResourceString(context, R.string.category_shopping), getResourceString(context, R.string.category_transportation), getResourceString(context, R.string.category_travel), getResourceString(context, R.string.category_others), getResourceString(context, R.string.category_allowance), getResourceString(context, R.string.category_award), getResourceString(context, R.string.category_bonus), getResourceString(context, R.string.category_dividend), getResourceString(context, R.string.category_investment), getResourceString(context, R.string.category_lottery), getResourceString(context, R.string.category_salary), getResourceString(context, R.string.category_tips), getResourceString(context, R.string.category_others), getResourceString(context, R.string.adjustment), getResourceString(context, R.string.loan), getResourceString(context, R.string.repay), getResourceString(context, R.string.debt), getResourceString(context, R.string.collect)}[i - 1];
    }

    private static String getResourceString(Context context, int id) {
        return context.getResources().getString(id);
    }

    public static ArrayList<String> getCurrencySymbolList() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("AED");
        arrayList.add("AFN");
        arrayList.add(Rule.ALL);
        arrayList.add("AMD");
        arrayList.add("NAƒ");
        arrayList.add("AOA");
        arrayList.add("$");
        arrayList.add("$");
        arrayList.add("ƒ");
        arrayList.add("₼");
        arrayList.add("KM");
        arrayList.add("Bds$");
        arrayList.add("৳");
        arrayList.add("лв");
        arrayList.add("BD");
        arrayList.add("FBu");
        arrayList.add("BD$");
        arrayList.add("B$");
        arrayList.add("Bs");
        arrayList.add("R$");
        arrayList.add("B$");
        arrayList.add("BTC");
        arrayList.add("Nu.");
        arrayList.add("P");
        arrayList.add("Br");
        arrayList.add("BZ$");
        arrayList.add("$");
        arrayList.add("FC");
        arrayList.add("Fr.");
        arrayList.add("$");
        arrayList.add("¥");
        arrayList.add("$");
        arrayList.add("₡");
        arrayList.add("₱");
        arrayList.add("Esc");
        arrayList.add("Kč");
        arrayList.add("Fdj");
        arrayList.add("Kr.");
        arrayList.add("RD$");
        arrayList.add("DZD");
        arrayList.add("KR");
        arrayList.add("E£");
        arrayList.add("Nfk");
        arrayList.add("Br");
        arrayList.add("ETH");
        arrayList.add("€");
        arrayList.add("FJ$");
        arrayList.add("£");
        arrayList.add("£");
        arrayList.add("GEL");
        arrayList.add("GH₵");
        arrayList.add("£");
        arrayList.add("D");
        arrayList.add("FG");
        arrayList.add("Q");
        arrayList.add("GY$");
        arrayList.add("HK$");
        arrayList.add("L");
        arrayList.add("kn");
        arrayList.add(RequestConfiguration.MAX_AD_CONTENT_RATING_G);
        arrayList.add("Ft");
        arrayList.add("Rp");
        arrayList.add("₪");
        arrayList.add("₹");
        arrayList.add("IQD");
        arrayList.add("IRR");
        arrayList.add("kr");
        arrayList.add("J$");
        arrayList.add("¥");
        arrayList.add("JOD");
        arrayList.add("Ksh");
        arrayList.add("som");
        arrayList.add("KHR");
        arrayList.add("KMF");
        arrayList.add("₩");
        arrayList.add("K.D");
        arrayList.add("KY$");
        arrayList.add("₸");
        arrayList.add("₭");
        arrayList.add("LBP");
        arrayList.add("Rs");
        arrayList.add("L$");
        arrayList.add("M");
        arrayList.add("Ł");
        arrayList.add("Lt");
        arrayList.add("Ls");
        arrayList.add("LD");
        arrayList.add("MAD");
        arrayList.add("L");
        arrayList.add("Ar");
        arrayList.add("Ден");
        arrayList.add("K");
        arrayList.add("₮");
        arrayList.add("MOP$");
        arrayList.add("UM");
        arrayList.add("₨");
        arrayList.add("Rf");
        arrayList.add("MK");
        arrayList.add("Mex$");
        arrayList.add("RM");
        arrayList.add("MTn");
        arrayList.add("N$");
        arrayList.add("₦");
        arrayList.add("C$");
        arrayList.add("kr");
        arrayList.add("Rs.");
        arrayList.add("NZ$");
        arrayList.add("B/.");
        arrayList.add("S/.");
        arrayList.add("K");
        arrayList.add("₱");
        arrayList.add("₨.");
        arrayList.add("zł");
        arrayList.add("Gs");
        arrayList.add("QR");
        arrayList.add("lei");
        arrayList.add("din.");
        arrayList.add("₽");
        arrayList.add("RF");
        arrayList.add("SR");
        arrayList.add("Si$");
        arrayList.add("SR");
        arrayList.add("SDG");
        arrayList.add("SDR");
        arrayList.add("kr");
        arrayList.add("S$");
        arrayList.add("SKK");
        arrayList.add("Le");
        arrayList.add("Sh.");
        arrayList.add("$");
        arrayList.add("Db");
        arrayList.add("₡");
        arrayList.add("£S");
        arrayList.add(ExifInterface.LONGITUDE_EAST);
        arrayList.add("฿");
        arrayList.add("TJS");
        arrayList.add("m");
        arrayList.add("DT");
        arrayList.add("₺");
        arrayList.add("TT$");
        arrayList.add("NT$");
        arrayList.add("TZS");
        arrayList.add("₴");
        arrayList.add("Ush");
        arrayList.add("$");
        arrayList.add("$U");
        arrayList.add("UZ$");
        arrayList.add("Bs");
        arrayList.add("₫");
        arrayList.add("VT");
        arrayList.add("WS$");
        arrayList.add("CFA");
        arrayList.add("EC$");
        arrayList.add("CFA");
        arrayList.add("F");
        arrayList.add("XRP");
        arrayList.add("YER");
        arrayList.add("$");
        arrayList.add("ZK");
        arrayList.add("Z$");
        arrayList.add("₹");
        return arrayList;
    }

    public static ArrayList<String> getCurrencyList() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("AED - United Arab Emirati Dirham(AED)");
        arrayList.add("AFN - Afghan Afghani(AFN)");
        arrayList.add("ALL - Albanian Lek(ALL)");
        arrayList.add("AMD - Armenian Dram(AMD)");
        arrayList.add("ANG - Netherlands Antillean Guilder(NAƒ)");
        arrayList.add("AOA - Angolan Kwanza(AOA)");
        arrayList.add("ARS - Argentina Peso($)");
        arrayList.add("AUD - Australian Dollar($)");
        arrayList.add("AWG - Aruban Florin(ƒ)");
        arrayList.add("AZN - Azerbaijan New Manats(₼)");
        arrayList.add("BAM - Bosnia and Herzegovina Convertible Mark(KM)");
        arrayList.add("BBD - Barbadian Dollar(Bds$)");
        arrayList.add("BDT - Bangladeshi Taka(৳)");
        arrayList.add("BGN - Bulgarian Lev(лв)");
        arrayList.add("BHD - Bahraini Dinar(BD)");
        arrayList.add("BIF - Burundian Franc(FBu)");
        arrayList.add("BMD - Bermudian Dollar(BD$)");
        arrayList.add("BND - Brunei Dollar(B$)");
        arrayList.add("BOB - Bolivian Boliviano(Bs)");
        arrayList.add("BRL - Brazilian Real(R$)");
        arrayList.add("BSD - Bahamian Dollar(B$)");
        arrayList.add("BTC - Bitcoin(BTC)");
        arrayList.add("BTN - Bhutanese Ngultrum(Nu.)");
        arrayList.add("BWP - Botswana Pula(P)");
        arrayList.add("BYN - New Belarusian Ruble(Br)");
        arrayList.add("BZD - Belize Dollar(BZ$)");
        arrayList.add("CAD - Canadian Dollar($)");
        arrayList.add("CDF - Congolese Franc(FC)");
        arrayList.add("CHF - Swiss Franc(Fr.)");
        arrayList.add("CLP - Chilean Peso($)");
        arrayList.add("CNY - Chinese Yuan(¥)");
        arrayList.add("COP - Colombian Peso($)");
        arrayList.add("CRC - Costa Rican Colon(₡)");
        arrayList.add("CUP - Cuban Peso(₱)");
        arrayList.add("CVE - Cape Verdean Escudo(Esc)");
        arrayList.add("CZK - Czech Koruna(Kč)");
        arrayList.add("DJF - Djiboutian Franc(Fdj)");
        arrayList.add("DKK - Danish Krone(Kr.)");
        arrayList.add("DOP - Dominican Peso(RD$)");
        arrayList.add("DZD - Algerian Dinar(DZD)");
        arrayList.add("EEK - Estonian Kroon(KR)");
        arrayList.add("EGP - Egyptian Pound(E£)");
        arrayList.add("ERN - Eritrean Nakfa(Nfk)");
        arrayList.add("ETB - Ethiopian Birr(Br)");
        arrayList.add("ETH - Ethereum(ETH)");
        arrayList.add("EUR - Euro(€)");
        arrayList.add("FJD - Fijian Dollar(FJ$)");
        arrayList.add("FKP - Falkland Islands Pound(£)");
        arrayList.add("GBP - Pound Sterling(£)");
        arrayList.add("GEL - Georgian Lari(GEL)");
        arrayList.add("GHS - Ghanaian Cedi(GH₵)");
        arrayList.add("GIP - Gibraltar Pound(£)");
        arrayList.add("GMD - Gambian Dalasi(D)");
        arrayList.add("GNF - Guinean franc(FG)");
        arrayList.add("GTQ - Guatemalan quetzal(Q)");
        arrayList.add("GYD - Guyanese Dollar(GY$)");
        arrayList.add("HKD - Hong Kong Dollar(HK$)");
        arrayList.add("HNL - Honduran Lempira(L)");
        arrayList.add("HRK - Crotian Kuna(kn)");
        arrayList.add("HTG - Haitian Gourde(G)");
        arrayList.add("HUF - Hungarian Forint(Ft)");
        arrayList.add("IDR - Indonesian Rupiah(Rp)");
        arrayList.add("ILS - Israeli New Shekel(₪)");
        arrayList.add("INR - Indian Rupee(₹)");
        arrayList.add("IQD - Iraqi Dinar(IQD)");
        arrayList.add("IRR - Iranian Rial(IRR)");
        arrayList.add("ISK - Icelandic Króna(kr)");
        arrayList.add("JMD - Jamaican Dollar(J$)");
        arrayList.add("JPY - Japanese Yen(¥)");
        arrayList.add("JOD - Jordanian Dinar(JOD)");
        arrayList.add("KES - Kenyan shilling(Ksh)");
        arrayList.add("KGS - Kyrgyzstani Som(som)");
        arrayList.add("KHR - Cambodian Riel(KHR)");
        arrayList.add("KMF - Comorian Franc(KMF)");
        arrayList.add("KRW - South Korean Won(₩)");
        arrayList.add("KWD - Kuwaiti Dinar(K.D)");
        arrayList.add("KYD - Cayman Islands Dollar(KY$)");
        arrayList.add("KZT - Kazakhstani Tenge(₸)");
        arrayList.add("LAK - Lao Kip(₭)");
        arrayList.add("LBP - Lebanese Pound(LBP)");
        arrayList.add("LKR - Sri Lankan Rupee(Rs)");
        arrayList.add("LRD - Liberian Dollar(L$)");
        arrayList.add("LSL - Lesotho Loti(M)");
        arrayList.add("LTC - Lite Coin(Ł)");
        arrayList.add("LTL - Lithuanian Litas(Lt)");
        arrayList.add("LVL - Latvian Lats(Ls)");
        arrayList.add("LYD - Libyan Dinar(LD)");
        arrayList.add("MAD - Moroccan Dirham(MAD)");
        arrayList.add("MDL - Moldovan Leu(L)");
        arrayList.add("MGA - Malagasy Ariary(Ar)");
        arrayList.add("MKD - Macedonian Denar(Ден)");
        arrayList.add("MMK - Myanmar Kyat(K)");
        arrayList.add("MNT - Mongolian Tögrög(₮)");
        arrayList.add("MOP - Macanese Pataca(MOP$)");
        arrayList.add("MRO - Mauritanian Ouguiya(UM)");
        arrayList.add("MUR - Mauritian Rupee(₨)");
        arrayList.add("MVR - Maldivian Rufiyaa(Rf)");
        arrayList.add("MWK - Malawian Kwacha(MK)");
        arrayList.add("MXN - Mexican Peso(Mex$)");
        arrayList.add("MYR - Malaysian Ringgit(RM)");
        arrayList.add("MZN - Mozambican Metical(MTn)");
        arrayList.add("NAD - Namibian Dollar(N$)");
        arrayList.add("NGN - Nigerian Naira(₦)");
        arrayList.add("NIO - Nicaraguan Córdoba(C$)");
        arrayList.add("NOK - Norwegian Krone(kr)");
        arrayList.add("NPR - Nepalese Rupee(Rs.)");
        arrayList.add("NZD - New Zealand Dollar(NZ$)");
        arrayList.add("PAB - Panamanian Balboa(B/.)");
        arrayList.add("PEN - Peruvian Sol(S/.)");
        arrayList.add("PGK - Papua New Guinean Kina(K)");
        arrayList.add("PHP - Philippine Peso(₱)");
        arrayList.add("PKR - Pakistani Rupee(₨.)");
        arrayList.add("PLN - Polish Złoty(zł)");
        arrayList.add("PYG - Paraguayan Guaraní(Gs)");
        arrayList.add("QAR - Qatari Riyal(QR)");
        arrayList.add("RON - Romanian Leu(lei)");
        arrayList.add("RSD - Serbian Dinar(din.)");
        arrayList.add("RUB - Russian Ruble(₽)");
        arrayList.add("RWF - Rwandan Franc(RF)");
        arrayList.add("SAR - Saudi Riyal(SR)");
        arrayList.add("SBD - Solomon Islands Dollar(Si$)");
        arrayList.add("SCR - Seychellois Rupee(SR)");
        arrayList.add("SDG - Sudanese Pound(SDG)");
        arrayList.add("SDR - Special Drawing Rights(SDR)");
        arrayList.add("SEK - Swedish Krona(kr)");
        arrayList.add("SGD - Singapore Dollar(S$)");
        arrayList.add("SKK - Slovak Koruna(SKK)");
        arrayList.add("SLL - Sierra Leonean Leone(Le)");
        arrayList.add("SOS - Somali Shilling(Sh.)");
        arrayList.add("SRD - Surinamese Dollar($)");
        arrayList.add("STD - São Tomé and Príncipe Dobra(Db)");
        arrayList.add("SVC - Salvadoran Colón(₡)");
        arrayList.add("SYP - Syrian Pound(£S)");
        arrayList.add("SZL - Swazi Lilangeni(E)");
        arrayList.add("THB - Thai Baht(฿)");
        arrayList.add("TJS - Tajikistani Somoni(TJS)");
        arrayList.add("TMT - Turkmenistan Manat(m)");
        arrayList.add("TND - Tunisian Dinar(DT)");
        arrayList.add("TRY - Turkish Lira(₺)");
        arrayList.add("TTD - Trinidad and Tobago dollar(TT$)");
        arrayList.add("TWD - New Taiwan Dollar(NT$)");
        arrayList.add("TZS - Tanzanian Shilling(TZS)");
        arrayList.add("UAH - Ukrainian Hryvnia(₴)");
        arrayList.add("UGX - Ugandan Shilling(Ush)");
        arrayList.add("USD - United States Dollar($)");
        arrayList.add("UYU - Uruguayan Peso($U)");
        arrayList.add("UZS - Uzbekistani som(UZ$)");
        arrayList.add("VEF - Venezuelan Bolívar(Bs)");
        arrayList.add("VND - Vietnamese Dong(₫)");
        arrayList.add("VUV - Vanuatu Vatu(VT)");
        arrayList.add("WST - Samoan Tālā(WS$)");
        arrayList.add("XAF - Central African CFA franc(CFA)");
        arrayList.add("XCD - Eastern Caribbean Dollar(EC$)");
        arrayList.add("XOF - West African CFA franc(CFA)");
        arrayList.add("XPF - CFP franc(F)");
        arrayList.add("XRP - Ripple(XRP)");
        arrayList.add("YER - Yemeni Rial(YER)");
        arrayList.add("ZAR - South African Rand($)");
        arrayList.add("ZMW - Zambian Kwacha(ZK)");
        arrayList.add("ZWL - Zimbabwean Dollar(Z$)");
        arrayList.add("CPI - Indian Rupee(₹)");
        return arrayList;
    }

    public static ArrayList<String> getCurrencyCode() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("AED");
        arrayList.add("AFN");
        arrayList.add(Rule.ALL);
        arrayList.add("AMD");
        arrayList.add("ANG");
        arrayList.add("AOA");
        arrayList.add("ARS");
        arrayList.add("AUD");
        arrayList.add("AWG");
        arrayList.add("AZN");
        arrayList.add("BAM");
        arrayList.add("BBD");
        arrayList.add("BDT");
        arrayList.add("BGN");
        arrayList.add("BHD");
        arrayList.add("BIF");
        arrayList.add("BMD");
        arrayList.add("BND");
        arrayList.add("BOB");
        arrayList.add("BRL");
        arrayList.add("BSD");
        arrayList.add("BTC");
        arrayList.add("BTN");
        arrayList.add("BWP");
        arrayList.add("BYN");
        arrayList.add("BZD");
        arrayList.add("CAD");
        arrayList.add("CDF");
        arrayList.add("CHF");
        arrayList.add("CLP");
        arrayList.add("CNY");
        arrayList.add("COP");
        arrayList.add("CRC");
        arrayList.add("CUP");
        arrayList.add("CVE");
        arrayList.add("CZK");
        arrayList.add("DJF");
        arrayList.add("DKK");
        arrayList.add("DOP");
        arrayList.add("DZD");
        arrayList.add("EEK");
        arrayList.add("EGP");
        arrayList.add("ERN");
        arrayList.add("ETB");
        arrayList.add("ETH");
        arrayList.add("EUR");
        arrayList.add("FJD");
        arrayList.add("FKP");
        arrayList.add("GBP");
        arrayList.add("GEL");
        arrayList.add("GHS");
        arrayList.add("GIP");
        arrayList.add("GMD");
        arrayList.add("GNF");
        arrayList.add("GTQ");
        arrayList.add("GYD");
        arrayList.add("HKD");
        arrayList.add("HNL");
        arrayList.add("HRK");
        arrayList.add("HTG");
        arrayList.add("HUF");
        arrayList.add("IDR");
        arrayList.add("ILS");
        arrayList.add("INR");
        arrayList.add("IQD");
        arrayList.add("IRR");
        arrayList.add("ISK");
        arrayList.add("JMD");
        arrayList.add("JPY");
        arrayList.add("JOD");
        arrayList.add("KES");
        arrayList.add("KGS");
        arrayList.add("KHR");
        arrayList.add("KMF");
        arrayList.add("KRW");
        arrayList.add("KWD");
        arrayList.add("KYD");
        arrayList.add("KZT");
        arrayList.add("LAK");
        arrayList.add("LBP");
        arrayList.add("LKR");
        arrayList.add("LRD");
        arrayList.add("LSL");
        arrayList.add("LTC");
        arrayList.add("LTL");
        arrayList.add("LVL");
        arrayList.add("LYD");
        arrayList.add("MAD");
        arrayList.add("MDL");
        arrayList.add("MGA");
        arrayList.add("MKD");
        arrayList.add("MMK");
        arrayList.add("MNT");
        arrayList.add("MOP");
        arrayList.add("MRO");
        arrayList.add("MUR");
        arrayList.add("MVR");
        arrayList.add("MWK");
        arrayList.add("MXN");
        arrayList.add("MYR");
        arrayList.add("MZN");
        arrayList.add("NAD");
        arrayList.add("NGN");
        arrayList.add("NIO");
        arrayList.add("NOK");
        arrayList.add("NPR");
        arrayList.add("NZD");
        arrayList.add("PAB");
        arrayList.add("PEN");
        arrayList.add("PGK");
        arrayList.add("PHP");
        arrayList.add("PKR");
        arrayList.add("PLN");
        arrayList.add("PYG");
        arrayList.add("QAR");
        arrayList.add("RON");
        arrayList.add("RSD");
        arrayList.add("RUB");
        arrayList.add("RWF");
        arrayList.add("SAR");
        arrayList.add("SBD");
        arrayList.add("SCR");
        arrayList.add("SDG");
        arrayList.add("SDR");
        arrayList.add("SEK");
        arrayList.add("SGD");
        arrayList.add("SKK");
        arrayList.add("SLL");
        arrayList.add("SOS");
        arrayList.add("SRD");
        arrayList.add("STD");
        arrayList.add("SVC");
        arrayList.add("SYP");
        arrayList.add("SZL");
        arrayList.add("THB");
        arrayList.add("TJS");
        arrayList.add("TMT");
        arrayList.add("TND");
        arrayList.add("TRY");
        arrayList.add("TTD");
        arrayList.add("TWD");
        arrayList.add("TZS");
        arrayList.add("UAH");
        arrayList.add("UGX");
        arrayList.add("USD");
        arrayList.add("UYU");
        arrayList.add("UZS");
        arrayList.add("VEF");
        arrayList.add("VND");
        arrayList.add("VUV");
        arrayList.add("WST");
        arrayList.add("XAF");
        arrayList.add("XCD");
        arrayList.add("XOF");
        arrayList.add("XPF");
        arrayList.add("XRP");
        arrayList.add("YER");
        arrayList.add("ZAR");
        arrayList.add("ZMW");
        arrayList.add("ZWL");
        arrayList.add("CPI");
        return arrayList;
    }

    public static ArrayList<Integer> getWalletIcons() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(Integer.valueOf((int) R.drawable.wallet_0));
        arrayList.add(Integer.valueOf((int) R.drawable.wallet_1));
        arrayList.add(Integer.valueOf((int) R.drawable.wallet_2));
        arrayList.add(Integer.valueOf((int) R.drawable.wallet_3));
        arrayList.add(Integer.valueOf((int) R.drawable.wallet_4));
        arrayList.add(Integer.valueOf((int) R.drawable.wallet_5));
        arrayList.add(Integer.valueOf((int) R.drawable.wallet_6));
        arrayList.add(Integer.valueOf((int) R.drawable.wallet_7));
        arrayList.add(Integer.valueOf((int) R.drawable.wallet_8));
        arrayList.add(Integer.valueOf((int) R.drawable.wallet_9));
        arrayList.add(Integer.valueOf((int) R.drawable.wallet_10));
        arrayList.add(Integer.valueOf((int) R.drawable.wallet_11));
        arrayList.add(Integer.valueOf((int) R.drawable.wallet_12));
        arrayList.add(Integer.valueOf((int) R.drawable.wallet_13));
        arrayList.add(Integer.valueOf((int) R.drawable.wallet_14));
        arrayList.add(Integer.valueOf((int) R.drawable.wallet_15));
        arrayList.add(Integer.valueOf((int) R.drawable.wallet_16));
        arrayList.add(Integer.valueOf((int) R.drawable.wallet_17));
        arrayList.add(Integer.valueOf((int) R.drawable.wallet_18));
        arrayList.add(Integer.valueOf((int) R.drawable.wallet_19));
        arrayList.add(Integer.valueOf((int) R.drawable.wallet_20));
        arrayList.add(Integer.valueOf((int) R.drawable.wallet_21));
        arrayList.add(Integer.valueOf((int) R.drawable.wallet_22));
        arrayList.add(Integer.valueOf((int) R.drawable.wallet_23));
        arrayList.add(Integer.valueOf((int) R.drawable.wallet_24));
        arrayList.add(Integer.valueOf((int) R.drawable.wallet_25));
        arrayList.add(Integer.valueOf((int) R.drawable.wallet_26));
        arrayList.add(Integer.valueOf((int) R.drawable.wallet_27));
        arrayList.add(Integer.valueOf((int) R.drawable.wallet_28));
        arrayList.add(Integer.valueOf((int) R.drawable.wallet_29));
        return arrayList;
    }

    public static ArrayList<Integer> getCategoryIcons() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(Integer.valueOf((int) R.drawable.category_0));
        arrayList.add(Integer.valueOf((int) R.drawable.category_1));
        arrayList.add(Integer.valueOf((int) R.drawable.category_2));
        arrayList.add(Integer.valueOf((int) R.drawable.category_3));
        arrayList.add(Integer.valueOf((int) R.drawable.category_4));
        arrayList.add(Integer.valueOf((int) R.drawable.category_5));
        arrayList.add(Integer.valueOf((int) R.drawable.category_6));
        arrayList.add(Integer.valueOf((int) R.drawable.category_7));
        arrayList.add(Integer.valueOf((int) R.drawable.category_8));
        arrayList.add(Integer.valueOf((int) R.drawable.category_9));
        arrayList.add(Integer.valueOf((int) R.drawable.category_10));
        arrayList.add(Integer.valueOf((int) R.drawable.category_11));
        arrayList.add(Integer.valueOf((int) R.drawable.category_12));
        arrayList.add(Integer.valueOf((int) R.drawable.category_13));
        arrayList.add(Integer.valueOf((int) R.drawable.category_14));
        arrayList.add(Integer.valueOf((int) R.drawable.category_15));
        arrayList.add(Integer.valueOf((int) R.drawable.category_16));
        arrayList.add(Integer.valueOf((int) R.drawable.category_17));
        arrayList.add(Integer.valueOf((int) R.drawable.category_18));
        arrayList.add(Integer.valueOf((int) R.drawable.category_19));
        arrayList.add(Integer.valueOf((int) R.drawable.category_20));
        arrayList.add(Integer.valueOf((int) R.drawable.category_21));
        arrayList.add(Integer.valueOf((int) R.drawable.category_22));
        arrayList.add(Integer.valueOf((int) R.drawable.category_23));
        arrayList.add(Integer.valueOf((int) R.drawable.category_24));
        arrayList.add(Integer.valueOf((int) R.drawable.category_25));
        arrayList.add(Integer.valueOf((int) R.drawable.category_26));
        arrayList.add(Integer.valueOf((int) R.drawable.category_27));
        arrayList.add(Integer.valueOf((int) R.drawable.category_28));
        arrayList.add(Integer.valueOf((int) R.drawable.category_29));
        arrayList.add(Integer.valueOf((int) R.drawable.category_30));
        arrayList.add(Integer.valueOf((int) R.drawable.category_31));
        arrayList.add(Integer.valueOf((int) R.drawable.category_32));
        arrayList.add(Integer.valueOf((int) R.drawable.category_33));
        arrayList.add(Integer.valueOf((int) R.drawable.category_34));
        arrayList.add(Integer.valueOf((int) R.drawable.category_35));
        arrayList.add(Integer.valueOf((int) R.drawable.category_36));
        arrayList.add(Integer.valueOf((int) R.drawable.category_37));
        arrayList.add(Integer.valueOf((int) R.drawable.category_38));
        arrayList.add(Integer.valueOf((int) R.drawable.category_39));
        arrayList.add(Integer.valueOf((int) R.drawable.category_40));
        arrayList.add(Integer.valueOf((int) R.drawable.category_41));
        arrayList.add(Integer.valueOf((int) R.drawable.category_42));
        arrayList.add(Integer.valueOf((int) R.drawable.category_43));
        arrayList.add(Integer.valueOf((int) R.drawable.category_44));
        arrayList.add(Integer.valueOf((int) R.drawable.category_45));
        arrayList.add(Integer.valueOf((int) R.drawable.category_46));
        arrayList.add(Integer.valueOf((int) R.drawable.category_47));
        arrayList.add(Integer.valueOf((int) R.drawable.category_48));
        arrayList.add(Integer.valueOf((int) R.drawable.category_49));
        arrayList.add(Integer.valueOf((int) R.drawable.category_50));
        arrayList.add(Integer.valueOf((int) R.drawable.category_51));
        arrayList.add(Integer.valueOf((int) R.drawable.category_52));
        arrayList.add(Integer.valueOf((int) R.drawable.category_53));
        arrayList.add(Integer.valueOf((int) R.drawable.category_54));
        arrayList.add(Integer.valueOf((int) R.drawable.category_55));
        arrayList.add(Integer.valueOf((int) R.drawable.category_56));
        arrayList.add(Integer.valueOf((int) R.drawable.category_57));
        arrayList.add(Integer.valueOf((int) R.drawable.category_58));
        arrayList.add(Integer.valueOf((int) R.drawable.category_59));
        arrayList.add(Integer.valueOf((int) R.drawable.category_60));
        arrayList.add(Integer.valueOf((int) R.drawable.category_61));
        arrayList.add(Integer.valueOf((int) R.drawable.category_62));
        arrayList.add(Integer.valueOf((int) R.drawable.category_63));
        arrayList.add(Integer.valueOf((int) R.drawable.category_64));
        arrayList.add(Integer.valueOf((int) R.drawable.category_65));
        arrayList.add(Integer.valueOf((int) R.drawable.category_66));
        arrayList.add(Integer.valueOf((int) R.drawable.category_67));
        arrayList.add(Integer.valueOf((int) R.drawable.category_68));
        arrayList.add(Integer.valueOf((int) R.drawable.category_69));
        arrayList.add(Integer.valueOf((int) R.drawable.category_70));
        arrayList.add(Integer.valueOf((int) R.drawable.category_71));
        arrayList.add(Integer.valueOf((int) R.drawable.category_72));
        arrayList.add(Integer.valueOf((int) R.drawable.category_73));
        arrayList.add(Integer.valueOf((int) R.drawable.category_74));
        arrayList.add(Integer.valueOf((int) R.drawable.category_75));
        arrayList.add(Integer.valueOf((int) R.drawable.category_76));
        arrayList.add(Integer.valueOf((int) R.drawable.category_77));
        arrayList.add(Integer.valueOf((int) R.drawable.category_78));
        arrayList.add(Integer.valueOf((int) R.drawable.category_79));
        arrayList.add(Integer.valueOf((int) R.drawable.category_80));
        arrayList.add(Integer.valueOf((int) R.drawable.category_81));
        arrayList.add(Integer.valueOf((int) R.drawable.category_82));
        arrayList.add(Integer.valueOf((int) R.drawable.category_83));
        arrayList.add(Integer.valueOf((int) R.drawable.category_84));
        arrayList.add(Integer.valueOf((int) R.drawable.category_85));
        arrayList.add(Integer.valueOf((int) R.drawable.category_86));
        arrayList.add(Integer.valueOf((int) R.drawable.category_87));
        arrayList.add(Integer.valueOf((int) R.drawable.category_88));
        arrayList.add(Integer.valueOf((int) R.drawable.category_89));
        arrayList.add(Integer.valueOf((int) R.drawable.category_90));
        arrayList.add(Integer.valueOf((int) R.drawable.category_91));
        arrayList.add(Integer.valueOf((int) R.drawable.category_92));
        arrayList.add(Integer.valueOf((int) R.drawable.category_93));
        arrayList.add(Integer.valueOf((int) R.drawable.category_94));
        arrayList.add(Integer.valueOf((int) R.drawable.category_95));
        arrayList.add(Integer.valueOf((int) R.drawable.category_96));
        arrayList.add(Integer.valueOf((int) R.drawable.category_97));
        arrayList.add(Integer.valueOf((int) R.drawable.category_98));
        arrayList.add(Integer.valueOf((int) R.drawable.category_99));
        arrayList.add(Integer.valueOf((int) R.drawable.category_100));
        arrayList.add(Integer.valueOf((int) R.drawable.category_101));
        arrayList.add(Integer.valueOf((int) R.drawable.category_102));
        arrayList.add(Integer.valueOf((int) R.drawable.category_103));
        arrayList.add(Integer.valueOf((int) R.drawable.category_104));
        arrayList.add(Integer.valueOf((int) R.drawable.category_105));
        arrayList.add(Integer.valueOf((int) R.drawable.category_106));
        arrayList.add(Integer.valueOf((int) R.drawable.category_107));
        arrayList.add(Integer.valueOf((int) R.drawable.category_108));
        arrayList.add(Integer.valueOf((int) R.drawable.category_109));
        arrayList.add(Integer.valueOf((int) R.drawable.category_110));
        arrayList.add(Integer.valueOf((int) R.drawable.category_111));
        arrayList.add(Integer.valueOf((int) R.drawable.category_112));
        arrayList.add(Integer.valueOf((int) R.drawable.category_113));
        arrayList.add(Integer.valueOf((int) R.drawable.category_114));
        arrayList.add(Integer.valueOf((int) R.drawable.category_115));
        arrayList.add(Integer.valueOf((int) R.drawable.category_116));
        arrayList.add(Integer.valueOf((int) R.drawable.category_117));
        arrayList.add(Integer.valueOf((int) R.drawable.category_118));
        arrayList.add(Integer.valueOf((int) R.drawable.category_119));
        arrayList.add(Integer.valueOf((int) R.drawable.category_120));
        arrayList.add(Integer.valueOf((int) R.drawable.category_121));
        arrayList.add(Integer.valueOf((int) R.drawable.category_122));
        arrayList.add(Integer.valueOf((int) R.drawable.category_123));
        arrayList.add(Integer.valueOf((int) R.drawable.category_124));
        arrayList.add(Integer.valueOf((int) R.drawable.category_125));
        arrayList.add(Integer.valueOf((int) R.drawable.category_126));
        arrayList.add(Integer.valueOf((int) R.drawable.category_127));
        arrayList.add(Integer.valueOf((int) R.drawable.category_128));
        arrayList.add(Integer.valueOf((int) R.drawable.category_129));
        arrayList.add(Integer.valueOf((int) R.drawable.category_130));
        arrayList.add(Integer.valueOf((int) R.drawable.category_131));
        arrayList.add(Integer.valueOf((int) R.drawable.category_132));
        arrayList.add(Integer.valueOf((int) R.drawable.category_133));
        arrayList.add(Integer.valueOf((int) R.drawable.category_134));
        arrayList.add(Integer.valueOf((int) R.drawable.category_135));
        arrayList.add(Integer.valueOf((int) R.drawable.category_136));
        arrayList.add(Integer.valueOf((int) R.drawable.category_137));
        arrayList.add(Integer.valueOf((int) R.drawable.category_138));
        arrayList.add(Integer.valueOf((int) R.drawable.category_139));
        arrayList.add(Integer.valueOf((int) R.drawable.category_140));
        arrayList.add(Integer.valueOf((int) R.drawable.category_141));
        arrayList.add(Integer.valueOf((int) R.drawable.category_142));
        arrayList.add(Integer.valueOf((int) R.drawable.category_143));
        arrayList.add(Integer.valueOf((int) R.drawable.category_144));
        arrayList.add(Integer.valueOf((int) R.drawable.category_145));
        arrayList.add(Integer.valueOf((int) R.drawable.category_146));
        arrayList.add(Integer.valueOf((int) R.drawable.category_147));
        arrayList.add(Integer.valueOf((int) R.drawable.category_148));
        arrayList.add(Integer.valueOf((int) R.drawable.category_149));
        arrayList.add(Integer.valueOf((int) R.drawable.category_150));
        arrayList.add(Integer.valueOf((int) R.drawable.category_151));
        arrayList.add(Integer.valueOf((int) R.drawable.category_152));
        arrayList.add(Integer.valueOf((int) R.drawable.category_153));
        arrayList.add(Integer.valueOf((int) R.drawable.category_154));
        arrayList.add(Integer.valueOf((int) R.drawable.category_155));
        arrayList.add(Integer.valueOf((int) R.drawable.category_156));
        arrayList.add(Integer.valueOf((int) R.drawable.category_157));
        arrayList.add(Integer.valueOf((int) R.drawable.category_158));
        arrayList.add(Integer.valueOf((int) R.drawable.category_159));
        arrayList.add(Integer.valueOf((int) R.drawable.category_160));
        arrayList.add(Integer.valueOf((int) R.drawable.category_161));
        arrayList.add(Integer.valueOf((int) R.drawable.category_162));
        arrayList.add(Integer.valueOf((int) R.drawable.category_163));
        arrayList.add(Integer.valueOf((int) R.drawable.category_164));
        arrayList.add(Integer.valueOf((int) R.drawable.adjust));
        arrayList.add(Integer.valueOf((int) R.drawable.borrow));
        arrayList.add(Integer.valueOf((int) R.drawable.repay));
        arrayList.add(Integer.valueOf((int) R.drawable.lend));
        arrayList.add(Integer.valueOf((int) R.drawable.receive));
        arrayList.add(Integer.valueOf((int) R.drawable.increase_borrow));
        arrayList.add(Integer.valueOf((int) R.drawable.increase_lend));
        return arrayList;
    }

    public static ArrayList<String> getPeriodList(Context context) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(context.getResources().getString(R.string.weekly));
        arrayList.add(context.getResources().getString(R.string.monthly));
        arrayList.add(context.getResources().getString(R.string.quarterly));
        arrayList.add(context.getResources().getString(R.string.yearly));
        return arrayList;
    }

    public static ArrayList<String> getFormatList() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(".csv");
        arrayList.add(".xls");
        return arrayList;
    }

    public static ArrayList<String> getExportTimeRangeList(Context context) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(context.getResources().getString(R.string.yearly));
        arrayList.add(context.getResources().getString(R.string.monthly));
        arrayList.add(context.getResources().getString(R.string.weekly));
        arrayList.add(context.getResources().getString(R.string.daily));
        return arrayList;
    }

    public static ArrayList<String> getExportTypeList(Context context) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(context.getResources().getString(R.string.record));
        arrayList.add(context.getResources().getString(R.string.report));
        arrayList.add(context.getResources().getString(R.string.by_category));
        arrayList.add(context.getResources().getString(R.string.by_wallet));
        return arrayList;
    }

    public static String getLanguage(Context context) {
        String preferLanguage = SharePreferenceHelper.getPreferLanguage(context);
        preferLanguage.hashCode();
        char c = 65535;
        switch (preferLanguage.hashCode()) {
            case -704711850:
                if (preferLanguage.equals("zh-rTW")) {
                    c = 0;
                    break;
                }
                break;
            case 3201:
                if (preferLanguage.equals("de")) {
                    c = 1;
                    break;
                }
                break;
            case 3241:
                if (preferLanguage.equals("en")) {
                    c = 2;
                    break;
                }
                break;
            case 3246:
                if (preferLanguage.equals("es")) {
                    c = 3;
                    break;
                }
                break;
            case 3276:
                if (preferLanguage.equals("fr")) {
                    c = 4;
                    break;
                }
                break;
            case 3365:
                if (preferLanguage.equals("in")) {
                    c = 5;
                    break;
                }
                break;
            case 3371:
                if (preferLanguage.equals("it")) {
                    c = 6;
                    break;
                }
                break;
            case 3383:
                if (preferLanguage.equals("ja")) {
                    c = 7;
                    break;
                }
                break;
            case 3494:
                if (preferLanguage.equals("ms")) {
                    c = '\b';
                    break;
                }
                break;
            case 3588:
                if (preferLanguage.equals("pt")) {
                    c = '\t';
                    break;
                }
                break;
            case 3651:
                if (preferLanguage.equals("ru")) {
                    c = '\n';
                    break;
                }
                break;
            case 3886:
                if (preferLanguage.equals("zh")) {
                    c = 11;
                    break;
                }
                break;
            case 115813762:
                if (preferLanguage.equals("zh-TW")) {
                    c = '\f';
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
            case '\f':
                return "繁體中文";
            case 1:
                return "Deutsche";
            case 2:
                return "English";
            case 3:
                return "Español";
            case 4:
                return "Français";
            case 5:
                return "Bahasa Indonesia";
            case 6:
                return "Italiano";
            case 7:
                return "日本語";
            case '\b':
                return "Bahasa Melayu";
            case '\t':
                return "Português";
            case '\n':
                return "Pусский";
            case 11:
                return "简体中文";
            default:
                return context.getString(R.string.system_default);
        }
    }

    public static String getFirstDayOfWeek(Context context) {
        switch (SharePreferenceHelper.getFirstDayOfWeek(context)) {
            case 2:
                return context.getResources().getString(R.string.monday);
            case 3:
                return context.getResources().getString(R.string.tuesday);
            case 4:
                return context.getResources().getString(R.string.wednesday);
            case 5:
                return context.getResources().getString(R.string.thursday);
            case 6:
                return context.getResources().getString(R.string.friday);
            case 7:
                return context.getResources().getString(R.string.saturday);
            default:
                return context.getResources().getString(R.string.sunday);
        }
    }

    public static String getStartupScreen(Context context) {
        int startUpScreen = SharePreferenceHelper.getStartUpScreen(context);
        if (startUpScreen != 0) {
            if (startUpScreen != 1) {
                if (startUpScreen == 2) {
                    return context.getResources().getString(R.string.statistic);
                }
                return context.getResources().getString(R.string.wallet);
            }
            return context.getResources().getString(R.string.calendar);
        }
        return context.getResources().getString(R.string.transaction);
    }

    public static List<String> getFirstDayOfWeekData(Context context) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(context.getResources().getString(R.string.sunday));
        arrayList.add(context.getResources().getString(R.string.monday));
        arrayList.add(context.getResources().getString(R.string.tuesday));
        arrayList.add(context.getResources().getString(R.string.wednesday));
        arrayList.add(context.getResources().getString(R.string.thursday));
        arrayList.add(context.getResources().getString(R.string.friday));
        arrayList.add(context.getResources().getString(R.string.saturday));
        return arrayList;
    }

    public static List<String> getStartupScreenData(Context context) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(context.getResources().getString(R.string.transaction));
        arrayList.add(context.getResources().getString(R.string.calendar));
        arrayList.add(context.getResources().getString(R.string.statistic));
        arrayList.add(context.getResources().getString(R.string.wallet));
        return arrayList;
    }

    public static List<String> getLanguageData(Context context) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(context.getString(R.string.system_default));
        arrayList.add("English");
        arrayList.add("Deutsche");
        arrayList.add("Español");
        arrayList.add("Français");
        arrayList.add("Bahasa Indonesia");
        arrayList.add("Bahasa Melayu");
        arrayList.add("Italiano");
        arrayList.add("Português");
        arrayList.add("Pусский");
        arrayList.add("日本語");
//        arrayList.add("简体中文");
//        arrayList.add("繁體中文");
        return arrayList;
    }

    public static List<String> getDayOfWeekData(Context context) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(context.getResources().getString(R.string.sunday));
        arrayList.add(context.getResources().getString(R.string.monday));
        arrayList.add(context.getResources().getString(R.string.tuesday));
        arrayList.add(context.getResources().getString(R.string.wednesday));
        arrayList.add(context.getResources().getString(R.string.thursday));
        arrayList.add(context.getResources().getString(R.string.friday));
        arrayList.add(context.getResources().getString(R.string.saturday));
        return arrayList;
    }

    public static List<String> getRestoreData(Context context) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(context.getResources().getString(R.string.sd_card));
        arrayList.add(context.getResources().getString(R.string.google_drive));
        return arrayList;
    }

    public static List<Integer> getRestoreIconData() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(Integer.valueOf((int) R.drawable.sd_card));
        arrayList.add(Integer.valueOf((int) R.drawable.gdrive));
        return arrayList;
    }

    public static List<String> getDayOfMonthData(Context context) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(context.getResources().getString(R.string.first_month_format, 1));
        arrayList.add(context.getResources().getString(R.string.second_month_format, 2));
        arrayList.add(context.getResources().getString(R.string.third_month_format, 3));
        arrayList.add(context.getResources().getString(R.string.general_month_format, 4));
        arrayList.add(context.getResources().getString(R.string.general_month_format, 5));
        arrayList.add(context.getResources().getString(R.string.general_month_format, 6));
        arrayList.add(context.getResources().getString(R.string.general_month_format, 7));
        arrayList.add(context.getResources().getString(R.string.general_month_format, 8));
        arrayList.add(context.getResources().getString(R.string.general_month_format, 9));
        arrayList.add(context.getResources().getString(R.string.general_month_format, 10));
        arrayList.add(context.getResources().getString(R.string.general_month_format, 11));
        arrayList.add(context.getResources().getString(R.string.general_month_format, 12));
        arrayList.add(context.getResources().getString(R.string.general_month_format, 13));
        arrayList.add(context.getResources().getString(R.string.general_month_format, 14));
        arrayList.add(context.getResources().getString(R.string.general_month_format, 15));
        arrayList.add(context.getResources().getString(R.string.general_month_format, 16));
        arrayList.add(context.getResources().getString(R.string.general_month_format, 17));
        arrayList.add(context.getResources().getString(R.string.general_month_format, 18));
        arrayList.add(context.getResources().getString(R.string.general_month_format, 19));
        arrayList.add(context.getResources().getString(R.string.general_month_format, 20));
        arrayList.add(context.getResources().getString(R.string.first_month_format, 21));
        arrayList.add(context.getResources().getString(R.string.second_month_format, 22));
        arrayList.add(context.getResources().getString(R.string.third_month_format, 23));
        arrayList.add(context.getResources().getString(R.string.general_month_format, 24));
        arrayList.add(context.getResources().getString(R.string.general_month_format, 25));
        arrayList.add(context.getResources().getString(R.string.general_month_format, 26));
        arrayList.add(context.getResources().getString(R.string.general_month_format, 27));
        arrayList.add(context.getResources().getString(R.string.general_month_format, 28));
        return arrayList;
    }

    public static List<String> getMonthOfQuarterData(Context context) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(context.getResources().getString(R.string.first_month));
        arrayList.add(context.getResources().getString(R.string.second_month));
        arrayList.add(context.getResources().getString(R.string.third_month));
        return arrayList;
    }

    public static List<String> getMonthOfYearData(Context context) {
        ArrayList arrayList = new ArrayList();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2, 0);
        while (true) {
            arrayList.add(new SimpleDateFormat("MMMM", Locale.getDefault()).format(calendar.getTime()));
            if (calendar.get(2) == 11) {
                return arrayList;
            }
            calendar.add(2, 1);
        }
    }

    public static String getReminderTime(Context context) {
        int reminderTime = SharePreferenceHelper.getReminderTime(context);
        if (reminderTime == 0) {
            return context.getResources().getString(R.string.not_set);
        }
        Resources resources = context.getResources();
        return resources.getString(R.string.setting_notification_hint, reminderTime + ":00");
    }
}
