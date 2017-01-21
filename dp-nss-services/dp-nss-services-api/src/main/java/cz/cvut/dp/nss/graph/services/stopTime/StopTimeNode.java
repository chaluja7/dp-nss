package cz.cvut.dp.nss.graph.services.stopTime;

import cz.cvut.dp.nss.graph.services.common.AbstractNode;
import cz.cvut.dp.nss.graph.services.trip.TripNode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

/**
 * CREATE CONSTRAINT ON (n:StopTimeNode) ASSERT n.stopTimeId IS UNIQUE
 *
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

    /**
     * Trip tohoto zastaveni
     */
    @Relationship(type = "IN_TRIP", direction = Relationship.OUTGOING)
    private TripNode tripNode;

    /**
     * pristi zastaveni v ramci tripu
     */
    @Relationship(type = "NEXT_STOP", direction = Relationship.OUTGOING)
    private StopTimeNode nextStop;

    /**
     * pristi prestupni zastaveni z jineho tripu v ramci stanice
     */
    @Relationship(type = "NEXT_AWAITING_STOP", direction = Relationship.OUTGOING)
    private StopTimeNode nextAwaitingStop;

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

    public StopTimeNode getNextStop() {
        return nextStop;
    }

    public void setNextStop(StopTimeNode nextStop) {
        this.nextStop = nextStop;
    }

    public StopTimeNode getNextAwaitingStop() {
        return nextAwaitingStop;
    }

    public void setNextAwaitingStop(StopTimeNode nextAwaitingStop) {
        this.nextAwaitingStop = nextAwaitingStop;
    }

}
