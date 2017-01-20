package cz.cvut.dp.nss.graph.repository.trip;

import cz.cvut.dp.nss.graph.services.trip.TripNode;
import org.springframework.data.neo4j.repository.GraphRepository;

/**
 * @author jakubchalupa
 * @since 18.01.17
 */
public interface TripNodeRepository extends GraphRepository<TripNode> {

}
