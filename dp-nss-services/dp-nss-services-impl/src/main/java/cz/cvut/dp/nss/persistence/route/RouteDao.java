package cz.cvut.dp.nss.persistence.route;

import cz.cvut.dp.nss.persistence.generic.AbstractGenericJpaDao;
import cz.cvut.dp.nss.services.common.EnumUtils;
import cz.cvut.dp.nss.services.route.Route;
import cz.cvut.dp.nss.services.route.RouteFilter;
import cz.cvut.dp.nss.services.route.RouteType;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * JPA implementation of RouteDao.
 *
 * @author jakubchalupa
 * @since 05.01.17
 */
@Component
@SuppressWarnings("JpaQlInspection")
public class RouteDao extends AbstractGenericJpaDao<Route, String> {

    public RouteDao() {
        super(Route.class);
    }

    /**
     * @param searchQuery retezec, ktery se vyskytuje v ID nebo nazvu routy
     * @return vsechny routy, kde id nebo nazev odpovida searchQuery
     */
    public List<Route> findRoutesBySearchQuery(String searchQuery) {
        String queryString = "select r from Route r where lower(r.shortName) like lower(:searchQuery) ";
        queryString += "or lower(r.id) like lower(:searchQuery) or lower(r.longName) like lower(:searchQuery) order by lower(r.id)";

        Query<Route> query = sessionFactory.getCurrentSession().createQuery(queryString, Route.class);
        query.setParameter("searchQuery", searchQuery + "%");
        query.setMaxResults(10);

        return query.list();
    }

    /**
     * @param offset index prvniho vraceneho zaznamu
     * @param limit max pocet vracenych zaznamu
     * @return vsechny routy dle filtru
     */
    public List<Route> getByFilter(final RouteFilter filter, int offset, int limit, String orderColumn, boolean asc) {
        StringBuilder builder = new StringBuilder("select r from Route r where 1 = 1");
        addFilterParamsToSql(builder, filter);

        builder.append(" order by ");
        switch(orderColumn) {
            case "id":
                builder.append("lower(r.id)");
                break;
            case "shortName":
                builder.append("lower(r.shortName)");
                break;
            case "longName":
                builder.append("lower(r.longName)");
                break;
            case "typeCode":
                builder.append("r.routeType");
                break;
            case "color":
                builder.append("lower(r.color)");
                break;
            case "agencyId":
                builder.append("lower(r.agency.name)");
                break;
            default:
                builder.append("lower(r.id)");
                break;
        }

        if(!asc) {
            builder.append(" desc");
        }

        //aby to bylo deterministicke
        builder.append(", r.id");

        Query<Route> query = sessionFactory.getCurrentSession().createQuery(builder.toString(), Route.class);
        addFilterParamsToQuery(query, filter);

        query.setFirstResult(offset);
        query.setMaxResults(limit);

        return query.list();
    }

    /**
     * @return celkovy pocet zaznamu dle filtru
     */
    public long getCountByFilter(final RouteFilter filter) {
        StringBuilder builder = new StringBuilder("select count(r) from Route r where 1 = 1");
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
    private static void addFilterParamsToSql(StringBuilder builder, RouteFilter filter) {
        if(filter != null) {
            if(filter.getId() != null) {
                builder.append(" and lower(r.id) like lower(:id)");
            }
            if(filter.getShortName() != null) {
                builder.append(" and lower(r.shortName) like lower(:shortName)");
            }
            if(filter.getLongName() != null) {
                builder.append(" and lower(r.longName) like lower(:longName)");
            }
            if(filter.getTypeCode() != null) {
                builder.append(" and r.routeType = :routeType");
            }
            if(filter.getColor() != null) {
                builder.append(" and lower(r.color) like lower(:color)");
            }
            if(filter.getAgencyId() != null) {
                builder.append(" and r.agency.id = :agencyId");
            }
        }
    }

    /**
     * do result query naplni parametry dle filtru
     * @param query query pro naplneni parametry
     * @param filter filter pro vyber vysledku
     */
    private static void addFilterParamsToQuery(Query query, RouteFilter filter) {
        if(filter != null) {
            if(filter.getId() != null) {
                query.setParameter("id", filter.getId() + "%");
            }
            if(filter.getShortName() != null) {
                query.setParameter("shortName", "%" + filter.getShortName() + "%");
            }
            if(filter.getLongName() != null) {
                query.setParameter("longName", "%" + filter.getLongName() + "%");
            }
            if(filter.getTypeCode() != null) {
                query.setParameter("routeType", EnumUtils.fromCode(filter.getTypeCode(), RouteType.class));
            }
            if(filter.getColor() != null) {
                query.setParameter("color", "%" + filter.getColor() + "%");
            }
            if(filter.getAgencyId() != null) {
                query.setParameter("agencyId", filter.getAgencyId());
            }
        }
    }

}
