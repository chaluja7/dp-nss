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
     * nazev stanice - v ramci neo4j slouzi pro grupovani stanic k sobe
     * stopId totiz muze byt jine napr. pro Mustek - A a Mustek - B. To by pak znamenalo, ze by nebyl mozny prestup
     * v ramci traverzovani grafem
     */
    @Property
    private String stopName;

    /**
     * cas prijezdu v ramci dne v sekundach
     */
    @Property
    private Integer arrivalInSeconds;

    /**
     * cas odjezdu v ramci dne v sekundach
     */
    @Property
    private Integer departureInSeconds;

    /**
     * poradi uzlu v ramci tripu
     */
    @Property
    private Integer sequence;

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

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public Integer getArrivalInSeconds() {
        return arrivalInSeconds;
    }

    public void setArrivalInSeconds(Integer arrivalInSeconds) {
        this.arrivalInSeconds = arrivalInSeconds;
    }

    public Integer getDepartureInSeconds() {
        return departureInSeconds;
    }

    public void setDepartureInSeconds(Integer departureInSeconds) {
        this.departureInSeconds = departureInSeconds;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
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
