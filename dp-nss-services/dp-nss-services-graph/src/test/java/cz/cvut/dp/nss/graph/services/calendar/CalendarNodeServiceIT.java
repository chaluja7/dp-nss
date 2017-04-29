package cz.cvut.dp.nss.graph.services.calendar;

import cz.cvut.dp.nss.graph.services.AbstractServiceIT;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author jakubchalupa
 * @since 20.01.17
 */
public class CalendarNodeServiceIT extends AbstractServiceIT {

    @Autowired
    private CalendarNodeService calendarNodeService;

    @Before
    public void before() {
        super.before();
        calendarNodeService.deleteAll();
    }

    @Test
    public void testCreateAndGet() {
        final String calendarId = "calendarX";
        CalendarNode calendarNode = getCalendarNode(calendarId);

        calendarNode = calendarNodeService.save(calendarNode);
        Assert.assertNotNull(calendarNode.getId());

        calendarNode = calendarNodeService.findByCalendarId(calendarId);
        Assert.assertNotNull(calendarNode);
    }

    public static CalendarNode getCalendarNode(String calendarId) {
        CalendarNode calendarNode = new CalendarNode();
        calendarNode.setCalendarId(calendarId);
        calendarNode.setMonday(true);
        calendarNode.setTuesday(true);
        calendarNode.setWednesday(false);
        calendarNode.setThursday(false);
        calendarNode.setFriday(true);
        calendarNode.setSaturday(true);
        calendarNode.setSunday(true);

        calendarNode.setFromDateInSeconds(100L);
        calendarNode.setToDateInSeconds(200L);

        return calendarNode;
    }

}
