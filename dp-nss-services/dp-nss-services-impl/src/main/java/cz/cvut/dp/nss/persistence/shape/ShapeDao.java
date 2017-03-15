package cz.cvut.dp.nss.persistence.shape;

import cz.cvut.dp.nss.persistence.generic.AbstractGenericJpaDao;
import cz.cvut.dp.nss.services.shape.Shape;
import cz.cvut.dp.nss.services.shape.ShapeFilter;
import cz.cvut.dp.nss.services.shape.ShapeId;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

/**
 * JPA implementation of ShapeDao.
 *
 * @author jakubchalupa
 * @since 05.01.17
 */
@Component
@SuppressWarnings("JpaQlInspection")
public class ShapeDao extends AbstractGenericJpaDao<Shape, ShapeId> {

    public ShapeDao() {
        super(Shape.class);
    }

    /**
     * @param id shapeId
     * @return vsechny shapes s danym shapeId. serazene dle sequence vzestupne
     */
    public List<Shape> getByShapeId(String id) {
        final String queryString = "select s from Shape s where s.id.shapeId = :id order by s.id.sequence";

        Query<Shape> query = sessionFactory.getCurrentSession().createQuery(queryString, Shape.class);
        query.setParameter("id", id);

        return query.list();
    }

    /**
     * smaze vsechny shapes s danym shapeId
     * @param id shapeId
     */
    public void deleteByShapeId(String id) {
        final String queryString = "delete from Shape where id.shapeId = :id";

        Query query = sessionFactory.getCurrentSession().createQuery(queryString);
        query.setParameter("id", id);

        query.executeUpdate();
    }

    /**
     * @param idPattern pattern shapeId
     * @return vsechny shapes s shapeId odpovidajicim patternu. Serazene dle shapeId a nasledne dle sequnce vzestupne
     */
    public List<String> findShapeIdsByLikeId(String idPattern) {
        final String queryString = "select distinct(s.id.shapeId) from Shape s where lower(s.id.shapeId) like lower(:idPattern) order by s.id.shapeId";

        Query<String> query = sessionFactory.getCurrentSession().createQuery(queryString, String.class);
        query.setParameter("idPattern", idPattern + "%");
        query.setMaxResults(10);

        return query.list();
    }

    /**
     * @param offset index prvniho vraceneho zaznamu
     * @param limit max pocet vracenych zaznamu
     * @return vsechny stanice dle rozsahu
     */
    public List<String> getShapeIdsByFilter(final ShapeFilter filter, int offset, int limit, String orderColumn, boolean asc) {
        StringBuilder builder = new StringBuilder("select distinct s.id.shapeId from Shape s where 1 = 1");
        addFilterParamsToSql(builder, filter);

        builder.append(" order by ");
        switch(orderColumn) {
            case "id":
                builder.append("s.id.shapeId");
                break;
            default:
                builder.append("s.id.shapeId");
                break;
        }

        if(!asc) {
            builder.append(" desc");
        }

        //aby to bylo deterministicke
        builder.append(", s.id.shapeId");

        Query<String> query = sessionFactory.getCurrentSession().createQuery(builder.toString(), String.class);
        addFilterParamsToQuery(query, filter);

        query.setFirstResult(offset);
        query.setMaxResults(limit);

        return query.list();
    }

    /**
     * @param filter filter
     * @return pocet zaznamu unikatnich shapeId dle filtru
     */
    public long getCountByFilter(final ShapeFilter filter) {
        StringBuilder builder = new StringBuilder("select count(*) from (select distinct s.shapeId from shapes s where 1 = 1");
        addFilterParamsToSqlNative(builder, filter);
        builder.append(") as tmp");

        Query query = sessionFactory.getCurrentSession().createNativeQuery(builder.toString());
        addFilterParamsToQuery(query, filter);

        return ((BigInteger) query.uniqueResult()).longValue();
    }

    /**
     * do sql query prida sql dotazy na parametry dle filtru
     * @param builder builder s tvorenym sql dotazem
     * @param filter filter pro vyber vysledku
     */
    private static void addFilterParamsToSql(StringBuilder builder, ShapeFilter filter) {
        if(filter != null) {
            if(filter.getId() != null) {
                builder.append(" and lower(s.id.shapeId) like lower(:id)");
            }
        }
    }

    /**
     * do sql query prida sql dotazy na parametry dle filtru
     * @param builder builder s tvorenym sql dotazem
     * @param filter filter pro vyber vysledku
     */
    private static void addFilterParamsToSqlNative(StringBuilder builder, ShapeFilter filter) {
        if(filter != null) {
            if(filter.getId() != null) {
                builder.append(" and lower(s.shapeId) like lower(:id)");
            }
        }
    }

    /**
     * do result query naplni parametry dle filtru
     * @param query query pro naplneni parametry
     * @param filter filter pro vyber vysledku
     */
    private static void addFilterParamsToQuery(Query query, ShapeFilter filter) {
        if(filter != null) {
            if(filter.getId() != null) {
                query.setParameter("id", filter.getId() + "%");
            }
        }
    }


}
