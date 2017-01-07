package cz.cvut.dp.nss.services.calendarDate;

import cz.cvut.dp.nss.services.AbstractServiceIT;
import cz.cvut.dp.nss.services.calendar.Calendar;
import cz.cvut.dp.nss.services.calendar.CalendarService;
import cz.cvut.dp.nss.services.calendar.CalendarServiceIT;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

/**
 * @author jakubchalupa
 * @since 05.01.17
 */
public class CalendarDateServiceIT extends AbstractServiceIT {

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private CalendarDateService calendarDateService;

    @Test
    public void testCRUD() {
        Calendar calendar = CalendarServiceIT.getCalendar("calendar" + System.currentTimeMillis(), LocalDate.now(), LocalDate.now(), true, true);
        calendarService.create(calendar);

        LocalDate date = LocalDate.now();
        CalendarExceptionType exceptionType = CalendarExceptionType.INCLUDE;

        CalendarDate calendarDate = getCalendarDate(calendar, date, exceptionType);

        //insert
        calendarDateService.create(calendarDate);

        //retrieve
        CalendarDate retrieved = calendarDateService.get(calendarDate.getId());
        Assert.assertNotNull(retrieved);
        Assert.assertEquals(date, retrieved.getDate());
        Assert.assertEquals(exceptionType, retrieved.getExceptionType());

        //update
        date = LocalDate.now().plusDays(5L);
        exceptionType = CalendarExceptionType.EXCLUDE;

        retrieved.setDate(date);
        retrieved.setExceptionType(exceptionType);
        calendarDateService.update(retrieved);

        //check
        retrieved = calendarDateService.get(retrieved.getId());
        Assert.assertNotNull(retrieved);
        Assert.assertEquals(date, retrieved.getDate());
        Assert.assertEquals(exceptionType, retrieved.getExceptionType());

        //delete
        calendarDateService.delete(retrieved.getId());

        //check null get
        Assert.assertNull(calendarDateService.get(retrieved.getId()));

        //delete calendar
        calendarService.delete(calendar.getId());
    }

    public static CalendarDate getCalendarDate(Calendar calendar, LocalDate date, CalendarExceptionType exceptionType) {
        CalendarDate calendarDate = new CalendarDate();
        calendarDate.setCalendar(calendar);
        calendarDate.setDate(date);
        calendarDate.setExceptionType(exceptionType);

        return calendarDate;
    }

}
