package cz.cvut.dp.nss.persistence.generic;

import cz.cvut.dp.nss.services.common.AbstractEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Common interface for all Dao implementation.
 *
 * @author jakubchalupa
 * @since 24.11.14 - 12.12.16
 */
public interface GenericDao<ENT extends AbstractEntity<ID>, ID extends Serializable> {

    /**
     * persist entity
     * @param entity entity to persist
     */
    void create(ENT entity);

    /**
     * update entity
     * @param entity entity to update
     */
    void update(ENT entity);

    /**
     * find entity by id
     * @param id entity id
     * @return founded entity
     */
    ENT find(ID id);

    /**
     * delete entity
     * @param id entity id (to delete)
     */
    void delete(ID id);

    /**
     * will find all entities of a type
     * @return list of all entities by type
     */
    List<ENT> getAll();

}
