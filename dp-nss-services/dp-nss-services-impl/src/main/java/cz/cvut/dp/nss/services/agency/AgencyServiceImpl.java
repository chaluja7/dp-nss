package cz.cvut.dp.nss.services.agency;

import cz.cvut.dp.nss.persistence.agency.AgencyDao;
import cz.cvut.dp.nss.services.common.AbstractEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Agency> getAllOrdered() {
        return dao.getAllOrdered();
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Agency> getByFilter(final AgencyFilter filter, Integer offset, Integer limit, String orderColumn, boolean asc) {
        if(limit != null && limit <= 0) return new ArrayList<>();
        return dao.getByFilter(filter, getOffsetOrDefault(offset), getLimitOrDefault(limit), orderColumn != null ? orderColumn : "", asc);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public long getCountByFilter(final AgencyFilter filter) {
        return dao.getCountByFilter(filter);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public boolean canBeDeleted(String id) {
        Agency agency = get(id);
        return agency != null && agency.getRoutes().isEmpty();
    }
}
