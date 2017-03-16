package cz.cvut.dp.nss.persistence.trip;

import cz.cvut.dp.nss.persistence.generic.AbstractGenericJpaDao;
import cz.cvut.dp.nss.services.common.EnumUtils;
import cz.cvut.dp.nss.services.trip.Trip;
import cz.cvut.dp.nss.services.trip.TripFilter;
import cz.cvut.dp.nss.services.trip.TripWheelchairAccessibleType;
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

    /**
     * @param filter tripFilter
     * @param offset index prvniho vraceneho zaznamu
     * @param limit max pocet vracenych zaznamu
     * @param orderColumn radici sloupec
     * @param asc pokud false, radime desc
     * @return vsechny routy dle filtru
     */
    public List<Trip> getByFilter(final TripFilter filter, int offset, int limit, String orderColumn, boolean asc) {
        StringBuilder builder = new StringBuilder("select t from Trip t where 1 = 1");
        addFilterParamsToSql(builder, filter);

        builder.append(" order by ");
        switch(orderColumn) {
            case "id":
                builder.append("lower(t.id)");
                break;
            case "headSign":
                builder.append("lower(t.headSign)");
                break;
            case "shapeId":
                builder.append("lower(t.shapeId)");
                break;
            case "wheelChairCode":
                builder.append("t.tripWheelchairAccessibleType");
                break;
            case "calendarId":
                builder.append("lower(t.calendar.id)");
                break;
            case "routeShortName":
                builder.append("lower(t.route.shortName)");
                break;
            default:
                builder.append("lower(t.id)");
                break;
        }

        if(!asc) {
            builder.append(" desc");
        }

        //aby to bylo deterministicke
        builder.append(", t.id");

        Query<Trip> query = sessionFactory.getCurrentSession().createQuery(builder.toString(), Trip.class);
        addFilterParamsToQuery(query, filter);

        query.setFirstResult(offset);
        query.setMaxResults(limit);

        return query.list();
    }

    /**
     * @return celkovy pocet zaznamu dle filtru
     */
    public long getCountByFilter(final TripFilter filter) {
        StringBuilder builder = new StringBuilder("select count(t) from Trip t where 1 = 1");
        addFilterParamsToSql(builder, filter);

        Query<Long> query = sessionFactory.getCurrentSession().createQuery(builder.toString(), Long.class);
        addFilterParamsToQuery(query, filter);

        return query.uniqueResult();
    }

    /**
     * @param id trip id
     * @return trip s nafetchovanymi lazy atributy
     */
    public Trip getLazyInitialized(String id) {
        final String queryString = "select t from Trip t left outer join fetch t.calendar c " +
            "left outer join fetch t.route r left outer join fetch t.stopTimes st left outer join fetch st.stop s " +
            "where t.id = :id";

        Query<Trip> query = sessionFactory.getCurrentSession().createQuery(queryString, Trip.class);
        query.setParameter("id", id);

        return query.uniqueResult();
    }

    /**
     * do sql query prida sql dotazy na parametry dle filtru
     * @param builder builder s tvorenym sql dotazem
     * @param filter filter pro vyber vysledku
     */
    private static void addFilterParamsToSql(StringBuilder builder, TripFilter filter) {
        if(filter != null) {
            if(filter.getId() != null) {
                builder.append(" and lower(t.id) like lower(:id)");
            }
            if(filter.getHeadSign() != null) {
                builder.append(" and lower(t.headSign) like lower(:headSign)");
            }
            if(filter.getCalendarId() != null) {
                builder.append(" and lower(t.calendar.id) like lower(:calendarId)");
            }
            if(filter.getRouteShortName() != null) {
                builder.append(" and lower(t.route.shortName) like lower(:routeShortName)");
            }
            if(filter.getShapeId() != null) {
                builder.append(" and lower(t.shapeId) like lower(:shapeId)");
            }
            if(filter.getWheelChairCode() != null) {
                builder.append(" and t.tripWheelchairAccessibleType = :wheelChair");
            }
        }
    }

    /**
     * do result query naplni parametry dle filtru
     * @param query query pro naplneni parametry
     * @param filter filter pro vyber vysledku
     */
    private static void addFilterParamsToQuery(Query query, TripFilter filter) {
        if(filter != null) {
            if(filter.getId() != null) {
                query.setParameter("id", filter.getId() + "%");
            }
            if(filter.getHeadSign() != null) {
                query.setParameter("headSign", filter.getHeadSign() + "%");
            }
            if(filter.getCalendarId() != null) {
                query.setParameter("calendarId", filter.getCalendarId() + "%");
            }
            if(filter.getRouteShortName() != null) {
                query.setParameter("routeShortName", filter.getRouteShortName() + "%");
            }
            if(filter.getShapeId() != null) {
                query.setParameter("shapeId", filter.getShapeId() + "%");
            }
            if(filter.getWheelChairCode() != null) {
                query.setParameter("wheelChair", EnumUtils.fromCode(filter.getWheelChairCode(), TripWheelchairAccessibleType.class));
            }
        }
    }

}
