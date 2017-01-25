package cz.cvut.dp.nss.persistence.stopTime;

import cz.cvut.dp.nss.persistence.generic.AbstractGenericJpaDao;
import cz.cvut.dp.nss.services.stopTime.StopTime;
import org.springframework.stereotype.Component;

/**
 * JPA implementation of StopTimeDao.
 *
 * @author jakubchalupa
 * @since 05.01.17
 */
@Component
public class StopTimeDao extends AbstractGenericJpaDao<StopTime, Long> {

    public StopTimeDao() {
        super(StopTime.class);
    }

}
