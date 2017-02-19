package cz.cvut.dp.nss.graph.services.common;


import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jakubchalupa
 * @since 14.12.16
 */
public abstract class AbstractNodeService<NODE extends AbstractNode, REPO extends GraphRepository<NODE>> implements NodeService<NODE> {

    /**
     * dao konkretni implementace, musi byt injectovane v konstruktoru potomka
     */
    protected REPO repo;

    /**
     * @param repo konkretni dao implementace z potomka
     */
    public AbstractNodeService(REPO repo) {
        this.repo = repo;
    }

    @Override
    @Transactional("neo4jTransactionManager")
    public NODE save(NODE node) {
        //defaultne chci ukladat maximalne jednoho souseda/hranu
        return repo.save(node, 1);
    }

    @Override
    @Transactional("neo4jTransactionManager")
    public NODE save(NODE node, int depth) {
        return repo.save(node, depth);
    }

    @Override
    @Transactional("neo4jTransactionManager")
    public void deleteAll() {
        repo.deleteAll();
    }
}
