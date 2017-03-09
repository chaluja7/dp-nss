package cz.cvut.dp.nss.persistence.timeTable;

import cz.cvut.dp.nss.persistence.generic.AbstractGenericJpaDao;
import cz.cvut.dp.nss.services.timeTable.TimeTable;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * JPA implementation of TimeTableDao.
 *
 * @author jakubchalupa
 * @since 05.01.17
 */
@Component
@SuppressWarnings("JpaQlInspection")
public class TimeTableDao extends AbstractGenericJpaDao<TimeTable, String> {

    public TimeTableDao() {
        super(TimeTable.class);
    }

    /**
     * @param validOnly pokud true tak vracim pouze validni jizdni rady
     * @return vsechny jizdni rady
     */
    public List<TimeTable> getAll(boolean validOnly) {
        String queryString = "select distinct t from TimeTable t";
        if(validOnly) {
            queryString += " where t.valid = true";
        }
        queryString += " order by t.name";

        Query<TimeTable> query = sessionFactory.getCurrentSession().createQuery(queryString, TimeTable.class);
        return query.list();
    }

    /**
     * @param ids id pozadovanych jizdnich radu
     * @return vsechny jizdni rady dle zadanych id (i nevalidni)
     */
    public List<TimeTable> getAllByIds(Set<String> ids) {
        String queryString = "select distinct t from TimeTable t where t.id in (:ids)";
        queryString += " order by t.name";

        Query<TimeTable> query = sessionFactory.getCurrentSession().createQuery(queryString, TimeTable.class);
        query.setParameterList("ids", ids);

        return query.list();
    }

}
