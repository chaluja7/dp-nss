package cz.cvut.dp.nss.services.stop;

import cz.cvut.dp.nss.services.common.EntityService;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

    /**
     * @param startPattern retezec, kterym zacina nazev stanice
     * @return vsechny stanice, ktere zacinaji na pattern (case insensitive)
     */
    Set<String> findStopNamesByStartPattern(String startPattern);

    /**
     * @param searchQuery retezec, ktery se vyskytuje v ID nebo nazvu stanice
     * @return vsechny stanice, kde id nebo nazev odpovida searchQuery
     */
    List<Stop> findStopsBySearchQuery(String searchQuery);

    /**
     * @param offset index prvniho vraceneho zaznamu
     * @param limit max pocet vracenych zaznamu
     * @param orderColumn radici sloupec
     * @param asc true pokud radim asc, false jinak (desc)
     * @return vsechny stanice dle filtru
     */
    List<Stop> getByFilter(final StopFilter filter, Integer offset, Integer limit, String orderColumn, boolean asc);

    /**
     * @return celkovy pocet zaznamu dle filtru (pro ucely strankovani)
     */
    long getCountByFilter(final StopFilter filter);

    /**
     * @param id id stanice
     * @return true, pokud muze byt stanice smazana (nema zadne stopTimes ani podrizene stanice)
     */
    boolean canBeDeleted(String id);

}
