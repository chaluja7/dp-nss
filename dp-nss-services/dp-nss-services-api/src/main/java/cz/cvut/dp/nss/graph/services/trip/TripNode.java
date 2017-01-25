package cz.cvut.dp.nss.graph.services.trip;

import cz.cvut.dp.nss.graph.services.common.AbstractNode;
import cz.cvut.dp.nss.graph.services.stopTime.StopTimeNode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

/**
 * CREATE CONSTRAINT ON (n:TripNode) ASSERT n.tripId IS UNIQUE
 *
 *
 * @author jakubchalupa
 * @since 18.01.17
 */
@NodeEntity
public class TripNode extends AbstractNode {

    /**
     * id tripu
     */
    @Property
    private String tripId;

    /**
     * service_id (id intervalu platnosti)
     */
    @Property
    private String calendarId;

    /**
     * navazana zastaveni
     */
    @Relationship(type = "IN_TRIP", direction = Relationship.INCOMING)
    private List<StopTimeNode> stopTimeNodes;

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public List<StopTimeNode> getStopTimeNodes() {
        return stopTimeNodes;
    }

    public void setStopTimeNodes(List<StopTimeNode> stopTimeNodes) {
        this.stopTimeNodes = stopTimeNodes;
    }
}
