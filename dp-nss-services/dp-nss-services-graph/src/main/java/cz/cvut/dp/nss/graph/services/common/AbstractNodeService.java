package cz.cvut.dp.nss.graph.services.common;


import org.springframework.data.neo4j.repository.GraphRepository;

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
    //@Neo4jTransactional
    public NODE save(NODE rideNode) {
        return repo.save(rideNode);
    }

    @Override
    public NODE findById(Long id) {
        return repo.findOne(id);
    }

}
