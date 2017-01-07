package cz.cvut.dp.nss.services.stop;

import cz.cvut.dp.nss.persistence.stop.StopDao;
import cz.cvut.dp.nss.services.common.AbstractEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of StopService.
 *
 * @author jakubchalupa
 * @since 05.01.17
 */
@Service
public class StopServiceImpl extends AbstractEntityService<Stop, String, StopDao> implements StopService {

    @Autowired
    public StopServiceImpl(StopDao dao) {
        super(dao);
    }

}
