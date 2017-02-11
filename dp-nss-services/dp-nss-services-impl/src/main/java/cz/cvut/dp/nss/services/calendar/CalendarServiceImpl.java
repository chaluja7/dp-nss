package cz.cvut.dp.nss.services.calendar;

import cz.cvut.dp.nss.persistence.calendar.CalendarDao;
import cz.cvut.dp.nss.services.common.AbstractEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of CalendarService.
 *
 * @author jakubchalupa
 * @since 05.01.17
 */
@Service
public class CalendarServiceImpl extends AbstractEntityService<Calendar, String, CalendarDao> implements CalendarService {

    @Autowired
    public CalendarServiceImpl(CalendarDao dao) {
        super(dao);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Calendar> getAllForInsertToGraph() {
        return dao.getAllForInsertToGraph();
    }
}
