package cz.cvut.dp.nss.services;

import cz.cvut.dp.nss.services.common.DateTimeUtils;
import org.junit.Test;

import java.time.LocalTime;

/**
 * TODO jen helper, pak smazat!
 *
 * @author jakubchalupa
 * @since 16.03.17
 */
public class TestTest {

    @Test
    public void testNeco() {
        String s = "6:4:5";

        LocalTime parse = LocalTime.parse(s, DateTimeUtils.TIME_FORMATTER);
        int i = 0;
    }

}
