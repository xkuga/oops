package com.onepiece.kuga.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Datetime {
    /**
     * default format
     */
    protected static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm";

    /**
     * default locale
     */
    protected static final Locale DEFAULT_LOCALE = Locale.CHINA;

    /**
     * format
     *
     * @param timestamp timestamp
     * @return String
     */
    public static String format(long timestamp) {
        return format(timestamp, DEFAULT_FORMAT, DEFAULT_LOCALE);
    }

    /**
     * format
     *
     * @param timestamp timestamp
     * @param format format
     * @return String
     */
    public static String format(long timestamp, String format) {
        return format(timestamp, format, DEFAULT_LOCALE);
    }

    /**
     * format
     *
     * @param timestamp timestamp
     * @param locale locale
     * @return String
     */
    public static String format(long timestamp, Locale locale) {
        return format(timestamp, DEFAULT_FORMAT, locale);
    }

    /**
     * format
     *
     * @param timestamp timestamp
     * @param format format
     * @param locale locale
     * @return String
     */
    public static String format(long timestamp, String format, Locale locale) {
        return new SimpleDateFormat(format, locale).format(new Date(timestamp));
    }
}
