package cz.cvut.dp.nss.graph.services.stopTime;

import cz.cvut.dp.nss.graph.repository.stopTime.StopTimeNodeRepository;
import cz.cvut.dp.nss.graph.services.common.AbstractNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jakubchalupa
 * @since 18.01.17
 */
@Service
public class StopTimeNodeServiceImpl extends AbstractNodeService<StopTimeNode, StopTimeNodeRepository> implements StopTimeNodeService {

    @Autowired
    public StopTimeNodeServiceImpl(StopTimeNodeRepository repository) {
        super(repository);
    }

    @Override
    public StopTimeNode findByStopTimeId(Long stopTimeId) {
        return repo.findByStopTimeId(stopTimeId).createStopTimeNode();
    }

    @Override
    public List<StopTimeNode> findByStopIdOrderByActionTime(String stopId) {
        return repo.findByStopIdOrderByActionTime(stopId);
    }

    @Override
    public void connectStopTimeNodesOnStopWithWaitingRelationship(String stopId) {
        List<StopTimeNode> stopTimeNodesX = findByStopIdOrderByActionTime(stopId);

        int i = 0;
        StopTimeNode firstStopNode = null;
        StopTimeNode stopNodeFrom = null;
        for(StopTimeNode stopNodeTo : stopTimeNodesX) {
            if(i == 0) {
                firstStopNode = stopNodeTo;
            }

            if(stopNodeFrom != null) {
                stopNodeFrom.setNextAwaitingStop(stopNodeTo);
                save(stopNodeFrom);
            }

            //propojeni v kruhu (posledni s prvnim)
            if(i != 0 && i == stopTimeNodesX.size() - 1) {
                stopNodeTo.setNextAwaitingStop(firstStopNode);
                save(stopNodeTo);
            }

            stopNodeFrom = stopNodeTo;
            i++;
        }
    }
}
