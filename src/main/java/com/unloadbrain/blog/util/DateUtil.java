package com.unloadbrain.blog.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Provides date related utils
 */
public class DateUtil {

    /**
     * Return current date.
     */
    public Date getCurrentDate() {
        return new Date();
    }

    /**
     * Return Calendar object
     */
    public Calendar getCalendar() {
        return Calendar.getInstance();
    }

    /**
     * Return current year, month and date as string separated by forward slash.
     * For example: 2018/12/1 as 1st of December 2018
     */
    public String getCurrentYearMonthDateString() {
        Calendar calendar = getCalendar();
        return calendar.get(Calendar.YEAR)
                + "/"
                + (calendar.get(Calendar.MONTH) + 1)
                + "/"
                + calendar.get(Calendar.DATE);
    }
}
