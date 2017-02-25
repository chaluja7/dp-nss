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
public class StopTimeDao extends AbstractGenericJpaDao<StopTime, Long> {

    public StopTimeDao() {
        super(StopTime.class);
    }

    @SuppressWarnings("JpaQlInspection")
    public StopTime getWithStop(Long id) {
        final String queryString = "select st from StopTime st inner join fetch st.stop where st.id = :id";

        Query<StopTime> query = sessionFactory.getCurrentSession().createQuery(queryString, StopTime.class);
        query.setParameter("id", id);
        return query.uniqueResult();
    }

    @SuppressWarnings("JpaQlInspection")
    public StopTime getWithStopAndTripAndRoute(Long id) {
        final String queryString = "select st from StopTime st inner join fetch st.stop s inner join fetch st.trip t " +
            " inner join fetch t.route r where st.id = :id";

        Query<StopTime> query = sessionFactory.getCurrentSession().createQuery(queryString, StopTime.class);
        query.setParameter("id", id);
        return query.uniqueResult();
    }

}
