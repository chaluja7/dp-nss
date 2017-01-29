package cz.cvut.dp.nss.services.trip;

import cz.cvut.dp.nss.services.common.EntityService;

import java.util.Iterator;
import java.util.List;

/**
 * Common interface for all TripService implementations.
 *
 * @author jakubchalupa
 * @since 14.12.16
 */
public interface TripService extends EntityService<Trip, String> {

    /**
     * @param start index prvniho vraceneho zaznamu
     * @param limit max pocet vracenych zaznamu
     * @return vsechny tripy dle rozsahu s najoinovanymi calendar, stopTimes a stops
     */
    List<Trip> getAllForInsertToGraph(final int start, final int limit);

    /**
     * @return iterator nad zaznamy pro vlozeni do databaze neo4j
     */
    Iterator<Trip> iteratorOverTripsForInsertToGraph();

}
