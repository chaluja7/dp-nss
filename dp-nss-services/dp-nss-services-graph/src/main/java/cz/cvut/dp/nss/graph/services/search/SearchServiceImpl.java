package cz.cvut.dp.nss.graph.services.search;

import cz.cvut.dp.nss.graph.services.search.wrappers.SearchResultWrapper;
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
    public List<SearchResultWrapper> findPathByDepartureDate(String stopFromName, String stopToName, DateTime departure, int maxHoursAfterDeparture, int maxTransfers) {
        final Map<String, Object> params = new HashMap<>();
        params.put("from", stopFromName);
        params.put("to", stopToName);
        params.put("departure", departure.getMillis());
        params.put("maxDeparture", new DateTime(departure).plusHours(maxHoursAfterDeparture).getMillis());
        params.put("maxTransfers", maxTransfers);

        Result result = session.query("CALL cz.cvut.dp.nss.search.byDepartureSearch({from}, {to}, {departure}, {maxDeparture}, {maxTransfers})", params, true);

        //todo docasne pro debug
        List<Map<String, Object>> list = new ArrayList<>();
        List<SearchResultWrapper> retList = new ArrayList<>();
        for(Map<String, Object> searchResultMap : result) {
            list.add(searchResultMap);
            retList.add(buildSearchResultWrapperFromMap(searchResultMap));
        }

        return retList;
    }

    @Override
    public void initCalendarDates() {
        session.query("CALL cz.cvut.dp.nss.search.initCalendarDates()", new HashMap<>(), true);
    }

    private static SearchResultWrapper buildSearchResultWrapperFromMap(Map<String, Object> map) {
        SearchResultWrapper wrapper = new SearchResultWrapper();
        wrapper.setDeparture((long) map.get("departure"));
        wrapper.setArrival((long) map.get("arrival"));
        wrapper.setNumberOfTransfers(((Long) map.get("numberOfTransfers")).intValue());
        wrapper.setOverMidnightArrival((boolean) map.get("overMidnightArrival"));
        wrapper.setStopTimes(Arrays.asList((Long[]) map.get("stops")));

        return wrapper;
    }

}
