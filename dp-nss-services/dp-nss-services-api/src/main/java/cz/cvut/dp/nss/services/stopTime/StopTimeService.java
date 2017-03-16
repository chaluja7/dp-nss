package cz.cvut.dp.nss.services.stopTime;

import cz.cvut.dp.nss.services.common.EntityService;

/**
 * Common interface for all StopTimeService implementations.
 *
 * @author jakubchalupa
 * @since 14.12.16
 */
public interface StopTimeService extends EntityService<StopTime, Long> {

    StopTime getWithStop(Long id);

    StopTime getWithStopAndTripAndRoute(Long id);

    /**
     * smaze vsechny stopTimes k danemu tripId
     * @param tripId tripId
     */
    void deleteByTripId(String tripId);

}
