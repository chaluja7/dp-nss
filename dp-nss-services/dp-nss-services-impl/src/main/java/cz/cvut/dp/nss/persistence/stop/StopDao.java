package cz.cvut.dp.nss.persistence.stop;

import cz.cvut.dp.nss.persistence.generic.AbstractGenericJpaDao;
import cz.cvut.dp.nss.services.common.EnumUtils;
import cz.cvut.dp.nss.services.stop.Stop;
import cz.cvut.dp.nss.services.stop.StopFilter;
import cz.cvut.dp.nss.services.stop.StopWheelchairBoardingType;
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
        final String queryString = "select s from Stop s order by lower(s.id) asc";

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
        query.setMaxResults(10);

        return query.list();
    }

    /**
     * @param searchQuery retezec, ktery se vyskytuje v ID nebo nazvu stanice
     * @return vsechny stanice, kde id nebo nazev odpovida searchQuery
     */
    public List<Stop> findStopsBySearchQuery(String searchQuery) {
        final String queryString = "select s from Stop s where lower(s.name) like lower(:searchQuery) or lower(s.id) like lower(:searchQuery) order by lower(s.id)";

        Query<Stop> query = sessionFactory.getCurrentSession().createQuery(queryString, Stop.class);
        query.setParameter("searchQuery", searchQuery + "%");
        query.setMaxResults(10);

        return query.list();
    }

    /**
     * @param offset index prvniho vraceneho zaznamu
     * @param limit max pocet vracenych zaznamu
     * @return vsechny stanice dle rozsahu
     */
    public List<Stop> getByFilter(final StopFilter filter, int offset, int limit, String orderColumn, boolean asc) {
        StringBuilder builder = new StringBuilder("select s from Stop s where 1 = 1");
        addFilterParamsToSql(builder, filter);

        builder.append(" order by ");
        switch(orderColumn) {
            case "id":
                builder.append("lower(s.id)");
                break;
            case "name":
                builder.append("lower(s.name)");
                break;
            case "lat":
                builder.append("s.lat");
                break;
            case "lon":
                builder.append("s.lon");
                break;
            case "wheelChair":
                builder.append("s.stopWheelchairBoardingType");
                break;
            case "parentStopId":
                builder.append("lower(s.parentStop.id)");
                break;
            default:
                builder.append("lower(s.id)");
                break;
        }

        if(!asc) {
            builder.append(" desc");
        }

        //aby to bylo deterministicke
        builder.append(", s.id");

        Query<Stop> query = sessionFactory.getCurrentSession().createQuery(builder.toString(), Stop.class);
        addFilterParamsToQuery(query, filter);

        query.setFirstResult(offset);
        query.setMaxResults(limit);

        return query.list();
    }

    /**
     * @return celkovy pocet zaznamu dle filtru
     */
    public long getCountByFilter(final StopFilter filter) {
        StringBuilder builder = new StringBuilder("select count(s) from Stop s where 1 = 1");
        addFilterParamsToSql(builder, filter);

        Query<Long> query = sessionFactory.getCurrentSession().createQuery(builder.toString(), Long.class);
        addFilterParamsToQuery(query, filter);

        return query.uniqueResult();
    }

    /**
     * do sql query prida sql dotazy na parametry dle filtru
     * @param builder builder s tvorenym sql dotazem
     * @param filter filter pro vyber vysledku
     */
    private static void addFilterParamsToSql(StringBuilder builder, StopFilter filter) {
        if(filter != null) {
            if(filter.getId() != null) {
                builder.append(" and lower(s.id) like lower(:id)");
            }
            if(filter.getName() != null) {
                builder.append(" and lower(s.name) like lower(:name)");
            }
            if(filter.getLat() != null) {
                builder.append(" and s.lat = :lat");
            }
            if(filter.getLon() != null) {
                builder.append(" and s.lon = :lon");
            }
            if(filter.getWheelChairCode() != null) {
                builder.append(" and s.stopWheelchairBoardingType = :wheelChair");
            }
            if(filter.getParentStopId() != null) {
                builder.append(" and s.parentStop.id = :parentStopId");
            }
        }
    }

    /**
     * do result query naplni parametry dle filtru
     * @param query query pro naplneni parametry
     * @param filter filter pro vyber vysledku
     */
    private static void addFilterParamsToQuery(Query query, StopFilter filter) {
        if(filter != null) {
            if(filter.getId() != null) {
                query.setParameter("id", filter.getId() + "%");
            }
            if(filter.getName() != null) {
                query.setParameter("name", "%" + filter.getName() + "%");
            }
            if(filter.getLat() != null) {
                query.setParameter("lat", filter.getLat());
            }
            if(filter.getLon() != null) {
                query.setParameter("lon", filter.getLon());
            }
            if(filter.getWheelChairCode() != null) {
                query.setParameter("wheelChair", EnumUtils.fromCode(filter.getWheelChairCode(), StopWheelchairBoardingType.class));
            }
            if(filter.getParentStopId() != null) {
                query.setParameter("parentStopId", filter.getParentStopId());
            }
        }
    }

    @Override
    public void truncateAll() {
        Query query = sessionFactory.getCurrentSession().createNativeQuery("truncate stops cascade");
        query.executeUpdate();
    }

}
