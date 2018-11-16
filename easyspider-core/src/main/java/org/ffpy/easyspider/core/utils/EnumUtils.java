package org.ffpy.easyspider.core.utils;

public class EnumUtils {

    public static  <T> T fromStr(String s, Class<T> enumType) {
        for (T e : enumType.getEnumConstants()) {
            if (e.toString().equalsIgnoreCase(s))
                return e;
        }
        return null;
    }
}
