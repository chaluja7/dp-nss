package cz.cvut.dp.nss.persistence.stop;

import cz.cvut.dp.nss.persistence.generic.AbstractGenericJpaDao;
import cz.cvut.dp.nss.services.stop.Stop;
import org.springframework.stereotype.Repository;

/**
 * JPA implementation of StopDao.
 *
 * @author jakubchalupa
 * @since 05.01.17
 */
@Repository
public class StopDao extends AbstractGenericJpaDao<Stop, String> {

    public StopDao() {
        super(Stop.class);
    }

}