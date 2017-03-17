package cz.cvut.dp.nss.graph.repository.stopTime;

import cz.cvut.dp.nss.graph.services.common.StopTimeQueryResult;
import cz.cvut.dp.nss.graph.services.stopTime.StopTimeNode;
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
     * @param stopName nazev stanice
     * @return serazene stopTimes na stanici dle casu
     */
    @Query("match (n:StopTimeNode {stopName: {0}}) return n order by case when n.departureInSeconds is not null then n.departureInSeconds else n.arrivalInSeconds end, n.id")
    List<StopTimeNode> findByStopNameOrderByActionTime(String stopName);


    /**
     * najde stopTime dle stopName a casu (prvni na danem stopTime, ktery ma cas bezprostredne predchazejici casu z parametru)
     * metoda slouzi k pridavani novych stopTimes do jiz existujici struktury grafu
     * @param stopName nazev stanice
     * @param time cas uzlu (departure nebo arrival)
     * @return stopTime dle stopTimeId
     */
    @Query("match(n:StopTimeNode {stopName: {0}}) " +
        "where case when n.departureInSeconds is not null then n.departureInSeconds else n.arrivalInSeconds end < {1} " +
        "with n order by case when n.departureInSeconds is not null then n.departureInSeconds else n.arrivalInSeconds end desc, n.id desc " +
        "limit 1 match (n)-[r:NEXT_AWAITING_STOP]->(m) return n as stopTimeNode, m as nextAwaitingStop")
    StopTimeQueryResult findFirstByStopNameAndTimeBefore(String stopName, Integer time);

    /**
     * najde stopTime ktery je dle casu uplne posledni stopTime na stanici s danym jmenem
     * metoda slouzi k pridavani novych stopTimes do jiz existujici struktury grafu
     * @param stopName nazev stanice
     * @return stopTime dle stopTimeId
     */
    @Query("match(n:StopTimeNode {stopName: {0}}) with n " +
        "order by case when n.departureInSeconds is not null then n.departureInSeconds else n.arrivalInSeconds end desc, n.id desc " +
        "limit 1 match (n)-[r:NEXT_AWAITING_STOP]->(m) return n as stopTimeNode, m as nextAwaitingStop")
    StopTimeQueryResult findLastByStopName(String stopName);

    /**
     * smaze relaci nextAwaitingStop k danemu nodeId
     * @param nodeId nodeId
     */
    @Query("match (n:StopTimeNode)-[r:NEXT_AWAITING_STOP]->(m:StopTimeNode) where ID(n) = {0} delete r")
    void deleteNextAwaitingStopRelation(Long nodeId);

    /**
     * ssmaze vsechny stopTimes s danym tripId
     * @param tripId tripId
     */
    @Query("match (n:StopTimeNode {tripId: {0}}) detach delete n")
    void deleteStopTimesByTripId(String tripId);

    /**
     * @param tripId tripId
     * @return stopTimes s danym tripId navic s prev a next awaiting stop
     */
    @Query("match(n:StopTimeNode {tripId: {0}})-[r:NEXT_AWAITING_STOP]->(m:StopTimeNode) match(n)<-[s:NEXT_AWAITING_STOP]-(o:StopTimeNode) " +
        "return n as stopTimeNode, m as nextAwaitingStop, o as prevAwaitingStop")
    List<StopTimeQueryResult> findStopTimesToDeleteByTripId(String tripId);

}
