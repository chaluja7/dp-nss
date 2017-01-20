package cz.cvut.dp.nss.graph.services.common;


import org.neo4j.ogm.annotation.GraphId;

/**
 * @author jakubchalupa
 * @since 18.01.17
 */
public abstract class AbstractNode {

    @GraphId
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
