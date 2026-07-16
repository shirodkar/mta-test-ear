package com.acme.core.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DateUtil {

    private DateUtil() {}

    public static String formatDate(Date date, String pattern) {
        if (date == null) return null;
        return new SimpleDateFormat(pattern).format(date);
    }

    public static boolean isBusinessDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dow = cal.get(Calendar.DAY_OF_WEEK);
        return dow != Calendar.SATURDAY && dow != Calendar.SUNDAY;
    }

    public static Date addBusinessDays(Date startDate, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        int added = 0;
        while (added < days) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
            if (isBusinessDay(cal.getTime())) {
                added++;
            }
        }
        return cal.getTime();
    }
}
