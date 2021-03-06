package cz.cvut.dp.nss.controller.admin.trip;

import cz.cvut.dp.nss.controller.admin.AdminAbstractController;
import cz.cvut.dp.nss.controller.admin.calendar.AdminCalendarController;
import cz.cvut.dp.nss.controller.admin.route.AdminRouteController;
import cz.cvut.dp.nss.controller.admin.shape.AdminShapeController;
import cz.cvut.dp.nss.controller.admin.stop.AdminStopController;
import cz.cvut.dp.nss.controller.admin.wrapper.OrderWrapper;
import cz.cvut.dp.nss.exception.BadRequestException;
import cz.cvut.dp.nss.exception.ResourceNotFoundException;
import cz.cvut.dp.nss.services.calendar.Calendar;
import cz.cvut.dp.nss.services.calendar.CalendarService;
import cz.cvut.dp.nss.services.common.DateTimeUtils;
import cz.cvut.dp.nss.services.common.EnumUtils;
import cz.cvut.dp.nss.services.route.Route;
import cz.cvut.dp.nss.services.route.RouteService;
import cz.cvut.dp.nss.services.shape.Shape;
import cz.cvut.dp.nss.services.shape.ShapeFilter;
import cz.cvut.dp.nss.services.shape.ShapeService;
import cz.cvut.dp.nss.services.stop.Stop;
import cz.cvut.dp.nss.services.stop.StopService;
import cz.cvut.dp.nss.services.stopTime.StopTime;
import cz.cvut.dp.nss.services.trip.Trip;
import cz.cvut.dp.nss.services.trip.TripFilter;
import cz.cvut.dp.nss.services.trip.TripService;
import cz.cvut.dp.nss.services.trip.TripWheelchairAccessibleType;
import cz.cvut.dp.nss.wrapper.output.stopTime.StopTimeWrapper;
import cz.cvut.dp.nss.wrapper.output.trip.TripWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller obsluhujici jizdy.
 *
 * @author jakubchalupa
 * @since 16.03.17
 */
@RestController
@RequestMapping(value = "/admin/trip")
public class AdminTripController extends AdminAbstractController {

    @Autowired
    private TripService tripService;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private ShapeService shapeService;

    @Autowired
    private StopService stopService;

    private static final String FILTER_HEAD_SIGN = "headSign";
    private static final String FILTER_SHAPE_ID = "shapeId";
    private static final String FILTER_CALENDAR_ID = "calendarId";
    private static final String FILTER_WHEEL_CHAIR = "wheelChairCode";
    private static final String FILTER_ROUTE_SHORT_NAME = "routeShortName";

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<TripWrapper>> getTrips(@RequestHeader(value = X_LIMIT_HEADER, required = false) Integer xLimit,
                                                              @RequestHeader(value = X_OFFSET_HEADER, required = false) Integer xOffset,
                                                              @RequestHeader(value = X_ORDER_HEADER, required = false) String xOrder,
                                                              @RequestParam(value = FILTER_ID, required = false) String id,
                                                              @RequestParam(value = FILTER_HEAD_SIGN, required = false) String headSign,
                                                              @RequestParam(value = FILTER_SHAPE_ID, required = false) String shapeId,
                                                              @RequestParam(value = FILTER_CALENDAR_ID, required = false) String calendarId,
                                                              @RequestParam(value = FILTER_WHEEL_CHAIR, required = false) Integer wheelChairCode,
                                                              @RequestParam(value = FILTER_ROUTE_SHORT_NAME, required = false) String routeShortName) throws BadRequestException {


        List<TripWrapper> wrappers = new ArrayList<>();
        HttpHeaders httpHeaders = new HttpHeaders();

        final OrderWrapper order = getOrderFromHeader(xOrder);
        final TripFilter filter = getFilterFromParams(id, calendarId, shapeId, headSign, routeShortName, wheelChairCode);
        List<Trip> trips = tripService.getByFilter(filter, xOffset, xLimit, order.getOrderColumn(), order.isAsc());
        httpHeaders.add(X_COUNT_HEADER, tripService.getCountByFilter(filter) + "");

        for(Trip trip : trips) {
            wrappers.add(getTripWrapper(trip, false, false));
        }

        return new ResponseEntity<>(wrappers, httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public TripWrapper getTrip(@PathVariable("id") String id) {
        Trip trip = tripService.getLazyInitialized(id);
        if(trip == null) throw new ResourceNotFoundException();

        TripWrapper wrapper = getTripWrapper(trip, true, false);
        wrapper.setCanBeDeleted(true);
        return wrapper;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<TripWrapper> createTrip(@RequestBody TripWrapper wrapper) throws BadRequestException {
        Trip trip = getTrip(wrapper);
        tripService.create(trip);

        return getResponseCreated(getTripWrapper(tripService.getLazyInitialized(trip.getId()), true, false));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public TripWrapper updateTrip(@PathVariable("id") String id, @RequestBody TripWrapper wrapper) throws ResourceNotFoundException, BadRequestException {
        Trip existingTrip = tripService.get(id);
        if(existingTrip == null) throw new ResourceNotFoundException();

        Trip trip = getTrip(wrapper);
        trip.setId(id);
        tripService.update(trip);

        return getTripWrapper(trip, true, false);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteTrip(@PathVariable("id") String id) {
        Trip trip = tripService.get(id);
        if(trip == null) {
            //ok, jiz neni v DB
            return;
        }

        tripService.delete(trip.getId());
    }

    /**
     * @param trip entita jizdy
     * @param withStopTimes pokud true, tak se budou plnit i zastaveni
     * @param withAgency pokud true, tak se bude plnit i dopravce
     * @return wrapper jizdy
     */
    private static TripWrapper getTripWrapper(Trip trip, boolean withStopTimes, boolean withAgency) {
        return getTripWrapper(trip, withStopTimes, withAgency, null);
    }

    /**
     * @param trip entita jizdy
     * @param withStopTimes pokud true, tak se budou plnit i zastaveni
     * @param withAgency pokud true, tak se bude plnit i dopravce
     * @param shapes list prujezdnich bodu
     * @return wrapper jizdy
     */
    public static TripWrapper getTripWrapper(Trip trip, boolean withStopTimes, boolean withAgency, List<Shape> shapes) {
        if(trip == null) return null;

        TripWrapper wrapper = new TripWrapper();
        wrapper.setId(trip.getId());
        wrapper.setHeadSign(trip.getHeadSign());
        if(trip.getRoute() != null) {
            wrapper.setRouteId(trip.getRoute().getId());
            wrapper.setRouteShortName(trip.getRoute().getShortName());
            wrapper.setRoute(AdminRouteController.getRouteWrapper(trip.getRoute(), withAgency));
        }
        if(trip.getTripWheelchairAccessibleType() != null) wrapper.setWheelChairCode(trip.getTripWheelchairAccessibleType().getCode());
        wrapper.setShapeId(trip.getShapeId());
        if(trip.getCalendar() != null) {
            wrapper.setCalendarId(trip.getCalendar().getId());
            wrapper.setCalendar(AdminCalendarController.getCalendarWrapper(trip.getCalendar()));
        }

        if(withStopTimes && trip.getStopTimes() != null) {
            List<StopTimeWrapper> stopTimeWrappers = new ArrayList<>();
            for(StopTime stopTime : trip.getStopTimes()) {
                stopTimeWrappers.add(getStopTimeWrapper(stopTime));
            }
            wrapper.setStopTimes(stopTimeWrappers);
        }

        if(shapes != null && !shapes.isEmpty()) {
            wrapper.setShapes(new ArrayList<>());
            for(Shape shape : shapes) {
                wrapper.getShapes().add(AdminShapeController.getShapeWrapper(shape));
            }
        }

        return wrapper;
    }

    /**
     * @param stopTime stop time entita
     * @return stop time wrapper
     */
    private static StopTimeWrapper getStopTimeWrapper(StopTime stopTime) {
        if(stopTime == null) return null;

        StopTimeWrapper wrapper = new StopTimeWrapper();
        wrapper.setId(stopTime.getId());
        wrapper.setSequence(stopTime.getSequence());
        if(stopTime.getStop() != null) {
            wrapper.setStopId(stopTime.getStop().getId());
            wrapper.setStop(AdminStopController.getStopWrapper(stopTime.getStop()));
        }
        //schvalne zde neni trip, protoze stopTimes vzdy vybirame pouze v ramci tripu, takze jsou na nej navazane

        if(stopTime.getArrival() != null) {
            wrapper.setArrival(stopTime.getArrival().format(DateTimeUtils.GTFS_TIME_PATTERN_DATE_TIME_FORMATTER));
        }

        if(stopTime.getDeparture() != null) {
            wrapper.setDeparture(stopTime.getDeparture().format(DateTimeUtils.GTFS_TIME_PATTERN_DATE_TIME_FORMATTER));
        }

        return wrapper;
    }

    /**
     * @param wrapper trip wrapper
     * @return trip entita
     * @throws BadRequestException pri spatnych parametrech tripWrapper
     */
    private Trip getTrip(TripWrapper wrapper) throws BadRequestException {
        if(wrapper == null) return null;

        Trip trip = new Trip();
        trip.setId(wrapper.getId());
        trip.setHeadSign(wrapper.getHeadSign());
        if(wrapper.getWheelChairCode() != null )trip.setTripWheelchairAccessibleType(EnumUtils.fromCode(wrapper.getWheelChairCode(), TripWheelchairAccessibleType.class));

        if(wrapper.getCalendarId() != null) {
            Calendar calendar = calendarService.get(wrapper.getCalendarId());
            if(calendar == null) {
                throw new BadRequestException("unknown calendar - " + wrapper.getCalendarId());
            }
            trip.setCalendar(calendar);
        }

        if(wrapper.getRouteId() != null) {
            Route route = routeService.get(wrapper.getRouteId());
            if(route == null) {
                throw new BadRequestException("unknown route - " + wrapper.getRouteId());
            }
            trip.setRoute(route);
        }

        if(wrapper.getShapeId() != null) {
            ShapeFilter shapeFilter = new ShapeFilter();
            shapeFilter.setExactId(wrapper.getShapeId());
            long countByFilter = shapeService.getCountByFilter(shapeFilter);
            if(countByFilter <= 0) {
                throw new BadRequestException("unknown shape - " + wrapper.getShapeId());
            }
            trip.setShapeId(wrapper.getShapeId());
        }

        if(wrapper.getStopTimes() != null) {
            int i = 0;
            for(StopTimeWrapper stopTimeWrapper : wrapper.getStopTimes()) {
                if(i == 0) {
                    if(StringUtils.isEmpty(stopTimeWrapper.getDeparture())) {
                        throw new BadRequestException("alespon prvni zastaveni musi mit vyplneny cas odjezdu");
                    }
                    if(wrapper.getStopTimes().size() == 1 && StringUtils.isEmpty(stopTimeWrapper.getArrival())) {
                        throw new BadRequestException("alespon druhe zastaveni musi mit vyplneny cas prijezdu");
                    }
                }
                if(i == 1) {
                    if(StringUtils.isEmpty(stopTimeWrapper.getArrival()) && StringUtils.isEmpty(wrapper.getStopTimes().get(0).getArrival())) {
                        throw new BadRequestException("alespon druhe zastaveni musi mit vyplneny cas prijezdu");
                    }
                }
               trip.addStopTime(getStopTime(stopTimeWrapper));
               i++;
            }
        }

        return trip;
    }

    /**
     * @param wrapper stop time wrapper
     * @return stop time entita
     * @throws BadRequestException pokud neexistuje stanice s danym id
     */
    private StopTime getStopTime(StopTimeWrapper wrapper) throws BadRequestException {
        if(wrapper == null) return null;

        StopTime stopTime = new StopTime();
        stopTime.setId(wrapper.getId());
        stopTime.setSequence(wrapper.getSequence());

        if(!StringUtils.isBlank(wrapper.getArrival())) {
            stopTime.setArrival(LocalTime.parse(wrapper.getArrival(), DateTimeUtils.TIME_FORMATTER));
        }
        if(!StringUtils.isBlank(wrapper.getDeparture())) {
            stopTime.setDeparture(LocalTime.parse(wrapper.getDeparture(), DateTimeUtils.TIME_FORMATTER));
        }

        if(wrapper.getStopId() != null) {
            Stop stop = stopService.get(wrapper.getStopId());
            if(stop == null) {
                throw new BadRequestException("unknown stop - " + wrapper.getStopId());
            }
            stopTime.setStop(stop);
        }

        //trip tu schvalne nesetuji
        return stopTime;
    }

    /**
     * @param id id
     * @param calendarId id intervalu platnosti
     * @param shapeId id prujezdniho bodu
     * @param headSign navestidlo
     * @param routeShortName kratke oznaceni spoje
     * @param wheelChairCode typ bezbarierovosti
     * @return filtr s vyplnenymi parametry
     */
    private static TripFilter getFilterFromParams(String id, String calendarId, String shapeId, String headSign, String routeShortName, Integer wheelChairCode) {
        TripFilter filter = new TripFilter();
        filter.setId(id);
        filter.setCalendarId(calendarId);
        filter.setShapeId(shapeId);
        filter.setHeadSign(headSign);
        filter.setWheelChairCode(wheelChairCode);
        filter.setRouteShortName(routeShortName);

        return filter;
    }
}
