package cz.cvut.dp.nss.persistence.stop;

import cz.cvut.dp.nss.persistence.generic.AbstractGenericJpaDao;
import cz.cvut.dp.nss.services.stop.Stop;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * JPA implementation of StopDao.
 *
 * @author jakubchalupa
 * @since 05.01.17
 */
@Component
@SuppressWarnings("JpaQlInspection")
public class StopDao extends AbstractGenericJpaDao<Stop, String> {

    public StopDao() {
        super(Stop.class);
    }

    /**
     * @param start index prvniho vraceneho zaznamu
     * @param limit max pocet vracenych zaznamu
     * @return vsechny stanice dle rozsahu
     */
    public List<Stop> getAllInRange(final int start, final int limit) {
        final String queryString = "select distinct s from Stop s order by s.id asc";

        Query<Stop> query = sessionFactory.getCurrentSession().createQuery(queryString, Stop.class);
        query.setFirstResult(start);
        query.setMaxResults(limit);

        return query.list();
    }

    /**
     * @param startPattern retezec, kterym zacina nazev stanice
     * @return vsechny stanice, ktere zacinaji na pattern (case insensitive)
     */
    public List<String> findStopNamesByStartPattern(String startPattern) {
        final String queryString = "select distinct s.name from Stop s where lower(s.name) like lower(:startPattern) order by s.name";

        Query<String> query = sessionFactory.getCurrentSession().createQuery(queryString, String.class);
        query.setParameter("startPattern", startPattern + "%");

        return query.list();
    }

}
