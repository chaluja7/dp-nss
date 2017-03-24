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
     *
     * @param stopTimeId stopId
     * @return stopTime dle stopTimeId
     */
    StopTimeNode findByStopTimeId(Long stopTimeId);

    /**
     * vrati serazene stopy nalezici jedne stanici (dle jmena). Vyuziva se pro propojovani stopu na stanici hranami prestupnimi
     *
     * @param stopName nazev stanice
     * @return serazene stopTimes na stanici dle casu
     */
    List<StopTimeNode> findByStopNameOrderByActionTime(String stopName);

    /**
     * propoji stopTimes prestupnymi hranami (tedy v ramci jedne stanice)
     *
     * @param stopName nazev stanice, kde chceme propojovat (tedy vyzadujeme unikatni nazvy opravdu totoznych stanic v ramci datasetu)
     */
    void connectStopTimeNodesOnStopWithWaitingRelationship(String stopName);

    /**
     * prida novy stopTimeNode do jiz existujici struktury stopTimes v ramci jedne stanice (propojeni dle casu)
     * tedy najde spravne misto, kam node vlozit a prepropoji uzli tak, aby novy node zapadl do grafu
     *
     * @param stopTimeNode novy stopTimeNode
     */
    void addNewStopTimeToStop(StopTimeNode stopTimeNode);

    StopTimeNode findFirstByStopNameAndTimeBefore(String stopName, Integer time);

    void deleteStopTimesByTripId(String tripId);

    int deleteChunk();

    /**
     * ulozi NODE vcetne navazanych do uvedene hloubky
     * @param node NODE k ulozeni
     * @param depth max hloubka ulozeni (atributu)
     * @param performSessionClear pokud true tak provede session.clear()
     * @return ulozeny NODE
     */
    StopTimeNode save(StopTimeNode node, int depth, boolean performSessionClear);

    /**
     * aktualizuje wheelChair flag na vsech stopTimes pod danou stanici
     *
     * @param stopId id stanice
     * @param stopIsWheelChairAccessible true, pokud je na dane stanici mozne nastoupit s vozickem
     */
    void changeWheelChairOnStop(String stopId, boolean stopIsWheelChairAccessible);

}
