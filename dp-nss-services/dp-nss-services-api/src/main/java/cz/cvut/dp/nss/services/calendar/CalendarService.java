package cz.cvut.dp.nss.services.calendar;

import cz.cvut.dp.nss.services.common.EntityService;

import java.util.List;

/**
 * Common interface for all CalendarService implementations.
 *
 * @author jakubchalupa
 * @since 14.12.16
 */
public interface CalendarService extends EntityService<Calendar, String> {

    /**
     * @return vsechny calendar s najoinovanymi calendar date pro insert do neo4j
     */
    List<Calendar> getAllForInsertToGraph();

    /**
     * @return vsechny zaznamy, serazene dle id
     */
    List<Calendar> getAllOrdered();

    /**
     * @param filter filter
     * @param offset index prvniho vraceneho zaznamu
     * @param limit max pocet vracenych zaznamu
     * @param orderColumn radici sloupec
     * @param asc true pokud radim asc, false jinak (desc)
     * @return vsechny zaznamy dle filtru
     */
    List<Calendar> getByFilter(final CalendarFilter filter, Integer offset, Integer limit, String orderColumn, boolean asc);

    /**
     * @return celkovy pocet zaznamu dle filtru (pro ucely strankovani)
     */
    long getCountByFilter(final CalendarFilter filter);

    /**
     * @param id id calendar
     * @return true, pokud muze byt calendar smazan (nema zadny napojeny trip)
     */
    boolean canBeDeleted(String id);

    void create(Calendar calendar, boolean neo4jSynchronize);

    void update(Calendar calendar, boolean neo4jSynchronize);

    void delete(String s, boolean neo4jSynchronize);

}
