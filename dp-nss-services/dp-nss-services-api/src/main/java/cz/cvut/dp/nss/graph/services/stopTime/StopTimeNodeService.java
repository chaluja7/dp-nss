package cz.cvut.dp.nss.graph.services.stopTime;

import cz.cvut.dp.nss.graph.services.common.NodeService;

import java.util.List;

/**
 * @author jakubchalupa
 * @since 18.01.17
 */
public interface StopTimeNodeService extends NodeService<StopTimeNode> {

    /**
     * najde stopTime dle stopTimeId
     * @param stopTimeId stopId
     * @return stopTime dle stopTimeId
     */
    StopTimeNode findByStopTimeId(Long stopTimeId);

    /**
     * vrati serazene stopy nalezici jedne stanici. Vyuziva se pro propojovani stopu na stanici hranami prestupnimi
     * @param stopId id stanice
     * @return serazene stopTimes na stanici dle casu
     */
    List<StopTimeNode> findByStopIdOrderByActionTime(String stopId);

    /**
     * propoji stopTimes prestupnymi hranami (tedy v ramci jedne stanice)
     * @param stopId id stanice
     */
    void connectStopTimeNodesOnStopWithWaitingRelationship(String stopId);

}
