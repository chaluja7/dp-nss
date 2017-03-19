package cz.cvut.dp.nss.persistence.stopTime;

import cz.cvut.dp.nss.persistence.generic.AbstractGenericJpaDao;
import cz.cvut.dp.nss.services.stopTime.StopTime;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

/**
 * JPA implementation of StopTimeDao.
 *
 * @author jakubchalupa
 * @since 05.01.17
 */
@Component
@SuppressWarnings("JpaQlInspection")
public class StopTimeDao extends AbstractGenericJpaDao<StopTime, Long> {

    public StopTimeDao() {
        super(StopTime.class);
    }

    public StopTime getWithStop(Long id) {
        final String queryString = "select st from StopTime st inner join fetch st.stop where st.id = :id";

        Query<StopTime> query = sessionFactory.getCurrentSession().createQuery(queryString, StopTime.class);
        query.setParameter("id", id);
        return query.uniqueResult();
    }

    public StopTime getWithStopAndTripAndRoute(Long id) {
        final String queryString = "select st from StopTime st inner join fetch st.stop s inner join fetch st.trip t " +
            " inner join fetch t.route r where st.id = :id";

        Query<StopTime> query = sessionFactory.getCurrentSession().createQuery(queryString, StopTime.class);
        query.setParameter("id", id);
        return query.uniqueResult();
    }

    /**
     * smaze vsechny stopTimes k danemu tripId
     * @param tripId tripId
     */
    public void deleteByTripId(String tripId) {
        final String queryString = "delete from StopTime where trip.id = :tripId";

        Query query = sessionFactory.getCurrentSession().createQuery(queryString);
        query.setParameter("tripId", tripId);

        query.executeUpdate();
    }

    @Override
    public void truncateAll() {
        Query query = sessionFactory.getCurrentSession().createNativeQuery("truncate stop_times cascade");
        query.executeUpdate();
    }

}
