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
     * @param start index prvniho vraceneho zaznamu
     * @param limit max pocet vracenych zaznamu
     * @return vsechny tripy dle rozsahu s najoinovanymi calendar, stopTimes a stops
     */
    public List<Trip> getAllForInsertToGraph(final int start, final int limit) {
        final String queryString = "select distinct t from Trip t left outer join fetch t.calendar c " +
            "left outer join fetch t.stopTimes st left outer join fetch st.stop s order by t.id asc, st.sequence asc";

        Query<Trip> query = sessionFactory.getCurrentSession().createQuery(queryString, Trip.class);
        query.setFirstResult(start);
        query.setMaxResults(limit);

        return query.list();
    }

}
