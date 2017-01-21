package cz.cvut.dp.nss.graph.services.stopTime;

import cz.cvut.dp.nss.graph.repository.stopTime.StopTimeNodeRepository;
import cz.cvut.dp.nss.graph.services.common.AbstractNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return repo.findByStopTimeId(stopTimeId);
    }
}
