package com.acme.web.framework;

public final class WebHelper {

    private WebHelper() {}

    public static String getContextPath() {
        return "/mtatest";
    }

    public static String buildUrl(String path) {
        return getContextPath() + path;
    }

    public static boolean isAjaxRequest(String header) {
        return "XMLHttpRequest".equals(header);
    }
}
