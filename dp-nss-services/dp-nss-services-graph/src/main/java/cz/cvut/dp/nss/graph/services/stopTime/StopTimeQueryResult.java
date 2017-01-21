package cz.cvut.dp.nss.graph.services.stopTime;

import cz.cvut.dp.nss.graph.services.trip.TripNode;
import org.springframework.data.neo4j.annotation.QueryResult;

/**
 * Slouzi jako hack pro vytazeni nodu s libovolnymi entitamy dostupnymi pres hrany
 * Query totiz v pouzite verzi SDN (4.1.6) nedotahuje automaticky relationships z anotaci entit
 * viz http://stackoverflow.com/questions/34657610/custom-query-in-spring-data-neo4j-not-retrieving-relationships
 *
 * Navic i kdyz bych rad pouzil optional match, tak ten v ramci query funguje jen na provazani entit jineho typu
 * kdyz mam tedy vazbu na stejnou entitu, tak to proste neumi
 *
 * Z @Query tedy vytahnu tento QueryResult, kam si dotlacim co chci, a pak ho ztranformuji do Entity
 *
 * @author jakubchalupa
 * @since 21.01.17
 */
@QueryResult
public class StopTimeQueryResult {

    private StopTimeNode stopTimeNode;

    private TripNode tripNode;

    private StopTimeNode nextStop;

    private StopTimeNode nextAwaitingStop;

    public StopTimeNode getStopTimeNode() {
        return stopTimeNode;
    }

    public void setStopTimeNode(StopTimeNode stopTimeNode) {
        this.stopTimeNode = stopTimeNode;
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

    /**
     * @return vytvoreny stopTimeNode tak, jak by se mel vrace naprimo z @Query, kdyby SDN nefungovalo uplne na @#$%#@
     */
    public StopTimeNode createStopTimeNode() {
        stopTimeNode.setTripNode(getTripNode());
        stopTimeNode.setNextStop(getNextStop());
        stopTimeNode.setNextAwaitingStop(getNextAwaitingStop());

        return stopTimeNode;
    }
}
