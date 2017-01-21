package cz.cvut.dp.nss.graph.services.common;

/**
 * CRUD interface pro entity.
 *
 * @author jakubchalupa
 * @since 14.12.16
 */
public interface NodeService<NODE extends AbstractNode> {

    /**
     * ulozi NODE
     * @param node NODE k ulozeni
     * @return ulozeny NODE
     */
    NODE save(NODE node);

    /**
     * odstrani vsechny uzly daneho typu
     */
    void deleteAll();

}
