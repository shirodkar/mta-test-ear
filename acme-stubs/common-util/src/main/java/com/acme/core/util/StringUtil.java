package com.acme.core.util;

public final class StringUtil {

    private StringUtil() {}

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static String nullSafe(String value) {
        return value != null ? value : "";
    }

    public static String truncate(String value, int maxLength) {
        if (value == null || value.length() <= maxLength) return value;
        return value.substring(0, maxLength);
    }
}
