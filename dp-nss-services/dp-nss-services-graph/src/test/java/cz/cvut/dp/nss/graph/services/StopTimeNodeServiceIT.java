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
        StopTimeNode stopTimeNode = getStopTimeNode(stopTimeId, stopId, 0L, 0L, null, null);

        stopTimeNode = stopTimeNodeService.save(stopTimeNode);
        Assert.assertNotNull(stopTimeNode.getId());

        stopTimeNode = stopTimeNodeService.findByStopTimeId(stopTimeId);
        Assert.assertNotNull(stopTimeNode);
    }

    @Test
    public void testGraph() {
        final String tripId = "tripY";
        TripNode tripNode = TripNodeServiceIT.getTripNode(tripId, "calendar1");
        tripNodeService.save(tripNode);

        final Long middleStopTimeId = 3L;
        //a zkusim vytvorit strukturu s par hranamy
        StopTimeNode stopTimeNode1 = getStopTimeNode(2L, "X", 1L, 2L, tripNode, null);
        stopTimeNodeService.save(stopTimeNode1);

        StopTimeNode stopTimeNode2 = getStopTimeNode(middleStopTimeId, "Y", 3L, 4L, tripNode, stopTimeNode1);
        stopTimeNodeService.save(stopTimeNode2);

        StopTimeNode stopTimeNode3 = getStopTimeNode(4L, "Z", 4L, 5L, tripNode, stopTimeNode2);
        stopTimeNodeService.save(stopTimeNode3);

        //a test get
        StopTimeNode retrieved = stopTimeNodeService.findByStopTimeId(3L);
        Assert.assertNotNull(retrieved);
//        Assert.assertNotNull(retrieved.getTripNode());
//        Assert.assertNotNull(retrieved.getPrevStop());
//        Assert.assertNotNull(retrieved.getNextStop());
    }

    public static StopTimeNode getStopTimeNode(Long stopTimeId, String stopId, Long arrivalInMillis, Long departureInMillis, TripNode tripNode, StopTimeNode prevStop) {
        StopTimeNode stopTimeNode = new StopTimeNode();
        stopTimeNode.setStopTimeId(stopTimeId);
        stopTimeNode.setStopId(stopId);
        stopTimeNode.setArrivalInMillis(arrivalInMillis);
        stopTimeNode.setDepartureInMillis(departureInMillis);
        stopTimeNode.setTripNode(tripNode);
        stopTimeNode.setPrevStop(prevStop);

        return stopTimeNode;
    }

}
