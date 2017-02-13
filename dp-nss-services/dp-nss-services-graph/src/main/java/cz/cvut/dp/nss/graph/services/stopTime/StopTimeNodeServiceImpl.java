package cz.cvut.dp.nss.graph.services.stopTime;

import cz.cvut.dp.nss.graph.repository.stopTime.StopTimeNodeRepository;
import cz.cvut.dp.nss.graph.services.common.AbstractNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<StopTimeNode> findByStopNameOrderByActionTime(String stopName) {
        return repo.findByStopNameOrderByActionTime(stopName);
    }

    @Override
    @Transactional("neo4jTransactionManager")
    public void connectStopTimeNodesOnStopWithWaitingRelationship(String stopName) {
        List<StopTimeNode> stopTimeNodesX = findByStopNameOrderByActionTime(stopName);

        int i = 0;
        StopTimeNode firstStopNode = null;
        StopTimeNode stopNodeFrom = null;
        for(StopTimeNode stopNodeTo : stopTimeNodesX) {
            if(i == 0) {
                firstStopNode = stopNodeTo;
            }

            if(stopNodeFrom != null) {
                stopNodeFrom.setNextAwaitingStop(stopNodeTo);
                save(stopNodeFrom, 1);
            }

            //propojeni v kruhu (posledni s prvnim)
            if(i != 0 && i == stopTimeNodesX.size() - 1) {
                stopNodeTo.setNextAwaitingStop(firstStopNode);
                save(stopNodeTo, 1);
            }

            stopNodeFrom = stopNodeTo;
            i++;
        }

    }
}
