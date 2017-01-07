package cz.cvut.dp.nss.services.calendarDate;

import cz.cvut.dp.nss.persistence.calendarDate.CalendarDateDao;
import cz.cvut.dp.nss.services.common.AbstractEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of CalendarDateService.
 *
 * @author jakubchalupa
 * @since 05.01.17
 */
@Service
public class CalendarDateServiceImpl extends AbstractEntityService<CalendarDate, Long, CalendarDateDao> implements CalendarDateService {

    @Autowired
    public CalendarDateServiceImpl(CalendarDateDao dao) {
        super(dao);
    }

}
