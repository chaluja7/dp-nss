package cz.cvut.dp.nss.services.timeTable;

import cz.cvut.dp.nss.persistence.timeTable.TimeTableDao;
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
 * @since 02.03.17
 */
@Service
public class TimeTableServiceImpl extends AbstractEntityService<TimeTable, String, TimeTableDao> implements TimeTableService {

    @Autowired
    public TimeTableServiceImpl(TimeTableDao dao) {
        super(dao);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public List<TimeTable> getAll(boolean validOnly) {
        return dao.getAll(validOnly);
    }

}
