package cz.cvut.dp.nss.services.stop;

import java.util.List;

/**
 * Common interface for all StopService implementations.
 *
 * @author jakubchalupa
 * @since 24.11.14 - 12.12.16
 */
public interface StopService {

    /**
     * find stop by id
     * @param id stop
     * @return stop by id or null
     */
    Stop getStop(long id);

    /**
     * update stop
     * @param stop stop to update
     * @return updated stop
     */
    Stop updateStop(Stop stop);

    /**
     * persists stop
     * @param stop stop to persist
     */
    void createStop(Stop stop);

    /**
     * delete stop
     * @param id id of stop to delete
     */
    void deleteStop(long id);

    /**
     * find all stops
     * @return all stops
     */
    List<Stop> getAll();

}
