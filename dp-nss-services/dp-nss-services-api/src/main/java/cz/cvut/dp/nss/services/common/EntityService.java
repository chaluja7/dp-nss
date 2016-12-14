package cz.cvut.dp.nss.services.common;

import java.io.Serializable;
import java.util.List;

/**
 * CRUD interface pro entity.
 *
 * @author jakubchalupa
 * @since 14.12.16
 */
public interface EntityService<ENT extends AbstractEntity<ID>, ID extends Serializable> {

    /**
     * find entity by id
     * @param id id of entity
     * @return entity by id or null
     */
    ENT get(ID id);

    /**
     * update entity
     * @param entity entity to update
     */
    void update(ENT entity);

    /**
     * persist entity
     * @param entity entity to persist
     */
    void create(ENT entity);

    /**
     * delete entity by id
     * @param id id of entity to delete
     */
    void delete(ID id);

    /**
     * find all entities of given type
     * @return all entities of given type
     */
    List<ENT> getAll();

}
