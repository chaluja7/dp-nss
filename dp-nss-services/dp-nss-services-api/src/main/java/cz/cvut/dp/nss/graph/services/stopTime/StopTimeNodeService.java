package cz.cvut.dp.nss.graph.services.stopTime;

import cz.cvut.dp.nss.graph.services.common.NodeService;

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

}
