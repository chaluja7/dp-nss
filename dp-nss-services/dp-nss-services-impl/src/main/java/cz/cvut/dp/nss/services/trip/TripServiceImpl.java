package cz.cvut.dp.nss.services.trip;

import cz.cvut.dp.nss.persistence.trip.JdbcTripDao;
import cz.cvut.dp.nss.persistence.trip.TripDao;
import cz.cvut.dp.nss.services.common.AbstractEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of TripService.
 *
 * @author jakubchalupa
 * @since 05.01.17
 */
@Service
public class TripServiceImpl extends AbstractEntityService<Trip, String, TripDao> implements TripService {

    @Autowired
    public TripServiceImpl(TripDao dao) {
        super(dao);
    }

    @Autowired
    private JdbcTripDao jdbcTripDao;

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public List<TripWrapper> getAllForInsertToGraph() {
        return jdbcTripDao.getAllForInsertToGraph();
    }

}
