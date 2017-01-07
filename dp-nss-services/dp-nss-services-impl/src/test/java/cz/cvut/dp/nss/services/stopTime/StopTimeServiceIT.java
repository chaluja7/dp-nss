package cz.cvut.dp.nss.services.stopTime;

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
import cz.cvut.dp.nss.services.stop.Stop;
import cz.cvut.dp.nss.services.stop.StopService;
import cz.cvut.dp.nss.services.stop.StopServiceIT;
import cz.cvut.dp.nss.services.trip.Trip;
import cz.cvut.dp.nss.services.trip.TripService;
import cz.cvut.dp.nss.services.trip.TripServiceIT;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author jakubchalupa
 * @since 05.01.17
 */
public class StopTimeServiceIT extends AbstractServiceIT {

    @Autowired
    private TripService tripService;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private AgencyService agencyService;

    @Autowired
    private StopService stopService;

    @Autowired
    private StopTimeService stopTimeService;

    @Test
    public void testCRUD() {
        Calendar calendar = CalendarServiceIT.getCalendar("calendar" + System.currentTimeMillis(), LocalDate.now(), LocalDate.now(), true, true);
        calendarService.create(calendar);

        Agency agency = AgencyServiceIT.getAgency("agency" + System.currentTimeMillis(), "agencyW", "", "");
        agencyService.create(agency);

        Route route = RouteServiceIT.getRoute("route" + System.currentTimeMillis(), agency, "sh", "lon", "col", RouteType.BOAT);
        routeService.create(route);

        final String id = "stop" + System.currentTimeMillis();
        String headSign = "headSign";

        Trip trip = TripServiceIT.getTrip(id, calendar, route, headSign);
        tripService.create(trip);

        Stop stop = StopServiceIT.getStop("stop" + System.currentTimeMillis(), "stopY", 50.0, 59.0);
        stopService.create(stop);

        LocalTime departure = LocalTime.now();
        LocalTime arrival = LocalTime.now().minusHours(1L);
        Integer sequence = 1;

        StopTime stopTime = getStopTime(stop, trip, arrival, departure, sequence);

        //insert
        stopTimeService.create(stopTime);

        //retrieve
        StopTime retrieved = stopTimeService.get(stopTime.getId());
        Assert.assertNotNull(retrieved);
        Assert.assertEquals(arrival.toSecondOfDay(), retrieved.getArrival().toSecondOfDay());
        Assert.assertEquals(departure.toSecondOfDay(), retrieved.getDeparture().toSecondOfDay());
        Assert.assertEquals(sequence, retrieved.getSequence());

        //update
        sequence = 2;
        retrieved.setSequence(sequence);
        stopTimeService.update(retrieved);

        //check
        retrieved = stopTimeService.get(retrieved.getId());
        Assert.assertNotNull(retrieved);
        Assert.assertEquals(sequence, retrieved.getSequence());

        //delete
        stopTimeService.delete(retrieved.getId());

        //check null get
        Assert.assertNull(stopTimeService.get(retrieved.getId()));

        //delete other entities
        stopService.delete(stop.getId());
        tripService.delete(trip.getId());
        routeService.delete(route.getId());
        agencyService.delete(agency.getId());
        calendarService.delete(calendar.getId());
    }

    public static StopTime getStopTime(Stop stop, Trip trip, LocalTime arrival, LocalTime departure, Integer sequence) {
        StopTime stopTime = new StopTime();
        stopTime.setStop(stop);
        stopTime.setTrip(trip);
        stopTime.setArrival(arrival);
        stopTime.setDeparture(departure);
        stopTime.setSequence(sequence);

        return stopTime;
    }

}
