package cz.cvut.dp.nss.services.ride;

import java.util.List;

/**
 * Common interface for all RideService implementations.
 *
 * @author jakubchalupa
 * @since 24.11.14 - 12.12.16
 */
public interface RideService {

    /**
     * find ride by id
     * @param id id of a ride
     * @return ride by id or null
     */
    Ride getRide(long id);

    /**
     * find ride by id with pre-loaded lazy initialization
     * @param id id of a ride
     * @return ride by id or null
     */
    Ride getRideLazyInitialized(long id);

    /**
     * update ride
     * @param ride ride to update
     * @return updated ride
     */
    Ride updateRide(Ride ride);

    /**
     * persists ride
     * @param ride ride to persist
     */
    void createRide(Ride ride);

    /**
     * delete ride
     * @param id id of ride to delete
     */
    void deleteRide(long id);

    /**
     * find all rides
     * @return all rides
     */
    List<Ride> getAll();

    /**
     * import the whole ride into neo4j graph db
     * @param id ride id
     */
    void importRideToNeo4j(Ride id);

    /**
     * find rides by line id
     * @param lineId id of a line
     * @return all rides by line id
     */
    List<Ride> getByLineId(long lineId);

}
