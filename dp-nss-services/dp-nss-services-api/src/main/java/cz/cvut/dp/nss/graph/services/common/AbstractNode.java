package cz.cvut.dp.nss.graph.services.common;


import org.neo4j.ogm.annotation.GraphId;

/**
 * indexy uz nejsou podporovane z anotaci entit - je treba je vyrobit na neo4j
 * http://neo4j.com/docs/developer-manual/current/cypher/schema/index/
 * Dotazy, ktere je nad databazi Neo4j treba provest jsou na jednotlivych entitach, kterych se tykaji.
 *
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof AbstractNode) {
            AbstractNode other = (AbstractNode) obj;
            return this.getId() != null && other.getId() != null && this.getId().equals(other.getId());
        }

        return false;
    }

    @Override
    public int hashCode() {
        //TODO opravdu zde chci zakomponovat ID?
        //viz http://docs.spring.io/spring-data/neo4j/docs/current/reference/html/#_entity_equality
        int hash = 3;
        return 37 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
    }

}
