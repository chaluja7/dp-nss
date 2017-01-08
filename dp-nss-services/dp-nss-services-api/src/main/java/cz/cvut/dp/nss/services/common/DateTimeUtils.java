package cz.cvut.dp.nss.services.common;

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

    public static final String GTFS_DATE_PATTERN = "yyyyMMdd";

    public static final String TIME_PATTERN = "HH:mm";

    public static final String TIME_WITH_MILLIS_PATTERN = "HH:mm:ss.SSS";

    public static final String GTFS_TIME_PATTERN = "H:mm:ss";

    /**
     * pocet milisekund za 24 hodin
     */
    public static final int MILLIS_IN_DAY = 86400000;

    /**
     * 5 minut penalizace za prestup
     */
    public static final int TRANSFER_PENALTY_MILLIS = 300000;

    /**
     * je thread-safe viz https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
     */
    public static final DateTimeFormatter GTFS_DATE_PATTERN_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(GTFS_DATE_PATTERN);

    /**
     * je thread-safe viz https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
     */
    public static final DateTimeFormatter GTFS_TIME_PATTERN_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(GTFS_TIME_PATTERN);

}
