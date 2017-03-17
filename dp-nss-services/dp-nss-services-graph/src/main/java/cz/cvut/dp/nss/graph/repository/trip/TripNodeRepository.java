package cz.cvut.dp.nss.graph.repository.trip;

import cz.cvut.dp.nss.graph.services.trip.TripNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

/**
 * @author jakubchalupa
 * @since 18.01.17
 */
public interface TripNodeRepository extends GraphRepository<TripNode> {

    /**
     * najde trip dle tripId
     * @param tripId tripId
     * @return tripNode dle tripId
     */
    @Query("match (n:TripNode {tripId: {0}}) return n")
    TripNode findByTripId(String tripId);

    /**
     * smaze tripNode s danym tripId
     * @param tripId tripId
     */
    @Query("match (n:TripNode {tripId: {0}}) detach delete n")
    void deleteTripNode(String tripId);

}
