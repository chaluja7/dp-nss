package cz.cvut.dp.nss.persistence.calendar;

import cz.cvut.dp.nss.persistence.generic.AbstractGenericJpaDao;
import cz.cvut.dp.nss.services.calendar.Calendar;
import org.springframework.stereotype.Component;

/**
 * JPA implementation of CalendarDao.
 *
 * @author jakubchalupa
 * @since 05.01.17
 */
@Component
public class CalendarDao extends AbstractGenericJpaDao<Calendar, String> {

    public CalendarDao() {
        super(Calendar.class);
    }

}
