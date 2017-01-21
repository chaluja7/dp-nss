package cz.cvut.dp.nss.graph.repository.stopTime;

import cz.cvut.dp.nss.graph.services.stopTime.StopTimeNode;
import cz.cvut.dp.nss.graph.services.stopTime.StopTimeQueryResult;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import java.util.List;

/**
 * @author jakubchalupa
 * @since 18.01.17
 */
public interface StopTimeNodeRepository extends GraphRepository<StopTimeNode> {

    /**
     * najde stopTime dle stopTimeId
     * @param stopTimeId stopTimeId
     * @return stopTime dle stopTimeId
     */
    @Query("match (n:StopTimeNode {stopTimeId: {0}}) optional match (n)-[r:IN_TRIP]->(m:TripNode) " +
        "optional match (n)-[s:NEXT_STOP]->(o:StopTimeNode) " +
        "optional match (n)-[t:NEXT_AWAITING_STOP]->(p:StopTimeNode) " +
        "return n as stopTimeNode, m as tripNode, o as nextStop, p as nextAwaitingStop")
    StopTimeQueryResult findByStopTimeId(Long stopTimeId);

    /**
     * vrati serazene stopy nalezici jedne stanici. Vyuziva se pro propojovani stopu na stanici hranami prestupnimi
     * @param stopId id stanice
     * @return serazene stopTimes na stanici dle casu
     */
    @Query("match (n:StopTimeNode {stopId: {0}}) return n order by case when n.departureInMillis is not null then n.departureInMillis else n.arrivalInMillis end")
    List<StopTimeNode> findByStopIdOrderByActionTime(String stopId);

}
