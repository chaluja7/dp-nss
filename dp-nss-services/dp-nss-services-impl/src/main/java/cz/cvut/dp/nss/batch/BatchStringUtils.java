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

    /**
     * @param o objekt k zapsani jako csv hodnota
     * @return string pro csv export. Pokud jde o string obsahujici ",", musi byt vysledek obalen do uvozovek.
     */
    public static String getCsvStringValue(Object o) {
        if(o == null) return "";

        if(o instanceof String) {
            String s = (String) o;
            //zdvojeni uvozovek, pokud obsahuje uvozovky a carku
            if(s.contains(",") && s.contains("\"")) {
                s = s.replaceAll("\"", "\"\"");
            }

            if(s.contains(",")) {
                //wrapnuti do uvozovek, pokud obsahuje carku
                return new StringBuilder("\"").append(s).append("\"").toString();
            } else {
                return s;
            }
        } else if(o instanceof Boolean) {
            return Boolean.TRUE.equals(o) ? "1" : "0";
        } else {
            return o.toString();
        }
    }

}
