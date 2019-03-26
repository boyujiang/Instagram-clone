package com.cas.jiamin.mogic.Utility;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * DateUtil class
 *
 * The DateUtil class provides a method to get the current system date(year, month, day, hour,
 * minute, second)
 */
public class DateUtil {

    /**
     * The method takes no argument, gets and returns the current system date(year, month, day, hour
     * , minute, second)
     *
     * @return Return the current system date as a string in the specified pattern(yyyyMMddhhmmss).
     */
    @SuppressLint("SimpleDateFormat")
    public static String getNowDateTime() {
        SimpleDateFormat s_format = new SimpleDateFormat("yyyyMMddhhmmss");
        return s_format.format(new Date());
    }

}
