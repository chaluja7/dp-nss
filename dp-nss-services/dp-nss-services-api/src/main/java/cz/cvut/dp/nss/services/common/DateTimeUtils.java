package cz.cvut.dp.nss.services.common;

import org.joda.time.format.DateTimeFormat;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * DateTime utils.
 *
 * @author jakubchalupa
 * @since 29.11.14 - 12.12.16
 */
public final class DateTimeUtils {

    public static final String DATE_TIME_PATTERN = "dd.MM.yyyy HH:mm";

    public static final String DATE_PATTERN = "dd.MM.yyyy";

    public static final String SIMPLE_DATE_PATTERN = "d.M.yyyy";

    public static final String GTFS_DATE_PATTERN = "yyyyMMdd";

    public static final String TIME_PATTERN = "HH:mm";

    public static final String TIME_WITH_MILLIS_PATTERN = "HH:mm:ss.SSS";

    public static final String GTFS_TIME_PATTERN = "H:mm:ss";

    public static final String SIMPLE_TIME_PATTERN = "H:m:s";

    /**
     * pocet sekund za 24 hodin
     */
    public static final int SECONDS_IN_DAY = 86400;

    /**
     * 2,5 minuty minimalne nutne na prestup
     */
    public static final int MIN_TRANSFER_SECONDS = 150;

    /**
     * je thread-safe
     */
    public static final org.joda.time.format.DateTimeFormatter JODA_DATE_FORMATTER = DateTimeFormat.forPattern(DATE_PATTERN);

    /**
     * je thread-safe
     */
    public static final org.joda.time.format.DateTimeFormatter JODA_DATE_TIME_FORMATTER = DateTimeFormat.forPattern(DATE_TIME_PATTERN);

    /**
     * je thread-safe viz https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
     */
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    /**
     * je thread-safe viz https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
     */
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(SIMPLE_DATE_PATTERN);

    /**
     * je thread-safe viz https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
     */
    public static final DateTimeFormatter GTFS_DATE_PATTERN_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(GTFS_DATE_PATTERN);

    /**
     * je thread-safe viz https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
     */
    public static final DateTimeFormatter GTFS_TIME_PATTERN_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(GTFS_TIME_PATTERN);

    /**
     * je thread-safe viz https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
     */
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(SIMPLE_TIME_PATTERN);


    /**
     * @param localTime java localTime
     * @return pocet sekund v ramci localTime
     */
    public static int getSecondsOfDay(LocalTime localTime) {
        return localTime.getSecond() + (localTime.getMinute() * 60) + (localTime.getHour() * 60 * 60);
    }

}
