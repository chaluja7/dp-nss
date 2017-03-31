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
     * @param limit max pocet tripu
     * @param offset offset tripu (razeno dle id tripu asc)
     * @return vsechny tripy s najoinovanymi calendar, stopTimes a stops
     */
    List<TripWrapper> getAllForInsertToGraph(int limit, int offset);

    /**
     * @param calendarId id calendar
     * @return pocet tripu navazanych na calendar dle id
     */
    long getCountByCalendarId(String calendarId);

    /**
     * @param filter filter
     * @param offset index prvniho vraceneho zaznamu
     * @param limit max pocet vracenych zaznamu
     * @param orderColumn radici sloupec
     * @param asc true pokud radim asc, false jinak (desc)
     * @return vsechny tripy dle filtru s fetchnutymi routami a calendars
     */
    List<Trip> getByFilter(TripFilter filter, Integer offset, Integer limit, String orderColumn, boolean asc);

    /**
     * @return celkovy pocet zaznamu dle filtru (pro ucely strankovani)
     */
    long getCountByFilter(TripFilter filter);

    /**
     * @param id trip id
     * @return trip s fetchnutou routou, calendar a vsemi stopTimes, ktere navic maji fetchnute stops
     */
    Trip getLazyInitialized(String id);

    void create(Trip trip, boolean neo4jSynchronize);

    void update(Trip trip, boolean neo4jSynchronize);

    void delete(String s, boolean neo4jSynchronize);

}
