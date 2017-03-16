package cz.cvut.dp.nss.services.stopTime;

import cz.cvut.dp.nss.persistence.stopTime.StopTimeDao;
import cz.cvut.dp.nss.services.common.AbstractEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of StopTimeService.
 *
 * @author jakubchalupa
 * @since 05.01.17
 */
@Service
public class StopTimeServiceImpl extends AbstractEntityService<StopTime, Long, StopTimeDao> implements StopTimeService {

    @Autowired
    public StopTimeServiceImpl(StopTimeDao dao) {
        super(dao);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public StopTime getWithStop(Long id) {
        return id != null ? dao.getWithStop(id) : null;
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public StopTime getWithStopAndTripAndRoute(Long id) {
        return id != null ? dao.getWithStopAndTripAndRoute(id) : null;
    }

    @Override
    @Transactional(value = "transactionManager")
    public void deleteByTripId(String tripId) {
        if(tripId != null) dao.deleteByTripId(tripId);
    }

}
