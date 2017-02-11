package cz.cvut.dp.nss.services.calendar;

import cz.cvut.dp.nss.services.AbstractServiceIT;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

/**
 * @author jakubchalupa
 * @since 05.01.17
 */
public class CalendarServiceIT extends AbstractServiceIT {

    @Autowired
    private CalendarService calendarService;

    @Test
    public void testCRUD() {
        final String id = "calendar" + System.currentTimeMillis();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(1L);
        boolean monday = true;
        boolean sunday = true;

        Calendar calendar = getCalendar(id, startDate, endDate, monday, sunday);

        //insert
        calendarService.create(calendar);

        //retrieve
        Calendar retrieved = calendarService.get(calendar.getId());
        Assert.assertNotNull(retrieved);
        Assert.assertEquals(id, retrieved.getId());
        Assert.assertEquals(startDate, retrieved.getStartDate());
        Assert.assertEquals(endDate, retrieved.getEndDate());
        Assert.assertEquals(monday, retrieved.isMonday());
        Assert.assertEquals(sunday, retrieved.isSunday());

        //update
        endDate = LocalDate.now().plusDays(5L);
        sunday = false;

        retrieved.setEndDate(endDate);
        retrieved.setSunday(sunday);
        calendarService.update(retrieved);

        //check
        retrieved = calendarService.get(retrieved.getId());
        Assert.assertNotNull(retrieved);
        Assert.assertEquals(endDate, retrieved.getEndDate());
        Assert.assertEquals(sunday, retrieved.isSunday());

        //delete
        calendarService.delete(retrieved.getId());

        //check null get
        Assert.assertNull(calendarService.get(retrieved.getId()));
    }

    @Test
    public void testGetAllForInsertToGraph() {
        List<Calendar> allForInsertToGraph = calendarService.getAllForInsertToGraph();
        Assert.assertNotNull(allForInsertToGraph);
    }

    public static Calendar getCalendar(final String id, LocalDate startDate, LocalDate endDate, boolean monday, boolean sunday) {
        Calendar calendar = new Calendar();
        calendar.setId(id);
        calendar.setStartDate(startDate);
        calendar.setEndDate(endDate);

        calendar.setMonday(monday);
        calendar.setSunday(sunday);

        return calendar;
    }

}
