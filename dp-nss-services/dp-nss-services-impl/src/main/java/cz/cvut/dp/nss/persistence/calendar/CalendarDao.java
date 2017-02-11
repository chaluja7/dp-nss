package cz.cvut.dp.nss.persistence.calendar;

import cz.cvut.dp.nss.persistence.generic.AbstractGenericJpaDao;
import cz.cvut.dp.nss.services.calendar.Calendar;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * JPA implementation of CalendarDao.
 *
 * @author jakubchalupa
 * @since 05.01.17
 */
@Component
@SuppressWarnings("JpaQlInspection")
public class CalendarDao extends AbstractGenericJpaDao<Calendar, String> {

    public CalendarDao() {
        super(Calendar.class);
    }

    /**
     * @return vsechny calendar s najoinovanymi calendarDate
     */
    public List<Calendar> getAllForInsertToGraph() {
        final String queryString = "select distinct c from Calendar c left outer join fetch c.calendarDates cd " +
            "order by c.id asc, cd.id asc";

        Query<Calendar> query = sessionFactory.getCurrentSession().createQuery(queryString, Calendar.class);
        return query.list();
    }

}
