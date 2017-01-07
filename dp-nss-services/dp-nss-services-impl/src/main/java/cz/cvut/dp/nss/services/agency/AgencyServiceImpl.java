package cz.cvut.dp.nss.services.agency;

import cz.cvut.dp.nss.persistence.agency.AgencyDao;
import cz.cvut.dp.nss.services.common.AbstractEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of AgencyService.
 *
 * @author jakubchalupa
 * @since 05.01.17
 */
@Service
public class AgencyServiceImpl extends AbstractEntityService<Agency, String, AgencyDao> implements AgencyService {

    @Autowired
    public AgencyServiceImpl(AgencyDao dao) {
        super(dao);
    }

}
