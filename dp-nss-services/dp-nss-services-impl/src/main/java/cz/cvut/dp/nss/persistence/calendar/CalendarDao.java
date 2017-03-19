package cz.cvut.dp.nss.persistence.calendar;

import cz.cvut.dp.nss.persistence.generic.AbstractGenericJpaDao;
import cz.cvut.dp.nss.services.calendar.Calendar;
import cz.cvut.dp.nss.services.calendar.CalendarFilter;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * JPA implementation of CalendarDao.
 *
 * @author jakubchalupa
 * @since 05.01.17
 */
@Component
@SuppressWarnings("JpaQlInspection")
public class CalendarDao extends AbstractGenericJpaDao<Calendar, String> {

    public CalendarDao() {
        super(Calendar.class);
    }

    /**
     * @return vsechny calendar s najoinovanymi calendarDate
     */
    public List<Calendar> getAllForInsertToGraph() {
        final String queryString = "select distinct c from Calendar c left outer join fetch c.calendarDates cd " +
            "order by c.id asc, cd.id asc";

        Query<Calendar> query = sessionFactory.getCurrentSession().createQuery(queryString, Calendar.class);
        return query.list();
    }

    /**
     * @return vsechny zaznamy, serazene dle jmena
     */
    public List<Calendar> getAllOrdered() {
        final String queryString = "select c from Calendar c order by lower(c.id)";

        Query<Calendar> query = sessionFactory.getCurrentSession().createQuery(queryString, Calendar.class);
        return query.list();
    }

    /**
     * @param offset index prvniho vraceneho zaznamu
     * @param limit max pocet vracenych zaznamu
     * @return vsechny zaznamy dle rozsahu
     */
    public List<Calendar> getByFilter(final CalendarFilter filter, int offset, int limit, String orderColumn, boolean asc) {
        StringBuilder builder = new StringBuilder("select c from Calendar c where 1 = 1");
        addFilterParamsToSql(builder, filter);

        builder.append(" order by ");
        switch(orderColumn) {
            case "id":
                builder.append("lower(c.id)");
                break;
            case "startDate":
                builder.append("c.startDate");
                break;
            case "endDate":
                builder.append("c.endDate");
                break;
            default:
                builder.append("lower(c.id)");
                break;
        }

        if(!asc) {
            builder.append(" desc");
        }

        //aby to bylo deterministicke
        builder.append(", c.id");

        Query<Calendar> query = sessionFactory.getCurrentSession().createQuery(builder.toString(), Calendar.class);
        addFilterParamsToQuery(query, filter);

        query.setFirstResult(offset);
        query.setMaxResults(limit);

        return query.list();
    }

    /**
     * @return celkovy pocet zaznamu dle filtru
     */
    public long getCountByFilter(final CalendarFilter filter) {
        StringBuilder builder = new StringBuilder("select count(c) from Calendar c where 1 = 1");
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
    private static void addFilterParamsToSql(StringBuilder builder, CalendarFilter filter) {
        if(filter != null) {
            if(filter.getId() != null) {
                builder.append(" and lower(c.id) like lower(:id)");
            }
            if(filter.getStartDate() != null) {
                builder.append(" and c.startDate = :startDate");
            }
            if(filter.getEndDate() != null) {
                builder.append(" and c.endDate = :endDate");
            }
        }
    }

    /**
     * do result query naplni parametry dle filtru
     * @param query query pro naplneni parametry
     * @param filter filter pro vyber vysledku
     */
    private static void addFilterParamsToQuery(Query query, CalendarFilter filter) {
        if(filter != null) {
            if(filter.getId() != null) {
                query.setParameter("id", filter.getId() + "%");
            }
            if(filter.getStartDate() != null) {
                query.setParameter("startDate", filter.getStartDate());
            }
            if(filter.getEndDate() != null) {
                query.setParameter("endDate", filter.getEndDate());
            }
        }
    }

    @Override
    public void truncateAll() {
        Query query = sessionFactory.getCurrentSession().createNativeQuery("truncate calendar cascade");
        query.executeUpdate();
    }

}
