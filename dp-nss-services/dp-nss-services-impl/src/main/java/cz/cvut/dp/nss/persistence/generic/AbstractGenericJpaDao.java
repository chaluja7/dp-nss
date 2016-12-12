package cz.cvut.dp.nss.persistence.generic;

import cz.cvut.dp.nss.services.common.AbstractEntity;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Abstract JPA generic dao.
 *
 * @author jakubchalupa
 * @since 24.11.14 - 12.12.16
 */
public abstract class AbstractGenericJpaDao<T extends AbstractEntity> implements GenericDao<T> {

    @Autowired
    protected SessionFactory sessionFactory;

    protected final Class<T> type;

    public AbstractGenericJpaDao(Class<T> type) {
        this.type = type;
    }

    @Override
    public void create(T t) {
        sessionFactory.getCurrentSession().persist(t);
    }

    @Override
    public void update(T t) {
        sessionFactory.getCurrentSession().merge(t);
    }

    @Override
    public T find(long id) {
        return sessionFactory.getCurrentSession().get(type, id);
    }

    @Override
    public void delete(long id) {
        sessionFactory.getCurrentSession().delete(find(id));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return sessionFactory.getCurrentSession().createQuery("from " + type.getName()).list();
    }
}
