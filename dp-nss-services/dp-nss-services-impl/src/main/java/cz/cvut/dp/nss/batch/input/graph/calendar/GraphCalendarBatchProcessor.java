package cz.cvut.dp.nss.batch.input.graph.calendar;

import cz.cvut.dp.nss.graph.services.calendar.CalendarNode;
import cz.cvut.dp.nss.services.calendar.Calendar;
import cz.cvut.dp.nss.services.calendar.CalendarServiceImpl;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * Processor importu intervalu platnosti do grafu.
 *
 * @author jakubchalupa
 * @since 07.01.17
 */
@Component(value = "graphCalendarBatchProcessor")
public class GraphCalendarBatchProcessor implements ItemProcessor<Calendar, CalendarNode> {

    @Override
    public CalendarNode process(Calendar calendar) throws Exception {
        return CalendarServiceImpl.getCalendarNodeFromCalendar(calendar);
    }

}
