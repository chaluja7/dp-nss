package cz.cvut.dp.nss.graph.services.trip;

import cz.cvut.dp.nss.graph.repository.trip.TripNodeRepository;
import cz.cvut.dp.nss.graph.services.common.AbstractNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jakubchalupa
 * @since 18.01.17
 */
@Service
public class TripNodeServiceImpl extends AbstractNodeService<TripNode, TripNodeRepository> implements TripNodeService {

    @Autowired
    public TripNodeServiceImpl(TripNodeRepository repository) {
        super(repository);
    }

    @Override
    public TripNode findByTripId(String tripId) {
        return repo.findByTripId(tripId);
    }
}
