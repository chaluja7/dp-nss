package cz.cvut.dp.nss.services.stop;

import cz.cvut.dp.nss.services.common.EntityService;

import java.util.Iterator;
import java.util.List;

/**
 * Common interface for all StopService implementations.
 *
 * @author jakubchalupa
 * @since 24.11.14 - 12.12.16
 */
public interface StopService extends EntityService<Stop, String> {

    /**
     * @param start index prvniho vraceneho zaznamu
     * @param limit max pocet vracenych zaznamu
     * @return vsechny stanice dle rozsahu (serazeno dle id vzestupne)
     */
    List<Stop> getAllInRange(final int start, final int limit);

    /**
     * @return iterator nad vsemi stanicemi
     */
    Iterator<Stop> iteratorOverAllStops();

}
