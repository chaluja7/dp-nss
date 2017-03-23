package cz.cvut.dp.nss.graph.services.search;

import cz.cvut.dp.nss.graph.services.AbstractServiceIT;
import cz.cvut.dp.nss.graph.services.calendar.CalendarNodeService;
import cz.cvut.dp.nss.graph.services.search.wrappers.SearchResult;
import cz.cvut.dp.nss.services.common.DateTimeUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Before;
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

    @Autowired
    private CalendarNodeService calendarNodeService;

    @Before
    public void init() {
        //inicializace calendar dates
        calendarNodeService.initCalendarDates();
    }

    @Test
    public void testSearch() {
        final String from = "Dejvická";
        final String to = "Karlovo náměstí";
        final DateTimeFormatter formatter = DateTimeFormat.forPattern(DateTimeUtils.DATE_TIME_PATTERN);
        final DateTime departureDateTime = formatter.parseDateTime("02.02.2017 9:00");
        final int maxHoursAfterDeparture = 3;
        final int maxTransfers = 2;

        List<SearchResult> pathByDepartureDate = searchService.findPathByDepartureDate(from, to, departureDateTime, maxHoursAfterDeparture, maxTransfers);
        Assert.assertNotNull(pathByDepartureDate);
    }


}
