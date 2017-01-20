package cz.cvut.dp.nss.graph.services;

import cz.cvut.dp.nss.graph.services.trip.TripNode;
import cz.cvut.dp.nss.graph.services.trip.TripNodeService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author jakubchalupa
 * @since 20.01.17
 */
public class TripNodeServiceIT extends AbstractServiceIT {

    @Autowired
    private TripNodeService tripNodeService;

    @Test
    public void testCreateAndGet() {
        TripNode tripNode = getTripNode("trip1", "calendar1");

        tripNode = tripNodeService.save(tripNode);
        Assert.assertNotNull(tripNode.getId());

        //TODO nefunguje
//        tripNode = tripNodeService.findById(tripNode.getId());
//        Assert.assertNotNull(tripNode);
    }

    public static TripNode getTripNode(String tripId, String calendarId) {
        TripNode tripNode = new TripNode();
        tripNode.setTripId(tripId);
        tripNode.setCalendarId(calendarId);

        return tripNode;
    }

}
