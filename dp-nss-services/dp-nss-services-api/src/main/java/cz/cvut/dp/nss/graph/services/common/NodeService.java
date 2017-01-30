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
     * ulozi NODE vcetne navazanych do uvedene hloubky
     * @param node NODE k ulozeni
     * @param depth max hloubka ulozeni (atributu)
     * @return ulozeny NODE
     */
    NODE save(NODE node, int depth);

    /**
     * odstrani vsechny uzly daneho typu
     */
    void deleteAll();

}
