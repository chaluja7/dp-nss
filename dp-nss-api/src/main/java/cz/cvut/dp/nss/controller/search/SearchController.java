package cz.cvut.dp.nss.controller.search;

import cz.cvut.dp.nss.controller.AbstractController;
import cz.cvut.dp.nss.controller.admin.route.AdminRouteController;
import cz.cvut.dp.nss.controller.admin.stop.AdminStopController;
import cz.cvut.dp.nss.graph.services.search.SearchService;
import cz.cvut.dp.nss.graph.services.search.wrappers.SearchResult;
import cz.cvut.dp.nss.services.common.DateTimeUtils;
import cz.cvut.dp.nss.services.stopTime.StopTime;
import cz.cvut.dp.nss.services.stopTime.StopTimeService;
import cz.cvut.dp.nss.services.trip.Trip;
import cz.cvut.dp.nss.wrapper.output.search.SearchResultWrapper;
import cz.cvut.dp.nss.wrapper.output.search.SearchStopTimeWrapper;
import cz.cvut.dp.nss.wrapper.output.trip.TripWithRouteWrapper;
import org.apache.commons.lang3.StringUtils;
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

    /**
     * @param stopFromName stanice z
     * @param stopToName stanice do
     * @param stopThroughName prujezdni/prestupni stanice
     * @param departure cas vyjezdu
     * @param maxTransfers max pocet prestupu
     * @param withWheelChair pokud true tak hledam pouze bezbarierove spoje
     * @return nalezene vysledky vyhledavani (serazene a vyfiltrovane)
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<SearchResultWrapper> findPaths(@RequestParam("stopFromName") String stopFromName,
                                               @RequestParam("stopToName") String stopToName,
                                               @RequestParam(name = "stopThroughName", required = false) String stopThroughName,
                                               @RequestParam("departure") String departure,
                                               @RequestParam("maxTransfers") int maxTransfers,
                                               @RequestParam(name = "withWheelChair", required = false) Boolean withWheelChair) {

        DateTime dateTime = DateTimeUtils.JODA_DATE_TIME_FORMATTER.parseDateTime(departure);

        if(StringUtils.isBlank(stopThroughName)) stopThroughName = null;
        List<SearchResult> searchResults = searchService.findPathByDepartureDate(stopFromName, stopToName, stopThroughName,
            dateTime, new DateTime(dateTime).plusHours(SearchService.DEFAULT_MAX_HOUR_AFTER_DEPARTURE), maxTransfers,
            SearchService.DEFAULT_MAX_NUMBER_OF_RESULTS, Boolean.TRUE.equals(withWheelChair), null);

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

        wrapper.setId(stopTime.getId());
        wrapper.setArrival(arrival);
        wrapper.setDeparture(departure);
        wrapper.setStop(AdminStopController.getStopWrapper(stopTime.getStop()));
        wrapper.setTrip(getTripWithRouteWrapper(stopTime.getTrip()));

        return wrapper;
    }

    private TripWithRouteWrapper getTripWithRouteWrapper(Trip trip) {
        if(trip == null) return null;

        TripWithRouteWrapper wrapper = new TripWithRouteWrapper();
        wrapper.setId(trip.getId());
        wrapper.setHeadSign(trip.getHeadSign());
        wrapper.setWheelChairCode(trip.getTripWheelchairAccessibleType() != null ? trip.getTripWheelchairAccessibleType().getCode() : null);
        wrapper.setRoute(AdminRouteController.getRouteWrapper(trip.getRoute(), false));

        return wrapper;
    }

}
