package cz.cvut.dp.nss.batch.graph.calendar;

import cz.cvut.dp.nss.graph.services.calendar.CalendarNode;
import cz.cvut.dp.nss.graph.services.calendarDate.CalendarDateNode;
import cz.cvut.dp.nss.services.calendar.Calendar;
import cz.cvut.dp.nss.services.calendarDate.CalendarDate;
import cz.cvut.dp.nss.services.calendarDate.CalendarExceptionType;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jakubchalupa
 * @since 07.01.17
 */
@Component(value = "graphCalendarBatchProcessor")
public class GraphCalendarBatchProcessor implements ItemProcessor<Calendar, CalendarNode> {

    @Override
    public CalendarNode process(Calendar calendar) throws Exception {
        CalendarNode calendarNode = new CalendarNode();
        calendarNode.setCalendarId(calendar.getId());
        calendarNode.setFromDateInSeconds(getSecondsSinceEpoch(calendar.getStartDate()));
        calendarNode.setToDateInSeconds(getSecondsSinceEpoch(calendar.getEndDate()));

        calendarNode.setMonday(calendar.isMonday());
        calendarNode.setTuesday(calendar.isTuesday());
        calendarNode.setWednesday(calendar.isWednesday());
        calendarNode.setThursday(calendar.isThursday());
        calendarNode.setFriday(calendar.isFriday());
        calendarNode.setSaturday(calendar.isSaturday());
        calendarNode.setSunday(calendar.isSunday());

        List<CalendarDateNode> calendarDateNodes = new ArrayList<>();
        for(CalendarDate calendarDate : calendar.getCalendarDates()) {
           CalendarDateNode calendarDateNode = new CalendarDateNode();
            calendarDateNode.setCalendarDateId(calendarDate.getId());
            calendarDateNode.setDateInSeconds(getSecondsSinceEpoch(calendarDate.getDate()));
            calendarDateNode.setInclude(CalendarExceptionType.INCLUDE.equals(calendarDate.getExceptionType()));
            calendarDateNode.setCalendarNode(calendarNode);

            calendarDateNodes.add(calendarDateNode);
        }

        calendarNode.setCalendarDateNodes(calendarDateNodes);
        return calendarNode;
    }

    /**
     * @param localDate localDate
     * @return pocet vterin since epoch
     */
    private static long getSecondsSinceEpoch(LocalDate localDate) {
        return Date.valueOf(localDate).getTime() / 1000;
    }

}
