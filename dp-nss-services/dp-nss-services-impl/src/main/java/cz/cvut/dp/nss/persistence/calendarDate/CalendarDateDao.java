package cz.cvut.dp.nss.persistence.calendarDate;

import cz.cvut.dp.nss.persistence.generic.AbstractGenericJpaDao;
import cz.cvut.dp.nss.services.calendarDate.CalendarDate;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

/**
 * JPA implementation of CalendarDateDao.
 *
 * @author jakubchalupa
 * @since 05.01.17
 */
@Component
public class CalendarDateDao extends AbstractGenericJpaDao<CalendarDate, Long> {

    public CalendarDateDao() {
        super(CalendarDate.class);
    }

    @Override
    public void truncateAll() {
        Query query = sessionFactory.getCurrentSession().createNativeQuery("truncate calendar_date cascade");
        query.executeUpdate();
    }

}
