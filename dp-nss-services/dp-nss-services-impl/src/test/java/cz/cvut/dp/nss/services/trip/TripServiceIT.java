package cz.cvut.dp.nss.services.trip;

import cz.cvut.dp.nss.services.AbstractServiceIT;
import cz.cvut.dp.nss.services.agency.Agency;
import cz.cvut.dp.nss.services.agency.AgencyService;
import cz.cvut.dp.nss.services.agency.AgencyServiceIT;
import cz.cvut.dp.nss.services.calendar.Calendar;
import cz.cvut.dp.nss.services.calendar.CalendarService;
import cz.cvut.dp.nss.services.calendar.CalendarServiceIT;
import cz.cvut.dp.nss.services.route.Route;
import cz.cvut.dp.nss.services.route.RouteService;
import cz.cvut.dp.nss.services.route.RouteServiceIT;
import cz.cvut.dp.nss.services.route.RouteType;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * @author jakubchalupa
 * @since 05.01.17
 */
public class TripServiceIT extends AbstractServiceIT {

    @Autowired
    private TripService tripService;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private AgencyService agencyService;

    @Test
    @Transactional
    public void testCRUD() {
        Trip trip = prepareTrip();

        //insert
        tripService.create(trip);

        //retrieve
        Trip retrieved = tripService.get(trip.getId());
        Assert.assertNotNull(retrieved);
        Assert.assertEquals(trip.getId(), retrieved.getId());
        Assert.assertEquals(trip.getHeadSign(), retrieved.getHeadSign());

        //update
        String headSign = "newHeadSign";
        retrieved.setHeadSign(headSign);
        tripService.update(retrieved);

        //check
        retrieved = tripService.get(retrieved.getId());
        Assert.assertNotNull(retrieved);
        Assert.assertEquals(headSign, retrieved.getHeadSign());

        //delete
        tripService.delete(retrieved.getId());

        //check null get
        Assert.assertNull(tripService.get(retrieved.getId()));

        //delete other entities
        routeService.delete(trip.getRoute().getId());
        agencyService.delete(trip.getRoute().getAgency().getId());
        calendarService.delete(trip.getCalendar().getId());
    }

    @Test
    public void testGetAllForInsertToGraph() {
        List<TripWrapper> allForInsertToGraph = tripService.getAllForInsertToGraph(10, 1);
        Assert.assertNotNull(allForInsertToGraph);
    }

    @Test
    public void testGetCountByCalendarId() {
        long countByCalendarId = tripService.getCountByCalendarId("1");
        Assert.assertTrue(countByCalendarId >= 0);
    }

    @Test
    public void testGetByFilter() {
        List<Trip> trips = tripService.getByFilter(getFilter(), 0, 15, "calendarId", true);
        Assert.assertNotNull(trips);
    }

    @Test
    public void testGetCountByFilter() {
        long countByFilter = tripService.getCountByFilter(getFilter());
        Assert.assertTrue(true);
    }

    @Test
    public void testGetLazyInitialized() {
        Trip trip = prepareTrip();
        tripService.create(trip);

        Trip retrieved = tripService.getLazyInitialized(trip.getId());
        Assert.assertNotNull(retrieved);
    }

    public static Trip getTrip(final String id, Calendar calendar, Route route, String headSign) {
        Trip trip = new Trip();
        trip.setId(id);
        trip.setCalendar(calendar);
        trip.setRoute(route);
        trip.setHeadSign(headSign);

        return trip;
    }

    public static TripFilter getFilter() {
        TripFilter filter = new TripFilter();
        filter.setRouteShortName("124");

        return filter;
    }

    private Trip prepareTrip() {
        Calendar calendar = CalendarServiceIT.getCalendar("calendar" + System.currentTimeMillis(), LocalDate.now(), LocalDate.now(), true, true);
        calendarService.create(calendar);

        Agency agency = AgencyServiceIT.getAgency("agency" + System.currentTimeMillis(), "agencyZ", "", "");
        agencyService.create(agency);

        Route route = RouteServiceIT.getRoute("route" + System.currentTimeMillis(), agency, "sh", "lon", "col", RouteType.BOAT);
        routeService.create(route);

        final String id = "stop" + System.currentTimeMillis();
        String headSign = "headSign";

        return getTrip(id, calendar, route, headSign);
    }

}
