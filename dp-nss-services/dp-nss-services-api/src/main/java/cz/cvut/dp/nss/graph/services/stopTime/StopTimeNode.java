package cz.cvut.dp.nss.graph.services.stopTime;

import cz.cvut.dp.nss.graph.services.common.AbstractNode;
import cz.cvut.dp.nss.graph.services.trip.TripNode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

/**
 * @author jakubchalupa
 * @since 18.01.17
 */
@NodeEntity
public class StopTimeNode extends AbstractNode {

    /**
     * id stopTime
     */
    @Property
    private Long stopTimeId;

    /**
     * id stanice
     */
    @Property
    private String stopId;

    /**
     * cas prijezdu v ramci dne v millis
     */
    @Property
    private Long arrivalInMillis;

    /**
     * cas odjezdu v ramci dne v millis
     */
    @Property
    private Long departureInMillis;

    @Relationship(type = "IN_TRIP", direction = Relationship.OUTGOING)
    private TripNode tripNode;

    public Long getStopTimeId() {
        return stopTimeId;
    }

    public void setStopTimeId(Long stopTimeId) {
        this.stopTimeId = stopTimeId;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public Long getArrivalInMillis() {
        return arrivalInMillis;
    }

    public void setArrivalInMillis(Long arrivalInMillis) {
        this.arrivalInMillis = arrivalInMillis;
    }

    public Long getDepartureInMillis() {
        return departureInMillis;
    }

    public void setDepartureInMillis(Long departureInMillis) {
        this.departureInMillis = departureInMillis;
    }

    public TripNode getTripNode() {
        return tripNode;
    }

    public void setTripNode(TripNode tripNode) {
        this.tripNode = tripNode;
    }
}
