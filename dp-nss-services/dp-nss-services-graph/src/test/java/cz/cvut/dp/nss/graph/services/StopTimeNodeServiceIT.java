package cz.cvut.dp.nss.graph.services;

import cz.cvut.dp.nss.graph.services.stopTime.StopTimeNode;
import cz.cvut.dp.nss.graph.services.stopTime.StopTimeNodeService;
import cz.cvut.dp.nss.graph.services.trip.TripNode;
import cz.cvut.dp.nss.graph.services.trip.TripNodeService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author jakubchalupa
 * @since 20.01.17
 */
public class StopTimeNodeServiceIT extends AbstractServiceIT {

    @Autowired
    private StopTimeNodeService stopTimeNodeService;

    @Autowired
    private TripNodeService tripNodeService;

    @Before
    public void before() {
        tripNodeService.deleteAll();
        stopTimeNodeService.deleteAll();
    }

    @Test
    public void testCreateAndGet() {
        final Long stopTimeId = 1L;
        final String stopId = "stopX";
        StopTimeNode stopTimeNode = getStopTimeNode(stopTimeId, stopId, "name",0, 0, null, null);

        stopTimeNode = stopTimeNodeService.save(stopTimeNode);
        Assert.assertNotNull(stopTimeNode.getId());

        stopTimeNode = stopTimeNodeService.findByStopTimeId(stopTimeId);
        Assert.assertNotNull(stopTimeNode);
    }

    @Test
    public void testGraph() {
        final String stop1 = "X";
        final String stop2 = "Y";
        final String stop3 = "Z";
        final String stopName1 = "name";
        final String stopName2 = "name2";
        final String stopName3 = "name3";

        TripNode tripNode1 = TripNodeServiceIT.getTripNode("tripA", "calendar1");
        tripNodeService.save(tripNode1);

        //a zkusim vytvorit strukturu s par hranamy - 1. trip
        StopTimeNode stopTimeNode3 = getStopTimeNode(4L, stop3, stopName3, 5, 6, tripNode1, null);
        stopTimeNodeService.save(stopTimeNode3);

        StopTimeNode stopTimeNode2 = getStopTimeNode(3L, stop2, stopName2, 3, 4, tripNode1, stopTimeNode3);
        stopTimeNodeService.save(stopTimeNode2);

        StopTimeNode stopTimeNode1 = getStopTimeNode(2L, stop1, stopName1, 1, 2, tripNode1, stopTimeNode2);
        stopTimeNodeService.save(stopTimeNode1);

        //a test get
        StopTimeNode retrieved = stopTimeNodeService.findByStopTimeId(3L);
        Assert.assertNotNull(retrieved);
        Assert.assertNotNull(retrieved.getTripNode());
        Assert.assertNotNull(retrieved.getNextStop());


        //2. trip
        TripNode tripNode2 = TripNodeServiceIT.getTripNode("tripB", "calendar1");
        tripNodeService.save(tripNode2);

        StopTimeNode stopTimeNode6 = getStopTimeNode(7L, stop3, stopName3, 11, 12, tripNode2, null);
        stopTimeNodeService.save(stopTimeNode6);

        StopTimeNode stopTimeNode5 = getStopTimeNode(6L, stop2, stopName2, 9, 10, tripNode2, stopTimeNode6);
        stopTimeNodeService.save(stopTimeNode5);

        StopTimeNode stopTimeNode4 = getStopTimeNode(5L, stop1, stopName1,7, 8, tripNode2, stopTimeNode5);
        stopTimeNodeService.save(stopTimeNode4);

        //a test get
        retrieved = stopTimeNodeService.findByStopTimeId(6L);
        Assert.assertNotNull(retrieved);
        Assert.assertNotNull(retrieved.getTripNode());
        Assert.assertNotNull(retrieved.getNextStop());

        //3. trip
        TripNode tripNode3 = TripNodeServiceIT.getTripNode("tripC", "calendar2");
        tripNodeService.save(tripNode3);

        StopTimeNode stopTimeNode9 = getStopTimeNode(10L, stop3, stopName3,17, 18, tripNode3, null);
        stopTimeNodeService.save(stopTimeNode9);

        StopTimeNode stopTimeNode8 = getStopTimeNode(9L, stop2, stopName2,15, 16, tripNode3, stopTimeNode9);
        stopTimeNodeService.save(stopTimeNode8);

        StopTimeNode stopTimeNode7 = getStopTimeNode(8L, stop1, stopName1,13, 14, tripNode3, stopTimeNode8);
        stopTimeNodeService.save(stopTimeNode7);

        //a test get
        retrieved = stopTimeNodeService.findByStopTimeId(9L);
        Assert.assertNotNull(retrieved);
        Assert.assertNotNull(retrieved.getTripNode());
        Assert.assertNotNull(retrieved.getNextStop());

        //a jeste propojime uzly na jedne stanici
        stopTimeNodeService.connectStopTimeNodesOnStopWithWaitingRelationship(stopName1);
        stopTimeNodeService.connectStopTimeNodesOnStopWithWaitingRelationship(stopName2);
        stopTimeNodeService.connectStopTimeNodesOnStopWithWaitingRelationship(stopName3);

        //a test get
        retrieved = stopTimeNodeService.findByStopTimeId(3L);
        Assert.assertNotNull(retrieved);
        Assert.assertNotNull(retrieved.getTripNode());
        Assert.assertNotNull(retrieved.getNextStop());
        Assert.assertNotNull(retrieved.getNextAwaitingStop());
    }

    public static StopTimeNode getStopTimeNode(Long stopTimeId, String stopId, String stopName, int arrivalInSeconds, int departureInSeconds, TripNode tripNode, StopTimeNode nextStop) {
        StopTimeNode stopTimeNode = new StopTimeNode();
        stopTimeNode.setStopTimeId(stopTimeId);
        stopTimeNode.setStopId(stopId);
        stopTimeNode.setStopName(stopName);
        stopTimeNode.setArrivalInSeconds(arrivalInSeconds);
        stopTimeNode.setDepartureInSeconds(departureInSeconds);
        stopTimeNode.setTripNode(tripNode);
        stopTimeNode.setNextStop(nextStop);

        return stopTimeNode;
    }

}
