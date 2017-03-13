package cz.cvut.dp.nss.persistence.trip;

import cz.cvut.dp.nss.persistence.generic.AbstractGenericJpaDao;
import cz.cvut.dp.nss.services.trip.Trip;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * JPA implementation of TripDao.
 *
 * @author jakubchalupa
 * @since 05.01.17
 */
@Component
@SuppressWarnings("JpaQlInspection")
public class TripDao extends AbstractGenericJpaDao<Trip, String> {

    public TripDao() {
        super(Trip.class);
    }

    /**
     * @return vsechny tripy s najoinovanymi calendar, stopTimes a stops
     */
    public List<Trip> getAllForInsertToGraph() {
        final String queryString = "select distinct t from Trip t left outer join fetch t.calendar c " +
            "left outer join fetch t.stopTimes st left outer join fetch st.stop s order by t.id asc, st.sequence asc";

        Query<Trip> query = sessionFactory.getCurrentSession().createQuery(queryString, Trip.class);
        return query.list();
    }

    /**
     * @param calendarId id calendar
     * @return pocet tripu navazanych na calendar dle id
     */
    public long getCountByCalendarId(String calendarId) {
        StringBuilder builder = new StringBuilder("select count(distinct t) from Trip t where t.calendar.id = :calendarId");

        Query<Long> query = sessionFactory.getCurrentSession().createQuery(builder.toString(), Long.class);
        query.setParameter("calendarId", calendarId);

        return query.uniqueResult();
    }

}
