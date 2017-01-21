package cz.cvut.dp.nss.graph.services;

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
public class TripNodeServiceIT extends AbstractServiceIT {

    @Autowired
    private TripNodeService tripNodeService;

    public static final String TRIP_ID = "tripX";

    @Before
    public void before() {
        tripNodeService.deleteAll();
    }

    @Test
    public void testCreateAndGet() {
        final String tripId = "tripX";
        TripNode tripNode = getTripNode(tripId, "calendarX");

        tripNode = tripNodeService.save(tripNode);
        Assert.assertNotNull(tripNode.getId());

        tripNode = tripNodeService.findByTripId(tripId);
        Assert.assertNotNull(tripNode);
    }

    public static TripNode getTripNode(String tripId, String calendarId) {
        TripNode tripNode = new TripNode();
        tripNode.setTripId(tripId);
        tripNode.setCalendarId(calendarId);

        return tripNode;
    }

}
