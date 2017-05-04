package cz.cvut.dp.nss.graph.services.trip;

import cz.cvut.dp.nss.graph.services.common.NodeService;

/**
 * Servica jizdy.
 *
 * @author jakubchalupa
 * @since 18.01.17
 */
public interface TripNodeService extends NodeService<TripNode> {

    /**
     * najde trip dle tripId
     * @param tripId stopId
     * @return tripNode dle tripId
     */
    TripNode findByTripId(String tripId);

    /**
     * smaze tripNode s danym tripId vcetne hran IN_TRIP
     * @param tripId tripId
     */
    void deleteTripNode(String tripId);

    /**
     * smaze cast ze vsech uzlu jizd
     * @return pocet smazanych jizd
     */
    int deleteChunk();

}
