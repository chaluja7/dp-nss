package cz.cvut.dp.nss.graph.services.search;

import cz.cvut.dp.nss.graph.services.AbstractServiceIT;
import cz.cvut.dp.nss.graph.services.search.wrappers.SearchResult;
import cz.cvut.dp.nss.services.common.DateTimeUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author jakubchalupa
 * @since 11.02.17
 */
public class SearchServiceIT extends AbstractServiceIT {

    @Autowired
    private SearchService searchService;

    @Test
    public void testByDepartureSearch() {
        final String from = "Dejvická";
        final String to = "Karlovo náměstí";
        final String through = "Můstek";
        final DateTimeFormatter formatter = DateTimeFormat.forPattern(DateTimeUtils.DATE_TIME_PATTERN);
        final DateTime departureDateTime = formatter.parseDateTime("02.02.2017 9:00");
        final DateTime maxDepartureDateTime = new DateTime(departureDateTime).plusHours(3);
        final int maxTransfers = 2;

        List<SearchResult> pathByDepartureDate =
            searchService.findPathByDepartureDate(from, to, through, departureDateTime, maxDepartureDateTime, maxTransfers,
                SearchService.DEFAULT_MAX_NUMBER_OF_RESULTS, false, null);
        Assert.assertNotNull(pathByDepartureDate);
    }

    @Test
    public void testByArrivalSearch() {
        final String from = "Dejvická";
        final String to = "Karlovo náměstí";
        final String through = null;
        final DateTimeFormatter formatter = DateTimeFormat.forPattern(DateTimeUtils.DATE_TIME_PATTERN);
        final DateTime arrivalDateTime = formatter.parseDateTime("03.05.2017 15:00");
        final DateTime minArrivalDateTime = new DateTime(arrivalDateTime).minusHours(6);
        final int maxTransfers = 3;

        List<SearchResult> pathByArrivalDate =
            searchService.findPathByArrivalDate(from, to, through, arrivalDateTime, minArrivalDateTime, maxTransfers,
                SearchService.DEFAULT_MAX_NUMBER_OF_RESULTS, false, null);
        Assert.assertNotNull(pathByArrivalDate);
    }

}
