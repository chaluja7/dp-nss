package cz.cvut.dp.nss.persistence.stopTime;

import cz.cvut.dp.nss.persistence.generic.AbstractGenericJpaDao;
import cz.cvut.dp.nss.services.stopTime.StopTime;
import org.springframework.stereotype.Repository;

/**
 * JPA implementation of StopTimeDao.
 *
 * @author jakubchalupa
 * @since 05.01.17
 */
@Repository
public class StopTimeDao extends AbstractGenericJpaDao<StopTime, Long> {

    public StopTimeDao() {
        super(StopTime.class);
    }

}
