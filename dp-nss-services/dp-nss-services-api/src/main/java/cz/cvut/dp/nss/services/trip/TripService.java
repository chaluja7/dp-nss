package cz.cvut.dp.nss.services.trip;

import cz.cvut.dp.nss.services.common.EntityService;

import java.util.List;

/**
 * Common interface for all TripService implementations.
 *
 * @author jakubchalupa
 * @since 14.12.16
 */
public interface TripService extends EntityService<Trip, String> {

    /**
     * @return vsechny tripy vcetne nutnych atributu (calendar, stopTimes) pro vytvoreni kompletniho grafu v neo4j
     */
    List<Trip> getAllForInsertToGraph();

}
