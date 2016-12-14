package cz.cvut.dp.nss.services.common;

import cz.cvut.dp.nss.persistence.generic.GenericDao;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * @author jakubchalupa
 * @since 14.12.16
 */
public abstract class AbstractEntityService<ENT extends AbstractEntity<ID>, ID extends Serializable, DAO extends GenericDao<ENT, ID>> implements EntityService<ENT, ID> {

    /**
     * dao konkretni implementace, musi byt injectovane v konstruktoru potomka
     */
    protected DAO dao;

    /**
     * @param dao konkretni dao implementace z potomka
     */
    public AbstractEntityService(DAO dao) {
        this.dao = dao;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ENT get(ID id) {
        return dao.find(id);
    }

    @Override
    @Transactional
    public void update(ENT entity) {
        dao.update(entity);
    }

    @Override
    @Transactional
    public void create(ENT entity) {
        dao.create(entity);
    }

    @Override
    @Transactional
    public void delete(ID id) {
        dao.delete(id);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<ENT> getAll() {
        return dao.getAll();
    }

}
