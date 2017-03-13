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
     * @return vsechny tripy s najoinovanymi calendar, stopTimes a stops
     */
    List<TripWrapper> getAllForInsertToGraph();

    /**
     * @param calendarId id calendar
     * @return pocet tripu navazanych na calendar dle id
     */
    long getCountByCalendarId(String calendarId);

}
