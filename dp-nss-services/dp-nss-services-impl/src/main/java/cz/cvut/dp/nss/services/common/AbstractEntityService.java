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

    protected static final int OFFSET_DEFAULT = 0;

    protected static final int LIMIT_DEFAULT = 20;

    protected static final int LIMIT_MAX = 100;

    /**
     * @param dao konkretni dao implementace z potomka
     */
    public AbstractEntityService(DAO dao) {
        this.dao = dao;
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public ENT get(ID id) {
        return id != null ? dao.find(id) : null;
    }

    @Override
    @Transactional("transactionManager")
    public void update(ENT entity) {
        dao.update(entity);
    }

    @Override
    @Transactional("transactionManager")
    public void create(ENT entity) {
        dao.create(entity);
    }

    @Override
    @Transactional("transactionManager")
    public void delete(ID id) {
        dao.delete(id);
    }

    @Override
    @Transactional(value = "transactionManager", propagation = Propagation.SUPPORTS, readOnly = true)
    public List<ENT> getAll() {
        return dao.getAll();
    }

    protected int getOffsetOrDefault(Integer offset) {
        return offset != null ? offset : OFFSET_DEFAULT;
    }

    protected int getLimitOrDefault(Integer limit) {
        return limit == null ? LIMIT_DEFAULT : limit > LIMIT_MAX ? LIMIT_MAX : limit;
    }

}
