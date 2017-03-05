package cz.cvut.dp.nss.graph.services.search;

import cz.cvut.dp.nss.graph.services.search.wrappers.SearchResult;
import org.joda.time.DateTime;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author jakubchalupa
 * @since 11.02.17
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    protected Session session;

    @Override
    public List<SearchResult> findPathByDepartureDate(String stopFromName, String stopToName, DateTime departure, int maxHoursAfterDeparture, int maxTransfers) {
        Map<String, Object> params = new HashMap<>();
        params.put("from", stopFromName);
        params.put("to", stopToName);
        params.put("departure", departure.getMillis());
        params.put("maxDeparture", new DateTime(departure).plusHours(maxHoursAfterDeparture).getMillis());
        params.put("maxTransfers", maxTransfers);

        //TODO na neo4j pridat kontrolu na existenci stanic from a to. Pokud to jednu nenajde tak hned zariznout vyhledavani (vubec nevyhledavat).
        Result result = session.query("CALL cz.cvut.dp.nss.search.byDepartureSearch({from}, {to}, {departure}, {maxDeparture}, {maxTransfers})", params, true);

        List<SearchResult> retList = new ArrayList<>();
        for(Map<String, Object> searchResultMap : result) {
            retList.add(buildSearchResultWrapperFromMap(searchResultMap));
        }

        return retList;
    }

    @Override
    public void initCalendarDates() {
        session.query("CALL cz.cvut.dp.nss.search.initCalendarDates()", new HashMap<>(), true);
    }

    private static SearchResult buildSearchResultWrapperFromMap(Map<String, Object> map) {
        SearchResult wrapper = new SearchResult();
        wrapper.setDeparture((long) map.get("departure"));
        wrapper.setArrival((long) map.get("arrival"));
        wrapper.setTravelTime((long) map.get("travelTime"));
        wrapper.setNumberOfTransfers(((Long) map.get("numberOfTransfers")).intValue());
        wrapper.setOverMidnightDeparture((boolean) map.get("overMidnightDeparture"));
        wrapper.setOverMidnightArrival((boolean) map.get("overMidnightArrival"));
        wrapper.setStopTimes(Arrays.asList((Long[]) map.get("stops")));

        return wrapper;
    }

}
