package cz.cvut.dp.nss.services.stopTime;

import cz.cvut.dp.nss.persistence.stopTime.StopTimeDao;
import cz.cvut.dp.nss.services.common.AbstractEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
