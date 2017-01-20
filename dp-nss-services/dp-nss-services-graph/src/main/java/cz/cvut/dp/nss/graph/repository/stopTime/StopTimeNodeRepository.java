package cz.cvut.dp.nss.graph.repository.stopTime;

import cz.cvut.dp.nss.graph.services.stopTime.StopTimeNode;
import org.springframework.data.neo4j.repository.GraphRepository;

/**
 * @author jakubchalupa
 * @since 18.01.17
 */
public interface StopTimeNodeRepository extends GraphRepository<StopTimeNode> {

}
