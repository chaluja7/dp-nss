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
    public void testSearch() {
        final String from = "Dejvická";
        final String to = "Karlovo náměstí";
        final String through = "Můstek";
        final DateTimeFormatter formatter = DateTimeFormat.forPattern(DateTimeUtils.DATE_TIME_PATTERN);
        final DateTime departureDateTime = formatter.parseDateTime("02.02.2017 9:00");
        final DateTime maxDepartureDateTime = new DateTime(departureDateTime).plusHours(SearchService.DEFAULT_MAX_HOUR_AFTER_DEPARTURE);
        final int maxTransfers = 2;

        List<SearchResult> pathByDepartureDate =
            searchService.findPathByDepartureDate(from, to, through, departureDateTime, maxDepartureDateTime, maxTransfers,
                SearchService.DEFAULT_MAX_NUMBER_OF_RESULTS, false, null);
        Assert.assertNotNull(pathByDepartureDate);
    }


}
