package cz.cvut.dp.nss.batch.graph.calendar;

import cz.cvut.dp.nss.graph.services.calendar.CalendarNode;
import cz.cvut.dp.nss.graph.services.calendar.CalendarNodeService;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author jakubchalupa
 * @since 07.01.17
 */
@Component(value = "graphCalendarBatchWriter")
public class GraphCalendarBatchWriter implements ItemWriter<CalendarNode> {

    @Autowired
    protected CalendarNodeService calendarNodeService;

    @Override
    public void write(List<? extends CalendarNode> items) throws Exception {
        for(CalendarNode calendarNode : items) {
            //ulozi se jak calendar tak navazene (a uz propojene) calendarDates
            calendarNodeService.save(calendarNode, -1);
        }
    }

}
