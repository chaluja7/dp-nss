package cz.cvut.dp.nss.graph.services.calendarDate;

import cz.cvut.dp.nss.graph.services.AbstractServiceIT;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author jakubchalupa
 * @since 20.01.17
 */
public class CalendarDateNodeServiceIT extends AbstractServiceIT {

    @Autowired
    private CalendarDateNodeService calendarDateNodeService;

    @Before
    public void before() {
        calendarDateNodeService.deleteAll();
    }

    @Test
    public void testCreateAndGet() {
        final Long calendarDateId = 999L;
        CalendarDateNode calendarDateNode = getCalendarDateNode(calendarDateId);

        calendarDateNode = calendarDateNodeService.save(calendarDateNode);
        Assert.assertNotNull(calendarDateNode.getId());

        calendarDateNode = calendarDateNodeService.findByCalendarDateId(calendarDateId);
        Assert.assertNotNull(calendarDateNode);
    }

    public static CalendarDateNode getCalendarDateNode(Long calendarDateId) {
        CalendarDateNode calendarDateNode = new CalendarDateNode();
        calendarDateNode.setCalendarDateId(calendarDateId);
        calendarDateNode.setDateInSeconds(150L);
        calendarDateNode.setInclude(true);

        return calendarDateNode;
    }

}
