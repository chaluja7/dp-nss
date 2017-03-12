package cz.cvut.dp.nss.persistence.agency;

import cz.cvut.dp.nss.persistence.generic.AbstractGenericJpaDao;
import cz.cvut.dp.nss.services.agency.Agency;
import cz.cvut.dp.nss.services.agency.AgencyFilter;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * JPA implementation of AgencyDao.
 *
 * @author jakubchalupa
 * @since 05.01.17
 */
@Component
@SuppressWarnings("JpaQlInspection")
public class AgencyDao extends AbstractGenericJpaDao<Agency, String> {

    public AgencyDao() {
        super(Agency.class);
    }

    /**
     * @return vsechny doprace, serazene dle jmena
     */
    public List<Agency> getAllOrdered() {
        final String queryString = "select a from Agency a order by lower(a.name)";

        Query<Agency> query = sessionFactory.getCurrentSession().createQuery(queryString, Agency.class);
        return query.list();
    }

    /**
     * @param offset index prvniho vraceneho zaznamu
     * @param limit max pocet vracenych zaznamu
     * @return vsechny stanice dle rozsahu
     */
    public List<Agency> getByFilter(final AgencyFilter filter, int offset, int limit, String orderColumn, boolean asc) {
        StringBuilder builder = new StringBuilder("select a from Agency a where 1 = 1");
        addFilterParamsToSql(builder, filter);

        builder.append(" order by ");
        switch(orderColumn) {
            case "id":
                builder.append("lower(a.id)");
                break;
            case "name":
                builder.append("lower(a.name)");
                break;
            case "url":
                builder.append("lower(a.url)");
                break;
            case "phone":
                builder.append("lower(a.phone)");
                break;
            default:
                builder.append("lower(a.id)");
                break;
        }

        if(!asc) {
            builder.append(" desc");
        }

        //aby to bylo deterministicke
        builder.append(", a.id");

        Query<Agency> query = sessionFactory.getCurrentSession().createQuery(builder.toString(), Agency.class);
        addFilterParamsToQuery(query, filter);

        query.setFirstResult(offset);
        query.setMaxResults(limit);

        return query.list();
    }

    /**
     * @return celkovy pocet zaznamu dle filtru
     */
    public long getCountByFilter(final AgencyFilter filter) {
        StringBuilder builder = new StringBuilder("select count(distinct a) from Agency a where 1 = 1");
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
    private static void addFilterParamsToSql(StringBuilder builder, AgencyFilter filter) {
        if(filter != null) {
            if(filter.getId() != null) {
                builder.append(" and lower(a.id) like lower(:id)");
            }
            if(filter.getName() != null) {
                builder.append(" and lower(a.name) like lower(:name)");
            }
            if(filter.getUrl() != null) {
                builder.append(" and lower(a.url) like lower(:url)");
            }
            if(filter.getPhone() != null) {
                builder.append(" and lower(a.phone) like lower(:phone)");
            }
        }
    }

    /**
     * do result query naplni parametry dle filtru
     * @param query query pro naplneni parametry
     * @param filter filter pro vyber vysledku
     */
    private static void addFilterParamsToQuery(Query query, AgencyFilter filter) {
        if(filter != null) {
            if(filter.getId() != null) {
                query.setParameter("id", filter.getId() + "%");
            }
            if(filter.getName() != null) {
                query.setParameter("name", "%" + filter.getName() + "%");
            }
            if(filter.getUrl() != null) {
                query.setParameter("url", "%" + filter.getUrl() + "%");
            }
            if(filter.getPhone() != null) {
                query.setParameter("phone", "%" + filter.getPhone() + "%");
            }
        }
    }

}
