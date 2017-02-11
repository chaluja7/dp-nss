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

}
