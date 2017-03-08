package cz.cvut.dp.nss.controller.search;

import cz.cvut.dp.nss.controller.AbstractController;
import cz.cvut.dp.nss.controller.admin.stop.AdminStopController;
import cz.cvut.dp.nss.graph.services.search.SearchService;
import cz.cvut.dp.nss.graph.services.search.wrappers.SearchResult;
import cz.cvut.dp.nss.services.common.DateTimeUtils;
import cz.cvut.dp.nss.services.route.Route;
import cz.cvut.dp.nss.services.stopTime.StopTime;
import cz.cvut.dp.nss.services.stopTime.StopTimeService;
import cz.cvut.dp.nss.services.trip.Trip;
import cz.cvut.dp.nss.wrapper.out.route.RouteWrapper;
import cz.cvut.dp.nss.wrapper.out.search.SearchResultWrapper;
import cz.cvut.dp.nss.wrapper.out.search.SearchStopTimeWrapper;
import cz.cvut.dp.nss.wrapper.out.trip.TripWithRouteWrapper;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jakubchalupa
 * @since 22.10.16
 */
@RestController
@RequestMapping(value = "/search")
public class SearchController extends AbstractController {

    @Autowired
    private SearchService searchService;

    @Autowired
    private StopTimeService stopTimeService;

    @RequestMapping(method = RequestMethod.GET)
    public List<SearchResultWrapper> findPaths(@RequestParam("stopFromName") String stopFromName,
                                               @RequestParam("stopToName") String stopToName,
                                               @RequestParam("departure") String departure,
                                               @RequestParam("maxTransfers") int maxTransfers) {

        DateTime dateTime = DateTimeUtils.JODA_DATE_TIME_FORMATTER.parseDateTime(departure);
        searchService.initCalendarDates();

        List<SearchResult> searchResults = searchService.findPathByDepartureDate(stopFromName, stopToName,
            dateTime, SearchService.DEFAULT_MAX_HOUR_AFTER_DEPARTURE, maxTransfers);

        List<SearchResultWrapper> searchResultWrappers = new ArrayList<>();
        for(SearchResult searchResult : searchResults) {
            searchResultWrappers.add(getSearchResultWrapper(searchResult, dateTime));
        }

        return searchResultWrappers;
    }

    private SearchResultWrapper getSearchResultWrapper(SearchResult searchResult, DateTime departureDateTime) {
        if(searchResult == null) return new SearchResultWrapper();

        final DateTime thisDeparture = searchResult.isOverMidnightDeparture() ? departureDateTime.plusDays(1) : departureDateTime;
        final DateTime thisArrival = searchResult.isOverMidnightArrival() ? departureDateTime.plusDays(1) : departureDateTime;

        SearchResultWrapper wrapper = new SearchResultWrapper();
        wrapper.setDepartureDate(thisDeparture.toString(DateTimeUtils.JODA_DATE_FORMATTER));
        wrapper.setArrivalDate(thisArrival.toString(DateTimeUtils.JODA_DATE_FORMATTER));
        wrapper.setTravelTime(searchResult.getTravelTime());
        List<SearchStopTimeWrapper> searchStopTimeWrappers = new ArrayList<>();
        for(Long stopTimeId : searchResult.getStopTimes()) {
            StopTime stopTime = stopTimeService.getWithStopAndTripAndRoute(stopTimeId);

            SearchStopTimeWrapper searchStopTimeWrapper = getSearchStopTimeWrapper(stopTime);
            searchStopTimeWrappers.add(searchStopTimeWrapper);
        }
        wrapper.setStopTimes(searchStopTimeWrappers);

        return wrapper;
    }

    private SearchStopTimeWrapper getSearchStopTimeWrapper(StopTime stopTime) {
        if(stopTime == null) return null;

        SearchStopTimeWrapper wrapper = new SearchStopTimeWrapper();

        final String arrival = stopTime.getArrival() != null ? stopTime.getArrival().toString() : null;
        final String departure = stopTime.getDeparture() != null ? stopTime.getDeparture().toString() : null;

        wrapper.setArrival(arrival);
        wrapper.setDeparture(departure);
        wrapper.setStop(AdminStopController.getStopWrapper(stopTime.getStop()));
        wrapper.setTrip(getTripWithRouteWrapper(stopTime.getTrip()));

        return wrapper;
    }

    private TripWithRouteWrapper getTripWithRouteWrapper(Trip trip) {
        if(trip == null) return null;

        TripWithRouteWrapper wrapper = new TripWithRouteWrapper();
        wrapper.setHeadSign(trip.getHeadSign());
        wrapper.setWheelChair(trip.getTripWheelchairAccessibleType() != null ? trip.getTripWheelchairAccessibleType().name() : null);
        wrapper.setRoute(getRouteWrapper(trip.getRoute()));

        return wrapper;
    }

    private RouteWrapper getRouteWrapper(Route route) {
        if(route == null) return null;

        RouteWrapper wrapper = new RouteWrapper();
        wrapper.setShortName(route.getShortName());
        wrapper.setLongName(route.getLongName());
        wrapper.setRouteType(route.getRouteType() != null ? route.getRouteType().name() : null);
        wrapper.setColor(route.getColor());

        return wrapper;
    }

}
