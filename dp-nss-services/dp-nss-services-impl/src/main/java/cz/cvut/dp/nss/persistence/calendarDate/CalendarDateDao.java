package cz.cvut.dp.nss.persistence.calendarDate;

import cz.cvut.dp.nss.persistence.generic.AbstractGenericJpaDao;
import cz.cvut.dp.nss.services.calendarDate.CalendarDate;
import org.springframework.stereotype.Repository;

/**
 * JPA implementation of CalendarDateDao.
 *
 * @author jakubchalupa
 * @since 05.01.17
 */
@Repository
public class CalendarDateDao extends AbstractGenericJpaDao<CalendarDate, Long> {

    public CalendarDateDao() {
        super(CalendarDate.class);
    }

}
