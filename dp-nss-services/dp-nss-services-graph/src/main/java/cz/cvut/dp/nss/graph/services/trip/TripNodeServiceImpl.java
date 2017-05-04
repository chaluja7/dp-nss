package cz.cvut.dp.nss.graph.services.trip;

import cz.cvut.dp.nss.graph.repository.trip.TripNodeRepository;
import cz.cvut.dp.nss.graph.services.common.AbstractNodeService;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Implementace TripNodeService.
 *
 * @author jakubchalupa
 * @since 18.01.17
 */
@Service
public class TripNodeServiceImpl extends AbstractNodeService<TripNode, TripNodeRepository> implements TripNodeService {

    @Autowired
    private Session session;

    @Autowired
    public TripNodeServiceImpl(TripNodeRepository repository) {
        super(repository);
    }

    @Resource(name = "tripNodeServiceImpl")
    private TripNodeService tripNodeService;

    @Override
    @Transactional("neo4jTransactionManager")
    public TripNode findByTripId(String tripId) {
        return repo.findByTripId(tripId);
    }

    @Override
    @Transactional("neo4jTransactionManager")
    public void deleteTripNode(String tripId) {
        repo.deleteTripNode(tripId);
    }

    @Override
    @Transactional("neo4jTransactionManager")
    public int deleteChunk() {
        int count = repo.chunkDeleteAll();
        session.clear();

        return count;
    }

    @Override
    public void deleteAll() {
        //schvalne neni transactional ale odmazava se to postupne (protoze potvrzeni transakce, kde se smaze vsechno vubec nemusi dobehnout)
        int count;
        do {
            count = tripNodeService.deleteChunk();
        } while (count > 0);
    }

}
