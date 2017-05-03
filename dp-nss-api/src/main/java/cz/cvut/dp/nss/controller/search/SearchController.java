package cz.cvut.dp.nss.controller.search;

import cz.cvut.dp.nss.context.SchemaThreadLocal;
import cz.cvut.dp.nss.controller.AbstractController;
import cz.cvut.dp.nss.controller.admin.route.AdminRouteController;
import cz.cvut.dp.nss.controller.admin.stop.AdminStopController;
import cz.cvut.dp.nss.exception.BadRequestException;
import cz.cvut.dp.nss.graph.services.search.SearchService;
import cz.cvut.dp.nss.graph.services.search.wrappers.SearchResult;
import cz.cvut.dp.nss.services.common.DateTimeUtils;
import cz.cvut.dp.nss.services.stop.StopService;
import cz.cvut.dp.nss.services.stopTime.StopTime;
import cz.cvut.dp.nss.services.stopTime.StopTimeService;
import cz.cvut.dp.nss.services.timeTable.TimeTable;
import cz.cvut.dp.nss.services.timeTable.TimeTableService;
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
import java.util.Set;

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

    @Autowired
    private TimeTableService timeTableService;

    @Autowired
    private StopService stopService;

    /**
     * @param stopFromName stanice z
     * @param stopToName stanice do
     * @param stopThroughName prujezdni/prestupni stanice
     * @param date cas vyjezdu nebo prijezdu
     * @param maxTransfers max pocet prestupu
     * @param withWheelChair pokud true tak hledam pouze bezbarierove spoje
     * @param searchByArrival pokud true tak hledame podle dat prijezdu do cilove stanice
     * @return nalezene vysledky vyhledavani (serazene a vyfiltrovane)
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @RequestMapping(method = RequestMethod.GET)
    public List<SearchResultWrapper> findPaths(@RequestParam("stopFromName") String stopFromName,
                                               @RequestParam("stopToName") String stopToName,
                                               @RequestParam(name = "stopThroughName", required = false) String stopThroughName,
                                               @RequestParam("date") String date,
                                               @RequestParam("maxTransfers") int maxTransfers,
                                               @RequestParam(name = "withWheelChair", required = false) Boolean withWheelChair,
                                               @RequestParam(name = "byArrival", required = false) Boolean searchByArrival) throws BadRequestException {

        DateTime dateTime = DateTimeUtils.JODA_DATE_TIME_FORMATTER.parseDateTime(date);
        if(StringUtils.isBlank(stopThroughName)) stopThroughName = null;

        final TimeTable timeTable = timeTableService.get(SchemaThreadLocal.get());
        if(timeTable == null) throw new BadRequestException("Neni zvolen jizdni rad pro vyhledavani.");
        final Integer maxTravelTime = timeTable.getMaxTravelTime();

        //pokusim se najit stanici odkud, pres a kam. Pokud ji nenajdu tak koncim vyhledavani
        Set<String> stops = stopService.findStopNamesByPattern(stopFromName, false);
        if(stops.size() != 1) {
            stops = stopService.findStopNamesByPattern(stopFromName, true);
            if(stops.size() != 1) throw new BadRequestException("Výchozí stanice nebyla nalezena nebo je nejednoznačná");
        }
        stopFromName = stops.stream().findFirst().get();

        stops = stopService.findStopNamesByPattern(stopToName, false);
        if(stops.size() != 1) {
            stops = stopService.findStopNamesByPattern(stopToName, true);
            if(stops.size() != 1) throw new BadRequestException("Cílová stanice nebyla nalezena nebo je nejednoznačná");
        }
        stopToName = stops.stream().findFirst().get();

        if(stopThroughName != null) {
            stops = stopService.findStopNamesByPattern(stopThroughName, false);
            if(stops.size() != 1) {
                stops = stopService.findStopNamesByPattern(stopThroughName, true);
                if(stops.size() != 1) throw new BadRequestException("Přestupní stanice nebyla nalezena nebo je nejednoznačná");
            }
            stopThroughName = stops.stream().findFirst().get();
        }

        checkStops(stopFromName, stopToName, stopThroughName);

        List<SearchResult> searchResults;
        if(Boolean.TRUE.equals(searchByArrival)) {
            searchResults = searchService.findPathByArrivalDate(stopFromName, stopToName, stopThroughName,
                dateTime, new DateTime(dateTime).minusHours(maxTravelTime), maxTransfers,
                SearchService.DEFAULT_MAX_NUMBER_OF_RESULTS, Boolean.TRUE.equals(withWheelChair), null);
        } else {
            searchResults = searchService.findPathByDepartureDate(stopFromName, stopToName, stopThroughName,
                dateTime, new DateTime(dateTime).plusHours(maxTravelTime), maxTransfers,
                SearchService.DEFAULT_MAX_NUMBER_OF_RESULTS, Boolean.TRUE.equals(withWheelChair), null);
        }

        List<SearchResultWrapper> searchResultWrappers = new ArrayList<>();
        for(SearchResult searchResult : searchResults) {
            searchResultWrappers.add(getSearchResultWrapper(searchResult, dateTime, Boolean.TRUE.equals(searchByArrival)));
        }

        return searchResultWrappers;
    }

    private void checkStops(String stopFrom, String stopTo, String stopThrough) throws BadRequestException {
        if(stopFrom.equals(stopTo)) throw new BadRequestException("Výchozí a cílová stanice nemohou být shodné");
        if(stopThrough != null) {
            if(stopFrom.equals(stopThrough)) throw new BadRequestException("Výchozí a přestupní stanice nemohou být shodné");
            if(stopTo.equals(stopThrough)) throw new BadRequestException("Cílová a přestupní stanice nemohou být shodné");
        }
    }

    private SearchResultWrapper getSearchResultWrapper(SearchResult searchResult, DateTime departureDateTime, boolean byArrival) {
        if(searchResult == null) return new SearchResultWrapper();

        final DateTime thisDeparture;
        final DateTime thisArrival;
        if(!byArrival) {
            thisDeparture = searchResult.isOverMidnightDeparture() ? departureDateTime.plusDays(1) : departureDateTime;
            thisArrival = searchResult.isOverMidnightArrival() ? departureDateTime.plusDays(1) : departureDateTime;
        } else {
            thisDeparture = searchResult.isOverMidnightDeparture() ? departureDateTime.minusDays(1) : departureDateTime;
            thisArrival = searchResult.isOverMidnightArrival() ? departureDateTime.minusDays(1) : departureDateTime;
        }

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
