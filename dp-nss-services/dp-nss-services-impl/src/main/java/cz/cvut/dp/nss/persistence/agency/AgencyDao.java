package cz.cvut.dp.nss.persistence.agency;

import cz.cvut.dp.nss.persistence.generic.AbstractGenericJpaDao;
import cz.cvut.dp.nss.services.agency.Agency;
import org.springframework.stereotype.Component;

/**
 * JPA implementation of AgencyDao.
 *
 * @author jakubchalupa
 * @since 05.01.17
 */
@Component
public class AgencyDao extends AbstractGenericJpaDao<Agency, String> {

    public AgencyDao() {
        super(Agency.class);
    }

}
