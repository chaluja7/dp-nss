package cz.cvut.dp.nss.services;

import cz.cvut.dp.nss.services.common.DateTimeUtils;
import cz.cvut.dp.nss.services.stop.StopServiceImpl;
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

        String fixedStopName = StopServiceImpl.getFixedStopName("Mníšek p. Brdy,závod (hl.sil.)");
        fixedStopName = StopServiceImpl.getFixedStopName("Nádraží Hostivař (ul. Průmyslová)");
        fixedStopName = StopServiceImpl.getFixedStopName("Želivského (ul. Votická)");
        fixedStopName = StopServiceImpl.getFixedStopName("Želivského");
        int k = 0;
    }

}
