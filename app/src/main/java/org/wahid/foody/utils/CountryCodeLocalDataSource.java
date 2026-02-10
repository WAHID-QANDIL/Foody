package org.wahid.foody.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CountryCodeLocalDataSource {

    private static final Map<String, String> areaToCodeMap = new HashMap<>();

    static {
        areaToCodeMap.put("Algerian", "DZ");
        areaToCodeMap.put("American", "US");
        areaToCodeMap.put("Argentinian", "AR");
        areaToCodeMap.put("Australian", "AU");
        areaToCodeMap.put("British", "GB");
        areaToCodeMap.put("Canadian", "CA");
        areaToCodeMap.put("Chinese", "CN");
        areaToCodeMap.put("Croatian", "HR");
        areaToCodeMap.put("Dutch", "NL");
        areaToCodeMap.put("Egyptian", "EG");
        areaToCodeMap.put("Filipino", "PH");
        areaToCodeMap.put("French", "FR");
        areaToCodeMap.put("Greek", "GR");
        areaToCodeMap.put("Indian", "IN");
        areaToCodeMap.put("Irish", "IE");
        areaToCodeMap.put("Italian", "IT");
        areaToCodeMap.put("Jamaican", "JM");
        areaToCodeMap.put("Japanese", "JP");
        areaToCodeMap.put("Kenyan", "KE");
        areaToCodeMap.put("Malaysian", "MY");
        areaToCodeMap.put("Mexican", "MX");
        areaToCodeMap.put("Moroccan", "MA");
        areaToCodeMap.put("Norwegian", "NO");
        areaToCodeMap.put("Polish", "PL");
        areaToCodeMap.put("Portuguese", "PT");
        areaToCodeMap.put("Russian", "RU");
        areaToCodeMap.put("Saudi Arabian", "SA");
        areaToCodeMap.put("Slovakian", "SK");
        areaToCodeMap.put("Spanish", "ES");
        areaToCodeMap.put("Syrian", "SY");
        areaToCodeMap.put("Thai", "TH");
        areaToCodeMap.put("Tunisian", "TN");
        areaToCodeMap.put("Turkish", "TR");
        areaToCodeMap.put("Ukrainian", "UA");
        areaToCodeMap.put("Uruguayan", "UY");
        areaToCodeMap.put("Venezulan", "VE");
        areaToCodeMap.put("Vietnamese", "VN");
    }

    public static  String getCountryCode(String areaName) {
        if (areaName == null) return null;
        return areaToCodeMap.get(areaName);
    }
    public List<String> getAllAreas() {
        return new ArrayList<>(areaToCodeMap.keySet());
    }
    public static String getImageUrl(String areaName) {
        if (areaName == null) return null;

        String code = getCountryCode(areaName);
        if (code == null) return null;

        return "https://flagcdn.com/w160/" + code.toLowerCase() + ".png";
    }

}