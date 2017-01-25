package cz.cvut.dp.nss.persistence.generic;

import cz.cvut.dp.nss.services.common.AbstractEntity;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

/**
 * Abstract JPA generic dao.
 * Implementace by mely spravne byt oanotovany @Repository, ale ta anotace se pak mele s neo4j repositories
 * Dao jsou tedy oanotovany pomoci @Component (spring v tom v tomto pripade nedela rozdil)
 *
 * @author jakubchalupa
 * @since 24.11.14 - 12.12.16
 */
public abstract class AbstractGenericJpaDao<ENT extends AbstractEntity<ID>, ID extends Serializable> implements GenericDao<ENT, ID> {

    @Autowired
    protected SessionFactory sessionFactory;

    protected final Class<ENT> type;

    public AbstractGenericJpaDao(Class<ENT> type) {
        this.type = type;
    }

    @Override
    public void create(ENT entity) {
        sessionFactory.getCurrentSession().persist(entity);
    }

    @Override
    public void update(ENT entity) {
        sessionFactory.getCurrentSession().merge(entity);
    }

    @Override
    public ENT find(ID id) {
        return sessionFactory.getCurrentSession().get(type, id);
    }

    @Override
    public void delete(ID id) {
        sessionFactory.getCurrentSession().delete(find(id));
    }

    @Override
    public List<ENT> getAll() {
        return sessionFactory.getCurrentSession().createQuery("from " + type.getName(), type).list();
    }
}
