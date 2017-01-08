package cz.cvut.dp.nss.batch;

import org.springframework.util.StringUtils;

/**
 * @author jakubchalupa
 * @since 08.01.17
 */
public final class BatchStringUtils {

    public static String notEmptyStringOrNull(String s) {
        return StringUtils.hasText(s) ? s : null;
    }

    public static boolean booleanValue(String s) {
        return "1".equals(s) || "true".equalsIgnoreCase(s);
    }

}
